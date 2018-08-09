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

package org.intellij.images.ui;

import static com.intellij.util.ui.JBUI.ScaleType.OBJ_SCALE;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.intellij.images.ImagesBundle;
import org.intellij.images.editor.ImageDocument;
import org.intellij.images.options.GridOptions;
import org.intellij.images.options.TransparencyChessboardOptions;
import org.jetbrains.annotations.NonNls;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI.ScaleContext;

/**
 * Image component is draw image box with effects.
 *
 * @author <a href="mailto:aefimov.box@gmail.com">Alexey Efimov</a>
 */
public class ImageComponent extends JComponent
{
	public static final int IMAGE_INSETS = 2;

	@NonNls
	public static final String TRANSPARENCY_CHESSBOARD_CELL_SIZE_PROP = "TransparencyChessboard.cellSize";
	@NonNls
	public static final String TRANSPARENCY_CHESSBOARD_WHITE_COLOR_PROP = "TransparencyChessboard.whiteColor";
	@NonNls
	public static final String TRANSPARENCY_CHESSBOARD_BLACK_COLOR_PROP = "TransparencyChessboard.blackColor";
	@NonNls
	private static final String TRANSPARENCY_CHESSBOARD_VISIBLE_PROP = "TransparencyChessboard.visible";
	@NonNls
	private static final String GRID_LINE_ZOOM_FACTOR_PROP = "Grid.lineZoomFactor";
	@NonNls
	private static final String GRID_LINE_SPAN_PROP = "Grid.lineSpan";
	@NonNls
	private static final String GRID_LINE_COLOR_PROP = "Grid.lineColor";
	@NonNls
	private static final String GRID_VISIBLE_PROP = "Grid.visible";
	@NonNls
	private static final String FILE_SIZE_VISIBLE_PROP = "FileSize.visible";
	@NonNls
	private static final String FILE_NAME_VISIBLE_PROP = "FileName.visible";

	/**
	 * @see #getUIClassID
	 * @see #readObject
	 */
	@NonNls
	private static final String uiClassID = "ImageComponentUI";

	static
	{
		UIManager.getDefaults().put(uiClassID, ImageComponentUI.class.getName());
	}

	private final ImageDocument document = new ImageDocumentImpl(this);
	private final Grid grid = new Grid();
	private final Chessboard chessboard = new Chessboard();
	private boolean myFileSizeVisible = true;
	private boolean myFileNameVisible = true;
	private double zoomFactor = 1d;

	public ImageComponent()
	{
		updateUI();
	}

	public ImageDocument getDocument()
	{
		return document;
	}

	public double getZoomFactor()
	{
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor)
	{
		this.zoomFactor = zoomFactor;
	}

	public void setTransparencyChessboardCellSize(int cellSize)
	{
		int oldValue = chessboard.getCellSize();
		if(oldValue != cellSize)
		{
			chessboard.setCellSize(cellSize);
			firePropertyChange(TRANSPARENCY_CHESSBOARD_CELL_SIZE_PROP, oldValue, cellSize);
		}
	}

	public void setTransparencyChessboardWhiteColor(Color color)
	{
		Color oldValue = chessboard.getWhiteColor();
		if(oldValue != null && !oldValue.equals(color) || oldValue == null && color != null)
		{
			chessboard.setWhiteColor(color);
			firePropertyChange(TRANSPARENCY_CHESSBOARD_WHITE_COLOR_PROP, oldValue, color);
		}
	}

	public void setTransparencyChessboardBlankColor(Color color)
	{
		Color oldValue = chessboard.getBlackColor();
		if(oldValue != null && !oldValue.equals(color) || oldValue == null && color != null)
		{
			chessboard.setBlackColor(color);
			firePropertyChange(TRANSPARENCY_CHESSBOARD_BLACK_COLOR_PROP, oldValue, color);
		}
	}

	public void setTransparencyChessboardVisible(boolean visible)
	{
		boolean oldValue = chessboard.isVisible();
		if(oldValue != visible)
		{
			chessboard.setVisible(visible);
			firePropertyChange(TRANSPARENCY_CHESSBOARD_VISIBLE_PROP, oldValue, visible);
		}
	}

