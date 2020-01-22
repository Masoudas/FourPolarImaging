package fr.fresnel.fourPolar.io.image;

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
    public void checkCompatible_16bitTiff_ReturnsTrue() throws CorruptCapturedImage, IOException {
        File tiffImage = new File(_root, "16bit.tif");

        assertTrue(tiffChecker.checkCompatible(tiffImage));        
    }

    @Test
    public void checkCompatible_8bitTiff_ReturnsFalse() throws CorruptCapturedImage, IOException {
        File tiffImage = new File(_root, "8bit.tif");

        assertTrue(!tiffChecker.checkCompatible(tiffImage));
    }

    @Test
    public void checkCompatible_corruptTiff_ThrowsCorruptCapturedImage() throws CorruptCapturedImage, IOException {
        File image = new File(_root, "corrupt.tif");
        assertThrows(CorruptCapturedImage.class, () -> {tiffChecker.checkCompatible(image);});
    }

    @Test
    public void checkCompatible_otherFormat_ReturnsFalse() throws CorruptCapturedImage, IOException {
        File tiffImage = new File(_root, "otherFormat.jpeg");

        assertTrue(!tiffChecker.checkCompatible(tiffImage));
    }

    
}