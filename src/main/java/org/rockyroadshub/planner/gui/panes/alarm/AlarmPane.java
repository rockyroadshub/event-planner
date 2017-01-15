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
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.rockyroadshub.planner.gui.core.AbstractPane;
import org.rockyroadshub.planner.gui.core.GUIUtils;
import org.rockyroadshub.planner.gui.core.MToolBar;
import org.rockyroadshub.planner.utils.Globals;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public class AlarmPane extends AbstractPane{

    private AlarmPane() {
        initialize();
    }

    public static AlarmPane getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {
    }
    
    private static class Holder {
        private static final AlarmPane INSTANCE = new AlarmPane();
    }

    public static final String NAME = "AlarmPane";
    
    private final MToolBar menuToolBar = new MToolBar();
    private final JPanel container = new JPanel();
    private final CardLayout layout = new CardLayout();
    private final Map<String, AbstractPane> paneList = new HashMap<>();
    
    private final ActionListener action = (ActionEvent ae) -> {
        JButton button = (JButton)ae.getSource();
        onTrigger(button);
    };
    
    private final String[][] buttons = {
        {"LIST",   AlarmListPane.NAME},
        {"ADD",    AlarmAddPane.NAME},
        {"DELETE", Globals.DELETE}
    };
    
    private static final String BORDER = "Alarms";

    private PropertyLoader properties;

    private void initialize() {
        setOpaque(false);
        setName(NAME);      
        setLayout(new BorderLayout());


        properties = PropertyLoader.getInstance();       

        initMenu();
        initComponents();
        initContainer();
        pack();

        GUIUtils.addToPaneList(NAME, this);
    }
    
    private void initMenu() {
        menuToolBar.setRollover(true);
        menuToolBar.setFloatable(false);
        menuToolBar.addButtons(buttons, action);
    }
    
    private void initComponents() {
    }
    
    private void initContainer() {
        container.setOpaque(false);
        container.setLayout(layout);
        addToPaneList(AlarmAddPane.NAME, AlarmAddPane.getInstance());
        addToPaneList(AlarmListPane.NAME, AlarmListPane.getInstance());
        showPane(AlarmListPane.NAME);
    }

    private void pack() {
        add(menuToolBar, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder(BORDER));
    }

    private void onTrigger(JButton button) {
        String name = button.getName();
        switch(name) {
            case Globals.DELETE : onDelete();
                                  break;
            default             : onDefault(name);
                                  break;
        }
        
    }
    
    private void onDelete() {
    }
    
    private void onDefault(String name) {
        getPane(name).refresh();
        showPane(name);  
    }
    
    public AbstractPane getPane(String name) {
        return paneList.get(name);
    }
    
    public void addToPaneList(String name, AbstractPane pane) {
        container.add(pane, name);
        paneList.put(name, pane);
    }
    
    public void showPane(String name) {
        layout.show(container, name);
    }
    
    public void showListPane() {
        showPane(AlarmListPane.NAME);
    }
    
    public void showAddPane() {
        showPane(AlarmAddPane.NAME);
    }
}
