package rx;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * A reactive value.
 * <p>
 * Every time the value changes, other reactive changes can occur.
 */
public final class Signal<T> {
    /**
     * The actual value.
     */
    private T inner;
    /**
     * The effects that depend on the signal's value.
     */
    ArrayList<Effect> dependants;

    /**
     * Creates a new signal.
     */
    public Signal(T inner) {
        this.inner = inner;
        dependants = new ArrayList<>();
    }

    /**
     * Returns the value of the signal.
     * <p>
     * For correctness, the return value of this method should not be mutated.
     */
    public T get() {
        return inner;
    }

    /**
     * Sets the value of the signal, returning the previous value.
     */
    public T set(T value) {
        final var prev = inner;

        inner = value;
        notifyChanged();

        return prev;
    }

    /**
     * Sets the value of the signal to the result of a function call, returning the previous value.
     * <p>
     * For correctness, the input to the function should not be mutated.
     */
    public T update(Function<T, T> f) {
        final var prev = inner;

        inner = f.apply(prev);
        notifyChanged();

        return prev;
    }

    /**
     * Notifies all dependants that the value has changed.
     */
    public void notifyChanged() {
        for (final var dependant : dependants)
            dependant.trigger();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Signal signal) {
            return inner.equals(signal.inner);
        }

        return inner.equals(obj);
    }

    @Override
    public int hashCode() {
        return inner.hashCode();
    }

    @Override
    public String toString() {
        return inner.toString();
    }
}
