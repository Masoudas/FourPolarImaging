package fr.fresnel.fourPolar.core.image.generic.pixel.types.color;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;

/**
 * Blends colors using soft light method, as described in
 * {@linkplain https://en.wikipedia.org/wiki/Blend_modes}.
 */
public class SoftLightColorBlender implements ColorBlender {

    @Override
    public void blend(ARGB8 base, ARGB8 blendColor) {
        int blendedR = _softLightBlend(base.getR(), blendColor.getR());
        int blendedG = _softLightBlend(base.getG(), blendColor.getG());
        int blendedB = _softLightBlend(base.getB(), blendColor.getB());

        base.set(blendedR, blendedG, blendedB, base.getAlpha());

    }

    private int _softLightBlend(int baseColor, int blendColor) {
        double baseAsDouble = ((double) baseColor) / 255d;
        double blendAsDouble = ((double) blendColor) / 255d;

        double softBlendFactor;
        if (blendAsDouble < 0.5) {
            softBlendFactor = 2 * baseAsDouble * blendAsDouble + baseAsDouble * baseAsDouble * (1 - 2 * blendAsDouble);
        } else {
            softBlendFactor = 2 * baseAsDouble * (1 - blendAsDouble)
                    + Math.sqrt(baseAsDouble) * (2 * blendAsDouble - 1);
        }

        return (int) (softBlendFactor * 255);
    }
}