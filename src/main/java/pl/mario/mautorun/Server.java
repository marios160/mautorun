package pl.mario.mautorun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.mario.mautorun.Main.*;

/**
 *
 * @author Mateusz
 */
public class Server extends Thread {

    private String ip;
    private String localip;
    private String rcon;
    private String juniorRcon;
    private String status;
    private String hostname;
    private int port;
    private String mapname;
    private int numplayers;
    private int maxplayers;
    private int uptime;
    private String timeleft;
    private int objtime;
    private boolean password;
    private int scoreIGI;
    private int scoreCons;
    private Player[] players;
    private List<Player> oldPlayers;
    private List<Player> banPlayers;
    private int numPl, numBan;
    private String[] listMaps;
    private String[] listIdMaps;
    Player nullPlayer;
    private int items;
    private int itemstime;
    private int numPlIgi;
    private int numPlCons;

    public Server() {

        this.ip = Web.pobierzIP();
        getNetworkF();
        this.juniorRcon = Main.conf.getJuniorRcon();
        this.status = "";
        this.hostname = "IGI2 Server [igi2.xaa.pl]";
        this.mapname = "";
        this.numplayers = 0;
        this.maxplayers = 0;
        this.uptime = 0;
        this.timeleft = "00:00";
        this.password = false;
        this.scoreIGI = 0;
        this.scoreCons = 0;

        players = new Player[35];
        oldPlayers = new ArrayList<Player>();
        banPlayers = new ArrayList<Player>();
        this.numPl = 0;
        this.numBan = 0;
        nullPlayer = new Player("0", "0.0.0.0", "NullPlayer", 26015);
        itemstime = 1;
        this.objtime = 0;
        this.numPlIgi = 0;
        this.numPlCons = 0;
    }

