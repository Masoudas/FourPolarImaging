package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.image.IImageChecker;
import fr.fresnel.fourPolar.io.image.tiff.TiffImageChecker;

/**
 * Using this class, we can find the images of the sample set on the given root
 * folder. The class uses the labels to look for the files. Subfolders of the
 * root folder are not searched in this scheme.
 * <p>
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
    private IChannelImageFinder channelImageFinder = null;

    /**
     * Used for finding the images in case of single camera.
     * 
     * @param sampleSet  :
     * @param rootFolder :
     */
    public SampleImageSetByNamePatternFinder(SampleImageSet sampleSet, File rootFolder) {
        this.rootFolder = rootFolder;
        this.sampleSet = sampleSet;

        this.channelImageFinder = new OneCameraChannelImageFinder();
    }

    /**
     * Used for finding images in case of two cameras.
     * 
     * @param sampleSet
     * @param rootFolder
     * @param labelPol0_90
     * @param labelPol45_135
     */
    public SampleImageSetByNamePatternFinder(SampleImageSet sampleSet, File rootFolder, String labelPol0_90,
            String labelPol45_135) {
        this.rootFolder = rootFolder;
        this.sampleSet = sampleSet;

        polLabel = new String[2];
        polLabel[0] = labelPol0_90;
        polLabel[1] = labelPol45_135;

        this.channelImageFinder = new TwoCameraChannelImageFinder();
    }

    /**
     * Used for finding images in case of four cameras.
     * 
     * @param sampleSet
     * @param rootFolder
     * @param labelPol0
     * @param labelPol45
     * @param labelPol90
     * @param labelPol135
     */
    public SampleImageSetByNamePatternFinder(SampleImageSet sampleSet, File rootFolder, String labelPol0,
            String labelPol45, String labelPol90, String labelPol135) {
        this.rootFolder = rootFolder;
        this.sampleSet = sampleSet;

        polLabel = new String[4];
        polLabel[0] = labelPol0;
        polLabel[1] = labelPol45;
        polLabel[2] = labelPol90;
        polLabel[3] = labelPol135;

        this.channelImageFinder = new FourCameraChannelImageFinder();
    }

    public void findChannelImages(int channel, String channelLabel, IImageChecker imageChecker) {
        this.channelImageFinder.find(rootFolder, sampleSet, channel, channelLabel, polLabel, imageChecker);
    }

}