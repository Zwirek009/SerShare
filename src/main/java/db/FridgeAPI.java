package db;

import java.util.Arrays;

public class FridgeAPI {

    public FridgeStore getStore() {

        return new FridgeStore(Arrays.asList(new FoodProduct("Milk", 1.0),
                new FoodProduct("Bread", 2.0)));
    }
}
