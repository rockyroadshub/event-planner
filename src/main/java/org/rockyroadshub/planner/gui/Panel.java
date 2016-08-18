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

import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class Panel extends JPanel {
    public static final String NAME = "panel";
        
    private Panel () {
        initialize();
    }
    
    private void initialize() {
        setOpaque(false);
        setLayout(new CardLayout());
        setName(NAME);
        
        pack();
    }
    
    public CardLayout getCardLayout() {
        return (CardLayout)getLayout();
    }
    
    public void show(String name) {
        ((CardLayout)getLayout()).show(this, name);
    }
    
    private void pack() {
        CalendarPane  calendar = CalendarPane.getInstance();
        EventForm     form     = EventForm.getInstance();
        EventsDisplay display  = EventsDisplay.getInstance();
        EventView     view     = EventView.getInstance();
        
        add(calendar, calendar.getName());
        add(form    , form.getName());
        add(display , display.getName());
        add(view    , view.getName());
    }

    public static Panel getInstance() {
        return Holder.INSTANCE;
    }
    
    private static final class Holder {
        private static final Panel INSTANCE = new Panel();
    }  
}
