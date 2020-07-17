package fr.fresnel.fourPolar.io.image.captured.tiff.checker;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.io.image.generic.metadata.scifio.SCIFIOMetadataReader;

public class TiffCapturedImageCheckerTest {
    private static File _root;
    private static TiffCapturedImageChecker tiffChecker = new TiffCapturedImageChecker(new SCIFIOMetadataReader());

    @BeforeAll
    static void setParams() {
        _root = new File(TiffCapturedImageCheckerTest.class.getResource("").getPath());
    }

    @Test
    public void checkCompatible_16bitXYTiff_DoesNotThrowException() throws IncompatibleCapturedImage {
        final File tiffImage = new File(_root, "16bit.tif");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1 }, tiffImage);
        assertDoesNotThrow(() -> {
            tiffChecker.check(file);
        });

    }

    @Test
    public void checkCompatible_XYTCZWith2Channels_DoesNotThrowException()
            throws IncompatibleCapturedImage, IOException {
        final File nonExistent = new File(_root, "XYTCZ.tif");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1, 2 }, nonExistent);

        assertDoesNotThrow(() -> {
            tiffChecker.check(file);
        });
    }


    @Test
    public void checkCompatible_16bitXYTiffAssume2Channel_ThrowsIncompatibleCapturedImage()
            throws IncompatibleCapturedImage {
        final File tiffImage = new File(_root, "16bit.tif");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1, 2 }, tiffImage);
        IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(file);
        });

        assertTrue(exception.getRejectedImage().getReason().equals(TiffCapturedImageChecker.WRONG_NUM_CHANNEL));
    }

    @Test
    public void checkCompatible_8bitTiff_ThrowsIncompatibleCapturedImage() throws IncompatibleCapturedImage {
        final File tiffImage = new File(_root, "8bit.tif");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1 }, tiffImage);

        final IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(file);
        });

        assertTrue(TiffCapturedImageChecker.NOT_16_BIT.equals(exception.getRejectedImage().getReason()));

    }

    @Test
    public void checkCompatible_corruptTiff_ThrowsIncompatibleCapturedImage() throws IncompatibleCapturedImage {
        final File image = new File(_root, "corrupt.tif");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1 }, image);

        final IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(file);
        });

        assertTrue(TiffCapturedImageChecker.CONTENT_CORRUPT.equals(exception.getRejectedImage().getReason()));
    }

    @Test
    public void checkCompatible_otherFormat_ThrowsIncompatibleCapturedImage()
            throws IncompatibleCapturedImage, IOException {
        final File jpegImage = new File(_root, "otherFormat.jpeg");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1 }, jpegImage);

        final IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(file);
        });

        assertTrue(TiffCapturedImageChecker.NOT_TIFF.equals(exception.getRejectedImage().getReason()));
    }

    @Test
    public void checkCompatible_nonExistent_ThrowsIncompatibleCapturedImage()
            throws IncompatibleCapturedImage, IOException {
        final File nonExistent = new File(_root, "ThisFileDoesNotExist.tif");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1 }, nonExistent);

        final IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(file);
        });

        assertTrue(TiffCapturedImageChecker.NOT_EXIST.equals(exception.getRejectedImage().getReason()));
    }

    @Test
    public void checkCompatible_AxisUndefined_ThrowsIncompatibleCapturedImage()
            throws IncompatibleCapturedImage, IOException {
        final File unknownAxis = new File(_root, "UnknownAxis.tif");
        DummyCapturedImageFile file = new DummyCapturedImageFile(new int[] { 1 }, unknownAxis);

        final IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(file);
        });

        assertTrue(TiffCapturedImageChecker.UNDEFINED_AXIS.equals(exception.getRejectedImage().getReason()));
    }
}

class DummyCapturedImageFile implements ICapturedImageFile {
    int[] channels;
    File file;

    @Override
    public int[] channels() {
        return channels;
    }

    @Override
    public File file() {
        return file;
    }

    public DummyCapturedImageFile(int[] channels, File file) {
        this.channels = channels;
        this.file = file;
    }

}