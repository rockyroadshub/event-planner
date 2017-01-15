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
package org.rockyroadshub.planner.gui.panes.calendar;

import org.rockyroadshub.planner.gui.core.TButton;
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
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.gui.core.AbstractPane;
import org.rockyroadshub.planner.gui.core.MainFrame;
import org.rockyroadshub.planner.gui.core.GUIUtils;
import org.rockyroadshub.planner.utils.Globals;
import org.rockyroadshub.planner.gui.core.Buttons;
import org.rockyroadshub.planner.loader.Loaders;
import org.rockyroadshub.planner.loader.Property;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
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
    private final JToolBar toolBar = new JToolBar();    
    private PropertiesContainer container;    
    private PropertyLoader properties;
    
    private final ActionListener action = (ActionEvent ae) -> {    
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    }; 
    
    private final String[] buttons = {
        "SAVE",
        "DEFAULT"
    };   
    
    private static final String SAVE_DIALOG      = "Are you sure to save these changes?";
    private static final String DEFAULT_DIALOG   = "Are you sure to revert properties to default?";
    private static final String BORDER           = "Properties";
    
    private void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new BorderLayout());
        
        properties = PropertyLoader.getInstance();
        container  = new PropertiesContainer();
        
        initMenu();
        pack();
        
        GUIUtils.addToPaneList(NAME, this);
    }

    @Override
    public void refresh() {
        container.refresh();
        container.showPane(container.colorMainPane);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void initMenu() {
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
                
        for(String b : buttons) {
            JButton btn = new JButton();
            Buttons atr = Buttons.valueOf(b);          
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setFocusable(false);
            btn.setToolTipText(atr.toolTip());
            btn.setName(atr.toString());
            btn.setIcon(atr.icon());
            btn.addActionListener(action);
            toolBar.add(btn);
        }
    }
    
    private void pack() {
        add(toolBar, BorderLayout.PAGE_START);
        add(container, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder(BORDER));
    }
    
    private void onSave() {
        if(MainFrame.showConfirmDialog(SAVE_DIALOG)) {
            container.getColorButtonCache().stream().map((cb) -> {
                cb.setColor(cb.button.getFillColor());
                return cb;
            }).forEach((cb) -> {
                properties.setProperty(cb.property,cb.button.getFillColor());
            });                             
                
            container.getComboCache().stream().map((cmb) -> {
                cmb.setItemName((String)cmb.combo.getSelectedItem());
                return cmb;
            }).forEach((cmb) -> {
                properties.setProperty(cmb.property,cmb.combo.getSelectedItem());
            });
            
            container.getColorButtonCache().clear();
            container.getComboCache().clear();
            properties.commit();  
            properties.refresh(); 
            refresh();
        }
    }
    
    private void onDefault() {
        if(MainFrame.showConfirmDialog(DEFAULT_DIALOG)) {
            properties.reset();           
            container.reassess();
            properties.refresh();
            refresh();
        } 
    }
    
    private void onTrigger(JButton button) {
        String name = button.getName();
        switch(name) {
            case Globals.SAVE    : onSave();
                                   break;
            case Globals.DEFAULT : onDefault();
                                   break;
        }
    }
    
    enum ColorButtons {
        EVENT(new TButton(), new JLabel("Event Day Color"), Property.CALENDAR_COLOR_EVENTDAY),
        CURRENT(new TButton(), new JLabel("Current Day Color"), Property.CALENDAR_COLOR_CURRENTDAY),
        DEFAULT(new TButton(), new JLabel("Default Day Color"), Property.CALENDAR_COLOR_DEFAULTDAY),
        FOREGROUND(new TButton(), new JLabel("Foreground Color"), Property.CALENDAR_COLOR_FOREGROUND),
        WEEKDAYS(new TButton(), new JLabel("Weekdays Color"), Property.CALENDAR_COLOR_WEEKDAYS),
        ;
        
        final TButton button;
        final JLabel label;
        final Property property;
        Color color;
        
        ColorButtons(TButton button, JLabel label, Property property) {
            this.button = button;
            this.label = label;
            this.property = property;
            this.color = PropertyLoader.getInstance().getColor(property);
            this.button.setColorAttributes(color);
        }
        
        void setColor(Color color) {
            this.color = color;
            this.button.setColorAttributes(color);
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
        private final JToolBar      colorToolBar = new JToolBar();
        private final JColorChooser colorChooser = new JColorChooser();
        
        private final String colorMainPane = "display";
        private final String colorChooserPane = "color";
        private final String back = "back";
             
        private ColorButtons colorButton = null;
        private TButton targetButton;
        
        private final List<ColorButtons> colorButtonCache = new ArrayList<>();
        private final List<ComboBoxes> comboCache = new ArrayList<>();
        
        private final String[][] containerButtons = {
            {"BACK",back},
            {"APPLY",colorMainPane}
        };
        
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
                cb.button.setFillColor(cb.color);
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
            displayPane.setName(colorMainPane);
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
                cb.button.setFillColor(cb.color);
                cb.button.setName(cb.name());
                cb.button.addActionListener(propertiesAction);
                cb.button.setIfBordered(true);
                cb.button.setBorderColor(Color.BLACK);
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
            colorToolBar.setRollover(true); 
            colorToolBar.setFloatable(false);
            
            for(String[] b : containerButtons) {
                JButton btn = new JButton();
                Buttons atr = Buttons.valueOf(b[0]);
                btn.setToolTipText(atr.toolTip());
                btn.setName(b[1]);
                btn.addActionListener(propertiesAction);
                btn.setIcon(atr.icon());
                colorToolBar.add(btn);
            }
            
            colorPane.setLayout(new BorderLayout());
            colorPane.setOpaque(false);
            colorPane.add(colorToolBar, BorderLayout.NORTH);
            colorPane.add(colorChooser, BorderLayout.CENTER);
        }
        
        private void pack() {
            add(displayPane, colorMainPane);
            add(colorPane, colorChooserPane);
        }  
        
        private void buttonTrigger(JButton button) {
            String name = button.getName();
            
            if(button instanceof TButton) {
                colorButton = ColorButtons.valueOf(name);
                name = colorChooserPane;
                targetButton = (TButton)button;
            }
            
            switch(name) {
                case colorMainPane:
                    targetButton.setFillColor(colorChooser.getColor());
                    colorButtonCache.add(colorButton);
                    break;
                case back: 
                    name = colorMainPane;
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