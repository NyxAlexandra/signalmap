import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class App extends Application {
  @Override
  public void start(Stage stage) {
    setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

    final VBox root;
    {
      final var titleBar = new TitleBar();
      final var text = new Label("I love Java");

      root = new VBox(titleBar, text);
      text.setPadding(new Insets(32.0));
    }

    stage.initStyle(StageStyle.UNDECORATED);
    stage.setScene(new Scene(root));
    stage.setTitle("Counter");

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
