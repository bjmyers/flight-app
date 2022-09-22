package edu.psgv.sweng861.flight_app;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import edu.psgv.sweng861.flight_app.api.APIClient;
import edu.psgv.sweng861.flight_app.dto.APICallResponse;
import edu.psgv.sweng861.flight_app.dto.FlightDate;
import edu.psgv.sweng861.flight_app.dto.LocationDTO;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

/**
 * Gathers and organizes all components in the UI
 */
public class UIManager {
	
	private static APIClient CLIENT;
	private static Map<String, String> locationNameToCode;
	
	public static void main(final String[] args) {
		CLIENT = new APIClient();
		
		final LocationsResponseDTO locations = CLIENT.getLocations();
		locationNameToCode = new HashMap<>();
		if (locations.getLocations() != null) {
			// When there's an error in getting the locations, locationNameToCode will be empty
			locationNameToCode.put("Philadelphia", "PHL");
			for (LocationDTO loc: locations.getLocations()) {
				locationNameToCode.put(loc.getName(), loc.getCode());
			}
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
        
        final JLabel responseLabel = new JLabel();
        responsePanel.add(responseLabel);
		
		// Build text entry row for entering departure and arrival cities
        final JPanel cityEntryPanel = new JPanel();
        cityEntryPanel.setLayout(new FlowLayout());

        final JLabel cityFromTitle = new JLabel("Departure City");
        cityEntryPanel.add(cityFromTitle);

        if (locationNameToCode.isEmpty()) {
        	//TODO: Set up default list of cities or allow for manual airport code entry
        	responseLabel.setText("Unable to Load initial cities, please use manual entry");
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
        AutoCompleteDecorator.decorate(cityFromEntry, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        
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

		final JButton executeButton = buildExecuteButton(responseLabel, timeFromEntry, timeToEntry, cityFromEntry,
				cityToEntry);
		executePanel.add(executeButton);

        frame.add(cityEntryPanel);
        frame.add(timeEntryPanel);
        frame.add(executePanel);
        frame.add(responsePanel);
	}

	private static JButton buildExecuteButton(final JLabel responseLabel, final JTextField timeFromEntry,
			final JTextField timeToEntry, final JComboBox<String> cityFromEntry, final JComboBox<String> cityToEntry) {
		final JButton executeButton = new JButton("Find Cheapest Flight");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final FlightDate dateFrom = parseFlightDate(timeFromEntry.getText());
				final FlightDate dateTo = parseFlightDate(timeToEntry.getText());
				final String cityFromCode = locationNameToCode.get(cityFromEntry.getSelectedItem().toString());
				final String cityToCode = locationNameToCode.get(cityToEntry.getSelectedItem().toString());
				final APICallResponse response = CLIENT.callAPI(cityFromCode, cityToCode, dateFrom, dateTo);
				
				if (response.wasSuccessful()) {
					responseLabel.setText("Found Flight with price: " + response.getFlight().getPrice());
				}
				else {
					responseLabel.setText(response.getErrorMessage());
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

}
