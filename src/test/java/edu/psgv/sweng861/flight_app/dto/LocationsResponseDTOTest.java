package edu.psgv.sweng861.flight_app.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;

public class LocationsResponseDTOTest {
	
	@Test
	public void testSettersAndGetters() {
		final LocationDTO loc = mock(LocationDTO.class);
		
		final LocationsResponseDTO resp = new LocationsResponseDTO();
		
		resp.setLocations(List.of(loc));
		
		final List<LocationDTO> locs = resp.getLocations();
		
		assertEquals(1, locs.size());
		assertSame(loc, locs.get(0));
	}

}
