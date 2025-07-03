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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import model.estoque;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JasperExportManager;
import org.apache.pdfbox.Loader;

public final class main extends javax.swing.JFrame {

    public main() {

        initComponents();
        setLocationRelativeTo(null);
        loading();
//      iniciasistema();
//      pnlPrincipal.setVisible(true);

    }

    public void loading() {

        loading lo = new loading();
        lo.setVisible(true);

        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {

                try {

                    publish("Conectando ao banco de dados...");
                    Thread.sleep(1000);

                    connection.getConnection();

                    publish("Conectado com sucesso!");
                    Thread.sleep(1000);

                    publish("Verificando afazeres...");
                    Thread.sleep(1000);

                    if (!verificaafazeres()) {
                        publish("Atenção! Erro na verificação de afazeres!");
                        Thread.sleep(3000);
                    } else {
                        publish("Verificado com sucesso!");
                        Thread.sleep(1000);
                    }

                    publish("Fazendo backup automático...");
                    Thread.sleep(1000);

                    if (backupdatabase()) {
                        publish("Backup concluído! Iniciando...");
                    } else {
                        publish("Atenção! Erro ao fazer backup. Iniciando...");
                    }

                    Thread.sleep(3000);

                    lo.dispose();

                } catch (SQLException e) {
                    publish("Erro na conexão ao banco de dados!");
                    Thread.sleep(3000);
                    System.exit(0);

                }

                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {

                for (String mensagem : chunks) {
                    lo.txtLoaPan.setText(mensagem);
                }
            }

            @Override
            protected void done() {

                setVisible(true);
                pnlPrincipal.setVisible(true);
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
        pnlAlterarEntrada.setVisible(false);
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

        txtCodCadEnt.setVisible(false);

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);

        DefaultTableModel model = (DefaultTableModel) tblSelIteGerEnt.getModel();
        model.setRowCount(0);

        DefaultTableModel model1 = (DefaultTableModel) tblSelIteCadEnt.getModel();
        model1.setRowCount(0);

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

            if (exitCode != 0) {

                return false;
            }

        } catch (IOException | InterruptedException ex) {
            return false;
        }
        return true;
    }

    public class RoundedLabel extends JLabel {

        private final int cornerRadius;

        public RoundedLabel(String text, int radius) {
            super(text);
            this.cornerRadius = radius;
            setOpaque(false); // Deixa o Swing saber que você vai desenhar o fundo
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape rounded = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(getBackground());
            g2.fill(rounded);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    public class RoundedPanel extends JPanel {

        private final int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false); // Para permitir a transparência do fundo
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Desenha fundo arredondado
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape rounded = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(getBackground());
            g2.fill(rounded);
            g2.dispose();

            super.paintComponent(g);
        }
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

    private boolean verificaafazeres() {

        try {

            if (timerven != null) {

                ((Timer) timerven.getSource()).stop();

            }

            despezasDAO dedao = new despezasDAO();

            if (dedao.verificar()) {

                btnVenPri.setVisible(true);

                Timer timer = new Timer(700, new ActionListener() {

                    int n = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        timerven = e;

                        n++;

                        if (n % 2 == 0) {
                            btnVenPri.setForeground(corforeazulenter);
                        } else {
                            btnVenPri.setForeground(new Color(255, 255, 255));
                        }
                    }

                });
                timer.start();

            } else {

                btnVenPri.setVisible(false);

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;

    }

    private static String capitalizeFirstLetterOfEachWord(String text) {
        StringBuilder sb = new StringBuilder();

        String[] words = text.split("\\s+");

        for (String word : words) {
            if (!word.isEmpty()) {
                if (word.length() > 2) {
                    sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
                } else {
                    sb.append(word.toLowerCase());
                }
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }

    private double juros(int parcela) {

//        if (parcela == 0) {
//            return 1.99;
//        } else if (parcela == 1) {
//            return 4.98;
//        } else if (parcela == 2) {
//            return 9.90;
//        } else if (parcela == 3) {
//            return 11.28;
//        } else if (parcela == 4) {
//            return 12.64;
//        } else if (parcela == 5) {
//            return 13.97;
//        } else if (parcela == 6) {
//            return 15.27;
//        } else if (parcela == 7) {
//            return 16.55;
//        } else if (parcela == 8) {
//            return 17.81;
//        } else if (parcela == 9) {
//            return 19.04;
//        } else if (parcela == 10) {
//            return 20.24;
//        } else if (parcela == 11) {
//            return 21.43;
//        } else if (parcela == 12) {
//            return 22.59;
//        }
        switch (parcela) {
            case 0:
                return 1.68;
            case 1:
                return 3.48;
            case 2:
                return 8.99;
            case 3:
                return 10.99;
            case 4:
                return 11.99;
            case 5:
                return 12.99;
            case 6:
                return 13.99;
            case 7:
                return 14.99;
            case 8:
                return 15.99;
            case 9:
                return 16.99;
            case 10:
                return 17.99;
            case 11:
                return 17.99;
            case 12:
                return 17.99;
            default:
                break;
        }
        return parcela;

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
                        int atvValue = (int) table.getValueAt(row, 3);

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

                tblTipSer.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

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
                header.setBackground(new Color(241, 241, 241));

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
                header.setBackground(new Color(241, 241, 241));

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
                header.setBackground(new Color(241, 241, 241));

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
                header.setBackground(new Color(241, 241, 241));
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

                if (somaCusto > 0 && !rbtnTodRel.isSelected()) {
                    chkCus.setEnabled(true);
                } else {
                    chkCus.setEnabled(false);
                    chkCus.setSelected(false);
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
                header.setBackground(new Color(241, 241, 241));
                header.setFont(fontbold(12));
                header.setReorderingAllowed(false);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                }

                tbl.getColumnModel().getColumn(7).setMinWidth(0);
                tbl.getColumnModel().getColumn(7).setMaxWidth(0);
                tbl.getColumnModel().getColumn(7).setWidth(0);

                tbl.setVisible(true);
                scr.setVisible(true);

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

                if (en.getCtotal() > 0 && !rbtnTodRel.isSelected()) {
                    chkCus.setEnabled(true);
                } else {
                    chkCus.setEnabled(false);
                    chkCus.setSelected(false);
                }

                lblResRel.setVisible(false);

            } else {

                DefaultTableModel mol = (DefaultTableModel) tblRel.getModel();
                mol.setRowCount(0);

                scr.setVisible(false);
                tbl.setVisible(false);

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
                    rowData[9] = (row[9] != null && !"".equals(row[9])) ? row[9] : "Sem Detalhes";
                    rowData[10] = row[10];

                    modelo.addRow(rowData);

                }

                DefaultTableCellRenderer deheader = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if (isSelected) {
                            c.setBackground(new Color(211, 211, 211));
                        } else {
                            if ("Saída Caixa".equals(table.getValueAt(row, 2))) {
                                c.setBackground(new Color(255, 246, 246));
                            } else if ("Entrada Caixa".equals(table.getValueAt(row, 2))) {
                                c.setBackground(new Color(246, 255, 246));
                            } else {
                                c.setBackground(new Color(241, 241, 241));
                            }
                        }

                        return c;
                    }

                };

                deheader.setHorizontalAlignment(JLabel.CENTER);

                deheader.setForeground(Color.BLACK);
                deheader.setFont(fontmed(12));

                header.setForeground(corforeazul);
                header.setBackground(new Color(241, 241, 241));

                header.setFont(fontbold(12));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl.setDefaultEditor(Object.class, null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(deheader);
                }

                tbl.getColumnModel().getColumn(10).setMinWidth(0);
                tbl.getColumnModel().getColumn(10).setMaxWidth(0);
                tbl.getColumnModel().getColumn(10).setWidth(0);

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

                                if (comparacao1 < 0 && comparacao2 > 0 && diferencaDias > 2) { //dataatual menor data e data maior datacon

                                    component.setBackground(new Color(182, 222, 170));//verde

                                } else {

                                    component.setBackground(new Color(229, 190, 190));//vermelho

                                }

                            } else {

                                if (comparacao1 < 0 && diferencaDias > 2) {

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
                header.setBackground(new Color(241, 241, 241));

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

                tbl.getColumnModel().getColumn(4).setMinWidth(0);
                tbl.getColumnModel().getColumn(4).setMaxWidth(0);
                tbl.getColumnModel().getColumn(4).setWidth(0);

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
                header.setBackground(new Color(241, 241, 241));

                header.setFont(fontbold(13));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl
                        .setDefaultEditor(Object.class,
                                null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                }

                tbl.getColumnModel().getColumn(12).setMinWidth(0);
                tbl.getColumnModel().getColumn(12).setMaxWidth(0);
                tbl.getColumnModel().getColumn(12).setWidth(0);

                tbl.getColumnModel().getColumn(11).setMinWidth(0);
                tbl.getColumnModel().getColumn(11).setMaxWidth(0);
                tbl.getColumnModel().getColumn(11).setWidth(0);

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
                header.setBackground(new Color(241, 241, 241));

                header.setFont(fontbold(13));
                header.setReorderingAllowed(false);

                tbl.setModel(modelo);
                tbl.setRowHeight(25);
                tbl
                        .setDefaultEditor(Object.class,
                                null);
                scr.getVerticalScrollBar().setValue(0);

                for (int i = 0; i < tbl.getColumnCount(); i++) {
                    tbl.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                }

                tbl.getColumnModel().getColumn(12).setMinWidth(0);
                tbl.getColumnModel().getColumn(12).setMaxWidth(0);
                tbl.getColumnModel().getColumn(12).setWidth(0);

                tbl.getColumnModel().getColumn(11).setMinWidth(0);
                tbl.getColumnModel().getColumn(11).setMaxWidth(0);
                tbl.getColumnModel().getColumn(11).setWidth(0);

                tbl.setVisible(true);
                scr.setVisible(true);

            } else {
                return false;

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
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

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

                cellRenderer.setHorizontalAlignment(JLabel.CENTER);
                cellRenderer.setForeground(Color.BLACK);
                cellRenderer.setFont(fontmed(12));

                header.setForeground(corforeazul);
                header.setBackground(new Color(241, 241, 241));

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

                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

                cellRenderer.setHorizontalAlignment(JLabel.CENTER);
                cellRenderer.setForeground(Color.BLACK);
                cellRenderer.setFont(fontmed(12));

                header.setForeground(corforeazul);
                header.setBackground(new Color(241, 241, 241));

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
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
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
                header.setBackground(new Color(241, 241, 241));

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

                tbl.getColumnModel().getColumn(4).setMinWidth(0);
                tbl.getColumnModel().getColumn(4).setMaxWidth(0);
                tbl.getColumnModel().getColumn(4).setWidth(0);

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

    private void tabelacores() {

        JTableHeader header = tblVarCorCadEst.getTableHeader();

        DefaultTableModel modelo = new DefaultTableModel();

        DefaultTableCellRenderer deheader = new DefaultTableCellRenderer();

        String[] colunas = {
            "Cor",
            "Quantidade"
        };

        for (String coluna : colunas) {

            modelo.addColumn(coluna);

        }

        deheader.setHorizontalAlignment(JLabel.CENTER);

        deheader.setForeground(Color.BLACK);
        deheader.setFont(fontmed(11));

        header.setForeground(corforeazul);
        header.setBackground(new Color(241, 241, 241));

        header.setFont(fontbold(12));
        header.setReorderingAllowed(false);

        tblVarCorCadEst.setModel(modelo);
        tblVarCorCadEst.setRowHeight(25);
        tblVarCorCadEst
                .setDefaultEditor(Object.class,
                        null);
        scrVarCorCadEst.getVerticalScrollBar().setValue(0);

        for (int i = 0; i < tblVarCorCadEst.getColumnCount(); i++) {
            tblVarCorCadEst.getColumnModel().getColumn(i).setCellRenderer(deheader);
        }

        tblVarCorCadEst.setModel(modelo);

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
        header.setBackground(new Color(241, 241, 241));

        header.setFont(fontbold(12));
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
                    deheader.setFont(fontmed(12));

                    header.setForeground(corforeazul);
                    header.setBackground(new Color(241, 241, 241));

                    header.setFont(fontbold(11));
                    header.setReorderingAllowed(false);

                    tblSelIteGerEnt.setModel(modelo);
                    tblSelIteGerEnt.setRowHeight(25);
                    tblSelIteGerEnt
                            .setDefaultEditor(Object.class,
                                    null);

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
                    header.setBackground(new Color(241, 241, 241));

                    header.setFont(fontbold(11));
                    header.setReorderingAllowed(false);

                    tblSelIteGerEnt.setModel(modelo);
                    tblSelIteGerEnt.setRowHeight(25);
                    tblSelIteGerEnt
                            .setDefaultEditor(Object.class,
                                    null);

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
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void adicionarcor(JTable tabelaDestino, String cor, int qtd) {

        DefaultTableModel modelo = (DefaultTableModel) tabelaDestino.getModel();

        Object[] novaLinha = {cor, qtd};

        modelo.addRow(novaLinha);

        tabelaDestino.setModel(modelo);

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
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
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

                header.setBackground(new Color(241, 241, 241));
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

    public void copiarcontrato() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha um arquivo PDF");

        String userHome = System.getProperty("user.home");
        File downloadsFolder = new File(userHome, "Downloads");
        fileChooser.setCurrentDirectory(downloadsFolder);

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            String novoNome = txtNomMas.getText().toUpperCase();
            if (novoNome == null || novoNome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O nome do arquivo não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File renamedFile = new File(selectedFile.getParent(), novoNome + ".pdf");

            int counter = 1;
            while (renamedFile.exists()) {
                String newName = novoNome + " " + counter + ".pdf";
                renamedFile = new File(selectedFile.getParent(), newName);
                counter++;
            }

            boolean renamed = selectedFile.renameTo(renamedFile);
            if (!renamed) {
                JOptionPane.showMessageDialog(null, "Não foi possível renomear o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (PDDocument document = Loader.loadPDF(renamedFile)) {

                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage image = pdfRenderer.renderImageWithDPI(0, 150);

                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                ImageTransferable transferable = new ImageTransferable(image);
                clipboard.setContents(transferable, null);

                image.flush();

                JOptionPane.showMessageDialog(null, "Contrato copiado com sucesso!", "Máscara", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao abrir o PDF: " + ex.getMessage());

            }
        }
    }

    static class ImageTransferable implements Transferable {

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
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);

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

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup = new javax.swing.ButtonGroup();
        btnGroup1 = new javax.swing.ButtonGroup();
        btnGroup2 = new javax.swing.ButtonGroup();
        btnGroup3 = new javax.swing.ButtonGroup();
        btnGroup4 = new javax.swing.ButtonGroup();
        btnGroup5 = new javax.swing.ButtonGroup();
        pnlPrincipal = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        imgLogo = new javax.swing.JLabel();
        btnVenPri = new javax.swing.JLabel();
        pnlContent = new javax.swing.JPanel();
        pnlOutros = new RoundedPanel(30);
        lblOutros = new javax.swing.JLabel();
        btnRelatorio = new javax.swing.JLabel();
        btnCadTipSer = new javax.swing.JLabel();
        btnGerTipSer = new javax.swing.JLabel();
        btnDes = new javax.swing.JLabel();
        btnCadDes = new javax.swing.JLabel();
        btnGerDes = new javax.swing.JLabel();
        btnJurPri = new javax.swing.JLabel();
        pnlOS = new RoundedPanel(30);
        lblPlanos1 = new javax.swing.JLabel();
        btnCadOsPri = new javax.swing.JLabel();
        btnGerOsPri = new javax.swing.JLabel();
        pnlPlanos = new RoundedPanel(30);
        lblPlanos = new javax.swing.JLabel();
        btnCadVen = new javax.swing.JLabel();
        btnVen = new javax.swing.JLabel();
        btnMasPla = new javax.swing.JLabel();
        pnlEstoque = new RoundedPanel(30);
        lblEstoque = new javax.swing.JLabel();
        btnCadEst = new javax.swing.JLabel();
        btnConEst = new javax.swing.JLabel();
        btnGerEst = new javax.swing.JLabel();
        pnlEntrada = new RoundedPanel(30);
        lblEntrada = new javax.swing.JLabel();
        btnCadEnt = new javax.swing.JLabel();
        btnConEnt = new javax.swing.JLabel();
        btnGerEnt = new javax.swing.JLabel();
        pnlCadEnt = new javax.swing.JPanel();
        rbtnPixCadEnt = new javax.swing.JRadioButton();
        btnIteCadEnt = new javax.swing.JButton();
        btnSalCadEnt = new javax.swing.JButton();
        btnCanCadEnt = new javax.swing.JButton();
        rbtnSerCadEnt = new javax.swing.JRadioButton();
        rbtnVenCadEnt = new javax.swing.JRadioButton();
        rbtnAssCadEnt = new javax.swing.JRadioButton();
        rbtnTroPreCadEnt = new javax.swing.JRadioButton();
        rbtnTroPlaCadEnt = new javax.swing.JRadioButton();
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
        lblParCadEnt = new javax.swing.JLabel();
        lblNovaEntrada = new javax.swing.JLabel();
        spnParCadEnt = new javax.swing.JSpinner();
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
        lblNovaEntradaItens = new javax.swing.JLabel();
        pnlConEnt = new javax.swing.JPanel();
        lblConsultarEntrada = new javax.swing.JLabel();
        btnCanConEnt = new javax.swing.JButton();
        btnBusConEnt = new javax.swing.JButton();
        lblBusConEnt = new javax.swing.JLabel();
        txtBusConEnt = new javax.swing.JTextField();
        sepBusConEst1 = new javax.swing.JSeparator();
        scrConEnt = new javax.swing.JScrollPane();
        tblConEnt = new javax.swing.JTable();
        pnlGerEnt = new javax.swing.JPanel();
        lblGerenciarEntrada = new javax.swing.JLabel();
        btnBusGerEnt = new javax.swing.JButton();
        btnCanGerEnt = new javax.swing.JButton();
        btnAltGerEnt = new javax.swing.JButton();
        btnExcGerEnt = new javax.swing.JButton();
        lblDatBusGerEnt = new javax.swing.JLabel();
        txtDatBusGerEnt = new javax.swing.JTextField();
        sepBusGerEst1 = new javax.swing.JSeparator();
        scrGerEnt = new javax.swing.JScrollPane();
        tblGerEnt = new javax.swing.JTable();
        pnlAlterarEntrada = new javax.swing.JPanel();
        lblAlterarEntrada = new javax.swing.JLabel();
        btnCanAltEnt = new javax.swing.JButton();
        btnIteGerEnt = new javax.swing.JButton();
        btnSalGerEnt = new javax.swing.JButton();
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
        cmbSerGerEnt = new javax.swing.JComboBox<>();
        lblSerGerEnt = new javax.swing.JLabel();
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
        lblNovaEntradaItens1 = new javax.swing.JLabel();
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
        lblNovaEntradaItens2 = new javax.swing.JLabel();
        btnSalCadEst = new javax.swing.JButton();
        btnAdiCadEst = new javax.swing.JButton();
        btnCanCadEst = new javax.swing.JButton();
        rbtnCapCadEst = new javax.swing.JRadioButton();
        rbtnPelCadEst = new javax.swing.JRadioButton();
        rbtnChiCadEst = new javax.swing.JRadioButton();
        rbtnAceCadEst = new javax.swing.JRadioButton();
        chkVarCorCadEst = new javax.swing.JCheckBox();
        scrVarCorCadEst = new javax.swing.JScrollPane();
        tblVarCorCadEst = new javax.swing.JTable();
        scrCadEst = new javax.swing.JScrollPane();
        tblCadEst = new javax.swing.JTable();
        lblVarCorCadEst = new javax.swing.JLabel();
        txtVarCorCadEst = new javax.swing.JTextField();
        sepVarCorCadEst = new javax.swing.JSeparator();
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
        spnVarCorCadEst = new javax.swing.JSpinner();
        pnlConEst = new javax.swing.JPanel();
        lblNovaEntradaItens3 = new javax.swing.JLabel();
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
        lblNovaEntradaItens4 = new javax.swing.JLabel();
        btnExcGerEst = new javax.swing.JButton();
        btnBusGerEst = new javax.swing.JButton();
        btnAltGerEst = new javax.swing.JButton();
        btnCanGerEst = new javax.swing.JButton();
        chkAltGerEst = new javax.swing.JCheckBox();
        rbtnCapGerEst = new javax.swing.JRadioButton();
        rbtnPelGerEst = new javax.swing.JRadioButton();
        rbtnChiGerEst = new javax.swing.JRadioButton();
        rbtnAceGerEst = new javax.swing.JRadioButton();
        txtPre1GerEst = new javax.swing.JTextField();
        txtQua1GerEst = new javax.swing.JTextField();
        txtMod1GerEst = new javax.swing.JTextField();
        txtMar1GerEst = new javax.swing.JTextField();
        txtDet1GerEst = new javax.swing.JTextField();
        txtLoc1GerEst = new javax.swing.JTextField();
        txtMat1GerEst = new javax.swing.JTextField();
        txtCor1GerEst = new javax.swing.JTextField();
        txtChip1GerEst = new javax.swing.JTextField();
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
        sepBusGerEst = new javax.swing.JSeparator();
        scrGerEst = new javax.swing.JScrollPane();
        tblGerEst = new javax.swing.JTable();
        pnlCadVen = new javax.swing.JPanel();
        lblCadastrarPlano = new javax.swing.JLabel();
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
        pnlVen = new javax.swing.JPanel();
        lblNovaEntradaItens14 = new javax.swing.JLabel();
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
        pnlMas = new javax.swing.JPanel();
        lblNovaEntradaItens10 = new javax.swing.JLabel();
        chkAppMas = new javax.swing.JCheckBox();
        chkMelMas = new javax.swing.JCheckBox();
        btnConMas = new javax.swing.JButton();
        btnVenMas = new javax.swing.JButton();
        btnGerMas = new javax.swing.JButton();
        btnCanMas = new javax.swing.JButton();
        btnParMas = new javax.swing.JButton();
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
        lblVenMas = new javax.swing.JLabel();
        sepDesGerTipSer8 = new javax.swing.JSeparator();
        txtVenMas = new javax.swing.JTextField();
        sepDesGerTipSer9 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreMas = new javax.swing.JTextArea();
        chkDebMas = new javax.swing.JRadioButton();
        chkCarMas = new javax.swing.JRadioButton();
        chkBolMas = new javax.swing.JRadioButton();
        pnlOs = new javax.swing.JPanel();
        lblNovaEntradaItens6 = new javax.swing.JLabel();
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
        lblGerarOS = new javax.swing.JLabel();
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
        pnlRel = new javax.swing.JPanel();
        lblNovaEntradaItens5 = new javax.swing.JLabel();
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
        pnlCadTipSer = new javax.swing.JPanel();
        lblNovaEntradaItens8 = new javax.swing.JLabel();
        btnSalTipSer = new javax.swing.JButton();
        btnCanTipSer = new javax.swing.JButton();
        rbtnOutTipSer = new javax.swing.JRadioButton();
        rbtnSerTimTipSer = new javax.swing.JRadioButton();
        rbtnAssTipSer = new javax.swing.JRadioButton();
        lblDesTipSer = new javax.swing.JLabel();
        txtDesTipSer = new javax.swing.JTextField();
        sepDesTipSer = new javax.swing.JSeparator();
        pnlGerTipSer = new javax.swing.JPanel();
        lblNovaEntradaItens9 = new javax.swing.JLabel();
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
        pnlDes = new javax.swing.JPanel();
        lblNovaEntradaItens11 = new javax.swing.JLabel();
        scrConDes = new javax.swing.JScrollPane();
        tblConDes = new javax.swing.JTable();
        btnVolDes = new javax.swing.JButton();
        pnlCadDes = new javax.swing.JPanel();
        lblNovaEntradaItens12 = new javax.swing.JLabel();
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
        lblNovaEntradaItens13 = new javax.swing.JLabel();
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
        pnlJur = new javax.swing.JPanel();
        lblCadastrarPlano1 = new javax.swing.JLabel();
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
        setMinimumSize(new java.awt.Dimension(1200, 710));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlPrincipal.setVisible(false);
        pnlPrincipal.setBackground(new java.awt.Color(246, 246, 246));
        pnlPrincipal.setMinimumSize(new java.awt.Dimension(1200, 710));
        pnlPrincipal.setPreferredSize(new java.awt.Dimension(1200, 710));
        pnlPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlHeader.setBackground(new java.awt.Color(23, 23, 59));
        pnlHeader.setLayout(null);

        imgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LogoLojaBranco.png"))); // NOI18N
        pnlHeader.add(imgLogo);
        imgLogo.setBounds(420, 50, 390, 100);

        btnVenPri.setFont(fontmed(12));
        btnVenPri.setForeground(new java.awt.Color(255, 255, 255));
        btnVenPri.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnVenPri.setText("Afazer encontrado!");
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
        pnlHeader.add(btnVenPri);
        btnVenPri.setBounds(1010, 20, 150, 20);

        pnlPrincipal.add(pnlHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 200));

        pnlContent.setBackground(new java.awt.Color(241, 241, 241));
        pnlContent.setLayout(null);

        pnlOutros.setBackground(new java.awt.Color(236, 236, 236));
        pnlOutros.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblOutros.setBackground(new java.awt.Color(243, 243, 243));
        lblOutros.setFont(fontbold(20));
        lblOutros.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOutros.setText("Outros");
        pnlOutros.add(lblOutros, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 190, 30));

        btnRelatorio.setFont(fontmed(13));
        btnRelatorio.setForeground(corforeazul);
        btnRelatorio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnRelatorio.setText("Relatório");
        btnRelatorio.setToolTipText("");
        btnRelatorio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRelatorio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRelatorioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRelatorioMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnRelatorioMouseReleased(evt);
            }
        });
        pnlOutros.add(btnRelatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 130, 20));

