package me.anonim1133.zacja.traininglist;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		ImageView icon = (ImageView)view.findViewById(R.id.icon);
		TextView tv_type = (TextView)view.findViewById(R.id.training_type);
		TextView tv_date = (TextView)view.findViewById(R.id.training_date);
		TextView tv_stats = (TextView)view.findViewById(R.id.training_stats);
		TextView tv_points = (TextView)view.findViewById(R.id.training_points);


		//Get corresponding strings
		String txt_type = cursor.getString(cursor.getColumnIndexOrThrow("training_type"));

		String txt_date = "";
		String dtStart = cursor.getString(cursor.getColumnIndexOrThrow("gpx"));
		SimpleDateFormat  format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		try {
			Date date = format.parse(dtStart);

			SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");

			txt_date = fmtOut.format(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		String txt_stats;
		if(txt_type.equals("Squats") || txt_type.equals("Jumping")){
			txt_stats = cursor.getString(cursor.getColumnIndexOrThrow("moves")) + context.getString(R.string.moves)+ cursor.getString(cursor.getColumnIndexOrThrow("distance")) + context.getString(R.string.succesions);
		}else{
			txt_stats = cursor.getString(cursor.getColumnIndexOrThrow("distance")) + context.getString(R.string.km_in);
		}

		//ToDo: Zliczać punkty
		String txt_points = "0 punktów"; //cursor.getString(cursor.getColumnIndexOrThrow("points"));

		// Populate fields with extracted properties
		icon.setImageResource(R.drawable.biking);

		switch (cursor.getString(cursor.getColumnIndexOrThrow("training_type"))){
			case "Biking": icon.setImageResource(R.drawable.biking); break;
			case "Running": icon.setImageResource(R.drawable.running); break;
			case "Squats": icon.setImageResource(R.drawable.squats); break;
			case "Jumping": icon.setImageResource(R.drawable.jumping); break;
		}

		tv_type.setText(txt_type);
		tv_date.setText(txt_date);
		tv_stats.setText(txt_stats);
		tv_points.setText(txt_points);

	}
}