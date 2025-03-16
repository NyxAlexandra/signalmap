import org.junit.jupiter.api.Test;
import rx.Effect;

public class EffectTests {
    @Test
    void triggerNull() {
        final var effect = new Effect(null);

        effect.trigger();
    }
}
