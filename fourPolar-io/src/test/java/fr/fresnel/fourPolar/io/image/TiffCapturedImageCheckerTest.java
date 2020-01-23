package fr.fresnel.fourPolar.io.image;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.io.image.tiff.TiffCapturedImageChecker;

public class TiffCapturedImageCheckerTest {
    private static File _root;
    private static TiffCapturedImageChecker tiffChecker = new TiffCapturedImageChecker();

    @BeforeAll
    static void setParams() {
        _root = new File(TiffCapturedImageCheckerTest.class.getResource("").getPath(), "TiffCapturedImageChecker");

    }

    @Test
    public void checkCompatible_16bitTiff_ThrowsNoException() throws CorruptCapturedImage {
        File tiffImage = new File(_root, "16bit.tif");
        assertDoesNotThrow(() -> {
            tiffChecker.checkCompatible(tiffImage);
        }, "No exceptions thrown.");
    }

    @Test
    public void checkCompatible_8bitTiff_ThrowsCorruptCapturedImage() throws CorruptCapturedImage {
        File tiffImage = new File(_root, "8bit.tif");

        CorruptCapturedImage exception = assertThrows(CorruptCapturedImage.class, () -> {
            tiffChecker.checkCompatible(tiffImage);
        });

        assertTrue(TiffCapturedImageChecker.lowBitDepth.equals(exception.getRejectedImage().getReason()));

    }

    @Test
    public void checkCompatible_corruptTiff_ThrowsCorruptCapturedImage() throws CorruptCapturedImage {
        File image = new File(_root, "corrupt.tif");
        CorruptCapturedImage exception = assertThrows(CorruptCapturedImage.class, () -> {
            tiffChecker.checkCompatible(image);
        });

        assertTrue(TiffCapturedImageChecker.corruptContent.equals(exception.getRejectedImage().getReason()));
    }

    @Test
    public void checkCompatible_otherFormat_ThrowsCorruptCapturedImage() throws CorruptCapturedImage, IOException {
        File jpegImage = new File(_root, "otherFormat.jpeg");

        CorruptCapturedImage exception = assertThrows(CorruptCapturedImage.class, () -> {
            tiffChecker.checkCompatible(jpegImage);
        });

        assertTrue(TiffCapturedImageChecker.badExtension.equals(exception.getRejectedImage().getReason()));
    }

    @Test
    public void checkCompatible_nonExistent_ThrowsCorruptCapturedImage() throws CorruptCapturedImage, IOException {
        File nonExistent = new File(_root, "ThisFileDoesNotExist.jpeg");

        CorruptCapturedImage exception = assertThrows(CorruptCapturedImage.class, () -> {
            tiffChecker.checkCompatible(nonExistent);
        });

        assertTrue(TiffCapturedImageChecker.notExist.equals(exception.getRejectedImage().getReason()));
    }

}