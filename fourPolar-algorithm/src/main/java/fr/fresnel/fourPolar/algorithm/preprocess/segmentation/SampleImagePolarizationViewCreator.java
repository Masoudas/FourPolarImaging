package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import java.util.Map;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Creates polarization view for sample images as described by
 * {@link PolarizationViewCreator}.
 */
class SampleImagePolarizationViewCreator extends PolarizationViewCreator {

    @Override
    public Map<Polarization, Image<UINT16>> create(ICapturedImageSet capturedImageSet, IFieldOfView fov, int channel) {
        
        return null;
    }

}