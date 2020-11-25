package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok e;

    @BeforeEach
    void setUp() {
        e = new Ewok();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAcquire() {
       e.available=true;
       e.acquire();
        assertFalse(e.available);
    }

    @Test
    void testRelease() {
        e.available=false;
        e.release();
        assertTrue(e.available);
    }
}