package fr.fresnel.fourPolar.io.image;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.io.image.tiff.TiffImageChecker;

public class TiffImageCheckerTest {
    private static File _root;
    private static TiffImageChecker tiffChecker = new TiffImageChecker();

    @BeforeAll
    static void setParams() {
        _root = new File(TiffImageCheckerTest.class.getResource("").getPath(), "TiffImageChecker");

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

        assertTrue(TiffImageChecker.not16bit.equals(exception.getMessage()));

    }

    @Test
    public void checkCompatible_corruptTiff_ThrowsCorruptCapturedImage() throws CorruptCapturedImage {
        File image = new File(_root, "corrupt.tif");
        CorruptCapturedImage exception = assertThrows(CorruptCapturedImage.class, () -> {
            tiffChecker.checkCompatible(image);
        });

        assertTrue(TiffImageChecker.corruptContent.equals(exception.getMessage()));
    }

    @Test
    public void checkCompatible_otherFormat_ThrowsCorruptCapturedImage() throws CorruptCapturedImage, IOException {
        File jpegImage = new File(_root, "otherFormat.jpeg");

        CorruptCapturedImage exception = assertThrows(CorruptCapturedImage.class, () -> {
            tiffChecker.checkCompatible(jpegImage);
        });

        assertTrue(TiffImageChecker.badExtension.equals(exception.getMessage()));
    }

    @Test
    public void checkCompatible_nonExistent_ThrowsCorruptCapturedImage() throws CorruptCapturedImage, IOException {
        File nonExistent = new File(_root, "ThisFileDoesNotExist.jpeg");

        CorruptCapturedImage exception = assertThrows(CorruptCapturedImage.class, () -> {
            tiffChecker.checkCompatible(nonExistent);
        });

        assertTrue(TiffImageChecker.notExist.equals(exception.getMessage()));
    }

}