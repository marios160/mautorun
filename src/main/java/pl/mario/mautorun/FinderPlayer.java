
package pl.mario.mautorun;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import static pl.mario.mautorun.Main.gui;

public class FinderPlayer extends Thread{
    
    String value;

    public FinderPlayer(String value) {
        this.value = value;
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
            
            List<String> list = new ArrayList<>();
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
            
            List<String> found = new ArrayList<>();
            for (String r : list) {
                if(r.indexOf(value) > -1){
                    
                }
            }
            
            
        } catch (Exception ex) {
            Loggs.loguj("FinderPlayer-run", ex);
        }
    }
    
    
}
