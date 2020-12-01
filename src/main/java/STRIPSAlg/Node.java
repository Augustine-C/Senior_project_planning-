package STRIPSAlg;

import java.util.ArrayList;

public class Node {

    public StackElement actionName;
    public ArrayList<StackElement> preconditions = new ArrayList<>();
    public ArrayList<StackElement> doResults = new ArrayList<>();
    public ArrayList<StackElement> undoResults = new ArrayList<>();


    public Node(String name, String preconditions, String doResults, String undoResults) {
        // TODO: undo result is the same as preconditions
        actionName = new StackElement(name);

        for (String precondition : preconditions.split(" ")){
            this.preconditions.add(new StackElement(precondition));
        }

        for (String doResult: doResults.split(" ")) {
            this.doResults.add(new StackElement(doResult));
        }

        for (String undoResult: undoResults.split(" ")) {
            this.undoResults.add(new StackElement(undoResult));
        }
    }

    public boolean isGoalAchieved(String goal) {
        boolean goalAchieved = false;

        for (int i = 0; i < doResults.size(); i++) {
            if(doResults.get(i).nameOfActionOrState.equalsIgnoreCase(goal)){
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

        ArrayList<String> doResultsStringList = new ArrayList<>();
        for (StackElement doResult : this.doResults){
            doResultsStringList.add(doResult.toString());
        }

        ArrayList<String> undoResultsStringList = new ArrayList<>();
        for (StackElement undoResult : this.undoResults){
            undoResultsStringList.add(undoResult.toString());
        }

        return new Node(
                actionName.toString(),
                String.join(" ", preconditionsStringList),
                String.join(" ", doResultsStringList),
                String.join(" ", undoResultsStringList)
        );
    }

    @Override
    public String toString() {
        return ("action: " + actionName
                + "\n  preconditions: " + preconditions
                + "\n  doResults:          " + doResults
                + "\n  undoResults:       " + undoResults);
    }
}
