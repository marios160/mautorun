package pl.mario.mautorun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import static pl.mario.mautorun.Main.*;

/**
 *
 * @author Mario PL
 */
public class Cmd extends Thread {

    String linia;
    int pid;
    String pnick;
    String pip;
    Player player;

    public Cmd(int id, String linia) {
        this.linia = linia;
        this.pid = id;
        this.player = srv.getPlayer(id);
        this.pnick = player.getNick();
        this.pip = player.getIp();
    }

    public void run() {

        try {
            if (conf.isAdminPanel() && this.player.getAccess() > 0) {
                if (linia.indexOf(": /ban ") > -1) {
                    ban();
                } else if (linia.indexOf(": /tban ") > -1) {
                    tban();
                } else if (linia.indexOf(": /leftlist") > -1) {
                    leftlist();
                } else if (linia.indexOf(": /leftban ") > -1) {
                    leftban();
                } else if (linia.indexOf(": /banlist") > -1) {
                    banlist();
                } else if (linia.indexOf(": /unban ") > -1) {
                    unban();
                } else if (linia.indexOf(": /kick ") > -1) {
                    kick();
                } else if (linia.indexOf(": /kickall") > -1) {
                    kickall(1);
                } else if (linia.indexOf(": /restart") > -1) {
                    restart(1);
                } else if (linia.indexOf(": /aadmin ") > -1) {
                    aadmin();
                } else if (linia.indexOf(": /ajadmin") > -1) {
                    ajadmin();
                } else if (linia.indexOf(": /whois") > -1) {
                    whois();
                } else if (linia.indexOf(": /time") > -1) {
                    time();
                } else if (linia.indexOf(": /map ") > -1) {
                    map();
                } else if (linia.indexOf(": /help") > -1) {
                    if (linia.length() < linia.indexOf(": /help") + 8) {
                        help();
                    } else if (linia.indexOf(": /help ban") > -1) {
                        announce("/ban <ID>[/mask] t[tm] - ban ID");
                        announce("mask is range of ban e.g /ban 1/24");
                        announce("tm is time(min) ban e.q /ban 1/24 t30");
                        announce("mask and tm is not necessary");
                        announce("e.g /ban 1 or /ban 1/24 or /ban 1 t30");
                    } else if (linia.indexOf(": /help tban") > -1) {
                        announce("/tban <ID>[/mask] <tm> - time ban ID");
                        announce("mask is range of ban, tm is time ban");
                        announce("default tm value is minutes");
                        announce("tm can be: h - hours, d - days");
                        announce("w - weeks, m - months, y - years");
                        Thread.sleep(5000);
                        announce("e.g /tban 1/24 2h - ban for 2 hours");
                        announce("/tban 1 30 - ban for 30 minutes");
                        announce("/tban 1 3m - ban for 3 months");
                        announce("mask is not necessary");
                    } else if (linia.indexOf(": /help leftban") > -1) {
                        announce("/leftban <ID>[/mask] t[tm] - ban ID");
                        announce("from leftlist");
                        announce("mask is range of ban e.g /ban 1/24");
                        announce("tm is time(min) ban e.q /ban 1/24 t30");
                        announce("mask and tm is not necessary");
                        announce("e.g /ban 1 or /ban 1/24 or /ban 1 t30");
                    } else if (linia.indexOf(": /help leftlist") > -1) {
                        announce("/leftlist - show list players who ");
                        announce("left from server");
                        announce("use /leftban to ban player from list");
                    } else if (linia.indexOf(": /help banlist") > -1) {
                        announce("/banlist - show 3 last banned players");
                        announce("use /unban to unban player from list");
                    } else if (linia.indexOf(": /help unban") > -1) {
                        announce("/unban <ID> - unban player from banlist");
                        announce("e.g /unban 1");
                    } else if (linia.indexOf(": /help kick") > -1) {
                        announce("/kick <ID> - kick player with ID");
                        announce("e.g /kick 1");
                    } else if (linia.indexOf(": /help kickall") > -1) {
                        announce("/kickall - kick all players on server");
                    } else if (linia.indexOf(": /help restart") > -1) {
                        announce("/restart - restartmap");
                    } else if (linia.indexOf(": /help aadmin") > -1) {
                        announce("/aadmin <ID> - add admin");
                        announce("e.g aadmin 1");
                    } else if (linia.indexOf(": /help ajadmin") > -1) {
                        announce("/ajadmin <ID> - add junior admin");
                        announce("e.g ajadmin 1");
                    } else if (linia.indexOf(": /help whois") > -1) {
                        announce("/whois <ID> - show nicks linked with ID");
                        announce("e.g /whois 1");
                    } else if (linia.indexOf(": /help time") > -1) {
                        announce("/time - show current time");
                    } else if (linia.indexOf(": /help map") > -1) {
                        announce("/map <ID> - change map");
                        announce("to show ID maps enter to console: ");
                        announce("sv listmaps");
                    }
                }
            } else {
                File words = new File(Main.path + "words.txt");
                if (!words.exists()) {
                    JOptionPane.showMessageDialog(gui, "File words.txt not found!");
                    return;
                }
                Scanner censors = new Scanner(words);
                while (censors.hasNextLine()) {
                    if (linia.toLowerCase().indexOf(censors.nextLine()) > -1) {
                        if (!Main.conf.isCensors() || srv.getPlayer(pid).getAccess() > 1) {
                            return;
                        }
                        if (srv.getPlayer(pid).addWarrnings()) {
                            Cmd.message("[" + pid + "] was kicked for warnings");
                            gui.dodajLog("[" + pid + "] " + srv.getPlayer(pid).getNick() + " was kicked for warnings", gui.red);
                            srv.sendPck("/sv " + ServerCommands.kick + " " + pid);
                            srv.sendPck("/sv " + ServerCommands.kick + " " + pid);
                        } else {
                            Cmd.message("Warning to [" + pid + "] for vulgarism");
                            gui.dodajLog("Warning to [" + pid + "] " + srv.getPlayer(pid).getNick() + " for vulgarism", gui.org);
                        }
                        break;
                    }
                }
                censors.close();
            }
        } catch (InterruptedException ex) {
            Loggs.loguj("Cmd-Run", ex);
        } catch (FileNotFoundException ex) {
            Loggs.loguj("Cmd-Run", ex);
        }

    }

