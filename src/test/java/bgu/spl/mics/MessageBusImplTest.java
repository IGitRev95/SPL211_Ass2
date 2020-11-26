package bgu.spl.mics;

import bgu.spl.mics.application.messages.SimpleEventForTest;
import bgu.spl.mics.application.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl mBus;

    @BeforeEach
    void setUp() {
        mBus=new MessageBusImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSubscribeEvent() {
        /*
        scenario : 2 micro services init, one subscribe to an event
        type and the other send such an event.
        check the event arrived to its destination and make
        sure it is the same event.
         */

    }

    @Test
    void testSubscribeBroadcast() {
        /*
        scenario : 2 micro services init, one subscribe to a broadcast
        type and the other send such.
        check the broadcast arrived to the subscriber and make
        sure it is the same broadcast.
         */
    }

    @Test
    void testComplete() {
        /*
        scenario : create an @event with result A
        call complete with (@event,result B)
        assert that the event result is B
         */
    }

    @Test
    void testSendBroadcast() {
        /*
        make 3 micro services, 2 of them will subscribe to some broadcast
        send the broadcast make sure the subscribes queue contains the
        broadcast messege at the end of the queue and the left one hadn't get it
         */
    }

    @Test
    void testSendEvent() {
        /*
        make 2 micro services that will subscribe to the same event type
        - make sure the event were delivered
        - send 3 events and make sure they were delivered in around robin manner
         */
        MicroService m1 = new HanSoloMicroservice();
        MicroService m2 = new C3POMicroservice();
        mBus.subscribeEvent(SimpleEventForTest.class,m1);
        mBus.subscribeEvent(SimpleEventForTest.class,m2);
        SimpleEventForTest[] events = new SimpleEventForTest[3];
        for (int i=0;i<3;i++)
        {
            events[i] = new SimpleEventForTest(i);
            mBus.sendEvent(events[i]);
        }
        /*
        expected that the events will be delivered
        specifically in a round robin manner
         */
        try{
            Message msg;
            msg = mBus.awaitMessage(m1);
            assertEquals(events[0],msg);
            msg = mBus.awaitMessage(m2);
            assertEquals(events[1],msg);
            msg = mBus.awaitMessage(m1);
            assertEquals(events[2],msg);
        }catch(InterruptedException exp){
            fail("some event hadn't been delivered to it's destination");
        }
    }

    @Test
    void testRegister() {
        /*
        init a micro service and assert that the pool of micro services queues had grown per 1
         */
    }

    @Test
    void testAwaitMessage() {
    }
}