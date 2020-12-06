package STRIPSAlg;

import java.util.Arrays;

public class Condition {

    public String nameOfActionOrState;  // If the Condition records an action, this will be the name of that action
                                        // Example: "PICK_UP", "STACK", "UNSTACK", "PUT_DOWN"
                                        // Otherwise, it will record the name of the State Description
                                        // Example: "HOLDING", "ON_TABLE", "ON", "ARM_EMPTY"
    public String[] items;

    public Condition(String stateString) { // ON(C,B)
        int openBracket = stateString.indexOf('(');
        int closeBracket = stateString.indexOf(')');

        if (openBracket != -1) {
            nameOfActionOrState = stateString.substring(0, openBracket);
            items = stateString.substring(openBracket + 1, closeBracket).split(",");
        } else {
            nameOfActionOrState = stateString;
        }
    }

    @Override
    public String toString() {
        String stateDescString = nameOfActionOrState;

        if (items != null) {
            stateDescString += "(";
            stateDescString += String.join(",", items);
            stateDescString += ")";
        }

        return stateDescString;
    }

    @Override
    public boolean equals(Object element){
        return this.toString().equalsIgnoreCase(element.toString());
    }
}
