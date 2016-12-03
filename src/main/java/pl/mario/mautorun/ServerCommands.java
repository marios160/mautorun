package pl.mario.mautorun;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mateusz
 */
public class ServerCommands {

    static public String rcon;
    static public String rconpass;
    static public String finger;
    static public String kick;
    static public String votekick;
    static public String ban;
    static public String listban;
    static public String unban;
    static public String suicide;
    static public String restartmap;
    static public String listmaps;
    static public String activatemap;
    static public String deactivatemap;
    static public String gotonext;
    static public String gotomap;
    static public String votemap;
    static public String votenext;
    static public String showvotes;
    static public String addmoney;
    static public String accounts;
    static public String monitor;
    static public String ploss;
    static public String timeout;
    static public String bandwidth;
    static public String choke;
    static public String fillpercent;
    static public String smooth;
    static public String svname;
    static public String svpassword;
    static public String svport;
    static public String svinterface;
    static public String maxplayers;
    static public String specmode;
    static public String spawncost;
    static public String spawntimer;
    static public String teamdamage;
    static public String warmup;
    static public String publicc;
    static public String bombtime;
    static public String autobalance;
    static public String autokick;
    static public String announce;
    static public String forcefirstspec;
    static public String bombrepostime;
    static public String pingmax;
    static public String plossmax;
    static public String idlemax;
    static public String goutmax;
    static public String moneystart;
    static public String moneycap;
    static public String moneykill;
    static public String moneyteamkill;
    static public String moneyplayerobjwin;
    static public String moneyteamobjwin;
    static public String moneyteamobjlost;
    static public String moneymissionwin;
    static public String moneymissionlost;
    static public String maprounds;
    static public String maptime;
    static public String mapteamscore;
    static public String playername;
    static public String spawnsafetimer;
    static public String allowsniperrifles;
    static public String objtime;
    static public String clport;

    static public List<String> commands;

