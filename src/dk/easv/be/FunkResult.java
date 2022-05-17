package dk.easv.be;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FunkResult {
    private int importance;
    private String citizenString;
    private String technical;
    private String observation;
    private int execution;
    private int target;
    private int current;

    public FunkResult(FunkNodeContainer funkNodeContainer) {
        this.importance = getIndexFromComboBox(funkNodeContainer.getBetydningComboBox());
        this.citizenString = getString(funkNodeContainer.getCitizenTextArea());
        this.technical = getString(funkNodeContainer.getFagTextArea());
        this.observation = getString(funkNodeContainer.getObsTextArea());
        this.execution =getIndexFromComboBox(funkNodeContainer.getUdførelseComboBox());
        this.target = getIndexFromComboBox(funkNodeContainer.getTargetComboBox());
        this.current = getIndexFromComboBox(funkNodeContainer.getCurrentComboBox());

    }
    private int getIndexFromComboBox(ComboBox comboBox) {
        return comboBox.getSelectionModel().getSelectedIndex();
    }

    private String getString(TextArea textArea) {
        return textArea.getText();
    }

    public int getImportance() {
        return importance;
    }

    public String getCitizenString() {
        return citizenString;
    }

    public String getTechnical() {
        return technical;
    }

    public String getObservation() {
        return observation;
    }

    public int getExecution() {
        return execution;
    }

    public int getTarget() {
        return target;
    }

    public int getCurrent() {
        return current;
    }


    @Override
    public String toString() {
        return "FunkResult{" +
                "importance=" + importance +
                ", citizenString='" + citizenString + '\'' +
                ", technical='" + technical + '\'' +
                ", observation='" + observation + '\'' +
                ", execution=" + execution +
                ", target=" + target +
                ", current=" + current +
                '}';
    }
}
