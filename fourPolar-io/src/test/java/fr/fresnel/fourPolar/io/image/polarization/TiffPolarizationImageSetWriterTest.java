package fr.fresnel.fourPolar.io.image.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.fileSet.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.polarization.fileSet.TiffPolarizationImageFileSet;

public class TiffPolarizationImageSetWriterTest {
    final private static File _root = new File(TiffPolarizationImageSetReaderTest.class.getResource("").getPath(),
            "TiffPolarizationImageSetWriter");

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

        IPolarizationImageSetWriter writer = new TiffPolarizationImageSetWriter(imageSet);
        writer.write(_root, imageSet);    

        TiffPolarizationImageFileSet fSet = new TiffPolarizationImageFileSet(_root, fileSet);
        assertTrue(
            fSet.getFile(Polarization.pol0).exists() &&
            fSet.getFile(Polarization.pol45).exists() &&
            fSet.getFile(Polarization.pol90).exists() &&
            fSet.getFile(Polarization.pol135).exists());
    }
}