package edu.psgv.sweng861.flight_app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Stores a list of locations which can be flown to
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationsResponseDTO {
	
	private List<LocationDTO> locations;
	
	/**
	 * Default constructor needed by Jackson
	 */
	public LocationsResponseDTO() {
	}
	
	/**
	 * @param locations the locations which can be flown to
	 */
	public void setLocations(final List<LocationDTO> locations) {
		this.locations = locations;
	}
	
	/**
	 * @return the locations which can be flown to
	 */
	public List<LocationDTO> getLocations() {
		return this.locations;
	}

}
