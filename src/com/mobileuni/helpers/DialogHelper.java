package com.mobileuni.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.mobileuni.R;
import com.mobileuni.model.Session;

/**
 * @author Joshua Wöhle
 * A simple helper class to serve dialogs from a central place
 */
public class DialogHelper {
	
	/**
	 * @param a - the activity responsible for treating the alertdialog
	 * @return an alertdialog asking the user to install evernote
	 */
	public static AlertDialog evernoteInstallDialog(final Activity a) {
		// If the intent was not found, it means the app was not installed, so just redirect the user to install Evernote
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage(R.string.install_evernote)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.evernote"));
				a.startActivity(browserIntent);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		return builder.create();
	}
	
	/**
	 * @param a - the activity responsible of treating the alertdialog
	 * @return an alertdialog that asks the user to authenticate with evernote
	 */
	public static AlertDialog evernoteAuthenticateDialog(final Activity a) {
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage(R.string.authenticate_evernote)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Session.getEs().authenticate(Session.getContext());
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		return builder.create();
	}
	
	/**
	 * @param a - the activity responsible to treat the alertdialog
	 * @return an alertdialog explaining the user has an Android version which does not support this feature.
	 */
	public static AlertDialog androidSDKTooLow(final Activity a) {
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage(R.string.android_sdk_too_low)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		});

		return builder.create();
	}
}
