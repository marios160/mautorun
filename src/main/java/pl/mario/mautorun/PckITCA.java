
package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckITCA extends Packet{

    public PckITCA(Queue<PcapPacket> queue) {
        super(queue);
    }

    @Override
    void action(PacketData packet){
        int id = packet.getByteData()[20];
        int i1 = packet.getByteData()[41];
        int i2 = packet.getByteData()[40];
        if(i1<0)
            i1+=256;
        if(i2<0)
            i2+=256;
        int objId = i1*256+i2;
        for (Integer integer : ObjId.sand) {
            if(objId==integer){
                return;
            }
        }
        srv.banPlayer(Integer.toString(id), "", 1, "("+objId+")");
    }
    
}
