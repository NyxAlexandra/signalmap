import static javafx.scene.text.Font.font;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import map.Map;

public final class App extends Application {
  /** The HTTP server. */
  private final Server server;

  /** The root node the UI is rendered to. */
  private final BorderPane root;
  /**
   * List of recorded maps and their readings.
   */
  private final ArrayList<Map> maps;

  {
    root = new BorderPane();
    maps = new ArrayList<>();

    try {
      server = new Server();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    server.route(
        "map/create",
        request -> {
          final var id = maps.size();
          final var map = Map.fromJson(request.utf8());

          maps.add(map);

          return Server.Response.ok("" + id);
        }
    );

    render();
    server.start();
  }

  /**
   * Renders the UI.
   */
  public void render() {
    final VBox serverInfo;
    {
      // *Server info*
      final var label = new Label("Server info");

      label.setFont(font(24.0));

      // id: 127.0.0.1
      // port: 8080
      final VBox map;
      {
        final var address = new Label("address: " + server.address().getHostAddress());
        final var port = new Label("port: " + server.port());

        map = new VBox(address, port);
      }

      serverInfo = new VBox(label, map);
    }

    root.setLeft(serverInfo);
    root.setCenter(new Label("wanna cry"));
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

