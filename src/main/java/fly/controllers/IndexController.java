package fly.controllers;

import vinyl.annotation.Component;
import vinyl.dependencies.httpserver.RequestMapping;

@Component
public class IndexController {

    @RequestMapping("/")
    public void index() {

        System.out.println("index called");
    }

    @RequestMapping("/about")
    public void about() {

        System.out.println("about called");
    }
}
