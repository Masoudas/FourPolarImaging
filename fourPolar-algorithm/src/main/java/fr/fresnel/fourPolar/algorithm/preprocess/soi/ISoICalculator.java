package fr.fresnel.fourPolar.algorithm.preprocess.soi;

import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * An interface for calculating the sum of intensity from a given
 * {@link IIntensityVectorIterator}.
 */
public interface ISoICalculator {
    /**
     * Calculates SoI and returns a discrete sum, as {@link UINT16} pixels. This
     * method must ONLY be used when we know the underlying intensities are
     * themselves {@link UINT16} (when intensities are captured rather than
     * computed), and we want the outcome as integers. This ensures that no rounding
     * error occurs. Otherwise, we get rounding errors.
     */
    public void calculateUINT16Sum(IIntensityVectorIterator intensityIterator, IPixelCursor<UINT16> pixelCursor);

}