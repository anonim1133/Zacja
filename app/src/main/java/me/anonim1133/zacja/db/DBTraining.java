package me.anonim1133.zacja.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.utils.ScoreCalculator;

public class DBTraining {
	private static String TAG = "ZACJA_DB_TRAIN";
	private static String TABLE_NAME = "training";

	private SQLiteDatabase db;

	Context context;

	public DBTraining(SQLiteDatabase database, Context context){
		this.db = database;
		this.context = context;
	}

	public boolean add(String gpx, String training_type, long time, long time_active, int moves, float speed_max, float speed_avg, float tempo_min, float tempo_avg, float distance, int altitude_min, int altitude_max, int altitude_upward, int altitude_downward){
		ContentValues values = new ContentValues();

		ScoreCalculator scoring = new ScoreCalculator(training_type);
		scoring.setAltitude(altitude_upward);
		scoring.setAverage(Math.round(speed_avg));
		scoring.setDistance(Math.round(distance));
		scoring.setMoves(moves);

		MainActivity a = (MainActivity)context;
		a.showScore(scoring.getScore());

		values.put("gpx", gpx);
		values.put("score", scoring.getScore());
		values.put("training_type", training_type);
		values.put("time", time);
		values.put("time_active", time_active);
		values.put("moves", moves);
		values.put("speed_max", speed_max);
		values.put("speed_avg", speed_avg);
		values.put("tempo_min", tempo_min);
		values.put("tempo_avg", tempo_avg);
		values.put("distance", distance);
		values.put("altitude_min", altitude_min);
		values.put("altitude_max", altitude_max);
		values.put("altitude_upward", altitude_upward);
		values.put("altitude_downward", altitude_downward);

		try{
			long value = db.insertOrThrow(TABLE_NAME, null, values);

			if(value > 0) return true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		return false;
	}

	public boolean workAdd(String gpx, String training_type, long time, long time_active, int moves, float speed_max, float speed_avg, float tempo_min, float tempo_avg, float distance, int altitude_min, int altitude_max, int altitude_upward, int altitude_downward){
		ContentValues values = new ContentValues();
		int score = 0;

		ScoreCalculator scoring = new ScoreCalculator(training_type);
		scoring.setAltitude(altitude_upward);
		scoring.setAverage(Math.round(speed_avg));
		scoring.setDistance(Math.round(distance));
		scoring.setMoves(moves);

		score = scoring.getScore()*8;

		MainActivity a = (MainActivity)context;
		a.showScore(score);

		values.put("gpx", gpx);
		values.put("score", score);
		values.put("training_type", training_type);
		values.put("time", time);
		values.put("time_active", time_active);
		values.put("moves", moves);
		values.put("speed_max", speed_max);
		values.put("speed_avg", speed_avg);
		values.put("tempo_min", tempo_min);
		values.put("tempo_avg", tempo_avg);
		values.put("distance", distance);
		values.put("altitude_min", altitude_min);
		values.put("altitude_max", altitude_max);
		values.put("altitude_upward", altitude_upward);
		values.put("altitude_downward", altitude_downward);

		try{
			long value = db.insertOrThrow(TABLE_NAME, null, values);

			if(value > 0) return true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		return false;
	}

	public boolean add(String gpx, String training_type, long time, long time_active, float speed_max, float speed_avg, float tempo_min, float tempo_avg, float distance, int altitude_min, int altitude_max, int altitude_upward, int altitude_downward){
		ContentValues values = new ContentValues();

		ScoreCalculator scoring = new ScoreCalculator(training_type);
		scoring.setAltitude(altitude_upward);
		scoring.setAverage(Math.round(speed_avg));
		scoring.setDistance(Math.round(distance));

		MainActivity a = (MainActivity)context;
		a.showScore(scoring.getScore());

		values.put("gpx", gpx);
		values.put("score", scoring.getScore());
		values.put("training_type", training_type);
		values.put("time", time);
		values.put("time_active", time_active);
		values.put("speed_max", speed_max);
		values.put("speed_avg", speed_avg);
		values.put("tempo_min", tempo_min);
		values.put("tempo_avg", tempo_avg);
		values.put("distance", distance);
		values.put("altitude_min", altitude_min);
		values.put("altitude_max", altitude_max);
		values.put("altitude_upward", altitude_upward);
		values.put("altitude_downward", altitude_downward);

		try{
			long value = db.insertOrThrow(TABLE_NAME, null, values);

			if(value > 0) return true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		return false;
	}

	public Cursor getLast(int limit) {
		Cursor cursor;

		if(limit != 0)
			cursor = db.query(TABLE_NAME , new String[] {"rowid _id,*"}, null, null, null, null, "id DESC", String.valueOf(limit));
		else
			cursor = db.query(TABLE_NAME , new String[] {"rowid _id,*"}, null, null, null, null, "id DESC", null);

		return cursor;
	}

	public Cursor getByID(int id) {
		Cursor cursor;

		cursor = db.query(TABLE_NAME , new String[] {"rowid _id,*"}, "id=?", new String[]{ String.valueOf(id) }, null, null, null, String.valueOf(1));

		return cursor;
	}

	public boolean delete(int id){
		if(db.delete(TABLE_NAME,"id=?", new String[]{String.valueOf(id)}) == 0)
			return false;
		else
			return true;
	}
}