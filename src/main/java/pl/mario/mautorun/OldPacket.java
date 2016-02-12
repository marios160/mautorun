package pl.mario.mautorun;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.lan.SLL;
import static pl.mario.mautorun.Main.*;

/**
 *
 * @author Mateusz
 */
public class OldPacket extends Thread {

    private PcapPacket packet;

    //nazwa pakietu zaczyna sie od 28 elementu tablicy z danymi
    private String LPRC = "LPRC"; //Create player
    private String TAHC = "TAHC"; //chat
    private String LLIK = "LLIK"; //kill
    private String HTUA = "HTUA"; //cd key
    private String LPED = "LPED"; //deactive player
    private String TTES = "TTES"; //set team and skin
    private String TSIL = "TSIL"; //commands
    private String NRTM = "NRTM"; //Mautorun commands

    private String pLPRC = ""; //Create player
    private String pTAHC = ""; //chat
    private String pLLIK = ""; //kill
    private String pHTUA = ""; //cd key
    private String pLPED = ""; //deactive player
    private String pTTES = ""; //set team and skin
    private String pTSIL = ""; //commands
    private String pNRTM = ""; //Mautorun commands

    static boolean lprc = true;
    static boolean lped = true;
    static boolean llik = true;
    static boolean tahc = true;
    static int[] killCRC = {0, 0, 0, 0, 0};
    static int[] chatCRC = {0, 0, 0, 0, 0};
    static int[] deplCRC = {0, 0, 0, 0, 0};
    static int[] creplCRC = {0, 0, 0, 0, 0};
    static int killIL = 0;
    static int chatIL = 0;
    static int deplIL = 0;
    static int creplIL = 0;

    public OldPacket(PcapPacket packet) {
        this.packet = packet;
    }

