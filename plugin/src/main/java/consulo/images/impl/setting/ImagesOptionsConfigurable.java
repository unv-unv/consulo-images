package consulo.images.impl.setting;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.SearchableConfigurable;
import consulo.configurable.SimpleConfigurableByProperties;
import consulo.configurable.StandardConfigurableIds;
import consulo.container.plugin.PluginIds;
import consulo.container.plugin.PluginManager;
import consulo.disposer.Disposable;
import consulo.ide.setting.ShowSettingsUtil;
import consulo.images.localize.ImagesLocalize;
import consulo.platform.Platform;
import consulo.project.Project;
import consulo.ui.*;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.FileChooserTextBoxBuilder;
import consulo.ui.layout.LabeledLayout;
import consulo.ui.layout.TableLayout;
import consulo.ui.layout.VerticalLayout;
import consulo.ui.util.LabeledBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.intellij.images.options.*;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2020-11-28
 */
@ExtensionImpl
public class ImagesOptionsConfigurable extends SimpleConfigurableByProperties implements SearchableConfigurable, ApplicationConfigurable {
    @RequiredUIAccess
    public static void show(Project project) {
        ShowSettingsUtil util = ShowSettingsUtil.getInstance();
        util.showSettingsDialog(project, ImagesOptionsConfigurable.class);
    }

    private final Provider<OptionsManager> myOptionsManager;

    @Inject
    public ImagesOptionsConfigurable(Provider<OptionsManager> optionsManager) {
        myOptionsManager = optionsManager;
    }

