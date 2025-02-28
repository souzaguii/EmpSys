package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.despezas;

public class despezasDAO {

    public void inserir(despezas de) throws SQLException {

        String SQL = "INSERT INTO despezas(descricaoDes, precoDes, dataDes, dataconclusaoDes) VALUES (?, ?, ?, ?)";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, de.getDescricao());
        stmt.setDouble(2, de.getValor());
        stmt.setString(3, de.getData());
        stmt.setString(4, de.getDataconclusao());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void alterar(despezas de) throws SQLException {

        String SQL = "UPDATE despezas SET descricaoDes = ?, precoDes = ?, dataDes = ? WHERE idDes = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, de.getDescricao());
        stmt.setDouble(2, de.getValor());
        stmt.setString(3, de.getData());
        stmt.setInt(4, de.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void excluir(despezas de) throws SQLException {

        String SQL = "DELETE FROM despezas WHERE idDes = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setInt(1, de.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void conclusao(despezas de) throws SQLException {

        String SQL = "UPDATE despezas SET dataDes = DATE_ADD(dataDes, INTERVAL 1 MONTH), dataconclusaoDes = ? WHERE idDes = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, de.getDataconclusao());
        stmt.setInt(2, de.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public boolean verificar() throws SQLException {

        String SQL = "SELECT idDes FROM despezas WHERE dataDes <=  CURRENT_DATE() AND dataconclusaoDes < CURRENT_DATE()";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            return true;

        }

        rs.close();
        stmt.close();
        connection.Close();

        return false;

    }

    public List<String[]> buscar() throws SQLException {

        List<String[]> lista = new ArrayList<>();

        String SQL = "SELECT * FROM despezas ORDER BY dataDes";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String[] rowData = {
                rs.getString("idDes"),
                rs.getString("descricaoDes"),
                rs.getString("precoDes"),
                rs.getString("dataDes"),
                rs.getString("dataconclusaoDes")};

            lista.add(rowData);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return lista;
    }

}
