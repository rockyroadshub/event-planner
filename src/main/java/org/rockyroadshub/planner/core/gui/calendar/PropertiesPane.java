package org.rockyroadshub.planner.core.gui.calendar;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.gui.AbstractPane;
import org.rockyroadshub.planner.core.gui.MainFrame;
import org.rockyroadshub.planner.core.gui.GUIUtils;
import org.rockyroadshub.planner.loader.IconLoader;
import org.rockyroadshub.planner.core.gui.MainPane;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.loader.Property;
import org.rockyroadshub.planner.loader.PropertyLoader;

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

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.1.0
 */
@SuppressWarnings("serial")
public final class PropertiesPane extends AbstractPane {

    private PropertiesPane() {
        initialize();
    }

    public static PropertiesPane getInstance() {
        return Holder.INSTANCE;
    }
    
    private static class Holder {
        private static final PropertiesPane INSTANCE = new PropertiesPane();
    }

    public static final String NAME = "propertiespane";
    
    private final JPanel        menuPanel        = new JPanel();
    private final JButton       homeButton       = new JButton();
    private final JButton       backButton       = new JButton();
    private final JButton       saveButton       = new JButton();
    private final JButton       defaultButton    = new JButton();
    
    private PropertiesContainer container;
    
    private IconLoader iconLoader;
    private PropertyLoader properties;
    
    private final ActionListener action = (ActionEvent ae) -> {    
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };    
    
    private static final String SAVE_DIALOG      = "Are you sure to save these changes?";
    private static final String DEFAULT_DIALOG   = "Are you sure to revert properties to default?";
    private static final String BORDER           = "Properties";
    
