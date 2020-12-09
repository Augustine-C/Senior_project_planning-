package STRIPSAlg;

import utils.*;
import utils.Exceptions.*;
import utils.Enums.ElementType;

import java.util.*;

public class STRIPS {

    private final Stack<Element> goalStack;  // The Goal Stack
    private final HashSet<Condition> currentState; // The current state we have
    private HashSet<Condition> goalState; // The goal state that we want to achieve
    private final List<Action> allActions; // The List that stored all the possible Actions
    private final List<Action> plan; // The action plan in Action type
    private final boolean printGoalStack;
    private int whileLoopCount = 0;
    private final LinkedList<String[]> goalStackLogs = new LinkedList<>();

    public STRIPS(String[] initStatesArr, String[] goalStateArr, Boolean printGoalStack) throws CompleteAtStartException {

        if (Arrays.equals(initStatesArr, goalStateArr)){
            throw new CompleteAtStartException();
        }

        this.plan = new LinkedList<>();
        this.goalState = new HashSet<>();

        this.allActions = Init.initializeAllActions();
        this.currentState = Init.initializeCurrentStateWith(initStatesArr);
        this.goalStack = Init.initializeGoalStackWith(goalStateArr);
        this.goalState = Init.initializeGoalStateWith(goalStateArr);
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
            step();
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

    public void step() throws UnsolvableException {
        Element nextElement = goalStack.peek();

        if (nextElement.elementType == ElementType.Action) {
            doAction(nextElement);
        } else if (isGoalFullyAchieved((Condition) nextElement)) { // It is either a single part goal or a multi part goal
            goalStack.pop();
        } else if (nextElement.elementType == ElementType.MultiPartGoal) {
            planMultiPartGoal((Condition) nextElement);
        } else if (nextElement.elementType == ElementType.SinglePartGoal){ // The goal is single part goal
            planSinglePartGoal((Condition) nextElement);
        } else {
            throw new IllegalStateException("[STRIPS.attemptNextGoal] The goal is not an Action, not a SinglePartGoal, also not a MultiPartGoal");
        }

        if (printGoalStack){
            System.out.printf("While Loop Count: %d%n", whileLoopCount);
            System.out.println(goalStack);
            whileLoopCount++;
        }
    }

    private void doAction(Element element) {
        // This method will convert the plan using Action type to plan using String type
        for (Action action : plan) {
            if ((element.toString().equalsIgnoreCase(action.toString()))) {
                // Remove the action from the Goal Stack
                goalStack.pop();
                // Perform the action and update current state
                updateCurrentState(action);
                break;
            }
        }
    }

    /**
     * This method will update the currentState after an action is performed
     * @param newAction
     */
    private void updateCurrentState(Action newAction) {
        for (int i = 0; i < newAction.preconditions.size(); i++) {
            for (Condition condition : this.currentState){
                if(newAction.preconditions.get(i).equals(condition)){
                    this.currentState.remove(condition);
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
            String[] blocks = precondition.getItems();
            for (int i = 0; blocks != null && i < blocks.length; i++) {
                if (blocks[i].equalsIgnoreCase(target)) {
                    blocks[i] = replacement;
                    break;
                }
            }
        }

        // do action Results
        for (Condition result : action.results){
            String[] blocks = result.getItems();
            for (int i = 0; blocks != null && i < blocks.length; i++) {
                if (blocks[i].equalsIgnoreCase(target)) {
                    blocks[i] = replacement;
                    break;
                }
            }
        }

        // Action items
        String[] blocks = action.getItems();
        for (int i = 0; blocks != null && i < blocks.length; i++) {
            if (blocks[i].equalsIgnoreCase(target)) {
                blocks[i] = replacement;
                break;
            }
        }
    }

    /**
     * This method will help us plan the Goal with multiple parts
     * @param element
     */
    private void planMultiPartGoal(Condition element) {
        for (String goalPartNeedToDo : element.getConditions()){
            // If not all goals are achieved, we will store all parts of goals on Goal Stack
            // Each part of the goal is a single part goal
            goalStack.push(new Condition(goalPartNeedToDo));
        }
    }

    /**
     * This method will help us plan the Single Part Goal
     * @param element
     * @throws UnsolvableException
     */
    private void planSinglePartGoal(Condition element) throws UnsolvableException {

        // If we did not achieved the sub goal, we will find the best actions to achieve it.
        // After that, we will store it in the stack
        Action clone = findBestAction(element);
        plan.add(clone);
        pushPreconditionsToGoalStack(clone);
    }

    /**
     * This method can help us check whether a goal has been achieved under current state
     * @param element
     * @return
     */
    private boolean isGoalFullyAchieved(Condition element){
        if (element.elementType == ElementType.MultiPartGoal){
            String[] multiPartGoals = element.getConditions();
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
                        goalStack.push(new Condition(goalPartNeedToDo));
                    }
                    break;
                }
            }
            return !goalNotFullyAchieved;
        } else if (element.elementType == ElementType.SinglePartGoal) {
            boolean goalAchieved = false;

            for (Condition condition : currentState) {
                if (condition.toString().equalsIgnoreCase(element.toString())) {
                    // If we achieved our sub goal, we will remove it from the stack.
                    goalAchieved = true;
                    break;
                }
            }
            return goalAchieved;
        } else {
            throw new IllegalStateException("[STRIPS.isGoalFullyAchieved] The goal is nether a SinglePartGoal, nor a MultiPartGoal");
        }
    }

