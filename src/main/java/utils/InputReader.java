package utils;

import java.util.ArrayList;
import java.util.List;

public class InputReader {

    private static List<String[]> readInitStates() {
        List<String[]> initialStates = new ArrayList<>();

        String[] doNothingInitialState = {"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"};
        String[] notPossibleInitialState = {"ON_TABLE(a)", "ON_TABLE(b)", "CLEAR(a)", "ARM_EMPTY"};
        String[] easyInitialState = {"ON_TABLE(a)", "ON_TABLE(b)", "CLEAR(a)", "CLEAR(b)", "ARM_EMPTY"};
        String[] sussmanAnomalyInitialState = {"ON_TABLE(a)", "ON_TABLE(b)", "ON(c,a)", "CLEAR(b)", "CLEAR(c)", "ARM_EMPTY"};

        initialStates.add(doNothingInitialState);
        initialStates.add(notPossibleInitialState);
        initialStates.add(easyInitialState);
        initialStates.add(sussmanAnomalyInitialState);
        return initialStates;
    }

    private static List<String[]> readGoalStates() {
        List<String[]> goalStates = new ArrayList<>();

        String[] doNothingGoalState = {"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"};
        String[] notPossibleGoalState = {"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"};
        String[] easyGoalState = {"ON_TABLE(a)", "CLEAR(b)", "ON(b,a)", "ARM_EMPTY"};
        String[] sussmanAnomalyGoalState1 = {"ON_TABLE(b)", "CLEAR(a)", "ON(c,b)", "ON(a,c)", "ARM_EMPTY"};

        goalStates.add(doNothingGoalState);
        goalStates.add(notPossibleGoalState);
        goalStates.add(easyGoalState);
        goalStates.add(sussmanAnomalyGoalState1);
        return goalStates;
    }


    private static List<String> readNames() {
        List<String> names = new ArrayList<>();
        names.add("Do Nothing");
        names.add("Not Possible");
        names.add("Easy Condition");
        names.add("Sussman Anomaly");

        return names;

    }

    public static List<String> readNames(String inputFormat){
        List<String> namesList = new ArrayList<>();
        switch (inputFormat){
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

    public static List<String[]> readInitStates(String inputFormat){
        List<String[]> initialStatesList = new ArrayList<>();
        switch (inputFormat){
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

    public static List<String[]> readGoalStates(String inputFormat){
        List<String[]> goalStatesList = new ArrayList<>();
        switch (inputFormat){
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
