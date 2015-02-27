package me.anonim1133.zacja.utils;

import android.util.Log;

public class ScoreCalculator {
	private String type;

	private int score = 0;

	private int distance = 1;
	private int average = 1;
	private int altitude = 1;
	private int moves = 1;

	public ScoreCalculator(String t){
		this.type = t;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public void setMoves(int steps) {
		this.moves = steps;
		Log.d("STEPS", String.valueOf(steps));
	}


	public int getScore() {
		switch (type){
			case "Biking":
				score = distance+(altitude*average);
			break;
			case "Running":
				score = (distance*10)+(altitude*average)+moves;
			break;
			case "Walking":
				score = (distance*10)+(altitude*average)+moves;
				break;
			case "Squats":
				score = 128+(distance*moves);
			break;
			case "Jumping":
				score = 128+(distance*moves);
			break;
		}

		return this.score;
	}

}
