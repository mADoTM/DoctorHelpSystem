package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.dao.NotificationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;

public class NotificationController {
    public TextArea actionArea;
    public DatePicker datePicker;
    public CheckBox executedBox;
    public CheckBox directedCheckBox;
    public Button endActionButton;
    public Label patientNameLabel;

    private Patient patient;
    private Notification notification;

    private boolean createdWindow;

    public void setWindows(Patient patient, Notification notification) {
        this.patient = patient;
        this.notification = notification;
        this.createdWindow = notification.getId() < 1;

        if(!createdWindow) {
            actionArea.setText(notification.getAction());
            executedBox.setSelected(notification.isExecuted());
            datePicker.setValue(notification.getDate().toLocalDate());
            patientNameLabel.setText("Пациент - " + patient.getName());
            directedCheckBox.setSelected(notification.isDirected());
            switchEditAbility(new ActionEvent());
        }

        endActionButton.setText(createdWindow ? "Добавить" : "Сохранить");
    }

    public void endAction(ActionEvent actionEvent) {
        String action = actionArea.getText();
        boolean directed = directedCheckBox.isSelected();
        boolean executed = executedBox.isSelected();

        Date date = directed ? Date.valueOf(LocalDate.now()) : Date.valueOf(datePicker.getValue());

        if(createdWindow) {
            new NotificationDAO().save(new Notification(0, patient.getId(), date, action, executed, directed));
        } else {
            notification.setAction(action);
            notification.setDate(date);
            notification.setExecuted(executed);
            notification.setDirected(directed);
            new NotificationDAO().update(notification);
        }

        Stage stage = (Stage) actionArea.getScene().getWindow();
        stage.close();
    }

    public void switchEditAbility(ActionEvent actionEvent) {
        datePicker.setDisable(directedCheckBox.isSelected());
    }
}
