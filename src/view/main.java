package view;

import com.formdev.flatlaf.FlatLightLaf;
import dao.connection;
import dao.despezasDAO;
import dao.entradaDAO;
import dao.estoqueDAO;
import dao.osDAO;
import dao.planosDAO;
import dao.planosdiaDAO;
import dao.tiposervicoDAO;
import dao.vencimentoDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import model.estoque;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import model.despezas;
import model.entrada;
import model.tiposervico;
import model.vencimento;
import model.os;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.pdfbox.Loader;

public final class main extends javax.swing.JFrame {

    public main() {

        initComponents();
        setLocationRelativeTo(null);
        loading();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                try {
                    if (connection.connection != null) {
                        entrada en = new entrada();
                        entradaDAO endao = new entradaDAO();

                        if (tblSelIteCadEnt.getRowCount() != 0) {

                            for (int i = 1; i <= tblSelIteCadEnt.getRowCount(); i++) {

                                en.setQuantidade(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 0).toString()));
                                en.setIdestoque(Integer.parseInt(tblSelIteCadEnt.getValueAt(i - 1, 1).toString()));

                                endao.atualizarestoque(en, 1);

                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (connection.connection != null) {
                        entrada en = new entrada();
                        entradaDAO endao = new entradaDAO();

                        if (tblSelIteCadEnt.getRowCount() != 0) {

                            for (int i = 0; i <= tblSelIteCadEnt.getRowCount(); i++) {

                                en.setQuantidade(Integer.parseInt(tblSelIteCadEnt.getValueAt(i, 0).toString()));
                                en.setIdestoque(Integer.parseInt(tblSelIteCadEnt.getValueAt(i, 1).toString()));

                                endao.atualizarestoque(en, 1);

                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

    }

    public void loading() {

        loading lo = new loading();
        lo.setVisible(true);

        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {

                publish("Conectando ao banco de dados...");
                connection.Connect();

                if (connection.connection == null) {

                    publish("Erro na conexão ao banco de dados!");

                    Thread.sleep(3000);

                    System.exit(0);

                } else {

                    publish("Verificando dados...");

                    if (!verificavencimento()) {

                        publish("Erro na verificação!");

                        Thread.sleep(3000);

                        System.exit(0);

                    } else {

                        publish("Fazendo backup automático...");
                        if (backupdatabase()) {
                            publish("Backup concluído com sucesso! Iniciando...");
                            lblBakPri.setVisible(false);
                        } else {

                            publish("Atenção, erro na conclusão do backup!");

                            lblBakPri.setVisible(true);
                            btnTenPri.setVisible(true);
                            lblBakPri.setText("Atenção, erro na conclusão do backup!");

                        }

                        Thread.sleep(3000);

                        lo.dispose();

                    }

                }

                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {

                for (String mensagem : chunks) {
                    lo.lblLoa.setText(mensagem);
                }
            }

            @Override
            protected void done() {

                setVisible(true);
                pnlPri.setVisible(true);
                iniciasistema();

            }
        };

        worker.execute();
    }

    public void iniciasistema() {

        pnlCadEst.setVisible(false);
        pnlConEst.setVisible(false);
        pnlCadTipSer.setVisible(false);
        pnlGerTipSer.setVisible(false);
        pnlConEnt.setVisible(false);
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
        pnlCadVen.setVisible(false);
        pnlVen.setVisible(false);
        pnlJur.setVisible(false);
        pnlGerOs.setVisible(false);

        txtTipCadEst.setVisible(false);
        lblR$CadEst.setVisible(false);
        lblR$Des.setVisible(false);
        txtTipConEst.setVisible(false);
        lblTitPri.setVisible(false);
        txtCodCadEnt.setVisible(false);

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);

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
        btnConEnt.setVisible(false);
        btnGerDes.setVisible(false);
        btnGerEnt.setVisible(false);
        btnVen.setVisible(false);
        btnCadVen.setVisible(false);
        btnJurPri.setVisible(false);
        btnGerOsPri.setVisible(false);
        btnCadOsPri.setVisible(false);

    }

    ActionEvent timerven = null;

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

    public Font fontblack(int size) {

        try {
            Font fonte = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fonts/Poppins-ExtraBold.ttf"));
            return fonte.deriveFont((float) size);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean backupdatabase() {

        try {

            lblBakPri.setText("Backup automático em andamento...");
            btnTenPri.setVisible(false);
            lblBakPri.setVisible(true);

            LocalDateTime data = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
            String dataFormatada = data.format(formato);

            String databaseName = "empsysdatabase";
            String outputFile = "\\\\PC\\Arquivos\\BackupDatabase\\reg\\bkp-" + dataFormatada + ".sql";

            String loginFilePath = "\\\\PC\\Arquivos\\BackupDatabase\\bin\\bkp.cnf";
            String host = "192.168.0.101";

            String[] command = {
                "\\\\PC\\Arquivos\\BackupDatabase\\bin\\mysqldump",
                "--defaults-extra-file=" + loginFilePath,
                "--host=" + host,
                "--databases",
                databaseName,
                "--result-file=" + outputFile
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            Timer timer = new Timer(1000, new ActionListener() {
                int n = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    n++;

                    if (n >= 5) {

                        if (exitCode == 0) {
                            lblBakPri.setText("Backup concluído com sucesso!");

                            if (n >= 8) {
                                lblBakPri.setVisible(false);
                                ((Timer) e.getSource()).stop();
                            }

                        } else {

                            ((Timer) e.getSource()).stop();

                            lblBakPri.setVisible(true);
                            btnTenPri.setVisible(true);
                            lblBakPri.setText("Atenção, erro na conclusão do backup!");

                        }
                    }
                }
            });

            timer.start();

            if (exitCode != 0) {

                return false;
            }

        } catch (IOException | InterruptedException ex) {
            return false;
        }
        return true;
    }

    public void autocoluna(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 250;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
                columnModel.getColumn(column).setPreferredWidth(width);
            }

        }
    }

    private boolean tabelatiposervico() {

        try {

            tiposervicoDAO tsdao = new tiposervicoDAO();

            List<tiposervico> lista = tsdao.buscartodos();

            if (!lista.isEmpty()) {

                DefaultTableModel modelo = new DefaultTableModel() {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return getValueAt(0, columnIndex).getClass();
                    }
                };

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, 0);
                        int atvValue = (int) table.getValueAt(row, 3); // Assumindo que a coluna 1 contém o valor de getAtv

                        if (atvValue == 0) {
                            comp.setForeground(new Color(200, 200, 200));
                        } else {
                            comp.setForeground(table.getForeground());
                        }

                        return comp;
                    }
                };

                centerRenderer.setHorizontalAlignment(JLabel.LEFT);

                modelo.addColumn("ID");
                modelo.addColumn("Area");
                modelo.addColumn("Descrição");
                modelo.addColumn("Ativo");

                for (tiposervico ts : lista) {
                    Object[] rowData = {
                        ts.getIdtiposervico(),
                        ts.getArea(),
                        ts.getDescricao(),
                        ts.getAtv()
                    };

                    modelo.addRow(rowData);
                }

                tblTipSer.setModel(modelo);

                tblTipSer.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Aplicando o renderer à coluna 1

                tblTipSer.setRowHeight(25);

                tblTipSer.setDefaultRenderer(Object.class, centerRenderer);

                tblTipSer.setDefaultEditor(Object.class, null);

                tblTipSer.getColumnModel().getColumn(1).setMinWidth(0);
                tblTipSer.getColumnModel().getColumn(1).setMaxWidth(0);
                tblTipSer.getColumnModel().getColumn(1).setWidth(0);

                tblTipSer.getColumnModel().getColumn(0).setMinWidth(0);
                tblTipSer.getColumnModel().getColumn(0).setMaxWidth(0);
                tblTipSer.getColumnModel().getColumn(0).setWidth(0);

                tblTipSer.getColumnModel().getColumn(3).setMinWidth(0);
                tblTipSer.getColumnModel().getColumn(3).setMaxWidth(0);
                tblTipSer.getColumnModel().getColumn(3).setWidth(0);

                scrTipSer.getVerticalScrollBar().setValue(0);

            } else {

                return false;

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private boolean verificavencimento() {

        try {

            if (timerven != null) {

                ((Timer) timerven.getSource()).stop();

            }

            vencimentoDAO ve = new vencimentoDAO();

            if (ve.verificar()) {

                btnVenPri.setVisible(true);

                Timer timer = new Timer(700, new ActionListener() {

                    int n = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        timerven = e;

                        n++;

                        if (n % 2 == 0) {

                            btnVenPri.setText("Vencimento encontrado!");
                        } else {

                            btnVenPri.setText("");
                        }
                    }

                });
                timer.start();

            } else {

                btnVenPri.setVisible(false);

            }

        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    private static String capitalizeFirstLetterOfEachWord(String text) {
        StringBuilder sb = new StringBuilder();

        // Dividir o texto em palavras
        String[] words = text.split("\\s+");

        // Capitalizar a primeira letra de cada palavra
        for (String word : words) {
            if (!word.isEmpty()) {
                if (word.length() > 2) {
                    // Para palavras com mais de duas letras, capitalizar a primeira letra e concatenar com o restante da palavra em minúsculas
                    sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
                } else {
                    // Para palavras com duas ou menos letras, manter a palavra em minúsculas
                    sb.append(word.toLowerCase());
                }
                sb.append(" ");
            }
        }

        // Remover o espaço extra no final e retornar o texto capitalizado
        return sb.toString().trim();
    }

    private double juros(int parcela) {

        if (parcela == 0) {
            return 1.99;
        } else if (parcela == 1) {
            return 4.98;
        } else if (parcela == 2) {
            return 9.90;
        } else if (parcela == 3) {
            return 11.28;
        } else if (parcela == 4) {
            return 12.64;
        } else if (parcela == 5) {
            return 13.97;
        } else if (parcela == 6) {
            return 15.27;
        } else if (parcela == 7) {
            return 16.55;
        } else if (parcela == 8) {
            return 17.81;
        } else if (parcela == 9) {
            return 19.04;
        } else if (parcela == 10) {
            return 20.24;
        } else if (parcela == 11) {
            return 21.43;
        } else if (parcela == 12) {
            return 22.59;
        }

//        if (parcela == 0) {
//            return 1.68;
//        } else if (parcela == 1) {
//            return 3.48;
//        } else if (parcela == 2) {
//            return 8.99;
//        } else if (parcela == 3) {
//            return 10.99;
//        } else if (parcela == 4) {
//            return 11.99;
//        } else if (parcela == 5) {
//            return 12.99;
//        } else if (parcela == 6) {
//            return 13.99;
//        } else if (parcela == 7) {
//            return 14.99;
//        } else if (parcela == 8) {
//            return 15.99;
//        } else if (parcela == 9) {
//            return 16.99;
//        } else if (parcela == 10) {
//            return 17.99;
//        } else if (parcela == 11) {
//            return 17.99;
//        } else if (parcela == 12) {
//            return 17.99;
//        }
        return parcela;

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

                tbl.getColumnModel().getColumn(0).setMinWidth(0);
                tbl.getColumnModel().getColumn(0).setMaxWidth(0);
                tbl.getColumnModel().getColumn(0).setWidth(0);

            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    List<estoque> lista1;

    private boolean tabelaprodutoregistrado(estoque es, JTable tbl, JScrollPane scr) {
        try {

            estoqueDAO esdao = new estoqueDAO();
            List<estoque> lista = esdao.buscarprodutoregistrado(es);

            lista1 = esdao.buscarprodutoregistrado(es);

            if (!lista.isEmpty()) {

                JTableHeader header = tbl.getTableHeader();
                DefaultTableModel modelo = new DefaultTableModel();

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

                String[] colunas = {
                    "Modelo",
                    "Cor"

                };

                for (String coluna : colunas) {

                    modelo.addColumn(coluna);

                }

                for (estoque ess : lista) {

                    Object[] rowData = new Object[modelo.getColumnCount()];

                    for (int i = 0; i < modelo.getColumnCount(); i++) {

                        String columnName = modelo.getColumnName(i);
                        Object columnValue = getColumnValue(ess, columnName);

                        if (columnName.equals("Cor") && (columnValue == null || columnValue.toString().isEmpty())) {
                            columnValue = "Não Aplicável";
                        }
                        rowData[i] = columnValue;

                    }

                    modelo.addRow(rowData);
                }

                deheader.setHorizontalAlignment(JLabel.CENTER);
                deheader.setForeground(Color.BLACK);
                deheader.setFont(fontmed(11));

                header.setForeground(corforeazul);
                header.setBackground(new Color(246, 246, 246));

                header.setFont(fontbold(11));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {

                    tbl.getColumnModel().getColumn(i).setCellRenderer(deheader);

                }

                tbl.getColumnModel().getColumn(1).setWidth(5);

            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private boolean tabelaconsultarentrada(JTable tbl, JScrollPane scr, String busca) {

        try {

            entradaDAO endao = new entradaDAO();

            List<String[]> lista = endao.buscar(busca);

            if (!lista.isEmpty()) {

                DefaultTableModel modelo = new DefaultTableModel();

                JTableHeader header = tbl.getTableHeader();

                modelo.addColumn("Data");
                modelo.addColumn("Área");
                modelo.addColumn("Serviço");
                modelo.addColumn("Cliente");
                modelo.addColumn("Produto");
                modelo.addColumn("Preço");
                modelo.addColumn("Custo");
                modelo.addColumn("Pagamento");
                modelo.addColumn("Fornecedor");
                modelo.addColumn("Quantidade");
                modelo.addColumn("Detalhes");
                modelo.addColumn("Código Entrada");

                for (String[] row : lista) {

                    Object[] rowData = new Object[12];

                    Date date = formatterbanco.parse(row[0]);
                    rowData[0] = formatter.format(date);
                    rowData[1] = row[1];
                    rowData[2] = ((row[2] != null) ? row[2] : "Nenhum Serviço");
                    rowData[3] = ("Assistência".equals(row[1]) && "".equals(row[3])) ? "Não Informado"
                            : (!"Assistência".equals(row[1]) && row[3] == null) ? "Não Aplicável" : row[3];
                    rowData[4] = (!"null - null null - null".equals(row[4]) && row[4] != null) ? row[4] : "Nenhum Produto";
                    rowData[5] = (!row[5].equals("0.0")) ? moedadoublereal(Double.valueOf(row[5])) : "Não Aplicável";
                    rowData[6] = (row[6] != null) ? moedadoublereal(Double.valueOf(row[6])) : "Não Aplicável";
                    rowData[7] = ("1".equals(row[7])) ? "Dinheiro" : ("2".equals(row[7])) ? "Cartão" : ("3".equals(row[7])) ? "PIX" : null;
                    rowData[8] = ("Assistência".equals(row[1]) && "".equals(row[8])) ? "Não Informado"
                            : (!"Assistência".equals(row[1]) && row[8] == null) ? "Não Aplicável" : row[8];
                    rowData[9] = (row[9] == null || "0".equals(row[9])) ? "Não Aplicável" : row[9];
                    rowData[10] = (row[10] != null && !"".equals(row[10])) ? row[10] : "Sem Detalhes";
                    rowData[11] = row[11];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if ("Saída Caixa".equals(table.getValueAt(row, 2))) {
                            c.setBackground(new Color(255, 246, 246));
                        } else if ("Entrada Caixa".equals(table.getValueAt(row, 2))) {
                            c.setBackground(new Color(246, 255, 246));
                        } else {
                            c.setBackground(table.getBackground());
                        }
                        return c;
                    }
                };

                deheader.setHorizontalAlignment(JLabel.CENTER);

                deheader.setForeground(Color.BLACK);
                deheader.setFont(fontmed(11));

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

                tbl.getColumnModel().getColumn(11).setMinWidth(0);
                tbl.getColumnModel().getColumn(11).setMaxWidth(0);
                tbl.getColumnModel().getColumn(11).setWidth(0);

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

    List<String[]> listacmb;

    private void tabelacmbrelatorio(JTable tbl, JScrollPane scr, JComboBox cmb) {

        try {

            if (!listacmb.isEmpty()) {

                DefaultTableModel modelo = new DefaultTableModel();
                JTableHeader header = tbl.getTableHeader();

                modelo.addColumn("Data");
                modelo.addColumn("Serviço");
                modelo.addColumn("Produto");
                modelo.addColumn("Preço");
                modelo.addColumn("Custo");
                modelo.addColumn("Pagamento");
                modelo.addColumn("Detalhes");
                modelo.addColumn("Código Entrada");

                double somaCartao = 0;
                double somaDinheiro = 0;
                double somaPix = 0;
                double somaCusto = 0;

                List<String[]> listaa = new ArrayList<>();

                for (String[] row : listacmb) {

                    if (rbtnVenRel.isSelected()) {

                        if (cmb.getSelectedItem().equals(row[2]) || cmb.getSelectedItem().equals("Filtrar resultados")) {
                            listaa.add(row);
                        }

                    } else {

                        if (cmb.getSelectedItem().equals(row[1]) || cmb.getSelectedItem().equals("Filtrar resultados")) {
                            listaa.add(row);
                        }

                    }

                }

                for (String[] row : listaa) {

                    Object[] rowData = new Object[8];

                    Date date = formatterbanco.parse(row[0]);
                    rowData[0] = formatter.format(date);
                    rowData[1] = (row[1] != null) ? row[1] : "Nenhum Serviço";
                    rowData[2] = (!"null - null null - null".equals(row[2]) && row[2] != null) ? row[2] : "Nenhum Produto";
                    rowData[3] = (!row[3].equals("0.0")) ? moedadoublereal(Double.valueOf(row[3])) : "Não Aplicável";
                    rowData[4] = (row[4] != null) ? moedadoublereal(Double.valueOf(row[4])) : "Não Aplicável";
                    rowData[5] = ("1".equals(row[5])) ? "Dinheiro" : ("2".equals(row[5])) ? "Cartão" : ("3".equals(row[5])) ? "PIX" : null;
                    rowData[6] = (row[6] != null && !"".equals(row[6])) ? row[6] : "Sem Detalhes";
                    rowData[7] = row[7];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if ("Saída Caixa".equals(table.getValueAt(row, 1))) {
                            c.setBackground(new Color(255, 246, 246));
                        } else if ("Entrada Caixa".equals(table.getValueAt(row, 1))) {
                            c.setBackground(new Color(246, 255, 246));
                        } else {
                            c.setBackground(table.getBackground());
                        }
                        return c;
                    }
                };

                deheader.setHorizontalAlignment(JLabel.CENTER);
                deheader.setForeground(Color.BLACK);
                deheader.setFont(fontmed(11));

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

                double somaValor = 0;
                int somaentrada = 0;
                String codigoAnterior = null;

                for (String[] row : listaa) {

                    String codigoAtual = row[7];

                    if (codigoAnterior == null || !codigoAtual.equals(codigoAnterior)) {

                        if ("1".equals(row[5])) {
                            somaDinheiro += Double.parseDouble(row[3]);
                            if (row[4] != null && chkCus.isSelected()) {
                                somaDinheiro -= Double.parseDouble(row[4]);
                            }
                        } else if ("2".equals(row[5])) {
                            somaCartao += Double.parseDouble(row[3]);
                            if (row[4] != null && chkCus.isSelected()) {
                                somaCartao -= Double.parseDouble(row[4]);
                            }
                        } else if ("3".equals(row[5])) {
                            somaPix += Double.parseDouble(row[3]);
                            if (row[4] != null && chkCus.isSelected()) {
                                somaPix -= Double.parseDouble(row[4]);
                            }
                        }

                        if (row[4] != null) {

                            somaCusto += Double.parseDouble(row[4]);

                        }

                        somaValor += Double.parseDouble(row[3]);

                        somaentrada++;
                    }

                    codigoAnterior = codigoAtual;
                }

                tbl.getColumnModel().getColumn(7).setMinWidth(0);
                tbl.getColumnModel().getColumn(7).setMaxWidth(0);
                tbl.getColumnModel().getColumn(7).setWidth(0);

                tblRel.setVisible(true);
                scrRel.setVisible(true);

                if (chkCus.isSelected()) {
                    lblValTotRel.setText(moedadoublereal(somaValor - somaCusto));
                } else {
                    lblValTotRel.setText(moedadoublereal(somaValor));
                }

                lblValMedRel.setText(moedadoublereal(somaCusto));
                lblTotEntRel.setText(String.valueOf(somaentrada));

                lblValDinRel.setText(moedadoublereal(somaDinheiro));
                lblValCarRel.setText(moedadoublereal(somaCartao));
                lblValPixRel.setText(moedadoublereal(somaPix));

                if (somaCusto == 0) {

                    chkCus.setEnabled(false);
                    chkCus.setSelected(false);

                } else {

                    chkCus.setEnabled(true);
                }

            } else {

                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);

                tblRel.setVisible(false);
                scrRel.setVisible(false);

                lblResRel.setVisible(true);

                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean tabelarelatorio(JTable tbl, JScrollPane scr, int opc, int opc1, String data1, String data2, int opc2) {

        try {

            entradaDAO endao = new entradaDAO();
            entrada en = new entrada();

            List<String[]> lista;

            lista = endao.buscar(opc, opc1, data1, data2, en, opc2);

            if (!lista.isEmpty()) {

                listacmb = lista;

                DefaultTableModel modelo = new DefaultTableModel();
                JTableHeader header = tbl.getTableHeader();

                modelo.addColumn("Data");
                modelo.addColumn("Serviço");
                modelo.addColumn("Produto");
                modelo.addColumn("Preço");
                modelo.addColumn("Custo");
                modelo.addColumn("Pagamento");
                modelo.addColumn("Detalhes");
                modelo.addColumn("Código Entrada");

                for (String[] row : lista) {
                    Object[] rowData = new Object[8];

                    Date date = formatterbanco.parse(row[0]);
                    rowData[0] = formatter.format(date);
                    rowData[1] = (row[1] != null) ? row[1] : "Nenhum Serviço";
                    rowData[2] = (!"null - null null - null".equals(row[2]) && row[2] != null) ? row[2] : "Nenhum Produto";
                    rowData[3] = (!row[3].equals("0.0")) ? moedadoublereal(Double.valueOf(row[3])) : "Não Aplicável";
                    rowData[4] = (row[4] != null) ? moedadoublereal(Double.valueOf(row[4])) : "Não Aplicável";
                    rowData[5] = ("1".equals(row[5])) ? "Dinheiro" : ("2".equals(row[5])) ? "Cartão" : ("3".equals(row[5])) ? "PIX" : null;
                    rowData[6] = (row[6] != null && !"".equals(row[6])) ? row[6] : "Sem Detalhes";
                    rowData[7] = row[7];

                    modelo.addRow(rowData);
                }

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if ("Saída Caixa".equals(table.getValueAt(row, 1))) {
                            c.setBackground(new Color(255, 246, 246));
                        } else if ("Entrada Caixa".equals(table.getValueAt(row, 1))) {
                            c.setBackground(new Color(246, 255, 246));
                        } else {
                            c.setBackground(table.getBackground());
                        }
                        return c;
                    }
                };

                cellRenderer.setHorizontalAlignment(JLabel.CENTER);
                cellRenderer.setForeground(Color.BLACK);
                cellRenderer.setFont(fontmed(4));

                header.setForeground(corforeazul);
                header.setBackground(new Color(246, 246, 246));
                header.setFont(fontbold(12));
                header.setReorderingAllowed(false);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                }

                tbl.getColumnModel().getColumn(7).setMinWidth(0);
                tbl.getColumnModel().getColumn(7).setMaxWidth(0);
                tbl.getColumnModel().getColumn(7).setWidth(0);

                tblRel.setVisible(true);
                scrRel.setVisible(true);

                if (chkCus.isSelected()) {
                    lblValTotRel.setText(moedadoublereal(en.getPtotal() - en.getCtotal()));
                    lblValDinRel.setText(moedadoublereal(en.getPdin() - en.getCdin()));
                    lblValCarRel.setText(moedadoublereal(en.getPcartao() - en.getCcartao()));
                    lblValPixRel.setText(moedadoublereal(en.getPpix() - en.getCpix()));
                } else {
                    lblValTotRel.setText(moedadoublereal(en.getPtotal()));
                    lblValDinRel.setText(moedadoublereal(en.getPdin()));
                    lblValCarRel.setText(moedadoublereal(en.getPcartao()));
                    lblValPixRel.setText(moedadoublereal(en.getPpix()));
                }

                lblValMedRel.setText(moedadoublereal(en.getCtotal()));
                lblTotEntRel.setText(String.valueOf(en.getEnt()));

                if (en.getCtotal() == 0) {
                    chkCus.setEnabled(false);
                    chkCus.setSelected(false);
                } else {
                    chkCus.setEnabled(true);
                }

                lblResRel.setVisible(false);

            } else {

                tblRel.setVisible(false);
                scrRel.setVisible(false);

                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);

                lblResRel.setVisible(true);

                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public void cmbrelatorio(JTable tabela, JComboBox<String> cmbrelatorio, int coluna) {

        Set<String> valoresUnicos = new HashSet<>();

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        int rowCount = modelo.getRowCount();

        cmbrelatorio.removeAllItems();

        cmbrelatorio.addItem("Filtrar resultados");

        for (int i = 0; i < rowCount; i++) {
            String valor = (String) modelo.getValueAt(i, coluna);

            if ("Nenhum Serviço".equals(valor)) {
                valor = "Venda";
            }

            valoresUnicos.add(valor);
        }

        for (String valor : valoresUnicos) {
            cmbrelatorio.addItem(valor);
        }

        if (cmbrelatorio.getItemCount() == 1) {

            cmbrelatorio.setEnabled(false);

        } else {

            cmbrelatorio.setEnabled(true);

        }

    }

    private boolean tabelagerenciarentrada(JTable tbl, JScrollPane scr, String data) {

        try {

            entradaDAO endao = new entradaDAO();

            List<String[]> lista = endao.buscargerenciar(data);

            if (!lista.isEmpty()) {

                DefaultTableModel modelo = new DefaultTableModel();

                JTableHeader header = tbl.getTableHeader();

                modelo.addColumn("Data");
                modelo.addColumn("Área");
                modelo.addColumn("Serviço");
                modelo.addColumn("Cliente");
                modelo.addColumn("Produto");
                modelo.addColumn("Preço");
                modelo.addColumn("Custo");
                modelo.addColumn("Pagamento");
                modelo.addColumn("Fornecedor");
                modelo.addColumn("Quantidade");
                modelo.addColumn("Detalhes");
                modelo.addColumn("Código Entrada");

                for (String[] row : lista) {

                    Object[] rowData = new Object[12];

                    Date date = formatterbanco.parse(row[0]);
                    rowData[0] = formatter.format(date);
                    rowData[1] = row[1];
                    rowData[2] = (row[2] != null) ? row[2] : "Nenhum Serviço";
                    rowData[3] = ("Assistência".equals(row[1]) && "".equals(row[3])) ? "Não Informado"
                            : (!"Assistência".equals(row[1]) && row[3] == null) ? "Não Aplicável" : row[3];
                    rowData[4] = (!"null - null null - null".equals(row[4]) && row[4] != null) ? row[4] : "Nenhum Produto";
                    rowData[5] = (!row[5].equals("0.0")) ? moedadoublereal(Double.valueOf(row[5])) : "Não Aplicável";
                    rowData[6] = (row[6] != null) ? moedadoublereal(Double.valueOf(row[6])) : "Não Aplicável";
                    rowData[7] = ("1".equals(row[7])) ? "Dinheiro" : ("2".equals(row[7])) ? "Cartão" : ("3".equals(row[7])) ? "PIX" : null;
                    rowData[8] = ("Assistência".equals(row[1]) && "".equals(row[8])) ? "Não Informado"
                            : (!"Assistência".equals(row[1]) && row[8] == null) ? "Não Aplicável" : row[8];
                    rowData[9] = (row[9] == null || "0".equals(row[9])) ? "Não Aplicável" : row[9];
                    rowData[10] = (row[10] != null && !"".equals(row[10])) ? row[10] : "Sem Detalhes";
                    rowData[11] = row[11];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if ("Saída Caixa".equals(table.getValueAt(row, 2))) {
                            c.setBackground(new Color(255, 246, 246));
                        } else if ("Entrada Caixa".equals(table.getValueAt(row, 2))) {
                            c.setBackground(new Color(246, 255, 246));
                        } else {
                            c.setBackground(table.getBackground());
                        }
                        return c;
                    }
                };

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

                tbl.getColumnModel().getColumn(11).setMinWidth(0);
                tbl.getColumnModel().getColumn(11).setMaxWidth(0);
                tbl.getColumnModel().getColumn(11).setWidth(0);

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
                    rowData[2] = (!row[2].equals("0.0")) ? moedadoublereal(Double.valueOf(row[2])) : "Não Aplicável";
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

                                if (comparacao1 < 0 && comparacao2 > 0 && diferencaDias > 4) { //dataatual menor data e data maior datacon

                                    component.setBackground(new Color(182, 222, 170));//verde

                                } else {

                                    component.setBackground(new Color(229, 190, 190));//vermelho

                                }

                            } else {

                                if (comparacao1 < 0 && diferencaDias > 4) {

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

                tbl.getColumnModel().getColumn(0).setMinWidth(0);
                tbl.getColumnModel().getColumn(0).setMaxWidth(0);
                tbl.getColumnModel().getColumn(0).setWidth(0);

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

    private boolean tabelaos(JTable tbl, JScrollPane scr) {

        try {

            osDAO osdao = new osDAO();
            List<String[]> lista = osdao.buscar();

            if (!lista.isEmpty()) {

                JTableHeader header = tbl.getTableHeader();

                DefaultTableModel modelo = new DefaultTableModel();

                modelo.addColumn("Nome");
                modelo.addColumn("Endereço");
                modelo.addColumn("Telefone");
                modelo.addColumn("Equipamento");
                modelo.addColumn("Marca");
                modelo.addColumn("Modelo");
                modelo.addColumn("Defeito");
                modelo.addColumn("Reparo");
                modelo.addColumn("Preço");
                modelo.addColumn("Entrada");
                modelo.addColumn("Saída");
                modelo.addColumn("Garantia");
                modelo.addColumn("ID");

                for (String[] row : lista) {

                    Object[] rowData = new Object[13];

                    rowData[0] = row[0];
                    rowData[1] = row[1];
                    rowData[2] = row[2];
                    rowData[3] = row[3];
                    rowData[4] = row[4];
                    rowData[5] = row[5];
                    rowData[6] = row[6];
                    rowData[7] = row[7];
                    rowData[8] = row[8];
                    rowData[9] = row[9];
                    rowData[10] = row[10];
                    rowData[11] = row[11];
                    rowData[12] = row[12];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

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

                tbl.getColumnModel().getColumn(12).setMinWidth(0);
                tbl.getColumnModel().getColumn(12).setMaxWidth(0);
                tbl.getColumnModel().getColumn(12).setWidth(0);

                tbl.setVisible(true);
                scr.setVisible(true);

            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private boolean tabelaospa(JTable tbl, JScrollPane scr, os os) {

        try {

            osDAO osdao = new osDAO();
            List<String[]> lista = osdao.buscarpa(os);

            if (!lista.isEmpty()) {

                JTableHeader header = tbl.getTableHeader();

                DefaultTableModel modelo = new DefaultTableModel();

                modelo.addColumn("Nome");
                modelo.addColumn("Endereço");
                modelo.addColumn("Telefone");
                modelo.addColumn("Equipamento");
                modelo.addColumn("Marca");
                modelo.addColumn("Modelo");
                modelo.addColumn("Defeito");
                modelo.addColumn("Reparo");
                modelo.addColumn("Preço");
                modelo.addColumn("Entrada");
                modelo.addColumn("Saída");
                modelo.addColumn("Garantia");
                modelo.addColumn("ID");

                for (String[] row : lista) {

                    Object[] rowData = new Object[13];

                    rowData[0] = row[0];
                    rowData[1] = row[1];
                    rowData[2] = row[2];
                    rowData[3] = row[3];
                    rowData[4] = row[4];
                    rowData[5] = row[5];
                    rowData[6] = row[6];
                    rowData[7] = row[7];
                    rowData[8] = row[8];
                    rowData[9] = row[9];
                    rowData[10] = row[10];
                    rowData[11] = row[11];
                    rowData[12] = row[12];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

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

                tbl.getColumnModel().getColumn(12).setMinWidth(0);
                tbl.getColumnModel().getColumn(12).setMaxWidth(0);
                tbl.getColumnModel().getColumn(12).setWidth(0);

                tbl.setVisible(true);
                scr.setVisible(true);

            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    private boolean tabelavencimento(JTable tbl, JScrollPane scr) {

        try {

            vencimentoDAO vendao = new vencimentoDAO();
            List<String[]> lista = vendao.buscar();

            if (!lista.isEmpty()) {

                JTableHeader header = tbl.getTableHeader();

                DefaultTableModel modelo = new DefaultTableModel();

                modelo.addColumn("Cliente");
                modelo.addColumn("Telefone");
                modelo.addColumn("CPF");
                modelo.addColumn("Acesso");
                modelo.addColumn("Plano");
                modelo.addColumn("Data");
                modelo.addColumn("Vencimento");
                modelo.addColumn("Ok");
                modelo.addColumn("ID");

                for (String[] row : lista) {

                    Object[] rowData = new Object[9];

                    Date date = formatterbanco.parse(row[5]);

                    Date datecon = formatterbanco.parse(row[6]);

                    rowData[0] = row[0];
                    rowData[1] = row[1];
                    rowData[2] = row[2];
                    rowData[3] = row[3];
                    rowData[4] = row[4];
                    rowData[5] = formatter.format(date);
                    rowData[6] = formatter.format(datecon);
                    rowData[7] = row[7];
                    rowData[8] = row[8];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                        try {

                            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                            Date dataatu = new Date();

                            String dataat = formatterbanco.format(dataatu);

                            Date dataatual = formatterbanco.parse(dataat);

                            String vens = formatterbanco.format(((formatter.parse(table.getValueAt(row, 6).toString()))));

                            int ok = Integer.parseInt(table.getValueAt(row, 7).toString());

                            Date vencimento = formatterbanco.parse(vens);

                            int comparacao1 = dataatual.compareTo(vencimento);

                            if (comparacao1 >= 0 && ok == 0) {
                                component.setBackground(new Color(182, 222, 170));

                            } else {
                                component.setBackground(new Color(246, 246, 246));
                            }

                            component.setFont(fontmed(12));

                            if (isSelected) {
                                component.setBackground(new Color(211, 211, 211));
                                component.setForeground(Color.BLACK);
                            }

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

                tbl.getColumnModel().getColumn(7).setMinWidth(0);
                tbl.getColumnModel().getColumn(7).setMaxWidth(0);
                tbl.getColumnModel().getColumn(7).setWidth(0);

                tbl.getColumnModel().getColumn(8).setMinWidth(0);
                tbl.getColumnModel().getColumn(8).setMaxWidth(0);
                tbl.getColumnModel().getColumn(8).setWidth(0);

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

    private boolean tabelavencimentopa(JTable tbl, JScrollPane scr, vencimento ve) {

        try {

            vencimentoDAO vendao = new vencimentoDAO();
            List<String[]> lista = vendao.buscarpa(ve);

            if (!lista.isEmpty()) {

                JTableHeader header = tbl.getTableHeader();

                DefaultTableModel modelo = new DefaultTableModel();

                modelo.addColumn("Cliente");
                modelo.addColumn("Telefone");
                modelo.addColumn("CPF");
                modelo.addColumn("Acesso");
                modelo.addColumn("Plano");
                modelo.addColumn("Data");
                modelo.addColumn("Vencimento");
                modelo.addColumn("Ok");
                modelo.addColumn("ID");

                for (String[] row : lista) {

                    Object[] rowData = new Object[9];

                    Date date = formatterbanco.parse(row[5]);

                    Date datecon = formatterbanco.parse(row[6]);

                    rowData[0] = row[0];
                    rowData[1] = row[1];
                    rowData[2] = row[2];
                    rowData[3] = row[3];
                    rowData[4] = row[4];
                    rowData[5] = formatter.format(date);
                    rowData[6] = formatter.format(datecon);
                    rowData[7] = row[7];
                    rowData[8] = row[8];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                        try {
                            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                            Date dataatu = new Date();

                            String dataat = formatterbanco.format(dataatu);

                            Date dataatual = formatterbanco.parse(dataat);

                            String vens = formatterbanco.format(((formatter.parse(table.getValueAt(row, 6).toString()))));

                            int ok = Integer.parseInt(table.getValueAt(row, 7).toString());

                            Date vencimento = formatterbanco.parse(vens);

                            int comparacao1 = dataatual.compareTo(vencimento);

                            if (comparacao1 >= 0 && ok == 0) {
                                component.setBackground(new Color(182, 222, 170)); // verde
                            } else {
                                component.setBackground(new Color(246, 246, 246)); // vermelho
                            }

                            component.setFont(fontmed(12));

                            if (isSelected) {
                                component.setBackground(new Color(211, 211, 211)); // Defina a cor de fundo da linha selecionada como vermelho
                                component.setForeground(Color.BLACK);
                            }

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
                tbl.getColumnModel().getColumn(7).setMinWidth(0);
                tbl.getColumnModel().getColumn(7).setMaxWidth(0);
                tbl.getColumnModel().getColumn(7).setWidth(0);

                tbl.getColumnModel().getColumn(8).setMinWidth(0);
                tbl.getColumnModel().getColumn(8).setMaxWidth(0);
                tbl.getColumnModel().getColumn(8).setWidth(0);

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
                    rowData[2] = (!row[2].equals("0.0")) ? moedadoublereal(Double.valueOf(row[2])) : "Não Aplicável";
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

                tbl.getColumnModel().getColumn(0).setMinWidth(0);
                tbl.getColumnModel().getColumn(0).setMaxWidth(0);
                tbl.getColumnModel().getColumn(0).setWidth(0);

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

        header.setFont(fontbold(12));
        header.setReorderingAllowed(false);

        tblSelIteCadEnt.setModel(modelo);
        tblSelIteCadEnt.setRowHeight(25);
        tblSelIteCadEnt.setDefaultEditor(Object.class, null);
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
                    deheader.setFont(fontmed(12));

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

                    header.getColumnModel().getColumn(1).setMinWidth(0);
                    header.getColumnModel().getColumn(1).setMaxWidth(0);
                    header.getColumnModel().getColumn(1).setWidth(0);

                    tblSelIteGerEnt.setVisible(true);
                    scrSelIteGerEnt.setVisible(true);
                    lblSelIteGerEnt.setVisible(true);

                } else {

                    deheader.setHorizontalAlignment(JLabel.CENTER);

                    deheader.setForeground(Color.BLACK);
                    deheader.setFont(fontmed(12));

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

                    header.getColumnModel().getColumn(1).setMinWidth(0);
                    header.getColumnModel().getColumn(1).setMaxWidth(0);
                    header.getColumnModel().getColumn(1).setWidth(0);

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

            JTableHeader header = tabelaDestino.getTableHeader();
            DefaultTableModel modelo = (DefaultTableModel) tabelaDestino.getModel();

            String idt = tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), 0).toString();
            String precot;
            String produtot = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Produto"))).toString();
            String cort;
            String marcat;
            String chipt;
            String modelot;

            entrada en = new entrada();
            entradaDAO endao = new entradaDAO();

            en.setIdestoque(Integer.parseInt(idt));
            en.setQuantidade(Integer.parseInt(qua));

            endao.atualizarestoque(en, 0);

            if (rbtn.isSelected()) {

                precot = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Preço"))).toString();
                chipt = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Chip"))).toString();

                Object[] novaLinha = {qua, idt, produtot + " - " + chipt, precot};

                modelo.addRow(novaLinha);

                tabelaDestino.setModel(modelo);

            } else {

                marcat = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Marca"))).toString();
                modelot = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Modelo"))).toString();
                cort = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Cor"))).toString();
                precot = (tabelaOrigem.getValueAt(tabelaOrigem.getSelectedRow(), tabelaOrigem.getColumnModel().getColumnIndex("Preço"))).toString();

                Object[] novaLinha = {qua, idt, produtot + " - " + marcat + " " + modelot + " - " + cort, precot};

                modelo.addRow(novaLinha);

                header.getColumnModel().getColumn(1).setMinWidth(0);
                header.getColumnModel().getColumn(1).setMaxWidth(0);
                header.getColumnModel().getColumn(1).setWidth(0);

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

                header.getColumnModel().getColumn(0).setMinWidth(0);
                header.getColumnModel().getColumn(0).setMaxWidth(0);
                header.getColumnModel().getColumn(0).setWidth(0);

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

    @SuppressWarnings({"unchecked"})

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
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        return formatadorMoeda.format(valor);
    }

    private void pnlbtn() {

        btnCadEnt.setVisible(false);
        btnGerEnt.setVisible(false);
        btnConEnt.setVisible(false);

        btnCadEst.setVisible(false);
        btnConEst.setVisible(false);
        btnGerEst.setVisible(false);

        btnCadTipSer.setVisible(false);
        btnGerTipSer.setVisible(false);

        btnMasPla.setVisible(false);
        btnDes.setVisible(false);
        btnCadDes.setVisible(false);
        btnGerDes.setVisible(false);
        btnVen.setVisible(false);
        btnCadVen.setVisible(false);
        btnJurPri.setVisible(false);

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
        pnlGerEnt.setVisible(false);
        pnlOs.setVisible(false);
        pnlGerOs.setVisible(false);
        pnlIteGerEnt.setVisible(false);
        pnlConEnt.setVisible(false);
        pnlCadVen.setVisible(false);
        pnlVen.setVisible(false);
        pnlJur.setVisible(false);

    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        btnGroup1 = new javax.swing.ButtonGroup();
        btnGroup2 = new javax.swing.ButtonGroup();
        btnGroup3 = new javax.swing.ButtonGroup();
        btnGroup4 = new javax.swing.ButtonGroup();
        pnlPri = new javax.swing.JPanel();
        btnRecBan = new javax.swing.JLabel();
        imgLogo = new javax.swing.JLabel();
        btnEntPri = new javax.swing.JLabel();
        btnCadEnt = new javax.swing.JLabel();
        btnConEnt = new javax.swing.JLabel();
        btnGerEnt = new javax.swing.JLabel();
        btnEstPri = new javax.swing.JLabel();
        btnCadEst = new javax.swing.JLabel();
        btnConEst = new javax.swing.JLabel();
        btnGerEst = new javax.swing.JLabel();
        btnRelPri = new javax.swing.JLabel();
        btnOrdSerPri = new javax.swing.JLabel();
        btnCadOsPri = new javax.swing.JLabel();
        btnGerOsPri = new javax.swing.JLabel();
        btnTipSerPri = new javax.swing.JLabel();
        btnCadTipSer = new javax.swing.JLabel();
        btnGerTipSer = new javax.swing.JLabel();
        btnOutPri = new javax.swing.JLabel();
        btnMasPla = new javax.swing.JLabel();
        btnDes = new javax.swing.JLabel();
        btnCadDes = new javax.swing.JLabel();
        btnGerDes = new javax.swing.JLabel();
        btnVen = new javax.swing.JLabel();
        btnCadVen = new javax.swing.JLabel();
        btnJurPri = new javax.swing.JLabel();
        lblBakPri = new javax.swing.JLabel();
        btnTenPri = new javax.swing.JLabel();
        btnVenPri = new javax.swing.JLabel();
        lblTitPri = new javax.swing.JLabel();
        pnlCadEnt = new javax.swing.JPanel();
        rbtnPixCadEnt = new javax.swing.JRadioButton();
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
        cmbVezCar = new javax.swing.JComboBox<>();
        lblDetCadEnt = new javax.swing.JLabel();
        txtDetCadEnt = new javax.swing.JTextField();
        sepDetCadEnt = new javax.swing.JSeparator();
        lblSerCadEnt = new javax.swing.JLabel();
        txtCodCadEnt = new javax.swing.JTextField();
        rbtnCarCadEnt = new javax.swing.JRadioButton();
        rbtnDinCadEnt = new javax.swing.JRadioButton();
        lblR$CusCadEnt = new javax.swing.JLabel();
        lblCusCadEnt = new javax.swing.JLabel();
        txtCusCadEnt = new javax.swing.JTextField();
        sepCusCadEnt = new javax.swing.JSeparator();
        lblForCadEnt = new javax.swing.JLabel();
        txtForCadEnt = new javax.swing.JTextField();
        sepForCadEnt = new javax.swing.JSeparator();
        lblCliCadEnt = new javax.swing.JLabel();
        txtCliCadEnt = new javax.swing.JTextField();
        sepCliCadEnt = new javax.swing.JSeparator();
        rbtnCreCadEnt = new javax.swing.JRadioButton();
        rbtnDebCadEnt = new javax.swing.JRadioButton();
        spnParCadEnt = new javax.swing.JSpinner();
        lblParCadEnt = new javax.swing.JLabel();
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
        pnlConEnt = new javax.swing.JPanel();
        btnCanConEnt = new javax.swing.JButton();
        btnBusConEnt = new javax.swing.JButton();
        lblBusConEnt = new javax.swing.JLabel();
        txtBusConEnt = new javax.swing.JTextField();
        sepBusConEst1 = new javax.swing.JSeparator();
        scrConEnt = new javax.swing.JScrollPane();
        tblConEnt = new javax.swing.JTable();
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
        rbtnCarGerEnt = new javax.swing.JRadioButton();
        rbtnPixGerEnt = new javax.swing.JRadioButton();
        rbtnDinGerEnt = new javax.swing.JRadioButton();
        lblCliGerEnt = new javax.swing.JLabel();
        txtCliGerEnt = new javax.swing.JTextField();
        sepCliGerEnt = new javax.swing.JSeparator();
        lblCusGerEnt = new javax.swing.JLabel();
        lblR$CusGerEnt = new javax.swing.JLabel();
        txtCusGerEnt = new javax.swing.JTextField();
        sepCusGerEnt = new javax.swing.JSeparator();
        lblForGerEnt = new javax.swing.JLabel();
        txtForGerEnt = new javax.swing.JTextField();
        sepForGerEnt = new javax.swing.JSeparator();
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
        pnlCadEst = new javax.swing.JPanel();
        btnSalCadEst = new javax.swing.JButton();
        btnCanCadEst = new javax.swing.JButton();
        rbtnCapCadEst = new javax.swing.JRadioButton();
        rbtnPelCadEst = new javax.swing.JRadioButton();
        rbtnChiCadEst = new javax.swing.JRadioButton();
        rbtnAceCadEst = new javax.swing.JRadioButton();
        scrCadEst = new javax.swing.JScrollPane();
        tblCadEst = new javax.swing.JTable();
        lblProCadEst = new javax.swing.JLabel();
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
        pnlRel = new javax.swing.JPanel();
        lblResRel = new javax.swing.JLabel();
        cmbRel = new javax.swing.JComboBox<>();
        scrRel = new javax.swing.JScrollPane();
        tblRel = new javax.swing.JTable();
        btnVolRel = new javax.swing.JButton();
        rbtnSerRel = new javax.swing.JRadioButton();
        rbtnVenRel1 = new javax.swing.JRadioButton();
        rbtnVenRel = new javax.swing.JRadioButton();
        rbtnAssRel = new javax.swing.JRadioButton();
        rbtnTodRel = new javax.swing.JRadioButton();
        lblDatIniRel = new javax.swing.JLabel();
        txtDatIniRel = new javax.swing.JTextField();
        sepDatCadEnt1 = new javax.swing.JSeparator();
        btnTodRel = new javax.swing.JLabel();
        btnAnoRel = new javax.swing.JLabel();
        lblDatFinRel = new javax.swing.JLabel();
        txtDatFinRel = new javax.swing.JTextField();
        sepDatCadEnt2 = new javax.swing.JSeparator();
        lblDiaRel = new javax.swing.JLabel();
        btnMenDiaRel = new javax.swing.JLabel();
        btnNumDiaRel = new javax.swing.JLabel();
        btnMaiDiaRel = new javax.swing.JLabel();
        btnDiaRel = new javax.swing.JLabel();
        btnMesRel = new javax.swing.JLabel();
        btnSemRel = new javax.swing.JLabel();
        sepDatCadEnt3 = new javax.swing.JSeparator();
        lblValDinRel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTotEntRel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblValTotRel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblValMedRel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblValCarRel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblValPixRel = new javax.swing.JLabel();
        chkCus = new javax.swing.JCheckBox();
        pnlOs = new javax.swing.JPanel();
        btnGerOs = new javax.swing.JButton();
        chkGarOs = new javax.swing.JCheckBox();
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
        txtDefOs = new javax.swing.JTextField();
        sepDetCadEst1 = new javax.swing.JSeparator();
        lblDefOs = new javax.swing.JLabel();
        txtRepOs = new javax.swing.JTextField();
        sepDetCadEst2 = new javax.swing.JSeparator();
        lblDatEntOs = new javax.swing.JLabel();
        txtDatOs = new javax.swing.JTextField();
        sepDetCadEst3 = new javax.swing.JSeparator();
        lblHorOs = new javax.swing.JLabel();
        txtDatSaiOs = new javax.swing.JTextField();
        sepDetCadEst4 = new javax.swing.JSeparator();
        sepCusGerEnt1 = new javax.swing.JSeparator();
        lblPreOs = new javax.swing.JLabel();
        lblR$Os = new javax.swing.JLabel();
        txtPreOs = new javax.swing.JTextField();
        pnlGerOs = new javax.swing.JPanel();
        scrOs = new javax.swing.JScrollPane();
        tblOs = new javax.swing.JTable();
        btnAltGerOs = new javax.swing.JButton();
        btnGerGerOs = new javax.swing.JButton();
        btnExcGerOs = new javax.swing.JButton();
        btnVolGerOs = new javax.swing.JButton();
        lblErrGerOs = new javax.swing.JLabel();
        lblBusGerOs = new javax.swing.JLabel();
        txtBusGerOs = new javax.swing.JTextField();
        sepBusVen1 = new javax.swing.JSeparator();
        pnlCadTipSer = new javax.swing.JPanel();
        btnSalTipSer = new javax.swing.JButton();
        btnCanTipSer = new javax.swing.JButton();
        rbtnOutTipSer = new javax.swing.JRadioButton();
        rbtnSerTimTipSer = new javax.swing.JRadioButton();
        rbtnAssTipSer = new javax.swing.JRadioButton();
        lblDesTipSer = new javax.swing.JLabel();
        txtDesTipSer = new javax.swing.JTextField();
        sepDesTipSer = new javax.swing.JSeparator();
        pnlGerTipSer = new javax.swing.JPanel();
        btnExcGerTipSer = new javax.swing.JButton();
        btnAtvGerTipSer = new javax.swing.JButton();
        btnAltGerTipSer = new javax.swing.JButton();
        btnCanGerTipSer = new javax.swing.JButton();
        lblDesTipSer2 = new javax.swing.JLabel();
        lblDesGerTipSer = new javax.swing.JLabel();
        txtDesGerTipSer = new javax.swing.JTextField();
        sepDesGerTipSer = new javax.swing.JSeparator();
        scrTipSer = new javax.swing.JScrollPane();
        tblTipSer = new javax.swing.JTable();
        rbtnOutGerTipSer = new javax.swing.JRadioButton();
        rbtnAssGerTipSer = new javax.swing.JRadioButton();
        rbtnTimGerTipSer = new javax.swing.JRadioButton();
        pnlMas = new javax.swing.JPanel();
        chkCarMasa = new javax.swing.JCheckBox();
        chkAppMas = new javax.swing.JCheckBox();
        chkMelMas = new javax.swing.JCheckBox();
        btnConMas = new javax.swing.JButton();
        btnVenMas = new javax.swing.JButton();
        btnGerMas = new javax.swing.JButton();
        btnCanMas = new javax.swing.JButton();
        btnParMas = new javax.swing.JButton();
        btnCopMas = new javax.swing.JLabel();
        lblNomMas = new javax.swing.JLabel();
        chkDebMasa = new javax.swing.JCheckBox();
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
        chkC6Mas = new javax.swing.JCheckBox();
        lblVenMas = new javax.swing.JLabel();
        sepDesGerTipSer8 = new javax.swing.JSeparator();
        txtVenMas = new javax.swing.JTextField();
        sepDesGerTipSer9 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreMas = new javax.swing.JTextArea();
        chkDebMas = new javax.swing.JRadioButton();
        chkCarMas = new javax.swing.JRadioButton();
        chkBolMas = new javax.swing.JRadioButton();
        pnlDes = new javax.swing.JPanel();
        scrConDes = new javax.swing.JScrollPane();
        tblConDes = new javax.swing.JTable();
        btnVolDes = new javax.swing.JButton();
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
        pnlVen = new javax.swing.JPanel();
        scrVen = new javax.swing.JScrollPane();
        tblVen = new javax.swing.JTable();
        btnCopAVen = new javax.swing.JButton();
        btnCopVen = new javax.swing.JButton();
        btnAltVen = new javax.swing.JButton();
        btnWppVen = new javax.swing.JButton();
        btnVolVen = new javax.swing.JButton();
        btnExcVen = new javax.swing.JButton();
        lblConPlaVen = new javax.swing.JLabel();
        lblBusVen2 = new javax.swing.JLabel();
        lblErrVen = new javax.swing.JLabel();
        lblBusVen = new javax.swing.JLabel();
        txtBusVen = new javax.swing.JTextField();
        sepBusVen = new javax.swing.JSeparator();
        pnlCadVen = new javax.swing.JPanel();
        lblAceCadVen = new javax.swing.JLabel();
        txtAceCadVen = new javax.swing.JTextField();
        sepTelCadVen1 = new javax.swing.JSeparator();
        lblVenCadVen = new javax.swing.JLabel();
        txtVenCadVen = new javax.swing.JTextField();
        sepVenCadVen = new javax.swing.JSeparator();
        btnSalCadVen = new javax.swing.JButton();
        btnCanCadVen = new javax.swing.JButton();
        lblPlaCadVen = new javax.swing.JLabel();
        txtPlaCadVen = new javax.swing.JTextField();
        sepPlaCadVen = new javax.swing.JSeparator();
        lblCliCadVen = new javax.swing.JLabel();
        txtCliCadVen = new javax.swing.JTextField();
        sepCliCadVen = new javax.swing.JSeparator();
        lblTelCadVen = new javax.swing.JLabel();
        txtTelCadVen = new javax.swing.JTextField();
        sepTelCadVen = new javax.swing.JSeparator();
        lblDatCadVen = new javax.swing.JLabel();
        txtDatCadVen = new javax.swing.JTextField();
        sepDatCadVen = new javax.swing.JSeparator();
        lblCli = new javax.swing.JLabel();
        lblVen = new javax.swing.JLabel();
        lblDat = new javax.swing.JLabel();
        lblCpfCadVen = new javax.swing.JLabel();
        sepCadVen = new javax.swing.JSeparator();
        txtCpfCadVen = new javax.swing.JTextField();
        pnlJur = new javax.swing.JPanel();
        btnVolJur = new javax.swing.JButton();
        btnCalJur = new javax.swing.JButton();
        lblValMesPreJur = new javax.swing.JLabel();
        lblValParJur = new javax.swing.JLabel();
        lblValParJur1 = new javax.swing.JLabel();
        lblValJur = new javax.swing.JLabel();
        lblR$Jur = new javax.swing.JLabel();
        txtValJur = new javax.swing.JTextField();
        sepDesGerTipSer10 = new javax.swing.JSeparator();
        sepDesGerTipSer11 = new javax.swing.JSeparator();
        sepDesGerTipSer16 = new javax.swing.JSeparator();
        lblValJur2 = new javax.swing.JLabel();
        lblValJur3 = new javax.swing.JLabel();
        lblParJur = new javax.swing.JLabel();
        lblValFinJur = new javax.swing.JLabel();
        lblValJurJur = new javax.swing.JLabel();
        lblValFinJur1 = new javax.swing.JLabel();
        lblJurJur1 = new javax.swing.JLabel();
        lblJurJur = new javax.swing.JLabel();
        spnParJur = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EmpSys");
        setIconImage(new ImageIcon(getClass().getResource("/images/ICON.png")).getImage());
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlPri.setVisible(false);
        pnlPri.setBackground(new java.awt.Color(246, 246, 246));
        pnlPri.setPreferredSize(new java.awt.Dimension(1300, 760));
        pnlPri.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnRecBan.setFont(fontmed(12));
        btnRecBan.setForeground(corforeazul);
        btnRecBan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlPri.add(btnRecBan, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 160, 60, 20));

        imgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LogoLoja580.png"))); // NOI18N
        imgLogo.setText("jLabel1");
        pnlPri.add(imgLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 580, 190));

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
        pnlPri.add(btnEntPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 210, -1, 40));

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
        pnlPri.add(btnCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 70, 20));

        btnConEnt.setFont(fontmed(12));
        btnConEnt.setForeground(corforeazul);
        btnConEnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnConEnt.setText("Consultar");
        btnConEnt.setToolTipText("");
        btnConEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConEntMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConEntMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnConEntMouseReleased(evt);
            }
        });
        pnlPri.add(btnConEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 70, 20));

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
        pnlPri.add(btnGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 70, 20));

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
        pnlPri.add(btnEstPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 210, -1, 40));

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
        pnlPri.add(btnCadEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 260, 70, 20));

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
        pnlPri.add(btnConEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 70, 20));

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
        pnlPri.add(btnGerEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 300, 90, 20));

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
        pnlPri.add(btnRelPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(468, 210, -1, 40));

        btnOrdSerPri.setFont(fontmed(14));
        btnOrdSerPri.setForeground(new java.awt.Color(10, 60, 133));
        btnOrdSerPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnOrdSerPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BackBtnPrin2.png"))); // NOI18N
        btnOrdSerPri.setText("Ordem de Serviço");
        btnOrdSerPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOrdSerPri.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOrdSerPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOrdSerPriMouseClicked(evt);
            }
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
        pnlPri.add(btnOrdSerPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 210, 170, 40));

        btnCadOsPri.setFont(fontmed(12));
        btnCadOsPri.setForeground(corforeazul);
        btnCadOsPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadOsPri.setText("Nova");
        btnCadOsPri.setToolTipText("");
        btnCadOsPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadOsPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadOsPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadOsPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCadOsPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnCadOsPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 260, 70, 20));

        btnGerOsPri.setFont(fontmed(12));
        btnGerOsPri.setForeground(corforeazul);
        btnGerOsPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGerOsPri.setText("Gerenciar");
        btnGerOsPri.setToolTipText("");
        btnGerOsPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerOsPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGerOsPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGerOsPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnGerOsPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnGerOsPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 280, 90, 20));

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
        pnlPri.add(btnTipSerPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 210, 170, 40));

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
        pnlPri.add(btnCadTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 260, 70, 20));

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
        pnlPri.add(btnGerTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 280, 70, 20));

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
        pnlPri.add(btnOutPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 210, 140, 40));

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
        pnlPri.add(btnMasPla, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 260, 100, 20));

        btnDes.setFont(fontmed(12));
        btnDes.setForeground(corforeazul);
        btnDes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDes.setText("Afazeres");
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
        pnlPri.add(btnDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 280, 60, 20));

        btnCadDes.setFont(fontmed(12));
        btnCadDes.setForeground(corforeazul);
        btnCadDes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadDes.setText("Cadastrar Afazeres");
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
        pnlPri.add(btnCadDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 300, 140, 20));

        btnGerDes.setFont(fontmed(12));
        btnGerDes.setForeground(corforeazul);
        btnGerDes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGerDes.setText("Gerenciar Afazeres");
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
        pnlPri.add(btnGerDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 320, 140, 20));

        btnVen.setFont(fontmed(12));
        btnVen.setForeground(corforeazul);
        btnVen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnVen.setText("Planos");
        btnVen.setToolTipText("");
        btnVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVenMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnVenMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnVenMouseReleased(evt);
            }
        });
        pnlPri.add(btnVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 260, 140, 20));

        btnCadVen.setFont(fontmed(12));
        btnCadVen.setForeground(corforeazul);
        btnCadVen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadVen.setText("Cadastrar Plano");
        btnCadVen.setToolTipText("");
        btnCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadVen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadVenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadVenMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnCadVenMouseReleased(evt);
            }
        });
        pnlPri.add(btnCadVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 280, 180, 20));

        btnJurPri.setFont(fontmed(12));
        btnJurPri.setForeground(corforeazul);
        btnJurPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnJurPri.setText("Calcular Juros");
        btnJurPri.setToolTipText("");
        btnJurPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnJurPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnJurPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnJurPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnJurPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnJurPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 300, 140, 20));

        lblBakPri.setFont(fontmed(12));
        lblBakPri.setForeground(corforeazul);
        lblBakPri.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBakPri.setText("Backup automático em andamento...");
        lblBakPri.setToolTipText("");
        lblBakPri.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pnlPri.add(lblBakPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 280, 20));

        btnTenPri.setFont(fontbold(10));
        btnTenPri.setForeground(corforeazul);
        btnTenPri.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTenPri.setText("Tentar novamente");
        btnTenPri.setToolTipText("");
        btnTenPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTenPri.setVisible(false);
        btnTenPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTenPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTenPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnTenPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnTenPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 150, 20));

        btnVenPri.setFont(fontmed(12));
        btnVenPri.setForeground(corforeazul);
        btnVenPri.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnVenPri.setText("Vencimento encontrado!");
        btnVenPri.setToolTipText("");
        btnVenPri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVenPri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVenPriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVenPriMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnVenPriMouseReleased(evt);
            }
        });
        pnlPri.add(btnVenPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 20, 170, 20));

        lblTitPri.setFont(fontmed(17));
        lblTitPri.setForeground(corforeazul);
        lblTitPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitPri.setText("Cadastrar Estoque");
        pnlPri.add(lblTitPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 270, 30));

        pnlCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadEnt.setLayout(null);

        btnGroup1.add(rbtnPixCadEnt);
        rbtnPixCadEnt.setFont(fontmed(12));
        rbtnPixCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPixCadEnt.setText("PIX");
        rbtnPixCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPixCadEnt.setEnabled(false);
        rbtnPixCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPixCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnPixCadEnt);
        rbtnPixCadEnt.setBounds(690, 30, 90, 21);

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
        btnIteCadEnt.setBounds(840, 150, 90, 50);

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
        btnSalCadEnt.setBounds(840, 210, 90, 50);

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
        btnCanCadEnt.setBounds(840, 270, 90, 50);

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
        rbtnSerCadEnt.setBounds(520, 0, 90, 21);

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
        rbtnVenCadEnt.setBounds(610, 0, 80, 21);

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
        rbtnAssCadEnt.setBounds(690, 0, 100, 21);

        lblDatCadEnt.setFont(fontmed(12));
        lblDatCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDatCadEnt.setText("Data");
        lblDatCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblDatCadEnt);
        lblDatCadEnt.setBounds(390, 130, 40, 20);

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
        txtDatCadEnt.setBounds(390, 130, 100, 20);

        sepDatCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepDatCadEnt);
        sepDatCadEnt.setBounds(390, 150, 100, 10);

        lblR$CadEnt.setFont(fontmed(13));
        lblR$CadEnt.setText("R$");
        lblR$CadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblR$CadEnt);
        lblR$CadEnt.setBounds(390, 180, 20, 21);

        lblPreCadEnt.setFont(fontmed(12));
        lblPreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblPreCadEnt.setText("Preço");
        lblPreCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblPreCadEnt);
        lblPreCadEnt.setBounds(390, 180, 40, 20);

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
        txtPreCadEnt.setBounds(410, 180, 80, 20);

        sepPreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepPreCadEnt);
        sepPreCadEnt.setBounds(390, 200, 100, 10);

        cmbVezCar.setFont(fontmed(13));
        cmbVezCar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o serviço" }));
        cmbVezCar.setToolTipText("");
        cmbVezCar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbVezCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbVezCarActionPerformed(evt);
            }
        });
        pnlCadEnt.add(cmbVezCar);
        cmbVezCar.setBounds(390, 300, 190, 30);

        lblDetCadEnt.setFont(fontmed(12));
        lblDetCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDetCadEnt.setText("Detalhes");
        lblDetCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblDetCadEnt);
        lblDetCadEnt.setBounds(390, 230, 70, 20);

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
        txtDetCadEnt.setBounds(390, 230, 190, 20);

        sepDetCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepDetCadEnt);
        sepDetCadEnt.setBounds(390, 250, 190, 10);

        lblSerCadEnt.setFont(fontmed(12));
        lblSerCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSerCadEnt.setText("Serviço");
        pnlCadEnt.add(lblSerCadEnt);
        lblSerCadEnt.setBounds(390, 270, 90, 30);
        pnlCadEnt.add(txtCodCadEnt);
        txtCodCadEnt.setBounds(190, 60, 64, 22);

        btnGroup1.add(rbtnCarCadEnt);
        rbtnCarCadEnt.setFont(fontmed(12));
        rbtnCarCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCarCadEnt.setText("Cartão");
        rbtnCarCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCarCadEnt.setEnabled(false);
        rbtnCarCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCarCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnCarCadEnt);
        rbtnCarCadEnt.setBounds(610, 30, 70, 20);

        btnGroup1.add(rbtnDinCadEnt);
        rbtnDinCadEnt.setFont(fontmed(12));
        rbtnDinCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnDinCadEnt.setText("Dinheiro");
        rbtnDinCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnDinCadEnt.setEnabled(false);
        rbtnDinCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnDinCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnDinCadEnt);
        rbtnDinCadEnt.setBounds(520, 30, 90, 21);

        lblR$CusCadEnt.setFont(fontmed(13));
        lblR$CusCadEnt.setText("R$");
        lblR$CusCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblR$CusCadEnt);
        lblR$CusCadEnt.setBounds(630, 180, 20, 21);

        lblCusCadEnt.setFont(fontmed(12));
        lblCusCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCusCadEnt.setText("Custo");
        lblCusCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblCusCadEnt);
        lblCusCadEnt.setBounds(630, 180, 40, 20);

        txtCusCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtCusCadEnt.setFont(fontmed(13));
        txtCusCadEnt.setBorder(null);
        txtCusCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCusCadEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCusCadEntFocusLost(evt);
            }
        });
        txtCusCadEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCusCadEntKeyTyped(evt);
            }
        });
        pnlCadEnt.add(txtCusCadEnt);
        txtCusCadEnt.setBounds(650, 180, 80, 20);

        sepCusCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepCusCadEnt);
        sepCusCadEnt.setBounds(630, 200, 100, 10);

        lblForCadEnt.setFont(fontmed(12));
        lblForCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblForCadEnt.setText("Fornecedor");
        lblForCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblForCadEnt);
        lblForCadEnt.setBounds(630, 230, 90, 20);

        txtForCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtForCadEnt.setFont(fontmed(13));
        txtForCadEnt.setBorder(null);
        txtForCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtForCadEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtForCadEntFocusLost(evt);
            }
        });
        pnlCadEnt.add(txtForCadEnt);
        txtForCadEnt.setBounds(630, 230, 160, 20);

        sepForCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepForCadEnt);
        sepForCadEnt.setBounds(630, 250, 160, 10);

        lblCliCadEnt.setFont(fontmed(12));
        lblCliCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCliCadEnt.setText("Cliente");
        lblCliCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblCliCadEnt);
        lblCliCadEnt.setBounds(630, 130, 90, 20);

        txtCliCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtCliCadEnt.setFont(fontmed(13));
        txtCliCadEnt.setBorder(null);
        txtCliCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCliCadEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCliCadEntFocusLost(evt);
            }
        });
        pnlCadEnt.add(txtCliCadEnt);
        txtCliCadEnt.setBounds(630, 130, 160, 20);

        sepCliCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepCliCadEnt);
        sepCliCadEnt.setBounds(630, 150, 160, 10);

        btnGroup3.add(rbtnCreCadEnt);
        rbtnCreCadEnt.setFont(fontmed(12));
        rbtnCreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCreCadEnt.setText("Crédito");
        rbtnCreCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCreCadEnt.setEnabled(false);
        rbtnCreCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCreCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnCreCadEnt);
        rbtnCreCadEnt.setBounds(690, 60, 80, 21);

        btnGroup3.add(rbtnDebCadEnt);
        rbtnDebCadEnt.setFont(fontmed(12));
        rbtnDebCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnDebCadEnt.setText("Débito");
        rbtnDebCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnDebCadEnt.setEnabled(false);
        rbtnDebCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnDebCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnDebCadEnt);
        rbtnDebCadEnt.setBounds(610, 60, 80, 20);

        spnParCadEnt.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));
        spnParCadEnt.setFont(fontmed(13));
        spnParCadEnt.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        spnParCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        spnParCadEnt.setEditor(new javax.swing.JSpinner.NumberEditor(spnParCadEnt, ""));
        spnParCadEnt.setEnabled(false);
        spnParCadEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spnParCadEntKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                spnParCadEntKeyTyped(evt);
            }
        });
        pnlCadEnt.add(spnParCadEnt);
        spnParCadEnt.setBounds(774, 55, 55, 30);

        lblParCadEnt.setFont(fontmed(12));
        lblParCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblParCadEnt.setText("parcela(s)");
        lblParCadEnt.setEnabled(false);
        pnlCadEnt.add(lblParCadEnt);
        lblParCadEnt.setBounds(838, 59, 80, 20);

        pnlPri.add(pnlCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 420));

        pnlIteCadEnt.setBackground(new java.awt.Color(246, 246, 246));
        pnlIteCadEnt.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlIteCadEntComponentShown(evt);
            }
        });
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
        tblEstIteCadEnt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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
        tblSelIteCadEnt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        btnGroup2.add(rbtnAssIteCadEnt);
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

        btnGroup2.add(rbtnPelIteCadEnt);
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

        btnGroup2.add(rbtnCapIteCadEnt);
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

        btnGroup2.add(rbtnChiIteCadEnt);
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

        pnlPri.add(pnlIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

        pnlConEnt.setBackground(new java.awt.Color(246, 246, 246));
        pnlConEnt.setLayout(null);

        btnCanConEnt.setFont(fontmed(12));
        btnCanConEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnCanConEnt.setText("Cancelar");
        btnCanConEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanConEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanConEntActionPerformed(evt);
            }
        });
        pnlConEnt.add(btnCanConEnt);
        btnCanConEnt.setBounds(870, 10, 90, 40);

        btnBusConEnt.setFont(fontmed(12));
        btnBusConEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnBusConEnt.setText("Buscar");
        btnBusConEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBusConEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusConEntActionPerformed(evt);
            }
        });
        pnlConEnt.add(btnBusConEnt);
        btnBusConEnt.setBounds(760, 10, 90, 40);

        lblBusConEnt.setFont(fontmed(12));
        lblBusConEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblBusConEnt.setText("Buscar");
        lblBusConEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlConEnt.add(lblBusConEnt);
        lblBusConEnt.setBounds(450, 30, 50, 20);

        txtBusConEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtBusConEnt.setFont(fontmed(13));
        txtBusConEnt.setBorder(null);
        txtBusConEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusConEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusConEntFocusLost(evt);
            }
        });
        txtBusConEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusConEntKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusConEntKeyTyped(evt);
            }
        });
        pnlConEnt.add(txtBusConEnt);
        txtBusConEnt.setBounds(450, 30, 290, 20);

        sepBusConEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlConEnt.add(sepBusConEst1);
        sepBusConEst1.setBounds(450, 50, 290, 10);

        scrConEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrConEnt.setBorder(BorderFactory.createEmptyBorder());

        tblConEnt.setBackground(new java.awt.Color(246, 246, 246));
        tblConEnt.setBorder(null);
        tblConEnt.setFont(fontmed(10));
        tblConEnt.setModel(new javax.swing.table.DefaultTableModel(
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
        tblConEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblConEnt.setEnabled(false);
        tblConEnt.setFocusable(false);
        scrConEnt.setViewportView(tblConEnt);

        pnlConEnt.add(scrConEnt);
        scrConEnt.setBounds(60, 100, 1180, 200);

        pnlPri.add(pnlConEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        btnExcGerEnt.setBounds(1180, 230, 90, 50);

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
        btnIteGerEnt.setBounds(980, 230, 90, 50);

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
        lblDatGerEnt.setBounds(780, 80, 40, 20);

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
        txtDatGerEnt.setBounds(780, 80, 130, 20);

        sepDatGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepDatGerEnt);
        sepDatGerEnt.setBounds(780, 100, 130, 10);

        lblR$GerEnt.setFont(fontmed(13));
        lblR$GerEnt.setText("R$");
        lblR$GerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        lblR$GerEnt.setFocusable(false);
        pnlGerEnt.add(lblR$GerEnt);
        lblR$GerEnt.setBounds(780, 130, 20, 21);

        lblPreGerEnt.setFont(fontmed(12));
        lblPreGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblPreGerEnt.setText("Preço");
        lblPreGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblPreGerEnt);
        lblPreGerEnt.setBounds(780, 130, 40, 20);

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
        txtPreGerEnt.setBounds(800, 130, 80, 20);

        sepPreGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepPreGerEnt);
        sepPreGerEnt.setBounds(780, 150, 100, 10);

        lblDetGerEnt.setFont(fontmed(12));
        lblDetGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDetGerEnt.setText("Detalhes");
        lblDetGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblDetGerEnt);
        lblDetGerEnt.setBounds(780, 180, 70, 20);

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
        txtDetGerEnt.setBounds(780, 180, 190, 20);

        sepDetGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepDetGerEnt);
        sepDetGerEnt.setBounds(780, 200, 190, 10);

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDatBusGerEntKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatBusGerEntKeyTyped(evt);
            }
        });
        pnlGerEnt.add(txtDatBusGerEnt);
        txtDatBusGerEnt.setBounds(70, 50, 190, 20);

        sepMod3.setForeground(new java.awt.Color(10, 60, 133));
        sepMod3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlGerEnt.add(sepMod3);
        sepMod3.setBounds(745, 80, 20, 210);

        sepBusGerEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepBusGerEst1);
        sepBusGerEst1.setBounds(70, 70, 190, 10);

        cmbSerGerEnt.setFont(fontmed(13));
        cmbSerGerEnt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o serviço" }));
        cmbSerGerEnt.setToolTipText("");
        cmbSerGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlGerEnt.add(cmbSerGerEnt);
        cmbSerGerEnt.setBounds(780, 250, 190, 30);

        lblSerGerEnt.setFont(fontmed(12));
        lblSerGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSerGerEnt.setText("Serviço");
        pnlGerEnt.add(lblSerGerEnt);
        lblSerGerEnt.setBounds(780, 220, 90, 30);

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
        tblGerEnt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGerEntMouseClicked(evt);
            }
        });
        scrGerEnt.setViewportView(tblGerEnt);

        pnlGerEnt.add(scrGerEnt);
        scrGerEnt.setBounds(30, 170, 680, 170);

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
        btnAltGerEnt.setBounds(1080, 230, 90, 50);

        btnGroup.add(rbtnCarGerEnt);
        rbtnCarGerEnt.setFont(fontmed(12));
        rbtnCarGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCarGerEnt.setText("Cartão");
        rbtnCarGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCarGerEnt.setEnabled(false);
        rbtnCarGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCarGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(rbtnCarGerEnt);
        rbtnCarGerEnt.setBounds(877, 20, 90, 21);

        btnGroup.add(rbtnPixGerEnt);
        rbtnPixGerEnt.setFont(fontmed(12));
        rbtnPixGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPixGerEnt.setText("PIX");
        rbtnPixGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPixGerEnt.setEnabled(false);
        rbtnPixGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPixGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(rbtnPixGerEnt);
        rbtnPixGerEnt.setBounds(967, 20, 90, 21);

        btnGroup.add(rbtnDinGerEnt);
        rbtnDinGerEnt.setFont(fontmed(12));
        rbtnDinGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnDinGerEnt.setText("Dinheiro");
        rbtnDinGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnDinGerEnt.setEnabled(false);
        rbtnDinGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnDinGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(rbtnDinGerEnt);
        rbtnDinGerEnt.setBounds(777, 20, 90, 21);

        lblCliGerEnt.setFont(fontmed(12));
        lblCliGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCliGerEnt.setText("Cliente");
        lblCliGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblCliGerEnt);
        lblCliGerEnt.setBounds(1020, 80, 90, 20);

        txtCliGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtCliGerEnt.setFont(fontmed(13));
        txtCliGerEnt.setBorder(null);
        txtCliGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCliGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCliGerEntFocusLost(evt);
            }
        });
        pnlGerEnt.add(txtCliGerEnt);
        txtCliGerEnt.setBounds(1020, 80, 160, 20);

        sepCliGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepCliGerEnt);
        sepCliGerEnt.setBounds(1020, 100, 160, 10);

        lblCusGerEnt.setFont(fontmed(12));
        lblCusGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCusGerEnt.setText("Custo");
        lblCusGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblCusGerEnt);
        lblCusGerEnt.setBounds(1020, 130, 40, 20);

        lblR$CusGerEnt.setFont(fontmed(13));
        lblR$CusGerEnt.setText("R$");
        lblR$CusGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblR$CusGerEnt);
        lblR$CusGerEnt.setBounds(1020, 130, 20, 20);

        txtCusGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtCusGerEnt.setFont(fontmed(13));
        txtCusGerEnt.setBorder(null);
        txtCusGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCusGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCusGerEntFocusLost(evt);
            }
        });
        txtCusGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCusGerEntActionPerformed(evt);
            }
        });
        txtCusGerEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCusGerEntKeyTyped(evt);
            }
        });
        pnlGerEnt.add(txtCusGerEnt);
        txtCusGerEnt.setBounds(1040, 130, 80, 20);

        sepCusGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepCusGerEnt);
        sepCusGerEnt.setBounds(1020, 150, 100, 10);

        lblForGerEnt.setFont(fontmed(12));
        lblForGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblForGerEnt.setText("Fornecedor");
        lblForGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblForGerEnt);
        lblForGerEnt.setBounds(1020, 180, 90, 20);

        txtForGerEnt.setBackground(new java.awt.Color(246, 246, 246));
        txtForGerEnt.setFont(fontmed(13));
        txtForGerEnt.setBorder(null);
        txtForGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtForGerEntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtForGerEntFocusLost(evt);
            }
        });
        pnlGerEnt.add(txtForGerEnt);
        txtForGerEnt.setBounds(1020, 180, 160, 20);

        sepForGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepForGerEnt);
        sepForGerEnt.setBounds(1020, 200, 160, 10);

        pnlPri.add(pnlGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        tblEstIteGerEnt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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
        tblSelIteGerEnt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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
        rbtnAssIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAssIteGerEntActionPerformed(evt);
            }
        });
        pnlIteGerEnt.add(rbtnAssIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 100, -1));

        btnGroup1.add(rbtnPelIteGerEnt);
        rbtnPelIteGerEnt.setFont(fontmed(12));
        rbtnPelIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPelIteGerEnt.setText("Película");
        rbtnPelIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPelIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPelIteGerEntActionPerformed(evt);
            }
        });
        pnlIteGerEnt.add(rbtnPelIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 80, -1));

        btnGroup1.add(rbtnCapIteGerEnt);
        rbtnCapIteGerEnt.setFont(fontmed(12));
        rbtnCapIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCapIteGerEnt.setText("Capinha");
        rbtnCapIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCapIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCapIteGerEntActionPerformed(evt);
            }
        });
        pnlIteGerEnt.add(rbtnCapIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 90, -1));

        btnGroup1.add(rbtnChiIteGerEnt);
        rbtnChiIteGerEnt.setFont(fontmed(12));
        rbtnChiIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnChiIteGerEnt.setText("Chip");
        rbtnChiIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnChiIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnChiIteGerEntActionPerformed(evt);
            }
        });
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

        pnlPri.add(pnlIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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

        scrCadEst.setBackground(new java.awt.Color(250, 250, 250));
        scrCadEst.setBorder(BorderFactory.createEmptyBorder());

        tblCadEst.setBackground(new java.awt.Color(246, 246, 246));
        tblCadEst.setBorder(null);
        tblCadEst.setFont(fontmed(10));
        tblCadEst.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblCadEst.setFocusable(false);
        tblCadEst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCadEstMouseClicked(evt);
            }
        });
        scrCadEst.setViewportView(tblCadEst);

        pnlCadEst.add(scrCadEst);
        scrCadEst.setBounds(40, 80, 290, 250);

        lblProCadEst.setFont(fontbold(12));
        lblProCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblProCadEst.setText("Produtos registrados");
        lblProCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblProCadEst);
        lblProCadEst.setBounds(40, 50, 160, 20);

        lblModCadEst.setFont(fontmed(12));
        lblModCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblModCadEst.setText("Modelo ");
        lblModCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblModCadEst);
        lblModCadEst.setBounds(410, 130, 70, 20);

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
        txtModCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModCadEstActionPerformed(evt);
            }
        });
        txtModCadEst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtModCadEstKeyPressed(evt);
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
        lblMatCadEst.setBounds(700, 130, 70, 20);

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
        cmbChiCadEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o chip", "Triplo 4G HLR 230", "eSIM", "Naked" }));
        cmbChiCadEst.setToolTipText("");
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

        pnlPri.add(pnlCadEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusConEstKeyPressed(evt);
            }
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
        tblConEst.setFont(fontmed(10));
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
        tblConEst.setEnabled(false);
        tblConEst.setFocusable(false);
        scrConEst.setViewportView(tblConEst);

        pnlConEst.add(scrConEst);
        scrConEst.setBounds(160, 170, 980, 150);
        pnlConEst.add(txtTipConEst);
        txtTipConEst.setBounds(180, 50, 64, 22);

        pnlPri.add(pnlConEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        lblModGerEst.setBounds(730, 110, 70, 20);

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
        lblMatGerEst.setBounds(1030, 110, 80, 20);

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
        cmbChiGerEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o chip", "Triplo 4G HLR 230", "eSIM", "Naked" }));
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusGerEstKeyPressed(evt);
            }
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
        tblGerEst.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGerEst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGerEstMouseClicked(evt);
            }
        });
        scrGerEst.setViewportView(tblGerEst);

        pnlGerEst.add(scrGerEst);
        scrGerEst.setBounds(20, 210, 600, 130);

        pnlPri.add(pnlGerEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

        pnlRel.setBackground(new java.awt.Color(246, 246, 246));
        pnlRel.setLayout(null);

        lblResRel.setFont(fontbold(14));
        lblResRel.setForeground(new java.awt.Color(10, 60, 133));
        lblResRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblResRel.setText("Sem resultados para o período selecionado!");
        lblResRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblResRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblResRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblResRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblResRelMouseReleased(evt);
            }
        });
        pnlRel.add(lblResRel);
        lblResRel.setBounds(240, 270, 340, 20);

        cmbRel.setFont(fontmed(13));
        cmbRel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filtrar resultados" }));
        cmbRel.setToolTipText("");
        cmbRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRelActionPerformed(evt);
            }
        });
        pnlRel.add(cmbRel);
        cmbRel.setBounds(610, 83, 190, 30);

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
        tblRel.setToolTipText("");
        tblRel.setAutoscrolls(false);
        tblRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblRel.setEnabled(false);
        tblRel.setFocusable(false);
        tblRel.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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
        btnVolRel.setBounds(60, 123, 90, 50);

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
        rbtnSerRel.setBounds(168, 30, 90, 21);

        btnGroup.add(rbtnVenRel1);
        rbtnVenRel1.setFont(fontmed(12));
        rbtnVenRel1.setForeground(new java.awt.Color(10, 60, 133));
        rbtnVenRel1.setText("Serviço e Venda");
        rbtnVenRel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnVenRel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnVenRel1ActionPerformed(evt);
            }
        });
        pnlRel.add(rbtnVenRel1);
        rbtnVenRel1.setBounds(405, 30, 150, 21);

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
        rbtnVenRel.setBounds(286, 30, 80, 21);

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
        rbtnAssRel.setBounds(583, 30, 100, 21);

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
        rbtnTodRel.setBounds(58, 30, 70, 21);

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
        btnTodRel.setBounds(250, 90, 50, 20);

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
        btnAnoRel.setBounds(549, 90, 30, 20);

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
        txtDatFinRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDatFinRelActionPerformed(evt);
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

        lblDiaRel.setFont(fontbold(12));
        lblDiaRel.setForeground(new java.awt.Color(10, 60, 133));
        lblDiaRel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDiaRel.setText("Este dia");
        lblDiaRel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblDiaRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDiaRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDiaRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblDiaRelMouseReleased(evt);
            }
        });
        pnlRel.add(lblDiaRel);
        lblDiaRel.setBounds(60, 85, 120, 20);

        btnMenDiaRel.setFont(fontbold(18));
        btnMenDiaRel.setForeground(new java.awt.Color(255, 0, 0));
        btnMenDiaRel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMenDiaRel.setText("-");
        btnMenDiaRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMenDiaRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenDiaRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMenDiaRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMenDiaRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnMenDiaRel);
        btnMenDiaRel.setBounds(60, 65, 15, 20);

        btnNumDiaRel.setFont(fontbold(14));
        btnNumDiaRel.setForeground(new java.awt.Color(10, 60, 133));
        btnNumDiaRel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNumDiaRel.setText("10");
        btnNumDiaRel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnNumDiaRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNumDiaRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNumDiaRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnNumDiaRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnNumDiaRel);
        btnNumDiaRel.setBounds(60, 75, 100, 20);

        btnMaiDiaRel.setFont(fontbold(18));
        btnMaiDiaRel.setForeground(new java.awt.Color(51, 204, 0));
        btnMaiDiaRel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMaiDiaRel.setText("+");
        btnMaiDiaRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMaiDiaRel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMaiDiaRelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMaiDiaRelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnMaiDiaRelMouseReleased(evt);
            }
        });
        pnlRel.add(btnMaiDiaRel);
        btnMaiDiaRel.setBounds(85, 65, 15, 20);

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
        btnDiaRel.setBounds(330, 90, 30, 20);

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
        btnMesRel.setBounds(486, 90, 30, 20);

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
        btnSemRel.setBounds(378, 90, 80, 20);

        sepDatCadEnt3.setForeground(new java.awt.Color(10, 60, 133));
        pnlRel.add(sepDatCadEnt3);
        sepDatCadEnt3.setBounds(430, 170, 100, 10);

        lblValDinRel.setFont(fontmed(16));
        lblValDinRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValDinRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValDinRel.setText("R$0,00");
        pnlRel.add(lblValDinRel);
        lblValDinRel.setBounds(870, 310, 110, 30);

        jLabel7.setFont(fontmed(14));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Dinheiro");
        pnlRel.add(jLabel7);
        jLabel7.setBounds(870, 290, 110, 20);

        lblTotEntRel.setFont(fontmed(18));
        lblTotEntRel.setForeground(new java.awt.Color(10, 60, 133));
        lblTotEntRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotEntRel.setText("0");
        pnlRel.add(lblTotEntRel);
        lblTotEntRel.setBounds(990, 70, 160, 30);

        jLabel2.setFont(fontbold(16));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Total de entradas");
        pnlRel.add(jLabel2);
        jLabel2.setBounds(990, 50, 160, 20);

        lblValTotRel.setFont(fontmed(18));
        lblValTotRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValTotRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValTotRel.setText("R$0,00");
        pnlRel.add(lblValTotRel);
        lblValTotRel.setBounds(990, 150, 160, 30);

        jLabel4.setFont(fontbold(16));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Valor total");
        pnlRel.add(jLabel4);
        jLabel4.setBounds(990, 130, 160, 20);

        lblValMedRel.setFont(fontmed(18));
        lblValMedRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValMedRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValMedRel.setText("R$0,00");
        pnlRel.add(lblValMedRel);
        lblValMedRel.setBounds(990, 230, 160, 30);

        jLabel6.setFont(fontbold(16));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Custo");
        pnlRel.add(jLabel6);
        jLabel6.setBounds(990, 210, 160, 20);

        jLabel8.setFont(fontmed(14));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Cartão");
        pnlRel.add(jLabel8);
        jLabel8.setBounds(1010, 290, 120, 20);

        lblValCarRel.setFont(fontmed(16));
        lblValCarRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValCarRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValCarRel.setText("R$0,00");
        pnlRel.add(lblValCarRel);
        lblValCarRel.setBounds(1010, 310, 120, 30);

        jLabel9.setFont(fontmed(14));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("PIX");
        pnlRel.add(jLabel9);
        jLabel9.setBounds(1140, 290, 108, 20);

        lblValPixRel.setFont(fontmed(16));
        lblValPixRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValPixRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValPixRel.setText("R$0,00");
        pnlRel.add(lblValPixRel);
        lblValPixRel.setBounds(1140, 310, 108, 30);

        chkCus.setFont(fontmed(12));
        chkCus.setForeground(new java.awt.Color(10, 60, 133));
        chkCus.setText("Custo");
        chkCus.setBorder(null);
        chkCus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCus.setEnabled(false);
        chkCus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCusActionPerformed(evt);
            }
        });
        pnlRel.add(chkCus);
        chkCus.setBounds(730, 30, 60, 20);

        pnlPri.add(pnlRel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        btnGerOs.setBounds(720, 300, 90, 50);

        chkGarOs.setFont(fontmed(12));
        chkGarOs.setForeground(new java.awt.Color(10, 60, 133));
        chkGarOs.setText("Garantia");
        chkGarOs.setBorder(null);
        chkGarOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkGarOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGarOsActionPerformed(evt);
            }
        });
        pnlOs.add(chkGarOs);
        chkGarOs.setBounds(370, 330, 80, 30);

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
        btnCanOs.setBounds(830, 300, 90, 50);

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
        lblEquOs.setBounds(700, 30, 130, 20);

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelOsKeyReleased(evt);
            }
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
        lblConOs.setText("Defeito relatado");
        lblConOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblConOs);
        lblConOs.setBounds(700, 180, 130, 20);

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
        txtDefOs.setBounds(700, 180, 240, 20);

        sepDetCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst1);
        sepDetCadEst1.setBounds(700, 200, 240, 10);

        lblDefOs.setFont(fontmed(12));
        lblDefOs.setForeground(new java.awt.Color(10, 60, 133));
        lblDefOs.setText("Reparo");
        lblDefOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblDefOs);
        lblDefOs.setBounds(700, 230, 100, 20);

        txtRepOs.setBackground(new java.awt.Color(246, 246, 246));
        txtRepOs.setFont(fontmed(13));
        txtRepOs.setBorder(null);
        txtRepOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRepOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRepOsFocusLost(evt);
            }
        });
        pnlOs.add(txtRepOs);
        txtRepOs.setBounds(700, 230, 240, 20);

        sepDetCadEst2.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst2);
        sepDetCadEst2.setBounds(700, 250, 240, 10);

        lblDatEntOs.setFont(fontmed(12));
        lblDatEntOs.setForeground(new java.awt.Color(10, 60, 133));
        lblDatEntOs.setText("Data entrada");
        lblDatEntOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblDatEntOs);
        lblDatEntOs.setBounds(370, 180, 110, 20);

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
        lblHorOs.setText("Data saída");
        lblHorOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblHorOs);
        lblHorOs.setBounds(370, 230, 80, 20);

        txtDatSaiOs.setBackground(new java.awt.Color(246, 246, 246));
        txtDatSaiOs.setFont(fontmed(13));
        txtDatSaiOs.setBorder(null);
        txtDatSaiOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatSaiOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatSaiOsFocusLost(evt);
            }
        });
        txtDatSaiOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatSaiOsKeyTyped(evt);
            }
        });
        pnlOs.add(txtDatSaiOs);
        txtDatSaiOs.setBounds(370, 230, 90, 20);

        sepDetCadEst4.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst4);
        sepDetCadEst4.setBounds(370, 250, 90, 10);

        sepCusGerEnt1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepCusGerEnt1);
        sepCusGerEnt1.setBounds(370, 300, 90, 10);

        lblPreOs.setFont(fontmed(12));
        lblPreOs.setForeground(new java.awt.Color(10, 60, 133));
        lblPreOs.setText("Preço");
        lblPreOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblPreOs);
        lblPreOs.setBounds(370, 280, 40, 20);

        lblR$Os.setFont(fontmed(13));
        lblR$Os.setText("R$");
        lblR$Os.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblR$Os);
        lblR$Os.setBounds(370, 280, 20, 20);

        txtPreOs.setBackground(new java.awt.Color(246, 246, 246));
        txtPreOs.setFont(fontmed(13));
        txtPreOs.setBorder(null);
        txtPreOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPreOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPreOsFocusLost(evt);
            }
        });
        txtPreOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPreOsActionPerformed(evt);
            }
        });
        txtPreOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPreOsKeyTyped(evt);
            }
        });
        pnlOs.add(txtPreOs);
        txtPreOs.setBounds(390, 280, 70, 20);

        pnlPri.add(pnlOs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

        pnlGerOs.setBackground(new java.awt.Color(246, 246, 246));
        pnlGerOs.setLayout(null);

        scrOs.setBackground(new java.awt.Color(250, 250, 250));
        scrOs.setBorder(BorderFactory.createEmptyBorder());

        tblOs.setBackground(new java.awt.Color(246, 246, 246));
        tblOs.setBorder(null);
        tblOs.setFont(fontmed(10));
        tblOs.setForeground(new java.awt.Color(229, 192, 191));
        tblOs.setModel(new javax.swing.table.DefaultTableModel(
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
        tblOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblOs.setFocusable(false);
        tblOs.setGridColor(new java.awt.Color(192, 211, 250));
        tblOs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOsMouseClicked(evt);
            }
        });
        scrOs.setViewportView(tblOs);

        pnlGerOs.add(scrOs);
        scrOs.setBounds(160, 20, 980, 250);

        btnAltGerOs.setFont(fontmed(12));
        btnAltGerOs.setForeground(new java.awt.Color(10, 60, 133));
        btnAltGerOs.setText("Alterar");
        btnAltGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltGerOs.setEnabled(false);
        btnAltGerOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltGerOsActionPerformed(evt);
            }
        });
        pnlGerOs.add(btnAltGerOs);
        btnAltGerOs.setBounds(920, 280, 100, 50);

        btnGerGerOs.setFont(fontmed(12));
        btnGerGerOs.setForeground(new java.awt.Color(10, 60, 133));
        btnGerGerOs.setText("Gerar");
        btnGerGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerGerOs.setEnabled(false);
        btnGerGerOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerGerOsActionPerformed(evt);
            }
        });
        pnlGerOs.add(btnGerGerOs);
        btnGerGerOs.setBounds(800, 280, 100, 50);

        btnExcGerOs.setFont(fontmed(12));
        btnExcGerOs.setForeground(new java.awt.Color(10, 60, 133));
        btnExcGerOs.setText("Excluir");
        btnExcGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcGerOs.setEnabled(false);
        btnExcGerOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcGerOsActionPerformed(evt);
            }
        });
        pnlGerOs.add(btnExcGerOs);
        btnExcGerOs.setBounds(1040, 280, 100, 50);

        btnVolGerOs.setFont(fontmed(12));
        btnVolGerOs.setForeground(new java.awt.Color(10, 60, 133));
        btnVolGerOs.setText("Voltar");
        btnVolGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolGerOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolGerOsActionPerformed(evt);
            }
        });
        pnlGerOs.add(btnVolGerOs);
        btnVolGerOs.setBounds(160, 280, 100, 50);

        lblErrGerOs.setFont(fontbold(10));
        lblErrGerOs.setForeground(new java.awt.Color(204, 51, 0));
        lblErrGerOs.setText("Nenhum registro encontrado!");
        lblErrGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerOs.add(lblErrGerOs);
        lblErrGerOs.setBounds(310, 330, 190, 20);

        lblBusGerOs.setFont(fontmed(12));
        lblBusGerOs.setForeground(new java.awt.Color(10, 60, 133));
        lblBusGerOs.setText("Buscar");
        lblBusGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerOs.add(lblBusGerOs);
        lblBusGerOs.setBounds(310, 300, 80, 20);

        txtBusGerOs.setBackground(new java.awt.Color(246, 246, 246));
        txtBusGerOs.setFont(fontmed(13));
        txtBusGerOs.setBorder(null);
        txtBusGerOs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusGerOsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusGerOsFocusLost(evt);
            }
        });
        txtBusGerOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusGerOsActionPerformed(evt);
            }
        });
        txtBusGerOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusGerOsKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusGerOsKeyTyped(evt);
            }
        });
        pnlGerOs.add(txtBusGerOs);
        txtBusGerOs.setBounds(310, 300, 200, 20);

        sepBusVen1.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerOs.add(sepBusVen1);
        sepBusVen1.setBounds(310, 320, 200, 10);

        pnlPri.add(pnlGerOs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        btnSalTipSer.setBounds(545, 160, 90, 40);

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
        btnCanTipSer.setBounds(645, 160, 90, 40);

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
        rbtnOutTipSer.setBounds(780, 10, 80, 21);

        btnGroup.add(rbtnSerTimTipSer);
        rbtnSerTimTipSer.setFont(fontmed(12));
        rbtnSerTimTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnSerTimTipSer.setText("Serviço");
        rbtnSerTimTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnSerTimTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSerTimTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(rbtnSerTimTipSer);
        rbtnSerTimTipSer.setBounds(450, 10, 70, 21);

        btnGroup.add(rbtnAssTipSer);
        rbtnAssTipSer.setFont(fontmed(12));
        rbtnAssTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAssTipSer.setText("Assistência Técnica");
        rbtnAssTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAssTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAssTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(rbtnAssTipSer);
        rbtnAssTipSer.setBounds(576, 10, 170, 21);

        lblDesTipSer.setFont(fontmed(12));
        lblDesTipSer.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer.setText("Descrição");
        lblDesTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadTipSer.add(lblDesTipSer);
        lblDesTipSer.setBounds(505, 110, 70, 20);

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
        txtDesTipSer.setBounds(505, 110, 280, 20);

        sepDesTipSer.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadTipSer.add(sepDesTipSer);
        sepDesTipSer.setBounds(505, 130, 280, 10);

        pnlPri.add(pnlCadTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        btnExcGerTipSer.setBounds(650, 290, 100, 40);

        btnAtvGerTipSer.setFont(fontmed(12));
        btnAtvGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        btnAtvGerTipSer.setText("Desativar");
        btnAtvGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAtvGerTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtvGerTipSerActionPerformed(evt);
            }
        });
        pnlGerTipSer.add(btnAtvGerTipSer);
        btnAtvGerTipSer.setBounds(430, 290, 100, 40);

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
        btnAltGerTipSer.setBounds(540, 290, 100, 40);

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
        btnCanGerTipSer.setBounds(760, 290, 100, 40);

        lblDesTipSer2.setFont(fontmed(12));
        lblDesTipSer2.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer2.setText("Escolha um para gerenciar");
        lblDesTipSer2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerTipSer.add(lblDesTipSer2);
        lblDesTipSer2.setBounds(510, 50, 260, 20);

        lblDesGerTipSer.setFont(fontmed(12));
        lblDesGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        lblDesGerTipSer.setText("Descrição");
        lblDesGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerTipSer.add(lblDesGerTipSer);
        lblDesGerTipSer.setBounds(510, 240, 70, 20);

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
        txtDesGerTipSer.setBounds(510, 240, 270, 20);

        sepDesGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerTipSer.add(sepDesGerTipSer);
        sepDesGerTipSer.setBounds(510, 260, 270, 10);

        scrTipSer.setBackground(new java.awt.Color(250, 250, 250));
        scrTipSer.setBorder(BorderFactory.createEmptyBorder());
        scrTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tblTipSer.setTableHeader(null);
        tblTipSer.setBackground(new java.awt.Color(246, 246, 246));
        tblTipSer.setBorder(null);
        tblTipSer.setFont(fontmed(10));
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
        tblTipSer.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTipSer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTipSerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblTipSerMouseEntered(evt);
            }
        });
        scrTipSer.setViewportView(tblTipSer);

        pnlGerTipSer.add(scrTipSer);
        scrTipSer.setBounds(510, 80, 270, 120);

        btnGroup.add(rbtnOutGerTipSer);
        rbtnOutGerTipSer.setFont(fontmed(12));
        rbtnOutGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnOutGerTipSer.setText("Outros");
        rbtnOutGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnOutGerTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnOutGerTipSerActionPerformed(evt);
            }
        });
        pnlGerTipSer.add(rbtnOutGerTipSer);
        rbtnOutGerTipSer.setBounds(780, 10, 100, 21);

        btnGroup.add(rbtnAssGerTipSer);
        rbtnAssGerTipSer.setFont(fontmed(12));
        rbtnAssGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAssGerTipSer.setText("Assistência Técnica");
        rbtnAssGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnAssGerTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnAssGerTipSerActionPerformed(evt);
            }
        });
        pnlGerTipSer.add(rbtnAssGerTipSer);
        rbtnAssGerTipSer.setBounds(576, 10, 150, 21);

        btnGroup.add(rbtnTimGerTipSer);
        rbtnTimGerTipSer.setFont(fontmed(12));
        rbtnTimGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        rbtnTimGerTipSer.setText("Serviço");
        rbtnTimGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnTimGerTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnTimGerTipSerActionPerformed(evt);
            }
        });
        pnlGerTipSer.add(rbtnTimGerTipSer);
        rbtnTimGerTipSer.setBounds(450, 10, 80, 21);

        pnlPri.add(pnlGerTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

        pnlMas.setBackground(new java.awt.Color(246, 246, 246));
        pnlMas.setLayout(null);

        chkCarMasa.setFont(fontmed(12));
        chkCarMasa.setForeground(new java.awt.Color(10, 60, 133));
        chkCarMasa.setVisible(false);
        chkCarMasa.setText("Cartão de Crédito");
        pnlMas.add(chkCarMasa);
        chkCarMasa.setBounds(410, 290, 150, 20);

        chkAppMas.setFont(fontmed(12));
        chkAppMas.setForeground(new java.awt.Color(10, 60, 133));
        chkAppMas.setText("APP MEU TIM");
        pnlMas.add(chkAppMas);
        chkAppMas.setBounds(610, 290, 150, 20);

        chkMelMas.setFont(fontmed(12));
        chkMelMas.setForeground(new java.awt.Color(10, 60, 133));
        chkMelMas.setText("Melhor Data");
        pnlMas.add(chkMelMas);
        chkMelMas.setBounds(610, 250, 150, 20);

        btnConMas.setFont(fontmed(12));
        btnConMas.setForeground(new java.awt.Color(10, 60, 133));
        btnConMas.setText("Contrato");
        btnConMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConMasActionPerformed(evt);
            }
        });
        pnlMas.add(btnConMas);
        btnConMas.setBounds(90, 280, 100, 40);

        btnVenMas.setFont(fontmed(12));
        btnVenMas.setForeground(new java.awt.Color(10, 60, 133));
        btnVenMas.setText("Cadastrar");
        btnVenMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVenMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenMasActionPerformed(evt);
            }
        });
        pnlMas.add(btnVenMas);
        btnVenMas.setBounds(200, 230, 100, 40);

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
        btnGerMas.setBounds(90, 230, 100, 40);

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
        btnCanMas.setBounds(90, 330, 100, 40);

        btnParMas.setFont(fontmed(12));
        btnParMas.setForeground(new java.awt.Color(10, 60, 133));
        btnParMas.setText("Parcial");
        btnParMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnParMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParMasActionPerformed(evt);
            }
        });
        pnlMas.add(btnParMas);
        btnParMas.setBounds(200, 280, 100, 40);

        btnCopMas.setFont(fontbold(11));
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
        btnCopMas.setBounds(950, 260, 50, 20);

        lblNomMas.setFont(fontmed(12));
        lblNomMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNomMas.setText("Nome");
        lblNomMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNomMas);
        lblNomMas.setBounds(90, 60, 70, 20);

        chkDebMasa.setFont(fontmed(12));
        chkDebMasa.setForeground(new java.awt.Color(10, 60, 133));
        chkDebMasa.setVisible(false);
        chkDebMasa.setText("Débito em conta");
        pnlMas.add(chkDebMasa);
        chkDebMasa.setBounds(410, 260, 150, 20);

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
        txtNomMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomMasKeyReleased(evt);
            }
        });
        pnlMas.add(txtNomMas);
        txtNomMas.setBounds(90, 60, 190, 20);

        sepDesGerTipSer1.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer1);
        sepDesGerTipSer1.setBounds(90, 80, 190, 10);

        sepDesGerTipSer2.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer2);
        sepDesGerTipSer2.setBounds(90, 80, 190, 10);

        lblNumConMas.setFont(fontmed(12));
        lblNumConMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNumConMas.setText("Número de Contato");
        lblNumConMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNumConMas);
        lblNumConMas.setBounds(350, 60, 140, 20);

        sepDesGerTipSer3.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer3);
        sepDesGerTipSer3.setBounds(350, 80, 170, 10);

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumConMasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumConMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtNumConMas);
        txtNumConMas.setBounds(350, 60, 170, 20);

        lblCpfMas.setFont(fontmed(12));
        lblCpfMas.setForeground(new java.awt.Color(10, 60, 133));
        lblCpfMas.setText("CPF");
        lblCpfMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblCpfMas);
        lblCpfMas.setBounds(90, 110, 70, 20);

        sepDesGerTipSer4.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer4);
        sepDesGerTipSer4.setBounds(90, 130, 150, 10);

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCpfMasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCpfMasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCpfMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtCpfMas);
        txtCpfMas.setBounds(90, 110, 150, 20);

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumAceMasKeyReleased(evt);
            }
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
        lblNumPorMas.setBounds(350, 160, 140, 20);

        sepDesGerTipSer6.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer6);
        sepDesGerTipSer6.setBounds(350, 180, 170, 10);

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumPorMasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumPorMasKeyTyped(evt);
            }
        });
        pnlMas.add(txtNumPorMas);
        txtNumPorMas.setBounds(350, 160, 170, 20);

        btnGroup.add(rbtnMigTroMas);
        rbtnMigTroMas.setFont(fontmed(12));
        rbtnMigTroMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnMigTroMas.setText("Conversão");
        rbtnMigTroMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnMigTroMas);
        rbtnMigTroMas.setBounds(610, 70, 250, 21);

        btnGroup.add(rbtnAtiMas);
        rbtnAtiMas.setFont(fontmed(12));
        rbtnAtiMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAtiMas.setSelected(true);
        rbtnAtiMas.setText("Ativação");
        rbtnAtiMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnAtiMas);
        rbtnAtiMas.setBounds(610, 10, 90, 21);

        btnGroup.add(rbtnMigMas);
        rbtnMigMas.setFont(fontmed(12));
        rbtnMigMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnMigMas.setText("Migração");
        rbtnMigMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnMigMas);
        rbtnMigMas.setBounds(610, 40, 90, 21);

        lblPlaMas.setFont(fontmed(12));
        lblPlaMas.setForeground(new java.awt.Color(10, 60, 133));
        lblPlaMas.setText("Plano");
        lblPlaMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblPlaMas);
        lblPlaMas.setBounds(350, 210, 100, 20);

        sepDesGerTipSer7.setForeground(new java.awt.Color(10, 60, 133));
        sepDesGerTipSer7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlMas.add(sepDesGerTipSer7);
        sepDesGerTipSer7.setBounds(850, 37, 10, 230);

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
        txtPlaMas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlaMasKeyReleased(evt);
            }
        });
        pnlMas.add(txtPlaMas);
        txtPlaMas.setBounds(350, 210, 170, 20);

        chkC6Mas.setFont(fontmed(12));
        chkC6Mas.setForeground(new java.awt.Color(10, 60, 133));
        chkC6Mas.setText("Conta C6 Bank");
        pnlMas.add(chkC6Mas);
        chkC6Mas.setBounds(610, 210, 130, 20);

        lblVenMas.setFont(fontmed(12));
        lblVenMas.setForeground(new java.awt.Color(10, 60, 133));
        lblVenMas.setText("Vencimento");
        lblVenMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblVenMas);
        lblVenMas.setBounds(90, 160, 110, 20);

        sepDesGerTipSer8.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer8);
        sepDesGerTipSer8.setBounds(90, 180, 110, 10);

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
        txtVenMas.setBounds(90, 160, 110, 20);

        sepDesGerTipSer9.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer9);
        sepDesGerTipSer9.setBounds(350, 230, 170, 10);

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
        jScrollPane1.setBounds(950, 30, 260, 220);

        btnGroup4.add(chkDebMas);
        chkDebMas.setFont(fontmed(12));
        chkDebMas.setForeground(new java.awt.Color(10, 60, 133));
        chkDebMas.setText("Débito Automático");
        chkDebMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(chkDebMas);
        chkDebMas.setBounds(610, 140, 170, 21);

        btnGroup4.add(chkCarMas);
        chkCarMas.setFont(fontmed(12));
        chkCarMas.setForeground(new java.awt.Color(10, 60, 133));
        chkCarMas.setText("Cartão de Crédito");
        chkCarMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(chkCarMas);
        chkCarMas.setBounds(610, 170, 170, 21);

        btnGroup4.add(chkBolMas);
        chkBolMas.setFont(fontmed(12));
        chkBolMas.setForeground(new java.awt.Color(10, 60, 133));
        chkBolMas.setSelected(true);
        chkBolMas.setText("Boleto");
        chkBolMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(chkBolMas);
        chkBolMas.setBounds(610, 110, 130, 21);

        pnlPri.add(pnlMas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        tblConDes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        pnlPri.add(pnlDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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

        pnlPri.add(pnlCadDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
        tblGerDes.setFont(fontmed(10));
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
        tblGerDes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGerDes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGerDesMouseClicked(evt);
            }
        });
        scrGerDes.setViewportView(tblGerDes);

        pnlGerDes.add(scrGerDes);
        scrGerDes.setBounds(110, 80, 610, 160);

        pnlPri.add(pnlGerDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

        pnlVen.setBackground(new java.awt.Color(246, 246, 246));
        pnlVen.setLayout(null);

        scrVen.setBackground(new java.awt.Color(250, 250, 250));
        scrVen.setBorder(BorderFactory.createEmptyBorder());

        tblVen.setBackground(new java.awt.Color(246, 246, 246));
        tblVen.setBorder(null);
        tblVen.setFont(fontmed(10));
        tblVen.setForeground(new java.awt.Color(229, 192, 191));
        tblVen.setModel(new javax.swing.table.DefaultTableModel(
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
        tblVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblVen.setFocusable(false);
        tblVen.setGridColor(new java.awt.Color(192, 211, 250));
        tblVen.setSelectionBackground(new java.awt.Color(255, 51, 0));
        tblVen.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblVen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVenMouseClicked(evt);
            }
        });
        scrVen.setViewportView(tblVen);

        pnlVen.add(scrVen);
        scrVen.setBounds(160, 20, 980, 220);

        btnCopAVen.setFont(fontmed(12));
        btnCopAVen.setForeground(new java.awt.Color(10, 60, 133));
        btnCopAVen.setText("Acesso");
        btnCopAVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCopAVen.setEnabled(false);
        btnCopAVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopAVenActionPerformed(evt);
            }
        });
        pnlVen.add(btnCopAVen);
        btnCopAVen.setBounds(680, 260, 100, 50);

        btnCopVen.setFont(fontmed(12));
        btnCopVen.setForeground(new java.awt.Color(10, 60, 133));
        btnCopVen.setText("CPF");
        btnCopVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCopVen.setEnabled(false);
        btnCopVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopVenActionPerformed(evt);
            }
        });
        pnlVen.add(btnCopVen);
        btnCopVen.setBounds(560, 260, 100, 50);

        btnAltVen.setFont(fontmed(12));
        btnAltVen.setForeground(new java.awt.Color(10, 60, 133));
        btnAltVen.setText("Alterar");
        btnAltVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltVen.setEnabled(false);
        btnAltVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltVenActionPerformed(evt);
            }
        });
        pnlVen.add(btnAltVen);
        btnAltVen.setBounds(800, 260, 100, 50);

        btnWppVen.setFont(fontmed(12));
        btnWppVen.setForeground(new java.awt.Color(10, 60, 133));
        btnWppVen.setText("WhatsApp");
        btnWppVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWppVen.setEnabled(false);
        btnWppVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWppVenActionPerformed(evt);
            }
        });
        pnlVen.add(btnWppVen);
        btnWppVen.setBounds(1040, 260, 100, 50);

        btnVolVen.setFont(fontmed(12));
        btnVolVen.setForeground(new java.awt.Color(10, 60, 133));
        btnVolVen.setText("Voltar");
        btnVolVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolVenActionPerformed(evt);
            }
        });
        pnlVen.add(btnVolVen);
        btnVolVen.setBounds(160, 260, 100, 50);

        btnExcVen.setFont(fontmed(12));
        btnExcVen.setForeground(new java.awt.Color(10, 60, 133));
        btnExcVen.setText("Excluir");
        btnExcVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcVen.setEnabled(false);
        btnExcVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcVenActionPerformed(evt);
            }
        });
        pnlVen.add(btnExcVen);
        btnExcVen.setBounds(920, 260, 100, 50);

        lblConPlaVen.setFont(fontbold(12));
        lblConPlaVen.setForeground(new java.awt.Color(10, 60, 133));
        lblConPlaVen.setText("0");
        lblConPlaVen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pnlVen.add(lblConPlaVen);
        lblConPlaVen.setBounds(316, 321, 50, 40);

        lblBusVen2.setFont(fontmed(11));
        lblBusVen2.setForeground(new java.awt.Color(10, 60, 133));
        lblBusVen2.setText("Planos ativados este mês:");
        lblBusVen2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pnlVen.add(lblBusVen2);
        lblBusVen2.setBounds(160, 330, 160, 20);

        lblErrVen.setFont(fontbold(10));
        lblErrVen.setForeground(new java.awt.Color(204, 51, 0));
        lblErrVen.setText("Nenhum registro encontrado!");
        lblErrVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlVen.add(lblErrVen);
        lblErrVen.setBounds(310, 305, 190, 20);

        lblBusVen.setFont(fontmed(12));
        lblBusVen.setForeground(new java.awt.Color(10, 60, 133));
        lblBusVen.setText("Buscar");
        lblBusVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlVen.add(lblBusVen);
        lblBusVen.setBounds(310, 280, 80, 20);

        txtBusVen.setBackground(new java.awt.Color(246, 246, 246));
        txtBusVen.setFont(fontmed(13));
        txtBusVen.setBorder(null);
        txtBusVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBusVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusVenFocusLost(evt);
            }
        });
        txtBusVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusVenActionPerformed(evt);
            }
        });
        txtBusVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusVenKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusVenKeyTyped(evt);
            }
        });
        pnlVen.add(txtBusVen);
        txtBusVen.setBounds(310, 280, 200, 20);

        sepBusVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlVen.add(sepBusVen);
        sepBusVen.setBounds(310, 300, 200, 10);

        pnlPri.add(pnlVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

        pnlCadVen.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadVen.setLayout(null);

        lblAceCadVen.setFont(fontmed(12));
        lblAceCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblAceCadVen.setText("Acesso");
        lblAceCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblAceCadVen);
        lblAceCadVen.setBounds(400, 230, 60, 20);

        txtAceCadVen.setBackground(new java.awt.Color(246, 246, 246));
        txtAceCadVen.setFont(fontmed(13));
        txtAceCadVen.setBorder(null);
        txtAceCadVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAceCadVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAceCadVenFocusLost(evt);
            }
        });
        txtAceCadVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAceCadVenKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAceCadVenKeyTyped(evt);
            }
        });
        pnlCadVen.add(txtAceCadVen);
        txtAceCadVen.setBounds(400, 230, 190, 20);

        sepTelCadVen1.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepTelCadVen1);
        sepTelCadVen1.setBounds(400, 250, 190, 10);

        lblVenCadVen.setFont(fontmed(12));
        lblVenCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblVenCadVen.setText("Vencimento");
        lblVenCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblVenCadVen);
        lblVenCadVen.setBounds(700, 180, 90, 20);

        txtVenCadVen.setBackground(new java.awt.Color(246, 246, 246));
        txtVenCadVen.setFont(fontmed(13));
        txtVenCadVen.setBorder(null);
        txtVenCadVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtVenCadVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVenCadVenFocusLost(evt);
            }
        });
        txtVenCadVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVenCadVenKeyTyped(evt);
            }
        });
        pnlCadVen.add(txtVenCadVen);
        txtVenCadVen.setBounds(700, 180, 190, 20);

        sepVenCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepVenCadVen);
        sepVenCadVen.setBounds(700, 200, 190, 10);

        btnSalCadVen.setFont(fontmed(12));
        btnSalCadVen.setForeground(new java.awt.Color(10, 60, 133));
        btnSalCadVen.setText("Salvar");
        btnSalCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalCadVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalCadVenActionPerformed(evt);
            }
        });
        pnlCadVen.add(btnSalCadVen);
        btnSalCadVen.setBounds(700, 250, 90, 50);

        btnCanCadVen.setFont(fontmed(12));
        btnCanCadVen.setForeground(new java.awt.Color(10, 60, 133));
        btnCanCadVen.setText("Cancelar");
        btnCanCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanCadVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanCadVenActionPerformed(evt);
            }
        });
        pnlCadVen.add(btnCanCadVen);
        btnCanCadVen.setBounds(800, 250, 90, 50);

        lblPlaCadVen.setFont(fontmed(12));
        lblPlaCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblPlaCadVen.setText("Plano");
        lblPlaCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblPlaCadVen);
        lblPlaCadVen.setBounds(700, 80, 50, 20);

        txtPlaCadVen.setBackground(new java.awt.Color(246, 246, 246));
        txtPlaCadVen.setFont(fontmed(13));
        txtPlaCadVen.setBorder(null);
        txtPlaCadVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPlaCadVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPlaCadVenFocusLost(evt);
            }
        });
        txtPlaCadVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlaCadVenKeyReleased(evt);
            }
        });
        pnlCadVen.add(txtPlaCadVen);
        txtPlaCadVen.setBounds(700, 80, 190, 20);

        sepPlaCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepPlaCadVen);
        sepPlaCadVen.setBounds(700, 100, 190, 10);

        lblCliCadVen.setFont(fontmed(12));
        lblCliCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblCliCadVen.setText("Cliente");
        lblCliCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblCliCadVen);
        lblCliCadVen.setBounds(400, 80, 60, 20);

        txtCliCadVen.setBackground(new java.awt.Color(246, 246, 246));
        txtCliCadVen.setFont(fontmed(13));
        txtCliCadVen.setBorder(null);
        txtCliCadVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCliCadVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCliCadVenFocusLost(evt);
            }
        });
        txtCliCadVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliCadVenKeyReleased(evt);
            }
        });
        pnlCadVen.add(txtCliCadVen);
        txtCliCadVen.setBounds(400, 80, 190, 20);

        sepCliCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepCliCadVen);
        sepCliCadVen.setBounds(400, 100, 190, 10);

        lblTelCadVen.setFont(fontmed(12));
        lblTelCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblTelCadVen.setText("Telefone");
        lblTelCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblTelCadVen);
        lblTelCadVen.setBounds(400, 180, 60, 20);

        txtTelCadVen.setBackground(new java.awt.Color(246, 246, 246));
        txtTelCadVen.setFont(fontmed(13));
        txtTelCadVen.setBorder(null);
        txtTelCadVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelCadVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelCadVenFocusLost(evt);
            }
        });
        txtTelCadVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelCadVenKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelCadVenKeyTyped(evt);
            }
        });
        pnlCadVen.add(txtTelCadVen);
        txtTelCadVen.setBounds(400, 180, 190, 20);

        sepTelCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepTelCadVen);
        sepTelCadVen.setBounds(400, 200, 190, 10);

        lblDatCadVen.setFont(fontmed(12));
        lblDatCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblDatCadVen.setText("Data");
        lblDatCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblDatCadVen);
        lblDatCadVen.setBounds(700, 130, 50, 20);

        txtDatCadVen.setBackground(new java.awt.Color(246, 246, 246));
        txtDatCadVen.setFont(fontmed(13));
        txtDatCadVen.setBorder(null);
        txtDatCadVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDatCadVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDatCadVenFocusLost(evt);
            }
        });
        txtDatCadVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDatCadVenKeyTyped(evt);
            }
        });
        pnlCadVen.add(txtDatCadVen);
        txtDatCadVen.setBounds(700, 130, 190, 20);

        sepDatCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepDatCadVen);
        sepDatCadVen.setBounds(700, 150, 190, 10);

        lblCli.setText("jLabel1");
        pnlCadVen.add(lblCli);
        lblCli.setBounds(1150, 30, 37, 16);
        lblCli.setVisible(false);

        lblVen.setText("jLabel3");
        pnlCadVen.add(lblVen);
        lblVen.setBounds(1150, 90, 37, 16);
        lblVen.setVisible(false);

        lblDat.setText("jLabel5");
        pnlCadVen.add(lblDat);
        lblDat.setBounds(1150, 60, 37, 16);
        lblDat.setVisible(false);

        lblCpfCadVen.setFont(fontmed(12));
        lblCpfCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblCpfCadVen.setText("CPF");
        lblCpfCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblCpfCadVen);
        lblCpfCadVen.setBounds(400, 130, 70, 20);

        sepCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepCadVen);
        sepCadVen.setBounds(400, 150, 190, 10);

        txtCpfCadVen.setBackground(new java.awt.Color(246, 246, 246));
        txtCpfCadVen.setFont(fontmed(13));
        txtCpfCadVen.setBorder(null);
        txtCpfCadVen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCpfCadVenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCpfCadVenFocusLost(evt);
            }
        });
        txtCpfCadVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCpfCadVenKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCpfCadVenKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCpfCadVenKeyTyped(evt);
            }
        });
        pnlCadVen.add(txtCpfCadVen);
        txtCpfCadVen.setBounds(400, 130, 190, 20);

        pnlPri.add(pnlCadVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

        pnlJur.setBackground(new java.awt.Color(246, 246, 246));
        pnlJur.setLayout(null);

        btnVolJur.setFont(fontmed(12));
        btnVolJur.setForeground(new java.awt.Color(10, 60, 133));
        btnVolJur.setText("Voltar");
        btnVolJur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolJur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolJurActionPerformed(evt);
            }
        });
        pnlJur.add(btnVolJur);
        btnVolJur.setBounds(330, 180, 100, 40);

        btnCalJur.setFont(fontmed(12));
        btnCalJur.setForeground(new java.awt.Color(10, 60, 133));
        btnCalJur.setText("Calcular");
        btnCalJur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCalJur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalJurActionPerformed(evt);
            }
        });
        pnlJur.add(btnCalJur);
        btnCalJur.setBounds(210, 180, 100, 40);

        lblValMesPreJur.setFont(fontmed(16));
        lblValMesPreJur.setForeground(new java.awt.Color(10, 60, 133));
        lblValMesPreJur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValMesPreJur.setText("R$ 0,00");
        lblValMesPreJur.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValMesPreJur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValMesPreJurMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValMesPreJurMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValMesPreJurMouseReleased(evt);
            }
        });
        pnlJur.add(lblValMesPreJur);
        lblValMesPreJur.setBounds(800, 260, 340, 20);

        lblValParJur.setFont(fontmed(16));
        lblValParJur.setForeground(new java.awt.Color(10, 60, 133));
        lblValParJur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValParJur.setText("R$ 0,00");
        lblValParJur.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValParJur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValParJurMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValParJurMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValParJurMouseReleased(evt);
            }
        });
        pnlJur.add(lblValParJur);
        lblValParJur.setBounds(980, 170, 140, 20);

        lblValParJur1.setFont(fontbold(16));
        lblValParJur1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValParJur1.setText("Parcelas");
        lblValParJur1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValParJur1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValParJur1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValParJur1MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValParJur1MouseReleased(evt);
            }
        });
        pnlJur.add(lblValParJur1);
        lblValParJur1.setBounds(980, 140, 140, 20);

        lblValJur.setFont(fontmed(12));
        lblValJur.setForeground(new java.awt.Color(10, 60, 133));
        lblValJur.setText("Preço");
        lblValJur.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlJur.add(lblValJur);
        lblValJur.setBounds(200, 100, 70, 20);

        lblR$Jur.setFont(fontmed(13));
        lblR$Jur.setText("R$");
        lblR$Jur.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlJur.add(lblR$Jur);
        lblR$Jur.setBounds(200, 100, 20, 21);

        txtValJur.setBackground(new java.awt.Color(246, 246, 246));
        txtValJur.setFont(fontmed(13));
        txtValJur.setBorder(null);
        txtValJur.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValJurFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValJurFocusLost(evt);
            }
        });
        txtValJur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtValJurKeyTyped(evt);
            }
        });
        pnlJur.add(txtValJur);
        txtValJur.setBounds(220, 100, 60, 20);

        sepDesGerTipSer10.setForeground(new java.awt.Color(10, 60, 133));
        pnlJur.add(sepDesGerTipSer10);
        sepDesGerTipSer10.setBounds(200, 120, 80, 10);

        sepDesGerTipSer11.setForeground(new java.awt.Color(10, 60, 133));
        pnlJur.add(sepDesGerTipSer11);
        sepDesGerTipSer11.setBounds(200, 120, 80, 10);

        sepDesGerTipSer16.setBackground(new java.awt.Color(10, 60, 133));
        sepDesGerTipSer16.setForeground(new java.awt.Color(10, 60, 133));
        sepDesGerTipSer16.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlJur.add(sepDesGerTipSer16);
        sepDesGerTipSer16.setBounds(640, 40, 30, 235);

        lblValJur2.setFont(fontmed(16));
        lblValJur2.setForeground(new java.awt.Color(10, 60, 133));
        lblValJur2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValJur2.setText("R$ 0,00");
        lblValJur2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValJur2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValJur2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValJur2MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValJur2MouseReleased(evt);
            }
        });
        pnlJur.add(lblValJur2);
        lblValJur2.setBounds(820, 80, 130, 20);

        lblValJur3.setFont(fontbold(16));
        lblValJur3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValJur3.setText("Preço");
        lblValJur3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValJur3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValJur3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValJur3MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValJur3MouseReleased(evt);
            }
        });
        pnlJur.add(lblValJur3);
        lblValJur3.setBounds(820, 50, 130, 20);

        lblParJur.setFont(fontmed(12));
        lblParJur.setForeground(new java.awt.Color(10, 60, 133));
        lblParJur.setText("parcela(s)");
        lblParJur.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlJur.add(lblParJur);
        lblParJur.setBounds(384, 95, 70, 30);

        lblValFinJur.setFont(fontmed(16));
        lblValFinJur.setForeground(new java.awt.Color(10, 60, 133));
        lblValFinJur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValFinJur.setText("R$ 0,00");
        lblValFinJur.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValFinJur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValFinJurMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValFinJurMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValFinJurMouseReleased(evt);
            }
        });
        pnlJur.add(lblValFinJur);
        lblValFinJur.setBounds(970, 80, 150, 20);

        lblValJurJur.setFont(fontmed(16));
        lblValJurJur.setForeground(new java.awt.Color(10, 60, 133));
        lblValJurJur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValJurJur.setText("R$ 0,00");
        lblValJurJur.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValJurJur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValJurJurMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValJurJurMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValJurJurMouseReleased(evt);
            }
        });
        pnlJur.add(lblValJurJur);
        lblValJurJur.setBounds(810, 170, 150, 20);

        lblValFinJur1.setFont(fontbold(16));
        lblValFinJur1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValFinJur1.setText("Preço Final");
        lblValFinJur1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblValFinJur1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblValFinJur1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblValFinJur1MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblValFinJur1MouseReleased(evt);
            }
        });
        pnlJur.add(lblValFinJur1);
        lblValFinJur1.setBounds(970, 50, 150, 20);

        lblJurJur1.setFont(fontbold(16));
        lblJurJur1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJurJur1.setText("Para mesmo preço à vista");
        lblJurJur1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblJurJur1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblJurJur1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblJurJur1MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblJurJur1MouseReleased(evt);
            }
        });
        pnlJur.add(lblJurJur1);
        lblJurJur1.setBounds(840, 230, 260, 20);

        lblJurJur.setFont(fontbold(16));
        lblJurJur.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJurJur.setText("Juros");
        lblJurJur.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblJurJur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblJurJurMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblJurJurMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblJurJurMouseReleased(evt);
            }
        });
        pnlJur.add(lblJurJur);
        lblJurJur.setBounds(820, 140, 130, 20);

        spnParJur.setModel(new javax.swing.SpinnerNumberModel(1, 0, 12, 1));
        spnParJur.setFont(fontmed(13));
        spnParJur.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        spnParJur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        spnParJur.setEditor(new javax.swing.JSpinner.NumberEditor(spnParJur, ""));
        spnParJur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spnParJurKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                spnParJurKeyTyped(evt);
            }
        });
        pnlJur.add(spnParJur);
        spnParJur.setBounds(320, 93, 55, 30);

        pnlPri.add(pnlJur, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1300, 380));

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
            es.setPreco(Double.valueOf(txtPreCadEst.getText().replace(".", "").replace(",", ".")));
            es.setQuantidade(Integer.parseInt(txtQuaCadEst.getText()));

            if (txtTipCadEst.getText().equals("Chip")) {
                es.setTipochip((String) cmbChiCadEst.getSelectedItem());
            } else {
                es.setTipochip(null);
            }

            if (!esdao.verifica(es)) {

                esdao.inserir(es);

                JOptionPane.showMessageDialog(null, "Novo ítem inserido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } else {

                esdao.acrescentar(es);

                JOptionPane.showMessageDialog(null, "Inserido com sucesso! O ítem já existia no estoque e foi adicionado a nova quantidade!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            }

            pnlCadEst.setVisible(false);
            lblTitPri.setVisible(false);
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException n) {

            JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);

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
            btnJurPri.setVisible(false);
            btnCadEnt.setVisible(false);
            btnGerEnt.setVisible(false);
            btnConEnt.setVisible(false);

            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);
            btnCadTipSer.setVisible(false);
            btnGerTipSer.setVisible(false);
            btnMasPla.setVisible(false);
            btnDes.setVisible(false);
            btnCadDes.setVisible(false);
            btnGerDes.setVisible(false);
            btnVen.setVisible(false);
            btnCadVen.setVisible(false);
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

            int resp = JOptionPane.showOptionDialog(null, "Cancelar inserção? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                pnlCadEst.setVisible(false);
                lblTitPri.setVisible(false);
                lblProCadEst.setVisible(false);
                tblCadEst.setVisible(false);
                scrCadEst.setVisible(false);

            }

        } else {

            pnlCadEst.setVisible(false);
            lblTitPri.setVisible(false);
            lblProCadEst.setVisible(false);
            tblCadEst.setVisible(false);
            scrCadEst.setVisible(false);

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
                ts.setArea("2");
            } else {
                ts.setArea("3");
            }

            tsdao.inserir(ts);

            JOptionPane.showMessageDialog(null, "Inserido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            pnlCadTipSer.setVisible(false);
            lblTitPri.setVisible(false);

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_btnSalTipSerActionPerformed

    private void btnCanTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanTipSerActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Cancelar inserção? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

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
            lblDesTipSer.setLocation(505, 110);

            txtDesTipSer.setEnabled(false);
            lblDesTipSer.setEnabled(false);
            sepDesTipSer.setForeground(Color.GRAY);

            lblTitPri.setVisible(true);
            lblTitPri.setText("Cadastrar Tipo de Serviço");

            pnlbtn();
            pnlCadTipSer.setVisible(true);

        } else {

            pnlbtn();
            pnlCadTipSer.setVisible(true);

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
                btnAtvGerTipSer.setEnabled(false);
                btnAtvGerTipSer.setText("Desativar");
                rbtnOutGerTipSer.setEnabled(false);
                rbtnAssGerTipSer.setEnabled(false);
                rbtnTimGerTipSer.setEnabled(false);
                btnGroup.clearSelection();
                lblDesGerTipSer.setLocation(510, 240);

                lblTitPri.setVisible(true);
                lblTitPri.setText("Gerenciar Tipo de Serviço");

                txtDesGerTipSer.setEnabled(false);
                lblDesGerTipSer.setEnabled(false);
                sepDesGerTipSer.setForeground(Color.GRAY);

                pnlbtn();
                pnlGerTipSer.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(null, "Sem tipo de serviço para gerenciar. Cadastre-o primeiro!", "Gerenciar Tipo de Serviço", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {

            pnlbtn();
            pnlGerTipSer.setVisible(true);

        }
    }//GEN-LAST:event_btnGerTipSerMouseReleased

    private void btnTipSerPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTipSerPriMouseReleased
        if (!btnGerTipSer.isVisible()) {
            btnGerTipSer.setVisible(true);
            btnCadTipSer.setVisible(true);

            btnCadEnt.setVisible(false);
            btnGerEnt.setVisible(false);
            btnConEnt.setVisible(false);
            btnCadEst.setVisible(false);
            btnConEst.setVisible(false);
            btnGerEst.setVisible(false);
            btnJurPri.setVisible(false);
            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);

            btnVen.setVisible(false);
            btnCadVen.setVisible(false);

            btnMasPla.setVisible(false);
            btnDes.setVisible(false);
            btnCadDes.setVisible(false);
            btnGerDes.setVisible(false);
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

            lblProCadEst.setVisible(false);
            tblCadEst.setVisible(false);
            scrCadEst.setVisible(false);

            btnGroup.clearSelection();

            lblTitPri.setVisible(true);
            lblTitPri.setText("Cadastrar Estoque");

            btnSalCadEst.setEnabled(false);

            pnlbtn();
            pnlCadEst.setVisible(true);

        } else {

            pnlbtn();
            pnlCadEst.setVisible(true);

        }
    }//GEN-LAST:event_btnCadEstMouseReleased

    private void btnConEstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseEntered
        btnConEst.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnConEstMouseEntered

    private void btnConEstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseExited
        btnConEst.setForeground(corforeazul);
    }//GEN-LAST:event_btnConEstMouseExited

    private void btnConEstMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseReleased
        if (!pnlConEst.isVisible()) {

            txtBusConEst.setText(null);
            lblBusConEst.setLocation(450, 110);
            btnGroup.clearSelection();

            scrConEst.setVisible(false);
            btnBusConEst.setEnabled(false);

            txtBusConEst.setEnabled(false);
            lblBusConEst.setEnabled(false);
            sepBusConEst.setForeground(Color.GRAY);

            lblTitPri.setVisible(true);
            lblTitPri.setText("Consultar Estoque");

            pnlbtn();
            pnlConEst.setVisible(true);

        } else {

            pnlbtn();
            pnlConEst.setVisible(true);

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
            cmbChiGerEst.setSelectedIndex(0);

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

            lblTitPri.setVisible(true);
            lblTitPri.setText("Gerenciar Estoque");

            pnlbtn();
            pnlGerEst.setVisible(true);

        } else {

            pnlbtn();
            pnlGerEst.setVisible(true);

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
            btnVen.setVisible(false);
            btnCadVen.setVisible(false);
            btnJurPri.setVisible(false);

        } else {
            btnMasPla.setVisible(true);
            btnDes.setVisible(true);
            btnCadDes.setVisible(true);
            btnGerDes.setVisible(true);
            btnVen.setVisible(true);
            btnCadVen.setVisible(true);
            btnJurPri.setVisible(true);
            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);

            btnCadEnt.setVisible(false);
            btnGerEnt.setVisible(false);
            btnConEnt.setVisible(false);
            btnCadEst.setVisible(false);
            btnConEst.setVisible(false);
            btnGerEst.setVisible(false);
            btnCadTipSer.setVisible(false);
            btnGerTipSer.setVisible(false);

        }
    }//GEN-LAST:event_btnOutPriMouseReleased

    private void btnMasPlaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseEntered
        btnMasPla.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnMasPlaMouseEntered

    private void btnMasPlaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseExited
        btnMasPla.setForeground(corforeazul);
    }//GEN-LAST:event_btnMasPlaMouseExited

    private void btnMasPlaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseReleased
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
            chkMelMas.setSelected(false);
            chkAppMas.setSelected(false);
            chkBolMas.setSelected(true);
            chkDebMas.setSelected(false);
            chkCarMas.setSelected(false);
            rbtnAtiMas.setSelected(true);

            txtAreMas.setText(null);

            lblNomMas.setLocation(90, 60);
            lblCpfMas.setLocation(90, 110);
            lblVenMas.setLocation(90, 160);
            lblNumConMas.setLocation(350, 60);
            lblNumAceMas.setLocation(350, 110);
            lblNumPorMas.setLocation(350, 160);
            lblPlaMas.setLocation(350, 210);

            btnCopMas.setVisible(false);

            pnlbtn();
            pnlMas.setVisible(true);

        } else {

            pnlbtn();
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
        if (!pnlCadDes.isVisible()) {

            txtDesDes.setText(null);
            txtPreDes.setText(null);
            txtDatDes.setText(null);
            btnSalDes.setEnabled(false);

            lblTitPri.setText("Cadastrar Afazeres");
            lblTitPri.setVisible(true);

            lblDesDes.setLocation(540, 60);
            lblPreDes.setLocation(540, 120);
            lblDatDes.setLocation(540, 180);

            lblR$Des.setVisible(false);

            pnlbtn();
            pnlCadDes.setVisible(true);

        } else {

            pnlbtn();
            pnlCadDes.setVisible(true);

        }
    }//GEN-LAST:event_btnCadDesMouseReleased

    private void btnDesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseEntered
        btnDes.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnDesMouseEntered

    private void btnDesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseExited
        btnDes.setForeground(corforeazul);
    }//GEN-LAST:event_btnDesMouseExited

    private void btnDesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseReleased
        if (!pnlDes.isVisible()) {

            if (tabeladespezas(tblConDes, scrConDes)) {

                lblTitPri.setText("Afazeres");
                lblTitPri.setVisible(true);

                pnlbtn();
                pnlDes.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(null, "Sem afazeres. Cadastre-as primeiro!", "Gerenciar Afazeres", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {

            pnlbtn();
            pnlDes.setVisible(true);

        }
    }//GEN-LAST:event_btnDesMouseReleased

    private void btnExcGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerTipSerActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {
            try {
                tiposervico ts = new tiposervico();
                tiposervicoDAO tsdao = new tiposervicoDAO();

                ts.setIdtiposervico(Integer.parseInt(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 0).toString()));

                tsdao.excluir(ts);

                JOptionPane.showMessageDialog(null, "Excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                pnlGerTipSer.setVisible(false);
                lblTitPri.setVisible(false);
            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(null, "Erro ao excluir! Erro: " + ex.getMessage(), "Erro", JOptionPane.OK_OPTION);

            }
        }
    }//GEN-LAST:event_btnExcGerTipSerActionPerformed

    private void btnAltGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerTipSerActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                tiposervico ts = new tiposervico();
                tiposervicoDAO tsdao = new tiposervicoDAO();

                ts.setDescricao(txtDesGerTipSer.getText());
                ts.setIdtiposervico(Integer.parseInt(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 0).toString()));
                System.out.print(Integer.parseInt(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 0).toString()));
                if (rbtnTimGerTipSer.isSelected()) {
                    ts.setArea("1");
                } else if (rbtnAssGerTipSer.isSelected()) {
                    ts.setArea("2");
                } else {
                    ts.setArea("3");
                }

                tsdao.alterar(ts);

                JOptionPane.showMessageDialog(null, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                pnlGerTipSer.setVisible(false);
                lblTitPri.setVisible(false);

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(null, "Erro ao alterar! Erro: " + ex.getMessage(), "Erro", JOptionPane.OK_OPTION);

            }
        }
    }//GEN-LAST:event_btnAltGerTipSerActionPerformed

    private void txtDesGerTipSerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesGerTipSerFocusGained

    }//GEN-LAST:event_txtDesGerTipSerFocusGained

    private void txtDesGerTipSerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesGerTipSerFocusLost

    }//GEN-LAST:event_txtDesGerTipSerFocusLost

    private void btnCanGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerTipSerActionPerformed
        pnlGerTipSer.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnCanGerTipSerActionPerformed

    private void tblTipSerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTipSerMouseClicked
        if ("1".equals(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 3).toString())) {

            lblDesGerTipSer.setLocation(510, 240);

            lblDesGerTipSer.setEnabled(true);
            txtDesGerTipSer.setEnabled(true);
            sepDesGerTipSer.setForeground(corforeazul);

            txtDesGerTipSer.setText(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 2).toString());

            btnExcGerTipSer.setEnabled(true);
            btnAltGerTipSer.setEnabled(true);
            btnAtvGerTipSer.setEnabled(true);

            rbtnTimGerTipSer.setEnabled(true);
            rbtnAssGerTipSer.setEnabled(true);
            rbtnOutGerTipSer.setEnabled(true);
            btnGroup.clearSelection();

            if (tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 1).toString().equals("1")) {
                rbtnTimGerTipSer.setSelected(true);
            } else if (tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 1).toString().equals("2")) {
                rbtnAssGerTipSer.setSelected(true);
            } else {
                rbtnOutGerTipSer.setSelected(true);
            }

            btnAtvGerTipSer.setText("Desativar");

            if (!txtDesGerTipSer.getText().isEmpty()) {
                anitxtin(lblDesGerTipSer);
            }

        } else {

            lblDesGerTipSer.setLocation(510, 240);

            lblDesGerTipSer.setEnabled(false);
            txtDesGerTipSer.setEnabled(false);
            sepDesGerTipSer.setForeground(Color.GRAY);

            txtDesGerTipSer.setText(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 2).toString());

            btnExcGerTipSer.setEnabled(false);
            btnAltGerTipSer.setEnabled(false);
            btnAtvGerTipSer.setEnabled(true);
            btnAtvGerTipSer.setText("Ativar");

            rbtnTimGerTipSer.setEnabled(false);
            rbtnAssGerTipSer.setEnabled(false);
            rbtnOutGerTipSer.setEnabled(false);
            btnGroup.clearSelection();

            if (tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 1).toString().equals("1")) {
                rbtnTimGerTipSer.setSelected(true);
            } else if (tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 1).toString().equals("2")) {
                rbtnAssGerTipSer.setSelected(true);
            } else {
                rbtnOutGerTipSer.setSelected(true);
            }

            if (!txtDesGerTipSer.getText().isEmpty()) {
                anitxtin(lblDesGerTipSer);
            }

        }
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

            JOptionPane.showMessageDialog(null, "Ítem não cadastrado no sistema!", "Consultar", JOptionPane.INFORMATION_MESSAGE);

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
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            if (tblGerEst.getSelectedRow() != -1) {

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

                    JOptionPane.showMessageDialog(null, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    pnlGerEst.setVisible(false);
                    lblTitPri.setVisible(false);
                } catch (SQLException ex) {

                    JOptionPane.showMessageDialog(null, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                } catch (NumberFormatException n) {

                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);

                }

            } else {

                JOptionPane.showMessageDialog(null, "Selecione uma linha na tabela para alterar!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnAltGerEstActionPerformed

    private void btnCanGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerEstActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Cancelar alterações?", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

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

            JOptionPane.showMessageDialog(null, "Nenhum dado encontrado!", "Gerenciar", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_btnBusGerEstActionPerformed

    private void btnExcGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerEstActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                estoque es = new estoque();
                estoqueDAO esdao = new estoqueDAO();

                es.setId(Integer.parseInt(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 0).toString()));

                esdao.excluir(es);

                JOptionPane.showMessageDialog(null, "Excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

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

            } else if (tblGerEst.getColumnCount() == 8) {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtMatGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString());

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
        if ((rbtnDinCadEnt.isSelected() || rbtnPixCadEnt.isSelected()) || (rbtnCarCadEnt.isSelected() && (rbtnCreCadEnt.isSelected() || rbtnDebCadEnt.isSelected()))) {

            if (!(rbtnVenCadEnt.isSelected() && tblSelIteCadEnt.getRowCount() == 0)) {

                if (!(cmbVezCar.getSelectedIndex() == 0 && (rbtnAssCadEnt.isSelected() || rbtnSerCadEnt.isSelected()))) {

                    try {

                        int idser = 0;
                        int idpagamento = 1;
                        double preco = Double.parseDouble(txtPreCadEnt.getText().replace(".", "").replace(",", "."));

                        if (rbtnCarCadEnt.isSelected()) {

                            if (rbtnDebCadEnt.isSelected()) {

                                preco = preco - (preco * juros(0) / 100);

                            } else if (rbtnCreCadEnt.isSelected()) {

                                switch ((int) spnParCadEnt.getValue()) {
                                    case 1:
                                        preco = preco - (preco * juros(1) / 100);
                                        break;
                                    case 2:
                                        preco = preco - (preco * juros(2) / 100);
                                        break;
                                    case 3:
                                        preco = preco - (preco * juros(3) / 100);
                                        break;
                                    case 4:
                                        preco = preco - (preco * juros(4) / 100);
                                        break;
                                    case 5:
                                        preco = preco - (preco * juros(5) / 100);
                                        break;
                                    case 6:
                                        preco = preco - (preco * juros(6) / 100);
                                        break;
                                    case 7:
                                        preco = preco - (preco * juros(7) / 100);
                                        break;
                                    case 8:
                                        preco = preco - (preco * juros(8) / 100);
                                        break;
                                    case 9:
                                        preco = preco - (preco * juros(9) / 100);
                                        break;
                                    case 10:
                                        preco = preco - (preco * juros(10) / 100);
                                        break;
                                    case 11:
                                        preco = preco - (preco * juros(11) / 100);
                                        break;
                                    case 12:
                                        preco = preco - (preco * juros(12) / 100);
                                        break;
                                    default:
                                        break;
                                }

                            }

                        }

                        if (rbtnCarCadEnt.isSelected()) {
                            idpagamento = 2;
                        } else if (rbtnPixCadEnt.isSelected()) {
                            idpagamento = 3;
                        }

                        if (cmbVezCar.getSelectedIndex() != 0) {
                            itens selectedItem = (itens) cmbVezCar.getSelectedItem();
                            idser = selectedItem.getId();
                        }

                        if (tblSelIteCadEnt.getRowCount() != 0) {

                            for (int i = 1; i <= tblSelIteCadEnt.getRowCount(); i++) {

                                entrada en = new entrada();
                                entradaDAO endao = new entradaDAO();

                                en.setCodigo(txtCodCadEnt.getText());
                                en.setData(formatterbanco.format(((formatter.parse(txtDatCadEnt.getText())))));
                                en.setPreco(preco);
                                en.setDetalhes(txtDetCadEnt.getText());
                                en.setFormapagamento(idpagamento);

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
                            en.setPreco(preco);
                            en.setDetalhes(txtDetCadEnt.getText());
                            en.setIdtiposervico(idser);
                            en.setIdestoque(1);
                            en.setFormapagamento(idpagamento);

                            if (rbtnSerCadEnt.isSelected()) {

                                endao.inserir(en, 1);

                            } else if (rbtnAssCadEnt.isSelected()) {

                                en.setCliente(txtCliCadEnt.getText());
                                en.setCusto((!txtCusCadEnt.getText().isEmpty()) ? Double.valueOf(txtCusCadEnt.getText().replace(".", "").replace(",", ".")) : null);
                                en.setFornecedor(txtForCadEnt.getText());

                                endao.inserir(en, 3);

                            }

                        }

                        //INSERIR CONTADOR DE TROCAS 
                        itens selectedItem = (itens) cmbVezCar.getSelectedItem();

                        String textoSelecionado = selectedItem.getDescricao();

                        if (textoSelecionado.equals("Troca de Chip") || textoSelecionado.equals("Ativação eSIM")) {

                            planosdiaDAO pddao = new planosdiaDAO();
                            pddao.zerar();
                            pddao.adicionar(3);

                        }

                        JOptionPane.showMessageDialog(null, "Entrada feita com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        pnlCadEnt.setVisible(false);

                    } catch (SQLException | ParseException ex) {
                        Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Selecione o serviço!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                }

                lblTitPri.setVisible(false);

            } else {
                JOptionPane.showMessageDialog(null, "Selecione os ítem do estoque!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Selecione o método de pagamento!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSalCadEntActionPerformed

    private void btnCanCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanCadEntActionPerformed
        if (btnSalCadEnt.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(null, "Cancelar entrada? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

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

        txtCliCadEnt.setEnabled(false);
        txtCliCadEnt.setText(null);
        sepCliCadEnt.setForeground(Color.GRAY);
        lblCliCadEnt.setEnabled(false);
        lblR$CusCadEnt.setVisible(false);
        lblCliCadEnt.setLocation(630, 130);
        lblCusCadEnt.setLocation(630, 180);
        lblForCadEnt.setLocation(630, 230);

        txtCusCadEnt.setEnabled(false);
        txtCusCadEnt.setText(null);
        sepCusCadEnt.setForeground(Color.GRAY);
        lblCusCadEnt.setEnabled(false);

        txtForCadEnt.setEnabled(false);
        txtForCadEnt.setText(null);
        sepForCadEnt.setForeground(Color.GRAY);
        lblForCadEnt.setEnabled(false);
        cmbVezCar.setEnabled(true);
        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(true);
        rbtnDinCadEnt.setEnabled(true);
        rbtnCarCadEnt.setEnabled(true);
        rbtnPixCadEnt.setEnabled(true);

        comboboxentrada(cmbVezCar, 1);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(390, 110);
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
        txtCliCadEnt.setEnabled(false);
        txtCliCadEnt.setText(null);
        sepCliCadEnt.setForeground(Color.GRAY);
        lblCliCadEnt.setEnabled(false);
        lblR$CusCadEnt.setVisible(false);
        lblCliCadEnt.setLocation(630, 130);
        lblCusCadEnt.setLocation(630, 180);
        lblForCadEnt.setLocation(630, 230);

        txtCusCadEnt.setEnabled(false);
        txtCusCadEnt.setText(null);
        sepCusCadEnt.setForeground(Color.GRAY);
        lblCusCadEnt.setEnabled(false);

        txtForCadEnt.setEnabled(false);
        txtForCadEnt.setText(null);
        sepForCadEnt.setForeground(Color.GRAY);
        lblForCadEnt.setEnabled(false);
        cmbVezCar.setEnabled(false);
        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(true);
        rbtnDinCadEnt.setEnabled(true);
        rbtnCarCadEnt.setEnabled(true);
        rbtnPixCadEnt.setEnabled(true);

        comboboxentrada(cmbVezCar, 1);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(390, 110);
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
        txtCliCadEnt.setEnabled(true);
        sepCliCadEnt.setForeground(corforeazul);
        lblCliCadEnt.setEnabled(true);

        txtCusCadEnt.setEnabled(true);
        sepCusCadEnt.setForeground(corforeazul);
        lblCusCadEnt.setEnabled(true);

        txtForCadEnt.setEnabled(true);
        sepForCadEnt.setForeground(corforeazul);
        lblForCadEnt.setEnabled(true);
        cmbVezCar.setEnabled(true);
        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(false);
        rbtnDinCadEnt.setEnabled(true);
        rbtnCarCadEnt.setEnabled(true);
        rbtnPixCadEnt.setEnabled(true);

        comboboxentrada(cmbVezCar, 2);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));
        lblDatCadEnt.setLocation(390, 110);
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

            JOptionPane.showMessageDialog(null, "Ítem já adicionado!", "Entrada", JOptionPane.ERROR_MESSAGE);

        } else {

            String opc = JOptionPane.showInputDialog(null, "Informe a quantidade deste ítem", "Entrada", JOptionPane.QUESTION_MESSAGE);

            if (opc != null) {

                try {

                    int i = Integer.parseInt(opc);

                    if (rbtnChiIteCadEnt.isSelected()) {

                        if (Integer.parseInt(tblEstIteCadEnt.getValueAt(tblEstIteCadEnt.getSelectedRow(), 3).toString()) < i) {

                            JOptionPane.showMessageDialog(null, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteCadEnt, tblSelIteCadEnt, opc, rbtnChiIteCadEnt);

                            lblSelIteCadEnt.setForeground(corforeazul);
                            scrSelIteCadEnt.setVisible(true);
                            tblSelIteCadEnt.setVisible(true);

                        }

                    } else {

                        if (Integer.parseInt(tblEstIteCadEnt.getValueAt(tblEstIteCadEnt.getSelectedRow(), 5).toString()) < i) {

                            JOptionPane.showMessageDialog(null, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteCadEnt, tblSelIteCadEnt, opc, rbtnChiIteCadEnt);

                            lblSelIteCadEnt.setForeground(corforeazul);
                            scrSelIteCadEnt.setVisible(true);
                            tblSelIteCadEnt.setVisible(true);

                        }

                    }

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(null, "Digite apenas número!", "Erro", JOptionPane.ERROR_MESSAGE);

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

            lblDatCadEnt.setLocation(390, 130);
            lblPreCadEnt.setLocation(390, 180);
            lblDetCadEnt.setLocation(390, 230);

            lblCliCadEnt.setLocation(630, 130);
            lblCusCadEnt.setLocation(630, 180);
            lblForCadEnt.setLocation(630, 230);

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

            txtCliCadEnt.setEnabled(false);
            txtCliCadEnt.setText(null);
            sepCliCadEnt.setForeground(Color.GRAY);
            lblCliCadEnt.setEnabled(false);

            txtCusCadEnt.setEnabled(false);
            txtCusCadEnt.setText(null);
            sepCusCadEnt.setForeground(Color.GRAY);
            lblCusCadEnt.setEnabled(false);

            txtForCadEnt.setEnabled(false);
            txtForCadEnt.setText(null);
            sepForCadEnt.setForeground(Color.GRAY);
            lblForCadEnt.setEnabled(false);

            sepDetCadEnt.setForeground(Color.GRAY);
            lblSerCadEnt.setEnabled(false);
            cmbVezCar.setEnabled(false);
            cmbVezCar.setSelectedIndex(0);
            lblR$CadEnt.setVisible(false);
            lblR$CusCadEnt.setVisible(false);

            btnGroup.clearSelection();
            btnGroup1.clearSelection();
            btnGroup2.clearSelection();
            btnGroup3.clearSelection();

            spnParCadEnt.setVisible(false);
            lblParCadEnt.setVisible(false);

            JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spnParCadEnt.getEditor();
            editor.getTextField().setEditable(false);

            spnParCadEnt.setValue(0);
            spnParCadEnt.setEnabled(false);
            lblParCadEnt.setEnabled(false);
            rbtnCreCadEnt.setEnabled(false);
            rbtnDebCadEnt.setEnabled(false);
            rbtnCreCadEnt.setVisible(false);
            rbtnDebCadEnt.setVisible(false);

            rbtnDinCadEnt.setEnabled(false);
            rbtnCarCadEnt.setEnabled(false);
            rbtnPixCadEnt.setEnabled(false);

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
            cmbVezCar.setEnabled(false);

            pnlbtn();
            pnlCadEnt.setVisible(true);

        } else {

            pnlbtn();
            pnlCadEnt.setVisible(true);

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
            btnConEnt.setVisible(true);
            btnCadEst.setVisible(false);
            btnConEst.setVisible(false);
            btnGerEst.setVisible(false);
            btnCadTipSer.setVisible(false);
            btnGerTipSer.setVisible(false);
            btnMasPla.setVisible(false);
            btnDes.setVisible(false);
            btnCadDes.setVisible(false);
            btnGerDes.setVisible(false);
            btnVen.setVisible(false);
            btnCadVen.setVisible(false);
            btnJurPri.setVisible(false);
            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);

        } else {
            btnCadEnt.setVisible(false);
            btnGerEnt.setVisible(false);
            btnConEnt.setVisible(false);
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

                JOptionPane.showMessageDialog(null, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

            }

            tblEstIteCadEnt.getTableHeader().setFont(fontbold(11));

        }
    }//GEN-LAST:event_txtBusIteCadEntKeyPressed

    private void btnVolRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolRelActionPerformed
        pnlRel.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnVolRelActionPerformed

    private void rbtnSerRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSerRelActionPerformed
        try {

            rbtnSerRel.grabFocus();

            chkCus.setSelected(false);
            chkCus.setEnabled(false);

            if (btnTodRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 2, 1, null, null, 0);
            } else if (btnDiaRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 2, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnSemRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 2, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnMesRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 2, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnAnoRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 2, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (!"".equals(lblDatIniRel.getText()) && !"".equals(lblDatFinRel.getText())) {
                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));
                tabelarelatorio(tblRel, scrRel, 2, 6, data1, data2, 0);
            }

            cmbrelatorio(tblRel, cmbRel, 1);

            if (lblResRel.isVisible()) {
                tblRel.setVisible(false);
                scrRel.setVisible(false);
                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);
                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            } else {
                tblRel.setVisible(true);
                scrRel.setVisible(true);
            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtnSerRelActionPerformed

    private void rbtnVenRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnVenRelActionPerformed
        try {

            rbtnVenRel.grabFocus();

            chkCus.setSelected(false);
            chkCus.setEnabled(false);

            if (btnTodRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 3, 1, null, null, 0);
            } else if (btnDiaRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 3, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnSemRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 3, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnMesRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 3, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnAnoRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 3, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (!"".equals(lblDatIniRel.getText()) && !"".equals(lblDatFinRel.getText())) {
                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));
                tabelarelatorio(tblRel, scrRel, 3, 6, data1, data2, 0);
            }

            cmbrelatorio(tblRel, cmbRel, 2);

            if (lblResRel.isVisible()) {
                tblRel.setVisible(false);
                scrRel.setVisible(false);
                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);
                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            } else {
                tblRel.setVisible(true);
                scrRel.setVisible(true);
            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtnVenRelActionPerformed

    private void rbtnTodRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnTodRelActionPerformed
        try {

            rbtnTodRel.grabFocus();

            chkCus.setEnabled(false);
            chkCus.setSelected(false);

            if (btnTodRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 1, 1, null, null, 0);
            } else if (btnDiaRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 1, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnSemRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 1, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnMesRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 1, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnAnoRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 1, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (!"".equals(lblDatIniRel.getText()) && !"".equals(lblDatFinRel.getText())) {
                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));
                tabelarelatorio(tblRel, scrRel, 1, 6, data1, data2, 0);
            }

            cmbRel.setSelectedIndex(0);
            cmbRel.setEnabled(false);

            if (lblResRel.isVisible()) {
                tblRel.setVisible(false);
                scrRel.setVisible(false);
                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);
                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            } else {
                tblRel.setVisible(true);
                scrRel.setVisible(true);
            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {

            rbtnAssRel.grabFocus();

            chkCus.setEnabled(true);
            chkCus.setSelected(false);

            if (btnTodRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 5, 1, null, null, 0);
            } else if (btnDiaRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 5, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnSemRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 5, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnMesRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 5, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnAnoRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 5, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (!"".equals(lblDatIniRel.getText()) && !"".equals(lblDatFinRel.getText())) {
                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));
                tabelarelatorio(tblRel, scrRel, 5, 6, data1, data2, 0);
            }

            cmbrelatorio(tblRel, cmbRel, 1);

            if (lblResRel.isVisible()) {
                tblRel.setVisible(false);
                scrRel.setVisible(false);
                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);
                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            } else {
                tblRel.setVisible(true);
                scrRel.setVisible(true);
            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtnAssRelActionPerformed

    private void btnRelPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelPriMouseReleased
        if (!pnlRel.isVisible()) {

            btnNumDiaRel.setVisible(false);
            btnMenDiaRel.setVisible(false);
            btnMaiDiaRel.setVisible(false);
            lblDiaRel.setVisible(false);

            rbtnTodRel.setSelected(true);

            lblDatIniRel.setLocation(290, 150);
            lblDatFinRel.setLocation(430, 150);

            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);

            btnTodRel.setFont(fontbold(13));
            btnDiaRel.setFont(fontmed(12));
            btnSemRel.setFont(fontmed(12));
            btnMesRel.setFont(fontmed(12));
            btnAnoRel.setFont(fontmed(12));
            lblDatIniRel.setFont(fontmed(12));
            lblDatFinRel.setFont(fontmed(12));

            chkCus.setEnabled(false);
            chkCus.setSelected(false);

            tabelarelatorio(tblRel, scrRel, 1, 1, null, null, 0);

            cmbrelatorio(tblRel, cmbRel, 1);
            cmbRel.setEnabled(false);
            pnlRel.setVisible(true);
            lblTitPri.setText("Relatórios");
            lblTitPri.setVisible(true);

            pnlbtn();
            pnlRel.setVisible(true);

            btnVolRel.grabFocus();

        } else {

            pnlbtn();
            pnlRel.setVisible(true);

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
        btnNumDiaRel.setVisible(false);
        btnMenDiaRel.setVisible(false);
        btnMaiDiaRel.setVisible(false);
        lblDiaRel.setVisible(false);

        btnTodRel.setFont(fontbold(13));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 1, null, null, 0);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 1, null, null, 0);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 1, null, null, 0);
        } else if (rbtnVenRel1.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 1, null, null, 0);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 1, null, null, 0);
        }

        if (txtDatIniRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(430, 150);
        } else if (txtDatFinRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatIniRel.setLocation(290, 150);
        }

        if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
            if (rbtnVenRel.isSelected()) {
                cmbrelatorio(tblRel, cmbRel, 2);
            } else {
                cmbrelatorio(tblRel, cmbRel, 1);
            }
        } else {
            cmbRel.setSelectedIndex(0);
            cmbRel.setEnabled(false);
        }

        if (lblResRel.isVisible()) {
            tblRel.setVisible(false);
            scrRel.setVisible(false);
            DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
            mol.setRowCount(0);
            lblValTotRel.setText("R$ 0,00");
            lblValMedRel.setText("R$ 0,00");
            lblTotEntRel.setText("0");

            lblValDinRel.setText("R$ 0,00");
            lblValCarRel.setText("R$ 0,00");
            lblValPixRel.setText("R$ 0,00");
        } else {
            tblRel.setVisible(true);
            scrRel.setVisible(true);
        }

        btnTodRel.grabFocus();
    }//GEN-LAST:event_btnTodRelMouseReleased

    private void btnDiaRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDiaRelMouseReleased
        btnDiaRel.grabFocus();

        btnNumDiaRel.setText("0");
        btnNumDiaRel.setVisible(false);
        btnMenDiaRel.setVisible(true);
        btnMenDiaRel.setEnabled(false);
        btnMaiDiaRel.setVisible(true);

        lblDiaRel.setVisible(true);
        lblDiaRel.setText("Este dia");

        btnMaiDiaRel.setLocation(85, 65);
        btnMenDiaRel.setLocation(60, 65);
        lblDiaRel.setLocation(60, 85);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontbold(13));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 2, null, null, 0);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 2, null, null, 0);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 2, null, null, 0);
        } else if (rbtnVenRel1.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 2, null, null, 0);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 2, null, null, 0);
        }

        if (txtDatIniRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(430, 150);
        } else if (txtDatFinRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatIniRel.setLocation(290, 150);
        }

        if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
            if (rbtnVenRel.isSelected()) {
                cmbrelatorio(tblRel, cmbRel, 2);
            } else {
                cmbrelatorio(tblRel, cmbRel, 1);
            }
        } else {
            cmbRel.setSelectedIndex(0);
            cmbRel.setEnabled(false);
        }

        if (lblResRel.isVisible()) {
            tblRel.setVisible(false);
            scrRel.setVisible(false);
            DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
            mol.setRowCount(0);
            lblValTotRel.setText("R$ 0,00");
            lblValMedRel.setText("R$ 0,00");
            lblTotEntRel.setText("0");

            lblValDinRel.setText("R$ 0,00");
            lblValCarRel.setText("R$ 0,00");
            lblValPixRel.setText("R$ 0,00");
        } else {
            tblRel.setVisible(true);
            scrRel.setVisible(true);
        }

        btnDiaRel.grabFocus();
    }//GEN-LAST:event_btnDiaRelMouseReleased

    private void btnSemRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSemRelMouseReleased
        btnNumDiaRel.setText("0");
        btnNumDiaRel.setVisible(false);
        btnMenDiaRel.setVisible(true);
        btnMenDiaRel.setEnabled(false);
        btnMaiDiaRel.setVisible(true);

        lblDiaRel.setVisible(true);
        lblDiaRel.setText("Esta semana");

        btnMaiDiaRel.setLocation(85, 65);
        btnMenDiaRel.setLocation(60, 65);
        lblDiaRel.setLocation(60, 85);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontbold(13));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 3, null, null, 0);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 3, null, null, 0);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 3, null, null, 0);
        } else if (rbtnVenRel1.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 3, null, null, 0);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 3, null, null, 0);
        }

        if (txtDatIniRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(430, 150);
        } else if (txtDatFinRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatIniRel.setLocation(290, 150);
        }

        if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
            if (rbtnVenRel.isSelected()) {
                cmbrelatorio(tblRel, cmbRel, 2);
            } else {
                cmbrelatorio(tblRel, cmbRel, 1);
            }
        } else {
            cmbRel.setSelectedIndex(0);
            cmbRel.setEnabled(false);
        }

        if (lblResRel.isVisible()) {
            tblRel.setVisible(false);
            scrRel.setVisible(false);
            DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
            mol.setRowCount(0);
            lblValTotRel.setText("R$ 0,00");
            lblValMedRel.setText("R$ 0,00");
            lblTotEntRel.setText("0");

            lblValDinRel.setText("R$ 0,00");
            lblValCarRel.setText("R$ 0,00");
            lblValPixRel.setText("R$ 0,00");
        } else {
            tblRel.setVisible(true);
            scrRel.setVisible(true);
        }

        btnSemRel.grabFocus();
    }//GEN-LAST:event_btnSemRelMouseReleased

    private void btnMesRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMesRelMouseReleased
        btnNumDiaRel.setText("0");
        btnNumDiaRel.setVisible(false);
        btnMenDiaRel.setVisible(true);
        btnMenDiaRel.setEnabled(false);
        btnMaiDiaRel.setVisible(true);

        lblDiaRel.setVisible(true);
        lblDiaRel.setText("Este mês");

        btnMaiDiaRel.setLocation(85, 65);
        btnMenDiaRel.setLocation(60, 65);
        lblDiaRel.setLocation(60, 85);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontbold(13));
        btnAnoRel.setFont(fontmed(12));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 4, null, null, 0);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 4, null, null, 0);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 4, null, null, 0);
        } else if (rbtnVenRel1.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 4, null, null, 0);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 4, null, null, 0);
        }

        if (txtDatIniRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(430, 150);
        } else if (txtDatFinRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatIniRel.setLocation(290, 150);
        }

        if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
            if (rbtnVenRel.isSelected()) {
                cmbrelatorio(tblRel, cmbRel, 2);
            } else {
                cmbrelatorio(tblRel, cmbRel, 1);
            }
        } else {
            cmbRel.setSelectedIndex(0);
            cmbRel.setEnabled(false);
        }

        if (lblResRel.isVisible()) {
            tblRel.setVisible(false);
            scrRel.setVisible(false);
            DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
            mol.setRowCount(0);
            lblValTotRel.setText("R$ 0,00");
            lblValMedRel.setText("R$ 0,00");
            lblTotEntRel.setText("0");

            lblValDinRel.setText("R$ 0,00");
            lblValCarRel.setText("R$ 0,00");
            lblValPixRel.setText("R$ 0,00");
        } else {
            tblRel.setVisible(true);
            scrRel.setVisible(true);
        }

        btnMesRel.grabFocus();
    }//GEN-LAST:event_btnMesRelMouseReleased

    private void btnAnoRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnoRelMouseReleased
        btnNumDiaRel.setText("0");
        btnNumDiaRel.setVisible(false);
        btnMenDiaRel.setVisible(true);
        btnMenDiaRel.setEnabled(false);
        btnMaiDiaRel.setVisible(true);

        lblDiaRel.setVisible(true);
        lblDiaRel.setText("Este ano");

        btnMaiDiaRel.setLocation(85, 65);
        btnMenDiaRel.setLocation(60, 65);
        lblDiaRel.setLocation(60, 85);

        btnTodRel.setFont(fontmed(12));
        btnDiaRel.setFont(fontmed(12));
        btnSemRel.setFont(fontmed(12));
        btnMesRel.setFont(fontmed(12));
        btnAnoRel.setFont(fontbold(13));
        lblDatIniRel.setFont(fontmed(12));
        lblDatFinRel.setFont(fontmed(12));

        if (rbtnTodRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 1, 5, null, null, 0);
        } else if (rbtnSerRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 2, 5, null, null, 0);
        } else if (rbtnVenRel.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 3, 5, null, null, 0);
        } else if (rbtnVenRel1.isSelected()) {
            tabelarelatorio(tblRel, scrRel, 4, 5, null, null, 0);
        } else {
            tabelarelatorio(tblRel, scrRel, 5, 5, null, null, 0);
        }

        if (txtDatIniRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(430, 150);
        } else if (txtDatFinRel.hasFocus()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatIniRel.setLocation(290, 150);
        }

        if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
            if (rbtnVenRel.isSelected()) {
                cmbrelatorio(tblRel, cmbRel, 2);
            } else {
                cmbrelatorio(tblRel, cmbRel, 1);
            }
        } else {
            cmbRel.setSelectedIndex(0);
            cmbRel.setEnabled(false);
        }

        if (lblResRel.isVisible()) {
            tblRel.setVisible(false);
            scrRel.setVisible(false);
            DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
            mol.setRowCount(0);
            lblValTotRel.setText("R$ 0,00");
            lblValMedRel.setText("R$ 0,00");
            lblTotEntRel.setText("0");

            lblValDinRel.setText("R$ 0,00");
            lblValCarRel.setText("R$ 0,00");
            lblValPixRel.setText("R$ 0,00");
        } else {
            tblRel.setVisible(true);
            scrRel.setVisible(true);
        }

        btnAnoRel.grabFocus();
    }//GEN-LAST:event_btnAnoRelMouseReleased

    private void txtDatFinRelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatFinRelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            btnNumDiaRel.setVisible(false);
            btnMenDiaRel.setVisible(false);
            btnMaiDiaRel.setVisible(false);
            lblDiaRel.setVisible(false);

            try {

                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));

                if (rbtnTodRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 1, 6, data1, data2, 0);
                } else if (rbtnSerRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 2, 6, data1, data2, 0);
                } else if (rbtnVenRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 3, 6, data1, data2, 0);
                } else if (rbtnVenRel1.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 4, 6, data1, data2, 0);
                } else {
                    tabelarelatorio(tblRel, scrRel, 5, 6, data1, data2, 0);
                }

                btnTodRel.setFont(fontmed(12));
                btnDiaRel.setFont(fontmed(12));
                btnSemRel.setFont(fontmed(12));
                btnMesRel.setFont(fontmed(12));
                btnAnoRel.setFont(fontmed(12));
                lblDatIniRel.setFont(fontbold(13));
                lblDatFinRel.setFont(fontbold(13));

                chkCus.setSelected(false);

                if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
                    if (rbtnVenRel.isSelected()) {
                        cmbrelatorio(tblRel, cmbRel, 2);
                    } else {
                        cmbrelatorio(tblRel, cmbRel, 1);
                    }
                } else {
                    cmbRel.setSelectedIndex(0);
                    cmbRel.setEnabled(false);
                }

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Data inserida inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_txtDatFinRelKeyPressed

    private void txtDatIniRelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatIniRelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {

                btnNumDiaRel.setVisible(false);
                btnMenDiaRel.setVisible(false);
                btnMaiDiaRel.setVisible(false);
                lblDiaRel.setVisible(false);

                btnTodRel.setFont(fontmed(12));
                btnDiaRel.setFont(fontmed(12));
                btnSemRel.setFont(fontmed(12));
                btnMesRel.setFont(fontmed(12));
                btnAnoRel.setFont(fontmed(12));
                lblDatIniRel.setFont(fontbold(13));
                lblDatFinRel.setFont(fontbold(13));

                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));

                if (rbtnTodRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 1, 6, data1, data2, 0);
                } else if (rbtnSerRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 2, 6, data1, data2, 0);
                } else if (rbtnVenRel.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 3, 6, data1, data2, 0);
                } else if (rbtnVenRel1.isSelected()) {
                    tabelarelatorio(tblRel, scrRel, 4, 6, data1, data2, 0);
                } else {
                    tabelarelatorio(tblRel, scrRel, 5, 6, data1, data2, 0);
                }

                if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
                    if (rbtnVenRel.isSelected()) {
                        cmbrelatorio(tblRel, cmbRel, 2);
                    } else {
                        cmbrelatorio(tblRel, cmbRel, 1);
                    }
                } else {
                    cmbRel.setSelectedIndex(0);
                    cmbRel.setEnabled(false);
                }

                chkCus.setSelected(false);

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Data inserida inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
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
        int resp = JOptionPane.showOptionDialog(null, "Adicionar plano à contagem?", "Máscara", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                planosDAO pladao = new planosDAO();
                planosdiaDAO pddao = new planosdiaDAO();

                pladao.zerar();
                pddao.zerar();

                pladao.adicionar();

                if (txtPlaMas.getText().contains("Black")) {
                    pddao.adicionar(2);
                } else {
                    pddao.adicionar(1);
                }

                JOptionPane.showMessageDialog(null, "Adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        int resp3 = JOptionPane.showOptionDialog(null, "Adicionar à contagem de troca de chip?", "Máscara", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp3 == JOptionPane.YES_OPTION) {

            try {

                planosdiaDAO pddao = new planosdiaDAO();
                pddao.zerar();
                pddao.adicionar(3);

                JOptionPane.showMessageDialog(null, "Adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        int resp1 = JOptionPane.showOptionDialog(null, "Atualizar estoque de chip?", "Máscara", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp1 == JOptionPane.YES_OPTION) {

            estoque es = new estoque();
            estoqueDAO esdao = new estoqueDAO();

            int resp2 = JOptionPane.showOptionDialog(null, "Qual chip?", "Máscara", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Triplo", "eSIM", "Naked"}, null);

//TRIPLO 546
//ESIM 547
//NAKED 548
            try {
                if (resp2 == JOptionPane.YES_OPTION) {

                    es.setId(546);
                    esdao.excluirum(es);

                } else if (resp2 == JOptionPane.NO_OPTION) {
                    es.setId(547);
                    esdao.excluirum(es);

                } else if (resp2 == JOptionPane.CANCEL_OPTION) {
                    es.setId(548);
                    esdao.excluirum(es);

                }

                JOptionPane.showMessageDialog(null, "Estoque atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        String nome = txtNomMas.getText();
        String cpf = txtCpfMas.getText();
        String contato = txtNumConMas.getText();
        String acesso = txtNumAceMas.getText();
        String servico = "( X ) Ativação\n(    ) Migração (Pré P/Ctrl)\n(    ) Conversão (Troca Pré P/Controle)\n";
        String port = "(    ) Sim\n( X ) Não\n";
        String plano = txtPlaMas.getText();
        String c6 = "(    ) Sim\n( X ) Não\n";
        String pago = "(    ) DACC (Débito em Conta)\n( X ) Boleto\n(    ) Cartão de Crédito\n";
        String melhor = "(    ) Sim\n( X ) Não\n";
        String app = "(    ) Sim\n( X ) Não";
        String venc = txtVenMas.getText();

        if (rbtnMigMas.isSelected()) {
            servico = "(    ) Ativação\n( X ) Migração (Pré P/Ctrl)\n(    ) Conversão (Troca Pré P/Controle)\n";
        } else if (rbtnMigTroMas.isSelected()) {
            servico = "(    ) Ativação\n(    ) Migração (Pré P/Ctrl)\n( X ) Conversão (Troca Pré P/Controle)\n";
        }

        if (chkC6Mas.isSelected()) {
            c6 = "( X ) Sim\n(    ) Não\n";
        }

        if (!"".equals(txtNumPorMas.getText())) {
            port = "( X ) Sim\n(    ) Não\n"
                    + "\n*Nº Portado:* " + txtNumPorMas.getText() + "\n";
        }

        if (chkDebMas.isSelected()) {
            pago = "( X ) DACC (Débito em Conta)\n(    ) Boleto\n(    ) Cartão de Crédito\n";
        } else if (chkCarMas.isSelected()) {
            pago = "(    ) DACC (Débito em Conta)\n(    ) Boleto\n( X ) Cartão de Crédito\n";
        }

        if (chkMelMas.isSelected()) {
            melhor = "( X ) Sim\n(    ) Não\n";
        }

        if (chkAppMas.isSelected()) {
            app = "( X ) Sim\n(    ) Não";
        }

        txtAreMas.setText(
                "*Nome do PDV:* Empório Cell\n"
                + "*Nome do Vendedor:* Guilherme\n"
                + "*Nome do Cliente:* " + nome + "\n"
                + "*CPF:* " + cpf + "\n"
                + "*Telefone de Contato:* " + contato + "\n"
                + "\n*Nº do Acesso:* " + acesso + "\n"
                + "\n*Serviço Realizado*\n"
                + servico
                + "\n*Portabilidade*\n"
                + port
                + "\n*Qual Plano Foi Vendido?*\n"
                + plano
                + "\n\n*Forma de Pagamento*\n"
                + pago
                + "\n*Escolheu Melhor Data de Vencimento no Sistema*\n"
                + melhor
                + "\n*Data de Vencimento:* " + venc + "\n"
                + "\n*Sistema Utilizado*\n"
                + "( X ) App TIM Vendas\n"
                + "\n*Conta C6 Bank*\n"
                + c6
                + "\n*Instalou e Acessou App Meu TIM no Celular do Cliente*\n"
                + app
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
        chkMelMas.setSelected(false);
        chkAppMas.setSelected(false);
        chkDebMasa.setSelected(false);

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
        btnCopMas.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCopMasMouseEntered

    private void btnCopMasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCopMasMouseExited
        btnCopMas.setForeground(corforeazul);
    }//GEN-LAST:event_btnCopMasMouseExited

    private void txtVenMasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVenMasKeyTyped
        if (txtVenMas.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtVenMas.getText().length() == 2) {

                    txtVenMas.setText(txtVenMas.getText() + "/");
                    txtVenMas.setCaretPosition(3);

                } else if (txtVenMas.getText().length() == 5) {

                    txtVenMas.setText(txtVenMas.getText() + "/");
                    txtVenMas.setCaretPosition(6);

                }

            }
            if (txtVenMas.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtVenMasKeyTyped

    private void txtVenMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVenMasKeyReleased

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
            de.setValor(Double.valueOf(txtPreDes.getText().replaceAll(",", ".")));
            de.setData(formatterbanco.format(formatter.parse(txtDatDes.getText())));
            de.setStatus(0);

            dedao.inserir(de);

            JOptionPane.showMessageDialog(null, "Afazer inserida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            pnlCadDes.setVisible(false);
            lblTitPri.setVisible(false);

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);

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

            int resp = JOptionPane.showOptionDialog(null, "Ao confirmar a conclusão, a data será definida para o próximo mês e será considerado a data de hoje como a conclusão. Atualize somente se tiver feito o acerto! Deseja prosseguir?", "Conclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

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

            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                despezas de = new despezas();
                despezasDAO dedao = new despezasDAO();

                de.setId(Integer.parseInt(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 0).toString()));

                dedao.excluir(de);

                JOptionPane.showMessageDialog(null, "Excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

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

            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                despezas de = new despezas();
                despezasDAO dedao = new despezasDAO();

                de.setId(Integer.parseInt(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 0).toString()));
                de.setDescricao(txtDesGerDes.getText());
                de.setValor(Double.valueOf(txtPreGerDes.getText().replace(".", "").replace(",", ".")));
                de.setData(formatterbanco.format(formatter.parse(txtDatGerDes.getText())));

                dedao.alterar(de);

                JOptionPane.showMessageDialog(null, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

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
        if (!pnlGerDes.isVisible()) {

            if (tabelagerenciardespezas(tblGerDes, scrGerDes)) {

                lblTitPri.setText("Gerenciar Afazeres");
                lblTitPri.setVisible(true);

                btnExcGerDes.setEnabled(false);
                btnAltGerDes.setEnabled(false);

                txtDesGerDes.setText(null);
                txtPreGerDes.setText(null);
                txtDatGerDes.setText(null);
                pnlVen.setVisible(false);
                pnlJur.setVisible(false);

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

                pnlbtn();
                pnlGerDes.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(null, "Sem afazeres para gerenciar. Cadastre-as primeiro!", "Gerenciar afazeres", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {

            pnlbtn();
            pnlGerDes.setVisible(true);

        }
    }//GEN-LAST:event_btnGerDesMouseReleased

    private void btnExcGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerEntActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                if (tblSelIteGerEnt.getRowCount() != 0) {

                    int resp1 = JOptionPane.showOptionDialog(null, "Por favor, devolva os ítens para o estoque. Se prosseguir, todos os produtos serão excluídos!\n\nDevolver ítens?", "Entrada", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                    if (resp1 == JOptionPane.YES_OPTION) {

                        btnIteGerEnt.doClick();

                    } else if (resp1 == JOptionPane.NO_OPTION) {

                        entrada en = new entrada();
                        entradaDAO endao = new entradaDAO();

                        en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 11).toString());

                        endao.excluir(en);

                        JOptionPane.showMessageDialog(null, "Entrada excluída com sucesso!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

                        pnlGerEnt.setVisible(false);
                        lblTitPri.setVisible(false);

                    }

                } else {

                    entrada en = new entrada();
                    entradaDAO endao = new entradaDAO();
                    en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 11).toString());
                    endao.excluir(en);

                    JOptionPane.showMessageDialog(null, "Entrada excluída com sucesso!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

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

                tblGerEnt.setVisible(false);
                scrGerEnt.setVisible(false);

                JOptionPane.showMessageDialog(null, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBusGerEntActionPerformed

    private void btnIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIteGerEntActionPerformed
        if (tblSelIteGerEnt.getRowCount() == 0) {
            tblSelIteGerEnt.setVisible(false);
            scrSelIteGerEnt.setVisible(false);
            lblSelIteGerEnt.setVisible(false);
        } else {
            tblSelIteGerEnt.setVisible(true);
            scrSelIteGerEnt.setVisible(true);
            lblSelIteGerEnt.setVisible(true);
        }

        tblEstIteGerEnt.setVisible(false);
        scrEstIteGerEnt.setVisible(false);
        lblEstIteGerEnt.setVisible(false);

        lblEstIteGerEnt.setForeground(corforeazul);
        btnVolIteGerEnt.grabFocus();
        tblSelIteGerEnt.getTableHeader().setFont(fontbold(11));
        pnlIteGerEnt.setVisible(true);
        pnlGerEnt.setVisible(false);

        if (!txtBusIteGerEnt.getText().isEmpty()) {
            anitxtin(lblBusIteGerEnt);
        }
    }//GEN-LAST:event_btnIteGerEntActionPerformed

    private void btnCanGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerEntActionPerformed
        if (btnAltGerEnt.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(null, "Antes de cancelar, verifique a tabela de produtos selecionados e remova aqueles que não fazem parte da entrada! Deseja cancelar?", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

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
        lblDatGerEnt.setLocation(780, 80);
        lblPreGerEnt.setLocation(780, 130);
        lblDetGerEnt.setLocation(780, 180);
        lblCliGerEnt.setLocation(1020, 80);
        lblCusGerEnt.setLocation(1020, 130);
        lblForGerEnt.setLocation(1020, 180);
        lblR$CusGerEnt.setVisible(false);

        txtDatGerEnt.setText(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 0).toString());
        txtPreGerEnt.setText((!"Não Aplicável".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 5).toString())) ? (tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 5).toString()).substring(3, tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 5).toString().length()) : null);
        txtDetGerEnt.setText((!"Sem Detalhes".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString())) ? tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString() : null);

        txtCliGerEnt.setEnabled(false);
        sepCliGerEnt.setForeground(Color.GRAY);
        lblCliGerEnt.setEnabled(false);

        txtCusGerEnt.setEnabled(false);
        sepCusGerEnt.setForeground(Color.GRAY);
        lblCusGerEnt.setEnabled(false);

        txtForGerEnt.setEnabled(false);
        sepForGerEnt.setForeground(Color.GRAY);
        lblForGerEnt.setEnabled(false);

        txtCliGerEnt.setText(null);
        txtCusGerEnt.setText(null);
        txtForGerEnt.setText(null);

        estoque es = new estoque();

        es.setTipoproduto("Capinha");

        es.setModelo("");

        tabelaestoqueconsulta(es, tblEstIteGerEnt, scrEstIteGerEnt);

        tabelaitensselecionadosgerenciar(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 11).toString());

        txtBusIteGerEnt.setEnabled(false);
        txtBusIteGerEnt.setText(null);
        lblBusIteGerEnt.setEnabled(false);
        sepBusIteCadEnt1.setForeground(Color.GRAY);
        btnGroup1.clearSelection();

        tblEstIteGerEnt.requestFocus();

        if ("Dinheiro".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString())) {

            rbtnDinGerEnt.setSelected(true);

        } else if ("Cartão".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 7).toString())) {

            rbtnCarGerEnt.setSelected(true);

        } else {

            rbtnPixGerEnt.setSelected(true);

        }

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

            txtCliGerEnt.setEnabled(true);
            sepCliGerEnt.setForeground(corforeazul);
            lblCliGerEnt.setEnabled(true);

            txtCusGerEnt.setEnabled(true);
            sepCusGerEnt.setForeground(corforeazul);
            lblCusGerEnt.setEnabled(true);

            txtForGerEnt.setEnabled(true);
            sepForGerEnt.setForeground(corforeazul);
            lblForGerEnt.setEnabled(true);

            txtCliGerEnt.setText((!"Não Informado".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 3).toString())) ? tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 3).toString() : null);
            txtCusGerEnt.setText((!"Não Aplicável".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString())) ? (tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString()).substring(3, tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString().length()) : null);
            txtForGerEnt.setText((!"Não Informado".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 8).toString())) ? tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 8).toString() : null);

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

            try {

                for (int i = 1; i <= cmbSerGerEnt.getItemCount(); i++) {

                    cmbSerGerEnt.setSelectedIndex(i);

                    itens selectedItem = (itens) cmbSerGerEnt.getSelectedItem();

                    String textoSelecionado = selectedItem.getDescricao();

                    if (tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 2).toString().equals(textoSelecionado)) {

                        break;

                    }

                }

            } catch (Exception e) {

                cmbSerGerEnt.setSelectedIndex(0);

            }

            btnIteGerEnt.setEnabled(true);

        }

        txtDatGerEnt.setEnabled(true);
        txtPreGerEnt.setEnabled(true);
        if ("Não Aplicável".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 5).toString())) {
            lblR$GerEnt.setVisible(false);
        } else {
            lblR$GerEnt.setVisible(true);
        }
        txtDetGerEnt.setEnabled(true);

        lblDatGerEnt.setEnabled(true);
        lblPreGerEnt.setEnabled(true);
        lblDetGerEnt.setEnabled(true);

        rbtnDinGerEnt.setEnabled(true);
        rbtnCarGerEnt.setEnabled(true);
        rbtnPixGerEnt.setEnabled(true);

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
        if (!txtCliGerEnt.getText().isEmpty()) {
            anitxtin(lblCliGerEnt);
        }
        if (!txtCusGerEnt.getText().isEmpty()) {
            anitxtin(lblCusGerEnt);
            lblR$CusGerEnt.setVisible(true);
        } else {
            lblR$CusGerEnt.setVisible(false);
        }
        if (!txtForGerEnt.getText().isEmpty()) {
            anitxtin(lblForGerEnt);
        }
    }//GEN-LAST:event_tblGerEntMouseClicked

    private void btnAltGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerEntActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                entrada en = new entrada();
                entradaDAO endao = new entradaDAO();

                en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 11).toString());

                endao.excluir(en);

                if (!("Venda".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString()) && tblSelIteGerEnt.getRowCount() == 0)) {

                    if (!(cmbSerGerEnt.getSelectedIndex() == 0 && ("Assistência".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString()) || "Serviço".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())))) {

                        try {

                            int idser = 0;

                            int idpagamento = 1;

                            if (rbtnCarGerEnt.isSelected()) {
                                idpagamento = 2;
                            } else if (rbtnPixGerEnt.isSelected()) {
                                idpagamento = 3;
                            }

                            if (cmbSerGerEnt.getSelectedIndex() != 0) {
                                itens selectedItem = (itens) cmbSerGerEnt.getSelectedItem();
                                idser = selectedItem.getId();
                            }

                            if (tblSelIteGerEnt.getRowCount() != 0) {

                                for (int i = 1; i <= tblSelIteGerEnt.getRowCount(); i++) {

                                    en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 11).toString());
                                    en.setData(formatterbanco.format(((formatter.parse(txtDatGerEnt.getText())))));
                                    en.setPreco(Double.valueOf(txtPreGerEnt.getText().replace(".", "").replace(",", ".")));
                                    en.setDetalhes(txtDetGerEnt.getText());
                                    en.setFormapagamento(idpagamento);

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

                                en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 11).toString());
                                en.setData(formatterbanco.format((formatter.parse(txtDatGerEnt.getText()))));
                                en.setPreco(Double.valueOf(txtPreGerEnt.getText().replace(".", "").replace(",", ".")));
                                en.setDetalhes(txtDetGerEnt.getText());
                                en.setIdtiposervico(idser);
                                en.setIdestoque(1);
                                en.setFormapagamento(idpagamento);

                                if ("Serviço".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

                                    endao.inserir(en, 1);

                                } else if ("Assistência".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 1).toString())) {

                                    en.setCliente(txtCliGerEnt.getText());
                                    en.setCusto((!txtCusGerEnt.getText().isEmpty()) ? Double.valueOf(txtCusGerEnt.getText().replace(".", "").replace(",", ".")) : null);
                                    en.setFornecedor(txtForGerEnt.getText());

                                    endao.inserir(en, 3);

                                }

                            }

                            JOptionPane.showMessageDialog(null, "Entrada alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                            pnlGerEnt.setVisible(false);

                        } catch (SQLException | ParseException ex) {
                            Logger.getLogger(main.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Selecione o serviço!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    }

                    lblTitPri.setVisible(false);

                } else {

                    JOptionPane.showMessageDialog(null, "Selecione os ítem do estoque!", "Atenção", JOptionPane.INFORMATION_MESSAGE);

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
        if (!pnlGerEnt.isVisible()) {

            txtDatBusGerEnt.setText(null);
            txtDatGerEnt.setText(null);
            txtPreGerEnt.setText(null);
            txtDetGerEnt.setText(null);

            btnGroup.clearSelection();

            cmbSerGerEnt.setSelectedIndex(0);

            rbtnCarGerEnt.setEnabled(false);
            rbtnDinGerEnt.setEnabled(false);
            rbtnPixGerEnt.setEnabled(false);

            tblGerEnt.setVisible(false);
            scrGerEnt.setVisible(false);

            txtCliGerEnt.setEnabled(false);
            txtCliGerEnt.setText(null);
            sepCliGerEnt.setForeground(Color.GRAY);
            lblCliGerEnt.setEnabled(false);

            txtCusGerEnt.setEnabled(false);
            txtCusGerEnt.setText(null);
            sepCusGerEnt.setForeground(Color.GRAY);
            lblCusGerEnt.setEnabled(false);

            txtForGerEnt.setEnabled(false);
            txtForGerEnt.setText(null);
            sepForGerEnt.setForeground(Color.GRAY);
            lblForGerEnt.setEnabled(false);

            lblDatGerEnt.setEnabled(false);
            txtDatGerEnt.setEnabled(false);
            lblPreGerEnt.setEnabled(false);
            lblR$GerEnt.setVisible(false);
            lblR$CusGerEnt.setVisible(false);

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
            lblDatGerEnt.setLocation(780, 80);
            lblPreGerEnt.setLocation(780, 130);
            lblDetGerEnt.setLocation(780, 180);

            lblCliGerEnt.setLocation(1020, 80);
            lblCusGerEnt.setLocation(1020, 130);
            lblForGerEnt.setLocation(1020, 180);

            lblTitPri.setText("Gerenciar Entrada");
            lblTitPri.setVisible(true);

            pnlbtn();
            pnlGerEnt.setVisible(true);

        } else {

            pnlbtn();
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

            JOptionPane.showMessageDialog(null, "Ítem já adicionado!", "Entrada", JOptionPane.ERROR_MESSAGE);

        } else {

            String opc = JOptionPane.showInputDialog(null, "Informe a quantidade deste ítem", "Entrada", JOptionPane.QUESTION_MESSAGE);

            if (opc != null) {

                try {

                    int i = Integer.parseInt(opc);

                    if (rbtnChiIteGerEnt.isSelected()) {

                        if (Integer.parseInt(tblEstIteGerEnt.getValueAt(tblEstIteGerEnt.getSelectedRow(), 3).toString()) < i) {

                            JOptionPane.showMessageDialog(null, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteGerEnt, tblSelIteGerEnt, opc, rbtnChiIteGerEnt);

                            lblSelIteGerEnt.setForeground(corforeazul);
                            scrSelIteGerEnt.setVisible(true);
                            tblSelIteGerEnt.setVisible(true);

                        }

                    } else {

                        if (Integer.parseInt(tblEstIteGerEnt.getValueAt(tblEstIteGerEnt.getSelectedRow(), 5).toString()) < i) {

                            JOptionPane.showMessageDialog(null, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteGerEnt, tblSelIteGerEnt, opc, rbtnChiIteGerEnt);

                            lblSelIteGerEnt.setForeground(corforeazul);
                            scrSelIteGerEnt.setVisible(true);
                            tblSelIteGerEnt.setVisible(true);

                        }

                    }

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(null, "Digite apenas número!", "Erro", JOptionPane.ERROR_MESSAGE);

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
                lblEstIteGerEnt.setVisible(true);
                lblEstIteGerEnt.setForeground(corforeazul);

            } else {

                JOptionPane.showMessageDialog(null, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

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

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);

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

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);

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

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);

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

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);

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
        try {

            if (txtCliOs.getText().isEmpty() || txtEndOs.getText().isEmpty() || txtTelOs.getText().isEmpty() || txtEquOs.getText().isEmpty() || txtMarOs.getText().isEmpty() || txtModOs.getText().isEmpty() || txtDefOs.getText().isEmpty() || txtDatOs.getText().isEmpty() || txtRepOs.getText().isEmpty() || (txtDatSaiOs.getText().isEmpty() && chkGarOs.isSelected())) {

                JOptionPane.showMessageDialog(null, "Preencha todos os dados!", "Atenção", JOptionPane.WARNING_MESSAGE);

            } else {

                if (btnGerOs.getText().equals("Gerar")) {//VERIFICA SE IGUAL GERAR O BOTAP

                    int resp1 = JOptionPane.showOptionDialog(null, "Salvar OS no banco de dados?", "OS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                    if (resp1 != JOptionPane.CLOSED_OPTION) {//SE APERTAR X NO SALVAR BD

                        int resp2 = JOptionPane.showOptionDialog(null, "Quantas vias?", "OS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"1 Via", "2 Vias"}, "Sim");

                        if (resp2 != JOptionPane.CLOSED_OPTION) {//SE PAERTAR X VIA

                            if (resp1 == JOptionPane.YES_OPTION) {//YES = 1 VIA 

                                osDAO osdao = new osDAO();
                                os oss = new os();

                                oss.setCliente(txtCliOs.getText());
                                oss.setTelefone(txtTelOs.getText());
                                oss.setEndereco(txtEndOs.getText());
                                oss.setEquipamento(txtEquOs.getText());
                                oss.setMarca(txtMarOs.getText());
                                oss.setModelo(txtModOs.getText());
                                oss.setDefeito(txtDefOs.getText());
                                oss.setReparo(txtRepOs.getText());
                                oss.setDataentrada(txtDatOs.getText());
                                oss.setDatasaida((!txtDatSaiOs.getText().equals("")) ? txtDatSaiOs.getText() : "Não Aplicável");
                                oss.setPreco(moedadoublereal(Double.valueOf(txtPreOs.getText().replace(".", "").replace(",", "."))));

                                if (chkGarOs.isSelected()) {
                                    oss.setGarantia("Sim");

                                } else {
                                    oss.setGarantia("Não");
                                }

                                osdao.inserir(oss);

                            }

                            String datasaida = (!txtDatSaiOs.getText().equals("")) ? txtDatSaiOs.getText() : "____/____/________";

                            String datagarantia = "Não Aplicável";

                            if (!txtDatSaiOs.getText().equals("") && chkGarOs.isSelected()) {

                                Date data = formatter.parse(datasaida);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(data);
                                calendar.add(Calendar.MONTH, 3);
                                Date novaData = calendar.getTime();
                                datagarantia = formatter.format(novaData);

                            }

                            Map<String, Object> parameters = new HashMap<>();

                            parameters.clear();

                            parameters.put("LogoTim", getClass().getResourceAsStream("/images/LOGOTIM.png"));
                            parameters.put("LogoLoja", getClass().getResourceAsStream("/images/LogoLoja580.png"));
                            parameters.put("Nome", txtCliOs.getText());
                            parameters.put("Endereco", txtEndOs.getText());
                            parameters.put("Telefone", txtTelOs.getText());
                            parameters.put("Equipamento", txtEquOs.getText());
                            parameters.put("Marca", txtMarOs.getText());
                            parameters.put("Modelo", txtModOs.getText());
                            parameters.put("Condicoes", txtDefOs.getText());
                            parameters.put("Defeito", txtRepOs.getText());
                            parameters.put("DataEntrada", txtDatOs.getText());
                            parameters.put("DataSaida", datasaida);
                            parameters.put("Garantia", datagarantia);
                            parameters.put("Preco", moedadoublereal(Double.valueOf(txtPreOs.getText().replace(".", "").replace(",", "."))));

                            parameters.put("LogoTim2", getClass().getResourceAsStream("/images/LOGOTIM.png"));
                            parameters.put("LogoLoja2", getClass().getResourceAsStream("/images/LogoLoja580.png"));
                            parameters.put("Nome2", txtCliOs.getText());
                            parameters.put("Endereco2", txtEndOs.getText());
                            parameters.put("Telefone2", txtTelOs.getText());
                            parameters.put("Equipamento2", txtEquOs.getText());
                            parameters.put("Marca2", txtMarOs.getText());
                            parameters.put("Modelo2", txtModOs.getText());
                            parameters.put("Condicoes2", txtDefOs.getText());
                            parameters.put("Defeito2", txtRepOs.getText());
                            parameters.put("DataEntrada2", txtDatOs.getText());
                            parameters.put("DataSaida2", datasaida);
                            parameters.put("Garantia2", datagarantia);
                            parameters.put("Preco2", moedadoublereal(Double.valueOf(txtPreOs.getText().replace(".", "").replace(",", "."))));

                            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("os/OSEmpSysView2.jasper");

                            if (resp2 == JOptionPane.YES_OPTION) {
                                inputStream = getClass().getClassLoader().getResourceAsStream("os/OSEmpSysView1.jasper");
                            }

                            JasperPrint print = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource(1));

                            JasperViewer jc = new JasperViewer(print, false);
                            jc.setVisible(true);
                            jc.toFront();

                            pnlOs.setVisible(false);
                            lblTitPri.setVisible(false);

                        }

                    }

                } else {//BOTAO ALTERAR

                    int resp1 = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar a OS?", "OS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                    if (resp1 == JOptionPane.YES_OPTION) {//YES = ALTERAR

                        osDAO osdao = new osDAO();
                        os oss = new os();

                        oss.setCliente(txtCliOs.getText());
                        oss.setTelefone(txtTelOs.getText());
                        oss.setEndereco(txtEndOs.getText());
                        oss.setEquipamento(txtEquOs.getText());
                        oss.setMarca(txtMarOs.getText());
                        oss.setModelo(txtModOs.getText());
                        oss.setDefeito(txtDefOs.getText());
                        oss.setReparo(txtRepOs.getText());
                        oss.setDataentrada(txtDatOs.getText());
                        oss.setDatasaida((!txtDatSaiOs.getText().equals("")) ? txtDatSaiOs.getText() : "Não Aplicável");
                        oss.setPreco(moedadoublereal(Double.valueOf(txtPreOs.getText().replace(".", "").replace(",", "."))));

                        oss.setId(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("ID")).toString());

                        if (chkGarOs.isSelected()) {
                            oss.setGarantia("Sim");
                        } else {
                            oss.setGarantia("Não");
                        }

                        osdao.alterar(oss);

                        JOptionPane.showMessageDialog(null, "OS alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        pnlbtn();
                        lblTitPri.setVisible(false);

                    }
                }
            }

        } catch (JRException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular garantia!", "Erro", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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

    private void txtDefOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDefOsFocusGained
        if (txtDefOs.getText().isEmpty()) {
            anitxtin(lblConOs);
        }
    }//GEN-LAST:event_txtDefOsFocusGained

    private void txtDefOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDefOsFocusLost
        if (txtDefOs.getText().isEmpty()) {
            anitxtout(lblConOs);
        }
    }//GEN-LAST:event_txtDefOsFocusLost

    private void txtRepOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRepOsFocusGained
        if (txtRepOs.getText().isEmpty()) {
            anitxtin(lblDefOs);
        }
    }//GEN-LAST:event_txtRepOsFocusGained

    private void txtRepOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRepOsFocusLost
        if (txtRepOs.getText().isEmpty()) {
            anitxtout(lblDefOs);
        }
    }//GEN-LAST:event_txtRepOsFocusLost

    private void txtDatOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatOsFocusGained
        if (txtDatOs.getText().isEmpty()) {
            anitxtin(lblDatEntOs);
        }
    }//GEN-LAST:event_txtDatOsFocusGained

    private void txtDatOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatOsFocusLost
        if (txtDatOs.getText().isEmpty()) {
            anitxtout(lblDatEntOs);
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

    private void txtDatSaiOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatSaiOsFocusGained
        if (txtDatSaiOs.getText().isEmpty()) {
            anitxtin(lblHorOs);
        }
    }//GEN-LAST:event_txtDatSaiOsFocusGained

    private void txtDatSaiOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatSaiOsFocusLost
        if (txtDatSaiOs.getText().isEmpty()) {
            anitxtout(lblHorOs);
        }
    }//GEN-LAST:event_txtDatSaiOsFocusLost

    private void txtDatSaiOsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatSaiOsKeyTyped
        if (txtDatSaiOs.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatSaiOs.getText().length() == 2) {

                    txtDatSaiOs.setText(txtDatSaiOs.getText() + "/");
                    txtDatSaiOs.setCaretPosition(3);

                } else if (txtDatSaiOs.getText().length() == 5) {

                    txtDatSaiOs.setText(txtDatSaiOs.getText() + "/");
                    txtDatSaiOs.setCaretPosition(6);

                }

            }
            if (txtDatSaiOs.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtDatSaiOsKeyTyped

    private void btnOrdSerPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOrdSerPriMouseReleased
        if (btnCadOsPri.isVisible()) {

            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);

        } else {

            btnCadEst.setVisible(false);
            btnConEst.setVisible(false);
            btnGerEst.setVisible(false);
            btnCadOsPri.setVisible(true);
            btnGerOsPri.setVisible(true);
            btnJurPri.setVisible(false);
            btnCadEnt.setVisible(false);
            btnGerEnt.setVisible(false);
            btnConEnt.setVisible(false);

            btnCadTipSer.setVisible(false);
            btnGerTipSer.setVisible(false);
            btnMasPla.setVisible(false);
            btnDes.setVisible(false);
            btnCadDes.setVisible(false);
            btnGerDes.setVisible(false);
            btnVen.setVisible(false);
            btnCadVen.setVisible(false);

        }
    }//GEN-LAST:event_btnOrdSerPriMouseReleased

    private void txtDatBusGerEntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatBusGerEntKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {

                String data = formatterbanco.format(formatter.parse(txtDatBusGerEnt.getText()));

                if (tabelagerenciarentrada(tblGerEnt, scrGerEnt, data)) {

                    tblGerEnt.setVisible(true);
                    scrGerEnt.setVisible(true);

                } else {

                    JOptionPane.showMessageDialog(null, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

                }

            } catch (ParseException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_txtDatBusGerEntKeyPressed

    private void txtBusConEstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusConEstKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            estoque es = new estoque();

            es.setModelo(txtBusConEst.getText());
            es.setTipoproduto(txtTipConEst.getText());

            if (tabelaestoqueconsulta(es, tblConEst, scrConEst)) {

                scrConEst.setVisible(true);
                tblConEst.setVisible(true);

            } else {

                scrConEst.setVisible(false);
                tblConEst.setVisible(false);

                JOptionPane.showMessageDialog(null, "Ítem não cadastrado no sistema!", "Consultar", JOptionPane.INFORMATION_MESSAGE);

            }

        }
    }//GEN-LAST:event_txtBusConEstKeyPressed

    private void txtBusGerEstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusGerEstKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            estoque es = new estoque();

            es.setModelo(txtBusGerEst.getText());
            es.setTipoproduto(txtTipGerEst.getText());

            if (tabelaestoquegerenciar(es)) {

                scrGerEst.setVisible(true);
                tblGerEst.setVisible(true);

            } else {

                scrGerEst.setVisible(false);
                tblGerEst.setVisible(false);

                JOptionPane.showMessageDialog(null, "Nenhum dado encontrado!", "Gerenciar", JOptionPane.INFORMATION_MESSAGE);

            }

        }
    }//GEN-LAST:event_txtBusGerEstKeyPressed

    private void rbtnPixCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPixCadEntActionPerformed
        rbtnDebCadEnt.setEnabled(false);
        rbtnCreCadEnt.setEnabled(false);
        rbtnCreCadEnt.setVisible(false);
        rbtnDebCadEnt.setVisible(false);
        spnParCadEnt.setVisible(false);
        lblParCadEnt.setVisible(false);
    }//GEN-LAST:event_rbtnPixCadEntActionPerformed

    private void rbtnCarCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCarCadEntActionPerformed
        rbtnDebCadEnt.setEnabled(true);
        rbtnCreCadEnt.setEnabled(true);
        rbtnCreCadEnt.setVisible(true);
        rbtnDebCadEnt.setVisible(true);
        spnParCadEnt.setVisible(true);
        lblParCadEnt.setVisible(true);
        spnParCadEnt.setValue(0);
        spnParCadEnt.setEnabled(false);
        lblParCadEnt.setEnabled(false);

        btnGroup3.clearSelection();
    }//GEN-LAST:event_rbtnCarCadEntActionPerformed

    private void rbtnDinCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnDinCadEntActionPerformed
        rbtnDebCadEnt.setEnabled(false);
        rbtnCreCadEnt.setEnabled(false);
        rbtnCreCadEnt.setVisible(false);
        rbtnDebCadEnt.setVisible(false);
        spnParCadEnt.setVisible(false);
        lblParCadEnt.setVisible(false);
    }//GEN-LAST:event_rbtnDinCadEntActionPerformed

    private void rbtnDinGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnDinGerEntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnDinGerEntActionPerformed

    private void rbtnCarGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCarGerEntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnCarGerEntActionPerformed

    private void rbtnPixGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPixGerEntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnPixGerEntActionPerformed

    private void txtCusCadEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCusCadEntFocusGained
        if (txtCusCadEnt.getText().isEmpty()) {
            anitxtin(lblCusCadEnt);
            lblR$CusCadEnt.setVisible(true);
        }
    }//GEN-LAST:event_txtCusCadEntFocusGained

    private void txtCusCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCusCadEntFocusLost
        if (txtCusCadEnt.getText().isEmpty()) {
            anitxtout(lblCusCadEnt);
            lblR$CusCadEnt.setVisible(false);
        }
    }//GEN-LAST:event_txtCusCadEntFocusLost

    private void txtCusCadEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCusCadEntKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtCusCadEnt.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCusCadEntKeyTyped

    private void txtForCadEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtForCadEntFocusGained
        if (txtForCadEnt.getText().isEmpty()) {
            anitxtin(lblForCadEnt);
        }
    }//GEN-LAST:event_txtForCadEntFocusGained

    private void txtForCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtForCadEntFocusLost
        if (txtForCadEnt.getText().isEmpty()) {
            anitxtout(lblForCadEnt);
        }
    }//GEN-LAST:event_txtForCadEntFocusLost

    private void txtCliCadEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliCadEntFocusGained
        if (txtCliCadEnt.getText().isEmpty()) {
            anitxtin(lblCliCadEnt);
        }
    }//GEN-LAST:event_txtCliCadEntFocusGained

    private void txtCliCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliCadEntFocusLost
        if (txtCliCadEnt.getText().isEmpty()) {
            anitxtout(lblCliCadEnt);
        }
    }//GEN-LAST:event_txtCliCadEntFocusLost

    private void txtCusGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCusGerEntFocusGained
        if (txtCusGerEnt.getText().isEmpty()) {
            anitxtin(lblCusGerEnt);
            lblR$CusGerEnt.setVisible(true);
        }
    }//GEN-LAST:event_txtCusGerEntFocusGained

    private void txtCusGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCusGerEntFocusLost
        if (txtCusGerEnt.getText().isEmpty()) {
            anitxtout(lblCusGerEnt);
            lblR$CusGerEnt.setVisible(false);
        }
    }//GEN-LAST:event_txtCusGerEntFocusLost

    private void txtCusGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCusGerEntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCusGerEntActionPerformed

    private void txtCusGerEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCusGerEntKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtPreGerEnt.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCusGerEntKeyTyped

    private void txtForGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtForGerEntFocusGained
        if (txtForGerEnt.getText().isEmpty()) {
            anitxtin(lblForGerEnt);
        }
    }//GEN-LAST:event_txtForGerEntFocusGained

    private void txtForGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtForGerEntFocusLost
        if (txtForGerEnt.getText().isEmpty()) {
            anitxtout(lblForGerEnt);
        }
    }//GEN-LAST:event_txtForGerEntFocusLost

    private void txtCliGerEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliGerEntFocusGained
        if (txtCliGerEnt.getText().isEmpty()) {
            anitxtin(lblCliGerEnt);
        }
    }//GEN-LAST:event_txtCliGerEntFocusGained

    private void txtCliGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliGerEntFocusLost
        if (txtCliGerEnt.getText().isEmpty()) {
            anitxtout(lblCliGerEnt);
        }
    }//GEN-LAST:event_txtCliGerEntFocusLost

    private void btnCanConEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanConEntActionPerformed
        pnlConEnt.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnCanConEntActionPerformed

    private void btnBusConEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusConEntActionPerformed
        try {

            if (tabelaconsultarentrada(tblConEnt, scrConEnt, formatterbanco.format(formatter.parse(txtBusConEnt.getText())))) {

                scrConEnt.setVisible(true);
                tblConEnt.setVisible(true);

            } else {

                scrConEnt.setVisible(false);
                tblConEnt.setVisible(false);

                JOptionPane.showMessageDialog(null, "Entrada não encontrada!", "Consultar", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (ParseException ex) {

            if (tabelaconsultarentrada(tblConEnt, scrConEnt, txtBusConEnt.getText())) {

                scrConEnt.setVisible(true);
                tblConEnt.setVisible(true);

            } else {

                scrConEnt.setVisible(false);
                tblConEnt.setVisible(false);

                JOptionPane.showMessageDialog(null, "Entrada não encontrada!", "Consultar", JOptionPane.INFORMATION_MESSAGE);

            }

        }
    }//GEN-LAST:event_btnBusConEntActionPerformed

    private void txtBusConEntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusConEntFocusGained
        if (txtBusConEnt.getText().isEmpty()) {
            anitxtin(lblBusConEnt);
        }
    }//GEN-LAST:event_txtBusConEntFocusGained

    private void txtBusConEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusConEntFocusLost
        if (txtBusConEnt.getText().isEmpty()) {
            anitxtout(lblBusConEnt);
        }
    }//GEN-LAST:event_txtBusConEntFocusLost

    private void txtBusConEntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusConEntKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {

                if (tabelaconsultarentrada(tblConEnt, scrConEnt, formatterbanco.format(formatter.parse(txtBusConEnt.getText())))) {

                    scrConEnt.setVisible(true);
                    tblConEnt.setVisible(true);

                } else {

                    scrConEnt.setVisible(false);
                    tblConEnt.setVisible(false);

                    JOptionPane.showMessageDialog(null, "Entrada não encontrada!", "Consultar", JOptionPane.INFORMATION_MESSAGE);

                }

            } catch (ParseException ex) {

                if (tabelaconsultarentrada(tblConEnt, scrConEnt, txtBusConEnt.getText())) {

                    scrConEnt.setVisible(true);
                    tblConEnt.setVisible(true);

                } else {

                    scrConEnt.setVisible(false);
                    tblConEnt.setVisible(false);

                    JOptionPane.showMessageDialog(null, "Entrada não encontrada!", "Consultar", JOptionPane.INFORMATION_MESSAGE);

                }

            }
        }
    }//GEN-LAST:event_txtBusConEntKeyPressed

    private void txtBusConEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusConEntKeyTyped
        btnBusConEnt.setEnabled(true);
    }//GEN-LAST:event_txtBusConEntKeyTyped

    private void btnConEntMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEntMouseEntered
        btnConEnt.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnConEntMouseEntered

    private void btnConEntMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEntMouseExited
        btnConEnt.setForeground(corforeazul);
    }//GEN-LAST:event_btnConEntMouseExited

    private void btnConEntMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEntMouseReleased
        if (!pnlConEnt.isVisible()) {

            txtBusConEnt.setText(null);

            tblConEnt.setVisible(false);
            scrConEnt.setVisible(false);

            btnBusConEnt.setEnabled(false);

            lblBusConEnt.setLocation(450, 30);

            lblTitPri.setText("Consultar Entrada");
            lblTitPri.setVisible(true);

            pnlbtn();
            pnlConEnt.setVisible(true);

        } else {

            pnlbtn();
            pnlConEnt.setVisible(true);

        }
    }//GEN-LAST:event_btnConEntMouseReleased

    private void chkCusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCusActionPerformed
        try {

            if (cmbRel.getSelectedItem().equals("Filtrar resultados")) {

                if (btnTodRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 1, null, null, 0);
                } else if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (lblDatIniRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 6, formatterbanco.format(formatter.parse(txtDatIniRel.getText())), formatterbanco.format(formatter.parse(txtDatFinRel.getText())), 0);
                }

                if (rbtnTodRel.isSelected()) {

                    if (btnTodRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 1, 1, null, null, 0);
                    } else if (btnDiaRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 1, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnSemRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 1, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnMesRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 1, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnAnoRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 1, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (lblDatIniRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 1, 6, formatterbanco.format(formatter.parse(txtDatIniRel.getText())), formatterbanco.format(formatter.parse(txtDatFinRel.getText())), 0);
                    }

                } else if (rbtnSerRel.isSelected()) {

                    if (btnTodRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 2, 1, null, null, 0);
                    } else if (btnDiaRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 2, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnSemRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 2, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnMesRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 2, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnAnoRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 2, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (lblDatIniRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 2, 6, formatterbanco.format(formatter.parse(txtDatIniRel.getText())), formatterbanco.format(formatter.parse(txtDatFinRel.getText())), 0);
                    }

                } else if (rbtnVenRel.isSelected()) {

                    if (btnTodRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 3, 1, null, null, 0);
                    } else if (btnDiaRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 3, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnSemRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 3, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnMesRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 3, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnAnoRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 3, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (lblDatIniRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 3, 6, formatterbanco.format(formatter.parse(txtDatIniRel.getText())), formatterbanco.format(formatter.parse(txtDatFinRel.getText())), 0);
                    }

                } else {

                    if (btnTodRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 5, 1, null, null, 0);
                    } else if (btnDiaRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 5, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnSemRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 5, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnMesRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 5, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (btnAnoRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 5, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                    } else if (lblDatIniRel.getFont().getSize() == 13) {
                        tabelarelatorio(tblRel, scrRel, 5, 6, formatterbanco.format(formatter.parse(txtDatIniRel.getText())), formatterbanco.format(formatter.parse(txtDatFinRel.getText())), 0);
                    }

                }

            } else {

                if (cmbRel.getItemCount() >= 2) {
                    tabelacmbrelatorio(tblRel, scrRel, cmbRel);
                }

            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_chkCusActionPerformed

    private void txtPreOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreOsFocusGained
        if (txtPreOs.getText().isEmpty()) {
            anitxtin(lblPreOs);
            lblR$Os.setVisible(true);
        }
    }//GEN-LAST:event_txtPreOsFocusGained

    private void txtPreOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPreOsFocusLost
        if (txtPreOs.getText().isEmpty()) {
            anitxtout(lblPreOs);
            lblR$Os.setVisible(false);
        }
    }//GEN-LAST:event_txtPreOsFocusLost

    private void txtPreOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPreOsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreOsActionPerformed

    private void txtPreOsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreOsKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtPreOs.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPreOsKeyTyped

    private void rbtnCreCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCreCadEntActionPerformed
        spnParCadEnt.setEnabled(true);
        lblParCadEnt.setEnabled(true);
        spnParCadEnt.setValue(1);
    }//GEN-LAST:event_rbtnCreCadEntActionPerformed

    private void rbtnDebCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnDebCadEntActionPerformed
        spnParCadEnt.setEnabled(false);
        lblParCadEnt.setEnabled(false);
        spnParCadEnt.setValue(0);
    }//GEN-LAST:event_rbtnDebCadEntActionPerformed

    private void cmbVezCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbVezCarActionPerformed

    }//GEN-LAST:event_cmbVezCarActionPerformed

    private void spnParCadEntKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spnParCadEntKeyTyped

    }//GEN-LAST:event_spnParCadEntKeyTyped

    private void spnParCadEntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spnParCadEntKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_spnParCadEntKeyPressed

    private void btnCadVenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadVenMouseEntered
        btnCadVen.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadVenMouseEntered

    private void btnCadVenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadVenMouseExited
        btnCadVen.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadVenMouseExited

    private void btnCadVenMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadVenMouseReleased
        if (!pnlCadVen.isVisible()) {

            lblCliCadVen.setLocation(400, 80);
            lblPlaCadVen.setLocation(700, 80);
            lblTelCadVen.setLocation(400, 180);
            lblAceCadVen.setLocation(400, 230);
            lblCpfCadVen.setLocation(400, 130);
            lblDatCadVen.setLocation(700, 130);
            lblVenCadVen.setLocation(700, 180);

            txtCliCadVen.setText(null);
            txtPlaCadVen.setText(null);
            txtCpfCadVen.setText(null);
            txtTelCadVen.setText(null);
            txtVenCadVen.setText(null);
            txtAceCadVen.setText(null);
            LocalDate dataAtual = LocalDate.now();
            txtDatCadVen.setText(dataAtual.format(formatteratual));
            anitxtin(lblDatCadVen);

            lblTitPri.setText("Cadastrar Plano");
            lblTitPri.setVisible(true);

            pnlbtn();
            pnlCadVen.setVisible(true);

        } else {

            pnlbtn();
            pnlCadVen.setVisible(true);

        }
    }//GEN-LAST:event_btnCadVenMouseReleased

    private void btnVenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMouseEntered
        btnVen.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnVenMouseEntered

    private void btnVenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMouseExited
        btnVen.setForeground(corforeazul);
    }//GEN-LAST:event_btnVenMouseExited

    private void btnVenMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMouseReleased
        if (!pnlVen.isVisible()) {

            if (tabelavencimento(tblVen, scrVen)) {

                planosDAO pladao = new planosDAO();

                try {
                    lblConPlaVen.setText(String.valueOf(pladao.buscar()));
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }

                lblTitPri.setText("Planos");
                lblTitPri.setVisible(true);

                btnExcVen.setEnabled(false);
                btnWppVen.setEnabled(false);
                btnAltVen.setEnabled(false);
                btnCopVen.setEnabled(false);
                btnCopAVen.setEnabled(false);

                lblBusVen.setLocation(310, 280);
                txtBusVen.setText(null);
                lblErrVen.setVisible(false);

                pnlbtn();
                pnlVen.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(null, "Sem planos. Cadastre-os primeiro!", "Planos", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {

            pnlbtn();
            pnlVen.setVisible(true);

        }
    }//GEN-LAST:event_btnVenMouseReleased

    private void btnSalCadVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalCadVenActionPerformed
        try {

            vencimento ve = new vencimento();
            vencimentoDAO vedao = new vencimentoDAO();

            if (lblTitPri.getText().equals("Cadastrar Plano")) {

                ve.setCliente(txtCliCadVen.getText());
                ve.setPlano(txtPlaCadVen.getText());
                ve.setTelefone(txtTelCadVen.getText());
                ve.setAcesso(txtAceCadVen.getText());
                ve.setCpf(txtCpfCadVen.getText());
                ve.setData(formatterbanco.format(((formatter.parse(txtDatCadVen.getText())))));
                ve.setVencimento(formatterbanco.format(((formatter.parse(txtVenCadVen.getText())))));

                if (txtPlaCadVen.getText().equals("Não Aplicável")) {
                    vedao.inserir(ve, "1");
                } else {
                    vedao.inserir(ve, "0");
                }

                JOptionPane.showMessageDialog(null, "Plano cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } else {

                ve.setCliente(txtCliCadVen.getText());
                ve.setPlano(txtPlaCadVen.getText());
                ve.setCpf(txtCpfCadVen.getText());
                ve.setTelefone(txtTelCadVen.getText());
                ve.setAcesso(txtAceCadVen.getText());
                ve.setData(formatterbanco.format(((formatter.parse(txtDatCadVen.getText())))));
                ve.setVencimento(formatterbanco.format(((formatter.parse(txtVenCadVen.getText())))));

                ve.setId(tblVen.getValueAt(tblVen.getSelectedRow(), tblVen.getColumnModel().getColumnIndex("ID")).toString());

                if (txtPlaCadVen.getText().equals("Não Aplicável")) {
                    vedao.alterar(ve, "1");
                } else {
                    vedao.alterar(ve, "0");
                }

                JOptionPane.showMessageDialog(null, "Plano alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            }

            pnlCadVen.setVisible(false);
            lblTitPri.setVisible(false);

            verificavencimento();

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSalCadVenActionPerformed

    private void btnCanCadVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanCadVenActionPerformed
        pnlCadVen.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnCanCadVenActionPerformed

    private void txtPlaCadVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlaCadVenFocusGained
        if (txtPlaCadVen.getText().isEmpty()) {
            anitxtin(lblPlaCadVen);
        }
    }//GEN-LAST:event_txtPlaCadVenFocusGained

    private void txtPlaCadVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlaCadVenFocusLost
        if (txtPlaCadVen.getText().isEmpty()) {
            anitxtout(lblPlaCadVen);
        }
    }//GEN-LAST:event_txtPlaCadVenFocusLost

    private void txtCliCadVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliCadVenFocusGained
        if (txtCliCadVen.getText().isEmpty()) {
            anitxtin(lblCliCadVen);
        }
    }//GEN-LAST:event_txtCliCadVenFocusGained

    private void txtCliCadVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCliCadVenFocusLost
        if (txtCliCadVen.getText().isEmpty()) {
            anitxtout(lblCliCadVen);
        }
    }//GEN-LAST:event_txtCliCadVenFocusLost

    private void txtTelCadVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelCadVenFocusGained
        if (txtTelCadVen.getText().isEmpty()) {
            anitxtin(lblTelCadVen);
        }
    }//GEN-LAST:event_txtTelCadVenFocusGained

    private void txtTelCadVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelCadVenFocusLost
        if (txtTelCadVen.getText().isEmpty()) {
            anitxtout(lblTelCadVen);
        }
    }//GEN-LAST:event_txtTelCadVenFocusLost

    private void txtDatCadVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatCadVenFocusGained
        if (txtDatCadVen.getText().isEmpty()) {
            anitxtin(lblDatCadVen);
        }
    }//GEN-LAST:event_txtDatCadVenFocusGained

    private void txtDatCadVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDatCadVenFocusLost
        if (txtDatCadVen.getText().isEmpty()) {
            anitxtout(lblDatCadVen);
        }
    }//GEN-LAST:event_txtDatCadVenFocusLost

    private void txtDatCadVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatCadVenKeyTyped
        if (txtDatCadVen.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtDatCadVen.getText().length() == 2) {

                    txtDatCadVen.setText(txtDatCadVen.getText() + "/");
                    txtDatCadVen.setCaretPosition(3);

                } else if (txtDatCadVen.getText().length() == 5) {

                    txtDatCadVen.setText(txtDatCadVen.getText() + "/");
                    txtDatCadVen.setCaretPosition(6);

                }

            }
            if (txtDatCadVen.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtDatCadVenKeyTyped

    private void txtTelCadVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelCadVenKeyTyped
        if (txtTelCadVen.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtTelCadVen.getText().length() == 0) {

                    txtTelCadVen.setText(txtTelCadVen.getText() + "(");
                    txtTelCadVen.setCaretPosition(1);

                } else if (txtTelCadVen.getText().length() == 3) {

                    txtTelCadVen.setText(txtTelCadVen.getText() + ") ");
                    txtTelCadVen.setCaretPosition(5);

                } else if (txtTelCadVen.getText().length() == 10) {

                    txtTelCadVen.setText(txtTelCadVen.getText() + "-");
                    txtTelCadVen.setCaretPosition(11);

                }

            }
            if (txtTelCadVen.getText().length() > 14) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtTelCadVenKeyTyped

    private void txtVenCadVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVenCadVenFocusGained
        if (txtVenCadVen.getText().isEmpty()) {
            anitxtin(lblVenCadVen);
        }
    }//GEN-LAST:event_txtVenCadVenFocusGained

    private void txtVenCadVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVenCadVenFocusLost
        if (txtVenCadVen.getText().isEmpty()) {
            anitxtout(lblVenCadVen);
        }
    }//GEN-LAST:event_txtVenCadVenFocusLost

    private void txtVenCadVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVenCadVenKeyTyped
        if (txtVenCadVen.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtVenCadVen.getText().length() == 2) {

                    txtVenCadVen.setText(txtVenCadVen.getText() + "/");
                    txtVenCadVen.setCaretPosition(3);

                } else if (txtVenCadVen.getText().length() == 5) {

                    txtVenCadVen.setText(txtVenCadVen.getText() + "/");
                    txtVenCadVen.setCaretPosition(6);

                }

            }
            if (txtVenCadVen.getText().length() > 9) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtVenCadVenKeyTyped

    private void tblVenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVenMouseClicked
        btnExcVen.setEnabled(true);
        btnWppVen.setEnabled(true);
        btnAltVen.setEnabled(true);
        btnCopVen.setEnabled(true);
        btnCopAVen.setEnabled(true);
    }//GEN-LAST:event_tblVenMouseClicked

    private void btnVolVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolVenActionPerformed
        pnlVen.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnVolVenActionPerformed

    private void btnWppVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWppVenActionPerformed
        try {

            vencimentoDAO vendao = new vencimentoDAO();

            List<String[]> listaverifica = vendao.buscarverificaplano();

            Iterator<String[]> iterator = listaverifica.iterator();

            while (iterator.hasNext()) {

                String[] item = iterator.next();

                if (!item[0].equals(tblVen.getValueAt(tblVen.getSelectedRow(), 2).toString())) {
                    iterator.remove();
                }

            }

            if (!listaverifica.isEmpty()) {

                int c = 0;
                String plano = null;

                if (listaverifica.size() != 1) {

                    Map<String, Integer> contagemItens = new HashMap<>();
                    for (String[] array : listaverifica) {
                        String item = Arrays.toString(array);
                        contagemItens.put(item, contagemItens.getOrDefault(item, 0) + 1);
                    }

                    for (Map.Entry<String, Integer> entry : contagemItens.entrySet()) {

                        String[] array = entry.getKey().substring(1, entry.getKey().length() - 1).split(", ");

                        c++;

                        if (c == 1) {//o primeiro

                            if (entry.getValue() == 1) {
                                plano = "dos seus planos *" + array[1] + "*";
                            } else {
                                plano = "dos seus *" + entry.getValue() + "* planos *" + array[1] + "*";
                            }

                        } else {

                            if (c == contagemItens.entrySet().size()) {//se for o ultimo

                                if (entry.getValue() == 1) {
                                    plano = plano + " e o *" + array[1] + "*";
                                } else {
                                    plano = plano + " e os *" + entry.getValue() + " " + array[1] + "*";
                                }
                            } else {

                                if (entry.getValue() == 1) {
                                    plano = plano + ", *" + array[1] + "*";
                                } else {
                                    plano = plano + ", dos *" + entry.getValue() + " " + array[1] + "*";
                                }

                            }

                        }

                    }

                    plano = plano + ", contratados conosco no dia *" + tblVen.getValueAt(tblVen.getSelectedRow(), 5).toString()
                            + "*.\n\n";

                } else {//quando tiver so um
                    plano = "do seu plano *" + tblVen.getValueAt(tblVen.getSelectedRow(), 4).toString() + "*"
                            + ", contratado conosco no dia *" + tblVen.getValueAt(tblVen.getSelectedRow(), 5).toString()
                            + "*.\n\n";
                }

                String texto = "*Empório Cell - TIM*\n\n"
                        + "Olá, tudo bem? Esperamos que sim!\n\n"
                        + "Estamos aqui para lembrá-lo " + plano
                        + "Traga sua família e amigos para a *rede móvel líder em cobertura no Brasil*!\n"
                        + "Para qualquer dúvida, estamos à disposição. Agradecemos por confiar em nossos serviços!";

                String msg = texto.replaceAll(" ", "%20").replaceAll("\n", "%0A");

                int resp = JOptionPane.showOptionDialog(null, texto.replaceAll("\\*", "") + "\n\nEnviar mensagem ao cliente?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                if (resp == JOptionPane.YES_OPTION) {

                    String l = "https://api.whatsapp.com/send/?phone=55" + (tblVen.getValueAt(tblVen.getSelectedRow(), 1).toString()).replaceAll("-", "").replaceAll("\\(", "").replaceAll(" ", "").replaceAll("\\)", "") + "&text=" + msg + "&app_absent=0";

                    URI link = new URI(l);

                    Desktop.getDesktop().browse(link);

                    int resp1 = JOptionPane.showOptionDialog(null, "Navegador aberto para envio!\n\nMarcar como concluído?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                    if (resp1 == JOptionPane.YES_OPTION) {

                        try {

                            vencimento ve = new vencimento();
                            vencimentoDAO vedao = new vencimentoDAO();

                            ve.setCpf(tblVen.getValueAt(tblVen.getSelectedRow(), 2).toString());

                            vedao.marcarok(ve);

                            JOptionPane.showMessageDialog(null, "Marcado com sucesso!", "Planos", JOptionPane.INFORMATION_MESSAGE);

                        } catch (SQLException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                    if (tabelavencimento(tblVen, scrVen)) {

                    } else {

                        JOptionPane.showMessageDialog(null, "Sem planos. Cadastre-os primeiro!", "Planos", JOptionPane.INFORMATION_MESSAGE);
                        pnlVen.setVisible(false);
                        lblTitPri.setVisible(false);
                    }

                    verificavencimento();

                    btnWppVen.setEnabled(false);
                    btnExcVen.setEnabled(false);
                    btnAltVen.setEnabled(false);
                    btnCopVen.setEnabled(false);
                    btnCopAVen.setEnabled(false);

                }

            } else {

                int resp = JOptionPane.showOptionDialog(null, "Atenção, mensagem de aviso indisponível para este cliente!\n\nAbrir o WhatsApp mesmo assim?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                if (resp == JOptionPane.YES_OPTION) {

                    String l = "https://api.whatsapp.com/send/?phone=55" + (tblVen.getValueAt(tblVen.getSelectedRow(), 1).toString()).replaceAll("-", "").replaceAll("\\(", "").replaceAll(" ", "").replaceAll("\\)", "");

                    URI link = new URI(l);

                    Desktop.getDesktop().browse(link);

                }

            }

        } catch (URISyntaxException | IOException | SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnWppVenActionPerformed

    private void btnExcVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcVenActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                vencimento ve = new vencimento();
                vencimentoDAO vedao = new vencimentoDAO();

                ve.setId(tblVen.getValueAt(tblVen.getSelectedRow(), 8).toString());

                vedao.excluir(ve);

                int resp2 = JOptionPane.showOptionDialog(null, "Plano excluído com sucesso! Remover do contador?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, null);

                if (resp2 == JOptionPane.YES_OPTION) {

                    planosDAO pladao = new planosDAO();

                    pladao.remover();

                    JOptionPane.showMessageDialog(null, "Plano removido do contador!", "Planos", JOptionPane.INFORMATION_MESSAGE);

                    lblConPlaVen.setText(String.valueOf(pladao.buscar()));

                }

                if (tabelavencimento(tblVen, scrVen)) {

                } else {

                    JOptionPane.showMessageDialog(null, "Sem planos. Cadastre-os primeiro!", "Planos", JOptionPane.INFORMATION_MESSAGE);
                    pnlVen.setVisible(false);
                    lblTitPri.setVisible(false);
                }

                verificavencimento();

                btnWppVen.setEnabled(false);
                btnExcVen.setEnabled(false);
                btnAltVen.setEnabled(false);
                btnCopVen.setEnabled(false);
                btnCopAVen.setEnabled(false);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnExcVenActionPerformed

    private void btnVenMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenMasActionPerformed
        if (txtNomMas.getText().isEmpty() || txtNumConMas.getText().isEmpty() || txtPlaMas.getText().isEmpty()) {

        } else {

            txtCliCadVen.setText(txtNomMas.getText());
            txtPlaCadVen.setText(txtPlaMas.getText());
            txtTelCadVen.setText(txtNumConMas.getText());
            txtCpfCadVen.setText(txtCpfMas.getText());
            txtVenCadVen.setText(txtVenMas.getText());

            if (txtNumPorMas.getText().isEmpty()) {
                txtAceCadVen.setText(txtNumAceMas.getText());
            } else {
                txtAceCadVen.setText(txtNumPorMas.getText());
            }

            LocalDate dataAtual = LocalDate.now();
            txtDatCadVen.setText(dataAtual.format(formatteratual));

            lblCliCadVen.setLocation(400, 80);
            lblPlaCadVen.setLocation(700, 80);
            lblTelCadVen.setLocation(400, 180);
            lblAceCadVen.setLocation(400, 230);
            lblCpfCadVen.setLocation(400, 130);
            lblDatCadVen.setLocation(700, 130);
            lblVenCadVen.setLocation(700, 180);

            pnlMas.setVisible(false);
            pnlCadVen.setVisible(true);
            lblTitPri.setText("Cadastrar Plano");
            lblTitPri.setVisible(true);
            anitxtin(lblCliCadVen);
            anitxtin(lblPlaCadVen);
            anitxtin(lblTelCadVen);
            anitxtin(lblDatCadVen);
            anitxtin(lblCpfCadVen);
            anitxtin(lblAceCadVen);
            anitxtin(lblVenCadVen);

        }
    }//GEN-LAST:event_btnVenMasActionPerformed

    private void btnVenPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenPriMouseEntered
        btnVenPri.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnVenPriMouseEntered

    private void btnVenPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenPriMouseExited
        btnVenPri.setForeground(corforeazul);
    }//GEN-LAST:event_btnVenPriMouseExited

    private void btnVenPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenPriMouseReleased
        if (!pnlVen.isVisible()) {

            if (tabelavencimento(tblVen, scrVen)) {

                planosDAO pladao = new planosDAO();

                try {
                    lblConPlaVen.setText(String.valueOf(pladao.buscar()));
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }

                lblTitPri.setText("Planos");
                lblTitPri.setVisible(true);

                btnExcVen.setEnabled(false);
                btnWppVen.setEnabled(false);
                btnAltVen.setEnabled(false);
                btnCopVen.setEnabled(false);
                btnCopAVen.setEnabled(false);

                lblBusVen.setLocation(310, 280);
                txtBusVen.setText(null);
                lblErrVen.setVisible(false);

                pnlbtn();
                pnlVen.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(null, "Sem planos. Cadastre-os primeiro!", "Planos", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {

            pnlbtn();
            pnlVen.setVisible(true);

        }
    }//GEN-LAST:event_btnVenPriMouseReleased

    private void txtModCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModCadEstActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModCadEstActionPerformed

    private void txtModCadEstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModCadEstKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            estoque es = new estoque();

            es.setMarca(txtMarCadEst.getText());
            es.setModelo(txtModCadEst.getText());
            es.setTipoproduto(txtTipCadEst.getText());

            if (tabelaprodutoregistrado(es, tblCadEst, scrCadEst)) {

                lblProCadEst.setVisible(true);
                tblCadEst.setVisible(true);
                scrCadEst.setVisible(true);

            } else {

                lblProCadEst.setVisible(false);
                tblCadEst.setVisible(false);
                scrCadEst.setVisible(false);

            }
        }
    }//GEN-LAST:event_txtModCadEstKeyPressed

    private void tblCadEstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCadEstMouseClicked
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

        if ("Capinha".equals(txtTipCadEst.getText())) {

            lblMarCadEst.setLocation(410, 60);
            lblModCadEst.setLocation(410, 110);
            lblQuaCadEst.setLocation(410, 160);
            lblPreCadEst.setLocation(410, 210);

            lblMatCadEst.setLocation(700, 130);
            lblLocCadEst.setLocation(700, 160);

        } else if ("Chip".equals(txtTipCadEst.getText())) {

            lblMarCadEst.setLocation(410, 80);
            lblModCadEst.setLocation(410, 130);
            lblQuaCadEst.setLocation(410, 160);
            lblPreCadEst.setLocation(410, 210);
            lblCorCadEst.setLocation(700, 80);
            lblMatCadEst.setLocation(700, 130);
            lblLocCadEst.setLocation(700, 180);
            lblDetCadEst.setLocation(700, 230);

        } else {

            lblMarCadEst.setLocation(410, 60);
            lblModCadEst.setLocation(410, 110);
            lblQuaCadEst.setLocation(410, 160);
            lblPreCadEst.setLocation(410, 210);
            lblCorCadEst.setLocation(700, 60);
            lblMatCadEst.setLocation(700, 110);
            lblLocCadEst.setLocation(700, 160);
            lblDetCadEst.setLocation(700, 210);

        }

        String preco = String.valueOf(moedadoublereal(lista1.get(tblCadEst.getSelectedRow()).getPreco()));

        txtMarCadEst.setText(lista1.get(tblCadEst.getSelectedRow()).getMarca());
        txtModCadEst.setText(lista1.get(tblCadEst.getSelectedRow()).getModelo());
        txtPreCadEst.setText(preco.substring(3, preco.length()));
        txtQuaCadEst.setText(String.valueOf(lista1.get(tblCadEst.getSelectedRow()).getQuantidade()));
        txtMatCadEst.setText(lista1.get(tblCadEst.getSelectedRow()).getMaterial());
        txtLocCadEst.setText(lista1.get(tblCadEst.getSelectedRow()).getLocalizacao());

        if (!lista1.get(tblCadEst.getSelectedRow()).getCor().isEmpty()) {

            txtCorCadEst.setText(lista1.get(tblCadEst.getSelectedRow()).getCor());
            lblCorCadEst.setLocation(700, 60);

        } else {
            lblCorCadEst.setLocation(700, 80);
        }

        if (!lista1.get(tblCadEst.getSelectedRow()).getDetalhes().isEmpty()) {

            lblDetCadEst.setLocation(700, 210);
            txtDetCadEst.setText(lista1.get(tblCadEst.getSelectedRow()).getDetalhes());

        } else {

            lblDetCadEst.setLocation(700, 230);
        }

        lblR$CadEst.setVisible(true);
    }//GEN-LAST:event_tblCadEstMouseClicked

    private void rbtnVenRel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnVenRel1ActionPerformed
        try {

            rbtnVenRel1.grabFocus();

            chkCus.setSelected(false);
            chkCus.setEnabled(false);

            if (btnTodRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 4, 1, null, null, 0);
            } else if (btnDiaRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 4, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnSemRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 4, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnMesRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 4, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (btnAnoRel.getFont().equals(fontbold(13))) {
                tabelarelatorio(tblRel, scrRel, 4, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
            } else if (!"".equals(lblDatIniRel.getText()) && !"".equals(lblDatFinRel.getText())) {
                String data1 = formatterbanco.format(formatter.parse(txtDatIniRel.getText()));
                String data2 = formatterbanco.format(formatter.parse(txtDatFinRel.getText()));
                tabelarelatorio(tblRel, scrRel, 4, 6, data1, data2, 0);
            }

            cmbRel.setSelectedIndex(0);
            cmbRel.setEnabled(false);

            if (lblResRel.isVisible()) {
                tblRel.setVisible(false);
                scrRel.setVisible(false);
                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);
                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            } else {
                tblRel.setVisible(true);
                scrRel.setVisible(true);
            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtnVenRel1ActionPerformed

    private void cmbRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRelActionPerformed
        if (cmbRel.getItemCount() >= 2) {
            tabelacmbrelatorio(tblRel, scrRel, cmbRel);
        }
    }//GEN-LAST:event_cmbRelActionPerformed

    private void txtDatFinRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatFinRelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDatFinRelActionPerformed

    private void btnAltVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltVenActionPerformed
        txtCliCadVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 0).toString());
        txtTelCadVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 1).toString());
        txtCpfCadVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 2).toString());
        txtAceCadVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 3).toString());
        txtPlaCadVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 4).toString());
        txtDatCadVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 5).toString());
        txtVenCadVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 6).toString());

        lblCli.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 0).toString());
        lblDat.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 5).toString());
        lblVen.setText(tblVen.getValueAt(tblVen.getSelectedRow(), 6).toString());

        pnlCadEst.setVisible(false);
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
        pnlConEnt.setVisible(false);
        pnlCadVen.setVisible(true);
        pnlVen.setVisible(false);

        btnMasPla.setVisible(false);
        btnDes.setVisible(false);
        btnCadDes.setVisible(false);
        btnGerDes.setVisible(false);
        btnVen.setVisible(false);
        btnCadVen.setVisible(false);
        btnJurPri.setVisible(false);

        lblCliCadVen.setLocation(400, 60);
        lblPlaCadVen.setLocation(700, 60);
        lblTelCadVen.setLocation(400, 160);
        lblAceCadVen.setLocation(400, 210);
        lblCpfCadVen.setLocation(400, 110);
        lblDatCadVen.setLocation(700, 110);
        lblVenCadVen.setLocation(700, 160);

        txtCliCadVen.setSelectionStart(0);
        txtCliCadVen.setSelectionEnd(0);
        btnCanCadVen.grabFocus();

        lblTitPri.setText("Alterar Plano");
        lblTitPri.setVisible(true);
    }//GEN-LAST:event_btnAltVenActionPerformed

    private void lblResRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblResRelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblResRelMouseEntered

    private void lblResRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblResRelMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblResRelMouseExited

    private void lblResRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblResRelMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblResRelMouseReleased

    private void txtCpfMasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfMasKeyPressed

    }//GEN-LAST:event_txtCpfMasKeyPressed

    private void txtCpfMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfMasKeyReleased
        if (txtCpfMas.getText().length() == 11 && !txtCpfMas.getText().contains(".")) {

            StringBuilder string = new StringBuilder(txtCpfMas.getText());

            string.insert(3, ".");
            string.insert(7, ".");
            string.insert(11, "-");

            txtCpfMas.setText(string.toString());

        }
    }//GEN-LAST:event_txtCpfMasKeyReleased

    private void txtNumConMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumConMasKeyReleased
        if (txtNumConMas.getText().length() == 11 && !txtNumConMas.getText().contains("(")) {

            StringBuilder string = new StringBuilder(txtNumConMas.getText());

            string.insert(0, "(");
            string.insert(3, ")");
            string.insert(4, " ");
            string.insert(10, "-");

            txtNumConMas.setText(string.toString());

        }
    }//GEN-LAST:event_txtNumConMasKeyReleased

    private void txtNumAceMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumAceMasKeyReleased
        if (txtNumAceMas.getText().length() == 11 && !txtNumAceMas.getText().contains("(")) {

            StringBuilder string = new StringBuilder(txtNumAceMas.getText());

            string.insert(0, "(");
            string.insert(3, ")");
            string.insert(4, " ");
            string.insert(10, "-");

            txtNumAceMas.setText(string.toString());

        }
    }//GEN-LAST:event_txtNumAceMasKeyReleased

    private void txtNumPorMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumPorMasKeyReleased
        if (evt.getKeyChar() == '[') {
            txtNumPorMas.setText(txtNumConMas.getText());
        } else {

            if (txtNumPorMas.getText().length() == 11 && !txtNumPorMas.getText().contains("(")) {

                StringBuilder string = new StringBuilder(txtNumPorMas.getText());

                string.insert(0, "(");
                string.insert(3, ")");
                string.insert(4, " ");
                string.insert(10, "-");

                txtNumPorMas.setText(string.toString());

            }

        }
    }//GEN-LAST:event_txtNumPorMasKeyReleased

    private void txtTelOsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelOsKeyReleased
        if (txtTelOs.getText().length() == 11 && !txtTelOs.getText().contains("(")) {

            StringBuilder string = new StringBuilder(txtTelOs.getText());

            string.insert(0, "(");
            string.insert(3, ")");
            string.insert(4, " ");
            string.insert(10, "-");

            txtTelOs.setText(string.toString());

        }
    }//GEN-LAST:event_txtTelOsKeyReleased

    private void btnTenPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTenPriMouseEntered
        btnTenPri.setForeground(new Color(19, 84, 178));
    }//GEN-LAST:event_btnTenPriMouseEntered

    private void btnTenPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTenPriMouseExited
        btnTenPri.setForeground(new Color(10, 60, 133));
    }//GEN-LAST:event_btnTenPriMouseExited

    private void btnTenPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTenPriMouseReleased
        btnTenPri.setVisible(false);
        backupdatabase();
    }//GEN-LAST:event_btnTenPriMouseReleased

    private void btnCalJurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalJurActionPerformed
        double precoini = Double.parseDouble(txtValJur.getText().replace(".", "").replace(",", "."));
        double preco = Double.parseDouble(txtValJur.getText().replace(".", "").replace(",", "."));
        double taxa = 0;

        switch ((int) spnParJur.getValue()) {
            case 1:
                preco = preco - (preco * juros(1) / 100);
                taxa = (juros(1) / 100) - 1;
                break;
            case 2:
                preco = preco - (preco * juros(2) / 100);
                taxa = (juros(2) / 100) - 1;
                break;
            case 3:
                preco = preco - (preco * juros(3) / 100);
                taxa = (juros(3) / 100) - 1;
                break;
            case 4:
                preco = preco - (preco * juros(4) / 100);
                taxa = (juros(4) / 100) - 1;
                break;
            case 5:
                preco = preco - (preco * juros(5) / 100);
                taxa = (juros(5) / 100) - 1;
                break;
            case 6:
                preco = preco - (preco * juros(6) / 100);
                taxa = (juros(6) / 100) - 1;
                break;
            case 7:
                preco = preco - (preco * juros(7) / 100);
                taxa = (juros(7) / 100) - 1;
                break;
            case 8:
                preco = preco - (preco * juros(8) / 100);
                taxa = (juros(8) / 100) - 1;
                break;
            case 9:
                preco = preco - (preco * juros(9) / 100);
                taxa = (juros(9) / 100) - 1;
                break;
            case 10:
                preco = preco - (preco * juros(10) / 100);
                taxa = (juros(10) / 100) - 1;
                break;
            case 11:
                preco = preco - (preco * juros(11) / 100);
                taxa = (juros(11) / 100) - 1;
                break;
            case 12:
                preco = preco - (preco * juros(12) / 100);
                taxa = (juros(12) / 100) - 1;
                break;
            case 0:
                preco = preco - (preco * juros(0) / 100);
                taxa = (juros(0) / 100) - 1;
                break;
            default:
                break;
        }

        lblValJurJur.setText(moedadoublereal(precoini - preco));
        lblValFinJur.setText(moedadoublereal(preco));
        lblValJur2.setText(moedadoublereal(precoini));

        if ((int) spnParJur.getValue() != 0) {

            lblValMesPreJur.setText((int) spnParJur.getValue() + "x de " + moedadoublereal((precoini / Math.abs(taxa)) / (int) spnParJur.getValue()) + " = " + moedadoublereal(precoini / Math.abs(taxa)));

        } else {

            lblValMesPreJur.setText(moedadoublereal(precoini / Math.abs(taxa)));

        }

        if ((int) spnParJur.getValue() != 0) {

            lblValParJur.setText((int) spnParJur.getValue() + "x " + moedadoublereal(precoini / (int) spnParJur.getValue()));

        } else {

            lblValParJur.setText("R$ 0,00");

        }
    }//GEN-LAST:event_btnCalJurActionPerformed

    private void txtValJurFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValJurFocusGained

        if (txtValJur.getText().isEmpty()) {
            lblR$Jur.setVisible(true);
            anitxtin(lblValJur);
        }
    }//GEN-LAST:event_txtValJurFocusGained

    private void txtValJurFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValJurFocusLost

        if (txtValJur.getText().isEmpty()) {
            lblR$Jur.setVisible(false);
            anitxtout(lblValJur);
        }
    }//GEN-LAST:event_txtValJurFocusLost

    private void lblValFinJurMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValFinJurMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValFinJurMouseEntered

    private void lblValFinJurMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValFinJurMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValFinJurMouseExited

    private void lblValFinJurMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValFinJurMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValFinJurMouseReleased

    private void lblValJurJurMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJurJurMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJurJurMouseEntered

    private void lblValJurJurMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJurJurMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJurJurMouseExited

    private void lblValJurJurMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJurJurMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJurJurMouseReleased

    private void lblValFinJur1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValFinJur1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValFinJur1MouseEntered

    private void lblValFinJur1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValFinJur1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValFinJur1MouseExited

    private void lblValFinJur1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValFinJur1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValFinJur1MouseReleased

    private void lblJurJurMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblJurJurMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblJurJurMouseEntered

    private void lblJurJurMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblJurJurMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblJurJurMouseExited

    private void lblJurJurMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblJurJurMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblJurJurMouseReleased

    private void spnParJurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spnParJurKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_spnParJurKeyPressed

    private void spnParJurKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spnParJurKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_spnParJurKeyTyped

    private void lblValJur2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJur2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJur2MouseEntered

    private void lblValJur2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJur2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJur2MouseExited

    private void lblValJur2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJur2MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJur2MouseReleased

    private void lblValJur3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJur3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJur3MouseEntered

    private void lblValJur3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJur3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJur3MouseExited

    private void lblValJur3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValJur3MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValJur3MouseReleased

    private void btnJurPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJurPriMouseEntered
        btnJurPri.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnJurPriMouseEntered

    private void btnJurPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJurPriMouseExited
        btnJurPri.setForeground(corforeazul);
    }//GEN-LAST:event_btnJurPriMouseExited

    private void btnJurPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJurPriMouseReleased
        if (!pnlJur.isVisible()) {

            txtValJur.setText(null);

            lblValFinJur.setText("R$ 0,00");
            lblValJur2.setText("R$ 0,00");
            lblValJurJur.setText("R$ 0,00");
            lblValParJur.setText("R$ 0,00");
            lblValMesPreJur.setText("R$ 0,00");
            lblR$Jur.setVisible(false);
            lblValJur.setLocation(200, 100);
            spnParJur.setValue(1);

            lblR$Jur.setVisible(false);

            lblTitPri.setText("Calcular Juros");
            lblTitPri.setVisible(true);

            pnlbtn();
            pnlJur.setVisible(true);

        } else {

            pnlbtn();
            pnlJur.setVisible(true);

        }
    }//GEN-LAST:event_btnJurPriMouseReleased

    private void txtValJurKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValJurKeyTyped
        if (evt.getKeyChar() == 44) {
            if (txtValJur.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtValJurKeyTyped

    private void btnVolJurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolJurActionPerformed
        pnlJur.setVisible(false);
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnVolJurActionPerformed

    private void txtTelCadVenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelCadVenKeyReleased
        if (txtTelCadVen.getText().length() == 11 && !txtTelCadVen.getText().contains("(")) {

            StringBuilder string = new StringBuilder(txtTelCadVen.getText());

            string.insert(0, "(");
            string.insert(3, ")");
            string.insert(4, " ");
            string.insert(10, "-");

            txtTelCadVen.setText(string.toString());

        }
    }//GEN-LAST:event_txtTelCadVenKeyReleased

    private void lblValParJurMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValParJurMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValParJurMouseEntered

    private void lblValParJurMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValParJurMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValParJurMouseExited

    private void lblValParJurMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValParJurMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValParJurMouseReleased

    private void lblValParJur1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValParJur1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValParJur1MouseEntered

    private void lblValParJur1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValParJur1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValParJur1MouseExited

    private void lblValParJur1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValParJur1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValParJur1MouseReleased

    private void lblJurJur1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblJurJur1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblJurJur1MouseEntered

    private void lblJurJur1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblJurJur1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblJurJur1MouseExited

    private void lblJurJur1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblJurJur1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblJurJur1MouseReleased

    private void lblValMesPreJurMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValMesPreJurMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValMesPreJurMouseEntered

    private void lblValMesPreJurMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValMesPreJurMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValMesPreJurMouseExited

    private void lblValMesPreJurMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblValMesPreJurMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValMesPreJurMouseReleased

    private void txtCpfCadVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCpfCadVenFocusGained
        if (txtCpfCadVen.getText().isEmpty()) {
            anitxtin(lblCpfCadVen);
        }
    }//GEN-LAST:event_txtCpfCadVenFocusGained

    private void txtCpfCadVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCpfCadVenFocusLost
        if (txtCpfCadVen.getText().isEmpty()) {
            anitxtout(lblCpfCadVen);
        }
    }//GEN-LAST:event_txtCpfCadVenFocusLost

    private void txtCpfCadVenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfCadVenKeyPressed

    }//GEN-LAST:event_txtCpfCadVenKeyPressed

    private void txtCpfCadVenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfCadVenKeyReleased
        if (txtCpfCadVen.getText().length() == 11 && !txtCpfCadVen.getText().contains(".")) {

            StringBuilder string = new StringBuilder(txtCpfCadVen.getText());

            string.insert(3, ".");
            string.insert(7, ".");
            string.insert(11, "-");

            txtCpfCadVen.setText(string.toString());

        }
    }//GEN-LAST:event_txtCpfCadVenKeyReleased

    private void txtCpfCadVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfCadVenKeyTyped
        if (txtCpfCadVen.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtCpfCadVen.getText().length() == 3) {

                    txtCpfCadVen.setText(txtCpfCadVen.getText() + ".");
                    txtCpfCadVen.setCaretPosition(4);

                } else if (txtCpfCadVen.getText().length() == 7) {

                    txtCpfCadVen.setText(txtCpfCadVen.getText() + ".");
                    txtCpfCadVen.setCaretPosition(8);

                } else if (txtCpfCadVen.getText().length() == 11) {

                    txtCpfCadVen.setText(txtCpfCadVen.getText() + "-");
                    txtCpfCadVen.setCaretPosition(12);

                }

            }
            if (txtCpfCadVen.getText().length() > 13) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtCpfCadVenKeyTyped

    private void btnCopVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopVenActionPerformed
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        String texto = (tblVen.getValueAt(tblVen.getSelectedRow(), 2).toString().replaceAll("\\.", "")).replaceAll("-", "");

        StringSelection selecao = new StringSelection(texto);

        clipboard.setContents(selecao, null);
    }//GEN-LAST:event_btnCopVenActionPerformed

    private void txtBusVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusVenFocusGained
        if (txtBusVen.getText().isEmpty()) {
            anitxtin(lblBusVen);
        }
    }//GEN-LAST:event_txtBusVenFocusGained

    private void txtBusVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusVenFocusLost
        if (txtBusVen.getText().isEmpty()) {
            anitxtout(lblBusVen);
        }
    }//GEN-LAST:event_txtBusVenFocusLost

    private void txtBusVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusVenActionPerformed

    }//GEN-LAST:event_txtBusVenActionPerformed

    private void txtBusVenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusVenKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            vencimento ve = new vencimento();

            ve.setCliente(txtBusVen.getText());
            ve.setTelefone(txtBusVen.getText());
            ve.setAcesso(txtBusVen.getText());
            ve.setCpf(txtBusVen.getText());
            ve.setPlano(txtBusVen.getText());

            if (txtBusVen.getText().contains("/")) {

                try {
                    ve.setVencimento(formatterbanco.format(formatter.parse(txtBusVen.getText())));
                    ve.setData(formatterbanco.format(formatter.parse(txtBusVen.getText())));

                } catch (ParseException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
                lblErrVen.setVisible(false);

            }

            if (txtBusVen.getText().isEmpty()) {

                ve.setCliente("%");
                ve.setTelefone("%");
                ve.setAcesso("%");
                ve.setCpf("%");
                ve.setPlano("%");
                ve.setVencimento("%");
                ve.setData("%");

            }

            if (tabelavencimentopa(tblVen, scrVen, ve)) {
                lblErrVen.setVisible(false);
            } else {
                lblErrVen.setVisible(true);
            }

        }
    }//GEN-LAST:event_txtBusVenKeyPressed

    private void txtAceCadVenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAceCadVenFocusGained
        if (txtAceCadVen.getText().isEmpty()) {
            anitxtin(lblAceCadVen);
        }
    }//GEN-LAST:event_txtAceCadVenFocusGained

    private void txtAceCadVenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAceCadVenFocusLost
        if (txtAceCadVen.getText().isEmpty()) {
            anitxtout(lblAceCadVen);
        }
    }//GEN-LAST:event_txtAceCadVenFocusLost

    private void txtAceCadVenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAceCadVenKeyReleased
        if (txtAceCadVen.getText().length() == 11 && !txtAceCadVen.getText().contains("(")) {

            StringBuilder string = new StringBuilder(txtAceCadVen.getText());

            string.insert(0, "(");
            string.insert(3, ")");
            string.insert(4, " ");
            string.insert(10, "-");

            txtAceCadVen.setText(string.toString());

        }
    }//GEN-LAST:event_txtAceCadVenKeyReleased

    private void txtAceCadVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAceCadVenKeyTyped
        if (txtAceCadVen.getSelectedText() != null) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
            }
        } else {

            if (!Character.isDigit(evt.getKeyChar())) {

                evt.consume();

            } else {

                if (txtAceCadVen.getText().length() == 0) {

                    txtAceCadVen.setText(txtAceCadVen.getText() + "(");
                    txtAceCadVen.setCaretPosition(1);

                } else if (txtAceCadVen.getText().length() == 3) {

                    txtAceCadVen.setText(txtAceCadVen.getText() + ") ");
                    txtAceCadVen.setCaretPosition(5);

                } else if (txtAceCadVen.getText().length() == 10) {

                    txtAceCadVen.setText(txtAceCadVen.getText() + "-");
                    txtAceCadVen.setCaretPosition(11);

                }

            }
            if (txtAceCadVen.getText().length() > 14) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtAceCadVenKeyTyped

    private void btnCopAVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopAVenActionPerformed
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        String texto = (tblVen.getValueAt(tblVen.getSelectedRow(), 3).toString())
                .replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", "");

        StringSelection selecao = new StringSelection(texto);

        clipboard.setContents(selecao, null);
    }//GEN-LAST:event_btnCopAVenActionPerformed

    private void pnlIteCadEntComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pnlIteCadEntComponentShown
        if (!txtBusIteCadEnt.getText().isEmpty()) {
            lblBusIteCadEnt.setLocation(60, 70);
        }

        lblBusIteCadEnt.requestFocus();
    }//GEN-LAST:event_pnlIteCadEntComponentShown

    private void txtPlaMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlaMasKeyReleased
        switch (evt.getKeyChar()) {
            case '\'':
                txtPlaMas.setText("TIM Controle A Plus");
                break;
            case '1':
                txtPlaMas.setText("TIM Controle");
                break;
            case '2':
                txtPlaMas.setText("TIM Controle Plus");
                break;
            case '3':
                txtPlaMas.setText("TIM Controle Premium");
                break;
            case '4':
                txtPlaMas.setText("TIM Controle J Express");
                break;
            case '5':
                txtPlaMas.setText("TIM Controle L Express");
                break;
            case '6':
                txtPlaMas.setText("TIM Black");
                break;
            case '7':
                txtPlaMas.setText("TIM Black Plus");
                break;
            case '8':
                txtPlaMas.setText("TIM Black Premium");
                break;
            case '9':
                txtPlaMas.setText("TIM Black C Ultra");
                break;
            case '0':
                txtPlaMas.setText("TIM Black A Express");
                break;
            case '-':
                txtPlaMas.setText("TIM Black B Express");
                break;
            case '=':
                txtPlaMas.setText("TIM Black C Express");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_txtPlaMasKeyReleased

    private void txtPlaCadVenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlaCadVenKeyReleased
        switch (evt.getKeyChar()) {
            case '\'':
                txtPlaMas.setText("TIM Controle A Plus");
                break;
            case '1':
                txtPlaMas.setText("TIM Controle");
                break;
            case '2':
                txtPlaMas.setText("TIM Controle Plus");
                break;
            case '3':
                txtPlaMas.setText("TIM Controle Premium");
                break;
            case '4':
                txtPlaMas.setText("TIM Controle J Express");
                break;
            case '5':
                txtPlaMas.setText("TIM Controle L Express");
                break;
            case '6':
                txtPlaMas.setText("TIM Black");
                break;
            case '7':
                txtPlaMas.setText("TIM Black Plus");
                break;
            case '8':
                txtPlaMas.setText("TIM Black Premium");
                break;
            case '9':
                txtPlaMas.setText("TIM Black C Ultra");
                break;
            case '0':
                txtPlaMas.setText("TIM Black A Express");
                break;
            case '-':
                txtPlaMas.setText("TIM Black B Express");
                break;
            case '=':
                txtPlaMas.setText("TIM Black C Express");
                break;
            case '[':
                txtPlaCadVen.setText("Não Aplicável");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_txtPlaCadVenKeyReleased

    private void btnAtvGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtvGerTipSerActionPerformed
        try {

            tiposervico ts = new tiposervico();
            tiposervicoDAO tsdao = new tiposervicoDAO();

            ts.setIdtiposervico(Integer.parseInt(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 0).toString()));

            if (btnAtvGerTipSer.getText().equals("Ativar")) {
                ts.setAtv(1);
                tsdao.atvdes(ts);
            } else {
                ts.setAtv(0);
                tsdao.atvdes(ts);
            }

            tabelatiposervico();

            lblDesGerTipSer.setLocation(510, 240);
            lblDesGerTipSer.setEnabled(false);
            txtDesGerTipSer.setEnabled(false);
            sepDesGerTipSer.setForeground(Color.GRAY);

            txtDesGerTipSer.setText(null);

            btnExcGerTipSer.setEnabled(false);
            btnAltGerTipSer.setEnabled(false);
            btnAtvGerTipSer.setEnabled(false);
            btnAtvGerTipSer.setText("Desativar");

            rbtnTimGerTipSer.setEnabled(false);
            rbtnAssGerTipSer.setEnabled(false);
            rbtnOutGerTipSer.setEnabled(false);
            btnGroup.clearSelection();

            txtDesGerTipSer.requestFocus();

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAtvGerTipSerActionPerformed

    private void tblTipSerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTipSerMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTipSerMouseEntered

    private void rbtnOutGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnOutGerTipSerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnOutGerTipSerActionPerformed

    private void rbtnAssGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAssGerTipSerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnAssGerTipSerActionPerformed

    private void rbtnTimGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnTimGerTipSerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnTimGerTipSerActionPerformed

    private void txtBusVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusVenKeyTyped

    }//GEN-LAST:event_txtBusVenKeyTyped

    private void rbtnCapIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCapIteGerEntActionPerformed
        txtBusIteGerEnt.setEnabled(true);
        lblBusIteGerEnt.setEnabled(true);
        sepBusIteCadEnt1.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnCapIteGerEntActionPerformed

    private void rbtnPelIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPelIteGerEntActionPerformed
        txtBusIteGerEnt.setEnabled(true);
        lblBusIteGerEnt.setEnabled(true);
        sepBusIteCadEnt1.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnPelIteGerEntActionPerformed

    private void rbtnChiIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnChiIteGerEntActionPerformed
        txtBusIteGerEnt.setEnabled(true);
        lblBusIteGerEnt.setEnabled(true);
        sepBusIteCadEnt1.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnChiIteGerEntActionPerformed

    private void rbtnAssIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnAssIteGerEntActionPerformed
        txtBusIteGerEnt.setEnabled(true);
        lblBusIteGerEnt.setEnabled(true);
        sepBusIteCadEnt1.setForeground(corforeazul);
    }//GEN-LAST:event_rbtnAssIteGerEntActionPerformed

    private void btnMenDiaRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenDiaRelMouseEntered
        btnMenDiaRel.setForeground(new Color(255, 153, 153));
    }//GEN-LAST:event_btnMenDiaRelMouseEntered

    private void btnMenDiaRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenDiaRelMouseExited
        btnMenDiaRel.setForeground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnMenDiaRelMouseExited

    private void btnMenDiaRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenDiaRelMouseReleased
        if (btnMenDiaRel.isEnabled()) {

            int opc2 = Integer.parseInt(btnNumDiaRel.getText()) - 1;

            btnMaiDiaRel.setEnabled(true);

            if (opc2 == 0) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("Este dia");
                } else if (btnSemRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("Esta semana");
                } else if (btnMesRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("Este mês");
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("Este ano");
                }

                btnMaiDiaRel.setLocation(85, 65);
                btnMenDiaRel.setLocation(60, 65);
                lblDiaRel.setLocation(60, 85);

                btnNumDiaRel.setVisible(false);
                btnMenDiaRel.setEnabled(false);

            } else {

                btnNumDiaRel.setVisible(true);

                if (btnDiaRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("dia(s) atrás");
                } else if (btnSemRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("semana(s) atrás");
                } else if (btnMesRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("mês(es) atrás");
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("ano(s) atrás");
                }

            }

            btnNumDiaRel.setText(String.valueOf(opc2));

            lblDiaRel.setVisible(true);

