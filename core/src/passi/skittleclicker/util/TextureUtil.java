package passi.skittleclicker.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class TextureUtil {
    public static TextureRegion[] getTextureRegions(Texture sheet, int FRAME_COLS, int FRAME_ROWS) {
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / FRAME_COLS,
                sheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return frames;
    }

    public static Texture combineTextures(Texture texture1, Texture texture2) { //second texture wil be drawn on top
        texture1.getTextureData().prepare();
        Pixmap pixmap1 = texture1.getTextureData().consumePixmap();

        texture2.getTextureData().prepare();
        Pixmap pixmap2 = texture2.getTextureData().consumePixmap();

        pixmap1.drawPixmap(pixmap2, 0, 0);
        Texture textureResult = new Texture(pixmap1);

        pixmap1.dispose();
        pixmap2.dispose();

        return textureResult;
    }
    public static void drawPartOfSprite(SpriteBatch batch, Texture texture, float x, float y, float width, float height,
                                        int partX, int partY, int partWidth, int partHeight){
        batch.draw(
                texture,
                x,                       /* x the x-coordinate in screen space                                            */
                y,                       /* y the y-coordinate in screen space                                            */
                0,               /* originX the x-coordinate of the scaling and rotation origin relative to the screen space coordinates   */
                0,              /* originY the y-coordinate of the scaling and rotation origin relative to the screen space coordinates   */
                /* We only want to draw half the width of the sprite */
                width,               /* width the width in pixels                                                     */
                height,                  /* height the height in pixels                                                   */
                1,       /* scaleX the scale of the rectangle around originX/originY in x                 */
                1,       /* scaleY the scale of the rectangle around originX/originY in y                 */
                0 ,                      /* rotation the angle of counter clockwise rotation of the rectangle around originX/originY               */
                partX,      /* srcX the x-coordinate in texel space                                          */
                partY,      /* srcY the y-coordinate in texel space                                          */
                /* We only want to use half the source texture region */
                partWidth,   /* srcWidth the source with in texels                                            */
                partHeight, /* srcHeight the source height in texels                                         */
                false,                   /* flipX whether to flip the sprite horizontally                                 */
                false);                  /* flipY whether to flip the sprite vertically */
    }



    public static Texture scaleImage(String path, int width, int height){
        Pixmap original = new Pixmap(Gdx.files.internal(path));
        Pixmap scaled = new Pixmap(width, height, original.getFormat());
        scaled.drawPixmap(original,
                0, 0, original.getWidth(), original.getHeight(),
                0, 0, scaled.getWidth(), scaled.getHeight()
        );
        Texture texture = new Texture(scaled);
        original.dispose();
        scaled.dispose();
        return texture;
    }
}