    public Server(Server serv) {
        this.ip = serv.getIp();
        this.localip = serv.getLocalip();
        this.rcon = serv.getRcon();
        this.port = serv.getPort();
        this.juniorRcon = Main.conf.getJuniorRcon();
        this.status = "";
        this.hostname = "IGI2 Server [igi2.xaa.pl]";
        this.mapname = "";
        this.numplayers = 0;
        this.maxplayers = 0;
        this.uptime = 0;
        this.timeleft = "00:00";
        this.password = false;
        this.scoreIGI = 0;
        this.scoreCons = 0;
        //35 el
        players = new Player[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        oldPlayers = new ArrayList<Player>();
        banPlayers = new ArrayList<Player>();
        this.numPl = 0;
        this.numBan = 0;
        nullPlayer = new Player("0", "0.0.0.0", "NullPlayer", 26015);
        this.itemstime = 1;
        this.objtime = 0;
        this.numPlIgi = 0;
        this.numPlCons = 0;
    }

    public void run() {
        try {
//closeServer();
            gui.dodajLog("Starting server...", gui.green);
            Runtime r = Runtime.getRuntime();
            try {

                if (conf.getSystem().equals("win")) {
                    r.exec(conf.getExe() + " serverdedicated");
                } else if (conf.getSystem().equals("lin")) {
                    r.exec("./" + conf.getExe() + " serverdedicated");
                }
                waitForProcess();
                Thread.sleep(10000);
            } catch (IOException ex) {
                Loggs.loguj("Server-Run", ex);
            } catch (InterruptedException ex) {
                Loggs.loguj("Server-Run", ex);
            }

            String status = "";
            int i = 0;
            while (status.isEmpty() && i < 5) {
                try {
                    status = sendStatus();
                } catch (IOException ex) {
                    Loggs.loguj("Server-Run", ex);
                }
                i++;
            }
            if (i > 4) {
                gui.dodajLog("Server not response 5 times!", gui.boom);
                gui.dodajLog("Error with server network!", gui.boom);
                return;
            }

            while (!sendListMaps());
            gui.getMap_button().setModel(new javax.swing.DefaultComboBoxModel(listMaps));
            Main.conf.setCrash(false);
            gui.getStartSrvTogg().setText("Disable Server");
            gui.setCrashbar(0);
            gui.setAlwaysOnTop(true);
            gui.setAlwaysOnTop(false);
            String pom = null;
            while (pom == null || pom.isEmpty()) {
                pom = sendPck("/sv " + ServerCommands.objtime);
            }
            this.objtime = Integer.parseInt(pom.substring(10));

            do {
                try {
                    updateInfo();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Loggs.loguj("Server-Run", ex);
                }

                if (Main.stopServerLoop) {
                    Main.stopServerLoop = false;
                    Main.stopSnifferLoop = true;
                    return;
                }
            } while (!conf.isCrash());

            gui.dodajLog("CRASH!", gui.boom);
            gui.dodajChat("=========CRASH===========", gui.boom);
            closeServer();
            conf.setCrashes(conf.getCrashes() + 1);
            gui.getCrashVal().setText(conf.getCrashes() + "");

        } catch (Exception ex) {
            Loggs.loguj("Server-run", ex);
        }
    }

    public void updateInfo() {
        try {

            String pom;
            CrashBar bar = new CrashBar();
            status = "";
            try {
                status = sendStatus();
            } catch (IOException ex) {
                //Loggs.loguj("Server-UpdateInfo1", ex);
            }
            int i = 0;
            if (status.isEmpty()) {
                Main.stopCrashBarLoop = false;
                bar.start();
            }

            while (i <= 2 && status.isEmpty()) {

                try {
                    status = sendStatus();
                } catch (IOException ex) {
                    //Loggs.loguj("Server-UpdateInfo2", ex);
                }
                i++;
            }
            if (status.isEmpty()) {
                Main.conf.setCrash(true);
                return;
            } else {
                Main.stopCrashBarLoop = true;
            }
            try {
                pom = status.substring(status.indexOf("hostname\\") + 9, status.indexOf("\\hostport"));
                hostname = pom;
                gui.getName_server().setText(hostname);
                pom = status.substring(status.indexOf("hostport\\") + 9, status.indexOf("\\mapname"));
                port = Integer.parseInt(pom);
                pom = status.substring(status.indexOf("mapname\\") + 8, status.indexOf("\\gametype"));
                mapname = pom;
                gui.getMap_button().setSelectedItem(mapname);
                pom = status.substring(status.indexOf("numplayers\\") + 11, status.indexOf("\\maxplayers"));
                numplayers = Integer.parseInt(pom);
                gui.getCurrent_players().setText(pom);
                pom = status.substring(status.indexOf("maxplayers\\") + 11, status.indexOf("\\gamemode"));
                maxplayers = Integer.parseInt(pom);

                gui.getMaxplayers().setSelectedIndex(maxplayers);
                pom = status.substring(status.indexOf("uptime\\") + 7, status.indexOf("\\timeleft"));
                uptime = Integer.parseInt(pom);
                pom = uptime / 60 + "h " + uptime % 60 + "min";
                gui.getUptimeVal().setText(pom);
                pom = status.substring(status.indexOf("timeleft\\") + 9, status.indexOf("\\mapstat"));
                timeleft = pom;
                setItems(mapname, timeleft);
                gui.getTime_button().setText(pom);
                pom = status.substring(status.indexOf("password\\") + 9, status.indexOf("\\team_t0"));
                password = Boolean.valueOf(pom);
                if (password) {
                    gui.getPasswdVal().setForeground(new java.awt.Color(255, 0, 0));
                    gui.getPasswdVal().setText("Yes");
                } else {
                    gui.getPasswdVal().setForeground(new java.awt.Color(0, 153, 51));
                    gui.getPasswdVal().setText("No");
                }
                pom = status.substring(status.indexOf("score_t0\\") + 9, status.indexOf("\\score_t1"));
                scoreIGI = Integer.parseInt(pom);
                pom = status.substring(status.indexOf("score_t1\\") + 9);
                pom = pom.substring(0, pom.indexOf("\\"));
                scoreCons = Integer.parseInt(pom);
                gui.getVisitVal().setText(conf.getVisitors() + "");
                gui.getCrashVal().setText(conf.getCrashes() + "");
                gui.getRemainingItems().setText(this.items + "");

                //updatePlayers(status);
                updatePings(status);
                updateTable();

            } catch (StringIndexOutOfBoundsException ex) {
                Loggs.loguj("Server-updateInfo", ex);
            }

        } catch (Exception ex) {
            Loggs.loguj("Server-updateInfo", ex);
        }
    }

    void setItems(String mapname, String timeleft) {
        int time, sec;
        if (timeleft.contains("LOADING")) {
            time = 14;
            sec = 1;
        } else if (timeleft.contains("MISSION")) {
            time = 0;
            sec = 0;
        } else if (timeleft.contains("NO")) {
            return;
        } else {
            time = Integer.parseInt(timeleft.substring(0, 2));
            sec = Integer.parseInt(timeleft.substring(3, 5));
        }
        String pom = null;

        if (time >= this.objtime - 1) {
            if (itemstime > sec) {
                itemstime = sec;
                return;
            }
            itemstime = sec;
            if (mapname.equals(Variables.Sandstorm)) {
                this.items = Variables.sand;
            } else if (mapname.equals(Variables.Redstone)) {
                this.items = Variables.red;
            } else if (mapname.equals(Variables.Timberland)) {
                this.items = Variables.timb;
            } else if (mapname.equals(Variables.Forestraid)) {
                this.items = Variables.forest;
            } else if (mapname.equals(Variables.ChineseTemple)) {
                this.items = Variables.china;
            } else if (mapname.equals(Variables.AimMap)) {
                this.items = Variables.aim;
            }
        }
    }

    void addBaseIP(String ip, String nick) {

        try {
            String line = ip;
            while(line.length()<17){
                line = line.concat(" ");
            }
            line = line.concat(nick+"\n");
            PrintWriter base = null;
            File fbase = new File(Main.path + "baseIP.txt");
            if (!fbase.exists()) {
                fbase.createNewFile();
            }
            base = new PrintWriter(new FileWriter(fbase, true));
            base.append(line);
            base.close();
        } catch (Exception ex) {
            Loggs.loguj("Server-addBaseIP", ex);
        }
    }

    void addBaseCDK(String cdk, String ip) {
        try {
            PrintWriter base = null;

            File fbase = new File(Main.path + "baseCDK.txt");
            if (!fbase.exists()) {
                fbase.createNewFile();
            }
            base = new PrintWriter(new FileWriter(fbase, true));
            base.append(cdk + "   " + ip + "\n");
            base.close();
        } catch (IOException ex) {
            Loggs.loguj("Server-addBaseCDK", ex);
        }
    }

    /*static void getInterface() {

        Enumeration<NetworkInterface> nets = null;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (NetworkInterface netint : Collections.list(nets)) {

            System.out.printf("Display name: %s\n", netint.getDisplayName());
            System.out.printf("Name: %s\n", netint.getName());
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                System.out.printf("InetAddress: %s\n", inetAddress);
            }
            System.out.printf("\n");
        }

    }*/
    void closeServer() {
        try {
            if (conf.getSystem().equals("win")) {
                Runtime.getRuntime().exec("taskkill /F /IM " + conf.getExe() + "*");
            } else if (conf.getSystem().equals("lin")) {
                Runtime.getRuntime().exec("pkill " + conf.getExe());
            }
            Thread.sleep(1000);
            saveLog();

        } catch (IOException ex) {
            Loggs.loguj("Server-CloseServer", ex);
        } catch (InterruptedException ex) {
            Loggs.loguj("Server-CloseServer", ex);
        } catch (Exception ex) {
            Loggs.loguj("Server-CloseServer", ex);
        }
    }

    static void waitForProcess() {
        String linia;
        String cmd = null;

        try {
            if (conf.getSystem().equals("win")) {
                cmd = "tasklist";
            } else if (conf.getSystem().equals("lin")) {
                cmd = "ps ax";
            }
            while (true) {
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((linia = input.readLine()) != null) {
                    if (linia.contains(conf.getExe())) {
                        //System.out.println(linia);
                        return;
                    }
                }
                input.close();
            }
        } catch (IOException ex) {
            Loggs.loguj("Server-CloseServer", ex);
        }
    }

    private void getNetworkF() {
        File file = new File("networkconfig.cfg");
        if (file.exists()) {
            BufferedReader bufferedReader = null;

            try {
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                Loggs.loguj("Conf-pobierznetw", ex);
            }

            String linia = null;
            do {

                try {
                    linia = bufferedReader.readLine();
                } catch (IOException ex) {
                    Loggs.loguj("Conf-pobierznetw", ex);
                }

                if (linia.indexOf(ServerCommands.rconpass) > 0) {
                    this.rcon = linia.substring(13, linia.indexOf("\");"));
                } else if (linia.indexOf(ServerCommands.svport) > 0) {
                    this.port = Integer.parseInt(linia.substring(10));
                    break;
                }

            } while (linia != null);

            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Loggs.loguj("Conf-pobierznetw", ex);
            }
        }
    }

