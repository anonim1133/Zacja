package me.anonim1133.zacja.utils;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;
import me.anonim1133.zacja.db.DataBaseHelper;

public class Score extends Fragment {

	View rootView;

	private int score;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.show_score, container, false);

		this.score= getArguments().getInt("score");

		return rootView;
	}

	@Override
	public void onStart(){
		showScore();

		super.onStart();
	}

	public void showScore(){
		final LinearLayout score_layout = (LinearLayout) rootView.findViewById(R.id.score_layout);

		TextView tv_score = (TextView) rootView.findViewById(R.id.tv_score);
		tv_score.setText(String.valueOf(score));

		final Animation out = new AlphaAnimation(1.0f, 0.0f);
		out.setDuration(3000);
		out.setAnimationListener(new Animation.AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.d("Score", "Animation ends");
				MainActivity a = (MainActivity)getActivity();
				a.removeScore();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

		Animation in = new AlphaAnimation(0.0f, 1.0f);
		in.setDuration(3000);
		in.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				Log.d("Score", "Animation starts");
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				score_layout.startAnimation(out);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

		score_layout.startAnimation(in);
	}
}
