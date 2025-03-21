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

/** $Id$ */

package org.intellij.images.thumbnail.actions;

import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnActionEvent;
import consulo.virtualFileSystem.VirtualFile;
import consulo.ui.ex.action.AnAction;
import jakarta.annotation.Nonnull;
import org.intellij.images.thumbnail.ThumbnailView;
import org.intellij.images.thumbnail.actionSystem.ThumbnailViewActionUtil;

/**
 * Level up to browse images.
 *
 * @author <a href="mailto:aefimov.box@gmail.com">Alexey Efimov</a>
 */
public final class UpFolderAction extends AnAction {
    @Override
    @RequiredUIAccess
    public void actionPerformed(@Nonnull AnActionEvent e) {
        ThumbnailView view = ThumbnailViewActionUtil.getVisibleThumbnailView(e);
        if (view != null) {
            VirtualFile root = view.getRoot();
            if (root != null) {
                VirtualFile parent = root.getParent();
                if (parent != null) {
                    view.setRoot(parent);
                }
            }
        }
    }

    @Override
    @RequiredUIAccess
    public void update(@Nonnull AnActionEvent e) {
        super.update(e);
        if (ThumbnailViewActionUtil.setEnabled(e)) {
            ThumbnailView view = ThumbnailViewActionUtil.getVisibleThumbnailView(e);
            VirtualFile root = view.getRoot();
            e.getPresentation().setEnabled(root != null && root.getParent() != null && !view.isRecursive());
        }
    }
}
