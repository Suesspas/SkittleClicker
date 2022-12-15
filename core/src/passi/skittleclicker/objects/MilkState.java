package passi.skittleclicker.objects;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilkState {

    enum State {
        MILK,
        CHOCY,
        MACHA,
        CATGIRL
    }
    private static final Map<Integer, State> stateUpgradeIndices = new HashMap<>();
    private static State currentState = null;
    private static Color[] stateColors = new Color[State.values().length];

    public static void setUpStates(int[] upgradeIndices){
        for (int i = 0; i < upgradeIndices.length; i++) {
            stateUpgradeIndices.put(upgradeIndices[i], State.values()[i]);
        }
        stateColors[0] = Color.WHITE;
        stateColors[1] = Color.BROWN;
        stateColors[2] = Color.GREEN;
        stateColors[3] = Color.PINK;
    }
    public static void changeState(int upgradeIndex){
        if (!stateUpgradeIndices.containsKey(upgradeIndex)) return;
        State milkState = stateUpgradeIndices.get(upgradeIndex);
        if (currentState == null){
            currentState = milkState;
            return;
        }
        if (milkState.ordinal() > currentState.ordinal()){
            currentState = milkState;
        }
    }
    public static State getCurrentState(){
        return currentState;
    }
    public static Color getCurrentStateColor(){
        if (currentState == null) {
            return null;
        }
        return stateColors[currentState.ordinal()];
    }
    public static void deleteSaveData() {
        currentState = null;
    }

}
