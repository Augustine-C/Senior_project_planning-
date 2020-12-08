package STRIPSAlg;

import utils.Enums.GoalType;

public class Goal {
    public String goal;
    public GoalType goalType;
    private String nameOfGoal;
    private String[] items;

    public Goal(String goal, GoalType goalType){
        this.goal = goal;
        this.goalType = goalType;
        int openBracket = goal.indexOf('(');
        int closeBracket = goal.indexOf(')');

        if (openBracket != -1) {
            nameOfGoal = goal.substring(0, openBracket);
            items = goal.substring(openBracket + 1, closeBracket).split(",");
        } else {
            nameOfGoal = goal;
        }
    }

    public String[] getGoalParts(){
        return goal.split(" ");
    }

    @Override
    public String toString() {
        return goal + "\n";
    }

    public String getNameOfGoal() {
        return nameOfGoal;
    }

    public String[] getItems() {
        return items;
    }
}
