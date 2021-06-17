package fly.controllers;

import com.sun.net.httpserver.HttpExchange;
import vinyl.annotation.Component;
import vinyl.dependencies.httpserver.RequestMapping;
import vinyl.dependencies.httpserver.RequestParams;

@Component
public class IndexController {

    @RequestMapping("/")
    public void index(HttpExchange exchange, RequestParams params) {

        System.out.println("index calleddd");
    }

    @RequestMapping("/about")
    public void about(HttpExchange exchange, RequestParams params) {

        System.out.println("about called");
    }
}
