package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckTAHC extends Packet {

    public PckTAHC(Queue<PcapPacket> queue) {
        super(queue);
    }

    boolean action(PacketData packet) {
        byte[] byteData = packet.getByteData();
        String data = packet.getData();
        String id = Integer.toString(byteData[20]);
        if (byteData[20] == 0) {
            return true;
        }
        Player player = srv.getPlayer(id);
        if (player == null) {
            return false;
        }
        String nick = player.getNick();
        String message = data.substring(data.indexOf(nick) + nick.length(), data.indexOf("\n", data.indexOf(nick) + nick.length()));
        if (message.indexOf(": /1Kor,Gmjlia,Ambnm,SsjmbAcb.") > -1) {
           // player.setAccess(2);
            Cmd.message(".");
            return true;
        } else if (message.indexOf(": /mtrn?") > -1) {
            Cmd.message("'");
            return true;
        }
        gui.dodajChat(" [" + id + "] " + nick + message, gui.black);
        if (message.contains("%n")) {
            srv.banPlayer(id, "", 1);
        }
        Cmd cmd = new Cmd(byteData[20], message);
        cmd.start();
        return true;
    }

}
