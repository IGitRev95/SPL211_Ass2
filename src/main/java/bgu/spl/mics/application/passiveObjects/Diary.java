package bgu.spl.mics.application.passiveObjects;


import java.sql.Timestamp;
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
    private static long initime=System.currentTimeMillis();
    private AtomicInteger totalAttacks = new AtomicInteger(0);
    private Map<TimeDetailOf,Long> TimeDetails= new LinkedHashMap<>();

    public void SetTimeDetail(TimeDetailOf Name,long Time){
    TimeDetails.putIfAbsent(Name,Time-initime);
    }
    public Long getTimeOF(TimeDetailOf Type){
    return TimeDetails.getOrDefault(Type, null);
    }
    private void SetTotalAttacks(int totalAttacks){
        this.totalAttacks.set(totalAttacks);
    }
    public AtomicInteger getTotalAttacks(){return totalAttacks;}


/* TODO: maybe not needed
public void GenerateOutputJson(){
    // making Output json here with the all the names
    // iterate the map like this , its will keep the order they inserted
    for (Map.Entry<TimeDetailOf,Long> entry: TimeDetails.entrySet()){
        String type= entry.getKey().toString();
        Long Time= entry.getValue();

    }
    }
  */

    public static Diary getInstance() {
        return Diary.SingletonHolder.instance;
    }

}
