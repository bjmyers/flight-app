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
	
	public FlightDTO() {
	}
	
	public void setCityFrom(final String cityFrom) {
		this.cityFrom = cityFrom;
	}
	
	public void setCityTo(final String cityTo) {
		this.cityTo = cityTo;
	}
	
	public void setPrice(final int price) {
		this.price = price;
	}
	
	public String getCityFrom() {
		return this.cityFrom;
	}
	
	public String getCityTo() {
		return this.cityTo;
	}
	
	public int getPrice() {
		return this.price;
	}
	
}
