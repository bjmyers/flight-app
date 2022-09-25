package edu.psgv.sweng861.flight_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO class to store info about a particular flight
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightDTO {
	
	private String cityFrom;
	private String cityTo;
	private int price;
	private String local_departure;
	private String local_arrival;
	
	/**
	 * Empty constructor for Jackson
	 */
	public FlightDTO() {
	}
	
	/**
	 * @param cityFrom the airport code of the city the flight is leaving
	 */
	public void setCityFrom(final String cityFrom) {
		this.cityFrom = cityFrom;
	}
	
	/**
	 * @param cityTo the airport code of the city the flight is arriving at
	 */
	public void setCityTo(final String cityTo) {
		this.cityTo = cityTo;
	}
	
	/**
	 * @param price the price of the flight
	 */
	public void setPrice(final int price) {
		this.price = price;
	}
	
	/**
	 * @param localDeparture string representation of the local time when the flight leaves
	 */
	public void setLocal_departure(final String localDeparture) {
		this.local_departure = localDeparture;
	}
	
	/**
	 * @param localArrival string representation of the local time when the flight arrives
	 */
	public void setLocal_arrival(final String localArrival) {
		this.local_arrival = localArrival;
	}
	
	/**
	 * @return the airport code of the city the flight is leaving
	 */
	public String getCityFrom() {
		return this.cityFrom;
	}
	
	/**
	 * return the airport code of the city the flight is arriving at
	 */
	public String getCityTo() {
		return this.cityTo;
	}
	
	/**
	 * @return the price of the flight
	 */
	public int getPrice() {
		return this.price;
	}
	
	/**
	 * @return string representation of the local time when the flight leaves
	 */
	public String getLocal_departure() {
		return this.local_departure;
	}
	
	/**
	 * @return string representation of the local time when the flight arrives
	 */
	public String getLocal_arrival() {
		return this.local_arrival;
	}
	
}
