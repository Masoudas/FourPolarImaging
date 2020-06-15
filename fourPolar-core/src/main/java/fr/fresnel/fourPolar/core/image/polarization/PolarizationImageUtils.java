package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * A set of utility methods for polarization image (sets).
 */
public class PolarizationImageUtils {
    public PolarizationImageUtils(){
        throw new AssertionError();
    }
    
    /**
     * Returns the plane size of each polarization image of the set.
     */
    public static long getPlaneSize(IPolarizationImageSet polarizationImageSet){
        // Note that all four polarizations have same image size, hence we use pol0.
        Image<UINT16> pol0Image = polarizationImageSet.getPolarizationImage(Polarization.pol0).getImage();

        return MetadataUtil.getPlaneSize(pol0Image.getMetadata());
    }
}