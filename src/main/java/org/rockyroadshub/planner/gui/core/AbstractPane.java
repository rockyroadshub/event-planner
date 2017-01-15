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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rockyroadshub.planner.gui.panes.calendar.CalendarPane;
import org.rockyroadshub.planner.utils.Utilities;
import javax.swing.JPanel;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.2.3
 */
@SuppressWarnings("serial")
public abstract class AbstractPane extends JPanel implements GUI {
    public static final Pattern DATE_FORMAT = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
    
    private String targetDate;
    private String titleLabel;

    @Override
    public abstract void refresh();

    @Override
    public abstract void clear();

    @Override
    public String getDate() {
        return targetDate;
    }

    @Override
    public void setDate(int year, int month, int day) {
        targetDate = Utilities.formatDate(year,month,day);
        setTitleLabel(Utilities.formatDate(CalendarPane.MONTHS[month-1], day, year));
    }
    
    public void setDate(String date) {
        Matcher m = DATE_FORMAT.matcher(date);
        if(m.matches()) {
            int month = Integer.valueOf(m.group(2));
            int day   = Integer.valueOf(m.group(3));
            int year  = Integer.valueOf(m.group(1));
            setDate(year, month, day);
        }
    }
    
    @Override
    public String getTitleLabel() {
        return titleLabel;
    }
    
    @Override
    public void setTitleLabel(String titleLabel) {
        this.titleLabel = titleLabel;
    }
}