package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.animation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.animation.AnimationEvents;
import fr.fresnel.fourPolar.core.image.vector.animation.AnimationRepeatCount;
import fr.fresnel.fourPolar.core.image.vector.animation.RotationAnimation;

public class RotationAnimationToSVGElementConverterTest {
    private static final String _ANIMATION_TAG = "animateTransform";

    /**
     * List of attributes that are written with this converter
     */
    private static final String _ATTRIBUTE_NAME_ATTR = "attributeName";
    private static final String _TYPE_ATTR = "type";
    private static final String _VALUES_ATTR = "values";
    private static final String _BEGIN_ATTR = "begin";
    private static final String _DUR_ATTR = "dur";
    private static final String _REPEAT_COUNT_ATTR = "repeatCount";
    private static final String _END_ATTR = "end";

    @Test
    public void convert_0to10to0FromX0Y0CenterStartOnClickNoEndNoRepeat_CreatesAnimationWithCorrectAttributes() {
        int[] rot_angles = { 10, 0 };
        int[] center = { 0, 0 };
        double dur = 0.8;
        RotationAnimation rotationAnimation = _createAnimation(AnimationEvents.MOUSE_CLICK, null,
                0.8, rot_angles, center, null);

        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        Element vectorElement = document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");

        RotationAnimationToSVGElementConverter.convert(rotationAnimation, vectorElement, document);

        NodeList nodes = vectorElement.getChildNodes();

        // vectorElement has only one child element.
        assertTrue(nodes.getLength() == 1);
        Element animationElement = (Element) nodes.item(0);

        // check all animation fields are set properly
        assertTrue(animationElement.getTagName().equals(_ANIMATION_TAG));
        assertTrue(animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _ATTRIBUTE_NAME_ATTR)
                .equals("transform"));
        assertTrue(
                animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _TYPE_ATTR).equals("rotate"));
        assertTrue(animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _DUR_ATTR)
                .equals(String.valueOf(dur) + "s"));
        assertTrue(animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _VALUES_ATTR)
                .equals(String.valueOf(rot_angles[0] + " " + center[0] + " " + center[1] + ";" + rot_angles[1] + " "
                        + center[0] + " " + center[1] + ";")));
        assertTrue(
                animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _BEGIN_ATTR).equals("click"));
    }

    @Test
    public void convert_0to10to0FromX0Y0CenterStartOnClickEndOnHoverCountIndefinite_CreatesAnimationWithCorrectAttributes() {
        int[] rot_angles = { 10, 0 };
        int[] center = { 0, 0 };
        double dur = 0.8;
        RotationAnimation rotationAnimation = _createAnimation(AnimationEvents.MOUSE_CLICK, AnimationEvents.MOUSE_HOVER,
                0.8, rot_angles, center, AnimationRepeatCount.INDEFINITE);

        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        Element vectorElement = document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");

        RotationAnimationToSVGElementConverter.convert(rotationAnimation, vectorElement, document);

        NodeList nodes = vectorElement.getChildNodes();

        // vectorElement has only one child element.
        assertTrue(nodes.getLength() == 1);
        Element animationElement = (Element) nodes.item(0);

        // check all animation fields are set properly
        assertTrue(animationElement.getTagName().equals(_ANIMATION_TAG));
        assertTrue(animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _ATTRIBUTE_NAME_ATTR)
                .equals("transform"));
        assertTrue(
                animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _TYPE_ATTR).equals("rotate"));
        assertTrue(animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _DUR_ATTR)
                .equals(String.valueOf(dur) + "s"));
        assertTrue(animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _VALUES_ATTR)
                .equals(String.valueOf(rot_angles[0] + " " + center[0] + " " + center[1] + ";" + rot_angles[1] + " "
                        + center[0] + " " + center[1] + ";")));
        assertTrue(
                animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _BEGIN_ATTR).equals("click"));
        assertTrue(
                animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _END_ATTR).equals("mouseover"));
        assertTrue(animationElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _REPEAT_COUNT_ATTR)
                .equals("indefinite"));

    }
    private RotationAnimation _createAnimation(AnimationEvents begin, AnimationEvents end, double dur, int[] rot_angles,
            int[] center, AnimationRepeatCount count) {
        RotationAnimation animation = new RotationAnimation();

        animation.setBegin(begin);
        animation.setDuration(dur);
        animation.setEnd(end);
        animation.setRotationAngles(rot_angles);
        animation.setRotationCenter(center[0], center[1]);
        animation.setRepeatCount(count);

        return animation;
    }
}