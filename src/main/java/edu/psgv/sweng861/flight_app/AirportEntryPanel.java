package edu.psgv.sweng861.flight_app;

import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

/**
 * Class which handles the panel that the user uses to input airports
 */
public class AirportEntryPanel {

	private Map<String, String> locationNameToCode;
	private JPanel panel;
	private JLabel cityToTitle;
	private JLabel cityFromTitle;
	JComboBox<String> cityFromBox;
	JComboBox<String> cityToBox;
	JTextField cityFromField;
	JTextField cityToField;
	private boolean comboMode;

	public AirportEntryPanel(final ErrorReporter reporter, final JPanel panel,
			final Map<String, String> locationNameToCode) {
		
		this.locationNameToCode = locationNameToCode;
		
		// Start in combobox mode
		this.comboMode = true;
		
		this.panel = panel;

		this.panel.setLayout(new FlowLayout());

        this.cityFromTitle = new JLabel("Departure City");
        this.panel.add(this.cityFromTitle);

        // Build list of airports
        if (locationNameToCode.isEmpty()) {
        	reporter.addError("Unable to Load initial cities, please use manual entry");
        }
        final String[] cities = locationNameToCode.keySet().toArray(new String[locationNameToCode.size()]);
        Arrays.sort(cities);

		this.cityFromBox = new JComboBox<String>(cities);
		this.panel.add(this.cityFromBox);
        AutoCompleteDecorator.decorate(this.cityFromBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);

        this.cityToTitle = new JLabel("Arrival City");
        this.panel.add(this.cityToTitle);

        this.cityToBox = new JComboBox<String>(cities);
        // Starts disabled because we start in any destination mode
        this.cityToBox.setEnabled(false);
        this.panel.add(this.cityToBox);
        AutoCompleteDecorator.decorate(this.cityToBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        
        // Initialize the text fields even though they won't start enabled
        this.cityFromField = new JTextField("", 10);
        this.cityToField = new JTextField("", 10);
        // Starts disabled because we start in any destination mode
        this.cityToField.setEnabled(false);
	}
	
	public void switchToManualEntry() {
		this.comboMode = false;
		panel.removeAll();
		// Rebuild components
		panel.add(this.cityFromTitle);
		panel.add(this.cityFromField);
		panel.add(this.cityToTitle);
		panel.add(this.cityToField);
		panel.revalidate();
		panel.repaint();
	}
	
	public void switchToComboEntry() {
		this.comboMode = true;
		panel.removeAll();
		// Rebuild components
		panel.add(this.cityFromTitle);
		panel.add(this.cityFromBox);
		panel.add(this.cityToTitle);
		panel.add(this.cityToBox);
		panel.revalidate();
		panel.repaint();
	}
	
	public void disableCityTo() {
		this.cityToBox.setEnabled(false);
		this.cityToField.setEnabled(false);
	}
	
	public void enableCityTo() {
		this.cityToBox.setEnabled(true);
		this.cityToField.setEnabled(true);
	}
	
	public String getCityFromCode() {
		if (this.comboMode) {
			return locationNameToCode.get(this.cityFromBox.getSelectedItem().toString());
		}
		return this.cityFromField.getText().toUpperCase();
	}
	
	public String getCityToCode() {
		if (this.comboMode) {
			return locationNameToCode.get(this.cityToBox.getSelectedItem().toString());
		}
		return this.cityToField.getText().toUpperCase();
	}

}
