package fr.fresnel.fourPolar.io.image.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffWriter;
import fr.fresnel.fourPolar.io.image.polarization.file.TiffPolarizationImageFileSet;

public class TiffPolarizationImageSetReaderTest {
    final private static File _root = new File(TiffPolarizationImageSetReaderTest.class.getResource("").getPath(),
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
            throws IOException, CannotFormPolarizationImageSet {
        // Create polarization image set.
        long[] dim = { 2, 2, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        // File set
        int channel = 1;
        String setNameAlias = "testSet";
        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setNameAlias, channel);

        // Write to presumed destination.
        TiffPolarizationImageFileSet fSet = new TiffPolarizationImageFileSet(_root, fileSet, channel);
        SCIFIOUINT16TiffWriter writer = new SCIFIOUINT16TiffWriter();
        writer.write(fSet.getFile(Polarization.pol0), pol0);
        writer.write(fSet.getFile(Polarization.pol45), pol45);
        writer.write(fSet.getFile(Polarization.pol90), pol90);
        writer.write(fSet.getFile(Polarization.pol135), pol135);

        ImageFactory factory = new ImgLib2ImageFactory();
        IPolarizationImageSetReader reader = new TiffPolarizationImageSetReader(factory, channel);
        IPolarizationImageSet imageSet = reader.read(_root, fileSet, channel);

        assertTrue(Arrays.equals(imageSet.getPolarizationImage(Polarization.pol0).getImage().getMetadata().getDim(),
                dim)
                && Arrays.equals(imageSet.getPolarizationImage(Polarization.pol45).getImage().getMetadata().getDim(),
                        dim)
                && Arrays.equals(imageSet.getPolarizationImage(Polarization.pol90).getImage().getMetadata().getDim(),
                        dim)
                && Arrays.equals(imageSet.getPolarizationImage(Polarization.pol135).getImage().getMetadata().getDim(),
                        dim));
    }

}

class ReaderDummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;

    public ReaderDummyCapturedImageFileSet(String setName) {
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
