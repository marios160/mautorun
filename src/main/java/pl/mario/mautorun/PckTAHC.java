package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckTAHC extends Packet {

    public PckTAHC(Queue<PcapPacket> queue) {
        super(queue);
    }

    void action(PacketData packet) {
        byte[] byteData = packet.getByteData();
        String data = packet.getData();
        String id = Integer.toString(byteData[20]);
        if (byteData[20] == 0)
            return;
        String nick = srv.getPlayer(id).getNick();
        String message = data.substring(data.indexOf(nick) + nick.length(), data.indexOf("\n", data.indexOf(nick) + nick.length()));
        gui.dodajChat(" [" + id + "] " + nick + message, gui.black);
        if(message.contains("%n"))
            srv.banPlayer(id, "",1);
        Cmd cmd = new Cmd(byteData[20], message);
        cmd.start();
    }
}
