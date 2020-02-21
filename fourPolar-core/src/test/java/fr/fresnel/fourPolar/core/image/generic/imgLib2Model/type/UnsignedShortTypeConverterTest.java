package fr.fresnel.fourPolar.core.image.generic.imgLib2Model.type;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class UnsignedShortTypeConverterTest {

    @Test
    public void getPixel_UnsignedShortType_WrapsValueInuint16() throws ConverterNotFound {
        UnsignedShortType val1 = new UnsignedShortType(0);
        UnsignedShortType val2 = new UnsignedShortType(0xffff);

        TypeConverter<UnsignedShortType> converter = TypeConverterFactory.getConverter(new UnsignedShortType());

        UINT16 val1Uint16 = (UINT16)converter.getPixelType(val1);
        UINT16 val2Uint16 = (UINT16)converter.getPixelType(val2);

        assertTrue(val1Uint16.get() == val1.get() && val2Uint16.get() == val2.get());
    }
    
}