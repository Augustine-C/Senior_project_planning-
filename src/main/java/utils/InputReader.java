package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputReader {

    private static List<String> readNames() {
        return new ArrayList<>(Arrays.asList(
                "Easy Condition",
                "Do Nothing",
                "Not Possible",
                "Paper Example",
                "Sussman Anomaly"
        ));
    }

    private static List<String[]> readInitStates() {
        return new ArrayList<>(Arrays.asList(
                new String[]{"ON_TABLE(a)", "ON_TABLE(b)", "CLEAR(a)", "CLEAR(b)", "ARM_EMPTY"}, // easyInitialState
                new String[]{"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"}, // doNothingInitialState
                new String[]{"ON_TABLE(a)", "ON_TABLE(b)", "CLEAR(a)", "ARM_EMPTY"}, // notPossibleInitialState
                new String[]{"ON_TABLE(a)", "ON_TABLE(b)", "ON(c,a)", "CLEAR(b)", "CLEAR(c)", "ARM_EMPTY"}, // paperExampleInitialState
                new String[]{"ON_TABLE(a)", "ON_TABLE(b)", "ON(c,a)", "CLEAR(b)", "CLEAR(c)", "ARM_EMPTY"} // sussmanAnomalyInitialState
        ));
    }

    private static List<String[]> readGoalStates() {

        return new ArrayList<>(Arrays.asList(
                new String[]{"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"}, // easyGoalState
                new String[]{"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"}, // doNothingGoalState
                new String[]{"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"}, // notPossibleGoalState
                new String[]{"ON_TABLE(b)", "CLEAR(a)", "ON(c,b)", "ON(a,c)", "ARM_EMPTY"}, // paperExampleGoalState
                new String[]{"ON_TABLE(c)", "CLEAR(a)", "ON(b,c)", "ON(a,b)", "ARM_EMPTY"} // sussmanAnomalyGoalState
        ));
    }

    public static List<String> readNames(String inputFormat) {
        List<String> namesList = new ArrayList<>();
        switch (inputFormat) {
            case "":
            case "d":
            case "default":
                return readNames();
            case "C":
            case "c":
                throw new UnsupportedOperationException();
        }
        return namesList;
    }

    public static List<String[]> readInitStates(String inputFormat) {
        List<String[]> initialStatesList = new ArrayList<>();
        switch (inputFormat) {
            case "":
            case "d":
            case "default":
                return readInitStates();
            case "C":
            case "c":
                throw new UnsupportedOperationException();
        }
        return initialStatesList;
    }

    public static List<String[]> readGoalStates(String inputFormat) {
        List<String[]> goalStatesList = new ArrayList<>();
        switch (inputFormat) {
            case "":
            case "d":
            case "default":
                return readGoalStates();
            case "C":
            case "c":
                throw new UnsupportedOperationException();
        }
        return goalStatesList;
    }
}
