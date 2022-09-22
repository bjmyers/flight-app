package edu.psgv.sweng861.flight_app.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.psgv.sweng861.flight_app.dto.APICallResponse;
import edu.psgv.sweng861.flight_app.dto.FlightDate;
import edu.psgv.sweng861.flight_app.dto.FlightResponseDTO;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

public class APIClient {
	
	private String apiKey = "b2ihITqOeJqvn2PgXl9dWYtInUTsl1VN";
	private String searchURL = "https://api.tequila.kiwi.com/v2/search";
	private String locationURL = "https://api.tequila.kiwi.com/locations/topdestinations";

	private final ObjectMapper mapper = new ObjectMapper();
	
	private final Client client;
	
	public APIClient(final Client client) {
		this.client = client;
	}
	
	public APIClient() {
		this(ClientBuilder.newClient());
	}

	public APICallResponse callAPI(final String cityFrom, final String cityTo, final FlightDate dateFrom,
			final FlightDate dateTo) {

		final Invocation inv = client.target(searchURL)
				.queryParam("fly_from", cityFrom).queryParam("fly_to", cityTo)
				.queryParam("date_from", dateFrom.format()).queryParam("date_to", dateTo.format())
				.queryParam("sort", "price").queryParam("limit", 1)
				.queryParam("flight_type", "oneway").queryParam("one_for_city", 0)
				.queryParam("one_per_date", 0).queryParam("adults", 1).queryParam("children", 0)
				.queryParam("only_working_days", false).queryParam("only_weekends", false)
				.queryParam("partner_market", "us").queryParam("max_stopovers", 0)
				.queryParam("max_sector_stopovers", 0).queryParam("vehicle_type", "aircraft")
				.request().header("apikey", apiKey).buildGet();

		final Response resp = inv.invoke();
		
		final String responseJson = resp.readEntity(String.class);
		
		System.out.println(resp.getStatus());
		if (resp.getStatus() != 200) {
			return new APICallResponse(false, responseJson, null);
		}
		
		FlightResponseDTO response = null;
		try {
			response = mapper.readValue(responseJson, FlightResponseDTO.class);
		} catch (JsonProcessingException e) {
			return new APICallResponse(false, "Unable to parse flight response: " + e.getCause(), null);
		}
		
		if (response.getData().isEmpty()) {
			return new APICallResponse(false, "No flight found for the given inputs", null);
		}
		return new APICallResponse(true, null, response.getData().get(0));
	}
	
	/**
	 * @return A Response containing the 100 most popular destinations from Philadelphia
	 */
	public LocationsResponseDTO getLocations() {
		final Invocation inv = client.target(locationURL)
				.queryParam("term", "PHL").queryParam("locale", "en-US")
				.queryParam("limit", 100).queryParam("sort", "rank")
				.queryParam("active_only", true).queryParam("source_popularity", "searches")
				.request().header("apikey", apiKey).buildGet();
		
		final Response resp = inv.invoke();
		
		final String responseJson = resp.readEntity(String.class);
		
		LocationsResponseDTO response = null;
		try {
			response = mapper.readValue(responseJson, LocationsResponseDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return response;
	}

}
