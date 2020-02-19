package fr.fresnel.fourPolar.core.image.generic.imglib2Model.types;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.uint16;
import net.imglib2.type.numeric.ARGBType;

/**
 * Creates a suitable pixel type (of ours) based on the passed data type of
 * Imglib2.
 */
class ARGBConverter implements TypeConverter<ARGBType> {
    @Override
    public PixelType getPixel(ARGBType type) {
        ARGBType value = (ARGBType)type;
        
        int index = value.get();
        int red = ARGBType.red(index);
        int blue = ARGBType.blue(index);
        int green = ARGBType.green(index);

        return new RGB(new uint16(red), new uint16(blue), new uint16(green));
    }

    @Override
    public void setNativeType(PixelType pixel, ARGBType type) {
        RGB value = (RGB)pixel;                
        type.set(ARGBType.rgba(value.getR(), value.getG(), value.getB(), 0));
    }
}