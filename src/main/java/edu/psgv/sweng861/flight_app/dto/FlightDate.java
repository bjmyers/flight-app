package edu.psgv.sweng861.flight_app.dto;

import java.time.LocalDateTime;

/**
 * A Date object with methods to format it in a manner that the API can
 * understand
 */
public class FlightDate {

	private int year;
	private int month;
	private int day;

	/**
	 * @param year  the year
	 * @param month the month
	 * @param day   the day
	 */
	public FlightDate(final int year, final int month, final int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	/**
	 * Alternate constructor which takes in a {@link LocalDateTime} object
	 * 
	 * @param date the date to convert to a {@link FlightDate}
	 */
	public FlightDate(final LocalDateTime date) {
		this(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}

	/**
	 * @return this date with a DD/MM/YYYY format as needed by the API
	 */
	public String format() {
		return String.format("%02d/%02d/%04d", this.day, this.month, this.year);
	}

	/**
	 * @return this date with a YYYY/MM/DD format which is the form that should be
	 *         displayed to the user
	 */
	public String displayFormat() {
		return String.format("%04d/%02d/%02d", this.year, this.month, this.day);
	}

}
