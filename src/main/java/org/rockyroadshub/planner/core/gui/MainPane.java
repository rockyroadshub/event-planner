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

import java.awt.CardLayout;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class MainPane extends AbstractPane {    
    private MainPane() {}

    public static MainPane getInstance() {
        return Holder.INSTANCE;
    }
    
    private static class Holder {
        private static final MainPane INSTANCE = new MainPane();
    }
    
    public static final String NAME = "mainpane";

    @Override
    public void initialize() {
        setOpaque(false);
        setName(NAME);
        setLayout(new CardLayout());
        
        pack();
    }
    
    private void pack() {
        CalendarPane cldr = CalendarPane.getInstance();
        DisplayPane  disp = DisplayPane.getInstance();
        FormPane     form = FormPane.getInstance();
        ViewPane     view = ViewPane.getInstance();
        
        cldr.initialize();
        disp.initialize();
        view.initialize();
        form.initialize();
        
        add(cldr, cldr.getName());
        add(disp, disp.getName());
        add(form, form.getName());
        add(view, view.getName());
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void showPane(String name) {
        ((CardLayout)getLayout()).show(this, name);
    }
 }