//          -------
            if (rbtnTodRel.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else if (rbtnSerRel.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else if (rbtnVenRel.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else if (rbtnVenRel1.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            }

            lblDatFinRel.setLocation(430, 150);
            lblDatIniRel.setLocation(290, 150);

            if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
                if (rbtnVenRel.isSelected()) {
                    cmbrelatorio(tblRel, cmbRel, 2);
                } else {
                    cmbrelatorio(tblRel, cmbRel, 1);
                }
            } else {
                cmbRel.setSelectedIndex(0);
                cmbRel.setEnabled(false);
            }

            if (lblResRel.isVisible()) {
                tblRel.setVisible(false);
                scrRel.setVisible(false);
                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);
                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");

            } else {
                tblRel.setVisible(true);
                scrRel.setVisible(true);
            }

            btnDiaRel.grabFocus();

            btnMaiDiaRel.setVisible(true);

        }
    }//GEN-LAST:event_btnMenDiaRelMouseReleased

    private void btnMaiDiaRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaiDiaRelMouseEntered
        btnMaiDiaRel.setForeground(new Color(0, 230, 0));
    }//GEN-LAST:event_btnMaiDiaRelMouseEntered

    private void btnMaiDiaRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaiDiaRelMouseExited
        btnMaiDiaRel.setForeground(new Color(51, 204, 0));
    }//GEN-LAST:event_btnMaiDiaRelMouseExited

    private void btnMaiDiaRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaiDiaRelMouseReleased
        if (btnMaiDiaRel.isEnabled()) {

            int opc2 = Integer.parseInt(btnNumDiaRel.getText()) + 1;

            btnMenDiaRel.setEnabled(true);

            if (opc2 >= 60) {

                btnMaiDiaRel.setEnabled(false);

            } else {

                btnNumDiaRel.setVisible(true);

                lblDiaRel.setLocation(60, 95);
                btnMaiDiaRel.setLocation(85, 55);
                btnMenDiaRel.setLocation(60, 55);

                if (btnDiaRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("dia(s) atrás");
                } else if (btnSemRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("semana(s) atrás");
                } else if (btnMesRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("mês(es) atrás");
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    lblDiaRel.setText("ano(s) atrás");
                }

            }

            btnNumDiaRel.setText(String.valueOf(opc2));

            lblDiaRel.setVisible(true);

//          -------       
            if (rbtnTodRel.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 1, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else if (rbtnSerRel.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 2, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else if (rbtnVenRel.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 3, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else if (rbtnVenRel1.isSelected()) {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 4, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            } else {

                if (btnDiaRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 2, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnSemRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 3, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnMesRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 4, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                } else if (btnAnoRel.getFont().getSize() == 13) {
                    tabelarelatorio(tblRel, scrRel, 5, 5, null, null, Integer.parseInt(btnNumDiaRel.getText()));
                }

            }

            lblDatFinRel.setLocation(430, 150);
            lblDatIniRel.setLocation(290, 150);

            if (!(rbtnTodRel.isSelected() || rbtnVenRel1.isSelected())) {
                if (rbtnVenRel.isSelected()) {
                    cmbrelatorio(tblRel, cmbRel, 2);
                } else {
                    cmbrelatorio(tblRel, cmbRel, 1);
                }
            } else {
                cmbRel.setSelectedIndex(0);
                cmbRel.setEnabled(false);
            }

            if (lblResRel.isVisible()) {
                tblRel.setVisible(false);
                scrRel.setVisible(false);
                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);
                lblValTotRel.setText("R$ 0,00");
                lblValMedRel.setText("R$ 0,00");
                lblTotEntRel.setText("0");

                lblValDinRel.setText("R$ 0,00");
                lblValCarRel.setText("R$ 0,00");
                lblValPixRel.setText("R$ 0,00");
            } else {
                tblRel.setVisible(true);
                scrRel.setVisible(true);
            }

            btnDiaRel.grabFocus();

            btnMenDiaRel.setEnabled(true);

        }
    }//GEN-LAST:event_btnMaiDiaRelMouseReleased

    private void btnNumDiaRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumDiaRelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNumDiaRelMouseEntered

    private void btnNumDiaRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumDiaRelMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNumDiaRelMouseExited

    private void btnNumDiaRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNumDiaRelMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNumDiaRelMouseReleased

    private void lblDiaRelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDiaRelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblDiaRelMouseEntered

    private void lblDiaRelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDiaRelMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_lblDiaRelMouseExited

    private void lblDiaRelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDiaRelMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblDiaRelMouseReleased

    private void chkGarOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGarOsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkGarOsActionPerformed

    private void tblOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOsMouseClicked
        btnGerGerOs.setEnabled(true);
        btnExcGerOs.setEnabled(true);
        btnAltGerOs.setEnabled(true);
    }//GEN-LAST:event_tblOsMouseClicked

    private void btnAltGerOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerOsActionPerformed
        String preco = tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Preço")).toString();
        String datasaida = tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Saída")).toString();

        txtCliOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Nome")).toString());
        txtEndOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Endereço")).toString());
        txtTelOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Telefone")).toString());
        txtEquOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Equipamento")).toString());
        txtMarOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Marca")).toString());
        txtModOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Modelo")).toString());
        txtRepOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Reparo")).toString());
        txtDefOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Defeito")).toString());
        txtDatOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Entrada")).toString());

        if (datasaida.equals("Não Aplicável")) {
            txtDatSaiOs.setText(null);
            lblHorOs.setLocation(370, 230);
        } else {
            txtDatSaiOs.setText(datasaida);
            lblHorOs.setLocation(370, 210);
        }

        txtPreOs.setText(preco.substring(3, preco.length()));

        if (tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Garantia")).toString().equals("Sim")) {
            chkGarOs.setSelected(true);
        } else {
            chkGarOs.setSelected(false);
        }

        lblCliOs.setLocation(370, 10);
        lblTelOs.setLocation(370, 60);
        lblEndOs.setLocation(370, 110);
        lblDatEntOs.setLocation(370, 160);
        lblEquOs.setLocation(700, 10);
        lblMarOs.setLocation(700, 60);
        lblModOs.setLocation(700, 110);
        lblConOs.setLocation(700, 160);
        lblDefOs.setLocation(700, 210);
        lblPreOs.setLocation(370, 260);

        lblR$Os.setVisible(true);

        lblTitPri.setText("Alterar OS");

        btnGerOs.setText("Alterar");

        pnlbtn();
        pnlOs.setVisible(true);

    }//GEN-LAST:event_btnAltGerOsActionPerformed

    private void btnGerGerOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerGerOsActionPerformed
        try {

            int resp2 = JOptionPane.showOptionDialog(null, "Quantas vias?", "OS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"1 Via", "2 Vias"}, null);

            if (resp2 != JOptionPane.CLOSED_OPTION) {

                String datagarantia;

                String datasaida = tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Saída")).toString();

                if (tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Garantia")).toString().equals("Sim")) {

                    Date data = formatter.parse(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Saída")).toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(data);
                    calendar.add(Calendar.MONTH, 3);
                    Date novaData = calendar.getTime();
                    datagarantia = formatter.format(novaData);

                } else {

                    datagarantia = "Não Aplicável";

                }

                Map<String, Object> parameters = new HashMap<>();

                parameters.clear();

                parameters.put("LogoTim", getClass().getResourceAsStream("/images/LOGOTIM.png"));
                parameters.put("LogoLoja", getClass().getResourceAsStream("/images/LogoLoja580.png"));
                parameters.put("Nome", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Nome")).toString());
                parameters.put("Endereco", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Endereço")).toString());
                parameters.put("Telefone", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Telefone")).toString());
                parameters.put("Equipamento", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Equipamento")).toString());
                parameters.put("Marca", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Marca")).toString());
                parameters.put("Modelo", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Modelo")).toString());
                parameters.put("Condicoes", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Defeito")).toString());
                parameters.put("Defeito", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Reparo")).toString());
                parameters.put("DataEntrada", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Entrada")).toString());
                parameters.put("DataSaida", (!datasaida.equals("Não Aplicável")) ? datasaida : "____/____/________");
                parameters.put("Garantia", datagarantia);
                parameters.put("Preco", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Preço")).toString());

                parameters.put("LogoTim2", getClass().getResourceAsStream("/images/LOGOTIM.png"));
                parameters.put("LogoLoja2", getClass().getResourceAsStream("/images/LogoLoja580.png"));
                parameters.put("Nome2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Nome")).toString());
                parameters.put("Endereco2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Endereço")).toString());
                parameters.put("Telefone2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Telefone")).toString());
                parameters.put("Equipamento2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Equipamento")).toString());
                parameters.put("Marca2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Marca")).toString());
                parameters.put("Modelo2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Modelo")).toString());
                parameters.put("Condicoes2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Defeito")).toString());
                parameters.put("Defeito2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Reparo")).toString());
                parameters.put("DataEntrada2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Entrada")).toString());
                parameters.put("DataSaida2", (!datasaida.equals("Não Aplicável")) ? datasaida : "____/____/________");
                parameters.put("Garantia2", datagarantia);
                parameters.put("Preco2", tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Preço")).toString());

                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("os/OSEmpSysView2.jasper");

                if (resp2 == JOptionPane.YES_OPTION) {
                    inputStream = getClass().getClassLoader().getResourceAsStream("os/OSEmpSysView1.jasper");
                    System.out.print("wdwd");
                }

                JasperPrint print = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource(1));

                JasperViewer jc = new JasperViewer(print, false);
                jc.setVisible(true);
                jc.toFront();

                tabelaos(tblOs, scrOs);

                btnGerGerOs.setEnabled(false);
                btnExcGerOs.setEnabled(false);
                btnAltGerOs.setEnabled(false);

            }

        } catch (JRException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGerGerOsActionPerformed

    private void btnExcGerOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerOsActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                os os = new os();
                osDAO osdao = new osDAO();

                os.setId(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("ID")).toString());

                osdao.excluir(os);

                JOptionPane.showMessageDialog(null, "OS excluída com sucesso!", "OS", JOptionPane.INFORMATION_MESSAGE);

                if (tabelaos(tblOs, scrOs)) {

                } else {

                    JOptionPane.showMessageDialog(null, "Sem OS. Cadastre-as primeiro!", "OS", JOptionPane.INFORMATION_MESSAGE);
                    pnlGerOs.setVisible(false);
                    lblTitPri.setVisible(false);
                }

                btnGerGerOs.setEnabled(false);
                btnExcGerOs.setEnabled(false);
                btnAltGerOs.setEnabled(false);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnExcGerOsActionPerformed

    private void btnVolGerOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolGerOsActionPerformed
        pnlbtn();
        lblTitPri.setVisible(false);
    }//GEN-LAST:event_btnVolGerOsActionPerformed

    private void txtBusGerOsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusGerOsFocusGained
        if (txtBusGerOs.getText().isEmpty()) {
            anitxtin(lblBusGerOs);
        }
    }//GEN-LAST:event_txtBusGerOsFocusGained

    private void txtBusGerOsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusGerOsFocusLost
        if (txtBusGerOs.getText().isEmpty()) {
            anitxtout(lblBusGerOs);
        }
    }//GEN-LAST:event_txtBusGerOsFocusLost

    private void txtBusGerOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusGerOsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusGerOsActionPerformed

    private void txtBusGerOsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusGerOsKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            os os = new os();

            os.setCliente(txtBusGerOs.getText());
            os.setTelefone(txtBusGerOs.getText());
            os.setEndereco(txtBusGerOs.getText());
            os.setEquipamento(txtBusGerOs.getText());
            os.setMarca(txtBusGerOs.getText());
            os.setModelo(txtBusGerOs.getText());
            os.setDefeito(txtBusGerOs.getText());
            os.setReparo(txtBusGerOs.getText());
            os.setPreco(txtBusGerOs.getText());
            os.setDataentrada(txtBusGerOs.getText());
            os.setDatasaida(txtBusGerOs.getText());
            os.setGarantia(txtBusGerOs.getText());
            os.setId(txtBusGerOs.getText());

            if (txtBusGerOs.getText().isEmpty()) {

                os.setCliente("%");
                os.setTelefone("%");
                os.setEndereco("%");
                os.setEquipamento("%");
                os.setMarca("%");
                os.setModelo("%");
                os.setDefeito("%");
                os.setReparo("%");
                os.setPreco("%");
                os.setDataentrada("%");
                os.setDatasaida("%");
                os.setGarantia("%");
                os.setId("%");

            }

            if (tabelaospa(tblOs, scrOs, os)) {
                lblErrGerOs.setVisible(false);
            } else {
                lblErrGerOs.setVisible(true);
            }

        }
    }//GEN-LAST:event_txtBusGerOsKeyPressed

    private void txtBusGerOsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusGerOsKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusGerOsKeyTyped

    private void btnGerOsPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerOsPriMouseEntered
        btnGerOsPri.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerOsPriMouseEntered

    private void btnGerOsPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerOsPriMouseExited
        btnGerOsPri.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerOsPriMouseExited

    private void btnGerOsPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerOsPriMouseReleased
        if (!pnlGerOs.isVisible()) {

            if (tabelaos(tblOs, scrOs)) {

                lblTitPri.setText("Gerenciar OS");
                lblTitPri.setVisible(true);

                btnExcGerOs.setEnabled(false);
                btnGerGerOs.setEnabled(false);
                btnAltGerOs.setEnabled(false);

                lblBusGerOs.setLocation(310, 300);
                txtBusGerOs.setText(null);
                lblErrGerOs.setVisible(false);

                pnlbtn();
                pnlGerOs.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(null, "Sem OS. Cadastre-as primeiro!", "OS", JOptionPane.INFORMATION_MESSAGE);

            }

            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);

        } else {
            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);
        }
    }//GEN-LAST:event_btnGerOsPriMouseReleased

    private void btnVenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVenMousePressed

    private void btnOrdSerPriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOrdSerPriMouseClicked

    }//GEN-LAST:event_btnOrdSerPriMouseClicked

    private void btnCadOsPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadOsPriMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCadOsPriMouseEntered

    private void btnCadOsPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadOsPriMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCadOsPriMouseExited

    private void btnCadOsPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadOsPriMouseReleased
        if (!pnlOs.isVisible()) {

            txtCliOs.setText(null);
            txtEndOs.setText(null);
            txtTelOs.setText(null);
            txtEquOs.setText(null);
            txtMarOs.setText(null);
            txtModOs.setText(null);
            txtDefOs.setText(null);
            txtRepOs.setText(null);
            txtPreOs.setText(null);
            txtDatOs.setText(null);
            txtDatSaiOs.setText(null);
            chkGarOs.setSelected(false);

            lblCliOs.setLocation(370, 30);
            lblTelOs.setLocation(370, 80);
            lblEndOs.setLocation(370, 130);
            lblDatEntOs.setLocation(370, 180);
            lblHorOs.setLocation(370, 230);
            lblEquOs.setLocation(700, 30);
            lblMarOs.setLocation(700, 80);
            lblModOs.setLocation(700, 130);
            lblConOs.setLocation(700, 180);
            lblDefOs.setLocation(700, 230);
            lblPreOs.setLocation(370, 280);

            lblR$Os.setVisible(false);

            lblTitPri.setText("Ordem de Serviço");
            lblTitPri.setVisible(true);

            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);

            btnGerOs.setText("Gerar");

            pnlbtn();
            pnlOs.setVisible(true);

        } else {

            btnCadOsPri.setVisible(false);
            btnGerOsPri.setVisible(false);

        }
    }//GEN-LAST:event_btnCadOsPriMouseReleased

    private void txtNomMasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomMasKeyReleased
        if ((evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) || (Character.isLetter(evt.getKeyChar()))) {
            String capitalizedText = capitalizeFirstLetterOfEachWord(txtNomMas.getText());
            txtNomMas.setText(capitalizedText);
        }
    }//GEN-LAST:event_txtNomMasKeyReleased

    private void txtCliCadVenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliCadVenKeyReleased
        if ((evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) || (Character.isLetter(evt.getKeyChar()))) {
            String capitalizedText = capitalizeFirstLetterOfEachWord(txtCliCadVen.getText());
            txtCliCadVen.setText(capitalizedText);
        }
    }//GEN-LAST:event_txtCliCadVenKeyReleased

    private void btnParMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParMasActionPerformed
        planosdiaDAO pdDAO = new planosdiaDAO();

        Date dataa = new Date();
        String data = new SimpleDateFormat("dd/MM").format(dataa);

        try {

            String con = String.valueOf(pdDAO.buscar(1));
            String pos = String.valueOf(pdDAO.buscar(2));
            String troca = String.valueOf(pdDAO.buscar(3));

            txtAreMas.setText(
                    "*PARCIAL DO DIA " + data + "*\n\n"
                    + "Plano Controle: " + con
                    + "\nPlano Black: " + pos
                    + "\nTroca de chip: " + troca
            );

            txtAreMas.setCaretPosition(0);

            btnCopMas.setVisible(true);

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnParMasActionPerformed

    private void btnConMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConMasActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha um arquivo PDF");

        // Definindo o diretório de Downloads
        String userHome = System.getProperty("user.home");
        File downloadsFolder = new File(userHome, "Downloads");
        fileChooser.setCurrentDirectory(downloadsFolder);

        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {

                PDDocument document = Loader.loadPDF(selectedFile);

                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300); // Renderiza a primeira página com 300 DPI

                // Copia a imagem para a área de transferência
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                ImageTransferable transferable = new ImageTransferable(image);
                clipboard.setContents(transferable, null);

                document.close();
                JOptionPane.showMessageDialog(this, "Contrato copiado!", "Máscara", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao abrir o PDF: " + ex.getMessage());
            }
        }
    }

