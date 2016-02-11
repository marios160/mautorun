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
    
    
    
    public static void loguj(String fun, Exception message)
    {
        String mes = "";
        for (StackTraceElement stack : message.getStackTrace()) {
            mes+=stack.toString()+"\n";
        }
        try {
            PrintWriter log = new PrintWriter(new FileWriter("MautorunExeption.log",true));
            log.append(Main.conf.getTime()+fun.toUpperCase()+": "+message.getMessage()+"\n"+mes+"\n");
            log.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: "+ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: "+ex);
        }
        System.out.println(Main.conf.getTime()+fun.toUpperCase()+": "+message.getMessage()+"\n"+mes);
        
    }
    
    public static void loguj(String fun, Exception message, String msg)
    {
        String mes = "";
        for (StackTraceElement stack : message.getStackTrace()) {
            mes+=stack.toString()+"\n";
        }
        try {
            PrintWriter log = new PrintWriter(new FileWriter("MautorunExeption.log",true));
            log.append(Main.conf.getTime()+fun.toUpperCase()+": "+message.getMessage()+"\n"+mes+"\n"+msg+"\n");
            log.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: "+ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Loguj: "+ex);
        }
        System.out.println(Main.conf.getTime()+fun.toUpperCase()+": "+message.getMessage()+"\n"+mes+"\n"+msg+"\n");
        
    }
            
    
}
