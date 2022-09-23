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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	
	private static APIClient CLIENT;
	private static Map<String, String> locationNameToCode;
	private static ErrorReporter REPORTER;

	private static String AIRPORT_FILE_PATH = "src/main/resources/edu/psgv/sweng861/flight_app/Airports.json";

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
			// When there's an error in getting the locations, locationNameToCode will be empty
			locationNameToCode.put("Philadelphia", "PHL");
			for (LocationDTO loc: locations.getLocations()) {
				locationNameToCode.put(loc.getName(), loc.getCode());
			}
		}
		else {
			LOGGER.warn("Found no locations in either file or through client");
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("LeaveOnABudget");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addUserEntryFields(frame);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
	private static void addUserEntryFields(final JFrame frame) {
		
        frame.setLayout(new GridLayout(4, 1));
        
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
        cityEntryPanel.add(cityToEntry);
        AutoCompleteDecorator.decorate(cityToEntry, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        
		// Build text entry row for entering time range to search
        final JPanel timeEntryPanel = new JPanel();
        timeEntryPanel.setLayout(new FlowLayout());

        final JLabel timeFromTitle = new JLabel("Earliest Time");
        timeEntryPanel.add(timeFromTitle);
        
        final JTextField timeFromEntry = new JTextField(getCurrentDate().format(), 10);
        timeEntryPanel.add(timeFromEntry);

        final JLabel timeToTitle = new JLabel("Latest Time");
        timeEntryPanel.add(timeToTitle);
        
        final JTextField timeToEntry = new JTextField(getTomorrowsDate().format(), 10);
        timeEntryPanel.add(timeToEntry);
        
        // Build button to hit API
        final JPanel executePanel = new JPanel();
        executePanel.setLayout(new FlowLayout());

		final JButton executeButton = buildExecuteButton(timeFromEntry, timeToEntry, cityFromEntry,
				cityToEntry);
		executePanel.add(executeButton);

        frame.add(cityEntryPanel);
        frame.add(timeEntryPanel);
        frame.add(executePanel);
        frame.add(responsePanel);
	}

	private static JButton buildExecuteButton(final JTextField timeFromEntry, final JTextField timeToEntry,
			final JComboBox<String> cityFromEntry, final JComboBox<String> cityToEntry) {
		final JButton executeButton = new JButton("Find Cheapest Flight");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				REPORTER.clearErrors();
				final FlightDate dateFrom = parseFlightDate(timeFromEntry.getText());
				final FlightDate dateTo = parseFlightDate(timeToEntry.getText());
				final String cityFromCode = locationNameToCode.get(cityFromEntry.getSelectedItem().toString());
				final String cityToCode = locationNameToCode.get(cityToEntry.getSelectedItem().toString());
				final FlightDTO response = CLIENT.callAPI(REPORTER, cityFromCode, cityToCode, dateFrom, dateTo);
				
				if (response != null) {
					//TODO: Report the flight in its own label
					REPORTER.addWarning("Found Flight with price: " + response.getPrice());
				}
			}
        });
		return executeButton;
	}
	
	private static FlightDate parseFlightDate(final String input) {
		final String[] components = input.split("/");
		final int day = Integer.parseInt(components[0]);
		final int month = Integer.parseInt(components[1]);
		final int year = Integer.parseInt(components[2]);
		
		return new FlightDate(year, month, day);
	}

	private static FlightDate getCurrentDate() {
		final LocalDateTime currentTime = LocalDateTime.now();
		return new FlightDate(currentTime.getYear(), currentTime.getMonthValue(), currentTime.getDayOfMonth());
	}
	
	private static FlightDate getTomorrowsDate() {
		final LocalDateTime currentTime = LocalDateTime.now().plusDays(1);
		return new FlightDate(currentTime.getYear(), currentTime.getMonthValue(), currentTime.getDayOfMonth());
	}
	
	private static LocationsResponseDTO getAirportsFromFile(final String filePath) {
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
