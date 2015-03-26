
import java.util.ArrayList;

/**
 * A CD. A CD must always have a name and an album artist.
 * @author Mikko Piuhola
 */
public class CD extends Item implements Holdable,Installable {

	private static final long serialVersionUID = -830803010158008662L;
	
	private String albumName;
	private String artistName;
	private ArrayList<Track> trackList;
	private boolean isInstalled;
	
	/**
	 * Constructor. Create a new CD.
	 * @param albumName CD's name
	 * @param artistName CD artist's name
	 */
	public CD(String albumName, String artistName) {
		super("CD", "a music album, seems to be " + albumName + " by " + artistName);
		this.albumName = albumName;
		this.artistName = artistName;
		this.trackList = new ArrayList<Track>();
	}
	
	/**
	 * Add track to CD
	 * @param track Track object to insert
	 */
	public void addTrack(Track track) {
		this.trackList.add(track);
	}
	
	/**
	 * Remove track from CD
	 * @param trackNumber 1-based index of track on CD
	 */
	public void removeTrack(int trackNumber) {
		this.trackList.remove(trackNumber - 1);
	}
	
	/**
	 * Get album name
	 * @return Album's name
	 */
	public String getAlbumName() {
		return this.albumName;
	}
	
	/**
	 * Get name of the album's artist
	 * @return Name of the CD's artist
	 */
	public String getAlbumArtistName() {
		return this.artistName;
	}
	
	/**
	 * Get amount of tracks on CD
	 * @return Amount of tracks
	 */
	public int getTrackAmount() {
		return this.trackList.size();
	}
	
	/**
	 * Get full title of track
	 * @param trackNumber Track number of the wanted track
	 * @return Full title of track, e.g. 10 Metallica - St. Anger [120s]
	 */
	public String getFullTrackTitle(int trackNumber) {
		Track chosenTrack = this.trackList.get(trackNumber - 1); // Track number is 1-based, index 0-based
		String fullTitle = trackNumber + " "
				+ chosenTrack.getArtist() + " - "
				+ chosenTrack.getTrackName() + " ["
				+ chosenTrack.getDurationString() + "]";
		return fullTitle;
	}
	
	// INSPECTABLE
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inspect() {
		System.out.println("That must be one of Peter's CDs. " +
				this.getAlbumName() + " by " + this.getAlbumArtistName() +
				(this.isInstalled() ? "\nIt's currently inside the CD-Player" : "")
		);
	}
	
	// INSTALLABLE

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInstalled() {
		return this.isInstalled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIsInstalled(boolean installed) {
		this.isInstalled = installed;
	}
}
