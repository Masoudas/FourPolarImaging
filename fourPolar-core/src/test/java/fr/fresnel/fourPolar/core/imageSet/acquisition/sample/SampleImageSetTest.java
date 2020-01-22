package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class SampleImageSetTest {
    File root = new File("/root");

    File pol0 = new File(root, "pol0.tiff");
    File pol45 = new File(root, "pol45.tiff");
    File pol90 = new File(root, "pol90.tiff");
    File pol135 = new File(root, "pol135.tiff");

    CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);
    FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.Four);

    SampleImageSet sampleSet = new SampleImageSet(imagingSetup, new DummyImageChecker());

    public SampleImageSetTest() throws CorruptCapturedImage {
        sampleSet.addImage(1, fileSet);
        sampleSet.addImage(2, fileSet);
    }

    @Test
    public void addImage_DuplicateImage_ShouldThrowException() throws IllegalArgumentException, CorruptCapturedImage {
        try {
            sampleSet.addImage(1, fileSet);
        } catch (KeyAlreadyExistsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void removeImage_fileSet_ReturnsZeroLengthForChannelOne() {
        // Create a new file set.
        File pol0 = new File(root, "pol0.tiff");
        File pol45 = new File(root, "pol45.tiff");
        File pol90 = new File(root, "pol90.tiff");
        File pol135 = new File(root, "pol135.tiff");

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        sampleSet.removeImage(1, fileSet.getSetName());
        assertTrue(sampleSet.getChannelImages(1).size() == 0);

    }

    @Test
    public void removeImage_nonExistentfileSet_ShouldThrowException() {
        File pol0 = new File(root, "qpol0.tiff");
        File pol45 = new File(root, "qpol45.tiff");
        File pol90 = new File(root, "qpol90.tiff");
        File pol135 = new File(root, "qpol135.tiff");

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);

        try {
            sampleSet.removeImage(1, fileSet.getSetName());
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }

}

class DummyImageChecker implements ICapturedImageChecker {

    @Override
    public String getExtension() {
        return "tiff";
    }

    @Override
    public void checkCompatible(File imagePath) throws CorruptCapturedImage {

    }

    
}