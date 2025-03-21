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
package consulo.images.impl.action;

import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.ActionPlaces;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import org.intellij.images.thumbnail.ThumbnailManager;
import org.intellij.images.thumbnail.ThumbnailView;

/**
 * Show thumbnail for directory.
 *
 * @author <a href="mailto:aefimov.box@gmail.com">Alexey Efimov</a>
 */
public final class ShowThumbnailsAction extends AnAction {
    @Override
    @RequiredUIAccess
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(Project.KEY);
        VirtualFile file = e.getData(VirtualFile.KEY);
        if (project != null && file != null && file.isDirectory()) {
            ThumbnailManager thumbnailManager = ThumbnailManager.getManager(project);
            ThumbnailView thumbnailView = thumbnailManager.getThumbnailView();
            thumbnailView.setRoot(file);
            thumbnailView.setVisible(true);
            thumbnailView.activate();
        }
    }

    @Override
    @RequiredUIAccess
    public void update(@Nonnull AnActionEvent e) {
        super.update(e);
        VirtualFile file = e.getData(VirtualFile.KEY);
        boolean isEnabled = file != null && file.isDirectory();
        if (e.getPlace().equals(ActionPlaces.PROJECT_VIEW_POPUP)) {
            e.getPresentation().setVisible(isEnabled);
        }
        else {
            e.getPresentation().setEnabled(isEnabled);
        }
    }
}
