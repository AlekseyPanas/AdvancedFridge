import java.time.LocalDate;

public class ActualProduct extends Product{
    public LocalDate expiration;
    public int quantity;

    /*public ActualProduct (int ID, String barcode, String product_name, LocalDate expire) {
        this.ID = ID;
        this.barcode = barcode;
        this.product_name = product_name;
        this.expiration = expire;
        this.isQuantifiable = false;
        this.quantity = -1;
    }*/

    public ActualProduct (int ID, String barcode, String product_name, LocalDate expire, boolean isQuantifiable, int quantity) {
        super(ID, barcode, product_name, isQuantifiable);
        this.expiration = expire;
        this.quantity = quantity;
    }
}
