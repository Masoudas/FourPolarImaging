package fr.fresnel.fourPolar.core.physics.propagation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class OpticalPropagationTest {
    @Test
    public void setMethod_MatrixOfRandomNumbers_ShouldReturnSameMatrix() {
        Channel channel = new Channel(1, 2, 3, 4, 5);
        NumericalAperture na = new NumericalAperture(1, 2, 3, 4);
        OpticalPropagation matrix = new OpticalPropagation(channel, na);

        matrix.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 0);
        matrix.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 7);

        assertTrue(matrix.getPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0) == 0
            && matrix.getPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90) == 7);
    }
}