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

package org.rockyroadshub.planner.gui.panes.alarm;

import org.rockyroadshub.planner.data.Alarm;
import org.rockyroadshub.planner.gui.core.AbstractPane;
import org.rockyroadshub.planner.loader.PropertyLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public class AlarmListPane extends AbstractPane{

    private AlarmListPane() {
        initialize();
    }

    public static AlarmListPane getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void refresh() {
    }

    @Override
    public void clear() {
    }

    private static class Holder {
        private static final AlarmListPane INSTANCE = new AlarmListPane();
    }

    public static final String NAME = "AlarmListPane";

    private PropertyLoader properties;

    private void initialize() {
        setName(NAME);      


        properties = PropertyLoader.getInstance();       

        initComponents();
        pack();
    }
    
    private void initComponents() {
    }

    private void pack() {
    }
    
    public void addAlarm(Alarm alm) {
        revalidate();
    }
}