package stripsAlg;

import utils.enums.ElementType;

import java.util.ArrayList;

public class MultiCondition extends Element {

    private final ArrayList<Condition> condList;
    private final String[] conditions;

    public MultiCondition(String stateString) { // ON(C,B)
        super();
        condList = new ArrayList<>();
        conditions = stateString.split(" ");
        super.setElementType(ElementType.MULTI_PART_CONDITION);

        for (String condition : conditions) {
            condList.add(new Condition(condition));
        }
    }

    public ArrayList<Condition> getConditions() {
        return condList;
    }

    @Override
    public String toString() {
        return String.join(" ", conditions);
    }
}
