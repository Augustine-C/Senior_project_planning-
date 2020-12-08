package utils;

import STRIPSAlg.Action;
import STRIPSAlg.Condition;
import STRIPSAlg.Element;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

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

    public static HashSet<Condition> initializeCurrentStateWith(String[] states) {
        HashSet<Condition> currentState = new HashSet<>();
        for (String state : states) {
            currentState.add(new Condition(state));
        }
        return currentState;
    }

    public static Stack<Element> initializeGoalStackWith(String[] goalStackArr) {
        //put multi part goal on stack
        Stack<Element> elementStack = new Stack<>();

        //put single part goals on stack
        for (String s : goalStackArr) {
            elementStack.add(new Condition(s));
        }
        return elementStack;
    }

    public static HashSet<Condition> initializeGoalStateWith(String[] goalStackArr) {
        HashSet<Condition> goalState = new HashSet<>();
        for (String s : goalStackArr) {
            goalState.add(new Condition(s));
        }
        return goalState;
    }
}
