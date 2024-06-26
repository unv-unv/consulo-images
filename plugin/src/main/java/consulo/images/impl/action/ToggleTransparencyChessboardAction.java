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

import consulo.annotation.component.ActionImpl;
import consulo.images.icon.ImagesIconGroup;
import consulo.ui.ex.action.ToggleAction;
import consulo.ui.image.Image;
import jakarta.annotation.Nullable;
import org.intellij.images.ui.ImageComponentDecorator;
import consulo.ui.ex.action.AnActionEvent;

/**
 * Show/hide background action.
 *
 * @author <a href="mailto:aefimov.box@gmail.com">Alexey Efimov</a>
 * @see org.intellij.images.ui.ImageComponentDecorator#setTransparencyChessboardVisible
 */
@ActionImpl(id = "Images.ToggleTransparencyChessboard")
public final class ToggleTransparencyChessboardAction extends ToggleAction {
  @Nullable
  @Override
  protected Image getTemplateIcon() {
    return ImagesIconGroup.toggletransparencychessboard();
  }

  public boolean isSelected(AnActionEvent e) {
    ImageComponentDecorator decorator = e.getData(ImageComponentDecorator.DATA_KEY);
    return decorator != null && decorator.isEnabledForActionPlace(e.getPlace()) && decorator.isTransparencyChessboardVisible();
  }

  public void setSelected(AnActionEvent e, boolean state) {
    ImageComponentDecorator decorator = e.getData(ImageComponentDecorator.DATA_KEY);
    if (decorator != null && decorator.isEnabledForActionPlace(e.getPlace())) {
      decorator.setTransparencyChessboardVisible(state);
    }
  }

  public void update(final AnActionEvent e) {
    super.update(e);
    ImageComponentDecorator decorator = e.getData(ImageComponentDecorator.DATA_KEY);
    e.getPresentation().setEnabled(decorator != null && decorator.isEnabledForActionPlace(e.getPlace()));
    e.getPresentation().setText(isSelected(e) ? "Hide Chessboard" : "Show Chessboard");
  }
}
