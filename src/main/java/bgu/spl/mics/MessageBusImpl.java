package bgu.spl.mics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> MessegeQueses= new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<? extends Message> , ConcurrentLinkedQueue<MicroService>> Mes= new ConcurrentHashMap<>();
	private ConcurrentHashMap<Event,Future> M= new ConcurrentHashMap<>();

	private static class SingletonHolder {
		private static MessageBusImpl instance= new MessageBusImpl();
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
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