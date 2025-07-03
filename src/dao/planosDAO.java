package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class planosDAO {

    public void adicionar() throws SQLException {

        String SQL = "UPDATE planos SET numPla = numPla + 1 WHERE mesPla = MONTH(NOW())";

        PreparedStatement stmt = connection.getConnection().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void remover() throws SQLException {

        String SQL = "UPDATE planos SET numPla = numPla - 1 WHERE mesPla = MONTH(NOW())";

        PreparedStatement stmt = connection.getConnection().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public int buscar() throws SQLException {

        int num;

        String SQL = "SELECT numPla FROM planos WHERE mesPla = MONTH(NOW())";
        PreparedStatement stmt = connection.getConnection().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        rs.next();

        num = rs.getInt("numPla");

        rs.close();
        stmt.close();
        connection.Close();

        return num;

    }

    public void zerar() throws SQLException {

        String SQL = "UPDATE planos set numPla = 0, mesPla = MONTH(NOW()) WHERE mesPla != MONTH(NOW())";

        PreparedStatement stmt = connection.getConnection().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

}
