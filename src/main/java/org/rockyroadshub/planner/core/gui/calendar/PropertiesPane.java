package org.rockyroadshub.planner.core.gui.calendar;

import org.rockyroadshub.planner.core.gui.CButton;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
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
import org.rockyroadshub.planner.loader.Loaders;
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
 * @since 0.2.0
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
            for(ColorButtons cb : container.getColorButtonCache()) {
                cb.setColor(cb.button.getBackground());
                properties.setProperty(cb.property,cb.button.getBackground());
            }                             
                
            for(ComboBoxes cmb : container.getComboCache()) {
                cmb.setItemName((String)cmb.combo.getSelectedItem());
                properties.setProperty(cmb.property,cmb.combo.getSelectedItem());
            }
            
            container.getColorButtonCache().clear();
            container.getComboCache().clear();
            properties.commit();   
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
            properties.reset();           
            container.reassess();
            this.refresh();
        } 
    }
    
    private void onTrigger(JButton button) {
        String name = button.getName();
        switch(name) {
            case CalendarPane.NAME:
                CalendarPane cp = CalendarPane.getInstance();
                cp.refresh();
                cp.refreshWeekdays();
                MainPane.getInstance().showPane(name);
                container.showPane(Globals.SET);
                container.refresh();
                break;
            case Globals.BACK:
                container.showPane(Globals.SET);
                break;
            case Globals.SAVE:
                onSave();
                break;
            case Globals.DEFAULT:
                onDefault();
                break;
        }
    }
    
    enum ColorButtons {
        EVENT(new CButton(), new JLabel("Event Day Color"), Property.CALENDAR_COLOR_EVENTDAY),
        CURRENT(new CButton(), new JLabel("Current Day Color"), Property.CALENDAR_COLOR_CURRENTDAY),
        DEFAULT(new CButton(), new JLabel("Default Day Color"), Property.CALENDAR_COLOR_DEFAULTDAY),
        FOREGROUND(new CButton(), new JLabel("Foreground Color"), Property.CALENDAR_COLOR_FOREGROUND),
        WEEKDAYS(new CButton(), new JLabel("Weekdays Color"), Property.CALENDAR_COLOR_WEEKDAYS),
        ;
        
        final CButton button;
        final JLabel label;
        final Property property;
        Color color;
        
        ColorButtons(CButton button, JLabel label, Property property) {
            this.button = button;
            this.label = label;
            this.property = property;
            this.color = PropertyLoader.getInstance().getColor(property);
        }
        
        void setColor(Color color) {
            this.color = color;
        }
    }
    
    enum ComboBoxes {
        ICON(new JLabel("Icon Themes"), Property.CALENDAR_ICON_THEME, "icon")
        ;
        
        final JLabel label;
        final Property property;
        final JComboBox combo;
        String itemName;
        
        ComboBoxes(JLabel label, Property property, String loaderName) {
            this.label = label;
            this.property = property;
            this.itemName = PropertyLoader.getInstance().getString(property);
            this.combo = new JComboBox(Loaders.getLoader(loaderName).getComboItems());
        }
        
        void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }
    
    private class PropertiesContainer extends AbstractPane {
        private static final String NAME = "propertiescontainer";
        
        private final JPanel     displayPane     = new JPanel();
        private final JLabel     calendarColors  = new JLabel("Colors");
        private final JSeparator separator0      = new JSeparator();
        private final JLabel     calendarTheme   = new JLabel("Themes");
        private final JSeparator separator1      = new JSeparator();
        
        private final JPanel        colorPane    = new JPanel();
        private final JPanel        colorMenu    = new JPanel();
        private final JButton       setButton    = new JButton();
        private final JColorChooser colorChooser = new JColorChooser();
        
        private static final String COLOR_PANE   = "color";
             
        private ColorButtons colorButton = null;
        private JButton targetButton;
        
        private final List<ColorButtons> colorButtonCache = new ArrayList<>();
        private final List<ComboBoxes> comboCache = new ArrayList<>();
        
        private final ActionListener propertiesAction = (ActionEvent ae) -> {    
            JButton button = (JButton)ae.getSource();
            buttonTrigger(button);
        };    
        
        private final ItemListener propertiesItem = (ItemEvent ie) -> {    
            JComboBox combo = (JComboBox)ie.getSource();
            comboTrigger(combo);
        };    
        
        private final CardLayout layout = new CardLayout();
                
        private PropertiesContainer() {
            initialize();
        }
       
        private void initialize() {
            setOpaque(false);
            setName(NAME);
            setLayout(layout);           
            
            initColorPane();
            initDisplayPane();
            pack();
        }
               
        @Override
        public void refresh() {
            for(ColorButtons cb : ColorButtons.values()) {
                cb.button.setBackground(cb.color);
            }
            
            for(ComboBoxes cmb : ComboBoxes.values()) {
                cmb.combo.setSelectedItem(cmb.itemName);
            }
        }
        
        public void reassess() {
            for(ColorButtons cb : ColorButtons.values()) {
                cb.setColor(properties.getColor(cb.property));
            }
            
            for(ComboBoxes cmb : ComboBoxes.values()) {
                cmb.setItemName(properties.getString(cmb.property));
            }
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        private void initDisplayPane() {
            displayPane.setName(Globals.SET);
            displayPane.setLayout(new MigLayout(new LC().fillX()));
            initColorButtons();
            initComboBoxes();
        }
      
        private void initColorButtons() {
            displayPane.add(calendarColors, "wrap");
            displayPane.add(separator0, "growx, wrap");
            for(ColorButtons cb : ColorButtons.values()) {
                displayPane.add(cb.button, Globals.BUTTON_DIMENSIONS + ",split 2");
                displayPane.add(cb.label, "wrap");
                cb.button.setBackground(cb.color);
                cb.button.setName(cb.name());
                cb.button.addActionListener(propertiesAction);
            }
        }
        
        private void initComboBoxes() {
            displayPane.add(calendarTheme, "gaptop 30!, wrap");
            displayPane.add(separator1, "growx, wrap");            
            for(ComboBoxes cmb : ComboBoxes.values()) {
                displayPane.add(cmb.label, "split 2");
                displayPane.add(cmb.combo, "h 20!, w 200!, wrap");
                cmb.combo.addItemListener(propertiesItem);
                cmb.combo.setName(cmb.name());
                cmb.combo.setSelectedItem(cmb.itemName);
            }
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
            colorPane.setOpaque(false);
            colorPane.add(colorMenu, BorderLayout.NORTH);
            colorPane.add(colorChooser, BorderLayout.CENTER);
        }
        
        private void pack() {
            add(displayPane, Globals.SET);
            add(colorPane, COLOR_PANE);
        }  
        
        private void buttonTrigger(JButton button) {
            String name = button.getName();
            
            if(button instanceof CButton) {
                colorButton = ColorButtons.valueOf(name);
                name = COLOR_PANE;
                targetButton = button;
            }
            
            switch(name) {
                case Globals.SET:
                    targetButton.setBackground(colorChooser.getColor());
                    colorButtonCache.add(colorButton);
                    break;
            }
            
            showPane(name);
        }
        
        private void comboTrigger(JComboBox combo) {
            comboCache.add(ComboBoxes.valueOf(combo.getName()));    
        }
        
        private void showPane(String name) {
            layout.show(this, name);
        }
        
        List<ColorButtons> getColorButtonCache() {
            return colorButtonCache;
        }
        
        List<ComboBoxes> getComboCache() {
            return comboCache;
        }
    }
}