	public int getTransparencyChessboardCellSize()
	{
		return chessboard.getCellSize();
	}

	public Color getTransparencyChessboardWhiteColor()
	{
		return chessboard.getWhiteColor();
	}

	public Color getTransparencyChessboardBlackColor()
	{
		return chessboard.getBlackColor();
	}

	public boolean isTransparencyChessboardVisible()
	{
		return chessboard.isVisible();
	}

	public boolean isFileSizeVisible()
	{
		return myFileSizeVisible;
	}

	public void setFileSizeVisible(boolean fileSizeVisible)
	{
		boolean oldValue = myFileSizeVisible;
		myFileSizeVisible = fileSizeVisible;
		firePropertyChange(FILE_SIZE_VISIBLE_PROP, oldValue, fileSizeVisible);
	}

	public boolean isFileNameVisible()
	{
		return myFileNameVisible;
	}

	public void setFileNameVisible(boolean fileNameVisible)
	{
		boolean oldValue = myFileNameVisible;
		myFileNameVisible = fileNameVisible;
		firePropertyChange(FILE_NAME_VISIBLE_PROP, oldValue, fileNameVisible);
	}

	public void setGridLineZoomFactor(int lineZoomFactor)
	{
		int oldValue = grid.getLineZoomFactor();
		if(oldValue != lineZoomFactor)
		{
			grid.setLineZoomFactor(lineZoomFactor);
			firePropertyChange(GRID_LINE_ZOOM_FACTOR_PROP, oldValue, lineZoomFactor);
		}
	}

	public void setGridLineSpan(int lineSpan)
	{
		int oldValue = grid.getLineSpan();
		if(oldValue != lineSpan)
		{
			grid.setLineSpan(lineSpan);
			firePropertyChange(GRID_LINE_SPAN_PROP, oldValue, lineSpan);
		}
	}

	public void setGridLineColor(Color color)
	{
		Color oldValue = grid.getLineColor();
		if(oldValue != null && !oldValue.equals(color) || oldValue == null && color != null)
		{
			grid.setLineColor(color);
			firePropertyChange(GRID_LINE_COLOR_PROP, oldValue, color);
		}
	}

	public void setGridVisible(boolean visible)
	{
		boolean oldValue = grid.isVisible();
		if(oldValue != visible)
		{
			grid.setVisible(visible);
			firePropertyChange(GRID_VISIBLE_PROP, oldValue, visible);
		}
	}

	public int getGridLineZoomFactor()
	{
		return grid.getLineZoomFactor();
	}

	public int getGridLineSpan()
	{
		return grid.getLineSpan();
	}

	public Color getGridLineColor()
	{
		return grid.getLineColor();
	}

	public boolean isGridVisible()
	{
		return grid.isVisible();
	}

	@Nullable
	public String getDescription()
	{
		BufferedImage image = getDocument().getValue();
		if(image != null)
		{
			return ImagesBundle.message("icon.dimensions", image.getWidth(), image.getHeight(), image.getColorModel().getPixelSize());
		}
		return null;
	}

	public void setCanvasSize(int width, int height)
	{
		setSize(width + IMAGE_INSETS * 2, height + IMAGE_INSETS * 2);
	}

	public void setCanvasSize(Dimension dimension)
	{
		setCanvasSize(dimension.width, dimension.height);
	}

	public Dimension getCanvasSize()
	{
		Dimension size = getSize();
		return new Dimension(size.width - IMAGE_INSETS * 2, size.height - IMAGE_INSETS * 2);
	}

	public String getUIClassID()
	{
		return uiClassID;
	}

	public void updateUI()
	{
		setUI(UIManager.getUI(this));
	}

	private static class ImageDocumentImpl implements ImageDocument
	{
		private final List<ChangeListener> listeners = ContainerUtil.createLockFreeCopyOnWriteList();
		private CachedScaledImageProvider imageProvider;
		private String format;
		private Image renderer;
		private final Component myComponent;
		private final ScaleContext.Cache<Rectangle> cachedBounds = new ScaleContext.Cache<>((ctx) ->
		{
			BufferedImage image = getValue(ctx.getScale(OBJ_SCALE));
			return image != null ? new Rectangle(image.getWidth(), image.getHeight()) : null;
		});

