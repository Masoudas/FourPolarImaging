package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.security.KeyException;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class SampleImageSetTest {
    File root = new File("/root");

    File pol0 = new File(root, "pol0.tiff");
    File pol45 = new File(root, "pol45.tiff");
    File pol90 = new File(root, "pol90.tiff");
    File pol135 = new File(root, "pol135.tiff");

    FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.Four);

    SampleImageSet sampleSet = new SampleImageSet(imagingSetup, new DummyImageChecker());

    public SampleImageSetTest()
            throws IncompatibleCapturedImage, KeyAlreadyExistsException, IllegalArgumentException, KeyException {
        sampleSet.addImage(1, pol0, pol45, pol90, pol135);
        sampleSet.addImage(2, pol0, pol45, pol90, pol135);
    }

    @Test
    public void addImage_DuplicateImage_ShouldThrowException() throws IllegalArgumentException, IncompatibleCapturedImage, KeyException {
        try {
            sampleSet.addImage(1, pol0, pol45, pol90, pol135);
        } catch (KeyAlreadyExistsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void removeImage_fileSet_ReturnsZeroLengthForChannelOne() throws IllegalArgumentException, KeyException {
        sampleSet.removeImage(1, sampleSet.getChannelImages(1).get(0).getSetName());
        assertTrue(sampleSet.getChannelImages(1).size() == 0);
    }

    @Test
    public void removeImage_nonExistentfileSet_ShouldThrowException() throws KeyException {
        try {
            sampleSet.removeImage(1, "wrongSetName");
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
    public void check(File imagePath) throws IncompatibleCapturedImage {

    }

    
}