package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.R2D2Deactivate;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
private final long duration;
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
     subscribeEvent(DeactivationEvent.class,callback-> {
         try {
             Thread.sleep(duration);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         //here to update finishing
         //-----------for example----------------
         Diary d=Diary.getInstance();
         d.SetTimeDetail(R2D2Deactivate,1L);
        // --------------------------------------
         complete(callback,true);

     });
        subscribeBroadcast(TerminateBroadcast.class, c->{
            //here to update terminate
            terminate();});
    }
}
