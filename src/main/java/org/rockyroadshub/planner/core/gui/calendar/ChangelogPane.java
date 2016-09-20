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

import com.jcabi.aspects.LogExceptions;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.gui.AbstractPane;
import org.rockyroadshub.planner.core.gui.GUIUtils;
import org.rockyroadshub.planner.core.gui.MainPane;
import org.rockyroadshub.planner.core.gui.TButton;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.loader.Icons;
import org.rockyroadshub.planner.loader.Property;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.2
 */
@SuppressWarnings("serial")
public final class ChangelogPane extends AbstractPane {

    private ChangelogPane() {
        initialize();
    }

    public static ChangelogPane getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class Holder {
        private static final ChangelogPane INSTANCE = new ChangelogPane();
    }

    public static final String NAME = "ChangelogPane";
    
    private final JEditorPane readme = new JEditorPane();
    private final TButton closeButton = new TButton();
    private final JPanel menuPane = new JPanel();
    private final JCheckBox checkBox = new JCheckBox("Show changelogs every startup");
    private JScrollPane scrollPane;

    private PropertyLoader properties;

    private final ActionListener action = (ActionEvent ae) -> {
        Object obj = ae.getSource();
        if(obj instanceof JButton) {
            onTrigger((JButton)obj);
        }
        else if(obj instanceof JCheckBox) {
            onTrigger((JCheckBox)obj);
        }
    };
    
    private final HyperlinkListener hyper = (HyperlinkEvent he) -> {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(he.getEventType())) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(he.getURL().toURI());
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
    };
    
    private void initialize() {
        setOpaque(false);
        setName(NAME);      
        setLayout(new BorderLayout());

        properties = PropertyLoader.getInstance();
        
        initButtons();
        initCheckBox();
        initMenuPane();
        
        if(checkBox.isSelected()) {
            try {
                initChangelogPane();
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
            finally {
                initScrollPane();
                pack();
            }
        }
    }
    
    private void initButtons() {
        closeButton.setToolTipText(Globals.CLOSE);
        closeButton.setName(CalendarPane.NAME);
        closeButton.addActionListener(action);
        closeButton.setIcon(Icons.CLOSE.icon());
    }
    
    private void initCheckBox() {
        checkBox.setSelected(properties.changelog_is_display);
        checkBox.addActionListener(action);
    }
    
    private void initMenuPane() {      
        menuPane.setLayout(new MigLayout(
                Globals.BUTTON_INSETS,
                Globals.BUTTON_GAPX,
                Globals.BUTTON_GAPY));
        menuPane.setOpaque(false);
        menuPane.add(closeButton, Globals.BUTTON_DIMENSIONS);
        menuPane.add(checkBox);        
    }
    
    @LogExceptions
    private void initChangelogPane() throws MalformedURLException, IOException {
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("a:hover{color:red;}");
        Document doc = kit.createDefaultDocument();
        GUIUtils.addToPaneList(this);
        readme.setDocument(doc);
        readme.setPage(new File("changelogs/README.html").toURI().toURL());
        readme.setEditable(false);
        readme.setContentType("text/html");
        readme.addHyperlinkListener(hyper);
    }
    
    private void initScrollPane() {
        scrollPane = new JScrollPane(readme);
        scrollPane.setVisible(true);
    }
    
    private void pack() {
        add(menuPane, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);  
    }

    private void onTrigger(JButton button) {
        String name = button.getName();
        switch(name) {
            case CalendarPane.NAME: onClose(); break;
        }
    }
    
    private void onTrigger(JCheckBox checkBox) {
        properties.setProperty(Property.CHANGELOG_IS_DISPLAY, 
                String.valueOf(checkBox.isSelected()));
        properties.commit();
    }
    
    private void onClose() {
        MainPane.getInstance().showPane(CalendarPane.NAME);
    }
 }