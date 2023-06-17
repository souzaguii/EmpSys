package view;

import com.formdev.flatlaf.FlatLightLaf;
import dao.despezasDAO;
import dao.entradaDAO;
import dao.estoqueDAO;
import dao.tiposervicoDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import model.estoque;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import model.despezas;
import model.entrada;
import model.tiposervico;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public final class main extends javax.swing.JFrame {

    public main() {

        initComponents();
        iniciasistema();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                try {

                    entrada en = new entrada();
                    entradaDAO endao = new entradaDAO();

                    if (tblSelIteCadEnt.getRowCount() != 0) {

                        for (int i = 1; i <= tblSelIteCadEnt.getRowCount(); i++) {

                            en.setQuantidade(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 0).toString()));
                            en.setIdestoque(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 1).toString()));

                            endao.atualizarestoque(en, 1);

                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                try {
                    entrada en = new entrada();
                    entradaDAO endao = new entradaDAO();

                    if (tblSelIteCadEnt.getRowCount() != 0) {

                        for (int i = 0; i <= tblSelIteCadEnt.getRowCount(); i++) {

                            en.setQuantidade(Integer.parseInt(tblSelIteCadEnt.getValueAt(i, 0).toString()));
                            en.setIdestoque(Integer.parseInt(tblSelIteCadEnt.getValueAt(i, 1).toString()));

                            endao.atualizarestoque(en, 1);

                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

    }

    public void iniciasistema() {

        pnlCadEst.setVisible(false);
        pnlConEst.setVisible(false);
        pnlCadTipSer.setVisible(false);
        pnlGerTipSer.setVisible(false);
        pnlGerEst.setVisible(false);
        pnlCadEnt.setVisible(false);
        pnlIteCadEnt.setVisible(false);
        pnlRel.setVisible(false);
        pnlMas.setVisible(false);
        pnlDes.setVisible(false);
        pnlCadDes.setVisible(false);
        pnlGerDes.setVisible(false);
        pnlGerEnt.setVisible(false);
        pnlIteGerEnt.setVisible(false);
        pnlOs.setVisible(false);

        txtTipCadEst.setVisible(false);
        lblR$CadEst.setVisible(false);
        lblR$Des.setVisible(false);
        txtTipConEst.setVisible(false);
        lblTitPri.setVisible(false);
        txtCodCadEnt.setVisible(false);

        DefaultTableModel model = (DefaultTableModel) tblSelIteGerEnt.getModel();
        model.setRowCount(0);

        DefaultTableModel model1 = (DefaultTableModel) tblSelIteCadEnt.getModel();
        model1.setRowCount(0);

        btnGerTipSer.setVisible(false);
        btnCadTipSer.setVisible(false);
        btnCadEst.setVisible(false);
        btnConEst.setVisible(false);
        btnGerEst.setVisible(false);
        btnCadDes.setVisible(false);
        btnDes.setVisible(false);
        btnMasPla.setVisible(false);
        btnCadEnt.setVisible(false);
        btnGerDes.setVisible(false);
        btnGerEnt.setVisible(false);

    }

    public Color corforeazul = new java.awt.Color(10, 60, 133);
    public Color corforeazulenter = new Color(19, 84, 178);

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatterbanco = new SimpleDateFormat("yyyy-MM-dd");
    DateTimeFormatter formatteratual = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void anitxtin(JLabel lbl) {

        Timer timer = new Timer(4, new ActionListener() {

            int x = lbl.getX();
            int y = lbl.getY();
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {

                y--;
                count++;

                lbl.setLocation(x, y);

                if (count == 20) {

                    ((Timer) e.getSource()).stop();

                }

            }
        });
        timer.start();

    }

    public void anitxtout(JLabel lbl) {

        Timer timer = new Timer(4, new ActionListener() {

            int x = lbl.getX();
            int y = lbl.getY();
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {

                y++;
                count++;

                lbl.setLocation(x, y);

                if (count == 20) {

                    ((Timer) e.getSource()).stop();

                }

            }
        });
        timer.start();

    }

    public Font fontmed(int size) {

        try {
            Font fonte = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fonts/Poppins-Medium.ttf"));
            return fonte.deriveFont((float) size);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Font fontbold(int size) {

        try {
            Font fonte = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fonts/Poppins-SemiBold.ttf"));
            return fonte.deriveFont((float) size);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean tabelatiposervico() {

        try {

            tiposervicoDAO tsdao = new tiposervicoDAO();

            List<tiposervico> lista = tsdao.buscartodos();

            if (!lista.isEmpty()) {

                DefaultTableModel modelo = new DefaultTableModel();

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

                centerRenderer.setHorizontalAlignment(JLabel.LEFT);

                modelo.addColumn("Descrição");

                for (tiposervico ts : lista) {
                    Object[] rowData = {
                        ts.getDescricao()
                    };

                    modelo.addRow(rowData);
                }

                tblTipSer.setModel(modelo);

                tblTipSer.setRowHeight(25);

                tblTipSer.setDefaultEditor(Object.class, null);

                scrTipSer.getVerticalScrollBar().setValue(0);

            } else {

                return false;

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private boolean tabelaestoqueconsulta(estoque es, JTable tbl, JScrollPane scr) {
        try {
            estoqueDAO esdao = new estoqueDAO();
            List<estoque> lista = esdao.buscar(es);

            if (!lista.isEmpty()) {
                JTableHeader header = tbl.getTableHeader();
                DefaultTableModel modelo = new DefaultTableModel();

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

                String[] colunas = {
                    "ID",
                    "Produto",
                    "Marca",
                    "Modelo",
                    "Preço",
                    "Quantidade",
                    "Localização",
                    "Detalhes",
                    "Cor",
                    "Material",
                    "Chip"
                };

                for (String coluna : colunas) {
                    boolean colunaVazia = true;

                    for (estoque ess : lista) {
                        Object valorColuna = getColumnValue(ess, coluna);
                        if (valorColuna != null && !valorColuna.toString().isEmpty()) {
                            colunaVazia = false;
                            break;
                        }
                    }

                    if (!colunaVazia) {
                        modelo.addColumn(coluna);
                    }
                }

                for (estoque ess : lista) {
                    Object[] rowData = new Object[modelo.getColumnCount()];

                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        String columnName = modelo.getColumnName(i);
                        Object columnValue = getColumnValue(ess, columnName);

                        if (columnName.equals("Preço") && columnValue instanceof Double) {
                            columnValue = moedadoublereal((Double) columnValue);
                        }

                        if (columnName.equals("Detalhes") && (columnValue == null || columnValue.toString().isEmpty())) {
                            columnValue = "Sem Detalhes";
                        }

                        if (columnName.equals("Cor") && (columnValue == null || columnValue.toString().isEmpty())) {
                            columnValue = "Não Aplicável";
                        }

                        rowData[i] = columnValue;
                    }

                    modelo.addRow(rowData);
                }

                deheader.setHorizontalAlignment(JLabel.CENTER);
                deheader.setForeground(Color.BLACK);
                deheader.setFont(fontmed(14));

                header.setForeground(corforeazul);
                header.setBackground(new Color(246, 246, 246));

                header.setFont(fontbold(13));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(deheader);
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private boolean tabelarelatorio(JTable tbl, JScrollPane scr, int opc, int opc1, String data1, String data2) {

        try {

            entradaDAO endao = new entradaDAO();
            List<String[]> lista = endao.buscar(opc, opc1, data1, data2);

            if (!lista.isEmpty()) {

                DefaultTableModel modelo = new DefaultTableModel();

                JTableHeader header = tbl.getTableHeader();

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

                modelo.addColumn("Data");
                modelo.addColumn("Serviço");
                modelo.addColumn("Produto");
                modelo.addColumn("Preço");
                modelo.addColumn("Quantidade");
                modelo.addColumn("Detalhes");
                modelo.addColumn("Código Entrada");

                for (String[] row : lista) {

                    Object[] rowData = new Object[7];

                    Date date = formatterbanco.parse(row[0]);
                    rowData[0] = formatter.format(date);
                    rowData[1] = (row[1] != null) ? row[1] : "Nenhum Serviço";
                    rowData[2] = (!"null - null null - null".equals(row[2]) && row[2] != null) ? row[2] : "Nenhum Produto";
                    rowData[3] = moedadoublereal(Double.valueOf(row[3]));
                    rowData[4] = (!"0".equals(row[4]) && row[4] != null) ? row[4] : "Não Aplicável";
                    rowData[5] = (row[5] != null && !"".equals(row[5])) ? row[5] : "Sem Detalhes";
                    rowData[6] = row[6];

                    modelo.addRow(rowData);

                }

                deheader.setHorizontalAlignment(JLabel.CENTER);

                deheader.setForeground(Color.BLACK);
                deheader.setFont(fontmed(12));

                header.setForeground(corforeazul);
                header.setBackground(new Color(246, 246, 246));

                header.setFont(fontbold(13));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(deheader);
                }

                TableColumnModel columnModel = tbl.getColumnModel();
                TableColumn column = columnModel.getColumn(6);
                columnModel.removeColumn(column);

                double somavalor = 0;
                int somaentrada = 0;
                String codigoAnterior = null;

                for (String[] row : lista) {
                    String codigoAtual = row[6];

                    if (codigoAnterior == null || !codigoAtual.equals(codigoAnterior)) {
                        double valor = Double.parseDouble(row[3]);
                        somavalor += valor;
                        somaentrada++;
                    }

                    codigoAnterior = codigoAtual;
                }

                tblRel.setVisible(true);
                scrRel.setVisible(true);

                lblValTotRel.setText(moedadoublereal(somavalor));
                lblValMedRel.setText(moedadoublereal(somavalor / somaentrada));
                lblTotEntRel.setText(String.valueOf(somaentrada));

            } else {

                tblRel.setVisible(false);
                scrRel.setVisible(false);

                lblValTotRel.setText("R$0,00");
                lblValMedRel.setText("R$0,00");
                lblTotEntRel.setText("0");

            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private boolean tabelagerenciarentrada(JTable tbl, JScrollPane scr, String data) {

        try {

            entradaDAO endao = new entradaDAO();

            List<String[]> lista = endao.buscargerenciar(data);

            if (!lista.isEmpty()) {

                DefaultTableModel modelo = new DefaultTableModel();

                JTableHeader header = tbl.getTableHeader();

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

                modelo.addColumn("Data");
                modelo.addColumn("Área");
                modelo.addColumn("Serviço");
                modelo.addColumn("Produto");
                modelo.addColumn("Preço");
                modelo.addColumn("Quantidade");
                modelo.addColumn("Detalhes");
                modelo.addColumn("Código Entrada");

                for (String[] row : lista) {

                    Object[] rowData = new Object[8];

                    Date date = formatterbanco.parse(row[0]);
                    rowData[0] = formatter.format(date);
                    rowData[1] = row[1];
                    rowData[2] = (row[2] != null) ? row[2] : "Nenhum Serviço";
                    rowData[3] = (!"null - null null - null".equals(row[3]) && row[3] != null) ? row[3] : "Nenhum Produto";
                    rowData[4] = moedadoublereal(Double.valueOf(row[4]));
                    rowData[5] = (!"0".equals(row[5]) && row[5] != null) ? row[5] : "Não Aplicável";
                    rowData[6] = (row[6] != null && !"".equals(row[6])) ? row[6] : "Sem Detalhes";
                    rowData[7] = row[7];

                    modelo.addRow(rowData);

                }

                deheader.setHorizontalAlignment(JLabel.CENTER);

                deheader.setForeground(Color.BLACK);
                deheader.setFont(fontmed(12));

                header.setForeground(corforeazul);
                header.setBackground(new Color(246, 246, 246));

                header.setFont(fontbold(12));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(deheader);
                }

                tbl.setVisible(true);
                scr.setVisible(true);

            } else {

                return false;
            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private boolean tabeladespezas(JTable tbl, JScrollPane scr) {

        try {

            despezasDAO desdao = new despezasDAO();
            List<String[]> lista = desdao.buscar();

            if (!lista.isEmpty()) {

                JTableHeader header = tbl.getTableHeader();

                DefaultTableModel modelo = new DefaultTableModel();

                modelo.addColumn("ID");
                modelo.addColumn("Descrição");
                modelo.addColumn("Preço");
                modelo.addColumn("Data");
                modelo.addColumn("Data Conclusão");

                for (String[] row : lista) {

                    Object[] rowData = new Object[5];

                    Date date = formatterbanco.parse(row[3]);

                    Date datecon = null;

                    if (row[4] != null) {

                        datecon = formatterbanco.parse(row[4]);

                    }

                    rowData[0] = row[0];
                    rowData[1] = row[1];
                    rowData[2] = moedadoublereal(Double.valueOf(row[2]));
                    rowData[3] = formatter.format(date);
                    rowData[4] = (row[4] != null) ? formatter.format(datecon) : "Não Concluído";

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                        try {
                            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                            Date dataatual = new Date();

                            Date data = formatter.parse(table.getValueAt(row, 3).toString());

                            Object datacon = table.getValueAt(row, 4);

                            int comparacao1 = 0;
                            int comparacao2 = 0;

                            long diferencaMilissegundos = Math.abs(dataatual.getTime() - data.getTime());

                            long diferencaDias = TimeUnit.DAYS.convert(diferencaMilissegundos, TimeUnit.MILLISECONDS);

                            comparacao1 = dataatual.compareTo(data);

                            if (!datacon.equals("Não Concluído")) {

                                comparacao2 = data.compareTo(formatter.parse(table.getValueAt(row, 4).toString()));

                                if (comparacao1 < 0 && comparacao2 > 0 && diferencaDias > 10) { //dataatual menor data e data maior datacon

                                    component.setBackground(new Color(182, 222, 170));//verde

                                } else {

                                    component.setBackground(new Color(229, 190, 190));//vermelho

                                }

                            } else {

                                if (comparacao1 < 0 && diferencaDias > 10) {

                                    component.setBackground(new Color(182, 222, 170));//verde

                                } else {

                                    component.setBackground(new Color(229, 190, 190));
                                }

                            }

                            component.setFont(fontmed(12));

                            return component;

                        } catch (ParseException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }
                };

                cellRenderer.setHorizontalAlignment(JLabel.CENTER);

                cellRenderer.setForeground(Color.BLACK);
                cellRenderer.setFont(fontmed(12));

                header.setForeground(corforeazul);
                header.setBackground(new Color(246, 246, 246));

                header.setFont(fontbold(13));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                }

                tbl.setVisible(true);
                scr.setVisible(true);

            } else {

                return false;

            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private boolean tabelagerenciardespezas(JTable tbl, JScrollPane scr) {

        try {

            despezasDAO desdao = new despezasDAO();
            List<String[]> lista = desdao.buscar();

            if (!lista.isEmpty()) {

                JTableHeader header = tbl.getTableHeader();

                DefaultTableModel modelo = new DefaultTableModel();
                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

                modelo.addColumn("ID");
                modelo.addColumn("Descrição");
                modelo.addColumn("Preço");
                modelo.addColumn("Data");
                modelo.addColumn("Data Conclusão");

                for (String[] row : lista) {

                    Object[] rowData = new Object[5];

                    Date date = formatterbanco.parse(row[3]);

                    Date datecon = null;

                    if (row[4] != null) {

                        datecon = formatterbanco.parse(row[4]);

                    }

                    rowData[0] = row[0];
                    rowData[1] = row[1];
                    rowData[2] = moedadoublereal(Double.valueOf(row[2]));
                    rowData[3] = formatter.format(date);
                    rowData[4] = (row[4] != null) ? formatter.format(datecon) : "Não Concluído";

                    modelo.addRow(rowData);

                }

                cellRenderer.setHorizontalAlignment(JLabel.CENTER);

                cellRenderer.setForeground(Color.BLACK);
                cellRenderer.setFont(fontmed(12));

                header.setForeground(corforeazul);
                header.setBackground(new Color(246, 246, 246));

                header.setFont(fontbold(13));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                }

                tbl.setVisible(true);
                scr.setVisible(true);

            } else {

                return false;

            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private void tabelaitensselecionados() {

        JTableHeader header = tblSelIteCadEnt.getTableHeader();

        DefaultTableModel modelo = new DefaultTableModel();

        DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

        String[] colunas = {
            "Quantidade",
            "ID",
            "Produto",
            "Preço"

        };

        for (String coluna : colunas) {

            modelo.addColumn(coluna);

        }

        deheader.setHorizontalAlignment(JLabel.CENTER);

        deheader.setForeground(Color.BLACK);
        deheader.setFont(fontmed(11));

        header.setForeground(corforeazul);
        header.setBackground(new Color(246, 246, 246));

        header.setFont(fontbold(11));
        header.setReorderingAllowed(false);

        tblSelIteCadEnt.setModel(modelo);
        tblSelIteCadEnt.setRowHeight(25);
        tblSelIteCadEnt
                .setDefaultEditor(Object.class,
                        null);
        scrSelIteCadEnt.getVerticalScrollBar().setValue(0);

        for (int i = 0; i < tblSelIteCadEnt.getColumnCount(); i++) {
            tblSelIteCadEnt.getColumnModel().getColumn(i).setCellRenderer(deheader);
        }

        tblSelIteCadEnt.setModel(modelo);

    }

    private void tabelaitensselecionadosgerenciar(String codigo) {

        try {

            JTableHeader header = tblSelIteGerEnt.getTableHeader();

            DefaultTableModel modelo = new DefaultTableModel();

            DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

            entradaDAO endao = new entradaDAO();

            List<String[]> lista = endao.buscaritensselecionados(codigo);

            boolean ex = false;

            if (!lista.isEmpty()) {

                modelo.addColumn("Quantidade");
                modelo.addColumn("ID");
                modelo.addColumn("Produto");
                modelo.addColumn("Preço");

                for (String[] row : lista) {

                    if (row[1] != null) {

                        ex = true;
                        Object[] rowData = new Object[5];

                        rowData[0] = row[0];
                        rowData[1] = row[1];
                        rowData[2] = row[2];
                        rowData[3] = (row[3] != null) ? moedadoublereal(Double.valueOf(row[3])) : null;

                        modelo.addRow(rowData);

                    } else {

                        ex = false;

                    }
                }

                if (ex) {

                    deheader.setHorizontalAlignment(JLabel.CENTER);

                    deheader.setForeground(Color.BLACK);
                    deheader.setFont(fontmed(11));

                    header.setForeground(corforeazul);
                    header.setBackground(new Color(246, 246, 246));

                    header.setFont(fontbold(11));
                    header.setReorderingAllowed(false);

                    tblSelIteGerEnt.setModel(modelo);
                    tblSelIteGerEnt.setRowHeight(25);
                    tblSelIteGerEnt.setDefaultEditor(Object.class, null);

                    scrSelIteGerEnt.getVerticalScrollBar().setValue(0);

                    for (int i = 0; i < tblSelIteGerEnt.getColumnCount(); i++) {
                        tblSelIteGerEnt.getColumnModel().getColumn(i).setCellRenderer(deheader);
                    }

                    tblSelIteGerEnt.setModel(modelo);

                    tblSelIteGerEnt.setVisible(true);
                    scrSelIteGerEnt.setVisible(true);
                    lblSelIteGerEnt.setVisible(true);

                } else {

                    deheader.setHorizontalAlignment(JLabel.CENTER);

                    deheader.setForeground(Color.BLACK);
                    deheader.setFont(fontmed(11));

                    header.setForeground(corforeazul);
                    header.setBackground(new Color(246, 246, 246));

                    header.setFont(fontbold(11));
                    header.setReorderingAllowed(false);

                    tblSelIteGerEnt.setModel(modelo);
                    tblSelIteGerEnt.setRowHeight(25);
                    tblSelIteGerEnt.setDefaultEditor(Object.class, null);

                    scrSelIteGerEnt.getVerticalScrollBar().setValue(0);

                    for (int i = 0; i < tblSelIteGerEnt.getColumnCount(); i++) {
                        tblSelIteGerEnt.getColumnModel().getColumn(i).setCellRenderer(deheader);
                    }

                    tblSelIteGerEnt.setModel(modelo);

                    DefaultTableModel model = (DefaultTableModel) tblSelIteGerEnt.getModel();
                    model.setRowCount(0);

                    tblSelIteGerEnt.setVisible(false);
                    scrSelIteGerEnt.setVisible(false);
                    lblSelIteGerEnt.setVisible(false);

                }

            } else {

                tblSelIteGerEnt.setVisible(false);
                scrSelIteGerEnt.setVisible(false);
                lblSelIteGerEnt.setVisible(false);
            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void adicionarprodutos(JTable tabelaOrigem, JTable tabelaDestino, String qua, JRadioButton rbtn) {

        try {
            DefaultTableModel modelo = (DefaultTableModel) tabelaDestino.getModel();

            String idt = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("ID"))).toString();
            String precot = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Preço"))).toString();
            String produtot = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Produto"))).toString();
            String marcat = null;
            String chipt = null;
            String modelot = null;

            entrada en = new entrada();
            entradaDAO endao = new entradaDAO();

            en.setIdestoque(Integer.parseInt(idt));
            en.setQuantidade(Integer.parseInt(qua));

            endao.atualizarestoque(en, 0);

            if (rbtn.isSelected()) {

                chipt = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Chip"))).toString();

                Object[] novaLinha = {qua, idt, produtot + " - " + chipt, precot};

                modelo.addRow(novaLinha);

                tabelaDestino.setModel(modelo);

            } else {

                marcat = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Marca"))).toString();
                modelot = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Modelo"))).toString();

                Object[] novaLinha = {qua, idt, produtot + " - " + marcat + " " + modelot, precot};

                modelo.addRow(novaLinha);

                tabelaDestino.setModel(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean tabelaestoquegerenciar(estoque es) {
        try {
            estoqueDAO esdao = new estoqueDAO();
            List<estoque> lista = esdao.buscar(es);

            if (!lista.isEmpty()) {
                JTableHeader header = tblGerEst.getTableHeader();
                DefaultTableModel modelo = new DefaultTableModel();
                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

                String[] colunas = {
                    "ID",
                    "Produto",
                    "Marca",
                    "Modelo",
                    "Preço",
                    "Quantidade",
                    "Localização",
                    "Detalhes",
                    "Cor",
                    "Material",
                    "Chip"
                };

                for (String coluna : colunas) {
                    boolean colunaVazia = true;

                    for (estoque ess : lista) {
                        Object valorColuna = getColumnValue(ess, coluna);
                        if (valorColuna != null && !valorColuna.toString().isEmpty()) {
                            colunaVazia = false;
                            break;
                        }
                    }

                    if (!colunaVazia) {
                        modelo.addColumn(coluna);
                    }
                }

                for (estoque ess : lista) {
                    Object[] rowData = new Object[modelo.getColumnCount()];

                    for (int i = 0; i < modelo.getColumnCount(); i++) {
                        String columnName = modelo.getColumnName(i);
                        Object columnValue = getColumnValue(ess, columnName);

                        if (columnName.equals("Preço") && columnValue instanceof Double) {
                            columnValue = moedadoublereal((Double) columnValue);
                        }

                        if (columnName.equals("Cor") && (columnValue == null || columnValue.toString().isEmpty())) {
                            columnValue = "Não Aplicável";
                        }

                        if (columnName.equals("Detalhes") && (columnValue == null || columnValue.toString().isEmpty())) {
                            columnValue = "Sem Detalhes";
                        }

                        rowData[i] = columnValue;
                    }

                    modelo.addRow(rowData);
                }

                deheader.setHorizontalAlignment(JLabel.CENTER);

                header.setBackground(new Color(246, 246, 246));
                header.setFont(fontbold(11));
                header.setForeground(corforeazul);
                header.setReorderingAllowed(false);

                tblGerEst.setModel(modelo);
                tblGerEst.setRowHeight(25);
                tblGerEst.setDefaultEditor(Object.class, null);
                scrGerEst.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tblGerEst.getColumnCount(); i++) {
                    tblGerEst.getColumnModel().getColumn(i).setCellRenderer(deheader);
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private Object getColumnValue(estoque ess, String columnName) {
        return switch (columnName) {
            case "ID" ->
                ess.getId();
            case "Produto" ->
                ess.getTipoproduto();
            case "Marca" ->
                ess.getMarca();
            case "Modelo" ->
                ess.getModelo();
            case "Preço" ->
                ess.getPreco();
            case "Quantidade" ->
                ess.getQuantidade();
            case "Localização" ->
                ess.getLocalizacao();
            case "Detalhes" ->
                ess.getDetalhes();
            case "Cor" ->
                ess.getCor();
            case "Material" ->
                ess.getMaterial();
            case "Chip" ->
                ess.getTipochip();
            default ->
                null;
        };
    }

    public void comboboxentrada(JComboBox cmb, int op) {

        try {

            tiposervicoDAO tpdao = new tiposervicoDAO();

            List<tiposervico> tplist = null;

            switch (op) {
                case 1 ->
                    tplist = tpdao.buscarvenser();
                case 2 ->
                    tplist = tpdao.buscarass();
            }

            List<itens> listaitens = new ArrayList<>();

            for (tiposervico objeto : tplist) {

                listaitens.add(new itens(objeto.getIdtiposervico(), objeto.getDescricao()));

            }

            cmb.removeAllItems();

            for (itens objeto : listaitens) {
                cmb.addItem(objeto);
            }

            cmb.insertItemAt("Selecione o serviço", 0);
            cmb.setSelectedIndex(0);

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    static class itens {

        private final int id;
        private final String descricao;

        public itens(int id, String descricao) {
            this.id = id;
            this.descricao = descricao;
        }

        public int getId() {
            return id;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    private String moedadoublereal(Double valor) {
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatadorMoeda.format(valor);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        btnGroup1 = new javax.swing.ButtonGroup();
        pnlPri = new javax.swing.JPanel();
        imgLogo = new javax.swing.JLabel();
        btnCadEnt = new javax.swing.JLabel();
        btnGerEnt = new javax.swing.JLabel();
        btnEntPri = new javax.swing.JLabel();
        btnEstPri = new javax.swing.JLabel();
        btnRelPri = new javax.swing.JLabel();
        btnOrdSerPri = new javax.swing.JLabel();
        btnTipSerPri = new javax.swing.JLabel();
        btnOutPri = new javax.swing.JLabel();
        lblTitPri = new javax.swing.JLabel();
        btnCadTipSer = new javax.swing.JLabel();
        btnGerTipSer = new javax.swing.JLabel();
        btnCadEst = new javax.swing.JLabel();
        btnGerDes = new javax.swing.JLabel();
        btnConEst = new javax.swing.JLabel();
        btnGerEst = new javax.swing.JLabel();
        btnMasPla = new javax.swing.JLabel();
        btnCadDes = new javax.swing.JLabel();
        btnDes = new javax.swing.JLabel();
        pnlOs = new javax.swing.JPanel();
        btnGerOs = new javax.swing.JButton();
        btnCanOs = new javax.swing.JButton();
        lblEndOs = new javax.swing.JLabel();
        txtEndOs = new javax.swing.JTextField();
        sepModCadEst1 = new javax.swing.JSeparator();
        lblCliOs = new javax.swing.JLabel();
        txtCliOs = new javax.swing.JTextField();
        sepMarCadEst1 = new javax.swing.JSeparator();
        lblEquOs = new javax.swing.JLabel();
        txtEquOs = new javax.swing.JTextField();
        sepCorCadEst1 = new javax.swing.JSeparator();
        lblMarOs = new javax.swing.JLabel();
        txtMarOs = new javax.swing.JTextField();
        sepMatCadEst1 = new javax.swing.JSeparator();
        lblTelOs = new javax.swing.JLabel();
        txtTelOs = new javax.swing.JTextField();
        sepQuaCadEst1 = new javax.swing.JSeparator();
        lblModOs = new javax.swing.JLabel();
        txtModOs = new javax.swing.JTextField();
        sepLocCadEst1 = new javax.swing.JSeparator();
        lblConOs = new javax.swing.JLabel();
        txtConOs = new javax.swing.JTextField();
        sepDetCadEst1 = new javax.swing.JSeparator();
        lblDefOs = new javax.swing.JLabel();
        txtDefOs = new javax.swing.JTextField();
        sepDetCadEst2 = new javax.swing.JSeparator();
        lblDatOs = new javax.swing.JLabel();
        txtDatOs = new javax.swing.JTextField();
        sepDetCadEst3 = new javax.swing.JSeparator();
        lblHorOs = new javax.swing.JLabel();
        txtHorOs = new javax.swing.JTextField();
        sepDetCadEst4 = new javax.swing.JSeparator();
        pnlGerEnt = new javax.swing.JPanel();
        btnExcGerEnt = new javax.swing.JButton();
        btnBusGerEnt = new javax.swing.JButton();
        btnIteGerEnt = new javax.swing.JButton();
        btnCanGerEnt = new javax.swing.JButton();
        lblDatGerEnt = new javax.swing.JLabel();
        txtDatGerEnt = new javax.swing.JTextField();
        sepDatGerEnt = new javax.swing.JSeparator();
        lblR$GerEnt = new javax.swing.JLabel();
        lblPreGerEnt = new javax.swing.JLabel();
        txtPreGerEnt = new javax.swing.JTextField();
        sepPreGerEnt = new javax.swing.JSeparator();
        lblDetGerEnt = new javax.swing.JLabel();
        txtDetGerEnt = new javax.swing.JTextField();
        sepDetGerEnt = new javax.swing.JSeparator();
        lblDatBusGerEnt = new javax.swing.JLabel();
        txtDatBusGerEnt = new javax.swing.JTextField();
        sepMod3 = new javax.swing.JSeparator();
        sepBusGerEst1 = new javax.swing.JSeparator();
        cmbSerGerEnt = new javax.swing.JComboBox<>();
        lblSerGerEnt = new javax.swing.JLabel();
        scrGerEnt = new javax.swing.JScrollPane();
        tblGerEnt = new javax.swing.JTable();
        btnAltGerEnt = new javax.swing.JButton();
        pnlIteGerEnt = new javax.swing.JPanel();
        scrEstIteGerEnt = new javax.swing.JScrollPane();
        tblEstIteGerEnt = new javax.swing.JTable();
        scrSelIteGerEnt = new javax.swing.JScrollPane();
        tblSelIteGerEnt = new javax.swing.JTable();
        btnVolIteGerEnt = new javax.swing.JButton();
        lblSelIteGerEnt = new javax.swing.JLabel();
        lblEstIteGerEnt = new javax.swing.JLabel();
        rbtnAssIteGerEnt = new javax.swing.JRadioButton();
        rbtnPelIteGerEnt = new javax.swing.JRadioButton();
        rbtnCapIteGerEnt = new javax.swing.JRadioButton();
        rbtnChiIteGerEnt = new javax.swing.JRadioButton();
        lblBusIteGerEnt = new javax.swing.JLabel();
        txtBusIteGerEnt = new javax.swing.JTextField();
        sepBusIteCadEnt1 = new javax.swing.JSeparator();
        pnlDes = new javax.swing.JPanel();
        scrConDes = new javax.swing.JScrollPane();
        tblConDes = new javax.swing.JTable();
        btnVolDes = new javax.swing.JButton();
        pnlGerDes = new javax.swing.JPanel();
        lblDesGerDes = new javax.swing.JLabel();
        lblDatGerDes = new javax.swing.JLabel();
        lblPreGerDes = new javax.swing.JLabel();
        lblR$GerDes = new javax.swing.JLabel();
        txtDatGerDes = new javax.swing.JTextField();
        txtDesGerDes = new javax.swing.JTextField();
        sepPreGerDes = new javax.swing.JSeparator();
        sepDatGerDes = new javax.swing.JSeparator();
        sepDesGerDes = new javax.swing.JSeparator();
        txtPreGerDes = new javax.swing.JTextField();
        btnExcGerDes = new javax.swing.JButton();
        btnAltGerDes = new javax.swing.JButton();
        btnCanGerDes = new javax.swing.JButton();
        lblDesTipSer3 = new javax.swing.JLabel();
        scrGerDes = new javax.swing.JScrollPane();
        tblGerDes = new javax.swing.JTable();
        pnlCadDes = new javax.swing.JPanel();
        btnSalDes = new javax.swing.JButton();
        lblDatDes = new javax.swing.JLabel();
        txtDatDes = new javax.swing.JTextField();
        sepDatCadEnt7 = new javax.swing.JSeparator();
        lblDesDes = new javax.swing.JLabel();
        txtDesDes = new javax.swing.JTextField();
        sepDatCadEnt4 = new javax.swing.JSeparator();
        lblR$Des = new javax.swing.JLabel();
        lblPreDes = new javax.swing.JLabel();
        txtPreDes = new javax.swing.JTextField();
        sepDatCadEnt6 = new javax.swing.JSeparator();
        btnCanDes = new javax.swing.JButton();
        pnlRel = new javax.swing.JPanel();
        scrRel = new javax.swing.JScrollPane();
        tblRel = new javax.swing.JTable();
        btnVolRel = new javax.swing.JButton();
        rbtnSerTimRel = new javax.swing.JRadioButton();
        rbtnSerRel = new javax.swing.JRadioButton();
        rbtnVenRel = new javax.swing.JRadioButton();
        rbtnTodRel = new javax.swing.JRadioButton();
        lblDatIniRel = new javax.swing.JLabel();
        txtDatIniRel = new javax.swing.JTextField();
        sepDatCadEnt1 = new javax.swing.JSeparator();
        btnTodRel = new javax.swing.JLabel();
        btnAnoRel = new javax.swing.JLabel();
        lblDatFinRel = new javax.swing.JLabel();
        txtDatFinRel = new javax.swing.JTextField();
        sepDatCadEnt2 = new javax.swing.JSeparator();
        btnDiaRel = new javax.swing.JLabel();
        btnMesRel = new javax.swing.JLabel();
        btnSemRel = new javax.swing.JLabel();
        rbtnAssRel = new javax.swing.JRadioButton();
        sepDatCadEnt3 = new javax.swing.JSeparator();
        lblTotEntRel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblValTotRel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblValMedRel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pnlCadEnt = new javax.swing.JPanel();
        btnIteCadEnt = new javax.swing.JButton();
        btnSalCadEnt = new javax.swing.JButton();
        btnCanCadEnt = new javax.swing.JButton();
        rbtnSerCadEnt = new javax.swing.JRadioButton();
        rbtnVenCadEnt = new javax.swing.JRadioButton();
        rbtnAssCadEnt = new javax.swing.JRadioButton();
        lblDatCadEnt = new javax.swing.JLabel();
        txtDatCadEnt = new javax.swing.JTextField();
        sepDatCadEnt = new javax.swing.JSeparator();
        lblR$CadEnt = new javax.swing.JLabel();
        lblPreCadEnt = new javax.swing.JLabel();
        txtPreCadEnt = new javax.swing.JTextField();
        sepPreCadEnt = new javax.swing.JSeparator();
        cmbSerCadEnt = new javax.swing.JComboBox<>();
        lblDetCadEnt = new javax.swing.JLabel();
        txtDetCadEnt = new javax.swing.JTextField();
        sepDetCadEnt = new javax.swing.JSeparator();
        lblSerCadEnt = new javax.swing.JLabel();
        txtCodCadEnt = new javax.swing.JTextField();
        pnlIteCadEnt = new javax.swing.JPanel();
        btnVolIteCadEnt = new javax.swing.JButton();
        scrEstIteCadEnt = new javax.swing.JScrollPane();
        tblEstIteCadEnt = new javax.swing.JTable();
        scrSelIteCadEnt = new javax.swing.JScrollPane();
        tblSelIteCadEnt = new javax.swing.JTable();
        lblSelIteCadEnt = new javax.swing.JLabel();
        lblEstIteCadEnt = new javax.swing.JLabel();
        rbtnAssIteCadEnt = new javax.swing.JRadioButton();
        rbtnPelIteCadEnt = new javax.swing.JRadioButton();
        rbtnCapIteCadEnt = new javax.swing.JRadioButton();
        rbtnChiIteCadEnt = new javax.swing.JRadioButton();
        lblBusIteCadEnt = new javax.swing.JLabel();
        txtBusIteCadEnt = new javax.swing.JTextField();
        sepBusIteCadEnt = new javax.swing.JSeparator();
        pnlCadTipSer = new javax.swing.JPanel();
        btnSalTipSer = new javax.swing.JButton();
        btnCanTipSer = new javax.swing.JButton();
        rbtnOutTipSer = new javax.swing.JRadioButton();
        rbtnSerTimTipSer = new javax.swing.JRadioButton();
        rbtnAssTipSer = new javax.swing.JRadioButton();
        lblDesTipSer = new javax.swing.JLabel();
        txtDesTipSer = new javax.swing.JTextField();
        sepDesTipSer = new javax.swing.JSeparator();
        pnlCadEst = new javax.swing.JPanel();
        btnSalCadEst = new javax.swing.JButton();
        btnCanCadEst = new javax.swing.JButton();
        rbtnCapCadEst = new javax.swing.JRadioButton();
        rbtnPelCadEst = new javax.swing.JRadioButton();
        rbtnChiCadEst = new javax.swing.JRadioButton();
        rbtnAceCadEst = new javax.swing.JRadioButton();
        lblModCadEst = new javax.swing.JLabel();
        txtModCadEst = new javax.swing.JTextField();
        sepModCadEst = new javax.swing.JSeparator();
        lblMarCadEst = new javax.swing.JLabel();
        txtMarCadEst = new javax.swing.JTextField();
        sepMarCadEst = new javax.swing.JSeparator();
        lblCorCadEst = new javax.swing.JLabel();
        txtCorCadEst = new javax.swing.JTextField();
        sepCorCadEst = new javax.swing.JSeparator();
        lblMatCadEst = new javax.swing.JLabel();
        txtMatCadEst = new javax.swing.JTextField();
        sepMatCadEst = new javax.swing.JSeparator();
        lblQuaCadEst = new javax.swing.JLabel();
        txtQuaCadEst = new javax.swing.JTextField();
        sepQuaCadEst = new javax.swing.JSeparator();
        lblR$CadEst = new javax.swing.JLabel();
        lblPreCadEst = new javax.swing.JLabel();
        txtPreCadEst = new javax.swing.JTextField();
        sepPreCadEst = new javax.swing.JSeparator();
        lblChiCadEst = new javax.swing.JLabel();
        cmbChiCadEst = new javax.swing.JComboBox<>();
        lblLocCadEst = new javax.swing.JLabel();
        txtLocCadEst = new javax.swing.JTextField();
        sepLocCadEst = new javax.swing.JSeparator();
        lblDetCadEst = new javax.swing.JLabel();
        txtDetCadEst = new javax.swing.JTextField();
        sepDetCadEst = new javax.swing.JSeparator();
        txtTipCadEst = new javax.swing.JTextField();
        pnlConEst = new javax.swing.JPanel();
        btnCanConEst = new javax.swing.JButton();
        btnBusConEst = new javax.swing.JButton();
        rbtnCapConEst = new javax.swing.JRadioButton();
        rbtnPelConEst = new javax.swing.JRadioButton();
        rbtnChiConEst = new javax.swing.JRadioButton();
        rbtnAceConEst = new javax.swing.JRadioButton();
        lblBusConEst = new javax.swing.JLabel();
        txtBusConEst = new javax.swing.JTextField();
        sepBusConEst = new javax.swing.JSeparator();
        scrConEst = new javax.swing.JScrollPane();
        tblConEst = new javax.swing.JTable();
        txtTipConEst = new javax.swing.JTextField();
        pnlGerEst = new javax.swing.JPanel();
        btnExcGerEst = new javax.swing.JButton();
        btnBusGerEst = new javax.swing.JButton();
        btnAltGerEst = new javax.swing.JButton();
        btnCanGerEst = new javax.swing.JButton();
        rbtnCapGerEst = new javax.swing.JRadioButton();
        rbtnPelGerEst = new javax.swing.JRadioButton();
        rbtnChiGerEst = new javax.swing.JRadioButton();
        rbtnAceGerEst = new javax.swing.JRadioButton();
        lblModGerEst = new javax.swing.JLabel();
        txtModGerEst = new javax.swing.JTextField();
        sepModGerEst = new javax.swing.JSeparator();
        lblMarGerEst = new javax.swing.JLabel();
        txtMarGerEst = new javax.swing.JTextField();
        sepMarGerEst = new javax.swing.JSeparator();
        lblCorGerEst = new javax.swing.JLabel();
        txtCorGerEst = new javax.swing.JTextField();
        sepCorGerEst = new javax.swing.JSeparator();
        lblMatGerEst = new javax.swing.JLabel();
        txtMatGerEst = new javax.swing.JTextField();
        sepMatGerEst = new javax.swing.JSeparator();
        lblQuaGerEst = new javax.swing.JLabel();
        txtQuaGerEst = new javax.swing.JTextField();
        sepQuaGerEst = new javax.swing.JSeparator();
        lblR$GerEst = new javax.swing.JLabel();
        lblPreGerEst = new javax.swing.JLabel();
        txtPreGerEst = new javax.swing.JTextField();
        sepPreGerEst = new javax.swing.JSeparator();
        lblChiGerEst = new javax.swing.JLabel();
        cmbChiGerEst = new javax.swing.JComboBox<>();
        lblLocGerEst = new javax.swing.JLabel();
        txtLocGerEst = new javax.swing.JTextField();
        sepLocGerEst = new javax.swing.JSeparator();
        lblDetGerEst = new javax.swing.JLabel();
        txtDetGerEst = new javax.swing.JTextField();
        sepDetGerEst = new javax.swing.JSeparator();
        txtTipGerEst = new javax.swing.JTextField();
        lblBusGerEst = new javax.swing.JLabel();
        txtBusGerEst = new javax.swing.JTextField();
        sepMod2 = new javax.swing.JSeparator();
        sepBusGerEst = new javax.swing.JSeparator();
        scrGerEst = new javax.swing.JScrollPane();
        tblGerEst = new javax.swing.JTable();
        pnlMas = new javax.swing.JPanel();
        btnGerMas = new javax.swing.JButton();
        btnCanMas = new javax.swing.JButton();
        btnCopMas = new javax.swing.JLabel();
        lblNomMas = new javax.swing.JLabel();
        txtNomMas = new javax.swing.JTextField();
        sepDesGerTipSer1 = new javax.swing.JSeparator();
        sepDesGerTipSer2 = new javax.swing.JSeparator();
        lblNumConMas = new javax.swing.JLabel();
        sepDesGerTipSer3 = new javax.swing.JSeparator();
        txtNumConMas = new javax.swing.JTextField();
        lblCpfMas = new javax.swing.JLabel();
        sepDesGerTipSer4 = new javax.swing.JSeparator();
        txtCpfMas = new javax.swing.JTextField();
        lblNumAceMas = new javax.swing.JLabel();
        sepDesGerTipSer5 = new javax.swing.JSeparator();
        txtNumAceMas = new javax.swing.JTextField();
        lblNumPorMas = new javax.swing.JLabel();
        sepDesGerTipSer6 = new javax.swing.JSeparator();
        txtNumPorMas = new javax.swing.JTextField();
        rbtnMigTroMas = new javax.swing.JRadioButton();
        rbtnAtiMas = new javax.swing.JRadioButton();
        rbtnMigMas = new javax.swing.JRadioButton();
        lblPlaMas = new javax.swing.JLabel();
        sepDesGerTipSer7 = new javax.swing.JSeparator();
        txtPlaMas = new javax.swing.JTextField();
        rbtnSieMas = new javax.swing.JRadioButton();
        rbtnAppMas = new javax.swing.JRadioButton();
        chkC6Mas = new javax.swing.JCheckBox();
        lblVenMas = new javax.swing.JLabel();
        sepDesGerTipSer8 = new javax.swing.JSeparator();
        txtVenMas = new javax.swing.JTextField();
        sepDesGerTipSer9 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreMas = new javax.swing.JTextArea();
        pnlGerTipSer = new javax.swing.JPanel();
        btnExcGerTipSer = new javax.swing.JButton();
        btnAltGerTipSer = new javax.swing.JButton();
        btnCanGerTipSer = new javax.swing.JButton();
        lblDesTipSer2 = new javax.swing.JLabel();
        lblDesGerTipSer = new javax.swing.JLabel();
        txtDesGerTipSer = new javax.swing.JTextField();
        sepDesGerTipSer = new javax.swing.JSeparator();
        scrTipSer = new javax.swing.JScrollPane();
        tblTipSer = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EmpSys 1.0");
        setIconImage(new ImageIcon(getClass().getResource("/images/ICON.png")).getImage());
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlPri.setBackground(new java.awt.Color(246, 246, 246));
        pnlPri.setPreferredSize(new java.awt.Dimension(1300, 760));
        pnlPri.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        imgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LogoLoja580.png"))); // NOI18N
        imgLogo.setText("jLabel1");
        pnlPri.add(imgLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 580, 190));

        btnCadEnt.setFont(fontmed(12));
        btnCadEnt.setForeground(corforeazul);
        btnCadEnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadEnt.setText("Nova");
        btnCadEnt.setToolTipText("");
        btnCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadEntMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadEntMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCadEntMouseReleased(evt);
            }
        });
        pnlPri.add(btnCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 70, 20));

        btnGerEnt.setFont(fontmed(12));
        btnGerEnt.setForeground(corforeazul);
        btnGerEnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGerEnt.setText("Gerenciar");
        btnGerEnt.setToolTipText("");
        btnGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerEntMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerEntMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnGerEntMouseReleased(evt);
            }
        });
        pnlPri.add(btnGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 70, 20));

        btnEntPri.setFont(fontmed(14));
        btnEntPri.setForeground(new java.awt.Color(10, 60, 133));
        btnEntPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnEntPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BackBtnPrin4.png"))); // NOI18N
        btnEntPri.setText("Entrada");
        btnEntPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEntPri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEntPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEntPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEntPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEntPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnEntPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, -1, 40));

        btnEstPri.setFont(fontmed(14));
        btnEstPri.setForeground(new java.awt.Color(10, 60, 133));
        btnEstPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnEstPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BackBtnPrin4.png"))); // NOI18N
        btnEstPri.setText("Estoque");
        btnEstPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEstPri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEstPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEstPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEstPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEstPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnEstPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 250, -1, 40));

        btnRelPri.setFont(fontmed(14));
        btnRelPri.setForeground(new java.awt.Color(10, 60, 133));
        btnRelPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnRelPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BackBtnPrin3.png"))); // NOI18N
        btnRelPri.setText("Relatórios");
        btnRelPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRelPri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRelPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRelPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnRelPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnRelPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, -1, 40));

        btnOrdSerPri.setFont(fontmed(14));
        btnOrdSerPri.setForeground(new java.awt.Color(10, 60, 133));
        btnOrdSerPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnOrdSerPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BackBtnPrin2.png"))); // NOI18N
        btnOrdSerPri.setText("Ordem de Serviço");
        btnOrdSerPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOrdSerPri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOrdSerPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOrdSerPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOrdSerPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnOrdSerPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnOrdSerPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 250, 170, 40));

        btnTipSerPri.setFont(fontmed(14));
        btnTipSerPri.setForeground(new java.awt.Color(10, 60, 133));
        btnTipSerPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnTipSerPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BackBtnPrin2.png"))); // NOI18N
        btnTipSerPri.setText("Tipos de Serviços");
        btnTipSerPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTipSerPri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTipSerPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTipSerPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTipSerPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnTipSerPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnTipSerPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 250, 170, 40));

        btnOutPri.setFont(fontmed(14));
        btnOutPri.setForeground(new java.awt.Color(10, 60, 133));
        btnOutPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnOutPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BackBtnPrin4.png"))); // NOI18N
        btnOutPri.setText("Outros");
        btnOutPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOutPri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOutPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOutPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOutPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnOutPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnOutPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 250, 140, 40));

        lblTitPri.setFont(fontmed(17));
        lblTitPri.setForeground(corforeazul);
        lblTitPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitPri.setText("Cadastrar Estoque");
        pnlPri.add(lblTitPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 310, 270, 50));

        btnCadTipSer.setFont(fontmed(12));
        btnCadTipSer.setForeground(corforeazul);
        btnCadTipSer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadTipSer.setText("Cadastrar");
        btnCadTipSer.setToolTipText("");
        btnCadTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadTipSer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadTipSerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadTipSerMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCadTipSerMouseReleased(evt);
            }
        });
        pnlPri.add(btnCadTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 300, 70, 20));

        btnGerTipSer.setFont(fontmed(12));
        btnGerTipSer.setForeground(corforeazul);
        btnGerTipSer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGerTipSer.setText("Gerenciar");
        btnGerTipSer.setToolTipText("");
        btnGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerTipSer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerTipSerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerTipSerMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnGerTipSerMouseReleased(evt);
            }
        });
        pnlPri.add(btnGerTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 320, 70, 20));

        btnCadEst.setFont(fontmed(12));
        btnCadEst.setForeground(corforeazul);
        btnCadEst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadEst.setText("Cadastrar");
        btnCadEst.setToolTipText("");
        btnCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadEst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadEstMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadEstMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCadEstMouseReleased(evt);
            }
        });
        pnlPri.add(btnCadEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 300, 70, 20));

        btnGerDes.setFont(fontmed(12));
        btnGerDes.setForeground(corforeazul);
        btnGerDes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGerDes.setText("Gerenciar Despezas");
        btnGerDes.setToolTipText("");
        btnGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerDes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerDesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerDesMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnGerDesMouseReleased(evt);
            }
        });
        pnlPri.add(btnGerDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 360, 140, 20));

        btnConEst.setFont(fontmed(12));
        btnConEst.setForeground(corforeazul);
        btnConEst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnConEst.setText("Consultar");
        btnConEst.setToolTipText("");
        btnConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConEst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConEstMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConEstMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnConEstMouseReleased(evt);
            }
        });
        pnlPri.add(btnConEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 70, 20));

        btnGerEst.setFont(fontmed(12));
        btnGerEst.setForeground(corforeazul);
        btnGerEst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGerEst.setText("Gerenciar");
        btnGerEst.setToolTipText("");
        btnGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerEst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerEstMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerEstMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnGerEstMouseReleased(evt);
            }
        });
        pnlPri.add(btnGerEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 340, 90, 20));

        btnMasPla.setFont(fontmed(12));
        btnMasPla.setForeground(corforeazul);
        btnMasPla.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnMasPla.setText("Máscara Plano");
        btnMasPla.setToolTipText("");
        btnMasPla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMasPla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMasPlaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMasPlaMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMasPlaMouseReleased(evt);
            }
        });
        pnlPri.add(btnMasPla, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 300, 100, 20));

        btnCadDes.setFont(fontmed(12));
        btnCadDes.setForeground(corforeazul);
        btnCadDes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadDes.setText("Cadastrar Despezas");
        btnCadDes.setToolTipText("");
        btnCadDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadDes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadDesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadDesMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCadDesMouseReleased(evt);
            }
        });
        pnlPri.add(btnCadDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 340, 140, 20));

        btnDes.setFont(fontmed(12));
        btnDes.setForeground(corforeazul);
        btnDes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDes.setText("Despezas");
        btnDes.setToolTipText("");
        btnDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDesMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDesMouseReleased(evt);
            }
        });
        pnlPri.add(btnDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 320, 60, 20));

        pnlOs.setBackground(new java.awt.Color(246, 246, 246));
        pnlOs.setLayout(null);

        btnGerOs.setFont(fontmed(12));
        btnGerOs.setForeground(new java.awt.Color(10, 60, 133));
        btnGerOs.setText("Gerar");
        btnGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerOsActionPerformed(evt);
            }
        });
        pnlOs.add(btnGerOs);
        btnGerOs.setBounds(700, 280, 90, 50);

        btnCanOs.setFont(fontmed(12));
        btnCanOs.setForeground(new java.awt.Color(10, 60, 133));
        btnCanOs.setText("Cancelar");
        btnCanOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanOsActionPerformed(evt);
            }
        });
        pnlOs.add(btnCanOs);
        btnCanOs.setBounds(850, 280, 90, 50);

        lblEndOs.setFont(fontmed(12));
        lblEndOs.setForeground(new java.awt.Color(10, 60, 133));
        lblEndOs.setText("Endereço");
        lblEndOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblEndOs);
        lblEndOs.setBounds(370, 130, 60, 20);

        txtEndOs.setBackground(new java.awt.Color(246, 246, 246));
        txtEndOs.setFont(fontmed(13));
        txtEndOs.setBorder(null);
        txtEndOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEndOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEndOsFocusLost(evt);
            }
        });
        pnlOs.add(txtEndOs);
        txtEndOs.setBounds(370, 130, 240, 20);

        sepModCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepModCadEst1);
        sepModCadEst1.setBounds(370, 150, 240, 10);

        lblCliOs.setFont(fontmed(12));
        lblCliOs.setForeground(new java.awt.Color(10, 60, 133));
        lblCliOs.setText("Cliente");
        lblCliOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblCliOs);
        lblCliOs.setBounds(370, 30, 60, 20);

        txtCliOs.setBackground(new java.awt.Color(246, 246, 246));
        txtCliOs.setFont(fontmed(13));
        txtCliOs.setBorder(null);
        txtCliOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCliOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCliOsFocusLost(evt);
            }
        });
        pnlOs.add(txtCliOs);
        txtCliOs.setBounds(370, 30, 240, 20);

        sepMarCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepMarCadEst1);
        sepMarCadEst1.setBounds(370, 50, 240, 10);

        lblEquOs.setFont(fontmed(12));
        lblEquOs.setForeground(new java.awt.Color(10, 60, 133));
        lblEquOs.setText("Equipamento");
        lblEquOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblEquOs);
        lblEquOs.setBounds(700, 30, 80, 20);

        txtEquOs.setBackground(new java.awt.Color(246, 246, 246));
        txtEquOs.setFont(fontmed(13));
        txtEquOs.setBorder(null);
        txtEquOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEquOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEquOsFocusLost(evt);
            }
        });
        pnlOs.add(txtEquOs);
        txtEquOs.setBounds(700, 30, 240, 20);

        sepCorCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepCorCadEst1);
        sepCorCadEst1.setBounds(700, 50, 240, 10);

        lblMarOs.setFont(fontmed(12));
        lblMarOs.setForeground(new java.awt.Color(10, 60, 133));
        lblMarOs.setText("Marca");
        lblMarOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblMarOs);
        lblMarOs.setBounds(700, 80, 50, 20);

        txtMarOs.setBackground(new java.awt.Color(246, 246, 246));
        txtMarOs.setFont(fontmed(13));
        txtMarOs.setBorder(null);
        txtMarOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMarOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMarOsFocusLost(evt);
            }
        });
        pnlOs.add(txtMarOs);
        txtMarOs.setBounds(700, 80, 240, 20);

        sepMatCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepMatCadEst1);
        sepMatCadEst1.setBounds(700, 100, 240, 10);

        lblTelOs.setFont(fontmed(12));
        lblTelOs.setForeground(new java.awt.Color(10, 60, 133));
        lblTelOs.setText("Telefone");
        lblTelOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblTelOs);
        lblTelOs.setBounds(370, 80, 80, 20);

        txtTelOs.setBackground(new java.awt.Color(246, 246, 246));
        txtTelOs.setFont(fontmed(13));
        txtTelOs.setBorder(null);
        txtTelOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelOsFocusLost(evt);
            }
        });
        txtTelOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelOsKeyTyped(evt);
            }
        });
        pnlOs.add(txtTelOs);
        txtTelOs.setBounds(370, 80, 130, 20);

        sepQuaCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepQuaCadEst1);
        sepQuaCadEst1.setBounds(370, 100, 130, 10);

        lblModOs.setFont(fontmed(12));
        lblModOs.setForeground(new java.awt.Color(10, 60, 133));
        lblModOs.setText("Modelo");
        lblModOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblModOs);
        lblModOs.setBounds(700, 130, 60, 20);

        txtModOs.setBackground(new java.awt.Color(246, 246, 246));
        txtModOs.setFont(fontmed(13));
        txtModOs.setBorder(null);
        txtModOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtModOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtModOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtModOsFocusLost(evt);
            }
        });
        pnlOs.add(txtModOs);
        txtModOs.setBounds(700, 130, 240, 20);

        sepLocCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepLocCadEst1);
        sepLocCadEst1.setBounds(700, 150, 240, 10);

        lblConOs.setFont(fontmed(12));
        lblConOs.setForeground(new java.awt.Color(10, 60, 133));
        lblConOs.setText("Condições");
        lblConOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblConOs);
        lblConOs.setBounds(700, 180, 70, 20);

        txtConOs.setBackground(new java.awt.Color(246, 246, 246));
        txtConOs.setFont(fontmed(13));
        txtConOs.setBorder(null);
        txtConOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtConOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtConOsFocusLost(evt);
            }
        });
        pnlOs.add(txtConOs);
        txtConOs.setBounds(700, 180, 240, 20);

        sepDetCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst1);
        sepDetCadEst1.setBounds(700, 200, 240, 10);

        lblDefOs.setFont(fontmed(12));
        lblDefOs.setForeground(new java.awt.Color(10, 60, 133));
        lblDefOs.setText("Defeito(s)");
        lblDefOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblDefOs);
        lblDefOs.setBounds(700, 230, 70, 20);

        txtDefOs.setBackground(new java.awt.Color(246, 246, 246));
        txtDefOs.setFont(fontmed(13));
        txtDefOs.setBorder(null);
        txtDefOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDefOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDefOsFocusLost(evt);
            }
        });
        pnlOs.add(txtDefOs);
        txtDefOs.setBounds(700, 230, 240, 20);

        sepDetCadEst2.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst2);
        sepDetCadEst2.setBounds(700, 250, 240, 10);

        lblDatOs.setFont(fontmed(12));
        lblDatOs.setForeground(new java.awt.Color(10, 60, 133));
        lblDatOs.setText("Data");
        lblDatOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblDatOs);
        lblDatOs.setBounds(370, 160, 60, 20);

        txtDatOs.setBackground(new java.awt.Color(246, 246, 246));
        txtDatOs.setFont(fontmed(13));
        txtDatOs.setBorder(null);
        txtDatOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatOsFocusLost(evt);
            }
        });
        txtDatOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatOsKeyTyped(evt);
            }
        });
        pnlOs.add(txtDatOs);
        txtDatOs.setBounds(370, 180, 90, 20);

        sepDetCadEst3.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst3);
        sepDetCadEst3.setBounds(370, 200, 90, 10);

        lblHorOs.setFont(fontmed(12));
        lblHorOs.setForeground(new java.awt.Color(10, 60, 133));
        lblHorOs.setText("Horário");
        lblHorOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblHorOs);
        lblHorOs.setBounds(370, 210, 80, 20);

        txtHorOs.setBackground(new java.awt.Color(246, 246, 246));
        txtHorOs.setFont(fontmed(13));
        txtHorOs.setBorder(null);
        txtHorOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHorOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHorOsFocusLost(evt);
            }
        });
        txtHorOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHorOsKeyTyped(evt);
            }
        });
        pnlOs.add(txtHorOs);
        txtHorOs.setBounds(370, 230, 90, 20);

        sepDetCadEst4.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst4);
        sepDetCadEst4.setBounds(370, 250, 90, 10);

        pnlPri.add(pnlOs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 1300, 390));

        pnlGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        pnlGerEnt.setLayout(null);

        btnExcGerEnt.setFont(fontmed(12));
        btnExcGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnExcGerEnt.setText("Excluir");
        btnExcGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(btnExcGerEnt);
        btnExcGerEnt.setBounds(1120, 250, 90, 50);

        btnBusGerEnt.setFont(fontmed(12));
        btnBusGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnBusGerEnt.setText("Buscar");
        btnBusGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBusGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(btnBusGerEnt);
        btnBusGerEnt.setBounds(70, 90, 90, 50);

        btnIteGerEnt.setFont(fontmed(12));
        btnIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnIteGerEnt.setText("Ítens");
        btnIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIteGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(btnIteGerEnt);
        btnIteGerEnt.setBounds(1120, 110, 90, 50);

        btnCanGerEnt.setFont(fontmed(12));
        btnCanGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnCanGerEnt.setText("Cancelar");
        btnCanGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(btnCanGerEnt);
        btnCanGerEnt.setBounds(170, 90, 90, 50);

        lblDatGerEnt.setFont(fontmed(12));
        lblDatGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDatGerEnt.setText("Data");
        lblDatGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblDatGerEnt);
        lblDatGerEnt.setBounds(860, 70, 40, 20);

        txtDatGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtDatGerEnt.setFont(fontmed(13));
        txtDatGerEnt.setBorder(null);
        txtDatGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatGerEntFocusLost(evt);
            }
        });
        txtDatGerEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatGerEntKeyTyped(evt);
            }
        });
        pnlGerEnt.add(txtDatGerEnt);
        txtDatGerEnt.setBounds(860, 70, 130, 20);

        sepDatGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepDatGerEnt);
        sepDatGerEnt.setBounds(860, 90, 130, 10);

        lblR$GerEnt.setFont(fontmed(13));
        lblR$GerEnt.setText("R$");
        lblR$GerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblR$GerEnt);
        lblR$GerEnt.setBounds(860, 130, 20, 21);

        lblPreGerEnt.setFont(fontmed(12));
        lblPreGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblPreGerEnt.setText("Preço");
        lblPreGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblPreGerEnt);
        lblPreGerEnt.setBounds(860, 130, 40, 20);

        txtPreGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtPreGerEnt.setFont(fontmed(13));
        txtPreGerEnt.setBorder(null);
        txtPreGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPreGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPreGerEntFocusLost(evt);
            }
        });
        pnlGerEnt.add(txtPreGerEnt);
        txtPreGerEnt.setBounds(880, 130, 80, 20);

        sepPreGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepPreGerEnt);
        sepPreGerEnt.setBounds(860, 150, 100, 10);

        lblDetGerEnt.setFont(fontmed(12));
        lblDetGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDetGerEnt.setText("Detalhes");
        lblDetGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblDetGerEnt);
        lblDetGerEnt.setBounds(860, 190, 70, 20);

        txtDetGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtDetGerEnt.setFont(fontmed(13));
        txtDetGerEnt.setBorder(null);
        txtDetGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDetGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDetGerEntFocusLost(evt);
            }
        });
        txtDetGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDetGerEntMouseClicked(evt);
            }
        });
        pnlGerEnt.add(txtDetGerEnt);
        txtDetGerEnt.setBounds(860, 190, 190, 20);

        sepDetGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepDetGerEnt);
        sepDetGerEnt.setBounds(860, 210, 190, 10);

        lblDatBusGerEnt.setFont(fontmed(12));
        lblDatBusGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDatBusGerEnt.setText("Data");
        lblDatBusGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblDatBusGerEnt);
        lblDatBusGerEnt.setBounds(70, 50, 50, 20);

        txtDatBusGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtDatBusGerEnt.setFont(fontmed(13));
        txtDatBusGerEnt.setBorder(null);
        txtDatBusGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatBusGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatBusGerEntFocusLost(evt);
            }
        });
        txtDatBusGerEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatBusGerEntKeyTyped(evt);
            }
        });
        pnlGerEnt.add(txtDatBusGerEnt);
        txtDatBusGerEnt.setBounds(70, 50, 190, 20);

        sepMod3.setForeground(new java.awt.Color(10, 60, 133));
        sepMod3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlGerEnt.add(sepMod3);
        sepMod3.setBounds(750, 80, 20, 210);

        sepBusGerEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepBusGerEst1);
        sepBusGerEst1.setBounds(70, 70, 190, 10);

        cmbSerGerEnt.setFont(fontmed(13));
        cmbSerGerEnt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o serviço" }));
        cmbSerGerEnt.setToolTipText("");
        cmbSerGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlGerEnt.add(cmbSerGerEnt);
        cmbSerGerEnt.setBounds(860, 270, 190, 30);

        lblSerGerEnt.setFont(fontmed(12));
        lblSerGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSerGerEnt.setText("Serviço");
        pnlGerEnt.add(lblSerGerEnt);
        lblSerGerEnt.setBounds(860, 240, 90, 30);

        scrGerEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrGerEnt.setBorder(BorderFactory.createEmptyBorder());

        tblConEst.setEnabled(false);
        tblGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        tblGerEnt.setBorder(null);
        tblGerEnt.setFont(fontmed(10));
        tblGerEnt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGerEnt.setFocusable(false);
        tblGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGerEntMouseClicked(evt);
            }
        });
        scrGerEnt.setViewportView(tblGerEnt);

        pnlGerEnt.add(scrGerEnt);
        scrGerEnt.setBounds(40, 170, 670, 170);

        btnAltGerEnt.setFont(fontmed(12));
        btnAltGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnAltGerEnt.setText("Alterar");
        btnAltGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(btnAltGerEnt);
        btnAltGerEnt.setBounds(1120, 180, 90, 50);

        pnlPri.add(pnlGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlIteGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        pnlIteGerEnt.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrEstIteGerEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrEstIteGerEnt.setBorder(BorderFactory.createEmptyBorder());

        tblEstIteGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        tblEstIteGerEnt.setBorder(null);
        tblEstIteGerEnt.setFont(fontmed(10));
        tblEstIteGerEnt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblEstIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblEstIteGerEnt.setFocusable(false);
        tblEstIteGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEstIteGerEntMouseClicked(evt);
            }
        });
        scrEstIteGerEnt.setViewportView(tblEstIteGerEnt);

        pnlIteGerEnt.add(scrEstIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 780, 120));

        scrSelIteGerEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrSelIteGerEnt.setBorder(BorderFactory.createEmptyBorder());

        tblSelIteGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        tblSelIteGerEnt.setBorder(null);
        tblSelIteGerEnt.setFont(fontmed(10));
        tblSelIteGerEnt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSelIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblSelIteGerEnt.setFocusable(false);
        tblSelIteGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSelIteGerEntMouseClicked(evt);
            }
        });
        scrSelIteGerEnt.setViewportView(tblSelIteGerEnt);

        pnlIteGerEnt.add(scrSelIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 220, 780, 120));

        btnVolIteGerEnt.setFont(fontmed(12));
        btnVolIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnVolIteGerEnt.setText("Voltar");
        btnVolIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolIteGerEntActionPerformed(evt);
            }
        });
        pnlIteGerEnt.add(btnVolIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 90, 50));

        lblSelIteGerEnt.setFont(fontmed(12));
        lblSelIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSelIteGerEnt.setText("Ítens selecionados");
        pnlIteGerEnt.add(lblSelIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, 170, 20));

        lblEstIteGerEnt.setFont(fontmed(12));
        lblEstIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblEstIteGerEnt.setText("Ítens do estoque");
        pnlIteGerEnt.add(lblEstIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 120, 20));

        btnGroup1.add(rbtnAssIteGerEnt);
        rbtnAssIteGerEnt.setFont(fontmed(12));
        rbtnAssIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAssIteGerEnt.setText("Acessório");
        rbtnAssIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlIteGerEnt.add(rbtnAssIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 100, -1));

        btnGroup1.add(rbtnPelIteGerEnt);
        rbtnPelIteGerEnt.setFont(fontmed(12));
        rbtnPelIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPelIteGerEnt.setText("Película");
        rbtnPelIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlIteGerEnt.add(rbtnPelIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 80, -1));

        btnGroup1.add(rbtnCapIteGerEnt);
        rbtnCapIteGerEnt.setFont(fontmed(12));
        rbtnCapIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCapIteGerEnt.setSelected(true);
        rbtnCapIteGerEnt.setText("Capinha");
        rbtnCapIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlIteGerEnt.add(rbtnCapIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 90, -1));

        btnGroup1.add(rbtnChiIteGerEnt);
        rbtnChiIteGerEnt.setFont(fontmed(12));
        rbtnChiIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnChiIteGerEnt.setText("Chip");
        rbtnChiIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlIteGerEnt.add(rbtnChiIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 70, -1));

        lblBusIteGerEnt.setFont(fontmed(12));
        lblBusIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblBusIteGerEnt.setText("Buscar");
        lblBusIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlIteGerEnt.add(lblBusIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 50, 20));

        txtBusIteGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtBusIteGerEnt.setFont(fontmed(13));
        txtBusIteGerEnt.setBorder(null);
        txtBusIteGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusIteGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusIteGerEntFocusLost(evt);
            }
        });
        txtBusIteGerEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusIteGerEntKeyPressed(evt);
            }
        });
        pnlIteGerEnt.add(txtBusIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 200, 20));

        sepBusIteCadEnt1.setForeground(new java.awt.Color(10, 60, 133));
        pnlIteGerEnt.add(sepBusIteCadEnt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 200, 10));

        pnlPri.add(pnlIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlDes.setBackground(new java.awt.Color(246, 246, 246));
        pnlDes.setLayout(null);

        scrConDes.setBackground(new java.awt.Color(250, 250, 250));
        scrConDes.setBorder(BorderFactory.createEmptyBorder());

        tblConDes.setBackground(new java.awt.Color(246, 246, 246));
        tblConDes.setBorder(null);
        tblConDes.setFont(fontmed(10));
        tblConDes.setForeground(new java.awt.Color(229, 192, 191));
        tblConDes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblConDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblConDes.setFocusable(false);
        tblConDes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConDesMouseClicked(evt);
            }
        });
        scrConDes.setViewportView(tblConDes);

        pnlDes.add(scrConDes);
        scrConDes.setBounds(270, 20, 760, 190);

        btnVolDes.setFont(fontmed(12));
        btnVolDes.setForeground(new java.awt.Color(10, 60, 133));
        btnVolDes.setText("Voltar");
        btnVolDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolDesActionPerformed(evt);
            }
        });
        pnlDes.add(btnVolDes);
        btnVolDes.setBounds(270, 240, 90, 50);

        pnlPri.add(pnlDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlGerDes.setBackground(new java.awt.Color(246, 246, 246));
        pnlGerDes.setLayout(null);

        lblDesGerDes.setFont(fontmed(12));
        lblDesGerDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDesGerDes.setText("Descrição");
        lblDesGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblDesGerDes);
        lblDesGerDes.setBounds(870, 40, 90, 20);

        lblDatGerDes.setFont(fontmed(12));
        lblDatGerDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDatGerDes.setText("Data");
        lblDatGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblDatGerDes);
        lblDatGerDes.setBounds(870, 160, 50, 20);

        lblPreGerDes.setFont(fontmed(12));
        lblPreGerDes.setForeground(new java.awt.Color(10, 60, 133));
        lblPreGerDes.setText("Preço");
        lblPreGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblPreGerDes);
        lblPreGerDes.setBounds(870, 100, 50, 20);

        lblR$GerDes.setFont(fontmed(13));
        lblR$GerDes.setText("R$");
        lblR$GerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblR$GerDes);
        lblR$GerDes.setBounds(870, 100, 20, 21);

        txtDatGerDes.setBackground(new java.awt.Color(246, 246, 246));
        txtDatGerDes.setFont(fontmed(13));
        txtDatGerDes.setBorder(null);
        txtDatGerDes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatGerDesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatGerDesFocusLost(evt);
            }
        });
        txtDatGerDes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatGerDesKeyTyped(evt);
            }
        });
        pnlGerDes.add(txtDatGerDes);
        txtDatGerDes.setBounds(870, 160, 130, 20);

        txtDesGerDes.setBackground(new java.awt.Color(246, 246, 246));
        txtDesGerDes.setFont(fontmed(13));
        txtDesGerDes.setBorder(null);
        txtDesGerDes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDesGerDesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDesGerDesFocusLost(evt);
            }
        });
        pnlGerDes.add(txtDesGerDes);
        txtDesGerDes.setBounds(870, 40, 210, 20);

        sepPreGerDes.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerDes.add(sepPreGerDes);
        sepPreGerDes.setBounds(870, 120, 100, 10);

        sepDatGerDes.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerDes.add(sepDatGerDes);
        sepDatGerDes.setBounds(870, 180, 130, 10);

        sepDesGerDes.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerDes.add(sepDesGerDes);
        sepDesGerDes.setBounds(870, 60, 210, 10);

        txtPreGerDes.setBackground(new java.awt.Color(246, 246, 246));
        txtPreGerDes.setFont(fontmed(13));
        txtPreGerDes.setBorder(null);
        txtPreGerDes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPreGerDesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPreGerDesFocusLost(evt);
            }
        });
        pnlGerDes.add(txtPreGerDes);
        txtPreGerDes.setBounds(890, 100, 80, 20);

        btnExcGerDes.setFont(fontmed(12));
        btnExcGerDes.setForeground(new java.awt.Color(10, 60, 133));
        btnExcGerDes.setText("Excluir");
        btnExcGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcGerDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcGerDesActionPerformed(evt);
            }
        });
        pnlGerDes.add(btnExcGerDes);
        btnExcGerDes.setBounds(970, 220, 90, 40);

        btnAltGerDes.setFont(fontmed(12));
        btnAltGerDes.setForeground(new java.awt.Color(10, 60, 133));
        btnAltGerDes.setText("Alterar");
        btnAltGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltGerDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltGerDesActionPerformed(evt);
            }
        });
        pnlGerDes.add(btnAltGerDes);
        btnAltGerDes.setBounds(870, 220, 90, 40);

        btnCanGerDes.setFont(fontmed(12));
        btnCanGerDes.setForeground(new java.awt.Color(10, 60, 133));
        btnCanGerDes.setText("Cancelar");
        btnCanGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanGerDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanGerDesActionPerformed(evt);
            }
        });
        pnlGerDes.add(btnCanGerDes);
        btnCanGerDes.setBounds(1070, 220, 90, 40);

        lblDesTipSer3.setFont(fontmed(12));
        lblDesTipSer3.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer3.setText("Escolha um para alterar ou excluir");
        lblDesTipSer3.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblDesTipSer3);
        lblDesTipSer3.setBounds(110, 50, 260, 20);

        scrGerDes.setBackground(new java.awt.Color(250, 250, 250));
        scrGerDes.setBorder(BorderFactory.createEmptyBorder());
        scrGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tblTipSer.setTableHeader(null);
        tblGerDes.setBackground(new java.awt.Color(246, 246, 246));
        tblGerDes.setBorder(null);
        tblGerDes.setFont(fontmed(12));
        tblGerDes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGerDes.setFocusable(false);
        tblGerDes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGerDesMouseClicked(evt);
            }
        });
        scrGerDes.setViewportView(tblGerDes);

        pnlGerDes.add(scrGerDes);
        scrGerDes.setBounds(110, 80, 610, 160);

        pnlPri.add(pnlGerDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 1300, 360));

        pnlCadDes.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadDes.setLayout(null);

        btnSalDes.setFont(fontmed(12));
        btnSalDes.setForeground(new java.awt.Color(10, 60, 133));
        btnSalDes.setText("Salvar");
        btnSalDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalDesActionPerformed(evt);
            }
        });
        pnlCadDes.add(btnSalDes);
        btnSalDes.setBounds(540, 260, 90, 50);

        lblDatDes.setFont(fontmed(12));
        lblDatDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDatDes.setText("Data");
        lblDatDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblDatDes);
        lblDatDes.setBounds(540, 180, 50, 20);

        txtDatDes.setBackground(new java.awt.Color(246, 246, 246));
        txtDatDes.setFont(fontmed(13));
        txtDatDes.setBorder(null);
        txtDatDes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatDesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatDesFocusLost(evt);
            }
        });
        txtDatDes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatDesKeyTyped(evt);
            }
        });
        pnlCadDes.add(txtDatDes);
        txtDatDes.setBounds(540, 180, 130, 20);

        sepDatCadEnt7.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadDes.add(sepDatCadEnt7);
        sepDatCadEnt7.setBounds(540, 200, 130, 10);

        lblDesDes.setFont(fontmed(12));
        lblDesDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDesDes.setText("Descrição");
        lblDesDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblDesDes);
        lblDesDes.setBounds(540, 60, 90, 20);

        txtDesDes.setBackground(new java.awt.Color(246, 246, 246));
        txtDesDes.setFont(fontmed(13));
        txtDesDes.setBorder(null);
        txtDesDes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDesDesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDesDesFocusLost(evt);
            }
        });
        pnlCadDes.add(txtDesDes);
        txtDesDes.setBounds(540, 60, 190, 20);

        sepDatCadEnt4.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadDes.add(sepDatCadEnt4);
        sepDatCadEnt4.setBounds(540, 80, 190, 10);

        lblR$Des.setFont(fontmed(13));
        lblR$Des.setText("R$");
        lblR$Des.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblR$Des);
        lblR$Des.setBounds(540, 120, 20, 21);

        lblPreDes.setFont(fontmed(12));
        lblPreDes.setForeground(new java.awt.Color(10, 60, 133));
        lblPreDes.setText("Preço");
        lblPreDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblPreDes);
        lblPreDes.setBounds(540, 120, 50, 20);

        txtPreDes.setBackground(new java.awt.Color(246, 246, 246));
        txtPreDes.setFont(fontmed(13));
        txtPreDes.setBorder(null);
        txtPreDes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPreDesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPreDesFocusLost(evt);
            }
        });
        txtPreDes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPreDesKeyTyped(evt);
            }
        });
        pnlCadDes.add(txtPreDes);
        txtPreDes.setBounds(560, 120, 80, 20);

        sepDatCadEnt6.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadDes.add(sepDatCadEnt6);
        sepDatCadEnt6.setBounds(540, 140, 100, 10);

        btnCanDes.setFont(fontmed(12));
        btnCanDes.setForeground(new java.awt.Color(10, 60, 133));
        btnCanDes.setText("Cancelar");
        btnCanDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanDesActionPerformed(evt);
            }
        });
        pnlCadDes.add(btnCanDes);
        btnCanDes.setBounds(640, 260, 90, 50);

        pnlPri.add(pnlCadDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlRel.setBackground(new java.awt.Color(246, 246, 246));
        pnlRel.setLayout(null);

        scrRel.setBackground(new java.awt.Color(250, 250, 250));
        scrRel.setBorder(BorderFactory.createEmptyBorder());

        tblRel.setBackground(new java.awt.Color(246, 246, 246));
        tblRel.setBorder(null);
        tblRel.setFont(fontmed(10));
        tblRel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblRel.setFocusable(false);
        scrRel.setViewportView(tblRel);

        pnlRel.add(scrRel);
        scrRel.setBounds(40, 200, 760, 150);

        btnVolRel.setFont(fontmed(12));
        btnVolRel.setForeground(new java.awt.Color(10, 60, 133));
        btnVolRel.setText("Voltar");
        btnVolRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolRelActionPerformed(evt);
            }
        });
        pnlRel.add(btnVolRel);
        btnVolRel.setBounds(60, 122, 90, 50);

        btnGroup.add(rbtnSerTimRel);
        rbtnSerTimRel.setFont(fontmed(12));
        rbtnSerTimRel.setForeground(new java.awt.Color(10, 60, 133));
        rbtnSerTimRel.setText("Serviço TIM");
        rbtnSerTimRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnSerTimRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSerTimRelActionPerformed(evt);
            }
        });
        pnlRel.add(rbtnSerTimRel);
        rbtnSerTimRel.setBounds(455, 30, 100, 21);

        btnGroup.add(rbtnSerRel);
        rbtnSerRel.setFont(fontmed(12));
        rbtnSerRel.setForeground(new java.awt.Color(10, 60, 133));
        rbtnSerRel.setText("Serviço");
        rbtnSerRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnSerRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSerRelActionPerformed(evt);
            }
        });
        pnlRel.add(rbtnSerRel);
        rbtnSerRel.setBounds(260, 30, 90, 21);

        btnGroup.add(rbtnVenRel);
        rbtnVenRel.setFont(fontmed(12));
        rbtnVenRel.setForeground(new java.awt.Color(10, 60, 133));
        rbtnVenRel.setText("Venda");
        rbtnVenRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnVenRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnVenRelActionPerformed(evt);
            }
        });
        pnlRel.add(rbtnVenRel);
        rbtnVenRel.setBounds(360, 30, 80, 21);

        btnGroup.add(rbtnTodRel);
        rbtnTodRel.setFont(fontmed(12));
        rbtnTodRel.setForeground(new java.awt.Color(10, 60, 133));
        rbtnTodRel.setText("Todos");
        rbtnTodRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnTodRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnTodRelActionPerformed(evt);
            }
        });
        pnlRel.add(rbtnTodRel);
        rbtnTodRel.setBounds(170, 30, 70, 21);

        lblDatIniRel.setFont(fontmed(12));
        lblDatIniRel.setForeground(new java.awt.Color(10, 60, 133));
        lblDatIniRel.setText("Data inicial");
        lblDatIniRel.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        lblDatIniRel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lblDatIniRelFocusGained(evt);
            }
        });
        pnlRel.add(lblDatIniRel);
        lblDatIniRel.setBounds(290, 150, 90, 20);

        txtDatIniRel.setBackground(new java.awt.Color(246, 246, 246));
        txtDatIniRel.setFont(fontmed(13));
        txtDatIniRel.setBorder(null);
        txtDatIniRel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatIniRelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatIniRelFocusLost(evt);
            }
        });
        txtDatIniRel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDatIniRelKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDatIniRelKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatIniRelKeyTyped(evt);
            }
        });
        pnlRel.add(txtDatIniRel);
        txtDatIniRel.setBounds(290, 150, 100, 20);

        sepDatCadEnt1.setForeground(new java.awt.Color(10, 60, 133));
        pnlRel.add(sepDatCadEnt1);
        sepDatCadEnt1.setBounds(290, 170, 100, 10);

        btnTodRel.setFont(fontbold(12));
        btnTodRel.setForeground(new java.awt.Color(10, 60, 133));
        btnTodRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnTodRel.setText("Todos");
        btnTodRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTodRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTodRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTodRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnTodRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnTodRel);
        btnTodRel.setBounds(270, 90, 40, 20);

        btnAnoRel.setFont(fontmed(12));
        btnAnoRel.setForeground(new java.awt.Color(10, 60, 133));
        btnAnoRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnAnoRel.setText("Ano");
        btnAnoRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnoRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAnoRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAnoRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnAnoRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnAnoRel);
        btnAnoRel.setBounds(530, 90, 30, 20);

        lblDatFinRel.setFont(fontmed(12));
        lblDatFinRel.setForeground(new java.awt.Color(10, 60, 133));
        lblDatFinRel.setText("Data final");
        lblDatFinRel.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlRel.add(lblDatFinRel);
        lblDatFinRel.setBounds(430, 150, 80, 20);

        txtDatFinRel.setBackground(new java.awt.Color(246, 246, 246));
        txtDatFinRel.setFont(fontmed(13));
        txtDatFinRel.setBorder(null);
        txtDatFinRel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatFinRelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatFinRelFocusLost(evt);
            }
        });
        txtDatFinRel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDatFinRelKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatFinRelKeyTyped(evt);
            }
        });
        pnlRel.add(txtDatFinRel);
        txtDatFinRel.setBounds(430, 150, 100, 20);

        sepDatCadEnt2.setForeground(new java.awt.Color(10, 60, 133));
        sepDatCadEnt2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlRel.add(sepDatCadEnt2);
        sepDatCadEnt2.setBounds(840, 70, 10, 240);

        btnDiaRel.setFont(fontmed(12));
        btnDiaRel.setForeground(new java.awt.Color(10, 60, 133));
        btnDiaRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDiaRel.setText("Dia");
        btnDiaRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDiaRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDiaRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDiaRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnDiaRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnDiaRel);
        btnDiaRel.setBounds(340, 90, 30, 20);

        btnMesRel.setFont(fontmed(12));
        btnMesRel.setForeground(new java.awt.Color(10, 60, 133));
        btnMesRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnMesRel.setText("Mês");
        btnMesRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMesRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMesRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMesRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMesRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnMesRel);
        btnMesRel.setBounds(470, 90, 30, 20);

        btnSemRel.setFont(fontmed(12));
        btnSemRel.setForeground(new java.awt.Color(10, 60, 133));
        btnSemRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSemRel.setText("Semana");
        btnSemRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSemRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSemRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSemRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnSemRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnSemRel);
        btnSemRel.setBounds(390, 90, 60, 20);

        btnGroup.add(rbtnAssRel);
        rbtnAssRel.setFont(fontmed(12));
        rbtnAssRel.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAssRel.setText("Assistência");
        rbtnAssRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAssRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAssRelActionPerformed(evt);
            }
        });
        pnlRel.add(rbtnAssRel);
        rbtnAssRel.setBounds(580, 30, 100, 21);

        sepDatCadEnt3.setForeground(new java.awt.Color(10, 60, 133));
        pnlRel.add(sepDatCadEnt3);
        sepDatCadEnt3.setBounds(430, 170, 100, 10);

        lblTotEntRel.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        lblTotEntRel.setForeground(new java.awt.Color(10, 60, 133));
        lblTotEntRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotEntRel.setText("0");
        pnlRel.add(lblTotEntRel);
        lblTotEntRel.setBounds(990, 100, 160, 30);

        jLabel2.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Total de entradas");
        pnlRel.add(jLabel2);
        jLabel2.setBounds(990, 80, 160, 20);

        lblValTotRel.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        lblValTotRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValTotRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValTotRel.setText("R$0,00");
        pnlRel.add(lblValTotRel);
        lblValTotRel.setBounds(990, 180, 160, 30);

        jLabel4.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Valor total");
        pnlRel.add(jLabel4);
        jLabel4.setBounds(990, 160, 160, 20);

        lblValMedRel.setFont(new java.awt.Font("Poppins Medium", 0, 18)); // NOI18N
        lblValMedRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValMedRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValMedRel.setText("R$0,00");
        pnlRel.add(lblValMedRel);
        lblValMedRel.setBounds(990, 260, 160, 30);

        jLabel6.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Valor médio");
        pnlRel.add(jLabel6);
        jLabel6.setBounds(990, 240, 160, 20);

        pnlPri.add(pnlRel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadEnt.setLayout(null);

        btnIteCadEnt.setFont(fontmed(12));
        btnIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnIteCadEnt.setText("Ítens");
        btnIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIteCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(btnIteCadEnt);
        btnIteCadEnt.setBounds(720, 120, 90, 50);

        btnSalCadEnt.setFont(fontmed(12));
        btnSalCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnSalCadEnt.setText("Salvar");
        btnSalCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(btnSalCadEnt);
        btnSalCadEnt.setBounds(720, 180, 90, 50);

        btnCanCadEnt.setFont(fontmed(12));
        btnCanCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnCanCadEnt.setText("Cancelar");
        btnCanCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(btnCanCadEnt);
        btnCanCadEnt.setBounds(720, 240, 90, 50);

        btnGroup.add(rbtnSerCadEnt);
        rbtnSerCadEnt.setFont(fontmed(12));
        rbtnSerCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnSerCadEnt.setText("Serviço");
        rbtnSerCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnSerCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSerCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnSerCadEnt);
        rbtnSerCadEnt.setBounds(510, 20, 90, 21);

        btnGroup.add(rbtnVenCadEnt);
        rbtnVenCadEnt.setFont(fontmed(12));
        rbtnVenCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnVenCadEnt.setText("Venda");
        rbtnVenCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnVenCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnVenCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnVenCadEnt);
        rbtnVenCadEnt.setBounds(600, 20, 80, 21);

        btnGroup.add(rbtnAssCadEnt);
        rbtnAssCadEnt.setFont(fontmed(12));
        rbtnAssCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAssCadEnt.setText("Assistência");
        rbtnAssCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAssCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAssCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnAssCadEnt);
        rbtnAssCadEnt.setBounds(682, 20, 100, 21);

        lblDatCadEnt.setFont(fontmed(12));
        lblDatCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDatCadEnt.setText("Data");
        lblDatCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblDatCadEnt);
        lblDatCadEnt.setBounds(480, 90, 40, 20);

        txtDatCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtDatCadEnt.setFont(fontmed(13));
        txtDatCadEnt.setBorder(null);
        txtDatCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatCadEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatCadEntFocusLost(evt);
            }
        });
        txtDatCadEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatCadEntKeyTyped(evt);
            }
        });
        pnlCadEnt.add(txtDatCadEnt);
        txtDatCadEnt.setBounds(480, 90, 100, 20);

        sepDatCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepDatCadEnt);
        sepDatCadEnt.setBounds(480, 110, 100, 10);

        lblR$CadEnt.setFont(fontmed(13));
        lblR$CadEnt.setText("R$");
        lblR$CadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblR$CadEnt);
        lblR$CadEnt.setBounds(480, 140, 20, 21);

        lblPreCadEnt.setFont(fontmed(12));
        lblPreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblPreCadEnt.setText("Preço");
        lblPreCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblPreCadEnt);
        lblPreCadEnt.setBounds(480, 140, 40, 20);

        txtPreCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtPreCadEnt.setFont(fontmed(13));
        txtPreCadEnt.setBorder(null);
        txtPreCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPreCadEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPreCadEntFocusLost(evt);
            }
        });
        txtPreCadEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPreCadEntKeyTyped(evt);
            }
        });
        pnlCadEnt.add(txtPreCadEnt);
        txtPreCadEnt.setBounds(500, 140, 80, 20);

        sepPreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepPreCadEnt);
        sepPreCadEnt.setBounds(480, 160, 100, 10);

        cmbSerCadEnt.setFont(fontmed(13));
        cmbSerCadEnt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o serviço" }));
        cmbSerCadEnt.setToolTipText("");
        cmbSerCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbSerCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSerCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(cmbSerCadEnt);
        cmbSerCadEnt.setBounds(480, 260, 190, 30);

        lblDetCadEnt.setFont(fontmed(12));
        lblDetCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDetCadEnt.setText("Detalhes");
        lblDetCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblDetCadEnt);
        lblDetCadEnt.setBounds(480, 190, 70, 20);

        txtDetCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtDetCadEnt.setFont(fontmed(13));
        txtDetCadEnt.setBorder(null);
        txtDetCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDetCadEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDetCadEntFocusLost(evt);
            }
        });
        pnlCadEnt.add(txtDetCadEnt);
        txtDetCadEnt.setBounds(480, 190, 190, 20);

        sepDetCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepDetCadEnt);
        sepDetCadEnt.setBounds(480, 210, 190, 10);

        lblSerCadEnt.setFont(fontmed(12));
        lblSerCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSerCadEnt.setText("Serviço");
        pnlCadEnt.add(lblSerCadEnt);
        lblSerCadEnt.setBounds(480, 230, 90, 30);
        pnlCadEnt.add(txtCodCadEnt);
        txtCodCadEnt.setBounds(190, 40, 64, 22);

        pnlPri.add(pnlCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlIteCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        pnlIteCadEnt.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnVolIteCadEnt.setFont(fontmed(12));
        btnVolIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnVolIteCadEnt.setText("Voltar");
        btnVolIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolIteCadEntActionPerformed(evt);
            }
        });
        pnlIteCadEnt.add(btnVolIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 90, 50));

        scrEstIteCadEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrEstIteCadEnt.setBorder(BorderFactory.createEmptyBorder());

        tblEstIteCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        tblEstIteCadEnt.setBorder(null);
        tblEstIteCadEnt.setFont(fontmed(10));
        tblEstIteCadEnt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblEstIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblEstIteCadEnt.setFocusable(false);
        tblEstIteCadEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEstIteCadEntMouseClicked(evt);
            }
        });
        scrEstIteCadEnt.setViewportView(tblEstIteCadEnt);

        pnlIteCadEnt.add(scrEstIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 780, 120));

        scrSelIteCadEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrSelIteCadEnt.setBorder(BorderFactory.createEmptyBorder());

        tblSelIteCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        tblSelIteCadEnt.setBorder(null);
        tblSelIteCadEnt.setFont(fontmed(10));
        tblSelIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblSelIteCadEnt.setFocusable(false);
        tblSelIteCadEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSelIteCadEntMouseClicked(evt);
            }
        });
        scrSelIteCadEnt.setViewportView(tblSelIteCadEnt);
        tabelaitensselecionados();

        pnlIteCadEnt.add(scrSelIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 220, 780, 120));

        lblSelIteCadEnt.setFont(fontmed(12));
        lblSelIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSelIteCadEnt.setText("Ítens selecionados");
        pnlIteCadEnt.add(lblSelIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, 170, 20));

        lblEstIteCadEnt.setFont(fontmed(12));
        lblEstIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblEstIteCadEnt.setText("Ítens do estoque");
        pnlIteCadEnt.add(lblEstIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 120, 20));

        btnGroup1.add(rbtnAssIteCadEnt);
        rbtnAssIteCadEnt.setFont(fontmed(12));
        rbtnAssIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAssIteCadEnt.setText("Acessório");
        rbtnAssIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAssIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAssIteCadEntActionPerformed(evt);
            }
        });
        pnlIteCadEnt.add(rbtnAssIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 100, -1));

        btnGroup1.add(rbtnPelIteCadEnt);
        rbtnPelIteCadEnt.setFont(fontmed(12));
        rbtnPelIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPelIteCadEnt.setText("Película");
        rbtnPelIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPelIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPelIteCadEntActionPerformed(evt);
            }
        });
        pnlIteCadEnt.add(rbtnPelIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 80, -1));

        btnGroup1.add(rbtnCapIteCadEnt);
        rbtnCapIteCadEnt.setFont(fontmed(12));
        rbtnCapIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCapIteCadEnt.setText("Capinha");
        rbtnCapIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCapIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCapIteCadEntActionPerformed(evt);
            }
        });
        pnlIteCadEnt.add(rbtnCapIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 90, -1));

        btnGroup1.add(rbtnChiIteCadEnt);
        rbtnChiIteCadEnt.setFont(fontmed(12));
        rbtnChiIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnChiIteCadEnt.setText("Chip");
        rbtnChiIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnChiIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnChiIteCadEntActionPerformed(evt);
            }
        });
        pnlIteCadEnt.add(rbtnChiIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 70, -1));

        lblBusIteCadEnt.setFont(fontmed(12));
        lblBusIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblBusIteCadEnt.setText("Buscar");
        lblBusIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlIteCadEnt.add(lblBusIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 50, 20));

        txtBusIteCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtBusIteCadEnt.setFont(fontmed(13));
        txtBusIteCadEnt.setBorder(null);
        txtBusIteCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusIteCadEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusIteCadEntFocusLost(evt);
            }
        });
        txtBusIteCadEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusIteCadEntKeyPressed(evt);
            }
        });
        pnlIteCadEnt.add(txtBusIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 200, 20));

        sepBusIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlIteCadEnt.add(sepBusIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 200, 10));

        pnlPri.add(pnlIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlCadTipSer.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadTipSer.setLayout(null);

        btnSalTipSer.setFont(fontmed(12));
        btnSalTipSer.setForeground(new java.awt.Color(10, 60, 133));
        btnSalTipSer.setText("Salvar");
        btnSalTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(btnSalTipSer);
        btnSalTipSer.setBounds(550, 160, 90, 40);

        btnCanTipSer.setFont(fontmed(12));
        btnCanTipSer.setForeground(new java.awt.Color(10, 60, 133));
        btnCanTipSer.setText("Cancelar");
        btnCanTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(btnCanTipSer);
        btnCanTipSer.setBounds(650, 160, 90, 40);

        btnGroup.add(rbtnOutTipSer);
        rbtnOutTipSer.setFont(fontmed(12));
        rbtnOutTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnOutTipSer.setText("Outros");
        rbtnOutTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnOutTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnOutTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(rbtnOutTipSer);
        rbtnOutTipSer.setBounds(780, 20, 80, 21);

        btnGroup.add(rbtnSerTimTipSer);
        rbtnSerTimTipSer.setFont(fontmed(12));
        rbtnSerTimTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnSerTimTipSer.setText("Serviços TIM");
        rbtnSerTimTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnSerTimTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSerTimTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(rbtnSerTimTipSer);
        rbtnSerTimTipSer.setBounds(440, 20, 110, 21);

        btnGroup.add(rbtnAssTipSer);
        rbtnAssTipSer.setFont(fontmed(12));
        rbtnAssTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAssTipSer.setText("Serviços Assistência Técnica");
        rbtnAssTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAssTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAssTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(rbtnAssTipSer);
        rbtnAssTipSer.setBounds(562, 20, 210, 21);

        lblDesTipSer.setFont(fontmed(12));
        lblDesTipSer.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer.setText("Descrição");
        lblDesTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadTipSer.add(lblDesTipSer);
        lblDesTipSer.setBounds(510, 110, 70, 20);

        txtDesTipSer.setBackground(new java.awt.Color(246, 246, 246));
        txtDesTipSer.setFont(fontmed(13));
        txtDesTipSer.setBorder(null);
        txtDesTipSer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDesTipSerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDesTipSerFocusLost(evt);
            }
        });
        txtDesTipSer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDesTipSerKeyTyped(evt);
            }
        });
        pnlCadTipSer.add(txtDesTipSer);
        txtDesTipSer.setBounds(510, 110, 280, 20);

        sepDesTipSer.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadTipSer.add(sepDesTipSer);
        sepDesTipSer.setBounds(510, 130, 280, 10);

        pnlPri.add(pnlCadTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 1300, 360));

        pnlCadEst.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadEst.setLayout(null);

        btnSalCadEst.setFont(fontmed(12));
        btnSalCadEst.setForeground(new java.awt.Color(10, 60, 133));
        btnSalCadEst.setText("Salvar");
        btnSalCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(btnSalCadEst);
        btnSalCadEst.setBounds(410, 280, 90, 50);

        btnCanCadEst.setFont(fontmed(12));
        btnCanCadEst.setForeground(new java.awt.Color(10, 60, 133));
        btnCanCadEst.setText("Cancelar");
        btnCanCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(btnCanCadEst);
        btnCanCadEst.setBounds(510, 280, 90, 50);

        btnGroup.add(rbtnCapCadEst);
        rbtnCapCadEst.setFont(fontmed(12));
        rbtnCapCadEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCapCadEst.setText("Capinha");
        rbtnCapCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCapCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCapCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(rbtnCapCadEst);
        rbtnCapCadEst.setBounds(460, 20, 90, 21);

        btnGroup.add(rbtnPelCadEst);
        rbtnPelCadEst.setFont(fontmed(12));
        rbtnPelCadEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPelCadEst.setText("Película");
        rbtnPelCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPelCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPelCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(rbtnPelCadEst);
        rbtnPelCadEst.setBounds(570, 20, 80, 21);

        btnGroup.add(rbtnChiCadEst);
        rbtnChiCadEst.setFont(fontmed(12));
        rbtnChiCadEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnChiCadEst.setText("Chip");
        rbtnChiCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnChiCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnChiCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(rbtnChiCadEst);
        rbtnChiCadEst.setBounds(670, 20, 60, 21);

        btnGroup.add(rbtnAceCadEst);
        rbtnAceCadEst.setFont(fontmed(12));
        rbtnAceCadEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAceCadEst.setText("Acessório");
        rbtnAceCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAceCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAceCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(rbtnAceCadEst);
        rbtnAceCadEst.setBounds(750, 20, 100, 21);

        lblModCadEst.setFont(fontmed(12));
        lblModCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblModCadEst.setText("Modelo ");
        lblModCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblModCadEst);
        lblModCadEst.setBounds(410, 130, 50, 20);

        txtModCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtModCadEst.setFont(fontmed(13));
        txtModCadEst.setBorder(null);
        txtModCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtModCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtModCadEstFocusLost(evt);
            }
        });
        pnlCadEst.add(txtModCadEst);
        txtModCadEst.setBounds(410, 130, 190, 20);

        sepModCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepModCadEst);
        sepModCadEst.setBounds(410, 150, 190, 10);

        lblMarCadEst.setFont(fontmed(12));
        lblMarCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMarCadEst.setText("Marca");
        lblMarCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblMarCadEst);
        lblMarCadEst.setBounds(410, 80, 40, 20);

        txtMarCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtMarCadEst.setFont(fontmed(13));
        txtMarCadEst.setBorder(null);
        txtMarCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMarCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMarCadEstFocusLost(evt);
            }
        });
        pnlCadEst.add(txtMarCadEst);
        txtMarCadEst.setBounds(410, 80, 190, 20);

        sepMarCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepMarCadEst);
        sepMarCadEst.setBounds(410, 100, 190, 10);

        lblCorCadEst.setFont(fontmed(12));
        lblCorCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblCorCadEst.setText("Cor");
        lblCorCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblCorCadEst);
        lblCorCadEst.setBounds(700, 80, 30, 20);

        txtCorCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtCorCadEst.setFont(fontmed(13));
        txtCorCadEst.setBorder(null);
        txtCorCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCorCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorCadEstFocusLost(evt);
            }
        });
        pnlCadEst.add(txtCorCadEst);
        txtCorCadEst.setBounds(700, 80, 190, 20);

        sepCorCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepCorCadEst);
        sepCorCadEst.setBounds(700, 100, 190, 10);

        lblMatCadEst.setFont(fontmed(12));
        lblMatCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMatCadEst.setText("Material");
        lblMatCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblMatCadEst);
        lblMatCadEst.setBounds(700, 130, 50, 20);

        txtMatCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtMatCadEst.setFont(fontmed(13));
        txtMatCadEst.setBorder(null);
        txtMatCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMatCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMatCadEstFocusLost(evt);
            }
        });
        pnlCadEst.add(txtMatCadEst);
        txtMatCadEst.setBounds(700, 130, 190, 20);

        sepMatCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepMatCadEst);
        sepMatCadEst.setBounds(700, 150, 190, 10);

        lblQuaCadEst.setFont(fontmed(12));
        lblQuaCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblQuaCadEst.setText("Quantidade");
        lblQuaCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblQuaCadEst);
        lblQuaCadEst.setBounds(410, 180, 80, 20);

        txtQuaCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtQuaCadEst.setFont(fontmed(13));
        txtQuaCadEst.setBorder(null);
        txtQuaCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQuaCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuaCadEstFocusLost(evt);
            }
        });
        txtQuaCadEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQuaCadEstKeyTyped(evt);
            }
        });
        pnlCadEst.add(txtQuaCadEst);
        txtQuaCadEst.setBounds(410, 180, 100, 20);

        sepQuaCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepQuaCadEst);
        sepQuaCadEst.setBounds(410, 200, 100, 10);

        lblR$CadEst.setFont(fontmed(13));
        lblR$CadEst.setText("R$");
        lblR$CadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblR$CadEst);
        lblR$CadEst.setBounds(410, 230, 20, 21);

        lblPreCadEst.setFont(fontmed(12));
        lblPreCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblPreCadEst.setText("Preço");
        lblPreCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblPreCadEst);
        lblPreCadEst.setBounds(410, 230, 40, 20);

        txtPreCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtPreCadEst.setFont(fontmed(13));
        txtPreCadEst.setBorder(null);
        txtPreCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPreCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPreCadEstFocusLost(evt);
            }
        });
        txtPreCadEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPreCadEstKeyTyped(evt);
            }
        });
        pnlCadEst.add(txtPreCadEst);
        txtPreCadEst.setBounds(430, 230, 80, 20);

        sepPreCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepPreCadEst);
        sepPreCadEst.setBounds(410, 250, 100, 10);

        lblChiCadEst.setFont(fontmed(12));
        lblChiCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblChiCadEst.setText("Chip");
        pnlCadEst.add(lblChiCadEst);
        lblChiCadEst.setBounds(700, 270, 30, 30);

        cmbChiCadEst.setFont(fontmed(13));
        cmbChiCadEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Triplo 4G HLR 230", "eSIM", "Naked" }));
        cmbChiCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlCadEst.add(cmbChiCadEst);
        cmbChiCadEst.setBounds(700, 300, 190, 30);

        lblLocCadEst.setFont(fontmed(12));
        lblLocCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblLocCadEst.setText("Local");
        lblLocCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblLocCadEst);
        lblLocCadEst.setBounds(700, 180, 40, 20);

        txtLocCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtLocCadEst.setFont(fontmed(13));
        txtLocCadEst.setBorder(null);
        txtLocCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtLocCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLocCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLocCadEstFocusLost(evt);
            }
        });
        pnlCadEst.add(txtLocCadEst);
        txtLocCadEst.setBounds(700, 180, 190, 20);

        sepLocCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepLocCadEst);
        sepLocCadEst.setBounds(700, 200, 190, 10);

        lblDetCadEst.setFont(fontmed(12));
        lblDetCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblDetCadEst.setText("Detalhes");
        lblDetCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblDetCadEst);
        lblDetCadEst.setBounds(700, 230, 70, 20);

        txtDetCadEst.setBackground(new java.awt.Color(246, 246, 246));
        txtDetCadEst.setFont(fontmed(13));
        txtDetCadEst.setBorder(null);
        txtDetCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDetCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDetCadEstFocusLost(evt);
            }
        });
        pnlCadEst.add(txtDetCadEst);
        txtDetCadEst.setBounds(700, 230, 190, 20);

        sepDetCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepDetCadEst);
        sepDetCadEst.setBounds(700, 250, 190, 10);

        txtTipCadEst.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadEst.add(txtTipCadEst);
        txtTipCadEst.setBounds(390, 20, 40, 22);

        pnlPri.add(pnlCadEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 1300, 390));

        pnlConEst.setBackground(new java.awt.Color(246, 246, 246));
        pnlConEst.setLayout(null);

        btnCanConEst.setFont(fontmed(12));
        btnCanConEst.setForeground(new java.awt.Color(10, 60, 133));
        btnCanConEst.setText("Cancelar");
        btnCanConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanConEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanConEstActionPerformed(evt);
            }
        });
        pnlConEst.add(btnCanConEst);
        btnCanConEst.setBounds(870, 90, 90, 40);

        btnBusConEst.setFont(fontmed(12));
        btnBusConEst.setForeground(new java.awt.Color(10, 60, 133));
        btnBusConEst.setText("Buscar");
        btnBusConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBusConEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusConEstActionPerformed(evt);
            }
        });
        pnlConEst.add(btnBusConEst);
        btnBusConEst.setBounds(760, 90, 90, 40);

        btnGroup.add(rbtnCapConEst);
        rbtnCapConEst.setFont(fontmed(12));
        rbtnCapConEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCapConEst.setText("Capinha");
        rbtnCapConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCapConEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCapConEstActionPerformed(evt);
            }
        });
        pnlConEst.add(rbtnCapConEst);
        rbtnCapConEst.setBounds(450, 30, 90, 21);

        btnGroup.add(rbtnPelConEst);
        rbtnPelConEst.setFont(fontmed(12));
        rbtnPelConEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPelConEst.setText("Película");
        rbtnPelConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPelConEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPelConEstActionPerformed(evt);
            }
        });
        pnlConEst.add(rbtnPelConEst);
        rbtnPelConEst.setBounds(560, 30, 80, 21);

        btnGroup.add(rbtnChiConEst);
        rbtnChiConEst.setFont(fontmed(12));
        rbtnChiConEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnChiConEst.setText("Chip");
        rbtnChiConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnChiConEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnChiConEstActionPerformed(evt);
            }
        });
        pnlConEst.add(rbtnChiConEst);
        rbtnChiConEst.setBounds(660, 30, 60, 21);

        btnGroup.add(rbtnAceConEst);
        rbtnAceConEst.setFont(fontmed(12));
        rbtnAceConEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAceConEst.setText("Acessório");
        rbtnAceConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAceConEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAceConEstActionPerformed(evt);
            }
        });
        pnlConEst.add(rbtnAceConEst);
        rbtnAceConEst.setBounds(740, 30, 100, 21);

        lblBusConEst.setFont(fontmed(12));
        lblBusConEst.setForeground(new java.awt.Color(10, 60, 133));
        lblBusConEst.setText("Buscar");
        lblBusConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlConEst.add(lblBusConEst);
        lblBusConEst.setBounds(450, 110, 50, 20);

        txtBusConEst.setBackground(new java.awt.Color(246, 246, 246));
        txtBusConEst.setFont(fontmed(13));
        txtBusConEst.setBorder(null);
        txtBusConEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusConEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusConEstFocusLost(evt);
            }
        });
        txtBusConEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusConEstKeyTyped(evt);
            }
        });
        pnlConEst.add(txtBusConEst);
        txtBusConEst.setBounds(450, 110, 290, 20);

        sepBusConEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlConEst.add(sepBusConEst);
        sepBusConEst.setBounds(450, 130, 290, 10);

        scrConEst.setBackground(new java.awt.Color(250, 250, 250));
        scrConEst.setBorder(BorderFactory.createEmptyBorder());

        tblConEst.setBackground(new java.awt.Color(246, 246, 246));
        tblConEst.setBorder(null);
        tblConEst.setFont(fontmed(12));
        tblConEst.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblConEst.setFocusable(false);
        scrConEst.setViewportView(tblConEst);

        pnlConEst.add(scrConEst);
        scrConEst.setBounds(160, 170, 980, 150);
        pnlConEst.add(txtTipConEst);
        txtTipConEst.setBounds(180, 50, 64, 22);

        pnlPri.add(pnlConEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlGerEst.setBackground(new java.awt.Color(246, 246, 246));
        pnlGerEst.setLayout(null);

        btnExcGerEst.setFont(fontmed(12));
        btnExcGerEst.setForeground(new java.awt.Color(10, 60, 133));
        btnExcGerEst.setText("Excluir");
        btnExcGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(btnExcGerEst);
        btnExcGerEst.setBounds(830, 260, 90, 50);

        btnBusGerEst.setFont(fontmed(12));
        btnBusGerEst.setForeground(new java.awt.Color(10, 60, 133));
        btnBusGerEst.setText("Buscar");
        btnBusGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBusGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(btnBusGerEst);
        btnBusGerEst.setBounds(220, 140, 90, 50);

        btnAltGerEst.setFont(fontmed(12));
        btnAltGerEst.setForeground(new java.awt.Color(10, 60, 133));
        btnAltGerEst.setText("Alterar");
        btnAltGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(btnAltGerEst);
        btnAltGerEst.setBounds(730, 260, 90, 50);

        btnCanGerEst.setFont(fontmed(12));
        btnCanGerEst.setForeground(new java.awt.Color(10, 60, 133));
        btnCanGerEst.setText("Cancelar");
        btnCanGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(btnCanGerEst);
        btnCanGerEst.setBounds(320, 140, 90, 50);

        btnGroup.add(rbtnCapGerEst);
        rbtnCapGerEst.setFont(fontmed(12));
        rbtnCapGerEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCapGerEst.setText("Capinha");
        rbtnCapGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCapGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCapGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(rbtnCapGerEst);
        rbtnCapGerEst.setBounds(140, 30, 90, 21);

        btnGroup.add(rbtnPelGerEst);
        rbtnPelGerEst.setFont(fontmed(12));
        rbtnPelGerEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPelGerEst.setText("Película");
        rbtnPelGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPelGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPelGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(rbtnPelGerEst);
        rbtnPelGerEst.setBounds(240, 30, 80, 21);

        btnGroup.add(rbtnChiGerEst);
        rbtnChiGerEst.setFont(fontmed(12));
        rbtnChiGerEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnChiGerEst.setText("Chip");
        rbtnChiGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnChiGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnChiGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(rbtnChiGerEst);
        rbtnChiGerEst.setBounds(340, 30, 60, 21);

        btnGroup.add(rbtnAceGerEst);
        rbtnAceGerEst.setFont(fontmed(12));
        rbtnAceGerEst.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAceGerEst.setText("Acessório");
        rbtnAceGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAceGerEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAceGerEstActionPerformed(evt);
            }
        });
        pnlGerEst.add(rbtnAceGerEst);
        rbtnAceGerEst.setBounds(420, 30, 100, 21);

        lblModGerEst.setFont(fontmed(12));
        lblModGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblModGerEst.setText("Modelo ");
        lblModGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblModGerEst);
        lblModGerEst.setBounds(730, 110, 50, 20);

        txtModGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtModGerEst.setFont(fontmed(13));
        txtModGerEst.setBorder(null);
        txtModGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtModGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtModGerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtModGerEst);
        txtModGerEst.setBounds(730, 110, 190, 20);

        sepModGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepModGerEst);
        sepModGerEst.setBounds(730, 130, 190, 10);

        lblMarGerEst.setFont(fontmed(12));
        lblMarGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMarGerEst.setText("Marca");
        lblMarGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblMarGerEst);
        lblMarGerEst.setBounds(730, 60, 40, 20);

        txtMarGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtMarGerEst.setFont(fontmed(13));
        txtMarGerEst.setBorder(null);
        txtMarGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMarGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMarGerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtMarGerEst);
        txtMarGerEst.setBounds(730, 60, 190, 20);

        sepMarGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepMarGerEst);
        sepMarGerEst.setBounds(730, 80, 190, 10);

        lblCorGerEst.setFont(fontmed(12));
        lblCorGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblCorGerEst.setText("Cor");
        lblCorGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblCorGerEst);
        lblCorGerEst.setBounds(1030, 60, 30, 20);

        txtCorGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtCorGerEst.setFont(fontmed(13));
        txtCorGerEst.setBorder(null);
        txtCorGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCorGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCorGerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtCorGerEst);
        txtCorGerEst.setBounds(1030, 60, 190, 20);

        sepCorGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepCorGerEst);
        sepCorGerEst.setBounds(1030, 80, 190, 10);

        lblMatGerEst.setFont(fontmed(12));
        lblMatGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMatGerEst.setText("Material");
        lblMatGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblMatGerEst);
        lblMatGerEst.setBounds(1030, 110, 50, 20);

        txtMatGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtMatGerEst.setFont(fontmed(13));
        txtMatGerEst.setBorder(null);
        txtMatGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMatGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMatGerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtMatGerEst);
        txtMatGerEst.setBounds(1030, 110, 190, 20);

        sepMatGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepMatGerEst);
        sepMatGerEst.setBounds(1030, 130, 190, 10);

        lblQuaGerEst.setFont(fontmed(12));
        lblQuaGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblQuaGerEst.setText("Quantidade");
        lblQuaGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblQuaGerEst);
        lblQuaGerEst.setBounds(730, 160, 80, 20);

        txtQuaGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtQuaGerEst.setFont(fontmed(13));
        txtQuaGerEst.setBorder(null);
        txtQuaGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQuaGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuaGerEstFocusLost(evt);
            }
        });
        txtQuaGerEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQuaGerEstKeyTyped(evt);
            }
        });
        pnlGerEst.add(txtQuaGerEst);
        txtQuaGerEst.setBounds(730, 160, 100, 20);

        sepQuaGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepQuaGerEst);
        sepQuaGerEst.setBounds(730, 180, 100, 10);

        lblR$GerEst.setFont(fontmed(13));
        lblR$GerEst.setText("R$");
        lblR$GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblR$GerEst);
        lblR$GerEst.setBounds(730, 210, 20, 21);

        lblPreGerEst.setFont(fontmed(12));
        lblPreGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblPreGerEst.setText("Preço");
        lblPreGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblPreGerEst);
        lblPreGerEst.setBounds(730, 210, 40, 20);

        txtPreGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtPreGerEst.setFont(fontmed(13));
        txtPreGerEst.setBorder(null);
        txtPreGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPreGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPreGerEstFocusLost(evt);
            }
        });
        txtPreGerEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPreGerEstKeyTyped(evt);
            }
        });
        pnlGerEst.add(txtPreGerEst);
        txtPreGerEst.setBounds(750, 210, 83, 20);

        sepPreGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepPreGerEst);
        sepPreGerEst.setBounds(730, 230, 100, 10);

        lblChiGerEst.setFont(fontmed(12));
        lblChiGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblChiGerEst.setText("Chip");
        pnlGerEst.add(lblChiGerEst);
        lblChiGerEst.setBounds(1030, 250, 30, 30);

        cmbChiGerEst.setFont(fontmed(13));
        cmbChiGerEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Triplo 4G HLR 230", "eSIM", "Naked" }));
        cmbChiGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlGerEst.add(cmbChiGerEst);
        cmbChiGerEst.setBounds(1030, 280, 190, 30);

        lblLocGerEst.setFont(fontmed(12));
        lblLocGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblLocGerEst.setText("Local");
        lblLocGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblLocGerEst);
        lblLocGerEst.setBounds(1030, 160, 40, 20);

        txtLocGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtLocGerEst.setFont(fontmed(13));
        txtLocGerEst.setBorder(null);
        txtLocGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtLocGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLocGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLocGerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtLocGerEst);
        txtLocGerEst.setBounds(1030, 160, 190, 20);

        sepLocGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepLocGerEst);
        sepLocGerEst.setBounds(1030, 180, 190, 10);

        lblDetGerEst.setFont(fontmed(12));
        lblDetGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblDetGerEst.setText("Detalhes");
        lblDetGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblDetGerEst);
        lblDetGerEst.setBounds(1030, 210, 70, 20);

        txtDetGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtDetGerEst.setFont(fontmed(13));
        txtDetGerEst.setBorder(null);
        txtDetGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDetGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDetGerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtDetGerEst);
        txtDetGerEst.setBounds(1030, 210, 190, 20);

        sepDetGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepDetGerEst);
        sepDetGerEst.setBounds(1030, 230, 190, 10);

        txtTipGerEst.setBackground(new java.awt.Color(246, 246, 246));
        pnlGerEst.add(txtTipGerEst);
        txtTipGerEst.setBounds(30, 20, 40, 22);

        lblBusGerEst.setFont(fontmed(12));
        lblBusGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblBusGerEst.setText("Buscar");
        lblBusGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblBusGerEst);
        lblBusGerEst.setBounds(180, 100, 50, 20);

        txtBusGerEst.setBackground(new java.awt.Color(246, 246, 246));
        txtBusGerEst.setFont(fontmed(13));
        txtBusGerEst.setBorder(null);
        txtBusGerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusGerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusGerEstFocusLost(evt);
            }
        });
        txtBusGerEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusGerEstKeyTyped(evt);
            }
        });
        pnlGerEst.add(txtBusGerEst);
        txtBusGerEst.setBounds(180, 100, 280, 20);

        sepMod2.setForeground(new java.awt.Color(10, 60, 133));
        sepMod2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlGerEst.add(sepMod2);
        sepMod2.setBounds(645, 80, 20, 210);

        sepBusGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepBusGerEst);
        sepBusGerEst.setBounds(180, 120, 280, 10);

        scrGerEst.setBackground(new java.awt.Color(250, 250, 250));
        scrGerEst.setBorder(BorderFactory.createEmptyBorder());

        tblConEst.setEnabled(false);
        tblGerEst.setBackground(new java.awt.Color(246, 246, 246));
        tblGerEst.setBorder(null);
        tblGerEst.setFont(fontmed(10));
        tblGerEst.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblGerEst.setFocusable(false);
        tblGerEst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGerEstMouseClicked(evt);
            }
        });
        scrGerEst.setViewportView(tblGerEst);

        pnlGerEst.add(scrGerEst);
        scrGerEst.setBounds(20, 210, 600, 130);

        pnlPri.add(pnlGerEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1300, 380));

        pnlMas.setBackground(new java.awt.Color(246, 246, 246));
        pnlMas.setLayout(null);

        btnGerMas.setFont(fontmed(12));
        btnGerMas.setForeground(new java.awt.Color(10, 60, 133));
        btnGerMas.setText("Gerar");
        btnGerMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerMasActionPerformed(evt);
            }
        });
        pnlMas.add(btnGerMas);
        btnGerMas.setBounds(90, 250, 90, 40);

        btnCanMas.setFont(fontmed(12));
        btnCanMas.setForeground(new java.awt.Color(10, 60, 133));
        btnCanMas.setText("Voltar");
        btnCanMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanMasActionPerformed(evt);
            }
        });
        pnlMas.add(btnCanMas);
        btnCanMas.setBounds(190, 250, 90, 40);

        btnCopMas.setFont(fontmed(12));
        btnCopMas.setForeground(new java.awt.Color(10, 60, 133));
        btnCopMas.setText("Copiar");
        btnCopMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCopMas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCopMasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCopMasMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCopMasMouseReleased(evt);
            }
        });
        pnlMas.add(btnCopMas);
        btnCopMas.setBounds(980, 263, 50, 20);

        lblNomMas.setFont(fontmed(12));
        lblNomMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNomMas.setText("Nome");
        lblNomMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNomMas);
        lblNomMas.setBounds(90, 50, 70, 20);

        txtNomMas.setBackground(new java.awt.Color(246, 246, 246));
        txtNomMas.setFont(fontmed(13));
        txtNomMas.setBorder(null);
        txtNomMas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNomMasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomMasFocusLost(evt);
            }
        });
        pnlMas.add(txtNomMas);
        txtNomMas.setBounds(90, 50, 190, 20);

        sepDesGerTipSer1.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer1);
        sepDesGerTipSer1.setBounds(90, 70, 190, 10);

        sepDesGerTipSer2.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer2);
        sepDesGerTipSer2.setBounds(90, 70, 190, 10);

        lblNumConMas.setFont(fontmed(12));
        lblNumConMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNumConMas.setText("Número de Contato");
        lblNumConMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNumConMas);
        lblNumConMas.setBounds(350, 50, 120, 20);

        sepDesGerTipSer3.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer3);
        sepDesGerTipSer3.setBounds(350, 70, 170, 10);

        txtNumConMas.setBackground(new java.awt.Color(246, 246, 246));
        txtNumConMas.setFont(fontmed(13));
        txtNumConMas.setBorder(null);
        txtNumConMas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumConMasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumConMasFocusLost(evt);
            }
        });
        txtNumConMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumConMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtNumConMas);
        txtNumConMas.setBounds(350, 50, 170, 20);

        lblCpfMas.setFont(fontmed(12));
        lblCpfMas.setForeground(new java.awt.Color(10, 60, 133));
        lblCpfMas.setText("CPF");
        lblCpfMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblCpfMas);
        lblCpfMas.setBounds(90, 110, 70, 20);

        sepDesGerTipSer4.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer4);
        sepDesGerTipSer4.setBounds(90, 130, 140, 10);

        txtCpfMas.setBackground(new java.awt.Color(246, 246, 246));
        txtCpfMas.setFont(fontmed(13));
        txtCpfMas.setBorder(null);
        txtCpfMas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCpfMasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCpfMasFocusLost(evt);
            }
        });
        txtCpfMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCpfMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtCpfMas);
        txtCpfMas.setBounds(90, 110, 140, 20);

        lblNumAceMas.setFont(fontmed(12));
        lblNumAceMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNumAceMas.setText("Número de Acesso");
        lblNumAceMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNumAceMas);
        lblNumAceMas.setBounds(350, 110, 130, 20);

        sepDesGerTipSer5.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer5);
        sepDesGerTipSer5.setBounds(350, 130, 170, 10);

        txtNumAceMas.setBackground(new java.awt.Color(246, 246, 246));
        txtNumAceMas.setFont(fontmed(13));
        txtNumAceMas.setBorder(null);
        txtNumAceMas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumAceMasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumAceMasFocusLost(evt);
            }
        });
        txtNumAceMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumAceMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtNumAceMas);
        txtNumAceMas.setBounds(350, 110, 170, 20);

        lblNumPorMas.setFont(fontmed(12));
        lblNumPorMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNumPorMas.setText("Número Portado");
        lblNumPorMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNumPorMas);
        lblNumPorMas.setBounds(350, 170, 100, 20);

        sepDesGerTipSer6.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer6);
        sepDesGerTipSer6.setBounds(350, 190, 170, 10);

        txtNumPorMas.setBackground(new java.awt.Color(246, 246, 246));
        txtNumPorMas.setFont(fontmed(13));
        txtNumPorMas.setBorder(null);
        txtNumPorMas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumPorMasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumPorMasFocusLost(evt);
            }
        });
        txtNumPorMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumPorMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtNumPorMas);
        txtNumPorMas.setBounds(350, 170, 170, 20);

        btnGroup.add(rbtnMigTroMas);
        rbtnMigTroMas.setFont(fontmed(12));
        rbtnMigTroMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnMigTroMas.setText("Migração (Troca de Chip)");
        rbtnMigTroMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnMigTroMas);
        rbtnMigTroMas.setBounds(600, 100, 190, 21);

        btnGroup.add(rbtnAtiMas);
        rbtnAtiMas.setFont(fontmed(12));
        rbtnAtiMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAtiMas.setText("Ativação");
        rbtnAtiMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnAtiMas);
        rbtnAtiMas.setBounds(600, 40, 90, 21);

        btnGroup.add(rbtnMigMas);
        rbtnMigMas.setFont(fontmed(12));
        rbtnMigMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnMigMas.setText("Migração");
        rbtnMigMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnMigMas);
        rbtnMigMas.setBounds(600, 70, 90, 21);

        lblPlaMas.setFont(fontmed(12));
        lblPlaMas.setForeground(new java.awt.Color(10, 60, 133));
        lblPlaMas.setText("Plano");
        lblPlaMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblPlaMas);
        lblPlaMas.setBounds(350, 230, 100, 20);

        sepDesGerTipSer7.setForeground(new java.awt.Color(10, 60, 133));
        sepDesGerTipSer7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlMas.add(sepDesGerTipSer7);
        sepDesGerTipSer7.setBounds(860, 30, 30, 235);

        txtPlaMas.setBackground(new java.awt.Color(246, 246, 246));
        txtPlaMas.setFont(fontmed(13));
        txtPlaMas.setBorder(null);
        txtPlaMas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPlaMasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPlaMasFocusLost(evt);
            }
        });
        pnlMas.add(txtPlaMas);
        txtPlaMas.setBounds(350, 230, 170, 20);

        btnGroup1.add(rbtnSieMas);
        rbtnSieMas.setFont(fontmed(12));
        rbtnSieMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnSieMas.setText("Siebel Pós");
        rbtnSieMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnSieMas);
        rbtnSieMas.setBounds(600, 160, 90, 21);

        btnGroup1.add(rbtnAppMas);
        rbtnAppMas.setFont(fontmed(12));
        rbtnAppMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAppMas.setText("App TIM Vendas");
        rbtnAppMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnAppMas);
        rbtnAppMas.setBounds(600, 190, 160, 21);

        chkC6Mas.setFont(fontmed(12));
        chkC6Mas.setForeground(new java.awt.Color(10, 60, 133));
        chkC6Mas.setText("Conta C6 Bank");
        pnlMas.add(chkC6Mas);
        chkC6Mas.setBounds(600, 240, 130, 20);

        lblVenMas.setFont(fontmed(12));
        lblVenMas.setForeground(new java.awt.Color(10, 60, 133));
        lblVenMas.setText("Vencimento");
        lblVenMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblVenMas);
        lblVenMas.setBounds(90, 170, 90, 20);

        sepDesGerTipSer8.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer8);
        sepDesGerTipSer8.setBounds(90, 190, 90, 10);

        txtVenMas.setBackground(new java.awt.Color(246, 246, 246));
        txtVenMas.setFont(fontmed(13));
        txtVenMas.setBorder(null);
        txtVenMas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtVenMasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVenMasFocusLost(evt);
            }
        });
        txtVenMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVenMasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVenMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtVenMas);
        txtVenMas.setBounds(90, 170, 90, 20);

        sepDesGerTipSer9.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer9);
        sepDesGerTipSer9.setBounds(350, 250, 170, 10);

        jScrollPane1.setBackground(new java.awt.Color(240, 240, 240));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txtAreMas.setEditable(false);
        txtAreMas.setBackground(new java.awt.Color(246, 246, 246));
        txtAreMas.setColumns(20);
        txtAreMas.setFont(fontmed(10));
        txtAreMas.setForeground(new java.awt.Color(10, 60, 133));
        txtAreMas.setRows(5);
        txtAreMas.setBorder(null);
        txtAreMas.setFocusable(false);
        jScrollPane1.setViewportView(txtAreMas);

        pnlMas.add(jScrollPane1);
        jScrollPane1.setBounds(980, 15, 220, 240);

        pnlPri.add(pnlMas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 1300, 360));

        pnlGerTipSer.setBackground(new java.awt.Color(246, 246, 246));
        pnlGerTipSer.setLayout(null);

        btnExcGerTipSer.setFont(fontmed(12));
        btnExcGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        btnExcGerTipSer.setText("Excluir");
        btnExcGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcGerTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcGerTipSerActionPerformed(evt);
            }
        });
        pnlGerTipSer.add(btnExcGerTipSer);
        btnExcGerTipSer.setBounds(600, 250, 90, 40);

        btnAltGerTipSer.setFont(fontmed(12));
        btnAltGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        btnAltGerTipSer.setText("Alterar");
        btnAltGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltGerTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltGerTipSerActionPerformed(evt);
            }
        });
        pnlGerTipSer.add(btnAltGerTipSer);
        btnAltGerTipSer.setBounds(500, 250, 90, 40);

        btnCanGerTipSer.setFont(fontmed(12));
        btnCanGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        btnCanGerTipSer.setText("Cancelar");
        btnCanGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanGerTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanGerTipSerActionPerformed(evt);
            }
        });
        pnlGerTipSer.add(btnCanGerTipSer);
        btnCanGerTipSer.setBounds(700, 250, 90, 40);

        lblDesTipSer2.setFont(fontmed(12));
        lblDesTipSer2.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer2.setText("Escolha um para alterar ou excluir");
        lblDesTipSer2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerTipSer.add(lblDesTipSer2);
        lblDesTipSer2.setBounds(510, 10, 260, 20);

        lblDesGerTipSer.setFont(fontmed(12));
        lblDesGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        lblDesGerTipSer.setText("Descrição");
        lblDesGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerTipSer.add(lblDesGerTipSer);
        lblDesGerTipSer.setBounds(510, 200, 70, 20);

        txtDesGerTipSer.setBackground(new java.awt.Color(246, 246, 246));
        txtDesGerTipSer.setFont(fontmed(13));
        txtDesGerTipSer.setBorder(null);
        txtDesGerTipSer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDesGerTipSerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDesGerTipSerFocusLost(evt);
            }
        });
        pnlGerTipSer.add(txtDesGerTipSer);
        txtDesGerTipSer.setBounds(510, 200, 270, 20);

        sepDesGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerTipSer.add(sepDesGerTipSer);
        sepDesGerTipSer.setBounds(510, 220, 270, 10);

        scrTipSer.setBackground(new java.awt.Color(250, 250, 250));
        scrTipSer.setBorder(BorderFactory.createEmptyBorder());
        scrTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tblTipSer.setTableHeader(null);
        tblTipSer.setBackground(new java.awt.Color(246, 246, 246));
        tblTipSer.setBorder(null);
        tblTipSer.setFont(fontmed(12));
        tblTipSer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblTipSer.setFocusable(false);
        tblTipSer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTipSerMouseClicked(evt);
            }
        });
        scrTipSer.setViewportView(tblTipSer);

        pnlGerTipSer.add(scrTipSer);
        scrTipSer.setBounds(510, 40, 270, 120);

        pnlPri.add(pnlGerTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 1300, 360));

        getContentPane().add(pnlPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalCadEstActionPerformed
        try {

            estoque es = new estoque();
            estoqueDAO esdao = new estoqueDAO();

            es.setTipoproduto(txtTipCadEst.getText());
            es.setModelo(txtModCadEst.getText());
            es.setMarca(txtMarCadEst.getText());
            es.setCor(txtCorCadEst.getText());
            es.setMaterial(txtMatCadEst.getText());
            es.setDetalhes(txtDetCadEst.getText());
            es.setLocalizacao(txtLocCadEst.getText());
            es.setPreco(Double.valueOf(txtPreCadEst.getText()));
            es.setQuantidade(Integer.parseInt(txtQuaCadEst.getText()));

            if (txtTipCadEst.getText().equals("Chip")) {
                es.setTipochip((String) cmbChiCadEst.getSelectedItem());
            } else {
                es.setTipochip(null);
            }

            if (!esdao.verifica(es)) {

                esdao.inserir(es);

                JOptionPane.showMessageDialog(pnlCadEst, "Novo ítem inserido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } else {

                esdao.acrescentar(es);

                JOptionPane.showMessageDialog(pnlCadEst, "Inserido com sucesso! O ítem já existia no estoque e foi adicionado a nova quantidade!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            }

            pnlCadEst.setVisible(false);
            lblTitPri.setVisible(false);
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(pnlCadEst, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException n) {

            JOptionPane.showMessageDialog(pnlCadEst, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);

        }
    }//GEN-LAST:event_btnSalCadEstActionPerformed

    private void btnTipSerPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTipSerPriMouseEntered
        btnTipSerPri.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnTipSerPriMouseEntered

    private void btnTipSerPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTipSerPriMouseExited
        btnTipSerPri.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnTipSerPriMouseExited

    private void btnRelPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelPriMouseEntered
        btnRelPri.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnRelPriMouseEntered

    private void btnRelPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelPriMouseExited
        btnRelPri.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnRelPriMouseExited

    private void btnEstPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstPriMouseEntered
        btnEstPri.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnEstPriMouseEntered

    private void btnEstPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstPriMouseExited
        btnEstPri.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnEstPriMouseExited

    private void btnEntPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntPriMouseEntered
        btnEntPri.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnEntPriMouseEntered

    private void btnEntPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntPriMouseExited
        btnEntPri.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnEntPriMouseExited

    private void btnEstPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEstPriMouseReleased
        if (btnCadEst.isVisible()) {
            btnCadEst.setVisible(false);
            btnConEst.setVisible(false);
            btnGerEst.setVisible(false);
        } else {
            btnCadEst.setVisible(true);
            btnConEst.setVisible(true);
            btnGerEst.setVisible(true);
        }
    }//GEN-LAST:event_btnEstPriMouseReleased

    private void txtModCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModCadEstFocusGained
        if (txtModCadEst.getText().isEmpty()) {
            anitxtin(lblModCadEst);
        }
    }//GEN-LAST:event_txtModCadEstFocusGained

    private void txtModCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModCadEstFocusLost
        if (txtModCadEst.getText().isEmpty()) {
            anitxtout(lblModCadEst);
        }
    }//GEN-LAST:event_txtModCadEstFocusLost

    private void txtMarCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarCadEstFocusGained
        if (txtMarCadEst.getText().isEmpty()) {
            anitxtin(lblMarCadEst);
        }
    }//GEN-LAST:event_txtMarCadEstFocusGained

    private void txtMarCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarCadEstFocusLost
        if (txtMarCadEst.getText().isEmpty()) {
            anitxtout(lblMarCadEst);
        }
    }//GEN-LAST:event_txtMarCadEstFocusLost

    private void txtCorCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorCadEstFocusGained
        if (txtCorCadEst.getText().isEmpty()) {
            anitxtin(lblCorCadEst);
        }
    }//GEN-LAST:event_txtCorCadEstFocusGained

    private void txtCorCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorCadEstFocusLost
        if (txtCorCadEst.getText().isEmpty()) {
            anitxtout(lblCorCadEst);
        }
    }//GEN-LAST:event_txtCorCadEstFocusLost

    private void txtMatCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMatCadEstFocusGained
        if (txtMatCadEst.getText().isEmpty()) {
            anitxtin(lblMatCadEst);
        }
    }//GEN-LAST:event_txtMatCadEstFocusGained

    private void txtMatCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMatCadEstFocusLost
        if (txtMatCadEst.getText().isEmpty()) {
            anitxtout(lblMatCadEst);
        }
    }//GEN-LAST:event_txtMatCadEstFocusLost

    private void txtQuaCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuaCadEstFocusGained
        if (txtQuaCadEst.getText().isEmpty()) {
            anitxtin(lblQuaCadEst);
        }
    }//GEN-LAST:event_txtQuaCadEstFocusGained

    private void txtQuaCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuaCadEstFocusLost
        if (txtQuaCadEst.getText().isEmpty()) {
            anitxtout(lblQuaCadEst);
        }
    }//GEN-LAST:event_txtQuaCadEstFocusLost

    private void txtLocCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLocCadEstFocusGained
        if (txtLocCadEst.getText().isEmpty()) {
            anitxtin(lblLocCadEst);
        }
    }//GEN-LAST:event_txtLocCadEstFocusGained

    private void txtLocCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLocCadEstFocusLost
        if (txtLocCadEst.getText().isEmpty()) {
            anitxtout(lblLocCadEst);
        }
    }//GEN-LAST:event_txtLocCadEstFocusLost

    private void txtDetCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetCadEstFocusGained
        if (txtDetCadEst.getText().isEmpty()) {
            anitxtin(lblDetCadEst);
        }
    }//GEN-LAST:event_txtDetCadEstFocusGained

    private void txtDetCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetCadEstFocusLost
        if (txtDetCadEst.getText().isEmpty()) {
            anitxtout(lblDetCadEst);
        }
    }//GEN-LAST:event_txtDetCadEstFocusLost

    private void txtQuaCadEstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuaCadEstKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtQuaCadEstKeyTyped

    private void btnCanCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanCadEstActionPerformed
        if (btnSalCadEst.isEnabled()) {
            int resp = JOptionPane.showOptionDialog(pnlCadEst, "Cancelar inserção? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                pnlCadEst.setVisible(false);
                lblTitPri.setVisible(false);

            }

        } else {

            pnlCadEst.setVisible(false);
            lblTitPri.setVisible(false);

        }
    }//GEN-LAST:event_btnCanCadEstActionPerformed

    private void btnSalTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalTipSerActionPerformed
        try {
            tiposervico ts = new tiposervico();
            tiposervicoDAO tsdao = new tiposervicoDAO();

            ts.setDescricao(txtDesTipSer.getText());

            if (rbtnSerTimTipSer.isSelected()) {

                ts.setArea("1");

            } else if (rbtnAssTipSer.isSelected()) {

                ts.setArea("3");

            } else {

                ts.setArea("2");

            }

            tsdao.inserir(ts);

            JOptionPane.showMessageDialog(pnlCadEst, "Inserido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            pnlCadTipSer.setVisible(false);
            lblTitPri.setVisible(false);

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(pnlCadEst, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_btnSalTipSerActionPerformed

    private void btnCanTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanTipSerActionPerformed
        int resp = JOptionPane.showOptionDialog(pnlCadTipSer, "Cancelar inserção? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            pnlCadTipSer.setVisible(false);
            lblTitPri.setVisible(false);

        }
    }//GEN-LAST:event_btnCanTipSerActionPerformed

    private void txtDesTipSerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesTipSerFocusGained
        if (txtDesTipSer.getText().isEmpty()) {
            anitxtin(lblDesTipSer);
        }
    }//GEN-LAST:event_txtDesTipSerFocusGained

    private void txtDesTipSerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesTipSerFocusLost
        if (txtDesTipSer.getText().isEmpty()) {
            anitxtout(lblDesTipSer);
        }
    }//GEN-LAST:event_txtDesTipSerFocusLost

    private void rbtnSerTimTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSerTimTipSerActionPerformed
        txtDesTipSer.setEnabled(true);
        lblDesTipSer.setEnabled(true);
        sepDesTipSer.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnSerTimTipSerActionPerformed

    private void btnOrdSerPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOrdSerPriMouseEntered
        btnOrdSerPri.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnOrdSerPriMouseEntered

    private void btnOrdSerPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOrdSerPriMouseExited
        btnOrdSerPri.setForeground(corforeazul);
    }//GEN-LAST:event_btnOrdSerPriMouseExited

    private void btnCadTipSerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadTipSerMouseReleased
        if (!pnlCadTipSer.isVisible()) {

            btnGroup.clearSelection();
            btnSalTipSer.setEnabled(false);
            txtDesTipSer.setText(null);
            lblDesTipSer.setLocation(510, 110);

            txtDesTipSer.setEnabled(false);
            lblDesTipSer.setEnabled(false);
            sepDesTipSer.setForeground(Color.GRAY);

            pnlGerTipSer.setVisible(false);
            pnlCadTipSer.setVisible(true);

            pnlCadEnt.setVisible(false);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(false);
            pnlMas.setVisible(false);
            pnlDes.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlGerEnt.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

            lblTitPri.setVisible(true);
            lblTitPri.setText("Cadastrar Tipo de Serviço");

            btnCadTipSer.setVisible(false);
            btnGerTipSer.setVisible(false);
        }
    }//GEN-LAST:event_btnCadTipSerMouseReleased

    private void btnCadTipSerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadTipSerMouseEntered
        btnCadTipSer.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadTipSerMouseEntered

    private void btnCadTipSerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadTipSerMouseExited
        btnCadTipSer.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadTipSerMouseExited

    private void btnGerTipSerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerTipSerMouseEntered
        btnGerTipSer.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerTipSerMouseEntered

    private void btnGerTipSerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerTipSerMouseExited
        btnGerTipSer.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerTipSerMouseExited

    private void btnGerTipSerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerTipSerMouseReleased
        if (!pnlGerTipSer.isVisible()) {

            if (tabelatiposervico()) {

                txtDesGerTipSer.setText(null);
                btnExcGerTipSer.setEnabled(false);
                btnAltGerTipSer.setEnabled(false);
                lblDesGerTipSer.setLocation(510, 200);

                pnlGerTipSer.setVisible(true);
                pnlCadTipSer.setVisible(false);

                pnlCadEnt.setVisible(false);
                pnlRel.setVisible(false);
                pnlIteCadEnt.setVisible(false);
                pnlCadEst.setVisible(false);
                pnlConEst.setVisible(false);
                pnlGerEst.setVisible(false);
                pnlMas.setVisible(false);
                pnlDes.setVisible(false);
                pnlGerDes.setVisible(false);
                pnlCadDes.setVisible(false);
                pnlGerEnt.setVisible(false);
                pnlOs.setVisible(false);
                pnlIteGerEnt.setVisible(false);

                lblTitPri.setVisible(true);
                lblTitPri.setText("Gerenciar Tipo de Serviço");

                txtDesGerTipSer.setEnabled(false);
                lblDesGerTipSer.setEnabled(false);
                sepDesGerTipSer.setForeground(Color.GRAY);

                btnCadTipSer.setVisible(false);
                btnGerTipSer.setVisible(false);

            } else {

                JOptionPane.showMessageDialog(pnlGerDes, "Sem tipo de serviço para gerenciar. Cadastre-o primeiro!", "Gerenciar Tipo de Serviço", JOptionPane.INFORMATION_MESSAGE);

            }

        }
    }//GEN-LAST:event_btnGerTipSerMouseReleased

    private void btnTipSerPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTipSerPriMouseReleased
        if (!btnGerTipSer.isVisible()) {
            btnGerTipSer.setVisible(true);
            btnCadTipSer.setVisible(true);
        } else {
            btnGerTipSer.setVisible(false);
            btnCadTipSer.setVisible(false);
        }
    }//GEN-LAST:event_btnTipSerPriMouseReleased

    private void btnCadEstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEstMouseEntered
        btnCadEst.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadEstMouseEntered

    private void btnCadEstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEstMouseExited
        btnCadEst.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadEstMouseExited

    private void btnCadEstMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEstMouseReleased
        if (!pnlCadEst.isVisible()) {

            txtModCadEst.setEnabled(false);
            lblModCadEst.setEnabled(false);
            txtMarCadEst.setEnabled(false);
            lblMarCadEst.setEnabled(false);
            txtCorCadEst.setEnabled(false);
            lblCorCadEst.setEnabled(false);
            txtMatCadEst.setEnabled(false);
            lblMatCadEst.setEnabled(false);
            txtQuaCadEst.setEnabled(false);
            lblQuaCadEst.setEnabled(false);
            txtPreCadEst.setEnabled(false);
            lblPreCadEst.setEnabled(false);
            lblR$CadEst.setVisible(false);
            lblChiCadEst.setEnabled(false);
            cmbChiCadEst.setEnabled(false);
            txtLocCadEst.setEnabled(false);
            lblLocCadEst.setEnabled(false);
            txtDetCadEst.setEnabled(false);
            lblDetCadEst.setEnabled(false);
            sepModCadEst.setForeground(Color.GRAY);
            sepMarCadEst.setForeground(Color.GRAY);
            sepCorCadEst.setForeground(Color.GRAY);
            sepMatCadEst.setForeground(Color.GRAY);
            sepQuaCadEst.setForeground(Color.GRAY);
            sepPreCadEst.setForeground(Color.GRAY);
            sepLocCadEst.setForeground(Color.GRAY);
            sepDetCadEst.setForeground(Color.GRAY);

            txtModCadEst.setText(null);
            txtMarCadEst.setText(null);
            txtCorCadEst.setText(null);
            txtMatCadEst.setText(null);
            txtQuaCadEst.setText(null);
            txtPreCadEst.setText(null);
            cmbChiCadEst.setSelectedIndex(0);
            txtLocCadEst.setText(null);
            txtDetCadEst.setText(null);

            lblMarCadEst.setLocation(410, 80);
            lblModCadEst.setLocation(410, 130);
            lblQuaCadEst.setLocation(410, 180);
            lblPreCadEst.setLocation(410, 230);
            lblCorCadEst.setLocation(700, 80);
            lblMatCadEst.setLocation(700, 130);
            lblLocCadEst.setLocation(700, 180);
            lblDetCadEst.setLocation(700, 230);

            btnGroup.clearSelection();

            pnlCadEst.setVisible(true);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(false);
            pnlGerDes.setVisible(false);

            pnlCadEnt.setVisible(false);
            pnlGerEnt.setVisible(false);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlMas.setVisible(false);
            pnlDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

            lblTitPri.setVisible(true);
            lblTitPri.setText("Cadastrar Estoque");

            btnSalCadEst.setEnabled(false);

        }

        btnCadEst.setVisible(false);
        btnConEst.setVisible(false);
        btnGerEst.setVisible(false);
    }//GEN-LAST:event_btnCadEstMouseReleased

    private void btnConEstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseEntered
        btnConEst.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnConEstMouseEntered

    private void btnConEstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseExited
        btnConEst.setForeground(corforeazul);
    }//GEN-LAST:event_btnConEstMouseExited

    private void btnConEstMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseReleased
        if (!pnlConEst.isVisible()) {

            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(true);
            pnlGerEst.setVisible(false);

            txtBusConEst.setText(null);
            lblBusConEst.setLocation(450, 110);
            btnGroup.clearSelection();

            pnlCadEnt.setVisible(false);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlMas.setVisible(false);
            pnlDes.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlGerEnt.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

            scrConEst.setVisible(false);
            btnBusConEst.setEnabled(false);

            txtBusConEst.setEnabled(false);
            lblBusConEst.setEnabled(false);
            sepBusConEst.setForeground(Color.GRAY);

            lblTitPri.setVisible(true);
            lblTitPri.setText("Consultar Estoque");

            btnCadEst.setVisible(false);
            btnConEst.setVisible(false);
            btnGerEst.setVisible(false);
        }
    }//GEN-LAST:event_btnConEstMouseReleased

    private void btnGerEstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEstMouseEntered
        btnGerEst.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerEstMouseEntered

    private void btnGerEstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEstMouseExited
        btnGerEst.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerEstMouseExited

    private void btnGerEstMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEstMouseReleased
        if (!pnlGerEst.isVisible()) {

            txtTipGerEst.setVisible(false);

            txtBusGerEst.setEnabled(false);
            lblBusGerEst.setEnabled(false);
            sepBusGerEst.setForeground(Color.GRAY);

            txtModGerEst.setEnabled(false);
            lblModGerEst.setEnabled(false);
            sepModGerEst.setForeground(Color.GRAY);

            txtMarGerEst.setEnabled(false);
            lblMarGerEst.setEnabled(false);
            sepMarGerEst.setForeground(Color.GRAY);

            txtCorGerEst.setEnabled(false);
            lblCorGerEst.setEnabled(false);
            sepCorGerEst.setForeground(Color.GRAY);

            txtMatGerEst.setEnabled(false);
            lblMatGerEst.setEnabled(false);
            sepMatGerEst.setForeground(Color.GRAY);

            txtQuaGerEst.setEnabled(false);
            lblQuaGerEst.setEnabled(false);
            sepQuaGerEst.setForeground(Color.GRAY);

            txtPreGerEst.setEnabled(false);
            lblPreGerEst.setEnabled(false);
            sepPreGerEst.setForeground(Color.GRAY);

            txtLocGerEst.setEnabled(false);
            lblLocGerEst.setEnabled(false);
            sepLocGerEst.setForeground(Color.GRAY);

            txtDetGerEst.setEnabled(false);
            lblDetGerEst.setEnabled(false);
            sepDetGerEst.setForeground(Color.GRAY);

            lblChiGerEst.setEnabled(false);
            cmbChiGerEst.setEnabled(false);

            btnAltGerEst.setEnabled(false);
            btnExcGerEst.setEnabled(false);
            btnBusGerEst.setEnabled(false);

            btnGroup.clearSelection();
            cmbChiGerEst.setSelectedItem("Triplo 4G HLR 230");

            lblR$GerEst.setVisible(false);

            tblGerEst.setVisible(false);
            scrGerEst.setVisible(false);

            txtMarGerEst.setText(null);
            txtModGerEst.setText(null);
            txtQuaGerEst.setText(null);
            txtPreGerEst.setText(null);
            txtCorGerEst.setText(null);
            txtMatGerEst.setText(null);
            txtLocGerEst.setText(null);
            txtDetGerEst.setText(null);
            txtBusGerEst.setText(null);

            lblMarGerEst.setLocation(730, 60);
            lblModGerEst.setLocation(730, 110);
            lblQuaGerEst.setLocation(730, 160);
            lblPreGerEst.setLocation(730, 210);
            lblCorGerEst.setLocation(1030, 60);
            lblMatGerEst.setLocation(1030, 110);
            lblLocGerEst.setLocation(1030, 160);
            lblDetGerEst.setLocation(1030, 210);
            lblBusGerEst.setLocation(180, 100);

            btnCadEst.setVisible(false);
            btnConEst.setVisible(false);
            btnGerEst.setVisible(false);

            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(true);
            pnlDes.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

            lblTitPri.setVisible(true);
            lblTitPri.setText("Gerenciar Estoque");

            pnlCadEnt.setVisible(false);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlMas.setVisible(false);
            pnlGerEnt.setVisible(false);

        }
    }//GEN-LAST:event_btnGerEstMouseReleased

    private void btnOutPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOutPriMouseEntered
        btnOutPri.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnOutPriMouseEntered

    private void btnOutPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOutPriMouseExited
        btnOutPri.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnOutPriMouseExited

    private void btnOutPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOutPriMouseReleased
        if (btnMasPla.isVisible()) {
            btnMasPla.setVisible(false);
            btnDes.setVisible(false);
            btnCadDes.setVisible(false);
            btnGerDes.setVisible(false);

        } else {
            btnMasPla.setVisible(true);
            btnDes.setVisible(true);
            btnCadDes.setVisible(true);
            btnGerDes.setVisible(true);
        }
    }//GEN-LAST:event_btnOutPriMouseReleased

    private void btnMasPlaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseEntered
        btnMasPla.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnMasPlaMouseEntered

    private void btnMasPlaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseExited
        btnMasPla.setForeground(corforeazul);
    }//GEN-LAST:event_btnMasPlaMouseExited

    private void btnMasPlaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseReleased
        btnMasPla.setVisible(false);
        btnDes.setVisible(false);
        btnCadDes.setVisible(false);
        btnGerDes.setVisible(false);

        if (!pnlMas.isVisible()) {

            lblTitPri.setText("Máscara de Plano");
            lblTitPri.setVisible(true);

            txtNomMas.setText(null);
            txtCpfMas.setText(null);
            txtVenMas.setText(null);
            txtNumConMas.setText(null);
            txtNumAceMas.setText(null);
            txtNumPorMas.setText(null);
            txtPlaMas.setText(null);
            btnGroup.clearSelection();
            btnGroup1.clearSelection();

            chkC6Mas.setSelected(false);

            txtAreMas.setText(null);

            lblNomMas.setLocation(90, 50);
            lblCpfMas.setLocation(90, 110);
            lblVenMas.setLocation(90, 170);
            lblNumConMas.setLocation(350, 50);
            lblNumAceMas.setLocation(350, 110);
            lblNumPorMas.setLocation(350, 170);
            lblPlaMas.setLocation(350, 230);

            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlCadEnt.setVisible(false);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlDes.setVisible(false);
            pnlGerEnt.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

            btnCanMas.grabFocus();

            btnCopMas.setVisible(false);
            pnlMas.setVisible(true);
        }
    }//GEN-LAST:event_btnMasPlaMouseReleased

    private void btnCadDesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadDesMouseEntered
        btnCadDes.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadDesMouseEntered

    private void btnCadDesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadDesMouseExited
        btnCadDes.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadDesMouseExited

    private void btnCadDesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadDesMouseReleased
        btnMasPla.setVisible(false);
        btnDes.setVisible(false);
        btnCadDes.setVisible(false);
        btnGerDes.setVisible(false);

        if (!pnlCadDes.isVisible()) {
            txtDesDes.setText(null);
            txtPreDes.setText(null);
            txtDatDes.setText(null);
            btnSalDes.setEnabled(false);

            lblTitPri.setText("Cadastrar Despezas");
            lblTitPri.setVisible(true);

            lblDesDes.setLocation(540, 60);
            lblPreDes.setLocation(540, 120);
            lblDatDes.setLocation(540, 180);

            lblR$Des.setVisible(false);

            pnlCadDes.setVisible(true);
            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(false);

            pnlCadEnt.setVisible(false);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlDes.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlMas.setVisible(false);
            pnlGerEnt.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

        }
    }//GEN-LAST:event_btnCadDesMouseReleased

    private void btnDesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseEntered
        btnDes.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnDesMouseEntered

    private void btnDesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseExited
        btnDes.setForeground(corforeazul);
    }//GEN-LAST:event_btnDesMouseExited

    private void btnDesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseReleased
        btnMasPla.setVisible(false);
        btnDes.setVisible(false);
        btnCadDes.setVisible(false);
        btnGerDes.setVisible(false);

        if (!pnlDes.isVisible()) {

            if (tabeladespezas(tblConDes, scrConDes)) {

                pnlCadEst.setVisible(false);
                pnlConEst.setVisible(false);
                pnlGerEst.setVisible(false);

                lblTitPri.setText("Despezas");
                lblTitPri.setVisible(true);
                pnlCadEnt.setVisible(false);
                pnlRel.setVisible(false);
                pnlIteCadEnt.setVisible(false);
                pnlCadTipSer.setVisible(false);
                pnlGerTipSer.setVisible(false);
                pnlMas.setVisible(false);
                pnlDes.setVisible(true);
                pnlGerDes.setVisible(false);
                pnlCadDes.setVisible(false);
                pnlGerEnt.setVisible(false);
                pnlOs.setVisible(false);
                pnlIteGerEnt.setVisible(false);

            } else {

                JOptionPane.showMessageDialog(pnlDes, "Sem despezas. Cadastre-as primeiro!", "Gerenciar Despezas", JOptionPane.INFORMATION_MESSAGE);

            }

        }
    }//GEN-LAST:event_btnDesMouseReleased

    private void btnExcGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerTipSerActionPerformed
        int resp = JOptionPane.showOptionDialog(pnlGerTipSer, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {
            try {
                tiposervico ts = new tiposervico();
                tiposervicoDAO tsdao = new tiposervicoDAO();

                ts.setDescricao(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 0).toString());

                tsdao.excluir(ts);

                JOptionPane.showMessageDialog(pnlGerTipSer, "Excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                pnlGerTipSer.setVisible(false);
                lblTitPri.setVisible(false);
            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(pnlGerTipSer, "Erro ao excluir! Erro: " + ex.getMessage(), "Erro", JOptionPane.OK_OPTION);

            }
        }
    }//GEN-LAST:event_btnExcGerTipSerActionPerformed

    private void btnAltGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerTipSerActionPerformed
        int resp = JOptionPane.showOptionDialog(pnlGerTipSer, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {
                tiposervico ts = new tiposervico();
                tiposervicoDAO tsdao = new tiposervicoDAO();

                ts.setDescricao(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 0).toString());
                ts.setArea(txtDesGerTipSer.getText());
                tsdao.alterar(ts);

                JOptionPane.showMessageDialog(pnlGerTipSer, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                pnlGerTipSer.setVisible(false);
                lblTitPri.setVisible(false);
            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(pnlGerTipSer, "Erro ao alterar! Erro: " + ex.getMessage(), "Erro", JOptionPane.OK_OPTION);

            }
        }
    }//GEN-LAST:event_btnAltGerTipSerActionPerformed

    private void txtDesGerTipSerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesGerTipSerFocusGained
        if (txtDesGerTipSer.getText().isEmpty()) {
            anitxtin(lblDesGerTipSer);
        }
    }//GEN-LAST:event_txtDesGerTipSerFocusGained

    private void txtDesGerTipSerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesGerTipSerFocusLost
        if (txtDesGerTipSer.getText().isEmpty()) {
            anitxtout(lblDesGerTipSer);
        }
    }//GEN-LAST:event_txtDesGerTipSerFocusLost

    private void btnCanGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerTipSerActionPerformed
        pnlGerTipSer.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnCanGerTipSerActionPerformed

    private void tblTipSerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTipSerMouseClicked
        lblDesGerTipSer.setLocation(510, 200);

        anitxtin(lblDesGerTipSer);

        String txt = tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 0).toString();

        txtDesGerTipSer.setEnabled(true);
        lblDesGerTipSer.setEnabled(true);
        sepDesGerTipSer.setForeground(corforeazul);

        txtDesGerTipSer.setText(txt);
        btnExcGerTipSer.setEnabled(true);
        btnAltGerTipSer.setEnabled(true);
    }//GEN-LAST:event_tblTipSerMouseClicked

    private void txtDesTipSerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDesTipSerKeyTyped
        btnSalTipSer.setEnabled(true);
    }//GEN-LAST:event_txtDesTipSerKeyTyped

    private void rbtnAssTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAssTipSerActionPerformed
        txtDesTipSer.setEnabled(true);
        lblDesTipSer.setEnabled(true);
        sepDesTipSer.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnAssTipSerActionPerformed

    private void txtBusConEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusConEstFocusGained
        if (txtBusConEst.getText().isEmpty()) {
            anitxtin(lblBusConEst);
        }
    }//GEN-LAST:event_txtBusConEstFocusGained

    private void txtBusConEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusConEstFocusLost
        if (txtBusConEst.getText().isEmpty()) {
            anitxtout(lblBusConEst);
        }
    }//GEN-LAST:event_txtBusConEstFocusLost

    private void rbtnAceConEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAceConEstActionPerformed
        txtTipConEst.setText(rbtnAceConEst.getText());
        txtBusConEst.setEnabled(true);
        lblBusConEst.setEnabled(true);
        sepBusConEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnAceConEstActionPerformed

    private void rbtnChiConEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnChiConEstActionPerformed
        txtTipConEst.setText(rbtnChiConEst.getText());
        txtBusConEst.setEnabled(true);
        lblBusConEst.setEnabled(true);
        sepBusConEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnChiConEstActionPerformed

    private void rbtnPelConEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPelConEstActionPerformed
        txtTipConEst.setText(rbtnPelConEst.getText());
        txtBusConEst.setEnabled(true);
        lblBusConEst.setEnabled(true);
        sepBusConEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnPelConEstActionPerformed

    private void rbtnCapConEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCapConEstActionPerformed
        txtTipConEst.setText(rbtnCapConEst.getText());
        txtBusConEst.setEnabled(true);
        lblBusConEst.setEnabled(true);
        sepBusConEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnCapConEstActionPerformed

    private void btnBusConEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusConEstActionPerformed
        estoque es = new estoque();

        es.setModelo(txtBusConEst.getText());
        es.setTipoproduto(txtTipConEst.getText());

        if (tabelaestoqueconsulta(es, tblConEst, scrConEst)) {

            scrConEst.setVisible(true);
            tblConEst.setVisible(true);

        } else {

            scrConEst.setVisible(false);
            tblConEst.setVisible(false);

            JOptionPane.showMessageDialog(pnlConEst, "Não temos este ítem cadastrado no sistema!", "Consultar", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_btnBusConEstActionPerformed

    private void txtBusConEstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusConEstKeyTyped
        btnBusConEst.setEnabled(true);
    }//GEN-LAST:event_txtBusConEstKeyTyped

    private void btnCanConEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanConEstActionPerformed
        pnlConEst.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnCanConEstActionPerformed

    private void btnAltGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerEstActionPerformed
        int resp = JOptionPane.showOptionDialog(pnlGerEst, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {
                estoque es = new estoque();
                estoqueDAO esdao = new estoqueDAO();

                es.setId(Integer.parseInt(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 0).toString()));
                es.setModelo(txtModGerEst.getText());
                es.setMarca(txtMarGerEst.getText());
                es.setCor(txtCorGerEst.getText());
                es.setMaterial(txtMatGerEst.getText());
                es.setDetalhes(txtDetGerEst.getText());
                es.setLocalizacao(txtLocGerEst.getText());
                es.setPreco(Double.valueOf(txtPreGerEst.getText().replace(".", "").replace(",", ".")));
                es.setQuantidade(Integer.parseInt(txtQuaGerEst.getText()));

                if (txtTipGerEst.getText().equals("Chip")) {
                    es.setTipochip((String) cmbChiGerEst.getSelectedItem());
                } else {
                    es.setTipochip(null);
                }

                esdao.alterar(es);

                JOptionPane.showMessageDialog(pnlGerEst, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                pnlGerEst.setVisible(false);
                lblTitPri.setVisible(false);
            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(pnlGerEst, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

            } catch (NumberFormatException n) {

                JOptionPane.showMessageDialog(pnlCadEst, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);

            }
        }
    }//GEN-LAST:event_btnAltGerEstActionPerformed

    private void btnCanGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerEstActionPerformed
        int resp = JOptionPane.showOptionDialog(pnlGerEst, "Cancelar alterações?", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            pnlGerEst.setVisible(false);
            lblTitPri.setVisible(false);

        }
    }//GEN-LAST:event_btnCanGerEstActionPerformed

    private void rbtnCapGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCapGerEstActionPerformed
        if (txtBusGerEst.getText().equals("")) {

            btnBusGerEst.setEnabled(false);
        } else {
            btnBusGerEst.setEnabled(true);
        }

        txtBusGerEst.setEnabled(false);
        lblBusGerEst.setEnabled(false);
        sepBusGerEst.setForeground(Color.GRAY);

        txtModGerEst.setEnabled(false);
        lblModGerEst.setEnabled(false);
        sepModGerEst.setForeground(Color.GRAY);

        txtMarGerEst.setEnabled(false);
        lblMarGerEst.setEnabled(false);
        sepMarGerEst.setForeground(Color.GRAY);

        txtCorGerEst.setEnabled(false);
        lblCorGerEst.setEnabled(false);
        sepCorGerEst.setForeground(Color.GRAY);

        txtMatGerEst.setEnabled(false);
        lblMatGerEst.setEnabled(false);
        sepMatGerEst.setForeground(Color.GRAY);

        txtQuaGerEst.setEnabled(false);
        lblQuaGerEst.setEnabled(false);
        sepQuaGerEst.setForeground(Color.GRAY);

        txtPreGerEst.setEnabled(false);
        lblPreGerEst.setEnabled(false);
        sepPreGerEst.setForeground(Color.GRAY);

        txtLocGerEst.setEnabled(false);
        lblLocGerEst.setEnabled(false);
        sepLocGerEst.setForeground(Color.GRAY);

        txtDetGerEst.setEnabled(false);
        lblDetGerEst.setEnabled(false);
        sepDetGerEst.setForeground(Color.GRAY);

        lblChiGerEst.setEnabled(false);
        cmbChiGerEst.setEnabled(false);
        cmbChiGerEst.setSelectedItem("Triplo 4G HLR 230");

        btnAltGerEst.setEnabled(false);
        btnExcGerEst.setEnabled(false);

        lblR$GerEst.setVisible(false);

        tblGerEst.setVisible(false);
        scrGerEst.setVisible(false);

        txtMarGerEst.setText(null);
        txtModGerEst.setText(null);
        txtQuaGerEst.setText(null);
        txtPreGerEst.setText(null);
        txtCorGerEst.setText(null);
        txtMatGerEst.setText(null);
        txtLocGerEst.setText(null);
        txtDetGerEst.setText(null);

        lblMarGerEst.setLocation(730, 60);
        lblModGerEst.setLocation(730, 110);
        lblQuaGerEst.setLocation(730, 160);
        lblPreGerEst.setLocation(730, 210);
        lblCorGerEst.setLocation(1030, 60);
        lblMatGerEst.setLocation(1030, 110);
        lblLocGerEst.setLocation(1030, 160);
        lblDetGerEst.setLocation(1030, 210);

        txtTipGerEst.setText(rbtnCapGerEst.getText());
        txtBusGerEst.setEnabled(true);
        lblBusGerEst.setEnabled(true);
        sepBusGerEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnCapGerEstActionPerformed

    private void rbtnPelGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPelGerEstActionPerformed
        if (txtBusGerEst.getText().equals("")) {

            btnBusGerEst.setEnabled(false);
        } else {
            btnBusGerEst.setEnabled(true);
        }

        txtBusGerEst.setEnabled(false);
        lblBusGerEst.setEnabled(false);
        sepBusGerEst.setForeground(Color.GRAY);

        txtModGerEst.setEnabled(false);
        lblModGerEst.setEnabled(false);
        sepModGerEst.setForeground(Color.GRAY);

        txtMarGerEst.setEnabled(false);
        lblMarGerEst.setEnabled(false);
        sepMarGerEst.setForeground(Color.GRAY);

        txtCorGerEst.setEnabled(false);
        lblCorGerEst.setEnabled(false);
        sepCorGerEst.setForeground(Color.GRAY);

        txtMatGerEst.setEnabled(false);
        lblMatGerEst.setEnabled(false);
        sepMatGerEst.setForeground(Color.GRAY);

        txtQuaGerEst.setEnabled(false);
        lblQuaGerEst.setEnabled(false);
        sepQuaGerEst.setForeground(Color.GRAY);

        txtPreGerEst.setEnabled(false);
        lblPreGerEst.setEnabled(false);
        sepPreGerEst.setForeground(Color.GRAY);

        txtLocGerEst.setEnabled(false);
        lblLocGerEst.setEnabled(false);
        sepLocGerEst.setForeground(Color.GRAY);

        txtDetGerEst.setEnabled(false);
        lblDetGerEst.setEnabled(false);
        sepDetGerEst.setForeground(Color.GRAY);

        lblChiGerEst.setEnabled(false);
        cmbChiGerEst.setEnabled(false);
        cmbChiGerEst.setSelectedItem("Triplo 4G HLR 230");

        btnAltGerEst.setEnabled(false);
        btnExcGerEst.setEnabled(false);

        lblR$GerEst.setVisible(false);

        tblGerEst.setVisible(false);
        scrGerEst.setVisible(false);

        txtMarGerEst.setText(null);
        txtModGerEst.setText(null);
        txtQuaGerEst.setText(null);
        txtPreGerEst.setText(null);
        txtCorGerEst.setText(null);
        txtMatGerEst.setText(null);
        txtLocGerEst.setText(null);
        txtDetGerEst.setText(null);

        lblMarGerEst.setLocation(730, 60);
        lblModGerEst.setLocation(730, 110);
        lblQuaGerEst.setLocation(730, 160);
        lblPreGerEst.setLocation(730, 210);
        lblCorGerEst.setLocation(1030, 60);
        lblMatGerEst.setLocation(1030, 110);
        lblLocGerEst.setLocation(1030, 160);
        lblDetGerEst.setLocation(1030, 210);

        txtTipGerEst.setText(rbtnPelGerEst.getText());
        txtBusGerEst.setEnabled(true);
        lblBusGerEst.setEnabled(true);
        sepBusGerEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnPelGerEstActionPerformed

    private void rbtnChiGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnChiGerEstActionPerformed
        if (txtBusGerEst.getText().equals("")) {

            btnBusGerEst.setEnabled(false);
        } else {
            btnBusGerEst.setEnabled(true);
        }

        txtBusGerEst.setEnabled(false);
        lblBusGerEst.setEnabled(false);
        sepBusGerEst.setForeground(Color.GRAY);

        txtModGerEst.setEnabled(false);
        lblModGerEst.setEnabled(false);
        sepModGerEst.setForeground(Color.GRAY);

        txtMarGerEst.setEnabled(false);
        lblMarGerEst.setEnabled(false);
        sepMarGerEst.setForeground(Color.GRAY);

        txtCorGerEst.setEnabled(false);
        lblCorGerEst.setEnabled(false);
        sepCorGerEst.setForeground(Color.GRAY);

        txtMatGerEst.setEnabled(false);
        lblMatGerEst.setEnabled(false);
        sepMatGerEst.setForeground(Color.GRAY);

        txtQuaGerEst.setEnabled(false);
        lblQuaGerEst.setEnabled(false);
        sepQuaGerEst.setForeground(Color.GRAY);

        txtPreGerEst.setEnabled(false);
        lblPreGerEst.setEnabled(false);
        sepPreGerEst.setForeground(Color.GRAY);

        txtLocGerEst.setEnabled(false);
        lblLocGerEst.setEnabled(false);
        sepLocGerEst.setForeground(Color.GRAY);

        txtDetGerEst.setEnabled(false);
        lblDetGerEst.setEnabled(false);
        sepDetGerEst.setForeground(Color.GRAY);

        lblChiGerEst.setEnabled(false);
        cmbChiGerEst.setEnabled(false);
        cmbChiGerEst.setSelectedItem("Triplo 4G HLR 230");

        btnAltGerEst.setEnabled(false);
        btnExcGerEst.setEnabled(false);

        lblR$GerEst.setVisible(false);

        tblGerEst.setVisible(false);
        scrGerEst.setVisible(false);

        txtMarGerEst.setText(null);
        txtModGerEst.setText(null);
        txtQuaGerEst.setText(null);
        txtPreGerEst.setText(null);
        txtCorGerEst.setText(null);
        txtMatGerEst.setText(null);
        txtLocGerEst.setText(null);
        txtDetGerEst.setText(null);

        lblMarGerEst.setLocation(730, 60);
        lblModGerEst.setLocation(730, 110);
        lblQuaGerEst.setLocation(730, 160);
        lblPreGerEst.setLocation(730, 210);
        lblCorGerEst.setLocation(1030, 60);
        lblMatGerEst.setLocation(1030, 110);
        lblLocGerEst.setLocation(1030, 160);
        lblDetGerEst.setLocation(1030, 210);

        txtTipGerEst.setText(rbtnChiGerEst.getText());
        txtBusGerEst.setEnabled(true);
        lblBusGerEst.setEnabled(true);
        sepBusGerEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnChiGerEstActionPerformed

    private void rbtnAceGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAceGerEstActionPerformed
        if (txtBusGerEst.getText().equals("")) {

            btnBusGerEst.setEnabled(false);
        } else {
            btnBusGerEst.setEnabled(true);
        }

        txtBusGerEst.setEnabled(false);
        lblBusGerEst.setEnabled(false);
        sepBusGerEst.setForeground(Color.GRAY);

        txtModGerEst.setEnabled(false);
        lblModGerEst.setEnabled(false);
        sepModGerEst.setForeground(Color.GRAY);

        txtMarGerEst.setEnabled(false);
        lblMarGerEst.setEnabled(false);
        sepMarGerEst.setForeground(Color.GRAY);

        txtCorGerEst.setEnabled(false);
        lblCorGerEst.setEnabled(false);
        sepCorGerEst.setForeground(Color.GRAY);

        txtMatGerEst.setEnabled(false);
        lblMatGerEst.setEnabled(false);
        sepMatGerEst.setForeground(Color.GRAY);

        txtQuaGerEst.setEnabled(false);
        lblQuaGerEst.setEnabled(false);
        sepQuaGerEst.setForeground(Color.GRAY);

        txtPreGerEst.setEnabled(false);
        lblPreGerEst.setEnabled(false);
        sepPreGerEst.setForeground(Color.GRAY);

        txtLocGerEst.setEnabled(false);
        lblLocGerEst.setEnabled(false);
        sepLocGerEst.setForeground(Color.GRAY);

        txtDetGerEst.setEnabled(false);
        lblDetGerEst.setEnabled(false);
        sepDetGerEst.setForeground(Color.GRAY);

        lblChiGerEst.setEnabled(false);
        cmbChiGerEst.setEnabled(false);
        cmbChiGerEst.setSelectedItem("Triplo 4G HLR 230");

        btnAltGerEst.setEnabled(false);
        btnExcGerEst.setEnabled(false);

        lblR$GerEst.setVisible(false);

        tblGerEst.setVisible(false);
        scrGerEst.setVisible(false);

        txtMarGerEst.setText(null);
        txtModGerEst.setText(null);
        txtQuaGerEst.setText(null);
        txtPreGerEst.setText(null);
        txtCorGerEst.setText(null);
        txtMatGerEst.setText(null);
        txtLocGerEst.setText(null);
        txtDetGerEst.setText(null);

        lblMarGerEst.setLocation(730, 60);
        lblModGerEst.setLocation(730, 110);
        lblQuaGerEst.setLocation(730, 160);
        lblPreGerEst.setLocation(730, 210);
        lblCorGerEst.setLocation(1030, 60);
        lblMatGerEst.setLocation(1030, 110);
        lblLocGerEst.setLocation(1030, 160);
        lblDetGerEst.setLocation(1030, 210);

        txtTipGerEst.setText(rbtnAceGerEst.getText());
        txtBusGerEst.setEnabled(true);
        lblBusGerEst.setEnabled(true);
        sepBusGerEst.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnAceGerEstActionPerformed

    private void txtModGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModGerEstFocusGained
        if (txtModGerEst.getText().isEmpty()) {
            anitxtin(lblModGerEst);
        }
    }//GEN-LAST:event_txtModGerEstFocusGained

    private void txtModGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModGerEstFocusLost
        if (txtModGerEst.getText().isEmpty()) {
            anitxtout(lblModGerEst);
        }
    }//GEN-LAST:event_txtModGerEstFocusLost

    private void txtMarGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarGerEstFocusGained
        if (txtMarGerEst.getText().isEmpty()) {
            anitxtin(lblMarGerEst);
        }
    }//GEN-LAST:event_txtMarGerEstFocusGained

    private void txtMarGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarGerEstFocusLost
        if (txtMarGerEst.getText().isEmpty()) {
            anitxtout(lblMarGerEst);
        }
    }//GEN-LAST:event_txtMarGerEstFocusLost

    private void txtCorGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorGerEstFocusGained
        if (txtCorGerEst.getText().isEmpty()) {
            anitxtin(lblCorGerEst);
        }
    }//GEN-LAST:event_txtCorGerEstFocusGained

    private void txtCorGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCorGerEstFocusLost
        if (txtCorGerEst.getText().isEmpty()) {
            anitxtout(lblCorGerEst);
        }
    }//GEN-LAST:event_txtCorGerEstFocusLost

    private void txtMatGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMatGerEstFocusGained
        if (txtMatGerEst.getText().isEmpty()) {
            anitxtin(lblMatGerEst);
        }
    }//GEN-LAST:event_txtMatGerEstFocusGained

    private void txtMatGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMatGerEstFocusLost
        if (txtMatGerEst.getText().isEmpty()) {
            anitxtout(lblMatGerEst);
        }
    }//GEN-LAST:event_txtMatGerEstFocusLost

    private void txtQuaGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuaGerEstFocusGained
        if (txtQuaGerEst.getText().isEmpty()) {
            anitxtin(lblQuaGerEst);
        }
    }//GEN-LAST:event_txtQuaGerEstFocusGained

    private void txtQuaGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuaGerEstFocusLost
        if (txtQuaGerEst.getText().isEmpty()) {
            anitxtout(lblQuaGerEst);
        }
    }//GEN-LAST:event_txtQuaGerEstFocusLost

    private void txtQuaGerEstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuaGerEstKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtQuaGerEstKeyTyped

    private void txtPreGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreGerEstFocusGained
        if (txtPreGerEst.getText().isEmpty()) {
            anitxtin(lblPreGerEst);
            lblR$GerEst.setVisible(true);
        }
    }//GEN-LAST:event_txtPreGerEstFocusGained

    private void txtPreGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreGerEstFocusLost
        if (txtPreGerEst.getText().isEmpty()) {
            anitxtout(lblPreGerEst);
            lblR$GerEst.setVisible(false);
        }
    }//GEN-LAST:event_txtPreGerEstFocusLost

    private void txtPreGerEstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreGerEstKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtPreGerEst.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPreGerEstKeyTyped

    private void txtLocGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLocGerEstFocusGained
        if (txtLocGerEst.getText().isEmpty()) {
            anitxtin(lblLocGerEst);
        }
    }//GEN-LAST:event_txtLocGerEstFocusGained

    private void txtLocGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLocGerEstFocusLost
        if (txtLocGerEst.getText().isEmpty()) {
            anitxtout(lblLocGerEst);
        }
    }//GEN-LAST:event_txtLocGerEstFocusLost

    private void txtDetGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetGerEstFocusGained
        if (txtDetGerEst.getText().isEmpty()) {
            anitxtin(lblDetGerEst);
        }
    }//GEN-LAST:event_txtDetGerEstFocusGained

    private void txtDetGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetGerEstFocusLost
        if (txtDetGerEst.getText().isEmpty()) {
            anitxtout(lblDetGerEst);
        }
    }//GEN-LAST:event_txtDetGerEstFocusLost

    private void txtBusGerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusGerEstFocusGained
        if (txtBusGerEst.getText().isEmpty()) {
            anitxtin(lblBusGerEst);
        }
    }//GEN-LAST:event_txtBusGerEstFocusGained

    private void txtBusGerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusGerEstFocusLost
        if (txtBusGerEst.getText().isEmpty()) {
            anitxtout(lblBusGerEst);
        }
    }//GEN-LAST:event_txtBusGerEstFocusLost

    private void btnBusGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusGerEstActionPerformed
        estoque es = new estoque();

        es.setModelo(txtBusGerEst.getText());
        es.setTipoproduto(txtTipGerEst.getText());

        if (tabelaestoquegerenciar(es)) {

            scrGerEst.setVisible(true);
            tblGerEst.setVisible(true);

        } else {

            scrGerEst.setVisible(false);
            tblGerEst.setVisible(false);

            JOptionPane.showMessageDialog(pnlGerEst, "Nenhum dado encontrado!", "Gerenciar", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_btnBusGerEstActionPerformed

    private void btnExcGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerEstActionPerformed
        int resp = JOptionPane.showOptionDialog(pnlGerEst, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                estoque es = new estoque();
                estoqueDAO esdao = new estoqueDAO();

                es.setId(Integer.parseInt(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 0).toString()));

                esdao.excluir(es);

                JOptionPane.showMessageDialog(pnlGerEst, "Excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                pnlGerEst.setVisible(false);
                lblTitPri.setVisible(false);

            } catch (SQLException ex) {
                Logger.getLogger(main.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btnExcGerEstActionPerformed

    private void txtBusGerEstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusGerEstKeyTyped
        btnBusGerEst.setEnabled(true);
    }//GEN-LAST:event_txtBusGerEstKeyTyped

    private void tblGerEstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGerEstMouseClicked
        lblMarGerEst.setLocation(730, 60);
        lblModGerEst.setLocation(730, 110);
        lblQuaGerEst.setLocation(730, 160);
        lblPreGerEst.setLocation(730, 210);
        lblCorGerEst.setLocation(1030, 60);
        lblMatGerEst.setLocation(1030, 110);
        lblLocGerEst.setLocation(1030, 160);
        lblDetGerEst.setLocation(1030, 210);

        txtModGerEst.setEnabled(false);
        lblModGerEst.setEnabled(false);
        sepModGerEst.setForeground(Color.GRAY);

        txtMarGerEst.setEnabled(false);
        lblMarGerEst.setEnabled(false);
        sepMarGerEst.setForeground(Color.GRAY);

        txtCorGerEst.setEnabled(false);
        lblCorGerEst.setEnabled(false);
        sepCorGerEst.setForeground(Color.GRAY);

        txtMatGerEst.setEnabled(false);
        lblMatGerEst.setEnabled(false);
        sepMatGerEst.setForeground(Color.GRAY);

        txtQuaGerEst.setEnabled(false);
        lblQuaGerEst.setEnabled(false);
        sepQuaGerEst.setForeground(Color.GRAY);

        txtPreGerEst.setEnabled(false);
        lblPreGerEst.setEnabled(false);
        sepPreGerEst.setForeground(Color.GRAY);

        txtLocGerEst.setEnabled(false);
        lblLocGerEst.setEnabled(false);
        sepLocGerEst.setForeground(Color.GRAY);

        txtDetGerEst.setEnabled(false);
        lblDetGerEst.setEnabled(false);
        sepDetGerEst.setForeground(Color.GRAY);

        lblChiGerEst.setEnabled(false);
        cmbChiGerEst.setEnabled(false);

        btnAltGerEst.setEnabled(false);
        btnExcGerEst.setEnabled(false);

        lblR$GerEst.setVisible(true);

        txtMarGerEst.setText(null);
        txtModGerEst.setText(null);
        txtQuaGerEst.setText(null);
        txtPreGerEst.setText(null);
        txtCorGerEst.setText(null);
        txtMatGerEst.setText(null);
        txtLocGerEst.setText(null);
        txtDetGerEst.setText(null);

        if (txtTipGerEst.getText().equals("Capinha")) {

            txtModGerEst.setEnabled(true);
            lblModGerEst.setEnabled(true);
            sepModGerEst.setForeground(corforeazul);

            txtMarGerEst.setEnabled(true);
            lblMarGerEst.setEnabled(true);
            sepMarGerEst.setForeground(corforeazul);

            txtCorGerEst.setEnabled(true);
            lblCorGerEst.setEnabled(true);
            sepCorGerEst.setForeground(corforeazul);

            txtQuaGerEst.setEnabled(true);
            lblQuaGerEst.setEnabled(true);
            sepQuaGerEst.setForeground(corforeazul);

            txtPreGerEst.setEnabled(true);
            lblPreGerEst.setEnabled(true);
            sepPreGerEst.setForeground(corforeazul);

            txtLocGerEst.setEnabled(true);
            lblLocGerEst.setEnabled(true);
            sepLocGerEst.setForeground(corforeazul);

            txtDetGerEst.setEnabled(true);
            lblDetGerEst.setEnabled(true);
            sepDetGerEst.setForeground(corforeazul);

            if (tblGerEst.getColumnCount() == 9) {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtDetGerEst.setText((!"Sem Detalhes".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");
                txtCorGerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString() : "");

            } else {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtCorGerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");

            }

        } else if (txtTipGerEst.getText().equals("Chip")) {

            txtPreGerEst.setEnabled(true);
            lblPreGerEst.setEnabled(true);
            sepPreGerEst.setForeground(corforeazul);

            txtQuaGerEst.setEnabled(true);
            lblQuaGerEst.setEnabled(true);
            sepQuaGerEst.setForeground(corforeazul);

            cmbChiGerEst.setEnabled(true);
            lblChiGerEst.setEnabled(true);

            txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString().length()));
            txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());

            if (tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().equals("Triplo 4G HLR 230")) {

                cmbChiGerEst.setSelectedItem("Triplo 4G HLR 230");

            } else if (tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().equals("eSIM")) {

                cmbChiGerEst.setSelectedItem("eSIM");

            } else {

                cmbChiGerEst.setSelectedItem("Naked");

            }

        } else {

            txtModGerEst.setEnabled(true);
            lblModGerEst.setEnabled(true);
            sepModGerEst.setForeground(corforeazul);

            txtMarGerEst.setEnabled(true);
            lblMarGerEst.setEnabled(true);
            sepMarGerEst.setForeground(corforeazul);

            txtCorGerEst.setEnabled(true);
            lblCorGerEst.setEnabled(true);
            sepCorGerEst.setForeground(corforeazul);

            txtQuaGerEst.setEnabled(true);
            lblQuaGerEst.setEnabled(true);
            sepQuaGerEst.setForeground(corforeazul);

            txtPreGerEst.setEnabled(true);
            lblPreGerEst.setEnabled(true);
            sepPreGerEst.setForeground(corforeazul);

            txtLocGerEst.setEnabled(true);
            lblLocGerEst.setEnabled(true);
            sepLocGerEst.setForeground(corforeazul);

            txtDetGerEst.setEnabled(true);
            lblDetGerEst.setEnabled(true);
            sepDetGerEst.setForeground(corforeazul);

            txtMatGerEst.setEnabled(true);
            lblMatGerEst.setEnabled(true);
            sepMatGerEst.setForeground(corforeazul);

            if (tblGerEst.getColumnCount() == 10) {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtDetGerEst.setText((!"Sem Detalhes".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");
                txtCorGerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString() : "");
                txtMatGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 9).toString());

            } else {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtCorGerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");
                txtMatGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString());

            }

        }

        if (!txtModGerEst.getText().isEmpty()) {
            anitxtin(lblModGerEst);
        }
        if (!txtMarGerEst.getText().isEmpty()) {
            anitxtin(lblMarGerEst);
        }
        if (!txtCorGerEst.getText().isEmpty()) {
            anitxtin(lblCorGerEst);
        }
        if (!txtQuaGerEst.getText().isEmpty()) {
            anitxtin(lblQuaGerEst);
        }
        if (!txtPreGerEst.getText().isEmpty()) {
            anitxtin(lblPreGerEst);
        }
        if (!txtLocGerEst.getText().isEmpty()) {
            anitxtin(lblLocGerEst);
        }
        if (!txtDetGerEst.getText().isEmpty()) {
            anitxtin(lblDetGerEst);
        }
        if (!txtMatGerEst.getText().isEmpty()) {
            anitxtin(lblMatGerEst);
        }

        btnExcGerEst.setEnabled(true);
        btnAltGerEst.setEnabled(true);
    }//GEN-LAST:event_tblGerEstMouseClicked

    private void btnSalCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalCadEntActionPerformed
        if (!(rbtnVenCadEnt.isSelected() && tblSelIteCadEnt.getRowCount() == 0)) {

            if (!(cmbSerCadEnt.getSelectedIndex() == 0 && (rbtnAssCadEnt.isSelected() || rbtnSerCadEnt.isSelected()))) {

                try {

                    int idser = 0;

                    if (cmbSerCadEnt.getSelectedIndex() != 0) {
                        itens selectedItem = (itens) cmbSerCadEnt.getSelectedItem();
                        idser = selectedItem.getId();
                    }

                    if (tblSelIteCadEnt.getRowCount() != 0) {

                        for (int i = 1; i <= tblSelIteCadEnt.getRowCount(); i++) {

                            entrada en = new entrada();
                            entradaDAO endao = new entradaDAO();
                            estoque es = new estoque();

                            en.setCodigo(txtCodCadEnt.getText());
                            en.setData(formatterbanco.format(((formatter.parse(txtDatCadEnt.getText())))));
                            en.setPreco(Double.valueOf(txtPreCadEnt.getText().replace(".", "").replace(",", ".")));
                            en.setDetalhes(txtDetCadEnt.getText());

                            if (rbtnSerCadEnt.isSelected()) {

                                en.setIdtiposervico(idser);
                                en.setIdestoque(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 1).toString()));
                                en.setQuantidade(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 0).toString()));

                                endao.inserir(en, 1);

                            } else if (rbtnVenCadEnt.isSelected()) {

                                en.setIdestoque(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 1).toString()));
                                en.setQuantidade(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 0).toString()));

                                endao.inserir(en, 2);
                            }

                        }

                    } else {

                        entrada en = new entrada();
                        entradaDAO endao = new entradaDAO();

                        en.setCodigo(txtCodCadEnt.getText());
                        en.setData(formatterbanco.format((formatter.parse(txtDatCadEnt.getText()))));
                        en.setPreco(Double.valueOf(txtPreCadEnt.getText().replace(".", "").replace(",", ".")));
                        en.setDetalhes(txtDetCadEnt.getText());
                        en.setIdtiposervico(idser);
                        en.setIdestoque(1);

                        if (rbtnSerCadEnt.isSelected()) {

                            endao.inserir(en, 1);

                        } else if (rbtnAssCadEnt.isSelected()) {

                            endao.inserir(en, 3);

                        }

                    }

                    JOptionPane.showMessageDialog(pnlCadEnt, "Entrada feita com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    pnlCadEnt.setVisible(false);

                } catch (SQLException | ParseException ex) {
                    Logger.getLogger(main.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(pnlCadEnt, "Selecione o serviço!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            }

            lblTitPri.setVisible(false);

        } else {

            JOptionPane.showMessageDialog(pnlCadEnt, "Selecione os ítem do estoque!", "Atenção", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_btnSalCadEntActionPerformed

    private void btnCanCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanCadEntActionPerformed
        if (btnSalCadEnt.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(pnlCadEnt, "Cancelar entrada? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                try {

                    entrada en = new entrada();
                    entradaDAO endao = new entradaDAO();

                    if (tblSelIteCadEnt.getRowCount() != 0) {

                        for (int i = 1; i <= tblSelIteCadEnt.getRowCount(); i++) {

                            en.setQuantidade(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 0).toString()));
                            en.setIdestoque(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 1).toString()));

                            endao.atualizarestoque(en, 1);

                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }

                pnlCadEnt.setVisible(false);
                pnlIteCadEnt.setVisible(false);
                lblTitPri.setVisible(false);

            }

        } else {

            pnlCadEnt.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            lblTitPri.setVisible(false);

        }
    }//GEN-LAST:event_btnCanCadEntActionPerformed

    private void rbtnSerCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSerCadEntActionPerformed
        lblDatCadEnt.setEnabled(true);
        txtDatCadEnt.setEnabled(true);
        sepDatCadEnt.setForeground(corforeazul);
        lblPreCadEnt.setEnabled(true);
        txtPreCadEnt.setEnabled(true);
        sepPreCadEnt.setForeground(corforeazul);
        lblDetCadEnt.setEnabled(true);
        txtDetCadEnt.setEnabled(true);
        sepDetCadEnt.setForeground(corforeazul);
        lblSerCadEnt.setEnabled(true);
        cmbSerCadEnt.setEnabled(true);
        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(true);

        comboboxentrada(cmbSerCadEnt, 1);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(480, 90);
        anitxtin(lblDatCadEnt);
    }//GEN-LAST:event_rbtnSerCadEntActionPerformed

    private void rbtnVenCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnVenCadEntActionPerformed
        lblDatCadEnt.setEnabled(true);
        txtDatCadEnt.setEnabled(true);
        sepDatCadEnt.setForeground(corforeazul);
        lblPreCadEnt.setEnabled(true);
        txtPreCadEnt.setEnabled(true);
        sepPreCadEnt.setForeground(corforeazul);
        lblDetCadEnt.setEnabled(true);
        txtDetCadEnt.setEnabled(true);
        sepDetCadEnt.setForeground(corforeazul);
        lblSerCadEnt.setEnabled(false);
        cmbSerCadEnt.setEnabled(false);
        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(true);

        comboboxentrada(cmbSerCadEnt, 1);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(480, 90);
        anitxtin(lblDatCadEnt);
    }//GEN-LAST:event_rbtnVenCadEntActionPerformed

    private void rbtnAssCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAssCadEntActionPerformed
        lblDatCadEnt.setEnabled(true);
        txtDatCadEnt.setEnabled(true);
        sepDatCadEnt.setForeground(corforeazul);
        lblPreCadEnt.setEnabled(true);
        txtPreCadEnt.setEnabled(true);
        sepPreCadEnt.setForeground(corforeazul);
        lblDetCadEnt.setEnabled(true);
        txtDetCadEnt.setEnabled(true);
        sepDetCadEnt.setForeground(corforeazul);
        lblSerCadEnt.setEnabled(true);
        cmbSerCadEnt.setEnabled(true);
        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(false);

        comboboxentrada(cmbSerCadEnt, 2);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(480, 90);
        anitxtin(lblDatCadEnt);
    }//GEN-LAST:event_rbtnAssCadEntActionPerformed

    private void txtDatCadEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatCadEntFocusGained
        if (txtDatCadEnt.getText().isEmpty()) {
            anitxtin(lblDatCadEnt);
        }
    }//GEN-LAST:event_txtDatCadEntFocusGained

    private void txtDatCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatCadEntFocusLost
        if (txtDatCadEnt.getText().isEmpty()) {
            anitxtout(lblDatCadEnt);
        }
    }//GEN-LAST:event_txtDatCadEntFocusLost

    private void txtPreCadEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreCadEntFocusGained
        if (txtPreCadEnt.getText().isEmpty()) {
            anitxtin(lblPreCadEnt);
            lblR$CadEnt.setVisible(true);
        }
    }//GEN-LAST:event_txtPreCadEntFocusGained

    private void txtPreCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreCadEntFocusLost
        if (txtPreCadEnt.getText().isEmpty()) {
            anitxtout(lblPreCadEnt);
            lblR$CadEnt.setVisible(false);

        }
    }//GEN-LAST:event_txtPreCadEntFocusLost

    private void txtPreCadEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreCadEntKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtPreCadEnt.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPreCadEntKeyTyped

    private void txtDetCadEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetCadEntFocusGained
        if (txtDetCadEnt.getText().isEmpty()) {
            anitxtin(lblDetCadEnt);
        }
    }//GEN-LAST:event_txtDetCadEntFocusGained

    private void txtDetCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetCadEntFocusLost
        if (txtDetCadEnt.getText().isEmpty()) {
            anitxtout(lblDetCadEnt);
        }
    }//GEN-LAST:event_txtDetCadEntFocusLost

    private void tblSelIteCadEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelIteCadEntMouseClicked
        try {
            String idt = (tblSelIteCadEnt.getValueAt(tblSelIteCadEnt.getSelectedRow(), 1).toString());
            String qua = (tblSelIteCadEnt.getValueAt(tblSelIteCadEnt.getSelectedRow(), 0).toString());

            entrada en = new entrada();
            entradaDAO endao = new entradaDAO();

            en.setIdestoque(Integer.parseInt(idt));
            en.setQuantidade(Integer.parseInt(qua));

            endao.atualizarestoque(en, 1);

            DefaultTableModel modelo = (DefaultTableModel) tblSelIteCadEnt.getModel();
            modelo.removeRow(tblSelIteCadEnt.getSelectedRow());

            if (tblSelIteCadEnt.getRowCount() == 0) {

                tblSelIteCadEnt.setVisible(false);
                scrSelIteCadEnt.setVisible(false);
                lblSelIteCadEnt.setForeground(new Color(246, 246, 246));

            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblSelIteCadEntMouseClicked

    private void tblEstIteCadEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEstIteCadEntMouseClicked
        boolean existe = false;

        int sel = Integer.parseInt(tblEstIteCadEnt.getValueAt(tblEstIteCadEnt.getSelectedRow(), 0).toString());

        for (int i = 1; i <= tblSelIteCadEnt.getRowCount(); i++) {

            if (sel == Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 1).toString())) {

                existe = true;

            }

        }

        if (existe) {

            JOptionPane.showMessageDialog(pnlIteCadEnt, "Ítem já adicionado!", "Entrada", JOptionPane.ERROR_MESSAGE);

        } else {

            String opc = JOptionPane.showInputDialog(null, "Informe a quantidade deste ítem", "Entrada", JOptionPane.QUESTION_MESSAGE);

            if (opc != null) {

                try {

                    int i = Integer.parseInt(opc);

                    if (rbtnChiIteCadEnt.isSelected()) {

                        if (Integer.parseInt(tblEstIteCadEnt.getValueAt(tblEstIteCadEnt.getSelectedRow(), 3).toString()) < i) {

                            JOptionPane.showMessageDialog(pnlIteCadEnt, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteCadEnt, tblSelIteCadEnt, opc, rbtnChiIteCadEnt);

                            lblSelIteCadEnt.setForeground(corforeazul);
                            scrSelIteCadEnt.setVisible(true);
                            tblSelIteCadEnt.setVisible(true);

                        }

                    } else {

                        if (Integer.parseInt(tblEstIteCadEnt.getValueAt(tblEstIteCadEnt.getSelectedRow(), 5).toString()) < i) {

                            JOptionPane.showMessageDialog(pnlIteCadEnt, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteCadEnt, tblSelIteCadEnt, opc, rbtnChiIteCadEnt);

                            lblSelIteCadEnt.setForeground(corforeazul);
                            scrSelIteCadEnt.setVisible(true);
                            tblSelIteCadEnt.setVisible(true);

                        }

                    }

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(pnlIteCadEnt, "Digite apenas número!", "Erro", JOptionPane.ERROR_MESSAGE);

                }

            }

        }

        tblEstIteCadEnt.clearSelection();
    }//GEN-LAST:event_tblEstIteCadEntMouseClicked

    private void btnCadEntMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEntMouseEntered
        btnCadEnt.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadEntMouseEntered

    private void btnCadEntMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEntMouseExited
        btnCadEnt.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadEntMouseExited

    private void btnCadEntMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEntMouseReleased
        if (!(pnlCadEnt.isVisible() || pnlIteCadEnt.isVisible())) {

            lblTitPri.setText("Nova Entrada");
            lblTitPri.setVisible(true);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 3; i++) {
                char letra = (char) (Math.random() * 26 + 'A');
                sb.append(letra);
            }

            for (int i = 0; i < 3; i++) {
                int numero = (int) (Math.random() * 10);
                sb.append(numero);
            }

            txtCodCadEnt.setText(sb.toString());

            lblDetCadEnt.setLocation(480, 190);

            lblPreCadEnt.setLocation(480, 140);
            lblDatCadEnt.setLocation(480, 90);

            lblDatCadEnt.setEnabled(false);
            txtDatCadEnt.setEnabled(false);
            txtDatCadEnt.setText(null);
            sepDatCadEnt.setForeground(Color.GRAY);
            lblPreCadEnt.setEnabled(false);

            txtPreCadEnt.setEnabled(false);
            txtPreCadEnt.setText(null);

            sepPreCadEnt.setForeground(Color.GRAY);
            lblDetCadEnt.setEnabled(false);

            txtDetCadEnt.setEnabled(false);
            txtDetCadEnt.setText(null);

            sepDetCadEnt.setForeground(Color.GRAY);
            lblSerCadEnt.setEnabled(false);
            cmbSerCadEnt.setEnabled(false);
            cmbSerCadEnt.setSelectedIndex(0);
            lblR$CadEnt.setVisible(false);

            btnCadEnt.setVisible(false);
            btnGerEnt.setVisible(false);

            btnGroup.clearSelection();
            btnGroup1.clearSelection();

            lblEstIteCadEnt.setForeground(new Color(246, 246, 246));
            lblSelIteCadEnt.setForeground(new Color(246, 246, 246));
            tblEstIteCadEnt.setVisible(false);
            tblSelIteCadEnt.setVisible(false);
            DefaultTableModel model = (DefaultTableModel) tblSelIteCadEnt.getModel();
            DefaultTableModel model1 = (DefaultTableModel) tblEstIteCadEnt.getModel();
            model.setRowCount(0);
            model1.setRowCount(0);
            scrEstIteCadEnt.setVisible(false);
            scrSelIteCadEnt.setVisible(false);

            txtBusIteCadEnt.setEnabled(false);
            txtBusIteCadEnt.setText(null);
            lblBusIteCadEnt.setEnabled(false);
            sepBusIteCadEnt.setForeground(Color.GRAY);

            btnIteCadEnt.setEnabled(false);
            btnSalCadEnt.setEnabled(false);
            cmbSerCadEnt.setEnabled(false);

            pnlCadEnt.setVisible(true);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlMas.setVisible(false);
            pnlDes.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlGerEnt.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

        }
    }//GEN-LAST:event_btnCadEntMouseReleased

    private void txtBusIteCadEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusIteCadEntFocusGained
        if (txtBusIteCadEnt.getText().isEmpty()) {
            anitxtin(lblBusIteCadEnt);
        }
    }//GEN-LAST:event_txtBusIteCadEntFocusGained

    private void txtBusIteCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusIteCadEntFocusLost
        if (txtBusIteCadEnt.getText().isEmpty()) {
            anitxtout(lblBusIteCadEnt);
        }
    }//GEN-LAST:event_txtBusIteCadEntFocusLost

    private void rbtnAssIteCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAssIteCadEntActionPerformed
        txtBusIteCadEnt.setEnabled(true);
        lblBusIteCadEnt.setEnabled(true);
        sepBusIteCadEnt.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnAssIteCadEntActionPerformed

    private void rbtnPelIteCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPelIteCadEntActionPerformed
        txtBusIteCadEnt.setEnabled(true);
        lblBusIteCadEnt.setEnabled(true);
        sepBusIteCadEnt.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnPelIteCadEntActionPerformed

    private void rbtnCapIteCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCapIteCadEntActionPerformed
        txtBusIteCadEnt.setEnabled(true);
        lblBusIteCadEnt.setEnabled(true);
        sepBusIteCadEnt.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnCapIteCadEntActionPerformed

    private void rbtnChiIteCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnChiIteCadEntActionPerformed
        txtBusIteCadEnt.setEnabled(true);
        lblBusIteCadEnt.setEnabled(true);
        sepBusIteCadEnt.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnChiIteCadEntActionPerformed

    private void btnIteCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIteCadEntActionPerformed
        pnlIteCadEnt.setVisible(true);
        pnlCadEnt.setVisible(false);

        if (!txtBusIteCadEnt.getText().isEmpty()) {
            anitxtin(lblBusIteCadEnt);
        }
    }//GEN-LAST:event_btnIteCadEntActionPerformed

    private void btnVolIteCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolIteCadEntActionPerformed
        pnlIteCadEnt.setVisible(false);
        pnlCadEnt.setVisible(true);
    }//GEN-LAST:event_btnVolIteCadEntActionPerformed

    private void txtDatCadEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatCadEntKeyTyped
        if (txtDatCadEnt.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatCadEnt.getText().length() == 2) {

                    txtDatCadEnt.setText(txtDatCadEnt.getText() + "/");
                    txtDatCadEnt.setCaretPosition(3);

                } else if (txtDatCadEnt.getText().length() == 5) {

                    txtDatCadEnt.setText(txtDatCadEnt.getText() + "/");
                    txtDatCadEnt.setCaretPosition(6);

                }

            }
            if (txtDatCadEnt.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtDatCadEntKeyTyped

    private void btnEntPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntPriMouseReleased
        if (!btnCadEnt.isVisible()) {
            btnCadEnt.setVisible(true);
            btnGerEnt.setVisible(true);
        } else {
            btnCadEnt.setVisible(false);
            btnGerEnt.setVisible(false);
        }
    }//GEN-LAST:event_btnEntPriMouseReleased

    private void rbtnOutTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnOutTipSerActionPerformed
        txtDesTipSer.setEnabled(true);
        lblDesTipSer.setEnabled(true);
        sepDesTipSer.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnOutTipSerActionPerformed

    private void txtBusIteCadEntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusIteCadEntKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            estoque es = new estoque();

            if (rbtnCapIteCadEnt.isSelected()) {
                es.setTipoproduto("Capinha");
            } else if (rbtnPelIteCadEnt.isSelected()) {
                es.setTipoproduto("Película");
            } else if (rbtnChiIteCadEnt.isSelected()) {
                es.setTipoproduto("Chip");
            } else {
                es.setTipoproduto("Acessório");
            }

            es.setModelo(txtBusIteCadEnt.getText());

            if (tabelaestoqueconsulta(es, tblEstIteCadEnt, scrEstIteCadEnt)) {

                tblEstIteCadEnt.setVisible(true);
                scrEstIteCadEnt.setVisible(true);
                lblEstIteCadEnt.setForeground(corforeazul);

            } else {

                JOptionPane.showMessageDialog(pnlIteCadEnt, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

            }

            tblEstIteCadEnt.getTableHeader().setFont(fontbold(11));

        }
    }//GEN-LAST:event_txtBusIteCadEntKeyPressed

    private void cmbSerCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSerCadEntActionPerformed

    }//GEN-LAST:event_cmbSerCadEntActionPerformed

    private void btnVolRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolRelActionPerformed
        pnlRel.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnVolRelActionPerformed

    private void rbtnSerRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSerRelActionPerformed
        rbtnSerRel.grabFocus();

        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontbold(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));
        tabelarelatorio(tblRel, scrRel, 2, 1, null, null);
        lblDatIniRel.setLocation(290, 150);
        lblDatFinRel.setLocation(430, 150);
    }//GEN-LAST:event_rbtnSerRelActionPerformed

    private void rbtnVenRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnVenRelActionPerformed
        rbtnVenRel.grabFocus();

        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontbold(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));
        tabelarelatorio(tblRel, scrRel, 3, 1, null, null);
        lblDatIniRel.setLocation(290, 150);
        lblDatFinRel.setLocation(430, 150);
    }//GEN-LAST:event_rbtnVenRelActionPerformed

    private void rbtnTodRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnTodRelActionPerformed
        rbtnTodRel.grabFocus();

        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontbold(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));
        tabelarelatorio(tblRel, scrRel, 1, 1, null, null);
        lblDatIniRel.setLocation(290, 150);
        lblDatFinRel.setLocation(430, 150);
    }//GEN-LAST:event_rbtnTodRelActionPerformed

    private void txtDatIniRelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatIniRelFocusGained
        if (txtDatIniRel.getText().isEmpty()) {
            anitxtin(lblDatIniRel);
        }
    }//GEN-LAST:event_txtDatIniRelFocusGained

    private void txtDatIniRelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatIniRelFocusLost
        if (txtDatIniRel.getText().isEmpty()) {
            anitxtout(lblDatIniRel);
        }
    }//GEN-LAST:event_txtDatIniRelFocusLost

    private void txtDatIniRelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatIniRelKeyTyped
        if (txtDatIniRel.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatIniRel.getText().length() == 2) {

                    txtDatIniRel.setText(txtDatIniRel.getText() + "/");
                    txtDatIniRel.setCaretPosition(3);

                } else if (txtDatIniRel.getText().length() == 5) {

                    txtDatIniRel.setText(txtDatIniRel.getText() + "/");
                    txtDatIniRel.setCaretPosition(6);

                }

            }
            if (txtDatIniRel.getText().length() > 9) {
                evt.consume();
                txtDatFinRel.grabFocus();
            }
        }
    }//GEN-LAST:event_txtDatIniRelKeyTyped

    private void txtDatFinRelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatFinRelFocusGained
        if (txtDatFinRel.getText().isEmpty()) {
            anitxtin(lblDatFinRel);
        }
    }//GEN-LAST:event_txtDatFinRelFocusGained

    private void txtDatFinRelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatFinRelFocusLost
        if (txtDatFinRel.getText().isEmpty()) {
            anitxtout(lblDatFinRel);
        }
    }//GEN-LAST:event_txtDatFinRelFocusLost

    private void txtDatFinRelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatFinRelKeyTyped
        if (txtDatFinRel.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatFinRel.getText().length() == 2) {

                    txtDatFinRel.setText(txtDatFinRel.getText() + "/");
                    txtDatFinRel.setCaretPosition(3);

                } else if (txtDatFinRel.getText().length() == 5) {

                    txtDatFinRel.setText(txtDatFinRel.getText() + "/");
                    txtDatFinRel.setCaretPosition(6);

                }

            }
            if (txtDatFinRel.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtDatFinRelKeyTyped

    private void rbtnAssRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAssRelActionPerformed
        rbtnAssRel.grabFocus();

        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontbold(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));
        tabelarelatorio(tblRel, scrRel, 5, 1, null, null);
        lblDatIniRel.setLocation(290, 150);
        lblDatFinRel.setLocation(430, 150);
    }//GEN-LAST:event_rbtnAssRelActionPerformed

    private void btnRelPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelPriMouseReleased
        if (!pnlRel.isVisible()) {

            rbtnTodRel.setSelected(true);

            lblDatIniRel.setLocation(290, 150);
            lblDatFinRel.setLocation(430, 150);

            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);

            btnVolRel.grabFocus();

            btnTodRel.setFont(fontbold(12));
            btnDiaRel.setFont(fontmed(12));
            btnSemRel.setFont(fontmed(12));
            btnMesRel.setFont(fontmed(12));
            btnAnoRel.setFont(fontmed(12));
            lblDatIniRel.setFont(fontmed(12));
            lblDatFinRel.setFont(fontmed(12));

            tabelarelatorio(tblRel, scrRel, 1, 1, null, null);

            pnlRel.setVisible(true);
            lblTitPri.setText("Relatórios");
            lblTitPri.setVisible(true);

            pnlCadEnt.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlMas.setVisible(false);
            pnlDes.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlGerEnt.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

        }
    }//GEN-LAST:event_btnRelPriMouseReleased

    private void btnTodRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTodRelMouseEntered
        btnTodRel.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnTodRelMouseEntered

    private void btnDiaRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDiaRelMouseEntered
        btnDiaRel.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnDiaRelMouseEntered

    private void btnSemRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSemRelMouseEntered
        btnSemRel.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnSemRelMouseEntered

    private void btnMesRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMesRelMouseEntered
        btnMesRel.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnMesRelMouseEntered

    private void btnAnoRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnoRelMouseEntered
        btnAnoRel.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnAnoRelMouseEntered

    private void btnTodRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTodRelMouseExited
        btnTodRel.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnTodRelMouseExited

    private void btnDiaRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDiaRelMouseExited
        btnDiaRel.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnDiaRelMouseExited

    private void btnSemRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSemRelMouseExited
        btnSemRel.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnSemRelMouseExited

    private void btnMesRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMesRelMouseExited
        btnMesRel.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnMesRelMouseExited

    private void btnAnoRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnoRelMouseExited
        btnAnoRel.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnAnoRelMouseExited

    private void lblDatIniRelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblDatIniRelFocusGained

    }//GEN-LAST:event_lblDatIniRelFocusGained

    private void btnTodRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTodRelMouseReleased
        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontbold(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 1, null, null);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 1, null, null);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 1, null, null);
        } else if (rbtnSerTimRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 1, null, null);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 1, null, null);
        }

        if (txtDatIniRel.hasFocus()) {

            lblDatFinRel.setLocation(430, 150);

        } else {
            lblDatIniRel.setLocation(290, 150);
        }
        btnTodRel.grabFocus();
    }//GEN-LAST:event_btnTodRelMouseReleased

    private void btnDiaRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDiaRelMouseReleased
        btnDiaRel.grabFocus();

        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontbold(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 2, null, null);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 2, null, null);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 2, null, null);
        } else if (rbtnSerTimRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 2, null, null);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 2, null, null);
        }

        if (txtDatIniRel.hasFocus()) {

            lblDatFinRel.setLocation(430, 150);

        } else {
            lblDatIniRel.setLocation(290, 150);
        }
        btnDiaRel.grabFocus();
    }//GEN-LAST:event_btnDiaRelMouseReleased

    private void btnSemRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSemRelMouseReleased
        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontbold(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 3, null, null);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 3, null, null);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 3, null, null);
        } else if (rbtnSerTimRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 3, null, null);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 3, null, null);
        }

        if (txtDatIniRel.hasFocus()) {

            lblDatFinRel.setLocation(430, 150);

        } else {
            lblDatIniRel.setLocation(290, 150);
        }
        btnSemRel.grabFocus();
    }//GEN-LAST:event_btnSemRelMouseReleased

    private void btnMesRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMesRelMouseReleased

        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontbold(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 4, null, null);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 4, null, null);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 4, null, null);
        } else if (rbtnSerTimRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 4, null, null);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 4, null, null);
        }
        if (txtDatIniRel.hasFocus()) {

            lblDatFinRel.setLocation(430, 150);

        } else {
            lblDatIniRel.setLocation(290, 150);
        }
        btnMesRel.grabFocus();

    }//GEN-LAST:event_btnMesRelMouseReleased

    private void btnAnoRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnoRelMouseReleased
        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontbold(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 5, null, null);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 5, null, null);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 5, null, null);
        } else if (rbtnSerTimRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 5, null, null);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 5, null, null);
        }

        if (txtDatIniRel.hasFocus()) {

            lblDatFinRel.setLocation(430, 150);

        } else {
            lblDatIniRel.setLocation(290, 150);
        }

        btnAnoRel.grabFocus();
    }//GEN-LAST:event_btnAnoRelMouseReleased

    private void rbtnSerTimRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSerTimRelActionPerformed
        rbtnSerTimRel.grabFocus();

        txtDatIniRel.setText(null);
        txtDatFinRel.setText(null);

        btnTodRel.setFont(fontbold(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        tabelarelatorio(tblRel, scrRel, 4, 1, null, null);
        lblDatIniRel.setLocation(290, 150);
        lblDatFinRel.setLocation(430, 150);
    }//GEN-LAST:event_rbtnSerTimRelActionPerformed

    private void txtDatFinRelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatFinRelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {

                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));

                if (rbtnTodRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 1, 6, data1, data2);
                } else if (rbtnSerRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 2, 6, data1, data2);
                } else if (rbtnVenRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 3, 6, data1, data2);
                } else if (rbtnSerTimRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 4, 6, data1, data2);
                } else {
                    tabelarelatorio(tblRel, scrRel, 5, 6, data1, data2);
                }

                btnTodRel.setFont(fontmed(12));
                btnDiaRel.setFont(fontmed(12));
                btnSemRel.setFont(fontmed(12));
                btnMesRel.setFont(fontmed(12));
                btnAnoRel.setFont(fontmed(12));
                lblDatIniRel.setFont(fontbold(12));
                lblDatFinRel.setFont(fontbold(12));

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(pnlRel, "Data inserida inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_txtDatFinRelKeyPressed

    private void txtDatIniRelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatIniRelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {

                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));

                if (rbtnTodRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 1, 6, data1, data2);
                } else if (rbtnSerRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 2, 6, data1, data2);
                } else if (rbtnVenRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 3, 6, data1, data2);
                } else if (rbtnSerTimRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 4, 6, data1, data2);
                } else {
                    tabelarelatorio(tblRel, scrRel, 5, 6, data1, data2);
                }

                btnTodRel.setFont(fontmed(12));
                btnDiaRel.setFont(fontmed(12));
                btnSemRel.setFont(fontmed(12));
                btnMesRel.setFont(fontmed(12));
                btnAnoRel.setFont(fontmed(12));

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(pnlRel, "Data inserida inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_txtDatIniRelKeyPressed

    private void txtDatIniRelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatIniRelKeyReleased
        if (txtDatIniRel.getText().length() > 9) {
            evt.consume();
            txtDatFinRel.grabFocus();
        }
    }//GEN-LAST:event_txtDatIniRelKeyReleased

    private void btnGerMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerMasActionPerformed
        String ativacao;
        String app;
        String c6;
        String port;

        if (rbtnAtiMas.isSelected()) {
            ativacao = "(X) Ativação\n"
                    + "(  ) Migração\n"
                    + "(  ) Migração (Troca de Chip)\n";
        } else if (rbtnMigMas.isSelected()) {
            ativacao = "(  ) Ativação\n"
                    + "(X) Migração\n"
                    + "(  ) Migração (Troca de Chip)\n";
        } else {
            ativacao = "(  ) Ativação\n"
                    + "(  ) Migração\n"
                    + "(X) Migração (Troca de Chip)\n";
        }

        if (rbtnSieMas.isSelected()) {
            app = "(X) Siebel Pós\n"
                    + "(  ) App TIM Vendas\n";
        } else {
            app = "(  ) Siebel Pós\n"
                    + "(X) App TIM Vendas\n";
        }

        if (chkC6Mas.isSelected()) {
            c6 = "(X) Sim\n"
                    + "(  ) Não\n";

        } else {
            c6 = "(  ) Sim\n"
                    + "(X) Não\n";
        }

        if ("".equals(txtNumPorMas.getText())) {
            port = "(  ) Sim\n"
                    + "(X) Não\n";
        } else {
            port = "(X) Sim\n"
                    + "(  ) Não\n"
                    + "---------------------------------------\n"
                    + "*Número Portado:* " + txtNumPorMas.getText() + "\n";
        }

        txtAreMas.setText(
                "*⚠️ Máscara para Input de Vendas ⚠*\n\n"
                + "️*Nome do PDV:* Empório Cell - Frutal\n"
                + "*Nome do Vendedor:* Guilherme\n"
                + "*Nome do Cliente:* " + txtNomMas.getText() + "\n"
                + "*CPF do Cliente:* " + txtCpfMas.getText() + "\n"
                + "*Telefone de Contato:* " + txtNumConMas.getText() + "\n"
                + "*Número do Acesso:* " + txtNumAceMas.getText() + "\n"
                + "---------------------------------------\n"
                + "*Ativação ou Migração*\n"
                + ativacao
                + "---------------------------------------\n"
                + "*Portabilidade*\n"
                + port
                + "---------------------------------------\n"
                + "*Qual plano foi vendido?*\n"
                + txtPlaMas.getText()
                + "\n---------------------------------------\n"
                + "*Sistema Utilizado*\n"
                + app
                + "---------------------------------------\n"
                + "*Conta C6 Bank*\n"
                + c6
                + "---------------------------------------\n"
                + "*Dia do vencimento da fatura:* " + txtVenMas.getText()
        );

        txtAreMas.setCaretPosition(0);

        btnCopMas.setVisible(true);

    }//GEN-LAST:event_btnGerMasActionPerformed

    private void btnCanMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanMasActionPerformed
        txtNomMas.setText(null);
        txtCpfMas.setText(null);
        txtVenMas.setText(null);
        txtNumConMas.setText(null);
        txtNumAceMas.setText(null);
        txtNumPorMas.setText(null);
        txtPlaMas.setText(null);
        btnGroup.clearSelection();
        btnGroup1.clearSelection();

        chkC6Mas.setSelected(false);

        txtAreMas.setText(null);

        lblNomMas.setLocation(90, 50);
        lblCpfMas.setLocation(90, 110);
        lblVenMas.setLocation(90, 170);
        lblNumConMas.setLocation(350, 50);
        lblNumAceMas.setLocation(350, 110);
        lblNumPorMas.setLocation(350, 170);
        lblPlaMas.setLocation(350, 230);

        btnCanMas.grabFocus();

        lblTitPri.setVisible(false);
        btnCopMas.setVisible(false);
        pnlMas.setVisible(false);
    }//GEN-LAST:event_btnCanMasActionPerformed

    private void txtNomMasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomMasFocusGained
        if (txtNomMas.getText().isEmpty()) {
            anitxtin(lblNomMas);
        }
    }//GEN-LAST:event_txtNomMasFocusGained

    private void txtNomMasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomMasFocusLost
        if (txtNomMas.getText().isEmpty()) {
            anitxtout(lblNomMas);
        }
    }//GEN-LAST:event_txtNomMasFocusLost

    private void txtNumConMasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumConMasFocusGained
        if (txtNumConMas.getText().isEmpty()) {
            anitxtin(lblNumConMas);
        }
    }//GEN-LAST:event_txtNumConMasFocusGained

    private void txtNumConMasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumConMasFocusLost
        if (txtNumConMas.getText().isEmpty()) {
            anitxtout(lblNumConMas);
        }
    }//GEN-LAST:event_txtNumConMasFocusLost

    private void txtCpfMasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCpfMasFocusGained
        if (txtCpfMas.getText().isEmpty()) {
            anitxtin(lblCpfMas);
        }
    }//GEN-LAST:event_txtCpfMasFocusGained

    private void txtCpfMasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCpfMasFocusLost
        if (txtCpfMas.getText().isEmpty()) {
            anitxtout(lblCpfMas);
        }
    }//GEN-LAST:event_txtCpfMasFocusLost

    private void txtNumAceMasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumAceMasFocusGained
        if (txtNumAceMas.getText().isEmpty()) {
            anitxtin(lblNumAceMas);
        }
    }//GEN-LAST:event_txtNumAceMasFocusGained

    private void txtNumAceMasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumAceMasFocusLost
        if (txtNumAceMas.getText().isEmpty()) {
            anitxtout(lblNumAceMas);
        }
    }//GEN-LAST:event_txtNumAceMasFocusLost

    private void txtNumPorMasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumPorMasFocusGained
        if (txtNumPorMas.getText().isEmpty()) {
            anitxtin(lblNumPorMas);
        }
    }//GEN-LAST:event_txtNumPorMasFocusGained

    private void txtNumPorMasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumPorMasFocusLost
        if (txtNumPorMas.getText().isEmpty()) {
            anitxtout(lblNumPorMas);
        }
    }//GEN-LAST:event_txtNumPorMasFocusLost

    private void txtPlaMasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlaMasFocusGained
        if (txtPlaMas.getText().isEmpty()) {
            anitxtin(lblPlaMas);
        }
    }//GEN-LAST:event_txtPlaMasFocusGained

    private void txtPlaMasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlaMasFocusLost
        if (txtPlaMas.getText().isEmpty()) {
            anitxtout(lblPlaMas);
        }
    }//GEN-LAST:event_txtPlaMasFocusLost

    private void txtVenMasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVenMasFocusGained
        if (txtVenMas.getText().isEmpty()) {
            anitxtin(lblVenMas);
        }
    }//GEN-LAST:event_txtVenMasFocusGained

    private void txtVenMasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVenMasFocusLost
        if (txtVenMas.getText().isEmpty()) {
            anitxtout(lblVenMas);
        }
    }//GEN-LAST:event_txtVenMasFocusLost

    private void btnCopMasMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopMasMouseReleased
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        String texto = txtAreMas.getText();

        StringSelection selecao = new StringSelection(texto);

        clipboard.setContents(selecao, null);
    }//GEN-LAST:event_btnCopMasMouseReleased

    private void btnCopMasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopMasMouseEntered
        btnCopMas.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnCopMasMouseEntered

    private void btnCopMasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopMasMouseExited
        btnCopMas.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnCopMasMouseExited

    private void txtVenMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVenMasKeyTyped
        if (txtVenMas.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {
            if (!Character.isDigit(evt.getKeyChar()) || txtVenMas.getText().length() > 1) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtVenMasKeyTyped

    private void txtVenMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVenMasKeyReleased
        if (!"".equals(txtVenMas.getText())) {
            if (Integer.parseInt(txtVenMas.getText()) > 31) {
                txtVenMas.setText("31");
            }
        }
    }//GEN-LAST:event_txtVenMasKeyReleased

    private void txtCpfMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfMasKeyTyped
        if (txtCpfMas.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtCpfMas.getText().length() == 3) {

                    txtCpfMas.setText(txtCpfMas.getText() + ".");
                    txtCpfMas.setCaretPosition(4);

                } else if (txtCpfMas.getText().length() == 7) {

                    txtCpfMas.setText(txtCpfMas.getText() + ".");
                    txtCpfMas.setCaretPosition(8);

                } else if (txtCpfMas.getText().length() == 11) {

                    txtCpfMas.setText(txtCpfMas.getText() + "-");
                    txtCpfMas.setCaretPosition(12);

                }

            }
            if (txtCpfMas.getText().length() > 13) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtCpfMasKeyTyped

    private void txtNumConMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumConMasKeyTyped
        if (txtNumConMas.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtNumConMas.getText().length() == 0) {

                    txtNumConMas.setText(txtNumConMas.getText() + "(");
                    txtNumConMas.setCaretPosition(1);

                } else if (txtNumConMas.getText().length() == 3) {

                    txtNumConMas.setText(txtNumConMas.getText() + ") ");
                    txtNumConMas.setCaretPosition(5);

                } else if (txtNumConMas.getText().length() == 10) {

                    txtNumConMas.setText(txtNumConMas.getText() + "-");
                    txtNumConMas.setCaretPosition(11);

                }

            }
            if (txtNumConMas.getText().length() > 14) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtNumConMasKeyTyped

    private void txtNumAceMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumAceMasKeyTyped
        if (txtNumAceMas.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtNumAceMas.getText().length() == 0) {

                    txtNumAceMas.setText(txtNumAceMas.getText() + "(");
                    txtNumAceMas.setCaretPosition(1);

                } else if (txtNumAceMas.getText().length() == 3) {

                    txtNumAceMas.setText(txtNumAceMas.getText() + ") ");
                    txtNumAceMas.setCaretPosition(5);

                } else if (txtNumAceMas.getText().length() == 10) {

                    txtNumAceMas.setText(txtNumAceMas.getText() + "-");
                    txtNumAceMas.setCaretPosition(11);

                }

            }
            if (txtNumAceMas.getText().length() > 14) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtNumAceMasKeyTyped

    private void txtNumPorMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumPorMasKeyTyped
        if (txtNumPorMas.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                switch (txtNumPorMas.getText().length()) {
                    case 0:
                        txtNumPorMas.setText(txtNumPorMas.getText() + "(");
                        txtNumPorMas.setCaretPosition(1);
                        break;
                    case 3:
                        txtNumPorMas.setText(txtNumPorMas.getText() + ") ");
                        txtNumPorMas.setCaretPosition(5);
                        break;
                    case 10:
                        txtNumPorMas.setText(txtNumPorMas.getText() + "-");
                        txtNumPorMas.setCaretPosition(11);
                        break;
                    default:
                        break;
                }

            }
            if (txtNumPorMas.getText().length() > 14) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtNumPorMasKeyTyped

    private void btnSalDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalDesActionPerformed
        try {

            despezas de = new despezas();
            despezasDAO dedao = new despezasDAO();

            de.setDescricao(txtDesDes.getText());
            de.setValor(Double.valueOf(txtPreDes.getText()));
            de.setData(formatterbanco.format(formatter.parse(txtDatDes.getText())));
            de.setStatus(0);

            dedao.inserir(de);

            JOptionPane.showMessageDialog(pnlCadDes, "Despeza inserida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            pnlCadDes.setVisible(false);
            lblTitPri.setVisible(false);

        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSalDesActionPerformed

    private void txtDesDesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesDesFocusGained
        if (txtDesDes.getText().isEmpty()) {
            anitxtin(lblDesDes);
        }

    }//GEN-LAST:event_txtDesDesFocusGained

    private void txtDesDesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesDesFocusLost
        if (txtDesDes.getText().isEmpty()) {
            anitxtout(lblDesDes);
        }
    }//GEN-LAST:event_txtDesDesFocusLost

    private void txtPreDesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreDesFocusGained
        if (txtPreDes.getText().isEmpty()) {
            anitxtin(lblPreDes);
            lblR$Des.setVisible(true);
        }
    }//GEN-LAST:event_txtPreDesFocusGained

    private void txtPreDesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreDesFocusLost
        if (txtPreDes.getText().isEmpty()) {
            anitxtout(lblPreDes);
            lblR$Des.setVisible(false);
        }
    }//GEN-LAST:event_txtPreDesFocusLost

    private void txtPreDesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreDesKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtPreDes.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPreDesKeyTyped

    private void btnCanDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanDesActionPerformed
        lblTitPri.setVisible(false);

        pnlCadDes.setVisible(false);
    }//GEN-LAST:event_btnCanDesActionPerformed

    private void tblConDesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConDesMouseClicked
        try {

            int resp = JOptionPane.showOptionDialog(pnlCadEst, "Ao confirmar a conclusão, a data será definida para o próximo mês e será considerado a data de hoje como a conclusão. Atualize somente se tiver feito o acerto! Deseja prosseguir?", "Conclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                despezas de = new despezas();
                despezasDAO dedao = new despezasDAO();

                de.setId(Integer.parseInt(tblConDes.getValueAt(tblConDes.getSelectedRow(), 0).toString()));
                de.setDataconclusao(formatterbanco.format(new Date()));

                dedao.conclusao(de);

                tabeladespezas(tblConDes, scrConDes);

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblConDesMouseClicked

    private void btnVolDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolDesActionPerformed
        pnlDes.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnVolDesActionPerformed

    private void txtDatDesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatDesFocusGained
        if (txtDatDes.getText().isEmpty()) {
            anitxtin(lblDatDes);
        }
    }//GEN-LAST:event_txtDatDesFocusGained

    private void txtDatDesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatDesFocusLost
        if (txtDatDes.getText().isEmpty()) {
            anitxtout(lblDatDes);
        }
    }//GEN-LAST:event_txtDatDesFocusLost

    private void txtDatDesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatDesKeyTyped
        if (txtDatDes.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatDes.getText().length() == 2) {

                    txtDatDes.setText(txtDatDes.getText() + "/");
                    txtDatDes.setCaretPosition(3);

                } else if (txtDatDes.getText().length() == 5) {

                    txtDatDes.setText(txtDatDes.getText() + "/");
                    txtDatDes.setCaretPosition(6);

                }

            }
            if (txtDatDes.getText().length() > 9) {
                evt.consume();
            }
        }

        btnSalDes.setEnabled(true);
    }//GEN-LAST:event_txtDatDesKeyTyped

    private void btnExcGerDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerDesActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(pnlGerDes, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                despezas de = new despezas();
                despezasDAO dedao = new despezasDAO();

                de.setId(Integer.parseInt(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 0).toString()));

                dedao.excluir(de);

                JOptionPane.showMessageDialog(pnlGerDes, "Excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                pnlGerDes.setVisible(false);
                lblTitPri.setVisible(false);

            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExcGerDesActionPerformed

    private void btnAltGerDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerDesActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(pnlGerDes, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                despezas de = new despezas();
                despezasDAO dedao = new despezasDAO();

                de.setId(Integer.parseInt(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 0).toString()));
                de.setDescricao(txtDesGerDes.getText());
                de.setValor(Double.valueOf(txtPreGerDes.getText().replace(".", "").replace(",", ".")));
                de.setData(formatterbanco.format(formatter.parse(txtDatGerDes.getText())));

                dedao.alterar(de);

                JOptionPane.showMessageDialog(pnlGerDes, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                pnlGerDes.setVisible(false);
                lblTitPri.setVisible(false);

            }
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAltGerDesActionPerformed

    private void btnCanGerDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerDesActionPerformed
        pnlGerDes.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnCanGerDesActionPerformed

    private void tblGerDesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGerDesMouseClicked
        txtDesGerDes.setText(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 1).toString());
        txtPreGerDes.setText((tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 2).toString()).substring(3, tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 2).toString().length()));
        txtDatGerDes.setText(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 3).toString());

        lblDesGerDes.setLocation(870, 20);
        lblPreGerDes.setLocation(870, 80);
        lblDatGerDes.setLocation(870, 140);

        sepDesGerDes.setForeground(corforeazul);
        sepPreGerDes.setForeground(corforeazul);
        sepDatGerDes.setForeground(corforeazul);

        lblDesGerDes.setEnabled(true);
        txtDesGerDes.setEnabled(true);
        lblPreGerDes.setEnabled(true);
        txtPreGerDes.setEnabled(true);
        lblDatGerDes.setEnabled(true);
        txtDatGerDes.setEnabled(true);

        lblR$GerDes.setVisible(true);

        btnExcGerDes.setEnabled(true);
        btnAltGerDes.setEnabled(true);

    }//GEN-LAST:event_tblGerDesMouseClicked

    private void txtDatGerDesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatGerDesFocusGained
        if (txtDatGerDes.getText().isEmpty()) {
            anitxtin(lblDatGerDes);
        }
    }//GEN-LAST:event_txtDatGerDesFocusGained

    private void txtDatGerDesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatGerDesFocusLost
        if (txtDatGerDes.getText().isEmpty()) {
            anitxtout(lblDatGerDes);
        }
    }//GEN-LAST:event_txtDatGerDesFocusLost

    private void txtDatGerDesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatGerDesKeyTyped
        if (txtDatGerDes.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatGerDes.getText().length() == 2) {

                    txtDatGerDes.setText(txtDatGerDes.getText() + "/");
                    txtDatGerDes.setCaretPosition(3);

                } else if (txtDatGerDes.getText().length() == 5) {

                    txtDatGerDes.setText(txtDatGerDes.getText() + "/");
                    txtDatGerDes.setCaretPosition(6);

                }

            }
            if (txtDatGerDes.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtDatGerDesKeyTyped

    private void txtDesGerDesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesGerDesFocusGained
        if (txtDesGerDes.getText().isEmpty()) {
            anitxtin(lblDesGerDes);
        }
    }//GEN-LAST:event_txtDesGerDesFocusGained

    private void txtDesGerDesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesGerDesFocusLost
        if (txtDesGerDes.getText().isEmpty()) {
            anitxtout(lblDesGerDes);
        }
    }//GEN-LAST:event_txtDesGerDesFocusLost

    private void txtPreGerDesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreGerDesFocusGained
        if (txtPreGerDes.getText().isEmpty()) {
            anitxtin(lblPreGerDes);
            lblR$GerDes.setVisible(true);
        }
    }//GEN-LAST:event_txtPreGerDesFocusGained

    private void txtPreGerDesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreGerDesFocusLost
        if (txtPreGerDes.getText().isEmpty()) {
            anitxtout(lblPreGerDes);
            lblR$GerDes.setVisible(false);
        }
    }//GEN-LAST:event_txtPreGerDesFocusLost

    private void btnGerDesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerDesMouseEntered
        btnGerDes.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerDesMouseEntered

    private void btnGerDesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerDesMouseExited
        btnGerDes.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerDesMouseExited

    private void btnGerDesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerDesMouseReleased
        btnMasPla.setVisible(false);
        btnDes.setVisible(false);
        btnCadDes.setVisible(false);
        btnGerDes.setVisible(false);

        if (!pnlGerDes.isVisible()) {

            if (tabelagerenciardespezas(tblGerDes, scrGerDes)) {

                pnlGerDes.setVisible(true);
                lblTitPri.setText("Gerenciar Despezas");
                lblTitPri.setVisible(true);

                pnlCadEst.setVisible(false);
                pnlConEst.setVisible(false);
                pnlGerEst.setVisible(false);
                pnlIteGerEnt.setVisible(false);

                pnlCadEnt.setVisible(false);
                pnlRel.setVisible(false);
                pnlIteCadEnt.setVisible(false);
                pnlCadTipSer.setVisible(false);
                pnlGerTipSer.setVisible(false);
                pnlDes.setVisible(false);
                pnlCadDes.setVisible(false);
                pnlMas.setVisible(false);
                pnlGerEnt.setVisible(false);
                pnlOs.setVisible(false);

                btnMasPla.setVisible(false);
                btnDes.setVisible(false);
                btnCadDes.setVisible(false);
                btnGerDes.setVisible(false);
                btnExcGerDes.setEnabled(false);
                btnAltGerDes.setEnabled(false);

                txtDesGerDes.setText(null);
                txtPreGerDes.setText(null);
                txtDatGerDes.setText(null);

                lblDesGerDes.setLocation(870, 40);
                lblPreGerDes.setLocation(870, 100);
                lblDatGerDes.setLocation(870, 160);

                sepDesGerDes.setForeground(Color.GRAY);
                sepPreGerDes.setForeground(Color.GRAY);
                sepDatGerDes.setForeground(Color.GRAY);

                lblDesGerDes.setEnabled(false);
                txtDesGerDes.setEnabled(false);
                lblPreGerDes.setEnabled(false);
                txtPreGerDes.setEnabled(false);
                lblDatGerDes.setEnabled(false);
                txtDatGerDes.setEnabled(false);

                lblR$GerDes.setVisible(false);

            } else {

                JOptionPane.showMessageDialog(pnlGerDes, "Sem despezas para gerenciar. Cadastre-as primeiro!", "Gerenciar Despezas", JOptionPane.INFORMATION_MESSAGE);

            }

        }
    }//GEN-LAST:event_btnGerDesMouseReleased

    private void btnExcGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerEntActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(pnlGerEnt, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                if (tblSelIteGerEnt.getRowCount() != 0) {

                    int resp1 = JOptionPane.showOptionDialog(pnlGerEnt, "Por favor, devolva os ítens para o estoque. Se prosseguir, todos os produtos serão excluídos!\n\nDevolver ítens?", "Entrada", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                    if (resp1 == JOptionPane.YES_OPTION) {

                        btnIteGerEnt.doClick();

                    } else if (resp1 == JOptionPane.NO_OPTION) {

                        entrada en = new entrada();
                        entradaDAO endao = new entradaDAO();

                        en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString());

                        endao.excluir(en);

                        JOptionPane.showMessageDialog(pnlIteGerEnt, "Entrada excluída com sucesso!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

                        pnlGerEnt.setVisible(false);

                    }

                } else {

                    entrada en = new entrada();
                    entradaDAO endao = new entradaDAO();

                    en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString());
                    endao.excluir(en);

                    JOptionPane.showMessageDialog(pnlIteGerEnt, "Entrada excluída com sucesso!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

                    pnlGerEnt.setVisible(false);
                    lblTitPri.setVisible(false);

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExcGerEntActionPerformed

    private void btnBusGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusGerEntActionPerformed
        try {

            String data = formatterbanco.format(formatter.parse(txtDatBusGerEnt.getText()));

            if (tabelagerenciarentrada(tblGerEnt, scrGerEnt, data)) {

                tblGerEnt.setVisible(true);
                scrGerEnt.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(pnlGerEnt, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBusGerEntActionPerformed

    private void btnIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIteGerEntActionPerformed
        tblEstIteGerEnt.setVisible(true);
        scrEstIteGerEnt.setVisible(true);
        lblEstIteGerEnt.setForeground(corforeazul);
        btnVolIteGerEnt.grabFocus();
        tblEstIteGerEnt.getTableHeader().setFont(fontbold(11));
        pnlIteGerEnt.setVisible(true);
        pnlGerEnt.setVisible(false);

        if (!txtBusIteGerEnt.getText().isEmpty()) {
            anitxtin(lblBusIteGerEnt);
        }
    }//GEN-LAST:event_btnIteGerEntActionPerformed

    private void btnCanGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerEntActionPerformed
        if (btnAltGerEnt.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(pnlCadEst, "Antes de cancelar, verifique a tabela de produtos selecionados e remova aqueles que não fazem parte da entrada! Deseja continuar?", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                pnlGerEnt.setVisible(false);
                lblTitPri.setVisible(false);
            }

        } else {

            pnlGerEnt.setVisible(false);
            lblTitPri.setVisible(false);

        }
    }//GEN-LAST:event_btnCanGerEntActionPerformed

    private void txtDatGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatGerEntFocusGained
        if (txtDatGerEnt.getText().isEmpty()) {
            anitxtin(lblDatGerEnt);
        }
    }//GEN-LAST:event_txtDatGerEntFocusGained

    private void txtDatGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatGerEntFocusLost
        if (txtDatGerEnt.getText().isEmpty()) {
            anitxtout(lblDatGerEnt);
        }
    }//GEN-LAST:event_txtDatGerEntFocusLost

    private void txtPreGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreGerEntFocusGained
        if (txtPreGerEnt.getText().isEmpty()) {
            anitxtin(lblPreGerEnt);
            lblR$GerEnt.setVisible(true);
        }
    }//GEN-LAST:event_txtPreGerEntFocusGained

    private void txtPreGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreGerEntFocusLost
        if (txtPreGerEnt.getText().isEmpty()) {
            anitxtout(lblPreGerEnt);
            lblR$GerEnt.setVisible(false);
        }
    }//GEN-LAST:event_txtPreGerEntFocusLost

    private void txtDetGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetGerEntFocusGained
        if (txtDetGerEnt.getText().isEmpty()) {
            anitxtin(lblDetGerEnt);
        }
    }//GEN-LAST:event_txtDetGerEntFocusGained

    private void txtDetGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDetGerEntFocusLost
        if (txtDetGerEnt.getText().isEmpty()) {
            anitxtout(lblDetGerEnt);
        }
    }//GEN-LAST:event_txtDetGerEntFocusLost

    private void txtDatBusGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatBusGerEntFocusGained
        if (txtDatBusGerEnt.getText().isEmpty()) {
            anitxtin(lblDatBusGerEnt);
        }
    }//GEN-LAST:event_txtDatBusGerEntFocusGained

    private void txtDatBusGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatBusGerEntFocusLost
        if (txtDatBusGerEnt.getText().isEmpty()) {
            anitxtout(lblDatBusGerEnt);
        }
    }//GEN-LAST:event_txtDatBusGerEntFocusLost

    private void txtDatBusGerEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatBusGerEntKeyTyped
        if (txtDatBusGerEnt.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatBusGerEnt.getText().length() == 2) {

                    txtDatBusGerEnt.setText(txtDatBusGerEnt.getText() + "/");
                    txtDatBusGerEnt.setCaretPosition(3);

                } else if (txtDatBusGerEnt.getText().length() == 5) {

                    txtDatBusGerEnt.setText(txtDatBusGerEnt.getText() + "/");
                    txtDatBusGerEnt.setCaretPosition(6);

                }

            }
            if (txtDatBusGerEnt.getText().length() > 9) {
                evt.consume();
            }
        }
        btnBusGerEnt.setEnabled(true);
    }//GEN-LAST:event_txtDatBusGerEntKeyTyped

    private void tblGerEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGerEntMouseClicked
        lblDatGerEnt.setLocation(860, 70);
        lblPreGerEnt.setLocation(860, 130);
        lblDetGerEnt.setLocation(860, 190);

        txtDatGerEnt.setText(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 0).toString());
        txtPreGerEnt.setText((tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 4).toString()).substring(3, tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 4).toString().length()));
        txtDetGerEnt.setText((!"Sem Detalhes".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString())) ? tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString() : null);

        estoque es = new estoque();

        es.setTipoproduto("Capinha");

        es.setModelo("");

        tabelaestoqueconsulta(es, tblEstIteGerEnt, scrEstIteGerEnt);

        tabelaitensselecionadosgerenciar(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString());

        if ("Venda".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

            cmbSerGerEnt.setEnabled(false);
            lblSerGerEnt.setEnabled(false);
            cmbSerGerEnt.setSelectedIndex(0);
            btnIteGerEnt.setEnabled(true);

        } else if ("Assistência".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

            btnIteGerEnt.setEnabled(false);

            cmbSerGerEnt.setEnabled(true);

            lblSerGerEnt.setEnabled(true);

            comboboxentrada(cmbSerGerEnt, 2);

            for (int i = 1; i <= cmbSerGerEnt.getItemCount(); i++) {

                cmbSerGerEnt.setSelectedIndex(i);

                itens selectedItem = (itens) cmbSerGerEnt.getSelectedItem();

                String textoSelecionado = selectedItem.getDescricao();

                if (tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 2).toString().equals(textoSelecionado)) {

                    break;

                }

            }

        } else {

            btnIteGerEnt.setEnabled(false);

            cmbSerGerEnt.setEnabled(true);

            lblSerGerEnt.setEnabled(true);

            comboboxentrada(cmbSerGerEnt, 1);

            for (int i = 1; i <= cmbSerGerEnt.getItemCount(); i++) {

                cmbSerGerEnt.setSelectedIndex(i);

                itens selectedItem = (itens) cmbSerGerEnt.getSelectedItem();

                String textoSelecionado = selectedItem.getDescricao();

                if (tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 2).toString().equals(textoSelecionado)) {

                    break;

                }

            }

            btnIteGerEnt.setEnabled(true);

        }

        txtDatGerEnt.setEnabled(true);
        txtPreGerEnt.setEnabled(true);
        lblR$GerEnt.setVisible(true);
        txtDetGerEnt.setEnabled(true);

        lblDatGerEnt.setEnabled(true);
        lblPreGerEnt.setEnabled(true);
        lblDetGerEnt.setEnabled(true);

        sepDatGerEnt.setForeground(corforeazul);
        sepPreGerEnt.setForeground(corforeazul);
        sepDetGerEnt.setForeground(corforeazul);

        btnAltGerEnt.setEnabled(true);
        btnExcGerEnt.setEnabled(true);

        if (!txtDatGerEnt.getText().isEmpty()) {
            anitxtin(lblDatGerEnt);
        }

        if (!txtPreGerEnt.getText().isEmpty()) {
            anitxtin(lblPreGerEnt);
        }

        if (!txtDetGerEnt.getText().isEmpty()) {
            anitxtin(lblDetGerEnt);
        }
    }//GEN-LAST:event_tblGerEntMouseClicked

    private void btnAltGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerEntActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(pnlGerEnt, "Tem certeza que deseja alterar?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                entrada en = new entrada();
                entradaDAO endao = new entradaDAO();

                en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString());

                endao.excluir(en);

                if (!("Venda".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString()) && tblSelIteGerEnt.getRowCount() == 0)) {

                    if (!(cmbSerGerEnt.getSelectedIndex() == 0 && ("Assistência".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString()) || "Serviço".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())))) {

                        try {

                            int idser = 0;

                            if (cmbSerGerEnt.getSelectedIndex() != 0) {
                                itens selectedItem = (itens) cmbSerGerEnt.getSelectedItem();
                                idser = selectedItem.getId();
                            }

                            if (tblSelIteGerEnt.getRowCount() != 0) {

                                for (int i = 1; i <= tblSelIteGerEnt.getRowCount(); i++) {

                                    en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString());
                                    en.setData(formatterbanco.format(((formatter.parse(txtDatGerEnt.getText())))));
                                    en.setPreco(Double.valueOf(txtPreGerEnt.getText().replace(".", "").replace(",", ".")));
                                    en.setDetalhes(txtDetGerEnt.getText());

                                    if ("Serviço".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

                                        en.setIdtiposervico(idser);
                                        en.setIdestoque(Integer.parseInt(tblSelIteGerEnt.getValueAt(i - 1, 1).toString()));
                                        en.setQuantidade(Integer.parseInt(tblSelIteGerEnt.getValueAt(i - 1, 0).toString()));

                                        endao.inserir(en, 1);

                                    } else if ("Venda".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

                                        en.setIdestoque(Integer.parseInt(tblSelIteGerEnt.getValueAt(i - 1, 1).toString()));
                                        en.setQuantidade(Integer.parseInt(tblSelIteGerEnt.getValueAt(i - 1, 0).toString()));

                                        endao.inserir(en, 2);
                                    }

                                }

                            } else {

                                en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString());
                                en.setData(formatterbanco.format((formatter.parse(txtDatGerEnt.getText()))));
                                en.setPreco(Double.valueOf(txtPreGerEnt.getText().replace(".", "").replace(",", ".")));
                                en.setDetalhes(txtDetGerEnt.getText());
                                en.setIdtiposervico(idser);
                                en.setIdestoque(1);

                                if ("Serviço".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

                                    endao.inserir(en, 1);

                                } else if ("Assistência".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

                                    endao.inserir(en, 3);

                                }

                            }

                            JOptionPane.showMessageDialog(pnlGerEnt, "Entrada alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                            pnlGerEnt.setVisible(false);

                        } catch (SQLException | ParseException ex) {
                            Logger.getLogger(main.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        JOptionPane.showMessageDialog(pnlGerEnt, "Selecione o serviço!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    }

                    lblTitPri.setVisible(false);

                } else {

                    JOptionPane.showMessageDialog(pnlGerEnt, "Selecione os ítem do estoque!", "Atenção", JOptionPane.INFORMATION_MESSAGE);

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAltGerEntActionPerformed

    private void btnGerEntMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEntMouseEntered
        btnGerEnt.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerEntMouseEntered

    private void btnGerEntMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEntMouseExited
        btnGerEnt.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerEntMouseExited

    private void btnGerEntMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEntMouseReleased
        btnGerEnt.setVisible(false);
        btnCadEnt.setVisible(false);

        if (!pnlGerEnt.isVisible()) {

            txtDatBusGerEnt.setText(null);
            txtDatGerEnt.setText(null);
            txtPreGerEnt.setText(null);
            txtDetGerEnt.setText(null);

            tblGerEnt.setVisible(false);
            scrGerEnt.setVisible(false);

            lblDatGerEnt.setEnabled(false);
            txtDatGerEnt.setEnabled(false);
            lblPreGerEnt.setEnabled(false);
            lblR$GerEnt.setVisible(false);

            txtPreGerEnt.setEnabled(false);
            lblDetGerEnt.setEnabled(false);
            txtDetGerEnt.setEnabled(false);
            lblSerGerEnt.setEnabled(false);
            cmbSerGerEnt.setEnabled(false);

            sepDatGerEnt.setForeground(Color.GRAY);
            sepPreGerEnt.setForeground(Color.GRAY);
            sepDetGerEnt.setForeground(Color.GRAY);

            btnAltGerEnt.setEnabled(false);
            btnExcGerEnt.setEnabled(false);
            btnIteGerEnt.setEnabled(false);
            btnBusGerEnt.setEnabled(false);

            lblDatBusGerEnt.setLocation(70, 50);
            lblDatGerEnt.setLocation(860, 70);
            lblPreGerEnt.setLocation(860, 130);
            lblDetGerEnt.setLocation(860, 190);

            lblTitPri.setText("Gerenciar Entrada");
            lblTitPri.setVisible(true);

            pnlCadEnt.setVisible(false);
            pnlRel.setVisible(false);
            pnlIteCadEnt.setVisible(false);
            pnlCadTipSer.setVisible(false);
            pnlCadEst.setVisible(false);
            pnlConEst.setVisible(false);
            pnlGerEst.setVisible(false);
            pnlGerTipSer.setVisible(false);
            pnlMas.setVisible(false);
            pnlDes.setVisible(false);
            pnlGerDes.setVisible(false);
            pnlCadDes.setVisible(false);
            pnlOs.setVisible(false);
            pnlIteGerEnt.setVisible(false);

            pnlGerEnt.setVisible(true);
        }
    }//GEN-LAST:event_btnGerEntMouseReleased

    private void btnVolIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolIteGerEntActionPerformed
        pnlIteGerEnt.setVisible(false);
        pnlGerEnt.setVisible(true);
    }//GEN-LAST:event_btnVolIteGerEntActionPerformed

    private void tblEstIteGerEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEstIteGerEntMouseClicked
        boolean existe = false;

        int sel = Integer.parseInt(tblEstIteGerEnt.getValueAt(tblEstIteGerEnt.getSelectedRow(), 0).toString());

        for (int i = 1; i <= tblSelIteGerEnt.getRowCount(); i++) {

            if (sel == Integer.parseInt(tblSelIteGerEnt.getValueAt(i - 1, 1).toString())) {

                existe = true;

            }

        }

        if (existe) {

            JOptionPane.showMessageDialog(pnlIteGerEnt, "Ítem já adicionado!", "Entrada", JOptionPane.ERROR_MESSAGE);

        } else {

            String opc = JOptionPane.showInputDialog(null, "Informe a quantidade deste ítem", "Entrada", JOptionPane.QUESTION_MESSAGE);

            if (opc != null) {

                try {

                    int i = Integer.parseInt(opc);

                    if (rbtnChiIteGerEnt.isSelected()) {

                        if (Integer.parseInt(tblEstIteGerEnt.getValueAt(tblEstIteGerEnt.getSelectedRow(), 3).toString()) < i) {

                            JOptionPane.showMessageDialog(pnlIteGerEnt, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteGerEnt, tblSelIteGerEnt, opc, rbtnChiIteGerEnt);

                            lblSelIteGerEnt.setForeground(corforeazul);
                            scrSelIteGerEnt.setVisible(true);
                            tblSelIteGerEnt.setVisible(true);

                        }

                    } else {

                        if (Integer.parseInt(tblEstIteGerEnt.getValueAt(tblEstIteGerEnt.getSelectedRow(), 5).toString()) < i) {

                            JOptionPane.showMessageDialog(pnlIteGerEnt, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteGerEnt, tblSelIteGerEnt, opc, rbtnChiIteGerEnt);

                            lblSelIteGerEnt.setForeground(corforeazul);
                            scrSelIteGerEnt.setVisible(true);
                            tblSelIteGerEnt.setVisible(true);

                        }

                    }

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(pnlIteGerEnt, "Digite apenas número!", "Erro", JOptionPane.ERROR_MESSAGE);

                }

            }

        }

        tblEstIteGerEnt.clearSelection();
    }//GEN-LAST:event_tblEstIteGerEntMouseClicked

    private void txtBusIteGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusIteGerEntFocusGained
        if (txtBusIteGerEnt.getText().isEmpty()) {
            anitxtin(lblBusIteGerEnt);
        }
    }//GEN-LAST:event_txtBusIteGerEntFocusGained

    private void txtBusIteGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusIteGerEntFocusLost
        if (txtBusIteGerEnt.getText().isEmpty()) {
            anitxtout(lblBusIteGerEnt);
        }
    }//GEN-LAST:event_txtBusIteGerEntFocusLost

    private void txtBusIteGerEntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusIteGerEntKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            estoque es = new estoque();

            if (rbtnCapIteGerEnt.isSelected()) {
                es.setTipoproduto("Capinha");
            } else if (rbtnPelIteGerEnt.isSelected()) {
                es.setTipoproduto("Película");
            } else if (rbtnChiIteGerEnt.isSelected()) {
                es.setTipoproduto("Chip");
            } else {
                es.setTipoproduto("Acessório");
            }

            es.setModelo(txtBusIteGerEnt.getText());

            if (tabelaestoqueconsulta(es, tblEstIteGerEnt, scrEstIteGerEnt)) {

                tblEstIteGerEnt.setVisible(true);
                scrEstIteGerEnt.setVisible(true);
                lblEstIteGerEnt.setForeground(corforeazul);

            } else {

                JOptionPane.showMessageDialog(pnlIteGerEnt, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

            }

            tblEstIteGerEnt.getTableHeader().setFont(fontbold(11));

        }
    }//GEN-LAST:event_txtBusIteGerEntKeyPressed

    private void tblSelIteGerEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSelIteGerEntMouseClicked
        try {
            String idt = (tblSelIteGerEnt.getValueAt(tblSelIteGerEnt.getSelectedRow(), 1).toString());
            String qua = (tblSelIteGerEnt.getValueAt(tblSelIteGerEnt.getSelectedRow(), 0).toString());

            entrada en = new entrada();
            entradaDAO endao = new entradaDAO();

            en.setIdestoque(Integer.parseInt(idt));
            en.setQuantidade(Integer.parseInt(qua));

            endao.atualizarestoque(en, 1);

            DefaultTableModel modelo = (DefaultTableModel) tblSelIteGerEnt.getModel();
            modelo.removeRow(tblSelIteGerEnt.getSelectedRow());

            if (tblSelIteGerEnt.getRowCount() == 0) {

                tblSelIteGerEnt.setVisible(false);
                scrSelIteGerEnt.setVisible(false);
                lblSelIteGerEnt.setForeground(new Color(246, 246, 246));

            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblSelIteGerEntMouseClicked

    private void txtDetGerEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDetGerEntMouseClicked
        if ("Sem Detalhes".equals(txtDetGerEnt.getText())) {

            txtDetGerEnt.setText(null);

        }
    }//GEN-LAST:event_txtDetGerEntMouseClicked

    private void txtDatGerEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatGerEntKeyTyped
        if (txtDatGerEnt.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatGerEnt.getText().length() == 2) {

                    txtDatGerEnt.setText(txtDatGerEnt.getText() + "/");
                    txtDatGerEnt.setCaretPosition(3);

                } else if (txtDatGerEnt.getText().length() == 5) {

                    txtDatGerEnt.setText(txtDatGerEnt.getText() + "/");
                    txtDatGerEnt.setCaretPosition(6);

                }

            }
            if (txtDatGerEnt.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtDatGerEntKeyTyped

    private void rbtnAceCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAceCadEstActionPerformed
        txtModCadEst.setEnabled(true);
        lblModCadEst.setEnabled(true);
        txtMarCadEst.setEnabled(true);
        lblMarCadEst.setEnabled(true);
        txtCorCadEst.setEnabled(true);
        lblCorCadEst.setEnabled(true);
        txtMatCadEst.setEnabled(true);
        lblMatCadEst.setEnabled(true);
        txtQuaCadEst.setEnabled(true);
        lblQuaCadEst.setEnabled(true);
        txtPreCadEst.setEnabled(true);
        lblPreCadEst.setEnabled(true);
        lblChiCadEst.setEnabled(false);
        cmbChiCadEst.setEnabled(false);
        txtLocCadEst.setEnabled(true);
        lblLocCadEst.setEnabled(true);
        txtDetCadEst.setEnabled(true);
        lblDetCadEst.setEnabled(true);

        sepModCadEst.setForeground(corforeazul);
        sepMarCadEst.setForeground(corforeazul);
        sepCorCadEst.setForeground(corforeazul);
        sepMatCadEst.setForeground(corforeazul);
        sepQuaCadEst.setForeground(corforeazul);
        sepPreCadEst.setForeground(corforeazul);
        sepLocCadEst.setForeground(corforeazul);
        sepDetCadEst.setForeground(corforeazul);

        txtMarCadEst.setText(null);
        txtModCadEst.setText(null);
        txtQuaCadEst.setText(null);
        txtPreCadEst.setText(null);
        txtCorCadEst.setText(null);
        txtMatCadEst.setText(null);
        txtLocCadEst.setText(null);
        txtDetCadEst.setText(null);
        lblR$CadEst.setVisible(false);
        cmbChiCadEst.setSelectedIndex(0);

        lblMarCadEst.setLocation(410, 80);
        lblModCadEst.setLocation(410, 130);
        lblQuaCadEst.setLocation(410, 180);
        lblPreCadEst.setLocation(410, 230);
        lblCorCadEst.setLocation(700, 80);
        lblMatCadEst.setLocation(700, 130);
        lblLocCadEst.setLocation(700, 180);
        lblDetCadEst.setLocation(700, 230);

        btnSalCadEst.setEnabled(true);
        btnCanCadEst.setEnabled(true);
        txtTipCadEst.setText("Acessório");
    }//GEN-LAST:event_rbtnAceCadEstActionPerformed

    private void rbtnChiCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnChiCadEstActionPerformed
        txtModCadEst.setEnabled(false);
        lblModCadEst.setEnabled(false);
        txtMarCadEst.setEnabled(false);
        lblMarCadEst.setEnabled(false);
        txtCorCadEst.setEnabled(false);
        lblCorCadEst.setEnabled(false);
        txtMatCadEst.setEnabled(false);
        lblMatCadEst.setEnabled(false);
        txtQuaCadEst.setEnabled(true);
        lblQuaCadEst.setEnabled(true);
        txtPreCadEst.setEnabled(true);
        lblPreCadEst.setEnabled(true);
        lblChiCadEst.setEnabled(true);
        cmbChiCadEst.setEnabled(true);
        txtLocCadEst.setEnabled(false);
        lblLocCadEst.setEnabled(false);
        txtDetCadEst.setEnabled(false);
        lblDetCadEst.setEnabled(false);

        sepModCadEst.setForeground(Color.GRAY);
        sepMarCadEst.setForeground(Color.GRAY);
        sepCorCadEst.setForeground(Color.GRAY);
        sepMatCadEst.setForeground(Color.GRAY);
        sepQuaCadEst.setForeground(corforeazul);
        sepPreCadEst.setForeground(corforeazul);
        sepLocCadEst.setForeground(Color.GRAY);
        sepDetCadEst.setForeground(Color.GRAY);

        txtMarCadEst.setText(null);
        txtModCadEst.setText(null);
        txtQuaCadEst.setText(null);
        txtPreCadEst.setText(null);
        txtCorCadEst.setText(null);
        txtMatCadEst.setText(null);
        txtLocCadEst.setText(null);
        txtDetCadEst.setText(null);
        lblR$CadEst.setVisible(false);
        cmbChiCadEst.setSelectedIndex(0);

        lblMarCadEst.setLocation(410, 80);
        lblModCadEst.setLocation(410, 130);
        lblQuaCadEst.setLocation(410, 180);
        lblPreCadEst.setLocation(410, 230);
        lblCorCadEst.setLocation(700, 80);
        lblMatCadEst.setLocation(700, 130);
        lblLocCadEst.setLocation(700, 180);
        lblDetCadEst.setLocation(700, 230);

        btnSalCadEst.setEnabled(true);
        btnCanCadEst.setEnabled(true);

        txtTipCadEst.setText("Chip");
    }//GEN-LAST:event_rbtnChiCadEstActionPerformed

    private void rbtnPelCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPelCadEstActionPerformed
        txtModCadEst.setEnabled(true);
        lblModCadEst.setEnabled(true);
        txtMarCadEst.setEnabled(true);
        lblMarCadEst.setEnabled(true);
        txtCorCadEst.setEnabled(true);
        lblCorCadEst.setEnabled(true);
        txtMatCadEst.setEnabled(true);
        lblMatCadEst.setEnabled(true);
        txtQuaCadEst.setEnabled(true);
        lblQuaCadEst.setEnabled(true);
        txtPreCadEst.setEnabled(true);
        lblPreCadEst.setEnabled(true);
        lblChiCadEst.setEnabled(false);
        cmbChiCadEst.setEnabled(false);
        txtLocCadEst.setEnabled(true);
        lblLocCadEst.setEnabled(true);
        txtDetCadEst.setEnabled(true);
        lblDetCadEst.setEnabled(true);

        sepModCadEst.setForeground(corforeazul);
        sepMarCadEst.setForeground(corforeazul);
        sepCorCadEst.setForeground(corforeazul);
        sepMatCadEst.setForeground(corforeazul);
        sepQuaCadEst.setForeground(corforeazul);
        sepPreCadEst.setForeground(corforeazul);
        sepLocCadEst.setForeground(corforeazul);
        sepDetCadEst.setForeground(corforeazul);

        txtMarCadEst.setText(null);
        txtModCadEst.setText(null);
        txtQuaCadEst.setText(null);
        txtPreCadEst.setText(null);
        txtCorCadEst.setText(null);
        txtMatCadEst.setText(null);
        txtLocCadEst.setText(null);
        txtDetCadEst.setText(null);
        lblR$CadEst.setVisible(false);
        cmbChiCadEst.setSelectedIndex(0);

        lblMarCadEst.setLocation(410, 80);
        lblModCadEst.setLocation(410, 130);
        lblQuaCadEst.setLocation(410, 180);
        lblPreCadEst.setLocation(410, 230);
        lblCorCadEst.setLocation(700, 80);
        lblMatCadEst.setLocation(700, 130);
        lblLocCadEst.setLocation(700, 180);
        lblDetCadEst.setLocation(700, 230);

        btnSalCadEst.setEnabled(true);
        btnCanCadEst.setEnabled(true);

        txtTipCadEst.setText("Película");
    }//GEN-LAST:event_rbtnPelCadEstActionPerformed

    private void rbtnCapCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCapCadEstActionPerformed
        txtModCadEst.setEnabled(true);
        lblModCadEst.setEnabled(true);
        txtMarCadEst.setEnabled(true);
        lblMarCadEst.setEnabled(true);
        txtCorCadEst.setEnabled(true);
        lblCorCadEst.setEnabled(true);
        txtMatCadEst.setEnabled(false);
        lblMatCadEst.setEnabled(false);
        txtQuaCadEst.setEnabled(true);
        lblQuaCadEst.setEnabled(true);
        txtPreCadEst.setEnabled(true);
        lblPreCadEst.setEnabled(true);
        lblChiCadEst.setEnabled(false);
        cmbChiCadEst.setEnabled(false);
        txtLocCadEst.setEnabled(true);
        lblLocCadEst.setEnabled(true);
        txtDetCadEst.setEnabled(true);
        lblDetCadEst.setEnabled(true);

        sepModCadEst.setForeground(corforeazul);
        sepMarCadEst.setForeground(corforeazul);
        sepCorCadEst.setForeground(corforeazul);
        sepMatCadEst.setForeground(Color.GRAY);
        sepQuaCadEst.setForeground(corforeazul);
        sepPreCadEst.setForeground(corforeazul);
        sepLocCadEst.setForeground(corforeazul);
        sepDetCadEst.setForeground(corforeazul);

        btnSalCadEst.setEnabled(true);
        btnCanCadEst.setEnabled(true);

        txtMarCadEst.setText(null);
        txtModCadEst.setText(null);
        txtQuaCadEst.setText(null);
        txtPreCadEst.setText(null);
        txtCorCadEst.setText(null);
        txtMatCadEst.setText(null);
        txtLocCadEst.setText(null);
        txtDetCadEst.setText(null);
        lblR$CadEst.setVisible(false);
        cmbChiCadEst.setSelectedIndex(0);

        lblMarCadEst.setLocation(410, 80);
        lblModCadEst.setLocation(410, 130);
        lblQuaCadEst.setLocation(410, 180);
        lblPreCadEst.setLocation(410, 230);
        lblCorCadEst.setLocation(700, 80);
        lblMatCadEst.setLocation(700, 130);
        lblLocCadEst.setLocation(700, 180);
        lblDetCadEst.setLocation(700, 230);

        txtTipCadEst.setText("Capinha");
    }//GEN-LAST:event_rbtnCapCadEstActionPerformed

    private void txtPreCadEstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreCadEstKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtPreCadEst.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPreCadEstKeyTyped

    private void txtPreCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreCadEstFocusLost
        if (txtPreCadEst.getText().isEmpty()) {
            anitxtout(lblPreCadEst);
            lblR$CadEst.setVisible(false);
        }
    }//GEN-LAST:event_txtPreCadEstFocusLost

    private void txtPreCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreCadEstFocusGained
        if (txtPreCadEst.getText().isEmpty()) {
            anitxtin(lblPreCadEst);
            lblR$CadEst.setVisible(true);
        }
    }//GEN-LAST:event_txtPreCadEstFocusGained

    private void btnGerOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerOsActionPerformed
        if (txtCliOs.getText().isEmpty() || txtEndOs.getText().isEmpty() || txtTelOs.getText().isEmpty() || txtEquOs.getText().isEmpty() || txtMarOs.getText().isEmpty() || txtModOs.getText().isEmpty() || txtConOs.getText().isEmpty() || txtDatOs.getText().isEmpty() || txtHorOs.getText().isEmpty() || txtDefOs.getText().isEmpty()) {

            JOptionPane.showMessageDialog(pnlOs, "Preencha todos os dados!", "Atenção", JOptionPane.WARNING_MESSAGE);

        } else {

            try {

                String datahora = txtDatOs.getText() + " - " + txtHorOs.getText();

                Map<String, Object> parameters = new HashMap<>();

                parameters.clear();

                parameters.put("LogoTim", getClass().getResourceAsStream("/images/LOGOTIM.png"));
                parameters.put("LogoLoja", getClass().getResourceAsStream("/images/LOGOLOJA.png"));
                parameters.put("Nome", txtCliOs.getText());
                parameters.put("Endereco", txtEndOs.getText());
                parameters.put("Telefone", txtTelOs.getText());
                parameters.put("Equipamento", txtEquOs.getText());
                parameters.put("Marca", txtMarOs.getText());
                parameters.put("Modelo", txtModOs.getText());
                parameters.put("Condicoes", txtConOs.getText());
                parameters.put("Defeito", txtDefOs.getText());
                parameters.put("DataEntrada", datahora);

                JasperPrint print = JasperFillManager.fillReport("C:\\Users\\Geral\\Documents\\NetBeansProjects\\EmporioSys\\src\\os\\OSEmpSysView.jasper", parameters, new JREmptyDataSource(1));

                JasperViewer jc = new JasperViewer(print, false);
                jc.setVisible(true);
                jc.toFront();

                pnlOs.setVisible(false);
                lblTitPri.setVisible(false);

            } catch (JRException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnGerOsActionPerformed

    private void btnCanOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanOsActionPerformed
        pnlOs.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnCanOsActionPerformed

    private void txtEndOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEndOsFocusGained
        if (txtEndOs.getText().isEmpty()) {
            anitxtin(lblEndOs);
        }
    }//GEN-LAST:event_txtEndOsFocusGained

    private void txtEndOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEndOsFocusLost
        if (txtEndOs.getText().isEmpty()) {
            anitxtout(lblEndOs);
        }
    }//GEN-LAST:event_txtEndOsFocusLost

    private void txtCliOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliOsFocusGained
        if (txtCliOs.getText().isEmpty()) {
            anitxtin(lblCliOs);
        }
    }//GEN-LAST:event_txtCliOsFocusGained

    private void txtCliOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliOsFocusLost
        if (txtCliOs.getText().isEmpty()) {
            anitxtout(lblCliOs);
        }
    }//GEN-LAST:event_txtCliOsFocusLost

    private void txtEquOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEquOsFocusGained
        if (txtEquOs.getText().isEmpty()) {
            anitxtin(lblEquOs);
        }
    }//GEN-LAST:event_txtEquOsFocusGained

    private void txtEquOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEquOsFocusLost
        if (txtEquOs.getText().isEmpty()) {
            anitxtout(lblEquOs);
        }
    }//GEN-LAST:event_txtEquOsFocusLost

    private void txtMarOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarOsFocusGained
        if (txtMarOs.getText().isEmpty()) {
            anitxtin(lblMarOs);
        }
    }//GEN-LAST:event_txtMarOsFocusGained

    private void txtMarOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarOsFocusLost
        if (txtMarOs.getText().isEmpty()) {
            anitxtout(lblMarOs);
        }
    }//GEN-LAST:event_txtMarOsFocusLost

    private void txtTelOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelOsFocusGained
        if (txtTelOs.getText().isEmpty()) {
            anitxtin(lblTelOs);
        }
    }//GEN-LAST:event_txtTelOsFocusGained

    private void txtTelOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelOsFocusLost
        if (txtTelOs.getText().isEmpty()) {
            anitxtout(lblTelOs);
        }
    }//GEN-LAST:event_txtTelOsFocusLost

    private void txtTelOsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelOsKeyTyped
        if (txtTelOs.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                switch (txtTelOs.getText().length()) {
                    case 0:
                        txtTelOs.setText(txtTelOs.getText() + "(");
                        txtTelOs.setCaretPosition(1);
                        break;
                    case 3:
                        txtTelOs.setText(txtTelOs.getText() + ") ");
                        txtTelOs.setCaretPosition(5);
                        break;
                    case 10:
                        txtTelOs.setText(txtTelOs.getText() + "-");
                        txtTelOs.setCaretPosition(11);
                        break;
                    default:
                        break;
                }

            }
            if (txtTelOs.getText().length() > 14) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtTelOsKeyTyped

    private void txtModOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModOsFocusGained
        if (txtModOs.getText().isEmpty()) {
            anitxtin(lblModOs);
        }
    }//GEN-LAST:event_txtModOsFocusGained

    private void txtModOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModOsFocusLost
        if (txtModOs.getText().isEmpty()) {
            anitxtout(lblModOs);
        }
    }//GEN-LAST:event_txtModOsFocusLost

    private void txtConOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConOsFocusGained
        if (txtConOs.getText().isEmpty()) {
            anitxtin(lblConOs);
        }
    }//GEN-LAST:event_txtConOsFocusGained

    private void txtConOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtConOsFocusLost
        if (txtConOs.getText().isEmpty()) {
            anitxtout(lblConOs);
        }
    }//GEN-LAST:event_txtConOsFocusLost

    private void txtDefOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDefOsFocusGained
        if (txtDefOs.getText().isEmpty()) {
            anitxtin(lblDefOs);
        }
    }//GEN-LAST:event_txtDefOsFocusGained

    private void txtDefOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDefOsFocusLost
        if (txtDefOs.getText().isEmpty()) {
            anitxtout(lblDefOs);
        }
    }//GEN-LAST:event_txtDefOsFocusLost

    private void txtDatOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatOsFocusGained
        if (txtDatOs.getText().isEmpty()) {
            anitxtin(lblDatOs);
        }
    }//GEN-LAST:event_txtDatOsFocusGained

    private void txtDatOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatOsFocusLost
        if (txtDatOs.getText().isEmpty()) {
            anitxtout(lblDatOs);
        }
    }//GEN-LAST:event_txtDatOsFocusLost

    private void txtDatOsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatOsKeyTyped
        if (txtDatOs.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatOs.getText().length() == 2) {

                    txtDatOs.setText(txtDatOs.getText() + "/");
                    txtDatOs.setCaretPosition(3);

                } else if (txtDatOs.getText().length() == 5) {

                    txtDatOs.setText(txtDatOs.getText() + "/");
                    txtDatOs.setCaretPosition(6);

                }

            }
            if (txtDatOs.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtDatOsKeyTyped

    private void txtHorOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHorOsFocusGained
        if (txtHorOs.getText().isEmpty()) {
            anitxtin(lblHorOs);
        }
    }//GEN-LAST:event_txtHorOsFocusGained

    private void txtHorOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHorOsFocusLost
        if (txtHorOs.getText().isEmpty()) {
            anitxtout(lblHorOs);
        }
    }//GEN-LAST:event_txtHorOsFocusLost

    private void txtHorOsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorOsKeyTyped
        if (txtHorOs.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtHorOs.getText().length() == 2) {

                    txtHorOs.setText(txtHorOs.getText() + ":");
                    txtHorOs.setCaretPosition(3);

                }
                if (txtHorOs.getText().length() > 4) {
                    evt.consume();
                }
            }
        }
    }//GEN-LAST:event_txtHorOsKeyTyped

    private void btnOrdSerPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOrdSerPriMouseReleased
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        txtCliOs.setText(null);
        txtEndOs.setText(null);
        txtTelOs.setText(null);
        txtEquOs.setText(null);
        txtMarOs.setText(null);
        txtModOs.setText(null);
        txtConOs.setText(null);
        txtDefOs.setText(null);
        txtHorOs.setText(LocalTime.now().format(formatoHora));
        txtDatOs.setText(LocalDate.now().format(formatteratual));

        lblCliOs.setLocation(370, 30);
        lblTelOs.setLocation(370, 80);
        lblEndOs.setLocation(370, 130);
        lblDatOs.setLocation(370, 160);
        lblHorOs.setLocation(370, 210);
        lblEquOs.setLocation(700, 30);
        lblMarOs.setLocation(700, 80);
        lblModOs.setLocation(700, 130);
        lblConOs.setLocation(700, 180);
        lblDefOs.setLocation(700, 230);

        lblTitPri.setText("Ordem de Serviço");
        lblTitPri.setVisible(true);

        pnlCadEnt.setVisible(false);
        pnlRel.setVisible(false);
        pnlIteCadEnt.setVisible(false);
        pnlIteGerEnt.setVisible(false);
        pnlCadTipSer.setVisible(false);
        pnlCadEst.setVisible(false);
        pnlConEst.setVisible(false);
        pnlGerEst.setVisible(false);
        pnlGerTipSer.setVisible(false);
        pnlMas.setVisible(false);
        pnlDes.setVisible(false);
        pnlGerDes.setVisible(false);
        pnlCadDes.setVisible(false);
        pnlGerEnt.setVisible(false);

        pnlOs.setVisible(true);
    }//GEN-LAST:event_btnOrdSerPriMouseReleased
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            new main().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltGerDes;
    private javax.swing.JButton btnAltGerEnt;
    private javax.swing.JButton btnAltGerEst;
    private javax.swing.JButton btnAltGerTipSer;
    private javax.swing.JLabel btnAnoRel;
    private javax.swing.JButton btnBusConEst;
    private javax.swing.JButton btnBusGerEnt;
    private javax.swing.JButton btnBusGerEst;
    private javax.swing.JLabel btnCadDes;
    private javax.swing.JLabel btnCadEnt;
    private javax.swing.JLabel btnCadEst;
    private javax.swing.JLabel btnCadTipSer;
    private javax.swing.JButton btnCanCadEnt;
    private javax.swing.JButton btnCanCadEst;
    private javax.swing.JButton btnCanConEst;
    private javax.swing.JButton btnCanDes;
    private javax.swing.JButton btnCanGerDes;
    private javax.swing.JButton btnCanGerEnt;
    private javax.swing.JButton btnCanGerEst;
    private javax.swing.JButton btnCanGerTipSer;
    private javax.swing.JButton btnCanMas;
    private javax.swing.JButton btnCanOs;
    private javax.swing.JButton btnCanTipSer;
    private javax.swing.JLabel btnConEst;
    private javax.swing.JLabel btnCopMas;
    private javax.swing.JLabel btnDes;
    private javax.swing.JLabel btnDiaRel;
    private javax.swing.JLabel btnEntPri;
    private javax.swing.JLabel btnEstPri;
    private javax.swing.JButton btnExcGerDes;
    private javax.swing.JButton btnExcGerEnt;
    private javax.swing.JButton btnExcGerEst;
    private javax.swing.JButton btnExcGerTipSer;
    private javax.swing.JLabel btnGerDes;
    private javax.swing.JLabel btnGerEnt;
    private javax.swing.JLabel btnGerEst;
    private javax.swing.JButton btnGerMas;
    private javax.swing.JButton btnGerOs;
    private javax.swing.JLabel btnGerTipSer;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.ButtonGroup btnGroup1;
    private javax.swing.JButton btnIteCadEnt;
    private javax.swing.JButton btnIteGerEnt;
    private javax.swing.JLabel btnMasPla;
    private javax.swing.JLabel btnMesRel;
    private javax.swing.JLabel btnOrdSerPri;
    private javax.swing.JLabel btnOutPri;
    private javax.swing.JLabel btnRelPri;
    private javax.swing.JButton btnSalCadEnt;
    private javax.swing.JButton btnSalCadEst;
    private javax.swing.JButton btnSalDes;
    private javax.swing.JButton btnSalTipSer;
    private javax.swing.JLabel btnSemRel;
    private javax.swing.JLabel btnTipSerPri;
    private javax.swing.JLabel btnTodRel;
    private javax.swing.JButton btnVolDes;
    private javax.swing.JButton btnVolIteCadEnt;
    private javax.swing.JButton btnVolIteGerEnt;
    private javax.swing.JButton btnVolRel;
    private javax.swing.JCheckBox chkC6Mas;
    private javax.swing.JComboBox<String> cmbChiCadEst;
    private javax.swing.JComboBox<String> cmbChiGerEst;
    private javax.swing.JComboBox<String> cmbSerCadEnt;
    private javax.swing.JComboBox<String> cmbSerGerEnt;
    private javax.swing.JLabel imgLogo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBusConEst;
    private javax.swing.JLabel lblBusGerEst;
    private javax.swing.JLabel lblBusIteCadEnt;
    private javax.swing.JLabel lblBusIteGerEnt;
    private javax.swing.JLabel lblChiCadEst;
    private javax.swing.JLabel lblChiGerEst;
    private javax.swing.JLabel lblCliOs;
    private javax.swing.JLabel lblConOs;
    private javax.swing.JLabel lblCorCadEst;
    private javax.swing.JLabel lblCorGerEst;
    private javax.swing.JLabel lblCpfMas;
    private javax.swing.JLabel lblDatBusGerEnt;
    private javax.swing.JLabel lblDatCadEnt;
    private javax.swing.JLabel lblDatDes;
    private javax.swing.JLabel lblDatFinRel;
    private javax.swing.JLabel lblDatGerDes;
    private javax.swing.JLabel lblDatGerEnt;
    private javax.swing.JLabel lblDatIniRel;
    private javax.swing.JLabel lblDatOs;
    private javax.swing.JLabel lblDefOs;
    private javax.swing.JLabel lblDesDes;
    private javax.swing.JLabel lblDesGerDes;
    private javax.swing.JLabel lblDesGerTipSer;
    private javax.swing.JLabel lblDesTipSer;
    private javax.swing.JLabel lblDesTipSer2;
    private javax.swing.JLabel lblDesTipSer3;
    private javax.swing.JLabel lblDetCadEnt;
    private javax.swing.JLabel lblDetCadEst;
    private javax.swing.JLabel lblDetGerEnt;
    private javax.swing.JLabel lblDetGerEst;
    private javax.swing.JLabel lblEndOs;
    private javax.swing.JLabel lblEquOs;
    private javax.swing.JLabel lblEstIteCadEnt;
    private javax.swing.JLabel lblEstIteGerEnt;
    private javax.swing.JLabel lblHorOs;
    private javax.swing.JLabel lblLocCadEst;
    private javax.swing.JLabel lblLocGerEst;
    private javax.swing.JLabel lblMarCadEst;
    private javax.swing.JLabel lblMarGerEst;
    private javax.swing.JLabel lblMarOs;
    private javax.swing.JLabel lblMatCadEst;
    private javax.swing.JLabel lblMatGerEst;
    private javax.swing.JLabel lblModCadEst;
    private javax.swing.JLabel lblModGerEst;
    private javax.swing.JLabel lblModOs;
    private javax.swing.JLabel lblNomMas;
    private javax.swing.JLabel lblNumAceMas;
    private javax.swing.JLabel lblNumConMas;
    private javax.swing.JLabel lblNumPorMas;
    private javax.swing.JLabel lblPlaMas;
    private javax.swing.JLabel lblPreCadEnt;
    private javax.swing.JLabel lblPreCadEst;
    private javax.swing.JLabel lblPreDes;
    private javax.swing.JLabel lblPreGerDes;
    private javax.swing.JLabel lblPreGerEnt;
    private javax.swing.JLabel lblPreGerEst;
    private javax.swing.JLabel lblQuaCadEst;
    private javax.swing.JLabel lblQuaGerEst;
    private javax.swing.JLabel lblR$CadEnt;
    private javax.swing.JLabel lblR$CadEst;
    private javax.swing.JLabel lblR$Des;
    private javax.swing.JLabel lblR$GerDes;
    private javax.swing.JLabel lblR$GerEnt;
    private javax.swing.JLabel lblR$GerEst;
    private javax.swing.JLabel lblSelIteCadEnt;
    private javax.swing.JLabel lblSelIteGerEnt;
    private javax.swing.JLabel lblSerCadEnt;
    private javax.swing.JLabel lblSerGerEnt;
    private javax.swing.JLabel lblTelOs;
    private javax.swing.JLabel lblTitPri;
    private javax.swing.JLabel lblTotEntRel;
    private javax.swing.JLabel lblValMedRel;
    private javax.swing.JLabel lblValTotRel;
    private javax.swing.JLabel lblVenMas;
    private javax.swing.JPanel pnlCadDes;
    private javax.swing.JPanel pnlCadEnt;
    private javax.swing.JPanel pnlCadEst;
    private javax.swing.JPanel pnlCadTipSer;
    private javax.swing.JPanel pnlConEst;
    private javax.swing.JPanel pnlDes;
    private javax.swing.JPanel pnlGerDes;
    private javax.swing.JPanel pnlGerEnt;
    private javax.swing.JPanel pnlGerEst;
    private javax.swing.JPanel pnlGerTipSer;
    private javax.swing.JPanel pnlIteCadEnt;
    private javax.swing.JPanel pnlIteGerEnt;
    private javax.swing.JPanel pnlMas;
    private javax.swing.JPanel pnlOs;
    private javax.swing.JPanel pnlPri;
    private javax.swing.JPanel pnlRel;
    private javax.swing.JRadioButton rbtnAceCadEst;
    private javax.swing.JRadioButton rbtnAceConEst;
    private javax.swing.JRadioButton rbtnAceGerEst;
    private javax.swing.JRadioButton rbtnAppMas;
    private javax.swing.JRadioButton rbtnAssCadEnt;
    private javax.swing.JRadioButton rbtnAssIteCadEnt;
    private javax.swing.JRadioButton rbtnAssIteGerEnt;
    private javax.swing.JRadioButton rbtnAssRel;
    private javax.swing.JRadioButton rbtnAssTipSer;
    private javax.swing.JRadioButton rbtnAtiMas;
    private javax.swing.JRadioButton rbtnCapCadEst;
    private javax.swing.JRadioButton rbtnCapConEst;
    private javax.swing.JRadioButton rbtnCapGerEst;
    private javax.swing.JRadioButton rbtnCapIteCadEnt;
    private javax.swing.JRadioButton rbtnCapIteGerEnt;
    private javax.swing.JRadioButton rbtnChiCadEst;
    private javax.swing.JRadioButton rbtnChiConEst;
    private javax.swing.JRadioButton rbtnChiGerEst;
    private javax.swing.JRadioButton rbtnChiIteCadEnt;
    private javax.swing.JRadioButton rbtnChiIteGerEnt;
    private javax.swing.JRadioButton rbtnMigMas;
    private javax.swing.JRadioButton rbtnMigTroMas;
    private javax.swing.JRadioButton rbtnOutTipSer;
    private javax.swing.JRadioButton rbtnPelCadEst;
    private javax.swing.JRadioButton rbtnPelConEst;
    private javax.swing.JRadioButton rbtnPelGerEst;
    private javax.swing.JRadioButton rbtnPelIteCadEnt;
    private javax.swing.JRadioButton rbtnPelIteGerEnt;
    private javax.swing.JRadioButton rbtnSerCadEnt;
    private javax.swing.JRadioButton rbtnSerRel;
    private javax.swing.JRadioButton rbtnSerTimRel;
    private javax.swing.JRadioButton rbtnSerTimTipSer;
    private javax.swing.JRadioButton rbtnSieMas;
    private javax.swing.JRadioButton rbtnTodRel;
    private javax.swing.JRadioButton rbtnVenCadEnt;
    private javax.swing.JRadioButton rbtnVenRel;
    private javax.swing.JScrollPane scrConDes;
    private javax.swing.JScrollPane scrConEst;
    private javax.swing.JScrollPane scrEstIteCadEnt;
    private javax.swing.JScrollPane scrEstIteGerEnt;
    private javax.swing.JScrollPane scrGerDes;
    private javax.swing.JScrollPane scrGerEnt;
    private javax.swing.JScrollPane scrGerEst;
    private javax.swing.JScrollPane scrRel;
    private javax.swing.JScrollPane scrSelIteCadEnt;
    private javax.swing.JScrollPane scrSelIteGerEnt;
    private javax.swing.JScrollPane scrTipSer;
    private javax.swing.JSeparator sepBusConEst;
    private javax.swing.JSeparator sepBusGerEst;
    private javax.swing.JSeparator sepBusGerEst1;
    private javax.swing.JSeparator sepBusIteCadEnt;
    private javax.swing.JSeparator sepBusIteCadEnt1;
    private javax.swing.JSeparator sepCorCadEst;
    private javax.swing.JSeparator sepCorCadEst1;
    private javax.swing.JSeparator sepCorGerEst;
    private javax.swing.JSeparator sepDatCadEnt;
    private javax.swing.JSeparator sepDatCadEnt1;
    private javax.swing.JSeparator sepDatCadEnt2;
    private javax.swing.JSeparator sepDatCadEnt3;
    private javax.swing.JSeparator sepDatCadEnt4;
    private javax.swing.JSeparator sepDatCadEnt6;
    private javax.swing.JSeparator sepDatCadEnt7;
    private javax.swing.JSeparator sepDatGerDes;
    private javax.swing.JSeparator sepDatGerEnt;
    private javax.swing.JSeparator sepDesGerDes;
    private javax.swing.JSeparator sepDesGerTipSer;
    private javax.swing.JSeparator sepDesGerTipSer1;
    private javax.swing.JSeparator sepDesGerTipSer2;
    private javax.swing.JSeparator sepDesGerTipSer3;
    private javax.swing.JSeparator sepDesGerTipSer4;
    private javax.swing.JSeparator sepDesGerTipSer5;
    private javax.swing.JSeparator sepDesGerTipSer6;
    private javax.swing.JSeparator sepDesGerTipSer7;
    private javax.swing.JSeparator sepDesGerTipSer8;
    private javax.swing.JSeparator sepDesGerTipSer9;
    private javax.swing.JSeparator sepDesTipSer;
    private javax.swing.JSeparator sepDetCadEnt;
    private javax.swing.JSeparator sepDetCadEst;
    private javax.swing.JSeparator sepDetCadEst1;
    private javax.swing.JSeparator sepDetCadEst2;
    private javax.swing.JSeparator sepDetCadEst3;
    private javax.swing.JSeparator sepDetCadEst4;
    private javax.swing.JSeparator sepDetGerEnt;
    private javax.swing.JSeparator sepDetGerEst;
    private javax.swing.JSeparator sepLocCadEst;
    private javax.swing.JSeparator sepLocCadEst1;
    private javax.swing.JSeparator sepLocGerEst;
    private javax.swing.JSeparator sepMarCadEst;
    private javax.swing.JSeparator sepMarCadEst1;
    private javax.swing.JSeparator sepMarGerEst;
    private javax.swing.JSeparator sepMatCadEst;
    private javax.swing.JSeparator sepMatCadEst1;
    private javax.swing.JSeparator sepMatGerEst;
    private javax.swing.JSeparator sepMod2;
    private javax.swing.JSeparator sepMod3;
    private javax.swing.JSeparator sepModCadEst;
    private javax.swing.JSeparator sepModCadEst1;
    private javax.swing.JSeparator sepModGerEst;
    private javax.swing.JSeparator sepPreCadEnt;
    private javax.swing.JSeparator sepPreCadEst;
    private javax.swing.JSeparator sepPreGerDes;
    private javax.swing.JSeparator sepPreGerEnt;
    private javax.swing.JSeparator sepPreGerEst;
    private javax.swing.JSeparator sepQuaCadEst;
    private javax.swing.JSeparator sepQuaCadEst1;
    private javax.swing.JSeparator sepQuaGerEst;
    private javax.swing.JTable tblConDes;
    private javax.swing.JTable tblConEst;
    private javax.swing.JTable tblEstIteCadEnt;
    private javax.swing.JTable tblEstIteGerEnt;
    private javax.swing.JTable tblGerDes;
    private javax.swing.JTable tblGerEnt;
    private javax.swing.JTable tblGerEst;
    private javax.swing.JTable tblRel;
    private javax.swing.JTable tblSelIteCadEnt;
    private javax.swing.JTable tblSelIteGerEnt;
    private javax.swing.JTable tblTipSer;
    private javax.swing.JTextArea txtAreMas;
    private javax.swing.JTextField txtBusConEst;
    private javax.swing.JTextField txtBusGerEst;
    private javax.swing.JTextField txtBusIteCadEnt;
    private javax.swing.JTextField txtBusIteGerEnt;
    private javax.swing.JTextField txtCliOs;
    private javax.swing.JTextField txtCodCadEnt;
    private javax.swing.JTextField txtConOs;
    private javax.swing.JTextField txtCorCadEst;
    private javax.swing.JTextField txtCorGerEst;
    private javax.swing.JTextField txtCpfMas;
    private javax.swing.JTextField txtDatBusGerEnt;
    private javax.swing.JTextField txtDatCadEnt;
    private javax.swing.JTextField txtDatDes;
    private javax.swing.JTextField txtDatFinRel;
    private javax.swing.JTextField txtDatGerDes;
    private javax.swing.JTextField txtDatGerEnt;
    private javax.swing.JTextField txtDatIniRel;
    private javax.swing.JTextField txtDatOs;
    private javax.swing.JTextField txtDefOs;
    private javax.swing.JTextField txtDesDes;
    private javax.swing.JTextField txtDesGerDes;
    private javax.swing.JTextField txtDesGerTipSer;
    private javax.swing.JTextField txtDesTipSer;
    private javax.swing.JTextField txtDetCadEnt;
    private javax.swing.JTextField txtDetCadEst;
    private javax.swing.JTextField txtDetGerEnt;
    private javax.swing.JTextField txtDetGerEst;
    private javax.swing.JTextField txtEndOs;
    private javax.swing.JTextField txtEquOs;
    private javax.swing.JTextField txtHorOs;
    private javax.swing.JTextField txtLocCadEst;
    private javax.swing.JTextField txtLocGerEst;
    private javax.swing.JTextField txtMarCadEst;
    private javax.swing.JTextField txtMarGerEst;
    private javax.swing.JTextField txtMarOs;
    private javax.swing.JTextField txtMatCadEst;
    private javax.swing.JTextField txtMatGerEst;
    private javax.swing.JTextField txtModCadEst;
    private javax.swing.JTextField txtModGerEst;
    private javax.swing.JTextField txtModOs;
    private javax.swing.JTextField txtNomMas;
    private javax.swing.JTextField txtNumAceMas;
    private javax.swing.JTextField txtNumConMas;
    private javax.swing.JTextField txtNumPorMas;
    private javax.swing.JTextField txtPlaMas;
    private javax.swing.JTextField txtPreCadEnt;
    private javax.swing.JTextField txtPreCadEst;
    private javax.swing.JTextField txtPreDes;
    private javax.swing.JTextField txtPreGerDes;
    private javax.swing.JTextField txtPreGerEnt;
    private javax.swing.JTextField txtPreGerEst;
    private javax.swing.JTextField txtQuaCadEst;
    private javax.swing.JTextField txtQuaGerEst;
    private javax.swing.JTextField txtTelOs;
    private javax.swing.JTextField txtTipCadEst;
    private javax.swing.JTextField txtTipConEst;
    private javax.swing.JTextField txtTipGerEst;
    private javax.swing.JTextField txtVenMas;
    // End of variables declaration//GEN-END:variables
}
