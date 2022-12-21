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
            objects.add(0);
            for (int i = 0; i < numberOfShopGroups; i++) {
                objects.add(0);
            }
            for (int i = 0; i < numberOfUpgrades; i++) {
                objects.add(false);
            }
            return objects;
        }

        try {
            JsonReader reader = new JsonReader();
            JsonValue value = reader.parse(Files.newInputStream(file.toPath()));
            objects.add(value.get("skittles") == null ? 0 : value.get("skittles").asLong());
//            objects.add(value.get("clickers") == null ? 0 : value.get("clickers").asLong());
//            objects.add(value.get("grandmas") == null ? 0 : value.get("grandmas").asLong());
//            objects.add(value.get("bakeries") == null ? 0 : value.get("bakeries").asLong());
//            objects.add(value.get("factories") == null ? 0 : value.get("factories").asLong());

            for (int i = 0; i < numberOfShopGroups; i++) {
                objects.add(value.get("sg" + i) == null ? 0 : value.get("sg" + i).asLong());
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
            for (int i = 0; i < shopGroups.size(); i++) {
                value.addChild("sg" + i, new JsonValue(shopGroups.get(i)));
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