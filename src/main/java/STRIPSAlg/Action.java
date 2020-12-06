package STRIPSAlg;

import java.util.ArrayList;

public class Action {

    public Condition action;
    public ArrayList<Condition> preconditions = new ArrayList<>();
    public ArrayList<Condition> results = new ArrayList<>();


    public Action(String name, String preconditions, String results) {
        // TODO: undo result is the same as preconditions
        action = new Condition(name);

        for (String precondition : preconditions.split(" ")){
            this.preconditions.add(new Condition(precondition));
        }

        for (String result: results.split(" ")) {
            this.results.add(new Condition(result));
        }
    }

    public boolean isGoalAchieved(String goal) {
        boolean goalAchieved = false;

        for (int i = 0; i < results.size(); i++) {
            if(results.get(i).nameOfActionOrState.equalsIgnoreCase(goal)){
                goalAchieved = true;
                break;
            }
        }

        return goalAchieved;
    }

    @Override
    public Action clone() {
        ArrayList<String> preconditionsStringList = new ArrayList<>();
        for (Condition precondition : this.preconditions){
            preconditionsStringList.add(precondition.toString());
        }

        ArrayList<String> resultsStringList = new ArrayList<>();
        for (Condition result : this.results){
            resultsStringList.add(result.toString());
        }

        return new Action(
                action.toString(),
                String.join(" ", preconditionsStringList),
                String.join(" ", resultsStringList)
        );
    }

    @Override
    public String toString() {
        return ("action: " + action
                + "\n  preconditions: " + preconditions
                + "\n  results:          " + results);
    }
}
