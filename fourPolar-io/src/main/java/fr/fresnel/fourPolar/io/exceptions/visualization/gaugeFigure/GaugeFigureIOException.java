package fr.fresnel.fourPolar.io.exceptions.visualization.gaugeFigure;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureLocalization;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;

/**
 * An exception that is thrown in case of gauge figure IO issues.
 */
public class GaugeFigureIOException extends IOException {
    private static final long serialVersionUID = 4961578452220391548L;

    private GaugeFigureLocalization _localization;
    private AngleGaugeType _gaugeType;
    private ICapturedImageFileSet _fileSet;
    private int _channel;
    private String _session;

    public GaugeFigureIOException(String message) {
        super(message);
    }

    /**
     * Sets all parameters of the exception directly from the figure.
     * 
     * @param message     is the exception message.
     * @param gaugeFigure is the gauge figure that has issues.
     * @param sessionName is the visualization session associated with the gauge
     *                    figure.
     */
    public GaugeFigureIOException(String message, IGaugeFigure gaugeFigure, String sessionName) {
        super(message);
        _localization = gaugeFigure.getLocalization();
        _gaugeType = gaugeFigure.getGaugeType();
        _fileSet = gaugeFigure.getFileSet();
        _channel = gaugeFigure.getChannel();
        _session = sessionName;
    }

    public void setLocalization(GaugeFigureLocalization localization) {
        _localization = localization;
    }

    public void setAngleGaugeType(AngleGaugeType gaugeType) {
        _gaugeType = gaugeType;
    }

    public void setFileSet(ICapturedImageFileSet fileSet) {
        _fileSet = fileSet;
    }

    public void setChannel(int channel) {
        _channel = channel;
    }

    public void setVisualizationSession(String session) {
        _session = session;
    }

    public GaugeFigureLocalization localization() {
        return _localization;
    }

    public AngleGaugeType angleGaugeType() {
        return _gaugeType;
    }

    public ICapturedImageFileSet fileSet() {
        return _fileSet;
    }

    public int channel() {
        return _channel;
    }

    public String visualizationSession() {
        return _session;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}