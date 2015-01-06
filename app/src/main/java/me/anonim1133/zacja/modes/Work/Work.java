package me.anonim1133.zacja.modes.Work;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.anonim1133.zacja.R;

public class Work extends Fragment {
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.work_main, container, false);
/*
		rootView.findViewById(R.id.btn_ctf).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
*/

		return rootView;
	}
}