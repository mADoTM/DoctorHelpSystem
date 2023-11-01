package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.dao.NotificationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.PatientDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import com.dolzhenko_m_s.doctorhelpsystem.services.NotificationService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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

    public TableView<Patient> patientTable;

    public TableView<Notification> notificationTable;

    public Button patientsList;

    public ChoiceBox<String> searchType;
    public TextField searchTag;
    public Button addPatientButton;
    public Button removePatientButton;
    public Label allPatientsSize;
    public Button openSettingsButton;

    @FXML
    private void initialize() {
        initPatientTable();
    }

    private void initPatientTable() {
        setPatientTableAppearance();
        fillPatientTable(new PatientDAO().all());
        patientTable.setItems(patientObservableList);

        TableColumn numberCol = new TableColumn("№");
        numberCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Patient, String>, ObservableValue<String>>) p -> new ReadOnlyObjectWrapper(patientTable.getItems().indexOf(p.getValue()) + 1 + ""));
        numberCol.setSortable(false);

        TableColumn<Patient, Integer> colId = new TableColumn<>("ФИО");
        colId.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Patient, String> colName = new TableColumn<>("Телефон");
        colName.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Patient, Integer> colBirthDate = new TableColumn<>("Дата рождения");
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        TableColumn<Patient, String> colRemark = new TableColumn<>("Примечание");
        colRemark.setCellValueFactory(new PropertyValueFactory<>("remark"));

        patientTable.getColumns().addAll(numberCol, colId, colName, colBirthDate, colRemark);

        patientTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.isHasLowAnalysis())
                    setStyle("-fx-background-color: #e50707;");
                else
                    setStyle("");
            }
        });

        addButtonToPatientTable();
        patientTable.setVisible(false);
    }

    private void setPatientTableAppearance() {
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        patientTable.setPrefWidth(600);
        patientTable.setPrefHeight(400);
    }

    private void fillPatientTable(List<Patient> patients) {
        patientObservableList.addAll(patients);
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
                            openNotificationWindow(new PatientDAO().get((int) data.getPatientId()), data);
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
        if (actionEvent.getSource() instanceof final Button button) {
            if (button.getId().equals("patientsList")) {
                patientTable.setVisible(!patientTable.isVisible());
                allPatientsSize.setText(patientTable.getItems().size() > 0 ? "Всего " + patientTable.getItems().size() : "");
                findPatients(new ActionEvent());
            }
        }
        System.out.println(allPatientsSize.getText());
    }

    public void findPatients(ActionEvent actionEvent) {
        var list = new PatientDAO().all();
        List<Patient> sortedList = null;
        if (searchTag.getText().equals("") || searchType.getValue() == null) {
            patientTable.getItems().clear();
            fillPatientTable(list);
            allPatientsSize.setVisible(true);
            allPatientsSize.setText("Всего " + patientTable.getItems().size());
            return;
        }

        if (searchType.getValue().equals("Телефон")) {
            sortedList = list.stream().filter(p -> p.getPhoneNumber().toLowerCase().contains(searchTag.getText())).toList();
        }

        if (searchType.getValue().equals("ФИО")) {
            sortedList = list.stream().filter(p -> p.getName().toLowerCase().contains(searchTag.getText())).toList();
        }

        if (sortedList != null && sortedList.isEmpty()) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Не было найдено пациентов по запросу: " + searchType.getValue() + " - " + searchTag.getText());
            alert.show();
        } else {
            patientTable.getItems().clear();
            fillPatientTable(sortedList);
            patientTable.setVisible(true);
            allPatientsSize.setVisible(true);
            allPatientsSize.setText("Всего " + patientTable.getItems().size());
        }
    }

    public void addEntity(ActionEvent actionEvent) {
        var button = (Button) actionEvent.getSource();
        if (button == addPatientButton) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("patient.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

        refreshItemsInTables();
    }


    private void refreshItemsInTables() {
        findPatients(new ActionEvent());
    }

    @FXML
    private void showAllNotifications(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notification_table_menu.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openSettings(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}