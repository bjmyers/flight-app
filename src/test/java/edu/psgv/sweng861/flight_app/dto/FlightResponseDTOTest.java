package edu.psgv.sweng861.flight_app.dto;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FlightResponseDTOTest {
	
	private String responseJson = "{\"search_id\": \"75611724-e40c-d4b4-9307-c83bfde6dec4\","
			+ " \"currency\": \"EUR\", \"fx_rate\": 1, \"data\":"
			+ " [{\"id\": \"231f243e4b3600009699a92a_0\", \"flyFrom\": \"PHL\", \"flyTo\": \"LAX\","
			+ " \"cityFrom\": \"Philadelphia\", \"cityCodeFrom\": \"PHL\", \"cityTo\": \"Los Angeles\","
			+ " \"cityCodeTo\": \"LAX\", \"countryFrom\": {\"code\": \"US\", \"name\": \"United States\"},"
			+ " \"countryTo\": {\"code\": \"US\", \"name\": \"United States\"}, \"nightsInDest\": null,"
			+ " \"quality\": 414.666515, \"distance\": 3860.56, \"duration\": {\"departure\": 21900,"
			+ " \"return\": 0, \"total\": 21900}, \"price\": 354, \"conversion\": {\"EUR\": 354},"
			+ " \"fare\": {\"adults\": 354, \"children\": 354, \"infants\": 354},"
			+ " \"price_dropdown\": {\"base_fare\": 354, \"fees\": 0}, \"bags_price\": {\"1\": 71.88, \"2\": 166.23},"
			+ " \"baglimit\": {\"hand_height\": 46, \"hand_length\": 56, \"hand_weight\": 10, \"hand_width\": 25,"
			+ " \"hold_dimensions_sum\": 158, \"hold_height\": 52, \"hold_length\": 78, \"hold_weight\": 18,"
			+ " \"hold_width\": 28, \"personal_item_height\": 35, \"personal_item_length\": 45,"
			+ " \"personal_item_weight\": 10, \"personal_item_width\": 20}, \"availability\": {\"seats\": 1},"
			+ " \"airlines\": [\"NK\"], \"route\": [{\"id\": \"231f243e4b3600009699a92a_0\","
			+ " \"combination_id\": \"231f243e4b3600009699a92a\", \"flyFrom\": \"PHL\", \"flyTo\": \"LAX\","
			+ " \"cityFrom\": \"Philadelphia\", \"cityCodeFrom\": \"PHL\", \"cityTo\": \"Los Angeles\","
			+ " \"cityCodeTo\": \"LAX\", \"airline\": \"NK\", \"flight_no\": 267, \"operating_carrier\": \"NK\","
			+ " \"operating_flight_no\": \"267\", \"fare_basis\": \"MNR\", \"fare_category\": \"M\","
			+ " \"fare_classes\": \"M\", \"fare_family\": \"\", \"return\": 0, \"bags_recheck_required\": false,"
			+ " \"vi_connection\": false, \"guarantee\": false, \"equipment\": null, \"vehicle_type\": \"aircraft\","
			+ " \"local_arrival\": \"2022-09-19T18:25:00.000Z\", \"utc_arrival\": \"2022-09-20T01:25:00.000Z\","
			+ " \"local_departure\": \"2022-09-19T15:20:00.000Z\", \"utc_departure\": \"2022-09-19T19:20:00.000Z\"}],"
			+ " \"booking_token\": \"EA6-DUj1QoiJ1wLeQuxeXS49AGDpb-gjCJGEp8gaz1V9NcdUBnbPNxQlRBItWsBpavB1lsUJxT9Rh0"
			+ "KQplnMuR1gZABuVTALY64Xt1g67kkV8rEe4PTSrwWiXwL_hyqoRITXkwFNJ4PVpXCnNcmmKAVvDhz2s84q2XO7m1uMMPTG2J4Hg2I"
			+ "R3hxHZEfBKGmV8pcJvrzIP72b_Oi8PEZHUWaqHXmCqxH5uyhUX4ntzB10rfOvNOESp3g3kqaKU7wo_iCr_xKaEJpjaNf2oVQBwye-"
			+ "B7qyVWCxrRFM7wpz4RHm8lB2UADMieN_m6xI8QbXIOPzYJXbDodq0KOsUSRkVur8RcJs6gqrSb08TgqgEuoA=\","
			+ " \"facilitated_booking_available\": true, \"pnr_count\": 1, \"has_airport_change\": false,"
			+ " \"technical_stops\": 0, \"throw_away_ticketing\": false, \"hidden_city_ticketing\": false,"
			+ " \"virtual_interlining\": false, \"local_arrival\": \"2022-09-19T18:25:00.000Z\","
			+ " \"utc_arrival\": \"2022-09-20T01:25:00.000Z\", \"local_departure\": \"2022-09-19T15:20:00.000Z\","
			+ " \"utc_departure\": \"2022-09-19T19:20:00.000Z\"}], \"_results\": 14,"
			+ " \"search_params\": {\"flyFrom_type\": \"airport\", \"to_type\": \"airport\","
			+ " \"seats\": {\"passengers\": 1, \"adults\": 1, \"children\": 0, \"infants\": 0}},"
			+ " \"all_stopover_airports\": [], \"sort_version\": 0}\r\n";
	
	/**
	 * Tests that the response string can be deserialized
	 * @throws JsonProcessingException if something goes wrong
	 * @throws JsonMappingException if something goes wrong
	 */
	@Test
	public void testDeserialization() throws JsonMappingException, JsonProcessingException {
		
		final ObjectMapper mapper = new ObjectMapper();
		
		final FlightResponseDTO response = mapper.readValue(responseJson, FlightResponseDTO.class);
		
		final List<FlightDTO> flights = response.getData();
		
		assertEquals(1, flights.size());
		
		final FlightDTO flight = flights.get(0);
		
		assertEquals("Philadelphia", flight.getCityFrom());
		assertEquals("Los Angeles", flight.getCityTo());
		assertEquals(354, flight.getPrice());
	}
	
	/**
	 * Tests the setters and getters
	 */
	@Test
	public void testSettersAndGetters() {
		
		final List<FlightDTO> flights = Arrays.asList(new FlightDTO());
		
		final FlightResponseDTO response = new FlightResponseDTO();
		
		response.setData(flights);
		
		assertEquals(flights, response.getData());
	}

}
