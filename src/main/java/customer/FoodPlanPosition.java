package customer;

import java.util.Date;

public class FoodPlanPosition {
    private String product;

    //1 unit is 1 liter for liquid products or 1kg for weighted products or 1 item for the rest
    private Double quantity;

    public FoodPlanPosition(String product, Double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public FoodPlanPosition(String product) {
        this.product = product;
        quantity = 1.0;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
