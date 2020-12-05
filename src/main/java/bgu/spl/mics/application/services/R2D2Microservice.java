package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.TimeDetailOf;

import java.util.concurrent.CountDownLatch;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.R2D2Deactivate;
import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.R2D2Terminate;

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
private CountDownLatch Initilized;
    public R2D2Microservice(long duration, CountDownLatch countdown) {
        super("R2D2");
        this.duration=duration;
        this.Initilized=countdown;
    }

    @Override
    protected void initialize() {
     subscribeEvent(DeactivationEvent.class,callback-> {
         try {
             Thread.sleep(duration);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }

         complete(callback,true);
         //here updating finishing
         Diary.getInstance().SetTimeDetail(R2D2Deactivate,System.currentTimeMillis());

     });
        subscribeBroadcast(TerminateBroadcast.class, c->{
            //here updating terminate
            Diary.getInstance().SetTimeDetail(R2D2Terminate,System.currentTimeMillis());
            terminate();} );
        Initilized.countDown();
    }

}
