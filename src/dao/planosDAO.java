package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class planosDAO {

    public void criar() throws SQLException {

        String SQL = "INSERT INTO planos(numPla, mesPla) VALUES (1, MONTH(NOW()))";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void adicionar() throws SQLException {

        String SQL = "UPDATE planos SET numPla = numPla + 1 WHERE mesPla = MONTH(NOW())";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void remover() throws SQLException {

        String SQL = "UPDATE planos SET numPla = numPla - 1 WHERE mesPla = MONTH(NOW())";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public int buscar() throws SQLException {

        int num;

        String SQL = "SELECT numPla FROM planos WHERE mesPla = MONTH(NOW())";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        rs.next();

        num = rs.getInt("numPla");

        rs.close();
        stmt.close();
        connection.Close();

        return num;

    }

    public boolean verifica() throws SQLException {

        boolean num = false;

        String SQL = "SELECT numPla FROM planos WHERE mesPla = MONTH(NOW())";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            num = true;

        }

        rs.close();
        stmt.close();
        connection.Close();

        return num;

    }

    public void excluir() throws SQLException {

        String SQL = "DELETE FROM planos WHERE mesPla != MONTH(NOW())";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

}
