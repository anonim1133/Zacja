package me.anonim1133.zacja.auth;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import me.anonim1133.zacja.Api;
import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;

public class SignUp extends Fragment {
	View rootView;
	Api api;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.signup, container, false);

		api = new Api( getString(R.string.server_ip), getString(R.string.server_port) );

		rootView.findViewById(R.id.btn_up_signup).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onSignUp();
			}
		});
		rootView.findViewById(R.id.btn_up_signin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onSignIn();
			}
		});

		return rootView;
	}

	void onSignUp(){
		EditText txt_login = (EditText)rootView.findViewById(R.id.txt_r_login);
		EditText txt_email = (EditText)rootView.findViewById(R.id.txt_r_email);
		EditText txt_pass1 = (EditText)rootView.findViewById(R.id.txt_r_pass1);
		EditText txt_pass2 = (EditText)rootView.findViewById(R.id.txt_r_pass2);

		final String login = txt_login.getText().toString();
		final String email = txt_email.getText().toString();
		final String pass1 = txt_pass1.getText().toString();
		String pass2 = txt_pass2.getText().toString();
		//check if all fields are filled
		if(login.length() > 0 && email.length() > 0 && pass1.length() > 0 && pass2.length() > 0){
			//check if password matches each other
			if(!pass1.matches(pass2)){
				Toast.makeText(getActivity(), getString(R.string.err_passwords_not_matches), Toast.LENGTH_SHORT).show();
			}else{
				//send data
				final Toast toast = Toast.makeText(getActivity(), getString(R.string.err_network), Toast.LENGTH_SHORT);
				final Thread thread_up = new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							api.addField("username", login);
							api.addField("password", pass1);
							api.addField("email", email);

							String response = api.post("SignUp");

							//check response
							if(response.matches("true")) {//if true sign in
								Log.d("UP", "Rejestracja: true");
								Thread thread_in = new Thread(new Runnable(){
									@Override
									public void run() {
										try {
											api.addField("login", login);
											api.addField("password", pass1);

											String api_key = api.get("SignIn");

											Log.d("SignIn", api_key);

											if(!api_key.matches("false")) {//logged in
												Log.d("UP", "Zalogowano");
												SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
												SharedPreferences.Editor editor = sharedPref.edit();
												editor.putString("apikey", api_key);
												editor.commit();

												onSignIn();
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

								thread_in.start();
							}else{ //else toast with error
								toast.setText(getString(R.string.err_up));
							}
						} catch (Exception e) {
							e.printStackTrace();

							toast.setText(getString(R.string.err_network));
							toast.show();
						}
					}
				});

				thread_up.start();



			}
		}else{
			Toast.makeText(getActivity(), getString(R.string.err_empty_fields), Toast.LENGTH_SHORT).show();
		}
	}

	void onSignIn(){
		MainActivity a = (MainActivity)getActivity();
		a.main();
	}
}
