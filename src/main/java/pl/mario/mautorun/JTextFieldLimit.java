/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mario.mautorun;

/**
 *
 * @author Mateusz
 */
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class JTextFieldLimit extends PlainDocument {

    private int limit;

    JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
    }

    JTextFieldLimit(int i) {
        super();
        this.limit = i;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}

/*public class Main extends JFrame {
 JTextField textfield1;

 JLabel label1;

 public void init() {
 setLayout(new FlowLayout());
 label1 = new JLabel("max 10 chars");
 textfield1 = new JTextField(15);
 add(label1);
 add(textfield1);
 textfield1.setDocument(new JTextFieldLimit(10));
    
 setSize(300,300);
 setVisible(true);
 }
 }*/
