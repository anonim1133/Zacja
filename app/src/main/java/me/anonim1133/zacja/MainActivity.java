package me.anonim1133.zacja;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.anonim1133.zacja.auth.SignIn;

public class MainActivity extends Activity {

	Fragment fragment;

	String apikey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			apikey = sharedPref.getString("apikey", "false");

			Log.d("MAIN", apikey);
			if(apikey.matches("false")){
				//signin
				showSignIn();
			}else{
				//check key validity
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	//Fragments

	public void showSignIn(){
		fragment = new SignIn();
		getFragmentManager().beginTransaction()
							.add(R.id.container, fragment)
							.commit();
	}
}
