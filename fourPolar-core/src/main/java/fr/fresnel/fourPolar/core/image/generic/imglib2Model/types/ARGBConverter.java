package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import net.imglib2.type.numeric.ARGBType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class ARGBConverter implements TypeConverter<ARGBType> {
    @Override
    public PixelType getPixelType(ARGBType type) {
        ARGBType value = (ARGBType)type;
        
        int index = value.get();
        int red = ARGBType.red(index);
        int blue = ARGBType.blue(index);
        int green = ARGBType.green(index);

        return new RGB16(red, green, blue);
    }

    @Override
    public void setNativeType(PixelType pixel, ARGBType type) {
        RGB16 value = (RGB16)pixel;                
        type.set(ARGBType.rgba(value.getR(), value.getG(), value.getB(), 0));
    }
}