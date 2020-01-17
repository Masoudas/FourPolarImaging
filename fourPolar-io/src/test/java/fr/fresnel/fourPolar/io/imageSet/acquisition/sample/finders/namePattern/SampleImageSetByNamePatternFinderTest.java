package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import static org.junit.Assert.assertTrue;

import java.io.File;
import org.junit.Test;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;
import fr.fresnel.fourPolar.io.image.tiff.TiffImageChecker;

public class SampleImageSetByNamePatternFinderTest {
        @Test
        public void findChannelImages_OneCamera_ReturnsThreeCapturedSetsForEachChannel() throws NoImageFoundOnRoot {
        File rootOneCamera = new File(SampleImageSetByNamePatternFinderTest.class.getResource("OneCamera").getPath());
        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.One);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootOneCamera, new TiffImageChecker());

        finder.findChannelImages(sampleImageSet, 1, "C1"); 
        finder.findChannelImages(sampleImageSet, 2, "C2");

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C1.tif"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C1.tif"));

        ICapturedImageFileSet Img1_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C2.tif"));
        ICapturedImageFileSet Img2_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C2.tif"));

        boolean contains_Img1_C1 = sampleImageSet.getChannelImages(1).contains(Img1_C1);
        boolean contains_Img2_C1 = sampleImageSet.getChannelImages(1).contains(Img2_C1);

        boolean contains_Img1_C2 = sampleImageSet.getChannelImages(2).contains(Img1_C2);
        boolean contains_Img2_C2 = sampleImageSet.getChannelImages(2).contains(Img2_C2);

        assertTrue(contains_Img1_C1 && contains_Img1_C2 && contains_Img2_C1 && contains_Img2_C2);
    }

    @Test
    public void findChannelImages_TwoCamera_ReturnsThreeCapturedSets() throws NoImageFoundOnRoot {
        File rootTwoCamera = new File(SampleImageSetByNamePatternFinderTest.class.getResource("TwoCamera").getPath());

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Two);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootTwoCamera,
                new TiffImageChecker(), "Pol0_90", "Pol45_135");

        finder.findChannelImages(sampleImageSet, 1, null);

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img1_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img1_C1_Pol45_135.tif"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img2_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img2_C1_Pol45_135.tif"));
        ICapturedImageFileSet Img3_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img3_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img3_C1_Pol45_135.tif"));

        boolean contains_Img1_C1 = sampleImageSet.getChannelImages(1).contains(Img1_C1);
        boolean contains_Img2_C1 = sampleImageSet.getChannelImages(1).contains(Img2_C1);
        boolean contains_Img3_C1 = sampleImageSet.getChannelImages(1).contains(Img3_C1);

        assertTrue(contains_Img1_C1 && contains_Img2_C1 && contains_Img3_C1);
    }

    @Test
    public void findChannelImages_FourCamera_ReturnsThreeCapturedSets() throws NoImageFoundOnRoot {
        File rootFourCamera = new File(SampleImageSetByNamePatternFinderTest.class.getResource("FourCamera").getPath());

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Four);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootFourCamera,
                new TiffImageChecker(), "Pol0", "Pol45", "Pol90", "Pol135");

        finder.findChannelImages(sampleImageSet, 1, null);

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img1_C1_Pol0.tif"),
                new File(rootFourCamera, "Img1_C1_Pol45.tif"), new File(rootFourCamera, "Img1_C1_Pol90.tif"),
                new File(rootFourCamera, "Img1_C1_Pol135.tif"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img2_C1_Pol0.tif"),
                new File(rootFourCamera, "Img2_C1_Pol45.tif"), new File(rootFourCamera, "Img2_C1_Pol90.tif"),
                new File(rootFourCamera, "Img2_C1_Pol135.tif"));
        ICapturedImageFileSet Img3_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img3_C1_Pol0.tif"),
                new File(rootFourCamera, "Img3_C1_Pol45.tif"), new File(rootFourCamera, "Img3_C1_Pol90.tif"),
                new File(rootFourCamera, "Img3_C1_Pol135.tif"));

        boolean contains_Img1_C1 = sampleImageSet.getChannelImages(1).contains(Img1_C1);
        boolean contains_Img2_C1 = sampleImageSet.getChannelImages(1).contains(Img2_C1);
        boolean contains_Img3_C1 = sampleImageSet.getChannelImages(1).contains(Img3_C1);

        assertTrue(contains_Img1_C1 && contains_Img2_C1 && contains_Img3_C1);
    }

}