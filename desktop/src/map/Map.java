package map;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A map of points collected in the mobile app.
 */
public class Map {
    /**
     * Collected points.
     */
    private ArrayList<Point> points = new ArrayList<>();
    /**
     * Collected readings.
     */
    private ArrayList<Reading> readings = new ArrayList<>();

    /**
     * Decodes a map from a JSON representation.
     */
    public static Map fromJson(String json) {
        final var mapper = new ObjectMapper();

        try {
            final var out = new Map();

            out.points = mapper.readValue(
                json,
                new TypeReference<ArrayList<Point>>() {}
            );

            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return points.toString() + "\n" + readings.toString();
    }
}
