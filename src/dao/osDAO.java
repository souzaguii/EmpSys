package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.os;

public class osDAO {

    public void inserir(os os) throws SQLException {

        String SQL = "INSERT INTO os(clienteOs, telefoneOs, enderecoOs, dataentradaOs, datasaidaOs, precoOs, equipamentoOs, marcaOs, modeloOs, defeitoOs, reparoOs, garantiaOs) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, os.getCliente());
        stmt.setString(2, os.getTelefone());
        stmt.setString(3, os.getEndereco());
        stmt.setString(4, os.getDataentrada());
        stmt.setString(5, os.getDatasaida());
        stmt.setString(6, os.getPreco());
        stmt.setString(7, os.getEquipamento());
        stmt.setString(8, os.getMarca());
        stmt.setString(9, os.getModelo());
        stmt.setString(10, os.getDefeito());
        stmt.setString(11, os.getReparo());
        stmt.setString(12, os.getGarantia());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void alterar(os os) throws SQLException {

        String SQL = "UPDATE os SET clienteOs = ?, telefoneOs = ?, enderecoOs = ?, dataentradaOs = ?, datasaidaOs = ?, precoOs = ?, equipamentoOs = ?, marcaOs = ?, modeloOs = ?, defeitoOs = ?, garantiaOs = ? WHERE idOs = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

       stmt.setString(1, os.getCliente());
        stmt.setString(2, os.getTelefone());
        stmt.setString(3, os.getEndereco());
        stmt.setString(4, os.getDataentrada());
        stmt.setString(5, os.getDatasaida());
        stmt.setString(6, os.getPreco());
        stmt.setString(7, os.getEquipamento());
        stmt.setString(8, os.getMarca());
        stmt.setString(9, os.getModelo());
        stmt.setString(10, os.getDefeito());
        stmt.setString(11, os.getReparo());
        stmt.setString(12, os.getGarantia());
        stmt.setString(13, os.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void excluir(os os) throws SQLException {

        String SQL = "DELETE FROM os WHERE idOs = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, os.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public List<String[]> buscar() throws SQLException {

        List<String[]> lista = new ArrayList<>();

        String SQL = "SELECT * FROM os ORDER BY clienteOs";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String[] rowData = {
                rs.getString("idOs"),
                rs.getString("clienteOs"),
                rs.getString("telefoneOs"),
                rs.getString("enderecoOs"),
                rs.getString("dataentradaOs"),
                rs.getString("datasaidaOs"),
                rs.getString("precoOs"),
                rs.getString("equipamentoOs"),
                rs.getString("marcaOs"),
                rs.getString("modeloOs"),
                rs.getString("defeitoOs"),
                rs.getString("reparoOs"),
                rs.getString("garantiaOs")};

            lista.add(rowData);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return lista;
    } 

}
