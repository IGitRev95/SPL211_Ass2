package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;//should already know it? maybe needless
import bgu.spl.mics.application.passiveObjects.*;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.LeiaTerminate;

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

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class,c->{
            //Updating Leia Terminate Time
            Diary.getInstance().SetTimeDetail(LeiaTerminate,System.currentTimeMillis());
            terminate();});
        //Sending the Attacks
        for (int i = 0; i < attacks.length; i++) {
            sendEvent(new AttackEvent(attacks[i]));
        }
        //sending Broadcast to all attacker microservices (HanSolo, C3po) that all attack event sent
        //informing them that one of them should send Deactivation Event to R2D2
       sendBroadcast(new NoMoreAttackBroadcast(attacks.length));
    }
}