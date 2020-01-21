package fr.fresnel.fourPolar.io.image;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.io.image.tiff.TiffImageChecker;

public class TiffImageCheckerTest {
    private File _root;
    private TiffImageChecker tiffChecker = new TiffImageChecker();

    @BeforeAll
    public void setParams() {
        this._root = new File(TiffImageCheckerTest.class.getResource("").toString(), "TiffImageChecker");

    }

    @Test
    public void checkCompatible_16bitTiff_ReturnsTrue() throws CorruptCapturedImage, IOException {
        File tiffImage = new File(this._root, "16bit.tif");

        assertTrue(tiffChecker.checkCompatible(tiffImage));

        
    }

    
}