package fr.fresnel.fourPolar.core.image.orientation;

import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageUtils;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * Factory for creating a plain {@link IOrinetationImage}.
 */
public class OrientationImageFactory {
    /**
     * Create a blank orientation image from an {@link IPolarizationImage}.
     * 
     * @param factory              is the desired image factory.
     * @param polarizationImageSet is the polarization image set.
     * @return an empty orientation image.
     */
    public static IOrientationImage create(ImageFactory factory, IPolarizationImageSet polarizationImageSet) {
        Objects.requireNonNull(factory, "factory should not be null");
        Objects.requireNonNull(polarizationImageSet, "polarizationImageSet should not be null");

        AngleImage rho = _createAngleImage(polarizationImageSet, OrientationAngle.rho, factory);
        AngleImage delta = _createAngleImage(polarizationImageSet, OrientationAngle.delta, factory);
        AngleImage eta = _createAngleImage(polarizationImageSet, OrientationAngle.eta, factory);

        IOrientationImage orientationImage = null;
        try {
            orientationImage = new OrientationImage(polarizationImageSet.getFileSet(), polarizationImageSet.channel(),
                    rho, delta, eta);
        } catch (CannotFormOrientationImage e) {
            // Never caught, because images are properly formed.
        }

        return orientationImage;
    }

    /**
     * Create an angle image based on the polarization image set.
     * 
     * @param polImageSet is the polarization image set.
     * @param angle       is the desired angle.
     * @param factory     is the image factory.
     * @return an angle image for an angle image with the provided image factory.
     */
    private static AngleImage _createAngleImage(IPolarizationImageSet polImageSet, OrientationAngle angle,
            ImageFactory factory) {
        IMetadata angleImageMetadata = _createAngleImageMetadata(polImageSet);
        return new AngleImage(angle, factory.create(angleImageMetadata, Float32.zero()));
    }

    /**
     * Creates a metadata for each angle image from the polarization image,
     * equivalent for each angle image.
     * 
     * @param polarizationImageSet is the polarization image.
     */
    private static IMetadata _createAngleImageMetadata(IPolarizationImageSet polarizationImageSet) {
        long[] dimension = PolarizationImageUtils.getPolarizationImageDim(polarizationImageSet);

        return new Metadata.MetadataBuilder(dimension).axisOrder(AxisOrder.XYCZT).bitPerPixel(PixelTypes.FLOAT_32)
                .build();
    }

    /**
     * Create the image from existing image interfaces. Used for reading the
     * orientation image from the disk.
     * 
     * @param fileSet is the file set associated with the images.
     * @param int     is the channel number.
     * @param rho     is the rho angle image.
     * @param delta   is the delta angle image.
     * @param eta     is the eta angle image.
     * @throws CannotFormOrientationImage if the images don't have the same
     *                                    dimension, or aren't XYCZT.
     */
    public static IOrientationImage create(ICapturedImageFileSet fileSet, int channel, Image<Float32> rho,
            Image<Float32> delta, Image<Float32> eta) throws CannotFormOrientationImage {
        AngleImage rhoImage = _createAngleImage(rho, OrientationAngle.rho);
        AngleImage deltaImage = _createAngleImage(delta, OrientationAngle.delta);
        AngleImage etaImage = _createAngleImage(eta, OrientationAngle.eta);

        return new OrientationImage(fileSet, channel, rhoImage, deltaImage, etaImage);
    }

    private static AngleImage _createAngleImage(Image<Float32> image, OrientationAngle angle)
            throws CannotFormOrientationImage {
        try {
            return new AngleImage(angle, image);
        } catch (IllegalArgumentException e) {
            throw new CannotFormOrientationImage(e.getMessage());
        }

    }
}