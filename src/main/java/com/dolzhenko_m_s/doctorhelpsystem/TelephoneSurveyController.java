package com.dolzhenko_m_s.doctorhelpsystem;

import com.dolzhenko_m_s.doctorhelpsystem.dao.HospitalizationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.NotificationDAO;
import com.dolzhenko_m_s.doctorhelpsystem.dao.TelephoneSurveyDAO;
import com.dolzhenko_m_s.doctorhelpsystem.models.Hospitalization;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import com.dolzhenko_m_s.doctorhelpsystem.models.TelephoneSurvey;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;

public class TelephoneSurveyController {
    public Button saveButton;
    public DatePicker executedDate;
    public ChoiceBox<String> hasHospitalization;
    public ChoiceBox<String> isEmergency;
    public TextField countHospitalizations;
    public ChoiceBox<String> isIndependent;
    public ChoiceBox<String> hasFamily;
    public ChoiceBox<String> progressiveDyspnea;
    public ChoiceBox<String> isEdemaIncrease;
    public ChoiceBox<String> hasHeartInterruptions;
    public ChoiceBox<String> hasPainDuringPhysicalExercise;
    public ChoiceBox<String> hasEveryDayControl;
    public TextField mass;
    public ChoiceBox<String> isMassChanged;
    public TextField upperBloodPressure;
    public TextField lowerBloodPressure;
    public TextField heartRate;
    public ChoiceBox<String> regularlyTakePills;
    public ChoiceBox<String> forgotTakePills;
    public ChoiceBox<String> drinkingWater1500ml;
    public ChoiceBox<String> isSaltRestricted;
    public ChoiceBox<String> decreasedExerciseTolerance;
    public ChoiceBox<String> isSmoking;
    public ChoiceBox<String> isDrinkingAlcohol;
    public ChoiceBox<String> treatPlan;
    public DatePicker nextSurvey;
    public TextField hospitalizationReason;
    private Patient patient;
    private TelephoneSurvey telephoneSurvey;

    private boolean createdWindow;

    public void setWindows(Patient patient, TelephoneSurvey telephoneSurvey) {
        this.patient = patient;
        this.telephoneSurvey = telephoneSurvey;
        this.createdWindow = telephoneSurvey.getId() < 1;

        if (!createdWindow) {
            executedDate.setValue(telephoneSurvey.getExecutedDate().toLocalDate());
            if (telephoneSurvey.getHospitalizationId() > 0) {
                var hospitalization = new HospitalizationDAO()
                        .get(telephoneSurvey.getHospitalizationId());
                setBoxValueByValue(hasHospitalization, true);
                if (hospitalization.isEmergency()) {
                    isEmergency.setValue("Экстренные");
                } else {
                    isEmergency.setValue("Плановые");
                }
                countHospitalizations.setText(hospitalization.getCount() + "");
                hospitalizationReason.setText(hospitalization.getReason());
            }

            setBoxValueByValue(isIndependent, this.telephoneSurvey.isIndependent());
            setBoxValueByValue(hasFamily, this.telephoneSurvey.isHasFamily());
            setBoxValueByValue(progressiveDyspnea, this.telephoneSurvey.isDyspneaProgressive());
            setBoxValueByValue(isEdemaIncrease, this.telephoneSurvey.isEdemaIncrease());
            setBoxValueByValue(hasHeartInterruptions, this.telephoneSurvey.isHasHeartInterruptions());
            setBoxValueByValue(hasPainDuringPhysicalExercise, this.telephoneSurvey.isHasPainDuringPhysicalExercise());
            setBoxValueByValue(hasEveryDayControl, this.telephoneSurvey.isHasEveryDayControl());
            mass.setText(this.telephoneSurvey.getMass() + "");
            heartRate.setText(telephoneSurvey.getHeartRate() + "");
            setBoxValueByValue(isMassChanged, this.telephoneSurvey.isMassChanged());
            upperBloodPressure.setText(this.telephoneSurvey.getUpperBloodPressure() + "");
            lowerBloodPressure.setText(this.telephoneSurvey.getLowerBloodPressure() + "");
            setBoxValueByValue(regularlyTakePills, this.telephoneSurvey.isRegularlyTakePills());
            setBoxValueByValue(forgotTakePills, this.telephoneSurvey.isForgotTakePills());
            setBoxValueByValue(drinkingWater1500ml, this.telephoneSurvey.isDrinkingWater1500ml());
            setBoxValueByValue(isSaltRestricted, this.telephoneSurvey.isSaltRestricted());
            setBoxValueByValue(isSmoking, this.telephoneSurvey.isSmoking());
            setBoxValueByValue(isDrinkingAlcohol, this.telephoneSurvey.isDrinkingAlcohol());
            nextSurvey.setValue(telephoneSurvey.getNextSurvey().toLocalDate());
            treatPlan.setValue(telephoneSurvey.getTreatPlan());
        } else {
            countHospitalizations.setEditable(false);
            hospitalizationReason.setEditable(false);
            isEmergency.setDisable(true);
        }

        switchHospitalization(new ActionEvent());
        telephoneSurvey.setPatientId((int) patient.getId());
        saveButton.setText(createdWindow ? "Добавить" : "Сохранить");
    }


