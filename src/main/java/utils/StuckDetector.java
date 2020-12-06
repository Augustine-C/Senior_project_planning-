package utils;

import java.util.*;

public class StuckDetector {

    private final static int CYCLE_THRESHOLD = 10;

    public static boolean detectCycle(LinkedList<String[]> head)
    {
        Iterator<String[]> itr = head.iterator();
        Set<String[]> set = new HashSet();

        String[] curr;
        int cycleCount = 0;
        while (itr.hasNext())
        {
            curr = itr.next();
            for (String[] element : set ){
                if (Arrays.equals(element, curr)){
                    cycleCount++;
                }
                if (cycleCount > CYCLE_THRESHOLD){
                    return true;
                }
            }
            set.add(curr);
        }

        return false;
    }
}
