package dk.easv.gui.teacher.controller;

import dk.easv.be.FunkChunkAnswer;
import dk.easv.be.Section;
import dk.easv.bll.Util.TabFactory;
import dk.easv.gui.supercontroller.saveCitizenController;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.*;

public class NySkabelonMainController extends saveCitizenController implements Initializable {
    public TextField fNameInput;
    public DatePicker dateInput;
    public TextField lNameInput;
    public TextArea descriptionInput;
    //todo hent alle info fields med ny thread
    CitizenModel sM = new CitizenModel();
    //funktion
    public TabPane funktionInnerTabPane;
    private final Map<Integer, FunkChunkAnswer> funkSectionAnswerMap = new LinkedHashMap<>();

    //Gen info
    public javafx.scene.control.ScrollPane genScrollPane;
    private final HashMap<String, TextArea> genInfoTextAreaMap = new HashMap<>();
    private final ArrayList<String> genInfoFieldList = sM.getGeneralinfoFields();

    //helbred
    private final HashMap<Integer, ToggleGroup> healthToggleMap = new HashMap<>();
    private final HashMap<Integer, TextArea> helbredTextAreaMap = new HashMap<>();
    public TabPane helbredsInnerTabPane;

    public NySkabelonMainController() throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFunkTab();

    }

    /*
        private void setupGeneralInfo() {
            VBox vBox = new VBox();

            for (String field : genInfoFieldList) {
                Label label = new Label(field);
                label.getStylesheets().add("dk/easv/CSS/Skabelon.css");
                TextArea textArea = createTextArea(genInfoTextAreaMap, field);
                textArea.setId("textArea");

                vBox.getChildren().add(label);
                vBox.getChildren().add(textArea);
            }
            genScrollPane.setId("VBOX");
            genScrollPane.getStylesheets().add("dk/easv/CSS/Skabelon.css");
            genScrollPane.setContent(vBox);
            genScrollPane.getStylesheets().add("dk/easv/CSS/Skabelon.css");
            vBox.setId("VBOX");
        }
     */
    private void setupFunkTab() {
        List<Section> funkSectionList = sM.getFunkSections();

        for (Section section : funkSectionList) {
            funktionInnerTabPane.getTabs().add(TabFactory.buildFunkTab(section, funkSectionAnswerMap));
        }

    }
    /*
    private void setupHelbredTab() {
        List<Section> healthSections = sM.getHealthSections();

        for (Section section : healthSections) {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(50);
            gridPane.getStylesheets().add("dk/easv/CSS/Skabelon.css");
            gridPane.setId("VBOX");
            int index = 0;

            for (int key : section.getProblemidTitleMap().keySet()) {
                String chunkTitle = section.getProblemidTitleMap().get(key);
                Label labelSub = new Label(chunkTitle);
                ArrayList<RadioButton> radioButtonList = createRadioButtons(healthToggleMap, key);
                TextArea textArea = createTextArea(helbredTextAreaMap, key);
                textArea.setDisable(true);
                gridPane.addRow(index++, labelSub, radioButtonList.get(0), radioButtonList.get(1), radioButtonList.get(2), textArea);
            }
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(gridPane);
            scrollPane.setPrefSize(gridPane.getPrefWidth(),500);
            Tab tab = new Tab(section.getSectionTitle());
            tab.setContent(scrollPane);
            helbredsInnerTabPane.getTabs().add(tab);
        }
    }
 */


    public void handleGembtn(ActionEvent actionEvent) {
        try {
            String fname = fNameInput.getText().trim();
            String lname = lNameInput.getText().trim();
            java.sql.Date date = Date.valueOf(dateInput.getValue().toString());
            String description = descriptionInput.getText().trim();

            sM.saveTemplate(fname, lname, date,description, null, funkSectionAnswerMap, null);
            Stage stage = (Stage) fNameInput.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("qqqqqqqqqqqqqqq");
        }


    }



    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }
}
