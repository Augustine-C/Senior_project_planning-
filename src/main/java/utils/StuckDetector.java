package utils;

import stripsAlg.Action;
import stripsAlg.Element;
import utils.enums.ElementType;

import java.util.*;

public class StuckDetector {

    private final LinkedList<String[]> goalStackLog = new LinkedList<>();
    private static Map<String, String> undoActionMap = new HashMap<String, String>() {{
        put("STACK", "UNSTACK");
        put("UNSTACK", "STACK");
        put("PICK_UP", "PUT_DOWN");
        put("PUT_DOWN", "PICK_UP");
    }};

    public StuckDetector(){
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

    public boolean detectCycle(List<Action> currentPlan) {
        for (int i = 0; i < currentPlan.size() - 1; i++){
            Action currAction = currentPlan.get(i);
            String currActionName = currAction.getName();
            Action nextAction = currentPlan.get(i + 1);
            String nextActionName = nextAction.getName();
            if (Arrays.equals(currAction.getItems(), nextAction.getItems())){
                return nextActionName.equals(undoActionMap.get(currActionName));
            }
        }

        return false;
    }
}
