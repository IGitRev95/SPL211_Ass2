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
	private ConcurrentHashMap<Event<?>,Future> eventFutureConcurrentHashMap = new ConcurrentHashMap<>();

	private static class SingletonHolder {
		private static MessageBusImpl instance= new MessageBusImpl();
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		subscribeMessage(type,m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		subscribeMessage(type,m);
	}

	private void subscribeMessage(Class<? extends Message> type, MicroService m) {
		//Assert MicroService Registration in the MessageBus
		if(!microservicesMessageQueues.containsKey(m))
		{
			throw new NullPointerException("none registered microservice");
		}
		//Adding the microservice to the Message Handler queue of the message type (creating new queue one if absent)
		messagesHandlersQueues.putIfAbsent(type, new LinkedBlockingQueue<MicroService>());
		BlockingQueue<MicroService> messageTypeHandlersQueue = messagesHandlersQueues.get(type);
		if (messageTypeHandlersQueue != null && !messageTypeHandlersQueue.contains(m)) {
		//no problem with sync cause only this microservice manipulates the MessageBus at all manners referring to itself
			messageTypeHandlersQueue.add(m);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		if (eventFutureConcurrentHashMap.containsKey(e)) {
			eventFutureConcurrentHashMap.get(e).resolve(result);
		} else {
			throw new IllegalArgumentException("Event was never added to Event-Future hash map");
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
	//getting the handlers queue and deliver the broadcast to them all
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
	public <T> Future<T> sendEvent(Event<T> e) {
		//Check existence of handlers queue of the Event type (mostly for using synchronized by monitor afterwords)
		if(messagesHandlersQueues.get(e.getClass())==null)
			return null;
		/*
		Synchronized on the BlockingQueue of the message type to insure
		 there's not two event are being delivered in the same time which
		  can affect the round robin manner and can cause a lost of DATA
		 */
		synchronized (messagesHandlersQueues.get(e.getClass())) {
			//Checking there's a micro service that will receive the event
			if ((!messagesHandlersQueues.containsKey(e.getClass())) || (messagesHandlersQueues.get(e.getClass()).isEmpty())) {
				return null;
			} else {
				Future<T> future = new Future<T>();
				eventFutureConcurrentHashMap.putIfAbsent(e, future);
				MicroService handler = messagesHandlersQueues.get(e.getClass()).remove();
				//remove() throws exception if Q is empty (shouldn't be by former check)
				try {
					microservicesMessageQueues.get(handler).put(e); // putting the event in the next handler message queue
					messagesHandlersQueues.get(e.getClass()).put(handler); // returning the handler microservice to the top of the queue for round robin fashion
				} catch (InterruptedException interruptedException) {
				/*
				exception might be thrown if handler wasn't a key in microservicesMessageQueues hash map - never registered or an other problem
				otherwise put() is blocking as mentioned above so in some cases it will be waiting till completion or interruption
				 */
					interruptedException.printStackTrace();
				}
				return future;
			}
		}
	}

	@Override
	public void register(MicroService m) {
	microservicesMessageQueues.putIfAbsent(m,new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		//unsubscribe from all message handlers queues
		messagesHandlersQueues.values().stream().filter(bq -> bq.contains(m)).forEach(bq -> bq.remove(m));
		//remove the microservice queue from map
		microservicesMessageQueues.remove(m);
		//no need for synchronization due to use of only concurrent container built in functions
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(microservicesMessageQueues.get(m)!=null) {
			return microservicesMessageQueues.get(m).take();
		}else{ throw new IllegalArgumentException("non registered microservice");}
	}



}