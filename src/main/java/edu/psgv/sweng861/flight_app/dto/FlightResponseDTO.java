package edu.psgv.sweng861.flight_app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO class stores a response from the FlightAPI
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightResponseDTO {
	
	private List<FlightDTO> data;
	
	/**
	 * Default constructor needed by Jackson
	 */
	public FlightResponseDTO() {
	}
	
	/**
	 * @param data the flights returned from the API
	 */
	public void setData(final List<FlightDTO> data) {
		this.data = data;
	}
	
	/**
	 * @return the flights returned from the API
	 */
	public List<FlightDTO> getData() {
		return this.data;
	}

}
