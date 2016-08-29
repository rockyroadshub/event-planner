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

package org.rockyroadshub.planner.core.gui.calendar;

import org.rockyroadshub.planner.core.utils.Utilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.data.EventMapper;
import org.rockyroadshub.planner.core.gui.AbstractPane;
import org.rockyroadshub.planner.core.gui.Frame;
import org.rockyroadshub.planner.core.gui.GUIUtils;
import org.rockyroadshub.planner.system.IconLoader;
import org.rockyroadshub.planner.core.gui.MainPane;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.system.Properties;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
@SuppressWarnings("serial")
public final class CalendarPane extends AbstractPane {
    private CalendarPane(){
        initialize();
    }
    
    private static final class Holder {
        private static final CalendarPane INSTANCE = new CalendarPane();
    }
    
    public static CalendarPane getInstance() {
        return Holder.INSTANCE;
    }
    
    public static final String NAME = "calendarpane";
    
    private final JLabel    monthLabel     = new JLabel("Month");
    private final JComboBox monthCombo     = new JComboBox();
    private final JLabel    yearLabel      = new JLabel("Year");
    private final JComboBox yearCombo      = new JComboBox();
    private final JPanel    menuPanel      = new JPanel();
    private final JPanel    buttonsPanel   = new JPanel();
    private final JPanel    datePanel      = new JPanel();
    private final JButton   settingsButton = new JButton();
    private final JButton   deleteButton   = new JButton();
    
    private final List<JButton> buttonMap  = new ArrayList<>();   
    private final List<JButton> weekdayMap = new LinkedList<>();
        
    public static final String[] MONTHS = { 
        "January", "February", "March", "April", 
        "May", "June", "July", "August", 
        "September", "October", "November", "December" 
    };
    