    @RequiredUIAccess
    @Nonnull
    @Override
    protected Component createLayout(@Nonnull PropertyBuilder propertyBuilder, @Nonnull Disposable uiDisposable) {
        Options options = myOptionsManager.get().getOptions();
        EditorOptions editorOptions = options.getEditorOptions();
        GridOptions gridOptions = editorOptions.getGridOptions();
        TransparencyChessboardOptions chessboardOptions = editorOptions.getTransparencyChessboardOptions();
        ZoomOptions zoomOptions = editorOptions.getZoomOptions();

        VerticalLayout root = VerticalLayout.create(0);

        TableLayout imagesLayout = TableLayout.create(StaticPosition.TOP);
        int imagesRow = 0;
        root.add(LabeledLayout.create(ImagesLocalize.mainPageBorderTitle(), imagesLayout));

        CheckBox showGridLines = CheckBox.create(ImagesLocalize.showGridLines());
        imagesLayout.add(showGridLines, TableLayout.cell(imagesRow, 0).fill());
        propertyBuilder.add(showGridLines, gridOptions::isShowDefault, it -> gridOptions.setOption(GridOptions.ATTR_SHOW_DEFAULT, it));

        IntBox gridLineZoomLimit = IntBox.create(2).withRange(2, 8);
        Label gridLineZoomLimitLabel = Label.create(ImagesLocalize.showGridZoomLimit());
        gridLineZoomLimitLabel.setTarget(gridLineZoomLimit);
        imagesLayout.add(gridLineZoomLimitLabel, TableLayout.cell(++imagesRow, 0));
        imagesLayout.add(gridLineZoomLimit, TableLayout.cell(imagesRow, 1));
        propertyBuilder.add(
            gridLineZoomLimit,
            gridOptions::getLineZoomFactor,
            it -> gridOptions.setOption(GridOptions.ATTR_LINE_ZOOM_FACTOR, it)
        );

        IntBox gridLineSpan = IntBox.create(1).withRange(1, 100);
        Label gridLineLabel = Label.create(ImagesLocalize.showGridEvery());
        gridLineLabel.setTarget(gridLineSpan);
        imagesLayout.add(gridLineLabel, TableLayout.cell(++imagesRow, 0));
        imagesLayout.add(gridLineSpan, TableLayout.cell(imagesRow, 1));
        propertyBuilder.add(gridLineSpan, gridOptions::getLineSpan, it -> gridOptions.setOption(GridOptions.ATTR_LINE_SPAN, it));

        CheckBox showChessboard = CheckBox.create(ImagesLocalize.showTransparencyChessboard());
        imagesLayout.add(showChessboard, TableLayout.cell(++imagesRow, 0));
        propertyBuilder.add(
            showChessboard,
            chessboardOptions::isShowDefault,
            it -> chessboardOptions.setOption(TransparencyChessboardOptions.ATTR_SHOW_DEFAULT, it)
        );

        IntBox chessboardSize = IntBox.create(1).withRange(1, 100);
        Label chessboardSizeLabel = Label.create(ImagesLocalize.chessboardCellSize());
        chessboardSizeLabel.setTarget(chessboardSize);
        imagesLayout.add(chessboardSizeLabel, TableLayout.cell(++imagesRow, 0));
        imagesLayout.add(chessboardSize, TableLayout.cell(imagesRow, 1));
        propertyBuilder.add(
            chessboardSize,
            chessboardOptions::getCellSize,
            it -> chessboardOptions.setOption(TransparencyChessboardOptions.ATTR_CELL_SIZE, it)
        );

        Platform platform = Platform.current();

        CheckBox zoomWheel = CheckBox.create(ImagesLocalize.enableMousewheelZooming(platform.os().isMac() ? "Cmd" : "Ctrl"));
        imagesLayout.add(zoomWheel, TableLayout.cell(++imagesRow, 0).fill());
        propertyBuilder.add(zoomWheel, zoomOptions::isWheelZooming, it -> zoomOptions.setOption(ZoomOptions.ATTR_WHEEL_ZOOMING, it));

        CheckBox smartWheel = CheckBox.create(ImagesLocalize.smartZoom());
        imagesLayout.add(smartWheel, TableLayout.cell(++imagesRow, 0));
        propertyBuilder.add(smartWheel, zoomOptions::isSmartZooming, it -> zoomOptions.setOption(ZoomOptions.ATTR_SMART_ZOOMING, it));

        IntBox smartZoomingWidth = IntBox.create(1).withRange(1, 9999);
        Label smartZoomingWidthLabel = Label.create(ImagesLocalize.settingsPrefferedSmartZoomWidth());
        smartZoomingWidthLabel.setTarget(smartZoomingWidth);
        imagesLayout.add(smartZoomingWidthLabel, TableLayout.cell(++imagesRow, 0));
        imagesLayout.add(smartZoomingWidth, TableLayout.cell(imagesRow, 1));
        propertyBuilder.add(
            smartZoomingWidth,
            () -> zoomOptions.getPrefferedSize().getWidth(),
            it -> zoomOptions.setOption(ZoomOptions.ATTR_PREFFERED_WIDTH, it)
        );

        IntBox smartZoomingHeight = IntBox.create(1).withRange(1, 9999);
        Label smartZoomingHeightLabel = Label.create(ImagesLocalize.settingsPrefferedSmartZoomHeight());
        smartZoomingHeightLabel.setTarget(smartZoomingHeight);
        imagesLayout.add(smartZoomingHeightLabel, TableLayout.cell(++imagesRow, 0));
        imagesLayout.add(smartZoomingHeight, TableLayout.cell(imagesRow, 1));
        propertyBuilder.add(
            smartZoomingHeight,
            () -> zoomOptions.getPrefferedSize().getHeight(),
            it -> zoomOptions.setOption(ZoomOptions.ATTR_PREFFERED_HEIGHT, it)
        );

        if (PluginManager.findPlugin(PluginIds.CONSULO_WEB) == null) {
            VerticalLayout externalEditorLayout = VerticalLayout.create();

            FileChooserTextBoxBuilder fileChooserTextBoxBuilder = FileChooserTextBoxBuilder.create(null);
            fileChooserTextBoxBuilder.uiDisposable(uiDisposable);
            fileChooserTextBoxBuilder.dialogTitle(ImagesLocalize.selectExternalExecutableTitle());
            fileChooserTextBoxBuilder.dialogDescription(ImagesLocalize.selectExternalExecutableMessage());

            FileChooserTextBoxBuilder.Controller controller = fileChooserTextBoxBuilder.build();

            propertyBuilder.add(
                controller::getValue,
                controller::setValue,
                () -> options.getExternalEditorOptions().getExecutablePath(),
                it -> options.getExternalEditorOptions().setOption(ExternalEditorOptions.ATTR_EXECUTABLE_PATH, it)
            );

            externalEditorLayout.add(LabeledBuilder.filled(ImagesLocalize.externalEditorExecutablePath(), controller.getComponent()));

            root.add(LabeledLayout.create(ImagesLocalize.externalEditorBorderTitle(), externalEditorLayout));
        }
        return root;
    }

    @Nonnull
    @Override
    public String getId() {
        return "images";
    }

    @Nullable
    @Override
    public String getParentId() {
        return StandardConfigurableIds.EDITOR_GROUP;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return ImagesLocalize.settingsPageName().get();
    }
}
