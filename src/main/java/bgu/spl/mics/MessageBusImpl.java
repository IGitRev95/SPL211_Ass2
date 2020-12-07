package bgu.spl.mics;
import bgu.spl.mics.application.messages.AttackEvent;

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
	private ConcurrentHashMap<Event<?>,Future> eventFutureConcurrentHashMap = new ConcurrentHashMap<>();

	private static class SingletonHolder {
		private static MessageBusImpl instance= new MessageBusImpl();
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}


	@Override
	//TODO: Test subscribeEvent - hadn't been tested
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

		subscribeMessage(type,m);
	}

	@Override
	//TODO: Test subscribeBroadcast - hadn't been tested
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

		subscribeMessage(type,m);
	}

	//TODO: Test subscribeMessage - hadn't been tested
	private void subscribeMessage(Class<? extends Message> type, MicroService m) {

		if(!microservicesMessageQueues.containsKey(m))
		{
			throw new NullPointerException("none registered microservice");
		}

		messagesHandlersQueues.putIfAbsent(type, new LinkedBlockingQueue<MicroService>());
		BlockingQueue<MicroService> messageTypeHandlersQueue = messagesHandlersQueues.get(type);
		if (messageTypeHandlersQueue != null && !messageTypeHandlersQueue.contains(m)) {
			//no problem with sync cause only this microservice manipulates the MessageBus at all manners referring to itself
			messageTypeHandlersQueue.add(m);
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	//TODO: Test complete - hadn't been tested
	public <T> void complete(Event<T> e, T result) {

		if (eventFutureConcurrentHashMap.containsKey(e)) {
			eventFutureConcurrentHashMap.get(e).resolve(result);
		} else {
			try {
				throw new NoSuchFieldException("Event was never added to Event-Future hash map");
			} catch (NoSuchFieldException noSuchFieldException) {
				noSuchFieldException.printStackTrace();
			}
		}
	}

	@Override
	//TODO: Test sendBroadcast - hadn't been tested
	public void sendBroadcast(Broadcast b) {
	/*
	-maybe syncronized which insures that all the currents gets and no body is added in the middle
	-get the queue if possible (otherwise exception or nothing) and check who is the current
	microservice for reference of completion of full circle of delivery to all the subscribers
	-maybe block at once or  copy a version and the deliver to them all
	 */
		try {
			for (MicroService ms : messagesHandlersQueues.get(b.getClass())) {
				microservicesMessageQueues.get(ms).add(b);
			}
		} catch (NullPointerException npe){
			System.out.println("no one has ever subscribed to this kind of broadcast");
			npe.printStackTrace();
		}

	}


	@Override
	//TODO: Test sendEvent - hadn't been tested
	public <T> Future<T> sendEvent(Event<T> e) {
	/*
	-get the queue if possible (otherwise exception or nothing) deliver to the next microservice
	-add to eventFutureConcurrentHashMap
	 ??? more thoughts about this one
	 */
		//Syncronized for ,make sure that after checking that theres a subscriber to the message type you can actually take from the handlers queue for sending purposes
		if( (!messagesHandlersQueues.containsKey(e.getClass())) || (messagesHandlersQueues.get(e.getClass()).isEmpty()) ) {
			return null;
		}
		else{
			MicroService handler = messagesHandlersQueues.get(e.getClass()).remove(); //remove() throws exception if Q is empty (shouldn't be by former check)
			try {
				microservicesMessageQueues.get(handler).put(e); // putting the event in the next handler
				messagesHandlersQueues.get(e.getClass()).put(handler); // returning it to the top of the Q for round robin fashion -
				// TODO:!! must be sure that happens in one cpu cycle so no body else will interrupt in the Delivery Order of the Q
				// put() of blocking Q is add to the end and if theres no space wait until you can - therefore could possibly be blocking
			} catch (InterruptedException interruptedException) {
				/*
				exception might be thrown if handler wasn't a key in microservicesMessageQueues hash map - never registered or an other problem
				otherwise put() is blocking as mentioned above so in some cases it will be waiting till completion or interruption
				 */
				interruptedException.printStackTrace();
			}
			Future<T> future = new Future<T>();
			eventFutureConcurrentHashMap.putIfAbsent(e,future);
			return future;
		}
	}

	@Override
	public void register(MicroService m) {
	microservicesMessageQueues.putIfAbsent(m,new LinkedBlockingQueue<Message>());
	}

	@Override
	//TODO: Test unregister - hadn't been tested
	public void unregister(MicroService m) { //should be synchronized or at least block senders operations-maybe solved by the fact of using ConcurrentHashMap
		//unsubscribe from all message handlers queues
		/*
		the original code before intelleJ optimization offer:
			for (BlockingQueue bq: messagesHandlersQueues.values() ) {
				if (bq.contains(m)) {
					bq.remove(m);
				}
			}
 		*/
		messagesHandlersQueues.values().stream().filter(bq -> bq.contains(m)).forEach(bq -> bq.remove(m));
		microservicesMessageQueues.remove(m);

	}

	@Override
	//TODO: Test awaitMessage - hadn't been tested
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return microservicesMessageQueues.get(m).take();
	}



}