package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class ImgLib2ImageTest {
    @Test
    public void imgInterfaceMethods_UnsignedShortImage_AllMethodsShouldReturnTheSameResult() throws ConverterNotFound {
        long[] dimensions = new long[] { 1, 1, 1, 1 };
        UnsignedShortType type = new UnsignedShortType();
        Img<UnsignedShortType> img = new ArrayImgFactory<UnsignedShortType>(type).create(dimensions);

        ImgLib2Image<UINT16, UnsignedShortType> image = new ImgLib2Image<UINT16, UnsignedShortType>(img, type);


        assertTrue(// img.cursor().hashCode() == image.cursor().hashCode() && 
            img.dimension(0) == image.dimension(0) && 
            img.factory() == image.factory() &&
            img.firstElement() == image.firstElement() &&
            img.iterationOrder() == image.iterationOrder() &&
            img.iterator() == image.iterator() &&
            img.localizingCursor() == image.localizingCursor() &&
            img.max(1) == image.max(1) &&
            img.min(1) == image.min(1) &&
            img.numDimensions() == image.numDimensions() &&
            img.randomAccess() == image.randomAccess() &&
            img.realMax(1) == image.realMax(1) &&
            img.realMin(1) == img.realMin(1) &&
            img.size() == image.size() &&
            img.spliterator() == image.spliterator() &&
            img.toString().equals(image.toString()));
    }
}

