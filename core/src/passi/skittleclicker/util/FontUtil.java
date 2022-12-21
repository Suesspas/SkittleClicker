package passi.skittleclicker.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class FontUtil {

    public static BitmapFont FONT_30 = null;
    public static BitmapFont FONT_20 = null;
    public static BitmapFont FONT_15 = null;
    public static BitmapFont FONT_10 = null;
    public static BitmapFont FONT_8 = null;
    public static BitmapFont FONT_60 = null;

    private FontUtil() {
        throw new UnsupportedOperationException();
    }

    public static void init() {
        String komika = "fonts/KOMTITK_.ttf";
        String chunkRegular = "fonts/chunkfive/ChunkFive-Regular.otf";
        String openSansBold = "fonts/open-sans/OpenSans-Bold.ttf";
        String openSansExtraBold = "fonts/open-sans/OpenSans-ExtraBold.ttf";

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(openSansExtraBold));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        FONT_30 = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        FONT_20 = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;
        FONT_15 = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        FONT_10 = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 8;
        FONT_8 = generator.generateFont(parameter);

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        FONT_60 = generator.generateFont(parameter);

    }
}
