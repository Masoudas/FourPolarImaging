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
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
import fr.fresnel.fourPolar.core.physics.propagation.InverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class IntensityToOrientationConverterTest {
    private static IIntensityToOrientationConverter _converter;

    @BeforeAll
    public static void setInversePropagation() throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        // Channel and NA parameters are irrelevant to these tests.
        Channel channel = new Channel(520e-9, 1, 0.7, 1, 0.7);
        NumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);

        OpticalPropagation optPropagation = new OpticalPropagation(channel, na);
        InverseOpticalPropagation inverseProp = new InverseOpticalPropagation(optPropagation);

        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX, 0.7880897720);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY, 0.20470751910);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ, -1.0419630892);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY, 0);

        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX, 0.20470751910);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY, 0.78808977204);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ, -1.0419630892);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY, 0.0);

        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX, -0.1108420459);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY, -0.1108420459);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ, 0.55323020559);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY, 0.16834141369);

        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX, -0.11084204599);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY, -0.11084204599);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ, 0.553230205596);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY, -0.16834141369);

        _converter = new IntensityToOrientationConverter(inverseProp);
    }

    private static boolean _checkPrecision(double val1, double val2, double error) {
        return Math.abs(val1 - val2) < error;
    }

    @Test
    public void convert_BenchMark() throws ImpossibleOrientationVector {
        IntensityVector intensity = new IntensityVector(1, 1, 1, 1);

        IOrientationVector vector = null;
        for (int i = 0; i < 1000000; i++) {
            vector = _converter.convert(intensity);
        }

        System.out.println(vector.getAngle(OrientationAngle.eta));
    }

    @Test
    public void convert_UnfeasibleIntensityVectors_ThrowsImpossibleOrientationVector() {
        IntensityVector vec1 = new IntensityVector(1, 0, 0, 0);
        IntensityVector vec2 = new IntensityVector(0, 1, 0, 0);
        IntensityVector vec3 = new IntensityVector(0, 0, 1, 0);
        IntensityVector vec4 = new IntensityVector(0, 0, 0, 1);
        IntensityVector vec5 = new IntensityVector(1, 0, 0, 1);
        IntensityVector vec6 = new IntensityVector(0, 0, 0, 0);

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

        assertThrows(
            ImpossibleOrientationVector.class, ()->{_converter.convert(vec6);});

    }
}