package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CopyChooseNameController extends SuperController implements ICitizenSelector{
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField fNameCopyTextField;
    @FXML
    private TextField lNameCopyTextField;
    private Citizen citizen;
    CitizenModel citizenModel;


    public CopyChooseNameController() throws IOException {
        citizenModel = new CitizenModel();
    }


    public void handleSaveBtn(ActionEvent actionEvent) throws SQLServerException {
        String fName = getFirstName(fNameCopyTextField);
        String lName = getLastName(lNameCopyTextField);
        if (fName != null && lName != null){
            citizenModel.createCopyCase(citizen,fName,lName);
        }
        closeWindow(saveBtn);
    }

    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }

    public void setCitizenName(){
        fNameCopyTextField.setText(citizen.getFirstname());
        lNameCopyTextField.setText(citizen.getLastname());
    }

    @Override
    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
        setCitizenName();
    }


}