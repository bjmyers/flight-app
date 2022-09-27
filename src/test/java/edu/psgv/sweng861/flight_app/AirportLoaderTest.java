package edu.psgv.sweng861.flight_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.psgv.sweng861.flight_app.api.APIClient;
import edu.psgv.sweng861.flight_app.dto.LocationDTO;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

public class AirportLoaderTest {
	
	/**
	 * Tests getting aircraft from a file
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void getAirportsTest() throws IOException {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final APIClient client = mock(APIClient.class);
		
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

		final LocationsResponseDTO actualResponse = AirportLoader.getAirports(reporter, filePath, client);
		assertEquals(2, actualResponse.getLocations().size());
		verifyNoInteractions(reporter);
		verifyNoInteractions(client);
	}
	
	/**
	 * Tests getting the airports from a file when there is no content in the file
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void getAirportsFromFileNoContentTest() throws IOException {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		// Build temp file
		final File file = File.createTempFile("airports", "json", new File("src/main/resources"));
		file.deleteOnExit();
		final String filePath = file.getAbsolutePath();
		
		// Add data to the file
		final LocationsResponseDTO response = new LocationsResponseDTO();
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(file, response);

		final LocationsResponseDTO actualResponse = AirportLoader.getAirportsFromFile(reporter, filePath);
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

		final LocationsResponseDTO actualResponse = AirportLoader.getAirportsFromFile(reporter, "FakeFile.json");
		assertNull(actualResponse);
		verify(reporter).addWarning(any(String.class));
	}
	
	@Test
	public void getAirportsWhenFileFailsTest() {
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final APIClient client = mock(APIClient.class);
		final LocationsResponseDTO response = new LocationsResponseDTO();
		when(client.getLocations(eq(reporter))).thenReturn(response);

		final LocationsResponseDTO actualResponse = AirportLoader.getAirports(reporter, "FakeFile.json", client);
		
		assertSame(response, actualResponse);
		
	}

}
