package fr.fresnel.fourPolar.core.image.generic.Metadata;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class MetadataUtilTest {
    @Test
    public void getNPlanes_ReturnsCorrectNPlanes() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2, 2, 2, 2 }).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        assertTrue(MetadataUtil.getNPlanes(image.getMetadata()) == 16);

    }
}