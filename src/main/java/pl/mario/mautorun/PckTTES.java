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
        int t = p.getTeam();
        p.setTeam(packet.getByteData()[40]);
        if (p.getTeam() == 0) {
            srv.addNumPlIgi();
            if(t > -1)
                srv.subNumPlCons();
        } else {
            srv.addNumPlCons();
            if(t > -1)
                srv.subNumPlIgi();
        }
        if (Math.abs(srv.getNumPlIgi() - srv.getNumPlCons())>=2) {
            Cmd.message("Please balance teams!");
        }
    }

}
