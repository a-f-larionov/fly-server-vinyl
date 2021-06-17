package fly.controllers;

import com.sun.net.httpserver.HttpExchange;
import vinyl.annotation.Component;
import vinyl.dependencies.httpserver.RequestMapping;
import vinyl.dependencies.httpserver.RequestParams;

import java.util.Map;

@Component
public class ScoreController {



    @RequestMapping("/save-score")
    public String saveScore(HttpExchange exchange, RequestParams params) {

        String uid = params.get("uid");
        String score = params.get("score");

        System.out.println(" Save score called");
        return "df" + params.get("uid");
    }
}
