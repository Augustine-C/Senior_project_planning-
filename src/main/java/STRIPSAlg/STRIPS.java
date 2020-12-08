package STRIPSAlg;

import utils.*;
import utils.Exceptions.*;

import java.util.*;

public class STRIPS {

    private final Stack<Goal> goalStack;  // The Goal Stack
    private final LinkedList<Condition> currentState; // The current state we have
    private LinkedList<Condition> goalState; // The goal state that we want to achieve
    private final List<Action> allActions; // The List that stored all the possible Actions
    private final List<Action> plan; // The action plan in Action type
    private final boolean printGoalStack;
    private int whileLoopCount = 0;
    private final LinkedList<String[]> goalStackLogs = new LinkedList<>();

    public STRIPS(String[] initStatesArr, String[] goalStackArr, Boolean printGoalStack) throws CompleteAtStartException {

        if (Arrays.equals(initStatesArr, goalStackArr)){
            throw new CompleteAtStartException();
        }

        this.plan = new LinkedList<>();
        this.goalState = new LinkedList<>();

        this.allActions = Init.initializeAllActions();
        this.currentState = Init.initializeCurrentStateWith(initStatesArr);
        this.goalStack = Init.initializeGoalStackWith(goalStackArr);
        this.goalState = Init.initializeGoalStateWith(goalStackArr);
        this.printGoalStack = printGoalStack;
    }

    public List<Action> getPlan() throws UnsolvableException, StuckException {
        if (printGoalStack){
            System.out.println();
            System.out.println("------------------------------");
            System.out.println("Debug Information - Goal Stack");
            System.out.println("------------------------------");
        }

        while (!goalStack.empty()) {
            attemptNextGoal();
            goalStackLogs.add(goalStack.toString().split(","));
            if (StuckDetector.detectCycle(goalStackLogs)){
                throw new StuckException();
            }
        }

        if (printGoalStack){
            System.out.println("-------------------------------------");
            System.out.println("End of Debug Information - Goal Stack");
            System.out.println("-------------------------------------");
            System.out.println();
        }
        return plan;
    }

    public void attemptNextGoal() throws UnsolvableException {
        Goal nextGoal = goalStack.peek();


        if (nextGoal.isAction) {
            doAction(nextGoal);
        }
        else if (isGoalFullyAchieved(nextGoal)) { // It is either a single part goal or a multi part goal
                goalStack.pop();
        } else if (nextGoal.isMultiPartGoal) {
            planMultiPartGoal(nextGoal);
        } else { // The goal is single part goal
            planSinglePartGoal(nextGoal);
        }

        if (printGoalStack){
            System.out.printf("While Loop Count: %d%n", whileLoopCount);
            System.out.println(goalStack);
            whileLoopCount++;
        }
    }

    private void doAction(Goal goal) {
        // This method will convert the plan using Action type to plan using String type
        for (Action action : plan) {
            if ((goal.goal.equalsIgnoreCase(action.action.toString()))) {
                // Remove the action from the Goal Stack
                goalStack.pop();
                // Perform the action and update current state
                updateCurrentState(action);
                break;
            }
        }
    }

    private void updateCurrentState(Action newAction) {
        for (int i = 0; i < newAction.preconditions.size(); i++) {
            for (int j = 0; j < this.currentState.size(); j++) {
                if(newAction.preconditions.get(i).equals(this.currentState.get(j))){
                    this.currentState.remove(j);
                    break;
                }
            }
        }

        // add from results of action in currentState
        for (int i = 0; i < newAction.results.size(); i++) {
            this.currentState.add(new Condition(newAction.results.get(i).toString()));
        }
    }

    private void replaceBlockNames(Action action, String target, String replacement) {
        // replace all occurrence in action target with replacement in:

        // preconditions
        for (Condition precondition : action.preconditions){
            String[] blocks = precondition.items;
            for (int i = 0; blocks != null && i < blocks.length; i++) {
                if (blocks[i].equalsIgnoreCase(target)) {
                    blocks[i] = replacement;
                    break;
                }
            }
        }

        // do action Results
        for (Condition result : action.results){
            String[] blocks = result.items;
            for (int i = 0; blocks != null && i < blocks.length; i++) {
                if (blocks[i].equalsIgnoreCase(target)) {
                    blocks[i] = replacement;
                    break;
                }
            }
        }

        // Action itself
        String[] blocks = action.action.items;
        for (int i = 0; blocks != null && i < blocks.length; i++) {
            if (blocks[i].equalsIgnoreCase(target)) {
                blocks[i] = replacement;
                break;
            }
        }
    }

    private void planMultiPartGoal(Goal goal) {

        for (String goalPartNeedToDo : goal.goalParts){
            // If not all goals are achieved, we will store all parts of goals on Goal Stack
            // Each part of the goal is a single part goal
            goalStack.push(new Goal(goalPartNeedToDo, false, false));
        }

    }

    private void planSinglePartGoal(Goal goal) throws UnsolvableException {

        // If we did not achieved the sub goal, we will find the best actions to achieve it.
        // After that, we will store it in the stack
        Action clone = findBestAction(new Condition(goal.goal));
        plan.add(clone);
        pushPreconditionsToGoalStack(clone);
    }

