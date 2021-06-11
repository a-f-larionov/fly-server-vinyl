package vinyl.dependencies.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println(exchange.getRequestURI());

        OutputStream responseBody = exchange.getResponseBody();


        String responseText = "OK";

        exchange.sendResponseHeaders(200, responseText.length());

        responseBody.write(responseText.getBytes());
        responseBody.flush();
        responseBody.close();
    }
}
