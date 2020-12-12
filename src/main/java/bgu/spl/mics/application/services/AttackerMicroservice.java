package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.TimeDetailOf;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.*;

public class AttackerMicroservice extends MicroService {
    private Ewoks ewoks= Ewoks.getInstance();
    private AtomicInteger totalAttacks;
    private TimeDetailOf finish=null;
    private TimeDetailOf terminate=null;

    public AttackerMicroservice(String name) {
        super(name);
        /*
        Recording the total attacks that have been completed in total by all attack handlers by incrementing while finish each
         */
        this.totalAttacks= Diary.getInstance().getTotalAttacks();
        switch (name){
            case "C3PO":
                finish=C3POFinish;
                terminate=C3POTerminate;
                break;
            case "Han":
                finish=HanSoloFinish;
                terminate=HanSoloTerminate;
                break;
            default: break;
        }
    }

    @Override
    protected void initialize() {
        // initialize of AttackerMicroservice
        subscribeEvent( AttackEvent.class, callback-> {
            // how to react to attack event
            List<Integer> EwoksForAttack = callback.getSerials();
            EwoksForAttack.sort( Integer::compareTo );// sorting serials for preventing deadlock case [1,2] [2,1]
            // acquiring the required Ewoks in the Event for Attack
            for (int ewokSerialIndx=0 ; ewokSerialIndx<EwoksForAttack.size() ; ewokSerialIndx++) { ewoks.acquireEwok( EwoksForAttack.get( ewokSerialIndx)); }
            try {
                Thread.sleep(callback.getDuration());// Attack!!!
                complete(callback,true);// true if succeed to Attack without Interruption
            } catch (InterruptedException exp) {
                exp.printStackTrace();
                complete( callback, false);// false if the thread interrupted when attacked (in fact doesnt Matter the result and there arent interruption in our implementation)
            }
            // releasing the required Ewoks in the Event from Attack
            for (int ewokSerialIndx=0 ; ewokSerialIndx<EwoksForAttack.size() ; ewokSerialIndx++) { ewoks.releaseEwok( EwoksForAttack.get(ewokSerialIndx));
            }

            totalAttacks.incrementAndGet();// increment the attack that performed
        });

        // how to react when Leia send that there are no more attacks
        subscribeBroadcast( NoMoreAttackBroadcast.class, callback->{
            // Updating Finishing time
            Diary.getInstance().SetTimeDetail( finish, System.currentTimeMillis());
            /*
            condition to send Deactivation Event-> all the attacks done
            and the Deactivation Event hadn't already been sent (which both are checked atomically)
            Only one Attacker Microservice send this Event (implemented by using CompareAndSet atomic Method)
            */
            if ( ( callback.getNumberOfAttacks() == totalAttacks.get() ) && callback.getIsSentDeactivationEvent().compareAndSet(false, true )){
                // inform R2D2 to Deactivate
                Future<Boolean> DeactivationFuture = sendEvent( new DeactivationEvent() );
                //informing Lando to prepare for Bombing after R2D2 Deactivation (by the Future)
                // (because R2D2 cant talk...)
                sendEvent( new BombDestroyerEvent( DeactivationFuture ));
            }
        });

        // how to react to terminate broadcast
        subscribeBroadcast( TerminateBroadcast.class, callback->{
            //Updating C3PO Terminate Time
            Diary.getInstance().SetTimeDetail( terminate, System.currentTimeMillis());
            terminate();
        });
        Main.threadInitCounter.countDown();// by using countdown informing leia that the Attacker initialized
    }
}