    private boolean isGoalFullyAchieved(Goal goal){
        if (goal.isMultiPartGoal){
            String[] multiPartGoals = goal.goalParts;
            boolean goalNotFullyAchieved = true;

            for (String goalPart : multiPartGoals){
                goalNotFullyAchieved = true;
                for (Condition statePart : currentState){
                    if (goalPart.equalsIgnoreCase(statePart.toString())) {
                        goalNotFullyAchieved = false;
                        break;
                    }
                }
                if (goalNotFullyAchieved) {
                    for (String goalPartNeedToDo : multiPartGoals){
                        // If not all goals are achieved, we will store all parts of goals on Goal Stack
                        // Each part of the goal is a single part goal
                        goalStack.push(new Goal(goalPartNeedToDo, false, false));
                    }
                    break;
                }
            }
            return !goalNotFullyAchieved;
        } else {
            boolean goalAchieved = false;

            for (Condition condition : currentState) {
                if (condition.toString().equalsIgnoreCase(goal.goal)) {
                    // If we achieved our sub goal, we will remove it from the stack.
                    goalAchieved = true;
                    break;
                }
            }
            return goalAchieved;
        }
    }

    private void pushPreconditionsToGoalStack(Action action) {
        // Store the Action in the GoalStack
        this.goalStack.push(new Goal(action.action.toString(), false, true));

        // Store the multi part goals in the GoalStack
        this.goalStack.push(new Goal(action.getPreconditionString(), true, false));

        // Store the single part goals in the GoalStack
        for (Condition precondition : action.preconditions) {
            goalStack.push(new Goal(precondition.toString(), false, false));
        }
    }

    private Action findBestAction(Condition goal) throws UnsolvableException {
        // Heuristic

        Action currentAction = null;

        String[] items = new String[2];

        if (goal.nameOfActionOrState.equalsIgnoreCase("HOLDING")) {
            // If we want to HOLDING an item, there are two actions we can take: UNSTACK or PICK_UP
            for (Condition condition : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("ON")
                        && condition.items[0].equalsIgnoreCase(goal.items[0])) {
                    currentAction = getAction("UNSTACK");

                    items[0] = condition.items[0];
                    items[1] = condition.items[1];
                }
            }
            if (currentAction == null) {
                currentAction = getAction("PICK_UP");

                items[0] = goal.items[0];
            }
        } else if (goal.nameOfActionOrState.equalsIgnoreCase("ARM_EMPTY")) {
            // If we want to get our ARM_EMPTY, there are two actions we can take: STACK or PUT_DOWN
            // We will use the cloneActionHelper to determine which one is the best
            currentAction = findBestActionHelper(items);
        } else if (goal.nameOfActionOrState.equalsIgnoreCase("CLEAR")) {
            // If we want to get an item CLEAR, there are three actions we can take: STACK, UNSTACK, or PUT_DOWN
            boolean isHoldingItem = false;
            for (Condition condition : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("HOLDING")
                        && condition.items[0].equalsIgnoreCase(goal.items[0])) {
                    isHoldingItem = true;
                    break;
                }
            }

            if (isHoldingItem) {
                // If we are HOLDING an item now, we need to put it down some way
                // We will use the cloneActionHelper to determine which way is the best
                currentAction = findBestActionHelper(items);
            } else {
                // If we are not HOLDING any item. Alternatively, we have our ARM_EMPTY
                for (Condition condition : currentState) {
                    if (condition.nameOfActionOrState.equalsIgnoreCase("ON")
                            && condition.items[1].equalsIgnoreCase(goal.items[0])) {
                        currentAction = getAction("UNSTACK");

                        items[0] = condition.items[0];
                        items[1] = condition.items[1];

                        break;
                    }
                }
            }
        } else {
            // If we are not working on StateDescriptions, we will do an Action
            for(Action action : allActions){
                if (action.isGoalAchieved(goal.nameOfActionOrState)) {
                    currentAction = getAction(action.getName());

                    System.arraycopy(goal.items, 0, items, 0, goal.items.length);
                    break;
                }
            }
        }

        if (currentAction == null){
            throw new UnsolvableException();
        }

        // replace block names using clones so that we can add it to the planWithActions list
        for (int j = 0; j < currentAction.action.items.length; j++) {
            replaceBlockNames(currentAction, currentAction.action.items[j], items[j]);
        }

        return currentAction;
    }

    // Deal with the case when we need to achieve ARM_EMPTY or HOLDING
    private Action findBestActionHelper(String[] items) {
        Action action = null;
        for (Condition condition : goalState) {
            for (Condition value : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("ON")
                        && value.nameOfActionOrState.equalsIgnoreCase("HOLDING")
                        && condition.items[0].equalsIgnoreCase(value.items[0])) {
                    action = getAction("STACK");

                    items[0] = condition.items[0];
                    items[1] = condition.items[1];
                }
            }
        }
        if (action == null) {
            action = getAction("PUT_DOWN");

            for (Condition condition : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("HOLDING")) {
                    items[0] = condition.items[0];
                    break;
                }
            }
        }
        return action;
    }

    private Action getAction(String actionName) {
        for (Action allAction : allActions) {
            if (allAction.action.nameOfActionOrState.equalsIgnoreCase(actionName)) {
                return allAction.clone();
            }
        }
        return null;
    }
}
