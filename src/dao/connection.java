package dao;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import view.main;

public class connection {

    public static String status;
    public static Connection connection = null;

    public static Connection Connect() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }

    private static void createConnection() {

        try {

            String driverName = "com.mysql.cj.jdbc.Driver";
            Class.forName(driverName);
            String serverName = "192.18.0.101";
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
                status = "Erro na conexão com o banco de dados!";
            }

        } catch (ClassNotFoundException e) {
            if (main.pnlPrincipal.isVisible()) {
                JOptionPane.showMessageDialog(null, "O driver especificado não foi encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            status = "Erro na conexão com o banco de dados!";

        } catch (SQLException e) {
            if (main.pnlPrincipal.isVisible()) {
                JOptionPane.showMessageDialog(null, "Erro na conexão com o banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            status = "Erro na conexão com o banco de dados!";
                 
            java.util.logging.Logger.getLogger(connection.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        } 
    }

    public static String Status() {
        return status;
    }

    public static boolean Close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
            status = "Conexão fechada com sucesso!";
            return true;
        }
        return false;
    }

}