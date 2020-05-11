package fr.fresnel.fourPolar.core.imagingSetup;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;

/**
 * An interface that models the four polar imaging setup. The setup consists of
 * number of {@link Cameras}, {@link IChannel}, as well as
 * {@link INumericalAperture}.
 */
public interface IFourPolarImagingSetup {
    /**
     * Number of cameras used in the setup.
     */
    public Cameras getCameras();

    /**
     * Set number of cameras.
     * 
     * @throws IllegalArgumentException if already set.
     */
    public void setCameras(Cameras cameras) throws IllegalArgumentException;

    /**
     * Returns the propagation channel.
     * 
     * @param n is the channel number starting from one.
     * 
     * @throws IllegalArgumentException if channel is not in the imaging channels.
     */
    public IChannel getChannel(int channel) throws IllegalArgumentException;

    /**
     * Set propagation channel channel.
     * 
     * @param channel            channel number
     * @param propagationChannel propagation channel data
     * 
     * @throws IllegalArgumentException in case of duplicate channel number or
     *                                  channel parameters.
     */
    public void setChannel(int channel, IChannel propagationChannel) throws IllegalArgumentException;

    /**
     * Returns number of channels.
     */
    public int getNumChannel();

    /**
     * Returns the numerical aperture of the setup.
     */
    public INumericalAperture getNumericalAperture();

    /**
     * Sets the numerical aperture.
     * 
     * @throws IllegalArgumentException if already set.
     */
    public void setNumericalAperture(INumericalAperture na);

    /**
     * Return the field of view for each polarization.
     */
    public IFieldOfView getFieldOfView();

    /**
     * Set the field of view.
     * 
     * @throws IllegalArgumentException if already set.
     */
    public void setFieldOfView(IFieldOfView fov) throws IllegalArgumentException;

}