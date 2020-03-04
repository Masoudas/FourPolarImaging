package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation.MatrixBasedInverseOpticalPropagationCalculator;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.polarization.PolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;

public class IntensityToOrientationConverterTest {
    private static IIntensityToOrientationConverter _converter;

    @BeforeAll
    public static void setInversePropagation() throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        Channel channel = new Channel(520, 1, 0.7, 1, 0.7);
        NumericalAperture na = new NumericalAperture(1.45, 1.015, 1.45, 1.015);

        OpticalPropagation opticalPropagation = new OpticalPropagation(channel, na);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 4.423);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.551);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 3.406);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.551);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 4.423);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 3.406);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 0.304);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 1.617);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 0.818);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 0.304);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -1.617);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();
        _converter = new IntensityToOrientationConverter(inverseCalculator.getInverse(opticalPropagation));
    }

    private static boolean _checkPrecision(double val1, double val2, double error) {
        return Math.abs(val1 - val2) < error;        
    }

    @Test
    public void convert_BenchMark() {
        IPolarizationsIntensity intensity = new PolarizationsIntensity(1, 0, 0, 0);

        for (int i = 0; i < 1000000; i++) {
            _converter.convert(intensity);
        }

    }

    @Test
    public void convert_Pol0UnitIntensity_Rhois0Deltais0Etais0() {
        IPolarizationsIntensity intensity = new PolarizationsIntensity(1, 0, 0, 0);

        IOrientationVector vector = _converter.convert(intensity);

        assertTrue(
            vector.getAngle(OrientationAngle.rho) == 0 && vector.getAngle(OrientationAngle.delta) == 0
            && vector.getAngle(OrientationAngle.eta) == 0);
    }

    @Test
    public void convert_Pol45UnitIntensity_Rhois45Deltais0Etais0() {
        IPolarizationsIntensity intensity = new PolarizationsIntensity(0, 1, 0, 0);

        IOrientationVector vector = _converter.convert(intensity);

        assertTrue(
            _checkPrecision(vector.getAngle(OrientationAngle.rho), 45d / 180d * Math.PI, 1e-4)
            && _checkPrecision(vector.getAngle(OrientationAngle.delta), 0, 1e-4)
            && _checkPrecision(vector.getAngle(OrientationAngle.eta), 0, 1e-4));
    }

    @Test
    public void convert_Pol90UnitIntensity_Rhois90Deltais0Etais0() {
        IPolarizationsIntensity intensity = new PolarizationsIntensity(0, 0, 1, 0);

        IOrientationVector vector = _converter.convert(intensity);

        assertTrue(
            _checkPrecision(vector.getAngle(OrientationAngle.rho), 90d / 180d * Math.PI, 1e-4)
            && _checkPrecision(vector.getAngle(OrientationAngle.delta), 0, 1e-4)
            && _checkPrecision(vector.getAngle(OrientationAngle.eta), 0, 1e-4));
    }

    @Test
    public void convert_Pol135UnitIntensity_Rhois135Deltais0Etais0() {
        IPolarizationsIntensity intensity = new PolarizationsIntensity(0, 0, 0, 1);

        IOrientationVector vector = _converter.convert(intensity);

        assertTrue(
            _checkPrecision(vector.getAngle(OrientationAngle.rho), 135d / 180d * Math.PI, 1e-4)
            && _checkPrecision(vector.getAngle(OrientationAngle.delta), 0, 1e-4)
            && _checkPrecision(vector.getAngle(OrientationAngle.eta), 0, 1e-4));
    }

}