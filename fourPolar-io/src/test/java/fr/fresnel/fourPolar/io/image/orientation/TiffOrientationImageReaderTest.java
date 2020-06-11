package fr.fresnel.fourPolar.io.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOFloat32TiffWriter;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;

public class TiffOrientationImageReaderTest {
    final private static File _root = new File(TiffOrientationImageReaderTest.class.getResource("").getPath(),
            "Reader");

    @BeforeAll
    public static void createPath() {
        _root.mkdirs();
    }

    /**
     * The premise of the test is write and then read an orientation image.
     * 
     */
    @Test
    public void read_PolarizationImageSetFromDisk_ReadImagesHaveSameDimensionAsDisk()
            throws IOException, CannotFormOrientationImage {
        // Create an alias for a captured set.
        String setName = "TestCapturedSet";
        int channel = 1;
        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet(setName);
        TiffOrientationImageFileSet fSet = new TiffOrientationImageFileSet(_root, fileSet, channel);

        // Create orientation image to be written.
        long[] dim = { 2, 2, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        // Write the orientation images manually.
        SCIFIOFloat32TiffWriter writer = new SCIFIOFloat32TiffWriter();
        writer.write(fSet.getFile(OrientationAngle.rho), rho);
        writer.write(fSet.getFile(OrientationAngle.delta), delta);
        writer.write(fSet.getFile(OrientationAngle.eta), eta);

        ImageFactory factory = new ImgLib2ImageFactory();
        IOrientationImageReader reader = new TiffOrientationImageReader(factory, channel);
        IOrientationImage imageSet = reader.read(_root, fileSet, channel);

        assertTrue(Arrays.equals(imageSet.getAngleImage(OrientationAngle.rho).getImage().getMetadata().getDim(), dim)
                && Arrays.equals(imageSet.getAngleImage(OrientationAngle.delta).getImage().getMetadata().getDim(), dim)
                && Arrays.equals(imageSet.getAngleImage(OrientationAngle.eta).getImage().getMetadata().getDim(), dim));
    }

}

class DummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;

    public DummyCapturedImageFileSet(String setName) {
        this.setName = setName;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return this.setName;
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        return null;
    }

    @Override
    public int[] getChannels() {
        return null;
    }

}
