package fr.fresnel.fourPolar.core.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import javassist.tools.reflect.CannotCreateException;

public class SingleChannelCapturedImageFileSetBuilderTest {
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
    public void build_AxisOrderHasChannel_ThrowsException() {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYC).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.One);

        assertThrows(IllegalArgumentException.class, () -> {
            new CapturedImageFileSetBuilder(metadata, setup);
        });
    }

    @Test
    public void build_BuildWithInsufficientFiles_ThrowsException() {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XY).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.One);

        assertThrows(CannotCreateException.class, () -> {
            new CapturedImageFileSetBuilder(metadata, setup).add(1, pol0_90_45_135).build();
        });
    }

    @Test
    public void build_createTwoSeparateSetsOneCam_ReturnsTwoSets() throws CannotCreateException {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYZ).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.One);

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(metadata,
                setup);

        ICapturedImageFileSet fileSet = builder.add(1, pol0_90_45_135).add(2, pol0_90_45_135_1).build();
        ICapturedImageFileSet fileSet_1 = builder.add(1, pol0_90_45_135_2).add(2, pol0_90_45_135_3).build();

        String label = Cameras.getLabels(Cameras.One)[0];
        assertTrue(!Arrays.equals(fileSet.getFile(label), fileSet_1.getFile(label))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsTwoCam_ReturnsTwoSets() throws CannotCreateException {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYZ).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.Two);

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(metadata,
                setup);

        ICapturedImageFileSet fileSet = builder.add(1, pol0_90, pol45_135).add(2, pol0_90_1, pol45_135_1).build();
        ICapturedImageFileSet fileSet_1 = builder.add(1, pol0_90_2, pol45_135_2).add(2, pol0_90_3, pol45_135_3).build();

        String[] labels = Cameras.getLabels(Cameras.Two);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsFourCam_ReturnsTwoSets() throws CannotCreateException {
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(AxisOrder.XYZ).build();
        FourPolarImagingSetup setup = new FourPolarImagingSetup(2, Cameras.Four);

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(metadata,
                setup);

        ICapturedImageFileSet fileSet = builder.add(1, pol0, pol45, pol90, pol135)
                .add(2, pol0_1, pol45_1, pol90_1, pol135_1).build();
        ICapturedImageFileSet fileSet_1 = builder.add(1, pol0_2, pol45_2, pol90_2, pol135_2)
                .add(2, pol0_3, pol45_3, pol90_3, pol135_3).build();

        String[] labels = Cameras.getLabels(Cameras.Four);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !Arrays.equals(fileSet.getFile(labels[2]), fileSet_1.getFile(labels[2]))
                && !Arrays.equals(fileSet.getFile(labels[3]), fileSet_1.getFile(labels[3]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

}