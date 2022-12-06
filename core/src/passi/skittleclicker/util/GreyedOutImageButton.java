package passi.skittleclicker.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import passi.skittleclicker.objects.ShopGroup;

public class GreyedOutImageButton extends ImageButton {
    private boolean isGreyedOut; // A flag that determines whether this should be greyed out
    private final ShaderProgram shader;
    public GreyedOutImageButton(ImageButtonStyle style, ShaderProgram shader) {
        super(style);
//        addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                // When clicked, toggle the grey effect
//                Gdx.app.log("ClickListener", "Click");
//                setIsGreyedOut(!getIsGreyedOut());
//            }
//        });
        isGreyedOut = false;
        this.shader = shader;
    }

    public GreyedOutImageButton(Drawable up, Drawable down, ShaderProgram shader) {
        super(up, down);
        this.setIsGreyedOut(false);
        this.shader = shader;
    }

    public void setIsGreyedOut(boolean isGreyedOut) {
        this.setDisabled(isGreyedOut);
//        this.setTouchable(isGreyedOut? Touchable.disabled : Touchable.enabled); // disabling touchable prevents tooltips from rendering
        this.isGreyedOut = isGreyedOut;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isGreyedOut) {
            batch.end();
            batch.setShader(shader); // Set our grey-out shader to the batch
            batch.begin();
            super.draw(batch, parentAlpha); // Draw the image with the greyed out affect
            batch.setShader(null); // Remove shader from batch so that other images using the same batch won't be affected
        }
        else {
            // If not required to be grey-out, do normal drawing
            super.draw(batch, parentAlpha);
        }
    }
}
