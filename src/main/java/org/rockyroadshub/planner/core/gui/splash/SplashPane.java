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

package org.rockyroadshub.planner.core.gui.splash;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.core.utils.Utilities;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
@SuppressWarnings("serial")
public class SplashPane extends JPanel {    
    private SplashPane() {
        initialize();
    }

    public static SplashPane getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final SplashPane INSTANCE = new SplashPane();
    }

    public static final String NAME = "SplashPane";

    private void initialize() {
        setName(NAME);      
        setOpaque(false);
        setLayout(new BorderLayout());
        loadImage();         
    }
    
    BufferedImage f;
    private void loadImage() {
        try(InputStream in = SplashPane.class.getResourceAsStream(Globals.FRAME_ICON)) {
            f = ImageIO.read(in);
        } catch (IOException ex) {
            Logger.getLogger(SplashPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(f, 0, 0, this);
    }
 }
