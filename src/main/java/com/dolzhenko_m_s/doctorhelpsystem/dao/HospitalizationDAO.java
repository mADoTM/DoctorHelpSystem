package com.dolzhenko_m_s.doctorhelpsystem.dao;

import com.dolzhenko_m_s.doctorhelpsystem.common.DbConnectionHelper;
import com.dolzhenko_m_s.doctorhelpsystem.models.Hospitalization;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospitalizationDAO {
    private final @NotNull String SELECT_ALL_HOSPITALIZATIONS = "SELECT * FROM hospitalization";

    private final @NotNull String GET_HOSPITALIZATION_BY_ID = "SELECT * FROM hospitalization WHERE hospitalization_id = ";

    private final @NotNull String GET_HOSPITALIZATION_BY_ALL_FIELDS = "SELECT * FROM hospitalization WHERE is_emergency = ? AND reason = ?";

    private final @NotNull String SAVE_HOSPITALIZATION = "INSERT INTO hospitalization (is_emergency, reason) VALUES (?, ?)";

    private final @NotNull String UPDATE_HOSPITALIZATION = "UPDATE hospitalization SET is_emergency = ?, reason = ? WHERE hospitalization_id = ?";

    private final @NotNull String REMOVE_HOSPITALIZATION = "DELETE FROM hospitalization WHERE hospitalization_id = ?";

    public @NotNull Hospitalization get(int id) {
        try (var statement = DbConnectionHelper.getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(GET_HOSPITALIZATION_BY_ID + id)) {
                if (resultSet.next()) {
                    return new Hospitalization(resultSet.getInt("hospitalization_id"),
                            resultSet.getBoolean("is_emergency"),
                            resultSet.getString("reason"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record with id " + id + "not found");
    }

    public @NotNull List<Hospitalization> all() {
        final var result = new ArrayList<Hospitalization>();
        try (var statement = DbConnectionHelper.getConnection().createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL_HOSPITALIZATIONS)) {
                while (resultSet.next()) {
                    result.add(new Hospitalization(resultSet.getInt("hospitalization_id"),
                            resultSet.getBoolean("is_emergency"),
                            resultSet.getString("reason")));
                }
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Hospitalization save(@NotNull Hospitalization entity) {
        try (var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(SAVE_HOSPITALIZATION)) {
            preparedStatement.setBoolean(1, entity.isEmergency());
            preparedStatement.setString(2, entity.getReason());
            preparedStatement.executeUpdate();

            entity = getByFields(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public void update(@NotNull Hospitalization entity) {
        try (var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(UPDATE_HOSPITALIZATION)) {
            preparedStatement.setBoolean(1, entity.isEmergency());
            preparedStatement.setString(2, entity.getReason());
            preparedStatement.setInt(3, (int) entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(@NotNull Hospitalization entity) {
        try(var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(REMOVE_HOSPITALIZATION)) {
            preparedStatement.setInt(1, (int) entity.getId());
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalStateException("Record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public @NotNull Hospitalization getByFields(Hospitalization hospitalization) {
        try (var preparedStatement = DbConnectionHelper.getConnection().prepareStatement(GET_HOSPITALIZATION_BY_ALL_FIELDS)) {
            preparedStatement.setBoolean(1, hospitalization.isEmergency());
            preparedStatement.setString(2, hospitalization.getReason());
            preparedStatement.execute();
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Hospitalization(resultSet.getInt("hospitalization_id"),
                            resultSet.getBoolean("is_emergency"),
                            resultSet.getString("reason"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Record not found");
    }
}
