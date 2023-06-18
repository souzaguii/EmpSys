package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {

    public static String status;

    public static java.sql.Connection Connect() {

        Connection connection;

        try {

            String driverName = "com.mysql.cj.jdbc.Driver";
            Class.forName(driverName);
            String serverName = "localhost";
            String mydatabase = "empsysdatabase";
            String port = "3306";
            String aux = "?useTimezone=true&serverTimezone=UTC&autoReconnect=true&useSSL=false";
            String url = "jdbc:mysql://" + serverName + ":" + port + "/" + mydatabase + aux;
            String username = "root";
            String password = "Empcell4848@root";
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                status = ("Conectado com sucesso!");
            } else {
                status = ("Não foi possível realizar a conexão!");
            }

            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("O driver especificado não foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println("Não foi possível conectar ao banco de dados.");
            return null;
        }
    }

    public static String Status() {
        return status;
    }

    public static boolean Close() throws SQLException {
        connection.Connect().close();
        return true;
    }

    public static java.sql.Connection Restart() throws SQLException {
        Close();
        return connection.Connect();
    }
}
