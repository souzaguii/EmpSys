package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.tiposervico;

public class tiposervicoDAO {

    public void inserir(tiposervico ts) throws SQLException {

        String SQL = "INSERT INTO tiposervico(descricaoTipSer, areaTipSer) VALUES (?, ?)";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ts.getDescricao());
        stmt.setString(2, ts.getArea());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public List<tiposervico> buscartodos() throws SQLException {

        List<tiposervico> listats = new ArrayList<>();

        String SQL = "SELECT * FROM tiposervico";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            tiposervico tss = new tiposervico();

            tss.setDescricao(rs.getString("descricaoTipSer"));
            tss.setIdtiposervico(rs.getInt("idTipSer"));
          //  tss.setArea(rs.getString("areaTipSer"));

            listats.add(tss);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listats;
    }
    
     public List<tiposervico> buscarass() throws SQLException {

        List<tiposervico> listats = new ArrayList<>();

        String SQL = "SELECT * FROM tiposervico WHERE areaTipSer = '3'";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            tiposervico tss = new tiposervico();

            tss.setDescricao(rs.getString("descricaoTipSer"));
            tss.setIdtiposervico(rs.getInt("idTipSer"));
            tss.setArea(rs.getString("areaTipSer"));

            listats.add(tss);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listats;
    }
     
       public List<tiposervico> buscarvenser() throws SQLException {

        List<tiposervico> listats = new ArrayList<>();

        String SQL = "SELECT * FROM tiposervico WHERE areaTipSer != '3'";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            tiposervico tss = new tiposervico();

            tss.setDescricao(rs.getString("descricaoTipSer"));
            tss.setIdtiposervico(rs.getInt("idTipSer"));
            tss.setArea(rs.getString("areaTipSer"));

            listats.add(tss);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listats;
    }

    public void excluir(tiposervico ts) throws SQLException {

        String SQL = "DELETE FROM tiposervico WHERE descricaoTipSer = ?";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ts.getDescricao());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void alterar(tiposervico ts) throws SQLException {

        String SQL = "UPDATE tiposervico SET descricaoTipSer = ? WHERE descricaoTipSer = ?";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, ts.getArea());
        stmt.setString(2, ts.getDescricao());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

}