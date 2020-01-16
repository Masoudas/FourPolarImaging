package fr.fresnel.fourPolar.core.imagingSetup;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IPropagationChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;

/**
 * This class encapsulates the imaging setup, used for capturing images of four
 * polarization.
 */
public class FourPolarImagingSetup {
    private int _nChannel;
    private Cameras _cameras;
    private IFieldOfView _fov = null;
    private IPropagationChannel[] _pChannel;
    private INumericalAperture _numAperture;

    /**
     * This class encapsulates the imaging setup, used for capturing images of four
     * polarization.
     * 
     * @param nChannel : Number of channels
     * @param cameras  : Number of cameras
     */
    public FourPolarImagingSetup(int nChannel, Cameras cameras) {
        this._nChannel = nChannel;
        this._cameras = cameras;

        this._pChannel = new IPropagationChannel[nChannel];
    }

    /**
     * @return the cameras
     */
    public Cameras getCameras() {
        return this._cameras;
    }

    /**
     * @return the nChannel
     */
    public int getnChannel() {
        return this._nChannel;
    }

    /**
     * Set the field of view.
     * 
     * @param fov
     */
    public void setFieldOfView(IFieldOfView fov) {
        this._fov = fov;
    }

    /**
     * @return the field of view
     */
    public IFieldOfView getFieldOfView() {
        return this._fov;
    }

    /**
     * Sets the numerical aperture.
     * 
     * @param na
     */
    public void setNumericalAperture(INumericalAperture na) {
        this._numAperture = na;
    }

    /**
     * @return the _numAperture
     */
    public INumericalAperture getNumericalAperture() {
        return this._numAperture;
    }

    /**
     * Set propagation channel channel.
     * 
     * @param channel            : channel number
     * @param propagationChannel : propagation channel data
     */
    public void setPropagationChannel(int channel, IPropagationChannel propagationChannel) {
        this._pChannel[channel-1] = propagationChannel;
    }

    /**
     * @return the _pChannel
     */
    public IPropagationChannel getPropagationChannel(int channel) {
        return this._pChannel[channel-1];
    }
}