package fr.fresnel.fourPolar.core.physics.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PolarizationsIntensityTest {
    @Test
    public void getIntensity_NegativeIntensities_ReturnsZero() {
        IntensityVector intensity = new IntensityVector(-1, -1, -1, -1);

        assertTrue(
            intensity.getIntensity(Polarization.pol0) == 0 &&
            intensity.getIntensity(Polarization.pol45) == 0 &&
            intensity.getIntensity(Polarization.pol90) == 0 &&
            intensity.getIntensity(Polarization.pol135) == 0);
        
    }

    @Test
    public void getIntensity_NonNegativeIntensities_ReturnsIntensities() {
        IntensityVector intensity = new IntensityVector(0, 1, 2, 3);

        assertTrue(
            intensity.getIntensity(Polarization.pol0) == 0 &&
            intensity.getIntensity(Polarization.pol45) == 1 &&
            intensity.getIntensity(Polarization.pol90) == 2 &&
            intensity.getIntensity(Polarization.pol135) == 3);
   
    }
    
}