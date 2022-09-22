package edu.psgv.sweng861.flight_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Stores an airport which can be flown to
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
	
	private String name;
	private String code;
	
	public LocationDTO() {
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public void setCode(final String code) {
		this.code = code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}

}
