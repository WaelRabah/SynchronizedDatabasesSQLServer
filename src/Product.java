import java.util.Date;
import java.util.UUID;

public class Product {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product(String date, String region, String product, int qty, float cost, float amt, float tax, float total) {
        this.id=UUID.randomUUID().toString();
        this.date = date;
        this.region = region;
        this.product = product;
        this.qty = qty;
        this.cost = cost;
        this.amt = amt;
        this.tax = tax;
        this.total = total;
    }
    public Product(String id,String date, String region, String product, int qty, float cost, float amt, float tax, float total) {
        this.id=id;
        this.date = date;
        this.region = region;
        this.product = product;
        this.qty = qty;
        this.cost = cost;
        this.amt = amt;
        this.tax = tax;
        this.total = total;
    }
    private String id;
    private String date;
    private String region;
    private String product;
    private int qty;

    @Override
    public String toString() {
        return this.id+" "+this.date+" "+this.region+" "+this.product+" "+this.qty+" "+this.cost+" "+this.amt+" "+this.tax+" "+this.total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    private float cost;
    private float amt;
    private float tax;
    private float total;
}
