package pl.mario.mautorun;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mateusz
 */
public class Finder extends javax.swing.JFrame {

    /**
     * Creates new form Finder
     */
    public Finder() {
        initComponents();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }
    public Finder(String value, int cases) {
        initComponents();
        if(cases == 0)
            nick.setText(value);
        else
            ip.setText(value);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
                
            }
        });
        findActionPerformed(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nick = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listRecord = new javax.swing.JTextArea();
        find = new javax.swing.JButton();
        ip = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Find player");

        nick.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nickMousePressed(evt);
            }
        });
        nick.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nickKeyPressed(evt);
            }
        });

        jLabel1.setText("Nick name");

        listRecord.setColumns(20);
        listRecord.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        listRecord.setRows(5);
        jScrollPane1.setViewportView(listRecord);

        find.setText("Find");
        find.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findActionPerformed(evt);
            }
        });

        ip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ipMousePressed(evt);
            }
        });
        ip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ipKeyPressed(evt);
            }
        });

        jLabel2.setText("or");

        jLabel3.setText("IP adress");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE))
                            .addComponent(nick))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(ip, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(find)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(find)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void findActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findActionPerformed
        try {
            listRecord.setText(null);
            FinderPlayer f = null;
            String value = null;
            if (nick.getText().isEmpty()) {
                value = ip.getText().trim();
                if(value.contains("Jones"))
                    return;  
                f = new FinderPlayer(ip.getText(), 2);
            } else if (ip.getText().isEmpty()) {
                value = nick.getText().trim();
                if(value.contains("Jones"))
                    return;
                f = new FinderPlayer(nick.getText(), 1);
            }
            f.start();
            f.join();
            
            for (String r : f.found) {
                listRecord.append(r+"\n");
            }
        } catch (InterruptedException ex) {
            Loggs.loguj("Finder-findActionPerformed", ex);
        }
        
    }//GEN-LAST:event_findActionPerformed

    private void nickKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nickKeyPressed
        if (evt.getKeyCode() == 10) {
            findActionPerformed(null);
        }
    }//GEN-LAST:event_nickKeyPressed

    private void ipKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ipKeyPressed
        if (evt.getKeyCode() == 10) {
            findActionPerformed(null);
        }
    }//GEN-LAST:event_ipKeyPressed

    private void nickMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nickMousePressed
        ip.setText(null);
    }//GEN-LAST:event_nickMousePressed

    private void ipMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ipMousePressed
        nick.setText(null);
    }//GEN-LAST:event_ipMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton find;
    private javax.swing.JTextField ip;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea listRecord;
    private javax.swing.JTextField nick;
    // End of variables declaration//GEN-END:variables
}
