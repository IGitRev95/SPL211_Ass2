package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import bgu.spl.mics.application.services.R2D2Microservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl mBus;
    private Future f1;

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
        - send 3 events and make sure they were delivered in around robin manner
        - make sure there is no event that had been subscribed to both of the micro services
        - assert returning of a legal value
         */
    }

    @Test
    void testRegister() {
        /*
        init a micro service and assert that the pool of micro services queues had grown per 1
         */
    }

    @Test
    void testUnregister() {
        /*
        init a micro service and subscribe to it an event
        call unregister
        assert that the pool of micro services queues had shrank per 1
        assert all references had been cleaned from the bus
         */
    }

    @Test
    void testAwaitMessage() {
    }
}