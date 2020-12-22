package utils;

import stripsAlg.Action;
import stripsAlg.STRIPS;
import utils.enums.PlannerState;
import utils.exceptions.CompleteAtStartException;
import utils.exceptions.StuckException;
import utils.exceptions.UnsolvableException;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class StripsRunner extends Thread {

    private final long timeoutThreshold;
    String name;
    String[] initialState;
    String[] goalState;
    Boolean printGoalStack;
    ResultWriter resultWriter;
    Boolean threadDone = false;

    public StripsRunner(String name, String[] initialState, String[] goalState, Boolean printGoalStack, ResultWriter resultWriter, long timeoutThreshold){
        this.name = name;
        this.initialState = initialState;
        this.goalState = goalState;
        this.printGoalStack = printGoalStack;
        this.resultWriter = resultWriter;
        this.timeoutThreshold = timeoutThreshold;
    }

    @Override
    public void run() {
        System.out.println("Thread for task " + this.name + " starts");
        try {
            STRIPS strips = new STRIPS(initialState, goalState, printGoalStack, timeoutThreshold);
            List<Action> plan = strips.getPlan();
            resultWriter.writeResult(plan, name, initialState, goalState, PlannerState.NORMAL);
        } catch (UnsolvableException e) {
            resultWriter.writeStringResult("UNSOLVABLE", name, initialState, goalState, PlannerState.UNSOLVABLE);
        } catch (StuckException e) {
            resultWriter.writeStringResult("STRIPS_STUCK", name, initialState, goalState, PlannerState.STUCK);
        } catch (CompleteAtStartException e) {
            resultWriter.writeStringResult("NOTHING_NEEDS_TO_BE_DONE", name, initialState, goalState, PlannerState.DO_NOTHING);
        } catch (TimeoutException e){
            resultWriter.writeStringResult("STRIPS_STUCK", name, initialState, goalState, PlannerState.STUCK);
        }
        System.out.println("Thread for task " + this.name + " ends");
        threadDone = true;
    }
}
