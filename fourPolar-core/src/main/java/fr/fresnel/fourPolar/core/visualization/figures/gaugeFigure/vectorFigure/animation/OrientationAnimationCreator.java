package fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.animation;

import fr.fresnel.fourPolar.core.image.vector.animation.Animation;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * An interface for creating vector animation for an {@link OrientationAngle}.
 */
public interface OrientationAnimationCreator {
    /**
     * Create an animation for the given angle of the orientation vector.
     * 
     * @param orientationVector is the orientation vector.
     * @return the created animation.
     */
    public Animation create(IOrientationVector orientationVector);
}