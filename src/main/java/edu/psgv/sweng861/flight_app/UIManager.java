package edu.psgv.sweng861.flight_app;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import edu.psgv.sweng861.flight_app.api.APIClient;
import edu.psgv.sweng861.flight_app.dto.FlightDTO;
import edu.psgv.sweng861.flight_app.dto.FlightDate;
import edu.psgv.sweng861.flight_app.dto.LocationDTO;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

/**
 * Gathers and organizes all components in the UI
 */
public class UIManager {

	private static final Logger LOGGER = LogManager.getLogger();

	static APIClient CLIENT;
	static Map<String, String> locationNameToCode;
	static ErrorReporter REPORTER;
	static FlightDisplay FLIGHT_DISPLAY;

	static String AIRPORT_FILE_PATH = "src/main/resources/edu/psgv/sweng861/flight_app/Airports.json";

	/**
	 * Main method, builds UI
	 * 
	 * @param args unused
	 */
	public static void main(final String[] args) {
		CLIENT = new APIClient();

		// Build Error Reporter to be passed around everywhere
		final JLabel errorLabel = new JLabel();
		REPORTER = new ErrorReporter(errorLabel);

		LocationsResponseDTO locations = getAirportsFromFile(AIRPORT_FILE_PATH);
		if (locations == null) {
			LOGGER.warn("Null airports from file, calling client for airports");
			locations = CLIENT.getLocations(REPORTER);
		}
		// Retrieve the locations from a file
		locationNameToCode = new HashMap<>();
		if (locations != null && locations.getLocations() != null) {
			// When there's an error in getting the locations, locationNameToCode will be
			// empty
			locationNameToCode.put("Philadelphia", "PHL");
			for (LocationDTO loc : locations.getLocations()) {
				locationNameToCode.put(loc.getName(), loc.getCode());
			}
		} else {
			LOGGER.warn("Found no locations in either file or through client");
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Builds the initial {@link JFrame} and adds user entry fields
	 */
	private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("LeaveOnABudget");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addUserEntryFields(frame);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}

	/**
	 * Adds fields where the user can enter request parameters
	 * 
	 * @param frame The {@link JFrame} where all the fields will be housed
	 */
	private static void addUserEntryFields(final JFrame frame) {
		
        frame.setLayout(new GridLayout(7, 1));
        
        // Build Text Field to respond to the user
        final JPanel responsePanel = new JPanel();
        responsePanel.setLayout(new FlowLayout());
		responsePanel.add(REPORTER.getErrorLabel());
		
		// Build text entry row for entering departure and arrival cities
        final JPanel cityEntryPanel = new JPanel();
        cityEntryPanel.setLayout(new FlowLayout());

        final JLabel cityFromTitle = new JLabel("Departure City");
        cityEntryPanel.add(cityFromTitle);

        if (locationNameToCode.isEmpty()) {
        	//TODO: Allow manual entry lol
        	REPORTER.addError("Unable to Load initial cities, please use manual entry");
        }
        final String[] cities = locationNameToCode.keySet().toArray(new String[locationNameToCode.size()]);
        Arrays.sort(cities);

		final JComboBox<String> cityFromEntry = new JComboBox<String>(cities);
		cityEntryPanel.add(cityFromEntry);
        AutoCompleteDecorator.decorate(cityFromEntry, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);

        final JLabel cityToTitle = new JLabel("Arrival City");
        cityEntryPanel.add(cityToTitle);

        final JComboBox<String> cityToEntry = new JComboBox<String>(cities);
        // Starts disabled because we start in any destination mode
        cityToEntry.setEnabled(false);
        cityEntryPanel.add(cityToEntry);
        AutoCompleteDecorator.decorate(cityToEntry, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        
		// Build text entry row for entering time range to search
        final JPanel timeEntryPanel = new JPanel();
        timeEntryPanel.setLayout(new FlowLayout());

        final JLabel timeFromTitle = new JLabel("Earliest Date");
        timeEntryPanel.add(timeFromTitle);
        
        final JTextField timeFromEntry = new JTextField(getCurrentDate().displayFormat(), 10);
        timeEntryPanel.add(timeFromEntry);

        final JLabel timeToTitle = new JLabel("Latest Date");
        timeEntryPanel.add(timeToTitle);
        
        final JTextField timeToEntry = new JTextField(getTomorrowsDate().displayFormat(), 10);
        timeEntryPanel.add(timeToEntry);
        
        // Build radio button group to select entry mode
        final JRadioButton anyDestinationButton = new JRadioButton("Send me anywhere");
        anyDestinationButton.setSelected(true);
        anyDestinationButton.setLayout(new FlowLayout());
        anyDestinationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cityToEntry.setEnabled(false);
			}
        });
        final JRadioButton specificDestinationButton = new JRadioButton("Take me here");
        specificDestinationButton.setLayout(new FlowLayout());
        specificDestinationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cityToEntry.setEnabled(true);
			}
        });
        final ButtonGroup destinationEntryMode = new ButtonGroup();
        destinationEntryMode.add(anyDestinationButton);
        destinationEntryMode.add(specificDestinationButton);
        
        // Build button to hit API
        final JPanel executePanel = new JPanel();
        executePanel.setLayout(new FlowLayout());

		final JButton executeButton = buildExecuteButton(timeFromEntry, timeToEntry, cityFromEntry,
				cityToEntry, anyDestinationButton);
		executePanel.add(executeButton);
		
        // Build Text Field to display flights to the user
        final JPanel flightPanel = new JPanel();
        flightPanel.setLayout(new FlowLayout());
        final JLabel display = new JLabel();
        FLIGHT_DISPLAY = new FlightDisplay(display);
        flightPanel.add(display);

        frame.add(anyDestinationButton);
        frame.add(specificDestinationButton);
        frame.add(cityEntryPanel);
        frame.add(timeEntryPanel);
        frame.add(executePanel);
        frame.add(flightPanel);
        frame.add(responsePanel);
	}

	/**
	 * Builds the button to execute the API call
	 * 
	 * @param timeFromEntry        the {@link JTextField} which holds the earliest
	 *                             date to query
	 * @param timeToEntry          the {@link JTextField} which holds the latest
	 *                             date to query
	 * @param cityFromEntry        the {@link JTextField} which holds the airport
	 *                             the user is leaving
	 * @param cityToEntry          the {@link JTextField} which holds the
	 *                             destination airport, only used in single
	 *                             destination mode
	 * @param anyDestinationButton A {@link JRadioButton} which tells if the user
	 *                             desires to use a single destination or all of
	 *                             them
	 * @return a {@link JButton} which executes the API call when pressed
	 */
	private static JButton buildExecuteButton(final JTextField timeFromEntry, final JTextField timeToEntry,
			final JComboBox<String> cityFromEntry, final JComboBox<String> cityToEntry,
			final JRadioButton anyDestinationButton) {
		final JButton executeButton = new JButton("Find Cheapest Flight");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				REPORTER.clearErrors();
				FLIGHT_DISPLAY.clearFlight();
				final FlightDate dateFrom = parseFlightDate(timeFromEntry.getText());
				final FlightDate dateTo = parseFlightDate(timeToEntry.getText());
				if (dateFrom == null || dateTo == null) {
					return;
				}
				final String cityFromCode = locationNameToCode.get(cityFromEntry.getSelectedItem().toString());
				final String cityToCode = locationNameToCode.get(cityToEntry.getSelectedItem().toString());
				final FlightDTO response;
				if (anyDestinationButton.isSelected()) {
					LOGGER.info("Calling API in any destination mode");
					response = CLIENT.callAPI(REPORTER, locationNameToCode, cityFromCode, dateFrom, dateTo);
				}
				else {
					LOGGER.info("Calling API in specific mode");
					response = CLIENT.callAPI(REPORTER, cityFromCode, cityToCode, dateFrom, dateTo);
				}
				
				if (response != null) {
					FLIGHT_DISPLAY.displayFlight(response);
				}
			}
        });
		return executeButton;
	}

	/**
	 * @param input a date as a String, must be in "YYYY/MM/DD" format
	 * @return a {@link FlightDate} representing the same date
	 */
	static FlightDate parseFlightDate(final String input) {
		final String[] components = input.split("/");
		if (components.length != 3) {
			REPORTER.addError(String.format("Unable to parse date %s. Date should be in the form YYYY/MM/DD", input));
			return null;
		}
		try {
			final int year = Integer.parseInt(components[0]);
			final int month = Integer.parseInt(components[1]);
			final int day = Integer.parseInt(components[2]);
			return new FlightDate(year, month, day);
		}
		catch (NumberFormatException e) {
			REPORTER.addError(String.format("Unable to parse date %s. Date should be in the form YYYY/MM/DD", input));
		}
		return null;
	}

	/**
	 * @return a {@link FlightDate} representing today's date
	 */
	static FlightDate getCurrentDate() {
		final LocalDateTime currentTime = LocalDateTime.now();
		return new FlightDate(currentTime.getYear(), currentTime.getMonthValue(), currentTime.getDayOfMonth());
	}
	
	/**
	 * @return a {@link FlightDate} representing tomorrow's date
	 */
	static FlightDate getTomorrowsDate() {
		final LocalDateTime currentTime = LocalDateTime.now().plusDays(1);
		return new FlightDate(currentTime.getYear(), currentTime.getMonthValue(), currentTime.getDayOfMonth());
	}

	/**
	 * @param filePath path to a file containing the JSON representation of a
	 *                 {@link LocationsResponseDTO} object
	 * @return the {@link LocationsResponseDTO} object deserialized from the file's
	 *         contents
	 */
	static LocationsResponseDTO getAirportsFromFile(final String filePath) {
		LOGGER.info("Retreiving Airports from File");

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectReader reader = mapper.readerFor(LocationsResponseDTO.class);
		try {
			final LocationsResponseDTO response = reader.readValue(new File(filePath));
			if (response.getLocations() == null) {
				REPORTER.addWarning("Found no Airports in static List, attempting to dynamically load Airports");
				return null;
			}
			LOGGER.info("Retrieved " + response.getLocations().size() + " airports");
			return response;
		} catch (IOException e) {
			REPORTER.addWarning("Unable to read in static Airport List, attempting to dynamically load Airports");
		}
		return null;
	}

}
