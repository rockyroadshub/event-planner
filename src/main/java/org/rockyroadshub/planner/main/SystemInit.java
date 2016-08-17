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
package org.rockyroadshub.planner.main;

import javax.swing.SwingUtilities;
import org.rockyroadshub.planner.database.DatabaseConfig;
import org.rockyroadshub.planner.database.DatabaseConnection;
import org.rockyroadshub.planner.database.DatabaseControl;
import org.rockyroadshub.planner.gui.Frame;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class SystemInit {    
    static {
        System.setProperty("log4j.configurationFile", "lib/log4j2.xml");
        
        DatabaseConnection.getInstance().initialize();
        DatabaseConfig.getInstance().initialize();
        DatabaseControl.getInstance().initialize();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame.getInstance().initialize();
        });
    }
}