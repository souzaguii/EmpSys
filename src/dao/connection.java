package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class connection {

    public static String status;
    private static Connection connection;

    public static java.sql.Connection Connect() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }

    private static void createConnection() {
        
        try {
            String driverName = "com.mysql.cj.jdbc.Driver";
            Class.forName(driverName);
            String serverName = "192.168.0.123";
            String mydatabase = "EmpSysDatabase";
            String port = "3306";
            String aux = "?useTimezone=true&serverTimezone=UTC&autoReconnect=true&useSSL=false";
            String url = "jdbc:mysql://" + serverName + ":" + port + "/" + mydatabase + aux;
            String username = "root";
            String password = "Empcell@4848ROOT";
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                status = "Conectado com sucesso!";
            } else {
                status = "Não foi possível realizar a conexão!";
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "O driver especificado não foi encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar banco de dados. Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String Status() {
        return status;
    }

    public static boolean Close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
            status = "Conexão fechada";
            return true;
        }
        return false;
    }
 
}
