package edu.psgv.sweng861.flight_app;

import java.time.LocalDateTime;

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
				"<html>Cheapest direct flight from %s (%s) to %s (%s): $%d<br>Leaves at %s and arrives at %s local time</html>",
				flight.getCityFrom(), flight.getCityCodeFrom(), flight.getCityTo(), flight.getCityCodeTo(),
				flight.getPrice(), formatDate(flight.getLocal_departure()), formatDate(flight.getLocal_arrival()));
		this.display.setText(textToDisplay);
	}

	/**
	 * Removes text from the display label
	 */
	public void clearFlight() {
		this.display.setText("");
	}

	/**
	 * @param isoDate a date formatted into an ISO string (YYYY-MM-DDTHH:MM:SS.SSSZ)
	 * @return a string representing the date converted to display format
	 */
	String formatDate(final String isoDate) {
		// LocalDateTime parsing doesn't support the Z character at the end of the
		// string, need to remove it
		final String trimmedIsoDate = isoDate.substring(0, isoDate.length() - 1);
		final LocalDateTime date = LocalDateTime.parse(trimmedIsoDate);
		return String.format("%02d:%02d %04d/%02d/%02d", date.getHour(), date.getMinute(), date.getYear(),
				date.getMonthValue(), date.getDayOfMonth());
	}

}
