package edu.psgv.sweng861.flight_app;

import javax.swing.JLabel;

import edu.psgv.sweng861.flight_app.dto.FlightDTO;

/**
 * Stores a {@link JLabel} which is used to display information about found
 * flights
 */
public class FlightDisplay {

	private JLabel display;

	/**
	 * Constructor
	 * 
	 * @param display The {@link JLabel} which will display flight information
	 */
	public FlightDisplay(final JLabel display) {
		this.display = display;
	}

	/**
	 * @param flight The flight which should have its information displayed to the
	 *               user
	 */
	public void displayFlight(final FlightDTO flight) {
		final String textToDisplay = String.format(
				"<html>Cheapest direct flight from %s to %s: $%d<br>Leaves at %s and arrives at %s</html>",
				flight.getCityFrom(), flight.getCityTo(), flight.getPrice(), flight.getLocal_departure(),
				flight.getLocal_arrival());
		this.display.setText(textToDisplay);
	}

	/**
	 * Removes text from the display label
	 */
	public void clearFlight() {
		this.display.setText("");
	}

}
