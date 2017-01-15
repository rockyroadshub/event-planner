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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.rockyroadshub.planner.data.Event;
import org.rockyroadshub.planner.data.EventMapper;
import org.rockyroadshub.planner.core.database.Data;
import org.rockyroadshub.planner.gui.core.AbstractPane;
import org.rockyroadshub.planner.gui.core.MainFrame;
import org.rockyroadshub.planner.gui.core.GUIUtils;
import org.rockyroadshub.planner.gui.core.MainPane;
import org.rockyroadshub.planner.utils.Globals;
import org.rockyroadshub.planner.utils.TextLimiter;
import org.rockyroadshub.planner.utils.Utilities;
import org.rockyroadshub.planner.gui.core.Buttons;
import org.rockyroadshub.planner.gui.core.MToolBar;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public final class ViewPane extends AbstractPane {

    private ViewPane() {
        initialize();
    }

    public static ViewPane getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ViewPane INSTANCE = new ViewPane();
    }
    
    public static final String NAME = "viewpane";
    
    private final JLabel       dateTitle        = new JLabel("Date: ");
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
    
    private final MToolBar     menuToolBar      = new MToolBar();
    private final JPanel       viewContainer    = new JPanel();
   
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
    
    private final String[][] buttons = {
        {"BACK", DisplayPane.NAME},
        {"SAVE", Globals.SAVE},
        {"EDIT", Globals.EDIT}
    };
    
    private final JButton[] menuButtons = new JButton[buttons.length];
    
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
    
    private static final String EVT_INPUT    = "Event Title Text Box";
    private static final String DSC_INPUT    = "Description Text Box";
    private static final String LOC_INPUT    = "Location Text Box";    
    private static final String SAVE_DIALOG  = "Are you sure to save these changes?";
    private static final String BORDER       = "View/Edit Panel";
    private static final String START_VS_END = "Start time cannot be more advanced than End time.";
    private static final String START_EQ_END = "Start time is the same with End time";
    private static final String OVERLAP      = "%s overlaps with another event";
    
    
    private void initialize() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setName(NAME);
                
        initDocuments();
        initSpinners();
        initMenu();
        initButtons();
        pack();
        
        GUIUtils.addToPaneList(NAME, this);
    }

    @Override
    public void refresh() {
        clear();
    }

    @Override
    public void clear() {
        eventInput.setText("");
        descriptionInput.setText("");
        locationInput.setText("");
        startHour.setValue(0);
        startMinute.setValue(0);      
        endHour.setValue(0);          
        endMinute.setValue(0);  
    }    
    
    @Override
    public void setDate(int year, int month, int day) {
        super.setDate(year, month, day);
        SwingUtilities.invokeLater(() -> {
            dateLabel.setText(getTitleLabel());
        });
    }
    
    private void initDocuments() {    
        dateLabel.setFont(font);
        
        documentEvt = new DefaultStyledDocument();
        documentEvt.setDocumentFilter(new TextLimiter(Globals.EVENT_TITLE_SIZE));
        documentEvt.addDocumentListener(document);
        formatEvt = Utilities.stamp(Globals.EVENT_TITLE_SIZE);
        eventInput.setFont(font);
        eventInput.setDocument(documentEvt);
        eventInput.setName(EVT_INPUT);
        eventLimit.setText(String.format(formatEvt, 0));
        
        documentDsc = new DefaultStyledDocument();
        documentDsc.setDocumentFilter(new TextLimiter(Globals.EVENT_DESCR_SIZE));
        documentDsc.addDocumentListener(document);
        formatDsc = Utilities.stamp(Globals.EVENT_DESCR_SIZE);
        descriptionInput.setFont(font);
        descriptionInput.setDocument(documentDsc);
        descriptionInput.setName(DSC_INPUT);
        descriptionLimit.setText(String.format(formatDsc, 0));
        descriptionInput.setLineWrap(true);
       
        documentLoc = new DefaultStyledDocument();
        documentLoc.setDocumentFilter(new TextLimiter(Globals.EVENT_LOCAT_SIZE));
        documentLoc.addDocumentListener(document);
        formatLoc = Utilities.stamp(Globals.EVENT_LOCAT_SIZE);
        locationInput.setFont(font);
        locationInput.setDocument(documentLoc);
        locationInput.setName(LOC_INPUT);
        locationLimit.setText(String.format(formatLoc, 0));
        locationInput.setLineWrap(true);
    }
    
    private void initSpinners() {
        setAllowsInvalid(startHour);
        setAllowsInvalid(startMinute, startHour);
        setAllowsInvalid(endHour);
        setAllowsInvalid(endMinute, endHour);
    }
    
    private void initMenu() {
        menuToolBar.setRollover(true);
        menuToolBar.setFloatable(false);
    }
    
    private void initButtons() {
        int i = 0;
        for (String[] button : buttons) {
            menuButtons[i] = new JButton();
            Buttons atr = Buttons.valueOf(button[0]);
            menuButtons[i].setBorderPainted(false);
            menuButtons[i].setFocusPainted(false);
            menuButtons[i].setFocusable(false);
            menuButtons[i].setIcon(atr.icon());
            menuButtons[i].setToolTipText(atr.toolTip());
            menuButtons[i].setName(button[1]);
            menuButtons[i].addActionListener(action);
            menuToolBar.add(menuButtons[i]);
            i++;
        }
    }
    
    private void pack() {
        viewContainer.setLayout(new MigLayout(new LC().fill()));
        viewContainer.setOpaque(false);
        viewContainer.add(dateTitle, "h 32!, split");
        viewContainer.add(dateLabel, "growx, h 32!, wrap");
        viewContainer.add(eventLabel, "h 32!");
        viewContainer.add(eventLimit, "h 32!, align right, wrap");
        viewContainer.add(eventInput, "growx, h 32!, span 3, wrap");
        viewContainer.add(locationLabel, "h 32!");
        viewContainer.add(locationLimit, "h 32!, align right, wrap");
        viewContainer.add(locationInput, "growx, h 75!, span 3, wrap"); 
        viewContainer.add(descriptionLabel, "h 32!");
        viewContainer.add(descriptionLimit, "h 32!, align right, wrap");
        viewContainer.add(descriptionInput, "growx, h 75!, span 3, wrap");
        viewContainer.add(startLabel, "h 32!, growx, split");
        viewContainer.add(startHour, "h 32!, w 64!");
        viewContainer.add(startMinute, "h 32!, w 64!, wrap");
        viewContainer.add(endLabel, "h 32!, growx, split");
        viewContainer.add(endHour, "h 32!, w 64!");
        viewContainer.add(endMinute, "h 32!, w 64!, wrap");
        add(menuToolBar, BorderLayout.PAGE_START);
        add(viewContainer, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder(BORDER));
    }
    
    private void setAllowsInvalid(JSpinner s) {
        JFormattedTextField txt = ((JSpinner.NumberEditor)s.getEditor()).getTextField();
        ((NumberFormatter)txt.getFormatter()).setAllowsInvalid(false);
        s.addChangeListener((ChangeEvent e) -> {
            if(s.getValue().equals(24)) {
                s.setValue(0);
            }
        });
    }
    
    private void setAllowsInvalid(JSpinner s1, JSpinner s2) {
        JFormattedTextField txt = ((JSpinner.NumberEditor)s1.getEditor()).getTextField();
        ((NumberFormatter)txt.getFormatter()).setAllowsInvalid(false);
        s1.addChangeListener((ChangeEvent e) -> {
            if(s1.getValue().equals(60)) {
                s1.setValue(0);
                int i = (int)s2.getValue() + 1;
                s2.setValue(i);
            }
        });
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
        if(isEmpty(eventInput)) return;
        if(isEmpty(locationInput)) return;
        if(isEmpty(descriptionInput)) return;
        
        event       = eventInput.getText();
        description = descriptionInput.getText();
        location    = locationInput.getText();
                
        int sH = (int)startModelH.getValue();
        int sM = (int)startModelM.getValue();
        int eH = (int)endModelH.getValue();
        int eM = (int)endModelM.getValue();
        
        start = String.format("%d:%02d:00", sH, sM);
        end   = String.format("%d:%02d:00", eH, eM);  
                
        if(!compareTime(start, end)) return;
        if(isOverlapping(event, sH, eH)) return;

        if(MainFrame.showConfirmDialog(SAVE_DIALOG)) {
            EventMapper map = EventMapper.getInstance();
            Event evt = new Event();
            
            evt.setID(id);
            evt.setEvent(event);
            evt.setDescription(description);
            evt.setLocation(location);
            evt.setDate(date);
            evt.setStart(start);
            evt.setEnd(end);           
            try {
                map.update(evt);
            } 
            catch (SQLException ex) {
                MainFrame.showErrorDialog(ex.getMessage());
                clear();
                return;
            }
            
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
        menuButtons[1].setEnabled(bool);
        menuButtons[2].setEnabled(!bool);
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
    
    private boolean isOverlapping(String evt, int start, int end) {
        DisplayPane disp = DisplayPane.getInstance();      
        for(int i = start; i < end + 1; i++) {
            int ID = disp.getIDCache().get(i);
            if(ID != id) {
                if(ID != -1) {
                    MainFrame.showErrorDialog(String.format(OVERLAP, evt));
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean compareTime(String start, String end) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date startTime = dateFormat.parse(start);
            Date endTime = dateFormat.parse(end);
            int timeComparison = endTime.compareTo(startTime);
            if(timeComparison == 0 || timeComparison == -1) {
                MainFrame.showErrorDialog((timeComparison == 0) ? START_EQ_END : START_VS_END);
                return false;
            }
            return true;
        }
        catch (ParseException ex) {
            MainFrame.showErrorDialog(ex.getMessage());
            return false;
        }
    }
    
    private static boolean isEmpty(JTextComponent comp) {
        boolean b = StringUtils.isBlank(comp.getText());
        if(b) {
            MainFrame.showErrorDialog(comp.getName() + " is empty.");
        }
        return b;
    }
}