    public static final String[] DAYS = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };
    
    private static final Color  WEEKDAYS_COLOR    = new Color(100,100,100);
    private static final Color  DEFAULT           = new Color(50,50,50);
    private static final Color  DEFAULT_FG        = Color.WHITE;
    private static final int    MAX               = 42;
    private static final int    MAX_YEAR          = 100;
    private static final int    START_YEAR        = 2000;
    private static final String DAYS_DIMENSION    = "h 60!, w 72!";
    private static final String COMBO_DIMENSION   = "h 35!, w 200!";
    private static final String SPACING           = "gapright 35!";
    private static final String WEEKDAY_BUTTON    = "weekdaybutton";
    private static final String DAY_BUTTON        = "daybutton";
    private static final String BUTTON_TOOLTIP    = "You have %d event(s) registered on this date.";
    private static final String BORDER            = "Calendar";
       
    private final GregorianCalendar present  = new GregorianCalendar();
    private final Calendar          calendar = Calendar.getInstance();
    
    private final int year  = present.get(Calendar.YEAR);
    private final int month = present.get(Calendar.MONTH);
    private final int date  = present.get(Calendar.DATE);    
    
    private static final String DELETE_DIALOG = "Delete all events from %s %s?";

    
    private IconLoader iconLoader;
    private Properties properties;
    
    private final ItemListener item = (ItemEvent ie) -> {
        if(ie.getStateChange() == ItemEvent.SELECTED) {
            refresh();
        }
    };
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };

    private void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new BorderLayout());
       
        iconLoader = IconLoader.getInstance();
        properties = Properties.getInstance();
        
        initButtonsPane();
        initMonthYearPane();
        initDaysPane();
        pack();
        
        GUIUtils.addToPaneList(this);
    }

    @Override
    public void refresh() {
        clear();
        int y = getSelectedYear();
        int m = getSelectedMonth();
        calendar.clear();
        calendar.set(y, m, 1);
        printDates(calendar);
    }

    @Override
    public void clear() {
        for(int i = 0; i < MAX; i++) {
            JButton button = buttonMap.get(i);
            button.setText(null);
            button.setBackground(DEFAULT);
            button.setForeground(DEFAULT_FG);
            button.setToolTipText(null);
            button.setEnabled(false);
        }
    }
    
    private void initMonthYearPane() {
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new MigLayout("wrap 3"));
        menuPanel.add(monthLabel,   SPACING);
        menuPanel.add(monthCombo,   COMBO_DIMENSION);
        menuPanel.add(buttonsPanel, SPACING);
        menuPanel.add(yearLabel);
        menuPanel.add(yearCombo,    COMBO_DIMENSION);
        menuPanel.setBorder(BorderFactory.createTitledBorder(BORDER));
        
        initComboYear();
        initComboMonth();
    }
    
    private void initComboYear() {
       for(int i = 0; i < MAX_YEAR; i++) {
            yearCombo.addItem(START_YEAR + i);
        }       
        yearCombo.addItemListener(item);
    }
    
    private void initComboMonth() {
        for(String i : MONTHS) {
            monthCombo.addItem(i);
        }        
        monthCombo.addItemListener(item);
    }
           
    private void initDaysPane() {
        datePanel.setOpaque(false);
        datePanel.setLayout(new MigLayout(new LC().fill()));
        
        initWeekDays();
        initDays();
    }
    
    private void initWeekDays() {
        for(int i = 0; i < DAYS.length; i++) {
            JButton button = new JButton(DAYS[i]);
            button.setBackground(WEEKDAYS_COLOR);
            button.setForeground(DEFAULT_FG);
            button.addActionListener(action);
            button.setName(WEEKDAY_BUTTON);
            weekdayMap.add(button);
            
            if((i+1) % 7 == 0) {
                datePanel.add(button, DAYS_DIMENSION + ", wrap");
            }
            else {
                datePanel.add(button, DAYS_DIMENSION);
            }
        }
    }
    
    private void initDays() {
        for(int i = 0; i < MAX; i++) {   
            JButton button = new JButton();
            button.setForeground(DEFAULT_FG);
            button.setBackground(DEFAULT);
            button.addActionListener(action);
            button.setEnabled(false);
            button.setName(DAY_BUTTON);
            buttonMap.add(button);
            
            if((i+1) % 7 == 0) {
                datePanel.add(button, DAYS_DIMENSION + ", wrap");
            }
            else {
                datePanel.add(button, DAYS_DIMENSION);
            }
        }
        
        calendar.clear();
        calendar.set(year, month, 1);
        printDates(calendar);
    }    
    
    private void initButtonsPane() {
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new MigLayout());
        buttonsPanel.add(settingsButton, Globals.BUTTON_DIMENSIONS);
        buttonsPanel.add(deleteButton, Globals.BUTTON_DIMENSIONS);
        initButtons();
    }
    
    private void initButtons() {
        settingsButton.setToolTipText(Globals.SETTINGS);
        settingsButton.setName(PropertiesPane.NAME);
        settingsButton.addActionListener(action);
        settingsButton.setIcon(iconLoader.getIcon(Globals.SETTINGS));
        
        deleteButton.setToolTipText(Globals.DELETE);
        deleteButton.setName(Globals.DELETE);
        deleteButton.addActionListener(action);
        deleteButton.setIcon(iconLoader.getIcon(Globals.DELETE));
    }
    
    private void pack() {
        monthCombo.setSelectedIndex(month);
        yearCombo.setSelectedItem(year);
        add(menuPanel, BorderLayout.NORTH);
        add(datePanel, BorderLayout.CENTER);
    }
    
    private void printDates(Calendar cal) {
        int s = cal.get(Calendar.DAY_OF_WEEK);
        int e = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int y = getSelectedYear();
        int m = getSelectedMonth();
        
        int start = s - 1;
        int end   = s + e - 1;
        int delta = start - 1;
        
        int monthBefore = m - 1;
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, monthBefore, 1);   
        int daysBefore = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int max = daysBefore - start;
        int p = 0;
                                     
        if(s == 1) {              
            for(int i = daysBefore - 7; i < daysBefore; i++) {
                JButton button = buttonMap.get(p);
                button.setText((String.valueOf(i+1)));
                p++;
            }
        }
        else {
            for(int i = 0; i < start; i++) {
                JButton button = buttonMap.get(i);
                button.setText(String.valueOf(max + i + 1));
            }     
        }
                
        EventMapper map = EventMapper.getInstance();
        List<Integer> dayList = map.getRegisteredDays(MONTHS[m], String.valueOf(y));

        for(int i = start + p; i < end + p; i++) {
            int current = i - delta - p;
            JButton button = buttonMap.get(i);
            button.setText(String.valueOf(current)); 
            button.setEnabled(true);
            
            if(current == date && y == year && m == month) {
                button.setForeground(properties.getColor(Properties.COLOR_CURRENTDAY));
            }
            
            if(dayList != null && dayList.contains(current)) {
                button.setBackground(properties.getColor(Properties.COLOR_EVENTDAY));
                int rows = map.getNumberOfEvents(Utilities.formatDate(y, m+1, current));
                if(rows != 0) {
                    button.setToolTipText(String.format(BUTTON_TOOLTIP, rows));
                }
            }
        }
        
        for(int i = end + p, j = 1; i < MAX; i++) {
            JButton button = buttonMap.get(i);
            button.setText(String.valueOf(j));             
            j++;
        }
    }    
    
    private void onTrigger(JButton button) {
        String name = button.getName();
        switch (name) {
            case PropertiesPane.NAME:
                MainPane.getInstance().showPane(name);
                break;
            case WEEKDAY_BUTTON:
                onWeekday();
                break;
            case Globals.DELETE:
                onDelete();
                break;
            default:
                int y = getSelectedYear();
                int m = getSelectedMonth();
                int d = Integer.valueOf(button.getText());
                DisplayPane disp = DisplayPane.getInstance();
                disp.setDate(y, m+1, d);
                disp.refresh();
                ViewPane.getInstance().setDate(y, m+1, d);
                FormPane.getInstance().setDate(y, m+1, d);
                MainPane.getInstance().showPane(disp.getName());
                break;
        }
    }   
    
    private void onWeekday() {
        
    }
    
    private void onDelete() {
        String y0 = String.valueOf(getSelectedYear());
        String m0 = (String)monthCombo.getSelectedItem();
        Frame  f = Frame.getInstance();
        String m = String.format(DELETE_DIALOG, m0, y0);
        String t = Globals.FRAME_TITLE;
        int    q = JOptionPane.OK_CANCEL_OPTION;
        int    o = JOptionPane.OK_OPTION;
        if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
            EventMapper map = EventMapper.getInstance();
            map.deleteAll(m0,y0);
            refresh();
        }      
    }
    
    private int getSelectedMonth() {
        return (int)monthCombo.getSelectedIndex();
    }
    
    private int getSelectedYear() {
        return (int)yearCombo.getSelectedItem();
    }
}