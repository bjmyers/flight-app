package edu.psgv.sweng861.flight_app.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import edu.psgv.sweng861.flight_app.ErrorReporter;
import edu.psgv.sweng861.flight_app.dto.FlightDTO;
import edu.psgv.sweng861.flight_app.dto.FlightDate;
import edu.psgv.sweng861.flight_app.dto.LocationDTO;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

/**
 * Tests for {@link APIClient}
 */
@RunWith(MockitoJUnitRunner.class)
public class APIClientTest {
	
	/**
	 * Tests the default constructor
	 */
	@Test
	public void testEmptyConstructor() {
		final APIClient client = new APIClient();
		
		assertNotNull(client);
	}
	
	/**
	 * Tests {@link APIClient#callAPI}
	 */
	@Test
	public void testCallAPI() {
		
		final Client client = mock(Client.class);
		final WebTarget target = mock(WebTarget.class);
		when(client.target(any(String.class))).thenReturn(target);

		when(target.queryParam(any(String.class), any())).thenReturn(target);
		
		final Builder builder = mock(Builder.class);
		when(target.request()).thenReturn(builder);
		
		when(builder.header(any(String.class), any())).thenReturn(builder);
		
		final Invocation inv = mock(Invocation.class);
		when(builder.buildGet()).thenReturn(inv);
		
		final Response resp = mock(Response.class);
		when(resp.getStatus()).thenReturn(200);
		when(inv.invoke()).thenReturn(resp);

		final String responseJson = "{\"data\": [ {\"cityTo\": \"LA\", \"cityFrom\": \"PHL\", \"price\": 100} ] }";
		when(resp.readEntity(String.class)).thenReturn(responseJson);

		final FlightDate dateTo = new FlightDate(2022, 10, 11);
		final FlightDate dateFrom = new FlightDate(2022, 10, 12);
		
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		final APIClient apiClient = new APIClient(client);
		final FlightDTO response = apiClient.callAPI(reporter, "PHL", "LA", dateTo, dateFrom, 1, 0);
		
		assertEquals(100, response.getPrice());
		verifyNoInteractions(reporter);
	}
	
	/**
	 * Tests {@link APIClient#callAPI} when the response has a bad status
	 */
	@Test
	public void testCallAPIBadStatus() {
		
		final Client client = mock(Client.class);
		final WebTarget target = mock(WebTarget.class);
		when(client.target(any(String.class))).thenReturn(target);

		when(target.queryParam(any(String.class), any())).thenReturn(target);
		
		final Builder builder = mock(Builder.class);
		when(target.request()).thenReturn(builder);
		
		when(builder.header(any(String.class), any())).thenReturn(builder);
		
		final Invocation inv = mock(Invocation.class);
		when(builder.buildGet()).thenReturn(inv);
		
		final Response resp = mock(Response.class);
		when(resp.getStatus()).thenReturn(403);
		when(inv.invoke()).thenReturn(resp);

		final String responseJson = "{\"data\": [ {\"cityTo\": \"LA\", \"cityFrom\": \"PHL\", \"price\": 100} ] }";
		when(resp.readEntity(String.class)).thenReturn(responseJson);

		final FlightDate dateTo = new FlightDate(2022, 10, 11);
		final FlightDate dateFrom = new FlightDate(2022, 10, 12);
		
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		final APIClient apiClient = new APIClient(client);
		final FlightDTO response = apiClient.callAPI(reporter, "PHL", "LA", dateTo, dateFrom, 1, 0);
		
		assertNull(response);
		verify(reporter, times(1)).addError(any(String.class));
	}
	
	/**
	 * Tests {@link APIClient#callAPI} when the response is poorly formed
	 */
	@Test
	public void testCallAPIBadJson() {
		
		final Client client = mock(Client.class);
		final WebTarget target = mock(WebTarget.class);
		when(client.target(any(String.class))).thenReturn(target);

		when(target.queryParam(any(String.class), any())).thenReturn(target);
		
		final Builder builder = mock(Builder.class);
		when(target.request()).thenReturn(builder);
		
		when(builder.header(any(String.class), any())).thenReturn(builder);
		
		final Invocation inv = mock(Invocation.class);
		when(builder.buildGet()).thenReturn(inv);
		
		final Response resp = mock(Response.class);
		when(resp.getStatus()).thenReturn(200);
		when(inv.invoke()).thenReturn(resp);
		
		final String badJson = "{\"data\": {{";
		when(resp.readEntity(String.class)).thenReturn(badJson);

		final FlightDate dateTo = new FlightDate(2022, 10, 11);
		final FlightDate dateFrom = new FlightDate(2022, 10, 12);
		
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		final APIClient apiClient = new APIClient(client);
		final FlightDTO response = apiClient.callAPI(reporter, "PHL", "LA", dateTo, dateFrom, 1, 0);

		assertNull(response);
		verify(reporter, times(1)).addError(any(String.class));
	}
	
