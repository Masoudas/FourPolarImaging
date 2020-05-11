package fr.fresnel.fourPolar.core.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import javassist.tools.reflect.CannotCreateException;

public class CapturedImageFileSetBuilderTest {
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
    public void build_BuildWithInsufficientFiles_ThrowsException() {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.Two);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        assertThrows(IllegalArgumentException.class, () -> {
            new CapturedImageFileSetBuilder(setup, new DummyChecker()).add(new int[] { 1, 2 }, pol0_90_45_135).build();
        });
    }

    @Test
    public void build_BuildWithInsufficientChannels_ThrowsException() {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.One);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        assertThrows(CannotCreateException.class, () -> {
            new CapturedImageFileSetBuilder(setup, new DummyChecker()).add(new int[] { 1 }, pol0_90_45_135).build();
        });
    }

    @Test
    public void build_BuildWithRepetitiveChannels_ThrowsException() {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.One);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        assertThrows(CannotCreateException.class, () -> {
            new CapturedImageFileSetBuilder(setup, new DummyChecker()).add(new int[] { 1 }, pol0_90_45_135)
                    .add(new int[] { 1 }, pol0_90_45_135).build();
        });
    }

    @Test
    public void build_createTwoSeparateSetsOneCamMultiChannel_ReturnsTwoSets()
            throws CannotCreateException, IncompatibleCapturedImage {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.One);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(setup, new DummyChecker());

        ICapturedImageFileSet fileSet = builder.add(new int[] { 1, 2 }, pol0_90_45_135).build();
        ICapturedImageFileSet fileSet_1 = builder.add(new int[] { 1, 2 }, pol0_90_45_135_2).build();

        String label = Cameras.getLabels(Cameras.One)[0];
        assertTrue(!Arrays.equals(fileSet.getFile(label), fileSet_1.getFile(label))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsOneCamSingleChannel_ReturnsTwoSets()
            throws CannotCreateException, IncompatibleCapturedImage {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.One);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(setup, new DummyChecker());

        ICapturedImageFileSet fileSet = builder.add(new int[] { 1 }, pol0_90_45_135)
                .add(new int[] { 2 }, pol0_90_45_135_1).build();
        ICapturedImageFileSet fileSet_1 = builder.add(new int[] { 1 }, pol0_90_45_135_2)
                .add(new int[] { 2 }, pol0_90_45_135_3).build();

        String label = Cameras.getLabels(Cameras.One)[0];
        assertTrue(!Arrays.equals(fileSet.getFile(label), fileSet_1.getFile(label))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsTwoCamSingleChannel_ReturnsTwoSets()
            throws CannotCreateException, IncompatibleCapturedImage {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.Two);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(setup, new DummyChecker());

        ICapturedImageFileSet fileSet = builder.add(new int[] { 1 }, pol0_90, pol45_135)
                .add(new int[] { 2 }, pol0_90_1, pol45_135_1).build();
        ICapturedImageFileSet fileSet_1 = builder.add(new int[] { 1 }, pol0_90_2, pol45_135_2)
                .add(new int[] { 2 }, pol0_90_3, pol45_135_3).build();

        String[] labels = Cameras.getLabels(Cameras.Two);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsTwoCamMultiChannel_ReturnsTwoSets()
            throws CannotCreateException, IncompatibleCapturedImage {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.Two);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(setup, new DummyChecker());

        ICapturedImageFileSet fileSet = builder.add(new int[] { 1, 2 }, pol0_90, pol45_135).build();
        ICapturedImageFileSet fileSet_1 = builder.add(new int[] { 1, 2 }, pol0_90_2, pol45_135_2).build();

        String[] labels = Cameras.getLabels(Cameras.Two);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void build_createTwoSeparateSetsFourCamSingleChannel_ReturnsTwoSets() throws CannotCreateException,
            IncompatibleCapturedImage {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.Four);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(setup, new DummyChecker());

        ICapturedImageFileSet fileSet = builder.add(new int[]{1}, pol0, pol45, pol90, pol135)
                .add(new int[]{2}, pol0_1, pol45_1, pol90_1, pol135_1).build();
        ICapturedImageFileSet fileSet_1 = builder.add(new int[]{1}, pol0_2, pol45_2, pol90_2, pol135_2)
                .add(new int[]{2}, pol0_3, pol45_3, pol90_3, pol135_3).build();

        String[] labels = Cameras.getLabels(Cameras.Four);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !Arrays.equals(fileSet.getFile(labels[2]), fileSet_1.getFile(labels[2]))
                && !Arrays.equals(fileSet.getFile(labels[3]), fileSet_1.getFile(labels[3]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }


    @Test
    public void build_createTwoSeparateSetsFourCamMultiChannel_ReturnsTwoSets() throws CannotCreateException,
            IncompatibleCapturedImage {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(Cameras.Four);
        setup.setChannel(1, new Channel(1, 1, 1, 1, 1));
        setup.setChannel(2, new Channel(2, 1, 1, 1, 1));

        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(setup, new DummyChecker());

        ICapturedImageFileSet fileSet = builder.add(new int[]{1, 2}, pol0, pol45, pol90, pol135).build();
        ICapturedImageFileSet fileSet_1 = builder.add(new int[]{1, 2}, pol0_2, pol45_2, pol90_2, pol135_2).build();

        String[] labels = Cameras.getLabels(Cameras.Four);
        assertTrue(!Arrays.equals(fileSet.getFile(labels[0]), fileSet_1.getFile(labels[0]))
                && !Arrays.equals(fileSet.getFile(labels[1]), fileSet_1.getFile(labels[1]))
                && !Arrays.equals(fileSet.getFile(labels[2]), fileSet_1.getFile(labels[2]))
                && !Arrays.equals(fileSet.getFile(labels[3]), fileSet_1.getFile(labels[3]))
                && !fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

}

/**
 * A dummy checker, always checks to valid.
 */
class DummyChecker implements ICapturedImageChecker {
    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public void check(ICapturedImageFile capturedImageFile) throws IncompatibleCapturedImage {

    }

}