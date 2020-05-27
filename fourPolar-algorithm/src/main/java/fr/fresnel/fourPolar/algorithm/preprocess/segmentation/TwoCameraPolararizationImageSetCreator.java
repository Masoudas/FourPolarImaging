package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

public class TwoCameraPolararizationImageSetCreator extends PolarizationImageSetCreator {
    public TwoCameraPolararizationImageSetCreator(IFieldOfView fov, int numChannels) {
        super(fov, numChannels);
    }

    public void setCapturedImageSet(ICapturedImageSet capturedImageSet) {
        assert _segmenter != null;

        String[] imageLabels = Cameras.getLabels(Cameras.Two);
        ICapturedImage[] capturedImages_pol0_90 = capturedImageSet.getCapturedImage(imageLabels[0]);
        ICapturedImage[] capturedImages_pol45_135 = capturedImageSet.getCapturedImage(imageLabels[1]);

        IBoxShape pol0FoV = this._fov.getFoV(Polarization.pol0);
        this._pol0 = this._segmenter.segment(capturedImages_pol0_90, pol0FoV);

        IBoxShape pol45FoV = this._fov.getFoV(Polarization.pol45);
        this._pol45 = this._segmenter.segment(capturedImages_pol45_135, pol45FoV);

        IBoxShape pol90FoV = this._fov.getFoV(Polarization.pol90);
        this._pol90 = this._segmenter.segment(capturedImages_pol0_90, pol90FoV);

        IBoxShape pol135FoV = this._fov.getFoV(Polarization.pol135);
        this._pol135 = this._segmenter.segment(capturedImages_pol45_135, pol135FoV);

        this._fileSet = capturedImageSet.fileSet();
    }

}