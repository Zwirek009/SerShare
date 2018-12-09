package customer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FoodPlan implements Serializable {

    private Map<LocalDate, List<FoodPlanPosition>> plan;

    public FoodPlan(Map<LocalDate, List<FoodPlanPosition>> plan) {
        this.plan = plan;
    }

    public List<FoodPlanPosition> getPositions(LocalDate date) {
        return plan.get(date);
    }

    public Set<LocalDate> getDates() { return plan.keySet(); }

    public List<FoodPlanPosition> getPositionsBeforeDay(LocalDate date) {
        return plan.entrySet().stream()
            .filter(d -> d.getKey().isBefore(date))
            .flatMap(d -> d.getValue().stream())
            .collect(Collectors.toList());
    }

    public void deletePositionsToDay(LocalDate date) {
        this.plan =  plan.entrySet().stream()
            .filter(d -> !d.getKey().isBefore(date))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deletePositions(LocalDate date) {
        plan.remove(date);
    }

    public void addPositions(LocalDate date, List<FoodPlanPosition> positions) {
        List<FoodPlanPosition> posForDate = plan.get(date);
        if (posForDate != null)
            posForDate.addAll(positions);
        else
            plan.put(date, positions);
    }

    public void addPosition(LocalDate date, FoodPlanPosition position) {
        List<FoodPlanPosition> posForDate = plan.get(date);
        if (posForDate != null)
            posForDate.add(position);
        else {
            plan.put(date, new ArrayList<>());
            plan.get(date).add(position);
        }
    }

    public void deletePosition(LocalDate date, FoodPlanPosition position) {
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