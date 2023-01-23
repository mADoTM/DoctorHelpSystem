package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.dao.AnalysisResultDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.NotificationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.PatientDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.TelephoneSurveyDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.AnalysisResult;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import com.dolzhenko_m_s.doctorhelpsystem.models.TelephoneSurvey;
import com.dolzhenko_m_s.doctorhelpsystem.services.AnalysisResultService;
import com.dolzhenko_m_s.doctorhelpsystem.services.NotificationService;
import com.dolzhenko_m_s.doctorhelpsystem.services.TelephoneSurveyService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class PatientCardController {
    public TextField nameTextField;
    public TextField phoneTextField;
    public TextField addressTextField;
    public TextArea diagnosisTextField;

    public CheckBox editCheckBox;
    public Button saveButton;
    public Button telephoneSurveyButton;

    public TableView<TelephoneSurvey> surveyTable;
    public TableView<Notification> notificationTable;
    public TableView<AnalysisResult> analysisTable;

    private final ObservableList<TelephoneSurvey> surveyObservableList = FXCollections.observableArrayList();

    private final ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();

    private final ObservableList<AnalysisResult> analysisResultObservableListObservableList = FXCollections.observableArrayList();
    public Button notificationButton;
    public Button addAnalysisButton;
    public Button removeAnalysisButton;
    public Button removeSurveyButton;
    public Button removeNotificationButton;
    public TextArea remarkArea;
    public DatePicker datePicker;
    public TextField clinicalCodeField;

    private Patient patient;



    public void setPatient(Patient patient) {
        this.patient = patient;

        nameTextField.setText(patient.getName());
        phoneTextField.setText(patient.getPhoneNumber());
        addressTextField.setText(patient.getAddress());
        clinicalCodeField.setText(patient.getClinicalCode());
        diagnosisTextField.setText(patient.getDiagnosis());
        remarkArea.setText(patient.getRemark());
        datePicker.setValue(patient.getBirthDate().toLocalDate());



        initSurveyTable(new TelephoneSurveyService().getByPatientId((int) patient.getId()));
        initNotificationTable(new NotificationService().getByPatientId((int) patient.getId()));
        initAnalysisTable(new AnalysisResultService().getByPatientId((int) patient.getId()));
    }


    public void switchEditAbility(ActionEvent actionEvent) {
        nameTextField.setEditable(editCheckBox.isSelected());
        phoneTextField.setEditable(editCheckBox.isSelected());
        diagnosisTextField.setEditable(editCheckBox.isSelected());
        addressTextField.setEditable(editCheckBox.isSelected());
        clinicalCodeField.setEditable(editCheckBox.isSelected());
        saveButton.setDisable(!editCheckBox.isSelected());
        remarkArea.setDisable(!editCheckBox.isSelected());
        datePicker.setDisable(!editCheckBox.isSelected());
    }

    public void savePatient(ActionEvent actionEvent) {
        if(!editCheckBox.isSelected()) return;

        patient.setName(nameTextField.getText());
        patient.setAddress(addressTextField.getText());
        patient.setClinicalCode(clinicalCodeField.getText());
        patient.setDiagnosis(diagnosisTextField.getText());
        patient.setPhoneNumber(phoneTextField.getText());
        patient.setBirthDate(Date.valueOf(datePicker.getValue()));
        patient.setRemark(remarkArea.getText());

        new PatientDAO().update(patient);
    }

    private void initSurveyTable(List<TelephoneSurvey> list) {
        setSurveyTableAppearance();
        fillSurveyTable(list);
        surveyTable.setItems(surveyObservableList);

        TableColumn<TelephoneSurvey, Date> colDate = new TableColumn<>("Выполнен");
        colDate.setCellValueFactory(new PropertyValueFactory<>("executedDate"));

        TableColumn<TelephoneSurvey, Date> colNext = new TableColumn<>("Следующий");
        colNext.setCellValueFactory(new PropertyValueFactory<>("nextSurvey"));

        surveyTable.getColumns().addAll(colDate, colNext);

        addButtonToSurveyTable();
    }

    private void fillSurveyTable(List<TelephoneSurvey> surveys) {
        surveyObservableList.addAll(surveys);
    }

    private void setSurveyTableAppearance() {
        surveyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void addButtonToSurveyTable() {
        TableColumn<TelephoneSurvey, Void> colBtn = new TableColumn<>("Подробно");

        Callback<TableColumn<TelephoneSurvey, Void>, TableCell<TelephoneSurvey, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<TelephoneSurvey, Void> call(final TableColumn<TelephoneSurvey, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("Подробно");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            TelephoneSurvey data = getTableView().getItems().get(getIndex());
                            openSurveyWindow(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colBtn.setCellFactory(cellFactory);

        surveyTable.getColumns().add(colBtn);
    }

    private void initNotificationTable(List<Notification> list) {
        setNotificationTableAppearance();
        fillNotificationTable(list);
        notificationTable.setItems(notificationObservableList);

        TableColumn<Notification, Date> colId = new TableColumn<>("Выполнить до");
        colId.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Notification, Boolean> colExecuted = new TableColumn<>("Выполнено");
        colExecuted.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isExecuted()));

        colExecuted.setCellFactory(col -> new TableCell<Notification, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty) ;
                setText(empty ? null : item ? "Да" : "Нет" );
            }
        });

        notificationTable.getColumns().addAll(colId, colExecuted);

        notificationTable.setRowFactory(tv -> new TableRow<Notification>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.isExecuted())
                    setStyle("-fx-background-color: #16e016;");
                else
                    setStyle("");
            }
        });

        addButtonToNotificationTable();
    }


    private void setNotificationTableAppearance() {
        notificationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void fillNotificationTable(List<Notification> notifications) {
        notificationObservableList.addAll(notifications);
    }

    private void addButtonToNotificationTable() {
        TableColumn<Notification, Void> colBtn = new TableColumn<>("Подробно");

        Callback<TableColumn<Notification, Void>, TableCell<Notification, Void>> cellFactory = new Callback<TableColumn<Notification, Void>, TableCell<Notification, Void>>() {
            @Override
            public TableCell<Notification, Void> call(final TableColumn<Notification, Void> param) {
                return new TableCell<Notification, Void>() {

                    private final Button btn = new Button("Подробно");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Notification data = getTableView().getItems().get(getIndex());
                            openNotificationWindow(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colBtn.setCellFactory(cellFactory);

        notificationTable.getColumns().add(colBtn);
    }

    private void initAnalysisTable(List<AnalysisResult> list) {
        setAnalysisTableAppearance();
        fillAnalysisTable(list);
        analysisTable.setItems(analysisResultObservableListObservableList);

        TableColumn<AnalysisResult, String> colId = new TableColumn<>("Анализ");
        colId.setCellValueFactory(new PropertyValueFactory<>("analysisName"));

        TableColumn<AnalysisResult, String> colResult = new TableColumn<>("Результат");
        colResult.setCellValueFactory(new PropertyValueFactory<>("result"));

        TableColumn<AnalysisResult, Date> colExecuted = new TableColumn<>("Выполнен");
        colExecuted.setCellValueFactory(new PropertyValueFactory<>("executedAnalysisDate"));

        TableColumn<AnalysisResult, Date> colNext = new TableColumn<>("Следующий");
        colNext.setCellValueFactory(new PropertyValueFactory<>("nextAnalysisDate"));

        analysisTable.getColumns().addAll(colId, colResult, colExecuted, colNext);

        addButtonToAnalysisTable();
    }


    private void setAnalysisTableAppearance() {
        analysisTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void fillAnalysisTable(List<AnalysisResult> analysisResults) {
        analysisResultObservableListObservableList.addAll(analysisResults);
    }

    private void addButtonToAnalysisTable() {
        TableColumn<AnalysisResult, Void> colBtn = new TableColumn<>("Редактировать");

        Callback<TableColumn<AnalysisResult, Void>, TableCell<AnalysisResult, Void>> cellFactory = new Callback<TableColumn<AnalysisResult, Void>, TableCell<AnalysisResult, Void>>() {
            @Override
            public TableCell<AnalysisResult, Void> call(final TableColumn<AnalysisResult, Void> param) {
                return new TableCell<AnalysisResult, Void>() {

                    private final Button btn = new Button("Подробно");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AnalysisResult data = getTableView().getItems().get(getIndex());
                            openAnalysisResultWindow(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colBtn.setCellFactory(cellFactory);

        analysisTable.getColumns().add(colBtn);
    }

    public void removeEntity(ActionEvent actionEvent) {
        var button = (Button) actionEvent.getSource();

        if(button == removeSurveyButton) {
            if(surveyTable.getSelectionModel().getSelectedItem() != null) {
                var survey = surveyTable.getSelectionModel().getSelectedItem();
                new TelephoneSurveyDAO().delete(survey);
            }
        }

        if(button == removeNotificationButton) {
            if(notificationTable.getSelectionModel().getSelectedItem() != null) {
                var notification = notificationTable.getSelectionModel().getSelectedItem();
                new NotificationDAO().delete(notification);
            }
        }

        if(button == removeAnalysisButton) {
            if(analysisTable.getSelectionModel().getSelectedItem() != null) {
                var analysisResult = analysisTable.getSelectionModel().getSelectedItem();
                new AnalysisResultDAO().delete(analysisResult);
            }
        }


        refreshItemsInTables();
    }

    private void refreshItemsInTables() {
        surveyTable.getItems().clear();
        fillSurveyTable(new TelephoneSurveyService().getByPatientId((int) patient.getId()));

        notificationTable.getItems().clear();
        fillNotificationTable(new NotificationService().getByPatientId((int) patient.getId()));

        analysisTable.getItems().clear();
        fillAnalysisTable(new AnalysisResultService().getByPatientId((int) patient.getId()));
    }

    public void addEntity(ActionEvent actionEvent) {
        var button = (Button) actionEvent.getSource();

        if(button == notificationButton) {
            openNotificationWindow(new Notification());
        }

        if(button == addAnalysisButton) {
            openAnalysisResultWindow(new AnalysisResult());
        }

        if(button == telephoneSurveyButton) {
            openSurveyWindow(new TelephoneSurvey());
        }
    }

    private void openNotificationWindow(Notification notification) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notification.fxml"));
        try {
            Parent root = loader.load();
            var notificationController = (NotificationController) loader.getController();
            notificationController.setWindows(patient, notification);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openAnalysisResultWindow(AnalysisResult analysisResult) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("analysis.fxml"));
        try {
            Parent root = loader.load();
            var analysisResultController = (AnalysisController) loader.getController();
            analysisResultController.setWindows(patient, analysisResult);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openSurveyWindow(TelephoneSurvey survey) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("survey.fxml"));
        try {
            Parent root = loader.load();
            var analysisResultController = (TelephoneSurveyController) loader.getController();
            analysisResultController.setWindows(patient, survey);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshEntities(ActionEvent actionEvent) {
        refreshItemsInTables();
    }
}
