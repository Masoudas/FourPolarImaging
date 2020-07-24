package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge3D;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.ColorBlender;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;

/**
 * A simple private interface for accessing the parameters of the builder.
 */
abstract public class IWholeSampleStick3DPainterBuilder {
    abstract ColorMap getColorMap();

    abstract int getSticklength();

    abstract IOrientationImage getOrientationImage();

    abstract ISoIImage getSoIImage();

    abstract int getStickThickness();

    abstract ColorBlender getColorBlender();

    abstract GaugeFigure getGaugeFigure();
}