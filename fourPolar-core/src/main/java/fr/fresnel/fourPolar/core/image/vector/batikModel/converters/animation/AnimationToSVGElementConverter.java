package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.animation;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.animation.Animation;
import fr.fresnel.fourPolar.core.image.vector.animation.RotationAnimation;

/**
 * A class for converting an {@link Animation} to an SVG element. As we're
 * aware, an animation should be added as a child element to an already existing
 * element. The following class implements such a scheme by first creating the
 * animation element as an element of svg document, and then adding it to the
 * base element to which we want to add the animation.
 */
public class AnimationToSVGElementConverter {
    private AnimationToSVGElementConverter() {
        throw new AssertionError();
    }

    /**
     * Converts the given animation to a proper svg element using the document, and
     * writes it as a child element of the given vector element.
     * 
     * @param animation     is the animation to be added.
     * @param vectorElement is the vector element to which we want to add this
     *                      animation.
     * @param svgDocument   is the document.
     * 
     * @throws IllegalArgumentException if no converter is found for the given
     *                                  animation
     */
    public static void addToVectorElement(Animation animation, Element vectorElement, SVGDocument svgDocument) {

        if (animation instanceof RotationAnimation) {
            RotationAnimationToSVGElementConverter.convert((RotationAnimation) animation, vectorElement, svgDocument);
        } else {
            throw new IllegalArgumentException(
                    "Can't convert the given animation type because no converters are found");
        }

    }
}