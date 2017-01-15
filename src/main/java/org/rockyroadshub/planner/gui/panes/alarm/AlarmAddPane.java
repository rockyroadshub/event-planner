/*
 * Copyright 2017 Arnell Christoper D. Dalid.
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

package org.rockyroadshub.planner.gui.panes.alarm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.rockyroadshub.planner.data.Alarm;
import org.rockyroadshub.planner.data.AlarmMapper;
import org.rockyroadshub.planner.gui.core.AbstractPane;
import org.rockyroadshub.planner.gui.core.MToolBar;
import org.rockyroadshub.planner.gui.core.MainFrame;
import org.rockyroadshub.planner.gui.core.TButton;
import org.rockyroadshub.planner.utils.Globals;
import org.rockyroadshub.planner.utils.TextLimiter;
import org.rockyroadshub.planner.utils.Utilities;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public final class AlarmAddPane extends AbstractPane {

    private AlarmAddPane() {
        initialize();
    }

    public static AlarmAddPane getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void refresh() {
        clear();
    }

    @Override
    public void clear() {
        resetMarkers();
        resetInputs();
    }
    
    private static class Holder {
        private static final AlarmAddPane INSTANCE = new AlarmAddPane();
    }

    public static final String NAME = "AlarmAddPane";
    
    private final JLabel       alarmLimit       = new JLabel();
    private final JLabel       alarmLabel       = new JLabel("Alarm Title");
    private final JTextField   alarmInput       = new JTextField();
    
    private final JLabel       daysLabel        = new JLabel("Alarm Every");
    private final JPanel       daysMenu         = new JPanel();
    
    private final JLabel       descriptionLimit = new JLabel();
    private final JLabel       descriptionLabel = new JLabel("Description");
    private final JTextArea    descriptionInput = new JTextArea();
    
    private final JLabel       timeLabel        = new JLabel("Start (HH:MM)");
    private final SpinnerModel timeModelH       = new SpinnerNumberModel(0,0,24,1);
    private final SpinnerModel timeModelM       = new SpinnerNumberModel(0,0,60,1);    
    private final JSpinner     timeHour         = new JSpinner(timeModelH);
    private final JSpinner     timeMinute       = new JSpinner(timeModelM);
    
    private final MToolBar     menuToolBar      = new MToolBar();
    private final JPanel       alarmContainer   = new JPanel();
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private final DocumentListener document = new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) {update(e);}
        @Override public void removeUpdate(DocumentEvent e) {update(e);}
        @Override public void changedUpdate(DocumentEvent e) {update(e);}    
    };
    
    private final String[][] buttons = {
        {"DEFAULT", ""},
        {"SAVE",    Globals.SAVE},
    };
    
    private static final String MARKER       = "marker";
    private final String[]      markers      = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
    private String              markerCache  = "0";
    private final TButton[]     markerButton = new TButton[markers.length];
    private final Boolean[]     markerState  = new Boolean[markers.length];
    private final Color[]       markerColor  = {Color.WHITE, new Color(50,50,50)};

    private final Font font = new Font("MONOSPACED", 0, 12); 
    
    private String alarm;
    private String description;
    private String time;
    private String days;
    
    private DefaultStyledDocument documentAlm;
    private DefaultStyledDocument documentDsc;
    
    private String formatAlm;
    private String formatDsc; 
    
    private static final String BORDER      = "Set an Alarm";
    private static final String ALM_INPUT   = "Alarm Title Text Box";
    private static final String DSC_INPUT   = "Description Text Box";
    private static final String SAVE_DIALOG = "Are you sure to save this alarm?";
    
    private PropertyLoader properties;

    private void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new BorderLayout());      


        properties = PropertyLoader.getInstance();      
        
        initMenu();
        initDocuments();
        initMarkers();
        initSpinners();
        initComponents();
        pack();
    }
    
    private void initMenu() {
        menuToolBar.setRollover(true);
        menuToolBar.setFloatable(false);
        menuToolBar.addButtons(buttons, action);
    }
    
    private void initDocuments() {
        documentAlm = new DefaultStyledDocument();
        documentAlm.setDocumentFilter(new TextLimiter(Globals.ALARM_TITLE_SIZE));
        documentAlm.addDocumentListener(document);
        formatAlm = Utilities.stamp(Globals.ALARM_TITLE_SIZE);
        alarmInput.setFont(font);
        alarmInput.setDocument(documentAlm);
        alarmInput.setName(ALM_INPUT);
        alarmLimit.setText(String.format(formatAlm, 0));
        
        documentDsc = new DefaultStyledDocument();
        documentDsc.setDocumentFilter(new TextLimiter(Globals.ALARM_DESCR_SIZE));
        documentDsc.addDocumentListener(document);
        formatDsc = Utilities.stamp(Globals.ALARM_DESCR_SIZE);
        descriptionInput.setFont(font);
        descriptionInput.setDocument(documentDsc);
        descriptionInput.setName(DSC_INPUT);
        descriptionLimit.setText(String.format(formatDsc, 0));
        descriptionInput.setLineWrap(true);
    }
    
    private void initMarkers() {
        daysMenu.setLayout(new MigLayout(
                Globals.BUTTON_INSETS,
                Globals.BUTTON_GAPX,
                Globals.BUTTON_GAPY));
        
        for(int i = 0; i < markers.length; i++) {
            markerButton[i] = new TButton();
            markerButton[i].setName(MARKER);
            markerButton[i].setString(markers[i]);
            markerButton[i].setFillColor(Color.WHITE);
            markerButton[i].addActionListener(action);
            markerState[i] = false;
            daysMenu.add(markerButton[i], "h 32!");
        }
    }
    
    private void initSpinners() {
        setAllowsInvalid(timeHour);
        setAllowsInvalid(timeMinute, timeHour);
    }
    
    private void initComponents() {
        alarmContainer.setLayout(new MigLayout(new LC().fillX()));
        alarmContainer.setOpaque(false);    
        alarmContainer.add(alarmLabel, "h 32!");
        alarmContainer.add(alarmLimit, "h 32!, align right, wrap");
        alarmContainer.add(alarmInput, "growx, h 32!, span 3, wrap"); 
        alarmContainer.add(daysLabel, "h 32!, split");
        alarmContainer.add(daysMenu, " h 32!, wrap");
        alarmContainer.add(descriptionLabel, "h 32!");
        alarmContainer.add(descriptionLimit, "h 32!, align right, wrap");
        alarmContainer.add(descriptionInput, "growx, h 150!, span 3, wrap");
        alarmContainer.add(timeLabel, "h 32!, growx, split");
        alarmContainer.add(timeHour, "h 32!, w 64!");
        alarmContainer.add(timeMinute, "h 32!, w 64!, wrap");
    }
    
    private void pack() {
        add(menuToolBar, BorderLayout.PAGE_START);
        add(alarmContainer, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder(BORDER));
    }

    private void onTrigger(JButton button) {
        String name = button.getName();
        switch(name) {
            case Globals.SAVE : onSave();
                                break;
            case MARKER       : onMarker(button);
                                break;
            default           : onDefault();
                                break;
        }
    }
    
    private void onSave() {
        if(isEmpty(alarmInput)) return;
        if(isEmpty(descriptionInput)) return;
        
        alarm       = alarmInput.getText();
        description = descriptionInput.getText();
        
        int tH = (int)timeModelH.getValue();
        int tM = (int)timeModelM.getValue();
        
        time = String.format("%d:%02d:00", tH, tM);
        days = markerCache;
        
        if(MainFrame.showConfirmDialog(SAVE_DIALOG)) {
            AlarmMapper map = AlarmMapper.getInstance();
            Alarm alm = new Alarm();
            
            alm.setAlarm(alarm);
            alm.setDescription(description);
            alm.setTime(time);
            alm.setDays(days);
            
            try {
                map.insert(alm);
            } 
            catch (SQLException ex) {
                MainFrame.showErrorDialog(ex.getMessage());
                clear();
                return;
            }
            
            AlarmListPane.getInstance().addAlarm(alm);
            AlarmPane.getInstance().showListPane();
            clear();
        }
    }
    
    private void onMarker(JButton button) {
        if(button instanceof TButton) {
            TButton b = (TButton)button;
            int i = getMarkerIndex(b.getString());
            markerState[i] = !markerState[i];
            b.setFillColor(markerState[i] ? markerColor[1] : markerColor[0]);
            b.setForeground(markerState[i] ? markerColor[0] : markerColor[1]);
        }
        
        StringBuilder cache0 = new StringBuilder();
        for(boolean bool : markerState) {
            cache0.append((bool) ? 1 : 0);
        }
        
        int cache = Integer.parseInt(cache0.toString(), 2);
        markerCache = Integer.toHexString(cache);
    }
    
    private void onDefault() {
        resetMarkers();
    }
    
    private void resetMarkers() {
        for(int i = 0; i < markers.length; i++) {
            markerButton[i].setFillColor(Color.WHITE);
            markerButton[i].setForeground(Color.BLACK);
            markerState[i] = false;
            markerCache = "0";
        }
    }
    
    private void resetInputs() {
        alarmInput.setText("");
        descriptionInput.setText("");
        timeHour.setValue(0);
        timeMinute.setValue(0);
    }
    
    private void update(DocumentEvent e) {
        DefaultStyledDocument doc = (DefaultStyledDocument)e.getDocument();
        if(doc.equals(documentAlm)) {
            update0(alarmLimit, formatAlm, doc);
        }
        else if(doc.equals(documentDsc)) {
            update0(descriptionLimit, formatDsc, doc);
        }
    }
      
    private void update0(JLabel limit, String format, DefaultStyledDocument doc) {
        limit.setText(String.format(format, doc.getLength()));
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
    
    private int getMarkerIndex(String day) {
        for(int i = 0; i < markers.length; i++) {
            if(markers[i].equals(day)) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean isEmpty(JTextComponent comp) {
        boolean b = StringUtils.isBlank(comp.getText());
        if(b) {
            MainFrame.showErrorDialog(comp.getName() + " is empty.");
        }
        return b;
    }
}
