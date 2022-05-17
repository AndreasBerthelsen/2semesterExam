package dk.easv.gui.supercontroller;

import dk.easv.be.FunkNodeContainer;
import dk.easv.be.FunkResult;
import dk.easv.be.HealthNodeContainer;
import dk.easv.be.HealthResult;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class saveCitizenController {
    protected Map<Integer, HealthResult> saveHealth(Map<Integer, HealthNodeContainer> healthNodeMap) {
        Map<Integer, HealthResult> resultMap = new LinkedHashMap<>();
        for (int id : healthNodeMap.keySet()){
            resultMap.put(id,new HealthResult(healthNodeMap.get(id)));
        }
        return resultMap;
    }

    protected Map<Integer, FunkResult> saveFunk(Map<Integer, FunkNodeContainer> funkNodeMap) {
        Map<Integer, FunkResult> resultMap = new LinkedHashMap<>();
                for(int id : funkNodeMap.keySet()){
                    resultMap.put(id,new FunkResult(funkNodeMap.get(id)));
                }
        return resultMap;
    }

    protected Map<String,String> saveGeninfo(Map<String, TextArea> genInfoNodeMap) {
        Map<String,String> resultMap = new LinkedHashMap<>();
        for (String fieldName : genInfoNodeMap.keySet()){
            resultMap.put(fieldName,genInfoNodeMap.get(fieldName).getText());
        }
        return resultMap;
    }

}
