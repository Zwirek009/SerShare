package customer;

import java.util.*;

public class FoodPlanManager {

    private FoodPlan plan;

    public FoodPlanManager() {
        this.plan = getFoodPlansFromCutomer();
    }

    private FoodPlan getFoodPlansFromCutomer() {
        FoodPlanPosition pos1 = new FoodPlanPosition("Milk", 1.5);
        FoodPlanPosition pos2 = new FoodPlanPosition("Bread", 0.6);
        List<FoodPlanPosition> positions = new ArrayList<>();
        positions.add(pos1);
        positions.add(pos2);

        Map<Date, List<FoodPlanPosition>> plan = new HashMap<>();
        plan.put(new Date(2018, 12, 3), positions);

        return new FoodPlan(plan);
    }

    public FoodPlan getPlan() {
        return plan;
    }

    public void setPlan(FoodPlan plan) {
        this.plan = plan;
    }
}
