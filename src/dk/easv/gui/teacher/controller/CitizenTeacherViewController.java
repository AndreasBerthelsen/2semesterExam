package dk.easv.gui.teacher.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;

public class CitizenTeacherViewController {

    @FXML
    private TableView<String> templateTableView;
    @FXML
    private TreeTableView<String> citizenTreeTableView;
    @FXML
    private TreeTableView<String> studentTreeTableView;

    public void handleCopyCitizenBtn(ActionEvent actionEvent) {

    }

    public void handleRemoveCitizenBtn(ActionEvent actionEvent) {
    }

    public void handleAddCitizenToStudentBtn(ActionEvent actionEvent) {


    }


    public void handleRemoveCitizenFromStudentBtn(ActionEvent actionEvent) {
    }
}
