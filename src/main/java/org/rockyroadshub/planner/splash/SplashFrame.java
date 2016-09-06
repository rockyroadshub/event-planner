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

package org.rockyroadshub.planner.splash;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.core.utils.Utilities;
import org.rockyroadshub.planner.loader.ConnectionLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
@SuppressWarnings("serial")
public class SplashFrame extends JFrame implements PropertyChangeListener {
    private final JPanel        progressPane    = new JPanel();
    private final Title         progressTitle   = new Title();
    private final ProgressBar   progressBar     = new ProgressBar(new Color(50, 130, 180, 200));   
    private final JLabel        progressLabel   = new JLabel();
    private final Background    background      = new Background();
    private final Logo          logo            = new Logo();
        
    private static final Font   TITLE_FONT      = new Font("Calibri", 0, 30);
    private static final String TITLE_TEXT      = "Event Planner 0.2.0";
    private static final Font   LABEL_FONT      = new Font("Calibri", 0, 10);
    
    private static final Color  TEXT_COLOR      = Color.WHITE;
    
    private static final String PATH_FONT       = "/org/rockyroadshub/planner/src/fnt/Digitalt.ttf";
    private static final String PATH_BACKGROUND = "/org/rockyroadshub/planner/src/img/Splash.jpg";
    private static final String PATH_LOGO       = "/org/rockyroadshub/planner/src/img/Frame.png";
    
    private Font progressFont;
        
    private SplashFrame() {
        initialize();
    }

    private static class Holder {
        private static final SplashFrame INSTANCE = new SplashFrame();
    }

    public static SplashFrame getInstance() {
        return Holder.INSTANCE;
    }

    public JPanel getProgressPane() {
        return progressPane;
    }

    public JLabel getProgressLabel() {
        return progressLabel;
    }
    
    public ProgressBar getProgressBar() {
        return progressBar;
    }
    
    private void initialize() {        
        SwingUtilities.invokeLater(() -> {
            create();
        });
        
        progressLabel.setText("Initializing...");
        Timer timer = new Timer("initialize");
        timer.schedule(new Task(), 3000L);
    }
    
    private void initFont() {
        try {
            progressFont = Utilities.getFont(PATH_FONT);
        } 
        catch (IOException | FontFormatException ex) {
            ex.printStackTrace(System.out);
            progressFont = TITLE_FONT;
        }
    }
    
    private void initProgressPane() {
        progressPane.setLayout(new BorderLayout());
        progressPane.setOpaque(false);
        progressPane.add(progressTitle, BorderLayout.NORTH);
        progressPane.add(progressBar, BorderLayout.CENTER);
        progressPane.add(progressLabel, BorderLayout.SOUTH);
    }
    
    private void initProgressTitle() {
        progressTitle.setForeground(TEXT_COLOR);
        progressTitle.setPreferredSize(new Dimension(300,35));
        progressTitle.setText(TITLE_TEXT);
        progressTitle.setFont(progressFont.deriveFont(30f));
    }
    
    private void initProgressLabel() {
        progressLabel.setFont(LABEL_FONT);
        progressLabel.setForeground(TEXT_COLOR);
    }
    
    private void initProgressBar() {
        progressBar.setPreferredSize(new Dimension(300,15));
        progressBar.setOpaque(false);
        progressBar.setFont(LABEL_FONT);
        progressBar.setForeground(TEXT_COLOR);
    }
    
    private void initFrameIcon() {
        try {
            setIconImage(Utilities.getBufferedImage(Globals.FRAME_ICON));
        } 
        catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(String.valueOf(evt.getNewValue()).equals("STARTED")) {
            progressBar.setValue(0);
        }
        else if(evt.getPropertyName().equals("progress")) {
            int i = (Integer)evt.getNewValue();
            progressBar.setValue(i);
        }
    }
    
    @Override
    public void pack() {
        background.add(logo, "h 241!, w 208!");
        background.add(progressPane);
        add(background, BorderLayout.CENTER);
        super.pack();
    }
    
    private void create() {
        setLayout(new BorderLayout());
        setSize(500, 255);
        setPreferredSize(new Dimension(500, 255));
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initFont();
        initProgressTitle();
        initProgressLabel();
        initProgressBar();
        initProgressPane();
        initFrameIcon();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private final class Task extends TimerTask {
        @Override
        public void run() {
            ConnectionLoader.getInstance().load();
        }      
    }
    
    private final class Background extends JPanel {
        Background() {
            initialize();
        }
        
        private void initialize() {
            setLayout(new MigLayout());  
            loadImage();
        }
        
        BufferedImage img;
        private void loadImage() {
            try {
                img = Utilities.getBufferedImage(PATH_BACKGROUND);
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
        
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, 
                    SplashFrame.getInstance().getWidth(), 
                    SplashFrame.getInstance().getHeight(), 
                    this);
        }
    }
    
    private final class Logo extends JPanel {
        Logo() {
            initialize();
        }
        
        private void initialize() {
            setOpaque(false);
            setLayout(new MigLayout());  
            loadImage();
        }
        
        BufferedImage img;
        private void loadImage() {
            try {
                img = Utilities.getBufferedImage(PATH_LOGO);
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
        }
        
        @Override 
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this);
        }
    }
    
    private final class Title extends JPanel {
        private String text;
        
        Title() {
            initialize();
        }
        
        private void initialize() {
            setOpaque(false);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            String stringToPaint = text;
            Canvas c = new Canvas();
            FontMetrics fm = c.getFontMetrics(getFont());
            final int stringHeight = fm.getHeight();
            g2d.setColor(getForeground());
            g2d.drawString(stringToPaint, 0, ((getHeight()/2) + (stringHeight/2))-2);
        }
        
        private void setText(String text) {
            this.text = text;
        }
    }
 }
