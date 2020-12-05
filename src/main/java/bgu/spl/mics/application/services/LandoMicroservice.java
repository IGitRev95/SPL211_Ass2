package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

import java.util.concurrent.CountDownLatch;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
private final long duration;
private CountDownLatch Initilized;
    public LandoMicroservice(long duration, CountDownLatch countdown) {
        super("Lando");
        this.duration=duration;
        this.Initilized=countdown;
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
           // here to updateTermiantion
           terminate();
       });
        Initilized.countDown();
    }
}
