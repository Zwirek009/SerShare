package customer;

import utils.ProductConstants;
import java.time.LocalDate;
import java.util.*;

public class FoodPlanManager {

    private FoodPlan getFoodPlansFromCustomer() {
        FoodPlanPosition pos1 = new FoodPlanPosition(ProductConstants.MILK, 1.5);
        FoodPlanPosition pos2 = new FoodPlanPosition(ProductConstants.BREAD, 0.5);
        List<FoodPlanPosition> positions = new ArrayList<>();
        positions.add(pos1);
        positions.add(pos2);

        Map<LocalDate, List<FoodPlanPosition>> plan = new HashMap<>();
        plan.put(LocalDate.of(2018, 12, 3), positions);

        return new FoodPlan(plan);
    }

    public FoodPlan getPlan() {
        return getFoodPlansFromCustomer();
    }

}
