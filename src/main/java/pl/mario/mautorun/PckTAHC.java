package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import static pl.mario.mautorun.Main.*;

public class PckTAHC extends Packet {

    public PckTAHC(Queue<PcapPacket> queue) {
        super(queue);
    }

    void action(PacketData packet) {
        byte[] byteData = packet.getByteData();
        String data = packet.getData();
        String id = Integer.toString(byteData[20]);
        String nick = srv.getPlayer(byteData[20]).getNick();
        String message = data.substring(data.indexOf(nick) + nick.length(), data.indexOf("\n", data.indexOf(nick) + nick.length()));
        gui.dodajChat(" [" + id + "] " + nick + message, gui.black);
        Cmd cmd = new Cmd(byteData[20], message);
        cmd.start();
    }
}
