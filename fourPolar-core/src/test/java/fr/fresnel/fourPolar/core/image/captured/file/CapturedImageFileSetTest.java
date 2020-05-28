package fr.fresnel.fourPolar.core.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class CapturedImageFileSetTest {
    File root = new File("/root");

    /**
     * This test should not confuse us about the fact only pol0 file is used for
     * creating the set name!
     */
    @Test
    public void getSetName_SameFileSet_ReturnsEqualSetName() {
        int[] channel = { 1 };

        CapturedImageFile[] pol0 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135 = { new CapturedImageFile(channel, new File(root, "pol135.tiff")) };

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        CapturedImageFile[] pol0_1 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45_1 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90_1 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135_1 = { new CapturedImageFile(channel, new File(root, "pol135.tiff")) };

        CapturedImageFileSet fileSet_1 = new CapturedImageFileSet(pol0_1, pol45_1, pol90_1, pol135_1);

        assertTrue(fileSet.getSetName().equals(fileSet_1.getSetName()));
    }

    @Test
    public void equals_CheckEqualSets_ReturnsEqual() {
        int[] channel = { 1 };

        CapturedImageFile[] pol0 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135 = { new CapturedImageFile(channel, new File(root, "pol135.tiff")) };

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        CapturedImageFile[] pol0_1 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45_1 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90_1 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135_1 = { new CapturedImageFile(channel, new File(root, "pol135.tiff")) };

        CapturedImageFileSet fileSet_1 = new CapturedImageFileSet(pol0_1, pol45_1, pol90_1, pol135_1);

        assertTrue(fileSet.equals(fileSet_1));

    }

    @Test
    public void deepEquals_CheckUnEqualSets_ReturnsUnEqual() {
        int[] channel = { 1 };

        CapturedImageFile[] pol0 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135 = { new CapturedImageFile(channel, new File(root, "pol135.tiff")) };

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        CapturedImageFile[] pol0_1 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45_1 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90_1 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135_1 = { new CapturedImageFile(channel, new File(root, "pol135_1.tiff")) };

        CapturedImageFileSet fileSet_1 = new CapturedImageFileSet(pol0_1, pol45_1, pol90_1, pol135_1);

        assertTrue(!fileSet.deepEquals(fileSet_1));

    }

    @Test
    public void deepEquals_CheckEqualSets_ReturnsEqual() {
        int[] channel = { 1 };

        CapturedImageFile[] pol0 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135 = { new CapturedImageFile(channel, new File(root, "pol135.tiff")) };

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        CapturedImageFile[] pol0_1 = { new CapturedImageFile(channel, new File(root, "pol0.tiff")) };
        CapturedImageFile[] pol45_1 = { new CapturedImageFile(channel, new File(root, "pol45.tiff")) };
        CapturedImageFile[] pol90_1 = { new CapturedImageFile(channel, new File(root, "pol90.tiff")) };
        CapturedImageFile[] pol135_1 = { new CapturedImageFile(channel, new File(root, "pol135.tiff")) };

        CapturedImageFileSet fileSet_1 = new CapturedImageFileSet(pol0_1, pol45_1, pol90_1, pol135_1);

        assertTrue(!fileSet.deepEquals(fileSet_1));

    }

    @Test
    public void getIterator_AFourCameraCase_ReturnsAllFiles() {
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        CapturedImageFile[] pol0 = { new CapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new CapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        CapturedImageFile[] pol45 = { new CapturedImageFile(channel1, new File(root, "pol45.tiff")),
                new CapturedImageFile(channel2, new File(root, "pol451.tiff")) };
        CapturedImageFile[] pol90 = { new CapturedImageFile(channel1, new File(root, "pol90.tiff")),
                new CapturedImageFile(channel2, new File(root, "pol901.tiff")) };
        CapturedImageFile[] pol135 = { new CapturedImageFile(channel1, new File(root, "pol135.tiff")),
                new CapturedImageFile(channel2, new File(root, "pol1351.tiff")) };

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        int counter = 0;
        for (Iterator<ICapturedImageFile> itr = fileSet.getIterator(); itr.hasNext();) {
            itr.next();
            counter++;
        }

        assertTrue(counter == 8);
    }

}
