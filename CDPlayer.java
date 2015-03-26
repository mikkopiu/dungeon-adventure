
/**
 * A CD Player item. Takes a CD to play and can be powered ON/OFF.
 * @author Mikko Piuhola
 */
public class CDPlayer extends Item implements Powerable,Openable,Hostable,Holdable,Playable {

	private static final long serialVersionUID = 1973062648826512380L;
	
	private boolean powerOn;
	private boolean trayOpen;
	private boolean playing;

	private final int MAX_VOLUME = 10;
	private int currentVolume;
	private int currentTrackNumber;
	
	private CD currentCD;
	
	/**
	 * Constructor. Create a new standard CD Player
	 */
	public CDPlayer() {
		super("CD-Player", "An early Discman from the '80s. That guy Peter was always listening to thrash metal on this thing.", new String[]{"cd-player","cdplayer","player","discman"});
		this.trayOpen = false;
		this.playing = false;
		this.powerOn = false;
		this.currentVolume = 3;
		this.currentCD = null; // No CD inserted
		this.currentTrackNumber = 1;
	}
	
	// POWERABLE
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean powerOn() {
		if (!this.trayOpen) {
			if (!this.isPoweredOn()) {
				this.powerOn = true;
				this.playing = false;
				System.out.println(this.getName() + " is now ON");
			} else {
				System.out.println(this.getName() + " is already ON!");
			}
		} else {
			this.playing = false;
			this.powerOn = false;
			System.out.println("Can't power on when player's tray is open!");
		}
		
		return this.isPoweredOn();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean powerOff() {
		if (!this.trayOpen) {
			if (this.isPoweredOn()) {
				this.playing = false;
				this.powerOn = false;
				System.out.println(this.getName() + " is now OFF");
			} else {
				this.playing = false;
				System.out.println(this.getName() + " is already OFF!");
			}
		} else {
			this.playing = false;
			this.powerOn = false;
			System.out.println("Can't power off when player's tray is open!");
		}
		
		return !this.isPoweredOn();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPoweredOn() {
		return this.powerOn;
	}
	
	// OPENABLE
	
	/**
	 * Close player's tray
	 * @return Is tray closed
	 */
	public boolean close() {		
		if (this.trayOpen) {
			this.powerOn = false;
			this.playing = false;
			this.trayOpen = false;
			this.currentTrackNumber = 1;
			System.out.println("Closed CD-Player's tray");
		} else {
			System.out.println("Tray is already closed!");
		}
		
		return !this.trayOpen;
	}
	
	/**
	 * Open player's tray
	 * @return Is tray open
	 */
	public boolean open() {		
		if (!this.trayOpen) {
			if (!this.playing) {
				this.playing = false;
				this.powerOn = false;
				this.trayOpen = true;
				this.currentTrackNumber = 1;
				System.out.println("Opened CD-Player tray. You can now remove or insert a new cd.");
			} else {
				System.out.println("Can't open tray when CD is playing");
			}
		} else {
			this.playing = false;
			this.powerOn = false;
			System.out.println("Tray already open!");
		}
		
		return this.trayOpen;
	}
	
	// HOSTABLE
	
	/**
	 * Get current CD
	 * @return Current CD or null
	 */
	@Override
	public Item installedItem() {
		return this.currentCD;
	}

	/**
	 * Insert new CD into player
	 * @param item CD to insert
	 * @return Is a CD inserted
	 */
	@Override
	public boolean install(Installable item) {
		if (item instanceof CD) {			
			if (this.trayOpen) {
				this.playing = false;
				this.powerOn = false;
				if (this.currentCD == null) {
					this.currentCD = (CD)item;
					this.currentTrackNumber = 1;
					return true;
				} else {
					System.out.println("A CD is already inserted!");
					return false;
				}
			} else {
				System.out.println("Can't insert CD when tray is closed!");
				this.currentCD = null;
				return false;
			}
		} else {
			System.out.println("That's not a CD, cannot insert " + ((CD)item).getName() + " into " + this.getName());
			return false;
		}
	}

	/**
	 * Remove the current CD from player
	 * @param item CD to remove
	 * @return Is there no CD in player
	 */
	public boolean uninstall(Installable item) {
		
		// Must be a CD, nothing else can be removed from CD Player
		if (!(item instanceof CD)) {
			System.out.println("Cannot remove that from " + this.getName());
			return false;
		}
		
		if (this.trayOpen) {
			this.playing = false;
			this.powerOn = false;
			if (this.currentCD != null) {
				this.currentCD = null;
				this.currentTrackNumber = 1;
				return true;
			} else {
				System.out.println("No CD to remove!");
				return false;
			}
		} else {
			System.out.println("Can't remove CD when is closed!");
			return false;
		}
	}
	
	// PLAYBACK
	
	/**
	 * Start playback of CD
	 * @return Is a CD playing
	 */
	public boolean play() {
		if (!this.trayOpen) {
			if (this.isPoweredOn()) {
				if (this.installedItem() != null && (this.installedItem() instanceof CD)) {
					if (!this.playing) {
						this.playing = true;
						System.out.println("Started playback, the display now reads: " + this.getCurrentDisplayText());
					} else {
						System.out.println("A CD is already playing!");
					}
				} else {
					this.playing = false;
					System.out.println("There is no CD in the " + this.getName());
				}
			} else {
				this.playing = false;
				System.out.println("Can't play CD with player powered off!");
			}
		} else {
			this.playing = false;
			this.powerOn = false;
			System.out.println("Close CD Player's tray before trying to play!");
		}
		
		return this.playing;
	}

	/**
	 * Stop playback of CD
	 * @return Is playback stopped
	 */
	public boolean stop() {		
		if (!this.trayOpen) {
			if (this.isPoweredOn()) {
				if (this.installedItem() != null && (this.installedItem() instanceof CD)) {
					this.playing = false;
					System.out.println("Stopped playback");
				} else {
					this.playing = false;
					System.out.println("There's no CD in the " + this.getName() + ". Stopping playback made no difference.");
				}
			} else {
				this.playing = false;
				System.out.println("Player is OFF, playback already stopped!");
			}
		} else {
			this.playing = false;
			this.powerOn = false;
			System.out.println("Player's tray is open, playback already stopped!");
		}
		
		return !this.playing;
	}
	
	/**
	 * Increase volume by 1
	 * @return Current volume, 0 for mute/off
	 */
	public int incVolume() {
		
		System.out.println("Increasing volume");
		
		if (!this.trayOpen) {
			if (this.isPoweredOn()) {
				if (this.currentVolume < this.MAX_VOLUME) {
					this.currentVolume++;
				} else {
					System.out.println("CD Player is already at max volume!");
				}
			} else {
				System.out.println("CD Player must be powered on to increase volume!");
				this.currentVolume = 0;
			}
		} else {
			System.out.println("Tray must be closed to increase volume!");
			this.currentVolume = 0;
		}
		
		return this.currentVolume;
	}
	
	/**
	 * Decrease volume by 1
	 * @return Current volume, 0 for mute/off
	 */
	public int decVolume() {
		
		System.out.println("Decreasing volume");
		
		if (!this.trayOpen) {
			if (this.isPoweredOn()) {
				if (this.currentVolume > 0) {
					this.currentVolume--;
					
					if (this.currentVolume == 0) {
						System.out.println("Player was muted");
					}
				} else {
					System.out.println("Player is already muted!");
				}
			} else {
				System.out.println("CD Player must be powered on to decrease volume!");
				this.currentVolume = 0;
			}
		} else {
			System.out.println("Tray must be closed to decrease volume!");
			this.currentVolume = 0;
		}
		
		return this.currentVolume;
	}
	
	/**
	 * Skip to the next track
	 * @return Current track number, 0 for off/no CD
	 */
	public int next() {		
		if (!this.trayOpen) {
			if (this.isPoweredOn()) {
				if (this.installedItem() != null && (this.installedItem() instanceof CD)) {
					if (this.currentTrackNumber < this.currentCD.getTrackAmount()) {
						this.currentTrackNumber++;
					} else {
						this.currentTrackNumber = 1;
					}
					System.out.println("Skipped tracks, the display now reads: " + this.getCurrentDisplayText());
				} else {
					System.out.println("Can't skip tracks without a CD inserted!");
					this.currentTrackNumber = 0;
				}
			} else {
				System.out.println("CD Player must be powered on to skip tracks!");
				this.currentTrackNumber = 0;
			}
		} else {
			System.out.println("Can't skip tracks with the tray open!");
			this.currentTrackNumber = 0;
		}
		
		return this.currentTrackNumber;
	}
	
	/**
	 * Skip to the next track
	 * @return Current track number, 0 for off/no cd
	 */
	public int prev() {		
		if (!this.trayOpen) {
			if (this.isPoweredOn()) {
				if (this.installedItem() != null && (this.installedItem() instanceof CD)) {
					if (this.currentTrackNumber > 1) {
						this.currentTrackNumber--;
					} else {
						this.currentTrackNumber = this.currentCD.getTrackAmount();
					}
					System.out.println((this.playing ? "Skipped tracks, t" : "T") + "he display now reads: " + this.getCurrentDisplayText());
				} else {
					System.out.println("Can't skip tracks without a CD inserted!");
					this.currentTrackNumber = 0;
				}
			} else {
				System.out.println("CD Player must be powered on to skip tracks!");
				this.currentTrackNumber = 0;
			}
		} else {
			System.out.println("Can't skip tracks with the tray open!");
			this.currentTrackNumber = 0;
		}
		
		return this.currentTrackNumber;
	}
	
	/**
	 * Get currently displayed text
	 * @return Display text
	 */
	public String getCurrentDisplayText() {
		
		String displayText = "";
		
		if (this.isPoweredOn()) {
			if (this.trayOpen) {
				displayText = "Tray open";
			} else if (this.currentCD == null) {
				displayText = "No CD";
			} else if (!this.playing) {
				displayText = "Stopped";
			} else {
				displayText = "Currently playing: "
						+ this.currentCD.getFullTrackTitle(this.currentTrackNumber);
			}
		}
		
		return displayText;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean winOnOpen() {
		return false; // Doesn't not matter here
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inspect() {
		System.out.println(this.toString());
		System.out.println("You look at the display: " + (this.isPoweredOn() ? "it reads: " + this.getCurrentDisplayText() : "it's empty, the CD-player is probably OFF."));
	}
}
