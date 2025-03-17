package map;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * A connection speed reading.
 */
public record Reading(Point location, double download) {
    /**
     * Decodes a reading from a JSON representation.
     */
    public static Reading fromJson(String json) {
        final var mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, Reading.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
