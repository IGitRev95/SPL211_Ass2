package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.LandoTerminate;
import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.R2D2Terminate;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
private final long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration=duration;
    }

    @Override
    protected void initialize() {

       subscribeEvent(BombDestroyerEvent.class,callback-> {
           try {
               if (callback.getDeactivitionIsPerformed().get()){
               Thread.sleep(duration);
               complete(callback,true);}
           } catch (InterruptedException e) {
               e.printStackTrace();
               complete(callback,false);}

           sendBroadcast(new TerminateBroadcast());
           //here updating terminate
           Diary.getInstance().SetTimeDetail(LandoTerminate,System.currentTimeMillis());
           terminate();
       });
        Main.threadInitCounter.countDown();
    }
}
