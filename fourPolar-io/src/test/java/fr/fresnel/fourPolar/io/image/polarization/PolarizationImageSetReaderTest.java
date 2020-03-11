package fr.fresnel.fourPolar.io.image.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.fileSet.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;

public class PolarizationImageSetReaderTest {
    final private static File _root = new File(PolarizationImageSetReaderTest.class.getResource("").getPath(),
            "PolarizationImageSetReader");

    @Test
    public void read_PolarizationImageSetFromDisk_ReadImagesHaveSameDimensionAsDisk()
            throws NoReaderFoundForImage, IOException, CannotFormPolarizationImageSet {
        

        File capturedImageFile = new File(_root, "testFile.tif");
        ICapturedImageFileSet fileSet = new CapturedImageFileSet(1, capturedImageFile);

        ImageFactory factory = new ImgLib2ImageFactory();
        IPolarizationImageSetReader reader = new PolarizationImageSetReader(factory);
        IPolarizationImageSet imageSet = reader.read(_root, fileSet);

        long[] dim = { 2, 2 };
        assertTrue(       
            Arrays.equals(imageSet.getPolarizationImage(Polarization.pol0).getImage().getDimensions(), dim) &&
            Arrays.equals(imageSet.getPolarizationImage(Polarization.pol45).getImage().getDimensions(), dim) &&
            Arrays.equals(imageSet.getPolarizationImage(Polarization.pol90).getImage().getDimensions(), dim) &&
            Arrays.equals(imageSet.getPolarizationImage(Polarization.pol135).getImage().getDimensions(), dim));
    }

}