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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.core.utils.Utilities;
import org.rockyroadshub.planner.loader.Icons;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.1
 */
@SuppressWarnings("serial")
public final class MainFrame extends JFrame {
    private final JToolBar menuToolBar = new JToolBar();
    
    private final String[] buttons = {
        "HOME",
        "BACK",
        "NEXT",
        "SETTINGS",
        "HELP",
    };
    
    private MainFrame() {
        initialize();
    }
    
    public static MainFrame getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final MainFrame INSTANCE = new MainFrame();
    }  
    
    private void initialize() {        
        SwingUtilities.invokeLater(() -> {
            create();
        });    
    }
    
    private void create() {
        setLayout(new BorderLayout());
        setTitle(Globals.FRAME_TITLE);
        setSize(820, 600);
        setPreferredSize(new Dimension(580, 670));
        addWindowListener(exit);
        setResizable(false);
//        initToolBar();
//        initButtons();
//        add(menuToolBar, BorderLayout.PAGE_START);
        add(MainPane.getInstance(), BorderLayout.CENTER);
        initFrameIcon();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);        
    }
    
    private void initToolBar() {
        menuToolBar.setLayout(new MigLayout(
                Globals.BUTTON_INSETS,
                Globals.BUTTON_GAPX,
                Globals.BUTTON_GAPY));
        menuToolBar.setRollover(true);
    }
    
    private void initButtons() {
        for(String btn : buttons) {
            JButton b = new JButton();
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setFocusable(false);
            b.setIcon(Icons.valueOf(btn).icon());
            menuToolBar.add(b, Globals.BUTTON_DIMENSIONS);
        }
    }
    
    private void initFrameIcon() {
        try {
            setIconImage(Utilities.getBufferedImage(Globals.FRAME_ICON));
        } 
        catch (IOException ex) {
            ex.printStackTrace(System.out);
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
    
    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            Holder.INSTANCE, 
            message, 
            Globals.FRAME_TITLE, 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public static boolean showConfirmDialog(String message) {
        return  
        JOptionPane.showConfirmDialog(
            Holder.INSTANCE, 
            message, 
            Globals.FRAME_TITLE, 
            JOptionPane.OK_CANCEL_OPTION) 
        ==  JOptionPane.OK_OPTION;
    }
}
