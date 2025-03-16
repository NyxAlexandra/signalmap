import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import rx.Effect;
import rx.Signal;

public class Root extends BorderPane {
  /** The current page the UI is displaying. */
  Page page;

  /** The HTTP server. */
  final Server server;

  /** The started/stopped state of the server. */
  final Signal<Boolean> running;

  public Root() throws IOException {
    super();

    server = new Server();
    running = new Signal<>(false);

    server.route(
        "connect",
        request -> {
          return Server.Response.ok("you said: " + request.utf8());
        });

    // Transition to initial page
    update(Page.Server);
  }

  /** Transition to a new page. */
  private void update(Page page) {
    this.page = page;

    switch (page) {
      case Server -> {
        // *Server info*
        //
        // address: 127.0.0.1
        // port: 8080
        //
        //    | start |

        final VBox info;
        {
          final var label = new Label("Server info");

          label.setFont(Font.font(24.0));

          final VBox map;
          {
            final var address = new Label("address: " + server.address().getHostAddress());
            final var port = new Label("port: " + server.port());

            map = new VBox(address, port);
          }

          final Signal<Button> startStop = new Signal<>(new Button());

          new Effect(
                  () ->
                      startStop.update(
                          button -> {
                            button.setText(!running.get() ? "Start" : "Stop");
                            button.setCancelButton(running.get());
                            button.setOnAction(
                                _e -> {
                                  running.update(v -> !v);
                                });

                            return button;
                          }))
              .dependOn(running);

          info = new VBox(label, map, startStop.get());
        }

        info.setSpacing(16.0);
        info.setPadding(new Insets(16.0));

        setCenter(info);
      }
    }
  }
}

/** The state of the app. */
enum Page {
  /** Display server info and allows the user to run it. */
  Server,
}
