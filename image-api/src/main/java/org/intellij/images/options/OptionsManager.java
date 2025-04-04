/*
 * Copyright 2004-2005 Alexey Efimov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.images.options;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.application.Application;

/**
 * Options manager.
 *
 * @author <a href="mailto:aefimov.box@gmail.com">Alexey Efimov</a>
 */
@ServiceAPI(ComponentScope.APPLICATION)
public abstract class OptionsManager {
    /**
     * Return current options.
     *
     * @return Options
     */
    public abstract Options getOptions();

    public static OptionsManager getInstance() {
        return Application.get().getInstance(OptionsManager.class);
    }
}
