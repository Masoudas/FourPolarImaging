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
     * Sets the beginning of animation as an animation event.
     * 
     * @param event is the event that fires the animation.
     */
    public void setDuration(AnimationEvents event);

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
     * @return the beginning of animation attribute as an string.
     */
    public String begin();

    /**
     * @return the duration of animation attribute as an string.
     */
    public String duration();

    /**
     * @return the end of animation attribute as an string or empty otherwise.
     */
    public Optional<String> end();

    /**
     * @return the repetition counr of animation attribute as an string or empty
     *         otherwise.
     */
    public Optional<String> repeatCount();

}