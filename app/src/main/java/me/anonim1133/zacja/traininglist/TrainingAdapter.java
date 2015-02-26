package me.anonim1133.zacja.traininglist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import me.anonim1133.zacja.R;

public class TrainingAdapter extends CursorAdapter {
	public TrainingAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.trainings_list_element, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// Find fields to populate in inflated template
		TextView tv_type = (TextView)view.findViewById(R.id.training_type);
		TextView tv_date = (TextView)view.findViewById(R.id.training_date);
		TextView tv_stats = (TextView)view.findViewById(R.id.training_stats);
		TextView tv_points = (TextView)view.findViewById(R.id.training_points);

		String txt_type = cursor.getString(cursor.getColumnIndexOrThrow("training_type"));
		//ToDo: Wyciągnąć datę
		String txt_date = cursor.getString(cursor.getColumnIndexOrThrow("gpx"));
		String txt_stats = cursor.getString(cursor.getColumnIndexOrThrow("distance"));
		//ToDo: Zliczać punkty
		String txt_points = "0 punktów"; //cursor.getString(cursor.getColumnIndexOrThrow("points"));

		// Populate fields with extracted properties
		tv_type.setText(txt_type);
		tv_date.setText(txt_date);
		tv_stats.setText(txt_stats);
		tv_points.setText(txt_points);
	}
}