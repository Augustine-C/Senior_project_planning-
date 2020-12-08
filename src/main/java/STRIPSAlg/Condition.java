package STRIPSAlg;

import utils.Enums.ElementType;

public class Condition extends Element {

    private String[] conditions;

    public Condition(String stateString) { // ON(C,B)
        super();
        conditions = stateString.split(" ");

        if (conditions.length > 1) { // Multi Part
//            this.multiConString = stateString;
            super.setElementType(ElementType.MultiPartGoal);
        }
        else if (conditions.length == 1){
            super.parseStateString(stateString);
            super.setElementType(ElementType.SinglePartGoal);
        } else {
            throw new IllegalStateException("[Condition Initializer] ");
        }
    }

    public String[] getConditions() {
        return conditions;
    }

    @Override
    public String toString() {
        if (super.elementType == ElementType.MultiPartGoal){
            return String.join(" ", conditions);
        } else {
            return super.toString();
        }
    }


}
