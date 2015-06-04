package me.anonim1133.zacja.modes.Training;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import me.anonim1133.zacja.R;
import me.anonim1133.zacja.db.DataBaseHelper;
import me.anonim1133.zacja.utils.Api;


public class SyncTraining {
	Context c;
	private DataBaseHelper db;

	String key;

	public SyncTraining(Context c, String apikey){
		Log.d("SyncTraining", "Syncing...");

		this.c = c;
		key = apikey;
	}

	public int go(int last_id) throws SQLException, JSONException{
		db = new DataBaseHelper(c);

		if(db.training.getCount() > 0){

			Cursor training = db.training.getLast(1);
			training.moveToFirst();
			int last_training_id = training.getInt(0);

			FileInputStream gpx_file;

			while(last_training_id >= last_id){

				training = db.training.getByID(last_id);
				if(training.getCount() > 0){//Check if training with this id exists
					training.moveToFirst();

					Log.d("SyncTraining", "Syncing training id: " + training.getString(0));

					JSONObject json = new JSONObject();
					for(int i = 0; i<training.getColumnCount(); i++){
						json.put(training.getColumnName(i), training.getString(i));
					}

					StringBuffer gpx = new StringBuffer("");
					try {
						gpx_file = c.openFileInput(training.getString(training.getColumnIndex("gpx")));
						if(gpx_file.available() > 0) {
							byte[] buffer = new byte[1024];

							int n;
							while ((n = gpx_file.read(buffer)) != -1) {
								gpx.append(new String(buffer, 0, n));
							}
						}
					}catch (IOException e){
						Log.d("SyncTraining", "No gpx file for training");
						//e.printStackTrace();
					}

					json.put("gpx_file", gpx);


					Api api = new Api( c.getString(R.string.server_ip), c.getString(R.string.server_port) );
					api.addField("key", key);

					api.addField("training", json.toString());

					String response = api.post("addTraining");
					Log.d("SyncTraining", "Response: " + response);
					//if(response.equals("success"))
					//	c.deleteFile(training.getString(training.getColumnIndex("gpx")));

				}

				last_id++;
			}

		}

		db.close();
		Log.d("SyncTraining", "finished");

		return last_id;
	}
}
