package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.io.PathFactoryOfProject;

/**
 * A set of utility methods for creating the path to gauge figures.
 */
public class GaugeFigurePathUtil {
    public static String _GAUGE_FIGURE_FOLDER = "GaugeFigures";

    private GaugeFigurePathUtil() {
        throw new AssertionError();
    }

    /**
     * @see #createRoot(File, String, int, ICapturedImageFileSet, GaugeFigureLocalization, AngleGaugeType)
     */
    public static File createRoot(File root4PProject, String visualizationSession, IGaugeFigure gaugeFigure) {
        Objects.requireNonNull(gaugeFigure, "gaugeFigure can't be null.");
        return createRoot(root4PProject, visualizationSession, gaugeFigure.getChannel(), gaugeFigure.getFileSet(),
                gaugeFigure.getLocalization(), gaugeFigure.getGaugeType());
    }

    /**
     * Create the root folder in which the figures correspondong to this channel and
     * file set will be written. The formula is: visualizationSession +
     * {@link #_GAUGE_FIGURE_FOLDER} + captured image set name + channel.
     */
    public static File createRoot(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet, GaugeFigureLocalization figureFigureType,
            AngleGaugeType angleGaugeType) {
        Objects.requireNonNull(root4PProject, "root4PProject can't be null");
        Objects.requireNonNull(visualizationSession, "visualizationSession can't be null");
        Objects.requireNonNull(capturedImageFileSet, "capturedImageFileSet can't be null");
        Objects.requireNonNull(figureFigureType, "figureFigureType can't be null");
        Objects.requireNonNull(angleGaugeType, "angleGaugeType can't be null");

        ChannelUtils.checkChannelNumberIsNonZero(channel);

        String setName = capturedImageFileSet.getSetName();
        File visualizationRoot = PathFactoryOfProject.getFolder_Visualization(root4PProject);
        String channelAsString = ChannelUtils.channelAsString(channel);

        return Paths.get(visualizationRoot.getAbsolutePath(), visualizationSession, _GAUGE_FIGURE_FOLDER, setName,
                channelAsString).toFile();

    }

    /**
     * @see #getGaugeFigureName(GaugeFigureLocalization, AngleGaugeType);
     */
    public static String getGaugeFigureName(IGaugeFigure gaugeFigure) {
        return getGaugeFigureName(gaugeFigure.getLocalization(), gaugeFigure.getGaugeType());
    }

    /**
     * Creates the name of a gauge figure based on its localization level and its
     * gauge type. The formula is "localization_gaugeType".
     * 
     * @param localization is the localization of the gauge figure.
     * @param gaugeType    is the gauge type of the figure.
     * @return the name of the corresponding figure.
     */
    public static String getGaugeFigureName(GaugeFigureLocalization localization, AngleGaugeType gaugeType) {
        return localization + "_" + gaugeType;
    }
}