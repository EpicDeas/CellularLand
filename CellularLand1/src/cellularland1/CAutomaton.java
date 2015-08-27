
package cellularland1;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Deas
 */
public class CAutomaton {
    public final boolean[][] initPosition;
    private final ArrayList<Integer> S,B;
    
    public CAutomaton(boolean[][] initPosition, ArrayList<Integer> S, ArrayList<Integer> B) {
        this.S = S;
        this.B = B;
        this.initPosition = initPosition;
    }
    
    public boolean willSurvive(int alive) {
        return S.contains(alive);
    }
    public boolean willBeBorn(int alive) {
        return B.contains(alive);
    }
    
    public char[] getS() {
        char[] arr = new char[S.size()];
        for(int i = 0; i < S.size(); i++) {
            arr[i] = (char)('0' + S.get(i));
        }
        Arrays.sort(arr);
        return arr;
    }
    public char[] getB() {
        char[] arr = new char[B.size()];
        for(int i = 0; i < B.size(); i++) {
            arr[i] = (char)('0' + B.get(i));
        }
        Arrays.sort(arr);
        return arr;
    }
}
