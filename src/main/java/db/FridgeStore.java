package db;


import java.util.ArrayList;
import java.util.List;

public class FridgeStore {

    List<FoodProduct> products;

    public FridgeStore() {
        products = new ArrayList<>();
    }

    public FridgeStore(List<FoodProduct> products) {
        this.products = products;
    }


    public void setProducts(List<FoodProduct> products) {
        this.products = products;
    }

    public List<FoodProduct> getProducts() {
        return products;
    }
}
