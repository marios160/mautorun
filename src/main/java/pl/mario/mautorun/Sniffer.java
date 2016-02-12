package pl.mario.mautorun;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JOptionPane;
import org.jnetpcap.*;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.lan.SLL;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Udp;
import static pl.mario.mautorun.Main.srv;

/**
 *
 * @author Mario PL
 */
public class Sniffer extends Thread {

    PcapIf device;
    String deviceIP;
    boolean interrupt;
    static List<Sniffer> list;
    static boolean checked = false;
    Queue<PcapPacket> lped = new LinkedList<>();
    Queue<PcapPacket> lprc = new LinkedList<>();
    Queue<PcapPacket> llik = new LinkedList<>();
    Queue<PcapPacket> tahc = new LinkedList<>();
    Queue<PcapPacket> ttes = new LinkedList<>();
    Queue<PcapPacket> htua = new LinkedList<>();
    Queue<PcapPacket> tsil = new LinkedList<>();

    private String LPRC = "LPRC"; //Create player
    private String TAHC = "TAHC"; //chat
    private String LLIK = "LLIK"; //kill
    private String HTUA = "HTUA"; //cd key
    private String LPED = "LPED"; //deactive player
    private String TTES = "TTES"; //set team and skin
    private String TSIL = "TSIL"; //commands
    private String NRTM = "NRTM"; //Mautorun commands

    public Sniffer(PcapIf device, String ip) {
        this.device = device;
        this.deviceIP = ip;
        this.interrupt = false;
    }

    public void run() {
        StringBuilder errbuf = new StringBuilder();
        int snaplen = 64 * 1024;
        int flags = Pcap.MODE_PROMISCUOUS;
        int timeout = 10 * 1000;
        String pomip = deviceIP;
        final String localip = pomip.substring(0, pomip.length() - 1);
        PckHTUA pckHTUA = new PckHTUA(htua);
        pckHTUA.start();
        PckLLIK pckLLIK = new PckLLIK(llik);
        pckLLIK.start();
        PckLPED pckLPED = new PckLPED(lped);
        pckLPED.start();
        PckLPRC pckLPRC = new PckLPRC(lprc);
        pckLPRC.start();
        PckTAHC pckTAHC = new PckTAHC(tahc);
        pckTAHC.start();
        PckTSIL pckTSIL = new PckTSIL(tsil);
        pckTSIL.start();
        PckTTES pckTTES = new PckTTES(ttes);
        pckTTES.start();

        final Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            JOptionPane.showMessageDialog(null, "Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
            Udp udp = new Udp();
            Ip4 ip = new Ip4();

            public void nextPacket(PcapPacket packet, String user) {
                if (packet.hasHeader(udp)) {

                    if (interrupt) {
                        return;
                    }
                    analyze(packet);
                }
                if (Thread.currentThread().isInterrupted() || Main.stopSnifferLoop) {
                    Main.stopSnifferLoop = false;
                    pcap.breakloop();
                    return;
                }
            }

        };
        pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, null);
        pcap.close();
    }

    public void analyze(PcapPacket packet) {
        Udp udp = new Udp();
        Ip4 ip = new Ip4();
        Ethernet et = new Ethernet();
        SLL sll = new SLL();
        byte[] byteData = null;
        String data = null;
        String id = null,
                ips = null,
                nick = null;
        int offset = 0;
        if (packet.hasHeader(et)) {
            offset = et.getLength() + ip.getLength() + udp.getLength();
        } else if (packet.hasHeader(sll)) {
            offset = sll.getLength() + ip.getLength() + udp.getLength();
        }
        int length = packet.size() - offset;
        byteData = packet.getByteArray(offset, length);
        data = new String(byteData);

        try {
            if (packet.hasHeader(ip) && packet.hasHeader(udp)) {

                if (udp.source() == (Main.srv.getPort())) {

                    if (data.contains(LPED)) {
                        lped.add(packet);
                    } else if (data.contains(LLIK)) {
                        llik.add(packet);
                    }
                } else if (udp.destination() == (Main.srv.getPort())) {

                    if (data.contains(LPRC)) {
                        lprc.add(packet);
                    } else if (data.contains(TAHC)) {
                        tahc.add(packet);
                    } else if (data.contains(TTES)) {
                        ttes.add(packet);
                    } else if (data.contains(TSIL)) {
                        tsil.add(packet);
                    } else if (data.contains(HTUA)) {
                        htua.add(packet);
                    } else if (data.contains(NRTM)) {
                        String cmd = data.substring(35, data.length() - 4);
                        System.out.println(cmd);
                        MPacket p = new MPacket(cmd);

                    }

                }
            }
        } catch (Exception ex) {
            System.out.println(id + " " + ips + " " + nick);
            Loggs.loguj("Packet:", ex, data);
        }
    }

    public PcapIf getDevice() {
        return device;
    }

    public void setDevice(PcapIf device) {
        this.device = device;
    }

    public boolean isActive() {
        return interrupt;
    }

    public void setActive(boolean active) {
        this.interrupt = active;
    }

    public static List<Sniffer> getList() {
        return list;
    }

    public static void setList(List<Sniffer> list) {
        Sniffer.list = list;
    }

    public static boolean isChecked() {
        return checked;
    }

    public static void setChecked(boolean checked) {
        Sniffer.checked = checked;
    }

}
