package dk.easv.gui.supercontroller;

import com.sun.jdi.IntegerType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public abstract class saveCitizenController {
    protected HashMap<String, String> saveGenInfo(Map<String, TextArea> genInfoTextAreaMap) {
        HashMap<String, String> map = new HashMap<>();
        for (String key : genInfoTextAreaMap.keySet()) {
            TextArea textArea = genInfoTextAreaMap.get(key);
                map.put(key, textArea.getText().trim());

        }
        return map;
    }

    protected HashMap<Integer, String> saveFunkTextArea(Map<Integer,TextArea> funkInfoTextAreaMap) {
        HashMap<Integer, String> map = new HashMap<>();
        for (int key : funkInfoTextAreaMap.keySet()) {
            TextArea textArea = funkInfoTextAreaMap.get(key);
            if (!textArea.getText().isBlank()) {
                map.put(key, textArea.getText().trim());
            }
        }
        return map;
    }

    protected HashMap<Integer, Integer> saveFunkCurrentCombo(Map<Integer,ComboBox<ImageView>> currentComboMap) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int key : currentComboMap.keySet()) {
            ComboBox<ImageView> comboBox = currentComboMap.get(key);
            int index = comboBox.getSelectionModel().getSelectedIndex();
            int value = 0;

            switch (index) {
                case 0 -> value = 1;
                case 1 -> value = 2;
                case 2 -> value = 3;
                case 3 -> value = 4;
                case 4 -> value = 5;
                case 5-> value = 6;
                case -1 -> value = 6;
            }
            map.put(key, value);

        }
        return map;
    }

    protected HashMap<Integer, Integer> saveFunkTargetCombo(Map<Integer, ComboBox<String>> targetComboMap) {
        HashMap<Integer,Integer> map = new HashMap<>();
        for (int key : targetComboMap.keySet()) {
            ComboBox<String> comboBox = targetComboMap.get(key);
            int index = comboBox.getSelectionModel().getSelectedIndex();
            int value = 0;

            switch (index) {
                case 0 -> value = 1;
                case 1 -> value = 2;
                case 2 -> value = 3;
                case 3 -> value = 4;
                case -1 -> value = 4;
            }
            map.put(key,value);

        }
        return map;
    }

    protected HashMap<Integer, String> saveHealthInfo(Map<Integer, TextArea> helbredTextAreaMap) {
        HashMap<Integer,String> map = new HashMap<>();
        for(int key : helbredTextAreaMap.keySet()){
            TextArea textArea = helbredTextAreaMap.get(key);
            if (!textArea.isDisabled()){
                map.put(key,textArea.getText().trim());
            }
            else
                map.put(key,null);
        }
        return map;
    }

    protected HashMap<Integer, Integer> saveHealthRelevans(Map<Integer,ToggleGroup> healthToggleMap) {
        HashMap<Integer,Integer> map = new HashMap<>();
        for (int key : healthToggleMap.keySet()){
            ToggleGroup toggleGroup = healthToggleMap.get(key);
            if (toggleGroup.getSelectedToggle() != null){
                String data = (String) toggleGroup.getSelectedToggle().getUserData();
                int value = 0;
                //TODO REPLACE STRINGS MED ENUM HOLY SHIT
                switch (data){
                    case "Aktuel" -> value = 1;
                    case "Potentiel" -> value = 2;
                    case "Ikke relevant" -> value = 3;
                }
                map.put(key,value);
            }
            else{
                map.put(key,3);
            }
        }

        return map;
    }
}
