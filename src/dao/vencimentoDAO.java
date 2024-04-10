package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.vencimento;

public class vencimentoDAO {

    public void inserir(vencimento ve, String ok) throws SQLException {

        String SQL = "INSERT INTO vencimento(clienteVen, telefoneVen, acessoVen, cpfVen, dataVen, planoVen, vencimentoVen, okVen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ve.getCliente());
        stmt.setString(2, ve.getTelefone());
        stmt.setString(3, ve.getAcesso());
        stmt.setString(4, ve.getCpf());
        stmt.setString(5, ve.getData());
        stmt.setString(6, ve.getPlano());
        stmt.setString(7, ve.getVencimento());
        stmt.setString(8, ok);

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void alterar(vencimento ve, String ok) throws SQLException {

        String SQL = "UPDATE vencimento SET clienteVen = ?, telefoneVen = ?, acessoVen = ?, cpfVen = ?, dataVen = ?, planoVen = ?, vencimentoVen = ?, okVen = "
                + "CASE WHEN okVen = 1 THEN 1 ELSE ? END"
                + " WHERE idVen = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ve.getCliente());
        stmt.setString(2, ve.getTelefone());
        stmt.setString(3, ve.getAcesso());
        stmt.setString(4, ve.getCpf());
        stmt.setString(5, ve.getData());
        stmt.setString(6, ve.getPlano());
        stmt.setString(7, ve.getVencimento());
        stmt.setString(8, ok);
        stmt.setString(9, ve.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void excluir(vencimento ve) throws SQLException {

        String SQL = "DELETE FROM vencimento WHERE idVen = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ve.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void marcarok(vencimento ve) throws SQLException {

        String SQL = "UPDATE vencimento SET okVen = 1 WHERE vencimentoVen <= CURDATE() AND cpfVen = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ve.getCpf());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public List<String[]> buscar() throws SQLException {

        List<String[]> lista = new ArrayList<>();

        String SQL = "SELECT * FROM vencimento ORDER BY OkVen, vencimentoVen <= CURDATE() DESC, vencimentoVen >= CURDATE() DESC, ABS(DATEDIFF(vencimentoVen, CURDATE()))";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String[] rowData = {
                rs.getString("clienteVen"),
                rs.getString("telefoneVen"),
                rs.getString("cpfVen"),
                rs.getString("acessoVen"),
                rs.getString("planoVen"),
                rs.getString("dataVen"),
                rs.getString("vencimentoVen"),
                rs.getString("okVen"),
                rs.getString("idVen")};

            lista.add(rowData);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return lista;
    }
    
    public List<String[]> buscarpa(vencimento ve) throws SQLException {

        List<String[]> lista = new ArrayList<>();

        String SQL = "SELECT * FROM vencimento WHERE clienteVen LIKE '%" + ve.cliente + "%' "
                + "OR telefoneVen LIKE '%" + ve.telefone + "%' OR cpfVen LIKE '%" + ve.cpf + "%' "
                + "OR planoVen LIKE '%" + ve.plano + "%' OR dataVen LIKE '%" + ve.data + "%' "
                + "OR vencimentoVen LIKE '%" + ve.vencimento + "%' OR acessoVen LIKE '%" + ve.acesso + "%' ORDER BY vencimentoVen >= CURDATE() DESC, ABS(DATEDIFF(vencimentoVen, CURDATE()))";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String[] rowData = {
                rs.getString("clienteVen"),
                rs.getString("telefoneVen"),
                rs.getString("cpfVen"),
                rs.getString("acessoVen"),
                rs.getString("planoVen"),
                rs.getString("dataVen"),
                rs.getString("vencimentoVen"),
                rs.getString("okVen"),
                rs.getString("idVen")};

            lista.add(rowData);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return lista;
    }

     public List<String[]> buscarverificaplano() throws SQLException {

        List<String[]> lista = new ArrayList<>();

        String SQL = "SELECT * FROM vencimento WHERE vencimentoVen <= CURDATE() AND okVen = 0 ORDER BY planoVen";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String[] rowData = {
                rs.getString("cpfVen"),    
                rs.getString("planoVen")};

            lista.add(rowData);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return lista;
    }
    
    public boolean verificar() throws SQLException {

        String SQL = "SELECT * FROM vencimento WHERE vencimentoVen <=  CURRENT_DATE() AND okVen = 0";
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
