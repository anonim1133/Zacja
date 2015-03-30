package me.anonim1133.zacja.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DBWifi {
	private static String TAG = "ZACJA_DB_WIFI";
	private static String TABLE_NAME = "wifi";

	private SQLiteDatabase db;

	public DBWifi(SQLiteDatabase database) {
		this.db = database;
	}

	public boolean add(String ssid, String bssid, int signal, int security, double longitude, double latitude) {
		ContentValues values = new ContentValues();
		values.put("ssid", ssid);
		values.put("bssid", bssid);
		values.put("signal", signal);
		values.put("security", security);
		values.put("longitude", longitude);
		values.put("latitude", latitude);

		try {
			long value = db.insertOrThrow(TABLE_NAME, null, values);
			Log.i(TAG, "Dodajno wifi z id: " + value);
			if (value > 0) return true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

		return false;
	}

	public JSONObject getLast() throws JSONException {
		Cursor cursor = db.query(TABLE_NAME, new String[]{"id", "ssid", "bssid", "signal", "security", "longitude", "latitude"}, null, null, null, null, "id DESC", "1");

		JSONObject json = new JSONObject();

		if(cursor.getCount() == 0){
			json.put("error", "null");
		}


		cursor.moveToFirst();

		int id = cursor.getInt(0);
		String ssid = cursor.getString(1);
		String bssid = cursor.getString(2);
		int signal = cursor.getInt(3);
		int security = cursor.getInt(4);
		double longitude = cursor.getDouble(5);
		double latitude = cursor.getDouble(6);


		json.put("id", id);
		json.put("ssid", ssid);
		json.put("bssid", bssid);
		json.put("signal", signal);
		json.put("security", security);
		json.put("lon", longitude);
		json.put("lat", latitude);


		cursor.close();

		return json;
	}

	public int getCount(){
		Cursor cursor = db.query(TABLE_NAME, new String[]{"id", "ssid", "bssid", "signal", "security", "longitude", "latitude"}, null, null, null, null, "id DESC");

		return cursor.getCount();
	}


	public void remove(String id){
		db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
	}
}