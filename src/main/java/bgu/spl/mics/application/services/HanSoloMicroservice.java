package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.*;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private Ewoks ewoks = Ewoks.getInstance();
    private AtomicInteger totalAttacks;

    public HanSoloMicroservice() {
        super("Han");
        /*
        Recording the total attacks that have been completed in total by all attack handlers by incrementing while finish each
         */
        this.totalAttacks=Diary.getInstance().getTotalAttacks();
    }

    @Override
    protected void initialize() {
        // initialize of HanSolo is similar to Initialize of C3PO
        subscribeEvent(AttackEvent.class, callback -> {
            // how to react to attack event
            List<Integer> EwoksForAttack = callback.getSerials();
            EwoksForAttack.sort(Integer::compareTo);// sorting serials for preventing deadlock case [1,2] [2,1]
            // acquiring the required Ewoks in the Event for Attack
            for (int i = 0; i < EwoksForAttack.size(); i++) {
                ewoks.acquireEwok(EwoksForAttack.get(i));
            }
            try {
                Thread.sleep(callback.getDuration()); // Attack!!!
                complete(callback, true); // true if succeed to Attack without Interruption
            } catch (InterruptedException e) {
                e.printStackTrace();
                complete(callback, false); // false if the thread interrupted when attacked (in fact doesnt Matter the result and there arent interruption in our implementation)
            }
            // releasing the required Ewoks in the Event from Attack
            for (int i = 0; i < EwoksForAttack.size(); i++) {
                ewoks.releaseEwok(EwoksForAttack.get(i));
            }
            totalAttacks.incrementAndGet(); // increment the attack that performed
        });

        // how to react when Leia send that there are no more attacks
        subscribeBroadcast(NoMoreAttackBroadcast.class, callback -> {
            // Updating HanSolo Finishing time
            Diary.getInstance().SetTimeDetail(HanSoloFinish,System.currentTimeMillis());
            /*
            condition to send Deactivation Event-> all the attacks done
            and the Deactivation Event hadn't already been sent (which both are checked atomically)
            Only one Attacker Microservice send this Event (implemented by using CompareAndSet atomic Method)
            */
            if (callback.getNumberOfAttacks() == totalAttacks.get() && callback.getIsSentDeactivationEvent().compareAndSet(false, true)) {
                // inform R2D2 to Deactivate
                Future<Boolean> DeactivationFuture= sendEvent(new DeactivationEvent());
                //informing Lando to prepare for Bombing after R2D2 Deactivation (by the Future)
                // (because R2D2 cant talk...)
                sendEvent(new BombDestroyerEvent(DeactivationFuture));
            }
        });

        // how to react to terminate broadcast
        subscribeBroadcast(TerminateBroadcast.class, callback -> {
            //Updating HanSolo Terminate Time
            Diary.getInstance().SetTimeDetail(HanSoloTerminate,System.currentTimeMillis());
            terminate();
        });
        Main.threadInitCounter.countDown(); // by using countdown informing leia that HanSolo initialized
    }

}