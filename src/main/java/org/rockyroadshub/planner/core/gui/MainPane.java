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

import org.rockyroadshub.planner.core.gui.calendar.ViewPane;
import org.rockyroadshub.planner.core.gui.calendar.FormPane;
import org.rockyroadshub.planner.core.gui.calendar.CalendarPane;
import org.rockyroadshub.planner.core.gui.calendar.PropertiesPane;
import org.rockyroadshub.planner.core.gui.calendar.DisplayPane;
import java.awt.CardLayout;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
@SuppressWarnings("serial")
public final class MainPane extends AbstractPane {    
    private MainPane() {
        initialize();
    }

    public static MainPane getInstance() {
        return Holder.INSTANCE;
    }
    
    private static class Holder {
        private static final MainPane INSTANCE = new MainPane();
    }
    
    public static final String NAME = "mainpane";

    private void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new CardLayout());
        
        pack();
    }
    
    private void pack() {
        CalendarPane.getInstance();
        DisplayPane.getInstance();
        FormPane.getInstance();
        ViewPane.getInstance();
        PropertiesPane.getInstance();
        
        GUIUtils.packPanels(this);
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void showPane(String name) {
        ((CardLayout)getLayout()).show(this, name);
    }
 }
