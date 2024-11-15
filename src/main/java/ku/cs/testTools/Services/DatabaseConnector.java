package ku.cs.testTools.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String USER = "boat_tester";
    private static final String PASSWORD = "boatandgift";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
