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

package passi.skittleclicker.data;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import passi.skittleclicker.objects.ShopGroup;
import passi.skittleclicker.objects.Upgrade;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Data {

    private static File file = new File(String.format("%s//SkittleClicker//data.json", System.getenv("APPDATA")));;
    public static List<Object> loadProgress(int numberOfShopGroups, int numberOfUpgrades) {
        List<Object> objects = new ArrayList<>();
        if (!file.exists()) {
            for (int i = 0; i < numberOfShopGroups; i++) {
                objects.add(0);
            }
            for (int i = 0; i < numberOfUpgrades; i++) {
                objects.add(false);
            }
        }

        try {
            JsonReader reader = new JsonReader();
            JsonValue value = reader.parse(Files.newInputStream(file.toPath()));
            objects.add(value.get("skittles") == null ? 0 : value.get("skittles").asLong());
            objects.add(value.get("clickers") == null ? 0 : value.get("clickers").asLong());
            objects.add(value.get("grandmas") == null ? 0 : value.get("grandmas").asLong());
            objects.add(value.get("bakeries") == null ? 0 : value.get("bakeries").asLong());
            objects.add(value.get("factories") == null ? 0 : value.get("factories").asLong());

            for (int i = 5; i <= numberOfShopGroups; i++) {
                objects.add(value.get("placeholder" + (i-4)) == null ? 0 : value.get("placeholder" + (i-4)).asLong());
            }

            for (int i = 0; i < numberOfUpgrades; i++) {
                objects.add(value.get("upgrade" + i) != null && value.get("upgrade" + i).asBoolean());
//                objects.add(true);
            }
            return objects;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveProgress(long skittles, List<Long> shopGroups, List<Upgrade> upgradeList) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            System.out.println("saving progress in " + file.getAbsolutePath());


            JsonValue value = new JsonValue(JsonValue.ValueType.object);
            value.addChild("skittles", new JsonValue(skittles));
            value.addChild("clickers", new JsonValue(shopGroups.get(0)));
            value.addChild("grandmas", new JsonValue(shopGroups.get(1)));
            value.addChild("bakeries", new JsonValue(shopGroups.get(2)));
            value.addChild("factories", new JsonValue(shopGroups.get(3)));
            for (int i = 4; i < shopGroups.size(); i++) {
                value.addChild("placeholder" + (i-3), new JsonValue(shopGroups.get(i)));
            }

            int i = 0;
            for (Upgrade u:
                 upgradeList) {
                value.addChild("upgrade" + i, new JsonValue(u.isUnlocked()));
                i++;
            }

            System.out.println("saved value: " + value);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(value.toJson(JsonWriter.OutputType.json));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}