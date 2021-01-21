import java.time.LocalDate;

public class ActualProduct{
    int ID;
    String barcode;
    String product_name;
    LocalDate expiration;

    public ActualProduct (int ID, String barcode, String product_name, LocalDate expire) {
        this.ID = ID;
        this.barcode = barcode;
        this.product_name = product_name;
        this.expiration = expire;
    }
}
