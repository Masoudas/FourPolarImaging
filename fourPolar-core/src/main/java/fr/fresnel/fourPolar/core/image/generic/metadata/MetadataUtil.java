package fr.fresnel.fourPolar.core.image.generic.metadata;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;

/**
 * A set of utility methods on metadata information.
 */
public class MetadataUtil {
    private MetadataUtil() {
        throw new AssertionError();
    }

    /**
     * Returns true if image is planer (has at most one z point, and time point, and
     * channel or a combination of them).
     */
    public boolean isImagePlanar(IMetadata metadata) {
        long[] dim = metadata.getDim();

        long nChannels = dim[metadata.axisOrder().t_axis];
        long nZpoints = dim[metadata.axisOrder().z_axis];
        long nTimepoints = dim[metadata.axisOrder().c_axis];

        return nChannels < 1 && nZpoints < 1 && nTimepoints < 1;
    }

}