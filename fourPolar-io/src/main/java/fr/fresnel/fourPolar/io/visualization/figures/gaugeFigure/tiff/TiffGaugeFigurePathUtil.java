package fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.tiff;

import java.io.File;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.GaugeFigurePathUtil;

/**
 * A set of utility methods.
 */
class TiffGaugeFigurePathUtil {
    /**
     * Create the path to which this figure would be written. The formula is:
     * visualizationSession + _GAUGE_FIGURE_FOLDER + captured image set + channel +
     * gaugeFigureType_AngleGaugeType.tif
     */
    public static File createGaugeFigurePath(File root4PProject, String visualizationSession,
            IGaugeFigure gaugeFigure) {
        return new File(GaugeFigurePathUtil.createRoot(root4PProject, visualizationSession, gaugeFigure),
                _getGaugeFigureName(gaugeFigure.getLocalization(), gaugeFigure.getGaugeType()));
    }

    /**
     * Create the path to which this figure would be written. The formula is:
     * visualizationSession + _GAUGE_FIGURE_FOLDER + captured image set + channel +
     * gaugeFigureType_AngleGaugeType.tif
     */
    public static File createGaugeFigurePath(File root4PProject, String visualizationSession, int channel,
            ICapturedImageFileSet capturedImageFileSet, GaugeFigureLocalization localization,
            AngleGaugeType angleGaugeType) {
        return new File(GaugeFigurePathUtil.createRoot(root4PProject, visualizationSession, channel,
                capturedImageFileSet, localization, angleGaugeType), _getGaugeFigureName(localization, angleGaugeType));

    }

    private static String _getGaugeFigureName(GaugeFigureLocalization localization, AngleGaugeType gaugeType) {
        return GaugeFigurePathUtil.getGaugeFigureName(localization, gaugeType) + ".tif";
    }
}