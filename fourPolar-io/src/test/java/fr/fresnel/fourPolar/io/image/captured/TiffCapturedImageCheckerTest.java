package fr.fresnel.fourPolar.io.image.captured;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageChecker;

public class TiffCapturedImageCheckerTest {
    private static File _root;
    private static TiffCapturedImageChecker tiffChecker = new TiffCapturedImageChecker();

    @BeforeAll
    static void setParams() {
        _root = new File(TiffCapturedImageCheckerTest.class.getResource("").getPath(), "TiffCapturedImageChecker");
    }

    @Test
    public void checkCompatible_16bitTiff_ThrowsNoException() throws IncompatibleCapturedImage {
        File tiffImage = new File(_root, "16bit.tif");
        assertDoesNotThrow(() -> {
            tiffChecker.check(tiffImage);
        });
    }

    @Test
    public void checkCompatible_8bitTiff_ThrowsIncompatibleCapturedImage() throws IncompatibleCapturedImage {
        File tiffImage = new File(_root, "8bit.tif");

        IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(tiffImage);
        });

        assertTrue(TiffCapturedImageChecker.lowBitDepth.equals(exception.getRejectedImage().getReason()));

    }

    @Test
    public void checkCompatible_corruptTiff_ThrowsIncompatibleCapturedImage() throws IncompatibleCapturedImage {
        File image = new File(_root, "corrupt.tif");
        IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(image);
        });

        assertTrue(TiffCapturedImageChecker.corruptContent.equals(exception.getRejectedImage().getReason()));
    }

    @Test
    public void checkCompatible_otherFormat_ThrowsIncompatibleCapturedImage() throws IncompatibleCapturedImage, IOException {
        File jpegImage = new File(_root, "otherFormat.jpeg");

        IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(jpegImage);
        });

        assertTrue(TiffCapturedImageChecker.badExtension.equals(exception.getRejectedImage().getReason()));
    }

    @Test
    public void checkCompatible_nonExistent_ThrowsIncompatibleCapturedImage() throws IncompatibleCapturedImage, IOException {
        File nonExistent = new File(_root, "ThisFileDoesNotExist.jpeg");

        IncompatibleCapturedImage exception = assertThrows(IncompatibleCapturedImage.class, () -> {
            tiffChecker.check(nonExistent);
        });

        assertTrue(TiffCapturedImageChecker.notExist.equals(exception.getRejectedImage().getReason()));
    }

}