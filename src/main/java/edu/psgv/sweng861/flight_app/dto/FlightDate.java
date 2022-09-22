package edu.psgv.sweng861.flight_app.dto;

/**
 * A Date object with methods to format it in a manner that 
 */
public class FlightDate {
	
	private int year;
	private int month;
	private int day;
	
	/**
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 */
	public FlightDate(final int year, final int month, final int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	/**
	 * @return this date with a DD/MM/YYYY format
	 */
	public String format() {
		return String.format("%02d/%02d/%04d", this.day, this.month, this.year);
	}

}
