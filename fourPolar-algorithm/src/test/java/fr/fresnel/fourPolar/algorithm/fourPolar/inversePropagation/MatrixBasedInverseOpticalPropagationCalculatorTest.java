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
import fr.fresnel.fourPolar.core.physics.propagation.InverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class MatrixBasedInverseOpticalPropagationCalculatorTest {
    double acceptedError = 1e-12;

    @Test
    public void getInverseFactor_MatlabCalculatedInverse_CalculatedMatrixErrorIsLessThanacceptedError()
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IChannel channel = new Channel(520, 1, 0.7, 1, 0.7);
        INumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);
        
        IOpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 4.422973044436246);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.551215121520568);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 3.405602990940682);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0.0);
                
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.551215121520568 );
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 4.422973355351169);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 3.405602990940375);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0.000000000000000);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 0.304192988437901);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 1.617234768778063);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 0.304192988437901);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -1.617234768778063);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();
        IInverseOpticalPropagation calcInverse = inverseCalculator.getInverse(opticalPropagation);

        // The inverse matrix, calculated using Matlab and hard coded here.
        InverseOpticalPropagation actualInverse = new InverseOpticalPropagation(null);

        actualInverse.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX, 0.091685566886620);
        actualInverse.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX, -0.166595030225210);
        actualInverse.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX, 0.419325727568000);
        actualInverse.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX, 0.419325727568000);

        actualInverse.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY, -0.166595030225194);
        actualInverse.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY, 0.091685572902112);
        actualInverse.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY, 0.419325693894715);
        actualInverse.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY, 0.419325693894715);

        actualInverse.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ, 0.201522875905556);
        actualInverse.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ, 0.201522859722606);
        actualInverse.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ, -0.612462773475724);
        actualInverse.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ, -0.612462773475724);
        
        actualInverse.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY, -0.000000000000000);
        actualInverse.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY, 0.000000000000000);      
        actualInverse.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY, 0.309169707239095);
        actualInverse.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY, -0.309169707239096);

        assertTrue( 
            _checkFactorPrecision(
               Polarization.pol0, DipoleSquaredComponent.XX, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
               Polarization.pol45, DipoleSquaredComponent.XX, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol90, DipoleSquaredComponent.XX, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol135, DipoleSquaredComponent.XX, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol0, DipoleSquaredComponent.YY, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol45, DipoleSquaredComponent.YY, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol90, DipoleSquaredComponent.YY, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol135, DipoleSquaredComponent.YY, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol0, DipoleSquaredComponent.ZZ, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol45, DipoleSquaredComponent.ZZ, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol90, DipoleSquaredComponent.ZZ, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol135, DipoleSquaredComponent.ZZ, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol0, DipoleSquaredComponent.XY, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol45, DipoleSquaredComponent.XY, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol90, DipoleSquaredComponent.XY, actualInverse, calcInverse, acceptedError) &&
            _checkFactorPrecision(
                Polarization.pol135, DipoleSquaredComponent.XY, actualInverse, calcInverse, acceptedError));        
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

    private static boolean _checkFactorPrecision(Polarization pol, DipoleSquaredComponent component, IInverseOpticalPropagation inv1, 
        IInverseOpticalPropagation inv2, double error) {
        double val1 = inv1.getInverseFactor(pol, component);
        double val2 = inv2.getInverseFactor(pol, component);
        return _checkPrecision(val1, val2, error);
    }


    private static boolean _checkPrecision(double val1, double val2, double error) {
        return Math.abs(val1 - val2) < error;
    }

}