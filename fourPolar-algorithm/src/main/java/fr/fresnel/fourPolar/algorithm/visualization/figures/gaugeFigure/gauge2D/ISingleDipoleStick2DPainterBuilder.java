package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D;

import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;

/**
 * A simple private interface for accessing the parameters of the builder.
 */
abstract class ISingleDipoleStick2DPainterBuilder {
    abstract ColorMap getColorMap();

    abstract int getSticklength();

    abstract IOrientationImage getOrientationImage();

    abstract ISoIImage getSoIImage();

    abstract int getStickThickness();

    abstract IGaugeFigure getGaugeFigure();

    abstract int figSizeToStickLenRatio();

    abstract OrientationAngle getSlopeAngle();

    abstract OrientationAngle getColorAngle();
}