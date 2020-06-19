package fr.fresnel.fourPolar.core.image.captured;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

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

/**
 * CapturedImageSetTest
 */
public class CapturedImageSetTest {
    private String root = "/";

    @Test
    public void hasMultiChannelImage_FourCamASetWithOneMultiChannelFile_ReturnsTrue() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1, 2 };
        int[] channel2 = { 3 };

        ICapturedImageFile[] pol0 = { new DummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        ICapturedImage[] pol0image = {
                new DummyCapturedImage(pol0[0], Cameras.getLabels(camera)[0], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol0[1], Cameras.getLabels(camera)[0], createImage(channel2.length), channel2) };

        ICapturedImageFile[] pol45 = { new DummyCapturedImageFile(channel1, new File(root, "pol45.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol451.tiff")) };
        ICapturedImage[] pol45image = {
                new DummyCapturedImage(pol45[0], Cameras.getLabels(camera)[1], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol45[1], Cameras.getLabels(camera)[1], createImage(channel2.length),
                        channel2) };

        ICapturedImageFile[] pol90 = { new DummyCapturedImageFile(channel1, new File(root, "pol90.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol901.tiff")) };
        ICapturedImage[] pol90image = {
                new DummyCapturedImage(pol90[0], Cameras.getLabels(camera)[2], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol90[1], Cameras.getLabels(camera)[2], createImage(channel2.length),
                        channel2) };

        ICapturedImageFile[] pol135 = { new DummyCapturedImageFile(channel1, new File(root, "pol135.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol1351.tiff")) };
        ICapturedImage[] pol135image = {
                new DummyCapturedImage(pol135[0], Cameras.getLabels(camera)[3], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol135[1], Cameras.getLabels(camera)[3], createImage(channel2.length),
                        channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);
        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], pol0image);
        builder.setImages(Cameras.getLabels(camera)[1], pol45image);
        builder.setImages(Cameras.getLabels(camera)[2], pol90image);
        builder.setImages(Cameras.getLabels(camera)[3], pol135image);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.hasMultiChannelImage());
    }

    @Test
    public void hasMultiChannelImage_FourCamASetWithNoMultiChannelFile_ReturnsFalse() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        ICapturedImageFile[] pol0 = { new DummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        ICapturedImage[] pol0image = {
                new DummyCapturedImage(pol0[0], Cameras.getLabels(camera)[0], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol0[1], Cameras.getLabels(camera)[0], createImage(channel2.length), channel2) };

        ICapturedImageFile[] pol45 = { new DummyCapturedImageFile(channel1, new File(root, "pol45.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol451.tiff")) };
        ICapturedImage[] pol45image = {
                new DummyCapturedImage(pol45[0], Cameras.getLabels(camera)[1], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol45[1], Cameras.getLabels(camera)[1], createImage(channel2.length),
                        channel2) };

        ICapturedImageFile[] pol90 = { new DummyCapturedImageFile(channel1, new File(root, "pol90.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol901.tiff")) };
        ICapturedImage[] pol90image = {
                new DummyCapturedImage(pol90[0], Cameras.getLabels(camera)[2], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol90[1], Cameras.getLabels(camera)[2], createImage(channel2.length),
                        channel2) };

        ICapturedImageFile[] pol135 = { new DummyCapturedImageFile(channel1, new File(root, "pol135.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol1351.tiff")) };
        ICapturedImage[] pol135image = {
                new DummyCapturedImage(pol135[0], Cameras.getLabels(camera)[3], createImage(channel1.length), channel1),
                new DummyCapturedImage(pol135[1], Cameras.getLabels(camera)[3], createImage(channel2.length),
                        channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);
        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], pol0image);
        builder.setImages(Cameras.getLabels(camera)[1], pol45image);
        builder.setImages(Cameras.getLabels(camera)[2], pol90image);
        builder.setImages(Cameras.getLabels(camera)[3], pol135image);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(!capturedImageSet.hasMultiChannelImage());
    }

    @Test
    public void getChannelPolarizationImage_OneCameraCaseNoMultiChannel_ReturnsFalse() {
        Cameras camera = Cameras.One;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        ICapturedImageFile[] pol = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c1Image = createImage(channel1.length);
        Image<UINT16> c2Image = createImage(channel1.length);

        ICapturedImage[] polimage = { new DummyCapturedImage(pol[0], Cameras.getLabels(camera)[0], c1Image, channel1),
                new DummyCapturedImage(pol[1], Cameras.getLabels(camera)[0], c2Image, channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol);
        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], polimage);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol0).getImage() == c1Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol45).getImage() == c1Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol90).getImage() == c1Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol135).getImage() == c1Image);

        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol0).getImage() == c2Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol45).getImage() == c2Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol90).getImage() == c2Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol135).getImage() == c2Image);
    }

    @Test
    public void getChannelPolarizationImage_OneCameraCaseMultiChannelImage_ReturnsFalse() {
        Cameras camera = Cameras.One;
        int[] channel1 = { 1, 2 };
        int[] channel2 = { 3, 4 };

        ICapturedImageFile[] pol = { new DummyCapturedImageFile(channel1, new File(root, "c12.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c34.tiff")) };

        Image<UINT16> c12Image = createImage(channel1.length);
        Image<UINT16> c34Image = createImage(channel1.length);

        ICapturedImage[] polimage = { new DummyCapturedImage(pol[0], Cameras.getLabels(camera)[0], c12Image, channel1),
                new DummyCapturedImage(pol[1], Cameras.getLabels(camera)[0], c34Image, channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol);
        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], polimage);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol0).getImage() == c12Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol45).getImage() == c12Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol90).getImage() == c12Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol135).getImage() == c12Image);

        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol0).getImage() == c12Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol45).getImage() == c12Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol90).getImage() == c12Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol135).getImage() == c12Image);

        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol0).getImage() == c34Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol45).getImage() == c34Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol90).getImage() == c34Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol135).getImage() == c34Image);

        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol0).getImage() == c34Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol45).getImage() == c34Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol90).getImage() == c34Image);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol135).getImage() == c34Image);

    }

    @Test
    public void getChannelPolarizationImage_TwoCameraCaseNoMultiChannel_ReturnsFalse() {
        Cameras camera = Cameras.Two;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        ICapturedImageFile[] pol_0_90 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c1Image_0_90 = createImage(channel1.length);
        Image<UINT16> c2Image_0_90 = createImage(channel1.length);
        ICapturedImage[] polimage_0_90 = {
                new DummyCapturedImage(pol_0_90[0], Cameras.getLabels(camera)[0], c1Image_0_90, channel1),
                new DummyCapturedImage(pol_0_90[1], Cameras.getLabels(camera)[0], c2Image_0_90, channel2) };

        ICapturedImageFile[] pol_45_135 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c1Image_45_135 = createImage(channel1.length);
        Image<UINT16> c2Image_45_135 = createImage(channel1.length);
        ICapturedImage[] polimage_45_135 = {
                new DummyCapturedImage(pol_45_135[0], Cameras.getLabels(camera)[0], c1Image_45_135, channel1),
                new DummyCapturedImage(pol_45_135[1], Cameras.getLabels(camera)[0], c2Image_45_135, channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol_0_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol_45_135);

        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], polimage_0_90);
        builder.setImages(Cameras.getLabels(camera)[1], polimage_45_135);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol0).getImage() == c1Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol45).getImage() == c1Image_45_135);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol90).getImage() == c1Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol135).getImage() == c1Image_45_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol0).getImage() == c2Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol45).getImage() == c2Image_45_135);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol90).getImage() == c2Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol135).getImage() == c2Image_45_135);
    }

    @Test
    public void getChannelPolarizationImage_TwoCameraCaseMultiChannelImage_ReturnsFalse() {
        Cameras camera = Cameras.Two;
        int[] channel1 = { 1, 2 };
        int[] channel2 = { 3, 4 };

        ICapturedImageFile[] pol_0_90 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c12Image_0_90 = createImage(channel1.length);
        Image<UINT16> c34Image_0_90 = createImage(channel1.length);
        ICapturedImage[] polimage_0_90 = {
                new DummyCapturedImage(pol_0_90[0], Cameras.getLabels(camera)[0], c12Image_0_90, channel1),
                new DummyCapturedImage(pol_0_90[1], Cameras.getLabels(camera)[0], c34Image_0_90, channel2) };

        ICapturedImageFile[] pol_45_135 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c12Image_45_135 = createImage(channel1.length);
        Image<UINT16> c34Image_45_135 = createImage(channel1.length);
        ICapturedImage[] polimage_45_135 = {
                new DummyCapturedImage(pol_45_135[0], Cameras.getLabels(camera)[0], c12Image_45_135, channel1),
                new DummyCapturedImage(pol_45_135[1], Cameras.getLabels(camera)[0], c34Image_45_135, channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol_0_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol_45_135);

        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], polimage_0_90);
        builder.setImages(Cameras.getLabels(camera)[1], polimage_45_135);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol0).getImage() == c12Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol45).getImage() == c12Image_45_135);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol90).getImage() == c12Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol135).getImage() == c12Image_45_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol0).getImage() == c12Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol45).getImage() == c12Image_45_135);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol90).getImage() == c12Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol135).getImage() == c12Image_45_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol0).getImage() == c34Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol45).getImage() == c34Image_45_135);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol90).getImage() == c34Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol135).getImage() == c34Image_45_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol0).getImage() == c34Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol45).getImage() == c34Image_45_135);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol90).getImage() == c34Image_0_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol135).getImage() == c34Image_45_135);
    }

    @Test
    public void getChannelPolarizationImage_FourCameraCaseNoMultiChannelImage_ReturnsFalse() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        ICapturedImageFile[] pol_0 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c1Image_0 = createImage(channel1.length);
        Image<UINT16> c2Image_0 = createImage(channel1.length);
        ICapturedImage[] polimage_0 = {
                new DummyCapturedImage(pol_0[0], Cameras.getLabels(camera)[0], c1Image_0, channel1),
                new DummyCapturedImage(pol_0[1], Cameras.getLabels(camera)[0], c2Image_0, channel2) };

        ICapturedImageFile[] pol_90 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c1Image_90 = createImage(channel1.length);
        Image<UINT16> c2Image_90 = createImage(channel1.length);
        ICapturedImage[] polimage_90 = {
                new DummyCapturedImage(pol_90[0], Cameras.getLabels(camera)[0], c1Image_90, channel1),
                new DummyCapturedImage(pol_90[1], Cameras.getLabels(camera)[0], c2Image_90, channel2) };

        ICapturedImageFile[] pol_45 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c1Image_45 = createImage(channel1.length);
        Image<UINT16> c2Image_45 = createImage(channel1.length);
        ICapturedImage[] polimage_45 = {
                new DummyCapturedImage(pol_45[0], Cameras.getLabels(camera)[0], c1Image_45, channel1),
                new DummyCapturedImage(pol_45[1], Cameras.getLabels(camera)[0], c2Image_45, channel2) };

        ICapturedImageFile[] pol_135 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c1Image_135 = createImage(channel1.length);
        Image<UINT16> c2Image_135 = createImage(channel1.length);
        ICapturedImage[] polimage_135 = {
                new DummyCapturedImage(pol_135[0], Cameras.getLabels(camera)[0], c1Image_135, channel1),
                new DummyCapturedImage(pol_135[1], Cameras.getLabels(camera)[0], c2Image_135, channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol_0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol_45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol_135);

        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], polimage_0);
        builder.setImages(Cameras.getLabels(camera)[1], polimage_45);
        builder.setImages(Cameras.getLabels(camera)[2], polimage_90);
        builder.setImages(Cameras.getLabels(camera)[3], polimage_135);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol0).getImage() == c1Image_0);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol45).getImage() == c1Image_45);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol90).getImage() == c1Image_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol135).getImage() == c1Image_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol0).getImage() == c2Image_0);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol45).getImage() == c2Image_45);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol90).getImage() == c2Image_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol135).getImage() == c2Image_135);
    }

    @Test
    public void getChannelPolarizationImage_FourCameraCaseMultiChannelImage_ReturnsFalse() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1, 2 };
        int[] channel2 = { 3, 4 };

        ICapturedImageFile[] pol_0 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c12Image_0 = createImage(channel1.length);
        Image<UINT16> c34Image_0 = createImage(channel1.length);
        ICapturedImage[] polimage_0 = {
                new DummyCapturedImage(pol_0[0], Cameras.getLabels(camera)[0], c12Image_0, channel1),
                new DummyCapturedImage(pol_0[1], Cameras.getLabels(camera)[0], c34Image_0, channel2) };

        ICapturedImageFile[] pol_90 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c12Image_90 = createImage(channel1.length);
        Image<UINT16> c34Image_90 = createImage(channel1.length);
        ICapturedImage[] polimage_90 = {
                new DummyCapturedImage(pol_90[0], Cameras.getLabels(camera)[0], c12Image_90, channel1),
                new DummyCapturedImage(pol_90[1], Cameras.getLabels(camera)[0], c34Image_90, channel2) };

        ICapturedImageFile[] pol_45 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c12Image_45 = createImage(channel1.length);
        Image<UINT16> c34Image_45 = createImage(channel1.length);
        ICapturedImage[] polimage_45 = {
                new DummyCapturedImage(pol_45[0], Cameras.getLabels(camera)[0], c12Image_45, channel1),
                new DummyCapturedImage(pol_45[1], Cameras.getLabels(camera)[0], c34Image_45, channel2) };

        ICapturedImageFile[] pol_135 = { new DummyCapturedImageFile(channel1, new File(root, "c1.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "c2.tiff")) };

        Image<UINT16> c12Image_135 = createImage(channel1.length);
        Image<UINT16> c34Image_135 = createImage(channel1.length);
        ICapturedImage[] polimage_135 = {
                new DummyCapturedImage(pol_135[0], Cameras.getLabels(camera)[0], c12Image_135, channel1),
                new DummyCapturedImage(pol_135[1], Cameras.getLabels(camera)[0], c34Image_135, channel2) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol_0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol_45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol_135);

        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        builder.setImages(Cameras.getLabels(camera)[0], polimage_0);
        builder.setImages(Cameras.getLabels(camera)[1], polimage_45);
        builder.setImages(Cameras.getLabels(camera)[2], polimage_90);
        builder.setImages(Cameras.getLabels(camera)[3], polimage_135);

        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol0).getImage() == c12Image_0);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol45).getImage() == c12Image_45);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol90).getImage() == c12Image_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(1, Polarization.pol135).getImage() == c12Image_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol0).getImage() == c12Image_0);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol45).getImage() == c12Image_45);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol90).getImage() == c12Image_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(2, Polarization.pol135).getImage() == c12Image_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol0).getImage() == c34Image_0);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol45).getImage() == c34Image_45);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol90).getImage() == c34Image_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(3, Polarization.pol135).getImage() == c34Image_135);

        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol0).getImage() == c34Image_0);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol45).getImage() == c34Image_45);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol90).getImage() == c34Image_90);
        assertTrue(capturedImageSet.getChannelPolarizationImage(4, Polarization.pol135).getImage() == c34Image_135);
    }

    private Image<UINT16> createImage(int channels) {
        long[] dim1 = { 1, 1, channels, 1, 1 };
        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        return new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
    }

}

