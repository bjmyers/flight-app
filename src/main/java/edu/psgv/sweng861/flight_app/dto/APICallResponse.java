package edu.psgv.sweng861.flight_app.dto;

/**
 * Returns the response of calling the API for the cheapest flight, including an error message if applicable 
 */
public class APICallResponse {
	
	private final boolean success;
	private final String errorMessage;
	private final FlightDTO flight;
	
	public APICallResponse(final boolean success, final String errorMessage, final FlightDTO flight) {
		this.success = success;
		this.errorMessage = errorMessage;
		this.flight = flight;
	}
	
	public boolean wasSuccessful() {
		return this.success;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public FlightDTO getFlight() {
		return this.flight;
	}

}
