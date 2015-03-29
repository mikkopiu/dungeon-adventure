package dungeonadventure.items.resources;
import java.io.Serializable;


/**
 * A music track. A track must have a title, an artist and a duration.
 * @author Mikko Piuhola
 */
public class Track implements Serializable {

	private static final long serialVersionUID = -7431359062370141988L;
	
	private String artist;
	private String title;
	private int durationSecs;
	
	/**
	 * Constructor
	 * @param artist Artist's name
	 * @param title Title of the track
	 * @param mins Duration's full minutes
	 * @param secs Remaining seconds of the duration
	 */
	public Track(String artist, String title, int mins, int secs) {
		this.artist = artist;
		this.title = title;
		this.durationSecs = (mins * 60) + secs;
	}
	
	/**
	 * Get the artist's name
	 * @return Artist's name as String
	 */
	public String getArtist() {
		return this.artist;
	}
	
	/**
	 * Get the name of the track
	 * @return Track title
	 */
	public String getTrackName() {
		return this.title;
	}
	
	/**
	 * Get Track duration in display-format, e.g. 5:23
	 * @return Track duration in min:sec format
	 */
	public String getDurationString() {
		int sec = this.getDurationInSecs() % 60;
		int min = (this.getDurationInSecs() - sec) / 60;
		return min + ":" + sec;
	}
	
	/**
	 * Get duration in seconds
	 * @return Duration in seconds
	 */
	private int getDurationInSecs() {
		return this.durationSecs;
	}
}
