/*
 * Copyright 2017 Arnell Christoper D. Dalid.
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

import java.awt.event.ActionListener;
import javax.swing.JToolBar;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public class MToolBar extends JToolBar {

    public MToolBar() {
        super();
    }
    
    public void addButtons(String[][] buttons, ActionListener al) {
        for(String[] button : buttons) {
            MButton b = new MButton(button[0], button[1]);
            b.addActionListener(al);
            add(b);
        }
    }
}
