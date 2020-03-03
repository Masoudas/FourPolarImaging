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
    final private IInverseOpticalPropagation _inverseProp;

    /**
     * This implementation uses an instance of the
     * {@link IInverseOpticalPropagation} to convert an polarization intensity to a
     * intensity of individual polarizations.
     * 
     * @param inverseProp contains the inverse optical propagation factors.
     */
    public IntensityToOrientationConverter(IInverseOpticalPropagation inverseOpticalProp) {
        this._inverseProp = inverseOpticalProp;
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

        double raw_eta = Math.abs(Math.asin(
            Math.sqrt((2 * normalizedDipoleSquared_XY) / (cos2Rho * (3 * sumNormalizedDipoleSquared - 1)))));

        if (raw_eta < Math.PI / 2) {
            return (float) raw_eta;
        } else {
            return (float) (raw_eta- Math.PI / 2);
        }
    }

    @Override
    public IOrientationVector convert(IPolarizationsIntensity intensity) {
        double dipoleSquared_XX = this._computeDipoleSquared_XX(intensity);
        double dipoleSquared_YY = this._computeDipoleSquared_YY(intensity);
        double dipoleSquared_ZZ = this._computeDipoleSquared_ZZ(intensity);
        double dipoleSquared_XY = this._computeDipoleSquared_XY(intensity);

        double dipoleSquaredNormalizationFactor = this._getDipoleSquaredNormalizationFactor(
            dipoleSquared_XX, dipoleSquared_YY, dipoleSquared_ZZ);
        
        double normalizedDipoleSquared_XYdiff = this._normalizedDipoleSquared_XYdiff(
            dipoleSquared_XX, dipoleSquared_YY, dipoleSquaredNormalizationFactor);            
        
        double normalizedDipoleSquared_XY = this._normalizedDipoleSquared_XY(
            dipoleSquared_XY, dipoleSquaredNormalizationFactor);
        
        double normalizedDipoleSquared_Z = this._normalizedDipoleSquared_Z(
            dipoleSquared_ZZ, dipoleSquaredNormalizationFactor);

        double sumNormalizedDipoleSquared = this._sumNormalizedDipoleSquared(
            normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff,
            normalizedDipoleSquared_Z);
        
        float rho = this._getRho(normalizedDipoleSquared_XY, normalizedDipoleSquared_XYdiff);
        float delta = this._getDelta(sumNormalizedDipoleSquared);
        float eta = this._getEta(normalizedDipoleSquared_XY, sumNormalizedDipoleSquared, rho);

        IOrientationVector orientationVector = new OrientationVector(
            rho, delta, eta);

        return orientationVector;
    }

    private double _computeDipoleSquared_XX(IPolarizationsIntensity intensity) {
        double xx_0 = intensity.getIntensity(Polarization.pol0)
            * _inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX);

        double xx_45 = intensity.getIntensity(Polarization.pol45)
            * _inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX);

        double xx_90 = intensity.getIntensity(Polarization.pol90)
            * _inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX);

        double xx_135 = intensity.getIntensity(Polarization.pol135)
            * _inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX);

        return xx_0 + xx_45 + xx_90 + xx_135;
    }

    private double _computeDipoleSquared_YY(IPolarizationsIntensity intensity) {
        double yy_0 = intensity.getIntensity(Polarization.pol0)
            * _inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY);

        double yy_45 = intensity.getIntensity(Polarization.pol45)
            * _inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY);

        double yy_90 = intensity.getIntensity(Polarization.pol90)
            * _inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY);

        double yy_135 = intensity.getIntensity(Polarization.pol135)
            * _inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY);

        return yy_0 + yy_45 + yy_90 + yy_135;
    }

    private double _computeDipoleSquared_ZZ(IPolarizationsIntensity intensity) {
        double zz_0 = intensity.getIntensity(Polarization.pol0)
            * _inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ);

        double zz_45 = intensity.getIntensity(Polarization.pol45)
            * _inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ);

        double zz_90 = intensity.getIntensity(Polarization.pol90)
            * _inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ);

        double zz_135 = intensity.getIntensity(Polarization.pol135)
            * _inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ);

        return zz_0 + zz_45 + zz_90 + zz_135;
    }

    private double _computeDipoleSquared_XY(IPolarizationsIntensity intensity) {
        double xy_0 = intensity.getIntensity(Polarization.pol0)
            * _inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY);

        double xy_45 = intensity.getIntensity(Polarization.pol45)
            * _inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY);

        double xy_90 = intensity.getIntensity(Polarization.pol90)
            * _inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY);

        double xy_135 = intensity.getIntensity(Polarization.pol135)
            * _inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY);

        return xy_0 + xy_45 + xy_90 + xy_135;
    }

    private double _getDipoleSquaredNormalizationFactor(
        double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquared_ZZ) {
        return dipoleSquared_XX + dipoleSquared_YY + dipoleSquared_ZZ;
    }

    private double _normalizedDipoleSquared_XYdiff(
        double dipoleSquared_XX, double dipoleSquared_YY, double dipoleSquaredNormalizationFactor) {
        return (dipoleSquared_XX - dipoleSquared_YY) / dipoleSquaredNormalizationFactor;
    }

    private double _normalizedDipoleSquared_XY(
        double dipoleSquared_XY, double dipoleSquaredNormalizationFactor) {
        return 2 * dipoleSquared_XY / dipoleSquaredNormalizationFactor;
    }

    private double _normalizedDipoleSquared_Z(
        double dipoleSquared_ZZ, double dipoleSquaredNormalizationFactor) {
        return dipoleSquared_ZZ / dipoleSquaredNormalizationFactor;
    }

    private double _sumNormalizedDipoleSquared(
        double normalizedDipoleSquared_XY, double normalizedDipoleSquared_XYdiff,
        double normalizedDipoleSquared_Z) {
        return normalizedDipoleSquared_Z
                + Math.sqrt(Math.pow(normalizedDipoleSquared_XYdiff, 2) + Math.pow(normalizedDipoleSquared_XY, 2));
    }
}