package vinyl.dependencies.httpserver;

import java.lang.reflect.Method;

public class PathToMethod {

    public Object component;

    public Method method;

    public PathToMethod(Object component, Method method) {
        this.component = component;
        this.method = method;
    }
}
