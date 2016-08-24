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
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.data.EventMapper;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class CalendarPane extends AbstractPane {
    private CalendarPane(){}
    
    private static final class Holder {
        private static final CalendarPane INSTANCE = new CalendarPane();
    }
    
    public static CalendarPane getInstance() {
        return Holder.INSTANCE;
    }
    
    public static final String NAME = "calendarpane";
    
    private final JLabel    monthLabel = new JLabel("Month");
    private final JComboBox monthCombo = new JComboBox();
    private final JLabel    yearLabel  = new JLabel("Year");
    private final JComboBox yearCombo  = new JComboBox();
    private final JPanel    menuPanel  = new JPanel();
    private final JPanel    datePanel  = new JPanel();
    
    private final List<JButton> buttonMap = new ArrayList<>();   
        
    public static final String[] MONTHS = { 
        "January", "February", "March", "April", 
        "May", "June", "July", "August", 
        "September", "October", "November", "December" 
    };
    
    public static final String[] DAYS = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };
    
    private static final Color  CURRENT_DAY_COLOR = Color.YELLOW;
    private static final Color  SCHEDULE_COLOR    = new Color(50, 130, 180);
    private static final Color  WEEKDAYS_COLOR    = new Color(100,100,100);
    private static final Color  DEFAULT           = new Color(50,50,50);
    private static final Color  DEFAULT_FG        = Color.WHITE;
    private static final int    MAX               = 42;
    private static final int    MAX_YEAR          = 100;
    private static final int    START_YEAR        = 2000;
    private static final String DAYS_DIMENSION    = "h 60!, w 72!";
    private static final String COMBO_DIMENSION   = "h 35!, w 200!";
    private static final String SPACING           = "gapright 35!";
    private static final String BUTTON_TOOLTIP    = "You have %d event(s) registered on this date.";
       
    private final GregorianCalendar present  = new GregorianCalendar();
    private final Calendar          calendar = Calendar.getInstance();
    
    private final int year  = present.get(Calendar.YEAR);
    private final int month = present.get(Calendar.MONTH);
    private final int date  = present.get(Calendar.DATE);    
    
    private final ItemListener item = (ItemEvent ie) -> {
        if(ie.getStateChange() == ItemEvent.SELECTED) {
            refresh();
        }
    };
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };

    @Override
    public void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new BorderLayout());
       
        initMonthYearPane();
        initDaysPane();
        pack();
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
        menuPanel.setLayout(new MigLayout("wrap 2"));
        menuPanel.add(monthLabel, SPACING);
        menuPanel.add(monthCombo, COMBO_DIMENSION);
        menuPanel.add(yearLabel);
        menuPanel.add(yearCombo,  COMBO_DIMENSION);
        
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
        
        EventMapper map = EventMapper.getInstance();
        List<Integer> dayList = map.getRegisteredDays(MONTHS[m], String.valueOf(y));

        for(int i = start; i < end; i++) {
            int current = i - delta;
            JButton button = buttonMap.get(i);
            button.setText(String.valueOf(current)); 
            button.setEnabled(true);
            
            if(current == date && y == year && m == month) {
                button.setForeground(CURRENT_DAY_COLOR);
            }
            
            if(dayList != null && dayList.contains(current)) {
                button.setBackground(SCHEDULE_COLOR);
                int rows = map.getNumberOfEvents(Utilities.formatDate(y, m+1, current));
                if(rows != 0) {
                    button.setToolTipText(String.format(BUTTON_TOOLTIP, rows));
                }
            }
        }
    }    
    
    private void onTrigger(JButton button) {
        int y = getSelectedYear();
        int m = getSelectedMonth();
        int d = Integer.valueOf(button.getText());
                              
        DisplayPane disp = DisplayPane.getInstance();
        disp.setDate(y, m+1, d);
        disp.refresh();
        
        ViewPane.getInstance().setDate(y, m+1, d);
        FormPane.getInstance().setDate(y, m+1, d);
        MainPane.getInstance().showPane(disp.getName());
    }    
    
    private int getSelectedMonth() {
        return (int)monthCombo.getSelectedIndex();
    }
    
    private int getSelectedYear() {
        return (int)yearCombo.getSelectedItem();
    }
}