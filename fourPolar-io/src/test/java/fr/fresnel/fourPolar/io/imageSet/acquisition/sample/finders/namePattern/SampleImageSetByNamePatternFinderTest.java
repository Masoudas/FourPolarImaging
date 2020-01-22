package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;
import fr.fresnel.fourPolar.io.image.tiff.TiffImageChecker;

public class SampleImageSetByNamePatternFinderTest {
        private static File root;

        @BeforeAll
        private static void setRoot() {
                root = new File(SampleImageSetByNamePatternFinderTest.class
                                .getResource("SampleImageSetByNamePatternFinder").getPath());
        }

        @Test
        public void findChannelImages_OneCamera_ReturnsThreeCapturedSetsForEachChannel() throws NoImageFoundOnRoot {
                File rootOneCamera = new File(root, "OneCamera");
                FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.One);
                SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

                SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootOneCamera,
                                new TiffImageChecker());

                List<RejectedCapturedImage> rejectedChan1 = finder.findChannelImages(sampleImageSet, 1, "C1");
                List<RejectedCapturedImage> rejectedChan2 = finder.findChannelImages(sampleImageSet, 2, "C2");

                // Generate sets to see if found
                ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C1.tif"));
                ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C1.tif"));

                ICapturedImageFileSet Img1_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C2.tif"));
                ICapturedImageFileSet Img2_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C2.tif"));

                SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup);
                actualSampleImageSet.addImage(1, Img1_C1);
                actualSampleImageSet.addImage(1, Img2_C1);
                actualSampleImageSet.addImage(2, Img1_C2);
                actualSampleImageSet.addImage(2, Img2_C2);

                assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1))
                        && actualSampleImageSet.getChannelImages(2).equals(sampleImageSet.getChannelImages(2))
                        && rejectedChan1.size() == 2 && rejectedChan2.size() == 2);

        }

        @Test
        public void findChannelImages_TwoCamera_ReturnsThreeCapturedSets() throws NoImageFoundOnRoot {
                File rootTwoCamera = new File(root, "TwoCamera");

                FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Two);
                SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

                SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootTwoCamera,
                                new TiffImageChecker(), "Pol0_90", "Pol45_135");

                List<RejectedCapturedImage> rejectedChan1 = finder.findChannelImages(sampleImageSet, 1, null);

                // Generate sets to see if found
                ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img1_C1_Pol0_90.tif"),
                                new File(rootTwoCamera, "Img1_C1_Pol45_135.tif"));
                ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img2_C1_Pol0_90.tif"),
                                new File(rootTwoCamera, "Img2_C1_Pol45_135.tif"));
                ICapturedImageFileSet Img3_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img3_C1_Pol0_90.tif"),
                                new File(rootTwoCamera, "Img3_C1_Pol45_135.tif"));

                SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup);
                actualSampleImageSet.addImage(1, Img1_C1);
                actualSampleImageSet.addImage(1, Img2_C1);
                actualSampleImageSet.addImage(1, Img3_C1);
                assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1))
                        && rejectedChan1.size() == 3);
                }

        @Test
        public void findChannelImages_FourCamera_ReturnsThreeCapturedSets() throws NoImageFoundOnRoot {
                File rootFourCamera = new File(root, "FourCamera");

                FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Four);
                SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

                SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootFourCamera,
                                new TiffImageChecker(), "Pol0", "Pol45", "Pol90", "Pol135");

                List<RejectedCapturedImage> rejectedChan1 = finder.findChannelImages(sampleImageSet, 1, null);

                // Generate sets to see if found
                ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img1_C1_Pol0.tif"),
                                new File(rootFourCamera, "Img1_C1_Pol45.tif"),
                                new File(rootFourCamera, "Img1_C1_Pol90.tif"),
                                new File(rootFourCamera, "Img1_C1_Pol135.tif"));
                ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img2_C1_Pol0.tif"),
                                new File(rootFourCamera, "Img2_C1_Pol45.tif"),
                                new File(rootFourCamera, "Img2_C1_Pol90.tif"),
                                new File(rootFourCamera, "Img2_C1_Pol135.tif"));
                ICapturedImageFileSet Img3_C1 = new CapturedImageFileSet(new File(rootFourCamera, "Img3_C1_Pol0.tif"),
                                new File(rootFourCamera, "Img3_C1_Pol45.tif"),
                                new File(rootFourCamera, "Img3_C1_Pol90.tif"),
                                new File(rootFourCamera, "Img3_C1_Pol135.tif"));

                SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup);
                actualSampleImageSet.addImage(1, Img1_C1);
                actualSampleImageSet.addImage(1, Img2_C1);
                actualSampleImageSet.addImage(1, Img3_C1);
                assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1))
                        && rejectedChan1.size() == 3);
                }

        @Test
        public void findChannelImages_IncompleteFileSet_RejectsWithNoCorrespondence() {
                File rootFourCamera = new File(root, "IncompleteFourCamera");

                FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Four);
                SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

                SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootFourCamera,
                                new TiffImageChecker(), "Pol0", "Pol45", "Pol90", "Pol135");

                try {
                        List<RejectedCapturedImage> rejectedChan1 = finder.findChannelImages(sampleImageSet, 1, null);        
                        System.out.println(rejectedChan1.get(0).getReason());
                } catch (NoImageFoundOnRoot e) {
                }
                

                

        }

}