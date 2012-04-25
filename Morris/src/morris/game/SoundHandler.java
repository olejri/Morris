package morris.game;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;



public class SoundHandler {
	
	private static SoundPool mSoundPool; 
	private  HashMap<Integer, Integer> mSoundPoolMap; 
	private  AudioManager  mAudioManager;
	private  Context mContext;
	private Handler handler;
	
	
	
	public SoundHandler(){
		handler = new Handler();
	}
		
	public void initSounds(Context theContext) { 
		 mContext = theContext;
	     mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0); 
	     mSoundPoolMap = new HashMap<Integer, Integer>(); 
	     mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 
	     loadSounds();
	} 
	
	private void loadSounds(){
		mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.wii_menu_background_song, 1));
	}
	
	public void playSound(final int index) { 
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
			     mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f); 
			}
		}, 200);
	     
	}
	
	public void playLoopedSound(int index) { 
	     int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
	     mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f); 
	}

}