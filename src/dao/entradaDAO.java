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

    public List<String[]> buscar(int opc, int opc1, String data1, String data2, entrada en) throws SQLException {

        List<String[]> listaen = new ArrayList<>();

        String SQL;

        PreparedStatement stmt;

        switch (opc) {
            case 1:
                SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer ORDER BY dataEnt DESC";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE dataEnt = CURDATE() ORDER BY dataEnt DESC";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE WEEK(dataEnt) = WEEK(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE MONTH(dataEnt) = MONTH(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE YEAR(dataEnt) = YEAR(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE dataEnt BETWEEN ? AND ? ORDER BY dataEnt DESC";

                }
                break;
            case 2:
                SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 2 ORDER BY dataEnt DESC";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 2 AND dataEnt = CURDATE() ORDER BY dataEnt DESC";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 2 AND WEEK(dataEnt) = WEEK(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 2 AND MONTH(dataEnt) = MONTH(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 2 AND YEAR(dataEnt) = YEAR(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer WHERE areaTipSer != 2 AND dataEnt BETWEEN ? AND ? ORDER BY dataEnt DESC";

                }
                break;
            case 3:
                SQL = "SELECT * FROM entradas LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 ORDER BY dataEnt DESC";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND dataEnt = CURDATE() ORDER BY dataEnt DESC";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND WEEK(dataEnt) = WEEK(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND MONTH(dataEnt) = MONTH(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND YEAR(dataEnt) = YEAR(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas INNER JOIN estoque ON estoque.idEst = entradas.idEst WHERE idTip = 2 AND dataEnt BETWEEN ? AND ? ORDER BY dataEnt DESC";

                }
                break;
            case 4:
                SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer != 2 OR idTip = 2 ORDER BY dataEnt DESC";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 2 OR idTip = 2) AND dataEnt = CURDATE() ORDER BY dataEnt DESC";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 2 OR idTip = 2) AND WEEK(dataEnt) = WEEK(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 2 OR idTip = 2) AND MONTH(dataEnt) = MONTH(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 2 OR idTip = 2) AND YEAR(dataEnt) = YEAR(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE (areaTipSer != 2 OR idTip = 2) AND dataEnt BETWEEN ? AND ? ORDER BY dataEnt DESC";

                }
                break;
            default:

                SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 2 ORDER BY dataEnt DESC";
                if (opc1 == 2) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 2 AND dataEnt = CURDATE() ORDER BY dataEnt DESC";

                }
                if (opc1 == 3) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 2 AND WEEK(dataEnt) = WEEK(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 4) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 2 AND MONTH(dataEnt) = MONTH(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 5) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 2 AND YEAR(dataEnt) = YEAR(CURDATE()) ORDER BY dataEnt DESC";

                }
                if (opc1 == 6) {

                    SQL = "SELECT * FROM entradas INNER JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE areaTipSer = 2 AND dataEnt BETWEEN ? AND ? ORDER BY dataEnt DESC";

                }

                break;
        }

        stmt = connection.Connect().prepareStatement(SQL);

        if (opc1 == 6) {

            stmt.setString(1, data1);
            stmt.setString(2, data2);

        }

        ResultSet rs = stmt.executeQuery();

        int ent = 0;
        double pd;
        double pdc;
        double pad = 0;
        double padc = 0;
        double ppc;
        double pp;
        double pap = 0;
        double papc = 0;
        double pcc;
        double pc;
        double pac = 0;
        double pacc = 0;

        String ca = "1";
        String c;

        while (rs.next()) {

            String produto = null;

            if (!"3".equals(rs.getString("idTip"))) {  // CONCATENA O PRODUTO COM MARCA, CHIP..

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

            if (rs.getString("formapagamentoEnt").equals("1")) { //CALCULAR PRECO COM CODIGO DISTINTO E FORMAS SEPARADAS

                c = rs.getString("codigoEnt");
                pd = Double.parseDouble(rs.getString("precoEnt"));
                pdc = (rs.getString("custoEnt") != null) ? Double.parseDouble(rs.getString("custoEnt")) : 0;
                if (!ca.equals(c)) {
                    pad = pd + pad;
                    padc = pdc + padc;
                    ent++;
                }
                ca = c;

            } else if (rs.getString("formapagamentoEnt").equals("2")) {

                c = rs.getString("codigoEnt");
                pc = Double.parseDouble(rs.getString("precoEnt"));
                pcc = (rs.getString("custoEnt") != null) ? Double.parseDouble(rs.getString("custoEnt")) : 0;
                if (!ca.equals(c)) {
                    pac = pc + pac;
                    pacc = pcc + pacc;
                    ent++;
                }
                ca = c;

            } else {

                c = rs.getString("codigoEnt");
                pp = Double.parseDouble(rs.getString("precoEnt"));
                ppc = (rs.getString("custoEnt") != null) ? Double.parseDouble(rs.getString("custoEnt")) : 0;
                if (!ca.equals(c)) {
                    pap = pp + pap;
                    papc = ppc + papc;
                    ent++;
                }
                ca = c;

            }

            en.setEnt(ent);

            en.setPtotal(pad + pac + pap);
            en.setPdin(pad);
            en.setPcartao(pac);
            en.setPpix(pap);

            en.setCtotal(padc + pacc + papc);
            en.setCdin(padc);
            en.setCcartao(pacc);
            en.setCpix(papc);

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

        String SQL = "SELECT * FROM entradas LEFT JOIN tiposervico ON tiposervico.idTipSer = entradas.idTipSer LEFT JOIN estoque ON estoque.idEst = entradas.idEst WHERE dataEnt LIKE '%" + busca + "%' OR precoEnt LIKE '%" + busca + "%' OR detalhesEnt LIKE '%" + busca + "%' OR formapagamentoEnt LIKE '%" + busca + "%' OR clienteEnt LIKE '%" + busca + "%' OR custoEnt LIKE '%" + busca + "%' OR fornecedorEnt LIKE '%" + busca + "%' OR codigoEnt LIKE '%" + busca + "%' OR estoque.tipoprodutoEst LIKE '%" + busca + "%' OR estoque.modeloEst LIKE '%" + busca + "%' OR estoque.marcaEst LIKE '%" + busca + "%' OR estoque.tipochipEst LIKE '%" + busca + "%' OR tiposervico.descricaoTipSer LIKE '%" + busca + "%' ORDER BY dataEnt DESC";
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
