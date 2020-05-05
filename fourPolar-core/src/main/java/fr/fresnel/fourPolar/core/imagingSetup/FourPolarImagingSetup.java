package fr.fresnel.fourPolar.core.imagingSetup;

import java.util.ArrayList;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;

/**
 * This class encapsulates the imaging setup, used for capturing images of four
 * polarization.
 */
public class FourPolarImagingSetup {
    private final ArrayList<IChannel> _channels;

    private Cameras _cameras;
    private IFieldOfView _fov = null;
    private INumericalAperture _numAperture;

    private static FourPolarImagingSetup _instance = null;

    public static FourPolarImagingSetup instance() {
        if (_instance == null) {
            _instance = new FourPolarImagingSetup();
        }

        return _instance;
    }

    /**
     * This class encapsulates the imaging setup, used for capturing images of four
     * polarization.
     * 
     * @param nChannel : Number of channels
     * @param cameras  : Number of cameras
     */
    private FourPolarImagingSetup() {
        this._channels = new ArrayList<>();
    }

    public void setCameras(Cameras cameras) {
        this._cameras = cameras;
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
    public int getNumChannel() {
        return this._channels.size();
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
     * @return the numerical Aperture
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
    public void setChannel(int channel, IChannel propagationChannel) {
        this._checkChannel(channel);
        this._channels.add(channel, propagationChannel);
    }

    /**
     * @return the propagation channel properties.
     */
    public IChannel getChannel(int channel) {
        this._checkChannel(channel);
        return this._channels.get(channel);
    }

    private void _checkChannel(int channel) {
        if (channel <= 0) {
            throw new IllegalArgumentException("Channel number must be greater than zero.");
        }
    }

}