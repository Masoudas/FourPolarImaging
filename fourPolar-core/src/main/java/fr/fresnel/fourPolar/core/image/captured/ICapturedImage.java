package fr.fresnel.fourPolar.core.image.captured;

import java.io.File;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * An interface for accessing the captured image. It is assumed that the
 * captured image has been transformed to a 16bit grayscale, to be accessed
 * properly by this format.
 */
public interface ICapturedImage {
    /**
     * Returns the captured file set corresponding to this image.
     * 
     * @return
     */
    public ICapturedImageFileSet getCapturedFileSet();

    /**
     * Get the label of this file in the captured file set. This the same label as
     * those generated by {@link Cameras}.
     * 
     * @return
     */
    public String getLabel();

    /**
     * Get the actual image as an unsigned short.
     * @return
     */
    public Img<UnsignedShortType> getImage();
    
}