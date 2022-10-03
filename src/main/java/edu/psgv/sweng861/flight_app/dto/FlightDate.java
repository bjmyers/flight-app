package edu.psgv.sweng861.flight_app.dto;

import java.time.LocalDate;

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
	 * @return the year of this date
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * @return the month of this date
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * @return the day of this date
	 */
	public int getDay() {
		return this.day;
	}
	
	/**
	 * Alternate constructor which takes in a {@link LocalDate} object
	 * 
	 * @param date the date to convert to a {@link FlightDate}
	 */
	public FlightDate(final LocalDate date) {
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
	
	/**
	 * @param otherDate the date to compare to
	 * @return true if the input date is after this date, false if the input date is
	 *         identical to or before this date
	 */
	public boolean isAfter(final FlightDate otherDate) {
		if (this.year > otherDate.getYear()) {
			return true;
		}
		if (this.month > otherDate.getMonth()) {
			return true;
		}
		if (this.day > otherDate.getDay()) {
			return true;
		}
		return false;
	}

}
