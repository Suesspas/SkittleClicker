package passi.skittleclicker.objects;

import com.badlogic.gdx.math.Vector2;

public class ClickSkittle extends MiniSkittle{
    private final float originX;
    private final float originY;
    private final long skittleAmount;
    private int aliveTime;
    private final int MAX_ALIVE_TIME = 100;
    Vector2 direction;
    public ClickSkittle(float x, float y, float rotation, int color, long skittleAmount) {
        super(x, y, rotation, color);
        this.originX = x;
        this.originY = y;
        this.skittleAmount = skittleAmount;
        this.aliveTime = 0;
        this.direction = new Vector2(-1 + (float)Math.random() * 2, 0.5f + (float)Math.random());
    }
    public void update(){//TODO render list of clickSkittles in game screen and
        aliveTime++;
        x += SPEED * direction.x;
        y -= SPEED; //* direction.y for variable fall speed
    }
    public boolean isAlive(){
        return aliveTime < MAX_ALIVE_TIME;
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public long getSkittleAmount() {
        return skittleAmount;
    }
}
