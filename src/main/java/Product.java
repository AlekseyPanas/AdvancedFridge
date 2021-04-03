public class Product {
    public int ID;
    public String barcode;
    public String product_name;

    public Product (int ID, String barcode, String product_name) {
        this.ID = ID;
        this.barcode = barcode;
        this.product_name = product_name;
    }

    @Override
    public String toString () {
        return this.product_name;
    }
}
