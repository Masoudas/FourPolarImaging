package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import static org.junit.Assert.assertTrue;

import java.io.File;
import org.junit.Test;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class SampleImageSetByNamePatternFinderTest {
    File root = new File(
            "/home/masoud/Documents/four-polar/fourPolar-core/src/test/java/fr/fresnel/fourPolar/core/imageSet/acquisition/sample/finders/TestFiles/");

    @Test
    public void findChannelImages_OneCamera_ReturnsThreeCapturedSetsForEachChannel() {
        File rootOneCamera = new File(root, "OneCamera");
        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.One);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(sampleImageSet, rootOneCamera);

        finder.findChannelTiffImages(1, "C1");
        finder.findChannelTiffImages(2, "C2");

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C1.tiff"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C1.tiff"));

        ICapturedImageFileSet Img1_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C2.tiff"));
        ICapturedImageFileSet Img2_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C2.tiff"));

        boolean contains_Img1_C1 = sampleImageSet.getChannelImages(1).contains(Img1_C1);
        boolean contains_Img2_C1 = sampleImageSet.getChannelImages(1).contains(Img2_C1);

        boolean contains_Img1_C2 = sampleImageSet.getChannelImages(2).contains(Img1_C2);
        boolean contains_Img2_C2 = sampleImageSet.getChannelImages(2).contains(Img2_C2);

        assertTrue(contains_Img1_C1 && contains_Img1_C2 && contains_Img2_C1 && contains_Img2_C2);
    }

    @Test
    public void findChannelImages_TwoCamera_ReturnsThreeCapturedSets() {
        File rootTwoCamera = new File(root, "TwoCamera");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Two);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(sampleImageSet, rootTwoCamera,
                "Pol0_90", "Pol45_135");

        finder.findChannelTiffImages(1, null);

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img1_C1_Pol0_90.tiff"),
                new File(rootTwoCamera, "Img1_C1_Pol45_135.tiff"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img2_C1_Pol0_90.tiff"),
                new File(rootTwoCamera, "Img2_C1_Pol45_135.tiff"));
        ICapturedImageFileSet Img3_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img3_C1_Pol0_90.tiff"),
                new File(rootTwoCamera, "Img3_C1_Pol45_135.tiff"));

        boolean contains_Img1_C1 = sampleImageSet.getChannelImages(1).contains(Img1_C1);
        boolean contains_Img2_C1 = sampleImageSet.getChannelImages(1).contains(Img2_C1);
        boolean contains_Img3_C1 = sampleImageSet.getChannelImages(1).contains(Img3_C1);

        assertTrue(contains_Img1_C1 && contains_Img2_C1 && contains_Img3_C1);
    }

    @Test
    public void findChannelImages_FourCamera_ReturnsThreeCapturedSets() {
        File rootFourCamera = new File(root, "FourCamera");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Four);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(sampleImageSet, rootFourCamera,
                "Pol0", "Pol45", "Pol90", "Pol135");

        finder.findChannelTiffImages(1, null);

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img1_C1_Pol0.tiff"),
                new File(rootFourCamera, "Img1_C1_Pol45.tiff"), new File(rootFourCamera, "Img1_C1_Pol90.tiff"),
                new File(rootFourCamera, "Img1_C1_Pol135.tiff"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img2_C1_Pol0.tiff"),
                new File(rootFourCamera, "Img2_C1_Pol45.tiff"), new File(rootFourCamera, "Img2_C1_Pol90.tiff"),
                new File(rootFourCamera, "Img2_C1_Pol135.tiff"));
        ICapturedImageFileSet Img3_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img3_C1_Pol0.tiff"),
                new File(rootFourCamera, "Img3_C1_Pol45.tiff"), new File(rootFourCamera, "Img3_C1_Pol90.tiff"),
                new File(rootFourCamera, "Img3_C1_Pol135.tiff"));

        boolean contains_Img1_C1 = sampleImageSet.getChannelImages(1).contains(Img1_C1);
        boolean contains_Img2_C1 = sampleImageSet.getChannelImages(1).contains(Img2_C1);
        boolean contains_Img3_C1 = sampleImageSet.getChannelImages(1).contains(Img3_C1);

        assertTrue(contains_Img1_C1 && contains_Img2_C1 && contains_Img3_C1);
    }

}