package fr.fresnel.fourPolar.io.image.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.io.image.polarization.file.TiffPolarizationImageFileSet;

public class TiffPolarizationImageSetWriterTest {
    final private static File _root = new File(TiffPolarizationImageSetReaderTest.class.getResource("").getPath(),
            "Writer");

    @Test
    public void write_ImgLib2PolarizationImage_WritesIntoTheTargetFolder()
            throws CannotFormPolarizationImageSet, IOException {
        int channel = 1;
        String setNameAlias = "testSet";

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setNameAlias, channel);

        // Create polarization image set.
        long[] dim = { 2, 2, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        IPolarizationImageSet imageSet = new PolarizationImageSetBuilder(channel).fileSet(fileSet).pol0(pol0)
                .pol45(pol45).pol90(pol90).pol135(pol135).channel(channel).build();

        IPolarizationImageSetWriter writer = new TiffPolarizationImageSetWriter();
        writer.write(_root, imageSet);

        TiffPolarizationImageFileSet fSet = new TiffPolarizationImageFileSet(_root, fileSet, channel);
        assertTrue(fSet.getFile(Polarization.pol0).exists() && fSet.getFile(Polarization.pol45).exists()
                && fSet.getFile(Polarization.pol90).exists() && fSet.getFile(Polarization.pol135).exists());
    }
}

class WriterDummyCapturedImageFileSet implements ICapturedImageFileSet {
    String setName;
    int channel;

    public WriterDummyCapturedImageFileSet(String setName, int channel) {
        this.setName = setName;
        this.channel = channel;
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

