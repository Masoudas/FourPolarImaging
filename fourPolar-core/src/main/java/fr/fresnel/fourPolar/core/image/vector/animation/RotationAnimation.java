package fr.fresnel.fourPolar.core.image.vector.animation;

import java.util.Optional;

/**
 * An animation that rotates an element around the center point. This animation
 * is created under the "animateTransform" tag in SVG.
 * 
 * The element format for the rotation animation that we use with this class has
 * the following format:
 * <animateTransform attributeName="transform" type="rotate" values="r1 x1 y1;r2
 * x2 y2;..." begin="" dur="" repeatCount="" end=""/>, where transformation
 * starts from the first pair of values for dur, and then progresses to the next
 * pairs.
 * 
 * It's the responsibility of the caller to set all the necessary parameters of
 * the animation (including center, start and end points, etc ...).
 */
public class RotationAnimation implements Animation {
    /**
     * The attribute name of the animateTransform tag.
     */
    private final static String _ATTRIBUTE_NAME = "transform";

    /**
     * The type attribute of the animateTransform tag.
     */
    private final static String _TYPE = "rotate";

    private AnimationEvents _beginEvent;
    private double _duration;
    private AnimationRepeatCount _repeatCount;
    private AnimationEvents _endEvent;
    private int[] _rotationInDegree;
    private int _x_rotationCenter;
    private int _y_rotationCenter;

    /**
     * Constructs the animation by setting rotation angles and center to 0, begin
     * event to {@link AnimationEvents#MOUSE_HOVER}, duration to zero, and repeat
     * count and end to null.
     */
    public RotationAnimation() {
        _beginEvent = AnimationEvents.MOUSE_HOVER;
        _duration = 0;
        _repeatCount = null;
        _endEvent = null;

        _rotationInDegree = new int[1];
        _x_rotationCenter = 0;
        _y_rotationCenter = 0;
    }

    /**
     * Sets the rotation angles in degree.
     * 
     * @param angles is rotation angles in degree.
     */
    public void setRotationAngles(int[] angles) {
        _rotationInDegree = angles;
    }

    /**
     * Sets the rotation center of the animation. As is evident, all rotation angles
     * would use the same rotation center.
     * 
     * @param x is the x-coordinate of the rotation center.
     * @param y is the y-coordinate of the rotation center.
     */
    public void setRotationCenter(int x, int y) {
        _x_rotationCenter = x;
        _y_rotationCenter = y;
    }

    @Override
    public void setBegin(AnimationEvents event) {
        _beginEvent = event;
    }

    @Override
    public void setDuration(double duration) {
        _duration = duration;
    }

    @Override
    public void setRepeatCount(AnimationRepeatCount count) {
        _repeatCount = count;
    }

    @Override
    public void setEnd(AnimationEvents event) {
        _endEvent = event;
    }

    @Override
    public String begin() {
        return _beginEvent.eventAsString();
    }

    @Override
    public String duration() {
        return String.valueOf(_duration);
    }

    @Override
    public Optional<String> end() {
        return Optional.ofNullable(_endEvent.eventAsString());
    }

    @Override
    public Optional<String> repeatCount() {
        return Optional.ofNullable(_repeatCount.countAsString());
    }

    /**
     * @return the attributeName attribute of the animation element.
     */
    public String attributeName() {
        return _ATTRIBUTE_NAME;
    }

    /**
     * @return the type attribute of the animation element.
     */
    public String type() {
        return _TYPE;
    }

}