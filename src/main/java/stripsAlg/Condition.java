package stripsAlg;

import utils.enums.ElementType;

public class Condition extends Element {

//    private final String[] conditions;

    public Condition(String stateString) { // ON(C,B)
        super();
//        String[] conditions = stateString.split(" ");

//        if (conditions.length > 1) { // Multi Part
//            this.multiConString = stateString;
//            throw new IllegalStateException("[Condition Initializer] Multi-part string in Single Condition");
//        }
//        else if (conditions.length == 1){
        super.parseStateString(stateString);
        super.setElementType(ElementType.SINGLE_PART_CONDITION);
//        } else {
//            throw new IllegalStateException("[Condition Initializer] ");
//        }
    }


}
