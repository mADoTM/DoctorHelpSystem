package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.dao.AnalysisResultDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.NotificationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.PatientDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.TelephoneSurveyDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import com.dolzhenko_m_s.doctorhelpsystem.services.AnalysisResultService;
import com.dolzhenko_m_s.doctorhelpsystem.services.NotificationService;
import com.dolzhenko_m_s.doctorhelpsystem.services.TelephoneSurveyService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class MainController {
    private final ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();

    private final ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();

    public TableView<Patient> patientTable;

    public TableView<Notification> notificationTable;

    public Button patientsList;

    public Button notificationsList;
    public ChoiceBox<String> searchType;
    public TextField searchTag;
    public Button addPatientButton;
    public Button addNotificationButton;
    public Button removeNotificationButton;
    public Button removePatientButton;

    @FXML
    private void initialize() {
        initPatientTable();
        initNotificationTable();
    }

    private void initNotificationTable() {
        setNotificationTableAppearance();
        fillNotificationTable(new NotificationService().getByMonthAfter(Date.valueOf(LocalDate.now())));
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

        addButtonToNotificationTable();
        notificationTable.setVisible(false);
    }

    private void initPatientTable() {
        setPatientTableAppearance();
        fillPatientTable(new PatientDAO().all());
        patientTable.setItems(patientObservableList);

        TableColumn<Patient, Integer> colId = new TableColumn<>("ФИО");
        colId.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Patient, String> colName = new TableColumn<>("Телефон");
        colName.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        patientTable.getColumns().addAll(colId, colName);

        addButtonToPatientTable();
        patientTable.setVisible(false);
    }

    private void setNotificationTableAppearance() {
        notificationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        notificationTable.setPrefWidth(600);
        notificationTable.setPrefHeight(400);
    }

    private void setPatientTableAppearance() {
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        patientTable.setPrefWidth(600);
        patientTable.setPrefHeight(400);
    }

    private void fillPatientTable(List<Patient> patients) {
        patientObservableList.addAll(patients);
    }

    private void fillNotificationTable(List<Notification> notifications) {
        notificationObservableList.addAll(notifications);
    }

    private void addButtonToPatientTable() {
        TableColumn<Patient, Void> colBtn = new TableColumn<>("Мед. карта");

        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                return new TableCell<Patient, Void>() {

                    private final Button btn = new Button("Подробно");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Patient data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-card.fxml"));
                            try {
                                Parent root = loader.load();
                                var medController = (PatientCardController) loader.getController();
                                medController.setPatient(data);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setMinWidth(700);
                                stage.setTitle("Медицинская карта пациента " + data.getName());
                                stage.show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
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

        patientTable.getColumns().add(colBtn);

    }

    private void addButtonToNotificationTable() {
        TableColumn<Notification, Void> colBtn = new TableColumn<>("");

        Callback<TableColumn<Notification, Void>, TableCell<Notification, Void>> cellFactory = new Callback<TableColumn<Notification, Void>, TableCell<Notification, Void>>() {
            @Override
            public TableCell<Notification, Void> call(final TableColumn<Notification, Void> param) {
                return new TableCell<Notification, Void>() {

                    private final Button btn = new Button("Подробно");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Notification data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
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

    public void showPatients(ActionEvent actionEvent) {
        if(actionEvent.getSource() instanceof final Button button) {
            if(button.getId().equals("patientsList")) {
                patientTable.setVisible(!patientTable.isVisible());
            }
            if(button.getId().equals("notificationsList")) {
                notificationTable.setVisible(!notificationTable.isVisible());

                notificationTable.getItems().clear();
                fillNotificationTable(new NotificationService().getByMonthAfter(Date.valueOf(LocalDate.now())));
            }
        }
    }

    public void findPatients(ActionEvent actionEvent) {
        var list = new PatientDAO().all();
        List<Patient> sortedList = null;
        if(searchTag.getText().equals("") || searchType.getValue() == null) {
            patientTable.getItems().clear();
            fillPatientTable(list);
            return;
        }

        if(searchType.getValue().equals("Телефон")) {
            sortedList = list.stream().filter(p -> p.getPhoneNumber().startsWith(searchTag.getText())).toList();
        }

        if(searchType.getValue().equals("ФИО")) {
            sortedList = list.stream().filter(p -> p.getName().startsWith(searchTag.getText())).toList();
        }

        if(sortedList != null && sortedList.isEmpty()) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не было найдено пациентов по запросу: " + searchType.getValue() + " - " + searchTag.getText());
            alert.show();
        } else {
            patientTable.getItems().clear();
            fillPatientTable(sortedList);
            patientTable.setVisible(true);
        }
    }

    public void addEntity(ActionEvent actionEvent) {
        var button = (Button) actionEvent.getSource();
        if(button == addPatientButton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("patient.fxml"));
            try {
                Parent root = loader.load();
                var medController = (PatientController) loader.getController();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        var selectedItemInPatientTable = patientTable.getSelectionModel().getSelectedItem();

        if(button == addNotificationButton && selectedItemInPatientTable != null) {
            openNotificationWindow(selectedItemInPatientTable, new Notification());
        }
    }

    private void openNotificationWindow(Patient patient, Notification notification) {
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

    public void removeEntity(ActionEvent actionEvent) {
        var button = (Button) actionEvent.getSource();

        if (button == removePatientButton) {
            if (patientTable.getSelectionModel().getSelectedItem() != null) {
                var patient = patientTable.getSelectionModel().getSelectedItem();
                new PatientDAO().delete(patient);
            }
        }

        if (button == removeNotificationButton) {
            if (notificationTable.getSelectionModel().getSelectedItem() != null) {
                var notification = notificationTable.getSelectionModel().getSelectedItem();
                new NotificationDAO().delete(notification);
            }
        }

        refreshItemsInTables();
    }


    private void refreshItemsInTables() {
        findPatients(new ActionEvent());

        notificationTable.getItems().clear();
        fillNotificationTable(new NotificationService().getByMonthAfter(Date.valueOf(LocalDate.now())));
    }
}