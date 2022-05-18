package dk.easv.be;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class FunkNodeContainer {

    private ComboBox<ImageView> currentComboBox;
    private ComboBox<ImageView> targetComboBox;
    private ComboBox<String> udførelseComboBox;
    private ComboBox<String> betydningComboBox;
    private TextArea fagTextArea;
    private TextArea citizenTextArea;
    private TextArea obsTextArea;

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

    public void setObsString(String s){
        getObsTextArea().setText(s);
    }

    public void setCitizenString(String s){
        getCitizenTextArea().setText(s);
    }

    public void setTechnicalString(String s){
        getFagTextArea().setText(s);
    }

    public void setImportanceIndex(int index){
        getBetydningComboBox().getSelectionModel().select(index);
    }

    public void setExecutionIndex(int index){
        getUdførelseComboBox().getSelectionModel().select(index);
    }

    public void setCurrentIndex(int index){
        getCurrentComboBox().getSelectionModel().select(index);
    }

    public void setTargetIndex(int index){
        getTargetComboBox().getSelectionModel().select(index);
    }
}
