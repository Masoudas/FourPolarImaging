package fr.fresnel.fourPolar.io.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOFloat32TiffWriter;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;

public class TiffOrientationImageReaderTest {
    final private static File _root = new File(TiffOrientationImageReaderTest.class.getResource("").getPath(),
            "TiffOrientationImageReader");

    @Test
    public void read_PolarizationImageSetFromDisk_ReadImagesHaveSameDimensionAsDisk()
            throws NoReaderFoundForImage, IOException, CannotFormOrientationImage {
        File capturedImageFile = new File(_root, "testFile.tif");
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, capturedImageFile);
        TiffOrientationImageFileSet fSet = new TiffOrientationImageFileSet(_root, fileSet);
        
        long[] dim = { 2, 2 };
        Image<Float32> rho = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(dim, Float32.zero());

        SCIFIOFloat32TiffWriter writer = new SCIFIOFloat32TiffWriter();
        writer.write(fSet.getFile(OrientationAngle.rho), rho);
        writer.write(fSet.getFile(OrientationAngle.delta), delta);
        writer.write(fSet.getFile(OrientationAngle.eta), eta);
        
        ImageFactory factory = new ImgLib2ImageFactory();
        IOrientationImageReader reader = new TiffOrientationImageReader(factory);
        IOrientationImage imageSet = reader.read(_root, fileSet);

        assertTrue(
            Arrays.equals(
                imageSet.getAngleImage(OrientationAngle.rho).getImage().getMetadata().getDim(), dim) && 
            Arrays.equals(
                imageSet.getAngleImage(OrientationAngle.delta).getImage().getMetadata().getDim(), dim) && 
            Arrays.equals(
                imageSet.getAngleImage(OrientationAngle.eta).getImage().getMetadata().getDim(), dim));
    }

}