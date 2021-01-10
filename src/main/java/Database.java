import java.sql.*;

public class Database {

    public Database () {
        connect();
    }

    // Establishes connection with database
    private void connect () {

        // Db connection
        Connection conn = null;

        try {
            // db parameters
            String url = "jdbc:sqlite:food.db";
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
