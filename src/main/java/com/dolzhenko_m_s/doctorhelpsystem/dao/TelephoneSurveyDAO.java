package com.dolzhenko_m_s.doctorhelpsystem.dao;

import com.dolzhenko_m_s.doctorhelpsystem.common.DbConnectionHelper;
import com.dolzhenko_m_s.doctorhelpsystem.models.TelephoneSurvey;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TelephoneSurveyDAO {
    private final @NotNull String SELECT_ALL_TELEPHONE_SURVEY = "SELECT * FROM telephone_survey";

    private final @NotNull String GET_TELEPHONE_SURVEY_BY_ID = "SELECT * FROM telephone_survey WHERE telephone_survey_id = ";

    private final @NotNull String GET_TELEPHONE_SURVEY_BY_ALL_FIELDS = "SELECT * FROM telephone_survey WHERE executed_date = ? AND patient_id = ? AND hospitalization_id = ? AND  is_independent = ? AND has_family = ? AND is_dyspnea_progressive = ? AND is_idema_increase = ? AND has_heart_interruptions = ? AND has_pain_during_physical = ? AND has_everyday_control = ? AND upper_blood_pressure = ? AND lower_blood_pressure = ? AND heart_rate = ? AND mass = ? AND is_mass_changed = ? AND regularly_take_pills = ? AND forgot_take_pills = ? AND drinking_water_1500ml = ? AND is_salt_restricted = ? AND decreased_exercise_tolerance = ? AND is_smoking = ? AND is_drinking_alcohol = ? AND treat_plan = ? AND next_survey_date = ?";

    private final @NotNull String SAVE_TELEPHONE_SURVEY = "INSERT INTO telephone_survey (executed_date, patient_id, hospitalization_id, is_independent, has_family, is_dyspnea_progressive, is_idema_increase, has_heart_interruptions, has_pain_during_physical, has_everyday_control, upper_blood_pressure, lower_blood_pressure, heart_rate, mass, is_mass_changed, regularly_take_pills, forgot_take_pills, drinking_water_1500ml, is_salt_restricted, decreased_exercise_tolerance, is_smoking, is_drinking_alcohol, treat_plan, next_survey_date) VALUES (?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private final @NotNull String UPDATE_ANALYSIS_RESULT = "UPDATE telephone_survey SET executed_date = ?, patient_id = ?, hospitalization_id = ?, is_independent = ?, has_family = ?, is_dyspnea_progressive = ?, is_idema_increase = ?, has_heart_interruptions = ?, has_pain_during_physical = ?, has_everyday_control = ?, upper_blood_pressure = ?, lower_blood_pressure = ?, heart_rate = ?, mass = ?, is_mass_changed = ?, regularly_take_pills = ?, forgot_take_pills = ?, drinking_water_1500ml = ?, is_salt_restricted = ?, decreased_exercise_tolerance = ?, is_smoking = ?, is_drinking_alcohol = ?, treat_plan = ?, next_survey_date = ? WHERE telephone_survey_id = ?";

    private final @NotNull String REMOVE_TELEPHONE_SURVEY = "DELETE FROM telephone_survey WHERE telephone_survey_id = ?";

    public @NotNull TelephoneSurvey get(int id) {
        try (var statement = DbConnectionHelper.getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(GET_TELEPHONE_SURVEY_BY_ID + id)) {
                if (resultSet.next()) {
                    return new TelephoneSurvey(resultSet.getInt("telephone_survey_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getDate("executed_date"),
                            resultSet.getInt("hospitalization_id"),
                            resultSet.getBoolean("is_independent"),
                            resultSet.getBoolean("has_family"),
                            resultSet.getBoolean("is_dyspnea_progressive"),
                            resultSet.getBoolean("is_idema_increase"),
                            resultSet.getBoolean("has_heart_interruptions"),
                            resultSet.getBoolean("has_pain_during_physical"),
                            resultSet.getBoolean("has_everyday_control"),
                            resultSet.getDouble("upper_blood_pressure"),
                            resultSet.getDouble("lower_blood_pressure"),
                            resultSet.getInt("heart_rate"),
                            resultSet.getDouble("mass"),
                            resultSet.getBoolean("is_mass_changed"),
                            resultSet.getBoolean("regularly_take_pills"),
                            resultSet.getBoolean("forgot_take_pills"),
                            resultSet.getBoolean("drinking_water_1500ml"),
                            resultSet.getBoolean("is_salt_restricted"),
                            resultSet.getBoolean("decreased_exercise_tolerance"),
                            resultSet.getBoolean("is_smoking"),
                            resultSet.getBoolean("is_drinking_alcohol"),
                            resultSet.getString("treat_plan"),
                            resultSet.getDate("next_survey_date"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    public @NotNull List<TelephoneSurvey> all() {
        final var result = new ArrayList<TelephoneSurvey>();
        try (var statement = DbConnectionHelper
                .getConnection()
                .createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_TELEPHONE_SURVEY)) {
                while (resultSet.next()) {
                    result.add(new TelephoneSurvey(resultSet.getInt("telephone_survey_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getDate("executed_date"),
                            resultSet.getInt("hospitalization_id"),
                            resultSet.getBoolean("is_independent"),
                            resultSet.getBoolean("has_family"),
                            resultSet.getBoolean("is_dyspnea_progressive"),
                            resultSet.getBoolean("is_idema_increase"),
                            resultSet.getBoolean("has_heart_interruptions"),
                            resultSet.getBoolean("has_pain_during_physical"),
                            resultSet.getBoolean("has_everyday_control"),
                            resultSet.getDouble("upper_blood_pressure"),
                            resultSet.getDouble("lower_blood_pressure"),
                            resultSet.getInt("heart_rate"),
                            resultSet.getDouble("mass"),
                            resultSet.getBoolean("is_mass_changed"),
                            resultSet.getBoolean("regularly_take_pills"),
                            resultSet.getBoolean("forgot_take_pills"),
                            resultSet.getBoolean("drinking_water_1500ml"),
                            resultSet.getBoolean("is_salt_restricted"),
                            resultSet.getBoolean("decreased_exercise_tolerance"),
                            resultSet.getBoolean("is_smoking"),
                            resultSet.getBoolean("is_drinking_alcohol"),
                            resultSet.getString("treat_plan"),
                            resultSet.getDate("next_survey_date")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public TelephoneSurvey save(@NotNull TelephoneSurvey entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(SAVE_TELEPHONE_SURVEY)) {
            preparedStatement.setInt(2, entity.getPatientId());
            preparedStatement.setDate(1, entity.getExecutedDate());
            preparedStatement.setInt(3, entity.getHospitalizationId());
            preparedStatement.setBoolean(4, entity.isIndependent());
            preparedStatement.setBoolean(5, entity.isHasFamily());
            preparedStatement.setBoolean(6, entity.isDyspneaProgressive());
            preparedStatement.setBoolean(7, entity.isEdemaIncrease());
            preparedStatement.setBoolean(8, entity.isHasHeartInterruptions());
            preparedStatement.setBoolean(9, entity.isHasPainDuringPhysicalExercise());
            preparedStatement.setBoolean(10, entity.isHasEveryDayControl());
            preparedStatement.setDouble(11, entity.getUpperBloodPressure());
            preparedStatement.setDouble(12, entity.getLowerBloodPressure());
            preparedStatement.setInt(13, entity.getHeartRate());
            preparedStatement.setDouble(14, entity.getMass());
            preparedStatement.setBoolean(15, entity.isMassChanged());
            preparedStatement.setBoolean(16, entity.isRegularlyTakePills());
            preparedStatement.setBoolean(17, entity.isForgotTakePills());
            preparedStatement.setBoolean(18, entity.isDrinkingWater1500ml());
            preparedStatement.setBoolean(19, entity.isSaltRestricted());
            preparedStatement.setBoolean(20, entity.isDecreasedExerciseTolerance());
            preparedStatement.setBoolean(21, entity.isSmoking());
            preparedStatement.setBoolean(22, entity.isDrinkingAlcohol());
            preparedStatement.setString(23, entity.getTreatPlan());
            preparedStatement.setDate(24, entity.getNextSurvey());
            preparedStatement.executeUpdate();

            return(getByFields(entity));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public void update(@NotNull TelephoneSurvey entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(UPDATE_ANALYSIS_RESULT)) {
            preparedStatement.setInt(2, entity.getPatientId());
            preparedStatement.setDate(1, entity.getExecutedDate());
            preparedStatement.setInt(3, entity.getHospitalizationId());
            preparedStatement.setBoolean(4, entity.isIndependent());
            preparedStatement.setBoolean(5, entity.isHasFamily());
            preparedStatement.setBoolean(6, entity.isDyspneaProgressive());
            preparedStatement.setBoolean(7, entity.isEdemaIncrease());
            preparedStatement.setBoolean(8, entity.isHasHeartInterruptions());
            preparedStatement.setBoolean(9, entity.isHasPainDuringPhysicalExercise());
            preparedStatement.setBoolean(10, entity.isHasEveryDayControl());
            preparedStatement.setDouble(11, entity.getUpperBloodPressure());
            preparedStatement.setDouble(12, entity.getLowerBloodPressure());
            preparedStatement.setInt(13, entity.getHeartRate());
            preparedStatement.setDouble(14, entity.getMass());
            preparedStatement.setBoolean(15, entity.isMassChanged());
            preparedStatement.setBoolean(16, entity.isRegularlyTakePills());
            preparedStatement.setBoolean(17, entity.isForgotTakePills());
            preparedStatement.setBoolean(18, entity.isDrinkingWater1500ml());
            preparedStatement.setBoolean(19, entity.isSaltRestricted());
            preparedStatement.setBoolean(20, entity.isDecreasedExerciseTolerance());
            preparedStatement.setBoolean(21, entity.isSmoking());
            preparedStatement.setBoolean(22, entity.isDrinkingAlcohol());
            preparedStatement.setString(23, entity.getTreatPlan());
            preparedStatement.setDate(24, entity.getNextSurvey());
            preparedStatement.setInt(25, (int) entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull TelephoneSurvey entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(REMOVE_TELEPHONE_SURVEY)) {
            preparedStatement.setInt(1, (int) entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @NotNull TelephoneSurvey getByFields(TelephoneSurvey entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(GET_TELEPHONE_SURVEY_BY_ALL_FIELDS)) {
            preparedStatement.setInt(2, entity.getPatientId());
            preparedStatement.setDate(1, entity.getExecutedDate());
            preparedStatement.setInt(3, entity.getHospitalizationId());
            preparedStatement.setBoolean(4, entity.isIndependent());
            preparedStatement.setBoolean(5, entity.isHasFamily());
            preparedStatement.setBoolean(6, entity.isDyspneaProgressive());
            preparedStatement.setBoolean(7, entity.isEdemaIncrease());
            preparedStatement.setBoolean(8, entity.isHasHeartInterruptions());
            preparedStatement.setBoolean(9, entity.isHasPainDuringPhysicalExercise());
            preparedStatement.setBoolean(10, entity.isHasEveryDayControl());
            preparedStatement.setDouble(11, entity.getUpperBloodPressure());
            preparedStatement.setDouble(12, entity.getLowerBloodPressure());
            preparedStatement.setInt(13, entity.getHeartRate());
            preparedStatement.setDouble(14, entity.getMass());
            preparedStatement.setBoolean(15, entity.isMassChanged());
            preparedStatement.setBoolean(16, entity.isRegularlyTakePills());
            preparedStatement.setBoolean(17, entity.isForgotTakePills());
            preparedStatement.setBoolean(18, entity.isDrinkingWater1500ml());
            preparedStatement.setBoolean(19, entity.isSaltRestricted());
            preparedStatement.setBoolean(20, entity.isDecreasedExerciseTolerance());
            preparedStatement.setBoolean(21, entity.isSmoking());
            preparedStatement.setBoolean(22, entity.isDrinkingAlcohol());
            preparedStatement.setString(23, entity.getTreatPlan());
            preparedStatement.setDate(24, entity.getNextSurvey());
            preparedStatement.execute();
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new TelephoneSurvey(resultSet.getInt("telephone_survey_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getDate("executed_date"),
                            resultSet.getInt("hospitalization_id"),
                            resultSet.getBoolean("is_independent"),
                            resultSet.getBoolean("has_family"),
                            resultSet.getBoolean("is_dyspnea_progressive"),
                            resultSet.getBoolean("is_idema_increase"),
                            resultSet.getBoolean("has_heart_interruptions"),
                            resultSet.getBoolean("has_pain_during_physical"),
                            resultSet.getBoolean("has_everyday_control"),
                            resultSet.getDouble("upper_blood_pressure"),
                            resultSet.getDouble("lower_blood_pressure"),
                            resultSet.getInt("heart_rate"),
                            resultSet.getDouble("mass"),
                            resultSet.getBoolean("is_mass_changed"),
                            resultSet.getBoolean("regularly_take_pills"),
                            resultSet.getBoolean("forgot_take_pills"),
                            resultSet.getBoolean("drinking_water_1500ml"),
                            resultSet.getBoolean("is_salt_restricted"),
                            resultSet.getBoolean("decreased_exercise_tolerance"),
                            resultSet.getBoolean("is_smoking"),
                            resultSet.getBoolean("is_drinking_alcohol"),
                            resultSet.getString("treat_plan"),
                            resultSet.getDate("next_survey_date"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record not found");
    }
}
