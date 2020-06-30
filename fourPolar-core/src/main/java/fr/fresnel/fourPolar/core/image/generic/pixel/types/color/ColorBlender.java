package fr.fresnel.fourPolar.core.image.generic.pixel.types.color;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;

/**
 * An interface for blending colors. Note that blending (as opposed to simple
 * addition of colors) allow colors to be mixed in a meaningful way, that result
 * in acceptable subjective results (whereas a simple addition of colors as done
 * by {@link ARGB8#add()}) may result in color saturation.
 */
public interface ColorBlender {
    /**
     * Blend base and blendColor together, and put the resulting color in base.
     * 
     * @param base is the base color
     * @param blendColor
     */
    public void blend(ARGB8 base, ARGB8 blendColor);
}