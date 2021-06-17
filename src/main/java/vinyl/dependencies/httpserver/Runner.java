package vinyl.dependencies.httpserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import vinyl.annotation.Component;
import vinyl.annotation.PostConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Component
public class Runner {

    private HttpServer httpServer;

    @PostConstructor
    public void init() throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress(8081), 0);

        httpServer.setExecutor(null);

        ComponentProcessorImpl.pathToMethodMap.forEach((path, pathToMethod) -> {

            HttpContext context = httpServer.createContext(path);

            context.setHandler(exchange -> {

                if (!exchange.getRequestURI().getPath().equals(path)) {
                    String responseText = "Not found.";

                    exchange.sendResponseHeaders(404, responseText.length());

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(responseText.getBytes());
                    responseBody.flush();
                    responseBody.close();

                    return;
                }

                try {

                    Object response = pathToMethod.method
                            .invoke(pathToMethod.component,
                                    exchange,
                                    new RequestParams(queryToMap(exchange.getRequestURI().getQuery())));

                    String responseText = "-" + path;

                    if (response instanceof String) {
                        responseText = response.toString();
                    }

                    exchange.sendResponseHeaders(200, responseText.length());

                    OutputStream responseBody = exchange.getResponseBody();

                    responseBody.write(responseText.getBytes());
                    responseBody.flush();
                    responseBody.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        httpServer.start();
    }

    private Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
