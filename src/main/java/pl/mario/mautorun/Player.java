package pl.mario.mautorun;

import java.util.Arrays;

/**
 *
 * @author Mateusz
 */
public class Player {

    private String id;
    private String nick;
    private String ip;
    private int port;
    private int team = -1;
    private int access;
    private int warrnings;
    private int votes;
    private int frags;
    private int deaths;
    private int[] givVot;   //na ktore id glosowal
    private int[] gotVot;   //od kogo dostal vota
    private int nrVot;      //ile votow dostal
    private int ping;
    private boolean spawned = false;
    private int lostconn;

    String sfrag = "frags_";
    String sdeath = "deaths_";
    String sping = "ping_";
    String steam = "team_";
    String splayer = "player_";

    public Player() {
        this.id = "";
        this.nick = "";
        this.ip = "";
        this.team = -1;
        this.access = 0;
        this.warrnings = 0;
        this.votes = 0;
        this.frags = 0;
        this.deaths = 0;
        this.givVot = new int[3];
        this.gotVot = new int[5];
        this.nrVot = 0;
        this.ping = 0;
        this.spawned = false;
        this.lostconn = 0;
    }

    public Player(String id, String ip, String nick, int port) {
        this.id = id;
        this.nick = nick;
        this.ip = ip;
        this.port = port;
        this.access = 0;
        this.team = -1;
        this.warrnings = 0;
        this.votes = 0;
        this.givVot = new int[3];
        this.gotVot = new int[5];
        this.nrVot = 0;
        this.spawned = false;
        this.lostconn = 0;

        this.splayer += this.id + "\\";
        this.sfrag += this.id + "\\";
        this.sdeath += this.id + "\\";
        this.sping += this.id + "\\";
        this.steam += this.id + "\\";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getWarrnings() {
        return warrnings;
    }

    public void setWarrnings(int warrnings) {
        this.warrnings = warrnings;
    }

    public boolean addWarrnings() {

        this.warrnings++;
        if ((int) Main.gui.getWarnings().getValue() > this.warrnings) {
            return false;
        } else {
            return true;
        }
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getFrags() {
        return frags;
    }

    public void setFrags(int frags) {
        this.frags = frags;
    }

    public void addFrags() {
        this.frags++;
    }

    public void subFrags() {
        this.frags--;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeaths() {
        this.deaths++;
        setSpawned(false);
    }

    public void subDeaths() {
        this.deaths--;
    }
    
    public void addLostconn() {
        this.lostconn++;
    }

    public void subLostconn() {
        this.lostconn--;
    }

    public int getLostconn() {
        return lostconn;
    }

    public void setLostconn(int lostconn) {
        this.lostconn = lostconn;
    }

    public int[] getGivVot() {
        return givVot;
    }

    public void setGivVot(int[] givVot) {
        this.givVot = givVot;
    }

    public int[] getGotVot() {
        return gotVot;
    }

    public void setGotVot(int[] gotVot) {
        this.gotVot = gotVot;
    }

    public int getNrVot() {
        return nrVot;
    }

    public void setNrVot(int nrVot) {
        this.nrVot = nrVot;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public String getSfrag() {
        return sfrag;
    }

    public void setSfrag(String sfrag) {
        this.sfrag = sfrag;
    }

    public String getSdeath() {
        return sdeath;
    }

    public void setSdeath(String sdeath) {
        this.sdeath = sdeath;
    }

    public String getSping() {
        return sping;
    }

    public void setSping(String sping) {
        this.sping = sping;
    }

    public String getSteam() {
        return steam;
    }

    public void setSteam(String steam) {
        this.steam = steam;
    }

    public String getSplayer() {
        return splayer;
    }

    public void setSplayer(String splayer) {
        this.splayer = splayer;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    /**
     * Ustawianie reszty parametrow gracza czyli wyniku pingu i druzyny
     */
    /*public void setNewPlayer()
    {
        Pakiety stat = new Pakiety();
        String status = stat.sendStatus();
        if (status.indexOf(splayer) < 0)
            return;
        String pom;
        pom = status.substring(status.indexOf(splayer)+splayer.length(),status.indexOf(sfrag));
        setNick(pom);
        System.out.println(pom);
        pom = status.substring(status.indexOf(sfrag)+sfrag.length(),status.indexOf(sdeath)-1);
        setFrags(Integer.parseInt(pom));
        pom = status.substring(status.indexOf(sdeath)+sdeath.length(),status.indexOf(sping)-1);
        setDeaths(Integer.parseInt(pom));
        pom = status.substring(status.indexOf(sping)+sping.length(),status.indexOf(steam)-1);
        setPing(Integer.parseInt(pom));
        pom = status.substring(status.indexOf(steam)+steam.length(),status.indexOf(steam)+steam.length()+1);
        setTeam(Integer.parseInt(pom));
        Info.players[Integer.parseInt(this.id)] = this;
    }*/
}
