/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;

/**
 *
 * @author Mateusz
 */
public class PckLPRC extends Packet {

    public PckLPRC(Queue<PcapPacket> queue) {
        super(queue);
    }

    private void action(byte[] byteData, String data) {
        String id = Integer.toString(byteData[36]);
        String ips = FormatUtils.ip(ip.source());
        if (data.indexOf(0, 172) < 0) {
            return;
        }
        String nick = data.substring(172, data.indexOf(0, 172));
        int port = udp.source();
        if (srv.getPlayer(byteData[36]) == null) {
            try {
                Player player = new Player(id, ips, nick, port);
                srv.addPlayer(player);
                conf.addVisitors();
                gui.dodajChat(" [" + id + "](" + ips + ") " + nick + " has joined", gui.green);
                srv.addBaseIP(ips, nick);
                Thread.sleep(5000);
                srv.welcomePlayer(player);
            } catch (InterruptedException ex) {
                Loggs.loguj("Packet-LPRC", ex);
            }
        }
    }
}