		public ImageDocumentImpl(Component component)
		{
			myComponent = component;
			myComponent.addPropertyChangeListener(new PropertyChangeListener()
			{
				@Override
				public void propertyChange(PropertyChangeEvent e)
				{
					if(e.getPropertyName().equals("ancestor") && e.getNewValue() == null && imageProvider != null)
					{
						imageProvider.clearCache();
					}
				}
			});
		}

		public Image getRenderer()
		{
			return renderer;
		}

		@Override
		public Image getRenderer(double scale)
		{
			return getValue(scale);
		}

		@Nullable
		@Override
		public Rectangle getBounds(double scale)
		{
			ScaleContext ctx = ScaleContext.create(myComponent);
			ctx.update(OBJ_SCALE.of(scale));
			return cachedBounds.getOrProvide(ctx);
		}

		@Override
		public BufferedImage getValue()
		{
			return getValue(1d);
		}

		@Override
		public BufferedImage getValue(double scale)
		{
			return imageProvider != null ? imageProvider.apply(scale, myComponent) : null;
		}

		public void setValue(BufferedImage image)
		{
			this.renderer = image != null ? Toolkit.getDefaultToolkit().createImage(image.getSource()) : null;
			setValue(image != null ? (scale, anchor) -> image : null);
		}

		@Override
		public void setValue(ScaledImageProvider imageProvider)
		{
			this.imageProvider = imageProvider instanceof CachedScaledImageProvider ?
					(CachedScaledImageProvider) imageProvider :
					imageProvider != null ? (zoom, ancestor) -> imageProvider.apply(zoom, ancestor) :
							null;

			cachedBounds.clear();
			fireChangeEvent(new ChangeEvent(this));
		}

		public String getFormat()
		{
			return format;
		}


		public void setFormat(String format)
		{
			this.format = format;
			fireChangeEvent(new ChangeEvent(this));
		}

		private void fireChangeEvent(ChangeEvent e)
		{
			for(ChangeListener listener : listeners)
			{
				listener.stateChanged(e);
			}
		}

		public void addChangeListener(ChangeListener listener)
		{
			listeners.add(listener);
		}

		public void removeChangeListener(ChangeListener listener)
		{
			listeners.remove(listener);
		}
	}

	private static final class Chessboard
	{
		private int cellSize = TransparencyChessboardOptions.DEFAULT_CELL_SIZE;
		private Color whiteColor = TransparencyChessboardOptions.DEFAULT_WHITE_COLOR;
		private Color blackColor = TransparencyChessboardOptions.DEFAULT_BLACK_COLOR;
		private boolean visible = false;

		public int getCellSize()
		{
			return cellSize;
		}

		public void setCellSize(int cellSize)
		{
			this.cellSize = cellSize;
		}

		public Color getWhiteColor()
		{
			return whiteColor;
		}

		public void setWhiteColor(Color whiteColor)
		{
			this.whiteColor = whiteColor;
		}

		public Color getBlackColor()
		{
			return blackColor;
		}

		public void setBlackColor(Color blackColor)
		{
			this.blackColor = blackColor;
		}

		public boolean isVisible()
		{
			return visible;
		}

		public void setVisible(boolean visible)
		{
			this.visible = visible;
		}
	}

	private static final class Grid
	{
		private int lineZoomFactor = GridOptions.DEFAULT_LINE_ZOOM_FACTOR;
		private int lineSpan = GridOptions.DEFAULT_LINE_SPAN;
		private Color lineColor = GridOptions.DEFAULT_LINE_COLOR;
		private boolean visible = false;

		public int getLineZoomFactor()
		{
			return lineZoomFactor;
		}

		public void setLineZoomFactor(int lineZoomFactor)
		{
			this.lineZoomFactor = lineZoomFactor;
		}

		public int getLineSpan()
		{
			return lineSpan;
		}

		public void setLineSpan(int lineSpan)
		{
			this.lineSpan = lineSpan;
		}

		public Color getLineColor()
		{
			return lineColor;
		}

		public void setLineColor(Color lineColor)
		{
			this.lineColor = lineColor;
		}

		public boolean isVisible()
		{
			return visible;
		}

		public void setVisible(boolean visible)
		{
			this.visible = visible;
		}
	}
}
