package utils;

import java.util.List;

public class ResultWriter {
    public enum OutputFormat {
        TXT,
        PRINT_ONLY,
    }

    public static void writeResult(List<String> plan, String name, String[] initialStates, String[] goalStates, OutputFormat outputFileFormat){
        switch (outputFileFormat){
            case PRINT_ONLY:
                System.out.println();
                System.out.println("--------------------------");
                System.out.println(String.format("*******%s*******", name));
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
                System.out.println(String.join("\n", plan));
                System.out.println("--------------------------");
                System.out.println();
                break;
            case TXT:
                throw new UnsupportedOperationException("TXT file output is not supported yet!");
            default:
                break;

        }
    }

}