        btnCadTipSer.setFont(fontmed(13));
        btnCadTipSer.setForeground(corforeazul);
        btnCadTipSer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadTipSer.setText("Cadastrar Serviço");
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
        pnlOutros.add(btnCadTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 150, 20));

        btnGerTipSer.setFont(fontmed(13));
        btnGerTipSer.setForeground(corforeazul);
        btnGerTipSer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGerTipSer.setText("Gerenciar Serviço");
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
        pnlOutros.add(btnGerTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, 130, 20));

        btnDes.setFont(fontmed(13));
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
        pnlOutros.add(btnDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 70, 20));

        btnCadDes.setFont(fontmed(13));
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
        pnlOutros.add(btnCadDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 150, 20));

        btnGerDes.setFont(fontmed(13));
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
        pnlOutros.add(btnGerDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 150, 20));

        btnJurPri.setFont(fontmed(13));
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
        pnlOutros.add(btnJurPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 110, 20));

        pnlContent.add(pnlOutros);
        pnlOutros.setBounds(760, 30, 270, 300);

        pnlOS.setBackground(new java.awt.Color(236, 236, 236));
        pnlOS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblPlanos1.setBackground(new java.awt.Color(243, 243, 243));
        lblPlanos1.setFont(fontbold(20));
        lblPlanos1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPlanos1.setText("Ordem de Serviço");
        pnlOS.add(lblPlanos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 190, 30));

        btnCadOsPri.setFont(fontmed(13));
        btnCadOsPri.setForeground(corforeazul);
        btnCadOsPri.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadOsPri.setText("Gerar");
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
        pnlOS.add(btnCadOsPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 70, 20));

        btnGerOsPri.setFont(fontmed(13));
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
        pnlOS.add(btnGerOsPri, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 90, 20));

        pnlContent.add(pnlOS);
        pnlOS.setBounds(470, 230, 270, 150);

        pnlPlanos.setBackground(new java.awt.Color(236, 236, 236));
        pnlPlanos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblPlanos.setBackground(new java.awt.Color(243, 243, 243));
        lblPlanos.setFont(fontbold(20));
        lblPlanos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPlanos.setText("Planos");
        pnlPlanos.add(lblPlanos, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 210, 30));

        btnCadVen.setFont(fontmed(13));
        btnCadVen.setForeground(corforeazul);
        btnCadVen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCadVen.setText("Cadastrar");
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
        pnlPlanos.add(btnCadVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 90, 20));

        btnVen.setFont(fontmed(13));
        btnVen.setForeground(corforeazul);
        btnVen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnVen.setText("Consultar");
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
        pnlPlanos.add(btnVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 90, 20));

        btnMasPla.setFont(fontmed(13));
        btnMasPla.setForeground(corforeazul);
        btnMasPla.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnMasPla.setText("Máscara");
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
        pnlPlanos.add(btnMasPla, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 90, 20));

        pnlContent.add(pnlPlanos);
        pnlPlanos.setBounds(470, 30, 270, 180);

        pnlEstoque.setBackground(new java.awt.Color(236, 236, 236));
        pnlEstoque.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEstoque.setBackground(new java.awt.Color(243, 243, 243));
        lblEstoque.setFont(fontbold(20));
        lblEstoque.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstoque.setText("Estoque");
        pnlEstoque.add(lblEstoque, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 190, 30));

        btnCadEst.setFont(fontmed(13));
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
        pnlEstoque.add(btnCadEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 90, 20));

        btnConEst.setFont(fontmed(13));
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
        pnlEstoque.add(btnConEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 90, 20));

        btnGerEst.setFont(fontmed(13));
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
        pnlEstoque.add(btnGerEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 110, 20));

        pnlContent.add(pnlEstoque);
        pnlEstoque.setBounds(180, 230, 270, 180);

        pnlEntrada.setBackground(new java.awt.Color(236, 236, 236));
        pnlEntrada.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEntrada.setBackground(new java.awt.Color(243, 243, 243));
        lblEntrada.setFont(fontbold(20));
        lblEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEntrada.setText("Entrada");
        pnlEntrada.add(lblEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 190, 30));

        btnCadEnt.setFont(fontmed(13));
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
        pnlEntrada.add(btnCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 90, 20));

        btnConEnt.setFont(fontmed(13));
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
        pnlEntrada.add(btnConEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 90, 20));

        btnGerEnt.setFont(fontmed(13));
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
        pnlEntrada.add(btnGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 90, 20));

        pnlContent.add(pnlEntrada);
        pnlEntrada.setBounds(180, 30, 270, 180);

        pnlPrincipal.add(pnlContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnPixCadEnt.setBounds(630, 120, 90, 21);

        btnIteCadEnt.setFont(fontmed(12));
        btnIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnIteCadEnt.setText("Estoque");
        btnIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIteCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(btnIteCadEnt);
        btnIteCadEnt.setBounds(780, 310, 90, 30);

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
        btnSalCadEnt.setBounds(780, 350, 90, 30);

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
        btnCanCadEnt.setBounds(780, 390, 90, 30);

        rbtnSerCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnSerCadEnt.setBounds(460, 90, 90, 21);

        rbtnVenCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnVenCadEnt.setBounds(550, 90, 80, 21);

        rbtnAssCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnAssCadEnt.setBounds(630, 90, 100, 21);

        btnGroup5.add(rbtnTroPreCadEnt);
        rbtnTroPreCadEnt.setFont(fontmed(12));
        rbtnTroPreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnTroPreCadEnt.setText("Troca Pré");
        rbtnTroPreCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnTroPreCadEnt.setEnabled(false);
        rbtnTroPreCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnTroPreCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnTroPreCadEnt);
        rbtnTroPreCadEnt.setBounds(570, 370, 90, 21);

        btnGroup5.add(rbtnTroPlaCadEnt);
        rbtnTroPlaCadEnt.setFont(fontmed(12));
        rbtnTroPlaCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnTroPlaCadEnt.setText("Troca Plano");
        rbtnTroPlaCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnTroPlaCadEnt.setEnabled(false);
        rbtnTroPlaCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnTroPlaCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(rbtnTroPlaCadEnt);
        rbtnTroPlaCadEnt.setBounds(570, 400, 110, 21);

        lblDatCadEnt.setFont(fontmed(12));
        lblDatCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDatCadEnt.setText("Data");
        lblDatCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblDatCadEnt);
        lblDatCadEnt.setBounds(330, 220, 40, 20);

        txtDatCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatCadEnt.setBounds(330, 220, 100, 20);

        sepDatCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepDatCadEnt);
        sepDatCadEnt.setBounds(330, 240, 100, 10);

        lblR$CadEnt.setFont(fontmed(13));
        lblR$CadEnt.setText("R$");
        lblR$CadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblR$CadEnt);
        lblR$CadEnt.setBounds(330, 270, 20, 21);

        lblPreCadEnt.setFont(fontmed(12));
        lblPreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblPreCadEnt.setText("Preço");
        lblPreCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblPreCadEnt);
        lblPreCadEnt.setBounds(330, 270, 40, 20);

        txtPreCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtPreCadEnt.setBounds(350, 270, 80, 20);

        sepPreCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepPreCadEnt);
        sepPreCadEnt.setBounds(330, 290, 100, 10);

        cmbSerCadEnt.setFont(fontmed(13));
        cmbSerCadEnt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o serviço" }));
        cmbSerCadEnt.setToolTipText("");
        cmbSerCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbSerCadEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbSerCadEntMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cmbSerCadEntMouseReleased(evt);
            }
        });
        cmbSerCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSerCadEntActionPerformed(evt);
            }
        });
        pnlCadEnt.add(cmbSerCadEnt);
        cmbSerCadEnt.setBounds(330, 390, 190, 30);

        lblDetCadEnt.setFont(fontmed(12));
        lblDetCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDetCadEnt.setText("Detalhes");
        lblDetCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblDetCadEnt);
        lblDetCadEnt.setBounds(330, 320, 70, 20);

        txtDetCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDetCadEnt.setBounds(330, 320, 190, 20);

        sepDetCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepDetCadEnt);
        sepDetCadEnt.setBounds(330, 340, 190, 10);

        lblSerCadEnt.setFont(fontmed(12));
        lblSerCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSerCadEnt.setText("Serviço");
        pnlCadEnt.add(lblSerCadEnt);
        lblSerCadEnt.setBounds(330, 360, 90, 30);
        pnlCadEnt.add(txtCodCadEnt);
        txtCodCadEnt.setBounds(330, 170, 64, 22);

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
        rbtnCarCadEnt.setBounds(550, 120, 70, 20);

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
        rbtnDinCadEnt.setBounds(460, 120, 90, 21);

        lblR$CusCadEnt.setFont(fontmed(13));
        lblR$CusCadEnt.setText("R$");
        lblR$CusCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblR$CusCadEnt);
        lblR$CusCadEnt.setBounds(570, 270, 20, 21);

        lblCusCadEnt.setFont(fontmed(12));
        lblCusCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCusCadEnt.setText("Custo");
        lblCusCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblCusCadEnt);
        lblCusCadEnt.setBounds(570, 270, 40, 20);

        txtCusCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtCusCadEnt.setBounds(590, 270, 80, 20);

        sepCusCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepCusCadEnt);
        sepCusCadEnt.setBounds(570, 290, 100, 10);

        lblForCadEnt.setFont(fontmed(12));
        lblForCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblForCadEnt.setText("Fornecedor");
        lblForCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblForCadEnt);
        lblForCadEnt.setBounds(570, 320, 90, 20);

        txtForCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtForCadEnt.setBounds(570, 320, 160, 20);

        sepForCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepForCadEnt);
        sepForCadEnt.setBounds(570, 340, 160, 10);

        lblCliCadEnt.setFont(fontmed(12));
        lblCliCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCliCadEnt.setText("Cliente");
        lblCliCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEnt.add(lblCliCadEnt);
        lblCliCadEnt.setBounds(570, 220, 90, 20);

        txtCliCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtCliCadEnt.setBounds(570, 220, 160, 20);

        sepCliCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEnt.add(sepCliCadEnt);
        sepCliCadEnt.setBounds(570, 240, 160, 10);

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
        rbtnCreCadEnt.setBounds(630, 150, 80, 21);

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
        rbtnDebCadEnt.setBounds(550, 150, 80, 20);

        lblParCadEnt.setFont(fontmed(12));
        lblParCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblParCadEnt.setText("parcela(s)");
        lblParCadEnt.setEnabled(false);
        pnlCadEnt.add(lblParCadEnt);
        lblParCadEnt.setBounds(780, 150, 80, 20);

        lblNovaEntrada.setFont(fontbold(25));
        lblNovaEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntrada.setText("Nova Entrada");
        pnlCadEnt.add(lblNovaEntrada);
        lblNovaEntrada.setBounds(0, 30, 1200, 30);

        spnParCadEnt.setFont(fontmed(13));
        spnParCadEnt.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));
        spnParCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        spnParCadEnt.setEditor(new javax.swing.JSpinner.NumberEditor(spnParCadEnt, ""));
        spnParCadEnt.setEnabled(false);
        spnParCadEnt.setFocusable(false);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spnParCadEnt.getEditor();
        editor.getTextField().setEditable(false);
        pnlCadEnt.add(spnParCadEnt);
        spnParCadEnt.setBounds(710, 140, 60, 30);

        pnlPrincipal.add(pnlCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
        pnlIteCadEnt.setMinimumSize(new java.awt.Dimension(1200, 520));
        pnlIteCadEnt.setPreferredSize(new java.awt.Dimension(1200, 520));
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
        pnlIteCadEnt.add(btnVolIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 90, 30));

        scrEstIteCadEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrEstIteCadEnt.setBorder(BorderFactory.createEmptyBorder());

        tblEstIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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

        pnlIteCadEnt.add(scrEstIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 660, 120));

        scrSelIteCadEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrSelIteCadEnt.setBorder(BorderFactory.createEmptyBorder());

        tblSelIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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

        pnlIteCadEnt.add(scrSelIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, 660, 120));

        lblSelIteCadEnt.setFont(fontmed(11));
        lblSelIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSelIteCadEnt.setText("Ítens selecionados");
        pnlIteCadEnt.add(lblSelIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 280, 170, 20));

        lblEstIteCadEnt.setFont(fontmed(11));
        lblEstIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblEstIteCadEnt.setText("Ítens do estoque");
        pnlIteCadEnt.add(lblEstIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 130, 20));

        rbtnAssIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteCadEnt.add(rbtnAssIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 100, -1));

        rbtnPelIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteCadEnt.add(rbtnPelIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 80, -1));

        rbtnCapIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteCadEnt.add(rbtnCapIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 90, -1));

        rbtnChiIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteCadEnt.add(rbtnChiIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 70, -1));

        lblBusIteCadEnt.setFont(fontmed(12));
        lblBusIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblBusIteCadEnt.setText("Buscar");
        lblBusIteCadEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlIteCadEnt.add(lblBusIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 50, 20));

        txtBusIteCadEnt.setBackground(new java.awt.Color(241, 241, 241));
        txtBusIteCadEnt.setFont(fontmed(13));
        txtBusIteCadEnt.setBorder(null);
        txtBusIteCadEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusIteCadEntFocusLost(evt);
            }
        });
        txtBusIteCadEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtBusIteCadEntMousePressed(evt);
            }
        });
        txtBusIteCadEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusIteCadEntActionPerformed(evt);
            }
        });
        txtBusIteCadEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusIteCadEntKeyPressed(evt);
            }
        });
        pnlIteCadEnt.add(txtBusIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 200, 20));

        sepBusIteCadEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlIteCadEnt.add(sepBusIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 200, 10));

        lblNovaEntradaItens.setFont(fontbold(25));
        lblNovaEntradaItens.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens.setText("Nova Entrada | Ítens do Estoque");
        pnlIteCadEnt.add(lblNovaEntradaItens, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 1200, 30));

        pnlPrincipal.add(pnlIteCadEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlConEnt.setBackground(new java.awt.Color(241, 241, 241));
        pnlConEnt.setLayout(null);

        lblConsultarEntrada.setFont(fontbold(25));
        lblConsultarEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsultarEntrada.setText("Consultar Entrada");
        pnlConEnt.add(lblConsultarEntrada);
        lblConsultarEntrada.setBounds(0, 30, 1200, 30);

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
        btnCanConEnt.setBounds(600, 140, 90, 30);

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
        btnBusConEnt.setBounds(500, 140, 90, 30);

        lblBusConEnt.setFont(fontmed(12));
        lblBusConEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblBusConEnt.setText("Buscar");
        lblBusConEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlConEnt.add(lblBusConEnt);
        lblBusConEnt.setBounds(450, 100, 50, 20);

        txtBusConEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtBusConEnt.setBounds(450, 100, 290, 20);

        sepBusConEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlConEnt.add(sepBusConEst1);
        sepBusConEst1.setBounds(450, 120, 290, 10);

        scrConEnt.setBackground(new java.awt.Color(241, 241, 241));
        scrConEnt.setBorder(BorderFactory.createEmptyBorder());

        tblConEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        scrConEnt.setBounds(60, 200, 1080, 240);

        pnlPrincipal.add(pnlConEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlGerEnt.setBackground(new java.awt.Color(241, 241, 241));
        pnlGerEnt.setMinimumSize(new java.awt.Dimension(1200, 520));
        pnlGerEnt.setPreferredSize(new java.awt.Dimension(1200, 520));
        pnlGerEnt.setLayout(null);

        lblGerenciarEntrada.setFont(fontbold(25));
        lblGerenciarEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGerenciarEntrada.setText("Gerenciar Entrada");
        pnlGerEnt.add(lblGerenciarEntrada);
        lblGerenciarEntrada.setBounds(0, 30, 1200, 30);

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
        btnBusGerEnt.setBounds(500, 140, 90, 30);

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
        btnCanGerEnt.setBounds(600, 140, 90, 30);

        btnAltGerEnt.setFont(fontmed(12));
        btnAltGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnAltGerEnt.setText("Alterar");
        btnAltGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltGerEnt.setEnabled(false);
        btnAltGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(btnAltGerEnt);
        btnAltGerEnt.setBounds(1000, 400, 80, 30);

        btnExcGerEnt.setFont(fontmed(12));
        btnExcGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnExcGerEnt.setText("Excluir");
        btnExcGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcGerEnt.setEnabled(false);
        btnExcGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcGerEntActionPerformed(evt);
            }
        });
        pnlGerEnt.add(btnExcGerEnt);
        btnExcGerEnt.setBounds(1090, 400, 80, 30);

        lblDatBusGerEnt.setFont(fontmed(12));
        lblDatBusGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDatBusGerEnt.setText("Data");
        lblDatBusGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEnt.add(lblDatBusGerEnt);
        lblDatBusGerEnt.setBounds(500, 100, 50, 20);

        txtDatBusGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatBusGerEnt.setBounds(500, 100, 190, 20);

        sepBusGerEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEnt.add(sepBusGerEst1);
        sepBusGerEst1.setBounds(500, 120, 190, 10);

        scrGerEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrGerEnt.setBorder(BorderFactory.createEmptyBorder());

        tblConEst.setEnabled(false);
        tblGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        tblGerEnt.setName(""); // NOI18N
        tblGerEnt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGerEntMouseClicked(evt);
            }
        });
        scrGerEnt.setViewportView(tblGerEnt);

        pnlGerEnt.add(scrGerEnt);
        scrGerEnt.setBounds(80, 210, 1090, 170);

        pnlPrincipal.add(pnlGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlAlterarEntrada.setBackground(new java.awt.Color(241, 241, 241));
        pnlAlterarEntrada.setMinimumSize(new java.awt.Dimension(1200, 520));
        pnlAlterarEntrada.setLayout(null);

        lblAlterarEntrada.setFont(fontbold(25));
        lblAlterarEntrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlterarEntrada.setText("Alterar Entrada");
        pnlAlterarEntrada.add(lblAlterarEntrada);
        lblAlterarEntrada.setBounds(0, 30, 1200, 30);

        btnCanAltEnt.setFont(fontmed(12));
        btnCanAltEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnCanAltEnt.setText("Cancelar");
        btnCanAltEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanAltEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanAltEntActionPerformed(evt);
            }
        });
        pnlAlterarEntrada.add(btnCanAltEnt);
        btnCanAltEnt.setBounds(720, 330, 90, 30);

        btnIteGerEnt.setFont(fontmed(12));
        btnIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnIteGerEnt.setText("Estoque");
        btnIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIteGerEntActionPerformed(evt);
            }
        });
        pnlAlterarEntrada.add(btnIteGerEnt);
        btnIteGerEnt.setBounds(620, 290, 90, 30);

        btnSalGerEnt.setFont(fontmed(12));
        btnSalGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnSalGerEnt.setText("Salvar");
        btnSalGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalGerEntActionPerformed(evt);
            }
        });
        pnlAlterarEntrada.add(btnSalGerEnt);
        btnSalGerEnt.setBounds(720, 290, 90, 30);

        lblDatGerEnt.setFont(fontmed(12));
        lblDatGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDatGerEnt.setText("Data");
        lblDatGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlAlterarEntrada.add(lblDatGerEnt);
        lblDatGerEnt.setBounds(380, 150, 40, 20);

        txtDatGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlAlterarEntrada.add(txtDatGerEnt);
        txtDatGerEnt.setBounds(380, 150, 130, 20);

        sepDatGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlAlterarEntrada.add(sepDatGerEnt);
        sepDatGerEnt.setBounds(380, 170, 130, 10);

        lblR$GerEnt.setFont(fontmed(13));
        lblR$GerEnt.setText("R$");
        lblR$GerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        lblR$GerEnt.setFocusable(false);
        pnlAlterarEntrada.add(lblR$GerEnt);
        lblR$GerEnt.setBounds(380, 200, 20, 21);

        lblPreGerEnt.setFont(fontmed(12));
        lblPreGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblPreGerEnt.setText("Preço");
        lblPreGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlAlterarEntrada.add(lblPreGerEnt);
        lblPreGerEnt.setBounds(380, 200, 40, 20);

        txtPreGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlAlterarEntrada.add(txtPreGerEnt);
        txtPreGerEnt.setBounds(400, 200, 80, 20);

        sepPreGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlAlterarEntrada.add(sepPreGerEnt);
        sepPreGerEnt.setBounds(380, 220, 100, 10);

        lblDetGerEnt.setFont(fontmed(12));
        lblDetGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblDetGerEnt.setText("Detalhes");
        lblDetGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlAlterarEntrada.add(lblDetGerEnt);
        lblDetGerEnt.setBounds(380, 250, 70, 20);

        txtDetGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlAlterarEntrada.add(txtDetGerEnt);
        txtDetGerEnt.setBounds(380, 250, 190, 20);

        sepDetGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlAlterarEntrada.add(sepDetGerEnt);
        sepDetGerEnt.setBounds(380, 270, 190, 10);

        cmbSerGerEnt.setFont(fontmed(13));
        cmbSerGerEnt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o serviço" }));
        cmbSerGerEnt.setToolTipText("");
        cmbSerGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlAlterarEntrada.add(cmbSerGerEnt);
        cmbSerGerEnt.setBounds(380, 320, 190, 30);

        lblSerGerEnt.setFont(fontmed(12));
        lblSerGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSerGerEnt.setText("Serviço");
        pnlAlterarEntrada.add(lblSerGerEnt);
        lblSerGerEnt.setBounds(380, 290, 90, 30);

        rbtnCarGerEnt.setBackground(new java.awt.Color(241, 241, 241));
        btnGroup.add(rbtnCarGerEnt);
        rbtnCarGerEnt.setFont(fontmed(12));
        rbtnCarGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnCarGerEnt.setText("Cartão");
        rbtnCarGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnCarGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCarGerEntActionPerformed(evt);
            }
        });
        pnlAlterarEntrada.add(rbtnCarGerEnt);
        rbtnCarGerEnt.setBounds(582, 90, 90, 21);

        rbtnPixGerEnt.setBackground(new java.awt.Color(241, 241, 241));
        btnGroup.add(rbtnPixGerEnt);
        rbtnPixGerEnt.setFont(fontmed(12));
        rbtnPixGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnPixGerEnt.setText("PIX");
        rbtnPixGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnPixGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPixGerEntActionPerformed(evt);
            }
        });
        pnlAlterarEntrada.add(rbtnPixGerEnt);
        rbtnPixGerEnt.setBounds(672, 90, 90, 21);

        rbtnDinGerEnt.setBackground(new java.awt.Color(241, 241, 241));
        btnGroup.add(rbtnDinGerEnt);
        rbtnDinGerEnt.setFont(fontmed(12));
        rbtnDinGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        rbtnDinGerEnt.setText("Dinheiro");
        rbtnDinGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnDinGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnDinGerEntActionPerformed(evt);
            }
        });
        pnlAlterarEntrada.add(rbtnDinGerEnt);
        rbtnDinGerEnt.setBounds(482, 90, 90, 21);

        lblCliGerEnt.setFont(fontmed(12));
        lblCliGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCliGerEnt.setText("Cliente");
        lblCliGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlAlterarEntrada.add(lblCliGerEnt);
        lblCliGerEnt.setBounds(620, 150, 90, 20);

        txtCliGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlAlterarEntrada.add(txtCliGerEnt);
        txtCliGerEnt.setBounds(620, 150, 190, 20);

        sepCliGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlAlterarEntrada.add(sepCliGerEnt);
        sepCliGerEnt.setBounds(620, 170, 190, 10);

        lblCusGerEnt.setFont(fontmed(12));
        lblCusGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblCusGerEnt.setText("Custo");
        lblCusGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlAlterarEntrada.add(lblCusGerEnt);
        lblCusGerEnt.setBounds(620, 200, 40, 20);

        lblR$CusGerEnt.setFont(fontmed(13));
        lblR$CusGerEnt.setText("R$");
        lblR$CusGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlAlterarEntrada.add(lblR$CusGerEnt);
        lblR$CusGerEnt.setBounds(620, 200, 20, 20);

        txtCusGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlAlterarEntrada.add(txtCusGerEnt);
        txtCusGerEnt.setBounds(640, 200, 80, 20);

        sepCusGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlAlterarEntrada.add(sepCusGerEnt);
        sepCusGerEnt.setBounds(620, 220, 100, 10);

        lblForGerEnt.setFont(fontmed(12));
        lblForGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblForGerEnt.setText("Fornecedor");
        lblForGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlAlterarEntrada.add(lblForGerEnt);
        lblForGerEnt.setBounds(620, 250, 90, 20);

        txtForGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlAlterarEntrada.add(txtForGerEnt);
        txtForGerEnt.setBounds(620, 250, 190, 20);

        sepForGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        pnlAlterarEntrada.add(sepForGerEnt);
        sepForGerEnt.setBounds(620, 270, 190, 10);

        pnlPrincipal.add(pnlAlterarEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
        pnlIteGerEnt.setMinimumSize(new java.awt.Dimension(1200, 520));
        pnlIteGerEnt.setPreferredSize(new java.awt.Dimension(1200, 520));
        pnlIteGerEnt.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNovaEntradaItens1.setFont(fontbold(25));
        lblNovaEntradaItens1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens1.setText("Alterar Entrada | Ítens do Estoque");
        pnlIteGerEnt.add(lblNovaEntradaItens1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 1200, 30));

        scrEstIteGerEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrEstIteGerEnt.setBorder(BorderFactory.createEmptyBorder());
        scrEstIteGerEnt.setPreferredSize(new java.awt.Dimension(300, 80));

        tblEstIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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

        pnlIteGerEnt.add(scrEstIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 120, 660, 120));

        scrSelIteGerEnt.setBackground(new java.awt.Color(250, 250, 250));
        scrSelIteGerEnt.setBorder(BorderFactory.createEmptyBorder());
        scrSelIteGerEnt.setPreferredSize(new java.awt.Dimension(300, 80));

        tblSelIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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

        pnlIteGerEnt.add(scrSelIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, 660, 120));

        btnVolIteGerEnt.setFont(fontmed(12));
        btnVolIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        btnVolIteGerEnt.setText("Voltar");
        btnVolIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolIteGerEnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolIteGerEntActionPerformed(evt);
            }
        });
        pnlIteGerEnt.add(btnVolIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 90, 30));

        lblSelIteGerEnt.setFont(fontmed(11));
        lblSelIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblSelIteGerEnt.setText("Ítens selecionados");
        pnlIteGerEnt.add(lblSelIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 280, 170, 20));

        lblEstIteGerEnt.setFont(fontmed(11));
        lblEstIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblEstIteGerEnt.setText("Ítens do estoque");
        pnlIteGerEnt.add(lblEstIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 130, 20));

        rbtnAssIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteGerEnt.add(rbtnAssIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 100, -1));

        rbtnPelIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteGerEnt.add(rbtnPelIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 80, -1));

        rbtnCapIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteGerEnt.add(rbtnCapIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 90, -1));

        rbtnChiIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
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
        pnlIteGerEnt.add(rbtnChiIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 70, -1));

        lblBusIteGerEnt.setFont(fontmed(12));
        lblBusIteGerEnt.setForeground(new java.awt.Color(10, 60, 133));
        lblBusIteGerEnt.setText("Buscar");
        lblBusIteGerEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlIteGerEnt.add(lblBusIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 50, 20));

        txtBusIteGerEnt.setBackground(new java.awt.Color(241, 241, 241));
        txtBusIteGerEnt.setFont(fontmed(13));
        txtBusIteGerEnt.setBorder(null);
        txtBusIteGerEnt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBusIteGerEntFocusLost(evt);
            }
        });
        txtBusIteGerEnt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtBusIteGerEntMousePressed(evt);
            }
        });
        txtBusIteGerEnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusIteGerEntKeyPressed(evt);
            }
        });
        pnlIteGerEnt.add(txtBusIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 200, 20));

        sepBusIteCadEnt1.setForeground(new java.awt.Color(10, 60, 133));
        pnlIteGerEnt.add(sepBusIteCadEnt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 200, 10));

        pnlPrincipal.add(pnlIteGerEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlCadEst.setBackground(new java.awt.Color(241, 241, 241));
        pnlCadEst.setLayout(null);

        lblNovaEntradaItens2.setFont(fontbold(25));
        lblNovaEntradaItens2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens2.setText("Cadastrar Estoque");
        pnlCadEst.add(lblNovaEntradaItens2);
        lblNovaEntradaItens2.setBounds(0, 30, 1200, 30);

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
        btnSalCadEst.setBounds(360, 370, 90, 30);

        btnAdiCadEst.setFont(fontmed(12));
        btnAdiCadEst.setForeground(new java.awt.Color(10, 60, 133));
        btnAdiCadEst.setText("Adicionar");
        btnAdiCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdiCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdiCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(btnAdiCadEst);
        btnAdiCadEst.setBounds(1080, 210, 90, 30);

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
        btnCanCadEst.setBounds(460, 370, 90, 30);

        rbtnCapCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnCapCadEst.setBounds(410, 90, 90, 21);

        rbtnPelCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnPelCadEst.setBounds(520, 90, 80, 21);

        rbtnChiCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnChiCadEst.setBounds(620, 90, 60, 21);

        rbtnAceCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnAceCadEst.setBounds(700, 90, 100, 21);

        chkVarCorCadEst.setFont(fontmed(12));
        chkVarCorCadEst.setForeground(new java.awt.Color(10, 60, 133));
        chkVarCorCadEst.setText("Várias cores");
        chkVarCorCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkVarCorCadEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVarCorCadEstActionPerformed(evt);
            }
        });
        pnlCadEst.add(chkVarCorCadEst);
        chkVarCorCadEst.setBounds(880, 160, 150, 20);

        scrVarCorCadEst.setBackground(new java.awt.Color(250, 250, 250));
        scrVarCorCadEst.setBorder(BorderFactory.createEmptyBorder());

        tblVarCorCadEst.setBackground(new java.awt.Color(241, 241, 241));
        tblVarCorCadEst.setBorder(null);
        tblVarCorCadEst.setFont(fontmed(10));
        tblVarCorCadEst.setModel(new javax.swing.table.DefaultTableModel(
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
        tblVarCorCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblVarCorCadEst.setFocusable(false);
        tblVarCorCadEst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVarCorCadEstMouseClicked(evt);
            }
        });
        scrVarCorCadEst.setViewportView(tblVarCorCadEst);

        pnlCadEst.add(scrVarCorCadEst);
        scrVarCorCadEst.setBounds(880, 260, 290, 140);

        scrCadEst.setBackground(new java.awt.Color(250, 250, 250));
        scrCadEst.setBorder(BorderFactory.createEmptyBorder());

        tblCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        scrCadEst.setBounds(40, 150, 270, 250);

        lblVarCorCadEst.setFont(fontmed(12));
        lblVarCorCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblVarCorCadEst.setText("Cor");
        lblVarCorCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblVarCorCadEst);
        lblVarCorCadEst.setBounds(880, 220, 60, 20);

        txtVarCorCadEst.setBackground(new java.awt.Color(241, 241, 241));
        txtVarCorCadEst.setFont(fontmed(13));
        txtVarCorCadEst.setBorder(null);
        txtVarCorCadEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtVarCorCadEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtVarCorCadEstFocusLost(evt);
            }
        });
        pnlCadEst.add(txtVarCorCadEst);
        txtVarCorCadEst.setBounds(880, 220, 120, 20);

        sepVarCorCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepVarCorCadEst);
        sepVarCorCadEst.setBounds(880, 240, 120, 10);

        lblProCadEst.setFont(fontbold(12));
        lblProCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblProCadEst.setText("Produtos registrados");
        lblProCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblProCadEst);
        lblProCadEst.setBounds(40, 120, 160, 20);

        lblModCadEst.setFont(fontmed(12));
        lblModCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblModCadEst.setText("Modelo ");
        lblModCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblModCadEst);
        lblModCadEst.setBounds(360, 200, 70, 20);

        txtModCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtModCadEst.setBounds(360, 200, 190, 20);

        sepModCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepModCadEst);
        sepModCadEst.setBounds(360, 220, 190, 10);

        lblMarCadEst.setFont(fontmed(12));
        lblMarCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMarCadEst.setText("Marca");
        lblMarCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblMarCadEst);
        lblMarCadEst.setBounds(360, 150, 40, 20);

        txtMarCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtMarCadEst.setBounds(360, 150, 190, 20);

        sepMarCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepMarCadEst);
        sepMarCadEst.setBounds(360, 170, 190, 10);

        lblCorCadEst.setFont(fontmed(12));
        lblCorCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblCorCadEst.setText("Cor");
        lblCorCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblCorCadEst);
        lblCorCadEst.setBounds(650, 150, 30, 20);

        txtCorCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtCorCadEst.setBounds(650, 150, 190, 20);

        sepCorCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepCorCadEst);
        sepCorCadEst.setBounds(650, 170, 190, 10);

        lblMatCadEst.setFont(fontmed(12));
        lblMatCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMatCadEst.setText("Material");
        lblMatCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblMatCadEst);
        lblMatCadEst.setBounds(650, 200, 70, 20);

        txtMatCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtMatCadEst.setBounds(650, 200, 190, 20);

        sepMatCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepMatCadEst);
        sepMatCadEst.setBounds(650, 220, 190, 10);

        lblQuaCadEst.setFont(fontmed(12));
        lblQuaCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblQuaCadEst.setText("Quantidade");
        lblQuaCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblQuaCadEst);
        lblQuaCadEst.setBounds(360, 250, 80, 20);

        txtQuaCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtQuaCadEst.setBounds(360, 250, 90, 20);

        sepQuaCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepQuaCadEst);
        sepQuaCadEst.setBounds(360, 270, 90, 10);

        lblR$CadEst.setFont(fontmed(13));
        lblR$CadEst.setText("R$");
        lblR$CadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblR$CadEst);
        lblR$CadEst.setBounds(360, 300, 20, 21);

        lblPreCadEst.setFont(fontmed(12));
        lblPreCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblPreCadEst.setText("Preço");
        lblPreCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblPreCadEst);
        lblPreCadEst.setBounds(360, 300, 40, 20);

        txtPreCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtPreCadEst.setBounds(380, 300, 70, 20);

        sepPreCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepPreCadEst);
        sepPreCadEst.setBounds(360, 320, 90, 10);

        lblChiCadEst.setFont(fontmed(12));
        lblChiCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblChiCadEst.setText("Chip");
        pnlCadEst.add(lblChiCadEst);
        lblChiCadEst.setBounds(650, 340, 30, 30);

        cmbChiCadEst.setFont(fontmed(13));
        cmbChiCadEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o chip", "Triplo 4G HLR 230", "eSIM", "Naked" }));
        cmbChiCadEst.setToolTipText("");
        cmbChiCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlCadEst.add(cmbChiCadEst);
        cmbChiCadEst.setBounds(650, 370, 190, 30);

        lblLocCadEst.setFont(fontmed(12));
        lblLocCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblLocCadEst.setText("Local");
        lblLocCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblLocCadEst);
        lblLocCadEst.setBounds(650, 250, 40, 20);

        txtLocCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtLocCadEst.setBounds(650, 250, 190, 20);

        sepLocCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepLocCadEst);
        sepLocCadEst.setBounds(650, 270, 190, 10);

        lblDetCadEst.setFont(fontmed(12));
        lblDetCadEst.setForeground(new java.awt.Color(10, 60, 133));
        lblDetCadEst.setText("Detalhes");
        lblDetCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadEst.add(lblDetCadEst);
        lblDetCadEst.setBounds(650, 300, 70, 20);

        txtDetCadEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDetCadEst.setBounds(650, 300, 190, 20);

        sepDetCadEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadEst.add(sepDetCadEst);
        sepDetCadEst.setBounds(650, 320, 190, 10);

        txtTipCadEst.setBackground(new java.awt.Color(246, 246, 246));
        pnlCadEst.add(txtTipCadEst);
        txtTipCadEst.setBounds(250, 50, 40, 22);

        spnVarCorCadEst.setFont(fontmed(13));
        spnVarCorCadEst.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));
        spnVarCorCadEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        spnVarCorCadEst.setEditor(new javax.swing.JSpinner.NumberEditor(spnVarCorCadEst, ""));
        spnVarCorCadEst.setFocusable(false);
        JSpinner.DefaultEditor editorr = (JSpinner.DefaultEditor) spnVarCorCadEst.getEditor();
        editorr.getTextField().setEditable(false);
        pnlCadEst.add(spnVarCorCadEst);
        spnVarCorCadEst.setBounds(1010, 210, 60, 30);

        pnlPrincipal.add(pnlCadEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlConEst.setBackground(new java.awt.Color(241, 241, 241));
        pnlConEst.setLayout(null);

        lblNovaEntradaItens3.setFont(fontbold(25));
        lblNovaEntradaItens3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens3.setText("Consultar Estoque");
        pnlConEst.add(lblNovaEntradaItens3);
        lblNovaEntradaItens3.setBounds(0, 30, 1200, 30);

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
        btnCanConEst.setBounds(600, 190, 90, 30);

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
        btnBusConEst.setBounds(500, 190, 90, 30);

        rbtnCapConEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnCapConEst.setBounds(410, 90, 90, 21);

        rbtnPelConEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnPelConEst.setBounds(520, 90, 80, 21);

        rbtnChiConEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnChiConEst.setBounds(620, 90, 60, 21);

        rbtnAceConEst.setBackground(new java.awt.Color(241, 241, 241));
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
        rbtnAceConEst.setBounds(700, 90, 100, 21);

        lblBusConEst.setFont(fontmed(12));
        lblBusConEst.setForeground(new java.awt.Color(10, 60, 133));
        lblBusConEst.setText("Buscar");
        lblBusConEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlConEst.add(lblBusConEst);
        lblBusConEst.setBounds(450, 150, 50, 20);

        txtBusConEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtBusConEst.setBounds(450, 150, 290, 20);

        sepBusConEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlConEst.add(sepBusConEst);
        sepBusConEst.setBounds(450, 170, 290, 10);

        scrConEst.setBackground(new java.awt.Color(250, 250, 250));
        scrConEst.setBorder(BorderFactory.createEmptyBorder());

        tblConEst.setBackground(new java.awt.Color(241, 241, 241));
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
        scrConEst.setBounds(100, 270, 1000, 180);
        pnlConEst.add(txtTipConEst);
        txtTipConEst.setBounds(100, 230, 64, 22);

        pnlPrincipal.add(pnlConEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlGerEst.setBackground(new java.awt.Color(241, 241, 241));
        pnlGerEst.setMinimumSize(new java.awt.Dimension(1200, 520));
        pnlGerEst.setLayout(null);

        lblNovaEntradaItens4.setFont(fontbold(25));
        lblNovaEntradaItens4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens4.setText("Gerenciar Estoque");
        pnlGerEst.add(lblNovaEntradaItens4);
        lblNovaEntradaItens4.setBounds(0, 30, 1200, 30);

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
        btnExcGerEst.setBounds(830, 350, 90, 30);

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
        btnBusGerEst.setBounds(270, 200, 90, 30);

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
        btnAltGerEst.setBounds(730, 350, 90, 30);

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
        btnCanGerEst.setBounds(370, 200, 90, 30);

        chkAltGerEst.setFont(fontmed(12));
        chkAltGerEst.setForeground(new java.awt.Color(10, 60, 133));
        chkAltGerEst.setText("Alterar todas as linhas");
        chkAltGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlGerEst.add(chkAltGerEst);
        chkAltGerEst.setBounds(727, 320, 200, 20);

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
        rbtnCapGerEst.setBounds(190, 100, 90, 21);

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
        rbtnPelGerEst.setBounds(300, 100, 80, 21);

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
        rbtnChiGerEst.setBounds(400, 100, 60, 21);

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
        rbtnAceGerEst.setBounds(480, 100, 100, 21);

        txtPre1GerEst.setEditable(false);
        txtPre1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtPre1GerEst.setFont(fontmed(13));
        txtPre1GerEst.setBorder(null);
        txtPre1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtPre1GerEst.setFocusable(false);
        txtPre1GerEst.setOpaque(true);
        txtPre1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPre1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPre1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtPre1GerEst);
        txtPre1GerEst.setBounds(330, 460, 50, 20);

        txtQua1GerEst.setEditable(false);
        txtQua1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtQua1GerEst.setFont(fontmed(13));
        txtQua1GerEst.setBorder(null);
        txtQua1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtQua1GerEst.setFocusable(false);
        txtQua1GerEst.setOpaque(true);
        txtQua1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQua1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQua1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtQua1GerEst);
        txtQua1GerEst.setBounds(270, 460, 50, 20);

        txtMod1GerEst.setEditable(false);
        txtMod1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtMod1GerEst.setFont(fontmed(13));
        txtMod1GerEst.setBorder(null);
        txtMod1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtMod1GerEst.setFocusable(false);
        txtMod1GerEst.setOpaque(true);
        txtMod1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMod1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMod1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtMod1GerEst);
        txtMod1GerEst.setBounds(210, 460, 50, 20);

        txtMar1GerEst.setEditable(false);
        txtMar1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtMar1GerEst.setFont(fontmed(13));
        txtMar1GerEst.setBorder(null);
        txtMar1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtMar1GerEst.setFocusable(false);
        txtMar1GerEst.setOpaque(true);
        txtMar1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMar1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMar1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtMar1GerEst);
        txtMar1GerEst.setBounds(150, 460, 50, 20);

        txtDet1GerEst.setEditable(false);
        txtDet1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtDet1GerEst.setFont(fontmed(13));
        txtDet1GerEst.setBorder(null);
        txtDet1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtDet1GerEst.setFocusable(false);
        txtDet1GerEst.setOpaque(true);
        txtDet1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDet1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDet1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtDet1GerEst);
        txtDet1GerEst.setBounds(90, 460, 50, 20);

        txtLoc1GerEst.setEditable(false);
        txtLoc1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtLoc1GerEst.setFont(fontmed(13));
        txtLoc1GerEst.setBorder(null);
        txtLoc1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtLoc1GerEst.setFocusable(false);
        txtLoc1GerEst.setOpaque(true);
        txtLoc1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLoc1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLoc1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtLoc1GerEst);
        txtLoc1GerEst.setBounds(510, 460, 50, 20);

        txtMat1GerEst.setEditable(false);
        txtMat1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtMat1GerEst.setFont(fontmed(13));
        txtMat1GerEst.setBorder(null);
        txtMat1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtMat1GerEst.setFocusable(false);
        txtMat1GerEst.setOpaque(true);
        txtMat1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMat1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMat1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtMat1GerEst);
        txtMat1GerEst.setBounds(450, 460, 50, 20);

        txtCor1GerEst.setEditable(false);
        txtCor1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtCor1GerEst.setFont(fontmed(13));
        txtCor1GerEst.setBorder(null);
        txtCor1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtCor1GerEst.setFocusable(false);
        txtCor1GerEst.setOpaque(true);
        txtCor1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCor1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCor1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtCor1GerEst);
        txtCor1GerEst.setBounds(390, 460, 50, 20);

        txtChip1GerEst.setEditable(false);
        txtChip1GerEst.setBackground(new java.awt.Color(255, 255, 255));
        txtChip1GerEst.setFont(fontmed(13));
        txtChip1GerEst.setBorder(null);
        txtChip1GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtChip1GerEst.setFocusable(false);
        txtChip1GerEst.setOpaque(true);
        txtChip1GerEst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtChip1GerEstFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChip1GerEstFocusLost(evt);
            }
        });
        pnlGerEst.add(txtChip1GerEst);
        txtChip1GerEst.setBounds(570, 460, 50, 20);

        lblModGerEst.setFont(fontmed(12));
        lblModGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblModGerEst.setText("Modelo ");
        lblModGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblModGerEst);
        lblModGerEst.setBounds(730, 180, 70, 20);

        txtModGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtModGerEst.setBounds(730, 180, 190, 20);

        sepModGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepModGerEst);
        sepModGerEst.setBounds(730, 200, 190, 10);

        lblMarGerEst.setFont(fontmed(12));
        lblMarGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMarGerEst.setText("Marca");
        lblMarGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblMarGerEst);
        lblMarGerEst.setBounds(730, 130, 40, 20);

        txtMarGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtMarGerEst.setBounds(730, 130, 190, 20);

        sepMarGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepMarGerEst);
        sepMarGerEst.setBounds(730, 150, 190, 10);

        lblCorGerEst.setFont(fontmed(12));
        lblCorGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblCorGerEst.setText("Cor");
        lblCorGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblCorGerEst);
        lblCorGerEst.setBounds(970, 130, 30, 20);

        txtCorGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtCorGerEst.setBounds(970, 130, 190, 20);

        sepCorGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepCorGerEst);
        sepCorGerEst.setBounds(970, 150, 190, 10);

        lblMatGerEst.setFont(fontmed(12));
        lblMatGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblMatGerEst.setText("Material");
        lblMatGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblMatGerEst);
        lblMatGerEst.setBounds(970, 180, 80, 20);

        txtMatGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtMatGerEst.setBounds(970, 180, 190, 20);

        sepMatGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepMatGerEst);
        sepMatGerEst.setBounds(970, 200, 190, 10);

        lblQuaGerEst.setFont(fontmed(12));
        lblQuaGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblQuaGerEst.setText("Quantidade");
        lblQuaGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblQuaGerEst);
        lblQuaGerEst.setBounds(730, 230, 80, 20);

        txtQuaGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtQuaGerEst.setBounds(730, 230, 100, 20);

        sepQuaGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepQuaGerEst);
        sepQuaGerEst.setBounds(730, 250, 100, 10);

        lblR$GerEst.setFont(fontmed(13));
        lblR$GerEst.setText("R$");
        lblR$GerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblR$GerEst);
        lblR$GerEst.setBounds(730, 280, 20, 21);

        lblPreGerEst.setFont(fontmed(12));
        lblPreGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblPreGerEst.setText("Preço");
        lblPreGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblPreGerEst);
        lblPreGerEst.setBounds(730, 280, 40, 20);

        txtPreGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtPreGerEst.setBounds(750, 280, 83, 20);

        sepPreGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepPreGerEst);
        sepPreGerEst.setBounds(730, 300, 100, 10);

        lblChiGerEst.setFont(fontmed(12));
        lblChiGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblChiGerEst.setText("Chip");
        pnlGerEst.add(lblChiGerEst);
        lblChiGerEst.setBounds(970, 320, 30, 30);

        cmbChiGerEst.setFont(fontmed(13));
        cmbChiGerEst.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o chip", "Triplo 4G HLR 230", "eSIM", "Naked" }));
        cmbChiGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlGerEst.add(cmbChiGerEst);
        cmbChiGerEst.setBounds(970, 350, 190, 30);

        lblLocGerEst.setFont(fontmed(12));
        lblLocGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblLocGerEst.setText("Local");
        lblLocGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblLocGerEst);
        lblLocGerEst.setBounds(970, 230, 40, 20);

        txtLocGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtLocGerEst.setBounds(970, 230, 190, 20);

        sepLocGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepLocGerEst);
        sepLocGerEst.setBounds(970, 250, 190, 10);

        lblDetGerEst.setFont(fontmed(12));
        lblDetGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblDetGerEst.setText("Detalhes");
        lblDetGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblDetGerEst);
        lblDetGerEst.setBounds(970, 280, 70, 20);

        txtDetGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDetGerEst.setBounds(970, 280, 190, 20);

        sepDetGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepDetGerEst);
        sepDetGerEst.setBounds(970, 300, 190, 10);

        txtTipGerEst.setBackground(new java.awt.Color(246, 246, 246));
        pnlGerEst.add(txtTipGerEst);
        txtTipGerEst.setBounds(40, 230, 40, 22);

        lblBusGerEst.setFont(fontmed(12));
        lblBusGerEst.setForeground(new java.awt.Color(10, 60, 133));
        lblBusGerEst.setText("Buscar");
        lblBusGerEst.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerEst.add(lblBusGerEst);
        lblBusGerEst.setBounds(230, 160, 50, 20);

        txtBusGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        txtBusGerEst.setBounds(230, 160, 280, 20);

        sepBusGerEst.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerEst.add(sepBusGerEst);
        sepBusGerEst.setBounds(230, 180, 280, 10);

        scrGerEst.setBackground(new java.awt.Color(250, 250, 250));
        scrGerEst.setBorder(BorderFactory.createEmptyBorder());

        tblConEst.setEnabled(false);
        tblGerEst.setBackground(new java.awt.Color(241, 241, 241));
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
        scrGerEst.setBounds(40, 270, 650, 170);

        pnlPrincipal.add(pnlGerEst, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlCadVen.setBackground(new java.awt.Color(241, 241, 241));
        pnlCadVen.setLayout(null);

        lblCadastrarPlano.setFont(fontbold(25));
        lblCadastrarPlano.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCadastrarPlano.setText("Cadastrar Plano");
        pnlCadVen.add(lblCadastrarPlano);
        lblCadastrarPlano.setBounds(0, 30, 1200, 30);

        lblAceCadVen.setFont(fontmed(12));
        lblAceCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblAceCadVen.setText("Acesso");
        lblAceCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblAceCadVen);
        lblAceCadVen.setBounds(350, 260, 60, 20);

        txtAceCadVen.setBackground(new java.awt.Color(241, 241, 241));
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
        txtAceCadVen.setBounds(350, 260, 190, 20);

        sepTelCadVen1.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepTelCadVen1);
        sepTelCadVen1.setBounds(350, 280, 190, 10);

        lblVenCadVen.setFont(fontmed(12));
        lblVenCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblVenCadVen.setText("Vencimento");
        lblVenCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblVenCadVen);
        lblVenCadVen.setBounds(650, 210, 90, 20);

        txtVenCadVen.setBackground(new java.awt.Color(241, 241, 241));
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
        txtVenCadVen.setBounds(650, 210, 190, 20);

        sepVenCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepVenCadVen);
        sepVenCadVen.setBounds(650, 230, 190, 10);

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
        btnSalCadVen.setBounds(650, 260, 90, 30);

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
        btnCanCadVen.setBounds(750, 260, 90, 30);

        lblPlaCadVen.setFont(fontmed(12));
        lblPlaCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblPlaCadVen.setText("Plano");
        lblPlaCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblPlaCadVen);
        lblPlaCadVen.setBounds(650, 110, 50, 20);

        txtPlaCadVen.setBackground(new java.awt.Color(241, 241, 241));
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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPlaCadVenKeyTyped(evt);
            }
        });
        pnlCadVen.add(txtPlaCadVen);
        txtPlaCadVen.setBounds(650, 110, 190, 20);

        sepPlaCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepPlaCadVen);
        sepPlaCadVen.setBounds(650, 130, 190, 10);

        lblCliCadVen.setFont(fontmed(12));
        lblCliCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblCliCadVen.setText("Cliente");
        lblCliCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblCliCadVen);
        lblCliCadVen.setBounds(350, 110, 60, 20);

        txtCliCadVen.setBackground(new java.awt.Color(241, 241, 241));
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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCliCadVenKeyTyped(evt);
            }
        });
        pnlCadVen.add(txtCliCadVen);
        txtCliCadVen.setBounds(350, 110, 190, 20);

        sepCliCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepCliCadVen);
        sepCliCadVen.setBounds(350, 130, 190, 10);

        lblTelCadVen.setFont(fontmed(12));
        lblTelCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblTelCadVen.setText("Telefone");
        lblTelCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblTelCadVen);
        lblTelCadVen.setBounds(350, 210, 60, 20);

        txtTelCadVen.setBackground(new java.awt.Color(241, 241, 241));
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
        txtTelCadVen.setBounds(350, 210, 190, 20);

        sepTelCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepTelCadVen);
        sepTelCadVen.setBounds(350, 230, 190, 10);

        lblDatCadVen.setFont(fontmed(12));
        lblDatCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblDatCadVen.setText("Data");
        lblDatCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblDatCadVen);
        lblDatCadVen.setBounds(650, 160, 50, 20);

        txtDatCadVen.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatCadVen.setBounds(650, 160, 190, 20);

        sepDatCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepDatCadVen);
        sepDatCadVen.setBounds(650, 180, 190, 10);

        lblCli.setText("jLabel1");
        pnlCadVen.add(lblCli);
        lblCli.setBounds(150, 160, 37, 16);
        lblCli.setVisible(false);

        lblVen.setText("jLabel3");
        pnlCadVen.add(lblVen);
        lblVen.setBounds(150, 220, 37, 16);
        lblVen.setVisible(false);

        lblDat.setText("jLabel5");
        pnlCadVen.add(lblDat);
        lblDat.setBounds(150, 190, 37, 16);
        lblDat.setVisible(false);

        lblCpfCadVen.setFont(fontmed(12));
        lblCpfCadVen.setForeground(new java.awt.Color(10, 60, 133));
        lblCpfCadVen.setText("CPF");
        lblCpfCadVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadVen.add(lblCpfCadVen);
        lblCpfCadVen.setBounds(350, 160, 70, 20);

        sepCadVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadVen.add(sepCadVen);
        sepCadVen.setBounds(350, 180, 190, 10);

        txtCpfCadVen.setBackground(new java.awt.Color(241, 241, 241));
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
        txtCpfCadVen.setBounds(350, 160, 190, 20);

        pnlPrincipal.add(pnlCadVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlVen.setBackground(new java.awt.Color(241, 241, 241));
        pnlVen.setLayout(null);

        lblNovaEntradaItens14.setFont(fontbold(25));
        lblNovaEntradaItens14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens14.setText("Planos");
        pnlVen.add(lblNovaEntradaItens14);
        lblNovaEntradaItens14.setBounds(0, 30, 1200, 30);

        scrVen.setBackground(new java.awt.Color(241, 241, 241));
        scrVen.setBorder(BorderFactory.createEmptyBorder());

        tblVen.setBackground(new java.awt.Color(241, 241, 241));
        tblVen.setBorder(null);
        tblVen.setFont(fontmed(10));
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
        tblVen.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblVen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVenMouseClicked(evt);
            }
        });
        scrVen.setViewportView(tblVen);

        pnlVen.add(scrVen);
        scrVen.setBounds(70, 100, 1060, 220);

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
        btnCopAVen.setBounds(810, 343, 100, 30);

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
        btnCopVen.setBounds(700, 343, 100, 30);

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
        btnAltVen.setBounds(920, 343, 100, 30);

        btnWppVen.setVisible(false);
        btnWppVen.setFont(fontmed(12));
        btnWppVen.setForeground(new java.awt.Color(10, 60, 133));
        btnWppVen.setText("Limpar");
        btnWppVen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWppVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWppVenActionPerformed(evt);
            }
        });
        pnlVen.add(btnWppVen);
        btnWppVen.setBounds(1030, 390, 100, 30);

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
        btnVolVen.setBounds(70, 343, 100, 30);

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
        btnExcVen.setBounds(1030, 343, 100, 30);

        lblConPlaVen.setFont(fontbold(12));
        lblConPlaVen.setForeground(new java.awt.Color(10, 60, 133));
        lblConPlaVen.setText("0");
        lblConPlaVen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pnlVen.add(lblConPlaVen);
        lblConPlaVen.setBounds(225, 381, 50, 20);

        lblBusVen2.setFont(fontmed(11));
        lblBusVen2.setForeground(new java.awt.Color(10, 60, 133));
        lblBusVen2.setText("Planos ativados este mês:");
        lblBusVen2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pnlVen.add(lblBusVen2);
        lblBusVen2.setBounds(70, 380, 160, 20);

        lblErrVen.setFont(fontbold(10));
        lblErrVen.setForeground(new java.awt.Color(204, 51, 0));
        lblErrVen.setText("Nenhum registro encontrado!");
        lblErrVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlVen.add(lblErrVen);
        lblErrVen.setBounds(350, 370, 200, 30);

        lblBusVen.setFont(fontmed(12));
        lblBusVen.setForeground(new java.awt.Color(10, 60, 133));
        lblBusVen.setText("Buscar");
        lblBusVen.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlVen.add(lblBusVen);
        lblBusVen.setBounds(350, 350, 90, 20);

        txtBusVen.setBackground(new java.awt.Color(241, 241, 241));
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
        txtBusVen.setBounds(350, 350, 210, 20);

        sepBusVen.setForeground(new java.awt.Color(10, 60, 133));
        pnlVen.add(sepBusVen);
        sepBusVen.setBounds(350, 370, 210, 10);

        pnlPrincipal.add(pnlVen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlMas.setBackground(new java.awt.Color(241, 241, 241));
        pnlMas.setLayout(null);

        lblNovaEntradaItens10.setFont(fontbold(25));
        lblNovaEntradaItens10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens10.setText("Máscara de Plano");
        pnlMas.add(lblNovaEntradaItens10);
        lblNovaEntradaItens10.setBounds(0, 30, 1200, 30);

        chkAppMas.setFont(fontmed(12));
        chkAppMas.setForeground(new java.awt.Color(10, 60, 133));
        chkAppMas.setText("APP MEU TIM");
        pnlMas.add(chkAppMas);
        chkAppMas.setBounds(570, 360, 150, 20);

        chkMelMas.setFont(fontmed(12));
        chkMelMas.setForeground(new java.awt.Color(10, 60, 133));
        chkMelMas.setText("Melhor Data");
        pnlMas.add(chkMelMas);
        chkMelMas.setBounds(570, 320, 150, 20);

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
        btnConMas.setBounds(80, 350, 100, 30);

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
        btnVenMas.setBounds(190, 310, 100, 30);

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
        btnGerMas.setBounds(80, 310, 100, 30);

        btnCanMas.setFont(fontmed(12));
        btnCanMas.setForeground(new java.awt.Color(10, 60, 133));
        btnCanMas.setText("Cancelar");
        btnCanMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCanMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanMasActionPerformed(evt);
            }
        });
        pnlMas.add(btnCanMas);
        btnCanMas.setBounds(80, 390, 100, 30);

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
        btnParMas.setBounds(190, 350, 100, 30);

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
        btnCopMas.setBounds(820, 410, 50, 20);

        lblNomMas.setFont(fontmed(12));
        lblNomMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNomMas.setText("Nome");
        lblNomMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNomMas);
        lblNomMas.setBounds(80, 160, 70, 20);

        txtNomMas.setBackground(new java.awt.Color(241, 241, 241));
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
        txtNomMas.setBounds(80, 160, 190, 20);

        sepDesGerTipSer1.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer1);
        sepDesGerTipSer1.setBounds(80, 180, 190, 10);

        sepDesGerTipSer2.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer2);
        sepDesGerTipSer2.setBounds(80, 180, 190, 10);

        lblNumConMas.setFont(fontmed(12));
        lblNumConMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNumConMas.setText("Número de Contato");
        lblNumConMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNumConMas);
        lblNumConMas.setBounds(340, 160, 140, 20);

        sepDesGerTipSer3.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer3);
        sepDesGerTipSer3.setBounds(340, 180, 170, 10);

        txtNumConMas.setBackground(new java.awt.Color(241, 241, 241));
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
        txtNumConMas.setBounds(340, 160, 170, 20);

        lblCpfMas.setFont(fontmed(12));
        lblCpfMas.setForeground(new java.awt.Color(10, 60, 133));
        lblCpfMas.setText("CPF");
        lblCpfMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblCpfMas);
        lblCpfMas.setBounds(80, 210, 70, 20);

        sepDesGerTipSer4.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer4);
        sepDesGerTipSer4.setBounds(80, 230, 150, 10);

        txtCpfMas.setBackground(new java.awt.Color(241, 241, 241));
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
        txtCpfMas.setBounds(80, 210, 150, 20);

        lblNumAceMas.setFont(fontmed(12));
        lblNumAceMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNumAceMas.setText("Número de Acesso");
        lblNumAceMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNumAceMas);
        lblNumAceMas.setBounds(340, 210, 130, 20);

        sepDesGerTipSer5.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer5);
        sepDesGerTipSer5.setBounds(340, 230, 170, 10);

        txtNumAceMas.setBackground(new java.awt.Color(241, 241, 241));
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
        txtNumAceMas.setBounds(340, 210, 170, 20);

        lblNumPorMas.setFont(fontmed(12));
        lblNumPorMas.setForeground(new java.awt.Color(10, 60, 133));
        lblNumPorMas.setText("Número Portado");
        lblNumPorMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblNumPorMas);
        lblNumPorMas.setBounds(340, 260, 140, 20);

        sepDesGerTipSer6.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer6);
        sepDesGerTipSer6.setBounds(340, 280, 170, 10);

        txtNumPorMas.setBackground(new java.awt.Color(241, 241, 241));
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
        txtNumPorMas.setBounds(340, 260, 170, 20);

        btnGroup.add(rbtnMigTroMas);
        rbtnMigTroMas.setFont(fontmed(12));
        rbtnMigTroMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnMigTroMas.setText("Conversão");
        rbtnMigTroMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnMigTroMas);
        rbtnMigTroMas.setBounds(570, 180, 130, 21);

        btnGroup.add(rbtnAtiMas);
        rbtnAtiMas.setFont(fontmed(12));
        rbtnAtiMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnAtiMas.setSelected(true);
        rbtnAtiMas.setText("Ativação");
        rbtnAtiMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnAtiMas);
        rbtnAtiMas.setBounds(570, 120, 90, 21);

        btnGroup.add(rbtnMigMas);
        rbtnMigMas.setFont(fontmed(12));
        rbtnMigMas.setForeground(new java.awt.Color(10, 60, 133));
        rbtnMigMas.setText("Migração");
        rbtnMigMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(rbtnMigMas);
        rbtnMigMas.setBounds(570, 150, 90, 21);

        lblPlaMas.setFont(fontmed(12));
        lblPlaMas.setForeground(new java.awt.Color(10, 60, 133));
        lblPlaMas.setText("Plano");
        lblPlaMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblPlaMas);
        lblPlaMas.setBounds(340, 310, 100, 20);

        sepDesGerTipSer7.setForeground(new java.awt.Color(10, 60, 133));
        sepDesGerTipSer7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlMas.add(sepDesGerTipSer7);
        sepDesGerTipSer7.setBounds(760, 110, 10, 260);

        txtPlaMas.setBackground(new java.awt.Color(241, 241, 241));
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
        txtPlaMas.setBounds(340, 310, 170, 20);

        lblVenMas.setBackground(new java.awt.Color(241, 241, 241));
        lblVenMas.setFont(fontmed(12));
        lblVenMas.setForeground(new java.awt.Color(10, 60, 133));
        lblVenMas.setText("Vencimento");
        lblVenMas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlMas.add(lblVenMas);
        lblVenMas.setBounds(80, 260, 80, 20);

        sepDesGerTipSer8.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer8);
        sepDesGerTipSer8.setBounds(80, 280, 110, 10);

        txtVenMas.setBackground(new java.awt.Color(241, 241, 241));
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
        txtVenMas.setBounds(80, 260, 110, 20);

        sepDesGerTipSer9.setForeground(new java.awt.Color(10, 60, 133));
        pnlMas.add(sepDesGerTipSer9);
        sepDesGerTipSer9.setBounds(340, 330, 170, 10);

        jScrollPane1.setBackground(new java.awt.Color(240, 240, 240));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txtAreMas.setEditable(false);
        txtAreMas.setBackground(new java.awt.Color(241, 241, 241));
        txtAreMas.setColumns(20);
        txtAreMas.setFont(fontmed(10));
        txtAreMas.setForeground(new java.awt.Color(10, 60, 133));
        txtAreMas.setRows(5);
        txtAreMas.setBorder(null);
        txtAreMas.setFocusable(false);
        jScrollPane1.setViewportView(txtAreMas);

        pnlMas.add(jScrollPane1);
        jScrollPane1.setBounds(820, 110, 300, 290);

        btnGroup4.add(chkDebMas);
        chkDebMas.setFont(fontmed(12));
        chkDebMas.setForeground(new java.awt.Color(10, 60, 133));
        chkDebMas.setText("Débito Automático");
        chkDebMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(chkDebMas);
        chkDebMas.setBounds(570, 250, 170, 21);

        btnGroup4.add(chkCarMas);
        chkCarMas.setFont(fontmed(12));
        chkCarMas.setForeground(new java.awt.Color(10, 60, 133));
        chkCarMas.setText("Cartão de Crédito");
        chkCarMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(chkCarMas);
        chkCarMas.setBounds(570, 280, 170, 21);

        btnGroup4.add(chkBolMas);
        chkBolMas.setFont(fontmed(12));
        chkBolMas.setForeground(new java.awt.Color(10, 60, 133));
        chkBolMas.setSelected(true);
        chkBolMas.setText("Boleto");
        chkBolMas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlMas.add(chkBolMas);
        chkBolMas.setBounds(570, 220, 130, 21);

        pnlPrincipal.add(pnlMas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlOs.setBackground(new java.awt.Color(241, 241, 241));
        pnlOs.setLayout(null);

        lblNovaEntradaItens6.setFont(fontbold(25));
        lblNovaEntradaItens6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens6.setText("Gerar OS");
        pnlOs.add(lblNovaEntradaItens6);
        lblNovaEntradaItens6.setBounds(0, 30, 1200, 30);

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
        btnGerOs.setBounds(790, 360, 90, 30);

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
        chkGarOs.setBounds(640, 350, 80, 30);

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
        btnCanOs.setBounds(790, 400, 90, 30);

        lblEndOs.setFont(fontmed(12));
        lblEndOs.setForeground(new java.awt.Color(10, 60, 133));
        lblEndOs.setText("Endereço");
        lblEndOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblEndOs);
        lblEndOs.setBounds(310, 210, 60, 20);

        txtEndOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtEndOs.setBounds(310, 210, 240, 20);

        sepModCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepModCadEst1);
        sepModCadEst1.setBounds(310, 230, 240, 10);

        lblCliOs.setFont(fontmed(12));
        lblCliOs.setForeground(new java.awt.Color(10, 60, 133));
        lblCliOs.setText("Cliente");
        lblCliOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblCliOs);
        lblCliOs.setBounds(310, 110, 60, 20);

        txtCliOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtCliOs.setBounds(310, 110, 240, 20);

        sepMarCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepMarCadEst1);
        sepMarCadEst1.setBounds(310, 130, 240, 10);

        lblEquOs.setFont(fontmed(12));
        lblEquOs.setForeground(new java.awt.Color(10, 60, 133));
        lblEquOs.setText("Equipamento");
        lblEquOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblEquOs);
        lblEquOs.setBounds(640, 110, 130, 20);

        txtEquOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtEquOs.setBounds(640, 110, 240, 20);

        sepCorCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepCorCadEst1);
        sepCorCadEst1.setBounds(640, 130, 240, 10);

        lblMarOs.setFont(fontmed(12));
        lblMarOs.setForeground(new java.awt.Color(10, 60, 133));
        lblMarOs.setText("Marca");
        lblMarOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblMarOs);
        lblMarOs.setBounds(640, 160, 50, 20);

        txtMarOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtMarOs.setBounds(640, 160, 240, 20);

        sepMatCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepMatCadEst1);
        sepMatCadEst1.setBounds(640, 180, 240, 10);

        lblTelOs.setFont(fontmed(12));
        lblTelOs.setForeground(new java.awt.Color(10, 60, 133));
        lblTelOs.setText("Telefone");
        lblTelOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblTelOs);
        lblTelOs.setBounds(310, 160, 80, 20);

        txtTelOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtTelOs.setBounds(310, 160, 130, 20);

        sepQuaCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepQuaCadEst1);
        sepQuaCadEst1.setBounds(310, 180, 130, 10);

        lblModOs.setFont(fontmed(12));
        lblModOs.setForeground(new java.awt.Color(10, 60, 133));
        lblModOs.setText("Modelo");
        lblModOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblModOs);
        lblModOs.setBounds(640, 210, 60, 20);

        txtModOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtModOs.setBounds(640, 210, 240, 20);

        sepLocCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepLocCadEst1);
        sepLocCadEst1.setBounds(640, 230, 240, 10);

        lblConOs.setFont(fontmed(12));
        lblConOs.setForeground(new java.awt.Color(10, 60, 133));
        lblConOs.setText("Defeito relatado");
        lblConOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblConOs);
        lblConOs.setBounds(640, 260, 130, 20);

        txtDefOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDefOs.setBounds(640, 260, 240, 20);

        sepDetCadEst1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst1);
        sepDetCadEst1.setBounds(640, 280, 240, 10);

        lblDefOs.setFont(fontmed(12));
        lblDefOs.setForeground(new java.awt.Color(10, 60, 133));
        lblDefOs.setText("Reparo");
        lblDefOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblDefOs);
        lblDefOs.setBounds(640, 310, 100, 20);

        txtRepOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtRepOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRepOsKeyTyped(evt);
            }
        });
        pnlOs.add(txtRepOs);
        txtRepOs.setBounds(640, 310, 240, 20);

        sepDetCadEst2.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst2);
        sepDetCadEst2.setBounds(640, 330, 240, 10);

        lblDatEntOs.setFont(fontmed(12));
        lblDatEntOs.setForeground(new java.awt.Color(10, 60, 133));
        lblDatEntOs.setText("Data entrada");
        lblDatEntOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblDatEntOs);
        lblDatEntOs.setBounds(310, 260, 90, 20);

        txtDatOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatOs.setBounds(310, 260, 90, 20);

        sepDetCadEst3.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst3);
        sepDetCadEst3.setBounds(310, 280, 90, 10);

        lblHorOs.setFont(fontmed(12));
        lblHorOs.setForeground(new java.awt.Color(10, 60, 133));
        lblHorOs.setText("Data saída");
        lblHorOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblHorOs);
        lblHorOs.setBounds(310, 310, 80, 20);

        txtDatSaiOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatSaiOs.setBounds(310, 310, 90, 20);

        sepDetCadEst4.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepDetCadEst4);
        sepDetCadEst4.setBounds(310, 330, 90, 10);

        sepCusGerEnt1.setForeground(new java.awt.Color(10, 60, 133));
        pnlOs.add(sepCusGerEnt1);
        sepCusGerEnt1.setBounds(310, 380, 90, 10);

        lblPreOs.setFont(fontmed(12));
        lblPreOs.setForeground(new java.awt.Color(10, 60, 133));
        lblPreOs.setText("Preço");
        lblPreOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblPreOs);
        lblPreOs.setBounds(310, 360, 40, 20);

        lblR$Os.setFont(fontmed(13));
        lblR$Os.setText("R$");
        lblR$Os.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlOs.add(lblR$Os);
        lblR$Os.setBounds(310, 360, 20, 20);

        txtPreOs.setBackground(new java.awt.Color(241, 241, 241));
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
        txtPreOs.setBounds(330, 360, 70, 20);

        pnlPrincipal.add(pnlOs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlGerOs.setBackground(new java.awt.Color(241, 241, 241));
        pnlGerOs.setLayout(null);

        lblGerarOS.setFont(fontbold(25));
        lblGerarOS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGerarOS.setText("Gerenciar OS");
        pnlGerOs.add(lblGerarOS);
        lblGerarOS.setBounds(0, 30, 1200, 30);

        scrOs.setBackground(new java.awt.Color(250, 250, 250));
        scrOs.setBorder(BorderFactory.createEmptyBorder());

        tblOs.setBackground(new java.awt.Color(241, 241, 241));
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
        scrOs.setBounds(50, 110, 1100, 250);

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
        btnAltGerOs.setBounds(940, 383, 100, 30);

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
        btnGerGerOs.setBounds(830, 383, 100, 30);

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
        btnExcGerOs.setBounds(1050, 383, 100, 30);

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
        btnVolGerOs.setBounds(50, 383, 100, 30);

        lblErrGerOs.setFont(fontbold(10));
        lblErrGerOs.setForeground(new java.awt.Color(204, 51, 0));
        lblErrGerOs.setText("Nenhum registro encontrado!");
        lblErrGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerOs.add(lblErrGerOs);
        lblErrGerOs.setBounds(580, 415, 190, 20);

        lblBusGerOs.setFont(fontmed(12));
        lblBusGerOs.setForeground(new java.awt.Color(10, 60, 133));
        lblBusGerOs.setText("Buscar");
        lblBusGerOs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerOs.add(lblBusGerOs);
        lblBusGerOs.setBounds(580, 390, 80, 20);

        txtBusGerOs.setBackground(new java.awt.Color(241, 241, 241));
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
        });
        pnlGerOs.add(txtBusGerOs);
        txtBusGerOs.setBounds(580, 390, 200, 20);

        sepBusVen1.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerOs.add(sepBusVen1);
        sepBusVen1.setBounds(580, 410, 200, 10);

        pnlPrincipal.add(pnlGerOs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlRel.setBackground(new java.awt.Color(241, 241, 241));
        pnlRel.setLayout(null);

        lblNovaEntradaItens5.setFont(fontbold(25));
        lblNovaEntradaItens5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens5.setText("Relatório");
        pnlRel.add(lblNovaEntradaItens5);
        lblNovaEntradaItens5.setBounds(0, 30, 1200, 30);

        lblResRel.setFont(fontbold(14));
        lblResRel.setForeground(new java.awt.Color(10, 60, 133));
        lblResRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblResRel.setText("Sem resultados para o período selecionado!");
        lblResRel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlRel.add(lblResRel);
        lblResRel.setBounds(60, 350, 780, 20);

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
        cmbRel.setBounds(650, 213, 190, 30);

        scrRel.setBackground(new java.awt.Color(250, 250, 250));
        scrRel.setBorder(BorderFactory.createEmptyBorder());

        tblRel.setBackground(new java.awt.Color(241, 241, 241));
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
        scrRel.setBounds(60, 260, 780, 180);

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
        btnVolRel.setBounds(60, 213, 90, 30);

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
        rbtnSerRel.setBounds(170, 100, 90, 21);

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
        rbtnVenRel1.setBounds(390, 100, 150, 21);

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
        rbtnVenRel.setBounds(280, 100, 80, 21);

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
        rbtnAssRel.setBounds(550, 100, 100, 21);

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
        rbtnTodRel.setBounds(60, 100, 70, 21);

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
        lblDatIniRel.setBounds(270, 220, 90, 20);

        txtDatIniRel.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatIniRel.setBounds(270, 220, 100, 20);

        sepDatCadEnt1.setForeground(new java.awt.Color(10, 60, 133));
        pnlRel.add(sepDatCadEnt1);
        sepDatCadEnt1.setBounds(270, 240, 100, 10);

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
        btnTodRel.setBounds(230, 160, 50, 20);

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
        btnAnoRel.setBounds(530, 160, 30, 20);

        lblDatFinRel.setFont(fontmed(12));
        lblDatFinRel.setForeground(new java.awt.Color(10, 60, 133));
        lblDatFinRel.setText("Data final");
        lblDatFinRel.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlRel.add(lblDatFinRel);
        lblDatFinRel.setBounds(420, 220, 80, 20);

        txtDatFinRel.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatFinRel.setBounds(420, 220, 100, 20);

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
        lblDiaRel.setBounds(60, 170, 120, 20);

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
        btnMenDiaRel.setBounds(60, 140, 15, 20);

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
        btnNumDiaRel.setBounds(60, 155, 100, 20);

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
        btnMaiDiaRel.setBounds(80, 140, 15, 20);

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
        btnDiaRel.setBounds(310, 160, 30, 20);

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
        btnMesRel.setBounds(460, 160, 30, 20);

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
        btnSemRel.setBounds(360, 160, 80, 20);

        sepDatCadEnt3.setForeground(new java.awt.Color(10, 60, 133));
        pnlRel.add(sepDatCadEnt3);
        sepDatCadEnt3.setBounds(420, 240, 100, 10);

        lblValDinRel.setFont(fontbold(14));
        lblValDinRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValDinRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValDinRel.setText("R$0,00");
        pnlRel.add(lblValDinRel);
        lblValDinRel.setBounds(1020, 290, 120, 30);

        jLabel7.setFont(fontmed(12));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Dinheiro");
        pnlRel.add(jLabel7);
        jLabel7.setBounds(1020, 270, 120, 20);

        lblTotEntRel.setFont(fontbold(14));
        lblTotEntRel.setForeground(new java.awt.Color(10, 60, 133));
        lblTotEntRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotEntRel.setText("0");
        pnlRel.add(lblTotEntRel);
        lblTotEntRel.setBounds(890, 290, 120, 30);

        jLabel2.setFont(fontmed(12));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Total de entradas");
        pnlRel.add(jLabel2);
        jLabel2.setBounds(890, 270, 120, 20);

        lblValTotRel.setFont(fontbold(14));
        lblValTotRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValTotRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValTotRel.setText("R$0,00");
        pnlRel.add(lblValTotRel);
        lblValTotRel.setBounds(890, 350, 120, 30);

        jLabel4.setFont(fontmed(12));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Valor total");
        pnlRel.add(jLabel4);
        jLabel4.setBounds(890, 330, 120, 20);

        lblValMedRel.setFont(fontbold(14));
        lblValMedRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValMedRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValMedRel.setText("R$0,00");
        pnlRel.add(lblValMedRel);
        lblValMedRel.setBounds(890, 410, 120, 30);

        jLabel6.setFont(fontmed(12));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Custo");
        pnlRel.add(jLabel6);
        jLabel6.setBounds(890, 390, 120, 20);

        jLabel8.setFont(fontmed(12));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Cartão");
        pnlRel.add(jLabel8);
        jLabel8.setBounds(1020, 330, 120, 20);

        lblValCarRel.setFont(fontbold(14));
        lblValCarRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValCarRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValCarRel.setText("R$0,00");
        pnlRel.add(lblValCarRel);
        lblValCarRel.setBounds(1020, 350, 120, 30);

        jLabel9.setFont(fontmed(12));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("PIX");
        pnlRel.add(jLabel9);
        jLabel9.setBounds(1020, 390, 120, 20);

        lblValPixRel.setFont(fontbold(14));
        lblValPixRel.setForeground(new java.awt.Color(10, 60, 133));
        lblValPixRel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValPixRel.setText("R$0,00");
        pnlRel.add(lblValPixRel);
        lblValPixRel.setBounds(1020, 410, 120, 30);

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
        chkCus.setBounds(680, 100, 60, 20);

        pnlPrincipal.add(pnlRel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlCadTipSer.setBackground(new java.awt.Color(241, 241, 241));
        pnlCadTipSer.setLayout(null);

        lblNovaEntradaItens8.setFont(fontbold(25));
        lblNovaEntradaItens8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens8.setText("Cadastrar Serviço");
        pnlCadTipSer.add(lblNovaEntradaItens8);
        lblNovaEntradaItens8.setBounds(0, 30, 1200, 30);

        btnSalTipSer.setFont(fontmed(12));
        btnSalTipSer.setForeground(new java.awt.Color(10, 60, 133));
        btnSalTipSer.setText("Salvar");
        btnSalTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalTipSer.setEnabled(false);
        btnSalTipSer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalTipSerActionPerformed(evt);
            }
        });
        pnlCadTipSer.add(btnSalTipSer);
        btnSalTipSer.setBounds(500, 220, 90, 30);

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
        btnCanTipSer.setBounds(600, 220, 90, 30);

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
        rbtnOutTipSer.setBounds(730, 100, 80, 21);

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
        rbtnSerTimTipSer.setBounds(420, 100, 70, 21);

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
        rbtnAssTipSer.setBounds(540, 100, 170, 21);

        lblDesTipSer.setFont(fontmed(12));
        lblDesTipSer.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer.setText("Descrição");
        lblDesTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadTipSer.add(lblDesTipSer);
        lblDesTipSer.setBounds(460, 170, 70, 20);

        txtDesTipSer.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDesTipSer.setBounds(460, 170, 280, 20);

        sepDesTipSer.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadTipSer.add(sepDesTipSer);
        sepDesTipSer.setBounds(460, 190, 280, 10);

        pnlPrincipal.add(pnlCadTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlGerTipSer.setBackground(new java.awt.Color(241, 241, 241));
        pnlGerTipSer.setLayout(null);

        lblNovaEntradaItens9.setFont(fontbold(25));
        lblNovaEntradaItens9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens9.setText("Gerenciar Serviço");
        pnlGerTipSer.add(lblNovaEntradaItens9);
        lblNovaEntradaItens9.setBounds(0, 30, 1200, 30);

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
        btnExcGerTipSer.setBounds(600, 380, 100, 30);

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
        btnAtvGerTipSer.setBounds(380, 380, 100, 30);

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
        btnAltGerTipSer.setBounds(490, 380, 100, 30);

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
        btnCanGerTipSer.setBounds(710, 380, 100, 30);

        lblDesTipSer2.setFont(fontmed(11));
        lblDesTipSer2.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer2.setText("Escolha o serviço para gerenciar");
        lblDesTipSer2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerTipSer.add(lblDesTipSer2);
        lblDesTipSer2.setBounds(460, 140, 260, 20);

        lblDesGerTipSer.setFont(fontmed(12));
        lblDesGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        lblDesGerTipSer.setText("Descrição");
        lblDesGerTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerTipSer.add(lblDesGerTipSer);
        lblDesGerTipSer.setBounds(460, 330, 70, 20);

        txtDesGerTipSer.setBackground(new java.awt.Color(241, 241, 241));
        txtDesGerTipSer.setFont(fontmed(13));
        txtDesGerTipSer.setBorder(null);
        pnlGerTipSer.add(txtDesGerTipSer);
        txtDesGerTipSer.setBounds(460, 330, 270, 20);

        sepDesGerTipSer.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerTipSer.add(sepDesGerTipSer);
        sepDesGerTipSer.setBounds(460, 350, 270, 10);

        scrTipSer.setBackground(new java.awt.Color(250, 250, 250));
        scrTipSer.setBorder(BorderFactory.createEmptyBorder());
        scrTipSer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tblTipSer.setTableHeader(null);
        tblTipSer.setBackground(new java.awt.Color(241, 241, 241));
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
        });
        scrTipSer.setViewportView(tblTipSer);

        pnlGerTipSer.add(scrTipSer);
        scrTipSer.setBounds(460, 170, 270, 120);

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
        rbtnOutGerTipSer.setBounds(720, 90, 100, 21);

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
        rbtnAssGerTipSer.setBounds(530, 90, 150, 21);

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
        rbtnTimGerTipSer.setBounds(410, 90, 80, 21);

        pnlPrincipal.add(pnlGerTipSer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlDes.setBackground(new java.awt.Color(241, 241, 241));
        pnlDes.setLayout(null);

        lblNovaEntradaItens11.setFont(fontbold(25));
        lblNovaEntradaItens11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens11.setText("Afazeres");
        pnlDes.add(lblNovaEntradaItens11);
        lblNovaEntradaItens11.setBounds(0, 30, 1200, 30);

        scrConDes.setBackground(new java.awt.Color(250, 250, 250));
        scrConDes.setBorder(BorderFactory.createEmptyBorder());

        tblConDes.setBackground(new java.awt.Color(241, 241, 241));
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
        scrConDes.setBounds(190, 110, 820, 230);

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
        btnVolDes.setBounds(190, 350, 90, 30);

        pnlPrincipal.add(pnlDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlCadDes.setBackground(new java.awt.Color(241, 241, 241));
        pnlCadDes.setLayout(null);

        lblNovaEntradaItens12.setFont(fontbold(25));
        lblNovaEntradaItens12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens12.setText("Cadastrar Afazer");
        pnlCadDes.add(lblNovaEntradaItens12);
        lblNovaEntradaItens12.setBounds(0, 30, 1200, 30);

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
        btnSalDes.setBounds(500, 260, 90, 30);

        lblDatDes.setFont(fontmed(12));
        lblDatDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDatDes.setText("Data");
        lblDatDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblDatDes);
        lblDatDes.setBounds(500, 210, 50, 20);

        txtDatDes.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatDes.setBounds(500, 210, 100, 20);

        sepDatCadEnt7.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadDes.add(sepDatCadEnt7);
        sepDatCadEnt7.setBounds(500, 230, 100, 10);

        lblDesDes.setFont(fontmed(12));
        lblDesDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDesDes.setText("Descrição");
        lblDesDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblDesDes);
        lblDesDes.setBounds(500, 110, 90, 20);

        txtDesDes.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDesDes.setBounds(500, 110, 190, 20);

        sepDatCadEnt4.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadDes.add(sepDatCadEnt4);
        sepDatCadEnt4.setBounds(500, 130, 190, 10);

        lblR$Des.setFont(fontmed(13));
        lblR$Des.setText("R$");
        lblR$Des.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblR$Des);
        lblR$Des.setBounds(500, 160, 20, 21);

        lblPreDes.setFont(fontmed(12));
        lblPreDes.setForeground(new java.awt.Color(10, 60, 133));
        lblPreDes.setText("Preço");
        lblPreDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlCadDes.add(lblPreDes);
        lblPreDes.setBounds(500, 160, 50, 20);

        txtPreDes.setBackground(new java.awt.Color(241, 241, 241));
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
        txtPreDes.setBounds(520, 160, 80, 20);

        sepDatCadEnt6.setForeground(new java.awt.Color(10, 60, 133));
        pnlCadDes.add(sepDatCadEnt6);
        sepDatCadEnt6.setBounds(500, 180, 100, 10);

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
        btnCanDes.setBounds(600, 260, 90, 30);

        pnlPrincipal.add(pnlCadDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlGerDes.setBackground(new java.awt.Color(241, 241, 241));
        pnlGerDes.setLayout(null);

        lblNovaEntradaItens13.setFont(fontbold(25));
        lblNovaEntradaItens13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNovaEntradaItens13.setText("Gerenciar Afazer");
        pnlGerDes.add(lblNovaEntradaItens13);
        lblNovaEntradaItens13.setBounds(0, 30, 1200, 30);

        lblDesGerDes.setFont(fontmed(12));
        lblDesGerDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDesGerDes.setText("Descrição");
        lblDesGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblDesGerDes);
        lblDesGerDes.setBounds(860, 160, 90, 20);

        lblDatGerDes.setFont(fontmed(12));
        lblDatGerDes.setForeground(new java.awt.Color(10, 60, 133));
        lblDatGerDes.setText("Data");
        lblDatGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblDatGerDes);
        lblDatGerDes.setBounds(860, 260, 50, 20);

        lblPreGerDes.setFont(fontmed(12));
        lblPreGerDes.setForeground(new java.awt.Color(10, 60, 133));
        lblPreGerDes.setText("Preço");
        lblPreGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblPreGerDes);
        lblPreGerDes.setBounds(860, 210, 50, 20);

        lblR$GerDes.setFont(fontmed(13));
        lblR$GerDes.setText("R$");
        lblR$GerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblR$GerDes);
        lblR$GerDes.setBounds(860, 210, 20, 20);

        txtDatGerDes.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDatGerDes.setBounds(860, 260, 100, 20);

        txtDesGerDes.setBackground(new java.awt.Color(241, 241, 241));
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
        txtDesGerDes.setBounds(860, 160, 190, 20);

        sepPreGerDes.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerDes.add(sepPreGerDes);
        sepPreGerDes.setBounds(860, 230, 100, 10);

        sepDatGerDes.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerDes.add(sepDatGerDes);
        sepDatGerDes.setBounds(860, 280, 100, 10);

        sepDesGerDes.setForeground(new java.awt.Color(10, 60, 133));
        pnlGerDes.add(sepDesGerDes);
        sepDesGerDes.setBounds(860, 180, 190, 10);

        txtPreGerDes.setBackground(new java.awt.Color(241, 241, 241));
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
        txtPreGerDes.setBounds(880, 210, 80, 20);

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
        btnExcGerDes.setBounds(960, 310, 90, 30);

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
        btnAltGerDes.setBounds(860, 310, 90, 30);

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
        btnCanGerDes.setBounds(960, 350, 90, 30);

        lblDesTipSer3.setFont(fontmed(12));
        lblDesTipSer3.setForeground(new java.awt.Color(10, 60, 133));
        lblDesTipSer3.setText("Escolha o afazer para alterar ou excluir");
        lblDesTipSer3.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlGerDes.add(lblDesTipSer3);
        lblDesTipSer3.setBounds(140, 110, 260, 20);

        scrGerDes.setBackground(new java.awt.Color(250, 250, 250));
        scrGerDes.setBorder(BorderFactory.createEmptyBorder());
        scrGerDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        tblTipSer.setTableHeader(null);
        tblGerDes.setBackground(new java.awt.Color(241, 241, 241));
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
        scrGerDes.setBounds(140, 140, 570, 250);

        pnlPrincipal.add(pnlGerDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        pnlJur.setBackground(new java.awt.Color(241, 241, 241));
        pnlJur.setLayout(null);

        lblCadastrarPlano1.setFont(fontbold(25));
        lblCadastrarPlano1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCadastrarPlano1.setText("Calcular Juros");
        pnlJur.add(lblCadastrarPlano1);
        lblCadastrarPlano1.setBounds(0, 30, 1200, 30);

        btnVolJur.setFont(fontmed(12));
        btnVolJur.setForeground(new java.awt.Color(10, 60, 133));
        btnVolJur.setText("Cancelar");
        btnVolJur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolJur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolJurActionPerformed(evt);
            }
        });
        pnlJur.add(btnVolJur);
        btnVolJur.setBounds(290, 260, 100, 30);

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
        btnCalJur.setBounds(180, 260, 100, 30);

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
        lblValMesPreJur.setBounds(730, 340, 340, 20);

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
        lblValParJur.setBounds(910, 260, 140, 20);

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
        lblValParJur1.setBounds(910, 230, 140, 20);

        lblValJur.setFont(fontmed(12));
        lblValJur.setForeground(new java.awt.Color(10, 60, 133));
        lblValJur.setText("Preço");
        lblValJur.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlJur.add(lblValJur);
        lblValJur.setBounds(180, 200, 70, 20);

        lblR$Jur.setFont(fontmed(13));
        lblR$Jur.setText("R$");
        lblR$Jur.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlJur.add(lblR$Jur);
        lblR$Jur.setBounds(180, 200, 20, 21);

        txtValJur.setBackground(new java.awt.Color(241, 241, 241));
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
        txtValJur.setBounds(200, 200, 70, 20);

        sepDesGerTipSer10.setForeground(new java.awt.Color(10, 60, 133));
        pnlJur.add(sepDesGerTipSer10);
        sepDesGerTipSer10.setBounds(180, 220, 90, 10);

        sepDesGerTipSer11.setForeground(new java.awt.Color(10, 60, 133));
        pnlJur.add(sepDesGerTipSer11);
        sepDesGerTipSer11.setBounds(180, 220, 90, 10);

        sepDesGerTipSer16.setBackground(new java.awt.Color(10, 60, 133));
        sepDesGerTipSer16.setForeground(new java.awt.Color(10, 60, 133));
        sepDesGerTipSer16.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlJur.add(sepDesGerTipSer16);
        sepDesGerTipSer16.setBounds(600, 130, 30, 240);

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
        lblValJur2.setBounds(750, 180, 130, 20);

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
        lblValJur3.setBounds(750, 150, 130, 20);

        lblParJur.setFont(fontmed(12));
        lblParJur.setForeground(new java.awt.Color(10, 60, 133));
        lblParJur.setText("parcela(s)");
        lblParJur.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pnlJur.add(lblParJur);
        lblParJur.setBounds(360, 200, 70, 20);

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
        lblValFinJur.setBounds(900, 180, 150, 20);

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
        lblValJurJur.setBounds(740, 260, 150, 20);

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
        lblValFinJur1.setBounds(900, 150, 150, 20);

        lblJurJur1.setFont(fontbold(16));
        lblJurJur1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJurJur1.setText("Para receber o mesmo preço à vista");
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
        lblJurJur1.setBounds(720, 310, 360, 20);

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
        lblJurJur.setBounds(750, 230, 130, 20);

        spnParJur.setFont(fontmed(13));
        spnParJur.setModel(new javax.swing.SpinnerNumberModel(0, 0, 12, 1));
        spnParJur.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JSpinner.DefaultEditor spnParJur1 = (JSpinner.DefaultEditor) spnParJur.getEditor();
        spnParJur1.getTextField().setEditable(false);
        pnlJur.add(spnParJur);
        spnParJur.setBounds(290, 190, 60, 30);

        pnlPrincipal.add(pnlJur, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1200, 510));

        getContentPane().add(pnlPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 710));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalCadEstActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja salvar?", "Cadastrar Estoque", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                estoque es = new estoque();
                estoqueDAO esdao = new estoqueDAO();

                if (!chkVarCorCadEst.isSelected()) {

                    es.setTipoproduto(txtTipCadEst.getText());
                    es.setModelo(txtModCadEst.getText());
                    es.setMarca(txtMarCadEst.getText());
                    es.setCor(txtCorCadEst.getText());
                    es.setMaterial(txtMatCadEst.getText());
                    es.setDetalhes(txtDetCadEst.getText());
                    es.setLocalizacao(txtLocCadEst.getText());
                    es.setPreco(Double.valueOf(txtPreCadEst.getText().replace(".", "").replace(",", ".")));
                    es.setQuantidade(Integer.valueOf(txtQuaCadEst.getText()));

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

                } else {

//SE TIVER VARIAS CORES
                    int rows = tblVarCorCadEst.getRowCount();

                    es.setTipoproduto(txtTipCadEst.getText());
                    es.setModelo(txtModCadEst.getText());
                    es.setMarca(txtMarCadEst.getText());
                    es.setCor(txtCorCadEst.getText());
                    es.setMaterial(txtMatCadEst.getText());
                    es.setDetalhes(txtDetCadEst.getText());
                    es.setLocalizacao(txtLocCadEst.getText());
                    es.setPreco(Double.valueOf(txtPreCadEst.getText().replace(".", "").replace(",", ".")));
                    es.setQuantidade(Integer.valueOf(txtQuaCadEst.getText()));

                    if (txtTipCadEst.getText().equals("Chip")) {
                        es.setTipochip((String) cmbChiCadEst.getSelectedItem());
                    } else {
                        es.setTipochip(null);
                    }

                    if (!esdao.verifica(es)) {
                        esdao.inserir(es);
                    } else {
                        esdao.acrescentar(es);
                    }

                    for (int i = 0; i < rows; i++) {

                        es.setCor(tblVarCorCadEst.getValueAt(i, 0).toString());
                        es.setQuantidade(Integer.valueOf(tblVarCorCadEst.getValueAt(i, 1).toString()));

                        if (!esdao.verifica(es)) {
                            esdao.inserir(es);
                        } else {
                            esdao.acrescentar(es);
                        }

                    }

                    JOptionPane.showMessageDialog(null, "Novos ítens inseridos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                }

                DefaultTableModel model = (DefaultTableModel) tblCadEst.getModel();
                model.setRowCount(0);
                DefaultTableModel model1 = (DefaultTableModel) tblVarCorCadEst.getModel();
                model1.setRowCount(0);

                pnlCadEst.setVisible(false);
                pnlContent.setVisible(true);

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(null, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

            } catch (NumberFormatException n) {

                JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);

            }

        }
    }//GEN-LAST:event_btnSalCadEstActionPerformed

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
                pnlContent.setVisible(true);

                lblProCadEst.setVisible(false);
                tblCadEst.setVisible(false);
                scrCadEst.setVisible(false);

                DefaultTableModel model = (DefaultTableModel) tblCadEst.getModel();
                model.setRowCount(0);
                DefaultTableModel model1 = (DefaultTableModel) tblVarCorCadEst.getModel();
                model1.setRowCount(0);

            }

        } else {

            pnlCadEst.setVisible(false);
            pnlContent.setVisible(true);

            lblProCadEst.setVisible(false);
            tblCadEst.setVisible(false);
            scrCadEst.setVisible(false);

            DefaultTableModel model = (DefaultTableModel) tblCadEst.getModel();
            model.setRowCount(0);
            DefaultTableModel model1 = (DefaultTableModel) tblVarCorCadEst.getModel();
            model1.setRowCount(0);

        }
    }//GEN-LAST:event_btnCanCadEstActionPerformed

    private void btnSalTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalTipSerActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja cadastrar este serviço?", "Cadastrar Serviço", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

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

                JOptionPane.showMessageDialog(null, "Serviço cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                pnlCadTipSer.setVisible(false);
                pnlContent.setVisible(true);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnSalTipSerActionPerformed

    private void btnCanTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanTipSerActionPerformed
        if (btnSalTipSer.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(null, "Cancelar inserção? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                pnlCadTipSer.setVisible(false);
                pnlContent.setVisible(true);

            }

        } else {

            pnlCadTipSer.setVisible(false);
            pnlContent.setVisible(true);

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

    private void btnCadTipSerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadTipSerMouseReleased
        btnGroup.clearSelection();
        btnSalTipSer.setEnabled(false);
        txtDesTipSer.setText(null);
        lblDesTipSer.setLocation(460, 170);

        txtDesTipSer.setEnabled(false);
        lblDesTipSer.setEnabled(false);
        sepDesTipSer.setForeground(Color.GRAY);

        pnlContent.setVisible(false);
        pnlCadTipSer.setVisible(true);
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
            lblDesGerTipSer.setLocation(460, 330);

            txtDesGerTipSer.setEnabled(false);
            lblDesGerTipSer.setEnabled(false);
            sepDesGerTipSer.setForeground(Color.GRAY);

            pnlGerTipSer.setVisible(true);
            pnlContent.setVisible(false);

        } else {
            JOptionPane.showMessageDialog(null, "Sem serviço para gerenciar. Cadastre-o primeiro!", "Gerenciar Tipo de Serviço", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnGerTipSerMouseReleased

    private void btnCadEstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEstMouseEntered
        btnCadEst.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadEstMouseEntered

    private void btnCadEstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEstMouseExited
        btnCadEst.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadEstMouseExited

    private void btnCadEstMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadEstMouseReleased
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

        chkVarCorCadEst.setEnabled(false);

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

        lblMarCadEst.setLocation(360, 150);
        lblModCadEst.setLocation(360, 200);
        lblQuaCadEst.setLocation(360, 250);
        lblPreCadEst.setLocation(360, 300);
        lblCorCadEst.setLocation(650, 150);
        lblMatCadEst.setLocation(650, 200);
        lblLocCadEst.setLocation(650, 250);
        lblDetCadEst.setLocation(650, 300);
        lblVarCorCadEst.setLocation(880, 220);

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);

        chkVarCorCadEst.setSelected(false);
        lblVarCorCadEst.setVisible(false);
        txtVarCorCadEst.setVisible(false);
        spnVarCorCadEst.setVisible(false);
        sepVarCorCadEst.setVisible(false);
        scrVarCorCadEst.setVisible(false);
        tblVarCorCadEst.setVisible(false);
        btnAdiCadEst.setVisible(false);

        btnGroup.clearSelection();

        btnSalCadEst.setEnabled(false);

        pnlCadEst.setVisible(true);
        pnlContent.setVisible(false);
    }//GEN-LAST:event_btnCadEstMouseReleased

    private void btnConEstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseEntered
        btnConEst.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnConEstMouseEntered

    private void btnConEstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseExited
        btnConEst.setForeground(corforeazul);
    }//GEN-LAST:event_btnConEstMouseExited

    private void btnConEstMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConEstMouseReleased
        btnBusConEst.setEnabled(false);
        txtBusConEst.setText(null);
        txtBusConEst.setEnabled(false);
        lblBusConEst.setEnabled(false);
        sepBusConEst.setForeground(Color.GRAY);

        lblBusConEst.setLocation(450, 150);

        btnGroup.clearSelection();

        scrConEst.setVisible(false);

        pnlContent.setVisible(false);
        pnlConEst.setVisible(true);
    }//GEN-LAST:event_btnConEstMouseReleased

    private void btnGerEstMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEstMouseEntered
        btnGerEst.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerEstMouseEntered

    private void btnGerEstMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEstMouseExited
        btnGerEst.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerEstMouseExited

    private void btnGerEstMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEstMouseReleased
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
        chkAltGerEst.setEnabled(false);
        chkAltGerEst.setSelected(false);

        txtMar1GerEst.setVisible(false);
        txtMod1GerEst.setVisible(false);
        txtPre1GerEst.setVisible(false);
        txtQua1GerEst.setVisible(false);
        txtLoc1GerEst.setVisible(false);
        txtDet1GerEst.setVisible(false);
        txtCor1GerEst.setVisible(false);
        txtChip1GerEst.setVisible(false);
        txtMat1GerEst.setVisible(false);

        txtMarGerEst.setText(null);
        txtModGerEst.setText(null);
        txtQuaGerEst.setText(null);
        txtPreGerEst.setText(null);
        txtCorGerEst.setText(null);
        txtMatGerEst.setText(null);
        txtLocGerEst.setText(null);
        txtDetGerEst.setText(null);
        txtBusGerEst.setText(null);

        txtMar1GerEst.setText(null);
        txtMod1GerEst.setText(null);
        txtPre1GerEst.setText(null);
        txtQua1GerEst.setText(null);
        txtLoc1GerEst.setText(null);
        txtDet1GerEst.setText(null);
        txtCor1GerEst.setText(null);
        txtChip1GerEst.setText(null);
        txtMat1GerEst.setText(null);

        lblBusGerEst.setLocation(230, 160);

        lblMarGerEst.setLocation(730, 130);
        lblModGerEst.setLocation(730, 180);
        lblQuaGerEst.setLocation(730, 230);
        lblPreGerEst.setLocation(730, 280);
        lblCorGerEst.setLocation(970, 130);
        lblMatGerEst.setLocation(970, 180);
        lblLocGerEst.setLocation(970, 230);
        lblDetGerEst.setLocation(970, 280);

        pnlGerEst.setVisible(true);
        pnlContent.setVisible(false);
    }//GEN-LAST:event_btnGerEstMouseReleased

    private void btnMasPlaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseEntered
        btnMasPla.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnMasPlaMouseEntered

    private void btnMasPlaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseExited
        btnMasPla.setForeground(corforeazul);
    }//GEN-LAST:event_btnMasPlaMouseExited

    private void btnMasPlaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasPlaMouseReleased
        txtNomMas.setText(null);
        txtCpfMas.setText(null);
        txtVenMas.setText(null);
        txtNumConMas.setText(null);
        txtNumAceMas.setText(null);
        txtNumPorMas.setText(null);
        txtPlaMas.setText(null);

        chkMelMas.setSelected(false);
        chkAppMas.setSelected(false);
        chkBolMas.setSelected(true);
        rbtnAtiMas.setSelected(true);

        txtAreMas.setText(null);

        btnCopMas.setVisible(false);

        lblNomMas.setLocation(80, 160);
        lblCpfMas.setLocation(80, 210);
        lblVenMas.setLocation(80, 260);
        lblNumConMas.setLocation(340, 160);
        lblNumAceMas.setLocation(340, 210);
        lblNumPorMas.setLocation(340, 260);
        lblPlaMas.setLocation(340, 310);

        pnlContent.setVisible(false);
        pnlMas.setVisible(true);
    }//GEN-LAST:event_btnMasPlaMouseReleased

    private void btnCadDesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadDesMouseEntered
        btnCadDes.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadDesMouseEntered

    private void btnCadDesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadDesMouseExited
        btnCadDes.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadDesMouseExited

    private void btnCadDesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadDesMouseReleased
        txtDesDes.setText(null);
        txtPreDes.setText(null);
        txtDatDes.setText(null);
        btnSalDes.setEnabled(false);

        lblDesDes.setLocation(500, 110);
        lblPreDes.setLocation(500, 160);
        lblDatDes.setLocation(500, 210);

        lblR$Des.setVisible(false);

        pnlCadDes.setVisible(true);
        pnlContent.setVisible(false);
    }//GEN-LAST:event_btnCadDesMouseReleased

    private void btnDesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseEntered
        btnDes.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnDesMouseEntered

    private void btnDesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseExited
        btnDes.setForeground(corforeazul);
    }//GEN-LAST:event_btnDesMouseExited

    private void btnDesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDesMouseReleased
        if (tabeladespezas(tblConDes, scrConDes)) {

            pnlDes.setVisible(true);
            pnlContent.setVisible(false);

        } else {

            JOptionPane.showMessageDialog(null, "Sem afazeres. Cadastre-as primeiro!", "Gerenciar Afazeres", JOptionPane.INFORMATION_MESSAGE);

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

                DefaultTableModel model = (DefaultTableModel) tblTipSer.getModel();
                model.setRowCount(0);

                pnlGerTipSer.setVisible(false);
                pnlContent.setVisible(true);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Este serviço não pode ser excluído pois possui vínculo em alguma entrada. Desative-o para remover da lista!", "Excluir", JOptionPane.OK_OPTION);
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
                if (rbtnTimGerTipSer.isSelected()) {
                    ts.setArea("1");
                } else if (rbtnAssGerTipSer.isSelected()) {
                    ts.setArea("2");
                } else {
                    ts.setArea("3");
                }

                tsdao.alterar(ts);

                JOptionPane.showMessageDialog(null, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                DefaultTableModel model = (DefaultTableModel) tblTipSer.getModel();
                model.setRowCount(0);

                pnlGerTipSer.setVisible(false);
                pnlContent.setVisible(true);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao alterar! Erro: " + ex.getMessage(), "Erro", JOptionPane.OK_OPTION);
            }
        }
    }//GEN-LAST:event_btnAltGerTipSerActionPerformed

    private void btnCanGerTipSerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerTipSerActionPerformed
        if (!txtDesGerTipSer.getText().isEmpty()) {

            int resp = JOptionPane.showOptionDialog(null, "Cancelar alteração? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) tblTipSer.getModel();
                model.setRowCount(0);
                pnlGerTipSer.setVisible(false);
                pnlContent.setVisible(true);
            }
        } else {
            DefaultTableModel model = (DefaultTableModel) tblTipSer.getModel();
            model.setRowCount(0);
            pnlGerTipSer.setVisible(false);
            pnlContent.setVisible(true);
        }
    }//GEN-LAST:event_btnCanGerTipSerActionPerformed

    private void tblTipSerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTipSerMouseClicked
        if ("1".equals(tblTipSer.getValueAt(tblTipSer.getSelectedRow(), 3).toString())) {

            lblDesGerTipSer.setLocation(460, 310);

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

        } else {

            lblDesGerTipSer.setLocation(460, 310);

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
        DefaultTableModel model = (DefaultTableModel) tblConEst.getModel();
        model.setRowCount(0);

        pnlConEst.setVisible(false);
        pnlContent.setVisible(true);
    }//GEN-LAST:event_btnCanConEstActionPerformed

    private void btnAltGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerEstActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            int resp1 = 0;

            if (chkAltGerEst.isSelected()) {

                resp1 = JOptionPane.showOptionDialog(null, "Você selecionou para alterar todas as linhas da tabela, todas as linhas serão alteradas, deseja continuar?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            }

            if (resp1 == JOptionPane.YES_OPTION) {

                if (tblGerEst.getSelectedRow() != -1) {

                    try {
                        estoque es = new estoque();
                        estoqueDAO esdao = new estoqueDAO();

                        if (!chkAltGerEst.isSelected()) {

                            es.setId(Integer.parseInt(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 0).toString()));
                            es.setModelo(txtModGerEst.getText());
                            es.setMarca(txtMarGerEst.getText());
                            es.setCor(txtCorGerEst.getText());
                            es.setMaterial(txtMatGerEst.getText());
                            es.setDetalhes(txtDetGerEst.getText());
                            es.setLocalizacao(txtLocGerEst.getText());
                            es.setPreco(Double.valueOf(txtPreGerEst.getText().replace(".", "").replace(",", ".")));
                            es.setQuantidade(Integer.valueOf(txtQuaGerEst.getText()));

                            if (txtTipGerEst.getText().equals("Chip")) {
                                es.setTipochip((String) cmbChiGerEst.getSelectedItem());
                            } else {
                                es.setTipochip(null);
                            }

                            esdao.alterar(es);

                        } else {

                            es.setId(Integer.parseInt(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 0).toString()));
                            es.setModelo(txtModGerEst.getText());
                            es.setMarca(txtMarGerEst.getText());
                            es.setCor(txtCorGerEst.getText());
                            es.setMaterial(txtMatGerEst.getText());
                            es.setDetalhes(txtDetGerEst.getText());
                            es.setLocalizacao(txtLocGerEst.getText());
                            es.setPreco(Double.valueOf(txtPreGerEst.getText().replace(".", "").replace(",", ".")));
                            es.setQuantidade(Integer.valueOf(txtQuaGerEst.getText()));

                            if (txtTipGerEst.getText().equals("Chip")) {
                                es.setTipochip((String) cmbChiGerEst.getSelectedItem());
                            } else {
                                es.setTipochip(null);
                            }

                            esdao.alterar(es);

                            int rows = tblGerEst.getRowCount();

                            for (int i = 0; i < rows; i++) {

                                if (!txtMod1GerEst.getText().equals(txtModGerEst.getText())) {
                                    es.setModelo(txtModGerEst.getText());
                                } else {
                                    es.setModelo(null);
                                }

                                if (!txtMar1GerEst.getText().equals(txtMarGerEst.getText())) {
                                    es.setMarca(txtMarGerEst.getText());
                                } else {
                                    es.setMarca(null);
                                }
                                if (!txtCor1GerEst.getText().equals(txtCorGerEst.getText())) {
                                    es.setCor(txtCorGerEst.getText());
                                } else {
                                    es.setCor(null);
                                }
                                if (!txtMat1GerEst.getText().equals(txtMatGerEst.getText())) {
                                    es.setMaterial(txtMatGerEst.getText());
                                } else {
                                    es.setMaterial(null);
                                }

                                if (!txtDet1GerEst.getText().equals(txtDetGerEst.getText())) {
                                    es.setDetalhes(txtDetGerEst.getText());
                                } else {
                                    es.setDetalhes(null);
                                }
                                if (!txtLoc1GerEst.getText().equals(txtLocGerEst.getText())) {
                                    es.setLocalizacao(txtLocGerEst.getText());
                                } else {
                                    es.setLocalizacao(null);
                                }

                                Double precoori = Double.valueOf(txtPre1GerEst.getText().replace(".", "").replace(",", "."));

                                Double precomud = Double.valueOf(txtPreGerEst.getText().replace(".", "").replace(",", "."));

                                if (!precoori.equals(precomud)) {
                                    es.setPreco(precomud);
                                } else {
                                    es.setPreco(null);
                                }

                                if (!txtQua1GerEst.getText().equals(txtQuaGerEst.getText())) {
                                    es.setQuantidade(Integer.valueOf(txtQuaGerEst.getText()));
                                } else {
                                    es.setQuantidade(null);
                                }

                                if (rbtnChiGerEst.isSelected()) {

                                    if (!txtChip1GerEst.getText().equals((String) cmbChiGerEst.getSelectedItem())) {
                                        es.setTipochip((String) cmbChiGerEst.getSelectedItem());
                                    } else {
                                        es.setTipochip(null);
                                    }

                                } else {
                                    es.setTipochip(null);
                                }

                                es.setId(Integer.parseInt(tblGerEst.getValueAt(i, 0).toString()));
                                esdao.alterarvarios(es);

                            }

                        }

                        JOptionPane.showMessageDialog(null, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        DefaultTableModel model = (DefaultTableModel) tblGerEst.getModel();
                        model.setRowCount(0);

                        pnlGerEst.setVisible(false);
                        pnlContent.setVisible(true);

                    } catch (SQLException ex) {

                        JOptionPane.showMessageDialog(null, "Erro ao inserir! Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                    } catch (NumberFormatException n) {

                        JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);

                    }

                } else {

                    JOptionPane.showMessageDialog(null, "Selecione uma linha na tabela para alterar!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }//GEN-LAST:event_btnAltGerEstActionPerformed

    private void btnCanGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerEstActionPerformed
        if (btnAltGerEst.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(null, "Cancelar alterações?", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                pnlGerEst.setVisible(false);
                pnlContent.setVisible(true);

            }

        } else {

            pnlGerEst.setVisible(false);
            pnlContent.setVisible(true);

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
        cmbChiGerEst.setSelectedIndex(0);

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

        chkAltGerEst.setEnabled(false);
        chkAltGerEst.setSelected(false);

        lblMarGerEst.setLocation(730, 130);
        lblModGerEst.setLocation(730, 180);
        lblQuaGerEst.setLocation(730, 230);
        lblPreGerEst.setLocation(730, 280);
        lblCorGerEst.setLocation(970, 130);
        lblMatGerEst.setLocation(970, 180);
        lblLocGerEst.setLocation(970, 230);
        lblDetGerEst.setLocation(970, 280);

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
        cmbChiGerEst.setSelectedIndex(0);

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

        chkAltGerEst.setEnabled(false);
        chkAltGerEst.setSelected(false);

        lblMarGerEst.setLocation(730, 130);
        lblModGerEst.setLocation(730, 180);
        lblQuaGerEst.setLocation(730, 230);
        lblPreGerEst.setLocation(730, 280);
        lblCorGerEst.setLocation(970, 130);
        lblMatGerEst.setLocation(970, 180);
        lblLocGerEst.setLocation(970, 230);
        lblDetGerEst.setLocation(970, 280);

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
        cmbChiGerEst.setSelectedIndex(0);

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

        chkAltGerEst.setEnabled(false);
        chkAltGerEst.setSelected(false);

        lblMarGerEst.setLocation(730, 130);
        lblModGerEst.setLocation(730, 180);
        lblQuaGerEst.setLocation(730, 230);
        lblPreGerEst.setLocation(730, 280);
        lblCorGerEst.setLocation(970, 130);
        lblMatGerEst.setLocation(970, 180);
        lblLocGerEst.setLocation(970, 230);
        lblDetGerEst.setLocation(970, 280);

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

        cmbChiGerEst.setSelectedIndex(0);
        lblChiGerEst.setEnabled(false);
        cmbChiGerEst.setEnabled(false);

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

        chkAltGerEst.setEnabled(false);
        chkAltGerEst.setSelected(false);

        lblMarGerEst.setLocation(730, 130);
        lblModGerEst.setLocation(730, 180);
        lblQuaGerEst.setLocation(730, 230);
        lblPreGerEst.setLocation(730, 280);
        lblCorGerEst.setLocation(970, 130);
        lblMatGerEst.setLocation(970, 180);
        lblLocGerEst.setLocation(970, 230);
        lblDetGerEst.setLocation(970, 280);

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

            chkAltGerEst.setEnabled(false);
            chkAltGerEst.setSelected(false);

        } else {

            scrGerEst.setVisible(false);
            tblGerEst.setVisible(false);

            JOptionPane.showMessageDialog(null, "Nenhum dado encontrado!", "Gerenciar", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_btnBusGerEstActionPerformed

    private void btnExcGerEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcGerEstActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            int resp1 = 0;

            if (chkAltGerEst.isSelected()) {

                resp1 = JOptionPane.showOptionDialog(null, "Você selecionou para alterar todas as linhas da tabela, todas as linhas serão excluídas, deseja continuar?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            }

            if (resp1 == JOptionPane.YES_OPTION) {

                try {

                    estoque es = new estoque();
                    estoqueDAO esdao = new estoqueDAO();

                    if (!chkAltGerEst.isSelected()) {

                        es.setId(Integer.parseInt(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 0).toString()));

                        esdao.excluir(es);

                    } else {

                        int rows = tblGerEst.getRowCount();

                        for (int i = 0; i < rows; i++) {

                            es.setId(Integer.parseInt(tblGerEst.getValueAt(i, 0).toString()));

                            esdao.excluir(es);

                        }

                    }

                    JOptionPane.showMessageDialog(null, "Excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    DefaultTableModel model = (DefaultTableModel) tblGerEst.getModel();
                    model.setRowCount(0);

                    pnlGerEst.setVisible(false);
                    pnlContent.setVisible(true);

                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }//GEN-LAST:event_btnExcGerEstActionPerformed

    private void txtBusGerEstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusGerEstKeyTyped
        btnBusGerEst.setEnabled(true);
    }//GEN-LAST:event_txtBusGerEstKeyTyped

    private void tblGerEstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGerEstMouseClicked
        lblMarGerEst.setLocation(730, 130);
        lblModGerEst.setLocation(730, 180);
        lblQuaGerEst.setLocation(730, 230);
        lblPreGerEst.setLocation(730, 280);
        lblCorGerEst.setLocation(970, 130);
        lblMatGerEst.setLocation(970, 180);
        lblLocGerEst.setLocation(970, 230);
        lblDetGerEst.setLocation(970, 280);

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

        txtMar1GerEst.setText(null);
        txtMod1GerEst.setText(null);
        txtPre1GerEst.setText(null);
        txtQua1GerEst.setText(null);
        txtLoc1GerEst.setText(null);
        txtDet1GerEst.setText(null);
        txtCor1GerEst.setText(null);
        txtChip1GerEst.setText(null);
        txtMat1GerEst.setText(null);

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

                txtMar1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtMod1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPre1GerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQua1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLoc1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtDet1GerEst.setText((!"Sem Detalhes".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");
                txtCor1GerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString() : "");

            } else {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtCorGerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");

                txtMar1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtMod1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPre1GerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQua1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLoc1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtCor1GerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");

            }

            chkAltGerEst.setEnabled(true);
            chkAltGerEst.setSelected(false);

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

            txtPre1GerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString().length()));
            txtQua1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());

            txtChip1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString());

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

                txtMar1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtMod1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPre1GerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQua1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLoc1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtDet1GerEst.setText((!"Sem Detalhes".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");
                txtCor1GerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString() : "");
                txtMat1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 9).toString());

            } else if (tblGerEst.getColumnCount() == 8) {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtMatGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString());

                txtMar1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtMod1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPre1GerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQua1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLoc1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtMat1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString());

            } else {

                txtMarGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtModGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPreGerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQuaGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLocGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtCorGerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");
                txtMatGerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString());

                txtMar1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 2).toString());
                txtMod1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 3).toString());
                txtPre1GerEst.setText((tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString()).substring(3, tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 4).toString().length()));
                txtQua1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 5).toString());
                txtLoc1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 6).toString());
                txtCor1GerEst.setText((!"Não Aplicável".equals(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString())) ? tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 7).toString() : "");
                txtMat1GerEst.setText(tblGerEst.getValueAt(tblGerEst.getSelectedRow(), 8).toString());

            }

            chkAltGerEst.setEnabled(true);
            chkAltGerEst.setSelected(false);

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
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja salvar?", "Nova Entrada", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            if ((rbtnDinCadEnt.isSelected() || rbtnPixCadEnt.isSelected()) || (rbtnCarCadEnt.isSelected() && (rbtnCreCadEnt.isSelected() || rbtnDebCadEnt.isSelected()))) {

                if (!(rbtnVenCadEnt.isSelected() && tblSelIteCadEnt.getRowCount() == 0)) {

                    if (!(rbtnTroPreCadEnt.isEnabled() && !rbtnTroPreCadEnt.isSelected()) || !(rbtnTroPlaCadEnt.isEnabled() && !rbtnTroPlaCadEnt.isSelected())) {

                        if (!(cmbSerCadEnt.getSelectedIndex() == 0 && (rbtnAssCadEnt.isSelected() || rbtnSerCadEnt.isSelected()))) {

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

                                if (cmbSerCadEnt.getSelectedIndex() != 0) {
                                    itens selectedItem = (itens) cmbSerCadEnt.getSelectedItem();
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
                                if (cmbSerCadEnt.isEnabled()) {
                                    itens selectedItem = (itens) cmbSerCadEnt.getSelectedItem();

                                    String textoSelecionado = selectedItem.getDescricao();

                                    if (textoSelecionado.equals("Troca de Chip") || textoSelecionado.equals("Ativação eSIM")) {

                                        planosdiaDAO pddao = new planosdiaDAO();
                                        pddao.zerar();

                                        if (rbtnTroPreCadEnt.isSelected()) {
                                            pddao.adicionar(3);
                                        } else {
                                            pddao.adicionar(4);
                                        }

                                    }

                                }

                                JOptionPane.showMessageDialog(null, "Entrada feita com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                                DefaultTableModel model = (DefaultTableModel) tblSelIteCadEnt.getModel();
                                model.setRowCount(0);
                                DefaultTableModel model1 = (DefaultTableModel) tblEstIteCadEnt.getModel();
                                model1.setRowCount(0);

                                pnlCadEnt.setVisible(false);
                                pnlContent.setVisible(true);

                            } catch (SQLException | ParseException ex) {
                                Logger.getLogger(main.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Selecione o serviço!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Selecione o tipo de troca de chip!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Selecione os ítem do estoque!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Selecione o método de pagamento!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            }

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
                    Logger.getLogger(main.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                DefaultTableModel model = (DefaultTableModel) tblSelIteCadEnt.getModel();
                model.setRowCount(0);
                DefaultTableModel model1 = (DefaultTableModel) tblEstIteCadEnt.getModel();
                model1.setRowCount(0);

                pnlCadEnt.setVisible(false);
                pnlContent.setVisible(true);

            }

        } else {

            pnlCadEnt.setVisible(false);
            pnlContent.setVisible(true);

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

        lblCliCadEnt.setLocation(570, 220);
        lblCusCadEnt.setLocation(570, 270);
        lblForCadEnt.setLocation(570, 320);

        lblSerCadEnt.setEnabled(true);
        cmbSerCadEnt.setEnabled(true);
        cmbSerCadEnt.setSelectedIndex(0);

        rbtnDinCadEnt.setEnabled(true);
        rbtnCarCadEnt.setEnabled(true);
        rbtnPixCadEnt.setEnabled(true);

        lblCliCadEnt.setEnabled(false);
        txtCliCadEnt.setEnabled(false);
        txtCliCadEnt.setText(null);
        sepCliCadEnt.setForeground(Color.GRAY);

        lblCusCadEnt.setEnabled(false);
        txtCusCadEnt.setEnabled(false);
        txtCusCadEnt.setText(null);
        sepCusCadEnt.setForeground(Color.GRAY);
        lblR$CusCadEnt.setVisible(false);

        lblForCadEnt.setEnabled(false);
        txtForCadEnt.setEnabled(false);
        txtForCadEnt.setText(null);
        sepForCadEnt.setForeground(Color.GRAY);

        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(true);

        comboboxentrada(cmbSerCadEnt, 1);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(330, 200);
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

        lblCliCadEnt.setLocation(570, 220);
        lblCusCadEnt.setLocation(570, 270);
        lblForCadEnt.setLocation(570, 320);

        lblSerCadEnt.setEnabled(false);
        cmbSerCadEnt.setEnabled(false);
        cmbSerCadEnt.setSelectedIndex(0);

        rbtnDinCadEnt.setEnabled(true);
        rbtnCarCadEnt.setEnabled(true);
        rbtnPixCadEnt.setEnabled(true);

        lblCliCadEnt.setEnabled(false);
        txtCliCadEnt.setEnabled(false);
        txtCliCadEnt.setText(null);
        sepCliCadEnt.setForeground(Color.GRAY);

        lblCusCadEnt.setEnabled(false);
        txtCusCadEnt.setEnabled(false);
        txtCusCadEnt.setText(null);
        sepCusCadEnt.setForeground(Color.GRAY);
        lblR$CusCadEnt.setVisible(false);

        lblForCadEnt.setEnabled(false);
        txtForCadEnt.setEnabled(false);
        txtForCadEnt.setText(null);
        sepForCadEnt.setForeground(Color.GRAY);

        rbtnTroPreCadEnt.setEnabled(false);
        rbtnTroPlaCadEnt.setEnabled(false);
        btnGroup5.clearSelection();

        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(true);

        comboboxentrada(cmbSerCadEnt, 1);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(330, 200);
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
        cmbSerCadEnt.setSelectedIndex(0);

        rbtnDinCadEnt.setEnabled(true);
        rbtnCarCadEnt.setEnabled(true);
        rbtnPixCadEnt.setEnabled(true);

        lblCliCadEnt.setEnabled(true);
        txtCliCadEnt.setEnabled(true);
        sepCliCadEnt.setForeground(corforeazul);

        lblCusCadEnt.setEnabled(true);
        txtCusCadEnt.setEnabled(true);
        sepCusCadEnt.setForeground(corforeazul);
        lblR$CusCadEnt.setVisible(false);

        lblForCadEnt.setEnabled(true);
        txtForCadEnt.setEnabled(true);
        sepForCadEnt.setForeground(corforeazul);

        rbtnTroPreCadEnt.setEnabled(false);
        rbtnTroPlaCadEnt.setEnabled(false);
        btnGroup5.clearSelection();

        btnSalCadEnt.setEnabled(true);
        btnIteCadEnt.setEnabled(true);

        comboboxentrada(cmbSerCadEnt, 1);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadEnt.setText(dataAtual.format(formatteratual));

        lblDatCadEnt.setLocation(330, 200);
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
                lblSelIteCadEnt.setForeground(new Color(241, 241, 241));

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

                            lblSelIteCadEnt.setVisible(true);
                            scrSelIteCadEnt.setVisible(true);
                            tblSelIteCadEnt.setVisible(true);

                        }

                    } else {

                        if (Integer.parseInt(tblEstIteCadEnt.getValueAt(tblEstIteCadEnt.getSelectedRow(), 5).toString()) < i) {

                            JOptionPane.showMessageDialog(null, "Estoque insuficiente!", "Entrada", JOptionPane.ERROR_MESSAGE);

                        } else {

                            adicionarprodutos(tblEstIteCadEnt, tblSelIteCadEnt, opc, rbtnChiIteCadEnt);

                            lblSelIteCadEnt.setVisible(true);
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
        lblDatCadEnt.setLocation(330, 220);
        lblPreCadEnt.setLocation(330, 270);
        lblDetCadEnt.setLocation(330, 320);
        lblCliCadEnt.setLocation(570, 220);
        lblCusCadEnt.setLocation(570, 270);
        lblForCadEnt.setLocation(570, 320);

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
        txtDetCadEnt.setForeground(Color.GRAY);

        lblCliCadEnt.setEnabled(false);
        txtCliCadEnt.setEnabled(false);
        txtCliCadEnt.setText(null);
        sepCliCadEnt.setForeground(Color.GRAY);

        lblCusCadEnt.setEnabled(false);
        txtCusCadEnt.setEnabled(false);
        txtCusCadEnt.setText(null);
        sepCusCadEnt.setForeground(Color.GRAY);

        lblForCadEnt.setEnabled(false);
        txtForCadEnt.setEnabled(false);
        txtForCadEnt.setText(null);
        sepForCadEnt.setForeground(Color.GRAY);

        lblSerCadEnt.setEnabled(false);
        cmbSerCadEnt.setEnabled(false);
        cmbSerCadEnt.setSelectedIndex(0);

        spnParCadEnt.setVisible(false);
        lblParCadEnt.setVisible(false);
        spnParCadEnt.setValue(1);

        lblR$CadEnt.setVisible(false);
        lblR$CusCadEnt.setVisible(false);

        rbtnDinCadEnt.setEnabled(false);
        rbtnCarCadEnt.setEnabled(false);
        rbtnPixCadEnt.setEnabled(false);

        rbtnCreCadEnt.setEnabled(false);
        rbtnDebCadEnt.setEnabled(false);
        rbtnCreCadEnt.setVisible(false);
        rbtnDebCadEnt.setVisible(false);

        btnGroup.clearSelection();
        btnGroup1.clearSelection();
        btnGroup3.clearSelection();
        btnGroup5.clearSelection();

        rbtnTroPreCadEnt.setEnabled(false);
        rbtnTroPlaCadEnt.setEnabled(false);

        btnIteCadEnt.setEnabled(false);
        btnSalCadEnt.setEnabled(false);

        //TELA ESCOLHER PRODUTO
        btnGroup2.clearSelection();

        lblEstIteCadEnt.setVisible(false);
        lblSelIteCadEnt.setVisible(false);

        scrEstIteCadEnt.setVisible(false);
        scrSelIteCadEnt.setVisible(false);
        tblEstIteCadEnt.setVisible(false);
        tblSelIteCadEnt.setVisible(false);

        DefaultTableModel model = (DefaultTableModel) tblSelIteCadEnt.getModel();
        model.setRowCount(0);
        DefaultTableModel model1 = (DefaultTableModel) tblEstIteCadEnt.getModel();
        model1.setRowCount(0);

        lblBusIteCadEnt.setEnabled(false);
        txtBusIteCadEnt.setEnabled(false);
        txtBusIteCadEnt.setText(null);
        sepBusIteCadEnt.setForeground(Color.GRAY);
        //---

        pnlContent.setVisible(false);
        pnlCadEnt.setVisible(true);

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
    }//GEN-LAST:event_btnCadEntMouseReleased

    private void txtBusIteCadEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusIteCadEntFocusLost
        if (txtBusIteCadEnt.getText().isEmpty())
            lblBusIteCadEnt.setLocation(60, 160);
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
        if (!txtBusIteCadEnt.getText().isEmpty()) {
            anitxtin(lblBusIteCadEnt);
        }

        pnlCadEnt.setVisible(false);
        pnlIteCadEnt.setVisible(true);
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
                lblEstIteCadEnt.setVisible(true);

            } else {

                txtBusIteCadEnt.setText(null);

                JOptionPane.showMessageDialog(null, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

            }

            tblEstIteCadEnt.getTableHeader().setFont(fontbold(11));

            btnVolIteCadEnt.grabFocus();

            txtBusIteCadEnt.setText(null);

        }
    }//GEN-LAST:event_txtBusIteCadEntKeyPressed

    private void btnVolRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolRelActionPerformed
        pnlRel.setVisible(false);
        pnlContent.setVisible(true);
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

        if (!txtDatIniRel.getText().isEmpty() || !txtDatFinRel.getText().isEmpty()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(420, 220);
            lblDatIniRel.setLocation(270, 220);
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

        if (!txtDatIniRel.getText().isEmpty() || !txtDatFinRel.getText().isEmpty()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(420, 220);
            lblDatIniRel.setLocation(270, 220);
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

        if (!txtDatIniRel.getText().isEmpty() || !txtDatFinRel.getText().isEmpty()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(420, 220);
            lblDatIniRel.setLocation(270, 220);
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

        if (!txtDatIniRel.getText().isEmpty() || !txtDatFinRel.getText().isEmpty()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(420, 220);
            lblDatIniRel.setLocation(270, 220);
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

        if (!txtDatIniRel.getText().isEmpty() || !txtDatFinRel.getText().isEmpty()) {
            txtDatIniRel.setText(null);
            txtDatFinRel.setText(null);
            lblDatFinRel.setLocation(420, 220);
            lblDatIniRel.setLocation(270, 220);
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

            btnTodRel.setFont(fontmed(12));
            btnDiaRel.setFont(fontmed(12));
            btnSemRel.setFont(fontmed(12));
            btnMesRel.setFont(fontmed(12));
            btnAnoRel.setFont(fontmed(12));
            lblDatIniRel.setFont(fontbold(13));
            lblDatFinRel.setFont(fontbold(13));

            chkCus.setSelected(false);

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

                chkCus.setSelected(false);

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

        if (rbtnMigTroMas.isSelected()) {

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
        String pago = "(    ) DACC (Débito em Conta)\n( X ) Boleto\n(    ) Cartão de Crédito\n";
        String melhor = "(    ) Sim\n( X ) Não\n";
        String app = "(    ) Sim\n( X ) Não";
        String venc = txtVenMas.getText();

        if (rbtnMigMas.isSelected()) {
            servico = "(    ) Ativação\n( X ) Migração (Pré P/Ctrl)\n(    ) Conversão (Troca Pré P/Controle)\n";
        } else if (rbtnMigTroMas.isSelected()) {
            servico = "(    ) Ativação\n(    ) Migração (Pré P/Ctrl)\n( X ) Conversão (Troca Pré P/Controle)\n";
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
                + "\n*Instalou e Acessou App Meu TIM no Celular do Cliente*\n"
                + app
        );

        txtAreMas.setCaretPosition(0);

        btnCopMas.setVisible(true);
        jScrollPane1.setVisible(true);
        txtAreMas.setVisible(true);
    }//GEN-LAST:event_btnGerMasActionPerformed

    private void btnCanMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanMasActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Todos os dados serão perdidos, deseja cancelar?", "Máscara", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            txtNomMas.setText(null);
            txtCpfMas.setText(null);
            txtVenMas.setText(null);
            txtNumConMas.setText(null);
            txtNumAceMas.setText(null);
            txtNumPorMas.setText(null);
            txtPlaMas.setText(null);

            btnGroup.clearSelection();
            btnGroup1.clearSelection();

            chkMelMas.setSelected(false);
            chkAppMas.setSelected(false);

            txtAreMas.setText(null);

            btnCanMas.grabFocus();

            btnCopMas.setVisible(false);

            pnlMas.setVisible(false);
            pnlContent.setVisible(true);

        }
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
        int resp = JOptionPane.showOptionDialog(null, "Cadastrar afazer?", "Cadastrar Afazer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                despezas de = new despezas();
                despezasDAO dedao = new despezasDAO();

                de.setDescricao(txtDesDes.getText());
                de.setValor(Double.valueOf(txtPreDes.getText().replaceAll(",", ".")));
                de.setData(formatterbanco.format(formatter.parse(txtDatDes.getText())));
                de.setStatus(0);

                dedao.inserir(de);

                JOptionPane.showMessageDialog(null, "Afazer cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                pnlCadDes.setVisible(false);
                pnlContent.setVisible(true);

                verificaafazeres();

            } catch (SQLException | ParseException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

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
        if (btnSalDes.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(null, "Todos os dados serão perdidos, deseja continuar?", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                pnlCadDes.setVisible(false);
                pnlContent.setVisible(true);
            }
        } else {
            pnlCadDes.setVisible(false);
            pnlContent.setVisible(true);
        }
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

                JOptionPane.showMessageDialog(null, "Concluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                tabeladespezas(tblConDes, scrConDes);

                verificaafazeres();

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tblConDesMouseClicked

    private void btnVolDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolDesActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblConDes.getModel();
        model.setRowCount(0);

        pnlDes.setVisible(false);
        pnlContent.setVisible(true);
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

                DefaultTableModel model = (DefaultTableModel) tblGerDes.getModel();
                model.setRowCount(0);

                pnlGerDes.setVisible(false);
                pnlContent.setVisible(true);

                verificaafazeres();

            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExcGerDesActionPerformed

    private void btnAltGerDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerDesActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar?", "Alterar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                double preco;

                if (!txtPreGerDes.getText().isEmpty()) {
                    preco = Double.parseDouble(txtPreGerDes.getText().replace(".", "").replace(",", "."));
                } else {
                    preco = 0;
                }

                despezas de = new despezas();
                despezasDAO dedao = new despezasDAO();

                de.setId(Integer.parseInt(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 0).toString()));
                de.setDescricao(txtDesGerDes.getText());
                de.setValor(preco);
                de.setData(formatterbanco.format(formatter.parse(txtDatGerDes.getText())));

                dedao.alterar(de);

                JOptionPane.showMessageDialog(null, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                DefaultTableModel model = (DefaultTableModel) tblGerDes.getModel();
                model.setRowCount(0);

                pnlGerDes.setVisible(false);
                pnlContent.setVisible(true);

                verificaafazeres();

            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAltGerDesActionPerformed

    private void btnCanGerDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerDesActionPerformed
        if (btnAltGerDes.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(null, "Cancelar alteração? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) tblGerDes.getModel();
                model.setRowCount(0);
                pnlGerDes.setVisible(false);
                pnlContent.setVisible(true);
            }
        } else {
            DefaultTableModel model = (DefaultTableModel) tblGerDes.getModel();
            model.setRowCount(0);
            pnlGerDes.setVisible(false);
            pnlContent.setVisible(true);
        }
    }//GEN-LAST:event_btnCanGerDesActionPerformed

    private void tblGerDesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGerDesMouseClicked
        txtDesGerDes.setText(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 1).toString());

        if (!(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 2).toString()).equals("Não Aplicável")) {
            txtPreGerDes.setText((tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 2).toString()).substring(3, tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 2).toString().length()));
            lblPreGerDes.setLocation(860, 190);
            lblR$GerDes.setVisible(true);
        } else {
            txtPreGerDes.setText(null);
            lblPreGerDes.setLocation(860, 210);
            lblR$GerDes.setVisible(false);
        }

        txtDatGerDes.setText(tblGerDes.getValueAt(tblGerDes.getSelectedRow(), 3).toString());

        lblDesGerDes.setLocation(860, 140);
        lblDatGerDes.setLocation(860, 240);

        sepDesGerDes.setForeground(corforeazul);
        sepPreGerDes.setForeground(corforeazul);
        sepDatGerDes.setForeground(corforeazul);

        lblDesGerDes.setEnabled(true);
        txtDesGerDes.setEnabled(true);
        lblPreGerDes.setEnabled(true);
        txtPreGerDes.setEnabled(true);
        lblDatGerDes.setEnabled(true);
        txtDatGerDes.setEnabled(true);

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
        if (tabelagerenciardespezas(tblGerDes, scrGerDes)) {

            btnExcGerDes.setEnabled(false);
            btnAltGerDes.setEnabled(false);

            txtDesGerDes.setText(null);
            txtPreGerDes.setText(null);
            txtDatGerDes.setText(null);
            pnlVen.setVisible(false);
            pnlJur.setVisible(false);

            lblDesGerDes.setLocation(860, 160);
            lblPreGerDes.setLocation(860, 210);
            lblDatGerDes.setLocation(860, 260);

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

            pnlGerDes.setVisible(true);
            pnlContent.setVisible(false);

        } else {
            JOptionPane.showMessageDialog(null, "Sem afazeres para gerenciar. Cadastre-as primeiro!", "Gerenciar afazeres", JOptionPane.INFORMATION_MESSAGE);
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

                        en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString());

                        endao.excluir(en);

                        JOptionPane.showMessageDialog(null, "Entrada excluída com sucesso!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

                        DefaultTableModel model = (DefaultTableModel) tblGerEnt.getModel();
                        model.setRowCount(0);

                        pnlGerEnt.setVisible(false);
                        pnlContent.setVisible(true);

                    }

                } else {

                    entrada en = new entrada();
                    entradaDAO endao = new entradaDAO();
                    en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString());
                    endao.excluir(en);

                    JOptionPane.showMessageDialog(null, "Entrada excluída com sucesso!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

                    pnlGerEnt.setVisible(false);
                    pnlContent.setVisible(true);

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExcGerEntActionPerformed

    private void btnBusGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusGerEntActionPerformed
        try {

            String data = formatterbanco.format(formatter.parse(txtDatBusGerEnt.getText()));

            btnExcGerEnt.setVisible(false);
            btnAltGerEnt.setVisible(false);

            btnAltGerEnt.setEnabled(false);
            btnExcGerEnt.setEnabled(false);

            tblGerEnt.setVisible(false);
            scrGerEnt.setVisible(false);

            tblGerEnt.repaint();

            if (tabelagerenciarentrada(tblGerEnt, scrGerEnt, data)) {

                tblGerEnt.setVisible(true);
                scrGerEnt.setVisible(true);

                btnExcGerEnt.setVisible(true);
                btnAltGerEnt.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(null, "Nenhum ítem encontrado!", "Entrada", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (ParseException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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

        tblSelIteGerEnt.getTableHeader().setFont(fontbold(11));

        btnVolIteGerEnt.grabFocus();

        if (!txtBusIteGerEnt.getText().isEmpty()) {
            anitxtin(lblBusIteGerEnt);
        }

        pnlAlterarEntrada.setVisible(false);
        pnlIteGerEnt.setVisible(true);
    }//GEN-LAST:event_btnIteGerEntActionPerformed

    private void btnCanGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanGerEntActionPerformed
        pnlGerEnt.setVisible(false);
        pnlContent.setVisible(true);
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
        btnAltGerEnt.setEnabled(true);
        btnExcGerEnt.setEnabled(true);
    }//GEN-LAST:event_tblGerEntMouseClicked

    private void btnSalGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalGerEntActionPerformed
        try {

            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja alterar?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                entrada en = new entrada();
                entradaDAO endao = new entradaDAO();

                en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString());

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

                                    en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString());
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

                                en.setCodigo(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString());
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

                            DefaultTableModel model = (DefaultTableModel) tblGerEnt.getModel();
                            model.setRowCount(0);
                            DefaultTableModel model1 = (DefaultTableModel) tblEstIteGerEnt.getModel();
                            model1.setRowCount(0);
                            DefaultTableModel model2 = (DefaultTableModel) tblSelIteGerEnt.getModel();
                            model2.setRowCount(0);

                            pnlAlterarEntrada.setVisible(false);
                            pnlContent.setVisible(true);

                        } catch (SQLException | ParseException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Selecione o serviço!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {

                    JOptionPane.showMessageDialog(null, "Selecione os ítem do estoque!", "Atenção", JOptionPane.INFORMATION_MESSAGE);

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSalGerEntActionPerformed

    private void btnGerEntMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEntMouseEntered
        btnGerEnt.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerEntMouseEntered

    private void btnGerEntMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEntMouseExited
        btnGerEnt.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerEntMouseExited

    private void btnGerEntMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerEntMouseReleased
        scrGerEnt.setVisible(false);

        btnExcGerEnt.setVisible(false);
        btnAltGerEnt.setVisible(false);
        btnBusGerEnt.setEnabled(false);

        lblDatBusGerEnt.setLocation(500, 100);
        txtDatBusGerEnt.setText(null);

        pnlContent.setVisible(false);
        pnlGerEnt.setVisible(true);
    }//GEN-LAST:event_btnGerEntMouseReleased

    private void btnVolIteGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolIteGerEntActionPerformed
        pnlIteGerEnt.setVisible(false);
        pnlAlterarEntrada.setVisible(true);
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

    private void txtBusIteGerEntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBusIteGerEntFocusLost
        if (txtBusIteGerEnt.getText().isEmpty())
            lblBusIteGerEnt.setLocation(60, 160);
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
                lblSelIteGerEnt.setForeground(new Color(241, 241, 241));

            }
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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
        chkVarCorCadEst.setEnabled(true);
        chkVarCorCadEst.setSelected(false);

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);
        lblVarCorCadEst.setVisible(false);
        txtVarCorCadEst.setVisible(false);
        spnVarCorCadEst.setVisible(false);
        sepVarCorCadEst.setVisible(false);
        btnAdiCadEst.setVisible(false);
        tblVarCorCadEst.setVisible(false);
        scrVarCorCadEst.setVisible(false);

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

        lblMarCadEst.setLocation(360, 150);
        lblModCadEst.setLocation(360, 200);
        lblQuaCadEst.setLocation(360, 250);
        lblPreCadEst.setLocation(360, 300);
        lblCorCadEst.setLocation(650, 150);
        lblMatCadEst.setLocation(650, 200);
        lblLocCadEst.setLocation(650, 250);
        lblDetCadEst.setLocation(650, 300);
        lblVarCorCadEst.setLocation(880, 220);

        DefaultTableModel model = (DefaultTableModel) tblVarCorCadEst.getModel();
        model.setRowCount(0);

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
        chkVarCorCadEst.setEnabled(false);
        chkVarCorCadEst.setSelected(false);

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);
        lblVarCorCadEst.setVisible(false);
        txtVarCorCadEst.setVisible(false);
        spnVarCorCadEst.setVisible(false);
        sepVarCorCadEst.setVisible(false);
        btnAdiCadEst.setVisible(false);
        tblVarCorCadEst.setVisible(false);
        scrVarCorCadEst.setVisible(false);

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

        lblMarCadEst.setLocation(360, 150);
        lblModCadEst.setLocation(360, 200);
        lblQuaCadEst.setLocation(360, 250);
        lblPreCadEst.setLocation(360, 300);
        lblCorCadEst.setLocation(650, 150);
        lblMatCadEst.setLocation(650, 200);
        lblLocCadEst.setLocation(650, 250);
        lblDetCadEst.setLocation(650, 300);
        lblVarCorCadEst.setLocation(880, 220);

        DefaultTableModel model = (DefaultTableModel) tblVarCorCadEst.getModel();
        model.setRowCount(0);

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
        chkVarCorCadEst.setEnabled(true);
        chkVarCorCadEst.setSelected(false);

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);
        lblVarCorCadEst.setVisible(false);
        txtVarCorCadEst.setVisible(false);
        spnVarCorCadEst.setVisible(false);
        sepVarCorCadEst.setVisible(false);
        btnAdiCadEst.setVisible(false);
        tblVarCorCadEst.setVisible(false);
        scrVarCorCadEst.setVisible(false);

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

        lblMarCadEst.setLocation(360, 150);
        lblModCadEst.setLocation(360, 200);
        lblQuaCadEst.setLocation(360, 250);
        lblPreCadEst.setLocation(360, 300);
        lblCorCadEst.setLocation(650, 150);
        lblMatCadEst.setLocation(650, 200);
        lblLocCadEst.setLocation(650, 250);
        lblDetCadEst.setLocation(650, 300);
        lblVarCorCadEst.setLocation(880, 220);

        DefaultTableModel model = (DefaultTableModel) tblVarCorCadEst.getModel();
        model.setRowCount(0);

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
        chkVarCorCadEst.setEnabled(true);
        chkVarCorCadEst.setSelected(false);

        lblProCadEst.setVisible(false);
        tblCadEst.setVisible(false);
        scrCadEst.setVisible(false);
        lblVarCorCadEst.setVisible(false);
        txtVarCorCadEst.setVisible(false);
        spnVarCorCadEst.setVisible(false);
        sepVarCorCadEst.setVisible(false);
        btnAdiCadEst.setVisible(false);
        tblVarCorCadEst.setVisible(false);
        scrVarCorCadEst.setVisible(false);

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

        lblMarCadEst.setLocation(360, 150);
        lblModCadEst.setLocation(360, 200);
        lblQuaCadEst.setLocation(360, 250);
        lblPreCadEst.setLocation(360, 300);
        lblCorCadEst.setLocation(650, 150);
        lblMatCadEst.setLocation(650, 200);
        lblLocCadEst.setLocation(650, 250);
        lblDetCadEst.setLocation(650, 300);
        lblVarCorCadEst.setLocation(880, 220);

        DefaultTableModel model = (DefaultTableModel) tblVarCorCadEst.getModel();
        model.setRowCount(0);

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

            if (txtCliOs.getText().isEmpty() || txtTelOs.getText().isEmpty() || txtEquOs.getText().isEmpty() || txtMarOs.getText().isEmpty() || txtModOs.getText().isEmpty() || txtDefOs.getText().isEmpty() || txtDatOs.getText().isEmpty() || txtRepOs.getText().isEmpty() || (txtDatSaiOs.getText().isEmpty() && chkGarOs.isSelected())) {

                JOptionPane.showMessageDialog(null, "Preencha todos os dados!", "Atenção", JOptionPane.WARNING_MESSAGE);

            } else {

                if (btnGerOs.getText().equals("Gerar")) {//VERIFICA SE IGUAL GERAR O BOTAP

                    int resp1 = JOptionPane.showOptionDialog(null, "Salvar OS no banco de dados?", "OS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                    if (resp1 != JOptionPane.CLOSED_OPTION) {//SE APERTAR X NO SALVAR BD

                        int resp2 = JOptionPane.showOptionDialog(null, "Quantas vias?", "OS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"1 Via", "2 Vias"}, "Sim");

                        if (resp2 != JOptionPane.CLOSED_OPTION) {//SE APERTAR X VIA

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

                            String datagarantia = "Sem Garantia";

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

                            JasperExportManager.exportReportToHtmlFile(print, "saida.html");

                            JasperViewer jc = new JasperViewer(print, false);
                            jc.setVisible(true);
                            jc.toFront();

                            pnlOs.setVisible(false);
                            pnlContent.setVisible(true);

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

                        DefaultTableModel model = (DefaultTableModel) tblOs.getModel();
                        model.setRowCount(0);

                        pnlOs.setVisible(false);
                        pnlContent.setVisible(true);

                    }
                }
            }

        } catch (JRException | SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular garantia!", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnGerOsActionPerformed

    private void btnCanOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanOsActionPerformed
        int resp1 = JOptionPane.showOptionDialog(null, "Todos os dados serão perdidos, tem certeza que deseja continuar?", "OS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp1 == JOptionPane.YES_OPTION) {

            if (lblNovaEntradaItens6.getText().equals("Alterar OS")) {
                pnlOs.setVisible(false);
                pnlGerOs.setVisible(true);
            } else {
                pnlOs.setVisible(false);
                pnlContent.setVisible(true);
            }

        }
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

    private void txtDatBusGerEntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDatBusGerEntKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {

                String data = formatterbanco.format(formatter.parse(txtDatBusGerEnt.getText()));

                btnExcGerEnt.setVisible(false);
                btnAltGerEnt.setVisible(false);

                btnAltGerEnt.setEnabled(false);
                btnExcGerEnt.setEnabled(false);

                tblGerEnt.setVisible(false);
                scrGerEnt.setVisible(false);

                tblGerEnt.repaint();

                if (tabelagerenciarentrada(tblGerEnt, scrGerEnt, data)) {

                    tblGerEnt.setVisible(true);
                    scrGerEnt.setVisible(true);

                    btnExcGerEnt.setVisible(true);
                    btnAltGerEnt.setVisible(true);

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
                chkAltGerEst.setEnabled(false);
                chkAltGerEst.setSelected(false);

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
        pnlContent.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) tblConEnt.getModel();
        model.setRowCount(0);
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
        btnBusConEnt.setEnabled(false);
        txtBusConEnt.setText(null);
        lblBusConEnt.setLocation(450, 100);

        tblConEnt.setVisible(false);
        scrConEnt.setVisible(false);

        DefaultTableModel model = (DefaultTableModel) tblConEnt.getModel();
        model.setRowCount(0);

        pnlContent.setVisible(false);
        pnlConEnt.setVisible(true);
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
            Logger.getLogger(main.class
                    .getName()).log(Level.SEVERE, null, ex);
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

    private void cmbSerCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSerCadEntActionPerformed
        if (cmbSerCadEnt.getSelectedIndex() != 0 && cmbSerCadEnt.isEnabled() && cmbSerCadEnt.getSelectedItem() != null) {

            itens selectedItem = (itens) cmbSerCadEnt.getSelectedItem();
            String textoSelecionado = selectedItem.getDescricao();

            if (textoSelecionado.equals("Troca de Chip")
                    || textoSelecionado.equals("Ativação eSIM")) {

                rbtnTroPreCadEnt.setEnabled(true);
                rbtnTroPlaCadEnt.setEnabled(true);

            } else {
                rbtnTroPreCadEnt.setEnabled(false);
                rbtnTroPlaCadEnt.setEnabled(false);

                btnGroup5.clearSelection();

            }
        }
    }//GEN-LAST:event_cmbSerCadEntActionPerformed

    private void btnCadVenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadVenMouseEntered
        btnCadVen.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnCadVenMouseEntered

    private void btnCadVenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadVenMouseExited
        btnCadVen.setForeground(corforeazul);
    }//GEN-LAST:event_btnCadVenMouseExited

    private void btnCadVenMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadVenMouseReleased
        lblCliCadVen.setLocation(350, 110);
        lblCpfCadVen.setLocation(350, 160);
        lblTelCadVen.setLocation(350, 210);
        lblAceCadVen.setLocation(350, 260);

        lblPlaCadVen.setLocation(650, 110);
        lblDatCadVen.setLocation(650, 160);
        lblVenCadVen.setLocation(650, 210);

        txtCliCadVen.setText(null);
        txtPlaCadVen.setText(null);
        txtCpfCadVen.setText(null);
        txtTelCadVen.setText(null);
        txtVenCadVen.setText(null);
        txtAceCadVen.setText(null);

        btnSalCadVen.setEnabled(false);

        LocalDate dataAtual = LocalDate.now();
        txtDatCadVen.setText(dataAtual.format(formatteratual));
        anitxtin(lblDatCadVen);

        pnlContent.setVisible(false);
        pnlCadVen.setVisible(true);
    }//GEN-LAST:event_btnCadVenMouseReleased

    private void btnVenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMouseEntered
        btnVen.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnVenMouseEntered

    private void btnVenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMouseExited
        btnVen.setForeground(corforeazul);
    }//GEN-LAST:event_btnVenMouseExited

    private void btnVenMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMouseReleased
        if (tabelavencimento(tblVen, scrVen)) {

            planosDAO pladao = new planosDAO();

            try {
                lblConPlaVen.setText(String.valueOf(pladao.buscar()));

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

            btnExcVen.setEnabled(false);
            btnAltVen.setEnabled(false);
            btnCopVen.setEnabled(false);
            btnCopAVen.setEnabled(false);
            btnWppVen.setEnabled(false);

            lblBusVen.setLocation(350, 350);
            txtBusVen.setText(null);
            lblErrVen.setVisible(false);

            pnlContent.setVisible(false);
            pnlVen.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "Sem planos. Cadastre-os primeiro!", "Planos", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnVenMouseReleased

    private void btnSalCadVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalCadVenActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja salvar o plano?", "Cadastrar Plano", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {

            try {

                vencimento ve = new vencimento();
                vencimentoDAO vedao = new vencimentoDAO();

                if (lblCadastrarPlano.getText().equals("Cadastrar Plano")) {

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

                    DefaultTableModel model = (DefaultTableModel) tblVen.getModel();
                    model.setRowCount(0);

                    pnlCadVen.setVisible(false);
                    pnlContent.setVisible(true);

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

                    DefaultTableModel model = (DefaultTableModel) tblVen.getModel();
                    model.setRowCount(0);

                    pnlCadVen.setVisible(false);
                    pnlContent.setVisible(true);

                }

            } catch (SQLException | ParseException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btnSalCadVenActionPerformed

    private void btnCanCadVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanCadVenActionPerformed
        if (btnSalCadVen.isEnabled()) {

            int resp = JOptionPane.showOptionDialog(null, "Todos os dados serão perdidos, deseja continuar?", "Cadastrar Plano", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                if ("Alterar Plano".equals(lblCadastrarPlano.getText())) {
                    pnlCadVen.setVisible(false);
                    pnlVen.setVisible(true);
                } else {
                    pnlCadVen.setVisible(false);
                    pnlContent.setVisible(true);
                }

            }

        } else {
            pnlCadVen.setVisible(false);
            pnlContent.setVisible(true);
        }
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
        btnSalCadVen.setEnabled(true);

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
//      btnWppVen.setEnabled(true);
        btnAltVen.setEnabled(true);
        btnCopVen.setEnabled(true);
        btnCopAVen.setEnabled(true);
    }//GEN-LAST:event_tblVenMouseClicked

    private void btnVolVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolVenActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblVen.getModel();
        model.setRowCount(0);

        pnlVen.setVisible(false);
        pnlContent.setVisible(true);
    }//GEN-LAST:event_btnVolVenActionPerformed

    private void btnWppVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWppVenActionPerformed
//        try {
//
//            vencimentoDAO vendao = new vencimentoDAO();
//
//            List<String[]> listaverifica = vendao.buscarverificaplano();
//
//            Iterator<String[]> iterator = listaverifica.iterator();
//
//            while (iterator.hasNext()) {
//
//                String[] item = iterator.next();
//
//                if (!item[0].equals(tblVen.getValueAt(tblVen.getSelectedRow(), 2).toString())) {
//                    iterator.remove();
//                }
//
//            }
//
//            if (!listaverifica.isEmpty()) {
//
//                int c = 0;
//                String plano = null;
//
//                if (listaverifica.size() != 1) {
//
//                    Map<String, Integer> contagemItens = new HashMap<>();
//                    for (String[] array : listaverifica) {
//                        String item = Arrays.toString(array);
//                        contagemItens.put(item, contagemItens.getOrDefault(item, 0) + 1);
//                    }
//
//                    for (Map.Entry<String, Integer> entry : contagemItens.entrySet()) {
//
//                        String[] array = entry.getKey().substring(1, entry.getKey().length() - 1).split(", ");
//
//                        c++;
//
//                        if (c == 1) {//o primeiro
//
//                            if (entry.getValue() == 1) {
//                                plano = "dos seus planos *" + array[1] + "*";
//                            } else {
//                                plano = "dos seus *" + entry.getValue() + "* planos *" + array[1] + "*";
//                            }
//
//                        } else {
//
//                            if (c == contagemItens.entrySet().size()) {//se for o ultimo
//
//                                if (entry.getValue() == 1) {
//                                    plano = plano + " e o *" + array[1] + "*";
//                                } else {
//                                    plano = plano + " e os *" + entry.getValue() + " " + array[1] + "*";
//                                }
//                            } else {
//
//                                if (entry.getValue() == 1) {
//                                    plano = plano + ", *" + array[1] + "*";
//                                } else {
//                                    plano = plano + ", dos *" + entry.getValue() + " " + array[1] + "*";
//                                }
//
//                            }
//
//                        }
//
//                    }
//
//                    plano = plano + ", contratados conosco no dia *" + tblVen.getValueAt(tblVen.getSelectedRow(), 5).toString()
//                            + "*.\n\n";
//
//                } else {//quando tiver so um
//                    plano = "do seu plano *" + tblVen.getValueAt(tblVen.getSelectedRow(), 4).toString() + "*"
//                            + ", contratado conosco no dia *" + tblVen.getValueAt(tblVen.getSelectedRow(), 5).toString()
//                            + "*.\n\n";
//                }
//
//                String texto = "*Empório Cell - TIM*\n\n"
//                        + "Olá, tudo bem? Esperamos que sim!\n\n"
//                        + "Estamos aqui para lembrá-lo " + plano
//                        + "Traga sua família e amigos para a *rede móvel líder em cobertura no Brasil*!\n"
//                        + "Para qualquer dúvida, estamos à disposição. Agradecemos por confiar em nossos serviços!";
//
//                String msg = texto.replaceAll(" ", "%20").replaceAll("\n", "%0A");
//
//                int resp = JOptionPane.showOptionDialog(null, texto.replaceAll("\\*", "") + "\n\nEnviar mensagem ao cliente?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");
//
//                if (resp == JOptionPane.YES_OPTION) {
//
//                    String l = "https://api.whatsapp.com/send/?phone=55" + (tblVen.getValueAt(tblVen.getSelectedRow(), 1).toString()).replaceAll("-", "").replaceAll("\\(", "").replaceAll(" ", "").replaceAll("\\)", "") + "&text=" + msg + "&app_absent=0";
//
//                    URI link = new URI(l);
//
//                    Desktop.getDesktop().browse(link);
//
//                    int resp1 = JOptionPane.showOptionDialog(null, "Navegador aberto para envio!\n\nMarcar como concluído?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");
//
//                    if (resp1 == JOptionPane.YES_OPTION) {
//
//                        try {
//
//                            vencimento ve = new vencimento();
//                            vencimentoDAO vedao = new vencimentoDAO();
//
//                            ve.setCpf(tblVen.getValueAt(tblVen.getSelectedRow(), 2).toString());
//
//                            vedao.marcarok(ve);
//
//                            JOptionPane.showMessageDialog(null, "Marcado com sucesso!", "Planos", JOptionPane.INFORMATION_MESSAGE);
//
//                        } catch (SQLException ex) {
//                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                    }
//
//                    if (tabelavencimento(tblVen, scrVen)) {
//
//                    } else {
//
//                        JOptionPane.showMessageDialog(null, "Sem planos. Cadastre-os primeiro!", "Planos", JOptionPane.INFORMATION_MESSAGE);
//                        pnlVen.setVisible(false);
//                        
//                    }
//
//                    verificavencimento();
//
//                    btnWppVen.setEnabled(false);
//                    btnExcVen.setEnabled(false);
//                    btnAltVen.setEnabled(false);
//                    btnCopVen.setEnabled(false);
//                    btnCopAVen.setEnabled(false);
//
//                }
//
//            } else {
//
//                int resp = JOptionPane.showOptionDialog(null, "Atenção, mensagem de aviso indisponível para este cliente!\n\nAbrir o WhatsApp mesmo assim?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");
//
//                if (resp == JOptionPane.YES_OPTION) {
//
//                    String l = "https://api.whatsapp.com/send/?phone=55" + (tblVen.getValueAt(tblVen.getSelectedRow(), 1).toString()).replaceAll("-", "").replaceAll("\\(", "").replaceAll(" ", "").replaceAll("\\)", "");
//
//                    URI link = new URI(l);
//
//                    Desktop.getDesktop().browse(link);
//
//                }
//
//            }
//
//        } catch (URISyntaxException | IOException | SQLException ex) {
//            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
//        }

        int resp1 = JOptionPane.showOptionDialog(null, "Tem certeza que deseja limpar destaque de planos com vencimento próximo?", "Planos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp1 == JOptionPane.YES_OPTION) {

            try {

                vencimento ve = new vencimento();
                vencimentoDAO vedao = new vencimentoDAO();

                vedao.limparverde(ve);

                btnWppVen.setEnabled(false);

                tabelavencimento(tblVen, scrVen);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }

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

                DefaultTableModel model = (DefaultTableModel) tblVen.getModel();
                model.setRowCount(0);

                pnlVen.setVisible(false);
                pnlContent.setVisible(true);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnExcVenActionPerformed

    private void btnVenMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenMasActionPerformed
        if (txtNomMas.getText().isEmpty() || txtNumConMas.getText().isEmpty() || txtPlaMas.getText().isEmpty() || txtVenMas.getText().isEmpty() || txtNumAceMas.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);

        } else {

            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja cadastrar o plano?", "Cadastrar Plano", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {

                try {

                    vencimento ve = new vencimento();
                    vencimentoDAO vedao = new vencimentoDAO();

                    ve.setCliente(txtNomMas.getText());
                    ve.setCpf(txtCpfMas.getText());
                    ve.setVencimento(formatterbanco.format(((formatter.parse(txtVenMas.getText())))));
                    ve.setTelefone(txtNumConMas.getText());
                    if (txtNumPorMas.getText().isEmpty()) {
                        ve.setAcesso(txtNumAceMas.getText());
                    } else {
                        ve.setAcesso(txtNumPorMas.getText());
                    }

                    ve.setPlano(txtPlaMas.getText());
                    ve.setData(formatterbanco.format(new Date()));

                    vedao.inserir(ve, "0");

                    int resp1 = JOptionPane.showOptionDialog(null, "Plano cadastrado com sucesso! Deseja adicionar outro plano para o mesmo cliente?", "Sucesso", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

                    if (resp1 == JOptionPane.YES_OPTION) {

                        txtNumAceMas.setText(null);
                        txtNumPorMas.setText(null);
                        txtPlaMas.setText(null);
                        txtVenMas.setText(null);

                        lblNumAceMas.setLocation(340, 210);
                        lblNumPorMas.setLocation(340, 260);
                        lblPlaMas.setLocation(340, 310);
                        lblVenMas.setLocation(80, 260);

                        chkMelMas.setSelected(false);
                        chkAppMas.setSelected(false);
                        chkBolMas.setSelected(true);
                        rbtnAtiMas.setSelected(true);

                        txtAreMas.setText(null);

                        btnCopMas.setVisible(false);

                    } else {

                        DefaultTableModel model = (DefaultTableModel) tblVen.getModel();
                        model.setRowCount(0);

                        pnlCadVen.setVisible(false);
                        pnlContent.setVisible(true);

                    }

                } catch (SQLException | ParseException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }//GEN-LAST:event_btnVenMasActionPerformed

    private void btnVenPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenPriMouseEntered
        btnVenPri.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnVenPriMouseEntered

    private void btnVenPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenPriMouseExited
        btnVenPri.setForeground(new Color(255, 255, 255));
    }//GEN-LAST:event_btnVenPriMouseExited

    private void btnVenPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenPriMouseReleased
//        if (!pnlVen.isVisible()) {
//
//            if (tabelavencimento(tblVen, scrVen)) {
//
//                planosDAO pladao = new planosDAO();
//
//                try {
//                    lblConPlaVen.setText(String.valueOf(pladao.buscar()));
//                } catch (SQLException ex) {
//                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                //lblTitPri.setText("Planos");
//                //lblTitPri.setVisible(true);
//
//                btnExcVen.setEnabled(false);
        ////              btnWppVen.setEnabled(false);
//                btnAltVen.setEnabled(false);
//                btnCopVen.setEnabled(false);
//                btnCopAVen.setEnabled(false);
//
//                lblBusVen.setLocation(310, 280);
//                txtBusVen.setText(null);
//                lblErrVen.setVisible(false);
//
//               
//                pnlVen.setVisible(true);
//
//            } else {
//
//                JOptionPane.showMessageDialog(null, "Sem planos. Cadastre-os primeiro!", "Planos", JOptionPane.INFORMATION_MESSAGE);
//
//            }
//
//        } else {
//
//           
//            pnlVen.setVisible(true);
//
//        }

        if (!pnlDes.isVisible()) {

            if (tabeladespezas(tblConDes, scrConDes)) {

                pnlDes.setVisible(true);
                pnlContent.setVisible(false);

            } else {

                JOptionPane.showMessageDialog(null, "Sem afazeres. Cadastre-as primeiro!", "Gerenciar Afazeres", JOptionPane.INFORMATION_MESSAGE);

            }

        } else {

            pnlDes.setVisible(true);
            pnlContent.setVisible(false);

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
        lblR$CadEst.setVisible(true);
        cmbChiCadEst.setSelectedIndex(0);

        if ("Capinha".equals(txtTipCadEst.getText())) {

            lblMarCadEst.setLocation(360, 130);
            lblModCadEst.setLocation(360, 180);
            lblQuaCadEst.setLocation(360, 230);
            lblPreCadEst.setLocation(360, 280);
            lblMatCadEst.setLocation(650, 200);
            lblLocCadEst.setLocation(650, 230);

        } else {

            lblMarCadEst.setLocation(360, 130);
            lblModCadEst.setLocation(360, 180);
            lblQuaCadEst.setLocation(360, 230);
            lblPreCadEst.setLocation(360, 280);
            lblMatCadEst.setLocation(650, 180);
            lblLocCadEst.setLocation(650, 230);

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
            lblCorCadEst.setLocation(650, 130);
        } else {
            lblCorCadEst.setLocation(650, 150);
        }

        if (!lista1.get(tblCadEst.getSelectedRow()).getDetalhes().isEmpty()) {
            lblDetCadEst.setLocation(650, 280);
            txtDetCadEst.setText(lista1.get(tblCadEst.getSelectedRow()).getDetalhes());
        } else {
            lblDetCadEst.setLocation(650, 300);
        }
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

        lblCadastrarPlano.setText("Alterar Plano");

        lblCliCadVen.setLocation(350, 90);
        lblCpfCadVen.setLocation(350, 140);
        lblTelCadVen.setLocation(350, 190);
        lblAceCadVen.setLocation(350, 240);
        lblPlaCadVen.setLocation(650, 90);
        lblDatCadVen.setLocation(650, 140);
        lblVenCadVen.setLocation(650, 190);

        pnlVen.setVisible(false);
        pnlCadVen.setVisible(true);

        btnCanCadVen.grabFocus();
    }//GEN-LAST:event_btnAltVenActionPerformed

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
        txtValJur.setText(null);

        lblValFinJur.setText("R$ 0,00");
        lblValJur2.setText("R$ 0,00");
        lblValJurJur.setText("R$ 0,00");
        lblValParJur.setText("R$ 0,00");
        lblValMesPreJur.setText("R$ 0,00");
        lblR$Jur.setVisible(false);
        lblValJur.setLocation(180, 200);
        spnParJur.setValue(0);

        lblR$Jur.setVisible(false);

        btnCalJur.setEnabled(false);
        lblParJur.setEnabled(false);
        spnParJur.setEnabled(false);

        pnlJur.setVisible(true);
        pnlContent.setVisible(false);
    }//GEN-LAST:event_btnJurPriMouseReleased

    private void txtValJurKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValJurKeyTyped
        btnCalJur.setEnabled(true);
        lblParJur.setEnabled(true);
        spnParJur.setEnabled(true);

        if (evt.getKeyChar() == 44) {
            if (txtValJur.getText().contains(",")) {
                evt.consume();
            }

        } else if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_txtValJurKeyTyped

    private void btnVolJurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolJurActionPerformed
        if (!txtValJur.getText().isEmpty()) {

            int resp = JOptionPane.showOptionDialog(null, "Cancelar? Todos os dados serão perdidos.", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

            if (resp == JOptionPane.YES_OPTION) {
                pnlJur.setVisible(false);
                pnlContent.setVisible(true);
            }

        } else {
            pnlJur.setVisible(false);
            pnlContent.setVisible(true);
        }
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
                    Logger.getLogger(main.class
                            .getName()).log(Level.SEVERE, null, ex);
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
                txtPlaMas.setText("TIM Controle B Express");
                break;
            case '5':
                txtPlaMas.setText("TIM Controle C Express");
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
                txtPlaMas.setText("TIM Controle Redes Sociais");
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
        int resp;

        if (btnAtvGerTipSer.getText().equals("Ativar")) {
            resp = JOptionPane.showOptionDialog(null, "Ativar serviço?", "Ativar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");
        } else {
            resp = JOptionPane.showOptionDialog(null, "Desativar serviço? Este serviço não poderá ser vinculado à novas entradas!", "Desativar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");
        }

        if (resp == JOptionPane.YES_OPTION) {

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

                lblDesGerTipSer.setLocation(460, 330);
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

        }
    }//GEN-LAST:event_btnAtvGerTipSerActionPerformed

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

        txtCliOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Nome")).toString());
        txtEndOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Endereço")).toString());
        txtTelOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Telefone")).toString());
        txtEquOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Equipamento")).toString());
        txtMarOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Marca")).toString());
        txtModOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Modelo")).toString());
        txtRepOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Reparo")).toString());
        txtDefOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Defeito")).toString());
        txtDatOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Entrada")).toString());
        txtDatSaiOs.setText(tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Saída")).toString());

        txtPreOs.setText(preco.substring(3, preco.length()));

        if (tblOs.getValueAt(tblOs.getSelectedRow(), tblOs.getColumnModel().getColumnIndex("Garantia")).toString().equals("Sim")) {
            chkGarOs.setSelected(true);
        } else {
            chkGarOs.setSelected(false);
        }

        lblCliOs.setLocation(310, 90);
        lblTelOs.setLocation(310, 140);
        if (txtEndOs.getText().isEmpty()) {
            lblEndOs.setLocation(310, 210);
        } else {
            lblEndOs.setLocation(310, 190);
        }
        lblDatEntOs.setLocation(310, 240);
        lblHorOs.setLocation(310, 290);
        lblPreOs.setLocation(310, 340);

        lblEquOs.setLocation(640, 90);
        lblMarOs.setLocation(640, 140);
        lblModOs.setLocation(640, 190);
        lblConOs.setLocation(640, 240);
        lblDefOs.setLocation(640, 290);

        lblR$Os.setVisible(true);

        btnGerOs.setEnabled(true);

        lblNovaEntradaItens6.setText("Alterar OS");
        btnGerOs.setText("Salvar");

        pnlGerOs.setVisible(false);
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

                    datagarantia = "Sem Garantia";

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
                }

                JasperPrint print = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource(1));

                JasperViewer jc = new JasperViewer(print, false);
                jc.setVisible(true);
                jc.toFront();

            }

        } catch (JRException | ParseException ex) {
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

                DefaultTableModel model = (DefaultTableModel) tblOs.getModel();
                model.setRowCount(0);

                pnlGerOs.setVisible(false);
                pnlContent.setVisible(true);

            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnExcGerOsActionPerformed

    private void btnVolGerOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolGerOsActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblOs.getModel();
        model.setRowCount(0);

        pnlGerOs.setVisible(false);
        pnlContent.setVisible(true);
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

            btnGerGerOs.setEnabled(false);
            btnAltGerOs.setEnabled(false);
            btnExcGerOs.setEnabled(false);

        }
    }//GEN-LAST:event_txtBusGerOsKeyPressed

    private void btnGerOsPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerOsPriMouseEntered
        btnGerOsPri.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnGerOsPriMouseEntered

    private void btnGerOsPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerOsPriMouseExited
        btnGerOsPri.setForeground(corforeazul);
    }//GEN-LAST:event_btnGerOsPriMouseExited

    private void btnGerOsPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGerOsPriMouseReleased
        if (tabelaos(tblOs, scrOs)) {

            btnExcGerOs.setEnabled(false);
            btnGerGerOs.setEnabled(false);
            btnAltGerOs.setEnabled(false);

            lblBusGerOs.setLocation(580, 390);
            txtBusGerOs.setText(null);
            lblErrGerOs.setVisible(false);

            pnlGerOs.setVisible(true);
            pnlContent.setVisible(false);

        } else {

            JOptionPane.showMessageDialog(null, "Sem OS. Cadastre-as primeiro!", "OS", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_btnGerOsPriMouseReleased

    private void btnVenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVenMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVenMousePressed

    private void btnCadOsPriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadOsPriMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCadOsPriMouseEntered

    private void btnCadOsPriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadOsPriMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCadOsPriMouseExited

    private void btnCadOsPriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCadOsPriMouseReleased
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

        btnGerOs.setEnabled(false);

        lblCliOs.setLocation(310, 110);
        lblTelOs.setLocation(310, 160);
        lblEndOs.setLocation(310, 210);
        lblDatEntOs.setLocation(310, 260);
        lblHorOs.setLocation(310, 310);
        lblPreOs.setLocation(310, 360);

        lblEquOs.setLocation(640, 110);
        lblMarOs.setLocation(640, 160);
        lblModOs.setLocation(640, 210);
        lblConOs.setLocation(640, 260);
        lblDefOs.setLocation(640, 310);

        lblR$Os.setVisible(false);

        lblNovaEntradaItens6.setText("Gerar OS");
        btnGerOs.setText("Gerar");

        pnlContent.setVisible(false);
        pnlOs.setVisible(true);
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
            String trocapre = String.valueOf(pdDAO.buscar(3));
            String trocapla = String.valueOf(pdDAO.buscar(4));

            txtAreMas.setText(
                    "*PARCIAL DO DIA " + data + "*\n\n"
                    + "Plano Controle: " + con
                    + "\nPlano Black: " + pos
                    + "\n*Troca de Chip*"
                    + "\nPré-pago: " + trocapre
                    + "\nPlano: " + trocapla
            );

            txtAreMas.setCaretPosition(0);

            btnCopMas.setVisible(true);

        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnParMasActionPerformed

    private void btnConMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConMasActionPerformed
        copiarcontrato();
    }//GEN-LAST:event_btnConMasActionPerformed

    private void tblVarCorCadEstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVarCorCadEstMouseClicked
        DefaultTableModel modelo = (DefaultTableModel) tblVarCorCadEst.getModel();
        modelo.removeRow(tblVarCorCadEst.getSelectedRow());
           
        if(modelo.getRowCount()==0){
            tblVarCorCadEst.setVisible(false);   
            scrVarCorCadEst.setVisible(false); 
        }
    }//GEN-LAST:event_tblVarCorCadEstMouseClicked

    private void txtVarCorCadEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVarCorCadEstFocusGained
        if (txtVarCorCadEst.getText().isEmpty()) {
            anitxtin(lblVarCorCadEst);
        }
    }//GEN-LAST:event_txtVarCorCadEstFocusGained

    private void txtVarCorCadEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtVarCorCadEstFocusLost
        if (txtVarCorCadEst.getText().isEmpty()) {
            anitxtout(lblVarCorCadEst);
        }
    }//GEN-LAST:event_txtVarCorCadEstFocusLost

    private void btnAdiCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdiCadEstActionPerformed
        adicionarcor(tblVarCorCadEst, txtVarCorCadEst.getText(), (int) spnVarCorCadEst.getValue());
        tblVarCorCadEst.setVisible(true);
        scrVarCorCadEst.setVisible(true);
        txtVarCorCadEst.setText(null);
        spnVarCorCadEst.setValue(1);
        anitxtout(lblVarCorCadEst);
    }//GEN-LAST:event_btnAdiCadEstActionPerformed

    private void chkVarCorCadEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkVarCorCadEstActionPerformed
        if (!chkVarCorCadEst.isSelected()) {

            DefaultTableModel model = (DefaultTableModel) tblVarCorCadEst.getModel();
            model.setRowCount(0);

            lblVarCorCadEst.setVisible(false);
            txtVarCorCadEst.setVisible(false);
            spnVarCorCadEst.setVisible(false);
            sepVarCorCadEst.setVisible(false);
            btnAdiCadEst.setVisible(false);
            scrVarCorCadEst.setVisible(false);
            tblVarCorCadEst.setVisible(false);
            txtVarCorCadEst.setText(null);
            spnVarCorCadEst.setValue(1);
            lblVarCorCadEst.setLocation(880, 220);

        } else {

            tabelacores();

            lblVarCorCadEst.setVisible(true);
            txtVarCorCadEst.setVisible(true);
            spnVarCorCadEst.setVisible(true);
            sepVarCorCadEst.setVisible(true);
            btnAdiCadEst.setVisible(true);

        }
    }//GEN-LAST:event_chkVarCorCadEstActionPerformed

    private void txtChip1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChip1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChip1GerEstFocusGained

    private void txtChip1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChip1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChip1GerEstFocusLost

    private void txtPre1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPre1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPre1GerEstFocusGained

    private void txtPre1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPre1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPre1GerEstFocusLost

    private void txtQua1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQua1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQua1GerEstFocusGained

    private void txtQua1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQua1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQua1GerEstFocusLost

    private void txtMod1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMod1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMod1GerEstFocusGained

    private void txtMod1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMod1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMod1GerEstFocusLost

    private void txtMar1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMar1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMar1GerEstFocusGained

    private void txtMar1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMar1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMar1GerEstFocusLost

    private void txtDet1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDet1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDet1GerEstFocusGained

    private void txtDet1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDet1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDet1GerEstFocusLost

    private void txtLoc1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLoc1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoc1GerEstFocusGained

    private void txtLoc1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLoc1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoc1GerEstFocusLost

    private void txtMat1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMat1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMat1GerEstFocusGained

    private void txtMat1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMat1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMat1GerEstFocusLost

    private void txtCor1GerEstFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCor1GerEstFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCor1GerEstFocusGained

    private void txtCor1GerEstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCor1GerEstFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCor1GerEstFocusLost

    private void rbtnTroPlaCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnTroPlaCadEntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnTroPlaCadEntActionPerformed

    private void rbtnTroPreCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnTroPreCadEntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnTroPreCadEntActionPerformed

    private void cmbSerCadEntMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbSerCadEntMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSerCadEntMouseClicked

    private void cmbSerCadEntMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbSerCadEntMouseReleased

    }//GEN-LAST:event_cmbSerCadEntMouseReleased

    private void btnRelatorioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelatorioMouseEntered
        btnRelatorio.setForeground(corforeazulenter);
    }//GEN-LAST:event_btnRelatorioMouseEntered

    private void btnRelatorioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelatorioMouseExited
        btnRelatorio.setForeground(corforeazul);
    }//GEN-LAST:event_btnRelatorioMouseExited

    private void btnRelatorioMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRelatorioMouseReleased
        btnNumDiaRel.setVisible(false);
        btnMenDiaRel.setVisible(false);
        btnMaiDiaRel.setVisible(false);
        lblDiaRel.setVisible(false);

        rbtnTodRel.setSelected(true);

        lblDatIniRel.setLocation(270, 220);
        lblDatFinRel.setLocation(420, 220);

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

        pnlRel.setVisible(true);
        pnlContent.setVisible(false);

        btnVolRel.grabFocus();
    }//GEN-LAST:event_btnRelatorioMouseReleased

    private void btnAltGerEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltGerEntActionPerformed
        txtDatGerEnt.setText(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 0).toString());
        txtPreGerEnt.setText((!"Não Aplicável".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 5).toString())) ? (tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 5).toString()).substring(3, tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 5).toString().length()) : null);
        txtDetGerEnt.setText((!"Sem Detalhes".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 9).toString())) ? tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 9).toString() : null);
        txtCliGerEnt.setText(null);
        txtCusGerEnt.setText(null);
        txtForGerEnt.setText(null);

        lblR$GerEnt.setVisible(false);
        lblR$CusGerEnt.setVisible(false);

        txtBusIteGerEnt.setEnabled(false);
        lblBusIteGerEnt.setEnabled(false);
        sepBusIteCadEnt1.setForeground(Color.GRAY);
        txtBusIteGerEnt.setText(null);
        lblBusIteGerEnt.setLocation(60, 160);

        txtCliGerEnt.setEnabled(false);
        lblCliGerEnt.setEnabled(false);
        sepCliGerEnt.setForeground(Color.GRAY);

        txtCusGerEnt.setEnabled(false);
        lblCusGerEnt.setEnabled(false);
        sepCusGerEnt.setForeground(Color.GRAY);

        txtForGerEnt.setEnabled(false);
        lblForGerEnt.setEnabled(false);
        sepForGerEnt.setForeground(Color.GRAY);

        estoque es = new estoque();

        es.setTipoproduto("Capinha");

        es.setModelo("");

        tabelaestoqueconsulta(es, tblEstIteGerEnt, scrEstIteGerEnt);

        tabelaitensselecionadosgerenciar(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 10).toString());

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

            txtCliGerEnt.setText((!"Não Informado".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 3).toString())) ? tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 3).toString() : null);
            txtCusGerEnt.setText((!"Não Aplicável".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString())) ? (tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString()).substring(3, tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 6).toString().length()) : null);
            txtForGerEnt.setText((!"Não Informado".equals(tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 8).toString())) ? tblGerEnt.getValueAt(tblGerEnt.getSelectedRow(), 8).toString() : null);

            txtCliGerEnt.setEnabled(true);
            lblCliGerEnt.setEnabled(true);
            sepCliGerEnt.setForeground(corforeazul);

            txtCusGerEnt.setEnabled(true);
            lblCusGerEnt.setEnabled(true);
            sepCusGerEnt.setForeground(corforeazul);

            txtForGerEnt.setEnabled(true);
            lblForGerEnt.setEnabled(true);
            sepForGerEnt.setForeground(corforeazul);

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

        } else {

            btnIteGerEnt.setEnabled(true);
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

        }

        if (!txtDatGerEnt.getText().isEmpty()) {
            lblDatGerEnt.setLocation(380, 130);
        } else {
            lblDatGerEnt.setLocation(380, 150);
        }

        if (!txtPreGerEnt.getText().isEmpty()) {
            lblPreGerEnt.setLocation(380, 180);
            lblR$GerEnt.setVisible(true);
        } else {
            lblPreGerEnt.setLocation(380, 200);
            lblR$GerEnt.setVisible(false);
        }

        if (!txtDetGerEnt.getText().isEmpty()) {
            lblDetGerEnt.setLocation(380, 230);
        } else {
            lblDetGerEnt.setLocation(380, 250);
        }

        if (!txtCliGerEnt.getText().isEmpty()) {
            lblCliGerEnt.setLocation(620, 130);
        } else {
            lblCliGerEnt.setLocation(620, 150);
        }

        if (!txtCusGerEnt.getText().isEmpty()) {
            lblCusGerEnt.setLocation(620, 180);
            lblR$CusGerEnt.setVisible(true);
        } else {
            lblCusGerEnt.setLocation(620, 200);
            lblR$CusGerEnt.setVisible(false);
        }

        if (!txtForGerEnt.getText().isEmpty()) {
            lblForGerEnt.setLocation(620, 230);
        } else {
            lblForGerEnt.setLocation(620, 250);
        }

        pnlGerEnt.setVisible(false);
        pnlAlterarEntrada.setVisible(true);
    }//GEN-LAST:event_btnAltGerEntActionPerformed

    private void btnCanAltEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanAltEntActionPerformed
        int resp = JOptionPane.showOptionDialog(null, "Antes de cancelar, verifique a tabela de produtos selecionados e remova aqueles que não fazem parte da entrada! Deseja cancelar?", "Cancelar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, "Sim");

        if (resp == JOptionPane.YES_OPTION) {
            pnlAlterarEntrada.setVisible(false);
            pnlGerEnt.setVisible(true);

        }
    }//GEN-LAST:event_btnCanAltEntActionPerformed

    private void txtBusIteCadEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusIteCadEntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusIteCadEntActionPerformed

    private void txtBusIteCadEntMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusIteCadEntMousePressed
        if (txtBusIteCadEnt.getText().isEmpty() && txtBusIteCadEnt.isEnabled() && !txtBusIteCadEnt.hasFocus()) {
            anitxtin(lblBusIteCadEnt);
        }
    }//GEN-LAST:event_txtBusIteCadEntMousePressed

    private void txtBusIteGerEntMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusIteGerEntMousePressed
        if (txtBusIteGerEnt.getText().isEmpty() && txtBusIteGerEnt.isEnabled() && !txtBusIteGerEnt.hasFocus()) {
            anitxtin(lblBusIteGerEnt);
        }
    }//GEN-LAST:event_txtBusIteGerEntMousePressed

    private void txtCliCadVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliCadVenKeyTyped

    }//GEN-LAST:event_txtCliCadVenKeyTyped

    private void txtPlaCadVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlaCadVenKeyTyped

    }//GEN-LAST:event_txtPlaCadVenKeyTyped

    private void txtRepOsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRepOsKeyTyped
        btnGerOs.setEnabled(true);
    }//GEN-LAST:event_txtRepOsKeyTyped
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            FlatLightLaf.setup();
            new main().setVisible(false);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdiCadEst;
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
    private javax.swing.JButton btnCanAltEnt;
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
    private javax.swing.ButtonGroup btnGroup5;
    private javax.swing.JButton btnIteCadEnt;
    private javax.swing.JButton btnIteGerEnt;
    private javax.swing.JLabel btnJurPri;
    private javax.swing.JLabel btnMaiDiaRel;
    private javax.swing.JLabel btnMasPla;
    private javax.swing.JLabel btnMenDiaRel;
    private javax.swing.JLabel btnMesRel;
    private javax.swing.JLabel btnNumDiaRel;
    private javax.swing.JButton btnParMas;
    private javax.swing.JLabel btnRelatorio;
    private javax.swing.JButton btnSalCadEnt;
    private javax.swing.JButton btnSalCadEst;
    private javax.swing.JButton btnSalCadVen;
    private javax.swing.JButton btnSalDes;
    private javax.swing.JButton btnSalGerEnt;
    private javax.swing.JButton btnSalTipSer;
    private javax.swing.JLabel btnSemRel;
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
    private javax.swing.JCheckBox chkAltGerEst;
    private javax.swing.JCheckBox chkAppMas;
    private javax.swing.JRadioButton chkBolMas;
    private javax.swing.JRadioButton chkCarMas;
    private javax.swing.JCheckBox chkCus;
    private javax.swing.JRadioButton chkDebMas;
    private javax.swing.JCheckBox chkGarOs;
    private javax.swing.JCheckBox chkMelMas;
    private javax.swing.JCheckBox chkVarCorCadEst;
    private javax.swing.JComboBox<String> cmbChiCadEst;
    private javax.swing.JComboBox<String> cmbChiGerEst;
    private javax.swing.JComboBox<String> cmbRel;
    private javax.swing.JComboBox<String> cmbSerCadEnt;
    private javax.swing.JComboBox<String> cmbSerGerEnt;
    private javax.swing.JLabel imgLogo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAceCadVen;
    private javax.swing.JLabel lblAlterarEntrada;
    private javax.swing.JLabel lblBusConEnt;
    private javax.swing.JLabel lblBusConEst;
    private javax.swing.JLabel lblBusGerEst;
    private javax.swing.JLabel lblBusGerOs;
    private javax.swing.JLabel lblBusIteCadEnt;
    private javax.swing.JLabel lblBusIteGerEnt;
    private javax.swing.JLabel lblBusVen;
    private javax.swing.JLabel lblBusVen2;
    private javax.swing.JLabel lblCadastrarPlano;
    private javax.swing.JLabel lblCadastrarPlano1;
    private javax.swing.JLabel lblChiCadEst;
    private javax.swing.JLabel lblChiGerEst;
    private javax.swing.JLabel lblCli;
    private javax.swing.JLabel lblCliCadEnt;
    private javax.swing.JLabel lblCliCadVen;
    private javax.swing.JLabel lblCliGerEnt;
    private javax.swing.JLabel lblCliOs;
    private javax.swing.JLabel lblConOs;
    private javax.swing.JLabel lblConPlaVen;
    private javax.swing.JLabel lblConsultarEntrada;
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
    private javax.swing.JLabel lblEntrada;
    private javax.swing.JLabel lblEquOs;
    private javax.swing.JLabel lblErrGerOs;
    private javax.swing.JLabel lblErrVen;
    private javax.swing.JLabel lblEstIteCadEnt;
    private javax.swing.JLabel lblEstIteGerEnt;
    private javax.swing.JLabel lblEstoque;
    private javax.swing.JLabel lblForCadEnt;
    private javax.swing.JLabel lblForGerEnt;
    private javax.swing.JLabel lblGerarOS;
    private javax.swing.JLabel lblGerenciarEntrada;
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
    private javax.swing.JLabel lblNovaEntrada;
    private javax.swing.JLabel lblNovaEntradaItens;
    private javax.swing.JLabel lblNovaEntradaItens1;
    private javax.swing.JLabel lblNovaEntradaItens10;
    private javax.swing.JLabel lblNovaEntradaItens11;
    private javax.swing.JLabel lblNovaEntradaItens12;
    private javax.swing.JLabel lblNovaEntradaItens13;
    private javax.swing.JLabel lblNovaEntradaItens14;
    private javax.swing.JLabel lblNovaEntradaItens2;
    private javax.swing.JLabel lblNovaEntradaItens3;
    private javax.swing.JLabel lblNovaEntradaItens4;
    private javax.swing.JLabel lblNovaEntradaItens5;
    private javax.swing.JLabel lblNovaEntradaItens6;
    private javax.swing.JLabel lblNovaEntradaItens8;
    private javax.swing.JLabel lblNovaEntradaItens9;
    private javax.swing.JLabel lblNumAceMas;
    private javax.swing.JLabel lblNumConMas;
    private javax.swing.JLabel lblNumPorMas;
    private javax.swing.JLabel lblOutros;
    private javax.swing.JLabel lblParCadEnt;
    private javax.swing.JLabel lblParJur;
    private javax.swing.JLabel lblPlaCadVen;
    private javax.swing.JLabel lblPlaMas;
    private javax.swing.JLabel lblPlanos;
    private javax.swing.JLabel lblPlanos1;
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
    private javax.swing.JLabel lblVarCorCadEst;
    private javax.swing.JLabel lblVen;
    private javax.swing.JLabel lblVenCadVen;
    private javax.swing.JLabel lblVenMas;
    private javax.swing.JPanel pnlAlterarEntrada;
    private javax.swing.JPanel pnlCadDes;
    private javax.swing.JPanel pnlCadEnt;
    private javax.swing.JPanel pnlCadEst;
    private javax.swing.JPanel pnlCadTipSer;
    private javax.swing.JPanel pnlCadVen;
    private javax.swing.JPanel pnlConEnt;
    private javax.swing.JPanel pnlConEst;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlDes;
    private javax.swing.JPanel pnlEntrada;
    private javax.swing.JPanel pnlEstoque;
    private javax.swing.JPanel pnlGerDes;
    private javax.swing.JPanel pnlGerEnt;
    private javax.swing.JPanel pnlGerEst;
    private javax.swing.JPanel pnlGerOs;
    private javax.swing.JPanel pnlGerTipSer;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlIteCadEnt;
    private javax.swing.JPanel pnlIteGerEnt;
    private javax.swing.JPanel pnlJur;
    private javax.swing.JPanel pnlMas;
    private javax.swing.JPanel pnlOS;
    private javax.swing.JPanel pnlOs;
    private javax.swing.JPanel pnlOutros;
    private javax.swing.JPanel pnlPlanos;
    public static javax.swing.JPanel pnlPrincipal;
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
    private javax.swing.JRadioButton rbtnTroPlaCadEnt;
    private javax.swing.JRadioButton rbtnTroPreCadEnt;
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
    private javax.swing.JScrollPane scrVarCorCadEst;
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
    private javax.swing.JSeparator sepVarCorCadEst;
    private javax.swing.JSeparator sepVenCadVen;
    private javax.swing.JSpinner spnParCadEnt;
    private javax.swing.JSpinner spnParJur;
    private javax.swing.JSpinner spnVarCorCadEst;
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
    private javax.swing.JTable tblVarCorCadEst;
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
    private javax.swing.JTextField txtChip1GerEst;
    private javax.swing.JTextField txtCliCadEnt;
    private javax.swing.JTextField txtCliCadVen;
    private javax.swing.JTextField txtCliGerEnt;
    private javax.swing.JTextField txtCliOs;
    private javax.swing.JTextField txtCodCadEnt;
    private javax.swing.JTextField txtCor1GerEst;
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
    private javax.swing.JTextField txtDet1GerEst;
    private javax.swing.JTextField txtDetCadEnt;
    private javax.swing.JTextField txtDetCadEst;
    private javax.swing.JTextField txtDetGerEnt;
    private javax.swing.JTextField txtDetGerEst;
    private javax.swing.JTextField txtEndOs;
    private javax.swing.JTextField txtEquOs;
    private javax.swing.JTextField txtForCadEnt;
    private javax.swing.JTextField txtForGerEnt;
    private javax.swing.JTextField txtLoc1GerEst;
    private javax.swing.JTextField txtLocCadEst;
    private javax.swing.JTextField txtLocGerEst;
    private javax.swing.JTextField txtMar1GerEst;
    private javax.swing.JTextField txtMarCadEst;
    private javax.swing.JTextField txtMarGerEst;
    private javax.swing.JTextField txtMarOs;
    private javax.swing.JTextField txtMat1GerEst;
    private javax.swing.JTextField txtMatCadEst;
    private javax.swing.JTextField txtMatGerEst;
    private javax.swing.JTextField txtMod1GerEst;
    private javax.swing.JTextField txtModCadEst;
    private javax.swing.JTextField txtModGerEst;
    private javax.swing.JTextField txtModOs;
    private javax.swing.JTextField txtNomMas;
    private javax.swing.JTextField txtNumAceMas;
    private javax.swing.JTextField txtNumConMas;
    private javax.swing.JTextField txtNumPorMas;
    private javax.swing.JTextField txtPlaCadVen;
    private javax.swing.JTextField txtPlaMas;
    private javax.swing.JTextField txtPre1GerEst;
    private javax.swing.JTextField txtPreCadEnt;
    private javax.swing.JTextField txtPreCadEst;
    private javax.swing.JTextField txtPreDes;
    private javax.swing.JTextField txtPreGerDes;
    private javax.swing.JTextField txtPreGerEnt;
    private javax.swing.JTextField txtPreGerEst;
    private javax.swing.JTextField txtPreOs;
    private javax.swing.JTextField txtQua1GerEst;
    private javax.swing.JTextField txtQuaCadEst;
    private javax.swing.JTextField txtQuaGerEst;
    private javax.swing.JTextField txtRepOs;
    private javax.swing.JTextField txtTelCadVen;
    private javax.swing.JTextField txtTelOs;
    private javax.swing.JTextField txtTipCadEst;
    private javax.swing.JTextField txtTipConEst;
    private javax.swing.JTextField txtTipGerEst;
    private javax.swing.JTextField txtValJur;
    private javax.swing.JTextField txtVarCorCadEst;
    private javax.swing.JTextField txtVenCadVen;
    private javax.swing.JTextField txtVenMas;
    // End of variables declaration//GEN-END:variables
}
