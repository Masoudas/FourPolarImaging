package fr.fresnel.fourPolar.core.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;

/**
 * Unit test for simple App.
 */
public class CapturedImageFileSetTest {
    File root = new File("/root");

    /**
     * This test should not confuse us about the fact only pol0 file is used
     * for creating the set name!
     */
    @Test
    public void getSetName_SameFileSet_ReturnsEqualSetName() {
        File[] pol0 = { new File(root, "pol0.tiff") };
        File[] pol45 = { new File(root, "pol45.tiff") };
        File[] pol90 = { new File(root, "pol90.tiff") };
        File[] pol135 = { new File(root, "pol135.tiff") };

        CapturedImageFileSet fileSet = new CapturedImageFileSet(null, pol0, pol45, pol90, pol135);

        File[] pol0_1 = { new File(root, "pol0.tiff") };
        File[] pol45_1 = { new File(root, "pol45.tiff") };
        File[] pol90_1 = { new File(root, "pol90.tiff") };
        File[] pol135_1 = { new File(root, "pol135.tiff") };

        CapturedImageFileSet fileSet_1 = new CapturedImageFileSet(null, pol0_1, pol45_1, pol90_1, pol135_1);

        assertTrue(fileSet.getSetName().equals(fileSet_1.getSetName()));
    }


}
