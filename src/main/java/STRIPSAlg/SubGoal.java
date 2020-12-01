package STRIPSAlg;

public class SubGoal {
    public String goal;
    public boolean isMultiPartGoal;
    public boolean isAction;

    public SubGoal(String goal, boolean isMultiPartGoal, boolean isAction){
        this.goal = goal;
        this.isMultiPartGoal = isMultiPartGoal;
        this.isAction = isAction;
    }

    @Override
    public String toString() {
        return goal + "\n";
    }
}
