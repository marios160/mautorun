package pl.mario.mautorun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.mario.mautorun.Main.*;

/**
 *
 * @author Mateusz
 */
public class MLauncher extends Thread {

    public MLauncher() {

    }

    public void run() {
        gui.dodajLog("Starting MLauncher...", gui.green);
        do {
            try {
                Map<Integer, String> players = querySelectStatus("2");
                if (!players.isEmpty()) {
                    for (Map.Entry<Integer, String> el : players.entrySet()) {
                        String cmd = "iptables -A INPUT -s " + el.getValue() + " -p udp --dport 26001 -j ACCEPT";
                        execCmd(cmd);
                        PrintWriter log = new PrintWriter(new FileWriter(Main.path + "PlayersAccess.txt", true));
                        log.append(cmd.replace("-A", "-D") + "\n");
                        log.close();
                        queryUpdateStatus("id", Integer.toString(el.getKey()), "3");
                        gui.dodajLog("New access for: " + el.getValue(), gui.cyan);
                    }
                }
                for (int i = 0; i < 10; i++) {
                    if (conf.isCrash()) {
                        break;
                    }
                    Thread.sleep(500);
                }
            } catch (InterruptedException | IOException ex) {
                Loggs.loguj("MLauncher-run", ex);
            }
        } while (!Main.conf.isCrash());
        try {
            File file = new File(Main.path + "PlayersAccess.txt");
            if (!file.exists()) {
                return;
            }
            Scanner read = new Scanner(file);
            int i = 0;
            while (read.hasNextLine()) {
                String line = read.nextLine();
                System.out.println(line);
                execCmd(line);
            }
            read.close();
            file.delete();
        } catch (FileNotFoundException ex) {
            Loggs.loguj("MLauncher-run", ex);
        }

    }

    public static void delIptablesRule(String ip) {
        String cmd = "iptables -D INPUT -s " + ip + " -p udp --dport 26001 -j ACCEPT";
        String result = "";
        do {
            result = execCmd(cmd);
        } while (!result.contains("iptables: Bad rule (does a matching rule exist in that chain?)"));
        gui.dodajLog("Remove access for: " + ip, gui.pink);
        try {
            File file = new File(Main.path + "PlayersAccess.txt");
            File fileTMP = new File(Main.path + "PlayersAccessTMP.txt");
            PrintWriter print = new PrintWriter(new FileWriter(fileTMP, true));
            if (!file.exists()) {
                return;
            }
            Scanner read = new Scanner(file);
            int i = 0;
            while (read.hasNextLine()) {
                String line = read.nextLine();
                if (!line.contains(cmd)) {
                    print.append(line);
                }
            }
            read.close();
            file.delete();
            print.close();
            fileTMP.renameTo(file);
        } catch (FileNotFoundException ex) {
            Loggs.loguj("MLauncher-run", ex);
        } catch (IOException ex) {
            Logger.getLogger(MLauncher.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Map querySelectStatus(String status) {
        Map<Integer, String> players = new HashMap<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://185.238.74.50:3306/igi2", "root", "XedeX160!");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, ip FROM user WHERE status='" + status + "';");
            while (rs.next()) {
                players.put(rs.getInt("id"), rs.getString("ip"));
            }
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Loggs.loguj("MLauncher-querySelectStatus", ex);
        }
        return players;
    }

    public static void queryUpdateStatus(String field, String value, String status) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://185.238.74.50:3306/igi2", "root", "XedeX160!");
            Statement stmt = con.createStatement();
            int result = stmt.executeUpdate("UPDATE user SET status='" + status + "' WHERE " + field + "='" + value + "';");
//            while (rs.next()) {
//                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
//            }
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Loggs.loguj("MLauncher-queryUpdateStatus", ex);
        }
    }

    public static void removePlayerAccess(Player p) {
        delIptablesRule(p.getIp());
        if (p.getCdk().isEmpty()) {
            queryUpdateStatus("ip", p.getIp(), "0");
        } else {
            queryUpdateStatus("cdkey", p.getCdk(), "0");
        }
    }

    public static String execCmd(String cmd) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String s = "Outputs:\n";
            String line = null;
            while ((line = stdInput.readLine()) != null) {
                s += line;
            }

            line = null;
            while ((line = stdError.readLine()) != null) {
                s += line;
            }

            return s;
        } catch (IOException ex) {
            Loggs.loguj("MLauncher-execCmd", ex);
        }
        return null;
    }

}
