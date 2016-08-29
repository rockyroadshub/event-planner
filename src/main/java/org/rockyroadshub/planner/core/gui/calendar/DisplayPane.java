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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.data.Event;
import org.rockyroadshub.planner.core.data.EventMapper;
import org.rockyroadshub.planner.core.gui.AbstractPane;
import org.rockyroadshub.planner.core.gui.Frame;
import org.rockyroadshub.planner.core.gui.GUIUtils;
import org.rockyroadshub.planner.system.IconLoader;
import org.rockyroadshub.planner.core.gui.MainPane;
import org.rockyroadshub.planner.core.utils.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
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
        
    private final JLabel        paneLabel     = new JLabel();
    private final JPanel        menuPanel     = new JPanel();
    private final JButton       addButton     = new JButton();
    private final JButton       homeButton    = new JButton();
    private final JButton       viewButton    = new JButton();
    private final JButton       deleteButton  = new JButton();
   
    private final JTable        table         = new JTable();
    private final JScrollPane   tableScroll   = new JScrollPane(table);
    
    private static final int    FONT_SIZE     = 40;
    private static final int    FONT_STYLE    = Font.BOLD;
    private static final String FONT_NAME     = "Calibri";
    private static final String GAP_RIGHT     = "gapright 20!";
    private static final String DELETE_DIALOG = "Are you sure to delete \"%s\" event?";
    private static final String BORDER        = "Display Panel";
    
    private Object[] rowData;
    private IconLoader iconLoader;
     
    private final DefaultTableModel  tableModel   = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
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
        
        initTitle();
        initMenu();
        initButtons();
        initTableModel();
        pack();
        
        GUIUtils.addToPaneList(this);
    }

    @Override
    public void refresh() {
        clear();
        
        EventMapper map = EventMapper.getInstance();        
        for(Event evt : map.getEvents(getDate())) {
            rowData[0] = evt.getID();
            rowData[1] = evt.getEvent();
            rowData[2] = evt.getDate();
            rowData[3] = evt.getStart();
            rowData[4] = evt.getEnd();
            tableModel.addRow(rowData);
        }
        
        paneLabel.setText(getTitleLabel());
    }

    @Override
    public void clear() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
    }
    
    private void initTitle() {
        paneLabel.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
    }
    
    private void initMenu() {
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new MigLayout());
        menuPanel.add(paneLabel,    GAP_RIGHT);
        menuPanel.add(homeButton,   Globals.BUTTON_DIMENSIONS);
        menuPanel.add(addButton,    Globals.BUTTON_DIMENSIONS);
        menuPanel.add(viewButton,   Globals.BUTTON_DIMENSIONS);
        menuPanel.add(deleteButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.setBorder(BorderFactory.createTitledBorder(BORDER));
    }
    
    private void initButtons() {
        homeButton.setToolTipText(Globals.HOME);
        homeButton.setName(CalendarPane.NAME);
        homeButton.addActionListener(action);
        homeButton.setIcon(iconLoader.getIcon(Globals.HOME));
                
        addButton.setToolTipText(Globals.ADD);
        addButton.setName(FormPane.NAME);
        addButton.addActionListener(action);
        addButton.setIcon(iconLoader.getIcon(Globals.ADD));
        
        viewButton.setToolTipText(Globals.VIEW);
        viewButton.setName(ViewPane.NAME);
        viewButton.addActionListener(action);
        viewButton.setIcon(iconLoader.getIcon(Globals.VIEW));
        
        deleteButton.setToolTipText(Globals.DELETE);
        deleteButton.setName(Globals.DELETE);
        deleteButton.addActionListener(action);
        deleteButton.setIcon(iconLoader.getIcon(Globals.DELETE));
    }
    
    private void initTableModel() {
        EventMapper         map  = EventMapper.getInstance();
        List<String>        col  = map.getColumns();
        List<Integer>       disp = map.getDisplayColumns();
        Map<String, String> alt  = map.getColumnAltTexts();
        
        disp.stream().forEach((i) -> {
            tableModel.addColumn(alt.get(col.get(i)));
        });
        
        rowData = new Object[disp.size()];
        table.setModel(tableModel);
    }
    
    private void pack() {
        add(menuPanel, BorderLayout.NORTH);
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
                CalendarPane.getInstance().refresh();
                FormPane.getInstance().refresh();
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
            view.set(EventMapper.getInstance().find(i).get());
            MainPane.getInstance().showPane(ViewPane.NAME);
        }
    }
    
    private void onDelete() {
        int i = getID();
        if(i != -1) {
            Frame  f = Frame.getInstance();
            String m = String.format(DELETE_DIALOG, getEventTitle());
            String t = Globals.FRAME_TITLE;
            int    q = JOptionPane.OK_CANCEL_OPTION;
            int    o = JOptionPane.OK_OPTION;
            if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
                EventMapper map = EventMapper.getInstance();
                map.delete(i);
                refresh();
            }
        }
    }
    
    private Object getEventTitle() {        
        return table.getValueAt(table.getSelectedRow(), 1);
    }
    
    private int getID() {
        int row = table.getSelectedRow();
        if(row != -1) {
            Object o = table.getValueAt(row, 0);
            String n = String.valueOf(o);
            return Integer.parseInt(n);
        }
        return -1;
    }
 }
