package edu.psgv.sweng861.flight_app.api;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.psgv.sweng861.flight_app.ErrorReporter;
import edu.psgv.sweng861.flight_app.dto.FlightDTO;
import edu.psgv.sweng861.flight_app.dto.FlightDate;
import edu.psgv.sweng861.flight_app.dto.FlightResponseDTO;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

/**
 * Manages calls to the Tequila Kiwi API
 */
public class APIClient {
	
	private String apiKey = "b2ihITqOeJqvn2PgXl9dWYtInUTsl1VN";
	private String searchURL = "https://api.tequila.kiwi.com/v2/search";
	private String locationURL = "https://api.tequila.kiwi.com/locations/topdestinations";

	private final ObjectMapper mapper = new ObjectMapper();
	
	private final Client client;
	
	/**
	 * @param client the Client to use in calls to the API
	 */
	public APIClient(final Client client) {
		this.client = client;
	}

	/**
	 * Default constructor, builds a new client
	 */
	public APIClient() {
		this(ClientBuilder.newClient());
	}

	/**
	 * @param reporter an {@link ErrorReporter} to report errors to the user
	 * @param cityFrom string representing the airport code of the city the flight
	 *                 will leave from
	 * @param cityTo   string representing the airport code of the city the flight
	 *                 will arrive at
	 * @param dateFrom the earliest date to search for flights
	 * @param dateTo   the latest date to search for flights
	 * @return The cheapest {@link FlightDTO} returned from the API, or null if no
	 *         flight can be found
	 */
	public FlightDTO callAPI(final ErrorReporter reporter, final String cityFrom, final String cityTo,
			final FlightDate dateFrom, final FlightDate dateTo) {

		final Invocation inv = client.target(searchURL)
				.queryParam("fly_from", cityFrom).queryParam("fly_to", cityTo)
				.queryParam("date_from", dateFrom.format()).queryParam("date_to", dateTo.format())
				.queryParam("sort", "price").queryParam("limit", 1).queryParam("curr", "USD")
				.queryParam("flight_type", "oneway").queryParam("one_for_city", 0)
				.queryParam("one_per_date", 0).queryParam("adults", 1).queryParam("children", 0)
				.queryParam("only_working_days", false).queryParam("only_weekends", false)
				.queryParam("partner_market", "us").queryParam("max_stopovers", 0)
				.queryParam("max_sector_stopovers", 0).queryParam("vehicle_type", "aircraft")
				.request().header("apikey", apiKey).buildGet();

		final Response resp = inv.invoke();
		
		final String responseJson = resp.readEntity(String.class);
		
		if (resp.getStatus() != 200) {
			reporter.addError(responseJson);
			return null;
		}
		
		FlightResponseDTO response = null;
		try {
			response = mapper.readValue(responseJson, FlightResponseDTO.class);
		} catch (JsonProcessingException e) {
			reporter.addError("Unable to parse flight response: " + e.getCause());
			return null;
		}
		
		if (response.getData().isEmpty()) {
			reporter.addError("No flight found for the given inputs");
			return null;
		}
		return response.getData().get(0);
	}

	/**
	 * @param reporter          an {@link ErrorReporter} to report errors to the
	 *                          user
	 * @param airportNameToCode a map of airport names to codes, all airports should
	 *                          be used when calling the API
	 * @param cityFrom          string representing the airport code of the city the
	 *                          flight will leave from
	 * @param dateFrom          the earliest date to search for flights
	 * @param dateTo            the latest date to search for flights
	 * @return the cheapest flight found by the API, or null if no flight can be
	 *         found
	 */
	public FlightDTO callAPI(final ErrorReporter reporter, final Map<String, String> airportNameToCode,
			final String cityFrom, final FlightDate dateFrom, final FlightDate dateTo) {

		final String cityTo = airportNameToCode.values().stream().reduce("", (s1, s2) -> s1 + "," + s2);
		
		return callAPI(reporter, cityFrom, cityTo, dateFrom, dateTo);
	}
	
	/**
	 * @return A Response containing the 250 most popular destinations from Philadelphia
	 */
	public LocationsResponseDTO getLocations(final ErrorReporter reporter) {
		final Invocation inv = client.target(locationURL)
				.queryParam("term", "PHL").queryParam("locale", "en-US")
				.queryParam("limit", 250).queryParam("sort", "rank")
				.queryParam("active_only", true).queryParam("source_popularity", "searches")
				.request().header("apikey", apiKey).buildGet();
		
		final Response resp = inv.invoke();
		
		final String responseJson = resp.readEntity(String.class);
		
		if (resp.getStatus() != 200) {
			reporter.addError(responseJson);
			return null;
		}
		
		LocationsResponseDTO response = null;
		try {
			response = mapper.readValue(responseJson, LocationsResponseDTO.class);
		} catch (JsonProcessingException e) {
			reporter.addError("Unable to parse available airports: " + e.getCause());
			return null;
		}
		
		return response;
	}

}
