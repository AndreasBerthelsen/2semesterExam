package dk.easv.be;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;


import java.util.LinkedHashMap;
import java.util.Map;

public class FunkChunkAnswer {
    //et obj for hver section - 30ish obj til funk journal

    ComboBox<ImageView> currentComboBox;
    ComboBox<ImageView> targetComboBox;
    ComboBox<String> udførelseComboBox;
    ComboBox<String> betydningComboBox;
    TextArea fagTextArea;
    TextArea citizenTextArea;

    public FunkChunkAnswer(ComboBox<ImageView> currentComboBox, ComboBox<ImageView> targetComboBox, ComboBox<String> udførelseComboBox, ComboBox<String> betydningComboBox, TextArea fagTextArea, TextArea citizenTextArea) {
        this.currentComboBox = currentComboBox;
        this.targetComboBox = targetComboBox;
        this.udførelseComboBox = udførelseComboBox;
        this.betydningComboBox = betydningComboBox;
        this.fagTextArea = fagTextArea;
        this.citizenTextArea = citizenTextArea;
    }

    public ComboBox<ImageView> getCurrentComboBox() {
        return currentComboBox;
    }

    public void setCurrentComboBox(ComboBox<ImageView> currentComboBox) {
        this.currentComboBox = currentComboBox;
    }

    public ComboBox<ImageView> getTargetComboBox() {
        return targetComboBox;
    }

    public void setTargetComboBox(ComboBox<ImageView> targetComboBox) {
        this.targetComboBox = targetComboBox;
    }

    public ComboBox<String> getUdførelseComboBox() {
        return udførelseComboBox;
    }

    public void setUdførelseComboBox(ComboBox<String> udførelseComboBox) {
        this.udførelseComboBox = udførelseComboBox;
    }

    public ComboBox<String> getBetydningComboBox() {
        return betydningComboBox;
    }

    public void setBetydningComboBox(ComboBox<String> betydningComboBox) {
        this.betydningComboBox = betydningComboBox;
    }

    public TextArea getFagTextArea() {
        return fagTextArea;
    }

    public void setFagTextArea(TextArea fagTextArea) {
        this.fagTextArea = fagTextArea;
    }

    public TextArea getCitizenTextArea() {
        return citizenTextArea;
    }

    public void setCitizenTextArea(TextArea citizenTextArea) {
        this.citizenTextArea = citizenTextArea;
    }
}
