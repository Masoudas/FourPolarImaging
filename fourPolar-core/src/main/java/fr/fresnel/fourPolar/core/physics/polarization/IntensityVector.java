package fr.fresnel.fourPolar.core.physics.polarization;

/**
 * Models the propagated intensity from the sample for all polarizations. Note
 * that if an intensity is negative, it will be set to zero.
 */
public class IntensityVector {
    private double _pol0;
    private double _pol45;
    private double _pol90;
    private double _pol135;

    public IntensityVector(double pol0, double pol45, double pol90, double pol135) {
        this.setIntensity(pol0, pol45, pol90, pol135);

    }

    public void setIntensity(double pol0, double pol45, double pol90, double pol135) {
        _pol0 = pol0 > 0 ? pol0 : 0;
        _pol45 = pol45 > 0 ? pol45 : 0;
        _pol90 = pol90 > 0 ? pol90 : 0;
        _pol135 = pol135 > 0 ? pol135 : 0;
        
    }

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

    /**
     * @return the sum of intensity.
     */
    public double getSumOfIntensity() {
        return _pol0 + _pol45 + _pol90 + _pol135;
    }
}