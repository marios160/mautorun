package pl.mario.mautorun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
        this.linia = linia.toLowerCase();
        this.pid = id;

        this.player = srv.getPlayer(id);
        this.pnick = player.getNick();
        this.pip = player.getIp();
    }
    
    public Cmd( String linia) {
        this.linia = linia.toLowerCase();
        this.pid = 0;

        this.player = new Player("0", "0.0.0.0", "Admin", 26001);
        this.player.setAccess(2);
        this.pnick = "Admin";
        this.pip = "0.0.0.0";
    }
    
    void help() {
        if (!admin(1)) {
            return;
        }
        try {
            
            announce("to see detailed instructions -> ");
            announce("send /help command - e.g /help ban");
            Thread.sleep(2000);
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
            announce("/censor on/off - run censorship");
            announce("/sktk on/off - run sk and tk warnings");
            Thread.sleep(5000);
            announce("/welcome on/off - run welcome players");
            announce("/wmess msg+$var+msg - set welcome msg");
            announce("/warnings <NR> - after NR ->");
            announce("->warnings player will be kick");
            announce("/items on/off - run items control");
            Thread.sleep(5000);
            announce("/adisp on/off - displays \"Added admin\"");
            announce("/maxmask <NR> - max mask in /ban");
            announce("/defmask <NR> - default mask in /ban");
            announce("/announce <MSG> - announce MSG on srv");
            announce("/whoami - show who are you");
            Thread.sleep(5000);
            announce("/crash - close server");
            announce("to see detailed instructions -> ");
            announce("send /help command - e.g /help ban");
       
        } catch (InterruptedException ex) {
            Loggs.loguj("Cmd-help", ex);
        }
    }

    public void run() {

        try {
            if (linia.indexOf(": /apanel") > -1) {
                adminPanel();
            }

            if (conf.isAdminPanel() && admin(1)) {
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
                } else if (linia.indexOf(": /censor") > -1) {
                    censor();
                } else if (linia.indexOf(": /sktk") > -1) {
                    sktk();
                } else if (linia.indexOf(": /welcome") > -1) {
                    welcome();
                } else if (linia.indexOf(": /wmess") > -1) {
                    welcomeMessage();
                } else if (linia.indexOf(": /warnings") > -1) {
                    warnings();
                } else if (linia.indexOf(": /items") > -1) {
                    items();
                } else if (linia.indexOf(": /adisp") > -1) {
                    dispAdmin();
                } else if (linia.indexOf(": /maxmask") > -1) {
                    maxMask();
                } else if (linia.indexOf(": /defmask") > -1) {
                    defMask();
                } else if (linia.indexOf(": /announce ") > -1) {
                    announce();
                } else if (linia.indexOf(": /whoami") > -1) {
                    whoami();
                } else if (linia.indexOf(": /crash") > -1) {
                    crash();
                } else if (linia.indexOf(": /cmds") > -1) {
                    cmds();
                } else if (commands()) {
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
                    } else if (linia.indexOf(": /help censor") > -1) {
                        announce("/censor on/off - Censorship on or off");
                        announce("to check status send /censor");
                        announce("without on or off");
                    } else if (linia.indexOf(": /help sktk") > -1) {
                        announce("/sktk on/off - count warnings for");
                        announce("team kill or spawn kill. Kick is");
                        announce("dependent on warnings");
                    } else if (linia.indexOf(": /help welcome") > -1) {
                        announce("/welcome on/off - run welcome players");
                        announce("to set welcome message send");
                        announce("e.g /wmess Welcome+$nick+on Server");
                    } else if (linia.indexOf(": /help wmess") > -1) {
                        announce("/wmess <msg+$var+msg> - set msg");
                        announce("you can use $nick, $ip, $id, $none");
                        announce("e.g /wmess Welcome+$nick+on Server");
                    } else if (linia.indexOf(": /help warnings") > -1) {
                        announce("/warnings <NR> - after NR warnings");
                        announce("player will be kicked");
                        announce("e.g /warnings 3");
                    } else if (linia.indexOf(": /help items") > -1) {
                        announce("/items on/off - run items control");
                    } else if (linia.indexOf(": /help adisp") > -1) {
                        announce("/adisp on/off - if \"on\", server");
                        announce("display \"Added admin\" when ");
                        announce("Somebody login to admin");
                    } else if (linia.indexOf(": /help maxmask") > -1) {
                        announce("/maxmask <NR> - set max mask when");
                        announce("someone use ban command. Range");
                        announce("of masks - 0 to 32. 16 is the best");
                        announce("max mask. Mask 0 ban all IP");
                    } else if (linia.indexOf(": /help defmask") > -1) {
                        announce("/defmask <NR> - set default mask");
                        announce("when someone use ban command");
                        announce("without mask. Range of masks ");
                        announce("0 to 32. 24 is the best def mask");
                    } else if (linia.indexOf(": /help announce") > -1) {
                        announce("/announce <MSG> - announce MSG");
                        announce("on server. e.g /announce Hello");
                    } else if (linia.indexOf(": /help whoami") > -1) {
                        announce("/whoami - show who are you");
                        announce("You can be Admin or Junior Admin");
                        announce("Normal players not show");
                    } else if (linia.indexOf(": /help crash") > -1) {
                        announce("/crash - close server ");
                        announce("it make crash");
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
                mask = ln.substring(ln.indexOf("/") + 1);
                czas = "";
            } else if (ln.indexOf(" t") > -1) {
                id = ln.substring(0, ln.indexOf(" t"));
                czas += ln.substring(ln.indexOf("t") + 1) + " min";
            }

        } else if (ln.indexOf("/") > -1) {
            id = ln.substring(0, ln.indexOf("/"));
            mask = ln.substring(ln.indexOf("/") + 1, ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        } else {
            id = ln.substring(0, ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        }
        try {

            if (Integer.parseInt(id) < 1 || Integer.parseInt(id) > 34) {
                announce("ID " + id + " not exist!");
                return;
            }
            if (Integer.parseInt(mask) < maxMask || Integer.parseInt(mask) > 30) {
                announce("Mask must be between " + maxMask + " and 30");
                return;
            }
        } catch (Exception ex) {
            announce("Incorrect syntax");
            announce("Use: /ban <ID>[/mask]");
        }

        if (srv.getPlayer(id) != null) {
            ip = srv.getPlayer(id).getIp();
        } else {
            announce("Player " + id + " not found");
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);
            return;
        }
        p = srv.getPlayer(id);
        String cmd = "";

        try {
            if (conf.getSystem().equals("lin")) {
                cmd = "/sbin/iptables -I INPUT -s " + ip + "/" + mask + " -j DROP";
            } else if (conf.getSystem().equals("win")) {
                cmd = "netsh advfirewall firewall add rule name=\"IGIBan\" dir=in protocol=udp interface=any action=block remoteip=" + ip + "/" + mask;
            }
            announce(p.getNick() + " was banned" + czas);
            Runtime.getRuntime().exec(cmd);
            gui.dodajLog(p.getNick() + "(" + ip + "/" + mask + ") was banned" + czas + " by " + pnick, gui.blue);

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
        int i = 0;
        for (Player player : srv.getOldPlayers()) {
            announce(i + " " + srv.getOldPlayer(i).getIp() + " " + srv.getOldPlayer(i).getNick());
            i++;
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
                mask = ln.substring(ln.indexOf("/") + 1);
                czas = "";
            } else if (ln.indexOf(" t") > -1) {
                id = ln.substring(0, ln.indexOf(" t"));
                czas += ln.substring(ln.indexOf("t") + 1) + " min";
            }

        } else if (ln.indexOf("/") > -1) {
            id = ln.substring(0, ln.indexOf("/"));
            mask = ln.substring(ln.indexOf("/") + 1, ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        } else {
            id = ln.substring(0, ln.indexOf(" t"));
            czas += ln.substring(ln.indexOf("t") + 1) + " min";
        }
        if (Integer.parseInt(id) < 0 || Integer.parseInt(id) > 4) {
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
        p = srv.getOldPlayer(Integer.parseInt(id));
        String cmd = "";

        try {
            if (conf.getSystem().equals("lin")) {
                cmd = "/sbin/iptables -I INPUT -s " + ip + mask + " -j DROP";
            } else if (conf.getSystem().equals("win")) {
                cmd = "netsh advfirewall firewall add rule name=\"IGIBan\" dir=in protocol=udp interface=any action=block remoteip=" + ip + mask;
            }
            announce(p.getNick() + " was banned" + czas);
            Runtime.getRuntime().exec(cmd);
            gui.dodajLog(p.getNick() + " (" + ip + mask + ") was banned" + czas + " by " + pnick, gui.blue);

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
        int i = 0;
        for (Player player : srv.getBanPlayers()) {
            announce(i + " " + srv.getBanPlayer(i).getIp() + " " + srv.getBanPlayer(i).getNick());
            i++;
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
        Player p = srv.getBanPlayer(Integer.parseInt(id));
        try {
            String cmd = "";
            if (conf.getSystem().equals("lin")) {
                cmd = "/sbin/iptables -D INPUT -s " + p.getIp() + " -j DROP";
            } else if (conf.getSystem().equals("win")) {
                cmd = "netsh advfirewall firewall delete rule name=all remoteip=" + p.getIp();
            }
            Runtime.getRuntime().exec(cmd);
            announce("Unbanned " + p.getNick() + " (" + p.getIp() + ")");
            gui.dodajLog("Unbanned " + p.getNick() + " (" + p.getIp() + ") by " + pnick, gui.blue);

            Scanner in = new Scanner(Paths.get("banlist.txt"));
            PrintWriter bany = new PrintWriter(new FileWriter("tmp.txt", true));
            while (in.hasNextLine()) {
                ln = in.nextLine();
                if (ln.indexOf(p.getIp()) < 0) {
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
        }
    }

    void kick() {
        if (!admin(1)) {
            return;
        }
        String ids = linia.substring(linia.indexOf("/kick") + 6);
        if (srv.getPlayer(ids) == null) {
            announce("Player " + ids + " not exist");
            srv.sendPck("/sv " + ServerCommands.kick + " " + ids);
            srv.sendPck("/sv " + ServerCommands.kick + " " + ids);
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
        gui.dodajLog("Added Admin: " + "[" + pid + "] " + pnick + " (" + pip + ")", gui.green);
        if (conf.isDispAddAdmin()) {
            announce("Added Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
        }

    }

    void ajadmin() {
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
        gui.dodajLog("Added Junior Admin: " + "[" + pid + "] " + pnick + " (" + pip + ")", gui.green);
        if (conf.isDispAddAdmin()) {
            announce("Added Junior Admin " + srv.getPlayer(Integer.parseInt(id)).getNick());
        }
    }

    void whois() {
        if (!admin(2)) {
            return;
        }
        String id = linia.substring(linia.indexOf("/whois") + 7);
        if (Integer.parseInt(id) < 1 || Integer.parseInt(id) > 34) {
            announce("ID " + id + " not exist!");
            return;
        }
        if (srv.getPlayer(Integer.parseInt(id)) == null) {
            announce("Player " + id + " not found");
            return;
        }
        try {
            FinderPlayer f = new FinderPlayer(srv.getPlayer(id).getNick(), 1);
            f.start();
            f.join();
            ArrayList<String> lista = f.found;
            for (int i = 0; i < 50; i += 5) {
                announce(lista.get(i));
                announce(lista.get(i + 1));
                announce(lista.get(i + 2));
                announce(lista.get(i + 3));
                announce(lista.get(i + 4));

                Thread.sleep(3000);

            }
        } catch (InterruptedException ex) {
            Loggs.loguj("Cmd-whois", ex);
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
        if (!admin(2)) {
            return;
        }
        String id = linia.substring(linia.indexOf("/map") + 5);
        gui.dodajLog("Changed map to id " + id + " by " + pnick + "\n", gui.mag);
        srv.sendPckUnanswered("/sv gotomap " + id);

    }

    

    void announce(String msg) {
        srv.sendPck("/lo announce (\"" + msg + "\")");
    }

    static void message(String msg) {
        srv.sendPck("/lo announce (\"" + msg + "\")");
    }

    boolean admin(int admin) {
        if(pid == 0)
            return true;
        if (srv.getPlayer(pid).getAccess() < admin) {
            return false;
        } else {
            return true;
        }
    }

    void censor() {
        
        if (!admin(1)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /censor") + 9).equals("")) {
            if (gui.getCensors().isSelected()) {
                announce("Censorship control is ON");
            } else {
                announce("Censorship control is OFF");
            }

        } else if (linia.substring(linia.indexOf(": /censor") + 9).equals(" on")) {
            gui.getCensors().setSelected(true);
            announce("Censorship control is ON");
        } else if (linia.substring(linia.indexOf(": /censor") + 9).equals(" off")) {
            gui.getCensors().setSelected(false);
            announce("Censorship control is OFF");
        }
        gui.saveSettings();
    }

    void sktk() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /sktk") + 7).equals("")) {
            if (gui.getSktk().isSelected()) {
                announce("Sk/Tk control is ON");
            } else {
                announce("Sk/Tk control is OFF");
            }

        } else if (linia.substring(linia.indexOf(": /sktk") + 7).equals(" on")) {
            gui.getSktk().setSelected(true);
            announce("Sk/Tk control is ON");
        } else if (linia.substring(linia.indexOf(": /sktk") + 7).equals(" off")) {
            gui.getSktk().setSelected(false);
            announce("Sk/Tk control is OFF");
        }
        gui.saveSettings();
    }

    void warnings() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /warnings") + 11).equals("")) {

            announce("After " + gui.getWarnings().getValue() + " warnings kick");

        } else if (linia.indexOf(": /warnings ") > -1) {
            String vl = linia.substring(linia.indexOf(": /warnings ") + 12);
            gui.getWarnings().setValue(Integer.parseInt(vl));
            announce("After " + gui.getWarnings().getValue() + " warnings kick");
        }
        gui.saveSettings();
    }

    void welcome() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /welcome") + 10).equals("")) {
            if (gui.getWelcomeCheck().isSelected()) {
                announce("Welcome players is ON");
            } else {
                announce("Welcome players is OFF");
            }

        } else if (linia.substring(linia.indexOf(": /welcome") + 10).equals(" on")) {
            gui.getWelcomeCheck().setSelected(true);
            announce("Welcome players is ON");
        } else if (linia.substring(linia.indexOf(": /welcome") + 10).equals(" off")) {
            gui.getWelcomeCheck().setSelected(false);
            announce("Welcome players is OFF");
        }
        gui.saveSettings();
    }

    void welcomeMessage() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /wmess") + 8).equals("")) {
            announce(gui.getWelcomePlayers().getText() + " " + gui.getWelcomeCombo().getSelectedItem() + " "
                    + gui.getWelcome2().getText());

        } else if (linia.indexOf(": /wmess ") > -1) {
            int poz = linia.indexOf(": /wmess ") + 8;
            int poz2 = linia.indexOf("+", poz) + 1;
            int poz3 = -1;
            String w1 = "", wc = "", w2 = "";
            if (poz2 - 1 < 0) {
                announce("Too few parameters!");
                announce("Use: \"msg+$var+msg\"");
                announce("e.g: Welcome+$nick+on Server");
                return;
            }
            poz3 = linia.indexOf("+", poz2) + 1;
            if (poz3 - 1 < 0) {
                announce("Too few parameters!");
                announce("Use: \"msg+$var+msg\"");
                announce("e.g: Welcome+$nick+on Server");
                return;
            }
            w1 = linia.substring(poz, poz2 - 1);
            wc = linia.substring(poz2, poz3 - 1);
            w2 = linia.substring(poz3);

            gui.getWelcomePlayers().setText(w1);
            gui.getWelcome2().setText(w2);

            if (wc.equals("$none")) {
                gui.getWelcomeCombo().setSelectedIndex(0);
            } else if (wc.equals("$id")) {
                gui.getWelcomeCombo().setSelectedIndex(2);
            } else if (wc.equals("$ip")) {
                gui.getWelcomeCombo().setSelectedIndex(3);
            } else {
                gui.getWelcomeCombo().setSelectedIndex(1);
            }

            announce(gui.getWelcomePlayers().getText() + " " + gui.getWelcomeCombo().getSelectedItem() + " "
                    + gui.getWelcome2().getText());

        }

        gui.saveSettings();
    }

    void adminPanel() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /apanel") + 9).equals("")) {
            if (gui.getAdminPanel().isSelected()) {
                announce("Admin panel is ON");
            } else {
                announce("Admin panel is OFF");
            }

        } else if (linia.substring(linia.indexOf(": /apanel") + 9).equals(" on")) {
            gui.getAdminPanel().setSelected(true);
            announce("Admin panel is ON");
        } else if (linia.substring(linia.indexOf(": /apanel") + 9).equals(" off")) {
            gui.getAdminPanel().setSelected(false);
            announce("Admin panel is OFF");
        }
        gui.saveSettings();
    }

    void items() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /items") + 8).equals("")) {
            if (gui.getControlItems().isSelected()) {
                announce("Item control is ON");
            } else {
                announce("Item control is OFF");
            }

        } else if (linia.substring(linia.indexOf(": /items") + 8).equals(" on")) {
            gui.getControlItems().setSelected(true);
            announce("Item control is ON");
        } else if (linia.substring(linia.indexOf(": /items") + 8).equals(" off")) {
            gui.getControlItems().setSelected(false);
            announce("Item control OFF");
        }
        gui.saveSettings();
    }

    void dispAdmin() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /adisp") + 8).equals("")) {
            if (gui.getDispAddAdmin().isSelected()) {
                announce("Display add admin is ON");
            } else {
                announce("Display add admin is OFF");
            }

        } else if (linia.substring(linia.indexOf(": /adisp") + 8).equals(" on")) {
            gui.getDispAddAdmin().setSelected(true);
            announce("Display add admin is ON");
        } else if (linia.substring(linia.indexOf(": /adisp") + 8).equals(" off")) {
            gui.getDispAddAdmin().setSelected(false);
            announce("Display add admin OFF");
        }
        gui.saveSettings();
    }

    void defMask() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /defmask") + 10).equals("")) {
            String vl = gui.getDefMask().getSelectedItem().toString();
            announce("Default ban mask is " + vl);

        } else if (linia.indexOf(": /defmask ") + 11 > -1) {
            String vl = linia.substring(linia.indexOf(": /defmask ") + 11);
            gui.getDefMask().setSelectedIndex(Integer.parseInt(vl) - 1);
            announce("Default ban mask is " + gui.getDefMask().getSelectedItem());
        }
        gui.saveSettings();
    }

    void maxMask() {
        if (!admin(2)) {
            return;
        }
        if (linia.substring(linia.indexOf(": /maxmask") + 10).equals("")) {
            String vl = gui.getMaxMask().getSelectedItem().toString();
            announce("Maximal ban mask is " + vl);

        } else if (linia.indexOf(": /maxmask ") + 11 > -1) {
            String vl = linia.substring(linia.indexOf(": /maxmask ") + 11);
            gui.getMaxMask().setSelectedIndex(Integer.parseInt(vl) - 1);
            announce("Maximal ban mask is " + gui.getMaxMask().getSelectedItem());
        }
        gui.saveSettings();
    }

    private void announce() {
        if (!admin(2)) {
            return;
        }
        String msg = linia.substring(linia.indexOf("/announce ") + 10);
        if (msg.length() > 39) {
            List<String> list = new ArrayList<>();
            int num = msg.length() / 39;
            if (msg.length() % 39 > 0) {
                num++;
            }
            for (int i = 0; i < num; i++) {
                String ln = msg.substring(0, 39);
                msg = msg.substring(39);
                list.add(ln);
            }
            for (String string : list) {
                announce(string);
            }
        }

    }

    private boolean commands() {
        if (!admin(2)) {
            return false;
        }
        for (String command : ServerCommands.commands) {
            if (linia.indexOf("/" + command) > -1) {
                String msg = srv.sendPck("/sv " + command + linia.substring(linia.indexOf(command) + command.length()));
                announce(msg);
                return true;
            }
        }
        return false;
    }

    private void whoami() {
        if (!admin(1)) {
            return;
        }
        if (srv.getPlayer(pid).getAccess() == 1) {
            announce("Junior Admin");
        } else if (srv.getPlayer(pid).getAccess() == 2) {
            announce("Admin");
        }
    }

    private void crash() {
        if (!admin(2)) {
            return;
        }
        gui.dodajLog("Server closed by [" +pid+"] "+ pnick + "\n", gui.red);
        announce("Server closing...");
        announce("Server closing...");
        announce("Server closing...");
        Main.srv.closeServer();
    }

    private void cmds() {
        if (!admin(2)) {
            return;
        }
        for (String cmd : ServerCommands.commands) {
            try {
                announce(cmd);
                this.sleep(500);
            } catch (InterruptedException ex) {
                Loggs.loguj("Cmd-cmds", ex);
            }
            }
    }



}
