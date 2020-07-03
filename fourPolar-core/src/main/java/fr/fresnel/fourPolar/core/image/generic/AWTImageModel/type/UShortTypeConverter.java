package fr.fresnel.fourPolar.core.image.generic.AWTImageModel.type;

import java.awt.image.BufferedImage;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Converts the {@link BufferedImage#TYPE_USHORT_GRAY} to {@link UINT16}.
 */
class UShortTypeConverter implements TypeConverter<UINT16> {
    @Override
    public void setPixelType(UINT16 pixelType, int rgb) {
        pixelType.set(rgb);
    }

    @Override
    public int getRGB(UINT16 pixelType) {
        return pixelType.get();
    }

}