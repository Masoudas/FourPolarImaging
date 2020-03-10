package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.polarization.PolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.propagation.InverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class IntensityToOrientationConverterTest {
    private static IIntensityToOrientationConverter _converter;

    @BeforeAll
    public static void setInversePropagation() throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        // Channel and NA parameters are irrelevant to these tests.
        Channel channel = new Channel(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        NumericalAperture na = new NumericalAperture(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

        OpticalPropagation optPropagation = new OpticalPropagation(channel, na);
        InverseOpticalPropagation inverseProp = new InverseOpticalPropagation(optPropagation);

        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX, 0.788);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY, 0.204);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ, -1.042);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY, 0);

        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX, 0.204);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY, 0.788);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ, -1.042);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY, 0.0);

        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX, -0.110);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY, -0.110);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ, 0.553);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY, 0.168);

        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX, -0.110);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY, -0.110);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ, 0.553);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY, -0.168);

        _converter = new IntensityToOrientationConverter(inverseProp);
    }

    private static boolean _checkPrecision(double val1, double val2, double error) {
        return Math.abs(val1 - val2) < error;
    }

    @Test
    public void convert_BenchMark() throws ImpossibleOrientationVector {
        IPolarizationsIntensity intensity = new PolarizationsIntensity(3.163, 0.439, 3.163, 0.439);

        IOrientationVector vector = null;
        for (int i = 0; i < 1000000; i++) {
            vector = _converter.convert(intensity);
        }

        System.out.println(vector.getAngle(OrientationAngle.eta));
        

    }

    @Test
    public void convert_UnfeasibleIntensityVectors_ThrowsImpossibleOrientationVector() {
        IPolarizationsIntensity vec1 = new PolarizationsIntensity(1, 0, 0, 0);
        IPolarizationsIntensity vec2 = new PolarizationsIntensity(0, 1, 0, 0);
        IPolarizationsIntensity vec3 = new PolarizationsIntensity(0, 0, 1, 0);
        IPolarizationsIntensity vec4 = new PolarizationsIntensity(0, 0, 0, 1);
        IPolarizationsIntensity vec5 = new PolarizationsIntensity(1, 0, 0, 1);

        assertThrows(
            ImpossibleOrientationVector.class, ()->{_converter.convert(vec1);});

        assertThrows(
                ImpossibleOrientationVector.class, ()->{_converter.convert(vec2);});            
        
        assertThrows(
            ImpossibleOrientationVector.class, ()->{_converter.convert(vec3);});
        
        assertThrows(
            ImpossibleOrientationVector.class, ()->{_converter.convert(vec4);});

        assertThrows(
            ImpossibleOrientationVector.class, ()->{_converter.convert(vec5);});
    }
}