
package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;


public class PckPORD extends Packet{

    public PckPORD(Queue<PcapPacket> queue) {
        super(queue);
    }


    boolean action(PacketData packet) throws Exception {
        Main.srv.subItems();
        return true;
    }
    
}
