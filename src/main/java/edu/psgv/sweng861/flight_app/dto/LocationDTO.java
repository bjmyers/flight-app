package edu.psgv.sweng861.flight_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Stores an airport which can be flown to
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {
	
	private String name;
	private String code;
	
	/**
	 * Default constructor needed by Jackson
	 */
	public LocationDTO() {
	}
	
	/**
	 * @param name the name of the airport
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * @param code the code of the airport
	 */
	public void setCode(final String code) {
		this.code = code;
	}
	
	/**
	 * @return the name of the airport
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the code of the airport
	 */
	public String getCode() {
		return this.code;
	}

}
