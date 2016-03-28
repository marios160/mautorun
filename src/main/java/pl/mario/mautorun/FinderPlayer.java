package pl.mario.mautorun;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import static pl.mario.mautorun.Main.gui;

public class FinderPlayer extends Thread {

    String value;
    int type;
    List<String> found;
    List<String> list;

    public FinderPlayer(String value, int type) {
        this.value = value;
        this.type = type;
        list = new ArrayList<>();
        found = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            Optimization op = new Optimization("baseIP.txt");
            op.start();
            Optimization op2 = new Optimization("baseCDK.txt");
            op2.start();
            op.join();
            op2.join();

            File base = new File(Main.path + "baseIP.txt");
            if (!base.exists()) {
                JOptionPane.showMessageDialog(gui, "File baseIP.txt not found!");
                return;
            }
            Scanner record = new Scanner(base);
            while (record.hasNextLine()) {
                list.add(record.nextLine());
            }
            record.close();
            switch (type) {
                case 1:
                    findNick();
                    break;
                case 2:
                    findIP();
                    break;
            }

        } catch (Exception ex) {
            Loggs.loguj("FinderPlayer-run", ex);
        }
    }

    void findIP() {
        int num = checkIP(value);
        if (num == 0) {
            found.add("");
            return;
        }
        String ip = value;
        if (num == 4) {
            ip = value.substring(0, value.lastIndexOf("."));
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).indexOf(ip) == 0) {
                found.add(list.get(i));
                list.remove(i);
                i--;
            }
        }
        List<String> found2 = new ArrayList<>();
        for (int i = 0; i < found.size(); i++) {
            String rc = found.get(i).substring(17);
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).indexOf(rc) > -1) {
                    found2.add(list.get(j));
                    list.remove(list.get(j));
                    j--;
                }
            }
        }

        List<String> found3 = new ArrayList<>();
        for (int i = 0; i < found2.size(); i++) {
            
            String rc = null;
            try {
                
            rc = found2.get(i).substring(0, found2.get(i).indexOf(" "));
            } catch (Exception e) {
                System.out.println(found2.get(i));
            }
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).indexOf(rc) == 0) {
                    found3.add(list.get(j));
                    list.remove(list.get(j));
                    j--;
                }
            }
            if (num == 2 || num == 1) {
                return;
            }
            rc = rc.substring(0, rc.lastIndexOf("."));
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).indexOf(rc) == 0) {
                    found3.add(list.get(j));
                    list.remove(list.get(j));
                    j--;
                }
            }
            if (num == 3) {
                return;
            }
            rc = rc.substring(0, rc.lastIndexOf("."));
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).indexOf(rc) == 0) {
                    found3.add(list.get(j));
                    list.remove(list.get(j));
                    j--;
                }
            }
        }
        found.addAll(found2);
        found.addAll(found3);

    }

    void findNick() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).indexOf(value) > -1) {
                found.add(list.get(i));
                list.remove(i);
                i--;
            }
        }

        List<String> found2 = new ArrayList<>();
        String rc = null;
        for (int i = 0; i < found.size(); i++) {
            try {
                
            rc = found.get(i).substring(0, found.get(i).indexOf(" "));
            } catch (Exception e) {
                System.out.println(found.get(i));
            }
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).indexOf(rc) == 0) {
                    found2.add(list.get(j));
                    list.remove(list.get(j));
                    j--;
                }
            }
            try {

                rc = rc.substring(0, rc.lastIndexOf("."));

            } catch (Exception ex) {
                //Loggs.loguj("FinderPlayer-fundNick", ex);
                System.out.println(rc);
            }
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).indexOf(rc) == 0) {
                    found2.add(list.get(j));
                    list.remove(list.get(j));
                    j--;
                }
            }
            try {

                rc = rc.substring(0, rc.lastIndexOf("."));
            } catch (Exception ex) {
                System.out.println(rc);
                //Loggs.loguj("FinderPlayer-fundNick", ex);
            }
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).indexOf(rc) == 0) {
                    found2.add(list.get(j));
                    list.remove(list.get(j));
                    j--;
                }
            }
        }
        found.addAll(found2);

    }

    int checkIP(String ip) {
        if (ip.length() > 15) {
            return 0;
        }
        char[] ipa = ip.toCharArray();
        int l = 0;
        for (char c : ipa) {
            if (c == '.') {
                l++;
            }
        }
        String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
        int num = 0;
        switch (l) {
            case 0:
                return 0;
            case 1:
                ip1 = ip.substring(0, ip.indexOf(".")).trim();
                num = 1;
                if (ip.length() > ip1.length() + 1) {
                    ip2 = ip.substring(ip.indexOf(".") + 1).trim();
                    num++;
                }
                break;
            case 2:
                ip1 = ip.substring(0, ip.indexOf(".")).trim();
                ip2 = ip.substring(ip.indexOf(".") + 1, ip.indexOf(".", 4)).trim();
                num = 2;
                if (ip.length() > ip1.length() + ip2.length() + 2) {
                    ip3 = ip.substring(ip.indexOf(".", 4) + 1).trim();
                    num++;
                }
                break;
            case 3:
                ip1 = ip.substring(0, ip.indexOf(".")).trim();
                ip2 = ip.substring(ip.indexOf(".") + 1, ip.indexOf(".", 4)).trim();
                ip3 = ip.substring(ip.indexOf(".", ip1.length() + 1) + 1, ip.lastIndexOf(".")).trim();
                num = 3;
                if (ip.length() > ip1.length() + ip2.length() + ip3.length() + 3) {
                    ip4 = ip.substring(ip.lastIndexOf(".") + 1).trim();
                    num++;
                }
                break;
        }

        switch (num) {
            case 0:
                return 0;
            case 1:
                if (Integer.parseInt(ip1) > 0 && Integer.parseInt(ip1) < 256) {
                    return 1;
                } else {
                    return 0;
                }
            case 2:
                if (Integer.parseInt(ip1) > 0 && Integer.parseInt(ip1) < 256) {
                    if (Integer.parseInt(ip2) >= 0 && Integer.parseInt(ip2) < 256) {
                        return 2;
                    } else {
                        return 0;
                    }
                }
                break;
            case 3:
                if (Integer.parseInt(ip1) > 0 && Integer.parseInt(ip1) < 256) {
                    if (Integer.parseInt(ip2) >= 0 && Integer.parseInt(ip2) < 256) {
                        if (Integer.parseInt(ip3) >= 0 && Integer.parseInt(ip3) < 256) {
                            return 3;
                        } else {
                            return 0;
                        }
                    }
                }
                break;
            case 4:
                if (Integer.parseInt(ip1) > 0 && Integer.parseInt(ip1) < 256) {
                    if (Integer.parseInt(ip2) >= 0 && Integer.parseInt(ip2) < 256) {
                        if (Integer.parseInt(ip3) >= 0 && Integer.parseInt(ip3) < 256) {
                            if (Integer.parseInt(ip4) >= 0 && Integer.parseInt(ip4) < 256) {
                                return 4;
                            } else {
                                return 0;
                            }
                        }
                    }
                }
                break;
        }
        return 0;
    }

}
