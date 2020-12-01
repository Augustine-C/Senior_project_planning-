package STRIPSAlg;

import java.util.*;

public class STRIPS {

    private Stack<SubGoal> goalStack;  // The Goal Stack
    private LinkedList<StackElement> currentState; // The current state we have
    private LinkedList<StackElement> goalState; // The goal state that we want to achieve
    private List<Action> actionList; // The List that stored all the possible Actions
    private List<Action> planWithAction; // The action plan in Action type
    private List<String> planWithString; // The action plan in String type
    private Boolean completedAtStart;
    private boolean notPossibleState;
    private boolean printGoalStack;
    private int whileLoopCount = 0;

    public STRIPS(String[] initStatesArr, String[] goalStackArr, Boolean printGoalStack) {
        this.planWithString = new LinkedList<>();
        this.planWithAction = new LinkedList<>();
        this.goalState = new LinkedList<>();

        this.actionList = Init.initializeActionList();
        this.currentState = Init.initializeCurrentStateWith(initStatesArr);
        this.goalStack = Init.initializeGoalStackWith(goalStackArr);
        this.goalState = Init.initializeGoalStateWith(goalStackArr);
        this.completedAtStart = Arrays.equals(initStatesArr, goalStackArr);
        this.notPossibleState = false;
        this.printGoalStack = printGoalStack;
    }

    public List<String> getPlanWithString() {
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
            if (notPossibleState){
                return new ArrayList<>(Arrays.asList("NOT_POSSIBLE"));
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

    public void attemptNextGoal() {
        SubGoal nextGoal = goalStack.peek();
        StackElement goal = new StackElement(nextGoal.goal);

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

    private void replacePrevActionWith(Action newAction) {

        // iterate over strings in delList and delete its occurence in currentState
        for (int i = 0; i < newAction.undoResults.size(); i++) {
            for (int j = 0; j < this.currentState.size(); j++) {
                if ((newAction.undoResults.get(i).toString()).equalsIgnoreCase(this.currentState.get(j).toString())) {
                    this.currentState.remove(j);
                    break;
                }
            }
        }

        // add from doResults of action in currentState
        for (int i = 0; i < newAction.doResults.size(); i++) {
            this.currentState.add(new StackElement(newAction.doResults.get(i).toString()));
        }
    }

    private void replaceBlockNames(Action action, String target, String replacement) {
        // replace all occurence in action target with replecemant in:

        // preconditions
        for (int i = 0; i < action.preconditions.size(); i++) {
            String[] blo = action.preconditions.get(i).items;
            for (int j = 0; blo != null && j < blo.length; j++) {
                if (blo[j].equalsIgnoreCase(target)) {
                    blo[j] = replacement;
                    break;
                }
            }
        }

        // do action Results
        for (int i = 0; i < action.doResults.size(); i++) {
            String[] blo = action.doResults.get(i).items;
            for (int j = 0; blo != null && j < blo.length; j++) {
                if (blo[j].equalsIgnoreCase(target)) {
                    blo[j] = replacement;
                    break;
                }
            }
        }

        // undo action results
        for (int i = 0; i < action.undoResults.size(); i++) {
            String[] blo = action.undoResults.get(i).items;
            for (int j = 0; blo != null && j < blo.length; j++) {
                if (blo[j].equalsIgnoreCase(target)) {
                    blo[j] = replacement;
                    break;
                }
            }
        }

        // actionName
        String[] blo = action.actionName.items;
        for (int j = 0; blo != null && j < blo.length; j++) {
            if (blo[j].equalsIgnoreCase(target)) {
                blo[j] = replacement;
                break;
            }
        }
    }

    private void generateActionPlanWithString(SubGoal subGoal, StackElement goal) {
        // This method will convert the plan using Action type to plan using String type
        for (int i = 0; i < planWithAction.size(); i++) {
            if ((goal.nameOfActionOrState).equalsIgnoreCase(planWithAction.get(i).actionName.nameOfActionOrState)
                    && (goal.items[0]).equalsIgnoreCase(planWithAction.get(i).actionName.items[0])) {
                // Get the action and store the result from either do that action or undo that action
                replacePrevActionWith(planWithAction.get(i));
                // Remove the action from the Goal Stack
                goalStack.pop();
                // Store the sub goal that achieved by the action to the plan and remove the sub goal from Goal Stack
                planWithString.add(subGoal.goal);
                // Remove the action from the list
                planWithAction.remove(i);
                break;
            }
        }
    }

    private void planMultiPartGoal(SubGoal subGoal) {
        String[] multiPartGoals = subGoal.goal.split(" ");
        boolean goalFullyAchieved = true;

        for (String goalPart : multiPartGoals){
            goalFullyAchieved = true;
            for (StackElement statePart : currentState){
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

    private void planSinglePartGoal(SubGoal subGoal, StackElement stackElement) {
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
            Action clone = findBestAction(stackElement);
            if (clone == null && notPossibleState){
                return;
            }
            planWithAction.add(clone);
            storeActionOnStack(clone);
        }
    }

    private void storeActionOnStack(Action clonedAction) {
        // Store the Action in the GoalStack
        this.goalStack.push(new SubGoal(clonedAction.actionName.toString(), false, true));

        // Store the Preconditions for an action in the GoalStack
        List<String> clonedPreconditionsStringList = new ArrayList<>();
        for (StackElement precondition : clonedAction.preconditions) {
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

    private Action findBestAction(StackElement goal) {
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
            this.notPossibleState = true;
            return null;
        }

        // make clone of action so that we can add it to the planWithAction list
        Action clonedAction = currentAction.clone();

        // replace block names using clones so that we can add it to the planWithAction list
        for (int j = 0; j < clonedAction.actionName.items.length; j++) {
            replaceBlockNames(clonedAction, clonedAction.actionName.items[j], items[j]);
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
            if (actionList.get(i).actionName.nameOfActionOrState.equalsIgnoreCase(actionName)) {
                return actionList.get(i);
            }
        }
        return null;
    }
}
