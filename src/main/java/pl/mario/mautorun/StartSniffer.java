
package pl.mario.mautorun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import static pl.mario.mautorun.Sniffer.list;

/**
 *
 * @author Mateusz
 */
public class StartSniffer extends Thread{
    
    
    public void run(){
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); 
        StringBuilder errbuf = new StringBuilder();  
        int r = Pcap.findAllDevs(alldevs, errbuf);  
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {  
            JOptionPane.showMessageDialog(null,"Can't read list of devices, error is "+errbuf  
                .toString());  
            return;  
        }
        
        String addr = "";
        list = new ArrayList<>();
        for (PcapIf alldev : alldevs) {
            for (PcapAddr adr : alldev.getAddresses()) {
                if (adr.getAddr() != null)
                    addr = adr.getAddr().toString();
                if (addr.indexOf("[INET4:0.0.0.0]") < 0 && addr.indexOf("[INET4:") > -1) {
                    Sniffer s = new Sniffer(alldev, addr);
                    list.add(s);
                    s.start();
                }
            }
        } 
    }
    
     public void closeSniffers(){
        for (Sniffer snifer : list) {
            snifer.interrupt = true;
        }
    }

    
}
