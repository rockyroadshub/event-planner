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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
@SuppressWarnings("serial")
public final class DLabel extends JLabel implements ActionListener{
    SimpleDateFormat dateFormat;
    private final String format;
    private Timer timer;
    
    public DLabel(String format) {
        super();
        this.format = format;
        initialize();
    }
    
    private void initialize() {
        dateFormat = new SimpleDateFormat(format);
        timer = new Timer(50, this);
        timer.start();
    }
    
    public Timer getTimer() {
        return timer;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            setText(dateFormat.format(System.currentTimeMillis()));
        });
    }
}