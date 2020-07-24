package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.vectorModel;

import java.util.Optional;

import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.animation.OrientationAnimationCreator;

/**
 * A simple private interface for accessing the parameters of the builder.
 */
abstract class IVectorWholeSampleStick2DPainterBuilder {
    abstract ColorMap getColorMap();

    abstract int getSticklength();

    abstract IOrientationImage getOrientationImage();

    abstract ISoIImage getSoIImage();

    abstract int getStickThickness();

    abstract VectorGaugeFigure getGauageFigure();

    abstract Optional<FilterComposite> getColorBlender();

    abstract OrientationAngle getSlopeAngle();

    abstract OrientationAngle getColorAngle();

    abstract Optional<OrientationAnimationCreator> getAnimationCreator();
}