    public void addBanPlayers(Player p) {
        if (banPlayers.size() == 3) {
            banPlayers.remove(0);
        }
        banPlayers.add(p);
    }

    public void delPlayers(int id, int type) {
        Player player = srv.getPlayer(id);
        String ip = player.getIp();
        String nick = player.getNick();
        int team = player.getTeam();
        if (oldPlayers.size() == 5) {
            oldPlayers.remove(0);
        }
        oldPlayers.add(player);
        players[id] = null;
        switch (type) {
            case 0:
                gui.dodajChat(" [" + id + "](" + ip + ") " + nick + " kicked out of the game", gui.blue);
                break;
            case 1:
                gui.dodajChat(" [" + id + "](" + ip + ") " + nick + " left the game", gui.red);
                break;
            case 2:
                gui.dodajChat(" [" + id + "](" + ip + ") " + nick + " lost connection", gui.mag);
                break;
            default:
                gui.dodajChat(" [" + id + "](" + ip + ") " + nick + " lost connection(ex)", gui.mag);
                break;
        }
        numPl++;
        if (team == 0) {
            subNumPlIgi();
        } else {
            subNumPlCons();
        }
    }

    public void welcomePlayer(Player player) {
        if (!gui.getWelcomeCheck().isEnabled()) {
            return;
        }
        String msg = gui.getWelcomePlayers().getText();
        switch (gui.getWelcomeCombo().getSelectedIndex()) {
            case 0:
                msg = msg.concat(" ");
                break;
            case 1:
                msg = msg.concat(" " + player.getNick() + " ");
                break;
            case 2:
                msg = msg.concat(" [" + player.getId() + "] ");
                break;
            case 3:
                msg = msg.concat(" " + player.getIp() + " ");
                break;
            default:
                msg = msg.concat(" ");
                break;
        }
        msg = msg.concat(gui.getWelcome2().getText());
        Cmd.message(msg);
    }

