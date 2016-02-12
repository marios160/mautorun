package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;

/**
 *
 * @author Mateusz
 */
public class Packet extends Thread {

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
                PcapPacket packet = queue.poll();
                int offset = 0;
                int length = packet.size() - offset;
                byte[] byteData = packet.getByteArray(offset, length);
                String data = new String(byteData);
                if (!checkCrc(byteData)) {
                    continue;
                }
                action(byteData, data);
            }
        } catch (InterruptedException ex) {
            Loggs.loguj("Packet-run", ex);
        }
    }

    boolean checkCrc(byte[] byteData) {
        int crc = byteData[13] * 256 + byteData[12];
        for (int i : tabcrc) {
            if (i == crc) {
                return false;
            }
        }
        if (num > 4) {
            num = 0;
        }
        tabcrc[num] = crc;
        num++;
        return true;
    }

    private void action(byte[] byteData, String data) {

    }

}
