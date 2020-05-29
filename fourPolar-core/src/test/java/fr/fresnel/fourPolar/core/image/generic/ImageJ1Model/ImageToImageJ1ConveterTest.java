package fr.fresnel.fourPolar.core.image.generic.ImageJ1Model;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import ij.ImagePlus;

public class ImageToImageJ1ConveterTest {
    @Test
    public void convert_RGB16Image_ChangeToImagePlusIsReflectedInImageInterface() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 1, 1 }).axisOrder(AxisOrder.XYZCT)
                .build();
        Image<RGB16> image = new ImgLib2ImageFactory().create(metadata, RGB16.zero());

        ImagePlus imagePlus = ImageToImageJ1Conveter.convertToImgPlus(image, RGB16.zero());
        imagePlus.getProcessor().setPixels(new int[] { 1 });

        RGB16 pixelVal = image.getCursor().next().value();

        // TODO how should I write this test?
    }
}