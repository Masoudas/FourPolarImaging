package fr.fresnel.fourPolar.core.physics.polarization;

/**
 * Models the propagated intensity from the sample for all polarizations. Note
 * that if an intensity is negative, it will be set to zero.
 */
public class PolarizationsIntensity implements IPolarizationsIntensity {
    final private double _pol0;
    final private double _pol45;
    final private double _pol90;
    final private double _pol135;

    public PolarizationsIntensity(double pol0, double pol45, double pol90, double pol135) {
        _pol0 = pol0 > 0 ? pol0 : 0;
        _pol45 = pol45 > 0 ? pol45 : 0;
        _pol90 = pol90 > 0 ? pol90 : 0;
        _pol135 = pol135 > 0 ? pol135 : 0;

    }

    @Override
    public double getIntensity(Polarization pol) {
        double intensity = Double.NaN;

        switch (pol) {
            case pol0:
                intensity = _pol0;
                break;

            case pol45:
                intensity = _pol45;
                break;

            case pol90:
                intensity = _pol90;
                break;

            case pol135:
                intensity = _pol135;
                break;

            default:
                break;
        }

        return intensity;
    }
}