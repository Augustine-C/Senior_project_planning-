package utils;

import stripsAlg.Action;
import org.json.JSONObject;
import org.json.XML;
import utils.enums.PlannerState;
import utils.enums.OutputFormat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResultWriter {

    private final OutputFormat outputFormat;
    private Boolean isFirstOutput = true;
    private static FileWriter fileWriter;
    private final static String FOLDER_PATH = "./ResultOutput/";
    private final static String FILE_NAME = "ActionPlan";

    public ResultWriter(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void writeStringResult(String result, String name, String[] initialStates, String[] goalStates, PlannerState plannerState) {
        Action UnsolvableAction = new Action(result, "", "");
        writeResult(new ArrayList<>(Collections.singletonList(UnsolvableAction)), name, initialStates, goalStates, plannerState);
    }

    public void writeResult(List<Action> plan, String name, String[] initialStates, String[] goalStates, PlannerState plannerState) {
        if (outputFormat == OutputFormat.PRINT_ONLY) {
            writePrintOnly(plan, name, initialStates, goalStates);
        } else {
            writeFile(plan, name, initialStates, goalStates, plannerState, outputFormat);
        }
    }

    private void writePrintOnly(List<Action> plan, String name, String[] initialStates, String[] goalStates) {
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
    }

    private String planToString(List<Action> plan) {
        return String.join("\n", planToStringArr(plan));
    }

    private void writeFile(List<Action> plan, String name, String[] initialStates, String[] goalStates, PlannerState plannerState, OutputFormat outputFormat) {
        String filePath = FOLDER_PATH + FILE_NAME + "_" + name.replace(" ", "_");
        try {
            File directory = new File(FOLDER_PATH);

            if (isFirstOutput) {
                deleteDirectory(directory);
                isFirstOutput = false;
            }
            if (!directory.exists()) {
                boolean mkdirResult = directory.mkdir();
                if (!mkdirResult) {
                    throw new IOException("[ResultWriter.writeFile] Failed to create a new directory: " + directory.getAbsolutePath());
                }
            }
            switch (outputFormat) {
                case JSON:
                    fileWriter = new FileWriter(filePath + ".json");
                    fileWriter.write(getJSONOutput(plan, name, initialStates, goalStates, plannerState).toString(2));
                    break;
                case TXT:
                    fileWriter = new FileWriter(filePath + ".txt");
                    fileWriter.write(getTXTOutput(plan, name, initialStates, goalStates));
                    break;
                case XML:
                    fileWriter = new FileWriter(filePath + ".xml");
                    fileWriter.write(getXMLOutput(plan, name, initialStates, goalStates, plannerState));
                    break;
                default:
                    throw new UnsupportedOperationException("Output in this file type is not supported yet!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getXMLOutput(List<Action> plan, String name, String[] initialStates, String[] goalStates, PlannerState plannerState) {
        JSONObject jsonObject = getJSONOutput(plan, name, initialStates, goalStates, plannerState);
        return XML.toString(jsonObject);
    }

    private String getTXTOutput(List<Action> plan, String name, String[] initialStates, String[] goalStates) {
        System.out.println();

        System.out.println();
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("--------------------------");
        stringList.add(String.format("*******%s*******", name));
        stringList.add("--------------------------");
        stringList.add("|     Initial State      |");
        stringList.add("--------------------------");
        stringList.addAll(Arrays.asList(initialStates));
        stringList.add("--------------------------");
        stringList.add("|       Goal State       |");
        stringList.add("--------------------------");
        stringList.addAll(Arrays.asList(goalStates));
        stringList.add("--------------------------");
        stringList.add("|    Plan of Actions     |");
        stringList.add("--------------------------");
        stringList.add(planToString(plan));
        stringList.add("--------------------------");
        return String.join("\n", stringList);
    }

    private JSONObject getJSONOutput(List<Action> plan, String name, String[] initialStates, String[] goalStates, PlannerState plannerState) {
        JSONObject actionPlanJSON = new JSONObject();
        actionPlanJSON.put("name", name);
        actionPlanJSON.put("initialState", Arrays.asList(initialStates));
        actionPlanJSON.put("goalState", Arrays.asList(goalStates));

        JSONObject actionPlanStateJSON = new JSONObject();
        switch (plannerState) {
            case NORMAL:
                actionPlanJSON.put("plan", planToStringArr(plan));
                actionPlanStateJSON.put("unsolvable", false);
                actionPlanStateJSON.put("stuck", false);
                actionPlanStateJSON.put("doNothing", false);
                actionPlanStateJSON.put("normal", true);
                break;
            case UNSOLVABLE:
                actionPlanJSON.put("plan", new ArrayList<String>());
                actionPlanStateJSON.put("unsolvable", true);
                actionPlanStateJSON.put("stuck", false);
                actionPlanStateJSON.put("doNothing", false);
                actionPlanStateJSON.put("normal", false);
                break;
            case STUCK:
                actionPlanJSON.put("plan", new ArrayList<String>());
                actionPlanStateJSON.put("unsolvable", false);
                actionPlanStateJSON.put("stuck", true);
                actionPlanStateJSON.put("doNothing", false);
                actionPlanStateJSON.put("normal", false);
                break;
            case DO_NOTHING:
                actionPlanJSON.put("plan", new ArrayList<String>());
                actionPlanStateJSON.put("unsolvable", false);
                actionPlanStateJSON.put("stuck", false);
                actionPlanStateJSON.put("doNothing", true);
                actionPlanStateJSON.put("normal", false);
                break;
        }
        actionPlanJSON.put("state", actionPlanStateJSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ActionPlan", actionPlanJSON);
        return jsonObject;
    }

    private ArrayList<String> planToStringArr(List<Action> plan) {
        ArrayList<String> stringPlan = new ArrayList<>();
        for (Action action : plan) {
            stringPlan.add(action.getOutputString());
        }
        return stringPlan;
    }

    /**
     * This method will delete all the files and directories within the provided directory.
     * This will also delete the provided directory itself.
     *
     * @param directoryToBeDeleted the directory and all files under it to be removed
     */
    private static void deleteDirectory(File directoryToBeDeleted) throws IOException {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        boolean deleteResult = directoryToBeDeleted.delete();
        if (!deleteResult) {
            // throw new IOException("[ResultWriter.deleteDirectory] Failed to delete given directory: " + directoryToBeDeleted.getAbsolutePath());
            // TODO: 
        }
    }
}
