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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.rockyroadshub.planner.database.DatabaseConfig;
import org.rockyroadshub.planner.database.DatabaseConnection;
import org.rockyroadshub.planner.database.DatabaseControl;
import org.rockyroadshub.planner.lib.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class EventsDisplay extends JPanel {
    public static final String NAME = "eventsdisplay";
   
    private static final JLabel LBL_TL = new JLabel();

    private final JTable table = new JTable();
    private final JScrollPane scroll = new JScrollPane(table);
    DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    
    private static final JPanel      PNL_MU = new JPanel();
    private static final JButton     BTN_AD = new JButton();
    private static final JButton     BTN_HM = new JButton();
    private static final JButton     BTN_VW = new JButton();
    private static final JButton     BTN_DE = new JButton();
   
    private static final int    FONT_SIZE  = 40;
    private static final int    FONT_STYLE = Font.BOLD;
    private static final String FONT_NAME  = "Calibri";
    private static final String BUTTON_DIMENSIONS = "h 32!, w 32!";
    private static final String GAP_RIGHT = "gapright 20!";
    
    private static final String TOOLTIP_HOME = "Home";
    private static final String TOOLTIP_ADD  = "Add";
    private static final String TOOLTIP_EDIT = "View";
    private static final String TOOLTIP_DELETE = "Delete";
    
    private int year;
    private int month;
    private int day;
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
        List<String> columns = config.getTableColumns();
        columns.stream().forEach((d) -> {
            tableModel.addColumn(d);
        });
        rowData = new Object[columns.size()];
        table.setModel(tableModel);

        add(PNL_MU, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
   
    public void setTitleLabel(String l) {
        LBL_TL.setText(l);
    }
   
    private void initTitle() {
        LBL_TL.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
    }
   
    private void initMenu() {
        PNL_MU.setOpaque(false);
        PNL_MU.setLayout(new MigLayout());
        PNL_MU.add(LBL_TL, GAP_RIGHT);
        PNL_MU.add(BTN_HM, BUTTON_DIMENSIONS);
        PNL_MU.add(BTN_AD, BUTTON_DIMENSIONS);
        PNL_MU.add(BTN_VW, BUTTON_DIMENSIONS);
        PNL_MU.add(BTN_DE, BUTTON_DIMENSIONS);
    }
    
    private void initButtons() {
        try {
            initIcon(BTN_HM, "src/Home.png");
            initIcon(BTN_AD, "src/Add.png");
            initIcon(BTN_VW, "src/View.png");
            initIcon(BTN_DE, "src/Delete.png");
        }catch (IOException ex) {}
        
        BTN_HM.setToolTipText(TOOLTIP_HOME);
        BTN_HM.setName(CalendarPane.NAME);
        BTN_HM.addActionListener(action);
                
        BTN_AD.setToolTipText(TOOLTIP_ADD);
        BTN_AD.setName(EventForm.NAME);
        BTN_AD.addActionListener(action);
        
        BTN_VW.setToolTipText(TOOLTIP_EDIT);
        BTN_VW.addActionListener(view);
        BTN_VW.setName(EventView.NAME);
        
        BTN_DE.setToolTipText(TOOLTIP_DELETE);
        BTN_DE.addActionListener(delete);
    }
   
    @LogExceptions
    private void initIcon(JButton b, String n) throws IOException {
        Image img = ImageIO.read(getClass().getResource(String.format(Globals.JAR_ROOT, n)));
        b.setIcon(new ImageIcon(img));
    }
    
    private final ActionListener action = (ActionEvent ae) -> {    
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private final ActionListener view = (ActionEvent ae) -> {    
        onView();
    };
    
    private void onTrigger(JButton button) {
        String name  = button.getName();
        Panel.getInstance().show(name);
        if(name.equals(CalendarPane.NAME)) {
            CalendarPane.getInstance().refresh();
        }
    }
    
    private final ActionListener delete = (ActionEvent ae) -> {
        onDelete();
    };
    
    private void onDelete() {
        DatabaseControl dtb = DatabaseControl.getInstance();
        try {
            int i = getID();
            if(i != -1) {
                Frame  f = Frame.getInstance();
                String m = "Are you sure to delete this event?";
                String t = "Event Planner";
                int    q = JOptionPane.OK_CANCEL_OPTION;
                int    o = JOptionPane.OK_OPTION;
                if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
                    dtb.delete(getID());
                    refresh(year, month, day);
                }
            }
        } catch (SQLException ex) {
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
    
    public final void refresh(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        
        DatabaseConnection dtb = DatabaseConnection.getInstance();
        Connection connection = dtb.getConnection();

        GregorianCalendar cal = new GregorianCalendar(year, month, day);            
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date0 = cal.getTime();      
        String date = dateFormat.format(date0); 
        
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        
        try(Statement stmt = connection.createStatement()) {
            String statement = 
            String.format("SELECT * FROM EVENTS WHERE EVENT_DATE = '%s'", date);
            List<String> list = DatabaseConfig.getInstance().getTableColumns();
            Map<String, String> map = DatabaseConfig.getInstance().getColumnMap();
            try(ResultSet rs = stmt.executeQuery(statement)) {
                while(rs.next()) {
                    int j = 0;
                    for(String i : list) {
                        String data = rs.getString(map.get(i));
                        rowData[j] = data;
                        j++;
                    }
                    tableModel.addRow(rowData);
                    table.getSelectedRow();
                }
            }
        } 
        catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

    private void onView() {
        int i = getID();
        if(i != -1) {
            Panel.getInstance().show(EventView.NAME);
            EventView view_ = EventView.getInstance();
            view_.setID(i);
            view_.enableGUI(false);
            DatabaseConnection dtb = DatabaseConnection.getInstance();
            Connection connection = dtb.getConnection();

            GregorianCalendar cal = new GregorianCalendar(year, month, day);            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date0 = cal.getTime();      
            String date = dateFormat.format(date0); 

            tableModel.getDataVector().removeAllElements();
            tableModel.fireTableDataChanged();

            try(Statement stmt = connection.createStatement()) {
                String statement = 
                String.format("SELECT * FROM EVENTS WHERE %s = %s", "EVENT_ID", i);
                Map<String, String> map = DatabaseConfig.getInstance().getColumnMap();
                Object[] o = new Object[9];
                try(ResultSet rs = stmt.executeQuery(statement)) {
                    while(rs.next()) {       
                        int j = 0;
                        for(String k : DatabaseConfig.getInstance().getColumnsNAsList()) {
                            String data = rs.getString(k);
                            o[j] = data;
                            System.out.println(data);
                            j++;
                        }
                        EventView.getInstance().set(o);
                    }
                }
            } 
            catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }
       
    public static EventsDisplay getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EventsDisplay INSTANCE = new EventsDisplay();
    }
}