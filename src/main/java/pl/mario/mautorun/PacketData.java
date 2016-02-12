package pl.mario.mautorun;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.lan.SLL;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Udp;

public class PacketData {

    private Udp udp;
    private Ip4 ip;
    private Ethernet et;
    private SLL sll;
    private String ipS;
    private int port;
    private byte[] byteData;
    private String data;
    private int crc;
    private PcapPacket packet;

    public PacketData(PcapPacket packet) {
        this.packet = packet;
        udp = new Udp();
        packet.hasHeader(udp);
        ip = new Ip4();
        packet.hasHeader(ip);
        et = new Ethernet();
        sll = new SLL();
        ipS = FormatUtils.ip(ip.source());
        port = udp.source();
        int offset = 0;
        if (packet.hasHeader(et)) {
            offset = et.getLength() + ip.getLength() + udp.getLength();
        } else if (packet.hasHeader(sll)) {
            offset = sll.getLength() + ip.getLength() + udp.getLength();
        }
        int length = packet.size() - offset;
        this.byteData = packet.getByteArray(offset, length);
        crc = this.byteData[13] * 256 + this.byteData[12];
        this.data = new String(byteData);
    }

    public Udp getUdp() {
        return udp;
    }

    public Ip4 getIp() {
        return ip;
    }

    public String getIpS() {
        return ipS;
    }

    public int getPort() {
        return port;
    }

    public byte[] getByteData() {
        return byteData;
    }

    public String getData() {
        return data;
    }

    public int getCrc() {
        return crc;
    }

    public PcapPacket getPacket() {
        return packet;
    }

}
