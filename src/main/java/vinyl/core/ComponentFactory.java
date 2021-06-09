package vinyl.core;

import vinyl.annotation.Component;
import vinyl.annotation.Autowired;
import vinyl.annotation.PostConstructor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ComponentFactory {

    private Map<String, Object> singletons = new HashMap();

    ComponentFactory(Class applicationClass) throws IOException, URISyntaxException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        scanAndProcessClasses(applicationClass);
        autowiring();
        postConstructors();
    }

    private void postConstructors() throws InvocationTargetException, IllegalAccessException {

        for (Object component : singletons.values()) {
            for (Method method : component.getClass().getDeclaredMethods()) {

                if (method.isAnnotationPresent(PostConstructor.class)) {

                    processPostConstructor(component, method);
                }
            }
        }
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

            for (Field field : component.getClass().getDeclaredFields()) {

                if (field.isAnnotationPresent(Autowired.class)) {

                    processAutowiredField(component, field);

                }
            }
        }
    }

    private void processAutowiredField(Object component, Field field) throws IllegalAccessException {

        for (Object dependency : singletons.values()) {
            if (dependency.getClass().equals(field.getType())) {

                if (field.canAccess(component)) {
                    field.set(component, dependency);
                } else {
                    field.setAccessible(true);
                    field.set(component, dependency);
                    field.setAccessible(false);
                }
            }
        }
    }

    private void scanAndProcessClasses(Class applicationClass) throws IOException, URISyntaxException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        String packageName = applicationClass.getPackageName();

        String path = packageName.replace(".", "/");

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        Enumeration<URL> resources = systemClassLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            scanDir(url.toString(), packageName);
        }
    }

    private void scanDir(String url, String packageName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        //   System.out.println(url);
        File file = new File(URI.create(url));

        for (File classFile : file.listFiles()) {
            String filename;
            filename = classFile.getName();

            if (classFile.isDirectory()) {
                scanDir(classFile.toURI().toString(), packageName + "." + filename);
            }

            if (classFile.isFile() && filename.endsWith("class")) {

                String className = filename.substring(0, filename.lastIndexOf("."));

                Class aClass = Class.forName(packageName + "." + className);

                processClass(aClass);
            }
        }
    }

    private void processClass(Class<?> aClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (aClass.isAnnotationPresent(Component.class)) {
            processComponentAnnotation(aClass);
        }
    }

    private void processComponentAnnotation(Class<?> aClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Object instance = aClass.getDeclaredConstructor().newInstance();

        String componentName = aClass.getName().toLowerCase();

        singletons.put(componentName, instance);
    }
}
