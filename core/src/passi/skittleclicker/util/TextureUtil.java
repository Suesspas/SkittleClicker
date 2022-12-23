package passi.skittleclicker.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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

    public static TextureRegion[] timedGoldenStrawbFrames(TextureRegion[] regions){
        return new TextureRegion[]{
                regions[0], regions[0], regions[0], regions[0], regions[0], regions[0], regions[0], regions[0],
                regions[1],
                regions[2],
                regions[3],
                regions[4],
                regions[5], regions[5],
                regions[6], regions[6],
                regions[7], regions[7],
                regions[8], regions[8],
                regions[9], regions[9],
                regions[10], regions[10],
                regions[11], regions[11],
                regions[12], regions[12],
                regions[13],  regions[13],
                regions[14], regions[14],
                regions[15],
                regions[16],
                regions[17],
                regions[18],
                regions[19],
                regions[20],
                regions[21], regions[21], regions[21], regions[21], regions[21], regions[21], regions[21], regions[21],
                regions[22], regions[22],
                regions[23], regions[23],
                regions[24], regions[24],
                regions[25], regions[25],
                regions[26], regions[26],
                regions[27], regions[27], regions[27],
                regions[28], regions[28], regions[28], regions[28], regions[28], regions[28], regions[28], regions[28],
                regions[29], regions[29],
                regions[30],
                regions[31],
                regions[32],
                regions[33],
                regions[34],
                regions[35],
                regions[36],
                regions[37],
        };
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

    public static ImageButton.ImageButtonStyle getImageButtonStyle(String layoutStyle, int width, int height) {
        String imageUpPath = "button_" + layoutStyle + ".png";
        String imageDownPath = "button_" + layoutStyle + "_shadow.png";
        String imageMouseOverPath = "button_" + layoutStyle + "_light.png";
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(TextureUtil.scaleImage(imageUpPath,  width, height)));
        Drawable drawablePressed = new TextureRegionDrawable(new TextureRegion(TextureUtil.scaleImage(imageDownPath, width, height)));
        Drawable drawableMouseOver = new TextureRegionDrawable(new TextureRegion(TextureUtil.scaleImage(imageMouseOverPath, width, height)));
//        GreyedOutImageButton shopButton = new GreyedOutImageButton(drawable, drawablePressed, shader);
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle(null,null,null, drawable, drawablePressed,null);
        imageButtonStyle.imageOver = drawableMouseOver;
        return imageButtonStyle;
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
