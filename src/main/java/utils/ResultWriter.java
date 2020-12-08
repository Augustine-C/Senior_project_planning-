package utils;

import STRIPSAlg.Action;
import utils.Enums.OutputFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultWriter {

    public static void writeResult(List<Action> plan, String name, String[] initialStates, String[] goalStates, OutputFormat outputFileFormat){
        switch (outputFileFormat){
            case PRINT_ONLY:
                System.out.println();
                System.out.println("--------------------------");
                System.out.printf("*******%s*******%n", name);
                System.out.println("--------------------------");
                System.out.println("|     Initial State      |");
                System.out.println("--------------------------");
                for (String initialState : initialStates) {
                    System.out.println(initialState);
                }
                System.out.println("--------------------------");
                System.out.println("|       Goal State       |");
                System.out.println("--------------------------");
                for (String goalState : goalStates) {
                    System.out.println(goalState);
                }
                System.out.println("--------------------------");
                System.out.println("|    Plan of Actions     |");
                System.out.println("--------------------------");
                System.out.println(planToString(plan));
                System.out.println("--------------------------");
                System.out.println();
                break;
            case TXT:
                throw new UnsupportedOperationException("TXT file output is not supported yet!");
            default:
                break;

        }
    }

    private static String planToString(List<Action> plan){
        StringBuilder output = new StringBuilder();
        if (plan.isEmpty()){
            return output.toString();
        }
        for(Action action : plan){
            output.append(action.action.toString()).append("\n");
        }
        return output.substring(0, output.length()-1);
    }

    public static void writeStringResult(String result, String name, String[] initialStates, String[] goalStates, OutputFormat outputFileFormat){
        Action UnsolvableAction = new Action(result, "", "");
        writeResult(new ArrayList<>(Collections.singletonList(UnsolvableAction)), name, initialStates, goalStates, outputFileFormat);
    }

}
