package edu.psgv.sweng861.flight_app;

import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Stores a {@link JLabel} with convenient methods to report errors
 */
public class ErrorReporter {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private JTextArea errorDisplay;
	private String errorString;

	/**
	 * Constructor
	 * 
	 * @param errorDisplay The {@link JTextArea} which will display error and warnings
	 */
	public ErrorReporter(final JTextArea errorDisplay) {
		this.errorDisplay = errorDisplay;
		this.errorString = "";
		this.errorDisplay.setText(this.errorString);
	}

	/**
	 * @return the {@link JTextArea} stored in this reporter
	 */
	public JTextArea getErrorLabel() {
		return this.errorDisplay;
	}

	/**
	 * Clears all accumulated error and warning messages in this reporter
	 */
	public void clearErrors() {
		this.errorString = "";
		this.errorDisplay.setText(this.errorString);
	}

	/**
	 * Adds a text to the reporter label on a new line, it will have the ERROR
	 * prefix
	 * 
	 * @param error the text to display to the user
	 */
	public void addError(final String error) {
		LOGGER.error("Sending error to user: " + error);
		addText(error, "ERROR");
	}

	/**
	 * Adds a text to the reporter label on a new line, it will have the WARN prefix
	 * 
	 * @param warning the text to display to the user
	 */
	public void addWarning(final String warning) {
		LOGGER.warn("Sending warning to user: " + warning);
		addText(warning, "WARN");
	}

	/**
	 * Helper method to add arbitrary text to the {@link JLabel}
	 * 
	 * @param message The text to display
	 * @param prefix  The prefix to use before the message, lets the user know the
	 *                level of the message
	 */
	private void addText(final String message, final String prefix) {
		if (!this.errorString.isEmpty()) {
			this.errorString += "\n";
		}
		this.errorString += prefix + ": " + message;
		final String textRepr = this.errorString;
		this.errorDisplay.setText(textRepr);
	}

}
