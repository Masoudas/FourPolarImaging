package fr.fresnel.fourPolar.core.imagingSetup;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * This class encapsulates the imaging setup, used for capturing images of four
 * polarization.
 */
public class FourPolarImagingSetup {
    private int nChannel;
    private Cameras cameras;
    
    /**
     * 
     * @param nChannel : Number of channels
     * @param cameras : Number of cameras
     */
    public FourPolarImagingSetup(int nChannel, Cameras cameras){
        this.nChannel = nChannel;
        this.cameras = cameras;   
    }

    /**
     * @return the cameras
     */
    public Cameras getCameras() {
        return cameras;
    }

    /**
     * @return the nChannel
     */
    public int getnChannel() {
        return nChannel;
    }


    
}