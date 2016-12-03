/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mario.mautorun;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Mateusz
 */
public class FakePlayers extends Thread {

    boolean interrupt = false;
    int slots;

    public FakePlayers(int slots) {
        this.slots = slots;
        System.out.println(slots);
        if (slots < 1 || slots > 32) {
            this.slots = Main.srv.getMaxplayers();
        }
    }

    public void run() {
        try {
            while (!interrupt) {
                do {
                    Thread.sleep(3000);
                } while (Main.srv.getNumplayers() > this.slots);
                for (int i = 0; i < this.slots - Main.srv.getNumplayers(); i++) {
                    while (Main.srv.sendHexPck("deadbeefabababababababab0000000005abababbadeabee") == null) {
                    }
                }

            }

        } catch (InterruptedException ex) {
            Loggs.loguj(FakePlayers.class.getName() + "-run", ex);
        }
    }

}
