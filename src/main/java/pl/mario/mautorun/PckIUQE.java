
package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;


public class PckIUQE extends Packet{

    public PckIUQE(Queue<PcapPacket> queue) {
        super(queue);
    }


    void action(PacketData packet) {
        
    }
    
}
