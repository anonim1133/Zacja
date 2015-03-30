package me.anonim1133.zacja.modes.CTF;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import me.anonim1133.zacja.R;
import me.anonim1133.zacja.db.DataBaseHelper;
import me.anonim1133.zacja.utils.Api;

public class SyncCTF {
	Context c;
	private DataBaseHelper db;

	String key;

	public SyncCTF(Context c, String apikey) throws SQLException {
		Log.d("SyncCTF", "Syncing...");

		this.c  =  c;
		key = apikey;
		db = new DataBaseHelper(c);

		//syncWifi();
		syncConquers();

		db.close();
	}

	public void syncWifi(){
		Log.d("SyncCTF", "Wifi...");

		JSONObject json;

		while (db.wifi.getCount() > 0){
			Log.d("SyncCTF", db.wifi.getCount() + " left");
			try {
				json = db.wifi.getLast();
				Api api = new Api( c.getString(R.string.server_ip), c.getString(R.string.server_port) );
				api.addField("key", key);

				api.addField("wifi", json.toString());

				String response = api.post("addWifi");

				Log.d("SyncCTF", "Json: " + json);
				//Log.d("SyncCTF", "Response: "+ response);

				db.wifi.remove(json.getString("id"));
			} catch (Exception e) {
				e.printStackTrace();
				//Toast.makeText(c, c.getString(R.string.err_network), Toast.LENGTH_SHORT).show();
			}
		}

		Log.d("SyncCTF", "ended");
	}

	public void syncConquers(){
		Log.d("SyncCTF", "Conquers");
	}
}
