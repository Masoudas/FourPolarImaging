package fr.fresnel.fourPolar.core.image.vector.animation;

import java.util.Optional;

/**
 * An interface for defining an animation for an element.
 */
public interface Animation {
    /**
     * Sets the beginning of animation as an animation event.
     * 
     * @param event is the event that fires the animation start.
     */
    public void setBegin(AnimationEvents event);

    /**
     * Sets the duration of animation in seconds.
     * 
     * @param duration is the animation duration in seconds.
     */
    public void setDuration(double duration);

    /**
     * Sets the number of times the animation should be repeated.
     * 
     * @param count is the count number of animation.
     */
    public void setRepeatCount(AnimationRepeatCount count);

    /**
     * Set the end of animation an an animation event.
     * 
     * @param event is the event that fires the animation end.
     */
    public void setEnd(AnimationEvents event);

    /**
     * @return the "begin" attribute of animation as an string.
     */
    public String begin();

    /**
     * @return the "dur" attribure of animation element.
     */
    public String dur();

    /**
     * @return the "end" attribute of animation as an string or empty otherwise.
     */
    public Optional<String> end();

    /**
     * @return the "repeatCount" attribute of animation as an string or empty
     *         otherwise.
     */
    public Optional<String> repeatCount();

}