    private void setBoxValueByValue(ChoiceBox<String> box, boolean value) {
        box.setValue(value ? "Да" : "Нет");
    }

    private boolean getBooleanByAnswer(ChoiceBox<String> box) {
        return box.getValue().equals("Да");
    }

    public void switchHospitalization(ActionEvent actionEvent) {
        boolean flag = hasHospitalization.getValue().equals("Да");
        isEmergency.setDisable(!flag);
        countHospitalizations.setEditable(flag);
        hospitalizationReason.setEditable(flag);
    }

    public void saveSurvey(ActionEvent actionEvent) {
        try {
            if (hasHospitalization.getValue().equals("Нет")) {
                if (telephoneSurvey.getHospitalizationId() > 0) {
                    var hospitalization = new HospitalizationDAO()
                            .get(telephoneSurvey.getHospitalizationId());
                    new HospitalizationDAO().delete(hospitalization);
                }
                telephoneSurvey.setHospitalizationId(-1);
            } else {
                if (telephoneSurvey.getHospitalizationId() < 1) {
                    var hospitalization = new Hospitalization();
                    hospitalization.setCount(Integer.parseInt(countHospitalizations.getText()));
                    hospitalization.setReason(hospitalizationReason.getText());
                    hospitalization.setEmergency(isEmergency.getValue().equals("Экстренные"));
                    hospitalization = new HospitalizationDAO().save(hospitalization);
                    telephoneSurvey.setHospitalizationId((int) hospitalization.getId());
                } else {
                    var hospitalization = new HospitalizationDAO()
                            .get(telephoneSurvey.getHospitalizationId());
                    new HospitalizationDAO().update(hospitalization);
                }
            }

            telephoneSurvey.setExecutedDate(Date.valueOf(executedDate.getValue()));
            telephoneSurvey.setIndependent(getBooleanByAnswer(isIndependent));
            telephoneSurvey.setHasFamily(getBooleanByAnswer(hasFamily));
            telephoneSurvey.setDyspneaProgressive(getBooleanByAnswer(progressiveDyspnea));
            telephoneSurvey.setEdemaIncrease(getBooleanByAnswer(isEdemaIncrease));
            telephoneSurvey.setHasHeartInterruptions(getBooleanByAnswer(hasHeartInterruptions));
            telephoneSurvey.setHasPainDuringPhysicalExercise(getBooleanByAnswer(hasPainDuringPhysicalExercise));
            telephoneSurvey.setHasEveryDayControl(getBooleanByAnswer(hasEveryDayControl));
            telephoneSurvey.setMass(Double.parseDouble(mass.getText()));
            telephoneSurvey.setMassChanged(getBooleanByAnswer(isMassChanged));
            telephoneSurvey.setUpperBloodPressure(Integer.parseInt(upperBloodPressure.getText()));
            telephoneSurvey.setLowerBloodPressure(Integer.parseInt(lowerBloodPressure.getText()));
            telephoneSurvey.setHeartRate(Integer.parseInt(heartRate.getText()));
            telephoneSurvey.setRegularlyTakePills(getBooleanByAnswer(regularlyTakePills));
            telephoneSurvey.setForgotTakePills(getBooleanByAnswer(forgotTakePills));
            telephoneSurvey.setDrinkingWater1500ml(getBooleanByAnswer(drinkingWater1500ml));
            telephoneSurvey.setSaltRestricted(getBooleanByAnswer(isSaltRestricted));
            telephoneSurvey.setDecreasedExerciseTolerance(getBooleanByAnswer(decreasedExerciseTolerance));
            telephoneSurvey.setSmoking(getBooleanByAnswer(isSmoking));
            telephoneSurvey.setDrinkingAlcohol(getBooleanByAnswer(isDrinkingAlcohol));
            telephoneSurvey.setTreatPlan(treatPlan.getValue());

            if (nextSurvey.getValue() == null) {
                telephoneSurvey.setNextSurvey(Date.valueOf(executedDate.getValue().plusYears(1)));
            } else {
                telephoneSurvey.setNextSurvey(Date.valueOf(nextSurvey.getValue()));
            }

            if (createdWindow) {
                new TelephoneSurveyDAO().save(telephoneSurvey);
            } else {
                new TelephoneSurveyDAO().update(telephoneSurvey);
            }

            new NotificationDAO().save(new Notification(0, telephoneSurvey.getPatientId(), telephoneSurvey.getNextSurvey(), "телефонный опрос", false));

            Stage stage = (Stage) isMassChanged.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Вы ввели некорретные данные");
            alert.show();
        }
    }
}
