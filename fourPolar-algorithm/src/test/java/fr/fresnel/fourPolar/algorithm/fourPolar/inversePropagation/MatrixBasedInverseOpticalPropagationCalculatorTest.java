package fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class MatrixBasedInverseOpticalPropagationCalculatorTest {

    @Test
    public void getInverseFactor_BrasseletCurcioPaperMatrix_InverseEqualsPrecalculatedValuesTo3Digits()
            throws PropagationFactorNotFound {
        IChannel channel = new Channel(520, 1, 0.7, 1, 0.7);
        INumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);
        
        OpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 4.423);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 0.551);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 0.818);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.551);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 4.423);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 0.818);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 3.406);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 3.406);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 0.304);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 0.304);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 1.617);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -1.617);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator(
            opticalPropagation);

        double ixx_0 = 0.092;
        double ixx_45 = 0.419;
        double ixx_90 = -0.166;
        double ixx_135 = 0.419;
        double iyy_0 = -0.166; 
        double iyy_45 = 0.419;
        double iyy_90 = 0.092;
        double iyy_135 = 0.419;
        double izz_0 = 0.201;
        double izz_45 = -0.612;
        double izz_90 = 0.201;
        double izz_135 = -0.612; 
        double ixy_0 = 0;
        double ixy_45 = 0.309;
        double ixy_90 = 0;
        double ixy_135 = -0.309;

        assertTrue( ixx_0 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX), 3) &&
            ixx_45 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX), 3) &&
            ixx_90 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX), 3) &&
            ixx_135 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX), 3) &&
            iyy_0 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY), 3) &&
            iyy_45 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY), 3) &&
            iyy_90 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY), 3) &&
            iyy_135 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY), 3) &&
            izz_0 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ), 3) &&
            izz_45 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ), 3) &&
            izz_90 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ), 3) &&
            izz_135 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ), 3) &&
            ixy_0 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY), 3) &&
            ixy_45 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY), 3) &&
            ixy_90 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY), 3) &&
            ixy_135 == Precision.round(inverseCalculator.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY), 3));

        
    }

}