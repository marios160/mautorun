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
        
        Player pl1 = srv.getPlayer(id1);
        Player pl2 = srv.getPlayer(id2);

        if (id1 == -1 || id1 == id2 || weapon == -1) //miny,dzialko, zabity przez komputer;
        {
            pl2.addDeaths();
        } else if (spawn == 1 || pl1.getTeam() == pl2.getTeam()) {
            pl1.subFrags();
            pl2.addDeaths();
            if (!Main.conf.isSktk() || pl1.getAccess() > 1) {
                return;
            }
            if (pl1.addWarrnings()) {
                Cmd.message("[" + id1 + "] was kicked for warnings");
                gui.dodajLog("[" + id1 + "] " + pl1.getNick() + " was kicked by for warnings", gui.red);
                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
            } else {
                Cmd.message("Warning to [" + id1 + "] for SK/TK");
                gui.dodajLog("Warning to [" + id1 + "] " + pl1.getNick() + " for SK/TK", gui.org);
            }
        } else {
            pl1.addFrags();
            pl2.addDeaths();
        }
    }

}
