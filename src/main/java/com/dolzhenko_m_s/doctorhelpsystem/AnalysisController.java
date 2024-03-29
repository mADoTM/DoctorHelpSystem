package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.dao.AnalysisResultDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.NotificationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.AnalysisResult;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;

public class AnalysisController {
    public ChoiceBox<String> analysisTypeBox;
    public TextField resultField;
    public DatePicker executedDatePicker;
    public DatePicker nextDatePicker;

    public Button endActionButton;
    public TextField lpField;
    public Label resultLabel;
    public Label lpLabel;
    public CheckBox possibleCheckBox;

    private Patient patient;
    private AnalysisResult analysisResult;

    private boolean createdWindow;

    public void setWindows(Patient patient, AnalysisResult analysisResult) {
        this.patient = patient;
        this.analysisResult = analysisResult;
        this.createdWindow = analysisResult.getId() < 1;


        resultLabel.setVisible(false);
        resultField.setVisible(false);
        lpField.setVisible(false);
        lpLabel.setVisible(false);

        if(!createdWindow) {
            analysisTypeBox.setValue(analysisResult.getAnalysisName());
            possibleCheckBox.setSelected(analysisResult.isPossible());
            if(analysisResult.getExecutedAnalysisDate() != null)
                executedDatePicker.setValue(analysisResult.getExecutedAnalysisDate().toLocalDate());
            if(analysisResult.getNextAnalysisDate() != null)
                nextDatePicker.setValue(analysisResult.getNextAnalysisDate().toLocalDate());

            String[] results = analysisResult.getResult().split(";");
            resultField.setText(results[0]);
            if(results.length > 1)
                lpField.setText(results[1]);
            switchResultAbility(new ActionEvent());
        }

        endActionButton.setText(createdWindow ? "Добавить" : "Сохранить");
    }

    public void endAction(ActionEvent actionEvent) {
        if(analysisTypeBox.getValue() == null) {
            return;
        }

        boolean possible = possibleCheckBox.isSelected();
        String analysisName = analysisTypeBox.getValue();
        Date executedDate = null;
        if(possible)
            executedDate = Date.valueOf(executedDatePicker.getValue());
        String result = "";
        if (analysisName.equals("ЭХО-КГ")) {
            result = resultField.getText() + ";" + lpField.getText();
        } else if (analysisName.equals("ТЕСТ 6MX") || analysisName.equals("NT-proBNP")) {
            result = resultField.getText();
        }
        Date nextDate = null;
        if(possible) {
            if(nextDatePicker.getValue() == null) {
                nextDate = Date.valueOf(executedDatePicker.getValue().plusYears(1));
            } else {
                nextDate = Date.valueOf(nextDatePicker.getValue());
            }
        }


        if(createdWindow) {
            new AnalysisResultDAO().save(new AnalysisResult(0, patient.getId(), analysisName, result, possible,executedDate, nextDate));
        } else {
            analysisResult.setAnalysisName(analysisName);
            analysisResult.setResult(result);
            analysisResult.setExecutedAnalysisDate(executedDate);
            analysisResult.setNextAnalysisDate(nextDate);
            new AnalysisResultDAO().update(analysisResult);
        }

        if(possible) {
            String action = "сдать " + analysisName;
            new NotificationDAO().save(new Notification(0, patient.getId(), nextDate, action, false, false));
        }

        Stage stage = (Stage) analysisTypeBox.getScene().getWindow();
        stage.close();
    }

    public void switchResultAbility(ActionEvent actionEvent) {
        String analysisType = analysisTypeBox.getValue();

        if(analysisType.equals("ЭХО-КГ")) {
            resultLabel.setText("ФВ");
            lpField.setVisible(true);
            lpLabel.setVisible(true);
        } else {
            resultLabel.setText("Результат");
            lpField.setVisible(false);
            lpLabel.setVisible(false);
        }

        final var resultedAnalysies = analysisType.equals("ЭХО-КГ") || analysisType.equals("ТЕСТ 6MX") || analysisType.equals("NT-proBNP");
        resultLabel.setVisible(resultedAnalysies);
        resultField.setVisible(resultedAnalysies);
    }

    public void setPossibleResult(ActionEvent actionEvent) {
        nextDatePicker.setEditable(possibleCheckBox.isSelected());
        executedDatePicker.setEditable(possibleCheckBox.isSelected());
        resultField.setEditable(possibleCheckBox.isSelected());
        lpField.setEditable(possibleCheckBox.isSelected());
    }
}
