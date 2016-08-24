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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Members {
    
    private final StringBuilder createFormat0 = new StringBuilder("CREATE TABLE @ (");
    
    private final List<String> columns = new ArrayList<>();
    private final List<String> activeColumns = new ArrayList<>();
    private final List<Integer> displayColumns = new ArrayList<>();
    private final Map<String, String> columnAltTexts = new HashMap<>();
    
    private String createFormat;
    private String insertFormat;
    private String updateFormat;
    private String deleteFormat;
    private String selectFormat;
    private String mainKey;
    
//    private String findFormat0 = "SELECT * FROM @ WHERE ";;
//    private String findFormat1 = "SELECT * FROM @ WHERE %s = '%s'";
//    private String findFormat2 = "SELECT * FROM @ WHERE %s = '%s' OR %s = '%s'";
//    private String findFormat3 = "SELECT * FROM @ WHERE %s = '%s' AND %s = '%s'";
    
    private boolean keyExists = false;
    
    public Members() {}
    
    /**
     * Adds a column member
     * @param column column label
     * @param type column type (e.g. VARCHAR(20))
     * @param isMainKey parameter if the added column is a primary/main key
     * @return 
     */
    public Members add(String column, String type, boolean isMainKey) {   
        createFormat0.append(column)
                     .append(" ")
                     .append(type)
                     .append(",");
        
        columns.add(column);
        columnAltTexts.put(column, column);
        
        if(!isMainKey) {
            activeColumns.add(column);
        }
        else if(!keyExists) {
            mainKey = column;
            keyExists = true;
        }
        
        return this;
    }
  
    /**
     * Sets the columns to be displayed in the display panel. First <br>
     * column is 0, the second is 1.. and so on.
     * @param displayColumns columns to be displayed (as index)
     * @return 
     */
    public Members setDisplayColumns(Integer... displayColumns) {
        this.displayColumns.addAll(Arrays.asList(displayColumns));
        
        return this;
    }
    
    public Members pack() {
        createFormat0.replace(createFormat0.length()-1, createFormat0.length(), ")");
        createFormat = createFormat0.toString();
        
        StringBuilder insertColumn  = new StringBuilder("INSERT INTO @ (");
        StringBuilder insertValue   = new StringBuilder("VALUES (");
        StringBuilder updateFormat0 = new StringBuilder("UPDATE @ SET ");
        String deleteFormat0 = "DELETE FROM @ WHERE mainKey = %d";
        String selectFormat0 = "SELECT * FROM @ WHERE mainKey = %d";
        
        activeColumns.stream().map((column) -> {
            insertColumn.append(column).append(",");
            return column;
        }).forEach((column) -> {
            insertValue.append("'%s',");
            updateFormat0.append(column)
                         .append(" = '%s',");
        });
        
        insertColumn.replace(insertColumn.length()-1, insertColumn.length(), ") ");
        insertValue.replace(insertValue.length()-1, insertValue.length(), ")"); 
        updateFormat0.replace(updateFormat0.length()-1, updateFormat0.length(), " ")
                     .append("WHERE ").append(mainKey).append(" = %d"); 
        
        
        insertFormat = insertColumn.toString() + insertValue.toString();
        updateFormat = updateFormat0.toString();
        deleteFormat = deleteFormat0.replaceAll("mainKey", mainKey);
        selectFormat = selectFormat0.replaceAll("mainKey", mainKey);
        
        return this;
    }
    
    public String getCreateFormat() {
        return createFormat;
    }
    
    public void setCreateFormat(String createFormat) {
        this.createFormat = createFormat;
    }
    
    public String getInsertFormat() {
        return insertFormat;
    }
    
    public void setInsertFormat(String insertFormat) {
        this.insertFormat = insertFormat;
    }
    
    public String getUpdateFormat() {
        return updateFormat;
    }
    
    public void setUpdateFormat(String updateFormat) {
        this.updateFormat = updateFormat;
    }
    
    public String getDeleteFormat() {
        return deleteFormat;
    }
    
    public void setDeleteFormat(String deleteFormat) {
        this.deleteFormat = deleteFormat;
    }
    
    public String getSelectFormat() {
        return selectFormat;
    }
    
    public void setSelectFormat(String selectFormat) {
        this.selectFormat = selectFormat;
    }
    
    
    /**
     * 
     * @return total active columns
     */
    public int getTotalColumns() {
        return activeColumns.size();
    }
    
    /**
     * 
     * @return listed columns
     */
    public List<String> getColumns() {
        return columns;
    }
    
    /**
     * 
     * @return active columns(listed columns without the main/primary key)
     */
    public List<String> getActiveColumns() {
        return activeColumns;
    }
    
    /**
     * 
     * @return columns to be displayed in the display panel
     */
    public List<Integer> getDisplayColumns() {
        return displayColumns;
    }
    
    /**
     * 
     * @return alternative names of displayed columns
     */
    public Map<String, String> getColumnAltTexts() {
        return columnAltTexts;
    }
    
    /**
     * 
     * @param column the column label's index
     * @param altText column label's alternative name 
     */
    public void setColumnAltText(int column, String altText) {
        columnAltTexts.put(columns.get(column), altText);
    }
    
    /**
     * 
     * @param column the column label
     * @param altText column label's alternative name 
     */
    public void setColumnAltText(String column, String altText) {
        columnAltTexts.put(column, altText);
    }

//    public static void main(String[] args) {
//        Members mem = new Members().add("asdf", "qwer", true).add("baka","lang",false).pack()
//                .setDisplayColumns(1,2,5,8,9);
//        Memory  mer = new Memory(mem, "NUB");
//        mem.getDisplayColumns().stream().forEach((i) -> {
//            System.out.println(i);
//        });
//                
//        System.out.println(mer.getMembers().getCreateFormat());
//        System.out.println(mer.getMembers().getInsertFormat());
//        System.out.println(mer.getMembers().getUpdateFormat());
//        System.out.println(mer.getMembers().getDeleteFormat());
//        System.out.println(mer.getMembers().getSelectFormat());
//    }
}