    public void addPlayer(Player p) {
        players[Integer.parseInt(p.getId())] = p;
    }

    void updatePlayers(String status) {
        String pom;
        for (Player zm : players) {
            if (zm != null) {
                if (status.indexOf(zm.getNick()) < 0) {
                    srv.delPlayers(Integer.parseInt(zm.getId()), 3);
                    continue;
                }
                try {
                    if (numplayers < 14) {
                        pom = status.substring(status.indexOf(zm.sfrag) + zm.sfrag.length(), status.indexOf(zm.sdeath) - 1);
                        zm.setFrags(Integer.parseInt(pom));
                        pom = status.substring(status.indexOf(zm.sdeath) + zm.sdeath.length(), status.indexOf(zm.sping) - 1);
                        zm.setDeaths(Integer.parseInt(pom));
                    } else {
                        zm.setFrags(0);
                        zm.setDeaths(0);
                    }
                    pom = status.substring(status.indexOf(zm.sping) + zm.sping.length(), status.indexOf(zm.steam) - 1);
                    zm.setPing(Integer.parseInt(pom));
                    pom = status.substring(status.indexOf(zm.steam) + zm.steam.length(), status.indexOf(zm.steam) + zm.steam.length() + 1);
                    zm.setTeam(Integer.parseInt(pom));
                } catch (StringIndexOutOfBoundsException ex) {
                    Loggs.loguj("Server-updatePlayers", ex);
                    return;
                }
            }
        }
    }

    void updatePings(String status) {
        String pom;
        for (Player zm : players) {
            if (zm != null) {
                if (status.indexOf("\\" + zm.getNick() + "\\") < 0) {
                    zm.addLostconn();
                    if(zm.getLostconn() == 5)
                        srv.delPlayers(Integer.parseInt(zm.getId()), 3);
                    continue;
                }
                try {
                    pom = status.substring(status.indexOf(zm.sping) + zm.sping.length(), status.indexOf(zm.steam) - 1);
                    zm.setPing(Integer.parseInt(pom));         
                } catch (StringIndexOutOfBoundsException ex) {
                    Loggs.loguj("Server-updatePings", ex);
                    return;
                }
            }
        }
    }

