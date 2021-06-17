package fly.controllers;

import vinyl.annotation.Component;
import vinyl.dependencies.httpserver.RequestMapping;

@Component
public class ScoreController {

    @RequestMapping("/save-score")
    public String saveScore() {
        System.out.println(" Save score called");
        return "df";
    }
}
