package utils;

import java.util.*;

public class StuckDetector {

    private final static int CYCLE_THRESHOLD = 10;
    private final LinkedList<String[]> goalStackLog = new LinkedList<>();

    public void logGoalStack(String[] goalStack){
        goalStackLog.add(goalStack);
    }

    public boolean detectCycle() {
        Iterator<String[]> itr = goalStackLog.iterator();
        Set<String[]> set = new HashSet<>();

        String[] curr;
        int cycleCount = 0;
        while (itr.hasNext()) {
            curr = itr.next();
            for (String[] element : set) {
                if (Arrays.equals(element, curr)) {
                    cycleCount++;
                }
                if (cycleCount > CYCLE_THRESHOLD) {
                    return true;
                }
            }
            set.add(curr);
        }

        return false;
    }
}