    void updateTable() {
        int igi = 0;
        int cons = 0;
        for (int i = 0; i < 32; i++) {
            gui.getIgiTab().setValueAt(null, i, 0);
            gui.getIgiTab().setValueAt(null, i, 1);
            gui.getIgiTab().setValueAt(null, i, 2);
            gui.getIgiTab().setValueAt(null, i, 3);
            gui.getIgiTab().setValueAt(null, i, 4);
            gui.getConsTab().setValueAt(null, i, 0);
            gui.getConsTab().setValueAt(null, i, 1);
            gui.getConsTab().setValueAt(null, i, 2);
            gui.getConsTab().setValueAt(null, i, 3);
            gui.getConsTab().setValueAt(null, i, 4);

        }
        for (Player zm : players) {
            if (zm != null) {
                if (zm.getTeam() == 0 || zm.getTeam() == -1) {
                    gui.getIgiTab().setValueAt(zm.getId(), igi, 0);
                    if (zm.isSpawned()) {
                        gui.getIgiTab().setValueAt(zm.getNick(), igi, 1);
                    } else {
                        gui.getIgiTab().setValueAt("(S) " + zm.getNick(), igi, 1);
                    }
                    gui.getIgiTab().setValueAt(zm.getFrags() + "/" + zm.getDeaths(), igi, 2);
                    gui.getIgiTab().setValueAt(zm.getIp(), igi, 3);
                    gui.getIgiTab().setValueAt(zm.getPing(), igi, 4);
                    igi++;
                } else {
                    gui.getConsTab().setValueAt(zm.getId(), cons, 0);
                    if (zm.isSpawned()) {
                        gui.getConsTab().setValueAt(zm.getNick(), cons, 1);
                    } else {
                        gui.getConsTab().setValueAt("(S) " + zm.getNick(), cons, 1);
                    }
                    gui.getConsTab().setValueAt(zm.getFrags() + "/" + zm.getDeaths(), cons, 2);
                    gui.getConsTab().setValueAt(zm.getIp(), cons, 3);
                    gui.getConsTab().setValueAt(zm.getPing(), cons, 4);
                    cons++;
                }
            }
        }

    }

    public String sendPck(String msg) {
        String message = "";
        try {
            String rcon = "/" + this.rcon;
            InetAddress servAddr = InetAddress.getByName(ip);
            byte buf[] = msg.getBytes();
            byte buff[];
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2000);
            socket.send(new DatagramPacket(rcon.getBytes(), rcon.length(), servAddr, port)); //wysylamy rcon
            buff = new byte[4096];
            DatagramPacket prcon = new DatagramPacket(buff, buff.length);
            socket.receive(prcon);  //odbieramy komende

            socket.send(new DatagramPacket(buf, buf.length, servAddr, port));              //nastepnie komende
            buf = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);  //odbieramy komende
            socket.close();

