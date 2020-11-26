package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class SimpleEventForTest implements Event<Integer> {
    public int Number;

    public SimpleEventForTest(int Number){
        this.Number=Number;
    }

}
