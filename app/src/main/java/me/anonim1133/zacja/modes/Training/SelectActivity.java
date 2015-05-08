package me.anonim1133.zacja.modes.Training;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;

public class SelectActivity extends Fragment {

	public SelectActivity() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.training_activity_select, container, false);

		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		Log.d("Training", "OnCreateMenu");

		MainActivity a = (MainActivity)getActivity();

		if(a.isSignedIn()) {
			inflater.inflate(R.menu.training_menu, menu);
		}
	}
}