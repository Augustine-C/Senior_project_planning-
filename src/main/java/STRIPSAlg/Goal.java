package STRIPSAlg;

public class Goal {
    public String goal;
    public boolean isMultiPartGoal;
    public boolean isAction;
    public String[] goalParts;

    public Goal(String goal, boolean isMultiPartGoal, boolean isAction){
        this.goal = goal;
        this.isMultiPartGoal = isMultiPartGoal;
        this.isAction = isAction;
        goalParts = goal.split(" ");
    }

    @Override
    public String toString() {
        return goal + "\n";
    }
}
