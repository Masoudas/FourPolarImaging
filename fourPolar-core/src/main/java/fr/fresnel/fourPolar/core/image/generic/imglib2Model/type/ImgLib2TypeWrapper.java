package fr.fresnel.fourPolar.core.image.generic.imglib2Model.type;

import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import net.imglib2.type.NativeType;

public interface ImgLib2TypeWrapper {
    /**
     * Wraps the given {@code NativeType} into the proper type of our own. 
     */
    public <T extends NativeType<T>> IPixel<?> getPixel(T type);
}