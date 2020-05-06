package fr.fresnel.fourPolar.io.image.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.UINT16SCIFIOTiffImageWriter;
import fr.fresnel.fourPolar.io.image.polarization.file.TiffPolarizationImageFileSet;

public class TiffPolarizationImageSetReaderTest {
    final private static File _root = new File(TiffPolarizationImageSetReaderTest.class.getResource("").getPath(),
            "TiffPolarizationImageSetReader");

    @Test
    public void read_PolarizationImageSetFromDisk_ReadImagesHaveSameDimensionAsDisk()
            throws NoReaderFoundForImage, IOException, CannotFormPolarizationImageSet {
        File capturedImageFile = new File(_root, "testFile.tif");
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, capturedImageFile);
        TiffPolarizationImageFileSet fSet = new TiffPolarizationImageFileSet(_root, fileSet);

        long[] dim = { 2, 2 };
        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim, UINT16.zero());

        UINT16SCIFIOTiffImageWriter writer = new UINT16SCIFIOTiffImageWriter();
        writer.write(fSet.getFile(Polarization.pol0), pol0);
        writer.write(fSet.getFile(Polarization.pol45), pol45);
        writer.write(fSet.getFile(Polarization.pol90), pol90);
        writer.write(fSet.getFile(Polarization.pol135), pol135);


        ImageFactory factory = new ImgLib2ImageFactory();
        IPolarizationImageSetReader reader = new TiffPolarizationImageSetReader(factory);
        IPolarizationImageSet imageSet = reader.read(_root, fileSet);

        assertTrue(       
            Arrays.equals(
                imageSet.getPolarizationImage(Polarization.pol0).getImage().getMetadata().getDim(), dim) &&
            Arrays.equals(
                imageSet.getPolarizationImage(Polarization.pol45).getImage().getMetadata().getDim(), dim) &&
            Arrays.equals(
                imageSet.getPolarizationImage(Polarization.pol90).getImage().getMetadata().getDim(), dim) &&
            Arrays.equals(
                imageSet.getPolarizationImage(Polarization.pol135).getImage().getMetadata().getDim(), dim));
    }

}