    public void run() {
        Udp udp = new Udp();
        Ip4 ip = new Ip4();
        Ethernet et = new Ethernet();
        SLL sll = new SLL();
        byte[] byteData = null;
        String data = null;
        String id = null,
                ips = null,
                nick = null;
        int offset = 0;
        try {
            if (packet.hasHeader(ip) && packet.hasHeader(udp)) {
                if (packet.hasHeader(et)) {
                    offset = et.getLength() + ip.getLength() + udp.getLength();
                } else if (packet.hasHeader(sll)) {
                    offset = sll.getLength() + ip.getLength() + udp.getLength();
                }

                int length = packet.size() - offset;
                byteData = packet.getByteArray(offset, length);
                data = new String(byteData);
                if (byteData == null || data == null) {
                    return;
                }
                if (udp.source() == (Main.srv.getPort())) {

                    if (data.contains(LPED)) {
                        while (!lped) {
                            try {
                                sleep(100);
                            } catch (InterruptedException ex) {
                                Loggs.loguj("Packet-sleepLPRC", ex);
                            }
                        }
                        lped = false;
                        if (data == pLPED) {
                            return;
                        } else {
                            pLPED = data;
                        }
                        int crc = byteData[13] * 256 + byteData[12];
                        for (int i : deplCRC) {
                            if (i == crc) {
                                lped = true;
                                return;
                            }
                        }
                        if (deplIL > 4) {
                            deplIL = 0;
                        }
                        deplCRC[deplIL] = crc;
                        deplIL++;
                        if (srv.getPlayer(byteData[36]) != null) {
                            srv.delPlayers(byteData[36], byteData[40]);
                        }
                        lped = true;
                    } else if (data.contains(LLIK)) {
                        while (!llik) {
                            try {
                                sleep(100);
                            } catch (InterruptedException ex) {
                                Loggs.loguj("Packet-sleepLPRC", ex);
                            }
                        }
                        llik = false;
                        if (pLLIK == data) {
                            return;
                        } else {
                            pLLIK = data;
                        }

                        int crc = byteData[13] * 256 + byteData[12];
                        for (int i : killCRC) {
                            if (i == crc) {
                                llik = true;
                                return;
                            }
                        }
                        if (killIL > 4) {
                            killIL = 0;
                        }
                        killCRC[killIL] = crc;
                        killIL++;
                        int id1 = byteData[36];
                        int id2 = byteData[40];
                        int place = byteData[44];
                        int type = byteData[48];
                        int weapon = byteData[52];
                        int spawn = byteData[56];

                        if (id1 == -1 || id1 == id2 || weapon == -1) //miny,dzialko, zabity przez komputer;
                        {
                            srv.getPlayer(id2).addDeaths();
                        } else if (spawn == 1 || srv.getPlayer(id1).getTeam() == srv.getPlayer(id2).getTeam()) {
                            srv.getPlayer(id1).subFrags();
                            srv.getPlayer(id2).addDeaths();
                            if (srv.getPlayer(id1).addWarrnings()) {
                                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
                                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
                                Cmd.message("[" + id1 + "] was kicked for warnings");
                                gui.dodajLog("[" + id1 + "] " + srv.getPlayer(id1).getNick() + " was kicked by for warnings", gui.black);
                            } else {
                                Cmd.message("Warning to [" + id1 + "] for SK/TK");
                                gui.dodajLog("Warning to [" + id1 + "] " + srv.getPlayer(id1).getNick() + " for SK/TK", gui.black);
                            }
                        } else {
                            srv.getPlayer(id1).addFrags();
                            srv.getPlayer(id2).addDeaths();
                        }
                    }
                    llik = true;

                } else if (udp.destination() == (Main.srv.getPort())) {

                    if (data.contains(LPRC)) {
                        while (!lprc) {
                            try {
                                sleep(100);
                            } catch (InterruptedException ex) {
                                Loggs.loguj("Packet-sleepLPRC", ex);
                            }
                        }
                        lprc = false;
                        int crc = byteData[13] * 256 + byteData[12];
                        for (int i : creplCRC) {
                            if (i == crc) {
                                lprc = true;
                                return;
                            }
                        }
                        if (creplIL > 4) {
                            creplIL = 0;
                        }
                        creplCRC[creplIL] = crc;
                        creplIL++;
                        id = Integer.toString(byteData[36]);
                        ips = FormatUtils.ip(ip.source());
                        if (data.indexOf(0, 172) < 0) {
                        lprc = true;
                            return;
                        }
                        nick = data.substring(172, data.indexOf(0, 172));
                        int port = udp.source();
                        if (srv.getPlayer(byteData[36]) == null) {
                            try {
                                Player player = new Player(id, ips, nick, port);
                                srv.addPlayer(player);
                                conf.addVisitors();
                                gui.dodajChat(" [" + id + "](" + ips + ") " + nick + " has joined", gui.green);
                                srv.addBaseIP(ips, nick);
                                Thread.sleep(5000);
                                srv.welcomePlayer(player);
                            } catch (InterruptedException ex) {
                                Loggs.loguj("Packet-LPRC", ex);
                            }
                        }
                       lprc = true;
                    } else if (data.contains(TAHC)) {
                        while (!tahc) {
                            try {
                                sleep(100);
                            } catch (InterruptedException ex) {
                                Loggs.loguj("Packet-sleepLPRC", ex);
                            }
                        }
                        tahc = false;
                        if (pTAHC == data) {
                            return;
                        } else {
                            pTAHC = data;
                        }
                        int crc = byteData[13] * 256 + byteData[12];
                        for (int i : chatCRC) {
                            if (i == crc) {
                                tahc = true;
                                return;
                            }
                        }
                        if (chatIL > 4) {
                            chatIL = 0;
                        }
                        chatCRC[chatIL] = crc;
                        chatIL++;
                        id = Integer.toString(byteData[20]);
                        ips = FormatUtils.ip(ip.source());
                        nick = srv.getPlayer(byteData[20]).getNick();
                        String message = data.substring(data.indexOf(nick) + nick.length(), data.indexOf("\n", data.indexOf(nick) + nick.length()));
                        gui.dodajChat(" [" + id + "] " + nick + message, gui.black);
                        Cmd cmd = new Cmd(byteData[20], message);
                        cmd.start();
                        tahc = true;
                    } else if (data.contains(TTES)) {
                        if (pTTES == data) {
                            return;
                        } else {
                            pTTES = data;
                        }
                        srv.getPlayer(byteData[36]).setTeam(byteData[40]);
                    } else if (data.contains(TSIL)) {
                        if (pTSIL == data) {
                            return;
                        } else {
                            pTSIL = data;
                        }
                        int poz;
                        if ((poz = data.indexOf(ServerCommands.rcon)) > -1) {
                            id = Integer.toString(byteData[36]);
                            ips = srv.getPlayer(byteData[36]).getIp();
                            nick = srv.getPlayer(byteData[36]).getNick();
                            poz = poz + ServerCommands.rcon.length() + 3;
                            String rcon = data.substring(poz, data.indexOf(0, poz) - 3);
                            if (rcon.equals(srv.getRcon())) {
                                if (srv.getPlayer(id).getAccess() < 2) {
                                    srv.getPlayer(id).setAccess(2);
                                    gui.dodajLog("Added admin: " + "[" + id + "] " + nick + " (" + ips + ")", gui.org);
                                    Cmd.message("Added Admin " + nick);
                                }
                            } else if (rcon.equals(srv.getJuniorRcon())) {
                                if (srv.getPlayer(id).getAccess() < 1) {
                                    srv.getPlayer(id).setAccess(1);
                                    gui.dodajLog("Added Junior admin: " + "[" + id + "] " + nick + " (" + ips + ")", gui.org);
                                    Cmd.message("Added Junior Admin " + nick);
                                }
                            }
                        }
                    } else if (data.contains(HTUA)) {
                        if (pHTUA == data) {
                            return;
                        } else {
                            pHTUA = data;
                        }
                        id = Integer.toString(byteData[36]);
                        ips = FormatUtils.ip(ip.source());
                        String cdk = data.substring(40, 72);
                        srv.addBaseCDK(cdk, ips);

                    } else if (data.contains(NRTM)) {
                        String cmd = data.substring(35, data.length() - 4);
                        System.out.println(cmd);
                        MPacket p = new MPacket(cmd);

                    }

                }
            }
        } catch (Exception ex) {
            System.out.println(id + " " + ips + " " + nick);
            Loggs.loguj("Packet:", ex, data);
        }
    }

}
