package vinyl.dependencies.db;

public class Field {

    private String name;

    private Class<?> type;

    private boolean isID;

    private boolean isAutoincrement;

    public Field(String name, Class<?> type, boolean isID, boolean isAutoincrement) {

        this.name = name;
        this.type = type;
        this.isID = isID;
        this.isAutoincrement = isAutoincrement;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return isID;
    }

    public boolean isAutoincrement() {
        return isAutoincrement;
    }
}