    /**
     * This method will push the preconditions of the given action to goal stack
     * @param action
     */
    private void pushPreconditionsToGoalStack(Action action) {
        // Store the Action in the GoalStack
        this.goalStack.push(action);

        // Store the multi part goals in the GoalStack
        this.goalStack.push(new Condition(action.getPreconditionString()));
    }

    /**
     * This is the Heuristic method that help us get the best action for the given state that we want to achieve
     * @param element
     * @return
     * @throws UnsolvableException
     */
    private Action findBestAction(Condition element) throws UnsolvableException {
        // Heuristic

        Action currentAction = null;

        String[] items = new String[2];

        if (element.getNameOfActionOrState().equalsIgnoreCase("HOLDING")) {
            // If we want to HOLDING an item, there are two actions we can take: UNSTACK or PICK_UP
            for (Condition condition : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("ON")
                        && condition.getItems()[0].equalsIgnoreCase(element.getItems()[0])) {
                    currentAction = getAction("UNSTACK");

                    items[0] = condition.getItems()[0];
                    items[1] = condition.getItems()[1];
                }
            }
            if (currentAction == null) {
                currentAction = getAction("PICK_UP");

                items[0] = element.getItems()[0];
            }
        } else if (element.getNameOfActionOrState().equalsIgnoreCase("ARM_EMPTY")) {
            // If we want to get our ARM_EMPTY, there are two actions we can take: STACK or PUT_DOWN
            // We will use the cloneActionHelper to determine which one is the best
            currentAction = findBestActionHelper(items);
        } else if (element.getNameOfActionOrState().equalsIgnoreCase("CLEAR")) {
            // If we want to get an item CLEAR, there are three actions we can take: STACK, UNSTACK, or PUT_DOWN
            boolean isHoldingItem = false;
            for (Condition condition : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("HOLDING")
                        && condition.getItems()[0].equalsIgnoreCase(element.getItems()[0])) {
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
                            && condition.getItems()[1].equalsIgnoreCase(element.getItems()[0])) {
                        currentAction = getAction("UNSTACK");

                        items[0] = condition.getItems()[0];
                        items[1] = condition.getItems()[1];

                        break;
                    }
                }
            }
        } else {
            // If we are not working on StateDescriptions, we will do an Action
            for(Action action : allActions){
                if (action.isGoalAchieved(element.getNameOfActionOrState())) {
                    currentAction = getAction(action.getName());

                    System.arraycopy(element.getItems(), 0, items, 0, element.getItems().length);
                    break;
                }
            }
        }

        if (currentAction == null){
            throw new UnsolvableException();
        }

        // replace block names using clones so that we can add it to the planWithActions list
        for (int j = 0; j < currentAction.getItems().length; j++) {
            replaceBlockNames(currentAction, currentAction.getItems()[j], items[j]);
        }

        return currentAction;
    }

    /**
     * This method will deal with the case when we need to achieve ARM_EMPTY or HOLDING goal
     * @param items
     * @return
     */
    private Action findBestActionHelper(String[] items) {
        Action action = null;
        for (Condition condition : goalState) {
            for (Condition value : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("ON")
                        && value.nameOfActionOrState.equalsIgnoreCase("HOLDING")
                        && condition.getItems()[0].equalsIgnoreCase(value.getItems()[0])) {
                    action = getAction("STACK");

                    items[0] = condition.getItems()[0];
                    items[1] = condition.getItems()[1];
                }
            }
        }
        if (action == null) {
            action = getAction("PUT_DOWN");

            for (Condition condition : currentState) {
                if (condition.nameOfActionOrState.equalsIgnoreCase("HOLDING")) {
                    items[0] = condition.getItems()[0];
                    break;
                }
            }
        }
        return action;
    }

    /**
     * Get a clone of the action from the allAction list
     * @param actionName the name of the action we want
     * @return a clone of the action with given action name from the allAction list
     */
    private Action getAction(String actionName) {
        for (Action action : allActions) {
            if (action.nameOfActionOrState.equalsIgnoreCase(actionName)) {
                return action.clone();
            }
        }
        throw new IllegalStateException("[STRIPS.getAction] No Such Action named: <" + actionName + ">");
    }
}
