package fr.fresnel.fourPolar.algorithm.postprocess.orientation;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IAngleImage;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * Contains a set of utility methods for post-processing the orientation images.
 */
public class OrientationAngleConverter {
    private OrientationAngleConverter() {
        throw new AssertionError();
    }

    /**
     * Converts each {@link OrientationAngle} to degrees in place.
     */
    public static Image<Float32> convertToDegree(IOrientationImage orientationImage, OrientationAngle angle) {
        Image<Float32> degreeImage = _duplicateAngleImage(orientationImage.getAngleImage(angle));

        IPixelRandomAccess<Float32> ra_degreeImage = degreeImage.getRandomAccess();
        for (IPixelCursor<Float32> angleCursor = orientationImage.getAngleImage(angle).getImage().getCursor(); angleCursor.hasNext(); ){
                IPixel<Float32> angleInRadian = angleCursor.next();
                double angleInDegree = Math.toDegrees(angleInRadian.value().get());

                ra_degreeImage.setPosition(angleCursor.localize());
                IPixel<Float32> pixelInRadian = ra_degreeImage.getPixel();
                pixelInRadian.value().set((float)angleInDegree);
                ra_degreeImage.setPixel(pixelInRadian);
        }

        return degreeImage;

    }

    /**
     * Converts an image interface that has angle in degrees to radian in place.
     */
    public static void convertToRadian(Image<Float32> angleImageInDegree) {
        for (IPixelCursor<Float32> angleCursor = angleImageInDegree.getCursor(); angleCursor.hasNext();){
            IPixel<Float32> angle = angleCursor.next();
            double angleInRadian = Math.toRadians(angle.value().get());
    
            angle.value().set((float) angleInRadian);
            angleCursor.setPixel(angle);
        }

    }

    /**
     * @return an empty image that has the same metadata as the given orientation image
     */
    private static Image<Float32> _duplicateAngleImage(IAngleImage angleImage) {
        IMetadata metadata_angleImage = new Metadata.MetadataBuilder(angleImage.getImage().getMetadata()).build();
        return angleImage.getImage().getFactory().create(metadata_angleImage, Float32.zero());
    }
}