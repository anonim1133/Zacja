package me.anonim1133.zacja.auth;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import me.anonim1133.zacja.Api;
import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;

public class SignIn extends Fragment {

	View rootView;
	Api api;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.signin, container, false);

		api = new Api( getString(R.string.server_ip), getString(R.string.server_port) );

		rootView.findViewById(R.id.btn_in_signin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onSignIn();
			}
		});
		rootView.findViewById(R.id.btn_in_signup).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onSignUp();
			}
		});

		return rootView;
	}

	private void onSignIn(){
		final Context context = getActivity();

		CharSequence text = getString(R.string.err_signin);
		int duration = Toast.LENGTH_SHORT;

		final Toast toast = Toast.makeText(context, text, duration);

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					EditText txt_login = (EditText)rootView.findViewById(R.id.txt_login);
					EditText txt_pass = (EditText)rootView.findViewById(R.id.txt_password);

					api.addField("login", txt_login.getText().toString());
					api.addField("password", txt_pass.getText().toString());

					String api_key = api.get("SignIn");

					Log.d("SignIn", api_key);

					if(!api_key.matches("false")) {
						SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putString("apikey", api_key);
						editor.commit();
					}else{
						toast.setText(getString(R.string.err_signin));
						toast.show();
					}
				} catch (Exception e) {
					e.printStackTrace();

					toast.setText(getString(R.string.err_network));
					toast.show();
				}
			}
		});

		thread.start();

	}

	void onSignUp(){
		MainActivity a = (MainActivity)getActivity();
		a.showSignUp();
	}
}
