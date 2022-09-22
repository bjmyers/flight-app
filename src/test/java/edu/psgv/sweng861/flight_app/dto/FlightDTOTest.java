package edu.psgv.sweng861.flight_app.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link FlightDTO}
 */
public class FlightDTOTest {

	/**
	 * Test the setters and getters
	 */
	@Test
	public void testSettersAndGetters() {

		final String cityTo = "Minas Tirith";
		final String cityFrom = "The Shire";
		final int price = 102;
		
		final FlightDTO flight = new FlightDTO();
		
		flight.setCityTo(cityTo);
		flight.setCityFrom(cityFrom);
		flight.setPrice(price);
		
		assertEquals(cityTo, flight.getCityTo());
		assertEquals(cityFrom, flight.getCityFrom());
		assertEquals(price, flight.getPrice());
	}
}
