package fr.fresnel.fourPolar.io.util.transform;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.util.transform.Affine2D;

/**
 * An adaptor of {@link Affine2D} to JSON. Writes the matrix as a string of form
 * [[a00 a01 a02], [a10 a11 a12]] to disk.
 */
public class Affine2DToJSON {
    @JsonProperty("Affine 2D")
    private final String _matrixAsString;

    public Affine2DToJSON(Affine2D affine2d) {
        this._matrixAsString = this._convertMatrixToString(affine2d);
    }

    private String _convertMatrixToString(Affine2D affine2d) {
        return "[[" + affine2d.get(0, 0) + ", " + affine2d.get(0, 1) + ", " + affine2d.get(0, 2) + "], ["
                + affine2d.get(1, 0) + ", " + affine2d.get(1, 1) + ", " + affine2d.get(1, 2) + "]] ";

    }
}