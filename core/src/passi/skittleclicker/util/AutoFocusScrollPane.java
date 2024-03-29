package passi.skittleclicker.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class AutoFocusScrollPane extends ScrollPane {

    public AutoFocusScrollPane() {
        super(null);
        addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(AutoFocusScrollPane.this);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                getStage().setScrollFocus(null);
            }
        });
    }
    public AutoFocusScrollPane(Actor actor) {
        super(actor);
        addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(AutoFocusScrollPane.this);
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                getStage().setScrollFocus(null); //removed so after clicking button in shop table focus remains
            }
        });
    }
}
