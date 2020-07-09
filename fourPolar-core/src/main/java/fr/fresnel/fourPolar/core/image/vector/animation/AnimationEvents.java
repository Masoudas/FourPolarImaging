package fr.fresnel.fourPolar.core.image.vector.animation;

/**
 * List of events for firing or ending an {@link Animation}.
 */
public enum AnimationEvents {
    MOUSE_CLICK {
        public String eventAsString() {
            return "click";
        }
    },
    MOUSE_HOVER {
        public String eventAsString() {
            return "mouseover";
        }
    };

    /**
     * @return event as an string that can be used as an svg attribute.
     */
    public abstract String eventAsString();
}