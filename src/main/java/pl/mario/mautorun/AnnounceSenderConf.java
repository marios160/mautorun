/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mario.mautorun;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateusz
 */
public class AnnounceSenderConf extends Thread implements Serializable {

    static public boolean time = true;

    public String a1;
    public String a2;
    public String a3;
    public String a4;
    public String a5;

    public int aMin;

    public boolean aSender;

    public AnnounceSenderConf(String a1, String a2, String a3, String a4, String a5, int aMin, boolean aSender) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.a5 = a5;
        if (aMin <= 0) {
            this.aMin = 0;
            aSender = false;
        } else {
            this.aMin = aMin * 60 * 1000;
            this.aSender = aSender;
        }
    }

    @Override
    public void run() {
        if (aSender) {
            try {
                while (!this.isInterrupted()) {
                    this.sleep(this.aMin - 5000);
                    while (!time) {
                        this.sleep(1000);
                    }
                    time = false;
                    Cmd.message(a1);
                    Cmd.message(a2);
                    Cmd.message(a3);
                    Cmd.message(a4);
                    Cmd.message(a5);
                    this.sleep(5000);
                    time = true;
                }
            } catch (InterruptedException ex) {
                Loggs.loguj("AnnounceSenderConf-run", ex);
            }

        }

    }

}
