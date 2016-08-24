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
package org.rockyroadshub.planner.core.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.apache.logging.log4j.core.util.FileUtils;
import org.rockyroadshub.planner.core.utils.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Frame extends JFrame {
    private Frame() {}
    
    public static Frame getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final Frame INSTANCE = new Frame();
    }  
    
    public void initialize() {
        try {
            File[] files = new File(Globals.ICONS_PATH).listFiles();
            for(File file : files) {
                Image img = ImageIO.read(file);
                int index = file.getName().lastIndexOf('.');
                String name = file.getName().substring(0, index);
                Globals.ICONS.put(name, new ImageIcon(img)); 
            }
        } 
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        MainPane panel = MainPane.getInstance();
        panel.initialize();
        
        setLayout(new BorderLayout());   
        setTitle(Globals.FRAME_TITLE);
        setSize(800, 600);
        setPreferredSize(new Dimension(580, 650));
        addWindowListener(exit);
        setResizable(false);
        setVisible(true);
        add(panel, BorderLayout.CENTER);
        initIcon();
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initIcon() {
        try(InputStream in = getClass().getResourceAsStream(Globals.FRAME_ICON)) {
            BufferedImage img = ImageIO.read(in);
            setIconImage(img);
        } 
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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
}
