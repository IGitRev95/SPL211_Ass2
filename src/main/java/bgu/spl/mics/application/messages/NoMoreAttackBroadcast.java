package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

import java.util.concurrent.atomic.AtomicBoolean;

public class NoMoreAttackBroadcast implements Broadcast {
    private final int NumberOfAttacks;
    private final AtomicBoolean IsSentDeactivationEvent = new AtomicBoolean(false);

    public NoMoreAttackBroadcast (int NumberOfAttacks){
        this.NumberOfAttacks=NumberOfAttacks;
    }
    public int getNumberOfAttacks() {
        return NumberOfAttacks;
    }
    public AtomicBoolean getIsSentDeactivationEvent(){
        return IsSentDeactivationEvent;
    }
}
