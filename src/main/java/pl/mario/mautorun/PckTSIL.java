package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckTSIL extends Packet {

    public PckTSIL(Queue<PcapPacket> queue) {
        super(queue);
    }

    boolean action(PacketData packet) {
        int poz;
        byte[] byteData = packet.getByteData();
        String data = packet.getData();
        String id = Integer.toString(byteData[36]);
        Player player = srv.getPlayer(id);
        if (player == null) {
            return false;
        }
        String ips = player.getIp();
        String nick = player.getNick();
        if ((poz = data.indexOf(ServerCommands.rcon)) > -1) {
            poz = poz + ServerCommands.rcon.length() + 3;
            String rcon = data.substring(poz, data.indexOf(0, poz) - 3);
            if (rcon.equals(srv.getRcon())) {
                if (player.getAccess() < 2) {
                    player.setAccess(2);
                    gui.dodajLog("Added admin: " + "[" + id + "] " + nick + " (" + ips + ")", gui.green);
                    if (conf.isDispAddAdmin()) {
                        Cmd.message("Added Admin " + nick);
                    }
                }
            } else if (rcon.equals(srv.getJuniorRcon())) {
                if (player.getAccess() < 1) {
                    player.setAccess(1);
                    gui.dodajLog("Added Junior admin: " + "[" + id + "] " + nick + " (" + ips + ")", gui.green);
                    if (conf.isDispAddAdmin()) {
                        Cmd.message("Added Junior Admin " + nick);
                    }
                }
            } else {
                gui.dodajLog("Incorrect rcon: " + rcon + " by [" + id + "] " + nick + " (" + ips + ")", gui.pink);
            }
        } else {
            for (String cmd : ServerCommands.commands) {

                if ((poz = data.indexOf(cmd)) > -1) {
                    String pck = packet.getData();
                    String cmds = pck.substring(poz, pck.indexOf(0, poz));
                    gui.dodajLog("[" + id + "] " + nick + " used command: " + cmds, gui.blue);
                    return true;
                }
            }
        }
        return true;
    }

}
