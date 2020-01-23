package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.ExcelIncorrentRow;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.MissingExcelTitleRow;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.TemplateSampleSetExcelNotFound;
import fr.fresnel.fourPolar.io.image.tiff.TiffCapturedImageChecker;

public class SampleImageSetByExcelFileFinderTest {
        static private File root;

        @BeforeAll
        private static void setRoot() {
                root = new File(SampleImageSetByExcelFileFinderTest.class.getResource("SampleImageSetByExcelFileFinder")
                                .getPath());
        }

        @Test
        public void findChannelImages_OneCamera_ReturnsThreeCapturedSetsForEachChannel()
                        throws TemplateSampleSetExcelNotFound, MissingExcelTitleRow, ExcelIncorrentRow,
                        CorruptCapturedImage {
        File rootOneCamera = new File(root, "OneCamera");
        File oneCameraChannel1Excel = new File(rootOneCamera, "TemplateOneCamera-Channel1.xlsx");
        File oneCameraChannel2Excel = new File(rootOneCamera, "TemplateOneCamera-Channel2.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.One);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder();

        List<RejectedCapturedImage> rejectedChan1 = finder.findChannelImages(sampleImageSet, 1, oneCameraChannel1Excel);
        List<RejectedCapturedImage> rejectedChan2 = finder.findChannelImages(sampleImageSet, 2, oneCameraChannel2Excel);

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C1.tif"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C1.tif"));
        System.out.println(new File(rootOneCamera, "Img2_C1.tif").exists());

        ICapturedImageFileSet Img1_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img1_C2.tif"));
        ICapturedImageFileSet Img2_C2 = new CapturedImageFileSet(new File(rootOneCamera, "Img2_C2.tif"));

        SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());
        actualSampleImageSet.addImage(1, Img1_C1);
        actualSampleImageSet.addImage(1, Img2_C1);
        actualSampleImageSet.addImage(2, Img1_C2);
        actualSampleImageSet.addImage(2, Img2_C2);


        assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1))
                && actualSampleImageSet.getChannelImages(2).equals(sampleImageSet.getChannelImages(2))
                && rejectedChan1.size() == 2 && rejectedChan2.size() == 2);
    }

    @Test
    public void findChannelImages_TwoCamera_ReturnsThreeCapturedSets()
            throws TemplateSampleSetExcelNotFound, MissingExcelTitleRow, ExcelIncorrentRow,
                        KeyAlreadyExistsException, IllegalArgumentException, CorruptCapturedImage {
        File rootTwoCamera = new File(root, "TwoCamera");
        File twoCameraExcel = new File(rootTwoCamera, "TemplateTwoCamera.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Two);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder();

        List<RejectedCapturedImage> rejectedChan1 = finder.findChannelImages(sampleImageSet, 1, twoCameraExcel);

        // Generate sets to see if found
        ICapturedImageFileSet Img1_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img1_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img1_C1_Pol45_135.tif"));
        ICapturedImageFileSet Img2_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img2_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img2_C1_Pol45_135.tif"));
        ICapturedImageFileSet Img3_C1 = new CapturedImageFileSet(new File(rootTwoCamera, "Img3_C1_Pol0_90.tif"),
                new File(rootTwoCamera, "Img3_C1_Pol45_135.tif"));

        SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());
        actualSampleImageSet.addImage(1, Img1_C1);
        actualSampleImageSet.addImage(1, Img2_C1);
        actualSampleImageSet.addImage(1, Img3_C1);
        assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1))
                && rejectedChan1.size() == 3);
    }

    @Test
    public void findChannelImages_FourCamera_ReturnsThreeCapturedSets()
            throws TemplateSampleSetExcelNotFound, MissingExcelTitleRow, ExcelIncorrentRow,
                CorruptCapturedImage {
        File rootFourCamera = new File(root, "FourCamera");
        File fourCameraExcel = new File(rootFourCamera, "TemplateFourCamera.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Four);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder();
        List<RejectedCapturedImage> rejectedChan1 = finder.findChannelImages(sampleImageSet, 1, fourCameraExcel);

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

        SampleImageSet actualSampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());
        actualSampleImageSet.addImage(1, Img1_C1);
        actualSampleImageSet.addImage(1, Img2_C1);
        actualSampleImageSet.addImage(1, Img3_C1);

        assertTrue(actualSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1))
                && rejectedChan1.size() == 3);
    }

    @Test
    public void findChannelImage_WrongNFilesInExcel_RaisesExcelIncorrentRow() {
        File wrongOneCameraExcel = new File(root, "WrongTemplateOneCamera-Channel1.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.One);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder();
        ExcelIncorrentRow exception = assertThrows(ExcelIncorrentRow.class, () -> {
            finder.findChannelImages(sampleImageSet, 1, wrongOneCameraExcel);
        });

        System.out.println(exception.getMessage());
    }

    @Test
    public void findChannelImage_NoTitleRow_RaisesMissingExcelTitleRow() {
        File wrongOneCameraExcel = new File(root, "MissingTitleTemplateOneCamera-Channel1.xlsx");

        FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.One);
        SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffCapturedImageChecker());

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder();
        MissingExcelTitleRow exception = assertThrows(MissingExcelTitleRow.class, () -> {
            finder.findChannelImages(sampleImageSet, 1, wrongOneCameraExcel);
        });

        System.out.println(exception.getMessage());
    }

}