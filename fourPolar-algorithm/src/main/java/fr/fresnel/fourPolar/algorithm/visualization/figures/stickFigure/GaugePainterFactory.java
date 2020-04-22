package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;

/**
 * Using this class, we can create a @{link IAngleGaugePainter} to get an
 * {@link IGuageFigure}.
 * 
 */
public class GaugePainterFactory {
    /**
     * Using this method, we generate a {@link IAngleGaugePainter} through which we
     * can draw 2d rho sticks. These sticks have rho as their direction and rho as
     * their color. All sticks will have the same length and thickness with this
     * method.
     * <p>
     * CAUTION: The chosen colormap must not have black or white colors, otherwise,
     * it will be misinterpreted as intensity (because the background is an SoI
     * image).
     * 
     * @param orientationImage is the desired orientation image.
     * @param soiImage         is the stick figure that corresponds to this
     *                         orientation image (created using the
     *                         {@link ISoIImage} that corresponds to this
     *                         orientation image).
     * @param length           is the desired length of the stick in pixels, must be
     *                         greater than equal one.
     * @param thickness        is the desired thickness of sticks, must be greater
     *                         than equal one.
     * @param colorMap         is the color map used for filling the sticks.
     */

    public static IAngleGaugePainter rho2DStick(IOrientationImage orientationImage, ISoIImage soiImage, int len,
            int thickness, ColorMap colorMap) {
        IGaugeFigure gaugeFigure = GaugeFigureFactory.createEmpty(AngleGaugeType.Rho2D, soiImage,
                soiImage.getFileSet());
        return new Angle2DStickPainter(gaugeFigure, orientationImage, soiImage, len, thickness, colorMap);

    }

    /**
     * Using this method, we generate a {@link IAngleGaugePainter} through which we
     * can draw 2d delta sticks. These sticks have rho as their direction and delta
     * as their color. All sticks will have the same length and thickness with this
     * method.
     * <p>
     * CAUTION: The chosen colormap must not have black or white colors, otherwise,
     * it will be misinterpreted as intensity (because the background is an SoI
     * image).
     * 
     * @param orientationImage is the desired orientation image.
     * @param soiImage         is the stick figure that corresponds to this
     *                         orientation image (created using the
     *                         {@link ISoIImage} that corresponds to this
     *                         orientation image).
     * @param length           is the desired length of the stick in pixels, must be
     *                         greater than equal one.
     * @param thickness        is the desired thickness of sticks, must be greater
     *                         than equal one.
     * @param colorMap         is the color map used for filling the sticks.
     */

    public static IAngleGaugePainter delta2DStick(IOrientationImage orientationImage, ISoIImage soiImage, int len,
            int thickness, ColorMap colorMap) {
        IGaugeFigure gaugeFigure = GaugeFigureFactory.createEmpty(AngleGaugeType.Delta2D, soiImage,
                soiImage.getFileSet());
        return new Angle2DStickPainter(gaugeFigure, orientationImage, soiImage, len, thickness, colorMap);

    }

    /**
     * Using this method, we generate a {@link IAngleGaugePainter} through which we
     * can draw 2d eta sticks. These sticks have rho as their direction and eta as
     * their color. All sticks will have the same length and thickness with this
     * method.
     * <p>
     * CAUTION: The chosen colormap must not have black or white colors, otherwise,
     * it will be misinterpreted as intensity (because the background is an SoI
     * image).
     * 
     * @param orientationImage is the desired orientation image.
     * @param soiImage         is the stick figure that corresponds to this
     *                         orientation image (created using the
     *                         {@link ISoIImage} that corresponds to this
     *                         orientation image).
     * @param length           is the desired length of the stick in pixels, must be
     *                         greater than equal one.
     * @param thickness        is the desired thickness of sticks, must be greater
     *                         than equal one.
     * @param colorMap         is the color map used for filling the sticks.
     */

    public static IAngleGaugePainter eta2DStick(IOrientationImage orientationImage, ISoIImage soiImage, int len,
            int thickness, ColorMap colorMap) {
        IGaugeFigure gaugeFigure = GaugeFigureFactory.createEmpty(AngleGaugeType.Eta2D, soiImage,
                soiImage.getFileSet());
        return new Angle2DStickPainter(gaugeFigure, orientationImage, soiImage, len, thickness, colorMap);

    }

}