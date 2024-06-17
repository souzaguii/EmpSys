package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class planosdiaDAO {

    public void adicionar(int tip) throws SQLException {

        String SQL = "UPDATE planosdia SET numPlaDia = numPlaDia + 1 WHERE diaPlaDia = DAY(NOW()) AND tipPlaDia = "+tip+"";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public int buscar(int tip) throws SQLException {

        int num;

        String SQL = "SELECT numPlaDia FROM planosdia WHERE diaPlaDia = DAY(NOW()) AND tipPlaDia = "+tip+"";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        rs.next();

        num = rs.getInt("numPlaDia");

        rs.close();
        stmt.close();
        connection.Close();

        return num;

    }

    public void zerar() throws SQLException {

        String SQL = "UPDATE planosdia set numPlaDia = 0, diaPlaDia = DAY(NOW()) WHERE diaPlaDia != DAY(NOW())";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

}