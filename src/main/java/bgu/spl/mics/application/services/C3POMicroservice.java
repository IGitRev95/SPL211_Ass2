package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.*;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private Ewoks ewoks= Ewoks.getInstance();
    private CountDownLatch Initilized;
    AtomicInteger totalAttacks;

    public C3POMicroservice(CountDownLatch counter, AtomicInteger totalAttacks) {
        super("C3PO");
        Initilized=counter;
        this.totalAttacks=totalAttacks;
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, callback-> {
            List<Integer> EwoksForAttack = callback.getSerials();
            EwoksForAttack.sort(Integer::compareTo);// sorting serials for preventing deadlock case [1,2] [2,1]
            for (int i=0;i<EwoksForAttack.size(); i++) {ewoks.acquireEwok(EwoksForAttack.get(i)); }
            try {
                Thread.sleep(callback.getDuration());
                complete(callback,true);
            } catch (InterruptedException e) {
                e.printStackTrace();
                complete(callback, false);
            }
            for (int i=0;i<EwoksForAttack.size(); i++) {ewoks.releaseEwok(EwoksForAttack.get(i));
            }

            totalAttacks.incrementAndGet();
        });
        subscribeBroadcast(NoMoreAttackBroadcast.class, c->{
            if (c.getNumberOfAttacks()==totalAttacks.get()&&c.getIsSendedDeactivationEvent().compareAndSet(false,true)){
              // inform R2D2 to Deactivate
                Future<Boolean> DeactivionFuture= sendEvent(new DeactivationEvent());
                //informing Lando to prepare for Bombing after R2D2 Deactivition
                sendEvent(new BombDestroyerEvent(DeactivionFuture));
            }
            //here updating Finishing
            Diary.getInstance().SetTimeDetail(C3POFinish,System.currentTimeMillis());
        } );
        subscribeBroadcast(TerminateBroadcast.class, c->{
            //here updating terminate
            Diary.getInstance().SetTimeDetail(C3POTerminate,System.currentTimeMillis());
            terminate();
        });
        Initilized.countDown();
    }
}

