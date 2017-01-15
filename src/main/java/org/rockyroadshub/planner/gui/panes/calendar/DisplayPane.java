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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.rockyroadshub.planner.data.Event;
import org.rockyroadshub.planner.data.EventMapper;
import org.rockyroadshub.planner.gui.core.AbstractPane;
import org.rockyroadshub.planner.gui.core.MainFrame;
import org.rockyroadshub.planner.gui.core.GUIUtils;
import org.rockyroadshub.planner.gui.core.MainPane;
import org.rockyroadshub.planner.utils.Globals;
import org.rockyroadshub.planner.gui.core.MToolBar;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public final class DisplayPane extends AbstractPane {

    private DisplayPane() {
        initialize();
    }

    public static DisplayPane getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DisplayPane INSTANCE = new DisplayPane();
    }
    
    public static final String NAME = "displaypane";               
    public static final Pattern TIME_PATTERN = Pattern.compile("(\\d+):(\\d{2}):(\\d{2})");  
        
    private final JLabel        paneLabel     = new JLabel();
    private final MToolBar      menuToolBar   = new MToolBar();
   
    private final JTable        table         = new JTable();
    private final JScrollPane   tableScroll   = new JScrollPane(table);
    
    private static final int    FONT_SIZE     = 25;
    private static final int    FONT_STYLE    = Font.BOLD;
    private static final String GAP_RIGHT     = "gapright 20!";
    private static final String DELETE_DIALOG = "Are you sure to delete \"%s\" event?";
    private static final String BORDER        = "Display Panel";
    
    private PropertyLoader properties;
     
    private final DisplayTableModel    tableModel    = new DisplayTableModel();
    private final DisplayTableRenderer tableRenderer = new DisplayTableRenderer();
    
    private final List<Integer> hourCache = new ArrayList<>();
    private final List<Integer> idCache = new ArrayList<>();
    
    private final String[][] buttons = {
        {"ADD",    FormPane.NAME},
        {"VIEW",   ViewPane.NAME},
        {"DELETE", Globals.DELETE}
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
        
        initIDCache();
        initTitle();
        initMenu();
        initButtons();
        initTable();
        pack();
        
        GUIUtils.addToPaneList(NAME, this);
    }

    @Override
    public void refresh() {
        clear();
        EventMapper map = EventMapper.getInstance();
        tableModel.generateTable();
        hourCache.clear();
        resetIDCache();
        try {
            for(Event evt : map.getEvents(getDate())) {
                Matcher s = TIME_PATTERN.matcher(evt.getStart());    
                Matcher e = TIME_PATTERN.matcher(evt.getEnd());
                if(s.find() && e.find()) {
                    int row = Integer.valueOf(s.group(1));
                    int end = Integer.valueOf(e.group(1));
                    int eventLength = end - row;
                    for(int i = 0; i < eventLength + 1; i++) {
                        tableModel.setRowColor(i + row, properties.calendar_color_eventday);
                        tableModel.setRowForeground(i + row, properties.calendar_color_foreground);
                        tableModel.setIdentity(i + row, evt);
                        hourCache.add(i + row);
                        idCache.set(i + row, evt.getID());
                    }
                    tableModel.setValueAt(evt.getEvent(), row, 1);
                }
            }
        } 
        catch (SQLException ex) {
            MainFrame.showErrorDialog(ex.getMessage());
            return;
        }
        paneLabel.setText(getTitleLabel());
    }

    @Override
    public void clear() {        
        tableModel.resetColors();
        tableModel.resetIdentities();
        tableModel.resetForegrounds();
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
    }
    
    private void initIDCache() {
        for(int i = 0; i < 24; i++) {
            idCache.add(-1);
        }
    }
    
    private void initTitle() {
        paneLabel.setFont(getFont().deriveFont(FONT_STYLE, FONT_SIZE));
    }
    
    private void initMenu() {
        menuToolBar.setRollover(true);
        menuToolBar.setFloatable(false);
        menuToolBar.add(paneLabel);
        menuToolBar.add(new JToolBar.Separator(), "FILL");
        menuToolBar.setBorder(BorderFactory.createTitledBorder(BORDER));
    }
    
    private void initButtons() {
        menuToolBar.addButtons(buttons, action);
    }
    
    private void initTable() {
        EventMapper         map  = EventMapper.getInstance();
        List<String>        col  = map.getColumns();
        List<Integer>       disp = map.getDisplayColumns();
        Map<String, String> alt  = map.getColumnAltTexts();
        
        disp.stream().forEach((i) -> {
            tableModel.addColumn(alt.get(col.get(i)));
        });
        
        table.setModel(tableModel);
        table.setDefaultRenderer(table.getColumnClass(0), tableRenderer);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));      
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
    }
    
    private void pack() {
        add(menuToolBar, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }
    
    private void onTrigger(JButton button) {
        String name  = button.getName(); 
        switch (name) {
            case ViewPane.NAME:
                onView();
                break;
            case Globals.DELETE:
                onDelete();
                break;
            default:
                FormPane.getInstance().refresh();
                FormPane.getInstance().setBackEnabled(true);
                int row = table.getSelectedRow();
                if(row != -1) FormPane.getInstance().setTimeValues(row);
                MainPane.getInstance().showPane(name);
                clear();
                break;
        }
    }
    
    private void onView() {
        int i = getID();
        if(i != -1) {
            ViewPane view = ViewPane.getInstance();
            view.enableGUI(false);
            try {
                view.set(EventMapper.getInstance().find(i).get());
            } 
            catch (SQLException ex) {
                MainFrame.showErrorDialog(ex.getMessage());
                return;
            }
            
            MainPane.getInstance().showPane(ViewPane.NAME);
        }
    }
    
    private void onDelete() {
        int i = getID();
        if(i != -1) {
            if(MainFrame.showConfirmDialog(String.format(DELETE_DIALOG, getEventTitle()))) 
            {
                EventMapper map = EventMapper.getInstance();
                try {
                    map.delete(i);
                } 
                catch (SQLException ex) {
                    MainFrame.showErrorDialog(ex.getMessage());
                    return;
                }
                refresh();
            }
        }
    }
    
    private Object getEventTitle() {
        int row = table.getSelectedRow();
        Event evt = tableModel.getIdentity(row);
        return evt.getEvent();
    }
    
    private int getID() {
        int row = table.getSelectedRow();
        if(row != -1) {
            Event evt = tableModel.getIdentity(row);
            if(evt == null) return -1;
            return evt.getID();
        }
        return -1;
    }
    
    private void resetIDCache() {
        for(int i = 0; i < 24; i++) {
            idCache.set(i, -1);
        }
    }
    
    public List<Integer> getHourCache() {
        return hourCache;
    }
    
    public List<Integer> getIDCache() {
        return idCache;
    }
    
    private class DisplayTableRenderer extends DefaultTableCellRenderer {
        
        @Override
	public Component getTableCellRendererComponent 
        (JTable table, Object value, boolean selected, boolean focused, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            DisplayTableModel tableModel = (DisplayTableModel)table.getModel();
            setBackground(tableModel.getRowColor(row));
            setForeground(tableModel.getRowForeground(row));
            return this;  
	}
    }
    
    private class DisplayTableModel extends DefaultTableModel {
        final int rowCount = 24;
        final Color defaultColor = Color.WHITE;
        final Color defaultForeground = Color.BLACK;
        final List<Color> colorSet = new ArrayList<>();
        final List<Event> eventSet = new ArrayList<>();
        final List<Color> foregroundSet = new ArrayList<>();
        
        DisplayTableModel() {
            for(int i = 0; i < rowCount; i++) {
                eventSet.add(null);
                colorSet.add(defaultColor);
                foregroundSet.add(defaultForeground);
            }
        }
        
        void generateTable() {
            for(int i = 0; i < rowCount; i++) {
                addRow(new Object[]{String.format("%d:00:00",i), null});
            } 
        }
        
        void resetColors() {
            for(int i = 0; i < rowCount; i++) {
                colorSet.set(i, defaultColor);
            }
        }
        
        void resetForegrounds() {
            for(int i = 0; i < rowCount; i++) {
                foregroundSet.set(i, defaultForeground);
            }
        }
        
        void resetIdentities() {
            for(int i = 0; i < rowCount; i++) {
                eventSet.set(i, null);
            }
        }
        
        void setRowColor(int row, Color color) {
            colorSet.set(row, color);
            fireTableRowsUpdated(row, row);
        }
        
        Color getRowColor(int row) {
            return colorSet.get(row);
        }
        
        void setRowForeground(int row, Color color) {
            foregroundSet.set(row, color);
            fireTableRowsUpdated(row, row);
        }
        
        Color getRowForeground(int row) {
            return foregroundSet.get(row);
        }
        
        void setIdentity(int row, Event evt) {
            eventSet.set(row, evt); 
       }
        
        Event getIdentity(int row) {
            return eventSet.get(row);
        }
                
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    }
 }