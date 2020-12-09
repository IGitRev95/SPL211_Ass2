package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

import java.util.concurrent.atomic.AtomicBoolean;
/**
 * Signals attack event handlers that theres no more attacks
 * and give them a shared resource used for coordinate triggering after attacks sequences (Deactivation)
 */
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
