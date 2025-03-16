package rx;

/** A reactive effect ran when dependencies change. */
public final class Effect {
  private final Runnable inner;

  /**
   * Creates and triggers a new reactive effect.
   *
   * <p>You should likely call {@link Effect#dependOn} to set when this effect should run.
   */
  public Effect(Runnable inner) {
    this.inner = inner;

    trigger();
  }

  /**
   * Set the signals that the effect depends on.
   *
   * <p>Every time a signal changes, this effect will be run.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Effect dependOn(Signal... signals) {
    for (final var signal : signals) signal.dependants.add(this);

    return this;
  }

  /** Manually trigger the effect. */
  public void trigger() {
    if (inner != null) inner.run();
  }
}