// Classe auxiliar para transferir a imagem para a área de transferência
    class ImageTransferable implements Transferable {

        private final BufferedImage image;

        public ImageTransferable(BufferedImage image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }


    }//GEN-LAST:event_btnConMasActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            new main().setVisible(false);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltGerDes;
    private javax.swing.JButton btnAltGerEnt;
    private javax.swing.JButton btnAltGerEst;
    private javax.swing.JButton btnAltGerOs;
    private javax.swing.JButton btnAltGerTipSer;
    private javax.swing.JButton btnAltVen;
    private javax.swing.JLabel btnAnoRel;
    private javax.swing.JButton btnAtvGerTipSer;
    private javax.swing.JButton btnBusConEnt;
    private javax.swing.JButton btnBusConEst;
    private javax.swing.JButton btnBusGerEnt;
    private javax.swing.JButton btnBusGerEst;
    private javax.swing.JLabel btnCadDes;
    private javax.swing.JLabel btnCadEnt;
    private javax.swing.JLabel btnCadEst;
    private javax.swing.JLabel btnCadOsPri;
    private javax.swing.JLabel btnCadTipSer;
    private javax.swing.JLabel btnCadVen;
    private javax.swing.JButton btnCalJur;
    private javax.swing.JButton btnCanCadEnt;
    private javax.swing.JButton btnCanCadEst;
    private javax.swing.JButton btnCanCadVen;
    private javax.swing.JButton btnCanConEnt;
    private javax.swing.JButton btnCanConEst;
    private javax.swing.JButton btnCanDes;
    private javax.swing.JButton btnCanGerDes;
    private javax.swing.JButton btnCanGerEnt;
    private javax.swing.JButton btnCanGerEst;
    private javax.swing.JButton btnCanGerTipSer;
    private javax.swing.JButton btnCanMas;
    private javax.swing.JButton btnCanOs;
    private javax.swing.JButton btnCanTipSer;
    private javax.swing.JLabel btnConEnt;
    private javax.swing.JLabel btnConEst;
    private javax.swing.JButton btnConMas;
    private javax.swing.JButton btnCopAVen;
    private javax.swing.JLabel btnCopMas;
    private javax.swing.JButton btnCopVen;
    private javax.swing.JLabel btnDes;
    private javax.swing.JLabel btnDiaRel;
    private javax.swing.JLabel btnEntPri;
    private javax.swing.JLabel btnEstPri;
    private javax.swing.JButton btnExcGerDes;
    private javax.swing.JButton btnExcGerEnt;
    private javax.swing.JButton btnExcGerEst;
    private javax.swing.JButton btnExcGerOs;
    private javax.swing.JButton btnExcGerTipSer;
    private javax.swing.JButton btnExcVen;
    private javax.swing.JLabel btnGerDes;
    private javax.swing.JLabel btnGerEnt;
    private javax.swing.JLabel btnGerEst;
    private javax.swing.JButton btnGerGerOs;
    private javax.swing.JButton btnGerMas;
    private javax.swing.JButton btnGerOs;
    private javax.swing.JLabel btnGerOsPri;
    private javax.swing.JLabel btnGerTipSer;
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.ButtonGroup btnGroup1;
    private javax.swing.ButtonGroup btnGroup2;
    private javax.swing.ButtonGroup btnGroup3;
    private javax.swing.ButtonGroup btnGroup4;
    private javax.swing.JButton btnIteCadEnt;
    private javax.swing.JButton btnIteGerEnt;
    private javax.swing.JLabel btnJurPri;
    private javax.swing.JLabel btnMaiDiaRel;
    private javax.swing.JLabel btnMasPla;
    private javax.swing.JLabel btnMenDiaRel;
    private javax.swing.JLabel btnMesRel;
    private javax.swing.JLabel btnNumDiaRel;
    private javax.swing.JLabel btnOrdSerPri;
    private javax.swing.JLabel btnOutPri;
    private javax.swing.JButton btnParMas;
    private javax.swing.JLabel btnRecBan;
    private javax.swing.JLabel btnRelPri;
    private javax.swing.JButton btnSalCadEnt;
    private javax.swing.JButton btnSalCadEst;
    private javax.swing.JButton btnSalCadVen;
    private javax.swing.JButton btnSalDes;
    private javax.swing.JButton btnSalTipSer;
    private javax.swing.JLabel btnSemRel;
    private javax.swing.JLabel btnTenPri;
    private javax.swing.JLabel btnTipSerPri;
    private javax.swing.JLabel btnTodRel;
    private javax.swing.JLabel btnVen;
    private javax.swing.JButton btnVenMas;
    private javax.swing.JLabel btnVenPri;
    private javax.swing.JButton btnVolDes;
    private javax.swing.JButton btnVolGerOs;
    private javax.swing.JButton btnVolIteCadEnt;
    private javax.swing.JButton btnVolIteGerEnt;
    private javax.swing.JButton btnVolJur;
    private javax.swing.JButton btnVolRel;
    private javax.swing.JButton btnVolVen;
    private javax.swing.JButton btnWppVen;
    private javax.swing.JCheckBox chkAppMas;
    private javax.swing.JRadioButton chkBolMas;
    private javax.swing.JCheckBox chkC6Mas;
    private javax.swing.JRadioButton chkCarMas;
    private javax.swing.JCheckBox chkCarMasa;
    private javax.swing.JCheckBox chkCus;
    private javax.swing.JRadioButton chkDebMas;
    private javax.swing.JCheckBox chkDebMasa;
    private javax.swing.JCheckBox chkGarOs;
    private javax.swing.JCheckBox chkMelMas;
    private javax.swing.JComboBox<String> cmbChiCadEst;
    private javax.swing.JComboBox<String> cmbChiGerEst;
    private javax.swing.JComboBox<String> cmbRel;
    private javax.swing.JComboBox<String> cmbSerGerEnt;
    private javax.swing.JComboBox<String> cmbVezCar;
    private javax.swing.JLabel imgLogo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAceCadVen;
    private javax.swing.JLabel lblBakPri;
    private javax.swing.JLabel lblBusConEnt;
    private javax.swing.JLabel lblBusConEst;
    private javax.swing.JLabel lblBusGerEst;
    private javax.swing.JLabel lblBusGerOs;
    private javax.swing.JLabel lblBusIteCadEnt;
    private javax.swing.JLabel lblBusIteGerEnt;
    private javax.swing.JLabel lblBusVen;
    private javax.swing.JLabel lblBusVen2;
    private javax.swing.JLabel lblChiCadEst;
    private javax.swing.JLabel lblChiGerEst;
    private javax.swing.JLabel lblCli;
    private javax.swing.JLabel lblCliCadEnt;
    private javax.swing.JLabel lblCliCadVen;
    private javax.swing.JLabel lblCliGerEnt;
    private javax.swing.JLabel lblCliOs;
    private javax.swing.JLabel lblConOs;
    private javax.swing.JLabel lblConPlaVen;
    private javax.swing.JLabel lblCorCadEst;
    private javax.swing.JLabel lblCorGerEst;
    private javax.swing.JLabel lblCpfCadVen;
    private javax.swing.JLabel lblCpfMas;
    private javax.swing.JLabel lblCusCadEnt;
    private javax.swing.JLabel lblCusGerEnt;
    private javax.swing.JLabel lblDat;
    private javax.swing.JLabel lblDatBusGerEnt;
    private javax.swing.JLabel lblDatCadEnt;
    private javax.swing.JLabel lblDatCadVen;
    private javax.swing.JLabel lblDatDes;
    private javax.swing.JLabel lblDatEntOs;
    private javax.swing.JLabel lblDatFinRel;
    private javax.swing.JLabel lblDatGerDes;
    private javax.swing.JLabel lblDatGerEnt;
    private javax.swing.JLabel lblDatIniRel;
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
    private javax.swing.JLabel lblDiaRel;
    private javax.swing.JLabel lblEndOs;
    private javax.swing.JLabel lblEquOs;
    private javax.swing.JLabel lblErrGerOs;
    private javax.swing.JLabel lblErrVen;
    private javax.swing.JLabel lblEstIteCadEnt;
    private javax.swing.JLabel lblEstIteGerEnt;
    private javax.swing.JLabel lblForCadEnt;
    private javax.swing.JLabel lblForGerEnt;
    private javax.swing.JLabel lblHorOs;
    private javax.swing.JLabel lblJurJur;
    private javax.swing.JLabel lblJurJur1;
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
    private javax.swing.JLabel lblParCadEnt;
    private javax.swing.JLabel lblParJur;
    private javax.swing.JLabel lblPlaCadVen;
    private javax.swing.JLabel lblPlaMas;
    private javax.swing.JLabel lblPreCadEnt;
    private javax.swing.JLabel lblPreCadEst;
    private javax.swing.JLabel lblPreDes;
    private javax.swing.JLabel lblPreGerDes;
    private javax.swing.JLabel lblPreGerEnt;
    private javax.swing.JLabel lblPreGerEst;
    private javax.swing.JLabel lblPreOs;
    private javax.swing.JLabel lblProCadEst;
    private javax.swing.JLabel lblQuaCadEst;
    private javax.swing.JLabel lblQuaGerEst;
    private javax.swing.JLabel lblR$CadEnt;
    private javax.swing.JLabel lblR$CadEst;
    private javax.swing.JLabel lblR$CusCadEnt;
    private javax.swing.JLabel lblR$CusGerEnt;
    private javax.swing.JLabel lblR$Des;
    private javax.swing.JLabel lblR$GerDes;
    private javax.swing.JLabel lblR$GerEnt;
    private javax.swing.JLabel lblR$GerEst;
    private javax.swing.JLabel lblR$Jur;
    private javax.swing.JLabel lblR$Os;
    private javax.swing.JLabel lblResRel;
    private javax.swing.JLabel lblSelIteCadEnt;
    private javax.swing.JLabel lblSelIteGerEnt;
    private javax.swing.JLabel lblSerCadEnt;
    private javax.swing.JLabel lblSerGerEnt;
    private javax.swing.JLabel lblTelCadVen;
    private javax.swing.JLabel lblTelOs;
    private javax.swing.JLabel lblTitPri;
    private javax.swing.JLabel lblTotEntRel;
    private javax.swing.JLabel lblValCarRel;
    private javax.swing.JLabel lblValDinRel;
    private javax.swing.JLabel lblValFinJur;
    private javax.swing.JLabel lblValFinJur1;
    private javax.swing.JLabel lblValJur;
    private javax.swing.JLabel lblValJur2;
    private javax.swing.JLabel lblValJur3;
    private javax.swing.JLabel lblValJurJur;
    private javax.swing.JLabel lblValMedRel;
    private javax.swing.JLabel lblValMesPreJur;
    private javax.swing.JLabel lblValParJur;
    private javax.swing.JLabel lblValParJur1;
    private javax.swing.JLabel lblValPixRel;
    private javax.swing.JLabel lblValTotRel;
    private javax.swing.JLabel lblVen;
    private javax.swing.JLabel lblVenCadVen;
    private javax.swing.JLabel lblVenMas;
    private javax.swing.JPanel pnlCadDes;
    private javax.swing.JPanel pnlCadEnt;
    private javax.swing.JPanel pnlCadEst;
    private javax.swing.JPanel pnlCadTipSer;
    private javax.swing.JPanel pnlCadVen;
    private javax.swing.JPanel pnlConEnt;
    private javax.swing.JPanel pnlConEst;
    private javax.swing.JPanel pnlDes;
    private javax.swing.JPanel pnlGerDes;
    private javax.swing.JPanel pnlGerEnt;
    private javax.swing.JPanel pnlGerEst;
    private javax.swing.JPanel pnlGerOs;
    private javax.swing.JPanel pnlGerTipSer;
    private javax.swing.JPanel pnlIteCadEnt;
    private javax.swing.JPanel pnlIteGerEnt;
    private javax.swing.JPanel pnlJur;
    private javax.swing.JPanel pnlMas;
    private javax.swing.JPanel pnlOs;
    public static javax.swing.JPanel pnlPri;
    private javax.swing.JPanel pnlRel;
    private javax.swing.JPanel pnlVen;
    private javax.swing.JRadioButton rbtnAceCadEst;
    private javax.swing.JRadioButton rbtnAceConEst;
    private javax.swing.JRadioButton rbtnAceGerEst;
    private javax.swing.JRadioButton rbtnAssCadEnt;
    private javax.swing.JRadioButton rbtnAssGerTipSer;
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
    private javax.swing.JRadioButton rbtnCarCadEnt;
    private javax.swing.JRadioButton rbtnCarGerEnt;
    private javax.swing.JRadioButton rbtnChiCadEst;
    private javax.swing.JRadioButton rbtnChiConEst;
    private javax.swing.JRadioButton rbtnChiGerEst;
    private javax.swing.JRadioButton rbtnChiIteCadEnt;
    private javax.swing.JRadioButton rbtnChiIteGerEnt;
    private javax.swing.JRadioButton rbtnCreCadEnt;
    private javax.swing.JRadioButton rbtnDebCadEnt;
    private javax.swing.JRadioButton rbtnDinCadEnt;
    private javax.swing.JRadioButton rbtnDinGerEnt;
    private javax.swing.JRadioButton rbtnMigMas;
    private javax.swing.JRadioButton rbtnMigTroMas;
    private javax.swing.JRadioButton rbtnOutGerTipSer;
    private javax.swing.JRadioButton rbtnOutTipSer;
    private javax.swing.JRadioButton rbtnPelCadEst;
    private javax.swing.JRadioButton rbtnPelConEst;
    private javax.swing.JRadioButton rbtnPelGerEst;
    private javax.swing.JRadioButton rbtnPelIteCadEnt;
    private javax.swing.JRadioButton rbtnPelIteGerEnt;
    private javax.swing.JRadioButton rbtnPixCadEnt;
    private javax.swing.JRadioButton rbtnPixGerEnt;
    private javax.swing.JRadioButton rbtnSerCadEnt;
    private javax.swing.JRadioButton rbtnSerRel;
    private javax.swing.JRadioButton rbtnSerTimTipSer;
    private javax.swing.JRadioButton rbtnTimGerTipSer;
    private javax.swing.JRadioButton rbtnTodRel;
    private javax.swing.JRadioButton rbtnVenCadEnt;
    private javax.swing.JRadioButton rbtnVenRel;
    private javax.swing.JRadioButton rbtnVenRel1;
    private javax.swing.JScrollPane scrCadEst;
    private javax.swing.JScrollPane scrConDes;
    private javax.swing.JScrollPane scrConEnt;
    private javax.swing.JScrollPane scrConEst;
    private javax.swing.JScrollPane scrEstIteCadEnt;
    private javax.swing.JScrollPane scrEstIteGerEnt;
    private javax.swing.JScrollPane scrGerDes;
    private javax.swing.JScrollPane scrGerEnt;
    private javax.swing.JScrollPane scrGerEst;
    private javax.swing.JScrollPane scrOs;
    private javax.swing.JScrollPane scrRel;
    private javax.swing.JScrollPane scrSelIteCadEnt;
    private javax.swing.JScrollPane scrSelIteGerEnt;
    private javax.swing.JScrollPane scrTipSer;
    private javax.swing.JScrollPane scrVen;
    private javax.swing.JSeparator sepBusConEst;
    private javax.swing.JSeparator sepBusConEst1;
    private javax.swing.JSeparator sepBusGerEst;
    private javax.swing.JSeparator sepBusGerEst1;
    private javax.swing.JSeparator sepBusIteCadEnt;
    private javax.swing.JSeparator sepBusIteCadEnt1;
    private javax.swing.JSeparator sepBusVen;
    private javax.swing.JSeparator sepBusVen1;
    private javax.swing.JSeparator sepCadVen;
    private javax.swing.JSeparator sepCliCadEnt;
    private javax.swing.JSeparator sepCliCadVen;
    private javax.swing.JSeparator sepCliGerEnt;
    private javax.swing.JSeparator sepCorCadEst;
    private javax.swing.JSeparator sepCorCadEst1;
    private javax.swing.JSeparator sepCorGerEst;
    private javax.swing.JSeparator sepCusCadEnt;
    private javax.swing.JSeparator sepCusGerEnt;
    private javax.swing.JSeparator sepCusGerEnt1;
    private javax.swing.JSeparator sepDatCadEnt;
    private javax.swing.JSeparator sepDatCadEnt1;
    private javax.swing.JSeparator sepDatCadEnt2;
    private javax.swing.JSeparator sepDatCadEnt3;
    private javax.swing.JSeparator sepDatCadEnt4;
    private javax.swing.JSeparator sepDatCadEnt6;
    private javax.swing.JSeparator sepDatCadEnt7;
    private javax.swing.JSeparator sepDatCadVen;
    private javax.swing.JSeparator sepDatGerDes;
    private javax.swing.JSeparator sepDatGerEnt;
    private javax.swing.JSeparator sepDesGerDes;
    private javax.swing.JSeparator sepDesGerTipSer;
    private javax.swing.JSeparator sepDesGerTipSer1;
    private javax.swing.JSeparator sepDesGerTipSer10;
    private javax.swing.JSeparator sepDesGerTipSer11;
    private javax.swing.JSeparator sepDesGerTipSer16;
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
    private javax.swing.JSeparator sepForCadEnt;
    private javax.swing.JSeparator sepForGerEnt;
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
    private javax.swing.JSeparator sepPlaCadVen;
    private javax.swing.JSeparator sepPreCadEnt;
    private javax.swing.JSeparator sepPreCadEst;
    private javax.swing.JSeparator sepPreGerDes;
    private javax.swing.JSeparator sepPreGerEnt;
    private javax.swing.JSeparator sepPreGerEst;
    private javax.swing.JSeparator sepQuaCadEst;
    private javax.swing.JSeparator sepQuaCadEst1;
    private javax.swing.JSeparator sepQuaGerEst;
    private javax.swing.JSeparator sepTelCadVen;
    private javax.swing.JSeparator sepTelCadVen1;
    private javax.swing.JSeparator sepVenCadVen;
    private javax.swing.JSpinner spnParCadEnt;
    private javax.swing.JSpinner spnParJur;
    private javax.swing.JTable tblCadEst;
    private javax.swing.JTable tblConDes;
    private javax.swing.JTable tblConEnt;
    private javax.swing.JTable tblConEst;
    private javax.swing.JTable tblEstIteCadEnt;
    private javax.swing.JTable tblEstIteGerEnt;
    private javax.swing.JTable tblGerDes;
    private javax.swing.JTable tblGerEnt;
    private javax.swing.JTable tblGerEst;
    private javax.swing.JTable tblOs;
    private javax.swing.JTable tblRel;
    private javax.swing.JTable tblSelIteCadEnt;
    private javax.swing.JTable tblSelIteGerEnt;
    private javax.swing.JTable tblTipSer;
    private javax.swing.JTable tblVen;
    private javax.swing.JTextField txtAceCadVen;
    private javax.swing.JTextArea txtAreMas;
    private javax.swing.JTextField txtBusConEnt;
    private javax.swing.JTextField txtBusConEst;
    private javax.swing.JTextField txtBusGerEst;
    private javax.swing.JTextField txtBusGerOs;
    private javax.swing.JTextField txtBusIteCadEnt;
    private javax.swing.JTextField txtBusIteGerEnt;
    private javax.swing.JTextField txtBusVen;
    private javax.swing.JTextField txtCliCadEnt;
    private javax.swing.JTextField txtCliCadVen;
    private javax.swing.JTextField txtCliGerEnt;
    private javax.swing.JTextField txtCliOs;
    private javax.swing.JTextField txtCodCadEnt;
    private javax.swing.JTextField txtCorCadEst;
    private javax.swing.JTextField txtCorGerEst;
    private javax.swing.JTextField txtCpfCadVen;
    private javax.swing.JTextField txtCpfMas;
    private javax.swing.JTextField txtCusCadEnt;
    private javax.swing.JTextField txtCusGerEnt;
    private javax.swing.JTextField txtDatBusGerEnt;
    private javax.swing.JTextField txtDatCadEnt;
    private javax.swing.JTextField txtDatCadVen;
    private javax.swing.JTextField txtDatDes;
    private javax.swing.JTextField txtDatFinRel;
    private javax.swing.JTextField txtDatGerDes;
    private javax.swing.JTextField txtDatGerEnt;
    private javax.swing.JTextField txtDatIniRel;
    private javax.swing.JTextField txtDatOs;
    private javax.swing.JTextField txtDatSaiOs;
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
    private javax.swing.JTextField txtForCadEnt;
    private javax.swing.JTextField txtForGerEnt;
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
    private javax.swing.JTextField txtPlaCadVen;
    private javax.swing.JTextField txtPlaMas;
    private javax.swing.JTextField txtPreCadEnt;
    private javax.swing.JTextField txtPreCadEst;
    private javax.swing.JTextField txtPreDes;
    private javax.swing.JTextField txtPreGerDes;
    private javax.swing.JTextField txtPreGerEnt;
    private javax.swing.JTextField txtPreGerEst;
    private javax.swing.JTextField txtPreOs;
    private javax.swing.JTextField txtQuaCadEst;
    private javax.swing.JTextField txtQuaGerEst;
    private javax.swing.JTextField txtRepOs;
    private javax.swing.JTextField txtTelCadVen;
    private javax.swing.JTextField txtTelOs;
    private javax.swing.JTextField txtTipCadEst;
    private javax.swing.JTextField txtTipConEst;
    private javax.swing.JTextField txtTipGerEst;
    private javax.swing.JTextField txtValJur;
    private javax.swing.JTextField txtVenCadVen;
    private javax.swing.JTextField txtVenMas;
    // End of variables declaration//GEN-END:variables
}
