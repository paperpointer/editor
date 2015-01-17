package yk.myengine.optiseq;

import java.util.ArrayList;

/**
 * Created by: Yuri Kravchik
 * Date: 8/11/2007
 * Time: 18:23:37
 */
public class ObjectMachine {
    private ArrayList<AbstractState> currentState = new ArrayList<AbstractState>();

    public void execute(ArrayList<AbstractState> newState) {
        AbstractState current;
        AbstractState newS;
        for (int i = 0; i < currentState.size(); i++) {
            current = currentState.get(i);
            newS = newState.get(i);
            if (current != newS) {
                current.disable();
                newS.enable();
                currentState.set(i, newS);
            }
        }
    }
}
