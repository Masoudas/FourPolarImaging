package fr.fresnel.fourPolar.core.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import javassist.tools.reflect.CannotCreateException;

public class MultiChannelCapturedImageFileSetBuilderTest {
    File pol0_90_45_135 = new File("pol0_90_45_135.tiff");
    File pol0_90_45_135_1 = new File("pol0_90_45_135_1.tiff");
    File pol0_90_45_135_2 = new File("pol0_90_45_135_2.tiff");
    File pol0_90_45_135_3 = new File("pol0_90_45_135_3.tiff");

    File pol0_90 = new File("pol0_90.tiff");
    File pol45_135 = new File("pol45_135.tiff");

    File pol0_90_1 = new File("pol0_90_1.tiff");
    File pol45_135_1 = new File("pol45_135_1.tiff");

    File pol0_90_2 = new File("pol0_90_2.tiff");
    File pol45_135_2 = new File("pol45_135_2.tiff");

    File pol0_90_3 = new File("pol0_90_3.tiff");
    File pol45_135_3 = new File("pol45_135_3.tiff");

    File pol0 = new File("pol0.tiff");
    File pol45 = new File("pol45.tiff");
    File pol90 = new File("pol90.tiff");
    File pol135 = new File("pol135.tiff");

    File pol0_1 = new File("pol0_1.tiff");
    File pol45_1 = new File("pol45_1.tiff");
    File pol90_1 = new File("pol90_1.tiff");
    File pol135_1 = new File("pol135_1.tiff");

    File pol0_2 = new File("pol0_2.tiff");
    File pol45_2 = new File("pol45_2.tiff");
    File pol90_2 = new File("pol90_2.tiff");
    File pol135_2 = new File("pol135_2.tiff");

    File pol0_3 = new File("pol0_3.tiff");
    File pol45_3 = new File("pol45_3.tiff");
    File pol90_3 = new File("pol90_3.tiff");
    File pol135_3 = new File("pol135_3.tiff");

    @Test
    public void build_AxisOrderHasNoChannel_ThrowsException() {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XY).numChannels(2).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.One);

        assertThrows(IllegalArgumentException.class, () -> {
            new MultiChannelCapturedImageFileSetBuilder(metadata, setup);
        });
    }

    @Test
    public void build_BuildWithInsufficientFiles_ThrowsException() {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYC).numChannels(2).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.Two);

        assertThrows(IllegalArgumentException.class, () -> {
            new MultiChannelCapturedImageFileSetBuilder(metadata, setup).build(pol0_90_45_135);
        });
    }

    @Test
    public void build_createTwoSeparateSetsOneCam_ReturnsTwoSets() throws CannotCreateException {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYC).numChannels(2).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.One);

        MultiChannelCapturedImageFileSetBuilder builder = new MultiChannelCapturedImageFileSetBuilder(metadata, setup);

        ICapturedImageFileSet fileSet = builder.build(pol0_90_45_135);
        ICapturedImageFileSet fileSet_1 = builder.build(pol0_90_45_135_1);

        String label = Cameras.getLabels(Cameras.One)[0];
        assertTrue(!Arrays.equals(fileSet.getFile(label), fileSet_1.getFile(label))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsTwoCam_ReturnsTwoSets() throws CannotCreateException {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYZC).numChannels(2).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.Two);

        MultiChannelCapturedImageFileSetBuilder builder = new MultiChannelCapturedImageFileSetBuilder(metadata, setup);

        ICapturedImageFileSet fileSet = builder.build(pol0_90, pol45_135);
        ICapturedImageFileSet fileSet_1 = builder.build(pol0_90_1, pol45_135_1);

        String[] labels = Cameras.getLabels(Cameras.Two);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsFourCam_ReturnsTwoSets() throws CannotCreateException {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYZC).numChannels(2).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.Four);

        MultiChannelCapturedImageFileSetBuilder builder = new MultiChannelCapturedImageFileSetBuilder(metadata, setup);

        ICapturedImageFileSet fileSet = builder.build(pol0, pol45, pol90, pol135);
        ICapturedImageFileSet fileSet_1 = builder.build(pol0_1, pol45_1, pol90_1, pol135_1);

        String[] labels = Cameras.getLabels(Cameras.Four);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !Arrays.equals(fileSet.getFile(labels[2]), fileSet_1.getFile(labels[2]))
                && !Arrays.equals(fileSet.getFile(labels[3]), fileSet_1.getFile(labels[3]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

}