package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Attack;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NoMoreAttackBroadcast implements Broadcast {
    private final int NumberOfAttacks;
    private final AtomicBoolean IsSendedDeactivationEvent= new AtomicBoolean(false);

    public NoMoreAttackBroadcast (int NumberOfAttacks){
        this.NumberOfAttacks=NumberOfAttacks;
    }
    public int getNumberOfAttacks() {
        return NumberOfAttacks;
    }
    public AtomicBoolean getIsSendedDeactivationEvent(){
        return IsSendedDeactivationEvent;
    }
}
