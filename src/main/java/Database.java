import java.sql.*;

public class Database {

    public Database () {
        connect();
    }

    public void connect () {

        Connection conn = null;

        System.out.println("jdbc:sqlite:" + System.getProperty("user.dir").replace("\\", "/") +
                "/src/main/resources/assets/sqlite/food.db");

        try {
            // db parameters
            String url = "jdbc:sqlite:" + System.getProperty("user.dir").replace("\\", "/") +
                    "/src/main/resources/assets/sqlite/food.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
