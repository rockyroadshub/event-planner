/*
 * The MIT License
 *
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Event Planner), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rockyroadshub.planner.gui;

import com.jcabi.aspects.LogExceptions;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.NumberFormatter;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.database.DatabaseConfig;
import org.rockyroadshub.planner.database.DatabaseControl;
import org.rockyroadshub.planner.lib.DocumentSizeFilter;
import org.rockyroadshub.planner.lib.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class EventView extends JPanel {
    public static final String NAME = "eventview";
    
    private static final JLabel       DATE_LABEL        = new JLabel("Date");
   
    private static final JLabel       TITLE_LIMIT       = new JLabel();
    private static final JLabel       TITLE_LABEL       = new JLabel("Event Title");
    private static final JTextField   TITLE_INPUT       = new JTextField();
    
    private static final JLabel       DESCRIPTION_LIMIT = new JLabel();
    private static final JLabel       DESCRIPTION_LABEL = new JLabel("Description");
    private static final JTextArea    DESCRIPTION_INPUT = new JTextArea();
    
    private static final JLabel       LOCATION_LIMIT    = new JLabel();
    private static final JLabel       LOCATION_LABEL    = new JLabel("Location");
    private static final JTextArea    LOCATION_INPUT    = new JTextArea();
    
    private static final JLabel       START_LABEL       = new JLabel("Start");
    private static final SpinnerModel START_MODEL_H     = new SpinnerNumberModel(0,0,23,1);
    private static final SpinnerModel START_MODEL_M     = new SpinnerNumberModel(0,0,59,1);    
    private static final JSpinner     START_HOUR        = new JSpinner(START_MODEL_H);
    private static final JSpinner     START_MINUTE      = new JSpinner(START_MODEL_M); 
    
    private static final JLabel       END_LABEL         = new JLabel("End");    
    private static final SpinnerModel END_MODEL_H       = new SpinnerNumberModel(0,0,23,1);
    private static final SpinnerModel END_MODEL_M       = new SpinnerNumberModel(0,0,59,1);
    private static final JSpinner     END_HOUR          = new JSpinner(END_MODEL_H);
    private static final JSpinner     END_MINUTE        = new JSpinner(END_MODEL_M);
    
    private static final JPanel       MENU              = new JPanel();
    private static final JButton      HOME              = new JButton();
    private static final JButton      BACK              = new JButton();
    private static final JButton      SAVE              = new JButton();
    private static final JButton      EDIT              = new JButton();
   
    private final Font font = new Font("MONOSPACED", 0, 12);    
    
    private static final String HOME0 = "Home";
    private static final String BACK0 = "Back";
    private static final String SAVE0 = "Save";
    private static final String EDIT0 = "Edit";
    
    private int id;
    private String event;
    private String location;
    private String description;
    private String date;
    private String year;
    private String month;
    private String day;
    private String start;
    private String end;
    
    private int y_;
    private int m_;
    private int d_;

    private DefaultStyledDocument document_ttl;
    private DefaultStyledDocument document_loc;
    private DefaultStyledDocument document_dsc;
    
    private String format_ttl;
    private String format_dsc; 
    private String format_loc;
 
    private EventView() {
        initialize();
    }
   
    private void initialize() {
        setOpaque(false);
        setLayout(new MigLayout(new LC().fill()));
        setName(NAME);
       
        pack();
    }
   
   private void pack() {
       addComponents();
       initComponents();
   }
   
    private void initComponents() {
        DatabaseConfig config = DatabaseConfig.getInstance();        
        int t = config.getSize(DatabaseConfig.TITLE);
        int d = config.getSize(DatabaseConfig.DESCRIPTION);
        int l = config.getSize(DatabaseConfig.LOCATION);
        
        document_ttl = new DefaultStyledDocument();
        document_ttl.setDocumentFilter(new DocumentSizeFilter(t));
        document_ttl.addDocumentListener(document);
        format_ttl = getFormat(t);
        TITLE_INPUT.setFont(font);
        TITLE_INPUT.setDocument(document_ttl);
        TITLE_LIMIT.setText(String.format(format_ttl, 0));
        
        document_dsc = new DefaultStyledDocument();
        document_dsc.setDocumentFilter(new DocumentSizeFilter(d));
        document_dsc.addDocumentListener(document);
        format_dsc = getFormat(d);
        DESCRIPTION_INPUT.setFont(font);
        DESCRIPTION_INPUT.setDocument(document_dsc);
        DESCRIPTION_LIMIT.setText(String.format(format_dsc, 0));
        DESCRIPTION_INPUT.setLineWrap(true);
       
        document_loc = new DefaultStyledDocument();
        document_loc.setDocumentFilter(new DocumentSizeFilter(l));
        document_loc.addDocumentListener(document);
        format_loc = getFormat(l);
        LOCATION_INPUT.setFont(font);
        LOCATION_INPUT.setDocument(document_loc);
        LOCATION_LIMIT.setText(String.format(format_loc, 0));
        LOCATION_INPUT.setLineWrap(true);

        setAllowsInvalid(START_HOUR);
        setAllowsInvalid(START_MINUTE);
        setAllowsInvalid(END_HOUR);
        setAllowsInvalid(END_MINUTE);
        
        MENU.setLayout(new MigLayout());
        MENU.add(HOME, "h 32!, w 32!");
        MENU.add(BACK, "h 32!, w 32!");
        MENU.add(SAVE, "h 32!, w 32!");
        MENU.add(EDIT, "h 32!, w 32!");
        
        HOME.setToolTipText(HOME0);
        HOME.setName(CalendarPane.NAME);
        HOME.addActionListener(action);
        BACK.setToolTipText(BACK0);
        BACK.setName(EventsDisplay.NAME);
        BACK.addActionListener(action);
        SAVE.setToolTipText(SAVE0);
        SAVE.addActionListener(action);
        EDIT.setToolTipText(EDIT0);
        EDIT.addActionListener(action);
        
        try {
            initIcon(HOME, "src/Home.png");
            initIcon(BACK, "src/Back.png");
            initIcon(SAVE, "src/Save.png");
            initIcon(EDIT, "src/Edit.png");
        } catch (IOException ex) {}
    }
   
    private void addComponents() {
        add(DATE_LABEL, "h 32!");
        add(MENU, "growx, wrap");
        add(TITLE_LABEL, "h 32!");
        add(TITLE_LIMIT, "h 32!, align right, wrap");
        add(TITLE_INPUT, "growx, h 32!, span 3, wrap");
        add(LOCATION_LABEL, "h 32!");
        add(LOCATION_LIMIT, "h 32!, align right, wrap");
        add(LOCATION_INPUT, "growx, h 150!, span 3, wrap"); 
        add(DESCRIPTION_LABEL, "h 32!");
        add(DESCRIPTION_LIMIT, "h 32!, align right, wrap");
        add(DESCRIPTION_INPUT, "growx, h 150!, span 3, wrap");
        add(START_LABEL, "h 32!, growx");
        add(START_HOUR, "h 32!, w 64!");
        add(START_MINUTE, "h 32!, w 64!, wrap");
        add(END_LABEL, "h 32!, growx");
        add(END_HOUR, "h 32!, w 64!");
        add(END_MINUTE, "h 32!, w 64!, wrap");
    }
   
    private String formatDate(String m, String d, String y) {
         return String.format("%s %s, %s", m, d, y);
    }
    
    private final DocumentListener document = new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) {update(e);}
        @Override public void removeUpdate(DocumentEvent e) {update(e);}
        @Override public void changedUpdate(DocumentEvent e) {update(e);}    
    };
    
    private void update(DocumentEvent e) {
        DefaultStyledDocument doc = (DefaultStyledDocument)e.getDocument();
        if(doc.equals(document_ttl)) {
            update0(TITLE_LIMIT, format_ttl, doc);
        }
        else if(doc.equals(document_loc)) {
            update0(LOCATION_LIMIT, format_loc, doc);
        }
        else if(doc.equals(document_dsc)) {
            update0(DESCRIPTION_LIMIT, format_dsc, doc);
        }
    }
      
    private void update0(JLabel limit, String format, DefaultStyledDocument doc) {
        limit.setText(String.format(format, doc.getLength()));
    }
    
    private String getFormat(int limit) {
        StringBuilder bld = new StringBuilder("(%0");
        int digits = (int)(Math.log10(limit)+1);
        bld.append(digits).append("d/").append(limit).append(")");
        return bld.toString();
    }
    
    private void setAllowsInvalid(JSpinner s) {
        JFormattedTextField txt = ((JSpinner.NumberEditor)s.getEditor()).getTextField();
        ((NumberFormatter)txt.getFormatter()).setAllowsInvalid(false);
    }
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private void onTrigger(JButton button) {
        CalendarPane.getInstance().refresh();
        EventsDisplay.getInstance().refresh(y_, m_, d_);
        switch (button.getToolTipText()) {
            case SAVE0:
                onSave();
                break;
            case EDIT0:
                onEdit();
                break;
            default:
                Panel panel = Panel.getInstance();
                panel.show(button.getName());
                refresh();
                break;
        }
    }
    
    private void onSave() {
        event       = TITLE_INPUT.getText();
        description = DESCRIPTION_INPUT.getText();
        location    = LOCATION_INPUT.getText();
        
        start = String.format("%d.%02d.00", START_MODEL_H.getValue(), START_MODEL_M.getValue());
        end   = String.format("%d.%02d.00", END_MODEL_H.getValue(), END_MODEL_M.getValue());
        
        DatabaseControl dtb = DatabaseControl.getInstance();
        
        try {
            Frame  f = Frame.getInstance();
            String m = "Are you sure to save these changes?";
            String t = "Event Planner";
            int    q = JOptionPane.OK_CANCEL_OPTION;
            int    o = JOptionPane.OK_OPTION;
            if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
                dtb.update(id, event, description, location, date, year, month, day, start, end);
                Panel.getInstance().show(EventsDisplay.NAME);
                EventsDisplay.getInstance().refresh(y_, m_, d_);
                refresh();
            }
        } catch (SQLException ex) { ex.printStackTrace(System.out); }
    }
    
    private void onEdit() {
        enableGUI(true);
    }
    
    public void setDate(int year, int month, int day) {
        setParameters(year, month, day);
        this.year  = String.valueOf(year);
        this.month = CalendarPane.MONTHS[month];
        this.day   = String.valueOf(day);
        DATE_LABEL.setText(formatDate(this.month, this.day ,this.year));
        
        GregorianCalendar cal = new GregorianCalendar(year, month, day);            
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date0 = cal.getTime();
            
        date = dateFormat.format(date0);  
    }

    private void setParameters(int y, int m, int d) {
        this.y_ = y;
        this.m_ = m;
        this.d_ = d;
    }
    
    @LogExceptions
    private void initIcon(JButton b, String n) throws IOException {
        Image img = ImageIO.read(getClass().getResource(String.format(Globals.JAR_ROOT, n)));
        b.setIcon(new ImageIcon(img));
    }
    
    private void refresh() {
        TITLE_INPUT.setText("");
        DESCRIPTION_INPUT.setText("");
        LOCATION_INPUT.setText("");
        START_HOUR.setValue(0);
        START_MINUTE.setValue(0);      
        END_HOUR.setValue(0);          
        END_MINUTE.setValue(0);        
    }
    
    public void enableGUI(boolean bool) {
        TITLE_INPUT.setEditable(bool);
        DESCRIPTION_INPUT.setEditable(bool);
        LOCATION_INPUT.setEditable(bool);
        START_HOUR.setEnabled(bool);
        START_MINUTE.setEnabled(bool);      
        END_HOUR.setEnabled(bool);          
        END_MINUTE.setEnabled(bool);   
    }
    
    public void set(Object... args) 
    {
        this.event       = String.valueOf(args[0]);
        this.description = String.valueOf(args[1]);
        this.location    = String.valueOf(args[2]);
        this.date        = String.valueOf(args[3]);
        this.year        = String.valueOf(args[4]);
        this.month       = String.valueOf(args[5]);
        this.day         = String.valueOf(args[6]);
        this.start       = String.valueOf(args[7]);
        this.end         = String.valueOf(args[8]);
        
        TITLE_INPUT.setText(event);
        DESCRIPTION_INPUT.setText(description);
        LOCATION_INPUT.setText(location);
        String[] s = this.start.split(":");
        String[] e = this.end.split(":");
        START_HOUR.setValue(Integer.parseInt(s[0]));
        START_MINUTE.setValue(Integer.parseInt(s[1]));      
        END_HOUR.setValue(Integer.parseInt(e[0]));          
        END_MINUTE.setValue(Integer.parseInt(e[1]));  
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public static EventView getInstance() {
        return Holder.INSTANCE;
    }
   
    private static class Holder {
        private static final EventView INSTANCE = new EventView();
    }
}