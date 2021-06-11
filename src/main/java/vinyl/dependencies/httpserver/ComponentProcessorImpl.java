package vinyl.dependencies.httpserver;

import vinyl.annotation.Component;
import vinyl.intefaces.ComponentProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ComponentProcessorImpl implements ComponentProcessor {

    static Map<String, PathToMethod> pathToMethodMap = new HashMap<>();

    @Override
    public void beforeInit(Object component) {
        Method[] methods = component.getClass().getDeclaredMethods();

        Arrays.asList(methods).forEach((method) -> {

            if (method.isAnnotationPresent(RequestMapping.class)) {

                String path = method.getAnnotation(RequestMapping.class).value();

                pathToMethodMap.put(path, new PathToMethod(component, method));
            }
        });
    }

    @Override
    public void afterInit(Object component) {

    }
}
