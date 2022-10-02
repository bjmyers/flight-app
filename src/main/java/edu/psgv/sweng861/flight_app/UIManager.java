package edu.psgv.sweng861.flight_app;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		final JTextArea errorLabel = new JTextArea(1, 60);
		errorLabel.setEditable(false);
		errorLabel.setLineWrap(true);
		REPORTER = new ErrorReporter(errorLabel);

		LocationsResponseDTO locations = AirportLoader.getAirports(REPORTER, AIRPORT_FILE_PATH, CLIENT);

		locationNameToCode = new HashMap<>();
		if (locations != null && locations.getLocations() != null) {
			// When there's an error in getting the locations, locationNameToCode will be
			// empty
			locationNameToCode.put("Philadelphia (PHL)", "PHL");
			for (LocationDTO loc : locations.getLocations()) {
				locationNameToCode.put(String.format("%s (%s)", loc.getName(), loc.getCode()), loc.getCode());
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
	static void addUserEntryFields(final JFrame frame) {
		
        frame.setLayout(new GridLayout(9, 1));
        
        // Build Text Field to respond to the user
        final JPanel responsePanel = new JPanel();
        responsePanel.setLayout(new FlowLayout());
		responsePanel.add(REPORTER.getErrorLabel());
		
		// Build text entry row for entering departure and arrival cities
		final JPanel cityEntryPanel = new JPanel();
		final AirportEntryPanel airportEntryPanel = new AirportEntryPanel(REPORTER, cityEntryPanel, locationNameToCode);
		
		// Build toggle between manual and auto entry modes
		final JToggleButton airportEntryToggle = new JToggleButton("Enter Airport Code Manually");
		airportEntryToggle.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					airportEntryPanel.switchToManualEntry();
				} else {
					airportEntryPanel.switchToComboEntry();
				}
			}
		});
        final JPanel airportEntryToggleFrame = new JPanel();
        airportEntryToggleFrame.setLayout(new FlowLayout());
        airportEntryToggleFrame.add(airportEntryToggle);

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
        
        // Build fields to enter number of adults and children
        final JPanel passengersPanel = new JPanel();
        timeEntryPanel.setLayout(new FlowLayout());
        
        final JLabel adultsTitle = new JLabel("Number of Adults flying:");
        passengersPanel.add(adultsTitle);
        
        final JTextField adultsEntry = new JTextField("1", 10);
        passengersPanel.add(adultsEntry);

        final JLabel childrenTitle = new JLabel("Number of Children flying:");
        passengersPanel.add(childrenTitle);
        
        final JTextField childrenEntry = new JTextField("0", 10);
        passengersPanel.add(childrenEntry);
        
        // Build radio button group to select entry mode
        final JRadioButton anyDestinationButton = new JRadioButton("Send me anywhere");
        anyDestinationButton.setSelected(true);
        anyDestinationButton.setLayout(new FlowLayout());
        anyDestinationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				airportEntryPanel.disableCityTo();
			}
        });
        final JRadioButton specificDestinationButton = new JRadioButton("Take me here");
        specificDestinationButton.setLayout(new FlowLayout());
        specificDestinationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				airportEntryPanel.enableCityTo();
			}
        });
        final ButtonGroup destinationEntryMode = new ButtonGroup();
        destinationEntryMode.add(anyDestinationButton);
        destinationEntryMode.add(specificDestinationButton);
        
        // Build button to hit API
        final JPanel executePanel = new JPanel();
        executePanel.setLayout(new FlowLayout());

		final JButton executeButton = buildExecuteButton(airportEntryPanel, timeFromEntry, timeToEntry,
				adultsEntry, childrenEntry, anyDestinationButton);
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
        frame.add(airportEntryToggleFrame);
        frame.add(timeEntryPanel);
        frame.add(passengersPanel);
        frame.add(executePanel);
        frame.add(flightPanel);
        frame.add(responsePanel);
	}

	/**
	 * Builds the button to execute the API call
	 * 
	 * @param airportEntryPanel    the {@link AirportEntryPanel} which handles
	 *                             airport entry
	 * @param timeFromEntry        the {@link JTextField} which holds the earliest
	 *                             date to query
	 * @param timeToEntry          the {@link JTextField} which holds the latest
	 *                             date to query
	 * @param adultEntry           the {@link JTextField} which holds the number of
	 *                             adults
	 * @param childrenEntry        the {@link JTextField} which holds the number of
	 *                             children
	 * @param anyDestinationButton A {@link JRadioButton} which tells if the user
	 *                             desires to use a single destination or all of
	 *                             them
	 * @return a {@link JButton} which executes the API call when pressed
	 */
	private static JButton buildExecuteButton(final AirportEntryPanel airportEntryPanel, final JTextField timeFromEntry,
			final JTextField timeToEntry, final JTextField adultEntry, final JTextField childrenEntry,
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
				final Integer adults = parsePositiveNumber(adultEntry.getText());
				final Integer children = parsePositiveNumber(childrenEntry.getText());
				if (adults == null || children == null) {
					return;
				}
				
				final String cityFromCode = airportEntryPanel.getCityFromCode();
				final String cityToCode = airportEntryPanel.getCityToCode();
				final FlightDTO response;
				if (anyDestinationButton.isSelected()) {
					LOGGER.info("Calling API in any destination mode");
					// Use an empty string as the cityTo argument to use any destination
					response = CLIENT.callAPI(REPORTER, cityFromCode, "", dateFrom, dateTo, adults, children);
				}
				else {
					LOGGER.info("Calling API in specific mode");
					response = CLIENT.callAPI(REPORTER, cityFromCode, cityToCode, dateFrom, dateTo, adults, children);
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
	 * @param input a String to parse and convert to a positive integer
	 * @return
	 */
	static Integer parsePositiveNumber(final String input) {
		final Integer output;
		try {
			output = Integer.valueOf(input);
		}
		catch (NumberFormatException e) {
			REPORTER.addError("Unable to Parse input value: " + input);
			return null;
		}
		if (output != null && output < 0) {
			REPORTER.addError("Input value must be positive: " + input);
			return null;
		}
		return output;
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

}
