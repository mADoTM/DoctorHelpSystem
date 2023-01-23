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
        }

        endActionButton.setText(createdWindow ? "Добавить" : "Сохранить");
    }

    public void endAction(ActionEvent actionEvent) {
        String action = actionArea.getText();
        Date date = Date.valueOf(datePicker.getValue());
        boolean executed = executedBox.isSelected();

        if(createdWindow) {
            new NotificationDAO().save(new Notification(0, patient.getId(), date, action, executed));
        } else {
            notification.setAction(action);
            notification.setDate(date);
            notification.setExecuted(executed);
            new NotificationDAO().update(notification);
        }

        Stage stage = (Stage) actionArea.getScene().getWindow();
        stage.close();
    }
}
