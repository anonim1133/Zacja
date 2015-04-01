package me.anonim1133.zacja.modes.CTF;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import me.anonim1133.zacja.MainActivity;
import me.anonim1133.zacja.R;
import me.anonim1133.zacja.db.DataBaseHelper;

public class MapsActivity extends Fragment {


	static View rootView;

	private static String TAG = "CTF";

	private WifiManager mainWifi;
	private Location last_location;

    private GoogleMap mMap;
	private DataBaseHelper db;
	private ProgressBar progress;

	//private GpsHelper mGps;

	float distance_between_last_locations = 100f;
	long last_scan_time = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    if( rootView == null )
	        rootView = inflater.inflate(R.layout.ctf_main, container, false);

	    Log.d(MapsActivity.TAG, "onCreate");

	    //mGps = new GpsHelper(this);
	    try {
		    db = new DataBaseHelper(getActivity());
	    } catch (SQLException e) {
		    Log.d(MapsActivity.TAG, "Error while opening db: " + e.toString());
	    }

	    progress = (ProgressBar) rootView.findViewById(R.id.progressBar);

	    //setUpMapIfNeeded();

	    return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		Log.d("CTF", "OnCreateMenu");

		MainActivity a = (MainActivity)getActivity();

		if(a.isSignedIn())
			inflater.inflate(R.menu.ctf_menu, menu);
	}

	private  void setUpWifi(){
		mainWifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

		if(!mainWifi.isWifiEnabled()){
			mainWifi.setWifiEnabled(true);
		}
	}

	private void scanWifi(){
		mainWifi.startScan();

	}

	private void stopScanWifi(){

	}

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) ((FragmentActivity)getActivity())
	            .getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }

	    if (mMap != null) {
		    mMap.setMyLocationEnabled(true);
		    mMap.getUiSettings().setZoomControlsEnabled(false);
		    mMap.getUiSettings().setMyLocationButtonEnabled(true);
	    }

    }

	private void centerMapOnMyLocation() {

		mMap.setMyLocationEnabled(true);

		Location location = mMap.getMyLocation();

		if (location != null) {
			LatLng myLocation = new LatLng(location.getLatitude(),
					location.getLongitude());

			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, mMap.getMaxZoomLevel() - 5));
		}

	}

	public void onScan() {
		Log.d(MapsActivity.TAG, "onScan");
		last_location = mMap.getMyLocation();

		//time of last scan
		long time_difference = System.currentTimeMillis() - last_scan_time;

		//Checking ig it was more than 10s ago
		if(time_difference > 10000) {//10s
			last_scan_time = System.currentTimeMillis();

			setUpWifi();
			scanWifi();

			progress.setVisibility(View.VISIBLE);

			CountDownTimer cdt = new CountDownTimer(10000, 1000) {
				public void onTick(long millisUntilFinished) {
					progress.setProgress((int) ((10000 - millisUntilFinished) / 1000) + 1);
				}

				public void onFinish() {
					progress.setProgress(10);
					progress.setVisibility(View.GONE);
					onScanFinish(mainWifi.getScanResults());
				}
			}.start();

		}else{
			messageBox(getString(R.string.scanning_has_already_started), getString(R.string.one_scan_once_10s));
		}
	}

	public void onScanFinish(List<ScanResult> wifis){
		Location current_location = mMap.getMyLocation();
		Float distance = 0.0f;
		Boolean add = true;

		//Scoring
		int points = 0;
		Iterator<ScanResult> iterator = wifis.iterator();
		while (iterator.hasNext()) {
			ScanResult result = iterator.next();
			int tmp_points = 100; //max for hotspot

			tmp_points -= result.level;
			if(!result.capabilities.matches("\\[WEP\\]|\\[ESS\\]|\\[WPA\\]")){
				//open network
				tmp_points += 10;

				db.addWifi(result.SSID, result.BSSID, result.level, 0, current_location.getLongitude(), current_location.getLatitude());
			}else{
				//secured network
				db.addWifi(result.SSID, result.BSSID, result.level, 1, current_location.getLongitude(), current_location.getLatitude());
			}

			points = tmp_points/10;
		}

		//Check if current conquer doesnt cover with last conquers
		Cursor conquers = db.getLastConquers(5);
		conquers.moveToFirst();
		while (!conquers.isAfterLast()) {
			Location tmp_location = new Location("tmp");
			tmp_location.setLongitude(conquers.getDouble(3));
			tmp_location.setLatitude(conquers.getDouble(4));

			Log.d(TAG, "Distance between current and conquer location: " + tmp_location.distanceTo(current_location));

			if(tmp_location.distanceTo(current_location) < distance_between_last_locations){
				add = false;
				break;
			}

			conquers.moveToNext();
		}


		//Check if user is in the same place that he started scanning
		distance = current_location.distanceTo(last_location);

		Log.d(TAG, "Distance between current and last location: " + distance);
		if(distance > distance_between_last_locations){
			messageBox(getString(R.string.conquer_error), getString(R.string.conquer_too_far_from_start));
		}else if(!add){
			messageBox(getString(R.string.conquer_error), getString(R.string.already_conquered));
		}else{
			long date = System.currentTimeMillis()/1000;
			db.addConquer(points, date, current_location.getLongitude(), current_location.getLatitude());

			drawCircle(new LatLng(current_location.getLatitude(), current_location.getLongitude()));
		}
	}

	public void drawAllCircles(){
		Cursor conquers = db.getLastConquers(5);
		conquers.moveToFirst();
		while (!conquers.isAfterLast()) {
			Location tmp_location = new Location("tmp");
			tmp_location.setLongitude(conquers.getDouble(3));
			tmp_location.setLatitude(conquers.getDouble(4));

			drawCircle(new LatLng(tmp_location.getLatitude(), tmp_location.getLongitude()));

			conquers.moveToNext();
		}
	}

	public void drawCircle(LatLng where){
		mMap.addCircle(new CircleOptions()
				.center(where)
				.radius(distance_between_last_locations)
				.strokeColor(getResources().getColor(R.color.dark))
				.fillColor(getResources().getColor(R.color.light_a)));
	}

	public void messageBox(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(message)
				.setTitle(title);
		//builder.setView(R.layout.dialog_no_fix);

		AlertDialog dialog = builder.create();
		dialog.show();
	}

    @Override
    public void onStart() {
        super.onStart();
	    Log.d(MapsActivity.TAG, "onStart");
	    setHasOptionsMenu(true);
    }

	@Override
	public void onStop() {
		Log.d(MapsActivity.TAG, "onStop");

		super.onStop();
	}

	@Override
	public void onPause() {
		Log.d(MapsActivity.TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(MapsActivity.TAG, "onResume");
		super.onResume();

		setUpMapIfNeeded();
		drawAllCircles();
	}

}
