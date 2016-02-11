/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mario.mautorun;

import javax.swing.JProgressBar;

/**
 *
 * @author Mateusz
 */
public class CrashBar extends Thread{
    
    @Override
    public void run() {
        for(int i=0; i<1500 && !Main.stopCrashBarLoop; i++)
        {
            try {
                if(Main.stopCrashBarLoop){
                    Main.gui.setCrashbar(0);
                    Main.stopCrashBarLoop = false;
                    return;
                }
                Main.gui.setCrashbar(i);
                Thread.sleep(4);
            } catch (InterruptedException ex) {
                
            }
        }
    }
    
}
