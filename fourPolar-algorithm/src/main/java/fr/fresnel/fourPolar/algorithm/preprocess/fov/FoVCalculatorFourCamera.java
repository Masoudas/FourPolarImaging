package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

class FoVCalculatorFourCamera implements IFoVCalculator {
    private final IMetadata _pol0;
    private final IMetadata _pol45;
    private final IMetadata _pol90;
    private final IMetadata _pol135;

    public FoVCalculatorFourCamera(IMetadata pol0, IMetadata pol45, IMetadata pol90, IMetadata pol135) {
        _pol0 = pol0;
        _pol45 = pol45;
        _pol90 = pol90;
        _pol135 = pol135;

    }

    @Override
    public IFieldOfView calculate(IBoxShape pol0_fov, IBoxShape pol45_fov, IBoxShape pol90_fov, IBoxShape pol135_fov) {
        FoVCalculatorUtil.checkFoVBoxIsInImageBoundary(_pol0, pol0_fov, Polarization.pol0);
        FoVCalculatorUtil.checkFoVBoxIsInImageBoundary(_pol45, pol45_fov, Polarization.pol45);
        FoVCalculatorUtil.checkFoVBoxIsInImageBoundary(_pol90, pol90_fov, Polarization.pol90);
        FoVCalculatorUtil.checkFoVBoxIsInImageBoundary(_pol135, pol135_fov, Polarization.pol135);

        return new FieldOfView(pol0_fov, pol45_fov, pol90_fov, pol135_fov);
    }

}