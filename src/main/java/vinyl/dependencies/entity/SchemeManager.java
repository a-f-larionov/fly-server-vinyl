package vinyl.dependencies.entity;

import vinyl.annotation.Autowired;
import vinyl.annotation.Component;
import vinyl.dependencies.db.DB;

@Component
public class SchemeManager {

    @Autowired
    private Scanner scanner;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DB db;

    public void updateTables() {

        scanner.getEntityList()
                .forEach(this::updateTable);
    }

    public void updateTable(Class<?> entityClass) {

        String entityName;
        entityName = entityManager.getName(entityClass);

        //@todo
        // var dbFields = db.getTableFields(enitityName);
        var entityFields = entityManager.getFields(entityClass);

        if (db.isTableExists(entityName)) {
            db.dropTable(entityName);
        }
        db.createTable(entityName, entityFields);
        // fields.filter(!dbFields.equals(entityFields));

//        if (filterd.size > 0) {
//            db.dropEntity(entityName);
//            db.crateEntity(entityName, entityFields);
//        }
    }

    public void updateFields() {
    }


}
