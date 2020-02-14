package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import java.util.Hashtable;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;

/**
 * This class is used for calculating the inverse propagation factors, by
 * forming the matrix representation of optical propagation.
 */
public class MatrixBasedInverseOpticalPropagationCalculator implements IInverseOpticalPropagationCalculator {
    private final IOpticalPropagation _opticalProp;
    private Hashtable<String, Double> _ipropFact;

    public MatrixBasedInverseOpticalPropagationCalculator(IOpticalPropagation opticalPropagation)
            throws PropagationFactorNotFound {
        _opticalProp = opticalPropagation;
        _ipropFact = new Hashtable<String, Double>(16);

        _calculateInverseFactors();
    }

    @Override
    public double getInverseFactor(Polarization polarization, DipoleSquaredComponent component) {
        return _ipropFact.get(polarization.toString() + component.toString());
    }

    /**
     * Calculates the inverse propagation factors by converting to the matrix format
     * and calculating the inverse matrix.
     * 
     * @throws PropagationFactorNotFound
     */
    private void _calculateInverseFactors() throws PropagationFactorNotFound {
        double[][] matrixForm = _toMatrixForm();

        double[][] inversePropMatrix = _calculateMatrixInverse(matrixForm);

        _fromMatrixForm(inversePropMatrix);
    }

    /**
     * Form the propagation matrix as follows: [[XX_0, YY_0, ZZ_0, XY_0], [XX_90,
     * YY_90, ZZ_90, XY_90] [XX_45, YY_45, ZZ_45, XY_45] [XX_135, YY_135, ZZ_135,
     * XY_135]]
     * 
     * @return
     * @throws PropagationFactorNotFound
     */
    private double[][] _toMatrixForm() throws PropagationFactorNotFound {
        double[][] matrixForm = new double[4][4];

        matrixForm[0][0] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0);
        matrixForm[0][1] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0);
        matrixForm[0][2] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0);
        matrixForm[0][3] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0);

        matrixForm[1][0] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90);
        matrixForm[1][1] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90);
        matrixForm[1][2] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90);
        matrixForm[1][3] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90);

        matrixForm[2][0] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45);
        matrixForm[2][1] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45);
        matrixForm[2][2] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45);
        matrixForm[2][3] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45);

        matrixForm[3][0] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135);
        matrixForm[3][1] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135);
        matrixForm[3][2] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135);
        matrixForm[3][3] = _opticalProp.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135);
        
        return matrixForm;

    }

    /**
     * Calculates the inverse of the matrix.
     * @param matrix
     * @return
     */
    private double[][] _calculateMatrixInverse(double[][] matrix) {
        RealMatrix rmat = new Array2DRowRealMatrix(matrix);

        return MatrixUtils.inverse(rmat).getData();
    }

    private void _fromMatrixForm(double[][] matrixForm) {
        _setInverseFactor(DipoleSquaredComponent.XX, Polarization.pol0, matrixForm[0][0]);
        _setInverseFactor(DipoleSquaredComponent.XX, Polarization.pol90, matrixForm[0][1]);
        _setInverseFactor(DipoleSquaredComponent.XX, Polarization.pol45, matrixForm[0][2]);
        _setInverseFactor(DipoleSquaredComponent.XX, Polarization.pol135, matrixForm[0][3]);

        _setInverseFactor(DipoleSquaredComponent.YY, Polarization.pol0, matrixForm[1][0]);
        _setInverseFactor(DipoleSquaredComponent.YY, Polarization.pol90, matrixForm[1][1]);
        _setInverseFactor(DipoleSquaredComponent.YY, Polarization.pol45, matrixForm[1][2]);
        _setInverseFactor(DipoleSquaredComponent.YY, Polarization.pol135, matrixForm[1][3]);
        
        
        _setInverseFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, matrixForm[2][0]);
        _setInverseFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, matrixForm[2][1]);
        _setInverseFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, matrixForm[2][2]);
        _setInverseFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, matrixForm[2][3]);
        
        
        _setInverseFactor(DipoleSquaredComponent.XY, Polarization.pol0, matrixForm[3][0]);
        _setInverseFactor(DipoleSquaredComponent.XY, Polarization.pol90, matrixForm[3][1]);
        _setInverseFactor(DipoleSquaredComponent.XY, Polarization.pol45, matrixForm[3][2]);
        _setInverseFactor(DipoleSquaredComponent.XY, Polarization.pol135, matrixForm[3][3]);
    }

    private void _setInverseFactor(DipoleSquaredComponent component, Polarization polarization, double factor) {
        _ipropFact.put(polarization.toString() + component.toString(), factor);
    }

}