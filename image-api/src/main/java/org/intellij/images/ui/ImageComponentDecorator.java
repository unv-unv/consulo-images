/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package org.intellij.images.ui;

import consulo.util.dataholder.Key;
import org.intellij.images.editor.ImageZoomModel;

/**
 * Image Component manager. It can toggle backround transparency, grid, etc.
 *
 * @author Alexey Efimov
 */
public interface ImageComponentDecorator {
    Key<ImageComponentDecorator> DATA_KEY = Key.create(ImageComponentDecorator.class.getName());

    void setTransparencyChessboardVisible(boolean visible);

    boolean isTransparencyChessboardVisible();

    /**
     * Return <code>true</code> if this decorator is enabled for this action place.
     *
     * @param place Action place
     * @return <code>true</code> is decorator is enabled
     */
    boolean isEnabledForActionPlace(String place);

    ImageZoomModel getZoomModel();

    void setGridVisible(boolean visible);

    boolean isGridVisible();
}
