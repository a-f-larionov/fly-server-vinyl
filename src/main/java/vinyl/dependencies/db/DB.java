package vinyl.dependencies.db;

import vinyl.annotation.Component;

import java.sql.*;
import java.util.List;

@Component
public class DB {

    private Connection connection;

    private Connection getConnection() {

        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/fly_vinyl",
                        "root",
                        "root"
                );
            } catch (Exception e) {
                throw new RuntimeException("Cant connect", e);
            }
        }
        return connection;
    }

    private int executeUpdate(String query) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeUpdate(query);
        } catch (Exception e) {
            throw new RuntimeException("Cant execute query:" + e.getMessage(), e);
        }
    }

    private ResultSet executeQuery(String query) {
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            throw new RuntimeException("Cant execute query:" + e.getMessage(), e);
        }
    }

    public void getTableFields(String entityName) {

    }

    public void createTable(String tableName, List<Field> fields) {
        final var builder = new StringBuilder();

        builder.append("CREATE TABLE `" + tableName + "` (");

        fields.forEach((field) -> {
            builder
                    .append(" `")
                    .append(field.getName())
                    .append("` ")
                    .append(getMySQLType(field.getType().getName()));
            if (field.isPrimaryKey()) {
                builder.append(" PRIMARY KEY ");
            }
            if (field.isAutoincrement()) {
                builder.append(" AUTO_INCREMENT ");
            }
            builder.append(",");
        });

        builder.setLength(builder.length() - 1);

        builder.append(")");

        executeUpdate(builder.toString());
    }

    private String getMySQLType(String type) {

        String mySqlType;

        /*
         * int  32 bit - -2 147 483 648 - +2 147 483 647
         * mysql signed int -//-
         */

        switch (type) {
            case "int" -> mySqlType = "int(11)";

            case "long" -> mySqlType = "bigint(11)";

            default -> throw new RuntimeException("Type not found: " + type);
        }

        return mySqlType;
    }

    public void dropTable(String tableName) {

        executeUpdate("DROP TABLE " + tableName);
    }

    public boolean isTableExists(String tableName) {

        try {
            ResultSet r = executeQuery(
                    "SELECT count(*) as cnt FROM information_schema.tables " +
                            "WHERE table_name = '" + tableName + "' " +
                            "LIMIT 1");

            r.next();
            return r.getInt("cnt") > 0;

            //   return r.next();
        } catch (Exception e) {
            System.out.println("df");
            throw new RuntimeException("Cant" + e.getMessage(), e);
        }
    }
}
