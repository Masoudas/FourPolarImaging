package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
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

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 1.72622242);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.012080463);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 0.348276444);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0.0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.012080463);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 1.726222425);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 0.348276444);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0.0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 1.6369744726);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 1.6369744726);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 1.55973262275);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 2.97015445583);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 1.63697447260);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 1.63697447260);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 1.55973262275);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -2.9701544558);

        _converter = new OrientationToIntensityConverter(opticalPropagation);
    }

    private static boolean _checkIntensityPrecision(IntensityVector vec1, IntensityVector vec2, double error) {
        return _checkPrecision(vec1.getIntensity(Polarization.pol0), vec2.getIntensity(Polarization.pol0), error) &&
        _checkPrecision(vec1.getIntensity(Polarization.pol45), vec2.getIntensity(Polarization.pol45), error) &&
        _checkPrecision(vec1.getIntensity(Polarization.pol90), vec2.getIntensity(Polarization.pol90), error) &&
        _checkPrecision(vec1.getIntensity(Polarization.pol135), vec2.getIntensity(Polarization.pol135), error);

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
    public void convert_Delta180_ReturnsSameIntensityForAllRhoAndEta() {
        float delta = OrientationVector.MAX_Delta;
        float angleStep = (float)Math.PI/180;

        IOrientationVector baseVector = new OrientationVector(0f, delta, 0f);
        IntensityVector baseIntensity = _converter.convert(baseVector);

        boolean equals = true;
        for (float rho = 0; rho < OrientationVector.MAX_Rho; rho += angleStep) {
            for (float eta = 0; eta < OrientationVector.MAX_Eta; eta += angleStep) {
                IOrientationVector vector = new OrientationVector(rho, delta, eta);
                IntensityVector intensity = _converter.convert(vector);

                equals &= 
                    _checkPrecision(
                        intensity.getIntensity(Polarization.pol0), baseIntensity.getIntensity(Polarization.pol0), 1e-4)
                    && _checkPrecision(
                        intensity.getIntensity(Polarization.pol45), baseIntensity.getIntensity(Polarization.pol45), 1e-4)
                    && _checkPrecision(
                        intensity.getIntensity(Polarization.pol90), baseIntensity.getIntensity(Polarization.pol90), 1e-4)
                    && _checkPrecision(
                        intensity.getIntensity(Polarization.pol135), baseIntensity.getIntensity(Polarization.pol135), 1e-4);
            }
        }

        assertTrue(equals);
    }

    @Test
    public void convert_Eta0_ForOneDeltaReturnsSameIntensityForAllRho() {
        float eta = 0f;
        float angleStep = (float)Math.PI/180;

        boolean equals = true;
        for (float delta = 0; delta < OrientationVector.MAX_Delta; delta += angleStep) {
            IOrientationVector baseVector = new OrientationVector(0f, delta, eta);
            IntensityVector baseIntensity = _converter.convert(baseVector);

            for (float rho = 0; rho < OrientationVector.MAX_Rho; rho += angleStep) {
                IOrientationVector vector = new OrientationVector(rho, delta, eta);
                IntensityVector intensity = _converter.convert(vector);

                equals &= 
                    _checkPrecision(
                        intensity.getIntensity(Polarization.pol0), baseIntensity.getIntensity(Polarization.pol0), 1e-4)
                    && _checkPrecision(
                        intensity.getIntensity(Polarization.pol45), baseIntensity.getIntensity(Polarization.pol45), 1e-4)
                    && _checkPrecision(
                        intensity.getIntensity(Polarization.pol90), baseIntensity.getIntensity(Polarization.pol90), 1e-4)
                    && _checkPrecision(
                        intensity.getIntensity(Polarization.pol135), baseIntensity.getIntensity(Polarization.pol135), 1e-4);
            }
        }

        assertTrue(equals);

    }

    /**
     * Note the orientation-intensity pairs used here are calculated using the
     * Matlab program written by Valentina Curcio. The file is in the resource folder.
     */
    @Test
    public void convert_BrasseletCurcioPrecalculatedValues_IntensityDifferenceIsLessThanAThousandth() {
        double error = 1e-4;
        try (InputStream stream = OrientationToIntensityConverterTest.class
                .getResourceAsStream("IntensityOrientation.txt");) {
            InputStreamReader iReader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(iReader); // Now this baby actually

            String intensityOrientationPair = null;
            boolean equals = true;
            do {
                intensityOrientationPair = buffer.readLine();

                String[] values = intensityOrientationPair.split(",");

                OrientationVector oVector = new OrientationVector(Float.parseFloat(values[4]),
                Float.parseFloat(values[5]), Float.parseFloat(values[6]));

                IntensityVector original = new IntensityVector(Double.parseDouble(values[0]),
                        Double.parseDouble(values[2]), Double.parseDouble(values[1]), Double.parseDouble(values[3]));

                IntensityVector calculated = _converter.convert(oVector);

                equals &= _checkIntensityPrecision(original, calculated, error);

            } while (intensityOrientationPair != null);

            assertTrue(equals);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    

}