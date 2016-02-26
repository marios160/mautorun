
package pl.mario.mautorun;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static pl.mario.mautorun.Main.*;

public class Optimization extends Thread{

    public Optimization() {
    }

    @Override
    public void run() {
        try {
            List<String> list = new ArrayList<>();
            List<String> newlist = new ArrayList<>();
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
            
            for (int i = 0; i < list.size(); i++) {
                for (int j = list.size(); j > 0; j++) {
                    
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Optimization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
