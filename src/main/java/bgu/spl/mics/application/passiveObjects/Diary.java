package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private AtomicInteger totalAttacks = new AtomicInteger(0);
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private static class SingletonHolder {
        private static Diary instance= new Diary();
    }

    public void SetTimeDetail(TimeDetailOf Name,long Time){
    switch(Name){
        case HanSoloFinish:
            setHanSoloFinish(Time);
            break;
        case C3POFinish:
            setC3POFinish(Time);
            break;
        case R2D2Deactivate:
            setR2D2Deactivate(Time);
            break;
        case LeiaTerminate:
            setLeiaTerminate(Time);
            break;
        case HanSoloTerminate:
            setHanSoloTerminate(Time);
            break;
        case C3POTerminate:
            setC3POTerminate(Time);
            break;
        case R2D2Terminate:
            setR2D2Terminate(Time);
            break;
        case LandoTerminate:
            setLandoTerminate(Time);
            break;
    }
    }

    private void SetTotalAttacks(int totalAttacks){
        this.totalAttacks.set(totalAttacks);
    }

    public AtomicInteger getTotalAttacks(){return totalAttacks;}

    public static Diary getInstance() {
        return Diary.SingletonHolder.instance;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    private void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    private void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    private void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    private void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    private void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    private void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    private void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    private void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }
}
