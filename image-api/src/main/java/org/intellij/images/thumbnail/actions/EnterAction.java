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
package org.intellij.images.thumbnail.actions;

import consulo.fileEditor.FileEditorManager;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.Presentation;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import org.intellij.images.fileTypes.ImageFileTypeManager;
import org.intellij.images.thumbnail.ThumbnailView;
import org.intellij.images.thumbnail.actionSystem.ThumbnailViewActionUtil;

/**
 * Level up to browse images.
 *
 * @author <a href="mailto:aefimov.box@gmail.com">Alexey Efimov</a>
 */
public final class EnterAction extends AnAction {
    @Override
    @RequiredUIAccess
    public void actionPerformed(@Nonnull AnActionEvent e) {
        ThumbnailView view = ThumbnailViewActionUtil.getVisibleThumbnailView(e);
        if (view != null) {
            VirtualFile[] selection = view.getSelection();
            if (selection.length == 1 && selection[0].isDirectory()) {
                view.setRoot(selection[0]);
            }
            else if (selection.length > 0) {
                FileEditorManager fileEditorManager = FileEditorManager.getInstance(view.getProject());
                ImageFileTypeManager typeManager = ImageFileTypeManager.getInstance();
                for (VirtualFile file : selection) {
                    if (typeManager.isImage(file)) {
                        fileEditorManager.openFile(file, false);
                    }
                }
            }
        }
    }

    @Override
    @RequiredUIAccess
    public void update(@Nonnull AnActionEvent e) {
        super.update(e);
        if (ThumbnailViewActionUtil.setEnabled(e)) {
            Presentation presentation = e.getPresentation();
            ThumbnailView view = ThumbnailViewActionUtil.getVisibleThumbnailView(e);
            VirtualFile[] selection = view.getSelection();
            if (selection.length == 1 && selection[0].isDirectory()) {
                presentation.setVisible(true);
            }
            else if (selection.length > 0) {
                boolean notImages = false;
                ImageFileTypeManager typeManager = ImageFileTypeManager.getInstance();
                for (VirtualFile file : selection) {
                    notImages |= !typeManager.isImage(file);
                }
                presentation.setEnabled(!notImages);
                presentation.setVisible(false);
            }
            else {
                presentation.setVisible(false);
                presentation.setEnabled(false);
            }
        }
    }
}
