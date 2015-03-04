package me.anonim1133.zacja.modes.CTF;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by anonim1133 on 16.11.14.
 */

public class ErrorDialogFragment extends DialogFragment {

	// Global field to contain the error dialog
	private Dialog mDialog;

	/**
	 * Default constructor. Sets the dialog field to null
	 */
	public ErrorDialogFragment() {
		super();
		mDialog = null;
	}

	/**
	 * Set the dialog to display
	 *
	 * @param dialog An error dialog
	 */
	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}

	/*
	 * This method must return a Dialog to the DialogFragment.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}

}
