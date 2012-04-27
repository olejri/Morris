package morris.game;

import java.util.HashMap;

import morris.help.Constant;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Static soundmanager class
 * 
 */
public class SoundManager {
	private static SoundManager soundManager = null;

	private static MediaPlayer backgSong;
	private static SoundPool mSoundPool;
	private static HashMap<Integer, Integer> mSoundPoolMap;
	private static AudioManager mAudioManager;
	private static boolean sound = false;

	/**
	 * Initiate the MediaPlayer object and sets looping true
	 * 
	 * @param c
	 */

	private SoundManager() {
	}

	public static void setSound(boolean value) {
		SoundManager.getInstance().sound = value;
	}

	public static boolean getSound() {
		return sound;
	}

	public static SoundManager getInstance() {
		if (soundManager == null) {
			soundManager = new SoundManager();
			return soundManager;
		} else {
			return soundManager;
		}
	}

	/**
	 * Init sound pool and load sounds to the pool SoundPool is used to play
	 * several sounds at same time and use less memory than mediaplayer. But is
	 * not a god alternativ for longer songs.
	 * 
	 * @param mContext
	 */
	public static void initSound(Context mContext) {
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

		loadSoundsEffects(mContext);

		backgSong = MediaPlayer.create(mContext, R.raw.wii_menu_background_song);
		backgSong.setLooping(true);
	}

	public static void loadSoundsEffects(Context mContext) {
		// Add drop sound to SoundPool
		mSoundPoolMap.put(Constant.SOUND_DROP,mSoundPool.load(mContext, R.raw.drop, 1));
		mSoundPoolMap.put(Constant.SOUND_MOVE,mSoundPool.load(mContext, R.raw.move, 1));
		mSoundPoolMap.put(Constant.SOUND_REMOVE,mSoundPool.load(mContext, R.raw.remove, 1));
	}

	/**
	 * Start Background Song
	 */
	public static void startBackgroundSound() {
		if (backgSong != null && !backgSong.isPlaying()) {
			backgSong.start();
			sound = true;
		}
	}

	public static void playSoundEffect(int soundID) {
		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(soundID), streamVolume, streamVolume,1, 0, 1f);
	}

	/**
	 * Stop Background song
	 */
	public static void stopBackgroundSound() {
		if (backgSong != null && backgSong.isPlaying()) {
			backgSong.pause();
			sound = false;
		}
	}

	/**
	 * Release Mediaplayer resource
	 */
	public static void release() {
		if (backgSong != null && sound) {
			backgSong.stop();
			backgSong.release();
		}
	}

}
