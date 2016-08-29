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

package org.rockyroadshub.planner.core.gui;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class GUIUtils {
    private GUIUtils(){}
        
    /**
     * List of objects that implements {@code AbstractPane}
     * @see org.rockyroadshub.planner.core.gui.AbstractPane
     */
    public static final List<AbstractPane> PANE_LIST = new ArrayList<>();
    
    public static void addToPaneList(AbstractPane pane) {
        PANE_LIST.add(pane);
    }
    
    public static List<AbstractPane> getPaneList() {
        return PANE_LIST;
    }
    
    public static void packPanels(JComponent component) {
        if(!(component.getLayout() instanceof CardLayout))
            throw new IllegalArgumentException("Component's layout is not 'CardLayout'");
        
        PANE_LIST.stream().forEach((pane) -> {
            component.add(pane, pane.getName());
        });
    }
}