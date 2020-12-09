package utils;

public class Enums {
    public enum OutputFormat {
        TXT,
        PRINT_ONLY,
        JSON,
        XML
    }
    public enum ElementType {
        Action,
        MultiPartGoal,
        SinglePartGoal
    }
    public enum ActionPlanState {
        NORMAL,
        UNSOLVABLE,
        STUCK,
        DO_NOTHING
    }
}
