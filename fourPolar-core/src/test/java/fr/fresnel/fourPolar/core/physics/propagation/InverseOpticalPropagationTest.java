package fr.fresnel.fourPolar.core.physics.propagation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class InverseOpticalPropagationTest {
    @Test
    public void setMethod_MatrixOfRandomNumbers_ShouldReturnSameMatrix() throws PropagationFactorNotFound {
        InverseOpticalPropagation inverseProp = new InverseOpticalPropagation(null);

        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX, 1);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY, 2);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ, 3);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY, 4);

        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX, 5);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY, 6);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ, 7);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY, 8);

        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX, 9);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY, 10);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ, 11);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY, 12);

        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX, 13);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY, 14);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ, 15);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY, 16);

        assertTrue(
            inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX) == 1 &&
            inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY) == 2 &&
            inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ) == 3 &&
            inverseProp.getInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY) == 4 &&

            inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX) == 5 &&
            inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY) == 6 &&
            inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ) == 7 &&
            inverseProp.getInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY) == 8 &&

            inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX) == 9 &&
            inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY) == 10 &&
            inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ) == 11 &&
            inverseProp.getInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY) == 12 &&

            inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX) == 13 &&
            inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY) == 14 &&
            inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ) == 15 &&
            inverseProp.getInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY) == 16 
        );
    }
}