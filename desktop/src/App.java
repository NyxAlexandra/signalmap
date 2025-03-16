import static javafx.scene.text.Font.font;

import java.io.IOException;
import java.util.ArrayList;

import capture.Room;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Effect;
import rx.Signal;

public final class App extends Application {
  /**
   * The current page.
   *
   * <p>When this is mutated, the page updates.
   */
  private final Signal<Page> page;

  /** The HTTP server. */
  private final Server server;

  /** The root node the UI is rendered to. */
  private final BorderPane root;

  /** Pages of the UI. */
  enum Page {
    /** Display server info and allows the user to run it. */
    Server,
  }

  {
    try {
      server = new Server();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    page = new Signal<>(Page.Server);
    root = new BorderPane();

    // The effect that renders the UI
    new Effect(
            () -> {
              switch (page.get()) {
                case Server -> {
                  root.setCenter(new ServerPage(page, server));
                }
              }
            })
        .dependOn(page);
  }

  @Override
  public void start(Stage stage) throws IOException {
    stage.setTitle("Signalmap Desktop");
    stage.setScene(new Scene(root));

    stage.setResizable(true);
    stage.setMinHeight(500.0);
    stage.setMinWidth(600.0);

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

/** The UI for {@link App.Page#Server}. */
final class ServerPage extends VBox {
  final Server server;

  private final Signal<App.Page> page;
  private final Signal<Boolean> running;

  private final Label label;
  private final VBox map;
  private final Button startStop;

  ServerPage(Signal<App.Page> page, Server server) {
    super();

    this.server = server;
    this.page = page;
    running = new Signal<>(false);

    server.route(
      "map/create",
      request -> {
        return Server.Response.ok();
    });

    // *Server info*
    {
      label = new Label("Server info");

      label.setFont(font(24.0));
    }

    // id: 127.0.0.1
    // port: 8080
    {
      final var address = new Label("address: " + server.address().getHostAddress());
      final var port = new Label("port: " + server.port());

      map = new VBox(address, port);
    }

    // | start |
    {
      startStop = new Button();
      startStop.setOnAction(
          _e -> {
            server.start();
            running.update(v -> !v);
          });

      new Effect(() -> {
        startStop.setText(!running.get() ? "Start" : "Stop");
        startStop.setCancelButton(running.get());
      })
        .dependOn(running);
    }

    setSpacing(16.0);
    setPadding(new Insets(16.0));
    getChildren().addAll(label, map, startStop);
  }
}
