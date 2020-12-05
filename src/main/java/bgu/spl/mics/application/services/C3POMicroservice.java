package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.NoMoreAttackBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


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
        subscribeEvent(AttackEvent.class, c-> {
            List<Integer> EwoksForAttack = c.getSerials();
            for (int i=0;i<EwoksForAttack.size(); i++) {ewoks.acquireEwok(EwoksForAttack.get(i)); }
            try {
                Thread.sleep(c.getDuration());
                complete(c,true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i=0;i<EwoksForAttack.size(); i++) {ewoks.releaseEwok(EwoksForAttack.get(i));
            }

            totalAttacks.incrementAndGet();
        });
        subscribeBroadcast(NoMoreAttackBroadcast.class, c->{
            if (c.getNumberOfAttacks()==totalAttacks.get()&&c.getIsSendedDeactivationEvent().compareAndSet(false,true)){
                // TODO : need to wait R2D2 had initialized
                sendEvent(new DeactivationEvent());
            }
            //here to update Finishing
        } );
        subscribeBroadcast(TerminateBroadcast.class, c->{
            //here to update terminate
            terminate();
        });
        Initilized.countDown();
    }
}

