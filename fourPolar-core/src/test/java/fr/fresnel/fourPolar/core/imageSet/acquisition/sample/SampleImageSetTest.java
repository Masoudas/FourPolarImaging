package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.Test;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;


/**
 * SampleImageSetTest
 */
public class SampleImageSetTest {
    File root = new File("/root");
    
    File pol0 = new File(root, "pol0.tiff");
    File pol45 = new File(root, "pol45.tiff");
    File pol90 = new File(root, "pol90.tiff");
    File pol135 = new File(root, "pol135.tiff");

    CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);
    FourPolarImagingSetup imagingSetup = new FourPolarImagingSetup(2, Cameras.Four);

    SampleImageSet sampleSet = new SampleImageSet(imagingSetup);

    public SampleImageSetTest(){
        sampleSet.addImage(1, fileSet);
        sampleSet.addImage(2, fileSet);
    }

    @Test
    public void addImage_DuplicateImage_ShouldThrowException() {
        try {
            sampleSet.addImage(1, fileSet);    
        } catch (KeyAlreadyExistsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void removeImage_fileSet_ReturnsZeroLengthForChannelOne() {
        sampleSet.removeImage(1, fileSet);

        assertTrue(sampleSet.getChannelImages(1).size() == 0);
        
    }

    @Test
    public void removeImage_nonExistentfileSet_ShouldThrowException()
    {
        File pol0 = new File(root, "qpol0.tiff");
        File pol45 = new File(root, "qpol45.tiff");
        File pol90 = new File(root, "qpol90.tiff");
        File pol135 = new File(root, "qpol135.tiff");

        CapturedImageFileSet fileSet = new CapturedImageFileSet(pol0, pol45, pol90, pol135);
        
        try {
            sampleSet.removeImage(1, fileSet);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    
    }

}