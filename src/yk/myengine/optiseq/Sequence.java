package yk.myengine.optiseq;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by: Yuri Kravchik Date: 30/10/2007 Time: 18:14:58
 */
public class Sequence extends AbstractState {
    private LinkedList<State> states = new LinkedList<State>();

    public void disable() {
        Iterator<State> i = states.descendingIterator();
        while (i.hasNext()) {
            i.next().disable();
        }
    }

    public void enable() {
        for (State state : states) {
            state.enable();
        }
    }

    public List<State> getStates() {
        return states;
    }

    @Override
    public void release() {
        for (State state : states) {
            state.release();
        }
    }
}
