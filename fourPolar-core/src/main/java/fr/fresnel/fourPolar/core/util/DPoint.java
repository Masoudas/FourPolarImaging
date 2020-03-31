package fr.fresnel.fourPolar.core.util;

/**
 * A discrete point.
 */
public class DPoint {
    public int x;
    public int y;

    public DPoint(int x, int y){
        this.x = x;
        this.y = y;

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DPoint)){
            return false;
        }

        DPoint point = (DPoint)obj;
        return point.x == this.x && point.y == this.y;
    }
}