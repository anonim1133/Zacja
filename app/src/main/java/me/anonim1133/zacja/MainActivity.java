package me.anonim1133.zacja;


import android.content.Context;
import android.content.SharedPreferences;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class MainActivity extends ActionBarActivity {

	Fragment fragment;

	String apikey;

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
		if(apikey.matches("false")){
			//signin
			showSignIn();
		}else{
			//check key validity
			Log.d("Main", "checking key validity");

			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						Api api = new Api( getString(R.string.server_ip), getString(R.string.server_port) );
						api.addField("key", apikey);

						String valid = api.get("LoginCheck");
						if( valid == "false" ) showSignIn();
						else showMainScreen();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), getString(R.string.err_network), Toast.LENGTH_SHORT).show();
					}
				}
			});

			thread.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
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
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()	{
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	private void onHomePressed(){

	}

	private void onListPressed(){
		setTitle("Lista");

		showList();
	}

	private void onSyncPressed(){

	}

	private void onSettingsPressed(){

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
							.commit();
	}

	public void showSignUp(){
		fragment = new SignUp();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
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
		fragment = new Walking(); // Little cheat, becouse it counts steps, so its adequate for walking and running.
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
		TrainingDetailedView trainingDetailedView = new TrainingDetailedView();

		Bundle bundle = new Bundle();
		bundle.putInt("id", id);

		trainingDetailedView.setArguments(bundle);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, trainingDetailedView)
				.addToBackStack("List")
				.commit();
	}
}
