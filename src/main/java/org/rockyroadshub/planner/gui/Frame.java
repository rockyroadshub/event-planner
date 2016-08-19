/*
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
