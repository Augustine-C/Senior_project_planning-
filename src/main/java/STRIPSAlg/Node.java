package STRIPSAlg;

import java.util.ArrayList;

public class Node {

    public StackElement actionName;
    public ArrayList<StackElement> preconditions = new ArrayList<>();
    public ArrayList<StackElement> results = new ArrayList<>();


    public Node(String name, String preconditions, String results) {
        // TODO: undo result is the same as preconditions
        actionName = new StackElement(name);

        for (String precondition : preconditions.split(" ")){
            this.preconditions.add(new StackElement(precondition));
        }

        for (String result: results.split(" ")) {
            this.results.add(new StackElement(result));
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
    public Node clone() {
        ArrayList<String> preconditionsStringList = new ArrayList<>();
        for (StackElement precondition : this.preconditions){
            preconditionsStringList.add(precondition.toString());
        }

        ArrayList<String> resultsStringList = new ArrayList<>();
        for (StackElement result : this.results){
            resultsStringList.add(result.toString());
        }

        return new Node(
                actionName.toString(),
                String.join(" ", preconditionsStringList),
                String.join(" ", resultsStringList)
        );
    }

    @Override
    public String toString() {
        return ("action: " + actionName
                + "\n  preconditions: " + preconditions
                + "\n  results:          " + results);
    }
}
