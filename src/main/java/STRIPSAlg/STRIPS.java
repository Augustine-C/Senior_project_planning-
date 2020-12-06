package STRIPSAlg;

import utils.Init;
import utils.StuckDetector;
import utils.StuckException;
import utils.UnsolvableException;

import java.util.*;

public class STRIPS {

    private Stack<SubGoal> goalStack;  // The Goal Stack
    private LinkedList<Condition> currentState; // The current state we have
    private LinkedList<Condition> goalState; // The goal state that we want to achieve
    private List<Action> actionList; // The List that stored all the possible Actions
    private List<Action> planWithActions; // The action plan in Action type
    private List<String> planWithString; // The action plan in String type
    private Boolean completedAtStart;
    private boolean printGoalStack;
    private int whileLoopCount = 0;
    private LinkedList<String[]> goalStackLogs = new LinkedList<>();

    public STRIPS(String[] initStatesArr, String[] goalStackArr, Boolean printGoalStack) {
        this.planWithString = new LinkedList<>();
        this.planWithActions = new LinkedList<>();
        this.goalState = new LinkedList<>();

        this.actionList = Init.initializeActionList();
        this.currentState = Init.initializeCurrentStateWith(initStatesArr);
        this.goalStack = Init.initializeGoalStackWith(goalStackArr);
        this.goalState = Init.initializeGoalStateWith(goalStackArr);
        this.completedAtStart = Arrays.equals(initStatesArr, goalStackArr);
        this.printGoalStack = printGoalStack;
    }

    public List<String> getPlanWithString() throws UnsolvableException, StuckException {
        if (completedAtStart){
            return new ArrayList<>();
        }
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
        return planWithString;
    }

    public void attemptNextGoal() throws UnsolvableException {
        SubGoal nextGoal = goalStack.peek();
        Condition goal = new Condition(nextGoal.goal);

        if (nextGoal.isAction) {
            generateActionPlanWithString(nextGoal, goal);
        } else if (nextGoal.isMultiPartGoal) {
            planMultiPartGoal(nextGoal);
        } else { // The goal is single part goal
            planSinglePartGoal(nextGoal, goal);
        }
        if (printGoalStack){
            System.out.println(String.format("While Loop Count: %d", whileLoopCount));
            System.out.println(goalStack);
            whileLoopCount++;
        }
    }

    private void doAction(Action newAction) {
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
        // replace all occurence in action target with replecemant in:

        // preconditions
        for (int i = 0; i < action.preconditions.size(); i++) {
            String[] blocks = action.preconditions.get(i).items;
            for (int j = 0; blocks != null && j < blocks.length; j++) {
                if (blocks[j].equalsIgnoreCase(target)) {
                    blocks[j] = replacement;
                    break;
                }
            }
        }

        // do action Results
        for (int i = 0; i < action.results.size(); i++) {
            String[] blocks = action.results.get(i).items;
            for (int j = 0; blocks != null && j < blocks.length; j++) {
                if (blocks[j].equalsIgnoreCase(target)) {
                    blocks[j] = replacement;
                    break;
                }
            }
        }

        String[] blocks = action.action.items;
        for (int j = 0; blocks != null && j < blocks.length; j++) {
            if (blocks[j].equalsIgnoreCase(target)) {
                blocks[j] = replacement;
                break;
            }
        }
    }

    private void generateActionPlanWithString(SubGoal subGoal, Condition goal) {
        // This method will convert the plan using Action type to plan using String type
        for (int i = 0; i < planWithActions.size(); i++) {
            if ((goal.equals(planWithActions.get(i).action))) {
                // Get the action and store the result from either do that action or undo that action
                doAction(planWithActions.get(i));
                // Remove the action from the Goal Stack
                goalStack.pop();
                // Store the sub goal that achieved by the action to the plan and remove the sub goal from Goal Stack
                planWithString.add(subGoal.goal);
                // Remove the action from the list
                planWithActions.remove(i);
                break;
            }
        }
    }

    private void planMultiPartGoal(SubGoal subGoal) {
        String[] multiPartGoals = subGoal.goal.split(" ");
        boolean goalFullyAchieved = true;

        for (String goalPart : multiPartGoals){
            goalFullyAchieved = true;
            for (Condition statePart : currentState){
                if (goalPart.equalsIgnoreCase(statePart.toString())) {
                    goalFullyAchieved = false;
                    break;
                }
            }
            if (goalFullyAchieved) {
                break;
            }
        }

        if (goalFullyAchieved) {
            // If not all goals are achieved, we will store all parts of goals on Goal Stack
            // Each part of the goal is a single part goal
            for (String goalPart : multiPartGoals){
                goalStack.push(new SubGoal(goalPart, false, false));
            }
        } else {
            // If all parts of that goal are achieved, remove the goal from GoalStack
            goalStack.pop();
        }
    }

    private void planSinglePartGoal(SubGoal subGoal, Condition condition) throws UnsolvableException {
        boolean subGoalRemoved = false;

        for (int i = 0; i < currentState.size(); i++) {
            if (currentState.get(i).toString().equalsIgnoreCase(subGoal.goal)) {
                // If we achieved our sub goal, we will remove it from the stack.
                goalStack.pop();
                subGoalRemoved = true;
                break;
            }
        }

        if (!subGoalRemoved) {
            // If we did not achieved the sub goal, we will find the best actions to achieve it.
            // After that, we will store it in the stack
            Action clone = findBestAction(condition);
            if (clone == null){
                return;
            }
            planWithActions.add(clone);
            storeActionOnStack(clone);
        }
    }

