import capture.Point;
import capture.Room;
import org.junit.jupiter.api.Test;

public final class RoomTests {
  @Test
  void edgeEq() {
    assert new Room.Edge(Point.splat(0.0f), Point.splat(1.0f))
        .equals(new Room.Edge(Point.splat(0.0f), Point.splat(1.0f)));

    assert !new Room.Edge(Point.splat(0.0f), Point.splat(1.0f))
        .equals(new Room.Edge(Point.splat(1.0f), Point.splat(2.0f)));
  }

  @Test
  void edges() {
    final var a = Point.splat(0.0f);
    final var b = Point.splat(1.0f);
    final var c = Point.splat(2.0f);

    final var room = new Room(a, b, c);
    final var edges = room.edges().iterator();

    assert edges.next().equals(new Room.Edge(a, b));
    assert edges.next().equals(new Room.Edge(b, c));
    assert edges.next().equals(new Room.Edge(c, a));
    assert !edges.hasNext();
  }
}
