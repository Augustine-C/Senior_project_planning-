package STRIPSAlg;

import java.util.LinkedList;
import java.util.Stack;

public class Init {

    public static LinkedList<Node> initializeActionList() {
        LinkedList<Node> nodeList = new LinkedList<>();

        Node stack = new Node("STACK(top,bottom)", "CLEAR(bottom) HOLDING(top)", "ARM_EMPTY ON(top,bottom) CLEAR(top)", "CLEAR(bottom) HOLDING(top)");
        nodeList.add(stack);

        Node unstack = new Node("UNSTACK(top,bottom)", "ON(top,bottom) CLEAR(top) ARM_EMPTY", "clear(bottom) HOLDING(top)", "ON(top,bottom) CLEAR(top) ARM_EMPTY");
        nodeList.add(unstack);

        Node pickUp = new Node("PICK_UP(item)", "CLEAR(item) ON_TABLE(item) ARM_EMPTY", "HOLDING(item)", "CLEAR(item) ON_TABLE(item) ARM_EMPTY");
        nodeList.add(pickUp);

        Node putDown = new Node("PUT_DOWN(item)", "HOLDING(item)", "ON_TABLE(item) CLEAR(item) ARM_EMPTY", "HOLDING(item)");
        nodeList.add(putDown);

        return nodeList;
    }

    public static LinkedList<StackElement> initializeCurrentStateWith(String[] states) {
        LinkedList<StackElement> currentState = new LinkedList<>();
        for (int i = 0; i < states.length; i++) {
            currentState.add(new StackElement(states[i]));
        }
        return currentState;
    }

    public static Stack<SubGoal> initializeGoalStackWith(String[] goalStackArr) {
        //put multi part goal on stack
        Stack<SubGoal> goalStack = new Stack<>();
        String goalStackStr = String.join(" ", goalStackArr);
        goalStack.push(new SubGoal(goalStackStr, true, false));

        //put single part goals on stack
        for (int i = 0; i < goalStackArr.length; i++) {
            goalStack.add(new SubGoal(goalStackArr[i], false, false));
        }
        return goalStack;
    }

    public static LinkedList<StackElement> initializeGoalStateWith(String[] goalStackArr) {
        LinkedList<StackElement> goalState = new LinkedList<>();
        for (int i = 0; i < goalStackArr.length; i++) {
            goalState.add(new StackElement(goalStackArr[i]));
        }
        return goalState;
    }
}
