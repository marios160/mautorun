package pl.mario.mautorun;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JOptionPane;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Sniffer.list;

/**
 *
 * @author Mateusz
 */
public class StartSniffer extends Thread {

    Queue<PcapPacket> lped = new LinkedList<>();
    Queue<PcapPacket> lprc = new LinkedList<>();
    Queue<PcapPacket> llik = new LinkedList<>();
    Queue<PcapPacket> tahc = new LinkedList<>();
    Queue<PcapPacket> ttes = new LinkedList<>();
    Queue<PcapPacket> htua = new LinkedList<>();
    Queue<PcapPacket> tsil = new LinkedList<>();
    Queue<PcapPacket> pord = new LinkedList<>();
    Queue<PcapPacket> kcip = new LinkedList<>();
    Queue<PcapPacket> itca = new LinkedList<>();
    Queue<PcapPacket> wsqr = new LinkedList<>();
    Queue<PcapPacket> iuqe = new LinkedList<>();

    public void run() {
        try {
            
            List<PcapIf> alldevs = new ArrayList<PcapIf>();
            StringBuilder errbuf = new StringBuilder();
            int r = Pcap.findAllDevs(alldevs, errbuf);
            if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Can't read list of devices, error is " + errbuf
                        .toString());
                return;
            }

            String addr = "";
            list = new ArrayList<>();
            for (PcapIf alldev : alldevs) {
                if(alldev.getAddresses().isEmpty()){
                    Sniffer s = new Sniffer(alldev, addr, this);
                    s.start();
                    continue;
                }
                for (PcapAddr adr : alldev.getAddresses()) {
                    if (adr.getAddr() != null) {
                        addr = adr.getAddr().toString();
                    }
                    if (addr.indexOf("[INET4:0.0.0.0]") < 0 && addr.indexOf("[INET4:") > -1) {
                        Sniffer s = new Sniffer(alldev, addr, this);
                        list.add(s);
                        s.start();
                    }
                }
            }
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
            PckPORD pckPORD = new PckPORD(pord);
            pckPORD.start();
            PckKCIP pckKCIP = new PckKCIP(kcip);
            pckKCIP.start();
            PckITCA pckITCA = new PckITCA(itca);
            pckITCA.start();
            PckWSQR pckWSQR = new PckWSQR(wsqr);
            pckWSQR.start();
//        PckIUQE pckIUQE = new PckIUQE(iuqe);
//        pckIUQE.start();
        } catch (UnsatisfiedLinkError e) {
            JOptionPane.showMessageDialog(Main.gui,"You have problem with libraries.\nContact with Mario PL!");
            System.exit(0);
        } catch (Exception ex) {
            Loggs.loguj("StartSniffer-Run", ex);
        }
    }

    public void closeSniffers() {
        for (Sniffer snifer : list) {
            snifer.interrupt = true;
            snifer.interrupt();
        }
    }

}
