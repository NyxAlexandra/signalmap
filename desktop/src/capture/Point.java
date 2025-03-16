package capture;

/**
 * A point in a {@link Room} relative to the origin (first recorded point).
 */
public record Point(float x, float y) {
    public static Point splat(float v) {
        return new Point(v, v);
    }
}
