package fr.fresnel.fourPolar.core.image.soi;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * An interface that models the SoI (Sum of Intensity) images. This image is
 * formed by adding the four intensities of dipole together. See
 * {@link SoICalculator}.
 */
public interface ISoIImage {
    /**
     * Axis order of the image.
     */
    public static final AxisOrder AXIS_ORDER = AxisOrder.XYCZT;

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

}