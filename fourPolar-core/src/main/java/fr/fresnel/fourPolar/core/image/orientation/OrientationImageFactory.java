package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Factory for creating a plain {@link IOrinetationImage}.
 */
public class OrientationImageFactory {
    /**
     * Create the orientation image from an {@link IPolarizationImage}.
     * 
     */
    public static IOrientationImage create(ImageFactory factory, IPolarizationImageSet polarizationImageSet) {
        long[] dimension = polarizationImageSet.getPolarizationImage(Polarization.pol0).getImage().getMetadata()
                .getDim();

        IMetadata metadata = new Metadata.MetadataBuilder(dimension).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        Image<Float32> rho = factory.create(metadata, Float32.zero());
        Image<Float32> delta = factory.create(metadata, Float32.zero());
        Image<Float32> eta = factory.create(metadata, Float32.zero());

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
        return new OrientationImage(fileSet, channel, rho, delta, eta);
    }
}