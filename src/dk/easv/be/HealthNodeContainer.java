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

    public void disableAllNodes() {
        getCurrentTextarea().setDisable(true);
        getExpectedComboBox().setDisable(true);
        getObservationTextArea().setDisable(true);
        getTechnicalTextArea().setDisable(true);
    }

    public void enableAllNodes() {
        getCurrentTextarea().setDisable(false);
        getExpectedComboBox().setDisable(false);
        getObservationTextArea().setDisable(false);
        getTechnicalTextArea().setDisable(false);
    }

    public void setSelectedToggleId(int toggleId) {
        selectedToggleId = toggleId;
    }

    public int getSelectedToggleId() {
        return selectedToggleId;
    }
}
