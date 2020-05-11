package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;

public class SampleImageSetByNamePatternFinderTest {
    private static File root;

    @BeforeAll
    private static void setRoot() {
        root = new File(
                SampleImageSetByNamePatternFinderTest.class.getResource("SampleImageSetByNamePatternFinder").getPath());
    }

    @Test
    public void find_OneCameraTwoChannel_ReturnsThreeCapturedSetsForEachChannel() throws IncompatibleCapturedImage {
        File rootOneCamera = new File(root, "OneCamera");

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootOneCamera);

        Iterator<File[]> channel1 = finder.find("C1");
        Iterator<File[]> channel2 = finder.find("C2");

        File[] channel1Files = new File[] { new File(rootOneCamera, "Img1_C1.tif"),
                new File(rootOneCamera, "Img2_C1.tif"), new File(rootOneCamera, "Img3_C1.tif"),
                new File(rootOneCamera, "Img4_C1.tif") };

        File[] channel2Files = new File[] { new File(rootOneCamera, "Img1_C2.tif"),
                new File(rootOneCamera, "Img2_C2.tif"), new File(rootOneCamera, "Img3_C2.tif"),
                new File(rootOneCamera, "Img4_C2.tif") };

        assertTrue(isInArray(channel1, channel1Files) && isInArray(channel2, channel2Files));

    }

    @Test
    public void find_OneCameraFullChannel_ReturnsThreeCapturedSetsForEachChannel() throws IncompatibleCapturedImage {
        File rootOneCamera = new File(root, "OneCameraFullChannel");

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootOneCamera);

        Iterator<File[]> fullChannel = finder.find();

        File[] files = new File[] { new File(rootOneCamera, "Img1.tif"), new File(rootOneCamera, "Img2.tif"),
                new File(rootOneCamera, "Img3.tif"), new File(rootOneCamera, "Img4.tif") };

        assertTrue(isInArray(fullChannel, files));

    }

    @Test
    public void find_TwoCameraSingleChannel_ReturnsThreeCapturedSets()
            throws KeyAlreadyExistsException, IllegalArgumentException, IncompatibleCapturedImage {
        File rootTwoCamera = new File(root, "TwoCamera");

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootTwoCamera, "Pol0_90",
                "Pol45_135");

        Iterator<File[]> channel1 = finder.find("C1");

        File[] files = new File[] { new File(rootTwoCamera, "Img1_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img1_C1_Pol45_135.tif"), new File(rootTwoCamera, "Img2_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img2_C1_Pol45_135.tif"), new File(rootTwoCamera, "Img3_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img3_C1_Pol45_135.tif") };

        assertTrue(isInArray(channel1, files));
    }

    @Test
    public void findChannelImages_FourCamera_ReturnsThreeCapturedSets()
            throws KeyAlreadyExistsException, IllegalArgumentException, IncompatibleCapturedImage {
        File rootFourCamera = new File(root, "FourCamera");
        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootFourCamera, "Pol0",
                "Pol45", "Pol90", "Pol135");

        Iterator<File[]> channel1 = finder.find("C1");

        File[] files = new File[] { new File(rootFourCamera, "Img1_C1_Pol0.tif"),
                new File(rootFourCamera, "Img1_C1_Pol45.tif"), new File(rootFourCamera, "Img1_C1_Pol90.tif"),
                new File(rootFourCamera, "Img1_C1_Pol135.tif"), new File(rootFourCamera, "Img2_C1_Pol0.tif"),
                new File(rootFourCamera, "Img2_C1_Pol45.tif"), new File(rootFourCamera, "Img2_C1_Pol90.tif"),
                new File(rootFourCamera, "Img2_C1_Pol135.tif"), new File(rootFourCamera, "Img3_C1_Pol0.tif"),
                new File(rootFourCamera, "Img3_C1_Pol45.tif"), new File(rootFourCamera, "Img3_C1_Pol90.tif"),
                new File(rootFourCamera, "Img3_C1_Pol135.tif"), };

        assertTrue(isInArray(channel1, files));

    }

    @Test
    public void find_FullChannelIncompleteFileSet_RejectsWithNoCorrespondence() {
        File rootFourCamera = new File(root, "IncompleteFourCamera");

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootFourCamera, "Pol0",
                "Pol45", "Pol90", "Pol135");

        Iterator<File[]> fullChannel = finder.find();
        assertTrue(!fullChannel.hasNext());
    }

    /**
     * check all inside files[].
     */
    private boolean isInArray(Iterator<File[]> itr, File testFiles[]) {
        int counter = 0;
        while (itr.hasNext()) {
            File[] files = itr.next();
            for (File file : testFiles) {
                if (Arrays.stream(files).anyMatch((t) -> t.equals(file))) {
                    counter++;
                }
            }

        }
        return counter == testFiles.length;
    }

}