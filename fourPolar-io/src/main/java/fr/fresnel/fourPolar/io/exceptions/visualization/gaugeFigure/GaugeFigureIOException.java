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
    public static final String _WRITE_ERR = "The given gauge figure can't be written.";
    public static final String _READ_ERR = "The given gauge figure can't be read.";

    private static final long serialVersionUID = 4961578452220391548L;

    /**
     * Builds the exception for the failed gauge figure.
     */
    public static class Builder {
        protected GaugeFigureLocalization _localization;
        private AngleGaugeType _gaugeType;
        private ICapturedImageFileSet _fileSet;
        private int _channel;
        private String _session;
        private String _message;

        public Builder(String message) {
            _message = message;
        }

        /**
         * Sets all parameters of the exception directly from the figure.
         * 
         * @param message     is the exception message.
         * @param gaugeFigure is the gauge figure that has issues.
         * @param sessionName is the visualization session associated with the gauge
         *                    figure.
         */
        public Builder(String message, IGaugeFigure gaugeFigure, String sessionName) {
            _localization = gaugeFigure.getLocalization();
            _gaugeType = gaugeFigure.getGaugeType();
            _fileSet = gaugeFigure.getFileSet();
            _channel = gaugeFigure.getChannel();
            _session = sessionName;
        }

        public Builder localization(GaugeFigureLocalization localization) {
            _localization = localization;
            return this;
        }

        public Builder angleGaugeType(AngleGaugeType gaugeType) {
            _gaugeType = gaugeType;
            return this;
        }

        public Builder fileSet(ICapturedImageFileSet fileSet) {
            _fileSet = fileSet;
            return this;
        }

        public Builder channel(int channel) {
            _channel = channel;
            return this;
        }

        public Builder visualizationSession(String session) {
            _session = session;
            return this;
        }

        public GaugeFigureIOException build() {
            return new GaugeFigureIOException(this);
        }

    }

    private GaugeFigureLocalization _localization;
    private AngleGaugeType _gaugeType;
    private ICapturedImageFileSet _fileSet;
    private int _channel;
    private String _session;

    private GaugeFigureIOException(Builder builder) {
        super(builder._message);
        this._localization = builder._localization;
        this._gaugeType = builder._gaugeType;
        this._fileSet = builder._fileSet;
        this._channel = builder._channel;
        this._session = builder._session;
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