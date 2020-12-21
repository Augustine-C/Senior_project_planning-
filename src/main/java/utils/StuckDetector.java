package utils;

import stripsAlg.Action;
import stripsAlg.Condition;
import stripsAlg.Element;
import utils.enums.ElementType;

import java.util.*;

public class StuckDetector {

    private static int CYCLE_THRESHOLD;
    private final LinkedList<String[]> goalStackLog = new LinkedList<>();

    public StuckDetector(HashSet<Condition> state){
        int objectCount = getNumberOfObjects(state);
        CYCLE_THRESHOLD = objectCount * objectCount * objectCount;
    }

    private int getNumberOfObjects(HashSet<Condition> state) {
        HashSet<String> objectSet = new HashSet<>();
        for (Condition condition : state){
            if (condition.getItems() != null){
                objectSet.addAll(Arrays.asList(condition.getItems()));
            }
        }
        return objectSet.size();
    }

    public void logGoalStack(Stack<Element> goalStack){
        goalStackLog.add(getGoalStackStringArr(goalStack));
    }

    public static String[] getGoalStackStringArr(Stack<Element> goalStack){
        String[] goalStackStringArr = new String[goalStack.size()];
        for (int i = 0; i < goalStack.size(); i++){
            Element element = goalStack.elementAt(i);
            if (element.elementType == ElementType.ACTION){
                goalStackStringArr[i] = ((Action) element).getOutputString() + "\n";
            } else {
                goalStackStringArr[i] = element.toString() + "\n";
            }
        }
        return reverseArray(goalStackStringArr);
    }

    public static String[] reverseArray(String[] goalStackStringArr) {
        List<String> goalStackStringLst = Arrays.asList(goalStackStringArr);
        Collections.reverse(goalStackStringLst);
        return goalStackStringLst.toArray(goalStackStringArr);
    }

    public boolean detectCycle() {
        Iterator<String[]> itr = goalStackLog.iterator();
        Set<String[]> set = new HashSet<>();

        String[] curr;
        int cycleCount = 0;
        while (itr.hasNext()) {
            curr = itr.next();
            for (String[] element : set) {
                if (Arrays.equals(element, curr)) {
                    cycleCount++;
                }
                if (cycleCount > CYCLE_THRESHOLD) {
                    return true;
                }
            }
            set.add(curr);
        }

        return false;
    }
}
