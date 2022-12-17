package passi.skittleclicker.objects;
public abstract class GoldenSkittle {
    public enum State{
        ACTIVE, //effect active after clicking golden skittle
        SKITTLE, //golden skittle is on screen
        INACTIVE
    }
    private static State currentState = State.INACTIVE;
    private static int timer = 0;
    private static final int RESPAWN_TIME = 3;
    private static final int DESPAWN_TIME = 10;
    private static final int ACTIVE_DURATION = 113;

    public static boolean isInState(State state) {
        return currentState == state;
    }
    public static void clicked() {
        timer = -ACTIVE_DURATION;
        currentState = State.ACTIVE;
    }
    public static void incrementTimer() {
        timer++;
    }
    public static boolean isRespawnTime() {
        return timer == RESPAWN_TIME;
    }
    public static boolean isDespawnTime(){
        return timer > RESPAWN_TIME + DESPAWN_TIME;
    }
    public static boolean isActiveEndTime(){
        return timer == -1;
    }
    public static void respawn() {
        currentState = State.SKITTLE;
    }
    public static void despawn() {
        timer = 0;
        currentState = State.INACTIVE;
    }
    public static void activeEnd() {
        currentState = State.INACTIVE;
    }
}
