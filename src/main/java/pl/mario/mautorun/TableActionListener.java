package pl.mario.mautorun;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import static pl.mario.mautorun.Main.*;

public class TableActionListener implements ActionListener {

    static JMenuItem menuKick;
    static JMenuItem menuBan;
    static JMenuItem menuBanDef;
    static JMenuItem menuBanMax;
    static JMenuItem menuIP;
    static JMenuItem menuNick;
    static JMenuItem menuAdmin;
    static JMenuItem menuJAdmin;
    static JMenuItem menuCheckIP;
    static JMenuItem menuCheckNick;
    String id;
    String ip;
    String nick;
    Player player;
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
        id = (String) table.getValueAt(row, 0);
        ip = (String) table.getValueAt(row, 3);
        player = Main.srv.getPlayer(id);
        nick = player.getNick();
        menuKick = new JMenuItem("Kick [" + id + "] " + nick);
        menuBan = new JMenuItem("Ban " + ip);
        menuBanDef = new JMenuItem("Ban " + ip + "/" + (Main.conf.getDefMask() + 1));
        menuBanMax = new JMenuItem("Ban " + ip + "/" + (Main.conf.getMaxMask() + 1));
        menuIP = new JMenuItem("Copy IP: " + ip);
        menuNick = new JMenuItem("Copy Nick: " + nick);
        if (player.getAccess() <= 0) {
            menuAdmin = new JMenuItem("Add Admin: [" + id + "] " + nick);
            menuJAdmin = new JMenuItem("Add Junior Admin: [" + id + "] " + nick);
        } else if (player.getAccess() == 1) {
            menuAdmin = new JMenuItem("Add Admin: [" + id + "] " + nick);
            menuJAdmin = new JMenuItem("Remove Junior Admin: [" + id + "] " + nick);
        } else {
            menuAdmin = new JMenuItem("Remove Admin: [" + id + "] " + nick);
            menuJAdmin = new JMenuItem("Add Junior Admin: [" + id + "] " + nick);
        }
        menuCheckIP = new JMenuItem("Check IP: " + ip);
        menuCheckNick = new JMenuItem("Check Nick: " + nick);

        menuKick.addActionListener(this);
        menuBan.addActionListener(this);
        menuBanDef.addActionListener(this);
        menuBanMax.addActionListener(this);
        menuIP.addActionListener(this);
        menuNick.addActionListener(this);
        menuAdmin.addActionListener(this);
        menuJAdmin.addActionListener(this);
        menuCheckIP.addActionListener(this);
        menuCheckNick.addActionListener(this);

        popupMenu.add(menuKick);
        popupMenu.add(menuBan);
        popupMenu.add(menuBanDef);
        popupMenu.add(menuBanMax);
        popupMenu.add(menuIP);
        popupMenu.add(menuNick);
        popupMenu.add(menuAdmin);
        popupMenu.add(menuJAdmin);
        popupMenu.add(menuCheckIP);
        popupMenu.add(menuCheckNick);

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
            Main.srv.banPlayer(id, "", 2);
        } else if (menu == menuBanDef) {
            Main.srv.banPlayer(id, "/" + (Main.conf.getDefMask() + 1), 2);
        } else if (menu == menuBanMax) {
            Main.srv.banPlayer(id, "/" + (Main.conf.getMaxMask() + 1), 2);
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
        } else if (menu == menuAdmin) {
            if (player.getAccess() <= 1) {
                player.setAccess(2);
                gui.dodajLog("Added Admin: " + "[" + id + "] " + nick + " (" + srv.getPlayer(id).getIp() + ") (REMOTELY)", gui.green);
                if (conf.isDispAddAdmin()) {
                    Cmd.message("Added Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
                }
            } else {
                player.setAccess(0);
                gui.dodajLog("Removed Admin: " + "[" + id + "] " + nick + " (" + srv.getPlayer(id).getIp() + ") (REMOTELY)", gui.green);
                if (conf.isDispAddAdmin()) {
                    Cmd.message("Removed Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
                }
            }
        } else if (menu == menuJAdmin) {
            if (player.getAccess() <= 0 || player.getAccess() == 2) {
                player.setAccess(1);
                gui.dodajLog("Added Junior Admin: " + "[" + id + "] " + nick + " (" + srv.getPlayer(id).getIp() + ") (REMOTELY)", gui.green);
                if (conf.isDispAddAdmin()) {
                    Cmd.message("Added Junior Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
                }
            } else {
                player.setAccess(0);
                gui.dodajLog("Removed Junior Admin: " + "[" + id + "] " + nick + " (" + srv.getPlayer(id).getIp() + ") (REMOTELY)", gui.green);
                if (conf.isDispAddAdmin()) {
                    Cmd.message("Removed Junior Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
                }
            }
        } else if (menu == menuCheckIP) {
            Finder f = new Finder(ip, 1);
        } else if (menu == menuCheckNick) {
            Finder f = new Finder(nick, 0);

        }
    }

}
