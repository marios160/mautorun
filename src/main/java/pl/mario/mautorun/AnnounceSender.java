package pl.mario.mautorun;

import java.util.logging.Level;
import java.util.logging.Logger;
import static pl.mario.mautorun.Main.conf;

public class AnnounceSender extends javax.swing.JFrame {

    /**
     * Creates new form AnnounceSender
     */
    public AnnounceSender() {
        initComponents();
        a11.setDocument(new JTextFieldLimit(39));
        a12.setDocument(new JTextFieldLimit(39));
        a13.setDocument(new JTextFieldLimit(39));
        a14.setDocument(new JTextFieldLimit(39));
        a15.setDocument(new JTextFieldLimit(39));
        a21.setDocument(new JTextFieldLimit(39));
        a22.setDocument(new JTextFieldLimit(39));
        a23.setDocument(new JTextFieldLimit(39));
        a24.setDocument(new JTextFieldLimit(39));
        a25.setDocument(new JTextFieldLimit(39));
        AnnounceSenderConf a1 = conf.getAnnounceSenderConf1();
        AnnounceSenderConf a2 = conf.getAnnounceSenderConf2();
        if (a1 != null) {
            a11.setText(a1.a1);
            a12.setText(a1.a2);
            a13.setText(a1.a3);
            a14.setText(a1.a4);
            a15.setText(a1.a5);
            aMin1.setValue((a1.aMin/1000)/60);
            aSender1.setSelected(a1.aSender);
        }
        if (a2 != null) {
            a21.setText(a2.a1);
            a22.setText(a2.a2);
            a23.setText(a2.a3);
            a24.setText(a2.a4);
            a25.setText(a2.a5);
            aMin2.setValue((a2.aMin/1000)/60);
            aSender2.setSelected(a2.aSender);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aSender1 = new javax.swing.JCheckBox();
        a11 = new javax.swing.JTextField();
        a12 = new javax.swing.JTextField();
        a13 = new javax.swing.JTextField();
        a14 = new javax.swing.JTextField();
        a15 = new javax.swing.JTextField();
        aMin1 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        a21 = new javax.swing.JTextField();
        a22 = new javax.swing.JTextField();
        a23 = new javax.swing.JTextField();
        a24 = new javax.swing.JTextField();
        a25 = new javax.swing.JTextField();
        aMin2 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        aSender2 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Announce Sender");

        aSender1.setText("send every");

        aMin1.setValue(1);

        jLabel2.setText("min");

        aMin2.setValue(1);

        jLabel4.setText("min");

        aSender2.setText("send every");

        jButton1.setText("Save and start/stop");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(aSender1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aMin1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(a15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(a14)
                    .addComponent(a12)
                    .addComponent(a11)
                    .addComponent(a13)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(aSender2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aMin2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addComponent(a21)
                    .addComponent(a22)
                    .addComponent(a23)
                    .addComponent(a24)
                    .addComponent(a25))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aSender1)
                    .addComponent(aMin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aSender2)
                    .addComponent(aMin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            AnnounceSenderConf a1 = new AnnounceSenderConf(a11.getText(), a12.getText(), a13.getText(), a14.getText(),
                    a15.getText(), Integer.parseInt(aMin1.getValue().toString()), aSender1.isSelected());
            if (conf.getAnnounceSenderConf1() != null) {
                if (!Main.conf.getAnnounceSenderConf1().isInterrupted()) {
                    Main.conf.getAnnounceSenderConf1().interrupt();
                }
            }
            Main.conf.setAnnounceSenderConf1(a1);
            
            AnnounceSenderConf a2 = new AnnounceSenderConf(a21.getText(), a22.getText(), a23.getText(), a24.getText(),
                    a25.getText(), Integer.parseInt(aMin2.getValue().toString()), aSender2.isSelected());
            if (conf.getAnnounceSenderConf2() != null) {
                if (!Main.conf.getAnnounceSenderConf2().isInterrupted()) {
                    Main.conf.getAnnounceSenderConf2().interrupt();
                }
            }
            Main.conf.setAnnounceSenderConf2(a2);
            Main.conf.setClassFile(Main.conf);
            conf.getAnnounceSenderConf1().start();
            Thread.sleep(1000);
            conf.getAnnounceSenderConf2().start();
            dispose();
        } catch (InterruptedException ex) {
            Loggs.loguj("AnnounceSender-announceSenderButton", ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField a11;
    private javax.swing.JTextField a12;
    private javax.swing.JTextField a13;
    private javax.swing.JTextField a14;
    private javax.swing.JTextField a15;
    private javax.swing.JTextField a21;
    private javax.swing.JTextField a22;
    private javax.swing.JTextField a23;
    private javax.swing.JTextField a24;
    private javax.swing.JTextField a25;
    private javax.swing.JSpinner aMin1;
    private javax.swing.JSpinner aMin2;
    private javax.swing.JCheckBox aSender1;
    private javax.swing.JCheckBox aSender2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
