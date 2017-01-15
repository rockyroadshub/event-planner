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

package org.rockyroadshub.planner.gui.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.2
 */
@SuppressWarnings("serial")
public class TButton extends JButton {
    private Color hoverColor;
    private Color fillColor;
    private Color pressColor;
    private Color borderColor;
    private boolean isBordered = false;
    private String string = null;
    public static final Color DEFAULT_HOVER = new Color(50,200,255,80);
    public static final Color DEFAULT_FILLED = new Color(0,0,0,0);
    
    public TButton() {
        super();
        init();
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon  the Icon image to display on the button
     */
    public TButton(Icon icon) {
        super(icon);
        init();
    }

    /**
     * Creates a button with text.
     *
     * @param text  the text of the button
     */
    public TButton(String text) {
        super(text);
        init();
    }

    /**
     * Creates a button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     *
     * @since 1.3
     */
    public TButton(Action a) {
        super(a);
        init();
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text  the text of the button
     * @param icon  the Icon image to display on the button
     */
    public TButton(String text, Icon icon) {
        super(text, icon);
        init();
    }
    
    private void init() {
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        fillColor = DEFAULT_FILLED;
        setPressColor(DEFAULT_HOVER.darker());
        setHoverColor(DEFAULT_HOVER);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if(getModel().isPressed()) {          
            g2d.setColor(pressColor.darker());
            g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
            g2d.setColor(pressColor);
        } 
        else if(getModel().isRollover()) {           
            g2d.setColor(pressColor.darker());
            g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
            g2d.setColor(hoverColor);         
        } 
        else {
            g2d.setColor(fillColor);
        }
        
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        if(isBordered) {
            g2d.setColor(borderColor);
            g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
        }
        
        String s = (string == null) ? "" : string;
        Canvas c = new Canvas();
        FontMetrics fm = c.getFontMetrics(getFont());
        final int stringWidth = fm.stringWidth(s);
        final int stringHeight = fm.getHeight();
        g2d.setColor(isEnabled() ? getForeground() : getForeground().darker());
        g2d.drawString(s, (getWidth()/2) - (stringWidth/2), 
                         ((getHeight()/2) + (stringHeight/2))-2);
        
        super.paintComponent(g);
    }

    public void setString(String string) {
        this.string = string;
        revalidate();
        repaint();
    }
    
    public String getString() {
        return string;
    }
        
    public void setHoverColor(Color color) {
        hoverColor = color;
        revalidate();
        repaint();
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setPressColor(Color color) {
        pressColor = color;
        revalidate();
        repaint();
    }

    public Color getPressColor() {
        return pressColor;
    }

    public void setFillColor(Color color) {
        fillColor = color;
        revalidate();
        repaint();
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setBorderColor(Color color) {
        borderColor = color;
        revalidate();
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }
    
    public void setColorAttributes(Color color) {
        setFillColor(color);
        setHoverColor(color.brighter());
        setPressColor(color.darker());
    }
    
    public boolean getIsBordered() {
        return isBordered;
    }
    
    public void setIfBordered(boolean isBordered) {
        this.isBordered = isBordered;
    }
}
