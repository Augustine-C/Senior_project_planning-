import STRIPSAlg.STRIPS;
import utils.InputReader;
import utils.ResultWriter;
import utils.ResultWriter.OutputFormat;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> names = InputReader.readNames("");
        List<String[]> initialStates = InputReader.readInitStates("");
        List<String[]> goalStates = InputReader.readGoalStates("");

        for (int i = 0; i < names.size(); i++){
            String name = names.get(i);
            String[] initialState = initialStates.get(i);
            String[] goalState = goalStates.get(i);

            STRIPS strips = new STRIPS(initialState, goalState);
            List<String> plan = strips.getPlanWithString();

            ResultWriter.writeResult(plan, name, initialState, goalState, OutputFormat.PRINT_ONLY);
        }

    }
}
