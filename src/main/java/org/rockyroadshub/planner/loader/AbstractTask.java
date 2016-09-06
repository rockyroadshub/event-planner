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

import java.util.List;
import javax.swing.SwingWorker;
import org.rockyroadshub.planner.splash.SplashFrame;

/**
 *
 * @author Arnell Christoper D. Dalid
 * @param <T>
 * @since 0.2.0
 */
public abstract class AbstractTask<T> 
        extends SwingWorker<T, String> 
{
            
    protected SplashFrame frame;
    protected AbstractLoader loader;
    
    public AbstractTask() {}
    
    public AbstractTask(SplashFrame frame, AbstractLoader loader) {
        this.frame  = frame;
        this.loader = loader;
        this.addPropertyChangeListener(frame);
    }
    
    public void setSplashFrame(SplashFrame frame) {
        this.frame = frame;
    }
    
    public void setNextLoader(AbstractLoader loader) {
        this.loader = loader;
    }
    
    @Override
    protected void process(List<String> chunks) {
        chunks.stream().forEach((str) -> {
            frame.getProgressLabel().setText(str);
        });
    }

    @Override
    protected abstract T doInBackground() throws Exception;
    
    @Override
    protected void done() {
        loader.load();
    }
}