            message = new String(packet.getData());
            message = message.trim();
            message = message.substring(22, message.length() - 4);

        } catch (Exception ex) {

        }
        return message;
    }

    public void sendPckUnanswered(String msg) {
        try {
            String message = "";
            String rcon = "/" + this.rcon;
            InetAddress servAddr = InetAddress.getByName(ip);
            byte buf[] = msg.getBytes();
            byte buff[];
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2000);
            
            socket.send(new DatagramPacket(rcon.getBytes(), rcon.length(), servAddr, port)); //wysylamy rcon
            buff = new byte[4096];
            DatagramPacket prcon = new DatagramPacket(buff, buff.length);
            socket.receive(prcon);  //odbieramy komende
            
            socket.send(new DatagramPacket(buf, buf.length, servAddr, port));              //nastepnie komende
            socket.close();
        } catch (Exception ex) {
            Loggs.loguj("Server-sendPckUnanswered", ex);
        }
    }

    public String sendStatus() throws IOException {
        String message = "";
        String pck = "\\status\\";
        InetAddress servAddr = InetAddress.getByName(ip);

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(2000);
        socket.send(new DatagramPacket(pck.getBytes(), pck.length(), servAddr, port));              //nastepnie komende

        byte buf[] = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);                                             //odbieramy komende
        message = new String(packet.getData());
        socket.close();

        return message.trim();
    }

    public boolean sendListMaps() {
        String message = "";
        String msg = "/sv listmaps";
        String rcon = "/" + this.rcon;
        try {
            InetAddress servAddr = InetAddress.getByName(ip);
            byte buf[] = msg.getBytes();
            byte buff[];
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2000);
            socket.send(new DatagramPacket(rcon.getBytes(), rcon.length(), servAddr, port)); //wysylamy rcon
            buff = new byte[4096];
            DatagramPacket prcon = new DatagramPacket(buff, buff.length);
            socket.receive(prcon);  //odbieramy komende

            socket.send(new DatagramPacket(buf, buf.length, servAddr, port));              //nastepnie komende

            int i = 0;
            String[] maps = new String[100];
            buf = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            do {
                socket.receive(packet);  //odbieramy komende
                message = null;
                message = new String(packet.getData());
                message = message.trim();
                message = message.substring(22, message.length() - 4);
                if (message.indexOf("NetManager_ListMapsCB called") > 0) {
                    break;
                }
                maps[i] = message;
                i++;
            } while (message.indexOf("NetManager_ListMapsCB called") < 0);
            socket.close();

            listMaps = new String[i];
            listIdMaps = new String[i];
            for (int j = 0; j < i; j++) {
                listIdMaps[j] = maps[j].substring(maps[j].indexOf("[") + 1, maps[j].indexOf("]"));
                listMaps[j] = maps[j].substring(maps[j].indexOf("]") + 1, maps[j].length());
                if (listMaps[j].indexOf("[ACTIVE]") > 0) {
                    listMaps[j] = listMaps[j].substring(0, listMaps[j].indexOf("["));
                }
                listMaps[j] = listMaps[j].trim();
            }
        } catch (Exception ex) {
            Loggs.loguj("Pakiety-sendListMaps", ex);
            return false;
        }
        return true;
    }

    public boolean find(String nick) {
        for (Player zm : players) {
            if (zm != null && zm.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    void changeMaxplayers(int id) {
        if (maxplayers != id) {
            gui.dodajLog("Changing maxplayers to " + id, gui.pink);
            sendPck("/sv " + ServerCommands.maxplayers + " " + id);
        }
    }

    void sendCommand(String cmd) {
        if (!cmd.isEmpty()) {
            String odp = sendPck("/" + cmd);
            if (odp.isEmpty()) {
                odp = "No answer, wrong command";
            }

            gui.dodajLog(cmd, gui.gray);
            gui.dodajLog(odp.trim(), gui.gray);
            if (!Main.cmds.get(Main.cmds.size() - 1).equals(cmd)) {
                Main.cmds.add(cmd);
                if (Main.cmds.size() > 20) {
                    Main.cmds.remove(0);
                }
            }
            gui.getCommandField().setText(null);
        }
    }

    void sendAnnounce(String ann) {
        if (!ann.isEmpty()) {
            String m = "";
            while ((m = sendPck("/lo " + ServerCommands.announce + " (\"" + ann + "\")")).isEmpty());
            if (!m.isEmpty()) {
                gui.dodajChat("YOU > " + ann, gui.black);
            }
            if (!Main.anns.get(Main.anns.size() - 1).equals(ann)) {
                Main.anns.add(ann);
                if (Main.anns.size() > 20) {
                    Main.anns.remove(0);
                }
            }
            gui.getAnnounceField().setText(null);
        }
    }

    void changeMap(int id) {
        int idMap = 0;

        for (int i = 0; i < listMaps.length; i++) {
            if (mapname.equals(listMaps[i])) {
                idMap = i;
                break;
            }
        }
        if (idMap != id) {

            gui.dodajLog("Changing map to " + gui.getMap_button().getSelectedItem(), gui.pink);
            sendPckUnanswered("/sv " + ServerCommands.gotomap + " " + listIdMaps[id]);
            idMap = id;
        }
    }

    public void saveLog() {
        File log = new File("logs");
        if (!log.exists()) {
            log.mkdir();
        }
        String czas = conf.getTime("dd-MM-yyyy_HH-mm-ss");

        File afile = new File("Multiplayer.log");
        File bfile = null;
        if (conf.getSystem().equals("lin")) {
            bfile = new File("logs/" + czas + ".log");
        } else if (conf.getSystem().equals("win")) {
            bfile = new File("logs\\" + czas + ".log");
        }

        try {
            Files.copy(afile.toPath(), bfile.toPath(), REPLACE_EXISTING);
            Files.deleteIfExists(afile.toPath());
        } catch (IOException ex) {
            Loggs.loguj("Server-saveLog", ex);
        }

    }

    void kickPlayer(String id) {
        gui.dodajLog("[" + id + "] " + srv.getPlayer(id).getNick() + " was kicked (REMOTELY)", gui.pink);
        srv.sendPck("/sv " + ServerCommands.kick + " " + id);
        srv.sendPck("/sv " + ServerCommands.kick + " " + id);
        Cmd.message("[" + id + "]" + " was kicked (REMOTELY)");
    }

    void banPlayer(String id, String mask, int reason) {
        try {
            String cmd = "";
            Player p = getPlayer(id);
            if (conf.getSystem().equals("lin")) {
                cmd = "/sbin/iptables -I INPUT -s " + p.getIp() + mask + " -j DROP";
            } else if (conf.getSystem().equals("win")) {
                cmd = "netsh advfirewall firewall add rule name=\"CRASHER\" dir=in protocol=udp interface=any action=block remoteip=" + p.getIp() + mask;
            }
            switch (reason) {
                case 1:
                    Cmd.message(p.getNick() + " was banned for crash");
                    gui.dodajLog(p.getNick() + " (" + p.getIp() + mask + ") was banned for crash", gui.pink);
                    break;
                case 2:
                    Cmd.message(p.getNick() + " was banned");
                    gui.dodajLog(p.getNick() + " (" + p.getIp() + mask + ") was banned (REMOTELY)", gui.pink);
                    break;
            }
            Runtime.getRuntime().exec(cmd);
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);

            PrintWriter bany = new PrintWriter(new FileWriter("banlist.txt", true));
            switch (reason) {
                case 1:
                    cmd += "      #For Crash";
                    break;
                case 2:
                    cmd += "      #Remotely";
                    break;
            }
            bany.println(cmd);
            bany.close();

            p.setIp(p.getIp() + mask);
            srv.addBanPlayers(p);
        } catch (IOException ex) {
            Loggs.loguj("Server-banPlayer", ex);
        }
    }

    void banPlayer(String id, String mask, int reason, String msg) {
        try {
            String cmd = "";
            Player p = getPlayer(id);
            if (conf.getSystem().equals("lin")) {
                cmd = "/sbin/iptables -I INPUT -s " + p.getIp() + mask + " -j DROP";
            } else if (conf.getSystem().equals("win")) {
                cmd = "netsh advfirewall firewall add rule name=\"CRASHER\" dir=in protocol=udp interface=any action=block remoteip=" + p.getIp() + mask;
            }
            switch (reason) {
                case 1:
                    Cmd.message(p.getNick() + " was banned for crash");
                    gui.dodajLog(p.getNick() + " (" + p.getIp() + mask + ") was banned for crash " + msg, gui.pink);
                    break;
                case 2:
                    Cmd.message(p.getNick() + " was banned");
                    gui.dodajLog(p.getNick() + " (" + p.getIp() + mask + ") was banned (REMOTELY) " + msg, gui.pink);
                    break;
            }
            Runtime.getRuntime().exec(cmd);
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);

            PrintWriter bany = new PrintWriter(new FileWriter("banlist.txt", true));
            switch (reason) {
                case 1:
                    cmd += "      #For Crash";
                    break;
                case 2:
                    cmd += "      #Remotely";
                    break;
            }
            bany.println(cmd);
            bany.close();

            p.setIp(p.getIp() + mask);
            srv.addBanPlayers(p);
        } catch (IOException ex) {
            Loggs.loguj("Server-banPlayer", ex);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRcon() {
        return rcon;
    }

    public void setRcon(String rcon) {
        this.rcon = rcon;
    }

    public String getJuniorRcon() {
        return juniorRcon;
    }

    public void setJuniorRcon(String juniorRcon) {
        this.juniorRcon = juniorRcon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMapname() {
        return mapname;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }

    public int getNumplayers() {
        return numplayers;
    }

    public void setNumplayers(int numplayers) {
        this.numplayers = numplayers;
    }

    public int getMaxplayers() {
        return maxplayers;
    }

    public void setMaxplayers(int maxplayers) {
        this.maxplayers = maxplayers;
    }

    public int getUptime() {
        return uptime;
    }

    public void setUptime(int uptime) {
        this.uptime = uptime;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(String timeleft) {
        this.timeleft = timeleft;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public int getScoreIGI() {
        return scoreIGI;
    }

    public void setScoreIGI(int scoreIGI) {
        this.scoreIGI = scoreIGI;
    }

    public int getScoreCons() {
        return scoreCons;
    }

    public void setScoreCons(int scoreCons) {
        this.scoreCons = scoreCons;
    }

    public synchronized Player[] getPlayers() {
        return players;
    }

    public synchronized Player getPlayer(int id) {
        if (players[id] == null) {
            return getOldPlayerId(id);
        } else {
            return players[id];
        }
    }

    public synchronized Player getNullPlayer(int id) {
        return players[id];
    }

    public synchronized Player getPlayer(String id) {
        if (players[Integer.parseInt(id)] == null) {
            return getOldPlayerId(Integer.parseInt(id));
        } else {
            return players[Integer.parseInt(id)];
        }
    }

    public synchronized void setPlayers(Player[] players) {
        this.players = players;
    }

    public synchronized List<Player> getOldPlayers() {
        return oldPlayers;
    }

    public synchronized Player getOldPlayer(int id) {
        return oldPlayers.get(id);
    }

    public synchronized Player getOldPlayerId(int id) {
        for (Player oldPlayer : oldPlayers) {
            if (oldPlayer != null) {
                if (oldPlayer.getId().equals(Integer.toString(id))) {
                    return oldPlayer;
                }
            }
        }
        return null;
    }


    public synchronized List<Player> getBanPlayers() {
        return banPlayers;
    }

    public synchronized Player getBanPlayer(int id) {
        return banPlayers.get(id);
    }

    public int getLicz() {
        return numPl;
    }

    public void setLicz(int licz) {
        this.numPl = licz;
    }

    public int getLiczb() {
        return numBan;
    }

    public void setLiczb(int liczb) {
        this.numBan = liczb;
    }

    public String[] getListMaps() {
        return listMaps;
    }

    public void setListMaps(String[] listMaps) {
        this.listMaps = listMaps;
    }

    public String[] getListIdMaps() {
        return listIdMaps;
    }

    public void setListIdMaps(String[] listIdMaps) {
        this.listIdMaps = listIdMaps;
    }

    public String getLocalip() {
        return localip;
    }

    public void setLocalip(String localip) {
        this.localip = localip;
    }

    public int getNumPl() {
        return numPl;
    }

    public void setNumPl(int numPl) {
        this.numPl = numPl;
    }

    public int getNumBan() {
        return numBan;
    }

    public void setNumBan(int numBan) {
        this.numBan = numBan;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public synchronized void addItems() {
        if (!conf.isItems()) {
            return;
        }
        this.items++;
        gui.getRemainingItems().setText(this.items + "");
    }

    public synchronized int getNumPlIgi() {
        return numPlIgi;
    }

    public synchronized void setNumPlIgi(int numPlIgi) {
        this.numPlIgi = numPlIgi;
    }

    public void addNumPlIgi() {
        this.numPlIgi++;
    }

    public void subNumPlIgi() {
        this.numPlIgi--;
    }

    public synchronized int getNumPlCons() {
        return numPlCons;
    }

    public synchronized void setNumPlCons(int numPlCons) {
        this.numPlCons = numPlCons;
    }

    public void addNumPlCons() {
        this.numPlCons++;
    }

    public void subNumPlCons() {
        this.numPlCons--;
    }

    public synchronized void subItems() {
        if (!conf.isItems()) {
            return;
        }
        this.items--;
        if (this.items == 30) {
            Cmd.message("Remained 30 weapons to drop");
            Cmd.message("Please pick up weapons from the ground!");
            gui.dodajLog("Remained 30 weapons to drop", gui.cyan);
        } else if (this.items == 20) {
            Cmd.message("Remained 20 weapons to drop");
            Cmd.message("Please pick up weapons from the ground!");
            gui.dodajLog("Remained 20 weapons to drop", gui.cyan);
        } else if (this.items == 10) {
            Cmd.message("Remained 10 weapons to drop");
            Cmd.message("Please pick up weapons from the ground!");
            gui.dodajLog("Remained 10 weapons to drop", gui.cyan);
        } else if (this.items == 2) {
            Cmd.restart();
            Cmd.message("Too many weapons on the ground!");
            Cmd.message("Next time please pick up weapons");
            gui.dodajLog("Too many weapons on the ground!", gui.red);
        }
        gui.getRemainingItems().setText(this.items + "");

    }

}
