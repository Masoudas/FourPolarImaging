package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

/**
 * Removes the dark background from the given polarization set.
 */
public class ChannelDarkBackgroundRemover implements IChannelDarkBackgroundRemover {
    private final IChannelDarkBackground _darkBackground;

    public static IChannelDarkBackgroundRemover create(IChannelDarkBackground darkBackground) {
        return new ChannelDarkBackgroundRemover(darkBackground);
    }

    private ChannelDarkBackgroundRemover(IChannelDarkBackground darkBackground) {
        this._darkBackground = darkBackground;
    }

    /**
     * Remove the dark background of the given image set.
     * 
     * @param imageSet
     */
    @Override
    public void remove(IPolarizationImageSet imageSet) {
        Image<UINT16> pol0 = imageSet.getPolarizationImage(Polarization.pol0).getImage();
        int backgroundPol0 = (int) this._darkBackground.getBackgroundLevel(Polarization.pol0);
        this._removePolarizationBackground(pol0, backgroundPol0);

        Image<UINT16> pol45 = imageSet.getPolarizationImage(Polarization.pol45).getImage();
        int backgroundPol45 = (int) this._darkBackground.getBackgroundLevel(Polarization.pol45);
        this._removePolarizationBackground(pol45, backgroundPol45);

        Image<UINT16> pol90 = imageSet.getPolarizationImage(Polarization.pol90).getImage();
        int backgroundPol90 = (int) this._darkBackground.getBackgroundLevel(Polarization.pol90);
        this._removePolarizationBackground(pol90, backgroundPol90);

        Image<UINT16> pol135 = imageSet.getPolarizationImage(Polarization.pol135).getImage();
        int backgroundPol135 = (int) this._darkBackground.getBackgroundLevel(Polarization.pol135);
        this._removePolarizationBackground(pol135, backgroundPol135);

    }

    /**
     * If intensity - background <= 0, will be set to zero, otherwise, the new value
     * is set.
     * 
     */
    private void _removePolarizationBackground(Image<UINT16> image, int background) {
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            final IPixel<UINT16> pixel = cursor.next();
            pixel.value().set(pixel.value().get() - background);
            cursor.setPixel(pixel);
        }
    }
}