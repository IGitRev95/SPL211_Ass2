package bgu.spl.mics.application.passiveObjects;


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
public class Ewoks {
    //ConcurrentHashMap<Integer,Ewok> EwoksCollection;
    private int size;

    public Ewoks(int numOfEwoks){
        size=numOfEwoks;
    }

}
