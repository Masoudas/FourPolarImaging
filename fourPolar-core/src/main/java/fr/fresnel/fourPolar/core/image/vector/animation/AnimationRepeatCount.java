package fr.fresnel.fourPolar.core.image.vector.animation;

/**
 * An enumeration for indicating how (many times) an animation should be
 * repeated (including indefinite number of times).
 */
public enum AnimationRepeatCount {
    TEN(String.valueOf(10)), TWENTY(String.valueOf(20)), THIRTY(String.valueOf(30)), INDEFINITE("indefinite");

    private String _countAsString;

    AnimationRepeatCount(String countAsString) {
        _countAsString = countAsString;
    }

    /**
     * @return the count number that can be used as an svg attribute.
     */
    public String countAsString() {
        return _countAsString;
    };

}