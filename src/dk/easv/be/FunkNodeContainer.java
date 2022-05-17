package dk.easv.be;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class FunkNodeContainer {
    //et obj for hver section - 30ish obj til funk journal

    ComboBox<ImageView> currentComboBox;
    ComboBox<ImageView> targetComboBox;
    ComboBox<String> udførelseComboBox;
    ComboBox<String> betydningComboBox;
    TextArea fagTextArea;
    TextArea citizenTextArea;
    TextArea obsTextArea;

    public FunkNodeContainer(ComboBox<ImageView> currentComboBox, ComboBox<ImageView> targetComboBox, ComboBox<String> udførelseComboBox, ComboBox<String> betydningComboBox, TextArea fagTextArea, TextArea citizenTextArea, TextArea obsTextArea) {
        this.currentComboBox = currentComboBox;
        this.targetComboBox = targetComboBox;
        this.udførelseComboBox = udførelseComboBox;
        this.betydningComboBox = betydningComboBox;
        this.fagTextArea = fagTextArea;
        this.citizenTextArea = citizenTextArea;
        this.obsTextArea = obsTextArea;
    }

    public ComboBox<ImageView> getCurrentComboBox() {
        return currentComboBox;
    }


    public ComboBox<ImageView> getTargetComboBox() {
        return targetComboBox;
    }


    public ComboBox<String> getUdførelseComboBox() {
        return udførelseComboBox;
    }


    public ComboBox<String> getBetydningComboBox() {
        return betydningComboBox;
    }


    public TextArea getFagTextArea() {
        return fagTextArea;
    }


    public TextArea getCitizenTextArea() {
        return citizenTextArea;
    }

    public TextArea getObsTextArea() {
        return obsTextArea;
    }
}
