package edu.psgv.sweng861.flight_app.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link FlightDate}
 */
public class FlightDateTest {
	
	/**
	 * Tests {@link FlightDate#format()} with a double digit day and month
	 */
	@Test
	public void testFormatDoubleDigits() {
		final FlightDate date = new FlightDate(2022, 10, 11);
		final String expectedString = "11/10/2022";
		final String actualString = date.format();
		
		assertEquals(expectedString, actualString);
	}
	
	/**
	 * Tests {@link FlightDate#format()} with a single digit day
	 */
	@Test
	public void testFormatSingleDay() {
		final FlightDate date = new FlightDate(2022, 10, 3);
		final String expectedString = "03/10/2022";
		final String actualString = date.format();
		
		assertEquals(expectedString, actualString);
	}
	
	/**
	 * Tests {@link FlightDate#format()} with a single digit day
	 */
	@Test
	public void testFormatSingleMonth() {
		final FlightDate date = new FlightDate(2022, 2, 17);
		final String expectedString = "17/02/2022";
		final String actualString = date.format();
		
		assertEquals(expectedString, actualString);
	}

}
