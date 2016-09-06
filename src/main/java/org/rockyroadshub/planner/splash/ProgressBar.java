package org.rockyroadshub.planner.splash;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
@SuppressWarnings("serial")
public final class ProgressBar extends JPanel {
    private Color color;
    private double minimum = 0.0;
    private double maximum = 100.0;
    private double value = 0.0;
    private boolean indeterminate = false;
    
    private final Timer indeterminateTimer;
    private int x = 0;
    private final int y = 0;
    private final int res = 5;
    private final int ballSize = 25;
    private boolean changeDirection = false;
    
    public ProgressBar(Color color) {
        super();
        indeterminateTimer = new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                repaint();
            }
        });
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(getForeground());
        g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
        
        if(!indeterminate) {
            int drawAmount = (int) (((value - minimum) / (maximum - minimum)) * getWidth());
            g2d.setColor(color);
            g2d.fillRect(1, 1, drawAmount-2, getHeight()-2);

            String stringToPaint = (int)value + "%";
            Canvas c = new Canvas();
            FontMetrics fm = c.getFontMetrics(getFont());
            final int stringWidth = fm.stringWidth(stringToPaint);
            final int stringHeight = fm.getHeight();
            g2d.setColor(getForeground());
            g2d.drawString(stringToPaint, 
                    (getWidth()/2) - (stringWidth/2), 
                    ((getHeight()/2) + (stringHeight/2))-2);
        }
        else {
            if (!indeterminateTimer.isRunning()) {
                indeterminateTimer.start();
            }
            
            g2d.setColor(color);
            if (!changeDirection) {
                if (x + res < getWidth() - (ballSize / 2)) {
                    x += res;
                } else {
                    changeDirection = true;
                }
            } else if (changeDirection) {
                if (x + res > 0) {
                    x -= res;
                } else {
                    changeDirection = false;
                }
            }
            g2d.fillOval(x, y, ballSize, getHeight());
        }
    }
    
    public void setColor(Color color){
        this.color = color;
    }

    public void setMinimum(double minimum){
        this.minimum = minimum;
    }

    public void setMaximum(double maximum){
        this.maximum = maximum;
    }

    public void setValue(double value){
        this.value = value;
        repaint();
    }
    
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }
    
    public Timer getIndeterminateTimer() {
        return indeterminateTimer;
    }
}