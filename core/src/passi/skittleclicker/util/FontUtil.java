/*
 *  Copyright (c) 2018 Cerus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cerus
 *
 */

package passi.skittleclicker.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;

public class FontUtil {

    public static BitmapFont FONT_30 = null;
    public static BitmapFont FONT_20 = null;
    public static BitmapFont FONT_15 = null;
    public static BitmapFont FONT_10 = null;
    public static BitmapFont FONT_8 = null;

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

    }
}
