package edu.psgv.sweng861.flight_app;

import javax.swing.JLabel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Stores a {@link JLabel} with convenient methods to report errors
 */
public class ErrorReporter {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private JLabel errorDisplay;
	private String errorString;
	
	public ErrorReporter(final JLabel errorDisplay) {
		this.errorDisplay = errorDisplay;
		this.errorString = "";
		this.errorDisplay.setText(this.errorString);
	}
	
	public JLabel getErrorLabel() {
		return this.errorDisplay;
	}
	
	public void clearErrors() {
		this.errorString = "";
		this.errorDisplay.setText(this.errorString);
	}
	
	public void addError(final String error) {
		LOGGER.error("Sending error to user: " + error);
		addText(error, "ERROR");
	}
	
	public void addWarning(final String warning) {
		LOGGER.warn("Sending warning to user: " + warning);
		addText(warning, "WARN");
	}
	
	private void addText(final String message, final String prefix) {
		if (!this.errorString.isEmpty()) {
			this.errorString += "<br/>";
		}
		this.errorString += prefix + ": " + message;
		final String textRepr = "<html>" + this.errorString + "</html>";
		this.errorDisplay.setText(textRepr);
	}

}
