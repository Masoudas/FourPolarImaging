package fr.fresnel.fourPolar.core.imagingSetup;

import java.util.ArrayList;
import java.util.HashMap;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;

/**
 * This class encapsulates the imaging setup, used for capturing images of four
 * polarization.
 */
public class FourPolarImagingSetup {
    private final HashMap<Integer, IChannel> _channels;

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
        this._channels = new HashMap<>();
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
    public void setChannel(int channel, IChannel propagationChannel) throws IllegalArgumentException {
        this._checkChannel(channel);
        if (this._channels.containsKey(channel)) {
            throw new IllegalArgumentException("Duplicate channel number.");
        }

        for (IChannel existingChannel : this._channels.values()) {
            if (existingChannel.equals(propagationChannel)) {
                throw new IllegalArgumentException("Duplicate propagation channel.");
            }
        }

        this._channels.put(channel, propagationChannel);
    }

    /**
     * @return the propagation channel.
     */
    public IChannel getChannel(int channel) {
        this._checkChannel(channel);
        
        if (channel > this._channels.size()) {
            throw new IllegalArgumentException("Channel exceeds number of channels.");
        }
        
        return this._channels.get(channel);
    }

    private void _checkChannel(int channel) {
        if (channel <= 0) {
            throw new IllegalArgumentException("Channel number must be greater than zero.");
        }
    }

}