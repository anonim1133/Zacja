package me.anonim1133.zacja.modes.Work;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;

public class Work extends Fragment {
	View rootView;

	ProgressBar pb;
	TextView timer;

	int succession = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.work_main, container, false);

		pb = (ProgressBar) rootView.findViewById(R.id.progressBarToday);
		timer = (TextView) rootView.findViewById(R.id.tv_timer);

		Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
		//an.setFillAfter(true);
		pb.startAnimation(an);

		pb.setMax(60);
		pb.setProgress(0);

		Bundle b = getArguments();
		if(b != null)
			succession = b.getInt("succession");

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		CountDownTimer countDownTimer = new CountDownTimer(60 * pb.getMax() * 1000, 500) {
			// 500 means, onTick function will be called at every 500 milliseconds

			@Override
			public void onTick(long leftTimeInMilliseconds) {
				long seconds = leftTimeInMilliseconds / 1000;
				int barVal= pb.getMax() - (int)seconds/60;

				pb.setProgress(barVal);
				timer.setText(String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60));

			}
			@Override
			public void onFinish() {
				MainActivity a = (MainActivity)getActivity();

				a.selectWalking(256, succession);//Starts "walking" and it counts up to 100 steps, then ends.
			}
		}.start();

	}
}