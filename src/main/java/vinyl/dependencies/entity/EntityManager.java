package vinyl.dependencies.entity;

import vinyl.annotation.Component;
import vinyl.dependencies.db.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EntityManager {

    public List<Field> getFields(Class<?> entityClass) {

        List<Field> fields = new ArrayList<>();

        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {

            fields.add(new Field(
                    field.getName(),
                    field.getType(),
                    field.isAnnotationPresent(ID.class),
                    field.isAnnotationPresent(ID.class)
            ));
        }

        return fields;
    }

    public String getName(Class<?> entityClass) {

        return entityClass.getSimpleName().toLowerCase();
    }
}
