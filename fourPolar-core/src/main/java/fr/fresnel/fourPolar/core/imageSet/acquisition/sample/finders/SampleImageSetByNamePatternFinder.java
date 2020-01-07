package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders;

import java.io.File;
import java.util.ArrayList;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;

/**
 * Using this class, we can find the images of the sample set on the given root
 * folder. The class uses the labels to look for the files. Subfolders of the 
 * root folder are not searched in this scheme.
 * 
 * Example: Suppose for the four camera case, we have pol0 = "P0", pol45 =
 * "P45", pol90 = "P90" and pol135 = "P135", and channelLabel = null (meaning
 * there's only one channel present). In this case, All the files of the format
 * xxxP0xxx.xxx, xxxP45xxx.xxx, xxxP90xxx.xxx and xxxP135xxx.xxx are found, and
 * the class makes sure that the part that the before and after the polarization
 * labels of all files are the same.
 * 
 */
public class SampleImageSetByNamePatternFinder {
    private String polLabel[] = null;
    private File rootFolder = null;
    private SampleImageSet sampleSet = null;

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

    public void findChannelTiffImages(int channel, String channelLabel) {
        findChannelImages(channel, channelLabel, "tiff");
    }

    private void findChannelImages(int channel, String channelLabel, String fileType) {
        if (this.polLabel.length == 1)
            findChannelImages_OneCamera(channel, channelLabel, fileType);
    
        else if (this.polLabel.length == 2)
            findChannelImages_TwoCamera(channel, channelLabel, fileType);

        else if (this.polLabel.length == 4)
            findChannelImages_FourCamera(channel, channelLabel, fileType);

    }

    private void findChannelImages_OneCamera(int channel, String channelLabel, String fileType) {
        File[] imagesPol0_45_90_135 = rootFolder.listFiles(new FilterCapturedImage(channelLabel, null, fileType));

        for (File imagePol0_45_90_135 : imagesPol0_45_90_135) {
            CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_45_90_135);
            sampleSet.addImage(channel, fileSet);
        }
    }

    private void findChannelImages_TwoCamera(int channel, String channelLabel, String fileType) {
        File[] imagesPol0_90 = rootFolder.listFiles(new FilterCapturedImage(channelLabel, null, fileType));

        for (File imagePol0_90 : imagesPol0_90) {
            FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0_90, polLabel[0], polLabel[1]);
            File[] candidatesPol45_135 = rootFolder.listFiles(filterFile);
                
            if (candidatesPol45_135.length == 1){
                CapturedImageFileSet fileSet = new CapturedImageFileSet(imagePol0_90, candidatesPol45_135[0]);
                sampleSet.addImage(channel, fileSet);
            }
        
        }
    }

    private void findChannelImages_FourCamera(int channel, String channelLabel, String fileType) {
        File[] imagesPol0 = rootFolder.listFiles(new FilterCapturedImage(channelLabel, null, fileType));
        
        File[] polFiles = new File[4];
        for (File imagePol0 : imagesPol0) {
            for (int i = 1; i < this.polLabel.length; i++) {
                FilterPolarizationFile filterFile = new FilterPolarizationFile(imagePol0, polLabel[0], polLabel[i]);
                File[] candidates = rootFolder.listFiles(filterFile);
                
                if (candidates.length != 1)
                    break;
                
                polFiles[i-1] = candidates[0];
            }
            
            CapturedImageFileSet fileSet = new CapturedImageFileSet(polFiles[0], polFiles[1], polFiles[2], polFiles[3]);
            sampleSet.addImage(channel, fileSet);
        }

    }

}