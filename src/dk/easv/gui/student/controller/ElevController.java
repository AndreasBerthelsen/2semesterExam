package dk.easv.gui.student.controller;

import dk.easv.be.User;
import dk.easv.gui.teacher.Interfaces.IController;
import javafx.event.ActionEvent;

public class ElevController implements IController {
    private User student;
    public void handleInspect(ActionEvent actionEvent) {
    }

    @Override
    public void setUserInfo(User user) {
        this.student = user;
    }
}
