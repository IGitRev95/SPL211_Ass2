package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


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

    private AtomicInteger totalAttacks = new AtomicInteger(0);
    private Map<TimeDetailOf,Long> TimeDetails= new LinkedHashMap<>();

    public void SetTimeDetail(TimeDetailOf Name,long Time){
    TimeDetails.put(Name,Time);
    }
    public Long getTimeOF(TimeDetailOf Type){
    return TimeDetails.getOrDefault(Type, null);
    }
    private void SetTotalAttacks(int totalAttacks){
        this.totalAttacks.set(totalAttacks);
    }
    public AtomicInteger getTotalAttacks(){return totalAttacks;}

    public static Diary getInstance() {
        return Diary.SingletonHolder.instance;
    }

}
