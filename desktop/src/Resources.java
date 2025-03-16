import java.util.HashMap;
import javafx.scene.image.Image;

/** Class for loading resource files. */
public final class Resources {
  /** Image resource cache. */
  private static HashMap<String, Image> IMAGES = new HashMap<>();

  private Resources() {}

  /** Returns an image for the resource with the given name (possibly null). */
  public static Image image(String name) {
    return IMAGES.computeIfAbsent(
        name,
        _name -> {
          try {
            final var self = new Resources();

            final var stream = self.getClass().getResourceAsStream(name);
            final var image = new Image(stream);

            return image;
          } catch (Exception _e) {
            return null;
          }
        });
  }
}
