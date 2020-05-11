package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.ExcelIncorrentRow;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.MissingExcelTitleRow;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.TemplateSampleSetExcelNotFound;
import fr.fresnel.fourPolar.io.image.captured.tiff.checker.TiffCapturedImageChecker;

public class SampleImageSetByExcelFileFinderTest {
    static private File root;

    @BeforeAll
    private static void setRoot() {
        root = new File(
                SampleImageSetByExcelFileFinderTest.class.getResource("SampleImageSetByExcelFileFinder").getPath());
    }

    @Test
    public void findChannelImages_OneCamera_ReturnsThreeCapturedSetsForEachChannel()
            throws TemplateSampleSetExcelNotFound, MissingExcelTitleRow, ExcelIncorrentRow, IncompatibleCapturedImage {
        File rootOneCamera = new File(root, "OneCamera");
        File oneCameraChannel1Excel = new File(rootOneCamera, "TemplateOneCamera-Channel1.xlsx");
        File oneCameraChannel2Excel = new File(rootOneCamera, "TemplateOneCamera-Channel2.xlsx");

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(Cameras.One);

        Iterator<File[]> channel1 = finder.read(oneCameraChannel1Excel);
        Iterator<File[]> channel2 = finder.read(oneCameraChannel2Excel);

        assertTrue(channel1.next()[0].equals(new File(rootOneCamera, "Img1_C1.tif"))
                && channel1.next()[0].equals(new File(rootOneCamera, "Img2_C1.tif"))
                && channel2.next()[0].equals(new File(rootOneCamera, "Img1_C2.tif"))
                && channel2.next()[0].equals(new File(rootOneCamera, "Img2_C2.tif")));
    }

    @Test
    public void findChannelImages_TwoCamera_ReturnsThreeCapturedSets()
            throws TemplateSampleSetExcelNotFound, MissingExcelTitleRow, ExcelIncorrentRow, KeyAlreadyExistsException,
            IllegalArgumentException, IncompatibleCapturedImage {
        File rootTwoCamera = new File(root, "TwoCamera");
        File twoCameraExcel = new File(rootTwoCamera, "TemplateTwoCamera.xlsx");

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(Cameras.Two);

        Iterator<File[]> channel1 = finder.read(twoCameraExcel);

        File[] set1 = channel1.next();
        File[] set2 = channel1.next();
        File[] set3 = channel1.next();

        assertTrue(set1[0].equals(new File(rootTwoCamera, "Img1_C1_Pol0_90.tif"))
                && set1[1].equals(new File(rootTwoCamera, "Img1_C1_Pol45_135.tif"))
                && set2[0].equals(new File(rootTwoCamera, "Img2_C1_Pol0_90.tif"))
                && set2[1].equals(new File(rootTwoCamera, "Img2_C1_Pol45_135.tif"))
                && set3[0].equals(new File(rootTwoCamera, "Img3_C1_Pol0_90.tif"))
                && set3[1].equals(new File(rootTwoCamera, "Img3_C1_Pol45_135.tif")));
    }

    @Test
    public void findChannelImages_FourCamera_ReturnsThreeCapturedSets()
            throws TemplateSampleSetExcelNotFound, MissingExcelTitleRow, ExcelIncorrentRow, IncompatibleCapturedImage {
        File rootFourCamera = new File(root, "FourCamera");
        File fourCameraExcel = new File(rootFourCamera, "TemplateFourCamera.xlsx");

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(Cameras.Four);
        Iterator<File[]> channel1 = finder.read(fourCameraExcel);

        File[] set1 = channel1.next();
        File[] set2 = channel1.next();
        File[] set3 = channel1.next();

        assertTrue(set1[0].equals(new File(rootFourCamera, "Img1_C1_Pol0.tif"))
                && set1[1].equals(new File(rootFourCamera, "Img1_C1_Pol45.tif"))
                && set1[2].equals(new File(rootFourCamera, "Img1_C1_Pol90.tif"))
                && set1[3].equals(new File(rootFourCamera, "Img1_C1_Pol135.tif"))
                && set2[0].equals(new File(rootFourCamera, "Img2_C1_Pol0.tif"))
                && set2[1].equals(new File(rootFourCamera, "Img2_C1_Pol45.tif"))
                && set2[2].equals(new File(rootFourCamera, "Img2_C1_Pol90.tif"))
                && set2[3].equals(new File(rootFourCamera, "Img2_C1_Pol135.tif"))
                && set3[0].equals(new File(rootFourCamera, "Img3_C1_Pol0.tif"))
                && set3[1].equals(new File(rootFourCamera, "Img3_C1_Pol45.tif"))
                && set3[2].equals(new File(rootFourCamera, "Img3_C1_Pol90.tif"))
                && set3[3].equals(new File(rootFourCamera, "Img3_C1_Pol135.tif")));
    }

    @Test
    public void findChannelImage_WrongNFilesInExcel_RaisesExcelIncorrentRow() {
        File wrongOneCameraExcel = new File(root, "WrongTemplateOneCamera-Channel1.xlsx");

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(Cameras.One);

        ExcelIncorrentRow exception = assertThrows(ExcelIncorrentRow.class, () -> {
            finder.read(wrongOneCameraExcel);
        });

        System.out.println(exception.getMessage());
    }

    @Test
    public void findChannelImage_NoTitleRow_RaisesMissingExcelTitleRow() {
        File noHeaderRowExcel = new File(root, "MissingTitleTemplateOneCamera-Channel1.xlsx");

        SampleImageSetByExcelFileFinder finder = new SampleImageSetByExcelFileFinder(Cameras.One);
        MissingExcelTitleRow exception = assertThrows(MissingExcelTitleRow.class, () -> {
            finder.read(noHeaderRowExcel);
        });

        System.out.println(exception.getMessage());
    }

}