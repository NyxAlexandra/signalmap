package capture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** A capture of a room. */
public final class Room {
  /** The anchors of the room (order matters). */
  private final Point[] anchors;

  /** Readings taken in the room. */
  private final ArrayList<Reading> readings;

  /** A connected edge between two anchors. */
  public record Edge(Point a, Point b) {}

  /** Creates a room from its anchors. */
  public Room(Point... anchors) {
    this.anchors = anchors;
    readings = new ArrayList<>();
  }

  /** Returns an array of the room's anchors. */
  public Point[] anchors() {
    return anchors;
  }

  /** Returns a mutable list of the room's readings. */
  public List<Reading> readings() {
    return readings;
  }

  /** Returns a stream over the edges between anchors of the room. */
  public Stream<Edge> edges() {
    return IntStream.range(0, anchors.length)
        .mapToObj(
            i -> {
              final var a = anchors[i];
              // Wrap around for final index
              final var b = anchors[(i + 1) % anchors.length];

              return new Edge(a, b);
            });
  }
}
