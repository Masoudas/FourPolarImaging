package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import net.imglib2.type.numeric.ARGBType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class ARGBConverter implements TypeConverter<RGB16, ARGBType> {
    @Override
    public void setPixelType(ARGBType type, RGB16 pixel) {
        ARGBType value = (ARGBType)type;
        
        int index = value.get();
        int red = ARGBType.red(index);
        int blue = ARGBType.blue(index);
        int green = ARGBType.green(index);

        RGB16 rgb16 = (RGB16)pixel;
        rgb16.set(red, green, blue);
    }

    @Override
    public void setNativeType(RGB16 pixel, ARGBType type) {
        RGB16 value = (RGB16)pixel;                
        type.set(ARGBType.rgba(value.getR(), value.getG(), value.getB(), 0));
    }

    @Override
    public Type getPixelType() {
        return Type.RGB_16;
    }
}