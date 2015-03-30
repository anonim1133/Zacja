package me.anonim1133.zacja;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Fragment;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import me.anonim1133.zacja.auth.ModeChoice;
import me.anonim1133.zacja.auth.SignIn;
import me.anonim1133.zacja.auth.SignUp;
import me.anonim1133.zacja.modes.CTF.MapsActivity;
import me.anonim1133.zacja.modes.Training.Biking;
import me.anonim1133.zacja.modes.Training.Jumping;
import me.anonim1133.zacja.modes.Training.Running;
import me.anonim1133.zacja.modes.Training.SelectActivity;
import me.anonim1133.zacja.modes.Training.Squats;
import me.anonim1133.zacja.modes.Training.Walking;
import me.anonim1133.zacja.modes.Work.Work;
import me.anonim1133.zacja.traininglist.TrainingDetailedView;
import me.anonim1133.zacja.traininglist.TrainingList;
import me.anonim1133.zacja.utils.Api;
import me.anonim1133.zacja.utils.Score;

public class MainActivity extends ActionBarActivity {

	Fragment fragment;
	Fragment score_fragment;

	String apikey;

	boolean signed_in = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			main();
		}
	}

	public void main(){
		// Let's start with hiding keyboard
		hideKeyboard();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		apikey = sharedPref.getString("apikey", "false");

		Log.d("MAIN", apikey);
		if(!apikey.matches("false")){
			//check key validity
			Log.d("Main", "checking key validity");

			//try to login
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						Api api = new Api( getString(R.string.server_ip), getString(R.string.server_port) );
						api.addField("key", apikey);

						String valid = api.get("LoginCheck");
						if( !valid.equals("false") ){
							showMainScreen();
							signed_in = true;
							Log.d("Main", "Signed in");
						}else{
							Log.d("Main", "Valid: '" + valid + "'");
							showMainScreen();
						}
					} catch (Exception e) {
						showMainScreen();
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), getString(R.string.err_network), Toast.LENGTH_SHORT).show();
					}
				}
			});

			thread.start();
		}else{
			showMainScreen();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);

		if(!isSignedIn())
			getMenuInflater().inflate(R.menu.unsigned, menu);

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
			case android.R.id.home:
				onHomePressed();
				return true;
			case R.id.action_list:
					onListPressed();
				return true;
			case R.id.action_sync:
					onSyncPressed();
				return true;
			case R.id.action_settings:
				onSettingsPressed();
				return true;
			case R.id.action_signin:
				showSignIn();
				return true;
			case R.id.action_signup:
				showSignUp();
				return true;
			case R.id.action_delete:
				onDeletePressed();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()	{
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			finish();
			System.exit(0);
			//super.onBackPressed();
		}
	}

	public boolean isSignedIn(){
		return signed_in;
	}

	private void showUnsignedMenu(Menu m){
		Log.d("Main", "unsigned menu");
		getMenuInflater().inflate(R.menu.unsigned, m);
	}

	private void onHomePressed(){

	}

	private void onListPressed(){
		showList();
	}

	private void onSyncPressed(){

	}

	private void onSettingsPressed(){

	}

	private void onDeletePressed(){
		TrainingDetailedView training = (TrainingDetailedView)fragment;
		training.deleteActiveTraining();
	}

	private void hideKeyboard() {
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	//Fragments

	public void showSignIn(){
		fragment = new SignIn();
		getFragmentManager().beginTransaction()
							.replace(R.id.container, fragment)
							.addToBackStack("SignIn")
							.commit();
	}

	public void showSignUp(){
		fragment = new SignUp();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("SignUp")
				.commit();
	}

	public void showMainScreen(){
		hideKeyboard();
		fragment = new ModeChoice();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}

	public void showCTF(){
		fragment = new MapsActivity();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("CTF")
				.commit();
	}

	public void showTraining(){
		fragment = new SelectActivity();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Training")
				.commit();
	}

	public void showWork(){
		fragment = new Work();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Work")
				.commit();
	}

	public void showWork(int succession){
		fragment = new Work();

		Bundle bundle = new Bundle();
		bundle.putInt("succession", succession);

		fragment.setArguments(bundle);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Work")
				.commit();
	}


	/* Selecting proper fragments for activities */

	public void selectActivity(View view){
		fragment = new SelectActivity();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}

	public void selectBiking(View view){
		fragment = new Biking();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Biking")
				.commit();
	}

	public void selectRunning(View view){
		fragment = new Running();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Running")
				.commit();
	}

	public void selectWalking(View view){
		fragment = new Walking();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Walking")
				.commit();
	}

	public void selectWalking(int goal, int succession){
		fragment = new Walking();

		Bundle bundle = new Bundle();
		bundle.putInt("goal", goal);
		bundle.putInt("succession", succession);

		fragment.setArguments(bundle);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Walking")
				.commitAllowingStateLoss();
	}

	public void selectJumping(View view){
		fragment =  new Jumping();
		getFragmentManager().beginTransaction()
				.replace(R.id.container,fragment)
				.addToBackStack("Jumping")
				.commit();
	}

	public void selectSquats(View view){
		fragment = new Squats();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Squats")
				.commit();
	}

	public void showList(){
		fragment = new TrainingList();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("List")
				.commit();
	}

	public void showTrainingDetails(int id){
		fragment = new TrainingDetailedView();

		Bundle bundle = new Bundle();
		bundle.putInt("id", id);

		fragment.setArguments(bundle);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("Score")
				.commit();
	}

	public void showScore(int score){
		score_fragment = new Score();

		Bundle bundle = new Bundle();
		bundle.putInt("score", score);

		score_fragment.setArguments(bundle);

		getFragmentManager().beginTransaction()
				.add(R.id.container, score_fragment)
				.commit();
	}

	public void removeScore(){
		getFragmentManager().beginTransaction().remove(score_fragment).commit();
		Log.d("Score", "Animation remove");
	}

	public void onScan(View view) {
		MapsActivity a = (MapsActivity)fragment;
		a.onScan();
	}

	public void showNotification(String notification_title, String notification_text){
		Log.d("Notification", "start");

		try {
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		    r.play();
		} catch (Exception e) {
		    e.printStackTrace();
		}

		Intent intent = new Intent(this, MainActivity.class);
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		Notification n  = new Notification.Builder(this)
				.setContentTitle(notification_title)
				.setContentText(notification_text)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent)
				.setAutoCancel(true).build();


		NotificationManager notificationManager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, n);

		Log.d("notification", "end");
	}
}
