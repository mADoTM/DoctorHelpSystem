package com.dolzhenko_m_s.doctorhelpsystem.dao;

import com.dolzhenko_m_s.doctorhelpsystem.common.DbConnectionHelper;
import com.dolzhenko_m_s.doctorhelpsystem.models.Notification;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private final @NotNull String SELECT_ALL_NOTIFICATIONS = "SELECT * FROM notification";

    private final @NotNull String GET_NOTIFICATION_BY_ID = "SELECT * FROM notification WHERE notification_id = ";

    private final @NotNull String GET_NOTIFICATION_BY_ALL_FIELDS = "SELECT * FROM notification WHERE patient_id = ? AND date = ? AND action = ? AND executed = ?";

    private final @NotNull String SAVE_NOTIFICATION = "INSERT INTO notification (patient_id, date, action, executed) VALUES (?, ?, ?, ?)";

    private final @NotNull String UPDATE_NOTIFICATION = "UPDATE notification SET patient_id = ?, date = ?, action = ?, executed = ? WHERE notification_id = ?";

    private final @NotNull String REMOVE_NOTIFICATION = "DELETE FROM notification WHERE notification_id = ?";

    public @NotNull Notification get(int id) {
        try (var statement = DbConnectionHelper.getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(GET_NOTIFICATION_BY_ID + id)) {
                if (resultSet.next()) {
                    return new Notification(resultSet.getInt("notification_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getDate("date"),
                            resultSet.getString("action"),
                            resultSet.getBoolean("executed"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    public @NotNull List<Notification> all() {
        final var result = new ArrayList<Notification>();
        try (var statement = DbConnectionHelper
                .getConnection()
                .createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_NOTIFICATIONS)) {
                while (resultSet.next()) {
                    result.add(new Notification(resultSet.getInt("notification_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getDate("date"),
                            resultSet.getString("action"),
                            resultSet.getBoolean("executed")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Notification save(@NotNull Notification entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(SAVE_NOTIFICATION)) {
            preparedStatement.setInt(1, (int) entity.getPatientId());
            preparedStatement.setDate(2, entity.getDate());
            preparedStatement.setString(3, entity.getAction());
            preparedStatement.setBoolean(4, entity.isExecuted());
            preparedStatement.executeUpdate();

            entity = getByFields(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public void update(@NotNull Notification entity) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(UPDATE_NOTIFICATION)) {
            preparedStatement.setInt(1, (int) entity.getPatientId());
            preparedStatement.setDate(2, entity.getDate());
            preparedStatement.setString(3, entity.getAction());
            preparedStatement.setBoolean(4, entity.isExecuted());
            preparedStatement.setInt(5, (int) entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull Notification entity) {
        try(var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(REMOVE_NOTIFICATION)) {
            preparedStatement.setInt(1, (int) entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @NotNull Notification getByFields(Notification notification) {
        try (var preparedStatement = DbConnectionHelper
                .getConnection()
                .prepareStatement(GET_NOTIFICATION_BY_ALL_FIELDS)) {
            preparedStatement.setInt(1, (int) notification.getPatientId());
            preparedStatement.setDate(2, notification.getDate());
            preparedStatement.setString(3, notification.getAction());
            preparedStatement.setBoolean(4, notification.isExecuted());
            preparedStatement.execute();
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Notification(resultSet.getInt("notification_id"),
                            resultSet.getInt("patient_id"),
                            resultSet.getDate("date"),
                            resultSet.getString("action"),
                            resultSet.getBoolean("executed"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record not found");
    }
}
