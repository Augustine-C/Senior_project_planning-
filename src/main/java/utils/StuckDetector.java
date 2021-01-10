package utils;

import stripsAlg.Action;
import stripsAlg.Element;
import utils.enums.ElementType;

import java.util.*;

public class StuckDetector {

    private final LinkedList<String[]> goalStackLog = new LinkedList<>();

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
            Action nextAction = currentPlan.get(i + 1);
            if (currAction.getName().equals("STACK") && nextAction.getName().equals("UNSTACK") && Arrays.equals(currAction.getItems(), nextAction.getItems())){
                return true;
            } else if (currAction.getName().equals("UNSTACK") && nextAction.getName().equals("STACK") && Arrays.equals(currAction.getItems(), nextAction.getItems())){
                return true;
            } else if (currAction.getName().equals("PICK_UP") && nextAction.getName().equals("PUT_DOWN") && Arrays.equals(currAction.getItems(), nextAction.getItems())){
                return true;
            } else if (currAction.getName().equals("PUT_DOWN") && nextAction.getName().equals("PICK_UP") && Arrays.equals(currAction.getItems(), nextAction.getItems())){
                return true;
            }
        }

        return false;
    }
}
