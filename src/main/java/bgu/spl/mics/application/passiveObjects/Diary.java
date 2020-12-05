package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;




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
    private LinkedHashMap<TimeDetailOf,Long> TimeDetails= new LinkedHashMap<>();
    private int TotalAttacks=0;


public void SetTimeDetail(TimeDetailOf Name,long Time){
    TimeDetails.put(Name,Time);
}
public Long getTimeOF(TimeDetailOf Type){
    return TimeDetails.getOrDefault(Type, null);
}
public void SetTotalAttacks(int totalAttacks){TotalAttacks=totalAttacks;}
public int  getTotalAttacks(){return TotalAttacks;}


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
