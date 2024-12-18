import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server implements Closeable {
    private final HttpServer inner;

    public static void main(String[] _args) throws IOException {
        try (var server = new Server()) {
            while (true);
        }
    }

    // ---

    public Server() throws IOException {
        inner = HttpServer.create(new InetSocketAddress(0), 0);

        inner.createContext("/reply", new Reply());
        inner.setExecutor(null);

        System.out.printf(
            "%s:%d\n",
            InetAddress.getLocalHost().getHostAddress(),
            inner.getAddress().getPort()
        );

        inner.start();
    }

    @Override
    public void close() throws IOException {
        inner.stop(1);
    }

    // ---

    static class Reply implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            var request = new String(exchange.getRequestBody().readAllBytes());
            var response = "don't say that to me >:(";

            exchange.sendResponseHeaders(
                HttpURLConnection.HTTP_OK,
                response.getBytes().length
            );

            var output = exchange.getResponseBody();

            output.write(response.getBytes());
            output.close();
        }
    }


}