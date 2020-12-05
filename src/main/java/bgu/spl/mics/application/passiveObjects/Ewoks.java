package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
// this class implemented as a thread-safe singleton that constructed with parameter of NumberOfEwoks
    // instruction- first use init with the rellavent number of Ewoks and just then use getInstance to get the instance
public class Ewoks {
    private final Ewok[] EwoksCollection;
    private static Ewoks singleton= null;
    private Ewoks(int NumberOfEwoks){
        EwoksCollection= new Ewok[NumberOfEwoks];
        for (int i=0;i<EwoksCollection.length;i++) EwoksCollection[i]=new Ewok(i);
    }
public synchronized static Ewoks init(int NumberOfEwoks){
        if (singleton!=null)throw new AssertionError("you allready initialized");
        singleton=new Ewoks(NumberOfEwoks);
        return singleton;
     }

    public static Ewoks getInstance(){
        if (singleton==null) throw new AssertionError("have to call init first");
    return singleton;
    }
    // need to be public
    public void acquireEwok(int serial){
        EwoksCollection[serial].acquire();
    }
    //need to be public
    public void releaseEwok(int serial){
        if(EwoksCollection[serial]==null||EwoksCollection[serial].available)
            throw new IllegalArgumentException("null or already relesed");
        EwoksCollection[serial].release();

    }


}
