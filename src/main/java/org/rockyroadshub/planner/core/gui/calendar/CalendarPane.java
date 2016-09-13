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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.data.EventMapper;
import org.rockyroadshub.planner.core.gui.AbstractPane;
import org.rockyroadshub.planner.core.gui.DLabel;
import org.rockyroadshub.planner.core.gui.MainFrame;
import org.rockyroadshub.planner.core.gui.GUIUtils;
import org.rockyroadshub.planner.loader.IconLoader;
import org.rockyroadshub.planner.core.gui.MainPane;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
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
    
    private static final int    MAX               = 42;
    private static final int    MAX_YEAR          = 100;
    private static final int    START_YEAR        = 2000;
    private static final String COMBO_DIMENSION   = "h 35!, w 200!";
    private static final String SPACING           = "gapright 15!";
    private static final String WEEKDAY_BUTTON    = "weekdaybutton";
    private static final String DAY_BUTTON        = "daybutton";
    private static final String BUTTON_TOOLTIP    = "You have %d event(s) registered on this date.";
    private static final String BORDER            = "Calendar";
    
    private final JLabel    monthLabel     = new JLabel("Month");
    private final JComboBox monthCombo     = new JComboBox();
    private final JLabel    yearLabel      = new JLabel("Year");
    private final JComboBox yearCombo      = new JComboBox();
    private final JPanel    menuPanel      = new JPanel();
    private final JPanel    buttonsPanel   = new JPanel();
    private final JPanel    daysPanel      = new JPanel();
    private final JButton   settingsButton = new JButton();
    private final JButton   deleteButton   = new JButton();
    private final JPanel    dateMenu       = new JPanel();
    private final DLabel    dateLabel      = new DLabel("MMMM dd, yyyy");
    private final DLabel    timeLabel      = new DLabel("hh:mm:ss a");
    private final DLabel    dayLabel       = new DLabel("EEEE");
    
    public static final String[] MONTHS = { 
        "January", "February", "March", "April", 
        "May", "June", "July", "August", 
        "September", "October", "November", "December" 
    };
    
    public static final String[] WEEKDAYS = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };       
    
    private final JButton[]     dayButtons     = new JButton[MAX];
    private final JButton[]     weekdayButtons = new JButton[WEEKDAYS.length];
            
    private final GregorianCalendar present  = new GregorianCalendar();
    private final Calendar          calendar = Calendar.getInstance();
    
    private final int year  = present.get(Calendar.YEAR);
    private final int month = present.get(Calendar.MONTH);
    private final int date  = present.get(Calendar.DATE);    
    
    private static final String DELETE_DIALOG = "Delete all events from %s %s?";
  
    private IconLoader iconLoader;
    private PropertyLoader properties;
    
    private final ItemListener item = (ItemEvent ie) -> {
        if(ie.getStateChange() == ItemEvent.SELECTED) {
            refresh();
        }
    };
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private final MouseListener mouse = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            refreshButton(e.getSource());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            refreshButton(e.getSource());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            refreshButton(e.getSource());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            refreshButton(e.getSource());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            refreshButton(e.getSource());
        }
    };

    private void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new BorderLayout());
       
        iconLoader = IconLoader.getInstance();
        properties = PropertyLoader.getInstance();
        
        initDateMenu();
        initButtonsPane();
        initMonthYearPane();
        initDaysPane();
        pack();
        
        GUIUtils.addToPaneList(this);
    }

    @Override
    public void refresh() {
        clear();       
        SwingUtilities.invokeLater(() -> {
            int y = getSelectedYear();
            int m = getSelectedMonth();
            calendar.clear();
            calendar.set(y, m, 1);
            printDates(calendar);
        });  
    }

    @Override
    public void clear() {
        SwingUtilities.invokeLater(() -> {
            for(int i = 0; i < MAX; i++) {
                JButton button = dayButtons[i];
                button.setText(null);
                button.setBackground(properties.calendar_color_defaultday);
                button.setForeground(properties.calendar_color_foreground);
                button.setToolTipText(null);
                button.setEnabled(false);
            }
        });      
    }
    
    private void initDateMenu() {
        dateMenu.setOpaque(false);
        dateMenu.setLayout(new GridLayout(3,1));
        dateMenu.add(dateLabel);
        dateMenu.add(timeLabel);
        dateMenu.add(dayLabel);
    }
    
    private void initMonthYearPane() {
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new MigLayout("wrap 3"));
        menuPanel.add(monthLabel, SPACING);
        menuPanel.add(monthCombo, COMBO_DIMENSION + "," + SPACING);
        menuPanel.add(buttonsPanel);
        menuPanel.add(yearLabel);
        menuPanel.add(yearCombo, COMBO_DIMENSION + "," + SPACING);
        menuPanel.add(dateMenu);
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
        daysPanel.setOpaque(false);
        daysPanel.setLayout(new GridLayout(7,7));
        
        initWeekDays();
        initDays();
    }
    
    private void initWeekDays() {
        for(int i = 0; i < WEEKDAYS.length; i++) {
            weekdayButtons[i] = new JButton(WEEKDAYS[i]);
            weekdayButtons[i].setBackground(properties.calendar_color_weekdays);
            weekdayButtons[i].setForeground(properties.calendar_color_foreground);
            weekdayButtons[i].addActionListener(action);
            weekdayButtons[i].setName(WEEKDAY_BUTTON);
            daysPanel.add(weekdayButtons[i]);
        }
    }
    
    public void refreshWeekdays() {
        for(JButton button : weekdayButtons) {
            button.setBackground(properties.calendar_color_weekdays);
            button.setForeground(properties.calendar_color_foreground);
        }
    }
    
    private void initDays() {
        for(int i = 0; i < MAX; i++) {   
            dayButtons[i] = new JButton();
            dayButtons[i].setForeground(properties.calendar_color_foreground);
            dayButtons[i].setBackground(properties.calendar_color_defaultday);
            dayButtons[i].addActionListener(action);
            dayButtons[i].addMouseListener(mouse);
            dayButtons[i].setEnabled(false);
            dayButtons[i].setName(DAY_BUTTON);
            daysPanel.add(dayButtons[i]);
        }
        
        calendar.clear();
        calendar.set(year, month, 1);
        SwingUtilities.invokeLater(() -> {
            printDates(calendar);
        });     
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
        settingsButton.setIcon(iconLoader.get(Globals.SETTINGS));
        
        deleteButton.setToolTipText(Globals.DELETE);
        deleteButton.setName(Globals.DELETE);
        deleteButton.addActionListener(action);
        deleteButton.setIcon(iconLoader.get(Globals.DELETE));
    }
    
    private void pack() {
        monthCombo.setSelectedIndex(month);
        yearCombo.setSelectedItem(year);
        add(menuPanel, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);
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
                JButton button = dayButtons[p];
                button.setText((String.valueOf(i+1)));
                p++;
            }
        }
        else {
            for(int i = 0; i < start; i++) {
                JButton button = dayButtons[i];
                button.setText(String.valueOf(max + i + 1));
            }     
        }
                
        EventMapper map = EventMapper.getInstance();
        List<Integer> dayList = map.getRegisteredDays(MONTHS[m], String.valueOf(y));

        for(int i = start + p; i < end + p; i++) {
            int current = i - delta - p;
            JButton button = dayButtons[i];
            button.setText(String.valueOf(current)); 
            button.setEnabled(true);
            
            if(current == date && y == year && m == month) {
                button.setForeground(properties.calendar_color_currentday);
            }
            
            if(dayList != null && dayList.contains(current)) {
                button.setBackground(properties.calendar_color_eventday);
                int rows = map.getNumberOfEvents(Utilities.formatDate(y, m+1, current));
                if(rows != 0) {
                    button.setToolTipText(String.format(BUTTON_TOOLTIP, rows));
                }
            }
        }
        
        for(int i = end + p, j = 1; i < MAX; i++) {
            JButton button = dayButtons[i];
            button.setText(String.valueOf(j));             
            j++;
        }
    }    
    
    private void onTrigger(JButton button) {
        String name = button.getName();
        switch (name) {
            case PropertiesPane.NAME : MainPane.getInstance().showPane(name);
                                       break;
            case WEEKDAY_BUTTON      : onWeekday();
                                       break;
            case Globals.DELETE      : onDelete();
                                       break;                                     
            case DAY_BUTTON          : int y = getSelectedYear();
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
        MainFrame  f = MainFrame.getInstance();
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
    
    private void refreshButton(Object butonObject) {
        if(butonObject instanceof JButton) {
            ((JButton)butonObject).getRootPane().revalidate();
            ((JButton)butonObject).getRootPane().repaint();
        }
    }
    
    private int getSelectedMonth() {
        return (int)monthCombo.getSelectedIndex();
    }
    
    private int getSelectedYear() {
        return (int)yearCombo.getSelectedItem();
    }
}