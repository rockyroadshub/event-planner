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

package org.rockyroadshub.planner.core.database;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public abstract class DataMapper implements Mapper {
    /**
     * Memory used by the mapper
     */
    private Memory memory;
    
    /**
     * Sets the memory to be accessed by the mapper
     * @param memory memory to be accessed
     */
    public final void setMemory(Memory memory) {
        this.memory = memory;
    }
    
    /**
     * Gets all the members of the set memory
     * @return members of the memory(columns)
     */
    public final Members getMembers() {
        return memory.getMembers();
    }
    
    /**
     * Sets the display column's alternative name
     * @param altTexts alternative text parameter
     */
    public final void setDisplayAltTexts(String[] altTexts) {
        int length = altTexts.length;
        List<Integer> display = getMembers().getDisplayColumns();
        
        if(display.size() != length)
            throw new IllegalArgumentException("Number of columns"
                    + " does not match with the number of alternative texts.");
        
        for(int i = 0; i < length; i++) {
            getMembers().setColumnAltText(display.get(i), altTexts[i]);
        }
    }
    
    /**
     * Sets the column's alternative name
     * @param column column's index
     * @param altText alternative text parameter
     */
    public final void setColumnAltText(int column, String altText) {
        getMembers().setColumnAltText(column, altText);
    }
    
    /**
     * Sets the column's alternative name
     * @param column column's label
     * @param altText alternative text parameter
     */
    public final void setColumnAltText(String column, String altText) {
        getMembers().setColumnAltText(column, altText);
    }
    
    /**
     * Sets the column's alternative name
     * @param altTexts alternative text parameter (in array)
     */
    public final void setColumnAltTexts(String[] altTexts) {
        setColumnAltTexts(getMembers().getColumns(), altTexts);
    }
    
    /**
     * Sets all of the columns' alternative text
     * @param columns columns as list
     * @param altTexts alternative text parameter (in array)
     */
    public final void setColumnAltTexts(List<String> columns, String[] altTexts)
    {
        int length = altTexts.length;
        
        if(columns.size() != length)
            throw new IllegalArgumentException("Number of columns"
                    + " does not match with the number of alternative texts.");
        
        for(int i = 0; i < length; i++) {
            getMembers().setColumnAltText(i, altTexts[i]);
        }
    }  
        
    /**
     * 
     * @return listed columns
     */
    public List<String> getColumns() {
        return getMembers().getColumns();
    }
    
    /**
     * 
     * @return active columns(listed columns without the main/primary key)
     */
    public List<String> getActiveColumns() {
        return getMembers().getActiveColumns();
    }    
    
    /**
     * 
     * @return columns to be displayed in the display panel
     */
    public List<Integer> getDisplayColumns() {
        return getMembers().getDisplayColumns();
    }
    
    /**
     * 
     * @return alternative names of displayed columns
     */
    public Map<String, String> getColumnAltTexts() {
        return getMembers().getColumnAltTexts();
    }
}
