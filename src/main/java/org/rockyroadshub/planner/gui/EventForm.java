/*
 * The MIT License
 *
 * Copyright 2016 Arnell Christoper D. Dalid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Event Planner"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Event Planner.
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
import java.util.Calendar;
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
import org.rockyroadshub.planner.core.DocumentSizeFilter;
import org.rockyroadshub.planner.core.Globals;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class EventForm extends JPanel {
    public static final String NAME = "eventform";
    
    private final JLabel       dateLabel        = new JLabel("Date");
   
    private final JLabel       eventLimit       = new JLabel();
    private final JLabel       eventLabel       = new JLabel("Event Title");
    private final JTextField   eventInput       = new JTextField();
    
    private final JLabel       descriptionLimit = new JLabel();
    private final JLabel       descriptionLabel = new JLabel("Description");
    private final JTextArea    descriptionInput = new JTextArea();
    
    private final JLabel       locationLimit    = new JLabel();
    private final JLabel       locationLabel    = new JLabel("Location");
    private final JTextArea    locationInput    = new JTextArea();
    
    private final JLabel       startLabel       = new JLabel("Start (HH:MM)");
    private final SpinnerModel startModelH      = new SpinnerNumberModel(0,0,23,1);
    private final SpinnerModel startModelM      = new SpinnerNumberModel(0,0,59,1);    
    private final JSpinner     startHour        = new JSpinner(startModelH);
    private final JSpinner     startMinute      = new JSpinner(startModelM); 
    
    private final JLabel       endLabel         = new JLabel("End (HH:MM)");    
    private final SpinnerModel endModelH        = new SpinnerNumberModel(0,0,23,1);
    private final SpinnerModel endModelM        = new SpinnerNumberModel(0,0,59,1);
    private final JSpinner     endHour          = new JSpinner(endModelH);
    private final JSpinner     endMinute        = new JSpinner(endModelM);
    
    private final JPanel       menu             = new JPanel();
    private final JButton      home             = new JButton();
    private final JButton      back             = new JButton();
    private final JButton      save             = new JButton();
   
    private final Font font = new Font("MONOSPACED", 0, 12);    
    
    private static final String HOME0 = "Home";
    private static final String BACK0 = "Back";
    private static final String SAVE0 = "Save";
    
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

    private DefaultStyledDocument documentEvt;
    private DefaultStyledDocument documentLoc;
    private DefaultStyledDocument documentDsc;
    
    private String formatEvt;
    private String formatDsc; 
    private String formatLoc;
 
    private EventForm() {
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
        int t = config.getSize(DatabaseConfig.EVENT);
        int d = config.getSize(DatabaseConfig.DESCRIPTION);
        int l = config.getSize(DatabaseConfig.LOCATION);
        
        documentEvt = new DefaultStyledDocument();
        documentEvt.setDocumentFilter(new DocumentSizeFilter(t));
        documentEvt.addDocumentListener(document);
        formatEvt = timeStamp(t);
        eventInput.setFont(font);
        eventInput.setDocument(documentEvt);
        eventLimit.setText(String.format(formatEvt, 0));
        
        documentDsc = new DefaultStyledDocument();
        documentDsc.setDocumentFilter(new DocumentSizeFilter(d));
        documentDsc.addDocumentListener(document);
        formatDsc = timeStamp(d);
        descriptionInput.setFont(font);
        descriptionInput.setDocument(documentDsc);
        descriptionLimit.setText(String.format(formatDsc, 0));
        descriptionInput.setLineWrap(true);
       
        documentLoc = new DefaultStyledDocument();
        documentLoc.setDocumentFilter(new DocumentSizeFilter(l));
        documentLoc.addDocumentListener(document);
        formatLoc = timeStamp(l);
        locationInput.setFont(font);
        locationInput.setDocument(documentLoc);
        locationLimit.setText(String.format(formatLoc, 0));
        locationInput.setLineWrap(true);

        setAllowsInvalid(startHour);
        setAllowsInvalid(startMinute);
        setAllowsInvalid(endHour);
        setAllowsInvalid(endMinute);
        
        menu.setLayout(new MigLayout());
        menu.add(home, "h 32!, w 32!");
        menu.add(back, "h 32!, w 32!");
        menu.add(save, "h 32!, w 32!");
        
        home.setToolTipText(HOME0);
        home.setName(CalendarPane.NAME);
        home.addActionListener(action);
        back.setToolTipText(BACK0);
        back.setName(EventsDisplay.NAME);
        back.addActionListener(action);
        save.setToolTipText(SAVE0);
        save.addActionListener(action);
        
        try {
            initIcon(home, "src/Home.png");
            initIcon(back, "src/Back.png");
            initIcon(save, "src/Save.png");
        } catch (IOException ex) {}
    }
   
    private void addComponents() {
        add(dateLabel, "h 32!");
        add(menu, "growx, wrap");
        add(eventLabel, "h 32!");
        add(eventLimit, "h 32!, align right, wrap");
        add(eventInput, "growx, h 32!, span 3, wrap");
        add(locationLabel, "h 32!");
        add(locationLimit, "h 32!, align right, wrap");
        add(locationInput, "growx, h 150!, span 3, wrap"); 
        add(descriptionLabel, "h 32!");
        add(descriptionLimit, "h 32!, align right, wrap");
        add(descriptionInput, "growx, h 150!, span 3, wrap");
        add(startLabel, "h 32!, growx");
        add(startHour, "h 32!, w 64!");
        add(startMinute, "h 32!, w 64!, wrap");
        add(endLabel, "h 32!, growx");
        add(endHour, "h 32!, w 64!");
        add(endMinute, "h 32!, w 64!, wrap");
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
        if(doc.equals(documentEvt)) {
            update0(eventLimit, formatEvt, doc);
        }
        else if(doc.equals(documentLoc)) {
            update0(locationLimit, formatLoc, doc);
        }
        else if(doc.equals(documentDsc)) {
            update0(descriptionLimit, formatDsc, doc);
        }
    }
      
    private void update0(JLabel limit, String format, DefaultStyledDocument doc) {
        limit.setText(String.format(format, doc.getLength()));
    }
    
    private String timeStamp(int limit) {
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
        if(button.getToolTipText().equals(SAVE0)) {
            onSave();
        }
        else {
            Panel panel = Panel.getInstance();
            panel.show(button.getName());
            refresh();
        }
    }
    
    private void onSave() {
        event       = eventInput.getText();
        description = descriptionInput.getText();
        location    = locationInput.getText();
        
        int sH = (int)startModelH.getValue();
        int sM = (int)startModelM.getValue();
        int eH = (int)endModelH.getValue();
        int eM = (int)endModelM.getValue();
        
        start = String.format("%d.%02d.00", sH, sM);
        end   = String.format("%d.%02d.00", eH, eM);
        
        DatabaseControl dtb = DatabaseControl.getInstance();
        
        try {
            Frame  f = Frame.getInstance();
            String m = "Are you sure to save this event?";
            String t = "Event Planner";
            int    q = JOptionPane.OK_CANCEL_OPTION;
            int    o = JOptionPane.OK_OPTION;
            if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
                dtb.insert(event, description, location, date, year, month, day, start, end);
                Panel.getInstance().show(EventsDisplay.NAME);
                EventsDisplay.getInstance().refresh(y_, m_, d_);
                refresh();
            }
        } catch (SQLException ex) { ex.printStackTrace(System.out); }
    }
    
    public void setDate(int year, int month, int day) {
        setParameters(year, month, day);
        this.year  = String.valueOf(year);
        this.month = CalendarPane.MONTHS[month];
        this.day   = String.valueOf(day);
        dateLabel.setText(formatDate(this.month, this.day ,this.year));
        
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
        eventInput.setText("");
        descriptionInput.setText("");
        locationInput.setText("");
        startHour.setValue(0);
        startMinute.setValue(0);      
        endHour.setValue(0);          
        endMinute.setValue(0);        
    }
    
    public static EventForm getInstance() {
        return Holder.INSTANCE;
    }
   
    private static class Holder {
        private static final EventForm INSTANCE = new EventForm();
    }
}