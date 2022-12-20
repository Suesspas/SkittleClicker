package passi.skittleclicker.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextActor extends Actor {

    BitmapFont font;
    String text;      //I assumed you have some object
    //that you use to access score.
    //Remember to pass this in!
    public TextActor(String text, BitmapFont font){
        this.font = font;
        this.text = text;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.begin();
        font.draw(batch, text, 0, 0);
//        batch.end();
        //Also remember that an actor uses local coordinates for drawing within
        //itself!
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        // TODO Auto-generated method stub
        return null;
    }
}