    public static void getCommands() {
        try {
            FileReader fileReader = null;

            fileReader = new FileReader(Main.conf.getExe());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            do {
                line = bufferedReader.readLine();

                if (line != null && line.indexOf("Ban:Invalid parameters") > 0) {
                    announce = line.substring(395, 403);
                    finger = line.substring(407, 413);
                    showvotes = line.substring(415, 424);
                    monitor = line.substring(451, 458);
                    rcon = line.substring(479, 483);
                    suicide = line.substring(487, 494);
                    listban = line.substring(495, 502);
                    unban = line.substring(523, 528);
                    ban = line.substring(555, 558);
                    votekick = line.substring(583, 591);
                    kick = line.substring(627, 631);
                    restartmap = line.substring(659, 669);
                    listmaps = line.substring(695, 703);
                    votenext = line.substring(727, 735);
                    votemap = line.substring(759, 766);
                    deactivatemap = line.substring(787, 800);
                    activatemap = line.substring(831, 842);
                    gotomap = line.substring(923, 930);
                    gotonext = line.substring(951, 959);
                } else if (line != null && line.indexOf("IGI2 NETWORK CONFIG FILE") > 0) {
                    int poz = line.indexOf("IGI2 NETWORK CONFIG FILE");
                    int poz2 = line.indexOf("Not authorized");

                    goutmax = line.substring(poz2 + 16, poz2 + 23);
                    idlemax = line.substring(poz2 + 24, poz2 + 31);
                    plossmax = line.substring(poz2 + 32, poz2 + 40);
                    pingmax = line.substring(poz2 + 44, poz2 + 51);
                    forcefirstspec = line.substring(poz2 + 52, poz2 + 66);
                    bombrepostime = line.substring(poz2 + 68, poz2 + 81);
                    playername = line.substring(poz2 + 84, poz2 + 94);
                    ploss = line.substring(poz2 + 96, poz2 + 101);
                    autokick = line.substring(poz2 + 104, poz2 + 112);
                    timeout = line.substring(poz2 + 116, poz2 + 123);
                    fillpercent = line.substring(poz2 + 124, poz2 + 135);
                    choke = line.substring(poz2 + 136, poz2 + 141);
                    bandwidth = line.substring(poz2 + 144, poz2 + 153);
                    smooth = line.substring(poz2 + 304, poz2 + 310);
                    allowsniperrifles = line.substring(poz2 + 312, poz2 + 329);
                    spawnsafetimer = line.substring(poz2 + 332, poz2 + 346);
                    spawntimer = line.substring(poz2 + 348, poz2 + 358);
                    spawncost = line.substring(poz2 + 360, poz2 + 369);
                    bombtime = line.substring(poz2 + 372, poz2 + 380);
                    objtime = line.substring(poz2 + 384, poz2 + 391);
                    mapteamscore = line.substring(poz2 + 392, poz2 + 404);
                    maptime = line.substring(poz2 + 496, poz2 + 503);
                    maprounds = line.substring(poz2 + 576, poz2 + 585);
                    moneymissionlost = line.substring(poz2 + 684, poz2 + 700);
                    moneymissionwin = line.substring(poz2 + 704, poz2 + 719);
                    moneyteamobjlost = line.substring(poz2 + 720, poz2 + 736);
                    moneyteamobjwin = line.substring(poz2 + 740, poz2 + 755);
                    moneyplayerobjwin = line.substring(poz2 + 756, poz2 + 773);
                    moneyteamkill = line.substring(poz2 + 776, poz2 + 789);
                    moneykill = line.substring(poz2 + 792, poz2 + 801);
                    moneycap = line.substring(poz2 + 804, poz2 + 812);
                    moneystart = line.substring(poz2 + 816, poz2 + 826);
                    publicc = line.substring(poz2 + 1120, poz2 + 1126);
                    warmup = line.substring(poz2 + 1128, poz2 + 1134);
                    autobalance = line.substring(poz2 + 1136, poz2 + 1147);
                    teamdamage = line.substring(poz2 + 1148, poz2 + 1158);
                    specmode = line.substring(poz2 + 1160, poz2 + 1168);
                    maxplayers = line.substring(poz2 + 1172, poz2 + 1182);
                    clport = line.substring(poz2 + 1184, poz2 + 1190);
                    svinterface = line.substring(poz - 758, poz - 747);
                    svport = line.substring(poz - 662, poz - 656);
                    rconpass = line.substring(poz - 562, poz - 554);
                    svpassword = line.substring(poz - 486, poz - 476);
                    svname = line.substring(poz - 398, poz - 392);

                } else if (line != null && line.indexOf("Netshop_AMMOSLOT_MEDIPACK") > 0) {
                    int poz = line.indexOf("Netshop_AMMOSLOT_MEDIPACK");
                    accounts = line.substring(poz - 20, poz - 12);
                    addmoney = line.substring(poz + 112, poz + 120);

                }

            } while (line != null);
            bufferedReader.close();

            commands = new ArrayList<>();
            commands.add(rcon);
            commands.add(rconpass);
            commands.add(finger);
            commands.add(kick);
            commands.add(votekick);
            commands.add(ban);
            commands.add(listban);
            commands.add(unban);
            commands.add(suicide);
            commands.add(restartmap);
            commands.add(listmaps);
            commands.add(activatemap);
            commands.add(deactivatemap);
            commands.add(gotonext);
            commands.add(gotomap);
            commands.add(votemap);
            commands.add(votenext);
            commands.add(showvotes);
            commands.add(addmoney);
            commands.add(accounts);
            commands.add(monitor);
            commands.add(ploss);
            commands.add(timeout);
            commands.add(bandwidth);
            commands.add(choke);
            commands.add(fillpercent);
            commands.add(smooth);
            commands.add(svname);
            commands.add(svpassword);
            commands.add(svport);
            commands.add(svinterface);
            commands.add(maxplayers);
            commands.add(specmode);
            commands.add(spawncost);
            commands.add(spawntimer);
            commands.add(teamdamage);
            commands.add(warmup);
            commands.add(publicc);
            commands.add(bombtime);
            commands.add(autobalance);
            commands.add(autokick);
            commands.add(announce);
            commands.add(forcefirstspec);
            commands.add(bombrepostime);
            commands.add(pingmax);
            commands.add(plossmax);
            commands.add(idlemax);
            commands.add(goutmax);
            commands.add(moneystart);
            commands.add(moneycap);
            commands.add(moneykill);
            commands.add(moneyteamkill);
            commands.add(moneyplayerobjwin);
            commands.add(moneyteamobjwin);
            commands.add(moneyteamobjlost);
            commands.add(moneymissionwin);
            commands.add(moneymissionlost);
            commands.add(maprounds);
            commands.add(maptime);
            commands.add(mapteamscore);
            commands.add(playername);
            commands.add(spawnsafetimer);
            commands.add(allowsniperrifles);
            commands.add(objtime);
            commands.add(clport);

        } catch (FileNotFoundException ex) {
            Loggs.loguj("Komendy-wyciagnij", ex);
        } catch (IOException ex) {
            Loggs.loguj("Komendy-wyciagnij", ex);
        }

    }

}
