package fr.fresnel.fourPolar.core.physics.propagation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class OpticalPropagationTest {
    @Test
    public void setMethod_MatrixOfRandomNumbers_ShouldReturnSameMatrix() {
        OpticalPropagation matrix = new OpticalPropagation();

        matrix.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 0);
        matrix.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 7);

        assertTrue(matrix.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0) == 0
            && matrix.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90) == 7);
    }
}