    private void storeActionOnStack(Action clonedAction) {
        // Store the Action in the GoalStack
        this.goalStack.push(new SubGoal(clonedAction.action.toString(), false, true));

        // Store the Preconditions for an action in the GoalStack
        List<String> clonedPreconditionsStringList = new ArrayList<>();
        for (Condition precondition : clonedAction.preconditions) {
            clonedPreconditionsStringList.add(precondition.toString());
        }
        String clonedPreconditionsString = String.join(" ", clonedPreconditionsStringList);

        // Store the multi part goals in the GoalStack
        this.goalStack.push(new SubGoal(clonedPreconditionsString, true, false));

        // Store the single part goals in the GoalStack
        for (int i = 0; i < clonedAction.preconditions.size(); i++) {
            goalStack.push(new SubGoal(clonedAction.preconditions.get(i).toString(), false, false));
        }
    }

    private Action findBestAction(Condition goal) throws UnsolvableException {
        Action currentAction = null;

        String[] items = new String[2];

        if (goal.nameOfActionOrState.equalsIgnoreCase("HOLDING")) {
            // If we want to HOLDING an item, there are two actions we can take: UNSTACK or PICK_UP
            for (int i = 0; i < currentState.size(); i++) {
                if (currentState.get(i).nameOfActionOrState.equalsIgnoreCase("ON")
                        && currentState.get(i).items[0].equalsIgnoreCase(goal.items[0])) {
                    currentAction = getAction("UNSTACK");

                    items[0] = currentState.get(i).items[0];
                    items[1] = currentState.get(i).items[1];
                }
            }
            if (currentAction == null) {
                currentAction = getAction("PICK_UP");

                items[0] = goal.items[0];
            }
        } else if (goal.nameOfActionOrState.equalsIgnoreCase("ARM_EMPTY")) {
            // If we want to get our ARM_EMPTY, there are two actions we can take: STACK or PUT_DOWN
            // We will use the cloneActionHelper to determine which one is the best
            currentAction = cloneActionHelper(null, items);
        } else if (goal.nameOfActionOrState.equalsIgnoreCase("CLEAR")) {
            // If we want to get an item CLEAR, there are three actions we can take: STACK, UNSTACK, or PUT_DOWN
            boolean isHoldingItem = false;
            for (int i = 0; i < currentState.size(); i++) {
                if (currentState.get(i).nameOfActionOrState.equalsIgnoreCase("HOLDING")
                        && currentState.get(i).items[0].equalsIgnoreCase(goal.items[0])) {
                    isHoldingItem = true;
                    break;
                }
            }

            if (isHoldingItem) {
                // If we are HOLDING an item now, we need to put it down some way
                // We will use the cloneActionHelper to determine which way is the best
                currentAction = cloneActionHelper(null, items);
            } else {
                // If we are not HOLDING any item. Alternatively, we have our ARM_EMPTY
                for (int i = 0; i < currentState.size(); i++) {
                    if (currentState.get(i).nameOfActionOrState.equalsIgnoreCase("ON")
                            && currentState.get(i).items[1].equalsIgnoreCase(goal.items[0])) {
                        currentAction = getAction("UNSTACK");

                        items[0] = currentState.get(i).items[0];
                        items[1] = currentState.get(i).items[1];

                        break;
                    }
                }
            }
        } else {
            // If we are not working on StateDescriptions, we will do an Action
            for (int i = 0; i < actionList.size(); i++) {
                if (actionList.get(i).isGoalAchieved(goal.nameOfActionOrState)) {
                    currentAction = actionList.get(i);

                    for (int j = 0; j < goal.items.length; j++) {
                        items[j] = goal.items[j];
                    }
                    break;
                }
            }
        }

        if (currentAction == null){
            throw new UnsolvableException();
        }

        // make clone of action so that we can add it to the planWithActions list
        Action clonedAction = currentAction.clone();

        // replace block names using clones so that we can add it to the planWithActions list
        for (int j = 0; j < clonedAction.action.items.length; j++) {
            replaceBlockNames(clonedAction, clonedAction.action.items[j], items[j]);
        }

        return clonedAction;
    }

    private Action cloneActionHelper(Action action, String[] items) {
        for (int i = 0; i < goalState.size(); i++) {
            for (int j = 0; j < currentState.size(); j++) {
                if (goalState.get(i).nameOfActionOrState.equalsIgnoreCase("ON")
                        && currentState.get(j).nameOfActionOrState.equalsIgnoreCase("HOLDING")
                        && goalState.get(i).items[0].equalsIgnoreCase(currentState.get(j).items[0])) {
                    action = getAction("STACK");

                    items[0] = goalState.get(i).items[0];
                    items[1] = goalState.get(i).items[1];
                }
            }
        }
        if (action == null) {
            action = getAction("PUT_DOWN");

            for (int i = 0; i < currentState.size(); i++) {
                if (currentState.get(i).nameOfActionOrState.equalsIgnoreCase("HOLDING")) {
                    items[0] = currentState.get(i).items[0];

                    break;
                }
            }
        }
        return action;
    }

    private Action getAction(String actionName) {
        for (int i = 0; i < actionList.size(); i++) {
            if (actionList.get(i).action.nameOfActionOrState.equalsIgnoreCase(actionName)) {
                return actionList.get(i);
            }
        }
        return null;
    }
}
