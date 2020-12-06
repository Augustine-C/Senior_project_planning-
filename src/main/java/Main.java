import STRIPSAlg.STRIPS;
import utils.InputReader;
import utils.ResultWriter;
import utils.ResultWriter.OutputFormat;
import utils.StuckException;
import utils.UnsolvableException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    final static Boolean PRINT_GOAL_STACK = false;

    public static void main(String[] args) {
        List<String> names = InputReader.readNames("");
        List<String[]> initialStates = InputReader.readInitStates("");
        List<String[]> goalStates = InputReader.readGoalStates("");

        for (int i = 0; i < names.size(); i++){
            // TODO: Cannot solve Sussman Anomaly at current stage
            String name = names.get(i);
            String[] initialState = initialStates.get(i);
            String[] goalState = goalStates.get(i);

            STRIPS strips = new STRIPS(initialState, goalState, PRINT_GOAL_STACK);
            try {
                List<String> plan = strips.getPlanWithString();
                ResultWriter.writeResult(plan, name, initialState, goalState, OutputFormat.PRINT_ONLY);
            } catch (UnsolvableException e){
                ResultWriter.writeResult(new ArrayList<>(Arrays.asList("NOT_POSSIBLE")), name, initialState, goalState, OutputFormat.PRINT_ONLY);
                continue;
            } catch (StuckException e){
                ResultWriter.writeResult(new ArrayList<>(Arrays.asList("STRIPS_STUCK")), name, initialState, goalState, OutputFormat.PRINT_ONLY);
                continue;
            }
        }

    }
}
