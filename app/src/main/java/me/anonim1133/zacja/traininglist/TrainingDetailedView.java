package me.anonim1133.zacja.traininglist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;
import me.anonim1133.zacja.db.DataBaseHelper;

public class TrainingDetailedView extends Fragment {
	View rootView;
	DataBaseHelper db;

	int training_id = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.trainings_details, container, false);

		this.training_id = getArguments().getInt("id");

		return rootView;
	}

	@Override
	public void onStart(){
		try {
			db = new DataBaseHelper(getActivity());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			showTraining(training_id);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		setHasOptionsMenu(true);

		super.onStart();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.training_detailed_view, menu);
	}

	public void showTraining(int id) throws ParseException {
		Log.d("Training", "ShowTraining");

		//Get training
		Cursor training = db.training.getByID(id);
		training.moveToFirst();

		//Get TextViews
		ImageView icon = (ImageView)rootView.findViewById(R.id.training_icon);
		TextView tv_score = (TextView)rootView.findViewById(R.id.tv_score);
		TextView tv_time = (TextView)rootView.findViewById(R.id.tv_time);
		TextView tv_data = (TextView)rootView.findViewById(R.id.tv_data);
		TextView tv_time_active = (TextView)rootView.findViewById(R.id.tv_time_active);
		TextView tv_tempo = (TextView)rootView.findViewById(R.id.tv_tempo);
		TextView tv_distance = (TextView)rootView.findViewById(R.id.tv_distance);
		TextView tv_speed = (TextView)rootView.findViewById(R.id.tv_speed);
		TextView tv_upward = (TextView)rootView.findViewById(R.id.tv_upward);
		TextView tv_downward = (TextView)rootView.findViewById(R.id.tv_downward);


		//Populate them with proper content
		switch (training.getString(training.getColumnIndexOrThrow("training_type"))){
			case "Biking": icon.setImageResource(R.drawable.biking); break;
			case "Running": icon.setImageResource(R.drawable.running); break;
			case "Walking": icon.setImageResource(R.drawable.walking); break;
			case "Squats": icon.setImageResource(R.drawable.squats); break;
			case "Jumping": icon.setImageResource(R.drawable.jumping); break;
			default: icon.setImageResource(R.drawable.ic_launcher); break;
		}

		String dateStr = training.getString(training.getColumnIndexOrThrow("gpx")).replace(".gpx", "");

		SimpleDateFormat curFormater = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		Date dateObj = curFormater.parse(dateStr);
		SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yy HH:mm");

		String newDateStr = postFormater.format(dateObj);

		tv_data.setText(newDateStr);

		tv_score.setText(training.getString(training.getColumnIndexOrThrow("score")));

		Integer totalSecs = training.getInt(training.getColumnIndexOrThrow("time"))/1000;
		Integer hours = totalSecs / 3600;
		Integer minutes = (totalSecs % 3600) / 60;
		Integer seconds = totalSecs % 60;

		tv_time.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

		tv_time_active.setText(training.getString(training.getColumnIndexOrThrow("time_active")));
		tv_tempo.setText(training.getString(training.getColumnIndexOrThrow("tempo_avg")));
		tv_distance.setText(training.getString(training.getColumnIndexOrThrow("distance")));
		tv_speed.setText(training.getString(training.getColumnIndexOrThrow("speed_avg")));
		tv_upward.setText(training.getString(training.getColumnIndexOrThrow("altitude_upward")));
		tv_downward.setText(training.getString(training.getColumnIndexOrThrow("altitude_downward")));
	}

	public void deleteActiveTraining(){
		db.training.delete(this.training_id);
		MainActivity a = (MainActivity)getActivity();
		a.showList();
	}
}
