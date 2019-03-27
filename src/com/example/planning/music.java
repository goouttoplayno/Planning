package com.example.planning;

import java.io.IOException;

import com.example.socialplanning.R;

import android.content.Context;
import android.media.MediaPlayer;

public class music {
	private MediaPlayer playerMusic = null;
	private Context mContext = null;

	public music(Context context) {
		mContext = context;
	}

	public void PlayMusic() {
		playerMusic = MediaPlayer.create(mContext, R.raw.start);
		playerMusic.setLooping(true);
		try {
			playerMusic.prepare();
		} catch (IllegalStateException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		playerMusic.start();
	}

	public void FreeMusic() {
		if (playerMusic != null) {
			playerMusic.stop();
			playerMusic.release();
		}
	}

}
