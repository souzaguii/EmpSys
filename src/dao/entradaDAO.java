package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entrada;

public class entradaDAO {

    public void inserir(entrada en, int tipo) throws SQLException {

        String SQL = "";
        PreparedStatement stmt;
        String id = "null";

        if (en.getIdestoque() != 1) {

            id = String.valueOf(en.getIdestoque());

        }

        switch (tipo) {
            case 1 -> {
                SQL = "INSERT INTO entradas(idEst, idTipSer, codigoEnt, dataEnt, precoEnt, detalhesEnt, quantidadeEnt, idTip, formapagamentoEnt) VALUES (" + id + ", ?, ?, ?, ?, ?, ?, ?, ?)";

                stmt = connection.Connect().prepareStatement(SQL);

                stmt.setInt(1, en.getIdtiposervico());
                stmt.setString(2, en.getCodigo());
                stmt.setString(3, en.getData());
                stmt.setDouble(4, en.getPreco());
                stmt.setString(5, en.getDetalhes());
                stmt.setInt(6, en.getQuantidade());
                stmt.setInt(7, 1);
                stmt.setInt(8, en.getFormapagamento());

            }
            case 2 -> {
                SQL = "INSERT INTO entradas(idEst, codigoEnt, dataEnt, precoEnt, detalhesEnt, quantidadeEnt, idTip, formapagamentoEnt) VALUES (" + id + ", ?, ?, ?, ?, ?, ?, ?)";

                stmt = connection.Connect().prepareStatement(SQL);

                stmt.setString(1, en.getCodigo());
                stmt.setString(2, en.getData());
                stmt.setDouble(3, en.getPreco());
                stmt.setString(4, en.getDetalhes());
                stmt.setInt(5, en.getQuantidade());
                stmt.setInt(6, 2);
                stmt.setInt(7, en.getFormapagamento());

            }
            default -> {

                if (en.getCusto() != null) {

                    SQL = "INSERT INTO entradas(idTipSer, codigoEnt, dataEnt, precoEnt, detalhesEnt, idTip, formapagamentoEnt, clienteEnt, fornecedorEnt, custoEnt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    stmt = connection.Connect().prepareStatement(SQL);

                    stmt.setInt(1, en.getIdtiposervico());
                    stmt.setString(2, en.getCodigo());
                    stmt.setString(3, en.getData());
                    stmt.setDouble(4, en.getPreco());
                    stmt.setString(5, en.getDetalhes());
                    stmt.setInt(6, 3);
                    stmt.setInt(7, en.getFormapagamento());
                    stmt.setString(8, en.getCliente());
                    stmt.setString(9, en.getFornecedor());
                    stmt.setDouble(10, en.getCusto());
                    
                } else {

                    SQL = "INSERT INTO entradas(idTipSer, codigoEnt, dataEnt, precoEnt, detalhesEnt, idTip, formapagamentoEnt, clienteEnt, fornecedorEnt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    stmt = connection.Connect().prepareStatement(SQL);

                    stmt.setInt(1, en.getIdtiposervico());
                    stmt.setString(2, en.getCodigo());
                    stmt.setString(3, en.getData());
                    stmt.setDouble(4, en.getPreco());
                    stmt.setString(5, en.getDetalhes());
                    stmt.setInt(6, 3);
                    stmt.setInt(7, en.getFormapagamento());
                    stmt.setString(8, en.getCliente());
                    stmt.setString(9, en.getFornecedor());

                }
            }
        }

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void atualizarestoque(entrada en, int opc) throws SQLException {

        String SQL = "UPDATE estoque SET quantidadeEst = quantidadeEst - ?  WHERE idEst = ?";

        if (opc == 1) {

            SQL = "UPDATE estoque SET quantidadeEst = quantidadeEst + ?  WHERE idEst = ?";

        }

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setInt(1, en.getQuantidade());
        stmt.setInt(2, en.getIdestoque());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public void excluir(entrada en) throws SQLException {

        String SQL = "DELETE FROM entradas WHERE codigoEnt = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, en.getCodigo());

        stmt.executeUpdate();
        stmt.close();
        connection.Close();

    }

    public List<String[]> buscar(int opc, int opc1, String data1, String data2) throws SQLException {

        List<String[]> listaen = new ArrayList<>();

        String SQL = "";

        PreparedStatement stmt = null;

        switch (opc) {
            case 1:
                SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE dataEnt = CURDATE()";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE WEEK(dataEnt) = WEEK(CURDATE())";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE MONTH(dataEnt) = MONTH(CURDATE())";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE YEAR(dataEnt) = YEAR(CURDATE())";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE dataEnt BETWEEN ? AND ?";

                }
                break;
            case 2:
                SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 3";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 3 AND dataEnt = CURDATE()";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 3 AND WEEK(dataEnt) = WEEK(CURDATE())";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 3 AND MONTH(dataEnt) = MONTH(CURDATE())";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 3 AND YEAR(dataEnt) = YEAR(CURDATE())";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 3 AND dataEnt BETWEEN ? AND ?";

                }
                break;
            case 3:
                SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND dataEnt = CURDATE()";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND WEEK(dataEnt) = WEEK(CURDATE())";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND MONTH(dataEnt) = MONTH(CURDATE())";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND YEAR(dataEnt) = YEAR(CURDATE())";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND dataEnt BETWEEN ? AND ?";

                }
                break;
            case 4:
                SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer != 3 OR idTip = 2";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 3 OR idTip = 2) AND dataEnt = CURDATE()";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 3 OR idTip = 2) AND WEEK(dataEnt) = WEEK(CURDATE())";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 3 OR idTip = 2) AND MONTH(dataEnt) = MONTH(CURDATE())";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 3 OR idTip = 2) AND YEAR(dataEnt) = YEAR(CURDATE())";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 3 OR idTip = 2) AND dataEnt BETWEEN ? AND ?";

                }
                break;
            default:

                SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 3";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 3 AND dataEnt = CURDATE()";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 3 AND WEEK(dataEnt) = WEEK(CURDATE())";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 3 AND MONTH(dataEnt) = MONTH(CURDATE())";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 3 AND YEAR(dataEnt) = YEAR(CURDATE())";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 3 AND dataEnt BETWEEN ? AND ?";

                }

                break;
        }

        stmt = connection.Connect().prepareStatement(SQL);

        if (opc1 == 6) {

            stmt.setString(1, data1);
            stmt.setString(2, data2);

        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String produto = null;

            if (!"3".equals(rs.getString("idTip"))) {

                if ("Chip".equals(rs.getString("tipoprodutoEst"))) {

                    produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("tipochipEst");

                } else {

                    if (!"".equals(rs.getString("corEst"))) {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst") + " - " + rs.getString("corEst");

                    } else {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst");

                    }

                }
            }

            if ("2".equals(rs.getString("idTip"))) {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    null,
                    produto,
                    rs.getString("precoEnt"),
                    null,
                    rs.getString("formapagamentoEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            } else if ("1".equals(rs.getString("idTip"))) {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    rs.getString("descricaoTipSer"),
                    produto,
                    rs.getString("precoEnt"),
                    null,
                    rs.getString("formapagamentoEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            } else {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    rs.getString("descricaoTipSer"),
                    null,
                    rs.getString("precoEnt"),
                    rs.getString("custoEnt"),
                    rs.getString("formapagamentoEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            }

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listaen;
    }

    public List<String[]> buscargerenciar(String data) throws SQLException {

        List<String[]> listaen = new ArrayList<>();

        String SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE dataEnt = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, data);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String produto = null;

            if (!"3".equals(rs.getString("idTip"))) {

                if ("Chip".equals(rs.getString("tipoprodutoEst"))) {

                    produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("tipochipEst");

                } else {

                    if (!"".equals(rs.getString("corEst"))) {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst") + " - " + rs.getString("corEst");

                    } else {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst");

                    }

                }
            }

            if ("2".equals(rs.getString("idTip"))) {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    "Venda",
                    null,
                    null,
                    produto,
                    rs.getString("precoEnt"),
                    null,
                    rs.getString("formapagamentoEnt"),
                    null,
                    rs.getString("quantidadeEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            } else if ("1".equals(rs.getString("idTip"))) {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    "Serviço",
                    rs.getString("descricaoTipSer"),
                    null,
                    produto,
                    rs.getString("precoEnt"),
                    null,
                    rs.getString("formapagamentoEnt"),
                    null,
                    rs.getString("quantidadeEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            } else {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    "Assistência",
                    rs.getString("descricaoTipSer"),
                    rs.getString("clienteEnt"),
                    produto,
                    rs.getString("precoEnt"),
                    rs.getString("custoEnt"),
                    rs.getString("formapagamentoEnt"),
                    rs.getString("fornecedorEnt"),
                    rs.getString("quantidadeEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            }

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listaen;
    }

    public List<String[]> buscaritensselecionados(String codigo) throws SQLException {

        List<String[]> listaen = new ArrayList<>();

        String SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE codigoEnt = ?";

        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        stmt.setString(1, codigo);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String produto = null;

            if (!"3".equals(rs.getString("idTip"))) {

                if ("Chip".equals(rs.getString("tipoprodutoEst"))) {

                    produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("tipochipEst");

                } else {

                    if (!"".equals(rs.getString("corEst"))) {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst") + " - " + rs.getString("corEst");

                    } else {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst");

                    }

                }
            }

            String[] rowData = {
                rs.getString("quantidadeEnt"),
                rs.getString("idEst"),
                produto,
                rs.getString("precoEst")};

            listaen.add(rowData);

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listaen;
    }

    public List<String[]> buscar(String busca) throws SQLException {

        List<String[]> listaen = new ArrayList<>();

        String SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE dataEnt LIKE '%" + busca + "%' OR precoEnt LIKE '%" + busca + "%' OR detalhesEnt LIKE '%" + busca + "%' OR formapagamentoEnt LIKE '%" + busca + "%' OR clienteEnt LIKE '%" + busca + "%' OR custoEnt LIKE '%" + busca + "%' OR fornecedorEnt LIKE '%" + busca + "%' OR codigoEnt LIKE '%" + busca + "%'";
        PreparedStatement stmt = connection.Connect().prepareStatement(SQL);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String produto = null;

            if (!"3".equals(rs.getString("idTip"))) {

                if ("Chip".equals(rs.getString("tipoprodutoEst"))) {

                    produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("tipochipEst");

                } else {

                    if (!"".equals(rs.getString("corEst"))) {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst") + " - " + rs.getString("corEst");

                    } else {

                        produto = rs.getString("tipoprodutoEst") + " - " + rs.getString("marcaEst") + " " + rs.getString("modeloEst");

                    }

                }
            }

            if ("2".equals(rs.getString("idTip"))) {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    "Venda",
                    null,
                    null,
                    produto,
                    rs.getString("precoEnt"),
                    null,
                    rs.getString("formapagamentoEnt"),
                    null,
                    rs.getString("quantidadeEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            } else if ("1".equals(rs.getString("idTip"))) {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    "Serviço",
                    rs.getString("descricaoTipSer"),
                    null,
                    produto,
                    rs.getString("precoEnt"),
                    null,
                    rs.getString("formapagamentoEnt"),
                    null,
                    rs.getString("quantidadeEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            } else {

                String[] rowData = {
                    rs.getString("dataEnt"),
                    "Assistência",
                    rs.getString("descricaoTipSer"),
                    rs.getString("clienteEnt"),
                    produto,
                    rs.getString("precoEnt"),
                    rs.getString("custoEnt"),
                    rs.getString("formapagamentoEnt"),
                    rs.getString("fornecedorEnt"),
                    rs.getString("quantidadeEnt"),
                    rs.getString("detalhesEnt"),
                    rs.getString("codigoEnt")};

                listaen.add(rowData);

            }

        }

        rs.close();
        stmt.close();
        connection.Close();

        return listaen;
    }

}
