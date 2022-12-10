package passi.skittleclicker.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScreenUtil {
    public static Vector2 getScreenCoords(Actor actor, Camera camera){
        Vector2 vector = actor.localToScreenCoordinates(new Vector2(0, camera.viewportHeight + actor.getHeight()));
        vector.y = -vector.y;
        return vector;
    }
}
