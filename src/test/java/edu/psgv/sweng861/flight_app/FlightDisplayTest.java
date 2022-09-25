package edu.psgv.sweng861.flight_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.swing.JLabel;

import org.junit.Test;

import edu.psgv.sweng861.flight_app.dto.FlightDTO;

/**
 * Tests for {@link FlightDisplay}
 */
public class FlightDisplayTest {

	/**
	 * Tests the constructor and accessors
	 */
	@Test
	public void testDisplayFlight() {
		final JLabel label = new JLabel();
		
		final FlightDTO flight = new FlightDTO();
		flight.setCityFrom("Philadelphia");
		flight.setCityTo("Los Angeles");
		flight.setPrice(100);
		flight.setLocal_arrival("2022-09-25T10:10:10.000Z");
		flight.setLocal_departure("2022-09-25T12:10:10.000Z");
		
		final FlightDisplay display = new FlightDisplay(label);
		
		display.displayFlight(flight);
		
		// some text has been added to our label
		assertTrue(label.getText().length() > 0);
		
		display.clearFlight();
		
		// that text has been removed
		assertEquals(0, label.getText().length());
		
	}
}
