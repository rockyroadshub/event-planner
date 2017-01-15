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
package org.rockyroadshub.planner.gui.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.StringUtils;
import org.rockyroadshub.planner.gui.panes.alarm.AlarmPane;
import org.rockyroadshub.planner.gui.panes.calendar.CalendarPane;
import org.rockyroadshub.planner.gui.panes.calendar.FormPane;
import org.rockyroadshub.planner.gui.panes.calendar.PropertiesPane;
import org.rockyroadshub.planner.utils.Globals;
import org.rockyroadshub.planner.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public final class MainFrame extends JFrame {
    private final MToolBar menuToolBar = new MToolBar();
    
    private final DLabel dateLabel = new DLabel("MMMM dd, yyyy EEEE | hh:mm:ss a ");
    
    private final String[][] buttons0 = {
        {"HOME",     CalendarPane.NAME},
        {"SETTINGS", PropertiesPane.NAME},
        {"EVENT",    FormPane.NAME},
        {"ALARM",    AlarmPane.NAME}
    };
    
    private final String[][] buttons1 = {
        {"HELP",     ""},
        {"ABOUT",    ""}
    };
    
    private final String[][] buttons2 = {
        {"EXIT",     "Exit"}
    };
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        String name = button.getName();
        if(name.equals("Exit")) {
            systemExit();
        }
        else if(!StringUtils.isEmpty(name)) {
            SwingUtilities.invokeLater(() -> {
                CalendarPane cp = CalendarPane.getInstance();
                AbstractPane pane = GUIUtils.getPane(name);
                pane.refresh();
                pane.setDate(cp.getCurrentYear(), cp.getCurrentMonth()+1, cp.getCurrentDay());
                MainPane.getInstance().showPane(name);
            });
        }
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
        initToolBar();
        initButtons();
        add(menuToolBar, BorderLayout.PAGE_START);
        add(MainPane.getInstance(), BorderLayout.CENTER);
        initFrameIcon();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);        
    }
    
    private void initToolBar() {
        menuToolBar.setRollover(true);
    }
    
    private void initButtons() {       
        menuToolBar.addButtons(buttons0, action);
        menuToolBar.add(new JToolBar.Separator(), "FILL");
        menuToolBar.addButtons(buttons1, action);
        menuToolBar.add(Box.createGlue());
        menuToolBar.addButtons(buttons2, action);
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
            systemExit();
        }
    };
    
    private void systemExit() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch (SQLException ex) {}
        finally {
            System.exit(0);
        }
    }
    
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
    
    public static void home() {
        CalendarPane.getInstance().refresh();
        MainPane.getInstance().showPane(CalendarPane.NAME);
    }
}
