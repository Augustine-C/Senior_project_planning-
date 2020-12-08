import STRIPSAlg.Action;
import STRIPSAlg.STRIPS;
import utils.*;
import utils.Exceptions.CompleteAtStartException;
import utils.Exceptions.StuckException;
import utils.Exceptions.UnsolvableException;
import utils.Enums.OutputFormat;

import java.util.List;

public class Main {

    final static Boolean PRINT_GOAL_STACK = false;
    final static OutputFormat OUTPUT_FORMAT = OutputFormat.PRINT_ONLY;

    public static void main(String[] args) {
        List<String> names = InputReader.readNames("");
        List<String[]> initialStates = InputReader.readInitStates("");
        List<String[]> goalStates = InputReader.readGoalStates("");

        for (int i = 0; i < names.size(); i++){
            // TODO: Cannot solve Sussman Anomaly at current stage
            String name = names.get(i);
            String[] initialState = initialStates.get(i);
            String[] goalState = goalStates.get(i);

            try {
                STRIPS strips = new STRIPS(initialState, goalState, PRINT_GOAL_STACK);
                List<Action> plan = strips.getPlan();
                ResultWriter.writeResult(plan, name, initialState, goalState, OutputFormat.PRINT_ONLY);
            } catch (UnsolvableException e){
                ResultWriter.writeStringResult("UNSOLVABLE", name, initialState, goalState, OUTPUT_FORMAT);
            } catch (StuckException e){
                ResultWriter.writeStringResult("STRIPS_STUCK", name, initialState, goalState, OUTPUT_FORMAT);
            } catch (CompleteAtStartException e){
                ResultWriter.writeStringResult("NOTHING_NEEDS_TO_BE_DONE", name, initialState, goalState, OUTPUT_FORMAT);
            }
        }

    }
}
