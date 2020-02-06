package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * This class is used for calculating the inverse propagation factors, by
 * forming the matrix presentation of propagation.
 */
public class MatrixBasedInverseOpticalPropagationCalculator implements IInverseOpticalPropagationCalculator {
    private final IOpticalPropagation _op;

    public MatrixBasedInverseOpticalPropagationCalculator(IOpticalPropagation opticalPropagation) {
        _op = opticalPropagation;
    }

    private void _calculateInverseFactors() throws PropagationFactorNotFound {
        double[][] matrixForm = _getMatrixForm();

    }

    /**
     * Form the propagation matrix as follows: [[XX_0, YY_0, ZZ_0, XY_0], [XX_90,
     * YY_90, ZZ_90, XY_90] [XX_45, YY_45, ZZ_45, XY_45] [XX_135, YY_135, ZZ_135,
     * XY_135]]
     * 
     * @return
     * @throws PropagationFactorNotFound
     */
    private double[][] _getMatrixForm() throws PropagationFactorNotFound {
        double[][] propagationMatrix = new double[4][4];

        propagationMatrix[0][0] = getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0);
        propagationMatrix[0][1] = getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0);
        propagationMatrix[0][2] = getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0);
        propagationMatrix[0][3] = getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0);

        propagationMatrix[1][0] = getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90);
        propagationMatrix[1][1] = getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90);
        propagationMatrix[1][2] = getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90);
        propagationMatrix[1][3] = getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90);

        propagationMatrix[2][0] = getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45);
        propagationMatrix[2][1] = getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45);
        propagationMatrix[2][2] = getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45);
        propagationMatrix[2][3] = getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45);

        propagationMatrix[3][0] = getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135);
        propagationMatrix[3][1] = getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135);
        propagationMatrix[3][2] = getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135);
        propagationMatrix[3][3] = getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135);

        return propagationMatrix;

    }

    @Override
    public double getInverseFactor(Polarization polarization, DipoleSquaredComponent component) {
        // TODO Auto-generated method stub
        return 0;
    }


}