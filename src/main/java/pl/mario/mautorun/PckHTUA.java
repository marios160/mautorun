package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import static pl.mario.mautorun.Main.srv;

public class PckHTUA extends Packet {

    public PckHTUA(Queue<PcapPacket> queue) {
        super(queue);
    }

    boolean action(PacketData packet) {
        String cdk = packet.getData().substring(40, 72);
        srv.addBaseCDK(cdk, packet.getIpS());
        return true;
    }
}
