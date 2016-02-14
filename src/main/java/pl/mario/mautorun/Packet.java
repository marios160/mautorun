package pl.mario.mautorun;

import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnetpcap.packet.PcapPacket;

/**
 *
 * @author Mateusz
 */
public abstract class Packet extends Thread {

    int tabcrc[] = {0, 0, 0, 0, 0};
    int num = 0;
    Queue<PcapPacket> queue;

    public Packet(Queue<PcapPacket> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (!Main.stopSnifferLoop) {
                if (queue.isEmpty()) {
                    sleep(100);
                    continue;
                }
                PacketData packet;
                synchronized (queue) {
                    packet = new PacketData(queue.poll());
                }
                if (!checkCrc(packet)) {
                    continue;
                }
                try {
                    action(packet);
                } catch (Exception ex) {
                    Loggs.loguj("Packet-run", ex);
                }

            }
        } catch (InterruptedException ex) {
            Loggs.loguj("Packet-run", ex);
        }
    }

    boolean checkCrc(PacketData packet) {
        for (int i : tabcrc) {
            if (i == packet.getCrc()) {
                return false;
            }
        }
        if (num > 4) {
            num = 0;
        }
        tabcrc[num] = packet.getCrc();
        num++;
        return true;
    }

    abstract void action(PacketData packet) throws Exception;

}
