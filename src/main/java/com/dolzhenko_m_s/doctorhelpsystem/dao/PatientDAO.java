package com.dolzhenko_m_s.doctorhelpsystem.dao;

import com.dolzhenko_m_s.doctorhelpsystem.common.DbConnectionHelper;
import com.dolzhenko_m_s.doctorhelpsystem.models.Patient;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    private final @NotNull String SELECT_ALL_PATIENTS = "SELECT * FROM patient";

    private final @NotNull String GET_PATIENT_BY_ID = "SELECT * FROM patient WHERE patient_id = ";

    private final @NotNull String GET_PATIENT_BY_ALL_FIELDS = "SELECT * FROM patient WHERE name = ? AND phone = ? AND address = ? AND clinical_code = ? AND diagnosis = ? AND birth_date = ? AND remark = ? AND has_low_analysis = ?";

    private final @NotNull String SAVE_PATIENT = "INSERT INTO patient (name, phone, address, clinical_code, diagnosis, birth_date, remark, has_low_analysis) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private final @NotNull String UPDATE_PATIENT = "UPDATE patient SET name = ?, phone = ?, address = ?, clinical_code = ?, diagnosis = ?, birth_date = ?, remark = ?, has_low_analysis = ? WHERE patient_id = ?";

    private final @NotNull String REMOVE_PATIENT = "DELETE FROM patient WHERE patient_id = ?";

    public @NotNull Patient get(int id) {
        try (var statement = DbConnectionHelper.getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(GET_PATIENT_BY_ID + id)) {
                if (resultSet.next()) {
                    return new Patient(resultSet.getInt("patient_id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getString("address"),
                            resultSet.getString("clinical_code"),
                            resultSet.getString("diagnosis"),
                            resultSet.getDate("birth_date"),
                            resultSet.getString("remark"),
                            resultSet.getBoolean("has_low_analysis"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    public @NotNull List<Patient> all() {
        final var result = new ArrayList<Patient>();
        try (var statement = DbConnectionHelper.getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_PATIENTS)) {
                while (resultSet.next()) {
                    result.add(new Patient(resultSet.getInt("patient_id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getString("address"),
                            resultSet.getString("clinical_code"),
                            resultSet.getString("diagnosis"),
                            resultSet.getDate("birth_date"),
                            resultSet.getString("remark"),
                            resultSet.getBoolean("has_low_analysis")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Patient save(@NotNull Patient entity) {
        try (var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(SAVE_PATIENT)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getPhoneNumber());
            preparedStatement.setString(3, entity.getAddress());
            preparedStatement.setString(4, entity.getClinicalCode());
            preparedStatement.setString(5, entity.getDiagnosis());
            preparedStatement.setDate(6, entity.getBirthDate());
            preparedStatement.setString(7, entity.getRemark());
            preparedStatement.setBoolean(8, entity.isHasLowAnalysis());
            preparedStatement.executeUpdate();

            entity = getByFields(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public void update(@NotNull Patient entity) {
        try (var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(UPDATE_PATIENT)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getPhoneNumber());
            preparedStatement.setString(3, entity.getAddress());
            preparedStatement.setString(4, entity.getClinicalCode());
            preparedStatement.setString(5, entity.getDiagnosis());
            preparedStatement.setDate(6, entity.getBirthDate());
            preparedStatement.setString(7, entity.getRemark());
            preparedStatement.setBoolean(8, entity.isHasLowAnalysis());
            preparedStatement.setInt(9, (int) entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull Patient entity) {
        try(var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(REMOVE_PATIENT)) {
            preparedStatement.setInt(1, (int) entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @NotNull Patient getByFields(Patient patient) {
        try (var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(GET_PATIENT_BY_ALL_FIELDS)) {
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setString(2, patient.getPhoneNumber());
            preparedStatement.setString(3, patient.getAddress());
            preparedStatement.setString(4, patient.getClinicalCode());
            preparedStatement.setString(5, patient.getDiagnosis());
            preparedStatement.setDate(6, patient.getBirthDate());
            preparedStatement.setString(7, patient.getRemark());
            preparedStatement.setBoolean(8, patient.isHasLowAnalysis());
            preparedStatement.execute();
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Patient(resultSet.getInt("patient_id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getString("address"),
                            resultSet.getString("clinical_code"),
                            resultSet.getString("diagnosis"),
                            resultSet.getDate("birth_date"),
                            resultSet.getString("remark"),
                            resultSet.getBoolean("has_low_analysis"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record not found");
    }
}