    private void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new BorderLayout());
        
        iconLoader = IconLoader.getInstance();
        properties = PropertyLoader.getInstance();
        container  = new PropertiesContainer();
        
        initButtons();
        initMenu();
        pack();
        
        GUIUtils.addToPaneList(this);
    }

    @Override
    public void refresh() {
        properties.refresh();
        container.refresh();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void initButtons() {
        homeButton.setToolTipText(Globals.HOME);
        homeButton.setName(CalendarPane.NAME);
        homeButton.addActionListener(action);
        homeButton.setIcon(iconLoader.get(Globals.HOME));
        
        backButton.setToolTipText(Globals.BACK);
        backButton.setName(Globals.BACK);
        backButton.addActionListener(action);
        backButton.setIcon(iconLoader.get(Globals.BACK));
        
        saveButton.setToolTipText(Globals.SAVE);
        saveButton.setName(Globals.SAVE);
        saveButton.addActionListener(action);
        saveButton.setIcon(iconLoader.get(Globals.SAVE));
        
        defaultButton.setToolTipText(Globals.DEFAULT);
        defaultButton.setName(Globals.DEFAULT);
        defaultButton.addActionListener(action);
        defaultButton.setIcon(iconLoader.get(Globals.DEFAULT));
    }
    
    private void initMenu() {
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new MigLayout());
        menuPanel.add(homeButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(backButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(saveButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(defaultButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.setBorder(BorderFactory.createTitledBorder(BORDER));
    }
    
    private void pack() {
        add(menuPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }
    
    private void onSave() {
        MainFrame  f = MainFrame.getInstance();
        String m = SAVE_DIALOG;
        String t = Globals.FRAME_TITLE;
        int    q = JOptionPane.OK_CANCEL_OPTION;
        int    o = JOptionPane.OK_OPTION;
        if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
            properties.setProperty(Property.CALENDAR_COLOR_EVENTDAY,
                container.eventColor.getBackground());
            properties.setProperty(Property.CALENDAR_COLOR_CURRENTDAY,
                container.currentColor.getBackground());
            
            this.refresh();
        }
    }
    
    private void onDefault() {
        MainFrame  f = MainFrame.getInstance();
        String m = DEFAULT_DIALOG;
        String t = Globals.FRAME_TITLE;
        int    q = JOptionPane.OK_CANCEL_OPTION;
        int    o = JOptionPane.OK_OPTION;
        if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
            properties.setProperty(Property.CALENDAR_COLOR_EVENTDAY,
                new Color(50,130,180));
            properties.setProperty(Property.CALENDAR_COLOR_CURRENTDAY,
                Color.YELLOW);
        
            this.refresh();
        } 
    }
    
    private void onTrigger(JButton button) {
        String name = button.getName();
        switch(name) {
            case CalendarPane.NAME:
                CalendarPane.getInstance().refresh();
                MainPane.getInstance().showPane(name);
                container.showPane(PropertiesContainer.DISPLAY_PANE);
                container.refresh();
                break;
            case Globals.BACK:
                container.showPane(PropertiesContainer.DISPLAY_PANE);
                break;
            case Globals.SAVE:
                onSave();
                break;
            case Globals.DEFAULT:
                onDefault();
                break;
        }
    }
    
    private class PropertiesContainer extends AbstractPane {
        private static final String NAME = "propertiescontainer";
        
        private final JPanel     displayPane  = new JPanel();
        private final JLabel     calendar     = new JLabel("Calendar Properties");
        private final JSeparator separator    = new JSeparator();
        private final JButton    eventColor   = new JButton();
        private final JLabel     eventLabel   = new JLabel("Event Day Color");
        private final JButton    currentColor = new JButton();
        private final JLabel     currentLabel = new JLabel("Current Day Color");
        
        private final JPanel        colorPane    = new JPanel();
        private final JPanel        colorMenu    = new JPanel();
        private final JButton       setButton    = new JButton();
        private final JColorChooser colorChooser = new JColorChooser();
        
        private static final String COLOR_PANE            = "color";
        private static final String DISPLAY_PANE          = "display";
        private static final String COLOR_CHOOSER_EVENT   = "eventcolor";
        private static final String COLOR_CHOOSER_CURRENT = "currentcolor";
        
        private final ActionListener propertiesAction = (ActionEvent ae) -> {    
            JButton button = (JButton)ae.getSource();
            displayTrigger(button);
        };    
        
        private final CardLayout layout = new CardLayout();
        
        private JButton target;
        
        private PropertiesContainer() {
            initialize();
        }
       
        private void initialize() {
            setOpaque(false);
            setName(NAME);
            setLayout(layout);
            
            
            initDisplayPane();
            initColorPane();
            pack();
        }
               
        @Override
        public void refresh() {
            eventColor.setBackground(properties.calendar_color_eventday);
            currentColor.setBackground(properties.calendar_color_currentday);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        private void initDisplayPane() {
            displayPane.setLayout(new MigLayout(new LC().fillX()));
            displayPane.add(calendar, "wrap");
            displayPane.add(separator, "growx, wrap");
            displayPane.add(eventColor, Globals.BUTTON_DIMENSIONS + ",split 2");
            displayPane.add(eventLabel, "wrap");
            displayPane.add(currentColor, Globals.BUTTON_DIMENSIONS + ",split 2");
            displayPane.add(currentLabel,"wrap");
            
            eventColor.setBackground(properties.calendar_color_eventday);
            eventColor.setName(COLOR_CHOOSER_EVENT);      
            eventColor.addActionListener(propertiesAction);
            
            currentColor.setBackground(properties.calendar_color_currentday);
            currentColor.setName(COLOR_CHOOSER_CURRENT);
            currentColor.addActionListener(propertiesAction);
        }
        
        private void initColorPane() {
            setButton.setToolTipText(Globals.SET);
            setButton.setName(Globals.SET);
            setButton.addActionListener(propertiesAction);
            setButton.setIcon(iconLoader.get(Globals.SET));
            colorMenu.setLayout(new MigLayout());
            colorMenu.setOpaque(false);            
            colorMenu.add(setButton, Globals.BUTTON_DIMENSIONS);
            
            colorPane.setLayout(new BorderLayout());
            colorMenu.setOpaque(false);
            colorPane.add(colorMenu, BorderLayout.NORTH);
            colorPane.add(colorChooser, BorderLayout.CENTER);
        }
        
        private void pack() {
            add(displayPane, DISPLAY_PANE);
            add(colorPane, COLOR_PANE);
        }  
        
        private void displayTrigger(JButton button) {
            String name = button.getName();
            switch(name) {
                case COLOR_CHOOSER_EVENT:
                    showPane(COLOR_PANE);
                    target = button;
                    break;
                case COLOR_CHOOSER_CURRENT:
                    showPane(COLOR_PANE);
                    target = button;
                    break;
                case Globals.SET:
                    target.setBackground(colorChooser.getColor());
                    showPane(DISPLAY_PANE);
                    break;
            }
        }
        
        private void showPane(String name) {
            layout.show(this, name);
        }
    }
}