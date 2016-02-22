package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckTTES extends Packet {

    public PckTTES(Queue<PcapPacket> queue) {
        super(queue);
    }

    void action(PacketData packet) {
        Player p = srv.getPlayer(packet.getByteData()[36]);
        p.setTeam(packet.getByteData()[40]);
        int igi = 0, cons = 0;
        for (Player zm : Main.srv.getPlayers()) {
            if (zm != null) {
                if (zm.getTeam() == 0) {
                    igi++;
                } else {
                    cons++;
                }
                Main.srv.setNumPlIgi(igi);
                Main.srv.setNumPlCons(cons);
            }
        }
        if (Math.abs(Main.srv.getNumPlIgi() - Main.srv.getNumPlCons()) >= 2) {
            Cmd.message("Please balance teams!");
        }
    }

}
