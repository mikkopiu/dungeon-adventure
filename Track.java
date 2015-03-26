import java.io.Serializable;


/**
 * A music track. A track must have a title, an artist and a duration.
 * @author Mikko Piuhola
 */
public class Track implements Serializable {

	private static final long serialVersionUID = -7431359062370141988L;
	
	private String artistName;
	private String trackName;
	private int durationSecs;
	
	/**
	 * Constructor
	 * @param artist Artist's name
	 * @param title Title of the track
	 * @param durationMin Duration's full minutes
	 * @param durationSec Remaining seconds of the duration
	 */
	public Track(String artist, String title, int durationMin, int durationSec) {
		this.artistName = artist;
		this.trackName = title;
		this.durationSecs = (durationMin * 60) + durationSec;
	}
	
	/**
	 * Get the artist's name
	 * @return Name of the artist for this Track
	 */
	public String getArtist() {
		return this.artistName;
	}
	
	/**
	 * Get name of track
	 * @return Name of track
	 */
	public String getTrackName() {
		return this.trackName;
	}
	
	/**
	 * Get Track duration in seconds
	 * @return Duration in seconds
	 */
	public int getDurationInSecs() {
		return this.durationSecs;
	}
	
	/**
	 * Get Track duration in display-format, e.g. 5:23
	 * @return Track duration in min:sec format
	 */
	public String getDurationString() {
		int sec = this.durationSecs % 60;
		int min = (this.durationSecs - sec) / 60;
		return min + ":" + sec;
	}
}
