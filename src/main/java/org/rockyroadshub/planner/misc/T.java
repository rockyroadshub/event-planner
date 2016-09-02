package org.rockyroadshub.planner.misc;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class T extends JFrame {
    static HumanProgressBar p = new HumanProgressBar();
    public T() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(350, 75);
        
        p.setBounds(15, 15, 300, 15);
//        p.setIndeterminate(true);
        this.add(p);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new T();
            }
        });
        
        int i = 0;
        while(i < 100) {
            try {
                i++;
                p.setValue(i);
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(T.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}