package edu.psgv.sweng861.flight_app;

import javax.swing.JLabel;

import edu.psgv.sweng861.flight_app.dto.FlightDTO;

public class FlightDisplay {
	
	private JLabel display;
	
	public FlightDisplay(final JLabel display) {
		this.display = display;
	}

	public void displayFlight(final FlightDTO flight) {
		final String textToDisplay = String.format(
				"<html>Cheapest direct flight from %s to %s: $%d<br>Leaves at %s and arrives at %s</html>",
				flight.getCityFrom(), flight.getCityTo(), flight.getPrice(), flight.getLocal_departure(),
				flight.getLocal_arrival());
		this.display.setText(textToDisplay);
	}
	
	public void clearFlight() {
		this.display.setText("");
	}

}
