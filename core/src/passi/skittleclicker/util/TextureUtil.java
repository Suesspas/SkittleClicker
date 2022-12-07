package passi.skittleclicker.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
