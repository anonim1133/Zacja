package me.anonim1133.zacja.traininglist;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;
import me.anonim1133.zacja.db.DataBaseHelper;

public class TrainingList extends Fragment {
	View rootView;

	DataBaseHelper db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.trainings_list, container, false);

		ListView listView = (ListView)rootView.findViewById(R.id.trainings_list);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterViewCompat, View view, int i, long l) {
				//Toast.makeText(getActivity(), "Click ListItem Number " + String.valueOf(l), Toast.LENGTH_LONG).show();
				MainActivity a = (MainActivity)getActivity();
				a.showTrainingDetails((int)l);

			}
		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity){
		try {
			db = new DataBaseHelper(activity);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		super.onAttach(activity);
	}

	@Override
	public void onStart(){
		// Find ListView to populate
		ListView lvItems = (ListView) rootView.findViewById(R.id.trainings_list);
		// Setup cursor adapter using cursor from last step
		TrainingAdapter todoAdapter = new TrainingAdapter(getActivity(), db.training.getLast(32));
		// Attach cursor adapter to the ListView
		lvItems.setAdapter(todoAdapter);

		super.onStart();
	}
}
