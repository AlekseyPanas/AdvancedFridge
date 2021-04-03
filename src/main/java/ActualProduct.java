import java.time.LocalDate;

public class ActualProduct{
    public int ID;
    public String barcode;
    public String product_name;
    public LocalDate expiration;

    public ActualProduct (int ID, String barcode, String product_name, LocalDate expire) {
        this.ID = ID;
        this.barcode = barcode;
        this.product_name = product_name;
        this.expiration = expire;
    }
}
