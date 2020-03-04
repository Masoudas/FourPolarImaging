package fr.fresnel.fourPolar.algorithm.fourPolar.converters;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;

/**
 * A concreter implementation of the {@link IIntensityToOrientationConverter}.
 */
public class IntensityToOrientationConverter implements IIntensityToOrientationConverter {
    final private double _iProp_0_xx;
    final private double _iProp_0_yy;
    final private double _iProp_0_zz;
    final private double _iProp_0_xy;

    final private double _iProp_90_xx;
    final private double _iProp_90_yy;
    final private double _iProp_90_zz;
    final private double _iProp_90_xy;

    final private double _iProp_45_xx;
    final private double _iProp_45_yy;
    final private double _iProp_45_zz;
    final private double _iProp_45_xy;

    final private double _iProp_135_xx;
    final private double _iProp_135_yy;
    final private double _iProp_135_zz;
    final private double _iProp_135_xy;

    /**
     * This implementation uses an instance of the
     * {@link IInverseOpticalPropagation} to convert an polarization intensity to a
     * intensity of individual polarizations.
     * 
     * @param inverseProp contains the inverse optical propagation factors.
     */
    public IntensityToOrientationConverter(IInverseOpticalPropagation inverseOpticalProp) {
        _iProp_0_xx = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX);
        _iProp_0_yy = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY);
        _iProp_0_zz = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ);
        _iProp_0_xy = inverseOpticalProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY);

        _iProp_90_xx = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX);
        _iProp_90_yy = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY);
        _iProp_90_zz = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ);
        _iProp_90_xy = inverseOpticalProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY);

        _iProp_45_xx = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX);
        _iProp_45_yy = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY);
        _iProp_45_zz = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ);
        _iProp_45_xy = inverseOpticalProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY);

        _iProp_135_xx = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX);
        _iProp_135_yy = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY);
        _iProp_135_zz = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ);
        _iProp_135_xy = inverseOpticalProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY);
    }

    private float _getRho(double normalizedDipoleSquared_XY, double normalizedDipoleSquared_XYdiff) {
        double raw_Rho = 0.5 * Math.atan2(normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff);

        if (raw_Rho > 0) {
            return (float) raw_Rho;
        } else {
            return (float) (Math.PI + raw_Rho);
        }
    }

    private float _getDelta(double sumNormalizedDipoleSquared) {
        double raw_delta = 2 * Math.acos((Math.sqrt(12 * sumNormalizedDipoleSquared - 3)) / 2);

        if (raw_delta > 0) {
            return (float) raw_delta;
        } else {
            return (float) (Math.PI + raw_delta);
        }
    }

    private float _getEta(double normalizedDipoleSquared_XY, double sumNormalizedDipoleSquared, float rho) {
        double cos2Rho = Math.cos(2 * rho);

        double raw_eta = Math.abs(Math
                .asin(Math.sqrt((2 * normalizedDipoleSquared_XY) / (cos2Rho * (3 * sumNormalizedDipoleSquared - 1)))));

        if (raw_eta < Math.PI / 2) {
            return (float) raw_eta;
        } else {
            return (float) (raw_eta - Math.PI / 2);
        }
    }

    @Override
    public IOrientationVector convert(IPolarizationsIntensity intensity) {
        double pol0Intensity = intensity.getIntensity(Polarization.pol0);
        double pol45Intensity = intensity.getIntensity(Polarization.pol45);
        double pol90Intensity = intensity.getIntensity(Polarization.pol90);
        double pol135Intensity = intensity.getIntensity(Polarization.pol135);
        
        double dipoleSquared_XX = pol0Intensity * _iProp_0_xx + pol45Intensity * _iProp_45_xx +
            pol90Intensity * _iProp_90_xx + pol135Intensity * _iProp_135_xx;

        double dipoleSquared_YY = pol0Intensity * _iProp_0_yy + pol45Intensity * _iProp_45_yy +
            pol90Intensity * _iProp_90_yy + pol135Intensity * _iProp_135_yy;

        double dipoleSquared_ZZ = pol0Intensity * _iProp_0_zz + pol45Intensity * _iProp_45_zz +
        pol90Intensity * _iProp_90_zz + pol135Intensity * _iProp_135_zz;

        double dipoleSquared_XY = pol0Intensity * _iProp_0_xy + pol45Intensity * _iProp_45_xy +
        pol90Intensity * _iProp_90_xy + pol135Intensity * _iProp_135_xy;

        double dipoleSquaredNormalizationFactor = this._getDipoleSquaredNormalizationFactor(dipoleSquared_XX,
                dipoleSquared_YY, dipoleSquared_ZZ);

        double normalizedDipoleSquared_XYdiff = this._normalizedDipoleSquared_XYdiff(dipoleSquared_XX, dipoleSquared_YY,
                dipoleSquaredNormalizationFactor);

        double normalizedDipoleSquared_XY = this._normalizedDipoleSquared_XY(dipoleSquared_XY,
                dipoleSquaredNormalizationFactor);

        double normalizedDipoleSquared_Z = this._normalizedDipoleSquared_Z(dipoleSquared_ZZ,
                dipoleSquaredNormalizationFactor);

        double sumNormalizedDipoleSquared = this._sumNormalizedDipoleSquared(normalizedDipoleSquared_XY,
                normalizedDipoleSquared_XYdiff, normalizedDipoleSquared_Z);

        float rho = this._getRho(normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff);
        float delta = this._getDelta(sumNormalizedDipoleSquared);
        float eta = this._getEta(normalizedDipoleSquared_XY, sumNormalizedDipoleSquared, rho);

        return new OrientationVector(rho, delta, eta);
    }

    
    private double _getDipoleSquaredNormalizationFactor(double dipoleSquared_XX, double dipoleSquared_YY,
            double dipoleSquared_ZZ) {
        return dipoleSquared_XX + dipoleSquared_YY + dipoleSquared_ZZ;
    }

    private double _normalizedDipoleSquared_XYdiff(double dipoleSquared_XX, double dipoleSquared_YY,
            double dipoleSquaredNormalizationFactor) {
        return (dipoleSquared_XX - dipoleSquared_YY) / dipoleSquaredNormalizationFactor;
    }

    private double _normalizedDipoleSquared_XY(double dipoleSquared_XY, double dipoleSquaredNormalizationFactor) {
        return 2 * dipoleSquared_XY / dipoleSquaredNormalizationFactor;
    }

    private double _normalizedDipoleSquared_Z(double dipoleSquared_ZZ, double dipoleSquaredNormalizationFactor) {
        return dipoleSquared_ZZ / dipoleSquaredNormalizationFactor;
    }

    private double _sumNormalizedDipoleSquared(double normalizedDipoleSquared_XY, double normalizedDipoleSquared_XYdiff,
            double normalizedDipoleSquared_Z) {
        return normalizedDipoleSquared_Z
                + Math.sqrt(Math.pow(normalizedDipoleSquared_XYdiff, 2) + Math.pow(normalizedDipoleSquared_XY, 2));
    }
}