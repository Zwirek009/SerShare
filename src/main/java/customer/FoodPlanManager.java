package customer;

import utils.ProductConstants;
import java.time.LocalDate;
import java.util.*;

public class FoodPlanManager {

    private FoodPlan getFoodPlansFromCustomer() {
        //String[] products = {ProductConstants.MILK, ProductConstants.BREAD};
        Random rand = new Random(System.currentTimeMillis());
        Map<LocalDate, List<FoodPlanPosition>> plan = new HashMap<>();

        for (int i = 0; i < rand.nextInt(5); ++i)
            plan.put(LocalDate.of(2019, 01, (rand.nextInt(10) + 20)), getFoodPlanPositions(rand));

        return new FoodPlan(plan);
    }

    private List<FoodPlanPosition> getFoodPlanPositions(Random rand) {
        FoodPlanPosition pos1 = new FoodPlanPosition(ProductConstants.MILK, (rand.nextInt(5) + 0.5));
        FoodPlanPosition pos2 = new FoodPlanPosition(ProductConstants.BREAD, (rand.nextInt(5) + 0.5));
        List<FoodPlanPosition> positions = new ArrayList<>();
        positions.add(pos1);
        positions.add(pos2);

        return positions;
    }

    public FoodPlan getPlan() {
        return getFoodPlansFromCustomer();
    }

}
