package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    double acceptedError = 1e-4;

    @Test
    public void getInverseFactor_MatlabCodes_InverseEqualsPrecalculatedValuesTo3Digits()
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IChannel channel = new Channel(520, 1, 0.7, 1, 0.7);
        INumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);
        
        IOpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 1.626963199833505);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.009728431055409);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 0.304192988437901);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0.0);
                
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.009728431055409);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 1.626963199833505);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 0.304192988437901);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0.000000000000000);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 2.487090636400923);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 2.4870903802485626);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 3.405602990940682);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 3.871764178207835);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 2.487090636400922);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 2.487090380248562);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 3.405602990940682 );
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -3.871764178207835);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        // Matlab code matrix results.    
        double ixx_0 = 0.728495194449626;
        double ixx_45 = -0.037454709670348;
        double ixx_90 = 0.110155768106612;
        double ixx_135 = -0.037454709670348;
        double iyy_0 = 0.110155779971448;
        double iyy_45 = -0.037454709670348;
        double iyy_90 = 0.728495182584790;
        double iyy_135 = -0.037454709670348;
        double izz_0 = -0.612461570835762;
        double izz_45 = 0.201522757541622;
        double izz_90 = -0.612461506997745;
        double izz_135 = 0.201522757541623; 
        double ixy_0 = 0;
        double ixy_45 = 0.129140096603570;
        double ixy_90 = 0;
        double ixy_135 = -0.129140096603570;

        IInverseOpticalPropagation iPropagation = inverseCalculator.getInverse(opticalPropagation);

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