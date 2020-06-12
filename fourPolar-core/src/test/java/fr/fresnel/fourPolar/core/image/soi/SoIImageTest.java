package fr.fresnel.fourPolar.core.image.soi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class SoIImageTest {
    @Test
    public void create_NotXYCZTImage_ThrowsIllegalArgumentException() {
        long[] dim = { 10, 10, 10, 3 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYZT).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> SoIImage.create(new DummyFileSet(), image, 1));

    }

    @Test
    public void create_MultiChannelImage_ThrowsIllegalArgumentException() {
        long[] dim = { 10, 10, 2, 10, 3 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> SoIImage.create(new DummyFileSet(), image, 1));

    }

    @Test
    public void create_FromPolarizationImage_ReturnsSameSizeSoIImage() {
        IPolarizationImageSet polarizationImageSet = new DummyPolImgSet();
        ISoIImage soiImage = SoIImage.create(polarizationImageSet);

        assertArrayEquals(
                polarizationImageSet.getPolarizationImage(Polarization.pol0).getImage().getMetadata().getDim(),
                soiImage.getImage().getMetadata().getDim());
    }
}

class DummyPolImgSet implements IPolarizationImageSet {

    @Override
    public IPolarizationImage getPolarizationImage(Polarization pol) {
        return new PImg();
    }

    @Override
    public ICapturedImageFileSet getFileSet() {
        return null;
    }

    @Override
    public IIntensityVectorIterator getIterator() {
        return null;
    }

    @Override
    public int channel() {
        return 1;
    }

}

class PImg implements IPolarizationImage {

    @Override
    public Polarization getPolarization() {
        return null;
    }

    @Override
    public Image<UINT16> getImage() {
        long[] dim = { 10, 10, 1, 10, 3 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        return image;
    }
};

class DummyFileSet implements ICapturedImageFileSet {

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return null;
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