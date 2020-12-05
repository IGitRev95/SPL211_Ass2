package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

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
    private CountDownLatch Initilized;
    AtomicInteger totalAttacks;

    public HanSoloMicroservice(CountDownLatch counter, AtomicInteger totalAttacks) {
        super("Han");
        Initilized = counter;
        this.totalAttacks = totalAttacks;
    }


    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, callback -> {
            // how to react to attack event
            List<Integer> EwoksForAttack = callback.getSerials();
            for (int i = 0; i < EwoksForAttack.size(); i++) {
                ewoks.acquireEwok(EwoksForAttack.get(i));
            }
            try {
                Thread.sleep(callback.getDuration());
                complete(callback, true);
            } catch (InterruptedException e) {
                e.printStackTrace();
                complete(callback, false);
            }
            for (int i = 0; i < EwoksForAttack.size(); i++) {
                ewoks.releaseEwok(EwoksForAttack.get(i));
            }
            totalAttacks.incrementAndGet();
        });
        // how to react when Leia send that there are no more attacks
        subscribeBroadcast(NoMoreAttackBroadcast.class, callback -> {

            if (callback.getNumberOfAttacks() == totalAttacks.get() && callback.getIsSendedDeactivationEvent().compareAndSet(false, true)) {
                // TODO : need to wait R2D2 had initialized
                sendEvent(new BombDestroyerEvent(sendEvent(new DeactivationEvent())));
            }
            //here to update Finishing

        });


        // how to react to terminate broadcast
        subscribeBroadcast(TerminateBroadcast.class, callback -> {
            //here to update terminate
            terminate();
        });
        Initilized.countDown();
    }

    private void updateFinishtime() {

    }
}