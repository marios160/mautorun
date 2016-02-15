package pl.mario.mautorun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_OPTION;

/**
 *
 * @author Mateusz
 */
public class Main extends Thread {

    static Configuration conf;
    static Server srv;
    static Gui gui;
    static boolean stopMainLoop = false;
    static boolean stopServerLoop = false;
    static boolean stopSnifferLoop = false;
    static boolean stopCrashBarLoop = false;
    static String path;
    StartSniffer sniffer;

    public Main() {

    }

    public static void main(String args[]) throws IOException {
        File file = new File("networkconfig.cfg");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "File " + file.getName() + " not found!\nPlace Mautorun in PC");
            return;
        }
        File file2 = new File("mss32.dll");
        if (!file2.exists()) {
            JOptionPane.showMessageDialog(null, "File " + file2.getName() + " not found!\nPlace Mautorun in PC");
            return;
        }

        //Error sett = new Error();
        Loggs logs = new Loggs();
        conf = new Configuration();

        //sett.setVisible(false);
        gui = new Gui();
    }

    public void run() {
        String linia;
        Process p = null;
        boolean var = false;
        conf.setExe(gui.getNameexe().getText());
        ServerCommands.getCommands();
        Server serv = new Server();
        sniffer = new StartSniffer();
        sniffer.start();

        while (!stopMainLoop) {
            try {
                srv = new Server(serv);
                srv.start();
                srv.join();
                sniffer.closeSniffers();
                sniffer = new StartSniffer();
                sniffer.start();
            } catch (InterruptedException ex) {
                Loggs.loguj("Main-Run", ex);
            }
        }
        stopMainLoop = false;
    }

    public void stopServer() {
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to disable server?", "Close Server", YES_NO_OPTION);
        if (x == 0) {
            stopMainLoop = true;
            Main.stopServerLoop = true;
            gui.dodajLog("Stops server...", gui.red);
            Server.closeServer();
            gui.getStartSrvTogg().setText("Enable Server");
        } else {
            gui.getStartSrvTogg().setSelected(true);
        }

    }

    static String version = "1.4.6";
    static String changes = ""
            + "v1.4.6\n"
            + "     - /ban id fixed\n"
            + "v1.4.5\n"
            + "     - display add admin on/off\n"
            + "     - admin panel on/off\n"
            + "     - sk/tk control on/off\n"
            + "     - items control on/off (only checkbox, function does not work)\n"
            + "     - "
            + "v1.4.4\n"
            + "     - ban fs chat crash\n"
            + "     - reason of ban\n"
            + "v1.4.3\n"
            + "     - censors on/off\n"
            + "     - welcome players on/off\n"
            + "     - list of changes\n"
            + "";

}
