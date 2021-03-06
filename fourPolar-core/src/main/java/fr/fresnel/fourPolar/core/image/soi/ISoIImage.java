package fr.fresnel.fourPolar.core.image.soi;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;

/**
 * An interface that models the SoI (Sum of Intensity) images. This image is
 * formed by adding the four intensities of dipole together. See
 * {@link SoICalculator}.
 */
public interface ISoIImage {
    /**
     * Axis order of the image.
     */
    public static final AxisOrder AXIS_ORDER = IPolarizationImage.AXIS_ORDER;

    /**
     * Returns the image interface corresponding to this SoI Image.
     */
    public Image<UINT16> getImage();

    /**
     * Returns the captured image file set that corresponds to this SoI image
     */
    public ICapturedImageFileSet getFileSet();

    /**
     * Returns the channel number associated with this soi image.
     * 
     * @return
     */
    public int channel();

    /**
     * Returns true if this SoI image belongs to the given orientation image.
     * 
     * @param orientationImage is the orientation image
     * @return true if corresponds to the given image.
     */
    public boolean belongsTo(IOrientationImage orientationImage);

}