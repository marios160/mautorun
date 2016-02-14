package pl.mario.mautorun;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class TableActionListener implements ActionListener {

    static JMenuItem menuKick;
    static JMenuItem menuBan;
    static JMenuItem menuBanDef;
    static JMenuItem menuBanMax;
    static JMenuItem menuIP;
    static JMenuItem menuNick;
    String id;
    String ip;
    String nick;
    JPopupMenu popupMenu;

    public TableActionListener(int row, JTable table) {
        popupMenu = createPopupMenu(row, table);
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public JPopupMenu createPopupMenu(int row, JTable table) {

        JPopupMenu popupMenu = new JPopupMenu();
        if (table.getValueAt(row, 0) == null) {
            return null;
        }
        menuKick = new JMenuItem("Kick " + table.getValueAt(row, 1));
        menuBan = new JMenuItem("Ban " + table.getValueAt(row, 3));
        menuBanDef = new JMenuItem("Ban " + table.getValueAt(row, 3) + "/" + (Main.conf.getDefMask()+1));
        menuBanMax = new JMenuItem("Ban " + table.getValueAt(row, 3) + "/" + (Main.conf.getMaxMask()+1));
        menuIP = new JMenuItem("Copy IP: " + table.getValueAt(row, 3));
        menuNick = new JMenuItem("Copy Nick: " + table.getValueAt(row, 1));
        id = (String) table.getValueAt(row, 0);
        ip = (String) table.getValueAt(row, 3);
        nick = (String) table.getValueAt(row, 1);
        menuKick.addActionListener(this);
        menuBan.addActionListener(this);
        menuBanDef.addActionListener(this);
        menuBanMax.addActionListener(this);
        menuIP.addActionListener(this);
        menuNick.addActionListener(this);

        popupMenu.add(menuKick);
        popupMenu.add(menuBan);
        popupMenu.add(menuBanDef);
        popupMenu.add(menuBanMax);
        popupMenu.add(menuIP);
        popupMenu.add(menuNick);

        return popupMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem menu = (JMenuItem) e.getSource();
        if (id == null) {
            return;
        }
        if (menu == menuKick) {
            Main.srv.kickPlayer(id);
        } else if (menu == menuBan) {
            Main.srv.banPlayer(id, "",2);
        } else if (menu == menuBanDef) {
            Main.srv.banPlayer(id, "/" + (Main.conf.getDefMask()+1),2);
        } else if (menu == menuBanMax) {
            Main.srv.banPlayer(id, "/" + (Main.conf.getMaxMask()+1),2);
        } else if (menu == menuIP) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            StringSelection strSel = new StringSelection(ip);
            clipboard.setContents(strSel, null);
        } else if (menu == menuNick) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            StringSelection strSel = new StringSelection(nick);
            clipboard.setContents(strSel, null);

        }
    }

}
