
package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.srv;

public class PckTTES extends Packet{
    
    public PckTTES(Queue<PcapPacket> queue) {
        super(queue);
    }
    
    void action(PacketData packet){
        srv.getPlayer(packet.getByteData()[36]).setTeam(packet.getByteData()[40]);
    }
    
}
