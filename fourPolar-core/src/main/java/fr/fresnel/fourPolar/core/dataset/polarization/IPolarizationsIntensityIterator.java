package fr.fresnel.fourPolar.core.dataset.polarization;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;

/**
 * An iterator for iterating over the elements of a
 * {@link IPolarizationsIntensitySet}. There's no dimension associated with the set
 */
public interface IPolarizationsIntensityIterator extends Iterator<IPolarizationsIntensity> {
}