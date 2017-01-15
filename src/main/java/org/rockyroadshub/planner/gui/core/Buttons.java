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

import javax.swing.Icon;
import org.rockyroadshub.planner.loader.IconLoader;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
public enum Buttons {
    ABOUT("About","About Event Planner"),
    ADD("Add","Add"),
    ALARM("Alarm", "View Alarms"),
    APPLY("Apply","Apply"),
    BACK("Back","Back"),
    CLOSE("Close","Close"),
    CURRENT("Current","Set to Current Date"),
    CUT("Cut","Cut"),
    DEFAULT("Default","Revert to Default"),
    DELETE("Delete","Delete"),
    EDIT("Edit","Edit"),
    EVENT("Event","Add an Event"),
    EXIT("Exit","Exit"),
    HELP("Help","Help"),
    HOME("Home","Home"),
    LIST("List","List"),
    NEXT("Next","Next"),
    SAVE("Save","Save"),
    SEARCH("Search","Search"),
    SETTINGS("Settings","Application Settings"),
    VIEW("View","View");
    
    private final String string;
    private final String toolTip;
    Buttons(String string, String toolTip) {
        this.string = string;
        this.toolTip = toolTip;
    }
    
    public Icon icon() {
        IconLoader i = IconLoader.getInstance();
        return i.get(string);
    }
    
    public String toolTip() {
        return toolTip;
    }
    
    @Override
    public String toString() {
        return string;
    }
}
