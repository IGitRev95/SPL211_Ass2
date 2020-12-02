package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
private Ewoks ewoks= Ewoks.getInstance();
    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, c-> {
            List<Integer> EwoksForAttack= c.getSerials();
            for (int i=0;i<EwoksForAttack.size(); i++) {ewoks.acquireEwok(EwoksForAttack.get(i)); }
            try {
                Thread.sleep(c.getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i=0;i<EwoksForAttack.size(); i++) {ewoks.releaseEwok(EwoksForAttack.get(i));
        } complete(c,true);
        }
        );


    }
}