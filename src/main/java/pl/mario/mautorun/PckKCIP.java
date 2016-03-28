
package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;

public class PckKCIP extends Packet{

    public PckKCIP(Queue<PcapPacket> queue) {
        super(queue);
    }


    boolean action(PacketData packet) {
        Main.srv.addItems();
        return true;
    }
    
}
