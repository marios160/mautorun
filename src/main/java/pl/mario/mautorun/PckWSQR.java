package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;

public class PckWSQR extends Packet {

    public PckWSQR(Queue<PcapPacket> queue) {
        super(queue);
    }

    boolean action(PacketData packet) {
        Player player = Main.srv.getPlayer(packet.getByteData()[20]);
        if (player == null)
            return false;
        player.setSpawned(true);
        return true;
    }

}
