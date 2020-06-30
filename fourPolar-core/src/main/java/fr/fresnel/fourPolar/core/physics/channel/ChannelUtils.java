package fr.fresnel.fourPolar.core.physics.channel;

/** Utility methods for channel */
public class ChannelUtils {
    /**
     * Checks the number of channels is greater than zero, and throw exception if not.
     */
    public static void checkNumChannelsNonZero(int numChannels) {
        if (numChannels <= 0) {
            throw new IllegalArgumentException("Number of channels must be greater than zero.");
        }

    }

	/**
     * Checks channel number is zero, and throw exception if not.
     */
    public static void checkChannelNumberIsNonZero(int channelNo) {
        if (channelNo <= 0) {
            throw new IllegalArgumentException("Channel number must be greater than zero.");
        }

	}
	
	/**
	 * Check channel > 0 is in the range of numChannels and throws exception otherwise.
	 * @param channel is the channel number.
	 * @param numChannels is the total number of channels.
	 * 
	 * @throws IllegalArgumentException in case channel is not positive or greater than numChannels.
	 */
	public static void checkChannelExists(int channel, int numChannels) {
	    if (channel <= 0 || channel > numChannels){
	        throw new IllegalArgumentException("Channel does not exist.");
	    }
	    
    }
    
    /**
     * Returns "Channel " + channel.
     */
    public static String channelAsString(int channel) {
        ChannelUtils.checkChannelNumberIsNonZero(channel);
        return "Channel " + channel;
        
    }

}