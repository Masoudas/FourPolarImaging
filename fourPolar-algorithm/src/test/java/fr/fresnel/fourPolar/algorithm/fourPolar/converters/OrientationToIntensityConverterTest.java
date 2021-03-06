package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;
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
     * Checks that the difference in the ratio of intensities is less than a certain
     * percentage, point being that the two ratios must be as close to each other as
     * possible.
     */
    private static boolean _checkRatioPrecision(IntensityVector vec1, IntensityVector vec2, double error) {
        double one0_90 = vec1.getIntensity(Polarization.pol0) + vec1.getIntensity(Polarization.pol90);
        double one45_135 = vec1.getIntensity(Polarization.pol45) + vec1.getIntensity(Polarization.pol135);

        double two0_90 = vec2.getIntensity(Polarization.pol0) + vec2.getIntensity(Polarization.pol90);
        double two45_135 = vec2.getIntensity(Polarization.pol45) + vec2.getIntensity(Polarization.pol135);

        return Math.abs((one0_90 / two0_90 - one45_135 / two45_135) / (one45_135 / two45_135)) < error;
    }

    @Test
    public void convert_Delta180_ReturnsSameIntensityForAllRhoAndEta() {
        double delta = IOrientationVector.MAX_Delta;
        double angleStep = Math.PI / 180;

        IOrientationVector baseVector = new OrientationVector(0f, delta, 0f);
        IntensityVector baseIntensity = new IntensityVector(0, 0, 0, 0);
        
        IOrientationVector vector = new OrientationVector(0, 0, 0);
        IntensityVector intensity = new IntensityVector(0, 0, 0, 0);

        _converter.convert(baseVector, baseIntensity);

        boolean equals = true;
        for (double rho = 0; rho < IOrientationVector.MAX_Rho; rho += angleStep) {
            for (double eta = 0; eta < IOrientationVector.MAX_Eta && equals; eta += angleStep) {
                vector.setAngles(rho, delta, eta);
                _converter.convert(vector, intensity);
                equals &= _checkRatioPrecision(intensity, baseIntensity, 1e-4);
            }
        }

        assertTrue(equals);
    }

    @Test
    public void convert_Eta0_ForOneDeltaReturnsSameIntensityForAllRho() {
        double eta = 0f;
        double angleStep = Math.PI / 180;

        IOrientationVector baseVector = new OrientationVector(0, 0, 0);
        IntensityVector baseIntensity = new IntensityVector(0, 0, 0, 0);
        
        IOrientationVector vector = new OrientationVector(0, 0, 0);
        IntensityVector intensity = new IntensityVector(0, 0, 0, 0);
        
        
        boolean equals = true;
        for (double delta = 0; delta < IOrientationVector.MAX_Delta; delta += angleStep) {
            baseVector.setAngles(0f, delta, eta);
            _converter.convert(baseVector, baseIntensity);

            for (double rho = 0; rho < IOrientationVector.MAX_Rho && equals; rho += angleStep) {
                vector.setAngles(rho, delta, eta);
                _converter.convert(vector, intensity);
                equals &= _checkRatioPrecision(intensity, baseIntensity, 1e-4);
            }
        }

        assertTrue(equals);

    }

    /**
     * In this test, we try to compare our results with the forward problem. To do
     * so, we have calculated the intensity from an orientation angle using the
     * integration formulas (forward problem). Then, we try to estimate the
     * intensity from the angles with the propagation matrix. These two methods will
     * not yield the same results
     * 
     * @throws IOException
     * @throws NumberFormatException
     * @throws OrientationAngleOutOfRange
     * 
     */
    @Test
    public void convert_CurcioForwardValues_IntensityRatioDifferenceIsLessThanThreshold()
            throws OrientationAngleOutOfRange, NumberFormatException, IOException {
        double error = 0.005;
        BigDecimal etaGreaterThan = new BigDecimal(Math.PI / 180 * 5);
        BigDecimal rhoGreaterThan = new BigDecimal(Math.PI / 180 * 0);
        BigDecimal deltaLessThan = new BigDecimal(Math.PI / 180 * 170);

        BufferedReader forward = _readFile("ForwardMethodData-Curcio.txt");

        forward.readLine(); // Skip comment line.

        OrientationVector oVector = new OrientationVector(0, 0, 0);
        IntensityVector original = new IntensityVector(0, 0, 0, 0);
        IntensityVector calculated = new IntensityVector(0, 0, 0, 0);

        String intensityOrientationPair = null;
        boolean equals = true;
        while ((intensityOrientationPair = forward.readLine()) != null && equals) {
            String[] values = intensityOrientationPair.split(",");

            double rho = Double.parseDouble(values[4]) % Math.PI;
            double eta = Double.parseDouble(values[5]) % Math.PI;
            double delta = Double.parseDouble(values[6]);
            if (eta > IOrientationVector.MAX_Eta) {
                eta = eta - IOrientationVector.MAX_Eta;
            }

            if (isGreaterThan(eta, etaGreaterThan) && isGreaterThan(rho, rhoGreaterThan)
                    && isLessThan(delta, deltaLessThan)) {
                oVector.setAngles(rho, delta, eta);
                original.setIntensity(Double.parseDouble(values[0]), Double.parseDouble(values[2]),
                        Double.parseDouble(values[1]), Double.parseDouble(values[3]));
                _converter.convert(oVector, calculated);

                equals &= _checkRatioPrecision(original, calculated, error);
            }
        }

        assertTrue(equals);
    }

    @Test
    public void convert_CurcioInverseValues_IntensityRatioDifferenceIsLessThanThreshold()
            throws OrientationAngleOutOfRange, NumberFormatException, IOException {
        double error = 0.001;
        BufferedReader inverseData = _readFile("InverseMethodData-Curcio.txt");

        inverseData.readLine(); // Skip comment line.

        OrientationVector oVector = new OrientationVector(0, 0, 0);
        IntensityVector original = new IntensityVector(0, 0, 0, 0);
        IntensityVector calculated = new IntensityVector(0, 0, 0, 0);

        String intensityOrientationPair = null;
        boolean equals = true;
        while ((intensityOrientationPair = inverseData.readLine()) != null && equals) {
            String[] values = intensityOrientationPair.split(",");

            double rho = (Double.parseDouble(values[4]) / 180 * Math.PI + Math.PI) % Math.PI;
            double eta = Double.parseDouble(values[5]) / 180 * Math.PI;
            double delta = Double.parseDouble(values[6]) / 180 * Math.PI;
            if (!Double.isNaN(rho) && !Double.isNaN(delta) && !Double.isNaN(eta)) {
                oVector.setAngles(rho, delta, eta);
                original.setIntensity(Double.parseDouble(values[0]), Double.parseDouble(values[2]),
                        Double.parseDouble(values[1]), Double.parseDouble(values[3]));
                _converter.convert(oVector, calculated);
                equals &= _checkRatioPrecision(original, calculated, error);
            }
        }
        assertTrue(equals);
    }

    private BufferedReader _readFile(String file) {
        InputStream stream = IntensityToOrientationConverterTest.class.getResourceAsStream(file);
        InputStreamReader iReader = new InputStreamReader(stream);
        return new BufferedReader(iReader);
    }

    private boolean isGreaterThan(double value, BigDecimal threshold) {
        return new BigDecimal(value).compareTo(threshold) == 1;
    }

    private boolean isLessThan(double value, BigDecimal threshold) {
        return new BigDecimal(value).compareTo(threshold) == -1;
    }

}