package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;//should already know it? maybe needless
import bgu.spl.mics.application.passiveObjects.*;
import java.util.concurrent.CountDownLatch;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.LeiaTerminate;
import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.R2D2Terminate;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private final Attack[] attacks;
    private final CountDownLatch AttackersWereInitialized;

    public LeiaMicroservice(Attack[] attacks, CountDownLatch counter) {
        super("Leia");
        this.attacks = attacks;
        AttackersWereInitialized = counter;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class,c->{
            //here updating terminate
            Diary.getInstance().SetTimeDetail(LeiaTerminate,System.currentTimeMillis());
            terminate();});
        try {
            AttackersWereInitialized.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < attacks.length; i++) {
            sendEvent(new AttackEvent(attacks[i]));
        }
       sendBroadcast(new NoMoreAttackBroadcast(attacks.length));
    }
}