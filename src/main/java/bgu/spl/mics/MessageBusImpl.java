package bgu.spl.mics;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import bgu.spl.mics.application.services.R2D2Microservice;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> MessegeQueses= new ConcurrentHashMap<>();

        private static class Singleton{
        	private static MessageBusImpl instance= new MessageBusImpl();
		}

        public static MessageBusImpl getInstance() {
           return Singleton.instance;
        }


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {

	}

	@Override
	public void sendBroadcast(Broadcast b) {

	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {

		return null;
	}

	@Override
	public void register(MicroService m) {

	}

	@Override
	public void unregister(MicroService m) {
    // will not be checked
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		return null;
	}



}