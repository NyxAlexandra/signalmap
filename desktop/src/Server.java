import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * An HTTP server abstraction.
 * <p>
 * Create handled routes with
 */
public final class Server {
  final HttpServer server;

  /**
   * Creates a new HTTP server.
   * <p>
   * Will not be active until {@link Server#start()} is called.
   */
  public Server() throws IOException {
    server = HttpServer.create(new InetSocketAddress(8080), 0);
  }

  /**
   * Returns the currently bound network address of the server.
   */
  public InetAddress address() {
      try {
          return InetAddress.getLocalHost();
      } catch (UnknownHostException e) {
          throw new RuntimeException(e);
      }
  }

  /**
   * Returns the currently bound port of the server.
   */
  public int port() {
    return server.getAddress().getPort();
  }

  /**
   * Creates a new route.
   */
  public void route(String path, Function<Request, Response> handler) {
    // Accept both `foo` and`/foo`
    if (!path.startsWith("/"))
      path = "/" + path;

    server.createContext(path, exchange -> {
      final var request = new Request(exchange);
      final var response = handler.apply(request);
      final var bytes = response.body.getBytes("UTF-8");

      exchange.sendResponseHeaders(
        response.code,
        bytes.length
      );

      final var output = exchange.getResponseBody();

      output.write(bytes);
      output.close();
    });
  }

  /**
   * Starts the server.
   * <p>
   * The server logic runs on another thread, so this method will not block.
   */
  public void start() {
    server.start();
  }

  /**
   * Stops the server.
   */
  public void stop() {
    server.stop(0);
  }

  /**
   * An HTTP request.
   */
  public static class Request {
    final byte[] body;

    private Request(HttpExchange exchange) throws IOException {
      body = exchange.getRequestBody().readAllBytes();
    }

    /**
     * The bytes of the request.
     */
    public byte[] bytes() {
      return body;
    }

    /**
     * The bytes of the request re-interpreted as a UTF-8 string.
     * <p>
     * Returns null if the bytes were unable to be parsed as UTF-8.
     */
    public String utf8()  {
        try {
            return new String(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
  }

  /**
   * an HTTP response.
   */
  public static class Response {
    final int code;
    final String body;

    private Response(int code, String body) {
      this.code = code;
      this.body = body;
    }

    /**
     * The OK response.
     */
    public static Response ok(String response) {
      return new Response(HttpURLConnection.HTTP_OK, response);
    }
  }
}
