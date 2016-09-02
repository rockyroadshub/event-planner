package org.rockyroadshub.planner.misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class CustomProgressBarTest {

    public CustomProgressBarTest() {
        createAndShowGui();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomProgressBarTest();
            }
        });
    }

    private void createAndShowGui() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final MyProgressBar myProgressBar = new MyProgressBar(0, 100);
        myProgressBar.setProgressColor(new Color(0, 255, 0, 127));
        //myProgressBar.setIndeterminate(true); //if progress unknown show ball moving from side to side

        JPanel dummyPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 300);
            }
        };

        dummyPanel.add(new JLabel("DummyPanel"));

        frame.add(dummyPanel);

        frame.add(myProgressBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        //create timer to decrease progressbar
        createAndStartDecrementTimer(myProgressBar);

        //create timer to increase progressbar
        //myProgressBar.setValue(0);//set to 0 so we can increment
        //createAndStartIncrementTimer(myProgressBar);
    }

    private void createAndStartIncrementTimer(final MyProgressBar myProgressBar) {
        Timer progressBarIncrementTimer = new Timer(100, new AbstractAction() {
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (count == 100) {
                    System.out.println("Done");
                    ((Timer) ae.getSource()).stop();
                } else if (count < 100) {
                    //System.out.println("Here");
                    count++;
                    myProgressBar.setValue(count);
                }
            }
        });
        progressBarIncrementTimer.start();
    }

    private void createAndStartDecrementTimer(final MyProgressBar myProgressBar) {
        Timer progressBArCountDownTimer = new Timer(100, new AbstractAction() {
            int count = 100;

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (count == 0) {
                    System.out.println("Done");
                    ((Timer) ae.getSource()).stop();
                } else if (count > 0) {
                    count--;
                    myProgressBar.setValue(count);
                    System.out.println(myProgressBar.getValue());
                }
            }
        });
        progressBArCountDownTimer.start();
    }
}

class MyProgressBar extends JPanel {

    private final int minValue, maxValue;
    private boolean indeterminate = false;
    private int currentValue;
    private ArrayList<Rectangle> rects = new ArrayList<>();
    private Color PROGRESS_COLOR = Color.blue;
    private int removeValue = 0;
    private Timer indeterminateTimer;
    private int x = 0, y = 0, ballSize = 25;
    private boolean changeDirection = false;

    public MyProgressBar(int min, int max) {
        indeterminateTimer = new Timer(50, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                repaint();
            }
        });
        maxValue = max;
        minValue = min;
        currentValue = maxValue;
        setBorder(new LineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!indeterminate) {//if normal progress bar
            rects.clear();
            int rectWidths = getWidth() / maxValue;
            int startingX = 0;

            if (currentValue < maxValue) {//we started off with a value less than the max
                for (int i = minValue; i < currentValue; i++) {
                    rects.add(new Rectangle(startingX, 0, rectWidths, getHeight()));
                    startingX += rectWidths;
                }
            } else {
                for (int i = minValue; i < (maxValue - removeValue); i++) {
                    rects.add(new Rectangle(startingX, 0, rectWidths, getHeight()));
                    startingX += rectWidths;
                }
            }

            for (Rectangle r : rects) {
                g2d.setColor(PROGRESS_COLOR);
                g2d.fillRect(r.x, r.y, r.width, r.height);
            }
        } else {//if indeterminate
            if (!indeterminateTimer.isRunning()) {
                indeterminateTimer.start();
            }
            g2d.setColor(PROGRESS_COLOR);
            if (!changeDirection) {
                if (x + 10 < getWidth() - (ballSize / 2)) {
                    x += 10;
                } else {
                    changeDirection = true;
                }
            } else if (changeDirection) {
                if (x + 10 > 0) {
                    x -= 10;
                } else {
                    changeDirection = false;
                }
            }
            g2d.fillOval(x, y, ballSize, getHeight());
        }
    }

    int getValue() {
        return currentValue;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    void setValue(int value) {
        if (value > maxValue) {
            return;
        }
        if (value < minValue) {
            return;
        }
        if (value < currentValue) {
            removeValue++;
        } else {
            int rem = value - currentValue;
            removeValue -= rem;
        }
        currentValue = value;
        repaint();
    }

    void setProgressColor(Color c) {
        PROGRESS_COLOR = c;
    }

    Color getProgressColor() {
        return PROGRESS_COLOR;
    }
}