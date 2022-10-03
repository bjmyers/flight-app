package edu.psgv.sweng861.flight_app.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Tests for {@link FlightDate}
 */
public class FlightDateTest {
	
	/**
	 * Tests building a FlightDate from a LocalDateTime object
	 */
	@Test
	public void testBuildFromLocalDateTime() {
		final LocalDate localDate = LocalDate.of(2022, 2, 17);
		
		final String expectedString = "17/02/2022";
		
		final FlightDate date = new FlightDate(localDate);
		
		assertEquals(expectedString, date.format());
		assertEquals(2022, date.getYear());
		assertEquals(2, date.getMonth());
		assertEquals(17, date.getDay());
	}
	
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
	
	/**
	 * Tests {@link FlightDate#displayFormat()}
	 */
	@Test
	public void testDisplayFormat() {
		final FlightDate date = new FlightDate(2022, 2, 17);
		final String expectedString = "2022/02/17";
		final String actualString = date.displayFormat();
		
		assertEquals(expectedString, actualString);
	}
	
	/**
	 * Tests the isAfter method with dates that are before the original date
	 */
	@Test
	public void testIsAfterBefore() {
		final FlightDate date1 = new FlightDate(2022, 2, 17);
		
		final FlightDate date2 = new FlightDate(2021, 2, 17);
		final FlightDate date3 = new FlightDate(2022, 1, 17);
		final FlightDate date4 = new FlightDate(2022, 2, 16);
		
		assertTrue(date1.isAfter(date2));
		assertTrue(date1.isAfter(date3));
		assertTrue(date1.isAfter(date4));
	}
	
	/**
	 * Tests the isAfter method with a date that is after the original date
	 */
	@Test
	public void testIsAfter() {
		final FlightDate date1 = new FlightDate(2022, 2, 17);
		
		final FlightDate date2 = new FlightDate(2023, 2, 17);
		final FlightDate date3 = new FlightDate(2022, 3, 17);
		final FlightDate date4 = new FlightDate(2022, 2, 18);
		
		assertFalse(date1.isAfter(date2));
		assertFalse(date1.isAfter(date3));
		assertFalse(date1.isAfter(date4));
	}
	
	/**
	 * Tests the isAfter method with an identical date
	 */
	@Test
	public void testIsAfterEqual() {
		final FlightDate date1 = new FlightDate(2022, 2, 17);
		
		final FlightDate date2 = new FlightDate(2022, 2, 17);
		
		assertFalse(date1.isAfter(date2));
	}

}
