package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import net.imglib2.type.numeric.ARGBType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class ARGBConverter implements TypeConverter<ARGB8, ARGBType> {
    @Override
    public void setPixelType(ARGBType type, ARGB8 pixel) {
        ARGBType value = (ARGBType)type;
        
        int index = value.get();
        int red = ARGBType.red(index);
        int blue = ARGBType.blue(index);
        int green = ARGBType.green(index);
        int alpha = ARGBType.alpha(index);

        ARGB8 rgb16 = (ARGB8)pixel;
        rgb16.set(red, green, blue, alpha);
    }

    @Override
    public void setNativeType(ARGB8 pixel, ARGBType type) {
        ARGB8 value = (ARGB8)pixel;                
        type.set(ARGBType.rgba(value.getR(), value.getG(), value.getB(), pixel.getAlpha()));
    }

    @Override
    public PixelTypes getPixelType() {
        return PixelTypes.RGB_16;
    }
}