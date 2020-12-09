package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

/**
 * for Unit Test purposes only
 */
public class SimpleEventForTest implements Event<Integer> {
    public int Number;

    public SimpleEventForTest(int Number){
        this.Number=Number;
    }

}
