package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

/**
 * this Event is sended to Lando to ask him use Bomb
 */
public class BombDestroyerEvent implements Event<Boolean> {
    private Future<Boolean> DeactivitionIsPerformed;

    public BombDestroyerEvent(Future<Boolean> DeactivitionIsPerformed){
        this.DeactivitionIsPerformed=DeactivitionIsPerformed;
    }
    public Future<Boolean> getDeactivitionIsPerformed(){
        return DeactivitionIsPerformed;
    }
}
