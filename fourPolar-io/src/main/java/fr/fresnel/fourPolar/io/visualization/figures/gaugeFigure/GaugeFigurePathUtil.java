package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure;

import java.io.File;
import java.nio.file.Paths;

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

    private GaugeFigurePathUtil(){
        throw new AssertionError();
    }

    /**
     * Create the root folder in which this figure would be written. The formula is:
     * visualizationSession + {@link #_GAUGE_FIGURE_FOLDER} + captured image set
     * name + channel.
     */
    public static File createGaugeFigurePath(File root4PProject, String visualizationSession,
            IGaugeFigure gaugeFigure) {
        int channel = gaugeFigure.getChannel();
        return createGaugeFigurePath(root4PProject, visualizationSession, channel, gaugeFigure.getFileSet(),
                gaugeFigure.getLocalization(), gaugeFigure.getGaugeType());
    }

    /**
     * Create the root folder in which this figure would be written. The formula is:
     * visualizationSession + {@link #_GAUGE_FIGURE_FOLDER} + captured image set
     * name + channel +
     */
    public static File createGaugeFigurePath(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet, GaugeFigureLocalization figureFigureType,
            AngleGaugeType angleGaugeType) {
        String setName = capturedImageFileSet.getSetName();

        File visualizationRoot = PathFactoryOfProject.getFolder_Visualization(root4PProject);
        String channelAsString = ChannelUtils.channelAsString(channel);

        return Paths.get(visualizationRoot.getAbsolutePath(), visualizationSession, _GAUGE_FIGURE_FOLDER, setName,
                channelAsString).toFile();

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