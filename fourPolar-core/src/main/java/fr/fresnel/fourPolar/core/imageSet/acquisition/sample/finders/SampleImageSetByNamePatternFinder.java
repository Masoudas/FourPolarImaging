package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders;

import java.io.File;
import java.util.ArrayList;

import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;


/**
 * Using this class, we can find the images of the sample set on the given
 * root folder.
 * The class uses the labels to look for the files.
 
 * Example: 
 * Suppose for the four camera case, we have pol0 = "P0", pol45 = "P45",
 * pol90 = "P90" and pol135 = "P135", and channelLabel = null (meaning there's
 * only one channel present). In this case, All the files of the format
 * xxxP0xxx.xxx, xxxP45xxx.xxx, xxxP90xxx.xxx and xxxP135xxx.xxx are found,
 * and the class makes sure that the part that the before and after the polarization
 * labels of all files are the same.
 *  
 */
public class SampleImageSetByNamePatternFinder {
    private String polLabel[] = null;
    private File rootFolder = null;
    private SampleImageSet sampleSet = null;
    private ArrayList<File> rejectedFiles = new ArrayList<File>();

    public SampleImageSetByNamePatternFinder(SampleImageSet sampleSet, File rootFolder) {
        this.rootFolder = rootFolder;
        this.sampleSet = sampleSet;
    }

    public SampleImageSetByNamePatternFinder(SampleImageSet sampleSet, File rootFolder, String labelPol0_90,
            String labelPol45_135) {
        this(sampleSet, rootFolder);

        polLabel = new String[2];
        polLabel[0] = labelPol0_90;
        polLabel[1] = labelPol45_135;              
    }

    public SampleImageSetByNamePatternFinder(SampleImageSet sampleSet, File rootFolder, String labelPol0,
            String labelPol45, String labelPol90, String labelPol135) {
        this(sampleSet, rootFolder);        

        polLabel = new String[4];
        polLabel[0] = labelPol0;
        polLabel[1] = labelPol45;           
        polLabel[2] = labelPol90;           
        polLabel[3] = labelPol135;           
    }

    private void findChannelTiffImages(int channel, String channelLabel) {
        
        // rootFolder.listFiles(filter);
    }

    private void findChannelImages(int channel, String channelLabel, String fileType) {
        // right now we suppose that all images are in the root folder.
        File[] allImages = rootFolder.listFiles();

    }

    private File[] findChannelImagesOneCamera(int channel, String channelLabel, String fileType){
        return rootFolder.listFiles(new FilterCapturedImage(channelLabel, null, fileType));
    }


    private File[] findChannelImagesTwoCamera(int channel, String channelLabel, String fileType){
        File[] imagesPol0_90 = rootFolder.listFiles(new FilterCapturedImage(channelLabel, null, fileType));
        File[] imagesPol45_135 = rootFolder.listFiles(new FilterCapturedImage(channelLabel, null, fileType));

        
    }
}