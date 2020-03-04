package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class OrientationToIntensityConverterTest {
    private static IOrientationToIntensityConverter _converter;

    @BeforeAll
    private static void _setPropagationMatrix() throws PropagationFactorNotFound {
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

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 0.304);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 1.617);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 0.818);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 0.304);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -1.617);

        _converter = new OrientationToIntensityConverter(opticalPropagation);
    }

    private static boolean _checkPrecision(double val1, double val2, double error) {
        return Math.abs(val1 - val2) < error;        
    }

    @Test
    public void convert_BenchMark() {
        IOrientationVector vector = new OrientationVector(0, 0, 0);

        for (int i = 0; i < 1000000; i++) {
            _converter.convert(vector);
        }
        
    }

    @Test
    public void convert_Rho45Delta0Eta0_Returns() {
        IOrientationVector vector = new OrientationVector((float)(180f/180f * Math.PI), (float)(180f/180f * Math.PI), (float)(90f/180f * Math.PI));

        IPolarizationsIntensity intensity = _converter.convert(vector);

        assertTrue(
            _checkPrecision(intensity.getIntensity(Polarization.pol0), 1, 1e-4) &&
            _checkPrecision(intensity.getIntensity(Polarization.pol45), 0, 1e-4) &&
            _checkPrecision(intensity.getIntensity(Polarization.pol90), 0, 1e-4) &&
            _checkPrecision(intensity.getIntensity(Polarization.pol45), 0, 1e-4)
        );
        
    }


    
}