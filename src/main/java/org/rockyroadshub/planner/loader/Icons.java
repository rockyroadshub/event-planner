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

package org.rockyroadshub.planner.loader;

import javax.swing.Icon;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.2
 */
public enum Icons {
    ADD("Add"),
    APPLY("Apply"),
    BACK("Back"),
    CLOSE("Close"),
    CURRENT("Current"),
    CUT("Cut"),
    DEFAULT("Default"),
    DELETE("Delete"),
    EDIT("Edit"),
    HELP("Help"),
    HOME("Home"),
    NEXT("Next"),
    SAVE("Save"),
    SEARCH("Search"),
    SETTINGS("Settings"),
    VIEW("View");

    private final String name;
    Icons(String name) {
        this.name = name;
    }
    
    public Icon icon() {
        IconLoader i = IconLoader.getInstance();
        return i.get(name);
    }
}
