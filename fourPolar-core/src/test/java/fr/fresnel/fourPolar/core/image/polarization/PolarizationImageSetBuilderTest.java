package fr.fresnel.fourPolar.core.image.polarization;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class PolarizationImageSetBuilderTest {
    @Test
    public void createClass_DifferentImageDimension_ThrowsCannotFormPolarizationImageSet() {
        long[] dim1 = { 1, 1, 1, 1, 1 };
        long[] dim2 = { 2, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();
        IMetadata metadata2 = new Metadata.MetadataBuilder(dim2).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata2, UINT16.zero());

        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSetBuilder(1).channel(1).pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol135).build();
        });
    }

    @Test
    public void createClass_DuplicateImage_ThrowsCannotFormPolarizationImageSet() {
        long[] dim1 = { 1, 1, 1, 1, 1 };
        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSetBuilder(1).channel(1).pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol90).build();
        });
    }

    @Test
    public void createClass_NotXYCZTImage_ThrowsIllegalArgumentException() {
        long[] dim1 = { 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            new PolarizationImageSetBuilder(1).channel(1).pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol135).build();
        });
    }

    @Test
    public void createClass_MultiChannelImage_ThrowsIllegalArgumentException() {
        long[] dim1 = { 1, 1, 2, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        assertThrows(IllegalArgumentException.class, () -> {
            new PolarizationImageSetBuilder(1).channel(1).pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol135).build();
        });
    }

    @Test
    public void getPolarizationImage_CreateTwoSetsWithSameBuilder_BuiltPolImagesAreUnequal()
            throws CannotFormPolarizationImageSet {
        long[] dim1 = { 1, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        Image<UINT16> pol0_1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol45_1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol90_1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol135_1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        PolarizationImageSetBuilder builder = new PolarizationImageSetBuilder(1);

        IPolarizationImageSet imageSet = builder.channel(1).pol0(pol0).pol45(pol45).pol90(pol90).pol135(pol135)
                .fileSet(new DummyFileSet()).build();

        IPolarizationImageSet imageSet_1 = builder.channel(1).pol0(pol0_1).pol45(pol45_1).pol90(pol90_1)
                .pol135(pol135_1).fileSet(new DummyFileSet()).build();

        assertTrue(imageSet.getPolarizationImage(Polarization.pol0).getImage() != imageSet_1
                .getPolarizationImage(Polarization.pol0).getImage()
                && imageSet.getPolarizationImage(Polarization.pol45).getImage() != imageSet_1
                        .getPolarizationImage(Polarization.pol45).getImage()
                && imageSet.getPolarizationImage(Polarization.pol90).getImage() != imageSet_1
                        .getPolarizationImage(Polarization.pol90).getImage()
                && imageSet.getPolarizationImage(Polarization.pol135).getImage() != imageSet_1
                        .getPolarizationImage(Polarization.pol135).getImage());
    }

}

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