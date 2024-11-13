package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.estoque;

public class estoqueDAO {
    
    public void inserir(estoque es) throws SQLException {

        String SQL = "INSERT INTO estoque(tipoprodutoEst, modeloEst, marcaEst, corEst, materialEst, detalhesEst, localizacaoEst, precoEst, quantidadeEst, tipochipEst) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, es.getTipoproduto());
        stmt.setString(2, es.getModelo());
        stmt.setString(3, es.getMarca());
        stmt.setString(4, es.getCor());
        stmt.setString(5, es.getMaterial());
        stmt.setString(6, es.getDetalhes());
        stmt.setString(7, es.getLocalizacao());
        stmt.setDouble(8, es.getPreco());
        stmt.setInt(9, es.getQuantidade());
        stmt.setString(10, es.getTipochip());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void alterar(estoque es) throws SQLException {

        String SQL = "UPDATE estoque SET modeloEst = ?, marcaEst = ?, corEst = ?, materialEst = ?, detalhesEst = ?, localizacaoEst = ?, precoEst = ?, quantidadeEst = ?, tipochipEst = ? WHERE idEst = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, es.getModelo());
        stmt.setString(2, es.getMarca());
        stmt.setString(3, es.getCor());
        stmt.setString(4, es.getMaterial());
        stmt.setString(5, es.getDetalhes());
        stmt.setString(6, es.getLocalizacao());
        stmt.setDouble(7, es.getPreco());
        stmt.setInt(8, es.getQuantidade());
        stmt.setString(9, es.getTipochip());
        stmt.setInt(10, es.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }
    
   public void alterarvarios(estoque es) throws SQLException {
    // Criando a base da query SQL
    StringBuilder SQL = new StringBuilder("UPDATE estoque SET ");

    // Lista para os parâmetros da query
    List<Object> params = new ArrayList<>();

    // Verifica se o campo foi modificado e adiciona à query
    if (es.getModelo() != null) {
        SQL.append("modeloEst = ?, ");
        params.add(es.getModelo());
    }
    if (es.getMarca() != null) {
        SQL.append("marcaEst = ?, ");
        params.add(es.getMarca());
    }
    if (es.getCor() != null) {
        SQL.append("corEst = ?, ");
        params.add(es.getCor());
    }
    if (es.getMaterial() != null) {
        SQL.append("materialEst = ?, ");
        params.add(es.getMaterial());
    }
    if (es.getDetalhes() != null) {
        SQL.append("detalhesEst = ?, ");
        params.add(es.getDetalhes());
    }
    if (es.getLocalizacao() != null) {
        SQL.append("localizacaoEst = ?, ");
        params.add(es.getLocalizacao());
    }
    if (es.getPreco() != null) {
        SQL.append("precoEst = ?, ");
        params.add(es.getPreco());
    }
    if (es.getQuantidade() != null) {
        SQL.append("quantidadeEst = ?, ");
        params.add(es.getQuantidade());
    }
    if (es.getTipochip() != null) {
        SQL.append("tipochipEst = ?, ");
        params.add(es.getTipochip());
    }

    // Remover a última vírgula e espaço da query
    SQL.delete(SQL.length() - 2, SQL.length());

    // Adiciona a cláusula WHERE para garantir que o registro correto será atualizado
    SQL.append(" WHERE idEst = ?");

    // Preparar a query final
    PreparedStatement stmt = connection.Connect().prepareStatement(SQL.toString());

    // Adiciona o id do produto
    params.add(es.getId());

    // Define os parâmetros na query
    for (int i = 0; i < params.size(); i++) {
        stmt.setObject(i + 1, params.get(i)); // Adiciona o valor do parâmetro
    }

    // Executa a query
    stmt.executeUpdate();

    // Fecha a statement e a conexão
    stmt.close();
    connection.Close();
}

    
     public void excluir(estoque es) throws SQLException {

        String SQL = "UPDATE estoque SET quantidadeEst = 0 WHERE idEst = ?";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setInt(1, es.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }
     
       public void excluirum(estoque es) throws SQLException {

        String SQL = "UPDATE estoque SET quantidadeEst = quantidadeEst - 1 WHERE idEst = ?";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setInt(1, es.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public List<estoque> buscar(estoque es) throws SQLException {

        List<estoque> listaes = new ArrayList<>();

        String SQL = "SELECT * FROM estoque WHERE tipoprodutoEst = '" + es.getTipoproduto() + "' AND quantidadeEst != 0 AND (modeloEst LIKE '%" + es.getModelo() + "%' OR marcaEst LIKE '%" + es.getModelo() + "%' OR corEst LIKE '%" + es.getModelo() + "%' OR materialEst LIKE '%" + es.getModelo() + "%' OR detalhesEst LIKE '%" + es.getModelo() + "%' OR localizacaoEst LIKE '%" + es.getModelo() + "%' OR precoEst LIKE '%" + es.getModelo() + "%' OR tipochipEst LIKE '%" + es.getModelo() + "%')";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            estoque ess = new estoque();

            ess.setId(rs.getInt("idEst"));
            ess.setTipoproduto(rs.getString("tipoprodutoEst"));
            ess.setModelo(rs.getString("modeloEst"));
            ess.setMarca(rs.getString("marcaEst"));
            ess.setCor(rs.getString("corEst"));
            ess.setMaterial(rs.getString("materialEst"));
            ess.setDetalhes(rs.getString("detalhesEst"));
            ess.setLocalizacao(rs.getString("localizacaoEst"));
            ess.setPreco(rs.getDouble("precoEst"));
            ess.setQuantidade(rs.getInt("quantidadeEst"));
            ess.setTipochip(rs.getString("tipochipEst"));

            listaes.add(ess);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listaes;
    }

      public List<estoque> buscarprodutoregistrado(estoque es) throws SQLException {

        List<estoque> listaes = new ArrayList<>();

        String SQL = "SELECT * FROM estoque WHERE modeloEst LIKE '%" + es.getModelo() + "%' AND marcaEst LIKE '%" + es.getMarca() + "%' AND tipoprodutoEst = '"+es.getTipoproduto()+"'";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            estoque ess = new estoque();

            ess.setId(rs.getInt("idEst"));
            ess.setTipoproduto(rs.getString("tipoprodutoEst"));
            ess.setModelo(rs.getString("modeloEst"));
            ess.setMarca(rs.getString("marcaEst"));
            ess.setCor(rs.getString("corEst"));
            ess.setMaterial(rs.getString("materialEst"));
            ess.setDetalhes(rs.getString("detalhesEst"));
            ess.setLocalizacao(rs.getString("localizacaoEst"));
            ess.setPreco(rs.getDouble("precoEst"));
            ess.setQuantidade(rs.getInt("quantidadeEst"));
            ess.setTipochip(rs.getString("tipochipEst"));

            listaes.add(ess);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listaes;
    }

    
    public boolean verifica(estoque es) throws SQLException {

        String SQL = "SELECT * FROM estoque";
        PreparedStatement stmt;

        if (es.getTipoproduto().equals("Chip")) {

            SQL = SQL + " WHERE tipochipEst = ?";

            stmt = connection.Connect().prepareStatement(SQL);

            stmt.setString(1, es.getTipochip());

        } else {

            SQL = SQL + " WHERE modeloEst = ? AND marcaEst = ? AND tipoprodutoEst = ? AND corEst = ? AND materialEst = ? AND localizacaoEst = ?";

            stmt = connection.Connect().prepareStatement(SQL);

            stmt.setString(1, es.getModelo());
            stmt.setString(2, es.getMarca());
            stmt.setString(3, es.getTipoproduto());
            stmt.setString(4, es.getCor());
            stmt.setString(5, es.getMaterial());
            stmt.setString(6, es.getLocalizacao());
            
        }

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            es.setId(rs.getInt("idEst"));

            rs.close();
            stmt.close();
            connection.Close();
            return true;
        }

        rs.close();
        stmt.close();
        connection.Close();
        return false;
    }

    public void acrescentar(estoque es) throws SQLException {

        String SQL = "UPDATE estoque SET quantidadeEst = quantidadeEst + ? WHERE idEst = ?";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setInt(1, es.getQuantidade());
        stmt.setInt(2, es.getId());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

}
