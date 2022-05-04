package dk.easv.gui.teacher.controller;

import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import javafx.event.ActionEvent;

import java.io.IOException;

public class AdminstrateStudentsController extends SuperController {

    public void handleAddStudentBtn(ActionEvent actionEvent) throws IOException {
        openScene("/dk/easv/gui/teacher/view/AddStudentView.fxml",true, "Tilføj en elev", false);
    }


}
