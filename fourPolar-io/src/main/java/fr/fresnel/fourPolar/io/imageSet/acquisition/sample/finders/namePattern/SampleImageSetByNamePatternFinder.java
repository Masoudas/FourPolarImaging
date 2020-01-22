package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.WrongSampleSetFinder;

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
    private IChannelImageFinder channelImageFinder = null;
    private Cameras _camera;
    private Hashtable<File, String> _rejectedImages;

    /**
     * Used for finding the images in case of single camera.
     * 
     * @param sampleSet  :
     * @param rootFolder :
     */
    public SampleImageSetByNamePatternFinder(File rootFolder) {
        this.rootFolder = rootFolder;
        this._camera = Cameras.One;

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
    public SampleImageSetByNamePatternFinder(File rootFolder, String labelPol0_90, String labelPol45_135) {
        this(rootFolder);
        this._camera = Cameras.Two;

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
    public SampleImageSetByNamePatternFinder(File rootFolder, String labelPol0, String labelPol45, String labelPol90,
            String labelPol135) {
        this(rootFolder);
        this._camera = Cameras.Four;

        polLabel = new String[4];
        polLabel[0] = labelPol0;
        polLabel[1] = labelPol45;
        polLabel[2] = labelPol90;
        polLabel[3] = labelPol135;

        this.channelImageFinder = new FourCameraChannelImageFinder();
    }

    public List<RejectedCapturedImage> findChannelImages(SampleImageSet sampleImageSet, int channel,
            String channelLabel) throws NoImageFoundOnRoot, WrongSampleSetFinder {
        if (sampleImageSet.getImagingSetup().getCameras() != this._camera) {
            throw new WrongSampleSetFinder("Use class constructor for " + this._camera.toString() + " cameras");
        }

        return this.channelImageFinder.find(this, sampleImageSet, channel, channelLabel);
    }

    /**
     * @return the polLabel
     */
    public String[] getPolLabel() {
        return polLabel;
    }

    /**
     * @return the rootFolder
     */
    public File getRootFolder() {
        return rootFolder;
    }

    /**
     * Returns the rejected images as a disctionary, where the keys are the rejected
     * files and the value is the reason.
     * 
     * @return the rejected images
     */
    public Hashtable<File, String> getRejectedImages() {
        return _rejectedImages;
    }
}