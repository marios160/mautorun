/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mario.mautorun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Mateusz
 */
public class Web extends Thread {
 
    public static void visitHTML(String adres)
    {
        BufferedReader br = null;
        try {
            StringBuffer content = new StringBuffer();
            URL url = null;
            
            url = new URL(adres);

            br = new BufferedReader(new InputStreamReader(url.openStream()));
            if (br != null) {
                br.close();
            }
        } catch (MalformedURLException ex) {
            Loggs.loguj("HtmlReader-visitHTML", ex); 
        } catch (IOException ex) {
            Loggs.loguj("HtmlReader-visitHTML", ex);
        }

    }
    
   public static String readHTML(String adres)
   {
        StringBuffer content = new StringBuffer();
        try {
            
            URL url = null;
            
            url = new URL(adres);
            BufferedReader br = null;
            
            br = new BufferedReader(new InputStreamReader(url.openStream()));
 
            String line = null;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            if (br != null) {
                
                br.close();    
            }
        } catch (IOException ex) {
            Loggs.loguj("HtmlReader-readHTML", ex);
        }
         return content.toString();
    }
   
   public static String pobierzIP()
   {
       String ip = null; 
       visitHTML("http://www.igi2.xaa.pl/mautorun/mautorunip.php");
       ip = readHTML("http://www.igi2.xaa.pl/mautorun/ip.txt");
       visitHTML("http://www.igi2.xaa.pl/mautorun/mautorunip.php?usun=1");
       return ip.trim();
   }
 
}

