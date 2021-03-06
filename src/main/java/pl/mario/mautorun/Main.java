package pl.mario.mautorun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    static KillLog kill;
    static boolean stopMainLoop = false;
    static boolean stopServerLoop = false;
    static boolean stopSnifferLoop = false;
    static boolean stopCrashBarLoop = false;
    static String path;
    static StartSniffer sniffer;
    static long time;
    static List<String> cmds;
    static List<String> anns;

    public Main() {

    }

    public static void main(String args[]) throws IOException, InterruptedException {
        Error error = new Error();
        File file = new File("networkconfig.cfg");
        if (!file.exists()) {
            error.dispose();
            JOptionPane.showMessageDialog(null, "File " + file.getName() + " not found!\nPlace Mautorun in PC");
            return;
        }
        File file2 = new File("mss32.dll");
        if (!file2.exists()) {
            error.dispose();
            JOptionPane.showMessageDialog(null, "File " + file2.getName() + " not found!\nPlace Mautorun in PC");
            return;
        }

        //Error sett = new Error();
        Loggs logs = new Loggs();
        conf = new Configuration();
//////////////////////////////////////////////////////////////////////////////////////////////
//        Web.update();
//////////////////////////////////////////////////////////////////////////////////////////////       

        cmds = new ArrayList<>();
        cmds.add("");
        anns = new ArrayList<>();
        anns.add("");
        gui = new Gui();
        error.dispose();
        kill = new KillLog();
        time = System.currentTimeMillis();
        while (true) {
            try {
                conf.setMrunTime((System.currentTimeMillis() - time) / 1000);
                gui.getmRunTime().setText((conf.getMrunTime() / (60 * 60)) + "h "
                        + ((conf.getMrunTime() / 60) % (60)) + "min " + conf.getMrunTime() % 60 + "sec");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Loggs.loguj("Main-main", ex);
            }
        }
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
//                sniffer.closeSniffers();
//                sniffer = new StartSniffer();
//                sniffer.start();
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
            srv.closeServer();
            gui.getStartSrvTogg().setText("Enable Server");
        } else {
            gui.getStartSrvTogg().setSelected(true);
        }

    }

    static String version = "2.0.0";
    static String changes = ""
            + "v2.0.0 - 20.01.2019\n"
            + "     - MLauncher module"
            + "v1.5.7 - .12.2016\n"
            + "     - "//zmodyfikowac snifera tak zeby uzyc firtra
            //usunac wyswietlanie pakietow
            //naprawic fakeplayers
            //wymyslic cos na flooda zeby serwer sie nie crashowal
            + "v1.5.6 - 02.12.2016\n"
            + "     - Fixed Google VPS Bug\n"
            + "     - Added remotely ban/unban field\n"
            + "     - Mirroring sniffers\n"
            + "     - FakePlayers tool\n"
            + "     - "
            + "v1.5.5\n"
            + "     - New website to control updates\n"
            + "     - Fixed Connect error web\n"
            + "v1.5.4\n"
            + "     - Fixed web control\n"
            + "     - Added update system\n"
            + "v1.5.3\n"
            + "     - Change minimal requires version Java\n"
            + "v1.5.2\n"
            + "     - New commands to NRTM\n"
            + "     - New command \"crash\" in admin panel\n"
            + "     - New help information\n"
            + "v1.5.1\n"
            + "     - Announce sender\n"
            + "     - Admin add by CDK\n"
            + "     - Kick no name nick\n"
            + "     - Show reason of some crash\n"
            + "     - Kick denided nicks\n"
            + "     - Finger and listmaps command reply fixed\n"
            + "     - fixed whois\n"
            + "     - fixed regex error\n"
            + "v1.5.0\n"
            + "     - fixed packets nulls\n"
            + "     - fixed space in rcon command\n"
            + "     - fixed ban with mask\n"
            + "     - better secure of items\n"
            + "     - junior rcon in settings\n"
            + "     - fixed command whoami\n"
            + "     - add/remove admin remotelly\n"
            + "     - versioning in serialize\n"
            + "     - finder player in popup menu\n"
            + "     - hard kick/ban\n"
            + "     - command whois\n"
            + "     - antyLadder\n"
            + "v1.4.12\n"
            + "     - new commands:\n"
            + "         sktk on/off\n"
            + "         warnings\n"
            + "         censorship on/off\n"
            + "         items on/off\n"
            + "         display add admin on/off\n"
            + "         welcome players on/off\n"
            + "         set welcome message\n"
            + "         admin panel on/off\n"
            + "         set default ban mask\n"
            + "         set maximal ban mask\n"
            + "     - killlog vertical resizability\n"
            + "     - Ban mapmod crashers on maps red, forest, timb, china\n"
            + "     - Optimization bases\n"
            + "     - better view of baseIP\n"
            + "     - Finder of players\n"
            + "v1.4.11\n"
            + "     - fixed null KILL\n"
            + "     - fixed null SETT \n"
            + "     - show incorrect rcon\n"
            + "     - fixed double lost connection\n"
            + "     - fixed balance team\n"
            + "     - warning before close Mautorun\n"
            + "     - added popup menu - add admins option\n"
            + "     - change left list and ban list to List (not array)\n"
            + "     - Kill Log\n"
            + "     - fixed nick with chars \"\\\""
            + "v1.4.10\n"
            + "     - chat and logserver controll autoscroll\n"
            + "     - fix lped null\n"
            + "     - save log when close program\n"
            + "     - commands and announce like terminal\n"
            + "     - loop announce chat\n"
            + "     - fix nulls\n"
            + "v1.4.9\n"
            + "     - fix null in getPlayer\n"
            + "     - fix ban for crash by id 65535\n"
            + "     - improved message of ban\n"
            + "     - showing using commands in console\n"
            + "     - ban on windows\n"
            + "     - fix leftban\n"
            + "     - message: balance teams\n"
            + "     - marked not spawned players\n"
            + "     - separate chat after crash\n"
            + "     - fix lost connection EX (updatePing)\n"
            + "v1.4.8\n"
            + "     - Ban for MapMod Crash\n"
            + "v1.4.7\n"
            + "     - Items control\n"
            + "     - soft uptime\n"
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
