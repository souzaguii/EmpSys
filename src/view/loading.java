package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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

        txtLoaPan = new javax.swing.JTextPane();
        imgLogo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EmpSys");
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImage(new ImageIcon(getClass().getResource("/images/ICON.png")).getImage());
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtLoaPan.setEditable(false);
        txtLoaPan.setFont(fontbold(14));
        txtLoaPan.setForeground(corforeazul);
        txtLoaPan.setFocusable(false);
        txtLoaPan.setOpaque(false);
        getContentPane().add(txtLoaPan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, 260, 30));
        StyledDocument documentStyle = txtLoaPan.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        imgLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imgLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loading.png"))); // NOI18N
        getContentPane().add(imgLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 360));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imgLogo;
    public javax.swing.JTextPane txtLoaPan;
    // End of variables declaration//GEN-END:variables
}
