package passi.skittleclicker.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import passi.skittleclicker.SkittleClickerGame;

import java.util.Scanner;

public class ScreenUtil {
    public static Vector2 getScreenCoords(Actor actor, Camera camera){
        Vector2 vector = actor.localToScreenCoordinates(new Vector2(0, camera.viewportHeight + actor.getHeight()));
        vector.y = -vector.y;
        return vector;
    }
   private static final String[] cheatCodes ={
            "nazrin",
           "megumin",
           "pickle",
           "nodrip"
    };
    private static int[] currentCodePosition = new int[cheatCodes.length];
    public static String checkIfCheatCodeEntered(){
        int i = 0;
        for (String code:
             cheatCodes) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.valueOf(
                    code.toUpperCase().substring(currentCodePosition[i] , currentCodePosition[i] + 1)))) {
                System.out.println(code.toUpperCase().substring(currentCodePosition[i] , currentCodePosition[i] + 1) + " pressed");
                currentCodePosition[i]++;
                if (currentCodePosition[i] >= cheatCodes[i].length()){
                    currentCodePosition[i] = 0;
                    return cheatCodes[i];
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.valueOf(
                    code.toUpperCase().substring(0 , 1)))) {
                currentCodePosition[i] = 1;
            } else {
                currentCodePosition[i] = 0;
                System.out.println("Reset code " + cheatCodes[i]);
            }
            i++;
        }
        return null;
    }
}
