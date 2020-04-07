package fr.fresnel.fourPolar.io.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;

public class TiffOrientationImageWriterTest {
    final private static File _root = new File(TiffOrientationImageWriterTest.class.getResource("").getPath(),
            "TiffOrientationImageSetWriter");

    @Test
    public void write_ImgLib2ORientationImage_WritesIntoTheTargetFolder()
            throws CannotFormOrientationImage, NoWriterFoundForImage, IOException {
        File pol0_45_90_135_File = new File(_root, "testFile.tif");

        CapturedImageFileSet fileSet = new CapturedImageFileSet(
            1, pol0_45_90_135_File);

        long[] dim = { 2, 2 };
        Image<Float32> rho = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(dim, Float32.zero());

        IOrientationImage imageSet = new OrientationImage(fileSet, rho, delta, eta);

        IOrientationImageWriter writer = new TiffOrientationImageWriter(imageSet);
        writer.write(_root, imageSet);    

        TiffOrientationImageFileSet fSet = new TiffOrientationImageFileSet(_root, fileSet);
        assertTrue(
            fSet.getFile(OrientationAngle.rho).exists() &&
            fSet.getFile(OrientationAngle.delta).exists() &&
            fSet.getFile(OrientationAngle.eta).exists());
    }

    
}