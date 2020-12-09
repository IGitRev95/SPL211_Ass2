package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import static bgu.spl.mics.application.passiveObjects.TimeDetailOf.LandoTerminate;


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
               // get the Future of Deactivation and wait to be resolved
               if (callback.getDeactivitionIsPerformed().get()){
               Thread.sleep(duration); //Bombing !!
               complete(callback,true);} // result true if succeed Bombing
           } catch (InterruptedException e) {
               e.printStackTrace();
               complete(callback,false);} // result false if interrupted by Bombing

           sendBroadcast(new TerminateBroadcast());
           //Updating Lando Terminate Time
           Diary.getInstance().SetTimeDetail(LandoTerminate,System.currentTimeMillis());
           terminate();
       });
        Main.threadInitCounter.countDown();// by using countdown informing leia that Lando initialized
    }
}
