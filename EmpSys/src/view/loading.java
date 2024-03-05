package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public final class loading extends javax.swing.JFrame {

    public loading() {

        initComponents();
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

    }


    public Color corforeazul = new java.awt.Color(10, 60, 133);

    public Font fontbold(int size) {

        try {
            Font fonte = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("fonts/Poppins-SemiBold.ttf"));
            return fonte.deriveFont((float) size);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLoa = new javax.swing.JLabel();
        imgLogo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EmpSys");
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImage(new ImageIcon(getClass().getResource("/images/ICON.png")).getImage());
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLoa.setFont(fontbold(14));
        lblLoa.setForeground(corforeazul);
        lblLoa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoa.setText("Iniciando sistema...");
        getContentPane().add(lblLoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 278, 370, 20));

        imgLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loading.png"))); // NOI18N
        getContentPane().add(imgLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 360));

        pack();
    }// </editor-fold>//GEN-END:initComponents
//     public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(() -> {
//            new loading().setVisible(true);
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imgLogo;
    public javax.swing.JLabel lblLoa;
    // End of variables declaration//GEN-END:variables
}
