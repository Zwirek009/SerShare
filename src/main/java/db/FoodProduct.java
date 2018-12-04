package db;

public class FoodProduct {

    private String name;

    //1 unit is 1 liter for liquid products or 1kg for weighted products or 1 item for the rest
    private Double quantity;

    public FoodProduct(String name, Double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String product) {
        this.name = name;
    }
}
