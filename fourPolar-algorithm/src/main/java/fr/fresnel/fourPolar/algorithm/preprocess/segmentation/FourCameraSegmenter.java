package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

public class FourCameraSegmenter extends ConstellationSegmenter {
    public FourCameraSegmenter(IFieldOfView fov, int numChannels) {
        super(fov, numChannels);
    }

    @Override
    public void setCapturedImageSet(ICapturedImageSet capturedImageSet) {
        ChannelPolarizationSegmenter segmenter = _selectSegmenter(capturedImageSet);

        String[] imageLabels = Cameras.getLabels(Cameras.Four);
        ICapturedImage[] capturedImages_pol0 = capturedImageSet.getCapturedImage(imageLabels[0]);
        ICapturedImage[] capturedImages_pol45 = capturedImageSet.getCapturedImage(imageLabels[1]);
        ICapturedImage[] capturedImages_pol90 = capturedImageSet.getCapturedImage(imageLabels[2]);
        ICapturedImage[] capturedImages_pol135 = capturedImageSet.getCapturedImage(imageLabels[3]);

        IBoxShape pol0FoV = this._fov.getFoV(Polarization.pol0);
        this._pol0 = segmenter.segment(capturedImages_pol0, pol0FoV);

        IBoxShape pol45FoV = this._fov.getFoV(Polarization.pol45);
        this._pol45 = segmenter.segment(capturedImages_pol45, pol45FoV);

        IBoxShape pol90FoV = this._fov.getFoV(Polarization.pol90);
        this._pol90 = segmenter.segment(capturedImages_pol90, pol90FoV);

        IBoxShape pol135FoV = this._fov.getFoV(Polarization.pol135);
        this._pol135 = segmenter.segment(capturedImages_pol135, pol135FoV);

        this._fileSet = capturedImageSet.fileSet();
    }

}