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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @param <L>
 * @since 0.1.2
 */
public abstract class AbstractLoader<L> {
    /**
     * Loader Map where loaded entities are saved.
     * 
     */
    protected final Map<String, L> loaderMap = new HashMap<>();
    
    public abstract void load();
    
    protected void add(String name, L entity) {
        loaderMap.put(name, entity);
    }
    
    public synchronized L get(String name) {
        return loaderMap.get(name);
    }
    
    public synchronized Map<String, L> getMap() {
        return loaderMap;
    }
}
