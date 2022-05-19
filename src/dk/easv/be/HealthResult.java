package dk.easv.be;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class HealthResult {

   private int toggleId;
    private int expectedIndex;
    private String current;
    private String observation;
    private String technical;

    public HealthResult(HealthNodeContainer healthNodeContainer) {
        this.current = getString(healthNodeContainer.getCurrentTextarea());
        this.expectedIndex = getIndexFromComboBox(healthNodeContainer.getExpectedComboBox());
        this.observation = getString(healthNodeContainer.getObservationTextArea());
        this.toggleId = healthNodeContainer.getSelectedToggleId();
        this. technical = getString(healthNodeContainer.getTechnicalTextArea());
    }

    public HealthResult(int toggleId, int expectedIndex, String current, String observation, String technical) {
        this.toggleId = toggleId;
        this.expectedIndex = expectedIndex;
        this.current = current;
        this.observation = observation;
        this.technical = technical;
    }

    private int getIndexFromComboBox(ComboBox comboBox) {
        return comboBox.getSelectionModel().getSelectedIndex();
    }

    private String getString(TextArea textArea) {
        return textArea.getText();
    }

    public int getToggleId() {
        return toggleId;
    }

    public int getExpectedIndex() {
        return expectedIndex;
    }

    public String getCurrent() {
        return current;
    }

    public String getObservation() {
        return observation;
    }

    public String getTechnical() {
        return technical;
    }

    @Override
    public String toString() {
        return "HealthResult{" +
                "toggleId=" + toggleId +
                ", expectedIndex=" + expectedIndex +
                ", current='" + current + '\'' +
                ", observation='" + observation + '\'' +
                ", technical='" + technical + '\'' +
                '}';
    }
}
