/*
 * The MIT License
 *
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Event Planner"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Event Planner.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rockyroadshub.planner.gui;

import com.jcabi.aspects.LogExceptions;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.rockyroadshub.planner.core.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class Frame extends JFrame {
    private Frame() {}
    
    public final void initialize() {
        Panel panel = Panel.getInstance();
        setLayout(new BorderLayout());   
        setTitle("Event Planner");
        setSize(800, 600);
        setPreferredSize(new Dimension(580, 650));
        addWindowListener(exit);
        setResizable(false);
        setVisible(true);
        try {
            initIcon("src/calendar-icon.png");
        }catch (IOException ex) {}
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }
    
    @LogExceptions
    private void initIcon(String n) throws IOException {
        Image img = ImageIO.read(getClass().getResource(String.format(Globals.JAR_ROOT, n)));
        setIconImage(img);
    }
    
    private final WindowListener exit = new WindowAdapter(){
        @Override
        public void windowClosing(WindowEvent we) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            }
            catch (SQLException ex) {}
            finally {
                System.exit(0);
            }
        }
    };
    
    public static Frame getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final Frame INSTANCE = new Frame();
    }  
}
