public class Product {
    public int ID;
    public String barcode;
    public String product_name;
    public boolean isQuantifiable;

    public Product (int ID, String barcode, String product_name, boolean isQuantifiable) {
        this.ID = ID;
        this.barcode = barcode;
        this.product_name = product_name;
        this.isQuantifiable = isQuantifiable;
    }

    @Override
    public String toString () {
        return this.product_name;
    }
}
