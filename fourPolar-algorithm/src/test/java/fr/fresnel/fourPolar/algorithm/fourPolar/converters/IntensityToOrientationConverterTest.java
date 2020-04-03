package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
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

        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX, 0.091685566886620);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX, -0.166595030225210);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX, 0.419325727568000);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX, 0.419325727568000);

        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY, -0.166595030225194);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY, 0.091685572902112);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY, 0.419325693894715);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY, 0.419325693894715);
        

        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ, 0.201522875905556);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ, 0.201522859722606);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ, -0.612462773475724);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ, -0.612462773475724);
        
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY, -0.000000000000000);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY, 0.000000000000000);      
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY, 0.309169707239095);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY, -0.309169707239096);

        _converter = new IntensityToOrientationConverter(inverseProp);
    }

    private static boolean _checkAnglePrecision(IOrientationVector vec1, IOrientationVector vec2, double error) {
        return _checkPrecision(vec1.getAngle(OrientationAngle.rho), vec2.getAngle(OrientationAngle.rho), error)
                && _checkPrecision(vec1.getAngle(OrientationAngle.delta), vec2.getAngle(OrientationAngle.delta), error)
                && _checkPrecision(vec1.getAngle(OrientationAngle.eta), vec2.getAngle(OrientationAngle.eta), error);
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

        assertThrows(ImpossibleOrientationVector.class, () -> {
            _converter.convert(vec1);
        });

        assertThrows(ImpossibleOrientationVector.class, () -> {
            _converter.convert(vec2);
        });

        assertThrows(ImpossibleOrientationVector.class, () -> {
            _converter.convert(vec3);
        });

        assertThrows(ImpossibleOrientationVector.class, () -> {
            _converter.convert(vec4);
        });

        assertThrows(ImpossibleOrientationVector.class, () -> {
            _converter.convert(vec5);
        });

        assertThrows(ImpossibleOrientationVector.class, () -> {
            _converter.convert(vec6);
        });

    }

    /**
     * Note the orientation-intensity pairs used here are calculated using the
     * Matlab program written by Valentina Curcio. The file in the resource folder.
     * 
     * @throws IOException
     * @throws ImpossibleOrientationVector
     */ 
    @Test
    public void convert_BrasseletCurcioPrecalculatedValues_OrientationDifferenceIsLessThanHundredthOfDegree()
            throws IOException, ImpossibleOrientationVector {
        double error = Math.PI / 180 * 0.01;
        InputStream stream = IntensityToOrientationConverterTest.class
                .getResourceAsStream("inverse_YanAxelrod-NA_1.45-epi.txt");
        InputStreamReader iReader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(iReader); 

        String intensityOrientationPair = null;
        boolean equals = true; 
        do {
            intensityOrientationPair = buffer.readLine();

            String[] values = intensityOrientationPair.split(",");

            IntensityVector iVector = new IntensityVector(
                Double.parseDouble(values[0]), Double.parseDouble(values[2]),
                Double.parseDouble(values[1]), Double.parseDouble(values[3]));

            OrientationVector original = new OrientationVector(
                Float.parseFloat(values[4]), Float.parseFloat(values[6]),
                Float.parseFloat(values[5]));

            IOrientationVector calculated = _converter.convert(iVector);

            equals &= _checkAnglePrecision(original, calculated, error);

        } while (intensityOrientationPair != null);

        assertTrue(equals);
    }
}