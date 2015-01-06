package me.anonim1133.zacja.auth;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;

public class ModeChoice extends Fragment{
	View rootView;

	MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.mode_choice, container, false);

		activity = (MainActivity)getActivity();

		rootView.findViewById(R.id.btn_ctf).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.showCTF();
			}
		});

		rootView.findViewById(R.id.btn_training).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.showTraining();
			}
		});

		rootView.findViewById(R.id.btn_work).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.showWork();
			}
		});

		return rootView;
	}
}
