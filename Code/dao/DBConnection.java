package snchbilletterie.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL =
            "jdbc:mysql://mysql-victormelwig.alwaysdata.net:3306/victormelwig_snch?serverTimezone=UTC";
    private static final String USER = "victormelwig";
    private static final String PASSWORD = "SkaiMite83";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}