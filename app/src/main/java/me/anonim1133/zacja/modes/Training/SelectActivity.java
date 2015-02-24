package me.anonim1133.zacja.modes.Training;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.anonim1133.zacja.R;

public class SelectActivity extends Fragment {

	public SelectActivity() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.training_activity_select, container, false);
		return rootView;
	}
}