    void ban() {
        String id = null, mask, ip, ln, czas = " for ";
        int dl;
        Player p;

        // /ban 3/24 t3
        int maxMask = Integer.parseInt(gui.getMaxMask().getSelectedItem().toString().substring(1));
        int defMask = Integer.parseInt(gui.getDefMask().getSelectedItem().toString().substring(1));
        mask = Integer.toString(defMask);

        if (!admin(1)) {
            return;
        }
        ln = linia.substring(linia.indexOf(": /ban ") + 7);
        dl = ln.length();

        if (dl < 3) {
            id = ln;
            czas = "";
        } else if (dl < 6) {
            if (ln.indexOf("/") > -1) {
                id = ln.substring(0, ln.indexOf("/"));
                mask = ln.substring(ln.indexOf("/"));
                czas = "";
            } else if (ln.indexOf(" t") > -1) {
                id = ln.substring(0, ln.indexOf(" t"));
                czas += ln.substring(ln.indexOf("t") + 1) + " min";
            }

        } else if (ln.indexOf("/") > -1) {
            id = ln.substring(0, ln.indexOf("/"));
            mask = ln.substring(ln.indexOf("/"), ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        } else {
            id = ln.substring(0, ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        }
        if (Integer.parseInt(id) < 1 || Integer.parseInt(id) > 34) {
            announce("ID " + id + " not exist!");
            return;
        }
        System.out.println(mask);
        if (Integer.parseInt(mask) < maxMask || Integer.parseInt(mask) > 30) {
            announce("Mask must be between " + maxMask + " and 30");
            return;
        }

        if (srv.getPlayer(id) != null) {
            ip = srv.getPlayer(id).getIp();
        } else {
            announce("Player " + id + " not found");
            return;
        }
        p = srv.getPlayer(id);
        String cmd = "";

        try {
            if (conf.getSystem().equals("lin")) {
                cmd = "/sbin/iptables -I INPUT -s " + ip +"/"+ mask + " -j DROP";
            } else if (conf.getSystem().equals("win")) {
                cmd = "netsh advfirewall firewall add rule name=\"CRASHER\" dir=in protocol=udp interface=any action=block remoteip=" + ip +"/"+ mask;
            }
            announce(ip +"/"+ mask + " was banned" + czas);
            Runtime.getRuntime().exec(cmd);
            gui.dodajLog(ip +"/"+ mask + " was banned" + czas + " by " + pnick, gui.blue);

            PrintWriter bany = new PrintWriter(new FileWriter("banlist.txt", true));
            bany.println(cmd);
            bany.close();
            p.setIp(p.getIp() + mask);
            srv.addBanPlayers(p);
        } catch (IOException ex) {
            Loggs.loguj("Cmd-ban", ex);
        }
    }

    void tban() {
        if (!admin(1)) {
            return;
        }
    }

    void leftlist() {
        if (!admin(1)) {
            return;
        }
        for (int i = 0; i < 5; i++) {
            if (srv.getOldPlayer(i).getIp() != "") {
                announce(i + " " + srv.getOldPlayer(i).getIp() + " " + srv.getOldPlayer(i).getNick());
            }
        }
    }

    void leftban() {
        String id = null, mask, ip, ln, czas = " for ";
        int dl;
        Player p;

        // /ban 3/24 t3
        int maxMask = Integer.parseInt(gui.getMaxMask().getSelectedItem().toString().substring(1));
        mask = gui.getDefMask().getSelectedItem().toString();

        if (!admin(1)) {
            return;
        }
        ln = linia.substring(linia.indexOf(": /leftban ") + 11);
        dl = ln.length();

        if (dl < 3) {
            id = ln;
            czas = "";
        } else if (dl < 6) {
            if (ln.indexOf("/") > -1) {
                id = ln.substring(0, ln.indexOf("/"));
                mask = ln.substring(ln.indexOf("/"));
                czas = "";
            } else if (ln.indexOf(" t") > -1) {
                id = ln.substring(0, ln.indexOf(" t"));
                czas += ln.substring(ln.indexOf("t") + 1) + " min";
            }

        } else if (ln.indexOf("/") > -1) {
            id = ln.substring(0, ln.indexOf("/"));
            mask = ln.substring(ln.indexOf("/"), ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        } else {
            id = ln.substring(0, ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        }
        if (Integer.parseInt(id) < 1 || Integer.parseInt(id) > 34) {
            announce("ID " + id + " not exist!");
            return;
        }
        if (Integer.parseInt(mask.substring(1)) < maxMask || Integer.parseInt(mask.substring(1)) > 30) {
            announce("Mask must be between " + maxMask + " and 30");
            return;
        }

        if (srv.getOldPlayer(Integer.parseInt(id)) != null) {
            ip = srv.getOldPlayer(Integer.parseInt(id)).getIp();
        } else {
            announce("Player " + id + " not found");
            return;
        }
        String nick = srv.getPlayer(this.pid).getNick();
        p = srv.getOldPlayer(Integer.parseInt(id));
        String cmd = "";

        try {
            if (conf.getSystem().equals("lin")) {
                cmd = "/sbin/iptables -I INPUT -s " + ip + mask + " -j DROP";
            } else if (conf.getSystem().equals("win")) {
                cmd = "netsh advfirewall firewall add rule name=\"CRASHER\" dir=in protocol=udp interface=any action=block remoteip=" + ip + mask;
            }
            announce(ip + mask + " was banned" + czas);
            Runtime.getRuntime().exec(cmd);
            gui.dodajLog(ip + mask + " was banned" + czas + " by " + pnick, gui.blue);

            PrintWriter bany = new PrintWriter(new FileWriter("banlist.txt", true));
            bany.println(cmd);
            bany.close();
            p.setIp(p.getIp() + mask);
            srv.addBanPlayers(p);
        } catch (IOException ex) {
            Loggs.loguj("Cmd-ban", ex);
        }
    }

    void banlist() {
        if (!admin(2)) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (srv.getBanPlayer(i).getIp() != "") {
                announce(i + " " + srv.getBanPlayer(i).getIp() + " " + srv.getBanPlayer(i).getNick());
            }

        }
    }

    void unban() {
        //netsh advfirewall firewall delete rule name dir=in remoteip=1.1.1.2/32
        String ln;
        if (!admin(2)) {
            return;
        }

        String id = linia.substring(linia.indexOf("/unban") + 7);
        if (Integer.parseInt(id) < 0 || Integer.parseInt(id) > 2) {
            announce("ID " + id + " not exist!");
            return;
        }
        String ip = srv.getBanPlayer(Integer.parseInt(id)).getIp();
        String nick = srv.getPlayer(this.pid).getNick();
        try {
            if (conf.getSystem().equals("lin")) {
                String cmd = "/sbin/iptables -D INPUT -s " + ip + " -j DROP";
                gui.dodajLog("Unbanned " + ip + " by " + nick + "\n", gui.blue);
                announce("Unbanned " + ip);
                Runtime.getRuntime().exec(cmd);
            } else if (conf.getSystem().equals("win")) {
                gui.getDlog().insertString(gui.getDlog().getLength(), conf.getTime() + "Remove ONLY from banlist " + ip + " (" + nick + ")\n", gui.green);
                announce("Remove from banlist " + ip);
            }
            Scanner in = new Scanner(Paths.get("banlist.txt"));
            PrintWriter bany = new PrintWriter(new FileWriter("tmp.txt", true));
            while (in.hasNextLine()) {
                ln = in.nextLine();
                if (ln.indexOf(ip) < 0) {
                    bany.append(ln);
                }
            }
            in.close();
            bany.close();
            File file = new File("banlist.txt");
            file.delete();
            File file2 = new File("tmp.txt");
            file2.renameTo(file);
        } catch (IOException ex) {
            Loggs.loguj("Cmd-unban", ex);
        } catch (BadLocationException ex) {
            Loggs.loguj("Cmd-unban", ex);
        }
    }

    void kick() {
        if (!admin(1)) {
            return;
        }
        String ids = linia.substring(linia.indexOf("/kick") + 6);
        if (srv.getPlayer(ids) == null) {
            announce("Player " + ids + " not exist");
            return;
        }
        gui.dodajLog("[" + ids + "] " + srv.getPlayer(ids).getNick() + " was kicked by " + srv.getPlayer(pid).getNick(), gui.mag);
        srv.sendPck("/sv " + ServerCommands.kick + " " + ids);
        srv.sendPck("/sv " + ServerCommands.kick + " " + ids);
        announce("[" + ids + "]" + " was kicked");
    }

    void kickall(int a) {
        if (!admin(2)) {
            return;
        }

        for (Player zm : srv.getPlayers()) {
            if (zm != null) {
                srv.sendPck("/sv " + ServerCommands.kick + " " + zm.getId());
                srv.sendPck("/sv " + ServerCommands.kick + " " + zm.getId());
            }
        }
        gui.dodajLog("All was kicked by " + srv.getPlayer(pid).getNick(), gui.mag);
        announce("Kick all");

    }

    static void kickall() {

        message("/lo announce(\"Kick all\")");
        for (Player zm : srv.getPlayers()) {
            if (zm != null) {
                srv.sendPck("/sv " + ServerCommands.kick + " " + zm.getId());
                srv.sendPck("/sv " + ServerCommands.kick + " " + zm.getId());
            }
        }

    }

    void restart(int b) {
        if (!admin(1)) {
            return;
        }
        srv.sendPck("/sv " + ServerCommands.restartmap);
    }

    static void restart() {
        srv.sendPck("/sv " + ServerCommands.restartmap);
    }

    void aadmin() {
        try {
            if (!admin(2)) {
                return;
            }
            String id = linia.substring(linia.indexOf("/aadmin") + 8);
            if (Integer.parseInt(id) < 1 || Integer.parseInt(id) > 34) {
                announce("ID " + id + " not exist!");
                return;
            }
            if (srv.getPlayer(Integer.parseInt(id)) == null) {
                announce("Player " + id + " not found");
                return;
            }
            srv.getPlayer(Integer.parseInt(id)).setAccess(2);
            gui.getDlog().insertString(gui.getDlog().getLength(), conf.getTime() + "Added Admin " + srv.getPlayer(Integer.parseInt(id)).getNick() + " by " + pnick + "\n", gui.mag);
            if (conf.isDispAddAdmin()) {
                announce("Added Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
            }
        } catch (BadLocationException ex) {
            Loggs.loguj("Cmd-aadmin", ex);
        }

    }

    void ajadmin() {
        try {
            if (!admin(2)) {
                return;
            }
            String id = linia.substring(linia.indexOf("/ajadmin") + 9);
            if (Integer.parseInt(id) < 1 || Integer.parseInt(id) > 34) {
                announce("ID " + id + " not exist!");
                return;
            }
            if (srv.getPlayer(Integer.parseInt(id)) == null) {
                announce("Player " + id + " not found");
                return;
            }
            srv.getPlayer(Integer.parseInt(id)).setAccess(1);
            gui.getDlog().insertString(gui.getDlog().getLength(), conf.getTime() + "Added Junior Admin " + srv.getPlayer(Integer.parseInt(id)).getNick() + " by " + pnick + "\n", gui.mag);
            if (conf.isDispAddAdmin()) {
                announce("Added Junior Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
            }
        } catch (BadLocationException ex) {
            Loggs.loguj("Cmd-ajadmin", ex);
        }
    }

    void whois() {
        if (!admin(1)) {
            return;
        }
    }

    void time() {
        if (!admin(1)) {
            return;
        }
        SimpleDateFormat czas = new SimpleDateFormat("kk:mm:ss");
        announce(czas.format(new Date()));
    }

    void map() {
        try {
            if (!admin(2)) {
                return;
            }
            String id = linia.substring(linia.indexOf("/map") + 5);
            gui.getDlog().insertString(gui.getDlog().getLength(), conf.getTime() + "Changed map to id " + id + " by " + pnick + "\n", gui.mag);
            
            srv.sendPck("/sv gotomap " + id);
        } catch (BadLocationException ex) {
            Loggs.loguj("Cmd-map", ex);
        }

    }

    void help() {
        if (!admin(1)) {
            return;
        }
        try {
            announce("/ban <ID>[/mask] t[tm] - ban ID");
            announce("/tban <ID>[/mask] [tm] - time ban ID");
            announce("/leftban <ID>[/mask] t[tm] ->");
            announce("-> ban ID from leftlist");
            announce("/leftlist - list of players who left");
            Thread.sleep(5000);
            announce("/banlist - list of last bans");
            announce("/unban <ID> - unban ID from last bans");
            announce("/kick <ID> - kick ID");
            announce("/kickall - Kick all players");
            announce("/restart - restart map");
            Thread.sleep(5000);
            announce("/aadmin <ID> - add admin");
            announce("/ajadmin <ID> - add junior admin");
            announce("/whois <ID> - show ip & nicks of ID ");
            announce("/time - show current time");
            Thread.sleep(5000);
            announce("/map <ID> - change map");
            announce("to show id map - sv listmaps on console");
            announce("/help - show help");
        } catch (InterruptedException ex) {
            Loggs.loguj("Cmd-help", ex);
        }
    }

    void announce(String msg) {
        srv.sendPck("/lo announce (\"" + msg + "\")");
    }

    static void message(String msg) {
        srv.sendPck("/lo announce (\"" + msg + "\")");
    }

    boolean admin(int admin) {
        if (srv.getPlayer(pid).getAccess() < admin) {
            return false;
        } else {
            return true;
        }
    }

}
