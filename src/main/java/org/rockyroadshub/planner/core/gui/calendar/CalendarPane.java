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
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.data.EventMapper;
import org.rockyroadshub.planner.core.gui.AbstractPane;
import org.rockyroadshub.planner.core.gui.DLabel;
import org.rockyroadshub.planner.core.gui.MainFrame;
import org.rockyroadshub.planner.core.gui.GUIUtils;
import org.rockyroadshub.planner.core.gui.MainPane;
import org.rockyroadshub.planner.core.gui.TButton;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.loader.Icons;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.2
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
    private final TButton   currentButton  = new TButton();
    private final TButton   settingsButton = new TButton();
    private final TButton   deleteButton   = new TButton();
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
    
    private final TButton[]     dayButtons     = new TButton[MAX];
    private final TButton[]     weekdayButtons = new TButton[WEEKDAYS.length];
            
    private final GregorianCalendar present  = new GregorianCalendar();
    private final Calendar          calendar = Calendar.getInstance();
    
    private final int year  = present.get(Calendar.YEAR);
    private final int month = present.get(Calendar.MONTH);
    private final int date  = present.get(Calendar.DATE);    
    
    private static final String DELETE_DIALOG = "Delete all events from %s %s?";
  
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
    
    private void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new BorderLayout());
       
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
            printDates0(calendar);
            daysPanel.revalidate();
            daysPanel.repaint();
        });    
    }

    @Override
    public void clear() {
        SwingUtilities.invokeLater(() -> {
            for(int i = 0; i < MAX; i++) {
                TButton button = dayButtons[i];
                button.setString(null);
                button.setColorAttributes(properties.calendar_color_defaultday);
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
            weekdayButtons[i] = new TButton();
            weekdayButtons[i].setString(WEEKDAYS[i]);
            weekdayButtons[i].setColorAttributes(properties.calendar_color_weekdays);
            weekdayButtons[i].setForeground(properties.calendar_color_foreground);
            weekdayButtons[i].addActionListener(action);
            weekdayButtons[i].setName(WEEKDAY_BUTTON);
            daysPanel.add(weekdayButtons[i]);
        }
    }
    
    public void refreshWeekdays() {
        for(TButton button : weekdayButtons) {
            button.setColorAttributes(properties.calendar_color_weekdays);
            button.setForeground(properties.calendar_color_foreground);
        }
    }
    
    private void initDays() {
        for(int i = 0; i < MAX; i++) {   
            dayButtons[i] = new TButton();
            dayButtons[i].setColorAttributes(properties.calendar_color_defaultday);
            dayButtons[i].setForeground(properties.calendar_color_foreground);
            dayButtons[i].addActionListener(action);
            dayButtons[i].setEnabled(false);
            dayButtons[i].setName(DAY_BUTTON);
            daysPanel.add(dayButtons[i]);
        }
        
        calendar.clear();
        calendar.set(year, month, 1);
        printDates(calendar);
    }    
    
    private void initButtonsPane() {
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new MigLayout(
                Globals.BUTTON_INSETS,
                Globals.BUTTON_GAPX,
                Globals.BUTTON_GAPY));
        buttonsPanel.add(currentButton, Globals.BUTTON_DIMENSIONS);
        buttonsPanel.add(settingsButton, Globals.BUTTON_DIMENSIONS);
        buttonsPanel.add(deleteButton, Globals.BUTTON_DIMENSIONS);
        initButtons();
    }
    
    private void initButtons() {
        currentButton.setToolTipText(Globals.CURRENT);
        currentButton.setName(Globals.CURRENT);
        currentButton.addActionListener(action);
        currentButton.setIcon(Icons.CURRENT.icon());
        
        settingsButton.setToolTipText(Globals.SETTINGS);
        settingsButton.setName(PropertiesPane.NAME);
        settingsButton.addActionListener(action);
        settingsButton.setIcon(Icons.SETTINGS.icon());
        
        deleteButton.setToolTipText(Globals.DELETE);
        deleteButton.setName(Globals.DELETE);
        deleteButton.addActionListener(action);
        deleteButton.setIcon(Icons.DELETE.icon());
    }
    
    private void pack() {
        monthCombo.setSelectedIndex(month);
        yearCombo.setSelectedItem(year);
        add(menuPanel, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);
    }
    
    public void printDates(Calendar cal) {
        SwingUtilities.invokeLater(() -> {
            printDates0(cal);
        });
    }
    
    private void printDates0(Calendar cal) {
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
                TButton button = dayButtons[p];
                button.setString((String.valueOf(i+1)));
                p++;
            }
        }
        else {
            for(int i = 0; i < start; i++) {
                TButton button = dayButtons[i];
                button.setString(String.valueOf(max + i + 1));
            }     
        }
                
        EventMapper map = EventMapper.getInstance();
        List<Integer> dayList = null;
        try {
            dayList = map.getRegisteredDays(MONTHS[m], String.valueOf(y));
        } 
        catch (SQLException ex) {
            MainFrame.showErrorDialog(ex.getMessage());
        }
        finally {
            for(int i = start + p; i < end + p; i++) {
                int current = i - delta - p;
                TButton button = dayButtons[i];
                button.setString(String.valueOf(current)); 
                button.setEnabled(true);

                if(current == date && y == year && m == month) {
                    button.setForeground(properties.calendar_color_currentday);
                }

                if(dayList != null && dayList.contains(current)) {
                    button.setColorAttributes(properties.calendar_color_eventday);
                    int rows = 0;
                    try {
                        rows = map.getNumberOfEvents(Utilities.formatDate(y, m+1, current));
                    } 
                    catch (SQLException ex) {
                        MainFrame.showErrorDialog(ex.getMessage());
                    }
                    finally {
                        if(rows != 0) {
                            button.setToolTipText(String.format(BUTTON_TOOLTIP, rows));
                        }
                    }
                }
            }

            for(int i = end + p, j = 1; i < MAX; i++) {
                TButton button = dayButtons[i];
                button.setString(String.valueOf(j));             
                j++;
            }
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
            case Globals.CURRENT     : onCurrent();
                                       break;
            case DAY_BUTTON          : TButton tbutton = (TButton)button;
                                       int y = getSelectedYear();
                                       int m = getSelectedMonth();
                                       int d = Integer.valueOf(tbutton.getString());
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
        if(MainFrame.showConfirmDialog(String.format(DELETE_DIALOG, m0, y0))) 
        {
            EventMapper map = EventMapper.getInstance();
            try {
                map.deleteAll(m0,y0);
            } 
            catch (SQLException ex) {
                MainFrame.showErrorDialog(ex.getMessage());
            }
            finally {
                refresh();
            }
        }      
    }
    
    private void onCurrent() {
        calendar.clear();
        calendar.set(year, month, 1);
        SwingUtilities.invokeLater(() -> {
            monthCombo.setSelectedIndex(month);
            yearCombo.setSelectedItem(year);
            printDates0(calendar);
        });
    }
    
    private int getSelectedMonth() {
        return (int)monthCombo.getSelectedIndex();
    }
    
    private int getSelectedYear() {
        return (int)yearCombo.getSelectedItem();
    }
}