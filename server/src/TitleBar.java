import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public final class TitleBar extends HBox {
  private Button close;
  private double mouseX, mouseY;

  public TitleBar() {
    super();

    close = new Button("x");

    mouseX = 0.0;
    mouseY = 0.0;

    getChildren().add(close);

    // Drag window by title bar

    addEventFilter(
        MouseEvent.MOUSE_PRESSED,
        event -> {
          mouseX = event.getX();
          mouseY = event.getY();
        });
    addEventFilter(
        MouseEvent.MOUSE_DRAGGED,
        event -> {
          double crrX = event.getScreenX();
          double crrY = event.getScreenY();

          final var stage = getStage();

          stage.setX(crrX - mouseX);
          stage.setY(crrY - mouseY);
        });

    close.setOnMouseClicked(
        event -> {
          final var stage = getStage();

          stage.close();
        });
  }

  private Stage getStage() {
    return (Stage) getScene().getWindow();
  }
}
