package vinyl.core;

import vinyl.Vinyl;
import vinyl.annotation.Autowired;
import vinyl.annotation.Component;
import vinyl.annotation.PostConstructor;
import vinyl.intefaces.ComponentProcessor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

@Component
public class ComponentFactory {

    private Map<String, Object> singletons = new HashMap();

    private List<Class<?>> classList = new ArrayList<>();

    public ComponentFactory() {

    }

    public ComponentFactory(Class applicationClass) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        classList.addAll(scanClasses(Vinyl.class));

        classList.addAll(scanClasses(applicationClass));

        addToSingletons(classList);

        beforeInitCaller();

        autowiring();

        postConstructorsCaller();

        afterInitCaller();
    }

    public List<Class<?>> getClassList() {
        return classList;
    }

    private void beforeInitCaller() {
        doWithInterfaced(ComponentProcessor.class, (singleton) ->
                doWithSingletons(
                        ((ComponentProcessor) singleton)::beforeInit
                )
        );
    }

    private void postConstructorsCaller() throws InvocationTargetException, IllegalAccessException {

        for (Object component : singletons.values()) {
            for (Method method : component.getClass().getDeclaredMethods()) {

                if (method.isAnnotationPresent(PostConstructor.class)) {

                    processPostConstructor(component, method);
                }
            }
        }
    }

    private void afterInitCaller() {
        doWithInterfaced(ComponentProcessor.class, (singleton) ->
                doWithSingletons(
                        ((ComponentProcessor) singleton)::afterInit
                )
        );
    }

    private void doWithSingletons(Consumer run) {
        singletons.forEach((name, component) -> {
            run.accept(component);
        });
    }

    private void doWithInterfaced(Class aInterface, Consumer doWhat) {

        singletons.forEach((name, singleton) -> {
            if (Arrays.asList(singleton.getClass().getInterfaces())
                    .contains(aInterface)) {
                doWhat.accept(singleton);
            }
        });
    }

    private void processPostConstructor(Object component, Method method) throws InvocationTargetException, IllegalAccessException {

        if (method.canAccess(component)) {

            method.invoke(component);

        } else {

            method.setAccessible(true);
            method.invoke(component);
            method.setAccessible(false);
        }
    }

    private void autowiring() throws IllegalAccessException {
        for (Object component : singletons.values()) {

            for (java.lang.reflect.Field field : component.getClass().getDeclaredFields()) {

                if (field.isAnnotationPresent(Autowired.class)) {

                    processAutowiredField(component, field);

                }
            }
        }
    }

    private void processAutowiredField(Object component, java.lang.reflect.Field field) throws IllegalAccessException {

        boolean isSetted = false;

        for (Object dependency : singletons.values()) {
            if (dependency.getClass().equals(field.getType())) {

                if (isSetted) {
                    throw new RuntimeException("Field have more then one candidate" +
                            "" + component.getClass().getName() +
                            "::" + field.getName() +
                            " type " + field.getType()
                    );
                }
                isSetted = true;

                if (field.canAccess(component)) {
                    field.set(component, dependency);
                } else {
                    field.setAccessible(true);
                    field.set(component, dependency);
                    field.setAccessible(false);
                }
            }
        }

        if (!isSetted) {
            throw new RuntimeException("Cant find component to field: " +
                    "" + component.getClass().getName() +
                    "::" + field.getName() +
                    " type " + field.getType()
            );
        }
    }

    private List<Class<?>> scanClasses(Class applicationClass) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<Class<?>> classList = new ArrayList<>();

        String packageName = applicationClass.getPackageName();

        String path = packageName.replace(".", "/");

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        Enumeration<URL> resources = systemClassLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();

            classList.addAll(
                    scanDir(url.toString(), packageName)
            );
        }
        return classList;
    }

    private List<Class<?>> scanDir(String url, String packageName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<Class<?>> classList = new ArrayList<>();

        File file = new File(URI.create(url));

        for (File classFile : file.listFiles()) {
            String filename;
            filename = classFile.getName();

            if (classFile.isDirectory()) {
                classList.addAll(scanDir(classFile.toURI().toString(), packageName + "." + filename));
            }

            if (classFile.isFile() && filename.endsWith(".class")) {

                String className = filename.substring(0, filename.lastIndexOf("."));

                Class<?> aClass = Class.forName(packageName + "." + className);

                classList.add(aClass);
            }
        }
        return classList;
    }

    private void addToSingletons(List<Class<?>> classList) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        for (Class<?> aClass : classList) {

            if (aClass.isAnnotationPresent(Component.class)) {

                addToSingletons(aClass);
            }
        }
    }

    private void addToSingletons(Class<?> aClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        String componentName = aClass.getName().toLowerCase();
        Object instance;

        if (aClass == ComponentFactory.class) {
            instance = this;
        } else {
            instance = aClass.getDeclaredConstructor().newInstance();
        }

        singletons.put(componentName, instance);
    }
}
