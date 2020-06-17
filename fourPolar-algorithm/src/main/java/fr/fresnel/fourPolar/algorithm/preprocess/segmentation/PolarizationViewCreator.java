package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import java.util.Map;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Using this class, we can create a view of polarizations over a captured
 * image. Note that by a view we simply a cursor that is wrapped into an image.
 * Hence the process of creating a view does not add any memory overhead.
 */
abstract class PolarizationViewCreator {
    /**
     * Create view of all polarizations using the field of view for the given
     * channel.
     * 
     * @return
     */
    abstract public Map<Polarization, Image<UINT16>> create(ICapturedImageSet capturedImageSet, IFieldOfView fov, int channel);

}