package fr.fresnel.fourPolar.io.imageSet.acquisition.sample;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.CorruptSampleSetExcel;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.SampleSetExcelNotFound;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.ExcelIncorrentRow;
import fr.fresnel.fourPolar.io.image.tiff.TiffImageChecker;
import fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern.SampleImageSetByNamePatternFinder;

/**
 * This test class tests the reader and writer simultaneously.
 */
public class SampleImageSetReaderWriterTest {
        private static File root;

        @BeforeAll
        static void setRoot() {
                root = new File(SampleImageSetReaderWriterTest.class.getResource("").getPath(),
                                "SampleImageSetReaderWriterTestMaterial");
        }

        @Test
        public void writeThenRead_OneCamera_ReturnsTheSameSampleSet()
                        throws IOException, CorruptSampleSetExcel, ExcelIncorrentRow, SampleSetExcelNotFound,
                        CorruptCapturedImage {
                File rootOneCamera = new File(root, "OneCamera");
                FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.One);
                SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffImageChecker());

                SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootOneCamera);

                finder.findChannelImages(sampleImageSet, 1, "C1");
                finder.findChannelImages(sampleImageSet, 2, "C2");

                SampleImageSetWriter writer = new SampleImageSetWriter(sampleImageSet, rootOneCamera);
                writer.write();

                SampleImageSetReader reader = new SampleImageSetReader(imagingSetup, rootOneCamera);
                SampleImageSet newSampleImageSet = reader.read();

                assertTrue(newSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1))
                                && newSampleImageSet.getChannelImages(2).equals(sampleImageSet.getChannelImages(2)));
        }

        @Test
        public void writeThenRead_TwoCamera_ReturnsTheSameSampleSet() throws IOException, CorruptSampleSetExcel,
                        ExcelIncorrentRow, SampleSetExcelNotFound, CorruptCapturedImage {
                File rootTwoCamera = new File(root, "TwoCamera");
                FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Two);
                SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffImageChecker());

                SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootTwoCamera,
                                "Pol0_90", "Pol45_135");

                finder.findChannelImages(sampleImageSet, 1, "C1");

                SampleImageSetWriter writer = new SampleImageSetWriter(sampleImageSet, rootTwoCamera);
                writer.write();

                SampleImageSetReader reader = new SampleImageSetReader(imagingSetup, rootTwoCamera);
                SampleImageSet newSampleImageSet = reader.read();

                assertTrue(newSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1)));
        }

        @Test
        public void writeThenRead_FourCamera_ReturnsTheSameSampleSet() throws IOException, CorruptSampleSetExcel,
                        ExcelIncorrentRow, SampleSetExcelNotFound, CorruptCapturedImage {
                File rootFourCamera = new File(root, "FourCamera");
                FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(1, Cameras.Four);
                SampleImageSet sampleImageSet = new SampleImageSet(imagingSetup, new TiffImageChecker());

                SampleImageSetByNamePatternFinder finder = new SampleImageSetByNamePatternFinder(rootFourCamera, "Pol0",
                                "Pol45", "Pol90", "Pol135");

                finder.findChannelImages(sampleImageSet, 1, "C1");

                SampleImageSetWriter writer = new SampleImageSetWriter(sampleImageSet, rootFourCamera);
                writer.write();

                SampleImageSetReader reader = new SampleImageSetReader(imagingSetup, rootFourCamera);
                SampleImageSet newSampleImageSet = reader.read();

                assertTrue(newSampleImageSet.getChannelImages(1).equals(sampleImageSet.getChannelImages(1)));
        }

}