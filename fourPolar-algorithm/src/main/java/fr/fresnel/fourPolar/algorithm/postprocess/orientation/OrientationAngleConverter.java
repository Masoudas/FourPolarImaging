package fr.fresnel.fourPolar.algorithm.postprocess.orientation;

import java.util.EnumSet;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * Contains a set of utility methods for post-processing the orientation images.
 */
public class OrientationAngleConverter {
    /**
     * Converts each {@link OrientationAngle} to degrees in place.
     */
    public void convertToDegree(IOrientationImage orientationImage) {
        for (OrientationAngle orientationAngle : EnumSet.allOf(OrientationAngle.class)) {
            IPixelCursor<Float32> angleCursor = orientationImage.getAngleImage(orientationAngle).getImage().getCursor();

            while (angleCursor.hasNext()) {
                IPixel<Float32> angle = angleCursor.next();
                double angleInDegree = Math.toDegrees(angle.value().get());

                angle.value().set((float) angleInDegree);
                angleCursor.setPixel(angle);
            }
        }

    }

    /**
     * Converts each {@link OrientationAngle} to radian in place.
     */
    public void convertToRadian(IOrientationImage orientationImage) {
        for (OrientationAngle orientationAngle : EnumSet.allOf(OrientationAngle.class)) {
            IPixelCursor<Float32> angleCursor = orientationImage.getAngleImage(orientationAngle).getImage().getCursor();

            while (angleCursor.hasNext()) {
                IPixel<Float32> angle = angleCursor.next();
                double angleInRadian = Math.toRadians(angle.value().get());

                angle.value().set((float) angleInRadian);
                angleCursor.setPixel(angle);
            }
        }

    }
}