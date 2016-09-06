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

package org.rockyroadshub.planner.core.utils;

/**
 * Global configurations and constants used by the Event Planner
 * @author Arnell Christoper D. Dalid
 * @since 0.2.0
 */
public final class Globals {
    private Globals(){}
    
    /**
     * Title of the application's frame
     */
    public static final String FRAME_TITLE = "Event Planner";
    
    //================ Button Names =================
    public static final String ADD      = "Add";
    public static final String BACK     = "Back";
    public static final String CLOSE    = "Close";
    public static final String DEFAULT  = "Default";
    public static final String DELETE   = "Delete";
    public static final String EDIT     = "Edit";
    public static final String HOME     = "Home";
    public static final String SAVE     = "Save";
    public static final String SET      = "Set";
    public static final String SETTINGS = "Settings";
    public static final String VIEW     = "View";
    //================================================
    
    /**
     * Global button dimensions (for MigLayout)
     */
    public static final String BUTTON_DIMENSIONS = "h 32!, w 32!";
    
    /**
     * Path of the application's frame icon
     */
    public static final String FRAME_ICON = "/org/rockyroadshub/planner/src/img/Frame.png";

    //================ Calendar Constants ================
    /**
     * Character limit on event title input
     */
    public static final int EVENT_TITLE_SIZE = 25;
    
    /**
     * Character limit on event description input
     */
    public static final int EVENT_DESCR_SIZE = 200;
    
    /**
     * Character limit on event location input
     */
    public static final int EVENT_LOCAT_SIZE = 200;
    //=====================================================
}
