package pl.mario.mautorun;

import java.util.List;
import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckITCA extends Packet {

    public PckITCA(Queue<PcapPacket> queue) {
        super(queue);
    }

    boolean action(PacketData packet) {
        Integer list[] = null;
        int id = packet.getByteData()[20];
        int i1 = packet.getByteData()[41];
        int i2 = packet.getByteData()[40];
        int nr = packet.getByteData()[48];

        if (i1 < 0) {
            i1 += 256;
        }
        if (i2 < 0) {
            i2 += 256;
        }
        int objId = i1 * 256 + i2;
        if (srv.getMapname().equals(Variables.Sandstorm)) {
            list = ObjId.sand;
            if (nr > 8) {
                srv.banPlayer(Integer.toString(id), "", 1, "(" + objId + "-" + nr + ")");
                return true;
            }
        } else if (srv.getMapname().equals(Variables.Redstone)) {
            list = ObjId.red;
        } else if (srv.getMapname().equals(Variables.Timberland)) {
            list = ObjId.timb;
        } else if (srv.getMapname().equals(Variables.Forestraid)) {
            list = ObjId.forest;
        } else if (srv.getMapname().equals(Variables.ChineseTemple)) {
            list = ObjId.china;
        } else {
            return true;
        }

        for (Integer integer : list) {
            if (objId == integer) {
                return true;
            }
        }
        srv.banPlayer(Integer.toString(id), "", 1, "(" + objId + ")");
        return true;
    }

}
