package stripsAlg;

import utils.enums.ElementType;

public class Condition extends Element {

    private final String[] conditions;

    public Condition(String stateString) { // ON(C,B)
        super();
        conditions = stateString.split(" ");

        if (conditions.length > 1) { // Multi Part
//            this.multiConString = stateString;
            super.setElementType(ElementType.MULTI_PART_CONDITION);
        }
        else if (conditions.length == 1){
            super.parseStateString(stateString);
            super.setElementType(ElementType.SINGLE_PART_CONDITION);
        } else {
            throw new IllegalStateException("[Condition Initializer] ");
        }
    }

    public String[] getConditions() {
        return conditions;
    }

    @Override
    public String toString() {
        if (super.elementType == ElementType.MULTI_PART_CONDITION){
            return String.join(" ", conditions);
        } else {
            return super.toString();
        }
    }


}
