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
import java.time.LocalDateTime;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

import edu.psgv.sweng861.flight_app.dto.FlightDate;

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
	
}
