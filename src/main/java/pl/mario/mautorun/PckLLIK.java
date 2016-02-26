package pl.mario.mautorun;

import java.util.Queue;
import org.jnetpcap.packet.PcapPacket;
import static pl.mario.mautorun.Main.*;

public class PckLLIK extends Packet {

    public PckLLIK(Queue<PcapPacket> queue) {
        super(queue);
    }

    void action(PacketData packet) {

        byte[] byteData = packet.getByteData();
        int id1 = byteData[36];
        int id2 = byteData[40];
        int place = byteData[44];
        int type = byteData[48];
        int weapon = byteData[52];
        int spawn = byteData[56];
        Player pl1 = null;

        if (id1 != -1) {
            pl1 = srv.getPlayer(id1);
        }

        Player pl2 = srv.getPlayer(id2);

        if (id1 == -1 || id1 == id2 || weapon == -1) //miny,dzialko, zabity przez komputer;
        {
            if (id1 == id2 && type == 02) {
                kill.dodajLog("[" + id1 + "] " + pl1.getNick() + " feel to his death", kill.org);
            } else if (id1 == id2) {
                kill.dodajLog("[" + id1 + "] " + pl1.getNick() + " suicided", kill.org);
            } else if (type == 4) {
                kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " stepped on a mine and was blown to smithereens", kill.org);
            } else {
                kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " did a close inspection of a sentry gun", kill.org);
            }

            pl2.addDeaths();
        } else if (spawn == 1 || pl1.getTeam() == pl2.getTeam()) {
            pl1.subFrags();
            pl2.addDeaths();

            if (spawn == 1 && type == 3) {

                if (weapon == 18) {
                    kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was stabbed in "
                            + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] " + pl1.getNick()
                            + " in his spawn area", kill.red);
                } else {
                    kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was pummeled in "
                            + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] " + pl1.getNick()
                            + " from " + Variables.weapons[weapon] + " in his spawn area", kill.red);
                }

            } else if (spawn == 1) {
                kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was shot in "
                        + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] " + pl1.getNick()
                        + " from " + Variables.weapons[weapon] + " in his spawn area", kill.red);
            } else if (type == 3) {
                if (weapon == 18) {
                    kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was stabbed in "
                            + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] " + pl1.getNick()
                            + " in his spawn area", kill.red);
                } else {
                    kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was pummeled in "
                            + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] " + pl1.getNick()
                            + " from " + Variables.weapons[weapon] + " in his spawn area", kill.red);
                }

            } else {
                kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was shot in "
                        + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] "
                        + pl1.getNick() + " from " + Variables.weapons[weapon], kill.red);
            }
            if (!Main.conf.isSktk() || pl1.getAccess() > 1) {
                return;
            }
            if (pl1.addWarrnings()) {
                Cmd.message("[" + id1 + "] was kicked for warnings");
                gui.dodajLog("[" + id1 + "] " + pl1.getNick() + " was kicked for warnings", gui.red);
                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
                srv.sendPck("/sv " + ServerCommands.kick + " " + id1);
            } else {
                Cmd.message("Warning to [" + id1 + "] for SK/TK");
                gui.dodajLog("Warning to [" + id1 + "] " + pl1.getNick() + " for SK/TK", gui.org);
            }
        } else {

            pl1.addFrags();
            pl2.addDeaths();
            if (type == 3) {
                if (weapon == 18) {
                    kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was stabbed in "
                            + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] " + pl1.getNick()
                            , kill.green);
                } else {
                    kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was pummeled in "
                            + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] " + pl1.getNick()
                            + " from " + Variables.weapons[weapon], kill.green);
                }
            } else {
                kill.dodajLog("[" + id2 + "] " + pl2.getNick() + " was shot in "
                        + Variables.body[place].toLowerCase() + " by " + "[" + id1 + "] "
                        + pl1.getNick() + " from " + Variables.weapons[weapon], kill.green);
            }
        }
    }

}
