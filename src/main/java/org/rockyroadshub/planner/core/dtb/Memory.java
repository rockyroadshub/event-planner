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

package org.rockyroadshub.planner.core.dtb;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 1.8
 */
public final class Memory {

    private String schemaPattern;
    private String tableNamePattern;
    
    private final Members members;  
    private final String identifier;
    
    public Memory(Members members, String tableNamePattern) {
        this(members, "null", tableNamePattern);
    }
    
    public Memory(Members members, String schemaPattern, String tableNamePattern) {
        this.members = members;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        
        if(schemaPattern.equals("null")) {
            identifier = tableNamePattern;
        }
        else {
            identifier = schemaPattern + "." + tableNamePattern;
        }
        
        initializeFormats();
    }
    
    private void initializeFormats() {
        StringBuilder create = new StringBuilder(members.getCreateFormat());
        StringBuilder insert = new StringBuilder(members.getInsertFormat());
        StringBuilder update = new StringBuilder(members.getUpdateFormat());
        StringBuilder delete = new StringBuilder(members.getDeleteFormat());
        
        create.replace(13, 15, identifier);
        insert.replace(12, 14, identifier);
        update.replace(7, 9, identifier);
        delete.replace(12, 14, identifier);
        
        members.setCreateFormat(create.toString());
        members.setInsertFormat(insert.toString());
        members.setUpdateFormat(update.toString());
        members.setDeleteFormat(delete.toString());
    }
    
    public Members getMembers() {
        return members;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public String getSchemaPattern() {
        return schemaPattern;
    }
    
    public String getTableNamePattern() {
        return tableNamePattern;
    }
    
    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }
    
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }
}
