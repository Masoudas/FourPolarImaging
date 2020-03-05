package fr.fresnel.fourPolar.core.fourPolar;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;

/**
 * An iterator for iterating over a set of {@link IPolarizationsIntensity}
 * vectors.
 * 
 * We opted not to put a setter method for this iterator for the following
 * reasons:
 * <ol>
 * <li>Overriding the intensity will result in erasure of
 * original unbiased values.</li>
 * <li>In this code, we define polarization intensity as a double, but formally
 * when captured as an image, they are quantized. Hence a direct subtitution
 * of a double for an integer causes loss of information.</li>
 * <li>Third and most importantly, if we were to extract intensity through
 * inverse map of orientation to intensity, those values don't necessarily
 * have the same scale as the captured intensity. Hence, overriding the original
 * intensity would be incorrect.</li>
 * </ol>
 */
public interface IPolarizationsIntensityIterator extends Iterator<IPolarizationsIntensity> {
}