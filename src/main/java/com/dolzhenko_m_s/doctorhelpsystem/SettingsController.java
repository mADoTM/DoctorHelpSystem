package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.common.DbConnectionHelper;
import com.dolzhenko_m_s.doctorhelpsystem.common.JDBCCredentials;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.SQLException;

public class SettingsController {
    public Button endActionButton;
    public TextArea hostTextArea;

    @FXML
    public void initialize() {
        hostTextArea.setText(JDBCCredentials.DEFAULT.host);
    }

    public void endAction(ActionEvent actionEvent) throws SQLException {
        JDBCCredentials.DEFAULT.host = hostTextArea.getText();
        DbConnectionHelper.closeConnection();
    }
}
