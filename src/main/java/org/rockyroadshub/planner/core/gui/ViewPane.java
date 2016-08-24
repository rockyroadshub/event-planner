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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.rockyroadshub.planner.core.data.Event;
import org.rockyroadshub.planner.core.data.EventMapper;
import org.rockyroadshub.planner.core.database.Data;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.core.utils.TextLimiter;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class ViewPane extends AbstractPane {

    private ViewPane() {}

    public static ViewPane getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ViewPane INSTANCE = new ViewPane();
    }
    
    public static final String NAME = "viewpane";
    
    private final JLabel       dateLabel        = new JLabel();
   
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
    
    private final JPanel       menuPanel        = new JPanel();
    private final JButton      homeButton       = new JButton();
    private final JButton      backButton       = new JButton();
    private final JButton      saveButton       = new JButton();
    private final JButton      editButton       = new JButton();
   
    private final Font font = new Font("MONOSPACED", 0, 12);   
    
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

    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private final DocumentListener document = new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) {update(e);}
        @Override public void removeUpdate(DocumentEvent e) {update(e);}
        @Override public void changedUpdate(DocumentEvent e) {update(e);}    
    };
    
    private DefaultStyledDocument documentEvt;
    private DefaultStyledDocument documentDsc;
    private DefaultStyledDocument documentLoc;
    
    private String formatEvt;
    private String formatDsc; 
    private String formatLoc;
 
    private static final int EVT_LIMIT = 50;
    private static final int DSC_LIMIT = 500;
    private static final int LOC_LIMIT = 250;
    
    private static final String SAVE_DIALOG = "Are you sure to save these changes?";
    
    @Override
    public void initialize() {
        setOpaque(false);
        setLayout(new MigLayout(new LC().fill()));
        setName(NAME);
        
        initDocuments();
        initSpinners();
        initMenu();
        initButtons();
        pack();
    }

    @Override
    public void refresh() {
        clear();
        dateLabel.setText(getTitleLabel());
    }

    @Override
    public void clear() {
        dateLabel.setText("");
        eventInput.setText("");
        descriptionInput.setText("");
        locationInput.setText("");
        startHour.setValue(0);
        startMinute.setValue(0);      
        endHour.setValue(0);          
        endMinute.setValue(0);  
    }    
    
    private void initDocuments() {    
        documentEvt = new DefaultStyledDocument();
        documentEvt.setDocumentFilter(new TextLimiter(EVT_LIMIT));
        documentEvt.addDocumentListener(document);
        formatEvt = stamp(EVT_LIMIT);
        eventInput.setFont(font);
        eventInput.setDocument(documentEvt);
        eventLimit.setText(String.format(formatEvt, 0));
        
        documentDsc = new DefaultStyledDocument();
        documentDsc.setDocumentFilter(new TextLimiter(DSC_LIMIT));
        documentDsc.addDocumentListener(document);
        formatDsc = stamp(DSC_LIMIT);
        descriptionInput.setFont(font);
        descriptionInput.setDocument(documentDsc);
        descriptionLimit.setText(String.format(formatDsc, 0));
        descriptionInput.setLineWrap(true);
       
        documentLoc = new DefaultStyledDocument();
        documentLoc.setDocumentFilter(new TextLimiter(LOC_LIMIT));
        documentLoc.addDocumentListener(document);
        formatLoc = stamp(LOC_LIMIT);
        locationInput.setFont(font);
        locationInput.setDocument(documentLoc);
        locationLimit.setText(String.format(formatLoc, 0));
        locationInput.setLineWrap(true);
    }
    
    private void initSpinners() {
        setAllowsInvalid(startHour);
        setAllowsInvalid(startMinute);
        setAllowsInvalid(endHour);
        setAllowsInvalid(endMinute);
    }
    
    private void initMenu() {
        menuPanel.setLayout(new MigLayout());
        menuPanel.add(homeButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(backButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(saveButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(editButton, Globals.BUTTON_DIMENSIONS);        
    }
    
    private void initButtons() {
        homeButton.setToolTipText(Globals.HOME);
        homeButton.setName(CalendarPane.NAME);
        homeButton.addActionListener(action);
        homeButton.setIcon(Globals.ICONS.get(Globals.HOME));
        
        backButton.setToolTipText(Globals.BACK);
        backButton.setName(DisplayPane.NAME);
        backButton.addActionListener(action);
        backButton.setIcon(Globals.ICONS.get(Globals.BACK));
        
        saveButton.setToolTipText(Globals.SAVE);
        saveButton.setName(Globals.SAVE);
        saveButton.addActionListener(action);
        saveButton.setIcon(Globals.ICONS.get(Globals.SAVE));
        
        editButton.setToolTipText(Globals.EDIT);
        editButton.setName(Globals.EDIT);
        editButton.addActionListener(action);     
        editButton.setIcon(Globals.ICONS.get(Globals.EDIT));
    }
    
    private void pack() {
        add(dateLabel, "h 32!");
        add(menuPanel, "growx, wrap");
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
    
    private void setAllowsInvalid(JSpinner s) {
        JFormattedTextField txt = ((JSpinner.NumberEditor)s.getEditor()).getTextField();
        ((NumberFormatter)txt.getFormatter()).setAllowsInvalid(false);
    }
    
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
    
    private String stamp(int limit) {
        StringBuilder bld = new StringBuilder("(%0");
        int digits = (int)(Math.log10(limit)+1);
        bld.append(digits).append("d/").append(limit).append(")");
        return bld.toString();
    }
    
    private void onTrigger(JButton button) {
        String name = button.getName();
        switch (name) {
            case Globals.SAVE:
                onSave();
                break;
            case Globals.EDIT:
                onEdit();
                break;
            default:
                DisplayPane.getInstance().refresh();
                CalendarPane.getInstance().refresh();
                MainPane.getInstance().showPane(name);
                clear();
                break;
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
        
        start = String.format("%d:%02d:00", sH, sM);
        end   = String.format("%d:%02d:00", eH, eM);  
                
        Frame  f = Frame.getInstance();
        String m = SAVE_DIALOG;
        String t = Globals.FRAME_TITLE;
        int    q = JOptionPane.OK_CANCEL_OPTION;
        int    o = JOptionPane.OK_OPTION;
        if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
            EventMapper map = EventMapper.getInstance();
            Event evt = new Event();
            
            evt.setID(id);
            evt.setEvent(event);
            evt.setDescription(description);
            evt.setLocation(location);
            evt.setDate(date);
            evt.setStart(start);
            evt.setEnd(end);           
            map.update(evt);
            
            DisplayPane.getInstance().getInstance().refresh();
            MainPane.getInstance().showPane(DisplayPane.NAME);
            clear();
        }
    }
    
    private void onEdit() {
        enableGUI(true);
    }
    
    public void enableGUI(boolean bool) {
        eventInput.setEditable(bool);
        descriptionInput.setEditable(bool);
        locationInput.setEditable(bool);
        startHour.setEnabled(bool);
        startMinute.setEnabled(bool);      
        endHour.setEnabled(bool);          
        endMinute.setEnabled(bool);
        editButton.setEnabled(!bool);
        saveButton.setEnabled(bool);
    }
    
    public void set(Data data) {        
        Event evt = (Event)data;
        
        this.id          = evt.getID();
        this.event       = evt.getEvent();
        this.description = evt.getDescription();
        this.location    = evt.getLocation();
        this.date        = evt.getDate();
        this.year        = evt.getYear();
        this.month       = evt.getMonth();
        this.day         = evt.getDay();
        this.start       = evt.getStart();
        this.end         = evt.getEnd();
        
        dateLabel.setText(getTitleLabel());
        eventInput.setText(event);
        descriptionInput.setText(description);
        locationInput.setText(location);
        startHour.setValue(evt.getStartHour());
        startMinute.setValue(evt.getStartMinute());      
        endHour.setValue(evt.getEndHour());          
        endMinute.setValue(evt.getEndMinute());  
    }
}