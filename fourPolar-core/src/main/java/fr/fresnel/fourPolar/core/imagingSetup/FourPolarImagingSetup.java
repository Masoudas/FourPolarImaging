package fr.fresnel.fourPolar.core.imagingSetup;

import java.util.HashMap;
import java.util.Objects;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;

/**
 * This class models the imaging setup as a singleton. Note that once values are
 * set through the setter methods, they can't be overridden.
 */
public class FourPolarImagingSetup implements IFourPolarImagingSetup {
    private final HashMap<Integer, IChannel> _channels;

    private Cameras _cameras = null;
    private IFieldOfView _fov = null;
    private INumericalAperture _numAperture = null;

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

    @Override
    public void setCameras(Cameras cameras) {
        Objects.requireNonNull(cameras, "cameras must not be null.");

        if (this._cameras != null) {
            throw new IllegalArgumentException("Camera is already set");
        }

        this._cameras = cameras;
    }

    @Override
    public Cameras getCameras() {
        return this._cameras;
    }

    @Override
    public int getNumChannel() {
        return this._channels.size();
    }

    @Override
    public void setFieldOfView(IFieldOfView fov) {
        Objects.requireNonNull(fov, "fov must not be null.");

        if (this._fov != null) {
            throw new IllegalArgumentException("FoV is already set");
        }

        this._fov = fov;
    }

    @Override
    public IFieldOfView getFieldOfView() {
        return this._fov;
    }

    @Override
    public void setNumericalAperture(INumericalAperture na) {
        Objects.requireNonNull(na, "na must not be null.");

        if (this._numAperture != null) {
            throw new IllegalArgumentException("NA is already set");
        }

        this._numAperture = na;
    }

    @Override
    public INumericalAperture getNumericalAperture() {
        return this._numAperture;
    }

    @Override
    public void setChannel(int channel, IChannel propagationChannel) {
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

    @Override
    public IChannel getChannel(int channel) {
        this._checkChannel(channel);

        int numChannels = this._channels.size();
        if (channel > numChannels) {
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