class DummyBuilder extends ICapturedImageSetBuilder {
    private Hashtable<String, ICapturedImage[]> _images = new Hashtable<>();
    private ICapturedImageFileSet _fileSet;

    public void setFileSet(ICapturedImageFileSet fileSet) {
        this._fileSet = fileSet;
    }

    public void setImages(String label, ICapturedImage[] image) {
        _images.put(label, image);
    }

    @Override
    ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

    @Override
    ICapturedImage[] getCapturedImages(String label) {
        return _images.get(label);
    }

}

class DummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ICapturedImageFile[]> files = new Hashtable<>();
    private Cameras _cameras;

    public void setFileSet(String label, ICapturedImageFile[] file) {
        this.files.put(label, file);
    }

    public void setCameras(Cameras cameras) {
        this._cameras = cameras;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return this.files.get(label);
    }

    @Override
    public String getSetName() {
        return null;
    }

    @Override
    public Cameras getnCameras() {
        return this._cameras;
    }

    @Override
    public boolean hasLabel(String label) {
        return true;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        Stream<ICapturedImageFile> concatStream = Stream.empty();
        for (Iterator<ICapturedImageFile[]> iterator = this.files.values().iterator(); iterator.hasNext();) {
            concatStream = Stream.concat(concatStream, Arrays.stream(iterator.next()));
        }

        return concatStream.iterator();
    }

    @Override
    public int[] getChannels() {
        IntStream channels = IntStream.empty();
        for (ICapturedImageFile capturedImageFile : files.values().iterator().next()) {
            channels = IntStream.concat(channels, Arrays.stream(capturedImageFile.channels()));
        }

        return channels.toArray();
    }

}

class DummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public DummyCapturedImageFile(int[] channels, File file) {
        _channels = channels;
        _file = file;
    }

    @Override
    public int[] channels() {
        return this._channels;
    }

    @Override
    public File file() {
        return this._file;
    }

}

class DummyCapturedImage implements ICapturedImage {
    ICapturedImageFile file;
    String label;
    Image<UINT16> image;
    int[] channel;

    @Override
    public ICapturedImageFile getCapturedImageFile() {
        return file;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Image<UINT16> getImage() {
        return image;
    }

    @Override
    public int[] channels() {
        return channel;
    }

    public DummyCapturedImage(ICapturedImageFile file, String label, Image<UINT16> image, int[] channel) {
        this.file = file;
        this.label = label;
        this.image = image;
        this.channel = channel;
    }

}