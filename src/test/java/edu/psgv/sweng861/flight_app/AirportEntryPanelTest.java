package edu.psgv.sweng861.flight_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Arrays;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.junit.Test;

/**
 * Tests for {@link AirportEntryPanel}
 */
public class AirportEntryPanelTest {
	
	/**
	 * Tests the constructor
	 */
	@Test
	public void constructorTest() {
		
		final JPanel panel = mock(JPanel.class);
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");
		
		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		
		assertNotNull(entryPanel);
		verifyNoInteractions(reporter);
		// All 4 components were added to the panel
		verify(panel, times(4)).add(any(JComponent.class));
	}
	
	/**
	 * Tests the constructor when passed in an empty map
	 */
	@Test
	public void constructorEmptyMapTest() {

		final JPanel panel = mock(JPanel.class);
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of();
		
		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		
		assertNotNull(entryPanel);
		verify(reporter).addError(any(String.class));
		// All 4 components were still added to the panel
		verify(panel, times(4)).add(any(JComponent.class));
	}
	
	/**
	 * Tests {@link AirportEntryPanel#switchToManualEntry}
	 */
	@Test
	public void testSwitchToManualEntry() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");
		
		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		
		entryPanel.switchToManualEntry();
		
		assertTrue(Arrays.asList(panel.getComponents()).contains(entryPanel.cityFromField));
		assertTrue(Arrays.asList(panel.getComponents()).contains(entryPanel.cityToField));
		assertFalse(Arrays.asList(panel.getComponents()).contains(entryPanel.cityFromBox));
		assertFalse(Arrays.asList(panel.getComponents()).contains(entryPanel.cityToBox));
	}
	
	/**
	 * Tests {@link AirportEntryPanel#switchToComboEntry}
	 */
	@Test
	public void testSwitchToComboEntry() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");
		
		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		
		entryPanel.switchToComboEntry();
		
		assertTrue(Arrays.asList(panel.getComponents()).contains(entryPanel.cityFromBox));
		assertTrue(Arrays.asList(panel.getComponents()).contains(entryPanel.cityToBox));
		assertFalse(Arrays.asList(panel.getComponents()).contains(entryPanel.cityFromField));
		assertFalse(Arrays.asList(panel.getComponents()).contains(entryPanel.cityToField));
	}
	
	/**
	 * Tests {@link AirportEntryPanel#disableCityTo}
	 */
	@Test
	public void testDisableCityTo() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");
		
		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		
		entryPanel.disableCityTo();
		
		assertFalse(entryPanel.cityToBox.isEnabled());
		assertFalse(entryPanel.cityToField.isEnabled());
	}
	
	/**
	 * Tests {@link AirportEntryPanel#enableCityTo}
	 */
	@Test
	public void testEnableCityTo() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");
		
		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		
		entryPanel.enableCityTo();
		
		assertTrue(entryPanel.cityToBox.isEnabled());
		assertTrue(entryPanel.cityToField.isEnabled());
	}
	
	/**
	 * Tests getting the city from code in manual entry mode
	 */
	@Test
	public void testGetCityFromCodeManual() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");

		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		final String expectedOutput = "ISS";
		entryPanel.cityFromField.setText(expectedOutput);
		
		entryPanel.switchToManualEntry();
		
		final String actualOutput = entryPanel.getCityFromCode();
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	/**
	 * Tests getting the city from code in manual entry mode
	 */
	@Test
	public void testGetCityFromCodeCombo() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");

		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		final String expectedOutput = "LAX";
		entryPanel.cityFromBox.setSelectedIndex(0);
		
		entryPanel.switchToComboEntry();
		
		final String actualOutput = entryPanel.getCityFromCode();
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	/**
	 * Tests getting the city to code in manual entry mode
	 */
	@Test
	public void testGetCityToCodeManual() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");

		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		final String expectedOutput = "ISS";
		entryPanel.cityToField.setText(expectedOutput);
		
		entryPanel.switchToManualEntry();
		
		final String actualOutput = entryPanel.getCityToCode();
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	/**
	 * Tests getting the city to code in manual entry mode
	 */
	@Test
	public void testGetCityToCodeCombo() {
		
		final JPanel panel = new JPanel();
		final ErrorReporter reporter = mock(ErrorReporter.class);
		final Map<String, String> airportNameToCode = Map.of("Los Angeles (LAX)", "LAX");

		final AirportEntryPanel entryPanel = new AirportEntryPanel(reporter, panel, airportNameToCode);
		final String expectedOutput = "LAX";
		entryPanel.cityToBox.setSelectedIndex(0);
		
		entryPanel.switchToComboEntry();
		
		final String actualOutput = entryPanel.getCityToCode();
		
		assertEquals(expectedOutput, actualOutput);
	}

}
