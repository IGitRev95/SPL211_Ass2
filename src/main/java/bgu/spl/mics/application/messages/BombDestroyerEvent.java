package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

/**
 * this Event is sent to Lando to ask him use Bomb
 * holding the future of the deactivation event used to confirm full execution
 * of the Deactivation Event
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
