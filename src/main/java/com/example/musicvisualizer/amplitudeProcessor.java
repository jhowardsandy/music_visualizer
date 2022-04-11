package com.example.musicvisualizer;

import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.fxml.FXML;
import javafx.scene.media.AudioSpectrumListener;

public class amplitudeProcessor {
	MediaPlayer mediaPlayer;
//
//	@FXML
//	private Rectangle rec;

//	public amplitudeProcessor(MediaPlayer mediaPlayer) {
//		this.mediaPlayer = mediaPlayer;
//		mediaPlayer.setAudioSpectrumInterval(1);
//	}

//	public void addListener() {
//		mediaPlayer.setAudioSpectrumListener(new SpektrumListener());
//	}

//	public class SpektrumListener implements AudioSpectrumListener {
//		@Override
//		public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
//			correctedMag = new int[magnitudes.length];
//			for (int i = 0; i < magnitudes.length; i += 10) {
////				magnitudes[i] = magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold();
////				System.out.println(magnitudes[i]);
//				if (magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold() > 0) {
////					System.out.println(Math.floor(magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold()));
//					correctedMag[i] = (int) Math.floor(magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold());
//
//				}
////				rec.setHeight(Math.floor(magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold()));
////				System.out.println("will this print also?");
////				System.out.println(mediaPlayer.getAudioSpectrumThreshold());
////				rec.setHeight(correctedMag[0]);
//			}
////			System.out.println(correctedMag[0]);
//		}
//	}
}
