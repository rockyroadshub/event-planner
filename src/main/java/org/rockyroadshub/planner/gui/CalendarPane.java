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
package org.rockyroadshub.planner.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.database.DatabaseControl;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class CalendarPane extends JPanel {    
    /**
     * Name of the panel
     */
    public static final String NAME = "calendar";
    
    /**
     * Label for month combo box
     */
    private static final JLabel    LBL_MN = new JLabel("Month");
    
    /**
     * Month combo box
     */
    private static final JComboBox CBX_MN = new JComboBox();
    
    /**
     * Label for year combo box
     */
    private static final JLabel    LBL_YR = new JLabel("Year");
    
    /**
     * Year combo box
     */
    private static final JComboBox CBX_YR = new JComboBox();
    
    /**
     * Panel container for the month and year components
     */
    private static final JPanel    PNL_MY = new JPanel();
    
    /**
     * Panel container for the days component
     */
    private static final JPanel    PNL_DT = new JPanel();
    
    /**
     * Present date
     */
    private final GregorianCalendar present = new GregorianCalendar();
    
    /**
     * Present year
     */
    private final int year  = present.get(Calendar.YEAR);
    
    /**
     * Present month
     */
    private final int month = present.get(Calendar.MONTH);
    
    /**
     * Present date
     */
    private final int date  = present.get(Calendar.DATE);
    
    /**
     * Months list
     */
    public static final String[] MONTHS = { 
        "January", "February", "March", "April", 
        "May", "June", "July", "August", 
        "September", "October", "November", "December" 
    };
    
    /**
     * Weekdays list
     */
    public static final String[] DAYS = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };
    
    /**
     * Hash map of buttons for easy access
     */
    private static final HashMap<Integer, JButton> BTN = new HashMap<>();
    
    /**
     * Hash map of buttons (if a button is used or not)
     */
    private static final HashMap<JButton, Boolean> CTN = new HashMap<>();
    
    /**
     * Color for the current day
     */
    private static final Color CURRENT_DAY_COLOR = Color.YELLOW;
    
    /**
     * Color for the schedule day
     */
    private static final Color SCHEDULE_COLOR = new Color(180, 50, 130);
    
    /**
     * Color of the weekdays
     */
    private static final Color WEEKDAYS_COLOR = new Color(100,100,100);
    
    /**
     * Default "day" container color
     */
    private static final Color DEFAULT = new Color(50,50,50);
    
    /**
     * Default "day" container foreground color
     */
    private static final Color DEFAULT_FG = Color.WHITE;
    
    /**
     * Maximum number of "days" container in the calendar display
     */
    private static final int MAX = 42;
    
    /**
     * Maximum number of years in the combobox
     */
    private static final int MAX_YEAR = 100;
    
    /**
     * Starting year in the combobox
     */
    private static final int START_YEAR = 2000;
    
    /**
     * Dimension of the "days" container
     */
    private static final String DAYS_DIMENSION = "h 60!, w 72!";
    
    /**
     * Dimension of the combo boxes
     */
    private static final String COMBO_DIMENSION = "h 35!, w 200!";
    
    /**
     * Spacing between the labels and combo boxes
     */
    private static final String SPACING = "gapright 35!";
    
    /**
     * Events Display title label format
     */
    private static final String FORMAT = "%s %d, %d";
        
    private CalendarPane() {
//        SwingUtilities.invokeLater(() -> {
//            initialize();
//        });
        initialize();
    }
    
    private void initialize() {
        setOpaque(false);
        setLayout(new MigLayout(new LC().fill()));
        setName(NAME);
        
        initMonthYearPane();
        initDaysPane();
        pack();
    }
    
    private void initMonthYearPane() {
        PNL_MY.setOpaque(false);
        PNL_MY.setLayout(new MigLayout("wrap 2"));
        PNL_MY.add(LBL_MN, SPACING);
        PNL_MY.add(CBX_MN, COMBO_DIMENSION);
        PNL_MY.add(LBL_YR);
        PNL_MY.add(CBX_YR, COMBO_DIMENSION);
        
        initComboYear();
        initComboMonth();
    }
    
    private void initDaysPane() {
        PNL_DT.setOpaque(false);
        PNL_DT.setLayout(new MigLayout("wrap 7"));
        
        initWeekDays();
        initDates();
    }
    
    private void pack() {
        CBX_MN.setSelectedIndex(month);
        CBX_YR.setSelectedItem(year);
        add(PNL_MY, "wrap");
        add(PNL_DT);
    }
    
    private void initComboYear() {
       for(int i = 0; i < MAX_YEAR; i++) {
            CBX_YR.addItem(START_YEAR + i);
        } 
       
        CBX_YR.addItemListener(item);
    }
    
    private void initComboMonth() {
        for(String i : MONTHS) {
            CBX_MN.addItem(i);
        } 
        
        CBX_MN.addItemListener(item);
    }
    
    private void initWeekDays() {
        for(String i : DAYS) {
            JButton button = new JButton(i);
            button.setBackground(WEEKDAYS_COLOR);
            button.setForeground(DEFAULT_FG);
            button.setEnabled(false);
            PNL_DT.add(button, DAYS_DIMENSION);
        }
    }
    
    private void initDates() {
        for(int i = 0; i < MAX; i++) {   
            JButton button = new JButton();
            button.setForeground(DEFAULT_FG);
            button.setBackground(DEFAULT);
            button.addActionListener(action);
            BTN.put(i, button);
            CTN.put(button, false);
            PNL_DT.add(button, DAYS_DIMENSION);
        }
        
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);        
        printDates(cal);
    }
    
    private void clear() {
        for(int i = 0; i < MAX; i++) {
            JButton button = (JButton)BTN.get(i);
            button.setText("");
            button.setBackground(DEFAULT);
            button.setForeground(DEFAULT_FG);
            CTN.put(button, false);
        }
    }
    
    private void printDates(GregorianCalendar cal) {
        int s = cal.get(GregorianCalendar.DAY_OF_WEEK);
        int e = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int y = getSelectedYear();
        int m = getSelectedMonth();
            
        int start = s - 1;
        int end   = s + e - 1;
        int delta = start - 1;
        
        DatabaseControl dtb = DatabaseControl.getInstance();
        List<String> list = null;
        String _m  = "EVENT_MONTH";
        String _m0 = MONTHS[m];
        String _y  = "EVENT_YEAR";
        String _y0 = String.valueOf(y);
        
        try {
            list = dtb.select(_m,_m0,_y,_y0,"AND","EVENT_DAY");
        }catch (SQLException ex) {
            for(String i : list) {
                System.out.println(i);
            }
        }
        
        for(int i = start; i < end; i++) {
            int current = i - delta;
            
            JButton button = (JButton)BTN.get(i);
            button.setText(String.valueOf(current)); 
            CTN.put(button, true);
            
            if(current == date && y == year && m == month) {
                button.setForeground(CURRENT_DAY_COLOR);
            }
            
            if(list != null && list.contains(String.valueOf(current))) {
                button.setBackground(SCHEDULE_COLOR);
            }
        }
    }
    
    private final ItemListener item = (ItemEvent ie) -> {
        if(ie.getStateChange() == ItemEvent.SELECTED) {
            refresh();
        }
    };
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private void onTrigger(JButton button) {
        if(CTN.get(button)) {
            int y = getSelectedYear();
            int m = getSelectedMonth();
            int d = Integer.valueOf(button.getText());
            
            Panel         panel   = Panel.getInstance();
            EventsDisplay display = EventsDisplay.getInstance();
            EventForm     form    = EventForm.getInstance();
            
            form.setDate(y, m, d);
            display.refresh(y, m, d);
            display.setTitleLabel(String.format(FORMAT, MONTHS[m], d, y));
            panel.show(EventsDisplay.NAME);
        }
    }
    
    private int getSelectedMonth() {
        return (int)CBX_MN.getSelectedIndex();
    }
    
    private int getSelectedYear() {
        return (int)CBX_YR.getSelectedItem();
    }
    
    public final void refresh() {
        clear();
        int y = getSelectedYear();
        int m = getSelectedMonth();
        GregorianCalendar cal = new GregorianCalendar(y, m, 1);
        printDates(cal);
    }
    
    public static CalendarPane getInstance() {
        return Holder.INSTANCE;
    }
    
    private static class Holder {
        private static final CalendarPane INSTANCE = new CalendarPane();
    }
}
