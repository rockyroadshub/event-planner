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

import com.jcabi.aspects.LogExceptions;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.Event;
import org.rockyroadshub.planner.database.DatabaseConfig;
import org.rockyroadshub.planner.database.DatabaseControl;
import org.rockyroadshub.planner.core.Globals;
import org.rockyroadshub.planner.core.data.EventMapper;
import org.rockyroadshub.planner.database.DatabaseConnection;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class EventsDisplay extends JPanel {
    public static final String NAME = "eventsdisplay";
    
    private final JLabel             eventLabel = new JLabel();
    private final JPanel             menu       = new JPanel();
    private final JButton            add        = new JButton();
    private final JButton            home       = new JButton();
    private final JButton            view       = new JButton();
    private final JButton            delete     = new JButton();
   
    private final JTable            table      = new JTable();
    private final JScrollPane       scroll     = new JScrollPane(table);
    private final DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    
    private static final int    FONT_SIZE  = 40;
    private static final int    FONT_STYLE = Font.BOLD;
    private static final String FONT_NAME  = "Calibri";
    private static final String BUTTON_DIMENSIONS = "h 32!, w 32!";
    private static final String GAP_RIGHT = "gapright 20!";
    
    private static final String HOME0   = "Home";
    private static final String ADD0    = "Add";
    private static final String VIEW0   = "View";
    private static final String DELETE0 = "Delete";

    private Object[] rowData;
   
    private EventsDisplay() { 
       initialize();
    }
   
    private void initialize() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setName(NAME);
       
        initTitle();
        initMenu();
        initButtons();
        
        DatabaseConfig config = DatabaseConfig.getInstance();
        List<String> columns = config.getDisplayColumns();
        columns.stream().forEach((d) -> {
            tableModel.addColumn(d);
        });
        rowData = new Object[columns.size()];
        table.setModel(tableModel);

        add(menu, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
   
    public void setTitleLabel(String l) {
        eventLabel.setText(l);
    }
   
    private void initTitle() {
        eventLabel.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
    }
   
    private void initMenu() {
        menu.setOpaque(false);
        menu.setLayout(new MigLayout());
        menu.add(eventLabel, GAP_RIGHT);
        menu.add(home, BUTTON_DIMENSIONS);
        menu.add(add, BUTTON_DIMENSIONS);
        menu.add(view, BUTTON_DIMENSIONS);
        menu.add(delete, BUTTON_DIMENSIONS);
    }
    
    private void initButtons() {
        try {
            initIcon(home,  "Home.png");
            initIcon(add,   "Add.png");
            initIcon(view,  "View.png");
            initIcon(delete,"Delete.png");
        }catch (IOException ex) {}
        
        home.setToolTipText(HOME0);
        home.setName(CalendarPane.NAME);
        home.addActionListener(action);
                
        add.setToolTipText(ADD0);
        add.setName(EventForm.NAME);
        add.addActionListener(action);
        
        view.setToolTipText(VIEW0);
        view.setName(EventView.NAME);
        view.addActionListener(action);
        
        delete.setToolTipText(DELETE0);
        delete.setName(DELETE0);
        delete.addActionListener(action);
    }
   
    @LogExceptions
    private void initIcon(JButton b, String n) throws IOException {
        Image img = ImageIO.read(getClass().getResource(String.format(Globals.IMAGES_ROOT, n)));
        b.setIcon(new ImageIcon(img));
    }
    
    private final ActionListener action = (ActionEvent ae) -> {    
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private void onTrigger(JButton button) {
        String name  = button.getName(); 
        switch (name) {
            case EventView.NAME:
                onView();
                break;
            case DELETE0:
                onDelete();
                break;
            default:
                CalendarPane.getInstance().refresh();
                Panel.getInstance().show(name);
                break;
        }
    }

    private void onView() {
        int i = getID();
        if(i != -1) {
            Panel.getInstance().show(EventView.NAME);
            EventView v = EventView.getInstance();
            v.setID(i);
            v.enableGUI(false);
            
            try {
                v.set(DatabaseControl.getInstance().select(i));
            } 
            catch (SQLException ex) {}
        }
    }
    
    private void onDelete() {
        int i = getID();
        if(i != -1) {
            Frame  f = Frame.getInstance();
            String m = "Are you sure to delete this event?";
            String t = "Event Planner";
            int    q = JOptionPane.OK_CANCEL_OPTION;
            int    o = JOptionPane.OK_OPTION;
            if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
                EventMapper map = EventMapper.getInstance();
                map.delete(i);
                refresh();
            }
        }
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
    
    public void refresh() {
        Event event = Event.getInstance();
        String date = event.getDate();
        
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        
        DatabaseConnection  dtb         = DatabaseConnection.getInstance();
        DatabaseConfig      config      = DatabaseConfig.getInstance();
        List<String>        displayList = config.getDisplayColumns();
        Map<String, String> displayMap  = config.getDisplayColMap();
        String              tableName   = config.getTableName();
        Connection          connection  = dtb.getConnection();
        
        try(Statement stmt = connection.createStatement()) {
            String statement = String.format(
                    "SELECT * FROM %s WHERE EVENT_DATE = '%s'", tableName, date);
            try(ResultSet rs = stmt.executeQuery(statement)) {
                while(rs.next()) {
                    int i = 0;
                    for(String c : displayList) {
                        String data = rs.getString(displayMap.get(c));
                        rowData[i] = data;
                        i++;
                    }
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {}
    }
       
    public static EventsDisplay getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EventsDisplay INSTANCE = new EventsDisplay();
    }
}