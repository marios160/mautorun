package pl.mario.mautorun;

import java.util.Queue;
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
                    boolean ret = action(packet);
                    if (!ret) {
                        packet.addExpiry();
                        if (packet.getExpiry() < 3) {
                            queue.add(packet.getPacket());
                        }
                    }
                } catch (Exception ex) {
                    Loggs.loguj("Packet-run", ex);
                    packet.addExpiry();
                    if (packet.getExpiry() < 3) {
                        queue.add(packet.getPacket());
                    }
                    showHexPck(packet);
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

    abstract boolean action(PacketData packet) throws Exception;

    public void showHexPck(PacketData packet) {
        int i = 0, k = 0, l = 0, n = 0;
        System.out.println("----------------------------------------------");
        for (byte b : packet.getByteData()) {
            String bytes = Integer.toHexString(b);
            if (bytes.length() > 2) {
                bytes = bytes.substring(bytes.length() - 2);
            } else if (bytes.length() < 2) {
                bytes = "0" + bytes;
            }

            System.out.print(bytes + " ");
            i++;
            if (i > 7) {
                System.out.print(" ");
            }
            if (i > 15) {
                l = l + 16;
                for (k = l - 16, n = 0; k < l; k++, n++) {
                    if (n == 7) {
                        System.out.print(" ");
                    }
                    char x = packet.getData().charAt(k);
                    if ((int) x < 32) {
                        System.out.print(".");
                    } else if ((int) x > 127) {
                        System.out.print(".");
                    } else {
                        System.out.print(x);
                    }
                }

                System.out.println("");
                i = 0;
            }
        }
        System.out.println("");
    }

}
