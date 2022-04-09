package com.example.musicvisualizer;

import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;

public class amplitudeProcessor {
	MediaPlayer mediaPlayer;
//	private Media media;

	public amplitudeProcessor(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		mediaPlayer.setAudioSpectrumInterval(1);
	}

	public void addListener() {
		mediaPlayer.setAudioSpectrumListener(new SpektrumListener());
	}

	public class SpektrumListener implements AudioSpectrumListener {
		@Override
		public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {

			for (int i = 0; i < magnitudes.length; i += 10) {
//				magnitudes[i] = magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold();
//				System.out.println(magnitudes[i]);
				if (magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold() > 0) {
					System.out.println(Math.floor(magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold()));
				}
			}
		}
	}
}
