package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private static class SingletonHolder {
        private static Diary instance= new Diary();
    }

    public static Diary getInstance() {
        return Diary.SingletonHolder.instance;
    }



}
