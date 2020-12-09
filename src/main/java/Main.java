import STRIPSAlg.Action;
import STRIPSAlg.STRIPS;
import utils.*;
import utils.Exceptions.CompleteAtStartException;
import utils.Exceptions.StuckException;
import utils.Exceptions.UnsolvableException;
import utils.Enums.OutputFormat;
import utils.Enums.ActionPlanState;

import java.util.List;

public class Main {

    final static Boolean PRINT_GOAL_STACK = false;
    final static OutputFormat OUTPUT_FORMAT = OutputFormat.JSON;

    public static void main(String[] args) {
        List<String> names = InputReader.readNames("");
        List<String[]> initialStates = InputReader.readInitStates("");
        List<String[]> goalStates = InputReader.readGoalStates("");
        ResultWriter resultWriter = new ResultWriter(OUTPUT_FORMAT);

        for (int i = 0; i < names.size(); i++){
            String name = names.get(i);
            String[] initialState = initialStates.get(i);
            String[] goalState = goalStates.get(i);
            try {
                STRIPS strips = new STRIPS(initialState, goalState, PRINT_GOAL_STACK);
                List<Action> plan = strips.getPlan();
                resultWriter.writeResult(plan, name, initialState, goalState, ActionPlanState.NORMAL);
            } catch (UnsolvableException e){
                resultWriter.writeStringResult("UNSOLVABLE", name, initialState, goalState, ActionPlanState.UNSOLVABLE);
            } catch (StuckException e){
                resultWriter.writeStringResult("STRIPS_STUCK", name, initialState, goalState, ActionPlanState.STUCK);
            } catch (CompleteAtStartException e){
                resultWriter.writeStringResult("NOTHING_NEEDS_TO_BE_DONE", name, initialState, goalState, ActionPlanState.DO_NOTHING);
            }
        }

    }
}
