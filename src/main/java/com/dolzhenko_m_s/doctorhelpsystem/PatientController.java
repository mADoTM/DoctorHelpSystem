package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.dao.PatientDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;

public class PatientController {
    public TextField nameTextField;
    public TextField phoneTextField;
    public TextField addressTextField;
    public TextField diagnosisTextField;
    public DatePicker datePicker;
    public TextArea remarkArea;

    public void savePatient(ActionEvent actionEvent) {
        var patient = new Patient();
        patient.setName(nameTextField.getText());
        patient.setPhoneNumber(phoneTextField.getText());
        patient.setAddress(addressTextField.getText());
        patient.setDiagnosis(diagnosisTextField.getText());
        patient.setBirthDate(Date.valueOf(datePicker.getValue()));
        patient.setRemark(remarkArea.getText());

        patient = new PatientDAO().save(patient);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-card.fxml"));
        try {
            Parent root = loader.load();
            var medController = (PatientCardController) loader.getController();
            medController.setPatient(patient);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMinWidth(700);
            stage.setTitle("Медицинская карта пациента " + patient.getName());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) addressTextField.getScene().getWindow();
        stage.close();
    }
}
