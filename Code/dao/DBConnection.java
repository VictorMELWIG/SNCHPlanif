package snchbilletterie.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/snch_billetterie?serverTimezone=UTC";
    private static final String USER = "snch_app";
    private static final String PASSWORD = "snch1234";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
