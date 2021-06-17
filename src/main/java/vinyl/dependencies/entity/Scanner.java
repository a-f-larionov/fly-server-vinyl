package vinyl.dependencies.entity;

import vinyl.annotation.Autowired;
import vinyl.annotation.Component;
import vinyl.core.ComponentFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class Scanner {

    private List<Class<?>> entityList = new ArrayList<>();

    @Autowired
    ComponentFactory componentFactory;

    public void scan() {

        entityList = componentFactory.getClassList()
                .stream()
                .filter(aClass -> aClass.isAnnotationPresent(Entity.class))
                .toList();
    }

    public List<Class<?>> getEntityList() {
        return entityList;
    }
}
