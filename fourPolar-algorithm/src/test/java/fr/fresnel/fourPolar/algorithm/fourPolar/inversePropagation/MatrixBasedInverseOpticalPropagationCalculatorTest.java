package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class MatrixBasedInverseOpticalPropagationCalculatorTest {

    @Test
    public void getInverseFactor_BrasseletCurcioPaperMatrix_InverseEqualsPrecalculatedValuesTo3Digits()
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IChannel channel = new Channel(520, 1, 0.7, 1, 0.7);
        INumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);
        
        IOpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 4.423);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.551);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 3.406);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0);
                
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.551);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 4.423);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 3.406);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 0.304);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 1.617);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 0.304);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -1.617);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        // Brasselet-Curcio matrix results.    
        double ixx_0 = 0.092;
        double ixx_45 = 0.419;
        double ixx_90 = -0.167;
        double ixx_135 = 0.419;
        double iyy_0 = -0.167; 
        double iyy_45 = 0.419;
        double iyy_90 = 0.092;
        double iyy_135 = 0.419;
        double izz_0 = 0.201;
        double izz_45 = -0.613;
        double izz_90 = 0.201;
        double izz_135 = -0.613; 
        double ixy_0 = 0;
        double ixy_45 = 0.309;
        double ixy_90 = 0;
        double ixy_135 = -0.309;

        IInverseOpticalPropagation iPropagation = inverseCalculator.getInverse(opticalPropagation);

        assertTrue( 
            ixx_0 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX), 3) &&
            ixx_45 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX), 3) &&
            ixx_90 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX), 3) &&
            ixx_135 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX), 3) &&
            iyy_0 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY), 3) &&
            iyy_45 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY), 3) &&
            iyy_90 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY), 3) &&
            iyy_135 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY), 3) &&
            izz_0 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ), 3) &&
            izz_45 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ), 3) &&
            izz_90 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ), 3) &&
            izz_135 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ), 3) &&
            ixy_0 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY), 3) &&
            ixy_45 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY), 3) &&
            ixy_90 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY), 3) &&
            ixy_135 == Precision.round(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY), 3));        
    }

    @Test
    public void getInverseFactor_MatlabCodes_InverseEqualsPrecalculatedValuesTo3Digits()
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IChannel channel = new Channel(520, 1, 0.7, 1, 0.7);
        INumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);
        
        IOpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 1.7262);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.0120);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 0.3482);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0.0);
                
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.0120);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 1.726);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 0.348);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0.0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 1.559);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 2.970);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 1.559);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -2.970);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        // Matlab code matrix results.    
        double ixx_0 = 0.788;
        double ixx_45 = -0.110;
        double ixx_90 = 0.204;
        double ixx_135 = -0.1108;
        double iyy_0 = 0.204;
        double iyy_45 = -0.110;
        double iyy_90 = 0.788;
        double iyy_135 = -0.110;
        double izz_0 = -1.042;
        double izz_45 = 0.553;
        double izz_90 = -1.042;
        double izz_135 = 0.553; 
        double ixy_0 = 0;
        double ixy_45 = 0.168;
        double ixy_90 = 0;
        double ixy_135 = -0.168;

        IInverseOpticalPropagation iPropagation = inverseCalculator.getInverse(opticalPropagation);

        double acceptedError = 1e-3;
        assertTrue( 
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX), ixx_0, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX), ixx_45, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX), ixx_90, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX), ixx_135, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY), iyy_0, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY), iyy_45, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY), iyy_90, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY), iyy_135, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ), izz_0, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ), izz_45, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ), izz_90, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ), izz_135, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY), ixy_0, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY), ixy_45, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY), ixy_90, acceptedError) &&
            _checkPrecision(
                iPropagation.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY), ixy_135, acceptedError));        
    }

    @Test
    public void getInverseFactor_SingularPropagationMatrix_ThrowsOpticalPropagationNotInvertible()
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IChannel channel = new Channel(520, 1, 0.7, 1, 0.7);
        INumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);
        
        IOpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0);
                
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, 0);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        assertThrows(
            OpticalPropagationNotInvertible.class, ()->{inverseCalculator.getInverse(opticalPropagation);});
        
    } 
    
    @Test
    public void getInverseFactor_IncompletePropagationMatrix_ThrowsPropagationFactorNotFound()
        throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IChannel channel = new Channel(520, 1, 0.7, 1, 0.7);
        INumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);
        
        IOpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);
        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        assertThrows(
            PropagationFactorNotFound.class, ()->{inverseCalculator.getInverse(opticalPropagation);});
    }

    private static boolean _checkPrecision(double val1, double val2, double error) {
        return Math.abs(val1 - val2) < error;
    }

}