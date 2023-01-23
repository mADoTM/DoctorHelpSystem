package com.dolzhenko_m_s.doctorhelpsystem.dao;

import com.dolzhenko_m_s.doctorhelpsystem.common.DbConnectionHelper;
import com.dolzhenko_m_s.doctorhelpsystem.models.AnalysisResult;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AnalysisResultDAO {
    private final @NotNull String SELECT_ALL_ANALYSIS_RESULT = "SELECT * FROM analysis_result";

    private final @NotNull String GET_ANALYSIS_RESULT_BY_ID = "SELECT * FROM analysis_result WHERE analysis_result_id = ";

    private final @NotNull String GET_ANALYSIS_RESULT_BY_ALL_FIELDS = "SELECT * FROM analysis_result WHERE patient_id = ? AND result = ? AND possible = ? AND analysis_name = ? AND executed_date ";

    private final @NotNull String SAVE_ANALYSIS_RESULT = "INSERT INTO analysis_result (executed_date, patient_id, result, next_date, analysis_name, possible) VALUES (?, ?, ?, ?, ?, ?)";

    private final @NotNull String UPDATE_ANALYSIS_RESULT = "UPDATE analysis_result SET executed_date = ?, patient_id = ?, result = ?, next_date = ?, analysis_name = ?, possible = ? WHERE analysis_result_id = ?";

    private final @NotNull String REMOVE_ANALYSIS_RESULT = "DELETE FROM analysis_result WHERE analysis_result_id = ?";

    public @NotNull AnalysisResult get(int id) {
        try (var statement = DbConnectionHelper.getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(GET_ANALYSIS_RESULT_BY_ID + id)) {
                if (resultSet.next()) {
                    return new AnalysisResult(resultSet.getInt("analysis_result_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getString("analysis_name"),
                            resultSet.getString("result"),
                            resultSet.getBoolean("possible"),
                            resultSet.getDate("executed_date"),
                            resultSet.getDate("next_date"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    public @NotNull List<AnalysisResult> all() {
        final var result = new ArrayList<AnalysisResult>();
        try (var statement = DbConnectionHelper
                .getConnection()
                .createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_ANALYSIS_RESULT)) {
                while (resultSet.next()) {
                    result.add(new AnalysisResult(resultSet.getInt("analysis_result_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getString("analysis_name"),
                            resultSet.getString("result"),
                            resultSet.getBoolean("possible"),
                            resultSet.getDate("executed_date"),
                            resultSet.getDate("next_date")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public AnalysisResult save(@NotNull AnalysisResult entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(SAVE_ANALYSIS_RESULT)) {
            preparedStatement.setInt(2, (int) entity.getPatientId());
            preparedStatement.setString(5, entity.getAnalysisName());
            preparedStatement.setString(3, entity.getResult());
            preparedStatement.setDate(1, entity.getExecutedAnalysisDate());
            preparedStatement.setDate(4, entity.getNextAnalysisDate());
            preparedStatement.setBoolean(6, entity.isPossible());
            preparedStatement.executeUpdate();

            entity = getByFields(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public void update(@NotNull AnalysisResult entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(UPDATE_ANALYSIS_RESULT)) {
            preparedStatement.setInt(2, (int) entity.getPatientId());
            preparedStatement.setDate(1, entity.getExecutedAnalysisDate());
            preparedStatement.setDate(4, entity.getNextAnalysisDate());
            preparedStatement.setString(3, entity.getResult());
            preparedStatement.setString(5, entity.getAnalysisName());
            preparedStatement.setBoolean(6, entity.isPossible());
            preparedStatement.setInt(7, (int) entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull AnalysisResult entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(REMOVE_ANALYSIS_RESULT)) {
            preparedStatement.setInt(1, (int) entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @NotNull AnalysisResult getByFields(AnalysisResult entity) {
        var getAnalysis = entity.getExecutedAnalysisDate() == null ? "IS NULL" : "= ?";
        getAnalysis += " AND next_date ";
        getAnalysis += entity.getNextAnalysisDate() == null ? "IS NULL" : "= ?";
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(GET_ANALYSIS_RESULT_BY_ALL_FIELDS + getAnalysis)) {
            preparedStatement.setInt(1, (int) entity.getPatientId());
            preparedStatement.setString(4, entity.getAnalysisName());
            preparedStatement.setBoolean(3, entity.isPossible());
            preparedStatement.setString(2, entity.getResult());
            if(entity.getExecutedAnalysisDate() != null) {
                preparedStatement.setDate(5, entity.getExecutedAnalysisDate());
            }
            if(entity.getNextAnalysisDate() != null) {
                preparedStatement.setDate(6, entity.getNextAnalysisDate());
            }
            preparedStatement.execute();
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new AnalysisResult(resultSet.getInt("analysis_result_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getString("analysis_name"),
                            resultSet.getString("result"),
                            resultSet.getBoolean("possible"),
                            resultSet.getDate("executed_date"),
                            resultSet.getDate("next_date"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record not found");
    }

}
