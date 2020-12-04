package bgu.spl.mics;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	//mapping queues to micro services
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> microservicesMessageQueues = new ConcurrentHashMap<>();
	//mapping queues of message handlers to their message
	private ConcurrentHashMap<Class<? extends Message> , BlockingQueue<MicroService>> messagesHandlersQueues = new ConcurrentHashMap<>();
	//mapping Futures to their event
	private ConcurrentHashMap<Event<?>,Future<?>> eventFutureConcurrentHashMap = new ConcurrentHashMap<>();

	private static class SingletonHolder {
		private static MessageBusImpl instance= new MessageBusImpl();
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

		if(!microservicesMessageQueues.containsKey(m))
		{
			throw new NullPointerException("none registered microservice");
		}

		BlockingQueue<MicroService> eventTypeHandlersQueue = messagesHandlersQueues.putIfAbsent(type, new LinkedBlockingQueue<MicroService>());
		if (eventTypeHandlersQueue != null && !eventTypeHandlersQueue.contains(m)) {
			//no problem with sync cause only this microservice manipulates the MessageBus at all manners referring to itself
			eventTypeHandlersQueue.add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
	/*
	-checking if broadcast is already exists in the messagesHandlersQueues hash map
	-if it does insert the microservice to the message handler queue
	-else create a new queue for it and then insert the microservice to the message handler queue
	 */
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
	/*
	-checking if event exists in eventFutureConcurrentHashMap other wise throw an exception
	-updating the matching future (by using hash map) of the event with the given result
	 */
	}

	@Override
	public void sendBroadcast(Broadcast b) {
	/*
	-maybe syncronized which insures that all the currents gets and no body is added in the middle
	-get the queue if possible (otherwise exception or nothing) and check who is the current
	microservice for reference of completion of full circle of delivery to all the subscribers
	 */
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
	/*
	-get the queue if possible (otherwise exception or nothing) deliver to the next microservice
	-add to eventFutureConcurrentHashMap
	 ??? more thoughts about this one
	 */
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