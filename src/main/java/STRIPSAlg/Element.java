package STRIPSAlg;

import utils.Enums.ElementType;

public class Element {
    public ElementType elementType;
    private String[] items;
    public String nameOfActionOrState;  // If the Element records an action, this will be the name of that action
                                        // Example: "PICK_UP", "STACK", "UNSTACK", "PUT_DOWN"
                                        // Otherwise, it will record the name of the State Description
                                        // Example: "HOLDING", "ON_TABLE", "ON", "ARM_EMPTY"

    public Element() {}

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
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

    public String getNameOfActionOrState() {
        return nameOfActionOrState;
    }

    public String[] getItems() {
        return items;
    }

    @Override
    public boolean equals(Object element){
        return this.toString().equalsIgnoreCase(element.toString());
    }

    protected void parseStateString(String name) {
        // "On(a,b)"
        int openBracket = name.indexOf('(');
        int closeBracket = name.indexOf(')');

        if (openBracket != -1) {
            this.nameOfActionOrState = name.substring(0, openBracket);
            items = name.substring(openBracket + 1, closeBracket).split(",");
        } else {
            this.nameOfActionOrState = name;
        }
    }
}
