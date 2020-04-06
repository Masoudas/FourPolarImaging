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

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 4.422973044436246);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.551215121520568);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 3.405602990940682);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, -0.0000000000000);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.551215121520568);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 4.422973355351169);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 3.405602990940375);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, -0.000000000000000);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 0.304192988437901);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 1.617234768778063);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 0.818345815444442);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 0.304192988437901);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -1.617234768778063);

        _converter = new OrientationToIntensityConverter(opticalPropagation);
    }

    /**
     * Checks that the absolute difference of each component of intensity vector is
     * less than threshold.
     */
    private static boolean _checkIntensityPrecision(IntensityVector vec1, IntensityVector vec2, double error) {
        return _checkPrecision(vec1.getIntensity(Polarization.pol0), vec2.getIntensity(Polarization.pol0), error)
                && _checkPrecision(vec1.getIntensity(Polarization.pol45), vec2.getIntensity(Polarization.pol45), error)
                && _checkPrecision(vec1.getIntensity(Polarization.pol90), vec2.getIntensity(Polarization.pol90), error)
                && _checkPrecision(vec1.getIntensity(Polarization.pol135), vec2.getIntensity(Polarization.pol135),
                        error);

    }

    /** Checks that the ratio of each intensity is less than threshold. */
    private static boolean _checkRatioPrecision(IntensityVector vec1, IntensityVector vec2, double error) {
        double ratio0 = vec1.getIntensity(Polarization.pol0) / vec2.getIntensity(Polarization.pol0);
        double ratio45 = vec1.getIntensity(Polarization.pol45) / vec2.getIntensity(Polarization.pol45);
        double ratio90 = vec1.getIntensity(Polarization.pol90) / vec2.getIntensity(Polarization.pol90);
        double ratio135 = vec1.getIntensity(Polarization.pol135) / vec2.getIntensity(Polarization.pol135);

        return _checkPrecision(ratio0, ratio45, error) && _checkPrecision(ratio0, ratio90, error)
                && _checkPrecision(ratio0, ratio90, error) && _checkPrecision(ratio0, ratio135, error);

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
        double delta = OrientationVector.MAX_Delta;
        double angleStep = Math.PI / 180;

        IOrientationVector baseVector = new OrientationVector(0f, delta, 0f);
        IntensityVector baseIntensity = _converter.convert(baseVector);

        boolean equals = true;
        for (double rho = 0; rho < OrientationVector.MAX_Rho; rho += angleStep) {
            for (double eta = 0; eta < OrientationVector.MAX_Eta && equals; eta += angleStep) {
                IOrientationVector vector = new OrientationVector(rho, delta, eta);
                IntensityVector intensity = _converter.convert(vector);

                equals &= _checkIntensityPrecision(intensity, baseIntensity, 1e-4);
            }
        }

        assertTrue(equals);
    }

    @Test
    public void convert_Eta0_ForOneDeltaReturnsSameIntensityForAllRho() {
        double eta = 0f;
        double angleStep = Math.PI / 180;

        boolean equals = true;
        for (double delta = 0; delta < OrientationVector.MAX_Delta; delta += angleStep) {
            IOrientationVector baseVector = new OrientationVector(0f, delta, eta);
            IntensityVector baseIntensity = _converter.convert(baseVector);

            for (double rho = 0; rho < OrientationVector.MAX_Rho && equals; rho += angleStep) {
                IOrientationVector vector = new OrientationVector(rho, delta, eta);
                IntensityVector intensity = _converter.convert(vector);

                equals &= _checkIntensityPrecision(intensity, baseIntensity, 1e-4);
            }
        }

        assertTrue(equals);

    }

    /**
     * In this test, we try to compare our results with the forward problem. To do
     * so, we have calculated the intensity from an orientation angle using the
     * integration formulas (forward problem). Then, we try to estimate the intensity
     * from the angles with the propagation matrix. These two methods will
     * not yield the same results 
     * 
     */
    @Test
    public void convert_BrasseletCurcioForwardValues_IntensityRatioDifferenceIsLessThanThreshold() {
        double error = 2;
        try (InputStream stream = OrientationToIntensityConverterTest.class
                .getResourceAsStream("forwardMethodData.txt");) {
            InputStreamReader iReader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(iReader); // Now this baby actually

            String intensityOrientationPair = null;
            boolean equals = true;
            do {
                intensityOrientationPair = buffer.readLine();

                String[] values = intensityOrientationPair.split(",");

                OrientationVector oVector = new OrientationVector(Double.parseDouble(values[4]),
                        Double.parseDouble(values[6]), Double.parseDouble(values[5]));

                IntensityVector original = new IntensityVector(Double.parseDouble(values[0]),
                        Double.parseDouble(values[2]), Double.parseDouble(values[1]), Double.parseDouble(values[3]));

                IntensityVector calculated = _converter.convert(oVector);

                equals &= _checkRatioPrecision(original, calculated, error);

            } while (intensityOrientationPair != null && equals);

            assertTrue(equals);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

}