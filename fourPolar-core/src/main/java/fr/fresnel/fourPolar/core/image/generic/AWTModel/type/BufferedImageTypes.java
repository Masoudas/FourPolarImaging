package fr.fresnel.fourPolar.core.image.generic.AWTModel.type;

import java.awt.image.BufferedImage;

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
}