package org.rockyroadshub.planner.misc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;


public class CustomJProgressBar extends JPanel {
    private static final long serialVersionUID = 1L;
    private Color color;
    private int width, height;
    private double minimum = 0.0;
    private double maximum = 100.0;
    private double value = 100.0;
    private Font font = new Font("Calibri", 1, 12);

    public CustomJProgressBar(Color color) {
        super();
        this.color = color;
        setBounds(0, 0, width, height);
        setFont(font);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //border
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);

        //progress
        int drawAmount = (int) (((value - minimum) / (maximum - minimum)) * getWidth());
        g.setColor(color);
        g.fillRect(1, 1, drawAmount-2, getHeight()-2); //-2 to account for border

        //string painting
        String stringToPaint = (int)value + "/" + (int)maximum;
        Canvas c = new Canvas();
        FontMetrics fm = c.getFontMetrics(font);
        final int stringWidth = fm.stringWidth(stringToPaint);
        final int stringHeight = fm.getHeight();
        g.setColor(Color.WHITE);
        g.drawString(stringToPaint, (getWidth()/2) - (stringWidth/2), ((getHeight()/2) + (stringHeight/2))-2); //-2 to account for border
    }

    public void setColor(Color _color){
        this.color = _color;
    }

    public void setMinimum(double _minimum){
        this.minimum = _minimum;
    }

    public void setMaximum(double _maximum){
        this.maximum = _maximum;
    }

    public void setValue(double _value){
        this.value = _value;
    }
}