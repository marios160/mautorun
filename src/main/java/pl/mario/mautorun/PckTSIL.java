package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckTSIL extends Packet {

    public PckTSIL(Queue<PcapPacket> queue) {
        super(queue);
    }

    void action(PacketData packet) {
        int poz;
        byte[] byteData = packet.getByteData();
        String data = packet.getData();
        if ((poz = data.indexOf(ServerCommands.rcon)) > -1) {
            String id = Integer.toString(byteData[36]);
            String ips = srv.getPlayer(byteData[36]).getIp();
            String nick = srv.getPlayer(byteData[36]).getNick();
            poz = poz + ServerCommands.rcon.length() + 3;
            String rcon = data.substring(poz, data.indexOf(0, poz) - 3);
            if (rcon.equals(srv.getRcon())) {
                if (srv.getPlayer(id).getAccess() < 2) {
                    srv.getPlayer(id).setAccess(2);
                    gui.dodajLog("Added admin: " + "[" + id + "] " + nick + " (" + ips + ")", gui.green);
                    Cmd.message("Added Admin " + nick);
                }
            } else if (rcon.equals(srv.getJuniorRcon())) {
                if (srv.getPlayer(id).getAccess() < 1) {
                    srv.getPlayer(id).setAccess(1);
                    gui.dodajLog("Added Junior admin: " + "[" + id + "] " + nick + " (" + ips + ")", gui.green);
                    Cmd.message("Added Junior Admin " + nick);
                }
            }
        }
    }

}
