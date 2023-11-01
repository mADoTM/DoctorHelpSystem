package com.dolzhenko_m_s.doctorhelpsystem.common;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionHelper {
    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    private static Connection connection;
    public static @NotNull Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed())
            connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());

        System.out.println(CREDS.url());
        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

    public static void setAutoCommit(boolean autoCommit) throws SQLException {
        if(connection == null || connection.isClosed())
            getConnection();
        connection.setAutoCommit(autoCommit);
    }
}
