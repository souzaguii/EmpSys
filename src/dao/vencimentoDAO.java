package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.vencimento;

public class vencimentoDAO {

    public void inserir(vencimento ve) throws SQLException {

        String SQL = "INSERT INTO vencimento(clienteVen, telefoneVen, dataVen, planoVen, vencimentoVen) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ve.getCliente());
        stmt.setString(2, ve.getTelefone());
        stmt.setString(3, ve.getData());
        stmt.setString(4, ve.getPlano());
        stmt.setString(5, ve.getVencimento());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void alterar(vencimento ve) throws SQLException {

        String SQL = "UPDATE vencimento SET clienteVen = ?, telefoneVen = ?, dataVen = ?, planoVen = ?, vencimentoVen = ? WHERE clienteVen = ? AND dataVen = ? AND vencimentoVen = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ve.getCliente());
        stmt.setString(2, ve.getTelefone());
        stmt.setString(3, ve.getData());
        stmt.setString(4, ve.getPlano());
        stmt.setString(5, ve.getVencimento());
        stmt.setString(6, ve.getClienteold());
        stmt.setString(7, ve.getDataold());
        stmt.setString(8, ve.getVencimentoold());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void excluir(vencimento ve) throws SQLException {

        String SQL = "DELETE FROM vencimento WHERE clienteVen = ? and dataVen = ? and vencimentoVen = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ve.getCliente());
        stmt.setString(2, ve.getData());
        stmt.setString(3, ve.getVencimento());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public List<String[]> buscar() throws SQLException {

        List<String[]> lista = new ArrayList<>();

        String SQL = "SELECT * FROM vencimento";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String[] rowData = {
                rs.getString("clienteVen"),
                rs.getString("telefoneVen"),
                rs.getString("planoVen"),
                rs.getString("dataVen"),
                rs.getString("vencimentoVen")};

            lista.add(rowData);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return lista;
    }

    public boolean verificar() throws SQLException {

        String SQL = "SELECT * FROM vencimento WHERE vencimentoVen <=  CURRENT_DATE()";
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

}
