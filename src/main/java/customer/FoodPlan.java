package customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FoodPlan {

    private Map<Date, List<FoodPlanPosition>> plan;

    public FoodPlan(Map<Date, List<FoodPlanPosition>> plan) {
        this.plan = plan;
    }

    public List<FoodPlanPosition> getPositions(Date date) {
        return plan.get(date);
    }

    public void deletePositions(Date date) {
        plan.remove(date);
    }

    public void addPositions(Date date, List<FoodPlanPosition> positions) {
        List<FoodPlanPosition> posForDate = plan.get(date);
        if (posForDate != null)
            posForDate.addAll(positions);
        else
            plan.put(date, positions);
    }

    public void addPosition(Date date, FoodPlanPosition position) {
        List<FoodPlanPosition> posForDate = plan.get(date);
        if (posForDate != null)
            posForDate.add(position);
        else {
            plan.put(date, new ArrayList<>());
            plan.get(date).add(position);
        }
    }

    public void deletePosition(Date date, FoodPlanPosition position) {
        List<FoodPlanPosition> posForDate = plan.get(date);
        if (posForDate != null)
            posForDate.remove(position);
        //else
        //    new Exception("Próba usuniecia z planu nieistniejącej pozycji");
    }

    @Override
    public String toString() {
        return "FoodPlan{" +
                "plan=" + plan.toString() +
                '}';
    }
}