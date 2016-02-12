package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.srv;

public class PckLPED extends Packet {

    public PckLPED(Queue<PcapPacket> queue) {
        super(queue);
    }

    void action(PacketData packet) {
        if (srv.getPlayer(packet.getByteData()[36]) != null) {
            srv.delPlayers(packet.getByteData()[36], packet.getByteData()[40]);
        }
    }
}
