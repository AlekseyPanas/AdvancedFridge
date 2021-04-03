import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class Database {

    private final Connection conn;

    public Database () {
        this.conn = connect();
    }

    // Establishes connection with database and returns it
    private Connection connect () {
        // Db connection
        Connection conn = null;

        try {
            // db parameters
            String url = "jdbc:sqlite:src/main/resources/sqlite/food.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return conn;
    }

    // Runs a select query
    private Product executeSelect (String sql) {
        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            if (rs.next()) {
                // Gets results and creates product object
                return new Product(rs.getInt("ID"),
                        rs.getString("barcode"),
                        rs.getString("product_name"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    // Returns all DB entries that fit the search string by product name
    public ArrayList<Product> searchSelect (String searchString) {
        try (Statement stmt = this.conn.createStatement();
             ResultSet rs = stmt.executeQuery("select ID, barcode, product_name from food_table")){

            ArrayList<Product> dbProducts = new ArrayList<>();

            while (rs.next()) {
                // Adds products to list from db
                dbProducts.add(new Product(rs.getInt("ID"),
                        rs.getString("barcode"),
                        rs.getString("product_name")));
            }

            // Removes products that don't fit the search string
            ArrayList<Product> removeProducts = new ArrayList<>();
            for (int i = 0; i < dbProducts.size(); i++) {
                if (!dbProducts.get(i).product_name.toLowerCase(Locale.ROOT).contains(searchString.toLowerCase(Locale.ROOT))) {
                    removeProducts.add(dbProducts.get(i));
                }
            }
            for (int i = 0; i < removeProducts.size(); i++) {
                dbProducts.remove(removeProducts.get(i));
            }

            // Returns final product list
            return dbProducts;

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    // Returns food item from db based on given barcode
    public Product getItemFromBarcode (String barcodeID) {
        return this.executeSelect(String.format("SELECT ID, barcode, product_name FROM food_table where barcode = %s;", barcodeID));
    }

    // Returns food item from db based on given ID
    public Product getItemFromID (int ID){
        return this.executeSelect(String.format("SELECT ID, barcode, product_name FROM food_table where ID = %s;", ID));
    }

    // Closes DB connection
    public void closeDB () throws SQLException {
        this.conn.close();
        System.out.println("Connection Closed Successfully");
    }

}
