
package cellularland1;

import java.util.ArrayList;

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
    
    public String getS() {
        String Sstr = "";
        for(Integer i : S) {
            Sstr += i;
        }
        return Sstr;
    }
    public String getB() {
        String Bstr = "";
        for(Integer i : B) {
            Bstr += i;
        }
        return Bstr;
    }
}
