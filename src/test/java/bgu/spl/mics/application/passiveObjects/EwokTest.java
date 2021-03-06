package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok e;

    @BeforeEach
    void setUp() {
        e = new Ewok(10);
    }

    @Test
    void testAcquire() {
        assertTrue(e.available);
        e.acquire();
        assertFalse(e.available);
    }

    @Test
    void testRelease() {
        try{ // expect exception while trying to release an already available Ewok
            e.release();
            fail("Ewok already available");
        }catch (Exception exp){}
        e.acquire();
        e.release();
        assertTrue(e.available);
    }
}