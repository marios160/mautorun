/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mario.mautorun;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Mateusz
 */
public class Loggs {

    public Loggs() {

        try {
            PrintWriter log = new PrintWriter("MautorunExeption.log");
            log.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loggs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loguj(String fun, Exception message) {
        String mes = message.getMessage()+"\n";
        mes += message.getLocalizedMessage()+"\n";
        for (StackTraceElement stack : message.getStackTrace()) {
            mes += stack.toString() + "\n";
        }
        try {
            PrintWriter log = new PrintWriter(new FileWriter("MautorunExeption.log", true));
            log.append(zakoduj(Main.conf.getTime() + fun.toUpperCase() + ": " + message.getMessage() + "\n" + mes) + "\n\n");
            log.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: " + ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: " + ex);
        }
        System.out.println(Main.conf.getTime() + fun.toUpperCase() + ": " + message.getMessage() + "\n" + mes);

    }

    public static void loguj(String fun, Exception message, String msg) {
        String mes = "";
        for (StackTraceElement stack : message.getStackTrace()) {
            mes += stack.toString() + "\n";
        }
        try {
            PrintWriter log = new PrintWriter(new FileWriter("MautorunExeption.log", true));
            log.append(zakoduj(Main.conf.getTime() + fun.toUpperCase() + ": " + message.getMessage() + "\n" + mes + "\n" + msg) + "\n\n");
            log.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: " + ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: " + ex);
        }
        System.out.println(Main.conf.getTime() + fun.toUpperCase() + ": " + message.getMessage() + "\n" + mes + "\n" + msg + "\n");

    }

    public static String zakoduj(String txt) {
        char[] tab = txt.toCharArray();
        char[] tab2 = new char[txt.length() * 3];

        for (int i = 0, j = tab2.length - 1; i < txt.length(); i++, j -= 3) {
            int x = (int) tab[i];
            x = (x + 70) * 5;
            String l = Integer.toString(x);
            char[] tab3 = l.toCharArray();
            tab2[j] = tab3[0];
            tab2[j - 1] = tab3[1];
            tab2[j - 2] = tab3[2];
        }
        return new String(tab2);
    }

}
