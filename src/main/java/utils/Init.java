package utils;

import STRIPSAlg.Action;
import STRIPSAlg.Condition;
import STRIPSAlg.Goal;

import java.util.LinkedList;
import java.util.Stack;
import utils.Enums.GoalType;

public class Init {

    public static LinkedList<Action> initializeAllActions() {
        LinkedList<Action> allActions = new LinkedList<>();

        Action stack = new Action("STACK(top,bottom)", "CLEAR(bottom) HOLDING(top)", "ARM_EMPTY ON(top,bottom) CLEAR(top)");
        allActions.add(stack);

        Action unstack = new Action("UNSTACK(top,bottom)", "ON(top,bottom) CLEAR(top) ARM_EMPTY", "clear(bottom) HOLDING(top)");
        allActions.add(unstack);

        Action pickUp = new Action("PICK_UP(item)", "CLEAR(item) ON_TABLE(item) ARM_EMPTY", "HOLDING(item)");
        allActions.add(pickUp);

        Action putDown = new Action("PUT_DOWN(item)", "HOLDING(item)", "ON_TABLE(item) CLEAR(item) ARM_EMPTY");
        allActions.add(putDown);

        return allActions;
    }

    public static LinkedList<Condition> initializeCurrentStateWith(String[] states) {
        LinkedList<Condition> currentState = new LinkedList<>();
        for (String state : states) {
            currentState.add(new Condition(state));
        }
        return currentState;
    }

    public static Stack<Goal> initializeGoalStackWith(String[] goalStackArr) {
        //put multi part goal on stack
        Stack<Goal> goalStack = new Stack<>();

        //put single part goals on stack
        for (String s : goalStackArr) {
            goalStack.add(new Goal(s, GoalType.SinglePartGoal));
        }
        return goalStack;
    }

    public static LinkedList<Condition> initializeGoalStateWith(String[] goalStackArr) {
        LinkedList<Condition> goalState = new LinkedList<>();
        for (String s : goalStackArr) {
            goalState.add(new Condition(s));
        }
        return goalState;
    }
}
