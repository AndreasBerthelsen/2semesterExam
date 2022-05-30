package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

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

    /**
     * Denne metode gør således, at den valgte template kan kopieres med et nyt navn
     * ved anvendelse af metoden createCopyCase().
     * @param actionEvent
     * @throws SQLServerException
     */
    public void handleSaveBtn(ActionEvent actionEvent) throws SQLServerException {
        String fName = getFirstName(fNameCopyTextField);
        String lName = getLastName(lNameCopyTextField);
        if (fName != null && lName != null){
            citizenModel.createCopyCase(citizen,fName,lName);
        }
        closeWindow(saveBtn);
    }

    /**
     * Når denne knap trykkes, lukkes vinduet
     * @param actionEvent
     */
    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }

    /**
     * Sætter templatetens tidligere navn
     */
    public void setCitizenName(){
        fNameCopyTextField.setText(citizen.getFirstname());
        lNameCopyTextField.setText(citizen.getLastname());
    }

    /**
     * Sætter den citizens info, som er valgt
     * @param citizen
     */
    @Override
    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
        setCitizenName();
    }


}
