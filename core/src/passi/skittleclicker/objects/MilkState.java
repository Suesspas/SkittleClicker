package passi.skittleclicker.objects;

import java.util.HashMap;
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

    public static void setUpStates(int[] upgradeIndices){
        for (int i = 0; i < upgradeIndices.length; i++) {
            stateUpgradeIndices.put(upgradeIndices[i], State.values()[i]);
        }
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
}
