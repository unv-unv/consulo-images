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
package org.intellij.images.thumbnail;

import consulo.disposer.Disposable;
import consulo.images.localize.ImagesLocalize;
import consulo.project.Project;
import consulo.util.dataholder.Key;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import org.intellij.images.ui.ImageComponentDecorator;

/**
 * Thumbnail thumbnail is a component with thumbnails for a set of {@link VirtualFile}.
 *
 * @author <a href="mailto:aefimov.box@gmail.com">Alexey Efimov</a>
 */
public interface ThumbnailView extends Disposable, ImageComponentDecorator {
    Key<ThumbnailView> DATA_KEY = Key.create(ThumbnailView.class.getName());

    String TOOLWINDOW_ID = "Thumbnails";

    @Nonnull
    Project getProject();

    /**
     * Add virtual files to view
     *
     * @param root Root
     */
    void setRoot(@Nonnull VirtualFile root);

    /**
     * Return current root
     *
     * @return Current root
     */
    VirtualFile getRoot();

    boolean isRecursive();

    void setRecursive(boolean recursive);

    void setSelected(@Nonnull VirtualFile file, boolean selected);

    boolean isSelected(@Nonnull VirtualFile file);

    @Nonnull
    VirtualFile[] getSelection();

    /**
     * Scroll to selection. If ToolWindow is not active, then
     * it will perform activatation before scroll.
     */
    void scrollToSelection();

    void setVisible(boolean visible);

    boolean isVisible();

    void activate();
}