	/**
	 * Tests {@link APIClient#callAPI} when the response is empty
	 */
	@Test
	public void testCallAPIEmptyResponse() {
		
		final Client client = mock(Client.class);
		final WebTarget target = mock(WebTarget.class);
		when(client.target(any(String.class))).thenReturn(target);

		when(target.queryParam(any(String.class), any())).thenReturn(target);
		
		final Builder builder = mock(Builder.class);
		when(target.request()).thenReturn(builder);
		
		when(builder.header(any(String.class), any())).thenReturn(builder);
		
		final Invocation inv = mock(Invocation.class);
		when(builder.buildGet()).thenReturn(inv);
		
		final Response resp = mock(Response.class);
		when(resp.getStatus()).thenReturn(200);
		when(inv.invoke()).thenReturn(resp);

		final String emptyJson = "{\"data\": [] }";
		when(resp.readEntity(String.class)).thenReturn(emptyJson);

		final FlightDate dateTo = new FlightDate(2022, 10, 11);
		final FlightDate dateFrom = new FlightDate(2022, 10, 12);
		
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		final APIClient apiClient = new APIClient(client);
		final FlightDTO response = apiClient.callAPI(reporter, "PHL", "LA", dateTo, dateFrom, 1, 0);

		assertNull(response);
		verify(reporter, times(1)).addError(any(String.class));
	}
	
	/**
	 * Tests {@link APIClient#getLocations}
	 */
	@Test
	public void testGetLocations() {
		
		final Client client = mock(Client.class);
		final WebTarget target = mock(WebTarget.class);
		when(client.target(any(String.class))).thenReturn(target);

		when(target.queryParam(any(String.class), any())).thenReturn(target);
		
		final Builder builder = mock(Builder.class);
		when(target.request()).thenReturn(builder);
		
		when(builder.header(any(String.class), any())).thenReturn(builder);
		
		final Invocation inv = mock(Invocation.class);
		when(builder.buildGet()).thenReturn(inv);
		
		final Response resp = mock(Response.class);
		when(resp.getStatus()).thenReturn(200);
		when(inv.invoke()).thenReturn(resp);

		final String responseJson = "{\"locations\": [ {\"name\": \"Philadelphia\", \"code\": \"PHL\"} ] }";
		when(resp.readEntity(String.class)).thenReturn(responseJson);
		
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		final APIClient apiClient = new APIClient(client);
		final LocationsResponseDTO response = apiClient.getLocations(reporter);
		
		assertEquals(1, response.getLocations().size());
		
		final LocationDTO location = response.getLocations().get(0);
		
		assertEquals("Philadelphia", location.getName());
		assertEquals("PHL", location.getCode());
		
		verifyNoInteractions(reporter);
	}

	/**
	 * Tests {@link APIClient#getLocations}
	 */
	@Test
	public void testGetLocationsBadStatus() {
		
		final Client client = mock(Client.class);
		final WebTarget target = mock(WebTarget.class);
		when(client.target(any(String.class))).thenReturn(target);

		when(target.queryParam(any(String.class), any())).thenReturn(target);
		
		final Builder builder = mock(Builder.class);
		when(target.request()).thenReturn(builder);
		
		when(builder.header(any(String.class), any())).thenReturn(builder);
		
		final Invocation inv = mock(Invocation.class);
		when(builder.buildGet()).thenReturn(inv);
		
		final Response resp = mock(Response.class);
		when(resp.getStatus()).thenReturn(100);
		when(inv.invoke()).thenReturn(resp);

		final String responseJson = "{\"locations\": [ {\"name\": \"Philadelphia\", \"code\": \"PHL\"} ] }";
		when(resp.readEntity(String.class)).thenReturn(responseJson);
		
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		final APIClient apiClient = new APIClient(client);
		final LocationsResponseDTO response = apiClient.getLocations(reporter);

		assertNull(response);
		verify(reporter, times(1)).addError(any(String.class));
	}
	
	/**
	 * Tests {@link APIClient#getLocations} when the response is poorly formed
	 */
	@Test
	public void testGetLocationsBadJson() {
		
		final Client client = mock(Client.class);
		final WebTarget target = mock(WebTarget.class);
		when(client.target(any(String.class))).thenReturn(target);

		when(target.queryParam(any(String.class), any())).thenReturn(target);
		
		final Builder builder = mock(Builder.class);
		when(target.request()).thenReturn(builder);
		
		when(builder.header(any(String.class), any())).thenReturn(builder);
		
		final Invocation inv = mock(Invocation.class);
		when(builder.buildGet()).thenReturn(inv);
		
		final Response resp = mock(Response.class);
		when(resp.getStatus()).thenReturn(200);
		when(inv.invoke()).thenReturn(resp);

		final String responseJson = "{\"locations\": [ }";
		when(resp.readEntity(String.class)).thenReturn(responseJson);
		
		final ErrorReporter reporter = mock(ErrorReporter.class);
		
		final APIClient apiClient = new APIClient(client);
		final LocationsResponseDTO response = apiClient.getLocations(reporter);
		
		assertNull(response);
		verify(reporter, times(1)).addError(any(String.class));
	}

}
