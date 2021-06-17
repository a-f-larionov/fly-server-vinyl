package vinyl.dependencies.httpserver;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import vinyl.annotation.Component;
import vinyl.annotation.PostConstructor;
import vinyl.intefaces.ComponentProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

@Component
public class Runner {

    private HttpServer httpServer;

    @PostConstructor
    public void init() throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress(8081), 0);

        httpServer.setExecutor(null);

        ComponentProcessorImpl.pathToMethodMap.forEach((path, pathToMethod) -> {

            HttpContext context = httpServer.createContext(path);

            context.setHandler(new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {

                    try {
                        Object response = pathToMethod.method.invoke(pathToMethod.component);

                        OutputStream responseBody = exchange.getResponseBody();

                        String responseText = "-" + path;

                        exchange.sendResponseHeaders(200, responseText.length());

                        responseBody.write(responseText.getBytes());
                        responseBody.flush();
                        responseBody.close();

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        httpServer.start();

        HttpContext context = httpServer.createContext("/test");
        context.setHandler(new Handler());
    }
}
