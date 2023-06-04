package com.dolzhenko_m_s.doctorhelpsystem;

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
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class NotificationTableMenuController {

    @FXML
    private TableView<Notification> notificationTable;

    private final ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        initNotificationTable();
    }

    private void initNotificationTable() {
        fillNotificationTable(new NotificationService().getByMonthAfter(Date.valueOf(LocalDate.now())));
        notificationTable.setItems(notificationObservableList);

        TableColumn numberCol = new TableColumn("№");
        numberCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Notification, String>, ObservableValue<String>>) p -> new ReadOnlyObjectWrapper(notificationTable.getItems().indexOf(p.getValue()) + 1 + ""));
        numberCol.setSortable(false);

        TableColumn<Notification, Date> colId = new TableColumn<>("Выполнить до");
        colId.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Notification, Boolean> colExecuted = new TableColumn<>("Выполнено");
        colExecuted.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isExecuted()));

        colExecuted.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Да" : "Нет");
            }
        });

        notificationTable.getColumns().addAll(numberCol, colId, colExecuted);

        notificationTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.isDirected())
                    setStyle("-fx-background-color: #1638e0;");
                else
                    setStyle("");

            }
        });

        addButtonToNotificationTable();
    }

    private void addButtonToNotificationTable() {
        TableColumn<Notification, Void> colBtn = new TableColumn<>("");

        Callback<TableColumn<Notification, Void>, TableCell<Notification, Void>> cellFactory = new Callback<TableColumn<Notification, Void>, TableCell<Notification, Void>>() {
            @Override
            public TableCell<Notification, Void> call(final TableColumn<Notification, Void> param) {
                return new TableCell<>() {

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

    private void fillNotificationTable(List<Notification> notifications) {
        notificationObservableList.addAll(notifications);
    }

    public void refreshItemsInTable(ActionEvent actionEvent) {
        fillNotificationTable(new NotificationService().getByMonthAfter(Date.valueOf(LocalDate.now())));
    }
}
