package pl.mario.mautorun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static pl.mario.mautorun.Main.*;

public class Optimization extends Thread {

    String path;

    public Optimization(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        try {
            List<Struct> list = new ArrayList<>();
            File base = new File(Main.path + path);
            if (!base.exists()) {
                JOptionPane.showMessageDialog(gui, "File " + path + " not found!");
                return;
            }
            Scanner record = new Scanner(base);
            int i = 0;
            while (record.hasNextLine()) {
                list.add(new Struct(record.nextLine(), i));
                i++;
            }
            record.close();

            Collections.sort(list);

            for (int j = 1; j < list.size(); j++) {;
                if (list.get(j).line.equals(list.get(j - 1).line)) {
                    list.remove(j);
                    j--;
                }
            }
            Collections.sort(list, new Struct());
            if (!base.exists()) {
                JOptionPane.showMessageDialog(gui, "File " + path + " not found!");
                return;
            }
            PrintWriter nbase = new PrintWriter(base);
            for (Struct struct : list) {
                nbase.println(struct.line);
            }
            nbase.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Optimization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
