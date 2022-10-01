package edu.psgv.sweng861.flight_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextArea;

import org.junit.Test;

/**
 * Tests for {@link ErrorReporter}
 */
public class ErrorReporterTest {

	/**
	 * Tests the constructor and accessor
	 */
	@Test
	public void testAccessor() {
		final JTextArea label = new JTextArea();
		
		final ErrorReporter reporter = new ErrorReporter(label);
		
		assertEquals(label, reporter.getErrorLabel());
	}

	/**
	 * Test adding an error
	 */
	@Test
	public void testAddError() {
		final JTextArea label = new JTextArea();

		final ErrorReporter reporter = new ErrorReporter(label);

		reporter.addError("Houston we have a problem");

		// Some text was added to the label with an identification that it was an error
		assertTrue(label.getText().contains("ERROR"));
	}

	/**
	 * Test adding a warning
	 */
	@Test
	public void testAddWarning() {
		final JTextArea label = new JTextArea();

		final ErrorReporter reporter = new ErrorReporter(label);

		reporter.addWarning("Houston we have a warning");

		// Some text was added to the label with an identification that it was a warning
		assertTrue(label.getText().contains("WARN"));
	}
	
	/**
	 * Test adding text and clearing
	 */
	@Test
	public void testAddAndClear() {
		final JTextArea label = new JTextArea();

		final ErrorReporter reporter = new ErrorReporter(label);

		reporter.addError("Houston we have a problem");
		reporter.addWarning("Houston we have a warning");

		assertTrue(label.getText().contains("ERROR"));
		assertTrue(label.getText().contains("WARN"));
		
		reporter.clearErrors();
		
		assertEquals(0, label.getText().length());
	}

}
