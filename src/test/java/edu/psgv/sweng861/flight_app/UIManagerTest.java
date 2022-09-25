package edu.psgv.sweng861.flight_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.psgv.sweng861.flight_app.dto.FlightDate;
import edu.psgv.sweng861.flight_app.dto.LocationDTO;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

/**
 * Tests for {@link UIManager}
 */
public class UIManagerTest {
	
	/**
	 * Tests {@link UIManager#addUserEntryFields}
	 */
	@Test
	public void testAddUserInputElements() {
		final JFrame frame = mock(JFrame.class);

		final ErrorReporter reporter = mock(ErrorReporter.class);
		when(reporter.getErrorLabel()).thenReturn(new JLabel());
		UIManager.REPORTER = reporter;
		
		UIManager.locationNameToCode = Map.of();
		
		UIManager.addUserEntryFields(frame);
		
		// Verify all 7 components were successfully built and added to the frame
		verify(frame, times(7)).add(any(Component.class));
		
	}
	
	/**
	 * Tests parsing a good date
	 */
	@Test
	public void testParseFlightDate() {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		UIManager.REPORTER = reporter;
		
		final String input = "1999/02/17";
		
		final FlightDate date = UIManager.parseFlightDate(input);
		
		assertEquals("17/02/1999", date.format());
		verifyNoInteractions(reporter);
	}
	
	/**
	 * Tests parsing a date when there is no field for day
	 */
	@Test
	public void testParseFlightDateTooFewComponents() {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		UIManager.REPORTER = reporter;
		
		final String input = "1999/02";
		
		final FlightDate date = UIManager.parseFlightDate(input);
		
		assertNull(date);
		verify(reporter, times(1)).addError(any(String.class));
	}
	
	/**
	 * Tests parsing a date when one of the fields is a non-integer
	 */
	@Test
	public void testParseFlightDateNonInteger() {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		UIManager.REPORTER = reporter;
		
		final String input = "1999/02/AA";
		
		final FlightDate date = UIManager.parseFlightDate(input);
		
		assertNull(date);
		verify(reporter, times(1)).addError(any(String.class));
	}
	
	/**
	 * Tests getting the current date
	 */
	@Test
	public void getCurrentDateTest() {
		final LocalDateTime currentTime = LocalDateTime.now();

		final String expectedString = String.format("%02d/%02d/%04d", currentTime.getDayOfMonth(),
				currentTime.getMonthValue(), currentTime.getYear());
		
		final String actualString = UIManager.getCurrentDate().format();
		
		assertEquals(expectedString, actualString);
	}
	
	/**
	 * Tests getting tomorrow's date
	 */
	@Test
	public void getTomorrowsDateTest() {
		final LocalDateTime currentTime = LocalDateTime.now().plusDays(1);

		final String expectedString = String.format("%02d/%02d/%04d", currentTime.getDayOfMonth(),
				currentTime.getMonthValue(), currentTime.getYear());
		
		final String actualString = UIManager.getTomorrowsDate().format();
		
		assertEquals(expectedString, actualString);
	}
	
	/**
	 * Tests getting aircraft from a file
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void getAirportsFromFileTest() throws IOException {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		UIManager.REPORTER = reporter;
		
		// Build temp file
		final File file = File.createTempFile("airports", "json", new File("src/main/resources"));
		file.deleteOnExit();
		final String filePath = file.getAbsolutePath();
		
		// Add data to the file
		final LocationDTO loc1 = new LocationDTO();
		loc1.setName("King of Prussia");
		loc1.setCode("KOP");
		final LocationDTO loc2 = new LocationDTO();
		loc2.setName("Penn State University");
		loc2.setCode("PSU");
		final LocationsResponseDTO response = new LocationsResponseDTO();
		response.setLocations(List.of(loc1, loc2));
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(file, response);
		
		final LocationsResponseDTO actualResponse = UIManager.getAirportsFromFile(filePath);
		assertEquals(2, actualResponse.getLocations().size());
		verifyNoInteractions(reporter);
	}
	
	/**
	 * Tests getting the airports from a file when there is no content in the file
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void getAirportsFromFileNoContentTest() throws IOException {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		UIManager.REPORTER = reporter;
		
		// Build temp file
		final File file = File.createTempFile("airports", "json", new File("src/main/resources"));
		file.deleteOnExit();
		final String filePath = file.getAbsolutePath();
		
		// Add data to the file
		final LocationsResponseDTO response = new LocationsResponseDTO();
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(file, response);
		
		final LocationsResponseDTO actualResponse = UIManager.getAirportsFromFile(filePath);
		assertNull(actualResponse);
		verify(reporter).addWarning(any(String.class));
	}
	
	/**
	 * Tests getting the airports from a file when no file exists
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void getAirportsFromFileNoFileTest() throws IOException {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		UIManager.REPORTER = reporter;

		final LocationsResponseDTO actualResponse = UIManager.getAirportsFromFile("FakeFile.json");
		assertNull(actualResponse);
		verify(reporter).addWarning(any(String.class));
	}
	
}
