package STRIPSAlg;

import utils.Enums.ElementType;

public class Condition extends Element {

//    public String nameOfActionOrState;  // If the Condition records an action, this will be the name of that action
//                                        // Example: "PICK_UP", "STACK", "UNSTACK", "PUT_DOWN"
//                                        // Otherwise, it will record the name of the State Description
//                                        // Example: "HOLDING", "ON_TABLE", "ON", "ARM_EMPTY"
//    public String multiConString;
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
