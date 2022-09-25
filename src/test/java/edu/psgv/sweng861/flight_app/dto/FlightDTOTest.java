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
		final String localDeparture = "Tomorrow at Noon";
		final String localArrival = "Next week";
		
		final FlightDTO flight = new FlightDTO();
		
		flight.setCityTo(cityTo);
		flight.setCityFrom(cityFrom);
		flight.setPrice(price);
		flight.setLocal_departure(localDeparture);
		flight.setLocal_arrival(localArrival);
		
		assertEquals(cityTo, flight.getCityTo());
		assertEquals(cityFrom, flight.getCityFrom());
		assertEquals(price, flight.getPrice());
		assertEquals(localDeparture, flight.getLocal_departure());
		assertEquals(localArrival, flight.getLocal_arrival());
	}
}
