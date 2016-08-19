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
package org.rockyroadshub.planner.core;

import java.util.Map;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @version 0.0.0
 * @since 2016-08-13
 */
public class InitializableControl {
    
    private InitializableControl() {}
    
    public static void addToQueue(Initializable mem) {
        Map<Integer, Initializable> queue = Initializable.QUEUE;
        queue.put(mem.getPriority(), mem);
    }
    
    public static void initialize() {
        int size = Initializable.QUEUE.size();
        for(int i = 0; i < size; i++) {
            Initializable mem = Initializable.QUEUE.get(i);
            mem.initialize();
        }
    }
}
