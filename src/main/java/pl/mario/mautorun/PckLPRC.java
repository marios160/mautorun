package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;

public class PckLPRC extends Packet {

    public PckLPRC(Queue<PcapPacket> queue) {
        super(queue);
    }

     void action(PacketData packet) {
        String id = Integer.toString(packet.getByteData()[36]);
        if (packet.getData().indexOf(0, 172) < 0) {
            return;
        }
        String nick = packet.getData().substring(172, packet.getData().indexOf(0, 172));

        if (Main.srv.getNullPlayer(packet.getByteData()[36]) == null) {
            Player player = new Player(id, packet.getIpS(), nick, packet.getPort());
            Main.srv.addPlayer(player);
            Main.conf.addVisitors();
            Main.gui.dodajChat(" [" + id + "](" + packet.getIpS() + ") " + nick + " has joined", Main.gui.green);
            Main.srv.addBaseIP(packet.getIpS(), nick);
            WelcomePlayers pl = new WelcomePlayers(player);
            pl.start();
        }
    }
}
