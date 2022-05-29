package dk.easv.be;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class HealthNodeContainer {


    private TextArea technicalTextArea;
    private TextArea currentTextarea;
    private ComboBox<String> expectedComboBox;
    private TextArea observationTextArea;
    private int selectedToggleId;

    public HealthNodeContainer(TextArea technicalTextArea, TextArea currentTextarea, ComboBox<String> expectedComboBox, TextArea observationTextArea) {
        setSelectedToggleId(-1); // default no toggle picked
        this.technicalTextArea = technicalTextArea;
        this.currentTextarea = currentTextarea;
        this.expectedComboBox = expectedComboBox;
        this.observationTextArea = observationTextArea;
    }

    public TextArea getTechnicalTextArea() {
        return technicalTextArea;
    }

    public TextArea getCurrentTextarea() {
        return currentTextarea;
    }

    public ComboBox<String> getExpectedComboBox() {
        return expectedComboBox;
    }

    public TextArea getObservationTextArea() {
        return observationTextArea;
    }

    /**
     * Disabler alle nodes som dette objekt ejer
     */
    public void disableAllNodes() {
        getTechnicalTextArea().setDisable(true);
        getCurrentTextarea().setDisable(true);
        getExpectedComboBox().setDisable(true);
        getObservationTextArea().setDisable(true);
    }

    /**
     * Enabler alle nodes som dette objekt ejer
     */
    public void enableAllNodes() {
        getTechnicalTextArea().setDisable(false);
        getCurrentTextarea().setDisable(false);
        getExpectedComboBox().setDisable(false);
        getObservationTextArea().setDisable(false);
    }

    public void setSelectedToggleId(int toggleId) {
        selectedToggleId = toggleId;
    }

    public int getSelectedToggleId() {
        return selectedToggleId;
    }

    public void setTechnicalString(String s){
        getTechnicalTextArea().setText(s);
    }

    public void setCurrentString(String s){
        getCurrentTextarea().setText(s);
    }

    public void setExpectedIndex(int index){
        getExpectedComboBox().getSelectionModel().select(index);
    }

    public void setObservationString(String s){
        getObservationTextArea().setText(s);
    }

}
