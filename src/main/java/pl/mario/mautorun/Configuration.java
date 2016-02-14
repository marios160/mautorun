
package pl.mario.mautorun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Mateusz
 */
public class Configuration implements Serializable{
    
    private String juniorRcon;
    private  String system;
    private  SimpleDateFormat time;
    private  String exe;
    private int visitors;
    private int mrunTime;
    private int crashes;
    private boolean crash;
    
    private int warnings;
    private int defMask;
    private int maxMask;
    private boolean welcomeCheck;
    private String welcome1;
    private int welcomeCombo;
    private String welcome2;
    private boolean censors;
    
    

    public Configuration() {
        this.system = System.getProperty("os.name").toLowerCase().substring(0,3);
        this.time = new SimpleDateFormat("[MM.dd][HH:mm:ss]");
        if (system.equals("win")) {
            Main.path = "mfiles\\";
        } else if (system.equals("lin")) {
            Main.path = "mfiles/";
        }
        Configuration c = getConfFile();
        this.exe = c.exe;
        this.visitors = c.visitors;
        this.mrunTime = c.mrunTime;
        this.crashes = c.crashes;
        this.juniorRcon = c.juniorRcon;
        this.defMask = c.defMask;
        this.maxMask = c.maxMask;
        this.warnings = c.warnings;
        this.welcome1 = c.welcome1;
        this.welcome2 = c.welcome2;
        this.welcomeCombo = c.welcomeCombo;
        this.welcomeCheck = c.welcomeCheck;
        this.censors = c.censors;
    }
    
    public Configuration(int x){
        this.system = System.getProperty("os.name").toLowerCase().substring(0,3);
        this.time = new SimpleDateFormat("[MM.dd][HH:mm:ss]");
        if (system.equals("win")) {
            Main.path = "mfiles\\";
        } else if (system.equals("lin")) {
            Main.path = "mfiles/";
        }
        this.exe = "igi2.exe";
        crashes = 0;
        mrunTime = 0;
        visitors = 0;
        juniorRcon = "MarioPL";
        defMask = 23;
        maxMask = 15;
        warnings = 2;
        this.welcome1 = "Welcome";
        this.welcome2 = "on IGI2 Server!";
        this.welcomeCombo = 1;
        this.welcomeCheck = true;
        this.censors = true;
    }
    
    public Configuration getConfFile()
    {
        File file2 = new File(Main.path+"cfg.mat");
        Configuration c = null;
        
        if(file2.exists())
        {
            ObjectInputStream pl2=null;
            try {
                pl2=new ObjectInputStream(new FileInputStream(Main.path+"cfg.mat"));
                c = (Configuration)pl2.readObject();
                pl2.close();
            } catch (IOException | ClassNotFoundException ex) {Loggs.loguj("Conf-pobierzklase", ex);}
        }
        else
            c = new Configuration(1);
        
        return c;
    }
    
    public void setClassFile(Configuration c)
    {
        ObjectOutputStream pl=null;
        try {
            pl = new ObjectOutputStream(new FileOutputStream(Main.path+"cfg.mat"));
            pl.writeObject(c);
            pl.flush();
            pl.close();
        } catch (FileNotFoundException ex) {Loggs.loguj("Conf-zapiszklase", ex);}
         catch (IOException ex) { Loggs.loguj("Conf-zapiszklase", ex);}
    }

    public boolean isWelcomeCheck() {
        return welcomeCheck;
    }

    public void setWelcomeCheck(boolean welcomeCheck) {
        this.welcomeCheck = welcomeCheck;
    }

    public String getWelcome1() {
        return welcome1;
    }

    public void setWelcome1(String welcome1) {
        this.welcome1 = welcome1;
    }

    public int getWelcomeCombo() {
        return welcomeCombo;
    }

    public void setWelcomeCombo(int welcomeCombo) {
        this.welcomeCombo = welcomeCombo;
    }

    public String getWelcome2() {
        return welcome2;
    }

    public void setWelcome2(String welcome2) {
        this.welcome2 = welcome2;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public int getDefMask() {
        return defMask;
    }

    public void setDefMask(int defMask) {
        this.defMask = defMask;
    }

    public int getMaxMask() {
        return maxMask;
    }

    public void setMaxMask(int maxMask) {
        this.maxMask = maxMask;
    }

 

    public String getJuniorRcon() {
        return juniorRcon;
    }

    public void setJuniorRcon(String juniorRcon) {
        this.juniorRcon = juniorRcon;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getTime() {
        this.time = new SimpleDateFormat("[MM.dd][HH:mm:ss]");
        return this.time.format(new Date())+" ";
    }
    
    public String getTime(String format) {
        this.time = new SimpleDateFormat(format);
        return this.time.format(new Date())+" ";
    }
    public SimpleDateFormat gettTime() {
        return this.time;
    }

    public void setTime(SimpleDateFormat time) {
        this.time = time;
    }

    public String getExe() {
        return exe;
    }

    public void setExe(String exe) {
        this.exe = exe;
    }

    public int getVisitors() {
        return visitors;
    }
    
    public void addVisitors() {
       this.visitors++;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public int getMrunTime() {
        return mrunTime;
    }

    public void setMrunTime(int mrunTime) {
        this.mrunTime = mrunTime;
    }

    public int getCrashes() {
        return crashes;
    }

    public void setCrashes(int crashes) {
        this.crashes = crashes;
    }

    public boolean isCrash() {
        return crash;
    }

    public void setCrash(boolean crash) {
        this.crash = crash;
    }

    public boolean isCensors() {
        return censors;
    }

    public void setCensors(boolean censors) {
        this.censors = censors;
    }
    
    

}
