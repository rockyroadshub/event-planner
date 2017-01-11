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

import org.rockyroadshub.planner.core.gui.calendar.CalendarPane;
import org.rockyroadshub.planner.core.utils.Utilities;
import javax.swing.JPanel;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @since 0.1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractPane extends JPanel implements GUI {
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
        setTitleLabel(Utilities.formatDate(CalendarPane.MONTHS[month-1],day,year));
    }
    
    @Override
    public void setDate(String date) {
        targetDate = date;
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