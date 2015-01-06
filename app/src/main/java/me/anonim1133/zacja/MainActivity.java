package me.anonim1133.zacja;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import me.anonim1133.zacja.auth.ModeChoice;
import me.anonim1133.zacja.auth.SignIn;
import me.anonim1133.zacja.auth.SignUp;
import me.anonim1133.zacja.modes.CTF.CTF;
import me.anonim1133.zacja.modes.Training.ActivityChoice;
import me.anonim1133.zacja.modes.Work.Work;

public class MainActivity extends Activity {

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
		fragment = new ModeChoice();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}

	public void showCTF(){
		fragment = new CTF();
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.addToBackStack("CTF")
				.commit();
	}

	public void showTraining(){
		fragment = new ActivityChoice();
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
}
