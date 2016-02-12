
package pl.mario.mautorun;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JOptionPane;
import org.jnetpcap.*;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Udp;


/**
 *
 * @author Mario PL
 */
public class Sniffer extends Thread{
    

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
        final String localip = pomip.substring(0,pomip.length()-1);
        final Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);  
        if (pcap == null) {  
            JOptionPane.showMessageDialog(null,"Error while opening device for capture: "  
                + errbuf.toString());  
            return;  
        }  
        
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {  
            Udp udp = new Udp();
            Ip4 ip = new Ip4();
            
            
            public void nextPacket(PcapPacket packet, String user) {
                if(packet.hasHeader(udp))
                {

                        if (interrupt)
                            return;
                        OldPacket pck = new OldPacket(packet);
                        pck.start();
                }
                if(Thread.currentThread().isInterrupted() || Main.stopSnifferLoop){
                    Main.stopSnifferLoop = false;
                    pcap.breakloop();
                    return;
                }
            }
            
            
        };  
        pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler,null); 
        pcap.close();
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
