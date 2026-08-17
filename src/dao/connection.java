package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.0.101/EmpSysDatabase"
            + "?allowPublicKeyRetrieval=true&useTimezone=true&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "Empcell@4848ROOT";

    private static Connection conn;

    public static synchronized Connection getConnection() throws SQLException {
        try {
            if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                Class.forName(DRIVER);
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            System.getLogger(connection.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        return null;
    }

    public static synchronized void Close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
            conn = null;
        }
    }

}
