package edu.psgv.sweng861.flight_app.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link LocationDTO}
 */
public class LocationDTOTest {
	
	/**
	 * Test the setters and getters
	 */
	@Test
	public void testSettersAndGetters() {
		final String name = "International Space Station";
		final String code = "ISS";
		
		final LocationDTO loc = new LocationDTO();
		
		loc.setName(name);
		loc.setCode(code);
		
		assertEquals(name, loc.getName());
		assertEquals(code, loc.getCode());
	}

}
