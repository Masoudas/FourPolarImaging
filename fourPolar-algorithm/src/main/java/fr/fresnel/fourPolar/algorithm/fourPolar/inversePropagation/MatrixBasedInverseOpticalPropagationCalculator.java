package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.InverseOpticalPropagation;

/**
 * This class is used for calculating the inverse propagation factors, by
 * forming the matrix representation of optical propagation.
 */
public class MatrixBasedInverseOpticalPropagationCalculator implements IInverseOpticalPropagationCalculator {
    @Override
    public IInverseOpticalPropagation getInverse(IOpticalPropagation opticalPropagation)
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IInverseOpticalPropagation iProp = new InverseOpticalPropagation(opticalPropagation);

        double[][] propagationMatrix = _formPropagationMatrix(opticalPropagation);

        double[][] inversePropMatrix = _calculateMatrixInverse(propagationMatrix);

        _setInverseFactorsFromMatrix(inversePropMatrix, iProp);
        return iProp; 
    }

    /**
     * Form the propagation matrix as follows: [[XX_0, YY_0, ZZ_0, XY_0], [XX_90,
     * YY_90, ZZ_90, XY_90] [XX_45, YY_45, ZZ_45, XY_45] [XX_135, YY_135, ZZ_135,
     * XY_135]]
     * 
     * @return
     * @throws PropagationFactorNotFound
     */
    private double[][] _formPropagationMatrix(IOpticalPropagation opticalPropagation) throws PropagationFactorNotFound {
        double[][] matrixForm = new double[4][4];

        matrixForm[0][0] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0);
        matrixForm[0][1] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0);
        matrixForm[0][2] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0);
        matrixForm[0][3] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0);

        matrixForm[1][0] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90);
        matrixForm[1][1] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90);
        matrixForm[1][2] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90);
        matrixForm[1][3] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90);

        matrixForm[2][0] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45);
        matrixForm[2][1] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45);
        matrixForm[2][2] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45);
        matrixForm[2][3] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45);

        matrixForm[3][0] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135);
        matrixForm[3][1] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135);
        matrixForm[3][2] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135);
        matrixForm[3][3] = opticalPropagation.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135);

        return matrixForm;

    }

    /**
     * Calculates the inverse of the matrix.
     * 
     * @param matrix
     * @return
     * @throws OpticalPropagationNotInvertible
     */
    private double[][] _calculateMatrixInverse(double[][] matrix) throws OpticalPropagationNotInvertible {
        RealMatrix rmat = new Array2DRowRealMatrix(matrix);

        try {
            return MatrixUtils.inverse(rmat).getData();
        } catch (SingularMatrixException e) {
            throw new OpticalPropagationNotInvertible();
        }

    }

    /**
     * Sets the inverse propagation factors from the inverse propagation matrix.
     * 
     * @param matrixForm
     * @param iProp
     */
    private void _setInverseFactorsFromMatrix(double[][] matrixForm, IInverseOpticalPropagation iProp) {
        iProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX, matrixForm[0][0]);
        iProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX, matrixForm[0][1]);
        iProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX, matrixForm[0][2]);
        iProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX, matrixForm[0][3]);

        iProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY, matrixForm[1][0]);
        iProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY, matrixForm[1][1]);
        iProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY, matrixForm[1][2]);
        iProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY, matrixForm[1][3]);

        iProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ, matrixForm[2][0]);
        iProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ, matrixForm[2][1]);
        iProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ, matrixForm[2][2]);
        iProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ, matrixForm[2][3]);

        iProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY, matrixForm[3][0]);
        iProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY, matrixForm[3][1]);
        iProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY, matrixForm[3][2]);
        iProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY, matrixForm[3][3]);
    }

}