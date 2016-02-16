package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckLLIK extends Packet {

    public PckLLIK(Queue<PcapPacket> queue) {
        super(queue);
    }

    void action(PacketData packet) {

        byte[] byteData = packet.getByteData();
        int id1 = byteData[36];
        int id2 = byteData[40];
        int place = byteData[44];
        int type = byteData[48];
        int weapon = byteData[52];
        int spawn = byteData[56];

        if (id1 == -1 || id1 == id2 || weapon == -1) //miny,dzialko, zabity przez komputer;
        {
            srv.getPlayer(id2).addDeaths();
        } else if (spawn == 1 || srv.getPlayer(id1).getTeam() == srv.getPlayer(id2).getTeam()) {
            srv.getPlayer(id1).subFrags();
            srv.getPlayer(id2).addDeaths();
            if (!Main.conf.isSktk() || srv.getPlayer(id1).getAccess() > 1) {
                return;
            }
            if (srv.getPlayer(id1).addWarrnings()) {
                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
                Cmd.message("[" + id1 + "] was kicked for warnings");
                gui.dodajLog("[" + id1 + "] " + srv.getPlayer(id1).getNick() + " was kicked by for warnings", gui.red);
            } else {
                Cmd.message("Warning to [" + id1 + "] for SK/TK");
                gui.dodajLog("Warning to [" + id1 + "] " + srv.getPlayer(id1).getNick() + " for SK/TK", gui.org);
            }
        } else {
            srv.getPlayer(id1).addFrags();
            srv.getPlayer(id2).addDeaths();
        }
    }

}
