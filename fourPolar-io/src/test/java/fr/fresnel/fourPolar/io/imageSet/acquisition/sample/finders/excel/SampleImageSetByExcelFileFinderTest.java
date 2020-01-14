package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.image.tiff.TiffImageChecker;

public class SampleImageSetByExcelFileFinderTest {
    @Test
    public void findChannelImages_OneCamera_ReturnsThreeCapturedSetsForEachChannel() throws IOException {
        File rootOneCamera = new File(SampleImageSetByExcelFileFinderTest.class.getResource("OneCamera").getPath());
        File oneCameraChannel1Excel = new File(rootOneCamera, "TemplateOneCamera-Channel1.xlsx");
        File oneCameraChannel2Excel = new File(rootOneCamera, "TemplateOneCamera-Channel2.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.One);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(new TiffImageChecker());

        finder.findChannelImages(sampleImageSet, 1, oneCameraChannel1Excel);
        finder.findChannelImages(sampleImageSet, 2, oneCameraChannel2Excel);

        System.out.println(sampleImageSet.getChannelImages(1).size());

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C1.tif"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C1.tif"));
        System.out.println(new File(rootOneCamera, "Img2_C1.tif").exists());

        ICapturedImageFileSet Img1_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C2.tif"));
        ICapturedImageFileSet Img2_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C2.tif"));

        SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup);
        actualSampleImageSet.addImage(1, Img1_C1);
        actualSampleImageSet.addImage(1, Img2_C1);
        actualSampleImageSet.addImage(2, Img1_C2);
        actualSampleImageSet.addImage(2, Img2_C2);

        assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1)) &&
                actualSampleImageSet.getChannelImages(2).equals(sampleImageSet.getChannelImages(2)));
    }

    @Test
    public void findChannelImages_TwoCamera_ReturnsThreeCapturedSets() throws IOException {
        File rootTwoCamera = new File(SampleImageSetByExcelFileFinderTest.class.getResource("TwoCamera").getPath());

        File twoCameraExcel = new File(rootTwoCamera, "TemplateTwoCamera.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Two);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(new TiffImageChecker());

        finder.findChannelImages(sampleImageSet, 1, twoCameraExcel);

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
        assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1)));
    }

    @Test
    public void findChannelImages_FourCamera_ReturnsThreeCapturedSets() throws IOException {
        File rootFourCamera = new File(SampleImageSetByExcelFileFinderTest.class.getResource("FourCamera").getPath());
        File fourCameraExcel = new File(
            rootFourCamera, "TemplateFourCamera.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Four);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(new TiffImageChecker());
        finder.findChannelImages(sampleImageSet, 1, fourCameraExcel);

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

        SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup);
        actualSampleImageSet.addImage(1, Img1_C1);
        actualSampleImageSet.addImage(1, Img2_C1);
        actualSampleImageSet.addImage(1, Img3_C1);
        
        assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1)));
    }

    @Test
    public void findChannelImage_WrongNFilesInExcel_RaisesIOException() {
        File rootOneCamera = new File(SampleImageSetByExcelFileFinderTest.class.getResource("OneCamera").getPath());
        File wrongOneCameraExcel = new File(
            SampleImageSetByExcelFileFinderTest.class.getResource("WrongTemplateOneCamera-Channel1.xlsx").getPath());

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.One);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup);

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(new TiffImageChecker());
        IOException exception = assertThrows(IOException.class, () -> {finder.findChannelImages(sampleImageSet, 1, wrongOneCameraExcel);});

        System.out.println(exception.getMessage());
    }

}