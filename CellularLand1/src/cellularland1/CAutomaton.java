
package cellularland1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Encapsulates a cellular automaton, mainly its transition function and
 * the initial position. Also assigns the rules numbers in order to set the color
 * according to the rule used.
 *
 * @author Deas
 */
public class CAutomaton {
    /** The initial position of the automaton. */
    public final boolean[][] initPosition;
    /** The rules for survival and birth: */
    public final ArrayList<Integer> S,B;
    
    /** Numbers assigned to rules are stored here: */
    private final ArrayList<Integer> assignedS, assignedB;
    
    public CAutomaton(boolean[][] initPosition, ArrayList<Integer> S, ArrayList<Integer> B) {
        this.S = S;
        this.B = B;
        this.initPosition = initPosition;
        
        // Now generate the numbers assigned to the rules randomly.
        int n = S.size() + B.size();
        ArrayList<Integer> arr = new ArrayList<>();
        for(int i = 0; i < n; i++) arr.add(i);
        Collections.shuffle(arr);
        
        assignedS = new ArrayList<>();
        assignedB = new ArrayList<>();
        for(int i = 0; i < S.size(); i++) assignedS.add(arr.get(i));
        for(int i = S.size(); i < n; i++) assignedB.add(arr.get(i));
    }
    /**
     * Will a cell with the given number of living cells around survive?
     * @param alive Number of cells around the cell that are alive.
     * @return Returns -1 if it dies, number of the rule otherwise.
     */
    public int willSurvive(int alive) {
        if(!S.contains(alive)) return -1;
        else {
            return assignedS.get(S.indexOf(alive));
        }
    }
    /**
     * Will a cell with the given number of living cells around be born?
     * @param alive Number of cells around the cell that are alive.
     * @return Returns -1 if it remains dead, number of the rule otherwise.
     */
    public int willBeBorn(int alive) {
        if(!B.contains(alive)) return -1;
        else {
            return assignedB.get(B.indexOf(alive));
        }    
    }
    /** Returns SORTED char array of the rules for survival. */
    public char[] getS() {
        char[] arr = new char[S.size()];
        for(int i = 0; i < S.size(); i++) {
            arr[i] = (char)('0' + S.get(i));
        }
        Arrays.sort(arr);
        return arr;
    }
    /** Returns SORTED char array of the rules for birth. */
    public char[] getB() {
        char[] arr = new char[B.size()];
        for(int i = 0; i < B.size(); i++) {
            arr[i] = (char)('0' + B.get(i));
        }
        Arrays.sort(arr);
        return arr;
    }
}
