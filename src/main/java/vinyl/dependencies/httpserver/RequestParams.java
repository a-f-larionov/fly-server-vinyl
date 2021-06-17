package vinyl.dependencies.httpserver;

import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Map;

public class RequestParams {

    private Map<String, String> params;

    public RequestParams(Map<String, String> params) {

        this.params = params;
    }

    public String get(String name) {
        return params.get(name);
    }
}
