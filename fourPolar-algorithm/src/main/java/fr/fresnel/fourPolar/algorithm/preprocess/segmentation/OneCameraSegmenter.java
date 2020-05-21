package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

/**
 * Segments the images of a one camera setup.
 */
class OneCameraSegmenter extends ConstellationSegmenter {
    public OneCameraSegmenter(IFieldOfView fov, int numChannels) {
        super(fov, numChannels);
    }

    @Override
    public void setCapturedImageSet(ICapturedImageSet capturedImageSet) {
        ChannelPolarizationSegmenter segmenter = _selectSegmenter(capturedImageSet);

        ICapturedImage[] capturedImages = capturedImageSet.getCapturedImage(Cameras.getLabels(Cameras.One)[0]);

        IBoxShape pol0FoV = this._fov.getFoV(Polarization.pol0);
        this._pol0 = segmenter.segment(capturedImages, pol0FoV);

        IBoxShape pol45FoV = this._fov.getFoV(Polarization.pol45);
        this._pol45 = segmenter.segment(capturedImages, pol45FoV);

        IBoxShape pol90FoV = this._fov.getFoV(Polarization.pol90);
        this._pol90 = segmenter.segment(capturedImages, pol90FoV);

        IBoxShape pol135FoV = this._fov.getFoV(Polarization.pol135);
        this._pol135 = segmenter.segment(capturedImages, pol135FoV);

        this._fileSet = capturedImageSet.fileSet();
    }

}