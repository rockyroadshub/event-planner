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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
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
import org.rockyroadshub.planner.core.gui.AbstractPane;
import org.rockyroadshub.planner.core.gui.MainFrame;
import org.rockyroadshub.planner.core.gui.GUIUtils;
import org.rockyroadshub.planner.loader.IconLoader;
import org.rockyroadshub.planner.core.gui.MainPane;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.core.utils.TextLimiter;
import org.rockyroadshub.planner.core.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
@SuppressWarnings("serial")
public final class FormPane extends AbstractPane {

    private FormPane() {
        initialize();
    }

    public static FormPane getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final FormPane INSTANCE = new FormPane();
    }
    
    public static final String NAME = "formpane";
    
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
   
    private final Font font = new Font("MONOSPACED", 0, 12);   
    
    private String event;
    private String location;
    private String description;
    private String date;
    private String year;
    private String month;
    private String day;
    private String start;
    private String end;
    private IconLoader iconLoader;

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
    
    private static final String SAVE_DIALOG = "Are you sure to save this event?";
    private static final String BORDER      = "Add an Event";
    
    private void initialize() {
        setOpaque(false);
        setLayout(new MigLayout(new LC().fill()));
        setName(NAME);
        
        iconLoader = IconLoader.getInstance();
        
        initDocuments();
        initSpinners();
        initMenu();
        initButtons();
        pack();
        
        GUIUtils.addToPaneList(this);
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
        documentEvt.setDocumentFilter(new TextLimiter(Globals.EVENT_TITLE_SIZE));
        documentEvt.addDocumentListener(document);
        formatEvt = Utilities.stamp(Globals.EVENT_TITLE_SIZE);
        eventInput.setFont(font);
        eventInput.setDocument(documentEvt);
        eventLimit.setText(String.format(formatEvt, 0));
        
        documentDsc = new DefaultStyledDocument();
        documentDsc.setDocumentFilter(new TextLimiter(Globals.EVENT_DESCR_SIZE));
        documentDsc.addDocumentListener(document);
        formatDsc = Utilities.stamp(Globals.EVENT_DESCR_SIZE);
        descriptionInput.setFont(font);
        descriptionInput.setDocument(documentDsc);
        descriptionLimit.setText(String.format(formatDsc, 0));
        descriptionInput.setLineWrap(true);
       
        documentLoc = new DefaultStyledDocument();
        documentLoc.setDocumentFilter(new TextLimiter(Globals.EVENT_LOCAT_SIZE));
        documentLoc.addDocumentListener(document);
        formatLoc = Utilities.stamp(Globals.EVENT_LOCAT_SIZE);
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
        menuPanel.add(dateLabel, "h 32!, gapright 35");
        menuPanel.add(homeButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(backButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.add(saveButton, Globals.BUTTON_DIMENSIONS);
        menuPanel.setBorder(BorderFactory.createTitledBorder(BORDER));
    }
    
    private void initButtons() {
        homeButton.setToolTipText(Globals.HOME);
        homeButton.setName(CalendarPane.NAME);
        homeButton.addActionListener(action);
        homeButton.setIcon(iconLoader.get(Globals.HOME));
        
        backButton.setToolTipText(Globals.BACK);
        backButton.setName(DisplayPane.NAME);
        backButton.addActionListener(action);
        backButton.setIcon(iconLoader.get(Globals.BACK));
        
        saveButton.setToolTipText(Globals.SAVE);
        saveButton.setName(Globals.SAVE);
        saveButton.addActionListener(action);
        saveButton.setIcon(iconLoader.get(Globals.SAVE));
    }
    
    private void pack() {
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
        add(startLabel, "h 32!, growx, split");
        add(startHour, "h 32!, w 64!");
        add(startMinute, "h 32!, w 64!, wrap");
        add(endLabel, "h 32!, growx, split");
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
    
    private void onTrigger(JButton button) {
        String name = button.getName();
        
        switch(name) {
            case Globals.SAVE:
                onSave();
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
        date        = getDate();       
        
        int sH = (int)startModelH.getValue();
        int sM = (int)startModelM.getValue();
        int eH = (int)endModelH.getValue();
        int eM = (int)endModelM.getValue();
        
        start = String.format("%d:%02d:00", sH, sM);
        end   = String.format("%d:%02d:00", eH, eM);   
                        
        MainFrame  f = MainFrame.getInstance();
        String m = SAVE_DIALOG;
        String t = Globals.FRAME_TITLE;
        int    q = JOptionPane.OK_CANCEL_OPTION;
        int    o = JOptionPane.OK_OPTION;
        if(JOptionPane.showConfirmDialog(f, m, t, q) == o) {
            EventMapper map = EventMapper.getInstance();
            Event evt = new Event();
                        
            evt.setEvent(event);
            evt.setDescription(description);
            evt.setLocation(location);
            evt.setDate(date);
            evt.setStart(start);
            evt.setEnd(end);
            map.insert(evt);
            
            DisplayPane.getInstance().getInstance().refresh();
            MainPane.getInstance().showPane(DisplayPane.NAME);
            clear();
        }
    }
}