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
     * Returns the propagation channel.
     * 
     * @param n is the channel number starting from one.
     * 
     * @throws IllegalArgumentException if channel is not in the imaging channels.
     */
    public IChannel getChannel(int channel) throws IllegalArgumentException;

    /**
     * Returns number of channels.
     */
    public int getNumChannel();

    /**
     * Returns the numerical aperture of the setup.
     */
    public INumericalAperture getNumericalAperture();

    /**
     * Return the field of view for each polarization.
     */
    public IFieldOfView getFieldOfView();

}