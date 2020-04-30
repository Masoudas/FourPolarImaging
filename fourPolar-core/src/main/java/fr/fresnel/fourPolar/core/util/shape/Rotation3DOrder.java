package fr.fresnel.fourPolar.core.util.shape;

public enum Rotation3DOrder {
    XZY, ZXY, ZYX;

    /**
     * Returns the order of rotation as a vector.
     */
    public static int[] getAxisOrder(Rotation3DOrder rotation3dOrder) {
        int[] rotationAxis = null;
        switch (rotation3dOrder) {
            case XZY:
                rotationAxis = new int[] { 0, 2, 1 };
                break;

            case ZXY:
                rotationAxis = new int[] { 2, 0, 1 };
                break;

            case ZYX:
                rotationAxis = new int[] { 2, 1, 0 };
                break;

            default:
                break;
        }
        return rotationAxis;
    }
}