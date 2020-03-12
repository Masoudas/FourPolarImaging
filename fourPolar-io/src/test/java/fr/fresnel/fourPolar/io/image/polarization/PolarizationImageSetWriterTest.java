package fr.fresnel.fourPolar.io.image.polarization;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.fileSet.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;

public class PolarizationImageSetWriterTest {
    final private static File _root = new File(PolarizationImageSetReaderTest.class.getResource("").getPath(),
            "PolarizationImageSetWriter");

    @Test
    public void write_ImgLib2PolarizationImage_WritesIntoTheTargetFolder()
            throws CannotFormPolarizationImageSet, IOException, NoWriterFoundForImage {
        File pol0_45_90_135_File = new File(_root, "testFile.tif");

        CapturedImageFileSet fileSet = new CapturedImageFileSet(
            1, pol0_45_90_135_File);

        long[] dim = { 2, 2 };

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim, new UINT16());

        PolarizationImageSet imageSet = new PolarizationImageSet(fileSet, pol0, pol45, pol90, pol135);

        IPolarizationImageSetWriter writer = new PolarizationImageSetWriter(imageSet);
        writer.write(_root, imageSet);    
    }
}