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

import org.rockyroadshub.planner.misc.HumanProgressBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import net.miginfocom.swing.MigLayout;
import org.rockyroadshub.planner.core.utils.Globals;
import org.rockyroadshub.planner.loader.FileLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
@SuppressWarnings("serial")
public class SplashFrame extends JFrame implements PropertyChangeListener {
    private final HumanProgressBar progressBar = new HumanProgressBar();   
    private final JLabel label = new JLabel();
    
    private SplashFrame() {
        initialize();
    }

    public static SplashFrame getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("progress")) {
            progressBar.setValue((Integer)evt.getNewValue());
        }
    }

    private static class Holder {
        private static final SplashFrame INSTANCE = new SplashFrame();
    }

    public static final String NAME = "SplashFrame";


    public JLabel getLabel() {
        return label;
    }
    
    private void initialize() {
        setName(NAME);      
        
        SwingUtilities.invokeLater(() -> {
            setLayout(new MigLayout());
            setTitle(Globals.FRAME_TITLE);
            setSize(500, 255);
            setPreferredSize(new Dimension(500, 255));
            setResizable(false);
            this.setUndecorated(true);
            JPanel x = new JPanel();
            x.setLayout(new BorderLayout());
            x.add(new JLabel("Event Planner"), BorderLayout.NORTH);
            progressBar.setPreferredSize(new Dimension(300,10));
            x.add(progressBar, BorderLayout.CENTER);
            x.add(label, BorderLayout.SOUTH);
            
            add(SplashPane.getInstance(),"h 241!, w 208!");
            add(x);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        });    
        
        Task task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }
    
    class Task extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {               
            return null;
        }
        
        @Override
        protected void done() {
            FileLoader.getInstance().load();
        }
        
    }
 }
