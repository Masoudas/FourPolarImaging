package fr.fresnel.fourPolar.io.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.io.image.orientation.file.TiffOrientationImageFileSet;

public class TiffOrientationImageWriterTest {
    final private static File _root = new File(TiffOrientationImageWriterTest.class.getResource("").getPath(),
            "TiffOrientationImageSetWriter");

    @Test
    public void write_ImgLib2ORientationImage_WritesIntoTheTargetFolder()
            throws CannotFormOrientationImage, IOException {
        int channel = 1;
        String setNameAlias = "testSet";

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setNameAlias);

        // Create orientation image to be written.
        long[] dim = { 2, 2, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        Image<Float32> rho = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> delta = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> eta = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        IOrientationImage imageSet = OrientationImageFactory.create(fileSet, channel, rho, delta, eta);

        IOrientationImageWriter writer = new TiffOrientationImageWriter();
        writer.write(_root, imageSet);

        TiffOrientationImageFileSet fSet = new TiffOrientationImageFileSet(_root, fileSet, channel);
        assertTrue(fSet.getFile(OrientationAngle.rho).exists() && fSet.getFile(OrientationAngle.delta).exists()
                && fSet.getFile(OrientationAngle.eta).exists());
    }

}

class WriterDummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;

    public WriterDummyCapturedImageFileSet(String setName) {
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
