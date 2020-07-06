package fr.fresnel.fourPolar.core.image.generic.AWTModel.type;

import java.awt.image.BufferedImage;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;

/**
 * Represents the pixel types of {@link BufferedImage}. To each type is assigned
 * an integer which wraps the pixel types of buffered image.
 */
public enum BufferedImageTypes {
    TYPE_USHORT_GRAY {
        @Override
        public int getBufferedType() {
            return BufferedImage.TYPE_USHORT_GRAY;
        }
    };

    abstract public int getBufferedType();

    /**
     * Converts an instance of {@link PixelTypes}to an buffered image type.
     * 
     * @return
     */
    public static BufferedImageTypes convertPixelTypes(PixelTypes pixelTypes) {
        if (pixelTypes == PixelTypes.UINT_16) {
            return TYPE_USHORT_GRAY;
        } else {
            throw new IllegalArgumentException("No suitable buffered type was found for the given pixel type");
        }
    }
}