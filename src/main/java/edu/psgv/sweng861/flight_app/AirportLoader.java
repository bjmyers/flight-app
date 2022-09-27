package edu.psgv.sweng861.flight_app;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import edu.psgv.sweng861.flight_app.api.APIClient;
import edu.psgv.sweng861.flight_app.dto.LocationsResponseDTO;

public class AirportLoader {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * @param reporter a {@link ErrorReporter}
	 * @param filePath path to a file containing the JSON representation of a
	 *                 {@link LocationsResponseDTO} object
	 * @return the {@link LocationsResponseDTO} object deserialized from the file's
	 *         contents
	 */
	static LocationsResponseDTO getAirportsFromFile(final ErrorReporter reporter, final String filePath) {
		LOGGER.info("Retreiving Airports from File");

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectReader reader = mapper.readerFor(LocationsResponseDTO.class);
		try {
			final LocationsResponseDTO response = reader.readValue(new File(filePath));
			if (response.getLocations() == null) {
				reporter.addWarning("Found no Airports in static List, attempting to dynamically load Airports");
				return null;
			}
			LOGGER.info("Retrieved " + response.getLocations().size() + " airports");
			return response;
		} catch (IOException e) {
			reporter.addWarning("Unable to read in static Airport List, attempting to dynamically load Airports");
		}
		return null;
	}
	
	/**
	 * Loads airports, first from a file, then from the API if unsuccessful
	 * @param reporter an {@link ErrorReporter}
	 * @param filePath the path to the airports file
	 * @param client an {@link APIClient} to use if file loading is unsuccessful
	 * @return a {@link LocationsResponseDTO}, can be null if both methods were unsuccessful
	 */
	static LocationsResponseDTO getAirports(final ErrorReporter reporter, final String filePath, final APIClient client) {
		LocationsResponseDTO output = getAirportsFromFile(reporter, filePath);
		if (output == null) {
			LOGGER.warn("Null airports from file, calling client for airports");
			output = client.getLocations(reporter);
		}
		return output;
	}
}
