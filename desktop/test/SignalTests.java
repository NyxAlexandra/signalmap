import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import rx.Effect;
import rx.Signal;

public class SignalTests {
  @Test
  @SuppressWarnings("unlikely-arg-type")
  void eq() {
    final var signal = new Signal<>("hello");

    assert signal.equals(signal);

    assert signal.equals("hello");
    assert signal.equals(new Signal<>("hello"));

    assert !signal.equals("goodbye");
    assert !signal.equals(new Signal<>("goodbye"));
  }

  @Test
  void fromNull() {
    final var signal = new Signal<String>(null);

    assert signal.get() == null;
    assert signal.update(prev -> "non null") == null;
  }

  @Test
  void set() {
    final var signal = new Signal<>("hello");

    assert signal.set("goodbye").equals("hello");
  }

  @Test
  @SuppressWarnings("unlikely-arg-type")
  void update() {
    final var signal = new Signal<>("hello");

    assert signal.update(prev -> prev + " world!").equals("hello");
    assert signal.equals("hello world!");
  }

  @Test
  void notifyChanged() {
    final var signal = new Signal<>(0);
    final var counter = new AtomicInteger();

    new Effect(() -> counter.addAndGet(1)).dependOn(signal);

    signal.update(n -> n + 1);
    signal.notifyChanged();

    assert signal.get() == 1;
    assert counter.get() == 3;
  }
}
