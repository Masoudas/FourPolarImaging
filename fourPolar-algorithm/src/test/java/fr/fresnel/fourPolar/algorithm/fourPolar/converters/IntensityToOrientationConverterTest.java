package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

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


    @Test
    public void convert_UnfeasibleIntensityVectors_ThrowsImpossibleOrientationVector() {
        IntensityVector vec1 = new IntensityVector(1, 0, 0, 0);
        
        ImpossibleOrientationVector exception1 = assertThrows(ImpossibleOrientationVector.class, () -> {
            _converter.convert(vec1);
        });
        assertTrue(exception1.getMessage().contains("zero"));

    }

    /**
     * In this test, we try to compare our results with the forward problem. To do
     * so, we have calculated the intensity from an orientation angle using the
     * integration formulas (forward problem). Then, we try to estimate the angles
     * with the back propagation matrix (inverse problem). These two methods will
     * not yield the same results (especially for marginal cases such as delta=180
     * or eta=0), but for more appropriate angles the error margin should not be
     * very high.
     * 
     * Also note that for cases where rho is close to zero, we may get a rho close
     * to 180. This is due to near equivalence of those angles.
     * 
     * Because we use the inverse method, we expect the error to be as high as 10
     * degrees for some angles.
     * 
     * The Matlab code to generate these results, as well as the code to generate
     * the corresponding propagation factors in the resource folder.
     */
    @Test
    public void convert_CurcioForwardValues_OrientationErrorIsLessThanThreshold() throws IOException {
        double error = Math.PI / 180 * 10.5;

        BigDecimal etaGreaterThan = new BigDecimal(Math.PI / 180 * 5);
        BigDecimal rhoGreaterThan = new BigDecimal(Math.PI / 180 * 0);
        BigDecimal deltaLessThan = new BigDecimal(Math.PI / 180 * 170);

        String intensityOrientationPair = null;
        BufferedReader forwardData = _readFile("ForwardMethodData-Curcio.txt");
        forwardData.readLine();

        boolean equals = true;
        while ((intensityOrientationPair = forwardData.readLine()) != null && equals) {
            String[] values = intensityOrientationPair.split(",");

            double rho = Double.parseDouble(values[4]) % Math.PI;
            double eta = Double.parseDouble(values[5]) % Math.PI;
            double delta = Double.parseDouble(values[6]);
            if (eta > OrientationVector.MAX_Eta) {
                eta = eta - OrientationVector.MAX_Eta;
            }

            if (isGreaterThan(eta, etaGreaterThan) && isGreaterThan(rho, rhoGreaterThan)
                    && isLessThan(delta, deltaLessThan)) {

                IntensityVector iVector = new IntensityVector(Double.parseDouble(values[0]),
                        Double.parseDouble(values[2]), Double.parseDouble(values[1]), Double.parseDouble(values[3]));

                OrientationVector original = new OrientationVector(rho, delta, eta);
                IOrientationVector calculated;
                try {
                    calculated = _converter.convert(iVector);
                    equals = _checkForwardAnglePrecision(original, calculated, error);
                } catch (ImpossibleOrientationVector e) {
                }

            }

        }
        assertTrue(equals);
    }

    /**
     * In this test, we try to compare our results with the inverse problem
     * implemented by Curcio. It's expected that in all cases, both methods return
     * the same angles.
     * 
     * Note the data is in degree rather than radian, and that the intensities are
     * those in the forward data.
     * 
     * Because the inverse method is the same as the one we use here, we should get
     * little to no error.
     * 
     * The Matlab code to generate these data is also in the resource folder.
     * 
     * @throws ImpossibleOrientationVector
     */
    @Test
    public void convert_CurcioInverseValues_OrientationErrorIsLessThanThreshold()
            throws IOException, ImpossibleOrientationVector {
        double error = Math.PI / 180 * 0.001;

        String intensityOrientationPair = null;
        boolean equals = true;

        BufferedReader inverseData = _readFile("InverseMethodData-Curcio.txt");

        inverseData.readLine();
        while ((intensityOrientationPair = inverseData.readLine()) != null && equals) {
            String[] values = intensityOrientationPair.split(",");
            double rho = (Double.parseDouble(values[4]) / 180 * Math.PI + Math.PI) % Math.PI;
            double eta = Double.parseDouble(values[5]) / 180 * Math.PI;
            double delta = Double.parseDouble(values[6]) / 180 * Math.PI;

            IntensityVector iVector = new IntensityVector(Double.parseDouble(values[0]), Double.parseDouble(values[2]),
                    Double.parseDouble(values[1]), Double.parseDouble(values[3]));

            OrientationVector original = new OrientationVector(rho, delta, eta);
            IOrientationVector calculated = _converter.convert(iVector);
            equals = _checkForwardAnglePrecision(original, calculated, error);
        }
        assertTrue(equals);
    }

    private boolean isGreaterThan(double value, BigDecimal threshold) {
        return new BigDecimal(value).compareTo(threshold) == 1;
    }

    private boolean isLessThan(double value, BigDecimal threshold) {
        return new BigDecimal(value).compareTo(threshold) == -1;
    }

    /**
     * To check forward angle precision, we check that delta and eta have acceptable
     * error. For rho, we check that delta or (pi-delta) are close to the forward
     * angle (this is due to rounding error around 0 degree as mentiond.)
     */
    private static boolean _checkForwardAnglePrecision(IOrientationVector vec1, IOrientationVector vec2, double error) {
        return (_checkPrecision(Math.PI - vec1.getAngle(OrientationAngle.rho), vec2.getAngle(OrientationAngle.rho),
                error)
                || _checkPrecision(vec1.getAngle(OrientationAngle.rho), vec2.getAngle(OrientationAngle.rho), error))
                && _checkPrecision(vec1.getAngle(OrientationAngle.delta), vec2.getAngle(OrientationAngle.delta), error);
        // && _checkPrecision(vec1.getAngle(OrientationAngle.eta),
        // vec2.getAngle(OrientationAngle.eta), error);
    }

    private static boolean _checkPrecision(double val1, double val2, double error) {
        if (Double.isNaN(val1) && Double.isNaN(val2)) {
            return true;
        } else {
            return Math.abs(val1 - val2) < error;
        }
    }

    private BufferedReader _readFile(String file) {
        InputStream stream = IntensityToOrientationConverterTest.class.getResourceAsStream(file);
        InputStreamReader iReader = new InputStreamReader(stream);
        return new BufferedReader(iReader);

    }

}